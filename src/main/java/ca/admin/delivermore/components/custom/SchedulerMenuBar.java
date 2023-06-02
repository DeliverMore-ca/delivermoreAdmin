package ca.admin.delivermore.components.custom;

import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.stefan.fullcalendar.CalendarView;
import org.vaadin.stefan.fullcalendar.CalendarViewImpl;
import org.vaadin.stefan.fullcalendar.FullCalendar;
import org.vaadin.stefan.fullcalendar.SchedulerView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class SchedulerMenuBar extends MenuBar {

    private Logger log = LoggerFactory.getLogger(SchedulerMenuBar.class);
    private final FullCalendar scheduler;
    private Button buttonDatePicker;
    private MenuItem viewSelector;
    private MenuItem viewSettings;
    private MenuItem settingsIncludeAllDrivers;
    private SchedulerView selectedView;

    public SchedulerMenuBar(FullCalendar scheduler, SchedulerView selectedView) {
        this.scheduler = scheduler;
        this.selectedView = selectedView;
        buildDateSelectors();
        Icon icon = new Icon(VaadinIcon.COG);
        viewSettings = addItem(icon, "Settings");

        buildCalendarViewField();

        settingsIncludeAllDrivers = viewSettings.getSubMenu().addItem("Show all drivers");
        settingsIncludeAllDrivers.setCheckable(true);
        settingsIncludeAllDrivers.setChecked(false);
        settingsIncludeAllDrivers.addClickListener(e -> {
            log.info("ClickListener IncludeAllDrivers");
            scheduler.changeView(selectedView);
        });

        addThemeVariants(MenuBarVariant.LUMO_SMALL);
    }

    private void buildCalendarViewField(){
        List<SchedulerView> calendarViews = new ArrayList<>();
        calendarViews.add(SchedulerView.RESOURCE_TIMELINE_DAY);
        calendarViews.add(SchedulerView.RESOURCE_TIMELINE_WEEK);
        calendarViews.add(SchedulerView.RESOURCE_TIMELINE_MONTH);
        calendarViews.add(SchedulerView.RESOURCE_TIME_GRID_DAY);
        calendarViews.add(SchedulerView.RESOURCE_TIME_GRID_WEEK);
        //calendarViews = new ArrayList<>(Arrays.asList(SchedulerView.values()));
        calendarViews.sort(Comparator.comparing(CalendarView::getName));
        viewSelector = viewSettings.getSubMenu().addItem("View: " + getViewName(selectedView));
        SubMenu subMenu = viewSelector.getSubMenu();
        calendarViews.stream()
                .sorted(Comparator.comparing(this::getViewName))
                .forEach(view -> {
                    String viewName = getViewName(view);
                    MenuItem menuItem = subMenu.addItem(viewName, event -> {
                        scheduler.changeView(view);
                        viewSelector.setText("View: " + viewName);
                        selectedView = view;
                        for (MenuItem item: viewSelector.getSubMenu().getItems()) {
                            item.setChecked(false);
                        }
                        event.getSource().setChecked(true);
                        //scheduler.changeView(selectedView);
                    });
                    menuItem.setCheckable(true);
                    if(view.equals(selectedView)){
                        menuItem.setChecked(true);
                    }else{
                        menuItem.setChecked(false);
                    }
                });
    }

    private String getViewName(CalendarView view) {
        String name = null /*customViewNames.get(view)*/;
        if (name == null) {
            name = StringUtils.capitalize(String.join(" ", StringUtils.splitByCharacterTypeCamelCase(view.getClientSideValue())));
        }

        return name;
    }

    private void buildDateSelectors(){
        addItem(VaadinIcon.ANGLE_LEFT.create(),"Previous", e -> scheduler.previous());

        // simulate the date picker light that we can use in polymer
        DatePicker gotoDate = new DatePicker();
        gotoDate.addValueChangeListener(event1 -> scheduler.gotoDate(event1.getValue()));
        gotoDate.getElement().getStyle().set("visibility", "hidden");
        gotoDate.getElement().getStyle().set("position", "fixed");
        gotoDate.setWidth("0px");
        gotoDate.setHeight("0px");
        gotoDate.setWeekNumbersVisible(true);
        buttonDatePicker = new Button(VaadinIcon.CALENDAR.create());
        buttonDatePicker.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        buttonDatePicker.getElement().appendChild(gotoDate.getElement());
        buttonDatePicker.addClickListener(event -> gotoDate.open());
        buttonDatePicker.setWidthFull();
        addItem(buttonDatePicker,"Go to selected date");
        addItem(VaadinIcon.ANGLE_RIGHT.create(),"Next", e -> scheduler.next());
        addItem("Today","Go to today's date", e -> scheduler.today());
    }

    public void updateInterval(LocalDate intervalStart) {
        log.info("updateInterval: selectedView:" + selectedView + " scheduler:" + scheduler.toString());
        if (buttonDatePicker != null && selectedView != null) {
            updateIntervalLabel(buttonDatePicker, selectedView, intervalStart);
        }
    }

    void updateIntervalLabel(HasText intervalLabel, CalendarView view, LocalDate intervalStart) {
        String text = "--";
        Locale locale = scheduler.getLocale();

        if (view instanceof CalendarViewImpl) {
            switch ((CalendarViewImpl) view) {
                default:
                case DAY_GRID_MONTH:
                case LIST_MONTH:
                    text = intervalStart.format(DateTimeFormatter.ofPattern("MMMM yyyy").withLocale(locale));
                    break;
                case TIME_GRID_DAY:
                case DAY_GRID_DAY:
                case LIST_DAY:
                    text = intervalStart.format(DateTimeFormatter.ofPattern("dd.MM.yyyy").withLocale(locale));
                    break;
                case TIME_GRID_WEEK:
                case DAY_GRID_WEEK:
                case LIST_WEEK:
                    text = intervalStart.format(DateTimeFormatter.ofPattern("dd.MM.yy").withLocale(locale)) + " - " + intervalStart.plusDays(6).format(DateTimeFormatter.ofPattern("dd.MM.yy").withLocale(locale)) + " (cw " + intervalStart.format(DateTimeFormatter.ofPattern("ww").withLocale(locale)) + ")";
                    break;
                case LIST_YEAR:
                    text = intervalStart.format(DateTimeFormatter.ofPattern("yyyy").withLocale(locale));
                    break;
            }
        } else if (view instanceof SchedulerView) {
            switch ((SchedulerView) view) {
                case TIMELINE_DAY:
                case RESOURCE_TIMELINE_DAY:
                case RESOURCE_TIME_GRID_DAY:
                    text = intervalStart.format(DateTimeFormatter.ofPattern("MMM dd yyyy").withLocale(locale));
                    break;
                case TIMELINE_WEEK:
                case RESOURCE_TIMELINE_WEEK:
                case RESOURCE_TIME_GRID_WEEK:
                    text = intervalStart.format(DateTimeFormatter.ofPattern("M/d/yy").withLocale(locale)) + " - " + intervalStart.plusDays(6).format(DateTimeFormatter.ofPattern("M/d/yy").withLocale(locale));
                    break;
                case TIMELINE_MONTH:
                case RESOURCE_TIMELINE_MONTH:
                    text = intervalStart.format(DateTimeFormatter.ofPattern("MMMM yyyy").withLocale(locale));
                    break;
                case TIMELINE_YEAR:
                case RESOURCE_TIMELINE_YEAR:
                    text = intervalStart.format(DateTimeFormatter.ofPattern("yyyy").withLocale(locale));
                    break;
            }
        } else {
            String pattern = view != null && view.getDateTimeFormatPattern() != null ? view.getDateTimeFormatPattern() : "MMMM yyyy";
            text = intervalStart.format(DateTimeFormatter.ofPattern(pattern).withLocale(locale));

        }

        intervalLabel.setText(text);
    }

    public SchedulerView getSelectedView() {
        return selectedView;
    }

    public void setSelectedView(SchedulerView selectedView) {
        this.selectedView = selectedView;
    }

    public Boolean getShowAllDrivers(){
        return settingsIncludeAllDrivers.isChecked();
    }
}
