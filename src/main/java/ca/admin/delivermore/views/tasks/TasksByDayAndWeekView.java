package ca.admin.delivermore.views.tasks;

import ca.admin.delivermore.collector.data.service.RestClientService;
import ca.admin.delivermore.collector.data.service.TaskDetailRepository;
import ca.admin.delivermore.data.report.TasksForWeek;
import ca.admin.delivermore.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@PageTitle("Tasks by Day and Week")
@Route(value = "tasksbydayandweek", layout = MainLayout.class)
@AnonymousAllowed
public class TasksByDayAndWeekView extends VerticalLayout {
    private TaskDetailRepository taskDetailRepository;
    private RestClientService restClientService;
    private Grid<TasksForWeek> grid = new Grid<>();
    private Label countLabel = new Label();
    private List<TasksForWeek> tasksForWeeks = new ArrayList<>();
    private Logger log = LoggerFactory.getLogger(TasksByDayAndWeekView.class);
    private TasksForWeek recordTasksForWeek = new TasksForWeek(Boolean.TRUE);

    @Autowired
    public TasksByDayAndWeekView(TaskDetailRepository taskDetailRepository, RestClientService restClientService) {
        this.taskDetailRepository = taskDetailRepository;
        this.restClientService = restClientService;
        //addClassNames("tasksbydayandweek-view");
        configureGrid();
        // layout configuration
        setSizeFull();
        add(getToolbar(), grid);

        updateList();

    }

    private void configureGrid(){
        grid.removeAllColumns();
        //grid.setClassName("no-upload-grid-no");
        //grid.setClassName("task-record");
        grid.addColumn(TasksForWeek::getWeekName)
                .setWidth("150px")
                .setHeader("Week");
                //.getElement().getStyle().set("background-color", "yellow");
        grid.addColumn(TasksForWeek::getDowCountSunday)
                .setTextAlign(ColumnTextAlign.END)
                .setClassNameGenerator(item -> item.getDowCountSunday() == recordTasksForWeek.getDowCountSunday() ? "record" : null)
                .setHeader("Sun");
        grid.addColumn(TasksForWeek::getDowCountMonday)
                .setTextAlign(ColumnTextAlign.END)
                .setClassNameGenerator(item -> item.getDowCountMonday() == recordTasksForWeek.getDowCountMonday() ? "record" : null)
                .setHeader("Mon");
        grid.addColumn(TasksForWeek::getDowCountTuesday)
                .setTextAlign(ColumnTextAlign.END)
                .setClassNameGenerator(item -> item.getDowCountTuesday() == recordTasksForWeek.getDowCountTuesday() ? "record" : null)
                .setHeader("Tue");
        grid.addColumn(TasksForWeek::getDowCountWednesday)
                .setTextAlign(ColumnTextAlign.END)
                .setClassNameGenerator(item -> item.getDowCountWednesday() == recordTasksForWeek.getDowCountWednesday() ? "record" : null)
                .setHeader("Wed");
        grid.addColumn(TasksForWeek::getDowCountThursday)
                .setTextAlign(ColumnTextAlign.END)
                .setClassNameGenerator(item -> item.getDowCountThursday() == recordTasksForWeek.getDowCountThursday() ? "record" : null)
                .setHeader("Thu");
        grid.addColumn(TasksForWeek::getDowCountFriday)
                .setTextAlign(ColumnTextAlign.END)
                .setClassNameGenerator(item -> item.getDowCountFriday() == recordTasksForWeek.getDowCountFriday() ? "record" : null)
                .setHeader("Fri");
        grid.addColumn(TasksForWeek::getDowCountSaturday)
                .setTextAlign(ColumnTextAlign.END)
                .setClassNameGenerator(item -> item.getDowCountSaturday() == recordTasksForWeek.getDowCountSaturday() ? "record" : null)
                .setHeader("Sat");
        grid.addColumn(TasksForWeek::getWeekCount)
                .setTextAlign(ColumnTextAlign.END)
                .setClassNameGenerator(item -> item.getWeekCount() == recordTasksForWeek.getWeekCount() ? "record" : null)
                .setHeader("Total");
        //grid.setColumnReorderingAllowed(false);
        //grid.getColumns().forEach(col -> col.setAutoWidth(true));
        //grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);

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
        log.info("updateList: start:" + startDate + " end:" + endDate);
        Long taskCount = 0L;

        //build TasksForWeek list
        recordTasksForWeek.setStartDate(LocalDate.now());
        //TODO: set the max
        TasksForWeek tasksForWeek = new TasksForWeek();
        LocalDate firstDate = startDate;
        for (LocalDate date = startDate; date.isBefore(endDate.plusDays(1)); date = date.plusDays(1)){
            log.info("updateList: processing date:" + date);
            tasksForWeek.setEndDate(date);
            if(date.getDayOfWeek().equals(DayOfWeek.SUNDAY)){
                log.info("updateList: processing date:" + date + " found SUNDAY");
                //close out previous week if any
                if(!date.equals(startDate)){
                    log.info("updateList: processing date:" + date + " found SUNDAY that is NOT the start date");
                    tasksForWeek.setStartDate(firstDate);
                    recordTasksForWeek.addWeekIfHigher(tasksForWeek.getWeekCountLong());
                    tasksForWeeks.add(tasksForWeek);
                }
                log.info("updateList: processing date:" + date + " found SUNDAY - creating new TasksForWeek");
                tasksForWeek = new TasksForWeek();
                firstDate = date;
            }
            log.info("updateList: processing date:" + date + " adding...");
            Date dateForRequest = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Long dayCount = taskDetailRepository.findTaskCountByDate(dateForRequest);
            tasksForWeek.add(date,dayCount);
            recordTasksForWeek.addIfHigher(date,dayCount);
            taskCount = taskCount + dayCount;
        }
        //close out previous week if any
        tasksForWeek.setStartDate(firstDate);
        recordTasksForWeek.addWeekIfHigher(tasksForWeek.getWeekCountLong());

        //Update today directly from tookan API
        Long todayCount = Long.valueOf(restClientService.getTaskCount(LocalDate.now(), LocalDate.now()));
        tasksForWeek.add(endDate,todayCount);
        tasksForWeeks.add(tasksForWeek);
        log.info("**** count for today " + endDate + " updated from Tookan API:" + todayCount);

        tasksForWeeks.add(recordTasksForWeek);
        Collections.reverse(tasksForWeeks);
        grid.setItems(tasksForWeeks);
        grid.getDataProvider().refreshAll();

        countLabel.setText("(" + taskCount + " tasks)");

    }


}
