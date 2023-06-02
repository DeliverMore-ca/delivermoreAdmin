package ca.admin.delivermore.data.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.stefan.fullcalendar.ResourceEntry;
import org.vaadin.stefan.fullcalendar.SchedulerView;
import org.vaadin.stefan.fullcalendar.Timezone;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Entity
public class SchedulerEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Transient
    private Logger log = LoggerFactory.getLogger(SchedulerEvent.class);

    @Transient
    private Timezone tzDefault = new Timezone(ZoneId.of("America/Edmonton"));

    @Transient
    private ResourceEntry entry;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Scheduler.EventType type = Scheduler.EventType.SHIFT;

    @NotNull
    private LocalDateTime start;

    @NotNull
    private LocalDateTime end;

    @NotNull
    private String resourceId;

    @NotNull
    private Boolean published = false;

    @NotNull
    private Boolean fullDay = false;

    @Transient
    private String description = "";


    public SchedulerEvent() {
    }

    public SchedulerEvent(Scheduler.EventType type, LocalDateTime start, LocalDateTime end, String resourceId, Boolean fullDay, Boolean published) {
        this.type = type;
        this.start = start;
        this.end = end;
        this.resourceId = resourceId;
        this.fullDay = fullDay;
        this.published = published;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Scheduler.EventType getType() {
        return type;
    }

    public void setType(Scheduler.EventType type) {
        this.type = type;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public Boolean getFullDay() {
        return fullDay;
    }

    public void setFullDay(Boolean fullDay) {
        this.fullDay = fullDay;
    }

    public ResourceEntry getResourceEntry(Map<String, SchedulerResource> resourceMap, SchedulerView schedulerView){
        return getResourceEntry(resourceMap,false, schedulerView);
    }

    public ResourceEntry getResourceEntry(Map<String, SchedulerResource> resourceMap, Boolean editable, SchedulerView schedulerView){
        if(id==null){
            entry = new ResourceEntry();
        }else{
            entry = new ResourceEntry(id.toString());
        }
        //TODO: overlap needs custom function to allow user to decide to allow some conflicts
        //entry.setOverlapAllowed(false);

        //handle fullDay events
        LocalDateTime utcStart = convertToUtc(this.start);
        LocalDateTime utcEnd = convertToUtc(this.end);
        if(fullDay){
            entry.setStart(start);
            entry.setEnd(end);
        }else{
            entry.setStart(utcStart);
            entry.setEnd(utcEnd);
        }

        entry.setEditable(editable);
        entry.setResourceEditable(editable);
        entry.setStartEditable(editable);

        //assign to resource
        String resourceName = "Unassigned";
        log.info("getResourceEntry: assigning resource:" + this.resourceId);
        if(resourceMap.containsKey(this.resourceId)){
            entry.assignResource(resourceMap.get(this.resourceId).getResource());
            resourceName = resourceMap.get(this.resourceId).getTitle();
        }else{
            //resource is not in resourceMap so assign to available resource
            if(resourceMap.containsKey(Scheduler.availableShiftsResourceId)){
                entry.assignResource(resourceMap.get(Scheduler.availableShiftsResourceId).getResource());
            }else{
                //failed to assign event to resource
                log.info("getResourceEntry: could not assign resourceId '" + this.resourceId + "' to this event.  Id not found in resources.");
                return null;
            }
        }

        Boolean forceAllDay = Boolean.TRUE;
        if(schedulerView.equals(SchedulerView.RESOURCE_TIMELINE_WEEK) || schedulerView.equals(SchedulerView.RESOURCE_TIMELINE_MONTH)){
            forceAllDay = Boolean.TRUE;
        }else{
            forceAllDay = Boolean.FALSE;
        }

        String colorForEvent = "";
        if(this.type.equals(Scheduler.EventType.UNAVAILABLE)){
            colorForEvent = Scheduler.EventColor.UNAVAILABLE.color;
            setDaySettings(entry,"Not Available", "NA", forceAllDay);
            //entry.markAsDirty();
        }else if(this.type.equals(Scheduler.EventType.OFF)){
            colorForEvent = Scheduler.EventColor.OFF.color;
            setDaySettings(entry,"Off", "OFF", forceAllDay);
            //entry.markAsDirty();
        }else{  //all others will be treated as SHIFT
            colorForEvent = Scheduler.EventColor.SHIFT.color;
            if(forceAllDay || schedulerView.equals(SchedulerView.RESOURCE_TIMELINE_DAY)){
                entry.setTitle(getTimeFormatted(entry));
            }else{
                entry.setTitle("Shift");
            }
            description = "Shift:" + getDateTimeFormatted(entry);
            entry.setAllDay(forceAllDay);
            //entry.markAsDirty();
        }
        log.info("getResourceEntry: title:" + entry.getTitle() + " forceAllDay:" + forceAllDay + " fullDay:" + fullDay + " allDay:" + entry.isAllDay() + " start:" + entry.getStart() + " end:" + entry.getEnd());


        if(this.published){
            entry.setColor(colorForEvent);
        }else{
            //entry.setColor(null);
            entry.setBorderColor(colorForEvent);
            entry.setColor(Scheduler.defaultEventColor);
            entry.setTextColor("var(--lumo-body-text-color)");
        }

        String descText = resourceName + "<br>" + description;
        if(!published){
            descText += "<br><strong>Not published</strong>";
        }
        entry.setCustomProperty("description", descText);
        return entry;
    }

    public String getDescription(){
        return entry.getDescription();
    }

    private void setDaySettings(ResourceEntry entry, String longName, String shortName, Boolean forceAllDay){
        if(forceAllDay){
            if(fullDay){
                entry.setTitle(longName);
            }else{
                entry.setTitle(shortName + ":" + getTimeFormatted(entry));
            }
            entry.setAllDay(forceAllDay);
        }else{
            if(fullDay){
                entry.setTitle(longName);
            }else{
                entry.setTitle(shortName + ":" + getTimeFormatted(entry));
            }
            entry.setAllDay(fullDay);
        }
        if(fullDay){
            description = longName + "<br>" + getDateFormatted(entry);
        }else{
            description = longName + "<br>" + getDateTimeFormatted(entry);
        }
    }

    private String getDateTimeFormatted(ResourceEntry entry){
        String timeTitle = "";
        timeTitle = entry.getStartWithOffset(tzDefault).format(DateTimeFormatter.ofPattern("MMM dd yyyy h:mm"));
        timeTitle += " - " + entry.getEndWithOffset(tzDefault).format(DateTimeFormatter.ofPattern("h:mm"));
        return timeTitle;
    }
    private String getDateFormatted(ResourceEntry entry){
        String timeTitle = "";
        timeTitle = entry.getStartWithOffset(tzDefault).format(DateTimeFormatter.ofPattern("MMM dd yyyy"));
        return timeTitle;
    }
    private String getTimeFormatted(ResourceEntry entry){
        String timeTitle = "";
        timeTitle = entry.getStartWithOffset(tzDefault).format(DateTimeFormatter.ofPattern("h:mm"));
        timeTitle += " - " + entry.getEndWithOffset(tzDefault).format(DateTimeFormatter.ofPattern("h:mm"));
        return timeTitle;
    }
    private LocalDateTime convertToUtc(LocalDateTime time) {
        //return time.atZone(ZoneId.of("America/Edmonton")).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
        return time.atZone(tzDefault.getZoneId()).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }

    public String getHours(){
        Duration dur = Duration.between(start, end);
        long millis = dur.toMillis();

        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)));
    }

    @Override
    public String toString() {
        return "SchedulerEvent{" +
                "id=" + id +
                ", type=" + type +
                ", start=" + start +
                ", end=" + end +
                ", resourceId='" + resourceId + '\'' +
                ", published=" + published +
                '}';
    }
}
