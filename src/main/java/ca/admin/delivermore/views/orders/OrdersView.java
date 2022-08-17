package ca.admin.delivermore.views.orders;

import ca.admin.delivermore.data.entity.Orders;
import ca.admin.delivermore.data.service.OrdersService;
import ca.admin.delivermore.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

@PageTitle("Orders")
@Route(value = "orders/:ordersID?/:action?(edit)", layout = MainLayout.class)
@PermitAll
public class OrdersView extends Div implements BeforeEnterObserver {

    private final String ORDERS_ID = "ordersID";
    private final String ORDERS_EDIT_ROUTE_TEMPLATE = "orders/%s/edit";

    private Grid<Orders> grid = new Grid<>(Orders.class, false);

    private TextField taskid;
    private TextField storeid;
    private TextField storename;
    private TextField street;
    private TextField postalCode;
    private TextField city;
    private TextField state;
    private TextField country;
    private TextField subtotal;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private BeanValidationBinder<Orders> binder;

    private Orders orders;

    private final OrdersService ordersService;

    @Autowired
    public OrdersView(OrdersService ordersService) {
        this.ordersService = ordersService;
        addClassNames("orders-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("taskid").setAutoWidth(true);
        grid.addColumn("storeid").setAutoWidth(true);
        grid.addColumn("storename").setAutoWidth(true);
        grid.addColumn("street").setAutoWidth(true);
        grid.addColumn("postalCode").setAutoWidth(true);
        grid.addColumn("city").setAutoWidth(true);
        grid.addColumn("state").setAutoWidth(true);
        grid.addColumn("country").setAutoWidth(true);
        grid.addColumn("subtotal").setAutoWidth(true);
        grid.setItems(query -> ordersService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(ORDERS_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(OrdersView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Orders.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(taskid).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("taskid");
        binder.forField(storeid).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("storeid");
        binder.forField(subtotal).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("subtotal");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.orders == null) {
                    this.orders = new Orders();
                }
                binder.writeBean(this.orders);

                ordersService.update(this.orders);
                clearForm();
                refreshGrid();
                Notification.show("Orders details stored.");
                UI.getCurrent().navigate(OrdersView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the orders details.");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<UUID> ordersId = event.getRouteParameters().get(ORDERS_ID).map(UUID::fromString);
        if (ordersId.isPresent()) {
            Optional<Orders> ordersFromBackend = ordersService.get(ordersId.get());
            if (ordersFromBackend.isPresent()) {
                populateForm(ordersFromBackend.get());
            } else {
                Notification.show(String.format("The requested orders was not found, ID = %s", ordersId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(OrdersView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        taskid = new TextField("Taskid");
        storeid = new TextField("Storeid");
        storename = new TextField("Storename");
        street = new TextField("Street");
        postalCode = new TextField("Postal Code");
        city = new TextField("City");
        state = new TextField("State");
        country = new TextField("Country");
        subtotal = new TextField("Subtotal");
        Component[] fields = new Component[]{taskid, storeid, storename, street, postalCode, city, state, country,
                subtotal};

        formLayout.add(fields);
        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getLazyDataView().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Orders value) {
        this.orders = value;
        binder.readBean(this.orders);

    }
}
