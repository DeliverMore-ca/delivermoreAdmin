package ca.admin.delivermore.views.restaurants;

import ca.admin.delivermore.collector.data.Config;
import ca.admin.delivermore.collector.data.entity.DriverPayoutEntity;
import ca.admin.delivermore.collector.data.entity.Restaurant;
import ca.admin.delivermore.collector.data.entity.TaskEntity;
import ca.admin.delivermore.collector.data.service.RestaurantRepository;
import ca.admin.delivermore.components.custom.ListEditor;
import ca.admin.delivermore.data.report.RestPayoutAdjustmentDialog;
import ca.admin.delivermore.views.MainLayout;
import ca.admin.delivermore.views.drivers.DriverPayoutView;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.ShortcutRegistration;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextAreaVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import java.time.LocalDate;

@PageTitle("Restaurants")
@Route(value = "restaurants", layout = MainLayout.class)
@AnonymousAllowed
public class RestView extends VerticalLayout {

    enum DialogMode{
        NEW, EDIT, NEW_CLONE, DELETE
    }
    //Restaurant Dialog fields
    private Dialog restDialog = new Dialog();
    private DialogMode restDialogMode = DialogMode.EDIT;

    private Button dialogOkButton = new Button("OK");
    private Button dialogCancelButton = new Button("Cancel");
    private Button dialogCloseButton = new Button(new Icon("lumo", "cross"));
    private TextField dialogRestName = new TextField("Restaurant");
    private TextField dialogRestEffectiveDate = new TextField("Effective Date");
    private ListEditor dialogRestEmailEditor = new ListEditor();

    Grid<Restaurant> grid = new Grid<>();
    private Restaurant selectedRestaurant = new Restaurant();

    RestaurantRepository restaurantRepository;

    public RestView(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
        dialogConfigure();

        grid.removeAllColumns();
        grid.addComponentColumn(item -> {
            Icon editIcon = new Icon("lumo", "edit");
            //Button editButton = new Button("Edit");
            editIcon.addClickListener(e -> {
                restDialogMode = DialogMode.EDIT;
                dialogOpen(item);
            });
            return editIcon;
        }).setWidth("150px").setFlexGrow(0);
        grid.addColumn(Restaurant::getName)
                .setFlexGrow(1)
                .setHeader("Name");
        grid.addColumn(Restaurant::getRestaurantId).setHeader("Id");
        grid.addColumn(Restaurant::getDateEffective).setHeader("Effective");
        //grid.getGrid().addColumn(Restaurant::getDateExpired).setHeader("Expired");
        grid.addColumn(Restaurant::getEmail)
                .setFlexGrow(0)
                .setWidth("250px")
                .setHeader("Email");
        grid.setColumnReorderingAllowed(true);
        //grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

        // layout configuration
        setSizeFull();
        add(grid);
        grid.setItems(restaurantRepository.getEffectiveRestaurantsForPayout(LocalDate.now()));

    }

    public void dialogConfigure() {
        restDialog.getElement().setAttribute("aria-label", "Edit restaurant details");

        VerticalLayout dialogLayout = dialogLayout();
        restDialog.add(dialogLayout);
        restDialog.setHeaderTitle("Edit Selected Restaurant");

        dialogCloseButton.addClickListener((e) -> restDialog.close());
        dialogCloseButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        restDialog.getHeader().add(dialogCloseButton);
        restDialog.setCloseOnEsc(true);
        dialogCancelButton.addClickListener((e) -> restDialog.close());

        dialogOkButton.addClickListener(
                event -> {
                    dialogSave();
                }
        );
        dialogOkButton.addClickShortcut(Key.ENTER);
        dialogOkButton.setEnabled(true);

        HorizontalLayout footerLayout = new HorizontalLayout(dialogOkButton,dialogCancelButton);

        // Prevent click shortcut of the OK button from also triggering when another button is focused
        /*
        ShortcutRegistration shortcutRegistration = Shortcuts
                .addShortcutListener(footerLayout, () -> {}, Key.ENTER)
                .listenOn(footerLayout);
        shortcutRegistration.setEventPropagationAllowed(false);
        shortcutRegistration.setBrowserDefaultAllowed(true);

         */

        restDialog.getFooter().add(footerLayout);
    }

    private VerticalLayout dialogLayout() {
        dialogRestName.setReadOnly(true);
        dialogRestName.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        dialogRestEffectiveDate.setReadOnly(true);
        dialogRestEffectiveDate.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        dialogRestEmailEditor.setSeparator(", ");

        VerticalLayout fieldLayout = new VerticalLayout(dialogRestName,dialogRestEffectiveDate,dialogRestEmailEditor);
        fieldLayout.setSpacing(false);
        fieldLayout.setPadding(false);
        fieldLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        fieldLayout.getStyle().set("width", "300px").set("max-width", "100%");

        return fieldLayout;
    }

    private void dialogOpen(Restaurant restaurant){
        selectedRestaurant = restaurant;
        //set values
        dialogRestName.setValue(selectedRestaurant.getName());
        dialogRestEffectiveDate.setValue(selectedRestaurant.getDateEffective().toString());
        if(restaurant.getEmail()==null){
            dialogRestEmailEditor.setValue("");
        }else{
            dialogRestEmailEditor.setValue(selectedRestaurant.getEmail());
        }

        restDialog.open();
    }

    private void dialogSave() {
        System.out.println("dialogSave: called for:" + selectedRestaurant.toString());
        selectedRestaurant.setEmail(dialogRestEmailEditor.getValue());

        restaurantRepository.save(selectedRestaurant);
        Notification.show("Updated");

        //refresh
        grid.getDataProvider().refreshAll();
        restDialog.close();
    }

}
