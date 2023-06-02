package ca.admin.delivermore.data.scheduler;

import ca.admin.delivermore.collector.data.service.DriversRepository;
import ca.admin.delivermore.collector.data.tookan.Driver;
import ca.admin.delivermore.data.service.Registry;
import ca.admin.delivermore.data.service.SchedulerEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.stefan.fullcalendar.FullCalendarScheduler;
import org.vaadin.stefan.fullcalendar.ResourceEntry;
import org.vaadin.stefan.fullcalendar.SchedulerView;
import org.vaadin.stefan.fullcalendar.Timezone;
import org.vaadin.stefan.fullcalendar.dataprovider.AbstractEntryProvider;
import org.vaadin.stefan.fullcalendar.dataprovider.EntryQuery;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Stream;

import static ca.admin.delivermore.data.scheduler.Scheduler.tzDefault;

public class SchedulerEntryProvider extends AbstractEntryProvider<ResourceEntry> {

    private Logger log = LoggerFactory.getLogger(SchedulerEntryProvider.class);
    List<ResourceEntry> entryList = new ArrayList<>();
    private Map<String, SchedulerResource> resourceMap = new TreeMap<>();
    private Map<String, ResourceEntry> entriesMap = new HashMap<>();
    private Map<String, SchedulerEvent> eventsMap = new HashMap<>();

    private SchedulerView schedulerView;
    private Boolean includeAllDrivers = Boolean.FALSE;
    private Boolean allowEdit = Boolean.FALSE;
    private Long showSingleDriverId = null;  //if null show all drivers otherwise only display this driver id
    private DriversRepository driversRepository;
    private SchedulerEventRepository schedulerEventRepository;

    public SchedulerEntryProvider() {
        log.info("SchedulerEntryProvider constructor called");
        driversRepository = Registry.getBean(DriversRepository.class);
        schedulerEventRepository = Registry.getBean(SchedulerEventRepository.class);
        //buildSchedulerResources();
    }

    public void buildSchedulerResources(){
        log.info("buildSchedulerResources");
        resourceMap.clear();
        Boolean displayResource = Boolean.TRUE;

        List<Driver> driverList = new ArrayList<>();
        if(includeAllDrivers){
            driverList = driversRepository.findAll();
        }else{
            driverList = driversRepository.findActiveOrderByNameAsc();
        }

        for (Driver driver: driverList) {
            if(showSingleDriverId!=null && !driver.getFleetId().equals(showSingleDriverId)){
                displayResource = Boolean.FALSE;
            }else{
                displayResource = Boolean.TRUE;
            }
            String driverName = driver.getName();
            if(!driver.getIsActive().equals(1L)) driverName+=" (X)";
            addSchedulerResource(driver.getFleetId().toString(), "Drivers", driverName, displayResource);
        }
        addSchedulerResource(Scheduler.availableShiftsResourceId, "Available", "Available Shifts", Boolean.TRUE);

        //add resources to calendar
        FullCalendarScheduler scheduler = (FullCalendarScheduler) this.getCalendar();
        log.info("buildSchedulerResources: BEFORE removeAllResources");
        scheduler.removeAllResources();
        log.info("buildSchedulerResources: AFTER removeAllResources");
        for (SchedulerResource schedulerResource: resourceMap.values()) {
            if(schedulerResource.getDisplay()){
                log.info("buildSchedulerResources: BEFORE addResource");
                scheduler.addResource(schedulerResource.getResource());
                log.info("buildSchedulerResources: AFTER addResource");
            }
        }

    }

    private void addSchedulerResource(String id, String group, String title, Boolean displayResource){
        resourceMap.put(id, new SchedulerResource(id,group,title, displayResource));
    }

    @Override
    public Stream fetchAll() {
        log.info("fetchAll");
        return super.fetchAll();
    }

    @Override
    public Stream fetch(LocalDateTime start, LocalDateTime end) {
        log.info("fetch LocalDateTime start:" + start + " end:" + end);
        return super.fetch(start, end);
    }

    @Override
    public Stream fetch(Instant start, Instant end) {
        log.info("fetch instant start:" + start + " end:" + end);
        return super.fetch(start, end);
    }

    @Override
    public Stream fetch(@lombok.NonNull EntryQuery entryQuery) {
        if(entryQuery.getStart()!=null && tzDefault.applyTimezoneOffset(entryQuery.getStart()).getHour()==0){
            log.info("fetch query: start:" + tzDefault.applyTimezoneOffset(entryQuery.getStart()) + " end:" + tzDefault.applyTimezoneOffset(entryQuery.getEnd()) + " AllDay:" + entryQuery.getAllDay() + " isAttached:" + this.getCalendar().isAttached());
            List<SchedulerEvent> schedulerEvents = schedulerEventRepository.findByStartBetween(tzDefault.applyTimezoneOffset(entryQuery.getStart()),tzDefault.applyTimezoneOffset(entryQuery.getEnd()));
            entriesMap.clear();
            eventsMap.clear();
            for (SchedulerEvent schedulerEvent: schedulerEvents) {
                if(showSingleDriverId!=null && !schedulerEvent.getPublished()){
                    log.info("fetch query: skipping non published entry:" + schedulerEvent);
                }else{
                    log.info("addEntry: type:" + schedulerEvent.getType() + " start:" + schedulerEvent.getStart() + " fullDay:" + schedulerEvent.getFullDay() + " view:" + schedulerView);
                    ResourceEntry newEntry = schedulerEvent.getResourceEntry(resourceMap, getAllowEdit(), schedulerView);
                    entriesMap.put(newEntry.getId(),newEntry);
                    eventsMap.put(newEntry.getId(), schedulerEvent);
                }
            }
        }else{
            entriesMap.clear();
        }
        return entryQuery.applyFilter(entriesMap.values().stream());
    }

    @Override
    public Optional fetchById(@lombok.NonNull String s) {
        log.info("fetchById id:" + s);
        return Optional.ofNullable(entriesMap.get(s));
    }

    @Override
    public boolean isInMemory() {
        return super.isInMemory();
    }

    @Override
    public void refreshAll() {
        log.info("refreshAll called");
        super.refreshAll();
        log.info("refreshAll After Super");
        //buildSchedulerResources();
        //log.info("refreshAll After build");
    }

    public Map<String, SchedulerResource> getResourceMap() {
        return resourceMap;
    }

    public void setResourceMap(Map<String, SchedulerResource> resourceMap) {
        this.resourceMap = resourceMap;
    }

    public SchedulerView getSchedulerView() {
        return schedulerView;
    }

    public void setSchedulerView(SchedulerView schedulerView) {
        this.schedulerView = schedulerView;
    }

    public Long getShowSingleDriverId() {
        return showSingleDriverId;
    }

    public void setShowSingleDriverId(Long showSingleDriverId) {
        this.showSingleDriverId = showSingleDriverId;
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

    public void getEntries() {
        entriesMap.clear();
        addEntry(Scheduler.EventType.SHIFT,LocalDateTime.parse("2023-02-20T11:30"),LocalDateTime.parse("2023-02-20T13:30"),"1450067", true, true);
        addEntry(Scheduler.EventType.SHIFT,LocalDateTime.parse("2023-02-20T16:30"),LocalDateTime.parse("2023-02-20T20:30"),"1450067", false, true);
        addEntry(Scheduler.EventType.SHIFT,LocalDateTime.parse("2023-02-20T12:00"),LocalDateTime.parse("2023-02-20T14:00"),"1450141", true, true);
        addEntry(Scheduler.EventType.OFF,LocalDateTime.parse("2023-02-23T10:00"),LocalDateTime.parse("2023-02-23T22:00"),"1450141", true, true, true);
        addEntry(Scheduler.EventType.OFF,LocalDateTime.parse("2023-02-21T11:30"),LocalDateTime.parse("2023-02-21T13:30"),"1450141", false, true, false);
        addEntry(Scheduler.EventType.UNAVAILABLE,LocalDateTime.parse("2023-02-22T11:30"),LocalDateTime.parse("2023-02-22T13:30"),"1492801", true, false);
        addEntry(Scheduler.EventType.UNAVAILABLE,LocalDateTime.parse("2023-02-22T10:00"),LocalDateTime.parse("2023-02-22T22:00"),"1482952", false, false, true);

        /*
        createTimedEntry( null, LocalDateTime.parse("2023-02-14T11:30"), 120, "#4cdd66", "1");
        createTimedEntry( null, LocalDateTime.parse("2023-02-14T16:30"), 240, "#4cdd66", "1", false);
        createTimedEntry( null, LocalDateTime.parse("2023-02-15T10:00"), 240, null, "2");
        createTimedEntry( "off", LocalDateTime.parse("2023-02-16T00:00"), 1440, "dodgerblue", "3");

         */

        //TODO:: just testing this
        /*
        RecurrenceRule rule = null;
        try {
            rule = new RecurrenceRule("FREQ=YEARLY;BYMONTHDAY=23;BYMONTH=5");
        } catch (InvalidRecurrenceRuleException e) {
            throw new RuntimeException(e);
        }

        DateTime firstInstance = new DateTime(2010, 4 ,23);

        Integer counter = 0;
        for (DateTime instance:new RecurrenceSet(firstInstance, new RuleInstances(rule))) {
            counter++;
            log.info("getEntries: testing recurrence:" + instance);
            if(counter>10) break;
            // do something with instance
        }
         */

    }

    private void addEntry(Scheduler.EventType eventType, LocalDateTime start, LocalDateTime end, String resourceId, Boolean published, Boolean editable){
        addEntry(eventType,start,end,resourceId,published,editable,false);
    }

    private void addEntry(Scheduler.EventType eventType, LocalDateTime start, LocalDateTime end, String resourceId, Boolean published, Boolean editable, Boolean fullDay){
        SchedulerEvent newEvent = new SchedulerEvent(eventType,start,end,resourceId, fullDay,published);
        log.info("addEntry: type:" + newEvent.getType() + " start:" + newEvent.getStart() + " fullDay:" + fullDay);
        ResourceEntry newEntry = newEvent.getResourceEntry(resourceMap, editable, schedulerView);
        entriesMap.put(newEntry.getId(),newEntry);
        eventsMap.put(newEntry.getId(), newEvent);
    }

    public void createTimedEntry(String title, LocalDateTime start, int minutes, String color, String resourceID) {
        createTimedEntry(title,start,minutes,color,resourceID, true);
    }

    public void createTimedEntry(String title, LocalDateTime start, int minutes, String color, String resourceID, Boolean editable) {
        ResourceEntry entry = new ResourceEntry();
        LocalDateTime utcStart = convertToUtc(start);
        entry.setStart(utcStart);
        entry.setEnd(entry.getStart().plus(minutes, ChronoUnit.MINUTES));
        entry.setColor(color);
        setEntryTitle(entry,title);
        entry.setCustomProperty("description", "Description of " + entry.getTitle());
        log.info("setValues: title:" + title + " start:" + entry.getStart() + " end:" + entry.getEnd());
        if (resourceID != null) {
            log.info("createTimedEntry: assigning resource:" + resourceID);
            entry.assignResource(resourceMap.get(resourceID).getResource());
        }
        entry.setEditable(editable);
        entry.setResourceEditable(editable);
        entry.setStartEditable(editable);
        entry.setAllDay(true);
        /*
        if (this.getCalendar() != null && this.getCalendar().getEntryProvider().isInMemory()) {
            log.info("createTimedEntry: adding entry to calendar");
            this.getCalendar().addEntry(entry);
        }

         */
        entriesMap.put(entry.getId(), entry);
    }

    private void setEntryTitle(ResourceEntry entry, String title){
        if(title==null){
            String timeTitle = "";
            timeTitle = entry.getStartWithOffset(tzDefault).format(DateTimeFormatter.ofPattern("h:mma"));
            timeTitle += " - " + entry.getEndWithOffset(tzDefault).format(DateTimeFormatter.ofPattern("h:mma"));
            entry.setTitle(timeTitle);
        }else{
            entry.setTitle(title);
        }
    }

    private LocalDateTime convertToUtc(LocalDateTime time) {
        return time.atZone(ZoneId.of("America/Edmonton")).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }

    public SchedulerEvent getSchedulerEventById(String id){
        if(eventsMap.containsKey(id)){
            return eventsMap.get(id);
        }else{
            return null;
        }
    }

}
