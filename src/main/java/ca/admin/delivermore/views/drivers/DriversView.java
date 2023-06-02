package ca.admin.delivermore.views.drivers;

import ca.admin.delivermore.collector.data.Role;
import ca.admin.delivermore.collector.data.service.DriversRepository;
import ca.admin.delivermore.collector.data.service.RestClientService;
import ca.admin.delivermore.collector.data.tookan.Driver;
import ca.admin.delivermore.gridexporter.ButtonsAlignment;
import ca.admin.delivermore.gridexporter.GridExporter;
import ca.admin.delivermore.security.AuthenticatedUser;
import ca.admin.delivermore.views.MainLayout;
import ca.admin.delivermore.views.UIUtilities;
import ca.admin.delivermore.views.UIUtilities.MenuEntry;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import java.text.SimpleDateFormat;
import java.util.*;


@PageTitle("Drivers")
@Route(value = "drivers", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class DriversView extends VerticalLayout {

    private Logger log = LoggerFactory.getLogger(DriversView.class);

    private enum ActionMode{
        ADD, REMOVE
    }
    private List<Driver> driverList;

    RestClientService restClientService;
    DriversRepository driversRepository;
    AuthenticatedUser authenticatedUser;

    private VerticalLayout mainLayout = UIUtilities.getVerticalLayout();
    private Grid<Driver> grid = new Grid<>();
    private MenuBar menuBar = new MenuBar();
    private Map<String, UIUtilities.MenuEntry> menuItems = new LinkedHashMap<>();

    private List<Driver> selectedDrivers = new ArrayList<>();

    public DriversView(DriversRepository driversRepository, RestClientService restClientService, AuthenticatedUser authenticatedUser) {
        this.driversRepository = driversRepository;
        this.restClientService = restClientService;
        this.authenticatedUser = authenticatedUser;

        mainLayout.add(getToolbar());
        mainLayout.add(getGrid());
        setSizeFull();
        mainLayout.setSizeFull();
        add(mainLayout);

    }

    private HorizontalLayout getToolbar() {
        HorizontalLayout toolbar = UIUtilities.getHorizontalLayout(true,true,false );
        toolbar.setAlignItems(FlexComponent.Alignment.BASELINE);
        toolbar.addClassName("toolbar");
        Button refreshFromTookanButton = new Button("Refresh from Tookan");
        refreshFromTookanButton.setDisableOnClick(true);
        refreshFromTookanButton.addClickListener(event -> {
            refreshDrivers();
            grid.getDataProvider().refreshAll();
            refreshFromTookanButton.setEnabled(true);
        });

        //TODO: add a SELECT dropdown of bulk actions - AddAsUser, AddAsManager... RemoveUser...

        ComponentEventListener<ClickEvent<MenuItem>> listener = e -> {
            //log.info("ClickListener:" + e.getSource().getText());
            performAction(getMenuEntryByDisplayName(e.getSource().getText()));
        };

        MenuItem actions = menuBar.addItem("Actions");
        SubMenu actionsSubMenu = actions.getSubMenu();
        addMenuEntry(MenuEntry.MenuKeys.PAYOUTENABLE, "Enable Payout");
        addMenuEntry(MenuEntry.MenuKeys.PAYOUTDISABLE, "Disable Payout");
        addMenuEntryPlaceholder();
        addMenuEntry(MenuEntry.MenuKeys.ADDUSER, "Add User");
        addMenuEntry(MenuEntry.MenuKeys.REMOVEUSER, "Remove User");
        addMenuEntryPlaceholder();
        addMenuEntry(MenuEntry.MenuKeys.ADDMANAGER, "Add Manager");
        addMenuEntry(MenuEntry.MenuKeys.REMOVEMANAGER, "Remove Manager");
        addMenuEntryPlaceholder();
        addMenuEntry(MenuEntry.MenuKeys.ADDADMIN, "Add Admin");
        addMenuEntry(MenuEntry.MenuKeys.REMOVEADMIN, "Remove Admin");
        addMenuEntryPlaceholder();
        addMenuEntry(MenuEntry.MenuKeys.ALLOWLOGIN, "Allow Login");
        addMenuEntry(MenuEntry.MenuKeys.DISABLELOGIN, "Disable Login");
        addMenuEntryPlaceholder();
        addMenuEntry(MenuEntry.MenuKeys.PASSWORDRESET, "Send Password Reset/Invite");

        for (UIUtilities.MenuEntry menuEntry: menuItems.values() ) {
            if(menuEntry.getPlaceHolder()){
                actionsSubMenu.addItem(new Hr(), listener);
            }else{
                actionsSubMenu.addItem(menuEntry.getDisplayName(), listener);
            }
        }

        menuBar.setEnabled(false);

        toolbar.add(menuBar,refreshFromTookanButton);
        return toolbar;
    }

    private UIUtilities.MenuEntry getMenuEntryByDisplayName(String displayName){
        for (UIUtilities.MenuEntry menuEntry: menuItems.values()) {
            //log.info("getMenuEntryByDisplayName: Checking:" + menuEntry.getDisplayName() + " for value:" + displayName);
            if(menuEntry.getDisplayName().equals(displayName)){
                return menuEntry;
            }
        }
        log.info("getMenuEntryByDisplayName: Not found");
        return null;
    }

    private void addMenuEntry(MenuEntry.MenuKeys name, String displayName){
        menuItems.put(name.name(), new UIUtilities.MenuEntry(name, displayName));
    }
    private void addMenuEntryPlaceholder(){
        menuItems.put(RandomStringUtils.randomAlphabetic(10), new UIUtilities.MenuEntry());
    }

    private VerticalLayout getGrid(){
        VerticalLayout gridLayout = UIUtilities.getVerticalLayout();
        gridLayout.setWidthFull();
        gridLayout.setHeightFull();

        driverList = driversRepository.findByOrderByIsActiveDescNameAsc();
        grid.setItems(driverList);
        grid.setSizeFull();
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addThemeVariants(GridVariant.LUMO_COMPACT);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.setMultiSort(true, Grid.MultiSortPriority.APPEND);
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

        GridExporter<Driver> exporter = GridExporter.createFor(grid);
        Grid.Column imageColumn = grid.addColumn(createAvatarRenderer()).setHeader("Image").setAutoWidth(true).setFlexGrow(0);
        exporter.setExportColumn(imageColumn,false);
        exporter.createExportColumn(grid.addColumn(Driver::getName).setFlexGrow(1).setSortable(true),true,"Name");
        exporter.createExportColumn(grid.addColumn(Driver::getEmail).setAutoWidth(true).setFlexGrow(0),true,"Email");
        exporter.createExportColumn(grid.addColumn(Driver::getFleetId).setAutoWidth(true).setFlexGrow(0),true,"Id");
        String statusWidth = "50px";
        exporter.createExportColumn(grid.addComponentColumn(driver -> createStatusIcon(driver.getIsActive().equals(1L))).setWidth(statusWidth).setComparator(Driver::getIsActive),true,"Payout Enabled",grid.addColumn(Driver::getIsActivePresentation));
        exporter.createExportColumn(grid.addComponentColumn(driver -> createStatusIcon(driver.isUser())).setWidth(statusWidth).setComparator(Driver::isUser),true,"User",grid.addColumn(Driver::isUser));
        exporter.createExportColumn(grid.addComponentColumn(driver -> createStatusIcon(driver.isManager())).setWidth(statusWidth).setComparator(Driver::isManager),true,"Manager",grid.addColumn(Driver::isManager));
        exporter.createExportColumn(grid.addComponentColumn(driver -> createStatusIcon(driver.isAdmin())).setWidth(statusWidth).setComparator(Driver::isAdmin),true,"Admin",grid.addColumn(Driver::isAdmin));
        exporter.createExportColumn(grid.addComponentColumn(driver -> createStatusIcon(driver.getLoginAllowed())).setWidth(statusWidth).setComparator(Driver::getLoginAllowed),true,"Login Allowed",grid.addColumn(Driver::getLoginAllowed));
        exporter.createExportColumn(grid.addComponentColumn(driver -> createStatusIcon(driver.hasPassword())).setWidth(statusWidth).setComparator(Driver::hasPassword),true,"Password?",grid.addColumn(Driver::hasPassword));
        exporter.createExportColumn(grid.addColumn(Driver::getFleetThumbImage),false,"Image");

        exporter.setFileName("DriverExport" + new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()));
        exporter.setButtonsAlignment(ButtonsAlignment.LEFT);

        gridLayout.add(grid);
        gridLayout.setFlexGrow(1,grid);

        grid.addSelectionListener(e -> {
            selectedDrivers.clear();
            selectedDrivers.addAll(e.getAllSelectedItems());
            if(selectedDrivers.size()>0){
                menuBar.setEnabled(true);
            }else{
                menuBar.setEnabled(false);
            }
        });

        return gridLayout;
    }

    private static Renderer<Driver> createAvatarRenderer() {
        return LitRenderer.<Driver> of(
                        "<vaadin-avatar img=\"${item.pictureUrl}\" name=\"${item.fullName}\" alt=\"User avatar\"></vaadin-avatar>")
                .withProperty("pictureUrl", Driver::getFleetThumbImage)
                .withProperty("fullName", Driver::getName);
    }

    private Icon createStatusIcon(Boolean inRole) {
        Icon icon;
        if (inRole) {
            icon = VaadinIcon.CHECK.create();
            icon.getElement().getThemeList().add("badge success");
        } else {
            icon = VaadinIcon.CLOSE_SMALL.create();
            icon.getElement().getThemeList().add("badge error");
        }
        icon.getStyle().set("padding", "var(--lumo-space-xs");
        return icon;
    }

    private void refreshDrivers(){
        List<Driver> currentDriverList = restClientService.getAllDrivers();
        //update existing tookan data or save new
        for (Driver driver: currentDriverList) {
            Driver foundDriver = driversRepository.getDriverByFleetId(driver.getFleetId());
            if(foundDriver==null){ //new
                log.info("refreshDrivers: saving new driver:" + driver.getName());
                driversRepository.save(driver);
            }else{ //update
                log.info("refreshDrivers: updating tookan fields for driver:" + driver.getName());
                foundDriver.updateDriverTookanOnly(driver);
                driversRepository.save(foundDriver);
            }
        }
        List<Driver> allDriverList = driversRepository.findAll();
        for (Driver driver : allDriverList) {
            log.info("refreshDrivers: calling updateDriverIsActive for:" + driver.getName());
            driversRepository.save(driver.updateDriverIsActive(driver,currentDriverList));
        }
        //refresh the full list from the database
        driverList = driversRepository.findByOrderByIsActiveDescNameAsc();
        grid.setItems(driverList);
        grid.getDataProvider().refreshAll();
    }

    private void performAction(UIUtilities.MenuEntry action){
        log.info("performAction:" + action);
        if (action != null) {
            for (Driver driver : selectedDrivers) {
                if(action.getName().equals(MenuEntry.MenuKeys.ADDUSER)){
                    changeRole(driver,Role.USER,action,ActionMode.ADD);
                }else if(action.getName().equals(MenuEntry.MenuKeys.ADDMANAGER)){
                    changeRole(driver,Role.MANAGER,action,ActionMode.ADD);
                }else if(action.getName().equals(MenuEntry.MenuKeys.ADDADMIN)){
                    changeRole(driver,Role.ADMIN,action,ActionMode.ADD);
                }else if(action.getName().equals(MenuEntry.MenuKeys.REMOVEUSER)){
                    changeRole(driver,Role.USER,action,ActionMode.REMOVE);
                }else if(action.getName().equals(MenuEntry.MenuKeys.REMOVEMANAGER)){
                    changeRole(driver,Role.MANAGER,action,ActionMode.REMOVE);
                }else if(action.getName().equals(MenuEntry.MenuKeys.REMOVEADMIN)){
                    changeRole(driver,Role.ADMIN,action,ActionMode.REMOVE);
                }else if(action.getName().equals(MenuEntry.MenuKeys.PAYOUTENABLE)){
                    driver.setIsActive(1L);
                    updateDriver(driver,action);
                }else if(action.getName().equals(MenuEntry.MenuKeys.PAYOUTDISABLE)){
                    driver.setIsActive(0L);
                    updateDriver(driver,action);
                }else if(action.getName().equals(MenuEntry.MenuKeys.ALLOWLOGIN)){
                    driver.setLoginAllowed(Boolean.TRUE);
                    updateDriver(driver,action);
                }else if(action.getName().equals(MenuEntry.MenuKeys.DISABLELOGIN)){
                    driver.setLoginAllowed(Boolean.FALSE);
                    driver.setHashedPassword(null); //clear the password
                    updateDriver(driver,action);
                }
            }
            if(action.getName().equals(MenuEntry.MenuKeys.PASSWORDRESET)){
                sendPasswordResetToSelected();
            }
        }
    }

    private void changeRole(Driver driver, Role role,UIUtilities.MenuEntry action, ActionMode actionMode){
        if(actionMode.equals(ActionMode.ADD)){
            driver.getRoles().add(role);
        }else{
            driver.getRoles().remove(role);
        }
        updateDriver(driver,action);
    }

    private void updateDriver(Driver driver, UIUtilities.MenuEntry action){
        driversRepository.save(driver);
        grid.getDataProvider().refreshItem(driver);
        UIUtilities.showNotification(action.getDisplayName() + ":" + driver.getName());
    }

    public void sendPasswordResetToSelected(){
        if(selectedDrivers==null || selectedDrivers.size()==0){
            log.info("sendPasswordResetToSelected: no drivers selected");
            return;
        }
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Send password reset/invite?");
        //build the message from list of selected drivers
        String message = "<p>Confirm sending password reset/invite to:<br><br>";
        List<Driver> validDrivers = new ArrayList<>();
        for (Driver driver : selectedDrivers) {
            if(driver.getFleetId()<=10L || driver.getIsActive().equals(1L)){
                validDrivers.add(driver);
                message+= driver.getName() + ": " + driver.getEmail() + "<br>";
            }
        }
        message+="</p>";
        if(validDrivers.size()==0){
            UIUtilities.showNotification("No valid drivers selected. Check for blocked in Tookan.");
            return;
        }
        dialog.setText(new Html(message));

        dialog.setCancelable(true);
        dialog.addConfirmListener(event -> {
            //send the reset for each driver selected
            for (Driver driver : validDrivers) {
                log.info("sendPasswordResetToSelected: sending reset to:" + driver.getName() + " : " + driver.getEmail());
                //driver must be allowed to login
                driver.setLoginAllowed(Boolean.TRUE);
                driversRepository.save(driver);
                if(authenticatedUser.resetPassword(driver.getEmail())){
                    log.info("sendPasswordResetToSelected: reset complete - inform user");
                    grid.getDataProvider().refreshItem(driver);
                    UIUtilities.showNotification("Password reset/invite(s) sent to:" + driver.getName() + " : " + driver.getEmail());
                }else{
                    log.info("sendPasswordResetToSelected: reset failed - invalid user");
                    UIUtilities.showNotification("Password reset/invite(s) failed for:" + driver.getName() + " : " + driver.getEmail());
                }
            }
        });

        dialog.addCancelListener(e -> {
            log.info("sendPasswordResetToSelected: cancelled by user");
        });

        dialog.setConfirmText("Confirm");
        dialog.open();
    }

}
