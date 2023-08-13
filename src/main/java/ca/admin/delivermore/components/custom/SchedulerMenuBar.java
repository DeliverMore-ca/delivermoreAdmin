package ca.admin.delivermore.components.custom;

import ca.admin.delivermore.collector.data.Config;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.stefan.fullcalendar.CalendarView;
import org.vaadin.stefan.fullcalendar.CalendarViewImpl;
import org.vaadin.stefan.fullcalendar.FullCalendar;
import org.vaadin.stefan.fullcalendar.SchedulerView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class SchedulerMenuBar extends MenuBar {

    private Logger log = LoggerFactory.getLogger(SchedulerMenuBar.class);
    private final FullCalendar scheduler;
    private Button buttonDatePicker;
    private MenuItem viewSelector;
    private MenuItem viewSettings;
    private MenuItem settingsIncludeAllDrivers;
    private CalendarView selectedView;

    public SchedulerMenuBar(FullCalendar scheduler, CalendarView selectedView, Boolean isOnlyUser) {
        this.scheduler = scheduler;
        this.selectedView = selectedView;

        addThemeVariants(MenuBarVariant.LUMO_TERTIARY);

        buildDateSelectors();

        Icon icon = new Icon(VaadinIcon.COG);
        viewSettings = addItem(icon, "Settings");

        if(!isOnlyUser){
            settingsIncludeAllDrivers = viewSettings.getSubMenu().addItem("Show all drivers");
            settingsIncludeAllDrivers.setCheckable(true);
            settingsIncludeAllDrivers.setChecked(false);
            settingsIncludeAllDrivers.addClickListener(e -> {
                log.info("ClickListener IncludeAllDrivers");
                scheduler.changeView(selectedView);
            });
        }

        addThemeVariants(MenuBarVariant.LUMO_SMALL);
    }

    private void buildDateSelectors(){
        addItem(VaadinIcon.ANGLE_LEFT.create(),"Previous", e -> scheduler.previous());

        // simulate the date picker light that we can use in polymer
        DatePicker gotoDate = new DatePicker();
        gotoDate.addValueChangeListener(event1 -> scheduler.gotoDate(event1.getValue()));
        gotoDate.getElement().getStyle().set("visibility", "hidden");
        gotoDate.getElement().getStyle().set("position", "fixed");
        gotoDate.setWidth("0px");
        gotoDate.setHeight("0px");
        gotoDate.setWeekNumbersVisible(true);
        buttonDatePicker = new Button(VaadinIcon.CALENDAR.create());
        buttonDatePicker.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        buttonDatePicker.getElement().appendChild(gotoDate.getElement());
        buttonDatePicker.addClickListener(event -> gotoDate.open());
        buttonDatePicker.setWidthFull();
        addItem(buttonDatePicker,"Go to selected date");
        addItem(VaadinIcon.ANGLE_RIGHT.create(),"Next", e -> scheduler.next());
        addItem("Today","Go to today's date", e -> scheduler.today());
    }

    public void updateInterval(LocalDate intervalStart) {
        //log.info("updateInterval: scheduler:" + scheduler.toString());
        if (buttonDatePicker != null && selectedView != null) {
            updateIntervalLabel(buttonDatePicker, selectedView, intervalStart);
        }
    }

    void updateIntervalLabel(HasText intervalLabel, CalendarView view, LocalDate intervalStart) {
        String text = "--";
        Locale locale = scheduler.getLocale();
        text = intervalStart.format(DateTimeFormatter.ofPattern("M/d/yy").withLocale(locale)) + " - " + intervalStart.plusDays(6).format(DateTimeFormatter.ofPattern("M/d/yy").withLocale(locale));

        intervalLabel.setText(text);
    }

    public Boolean getShowAllDrivers(){
        if(settingsIncludeAllDrivers==null) return Boolean.FALSE;
        return settingsIncludeAllDrivers.isChecked();
    }

    public MenuItem getSettingsMenuItem(){
        return viewSettings;
    }
}
