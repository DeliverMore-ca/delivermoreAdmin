package ca.admin.delivermore.data.scheduler;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.parameter.ParticipationLevel;
import biweekly.property.Attendee;
import biweekly.property.Method;
import biweekly.property.Organizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.stefan.fullcalendar.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
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

    @ManyToOne
    @JoinColumn(name = "event_group_id")
    private SchedulerEventGroup eventGroup;

    public SchedulerEventGroup getEventGroup() {
        return eventGroup;
    }

    public void setEventGroup(SchedulerEventGroup eventGroup) {
        this.eventGroup = eventGroup;
    }

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

    public SchedulerEvent(SchedulerEvent schedulerEvent){
        this.type = schedulerEvent.type;
        this.start = schedulerEvent.start;
        this.end = schedulerEvent.end;
        this.resourceId = schedulerEvent.resourceId;
        this.fullDay = schedulerEvent.fullDay;
        this.published = schedulerEvent.published;
        this.eventGroup = schedulerEvent.eventGroup;
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

    public ResourceEntry getResourceEntry(Map<String, SchedulerResource> resourceMap, CalendarView schedulerView){
        return getResourceEntry(resourceMap,false, schedulerView);
    }

    public ResourceEntry getResourceEntry(Map<String, SchedulerResource> resourceMap, Boolean editable, CalendarView schedulerView){
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
        String resourceName = "unassigned";
        //log.info("getResourceEntry: assigning resource:" + this.resourceId);
        if(resourceMap.containsKey(this.resourceId)){
            entry.assignResource(resourceMap.get(this.resourceId).getResource());
            resourceName = resourceMap.get(this.resourceId).getTitle();
        }else{
            //resource is not in resourceMap so assign to available shifts
            if(resourceMap.containsKey(Scheduler.availableShiftsResourceId)){
                entry.assignResource(resourceMap.get(Scheduler.availableShiftsResourceId).getResource());
                resourceName = Scheduler.availableShiftsDisplayName;
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

        Boolean useResourceNameInTitle = Boolean.FALSE;
        if(schedulerView.equals(CalendarViewImpl.LIST_WEEK)){
            useResourceNameInTitle = Boolean.TRUE;
        }

        String colorForEvent = "";
        if(this.type.equals(Scheduler.EventType.UNAVAILABLE)){
            colorForEvent = Scheduler.EventColor.UNAVAILABLE.color;
            setDaySettings(entry,"Not Available", "NA", forceAllDay, resourceName, useResourceNameInTitle);
            //entry.markAsDirty();
        }else if(this.type.equals(Scheduler.EventType.OFF)){
            colorForEvent = Scheduler.EventColor.OFF.color;
            setDaySettings(entry,"Off", "OFF", forceAllDay, resourceName, useResourceNameInTitle);
            //entry.markAsDirty();
        }else{  //all others will be treated as SHIFT
            colorForEvent = Scheduler.EventColor.SHIFT.color;
            if(forceAllDay || schedulerView.equals(SchedulerView.RESOURCE_TIMELINE_DAY)){
                entry.setTitle(getTimeFormatted(entry));
            }else{
                if(useResourceNameInTitle){
                    //in ListView item we need to add a TEXT indicator for Unpublished entries
                    if(!this.published){
                        entry.setTitle(Scheduler.unpublishedPrefix + " " + resourceName);
                    }else{
                        entry.setTitle(resourceName);
                    }
                }else{
                    entry.setTitle("Shift");
                }
            }
            description = "Shift:" + getDateTimeFormatted(entry);
            entry.setAllDay(forceAllDay);
            //entry.markAsDirty();
        }
        //log.info("getResourceEntry: title:" + entry.getTitle() + " forceAllDay:" + forceAllDay + " fullDay:" + fullDay + " allDay:" + entry.isAllDay() + " start:" + entry.getStart() + " end:" + entry.getEnd());


        if(this.published){
            entry.setColor(colorForEvent);
        }else{
            //entry.setColor(null);
            entry.setBorderColor(colorForEvent);
            entry.setColor(Scheduler.defaultEventColor);
            entry.setTextColor("var(--lumo-body-text-color)");
        }

        //Add prefix if reoccurring event
        if(this.getEventGroup()!=null){
            String originalTitle = entry.getTitle();
            entry.setTitle(Scheduler.reoccurPrefix + originalTitle);
        }

        String descText = resourceName + "<br>" + description;
        if(!published){
            descText += "<br><strong>Not published</strong>";
        }

        if(this.getEventGroup()!=null){
            descText += "<br>" + eventGroup.getDescription(start.getDayOfWeek());
        }

        entry.setCustomProperty("description", descText);
        return entry;
    }

    public String getDescription(){
        if(entry==null || entry.getDescription()==null) return "";
        return entry.getDescription();
    }

    private void setDaySettings(ResourceEntry entry, String longName, String shortName, Boolean forceAllDay, String resourceName, Boolean useResourceName){
        if(forceAllDay){
            if(fullDay){
                entry.setTitle(longName);
            }else{
                entry.setTitle(shortName + ":" + getTimeFormatted(entry));
            }
            entry.setAllDay(forceAllDay);
        }else{
            if(fullDay){
                if(useResourceName){
                    entry.setTitle(resourceName + " : " + longName);
                }else{
                    entry.setTitle(longName);
                }
            }else{
                if(useResourceName){
                    entry.setTitle(resourceName + " : " + longName);
                }else{
                    entry.setTitle(shortName + ":" + getTimeFormatted(entry));
                }
            }
            entry.setAllDay(fullDay);
        }
        //use TEXT indicator for unpublished entries on List view
        if(useResourceName){
            if(!this.published){
                String originalTitle = entry.getTitle();
                if(this.getType().equals(Scheduler.EventType.OFF)){
                    entry.setTitle(Scheduler.unpublishedPrefix + originalTitle + " (request)");
                }else{
                    entry.setTitle(Scheduler.unpublishedPrefix + originalTitle);
                }
            }
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
        java.time.Duration dur = java.time.Duration.between(start, end);
        long millis = dur.toMillis();

        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)));
    }

    public String generateICalData(String driverEmailAddress, String driverName) {
        ICalendar ical = new ICalendar();
        ical.addProperty(new Method(Method.REQUEST));

        log.info("generateICalData: event: type:" + this.getType() + " fullDay:" + this.getFullDay() + " driverEmailAddress:" + driverEmailAddress);

        VEvent event = new VEvent();
        event.setSummary(formatSubjectForNotification());
        //event.setDescription("You have a new and/or changed schedule");

        if(this.getFullDay()){
            event.setDateStart(Timestamp.valueOf(this.start),false);
        }else{
            event.setDateStart(Timestamp.valueOf(this.start));
            event.setDateEnd(Timestamp.valueOf(this.end));
        }

        event.setOrganizer(new Organizer("DeliverMore", "tara.birch@delivermore.ca"));
        //event.setColor("4cdd66");
        event.setUrl("https://delivermore.ca/schedule");

        Attendee a = new Attendee(driverName, driverEmailAddress);
        a.setParticipationLevel(ParticipationLevel.REQUIRED);
        event.addAttendee(a);
        ical.addEvent(event);

        return Biweekly.write(ical).go();
    }

    public String formatSubjectForNotification(){
        String subject = "DeliverMore schedule:";
        subject += this.getType().typeName;
        if(this.getFullDay()){
            subject += " " + this.start.format(DateTimeFormatter.ofPattern("MMM dd yyyy"));
        }else{
            String timeTitle = "";
            timeTitle = this.start.format(DateTimeFormatter.ofPattern("MMM dd yyyy h:mm"));
            timeTitle += " - " + this.end.format(DateTimeFormatter.ofPattern("h:mm"));
            subject += " " + timeTitle;
        }
        return subject;
    }

    public String formatSummaryForNotification(){
        String subject = "- ";
        subject += this.getType().typeName;
        if(this.getFullDay()){
            subject += " " + this.start.format(DateTimeFormatter.ofPattern("MMM dd yyyy"));
        }else{
            String timeTitle = "";
            timeTitle = this.start.format(DateTimeFormatter.ofPattern("MMM dd yyyy h:mm"));
            timeTitle += " - " + this.end.format(DateTimeFormatter.ofPattern("h:mm"));
            subject += " " + timeTitle;
        }
        return subject;
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
