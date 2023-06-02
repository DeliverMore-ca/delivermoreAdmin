package ca.admin.delivermore.data.scheduler;

import org.vaadin.stefan.fullcalendar.Timezone;

import java.time.Duration;
import java.time.LocalTime;
import java.time.ZoneId;

public final class Scheduler {

    public Scheduler() {
    }

    public static final String availableShiftsResourceId = "0";
    public static final String availableShiftsDisplayName = "Available shifts";
    public static final String defaultEventColor = "var(--lumo-base-color)";

    public static final LocalTime minTime = LocalTime.of(10, 0);
    public static final LocalTime maxTime = LocalTime.of(22, 0);
    public static final Timezone tzDefault = new Timezone(ZoneId.of("America/Edmonton"));
    public static final Duration timeStep = Duration.ofMinutes(30);
    public static final String driverLastUsedView = "LastUsedSchedulerView";

    public static enum ListenerEventType{
        DROPPED, RESIZED
    }

    public static enum EditType{
        CALENDAR, DIALOG
    }

    public static enum EventType{
        SHIFT("Shift"), UNAVAILABLE("Unavailable"), OFF("Time off");

        public final String typeName;
        private EventType(String s) {
            this.typeName = s;
        }
    }

    public static enum EventColor{
        SHIFT("#4cdd66"), UNAVAILABLE("var(--lumo-contrast-80pct)"), OFF("var(--lumo-primary-color-50pct)");

        public final String color;
        private EventColor(String s) {
            this.color = s;
        }
    }

    public static enum EventDurationType{
        PARTIALDAY("Partial day"), FULLDAY("Full day");

        public final String typeName;
        private EventDurationType(String s) {
            this.typeName = s;
        }
    }

}
