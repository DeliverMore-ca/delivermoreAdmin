package ca.admin.delivermore.views;

import ca.admin.delivermore.collector.data.tookan.Driver;
import ca.admin.delivermore.components.appnav.AppNav;
import ca.admin.delivermore.components.appnav.AppNavItem;
import ca.admin.delivermore.components.appnav.BrandExpression;
import ca.admin.delivermore.data.service.Registry;
import ca.admin.delivermore.data.service.intuit.domain.OAuth2Configuration;
import ca.admin.delivermore.security.AuthenticatedUser;
import ca.admin.delivermore.views.about.AboutView;
import ca.admin.delivermore.views.drivers.*;
import ca.admin.delivermore.views.home.HomeView;
import ca.admin.delivermore.views.intuit.QBOConnectView;
import ca.admin.delivermore.views.login.PasswordReset;
import ca.admin.delivermore.views.report.PeriodSummaryView;
import ca.admin.delivermore.views.restaurants.RestPayoutView;
import ca.admin.delivermore.views.restaurants.RestView;
import ca.admin.delivermore.views.tasks.TaskListView;
import ca.admin.delivermore.views.tasks.TasksByCustomerView;
import ca.admin.delivermore.views.tasks.TasksByDayAndWeekView;
import ca.admin.delivermore.views.tasks.TasksView;
import ca.admin.delivermore.views.utility.GiftCardView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

import java.util.Optional;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    private Logger log = LoggerFactory.getLogger(MainLayout.class);

    @Value("${security.enabled:true}")
    private boolean securityEnabled;

    private H1 viewTitle;

    private AuthenticatedUser authenticatedUser;
    private AccessAnnotationChecker accessChecker;
    private OAuth2Configuration oAuth2Configuration;

    @Autowired
    Environment env;

    public MainLayout(AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker, Environment env) {
        this.authenticatedUser = authenticatedUser;
        this.accessChecker = accessChecker;
        this.oAuth2Configuration = Registry.getBean(OAuth2Configuration.class);
        if(env.containsProperty("security.enabled")){
            this.securityEnabled = Boolean.parseBoolean(env.getProperty("security.enabled"));
        }else{
            this.securityEnabled = true;
        }

        //TODO: load data from Tookan and then save to the TaskDetailRepository
        //create a method that provides a TaskEntity for each Task

        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        addToDrawer(createDrawerContent());
    }

    private Component createHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.addClassNames("view-toggle");
        toggle.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        viewTitle = new H1();
        viewTitle.addClassNames("view-title");

        Header header = new Header(toggle, viewTitle);
        header.addClassNames("view-header");
        return header;
    }

    private Component createDrawerContent() {
        String text = "DeliverMore";
        com.vaadin.flow.component.html.Section section = new com.vaadin.flow.component.html.Section(new BrandExpression(text), createFooter(),createNavigation());
        section.addClassNames("drawer-section");
        return section;
    }

    private AppNav createNavigation() {
        // AppNav is not yet an official component.
        // For documentation, visit https://github.com/vaadin/vcf-nav#readme
        AppNav nav = new AppNav();
        nav.addClassNames("app-nav");

        if (checkAccess(HomeView.class)) {
            nav.addItem(new AppNavItem("Home", HomeView.class, "la la-home"));

        }
        if (checkAccess(AboutView.class)) {
            nav.addItem(new AppNavItem("About", AboutView.class, "la la-question-circle"));

        }
        if (checkAccess(MyPayView.class)) {
            nav.addItem(new AppNavItem("My Pay", MyPayView.class, "la la-dollar-sign"));

        }

        if (checkAccess(DriverRedeemGiftCardView.class)) {
            nav.addItem(new AppNavItem("Redeem Gift Card", DriverRedeemGiftCardView.class, "la la-credit-card"));

        }

        if (checkAccess(ScheduleView.class)) {
            nav.addItem(new AppNavItem("Schedule", ScheduleView.class, "la la-calendar"));

        }

        //only add the menu folder if the user has access to at least one of the sub views
        if (checkAccess(DriversView.class)
                || checkAccess(RestView.class)
                || checkAccess(TaskListView.class)
                || checkAccess(QBOConnectView.class)
                || checkAccess(TasksView.class)) {
            AppNavItem utilities = new AppNavItem("Utilities");
            utilities.setIconClass("la la-folder-open");
            nav.addItem(utilities);
            if (checkAccess(DriversView.class)) {
                AppNavItem driversMenu = new AppNavItem("Drivers", DriversView.class, "la la-car-side");
                utilities.addItem(driversMenu);
            }
            if (checkAccess(RestView.class)) {
                AppNavItem restMenu = new AppNavItem("Restaurants", RestView.class, "la la-store-alt");
                utilities.addItem(restMenu);
            }
            if (checkAccess(TaskListView.class)) {
                AppNavItem taskListMenu = new AppNavItem("Task List", TaskListView.class, "la la-stack-overflow");
                utilities.addItem(taskListMenu);
            }
            if (checkAccess(GiftCardView.class)) {
                AppNavItem giftCardListMenu = new AppNavItem("Gift Card List", GiftCardView.class, "la la-credit-card");
                utilities.addItem(giftCardListMenu);
            }
            if(oAuth2Configuration.isConfigured()){
                if (checkAccess(QBOConnectView.class)) {
                    AppNavItem qboConnectMenu = new AppNavItem("QBO connect", QBOConnectView.class, "la la-network-wired");
                    utilities.addItem(qboConnectMenu);
                }
            }
            if (checkAccess(TasksView.class)) {
                AppNavItem tasksMenu = new AppNavItem("Tasks(under dev)", TasksView.class, "la la-stack-overflow");
                utilities.addItem(tasksMenu);
            }
        }

        //only add the menu folder if the user has access to at least one of the sub views
        if (checkAccess(TasksByCustomerView.class)
                || checkAccess(TasksByDayAndWeekView.class)
                || checkAccess(PeriodSummaryView.class)) {
            AppNavItem reports = new AppNavItem("Reports");
            reports.setIconClass("la la-folder-open");
            nav.addItem(reports);
            if (checkAccess(TasksByCustomerView.class)) {
                AppNavItem customerTasksMenu = new AppNavItem("Tasks by Customer", TasksByCustomerView.class, "la la-user");
                reports.addItem(customerTasksMenu);
            }
            if (checkAccess(TasksByDayAndWeekView.class)) {
                AppNavItem tasksByDayAndWeekMenu = new AppNavItem("Tasks by Day/Week", TasksByDayAndWeekView.class, "la la-calendar");
                reports.addItem(tasksByDayAndWeekMenu);
            }
            if (checkAccess(PeriodSummaryView.class)) {
                AppNavItem periodSummaryMenu = new AppNavItem("Period Summary", PeriodSummaryView.class, "la la-calendar");
                reports.addItem(periodSummaryMenu);
            }
        }

        if (checkAccess(DriverPayoutView.class)) {
            nav.addItem(new AppNavItem("Driver Payouts", DriverPayoutView.class, "la la-portrait"));

        }
        if (checkAccess(RestPayoutView.class)) {
            nav.addItem(new AppNavItem("Restaurant Payouts", RestPayoutView.class, "la la-file-invoice-dollar"));

        }
        if (checkAccess(PasswordReset.class)) {
            nav.addItem(new AppNavItem("Reset password", PasswordReset.class, "la la-key"));

        }

        return nav;
    }

    private Boolean checkAccess(Class<?> cls){
        if(securityEnabled){
            return accessChecker.hasAccess(cls);
        }else{
            return true;
        }
    }

    //NOTE: foot moved to the top of the side menu at request of owner
    private Footer createFooter() {
        Footer layout = new Footer();
        layout.addClassNames("app-nav-footer");
        String securityDisabledString = "";
        if(!securityEnabled) securityDisabledString = " (Security Disabled)";

        Optional<Driver> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            Driver user = maybeUser.get();

            Avatar avatar = new Avatar(user.getName(), user.getFleetThumbImage());
            avatar.addClassNames("me-xs");

            ContextMenu userMenu = new ContextMenu(avatar);
            userMenu.setOpenOnClick(true);
            userMenu.addItem("Logout", e -> {
                authenticatedUser.logout();
            });

            Span name = new Span(user.getName());
            name.addClassNames("font-medium", "text-s", "text-secondary");

            layout.add(avatar, name);
        } else {
            Anchor loginLink = new Anchor("login", "Sign in" + securityDisabledString);
            layout.add(loginLink);
        }

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
