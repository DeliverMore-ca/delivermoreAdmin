package ca.admin.delivermore.views.drivers;

import ca.admin.delivermore.collector.data.tookan.Driver;
import ca.admin.delivermore.data.report.DriverPayoutWeek;
import ca.admin.delivermore.security.AuthenticatedUser;
import ca.admin.delivermore.views.MainLayout;
import com.vaadin.componentfactory.DateRange;
import com.vaadin.componentfactory.EnhancedDateRangePicker;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.RolesAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Optional;

@PageTitle("My Pay")
@Route(value = "mypay", layout = MainLayout.class)
@RolesAllowed("USER")
public class MyPayView extends Main {

    private Logger log = LoggerFactory.getLogger(MyPayView.class);
    LocalDate startDate;
    LocalDate endDate;

    Optional<Driver> signedInDriver;
    private EnhancedDateRangePicker rangeDatePicker = new EnhancedDateRangePicker("Select period:");
    private VerticalLayout detailsLayout = new VerticalLayout();

    private AuthenticatedUser authenticatedUser;

    public MyPayView(@Autowired AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;

        signedInDriver = getSignedInDriver();
        log.info("signedInDriver:" + signedInDriver);

        configureDatePicker();
        startDate = rangeDatePicker.getValue().getStartDate();
        endDate = rangeDatePicker.getValue().getEndDate();

        buildDriverPayoutDetails();
        add(getToolbar(), getContent());
    }

    private Optional<Driver> getSignedInDriver() {
        return authenticatedUser.get();
    }

    private HorizontalLayout getToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout(rangeDatePicker);
        toolbar.setPadding(true);
        toolbar.setAlignItems(FlexComponent.Alignment.BASELINE);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void configureDatePicker() {
        LocalDate defaultDate = LocalDate.parse("2022-08-14");

        //get lastWeek as the default for the range picker
        LocalDate nowDate = LocalDate.now();
        LocalDate prevSun = nowDate.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
        prevSun = nowDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate startOfLastWeek = prevSun.minusWeeks(1);
        LocalDate endOfLastWeek = startOfLastWeek.plusDays(6);
        LocalDate startOfThisWeek = prevSun;
        LocalDate endOfThisWeek = nowDate;

        rangeDatePicker.setMin(defaultDate);
        rangeDatePicker.setValue(new DateRange(startOfThisWeek,endOfThisWeek));
        rangeDatePicker.addValueChangeListener(e -> {
            startDate = rangeDatePicker.getValue().getStartDate();
            endDate = rangeDatePicker.getValue().getEndDate();
            buildDriverPayoutDetails();
        });
    }

    private Component getContent() {
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.add(detailsLayout);
        detailsLayout.setSizeFull();
        return mainLayout;
    }

    private void buildDriverPayoutDetails() {
        detailsLayout.removeAll();
        if(signedInDriver.isEmpty()){
            log.info("buildDriverPayoutDetails: No user signed in. Cannot continue");
            detailsLayout.add(new Html("<p>No user signed in.  Cannot continue.</p>"));
            return;
        }
        if(signedInDriver.get().getIsActive()==0L){ //driver not ctive for payout info
            log.info("buildDriverPayoutDetails: signed in user:" + signedInDriver.get().getName() + " is not active for payout.");
            detailsLayout.add(new Html("<p>No payout information available for " + signedInDriver.get().getName() + "</p>"));
            return;
        }
        //display payout info
        DriverPayoutWeek driverPayoutWeek = new DriverPayoutWeek(signedInDriver.get().getFleetId(), startDate, endDate);
        detailsLayout.add(driverPayoutWeek.getDetails(true));
        String htmlNotes = "<p><b>Note:</b> <i>the above information is provided as a convenience and is not to be used as a statement of pay. Recent tasks may be missing due to processing delays and adjustments may be made prior to a payout statement being processed.</i></p>";
        Html notes = new Html(htmlNotes);
        notes.getElement().getStyle().set("font-size", "10px");
        detailsLayout.add(notes);
    }

}
