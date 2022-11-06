package ca.admin.delivermore.views;

import ca.admin.delivermore.components.appnav.AppNav;
import ca.admin.delivermore.components.appnav.AppNavItem;
import ca.admin.delivermore.components.appnav.BrandExpression;
import ca.admin.delivermore.data.entity.User;
import ca.admin.delivermore.security.AuthenticatedUser;
import ca.admin.delivermore.views.about.AboutView;
import ca.admin.delivermore.views.drivers.DriverPayoutView;
import ca.admin.delivermore.views.drivers.DriversView;
import ca.admin.delivermore.views.home.HomeView;
import ca.admin.delivermore.views.orders.OrdersView;
import ca.admin.delivermore.views.restaurants.RestPayoutView;
import ca.admin.delivermore.views.restaurants.RestView;
import ca.admin.delivermore.views.tasks.TasksView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import java.util.Optional;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    private H1 viewTitle;

    private AuthenticatedUser authenticatedUser;
    private AccessAnnotationChecker accessChecker;

    public MainLayout(AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker) {
        this.authenticatedUser = authenticatedUser;
        this.accessChecker = accessChecker;

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
        com.vaadin.flow.component.html.Section section = new com.vaadin.flow.component.html.Section(new BrandExpression(text),createNavigation(), createFooter());
        section.addClassNames("drawer-section");
        return section;
    }

    private AppNav createNavigation() {
        // AppNav is not yet an official component.
        // For documentation, visit https://github.com/vaadin/vcf-nav#readme
        AppNav nav = new AppNav();
        nav.addClassNames("app-nav");

        if (accessChecker.hasAccess(HomeView.class)) {
            nav.addItem(new AppNavItem("Home", HomeView.class, "la la-home"));

        }
        if (accessChecker.hasAccess(AboutView.class)) {
            nav.addItem(new AppNavItem("About", AboutView.class, "la la-question-circle"));

        }
        AppNavItem utilities = new AppNavItem("Utilities");
        utilities.setIconClass("la la-folder-open");
        //utilities.setIcon(new Icon(VaadinIcon.FOLDER_OPEN));
        nav.addItem(utilities);
        if (accessChecker.hasAccess(DriversView.class)) {
            AppNavItem driversMenu = new AppNavItem("Drivers", DriversView.class, "la la-car-side");
            utilities.addItem(driversMenu);
        }
        if (accessChecker.hasAccess(RestView.class)) {
            AppNavItem restMenu = new AppNavItem("Restaurants", RestView.class, "la la-store-alt");
            utilities.addItem(restMenu);
        }
        if (accessChecker.hasAccess(TasksView.class)) {
            AppNavItem tasksMenu = new AppNavItem("Tasks(in progress)", TasksView.class, "la la-stack-overflow");
            utilities.addItem(tasksMenu);
        }
        if (accessChecker.hasAccess(DriverPayoutView.class)) {
            nav.addItem(new AppNavItem("Driver Payouts", DriverPayoutView.class, "la la-portrait"));

        }
        if (accessChecker.hasAccess(RestPayoutView.class)) {
            nav.addItem(new AppNavItem("Restaurant Payouts", RestPayoutView.class, "la la-file-invoice-dollar"));

        }
        if (accessChecker.hasAccess(OrdersView.class)) {
            nav.addItem(new AppNavItem("Orders", OrdersView.class, "la la-columns"));

        }

        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();
        layout.addClassNames("app-nav-footer");

        Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();

            Avatar avatar = new Avatar(user.getName(), user.getProfilePictureUrl());
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
            Anchor loginLink = new Anchor("login", "Sign in");
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
