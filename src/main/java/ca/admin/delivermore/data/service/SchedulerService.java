package ca.admin.delivermore.data.service;

import ca.admin.delivermore.collector.data.service.DriversRepository;
import ca.admin.delivermore.collector.data.tookan.Driver;
import ca.admin.delivermore.data.scheduler.Scheduler;
import ca.admin.delivermore.data.scheduler.SchedulerEvent;
import ca.admin.delivermore.data.scheduler.SchedulerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.stefan.fullcalendar.CalendarView;
import org.vaadin.stefan.fullcalendar.FullCalendarScheduler;
import org.vaadin.stefan.fullcalendar.ResourceEntry;
import org.vaadin.stefan.fullcalendar.dataprovider.EntryProvider;
import org.vaadin.stefan.fullcalendar.dataprovider.InMemoryEntryProvider;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SchedulerService {
    private Logger log = LoggerFactory.getLogger(SchedulerService.class);
    private Map<String, SchedulerResource> resourceMap = new TreeMap<>();
    private Map<String, ResourceEntry> entriesMap = new HashMap<>();
    private Map<String, SchedulerEvent> eventsMap = new HashMap<>();

    private CalendarView schedulerView;
    private Boolean includeAllDrivers = Boolean.FALSE;
    private Boolean allowEdit = Boolean.FALSE;
    private Long showSingleDriverId = null;  //if null show all drivers otherwise only display this driver id
    private DriversRepository driversRepository;
    private SchedulerEventRepository schedulerEventRepository;

    public SchedulerService() {
        //log.info("SchedulerService constructor");
        driversRepository = Registry.getBean(DriversRepository.class);
        schedulerEventRepository = Registry.getBean(SchedulerEventRepository.class);
    }

    private void loadFromDatabase(LocalDate startDate, LocalDate endDate){
        //load entries from database
        //log.info("loadFromDatabase: start:" + startDate + " end:" + endDate);
        List<SchedulerEvent> schedulerEvents;
        if(startDate!=null && endDate!=null){
            schedulerEvents = schedulerEventRepository.findByStartBetween(startDate.atStartOfDay(),endDate.atTime(23,59));
        }else{
            schedulerEvents = schedulerEventRepository.findAll();
        }

        entriesMap.clear();
        eventsMap.clear();
        for (SchedulerEvent schedulerEvent: schedulerEvents) {
            //filter out unpublished events other than unpublished OFF events as drivers create these as unpublished so they are NOT approved yet
            if(showSingleDriverId!=null && !schedulerEvent.getPublished() && !schedulerEvent.getType().equals(Scheduler.EventType.OFF)){
                //log.info("loadFromDatabase: skipping non published entry:" + schedulerEvent);
            }else{
                //log.info("loadFromDatabase: addEntry: type:" + schedulerEvent.getType() + " start:" + schedulerEvent.getStart() + " fullDay:" + schedulerEvent.getFullDay() + " view:" + schedulerView);
                ResourceEntry newEntry = schedulerEvent.getResourceEntry(resourceMap, getAllowEdit(), schedulerView);
                entriesMap.put(newEntry.getId(),newEntry);
                eventsMap.put(newEntry.getId(), schedulerEvent);
            }
        }
        //log.info("loadFromDatabase: count:" + entriesMap.values().size());
    }

    public void buildSchedulerResources(FullCalendarScheduler scheduler, LocalDate startDate, LocalDate endDate){
        //log.info("buildSchedulerResources");
        //NOTE: the endDate is INCLUSIVE so it is actually the day PAST the last day of the week
        resourceMap.clear();
        Boolean displayResource = Boolean.TRUE;

        List<Driver> driverList = new ArrayList<>();
        if(includeAllDrivers){
            driverList = driversRepository.findAll();
        }else{
            driverList = driversRepository.findActiveOrderByNameAsc();
        }

        for (Driver driver: driverList) {
            displayResource = Boolean.TRUE;
            /*
            if(showSingleDriverId!=null && !driver.getFleetId().equals(showSingleDriverId)){
                displayResource = Boolean.FALSE;
            }else{
                displayResource = Boolean.TRUE;
            }

             */
            String driverName = driver.getName();
            //log.info("buildSchedulerResources: for driver " + driverName + " fleetId:" + driver.getFleetId() + " start:" + startDate + " end:" + endDate);
            if(endDate!=null){
                List<SchedulerEvent> resourceEvents = schedulerEventRepository.findByResourceIdAndStartBetween(driver.getFleetId().toString(),startDate.atStartOfDay(),endDate.atStartOfDay());
                if(resourceEvents==null || resourceEvents.size()==0){
                    //log.info("buildSchedulerResources: resourceEvents was null or size was 0 - no hours for driver " + driverName);
                }else{
                    //add up the hours for this resource
                    Long hoursAsMillis = 0L;
                    for (SchedulerEvent schedulerEvent : resourceEvents) {
                        if(showSingleDriverId!=null && !schedulerEvent.getPublished()){
                            //log.info("buildSchedulerResources: skipping non published entry:" + schedulerEvent);
                        }else{
                            //only add up SHIFT type entries
                            if(schedulerEvent.getType().equals(Scheduler.EventType.SHIFT)){
                                log.info("buildSchedulerResources: hours between " + schedulerEvent.getStart() + " and " + schedulerEvent.getEnd() + " = " + getHoursFormatted(getHoursBetween(schedulerEvent.getStart(), schedulerEvent.getEnd())));
                                hoursAsMillis += getHoursBetween(schedulerEvent.getStart(), schedulerEvent.getEnd());
                            }
                        }
                    }
                    if(hoursAsMillis>0L){
                        driverName += " (" + getHoursFormatted(hoursAsMillis) + ")";
                        log.info("buildSchedulerResources: total hours for driver " + driverName + " = " + getHoursFormatted(hoursAsMillis));
                    }
                }
            }

            if(!driver.getIsActive().equals(1L)) driverName+=" (X)";
            addSchedulerResource(driver.getFleetId().toString(), "Drivers", driverName, displayResource);
        }
        addSchedulerResource(Scheduler.availableShiftsResourceId, "Available", "Available Shifts", Boolean.TRUE);

        //add resources to calendar
        //FullCalendarScheduler scheduler = (FullCalendarScheduler) this.getCalendar();
        scheduler.removeAllResources();
        for (SchedulerResource schedulerResource: resourceMap.values()) {
            if(schedulerResource.getDisplay()){
                scheduler.addResource(schedulerResource.getResource());
            }
        }

    }

    private Long getHoursBetween(LocalDateTime start, LocalDateTime end){
        Duration dur = Duration.between(start, end);
        //log.info("**** getHoursBetween: toHours:" + dur.toHours() + " toHoursPart:" + dur.toHoursPart());
        return dur.toMillis();
    }

    private String getHoursFormatted(Long millis){
        //TODO: adjust to minimize the room it takes... 12.5 hours is better than 12:30
        Long hours = TimeUnit.MILLISECONDS.toHours(millis);
        Long minutes = (TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)));
        String result = hours.toString();
        if(minutes > 0L) result += ".5";
        return result;
        /*
        log.info("***** getHoursFormatted: toHours:" + TimeUnit.MILLISECONDS.toHours(millis));
        log.info("***** getHoursFormatted: toMinutes:" + (60/(TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)))));
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)));

         */
    }

    private void addSchedulerResource(String id, String group, String title, Boolean displayResource){
        resourceMap.put(id, new SchedulerResource(id,group,title, displayResource));
    }

    public void refresh(FullCalendarScheduler scheduler, LocalDate startDate, LocalDate endDate){
        buildSchedulerResources(scheduler,startDate,endDate);
        //read list from database
        loadFromDatabase(startDate,endDate);
        InMemoryEntryProvider<ResourceEntry> entryProvider2 = EntryProvider.inMemoryFrom(entriesMap.values().stream().collect(Collectors.toList()));
        scheduler.setEntryProvider(entryProvider2);
        entryProvider2.refreshAll();
    }

    public CalendarView getSchedulerView() {
        return schedulerView;
    }

    public void setSchedulerView(CalendarView schedulerView) {
        this.schedulerView = schedulerView;
    }

    public Boolean getIncludeAllDrivers() {
        return includeAllDrivers;
    }

    public void setIncludeAllDrivers(Boolean includeAllDrivers) {
        this.includeAllDrivers = includeAllDrivers;
    }

    public Boolean getAllowEdit() {
        return allowEdit;
    }

    public void setAllowEdit(Boolean allowEdit) {
        this.allowEdit = allowEdit;
    }

    public Long getShowSingleDriverId() {
        return showSingleDriverId;
    }

    public void setShowSingleDriverId(Long showSingleDriverId) {
        this.showSingleDriverId = showSingleDriverId;
    }

    public SchedulerEvent getSchedulerEventById(String id){
        if(eventsMap.containsKey(id)){
            return eventsMap.get(id);
        }else{
            return null;
        }
    }

    public Map<String, ResourceEntry> getEntriesMap() {
        return entriesMap;
    }

    public List<SchedulerEvent> getUnpublishedEntries(LocalDate start, LocalDate end){
        return schedulerEventRepository.findByPublishedAndStartBetween(false,start.atStartOfDay(), end.atTime(23,59));
    }

    public void saveEntry(SchedulerEvent schedulerEvent){
        schedulerEventRepository.save(schedulerEvent);
    }

}
