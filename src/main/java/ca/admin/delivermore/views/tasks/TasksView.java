package ca.admin.delivermore.views.tasks;

import ca.admin.delivermore.collector.data.entity.DriverPayoutEntity;
import ca.admin.delivermore.collector.data.entity.TaskEntity;
import ca.admin.delivermore.data.service.TaskDetailService;
import ca.admin.delivermore.views.MainLayout;
import ca.admin.delivermore.views.restaurants.RestView;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.vaadin.componentfactory.DateRange;
import com.vaadin.componentfactory.EnhancedDateRangePicker;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.apache.commons.collections4.comparators.FixedOrderComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.stream.Stream;

@PageTitle("Tasks")
@Route(value = "tasks", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class TasksView extends Main {
    private Logger log = LoggerFactory.getLogger(TasksView.class);
    private Grid<TaskEntity> tasksGrid = new Grid<>(TaskEntity.class);
    private TextField filterText = new TextField();

    private Label taskCount = new Label();
    private EnhancedDateRangePicker rangeDatePicker = new EnhancedDateRangePicker("Select range:");
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
                "commissionRate",
                "totalIncome",
                "templateId",
                "dispatcherId",
                "teamId",
                "fleetId",
                "fleetName",
                "driverIncome",
                "driverCash",
                "driverPayout",
                "webOrder",
                "feesOnly",
                "formId",
                "jobLatitude",
                "jobLongitude",
                "orderId",
                "autoAssignment",
                "userId",
                "sourceId",
                "source",
                "lastUpdated",
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
        form.addListener(TaskForm.SaveEvent.class,this::saveTask);
        form.addListener(TaskForm.DeleteEvent.class,this::deleteTask);
        form.addListener(TaskForm.CloseEvent.class,e -> closeEditor());

    }

    private void saveTask(TaskForm.SaveEvent event) {
        service.saveTaskDetail(event.getTaskEntity());
        updateList();
        closeEditor();
    }

    private void deleteTask(TaskForm.DeleteEvent event) {
        service.deleteTaskDetail(event.getTaskEntity());
        updateList();
        closeEditor();
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
                        //log.info("content:" + content);
                        //outputStream.write(content.getBytes());
                        return new ByteArrayInputStream(content.getBytes());
                    } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException ex) {
                        log.info("TasksView: CSV download failed");
                        return null;
                    }

                }
        );

        var downloadButton = new Anchor(streamResource,"Download");

        Button addContactButton = new Button("Add contact");

        LocalDate defaultDate = LocalDate.parse("2022-08-14");
        //TODO: get the date from the database for the largest task date and add 1 to set the defaultFromDate

        //get lastWeek as the default for the range picker
        LocalDate nowDate = LocalDate.now();
        LocalDate prevSun = nowDate.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
        prevSun = nowDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate startOfLastWeek = prevSun.minusWeeks(1);
        LocalDate endOfLastWeek = startOfLastWeek.plusDays(6);

        rangeDatePicker.setMin(defaultDate);
        rangeDatePicker.setValue(new DateRange(startOfLastWeek,endOfLastWeek));

        // Fetch all entities and show
        final Button fetchTasks = new Button("Fetch tasks",
                e -> updateList()
        );
        fetchTasks.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout toolbar = new HorizontalLayout(fetchTasks, filterText, rangeDatePicker, taskCount, downloadButton);
        toolbar.setAlignItems(FlexComponent.Alignment.BASELINE);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void updateList() {
        LocalDate startDate = rangeDatePicker.getValue().getStartDate();
        LocalDate endDate = rangeDatePicker.getValue().getEndDate();
        if(endDate==null){
            endDate = startDate;
        }
        log.info("updateList: start:" + startDate + " end:" + endDate);
        tasksGrid.setItems(service.findAllTaskDetails(startDate.atStartOfDay(),endDate.atTime(23,59,59)));
        taskCount.setText("(" + tasksGrid.getDataProvider().size(new Query<>()) + " tasks)");
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
