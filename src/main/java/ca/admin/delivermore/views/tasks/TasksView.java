package ca.admin.delivermore.views.tasks;

import ca.admin.delivermore.data.entity.TaskEntity;
import ca.admin.delivermore.data.service.RestaurantService;
import ca.admin.delivermore.data.service.TaskDetailService;
import ca.admin.delivermore.views.MainLayout;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.apache.commons.collections4.comparators.FixedOrderComparator;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.stream.Stream;

@PageTitle("Tasks")
@Route(value = "tasks", layout = MainLayout.class)
@AnonymousAllowed
public class TasksView extends Main {
    private Grid<TaskEntity> tasksGrid = new Grid<>(TaskEntity.class);
    private TextField filterText = new TextField();
    private DatePicker fromDatePicker = new DatePicker("From Date:");
    private DatePicker toDatePicker = new DatePicker("To Date:");
    private TaskDetailService service;
    private TaskForm form;
    private String[] columns;
    private String[] columnsUpper;

    public TasksView(@Autowired TaskDetailService service) {
        this.service = service;
        configureColumns();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        //updateList();
        closeEditor();

    }

    private void configureColumns() {
        columns = new String[]{
                "jobId",
                "jobStatus",
                "jobStatusName",
                "creationDate",
                "completedDate",
                "restaurantId",
                "restaurantName",
                "customerUsername",
                "customerEmail",
                "customerId",
                "customerPhone",
                "jobAddress",
                "jobDescription",
                "paymentMethod",
                "globalSubtotal",
                "globalTotalTaxes",
                "paidToVendor",
                "receiptTotal",
                "totalSale",
                "deliveryFee",
                "deliveryFeeFromVendor",
                "serviceFeePercent",
                "serviceFee",
                "totalFees",
                "driverPay",
                "feeBalance",
                "tip",
                "tipInNotesIssue",
                "notes",
                "commission",
                "totalIncome",
                "templateId",
                "dispatcherId",
                "teamId",
                "fleetId",
                "fleetName",
                "driverIncome",
                "driverCash",
                "driverPayout",
                "formId",
                "jobLatitude",
                "jobLongitude",
                "orderId",
                "autoAssignment",
                "userId",
                "createdBy"};

        columnsUpper = columns.clone();
        for(int i=0;i<columnsUpper.length;i++){
            columnsUpper[i] = columnsUpper[i].toUpperCase();
        }

    }

    private void closeEditor() {
        form.setTaskEntity(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(tasksGrid,form);
        content.setFlexGrow(2, tasksGrid);
        content.setFlexGrow(1, form);
        content.setClassName("content");
        content.setSizeFull();
        return content;

    }

    private void configureForm() {
        form = new TaskForm();
        form.setWidth("25em");

    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);

        var streamResource = new StreamResource(
                "tasks.csv",
                () -> {
                    try {

                        var mappingStrategy = new HeaderColumnNameMappingStrategy<TaskEntity>();
                        mappingStrategy.setType(TaskEntity.class);
                        mappingStrategy.setColumnOrderOnWrite(new FixedOrderComparator(columnsUpper));

                        Stream<TaskEntity> taskEntityStream = tasksGrid.getGenericDataView().getItems();
                        StringWriter output = new StringWriter();
                        StatefulBeanToCsv<TaskEntity> beanToCsv = new StatefulBeanToCsvBuilder<TaskEntity>(output)
                                .withMappingStrategy(mappingStrategy)
                                .build();
                        beanToCsv.write(taskEntityStream);
                        var content = output.toString();
                        //System.out.println("content:" + content);
                        //outputStream.write(content.getBytes());
                        return new ByteArrayInputStream(content.getBytes());
                    } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException ex) {
                        System.out.println("TasksView: CSV download failed");
                        return null;
                    }

                }
        );

        var downloadButton = new Anchor(streamResource,"Download");

        Button addContactButton = new Button("Add contact");

        LocalDate defaultDate = LocalDate.parse("2022-08-14");
        //TODO: get the date from the database for the largest task date and add 1 to set the defaultFromDate
        LocalDate defaultFromDate = defaultDate;
        LocalDate defaultToDate = defaultDate;

        DatePicker.DatePickerI18n singleFormatI18n = new DatePicker.DatePickerI18n();
        singleFormatI18n.setDateFormat("yyyy-MM-dd");

        fromDatePicker.setI18n(singleFormatI18n);
        fromDatePicker.setMin(defaultDate);
        fromDatePicker.setValue(defaultFromDate);
        toDatePicker.setI18n(singleFormatI18n);
        toDatePicker.setMin(defaultDate);
        toDatePicker.setValue(defaultToDate);

        LocalDate fromDate = fromDatePicker.getValue();
        LocalDate toDate = toDatePicker.getValue();

        // Fetch all entities and show
        final Button fetchTasks = new Button("Fetch tasks",
                e -> tasksGrid.setItems(service.findAllTaskDetails(fromDatePicker.getValue().atStartOfDay(),toDatePicker.getValue().atTime(23,59,59))));
        fetchTasks.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout toolbar = new HorizontalLayout(fetchTasks, filterText, fromDatePicker, toDatePicker, addContactButton, downloadButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }


    private void configureGrid() {
        tasksGrid.addClassNames("taskdetail-grid");
        tasksGrid.setWidthFull();
        //tasksGrid.setSpacing(false);
        //tasksGrid.setPadding(false);
        //tasksGrid.setMargin(false);
        //tasksGrid.setSizeFull();

        tasksGrid.setColumns(columns);

        tasksGrid.getColumns().forEach(col -> col.setAutoWidth(true));

        tasksGrid.asSingleSelect().addValueChangeListener(e -> editTaskEntity(e.getValue()));

    }

    private void editTaskEntity(TaskEntity taskEntity) {
        if(taskEntity == null){
            closeEditor();
        }else{
            form.setTaskEntity(taskEntity);
            form.setVisible(true);
            addClassName("editing");
        }
    }


}
