package ca.admin.delivermore.views.tasks;

import ca.admin.delivermore.collector.data.service.RestClientService;
import ca.admin.delivermore.collector.data.service.TaskDetailRepository;
import ca.admin.delivermore.data.report.TasksForMonth;
import ca.admin.delivermore.data.report.TasksForWeek;
import ca.admin.delivermore.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.annotation.security.RolesAllowed;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@PageTitle("Tasks by Day and Week")
@Route(value = "tasksbydayandweek", layout = MainLayout.class)
@RolesAllowed({"ADMIN","MANAGER"})
public class TasksByDayAndWeekView extends VerticalLayout {
    private TaskDetailRepository taskDetailRepository;
    private RestClientService restClientService;
    private Grid<TasksForWeek> grid = new Grid<>();

    private HeaderRow hRowRecords;
    private HeaderRow hRowAverages;
    private HeaderRow hRowMonthRecord;
    private HeaderRow hRowMonthAverage;
    private Grid.Column colName;
    private Grid.Column colSunday;
    private Grid.Column colMonday;
    private Grid.Column colTuesday;
    private Grid.Column colWednesday;
    private Grid.Column colThursday;
    private Grid.Column colFriday;
    private Grid.Column colSaturday;
    private Grid.Column colWeek;

    private Grid.Column colMonthName;
    private Grid.Column colMonthCount;


    private Grid<TasksForMonth> gridMonths = new Grid<>();
    private Label countLabel = new Label();
    private List<TasksForWeek> tasksForWeeks = new ArrayList<>();
    private Logger log = LoggerFactory.getLogger(TasksByDayAndWeekView.class);
    private TasksForWeek recordTasksForWeek = new TasksForWeek(TasksForWeek.TasksForWeekType.RECORD);
    private TasksForWeek sumTasksForWeek = new TasksForWeek(TasksForWeek.TasksForWeekType.SUM);
    private TasksForWeek averageTasksForWeek = new TasksForWeek(TasksForWeek.TasksForWeekType.AVERAGE);

    private List<TasksForMonth> tasksForMonths = new ArrayList<>();
    private TasksForMonth recordTasksForMonth = new TasksForMonth(TasksForMonth.TasksForMonthType.RECORD);
    private TasksForMonth sumTasksForMonth = new TasksForMonth(TasksForMonth.TasksForMonthType.SUM);
    private TasksForMonth averageTasksForMonth = new TasksForMonth(TasksForMonth.TasksForMonthType.AVERAGE);

    @Autowired
    public TasksByDayAndWeekView(TaskDetailRepository taskDetailRepository, RestClientService restClientService) {
        this.taskDetailRepository = taskDetailRepository;
        this.restClientService = restClientService;
        //addClassNames("tasksbydayandweek-view");
        configureGrid();
        configureGridMonths();
        // layout configuration
        setSizeFull();
        add(getToolbar(), grid, gridMonths);

        updateList();

    }

    private void configureGrid(){
        grid.removeAllColumns();
        //grid.setClassName("no-upload-grid-no");
        //grid.setClassName("task-record");
        colName = grid.addColumn(TasksForWeek::getWeekName)
                .setWidth("125px")
                .setHeader("Week")
                .setFrozen(true);
                //.getElement().getStyle().set("background-color", "yellow");
        colSunday = grid.addColumn(TasksForWeek::getDowCountSunday)
                .setTextAlign(ColumnTextAlign.END)
                .setClassNameGenerator(item -> item.getDowCountSunday() == recordTasksForWeek.getDowCountSunday() ? "record" : null)
                .setHeader("Sun");
        colMonday = grid.addColumn(TasksForWeek::getDowCountMonday)
                .setTextAlign(ColumnTextAlign.END)
                .setClassNameGenerator(item -> item.getDowCountMonday() == recordTasksForWeek.getDowCountMonday() ? "record" : null)
                .setHeader("Mon");
        colTuesday = grid.addColumn(TasksForWeek::getDowCountTuesday)
                .setTextAlign(ColumnTextAlign.END)
                .setClassNameGenerator(item -> item.getDowCountTuesday() == recordTasksForWeek.getDowCountTuesday() ? "record" : null)
                .setHeader("Tue");
        colWednesday = grid.addColumn(TasksForWeek::getDowCountWednesday)
                .setTextAlign(ColumnTextAlign.END)
                .setClassNameGenerator(item -> item.getDowCountWednesday() == recordTasksForWeek.getDowCountWednesday() ? "record" : null)
                .setHeader("Wed");
        colThursday = grid.addColumn(TasksForWeek::getDowCountThursday)
                .setTextAlign(ColumnTextAlign.END)
                .setClassNameGenerator(item -> item.getDowCountThursday() == recordTasksForWeek.getDowCountThursday() ? "record" : null)
                .setHeader("Thu");
        colFriday = grid.addColumn(TasksForWeek::getDowCountFriday)
                .setTextAlign(ColumnTextAlign.END)
                .setClassNameGenerator(item -> item.getDowCountFriday() == recordTasksForWeek.getDowCountFriday() ? "record" : null)
                .setHeader("Fri");
        colSaturday = grid.addColumn(TasksForWeek::getDowCountSaturday)
                .setTextAlign(ColumnTextAlign.END)
                .setClassNameGenerator(item -> item.getDowCountSaturday() == recordTasksForWeek.getDowCountSaturday() ? "record" : null)
                .setHeader("Sat");
        colWeek = grid.addColumn(TasksForWeek::getWeekCount)
                .setTextAlign(ColumnTextAlign.END)
                .setClassNameGenerator(item -> item.getWeekCount() == recordTasksForWeek.getWeekCount() ? "record" : null)
                .setHeader("Total");
        //grid.setColumnReorderingAllowed(false);
        //grid.getColumns().forEach(col -> col.setAutoWidth(true));
        //grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
        hRowRecords = grid.appendHeaderRow();
        hRowAverages = grid.appendHeaderRow();

    }

    private void configureGridMonths(){
        gridMonths.removeAllColumns();
        colMonthName = gridMonths.addColumn(TasksForMonth::getMonthName)
                .setWidth("150px")
                .setHeader("Month");
        colMonthCount = gridMonths.addColumn(TasksForMonth::getMonthCount)
                .setTextAlign(ColumnTextAlign.END)
                .setWidth("150px")
                .setHeader("Count");
        gridMonths.setWidth("350px");
        hRowMonthRecord = gridMonths.appendHeaderRow();
        hRowMonthAverage = gridMonths.appendHeaderRow();
    }

    private HorizontalLayout getToolbar() {

        //get lastWeek as the default for the range picker
        LocalDate nowDate = LocalDate.now();

        LocalDate defaultDate = LocalDate.parse("2022-08-14");

        // Fetch all entities and show
        final Button fetchTasks = new Button("Refresh",
                e -> updateList()
        );
        fetchTasks.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout toolbar = new HorizontalLayout(fetchTasks, countLabel);
        toolbar.setAlignItems(FlexComponent.Alignment.BASELINE);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void updateList() {
        tasksForWeeks.clear();
        LocalDate startDate = LocalDate.parse("2022-08-14");
        LocalDate endDate = LocalDate.now();
        //log.info("updateList: start:" + startDate + " end:" + endDate);
        Long taskCount = 0L;

        //build TasksForWeek list
        recordTasksForWeek.setStartDate(LocalDate.now());
        sumTasksForWeek.setStartDate(LocalDate.now());
        averageTasksForWeek.setStartDate(LocalDate.now());
        //TODO: set the max
        TasksForWeek tasksForWeek = new TasksForWeek();
        LocalDate firstDate = startDate;
        for (LocalDate date = startDate; date.isBefore(endDate.plusDays(1)); date = date.plusDays(1)) {
            //log.info("updateList: processing date:" + date);
            tasksForWeek.setEndDate(date);
            if (date.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                //log.info("updateList: processing date:" + date + " found SUNDAY");
                //close out previous week if any
                if (!date.equals(startDate)) {
                    //log.info("updateList: processing date:" + date + " found SUNDAY that is NOT the start date");
                    tasksForWeek.setStartDate(firstDate);
                    recordTasksForWeek.addWeekIfHigher(tasksForWeek.getWeekCountLong());
                    tasksForWeeks.add(tasksForWeek);
                }
                //log.info("updateList: processing date:" + date + " found SUNDAY - creating new TasksForWeek");
                tasksForWeek = new TasksForWeek();
                firstDate = date;
            }
            Long dayCount = 0L;
            if (date.equals(endDate)) {
                //Update today directly from tookan API
                dayCount = Long.valueOf(restClientService.getTaskCount(LocalDate.now(), LocalDate.now()));
                //log.info("updateList: count for today " + endDate + " updated from Tookan API:" + dayCount);
            } else {
                //log.info("updateList: processing date:" + date + " adding...");
                Date dateForRequest = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
                dayCount = taskDetailRepository.findTaskCountByDate(dateForRequest);
            }
            tasksForWeek.add(date, dayCount);
            recordTasksForWeek.addIfHigher(date, dayCount);
            sumTasksForWeek.addToSum(date,dayCount);
            taskCount = taskCount + dayCount;
        }
        //close out previous week if any
        tasksForWeek.setStartDate(firstDate);
        recordTasksForWeek.addWeekIfHigher(tasksForWeek.getWeekCountLong());

        tasksForWeeks.add(tasksForWeek);

        averageTasksForWeek.setDowCountSunday(String.valueOf(roundUp(sumTasksForWeek.getDOWCountLongSunday(), sumTasksForWeek.getCounterSunday())));
        averageTasksForWeek.setDowCountMonday(String.valueOf(roundUp(sumTasksForWeek.getDOWCountLongMonday(), sumTasksForWeek.getCounterMonday())));
        averageTasksForWeek.setDowCountTuesday(String.valueOf(roundUp(sumTasksForWeek.getDOWCountLongTuesday(), sumTasksForWeek.getCounterTuesday())));
        averageTasksForWeek.setDowCountWednesday(String.valueOf(roundUp(sumTasksForWeek.getDOWCountLongWednesday(), sumTasksForWeek.getCounterWednesday())));
        averageTasksForWeek.setDowCountThursday(String.valueOf(roundUp(sumTasksForWeek.getDOWCountLongThursday(),sumTasksForWeek.getCounterThursday())));
        averageTasksForWeek.setDowCountFriday(String.valueOf(roundUp(sumTasksForWeek.getDOWCountLongFriday(),sumTasksForWeek.getCounterFriday())));
        averageTasksForWeek.setDowCountSaturday(String.valueOf(roundUp(sumTasksForWeek.getDOWCountLongSaturday(),sumTasksForWeek.getCounterSaturday())));
        averageTasksForWeek.setWeekCount(roundUp(sumTasksForWeek.getWeekCountLong(),sumTasksForWeek.getCounterSaturday())); //use Saturday count as if Sat is complete then so is week

        hRowRecords.getCell(colName).setText(recordTasksForWeek.getWeekName());
        hRowRecords.getCell(colSunday).setText(recordTasksForWeek.getDowCountSunday());
        hRowRecords.getCell(colMonday).setText(recordTasksForWeek.getDowCountMonday());
        hRowRecords.getCell(colTuesday).setText(recordTasksForWeek.getDowCountTuesday());
        hRowRecords.getCell(colWednesday).setText(recordTasksForWeek.getDowCountWednesday());
        hRowRecords.getCell(colThursday).setText(recordTasksForWeek.getDowCountThursday());
        hRowRecords.getCell(colFriday).setText(recordTasksForWeek.getDowCountFriday());
        hRowRecords.getCell(colSaturday).setText(recordTasksForWeek.getDowCountSaturday());
        hRowRecords.getCell(colWeek).setText(recordTasksForWeek.getWeekCount());

        hRowAverages.getCell(colName).setText(averageTasksForWeek.getWeekName());
        hRowAverages.getCell(colSunday).setText(averageTasksForWeek.getDowCountSunday());
        hRowAverages.getCell(colMonday).setText(averageTasksForWeek.getDowCountMonday());
        hRowAverages.getCell(colTuesday).setText(averageTasksForWeek.getDowCountTuesday());
        hRowAverages.getCell(colWednesday).setText(averageTasksForWeek.getDowCountWednesday());
        hRowAverages.getCell(colThursday).setText(averageTasksForWeek.getDowCountThursday());
        hRowAverages.getCell(colFriday).setText(averageTasksForWeek.getDowCountFriday());
        hRowAverages.getCell(colSaturday).setText(averageTasksForWeek.getDowCountSaturday());
        hRowAverages.getCell(colWeek).setText(averageTasksForWeek.getWeekCount());

        Collections.reverse(tasksForWeeks);
        grid.setItems(tasksForWeeks);
        grid.getDataProvider().refreshAll();

        countLabel.setText("(" + taskCount + " tasks)");

        //get all the month counts
        tasksForMonths.clear();
        Integer monthInProcess = 0;
        for (LocalDate date = startDate.withDayOfMonth(1); date.isBefore(endDate); date = date.plusMonths(1)) {
            monthInProcess++;
            Long monthCount = taskDetailRepository.findTaskCountByYearMonth(date.getYear(), date.getMonthValue());
            TasksForMonth tasksForMonth = new TasksForMonth();
            tasksForMonth.setStartDate(date);
            tasksForMonth.setMonthCount(monthCount);
            tasksForMonths.add(tasksForMonth);
            if(monthCount> recordTasksForMonth.getMonthCount()){
                recordTasksForMonth.setStartDate(date);
                recordTasksForMonth.setMonthCount(monthCount);
            }
            if(monthInProcess>1){ //skip the first month as it is a part month
                sumTasksForMonth.addToSum(date,monthCount);
            }
            //log.info("updateList: processing month:" + date + " count:" + monthCount);
        }
        //log.info("updateList: record month:" + recordTasksForMonth.getMonthName() + recordTasksForMonth.getMonthCount());
        //create the average record
        averageTasksForMonth.setMonthCount(roundUp(sumTasksForMonth.getMonthCount(),sumTasksForMonth.getMonthCounter()));

        hRowMonthRecord.getCell(colMonthName).setText(recordTasksForMonth.getMonthName());
        hRowMonthRecord.getCell(colMonthCount).setText(recordTasksForMonth.getMonthCount().toString());

        hRowMonthAverage.getCell(colMonthName).setText(averageTasksForMonth.getMonthName());
        hRowMonthAverage.getCell(colMonthCount).setText(averageTasksForMonth.getMonthCount().toString());

        //tasksForMonths.add(recordTasksForMonth);
        //tasksForMonths.add(averageTasksForMonth);

        Collections.reverse(tasksForMonths);
        /*
        for (TasksForMonth taskMonth: tasksForMonths) {
            log.info("updateList: month:" + taskMonth.getMonthName() + " count:" + taskMonth.getMonthCount());
        }

         */
        gridMonths.setItems(tasksForMonths);
        gridMonths.getDataProvider().refreshAll();
    }

    private long roundUp(long num, long divisor) {
        return (num + divisor - 1) / divisor;
    }

}
