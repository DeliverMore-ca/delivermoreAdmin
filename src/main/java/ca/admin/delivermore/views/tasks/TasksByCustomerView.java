package ca.admin.delivermore.views.tasks;

import ca.admin.delivermore.collector.data.reportitem.CustomerTasks;
import ca.admin.delivermore.collector.data.service.TaskDetailRepository;
import ca.admin.delivermore.views.MainLayout;
import com.vaadin.componentfactory.DateRange;
import com.vaadin.componentfactory.EnhancedDateRangePicker;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@PageTitle("Tasks by Customer")
@Route(value = "customertasks", layout = MainLayout.class)
@RolesAllowed({"ADMIN","MANAGER"})
public class TasksByCustomerView extends VerticalLayout {
    private TaskDetailRepository taskDetailRepository;
    private List<CustomerTasks> customerTasksList = new ArrayList<>();
    private Grid<CustomerTasks> grid = new Grid<>();
    private GridListDataView<CustomerTasks> dataView;
    private Label countLabel = new Label();
    private Checkbox onlyGlobal = new Checkbox();

    private TextField searchField = new TextField();
    private EnhancedDateRangePicker rangeDatePicker = new EnhancedDateRangePicker("Select range:");

    private Logger log = LoggerFactory.getLogger(TasksByCustomerView.class);
    public TasksByCustomerView(TaskDetailRepository taskDetailRepository) {
        this.taskDetailRepository = taskDetailRepository;

        //TODO: add export to CSV button to toolbar
        //TODO: add filter to include phone ins

        configureGrid();
        configureSearch();
        // layout configuration
        setSizeFull();
        add(getToolbar(),searchField, grid);

        updateList();

    }

    private void configureSearch(){
        searchField.setWidth("50%");
        searchField.setPlaceholder("Search");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(e -> dataView.refreshAll());
        searchField.setClearButtonVisible(true);

    }

    private void configureFilter(){
        dataView.addFilter(customer -> {
            String searchTerm = searchField.getValue().trim();

            if (searchTerm.isEmpty())
                return true;

            boolean matchesFullName = matchesTerm(customer.getName(),
                    searchTerm);
            boolean matchesEmail = matchesTerm(customer.getEmail(), searchTerm);

            return matchesFullName || matchesEmail;
        });

    }

    private void configureGrid(){
        grid.removeAllColumns();
        grid.addColumn(CustomerTasks::getEmail)
                .setSortable(true)
                .setHeader("Email");
        grid.addColumn(CustomerTasks::getName)
                .setSortable(true)
                .setHeader("Name");
        grid.addColumn(CustomerTasks::getCount)
                .setSortable(true)
                .setHeader("Count");
        grid.setColumnReorderingAllowed(true);
        //grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

    }

    private boolean matchesTerm(String value, String searchTerm) {
        return value.toLowerCase().contains(searchTerm.toLowerCase());
    }

    private HorizontalLayout getToolbar() {

        //get lastWeek as the default for the range picker
        LocalDate nowDate = LocalDate.now();
        LocalDate prevSun = nowDate.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
        prevSun = nowDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate startOfLastWeek = prevSun.minusWeeks(1);
        LocalDate endOfLastWeek = startOfLastWeek.plusDays(6);

        LocalDate defaultDate = LocalDate.parse("2022-08-14");
        rangeDatePicker.setMin(defaultDate);
        rangeDatePicker.setValue(new DateRange(startOfLastWeek,endOfLastWeek));

        onlyGlobal.setValue(Boolean.TRUE);
        onlyGlobal.setLabel("Only Global");

        // Fetch all entities and show
        final Button fetchTasks = new Button("Fetch tasks",
                e -> updateList()
        );
        fetchTasks.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout toolbar = new HorizontalLayout(rangeDatePicker, onlyGlobal, fetchTasks, countLabel);
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
        if(onlyGlobal.getValue()){
            customerTasksList = taskDetailRepository.findCustomerTasksByCreatedByAndDates(43L,startDate.atStartOfDay(),endDate.atTime(23,59,59));
        }else{
            customerTasksList = taskDetailRepository.findCustomerTasksByDates(startDate.atStartOfDay(),endDate.atTime(23,59,59));
        }
        dataView = grid.setItems(customerTasksList);

        grid.setItems(customerTasksList);
        Long taskCount = 0L;
        for (CustomerTasks customerTasks: customerTasksList) {
            taskCount = taskCount + customerTasks.getCount();
        }
        countLabel.setText("(" + grid.getDataProvider().size(new Query<>()) + " rows " + taskCount + " tasks)");
        configureFilter();

    }

}
