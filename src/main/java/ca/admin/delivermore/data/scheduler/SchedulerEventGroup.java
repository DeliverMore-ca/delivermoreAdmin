package ca.admin.delivermore.data.scheduler;

import org.dmfs.rfc5545.DateTime;
import org.dmfs.rfc5545.Weekday;
import org.dmfs.rfc5545.iterable.RecurrenceSet;
import org.dmfs.rfc5545.iterable.instanceiterable.RuleInstances;
import org.dmfs.rfc5545.recur.Freq;
import org.dmfs.rfc5545.recur.InvalidRecurrenceRuleException;
import org.dmfs.rfc5545.recur.RecurrenceRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Entity
public class SchedulerEventGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Transient
    private Logger log = LoggerFactory.getLogger(SchedulerEventGroup.class);

    @NotNull
    private String reoccurrence;

    @Transient
    private LocalDate reoccurUntil = LocalDate.now();

    @Transient
    private Integer reoccurInterval = 1;

    @Transient
    private RecurrenceRule rule;

    public SchedulerEventGroup() {
        //log.info("SchedulerEventGroup: constructor: reoccurrence:" + this.reoccurrence);
        this.rule = new RecurrenceRule(Freq.WEEKLY);
    }

    private void validateRule(){
        //log.info("validateRule: rule:" + this.rule + " reoccurrence:" + this.reoccurrence);
        if(this.rule.getUntil()==null){
            try {
                this.rule = new RecurrenceRule(this.reoccurrence);
            } catch (InvalidRecurrenceRuleException e) {
                log.error("validateRule: could not create rule:" + e);
            }
        }
    }

    public List<LocalDate> getReoccurDates(LocalDate firstDate){
        log.info("getReoccurDates: get dates between::" + firstDate + " and " + getReoccurUntil());
        List<LocalDate> reoccurDates = new ArrayList<>();
        if(this.rule.toString()==null || this.rule.toString().isEmpty()){
            return reoccurDates;
        }
        if(this.rule==null){
            log.error("getReoccurDates: could not create dates list from rule:" + this.reoccurrence);
        }else{
            DateTime firstInstance = new DateTime(Timestamp.valueOf(firstDate.atStartOfDay()).getTime());
            for (DateTime instance:new RecurrenceSet(firstInstance, new RuleInstances(this.rule))) {
                log.info("getReoccurDates: adding instance:" + instance.toString());
                //Timestamp timestamp = new Timestamp(instance.getTimestamp());
                //reoccurDates.add(timestamp.toLocalDateTime().toLocalDate());
                reoccurDates.add(LocalDate.of(instance.getYear(), instance.getMonth() + 1, instance.getDayOfMonth()));
            }
        }
        return reoccurDates;
    }

    private Weekday getWeekDay(DayOfWeek dayOfWeek){
        String dowShort = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()).toUpperCase().substring(0,2);
        log.info("getWeekDay: dowShort:" + dowShort);
        return Weekday.valueOf(dowShort);
    }

    public LocalDate getReoccurUntil() {
        validateRule();
        if(this.rule==null) return null;
        Timestamp timestamp = new Timestamp(this.rule.getUntil().getTimestamp());
        return timestamp.toLocalDateTime().toLocalDate();
    }

    public void setReoccurUntil(LocalDate reoccurUntil) {
        Timestamp timestamp = Timestamp.valueOf(reoccurUntil.atTime(23,59));
        rule.setUntil(new DateTime(timestamp.getTime()));
        this.reoccurrence = rule.toString();
    }

    public Integer getReoccurInterval() {
        validateRule();
        if(this.rule==null) return 1;
        return this.rule.getInterval();
    }

    public void setReoccurInterval(Integer reoccurInterval) {
        this.rule.setInterval(reoccurInterval);
        this.reoccurrence = rule.toString();
    }

    public void setDOW(DayOfWeek dayOfWeek){
        List<RecurrenceRule.WeekdayNum> weekdayNums = new ArrayList<>();
        weekdayNums.add(new RecurrenceRule.WeekdayNum(0, getWeekDay(dayOfWeek)));
        this.rule.setByDayPart(weekdayNums);
        this.reoccurrence = rule.toString();
    }

    public String getDescription(DayOfWeek dayOfWeek){
        validateRule();
        String desc = "Reoccurrence info not available";
        if(getReoccurInterval()!=null && getReoccurUntil()!=null && dayOfWeek!=null){
            desc = Scheduler.getIntervalString(getReoccurInterval()) + dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault());
            desc += " until " + getReoccurUntil();
        }
        //TODO: convert to user readable string
        return desc;
    }

    public String getReoccurrence() {
        return reoccurrence;
    }

    public void setReoccurrence(String reoccurrence) {
        this.reoccurrence = reoccurrence;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "SchedulerEventGroup{" +
                "id=" + id +
                ", reoccurrence='" + reoccurrence + '\'' +
                ", reoccurUntil=" + reoccurUntil +
                ", reoccurInterval=" + reoccurInterval +
                ", rule=" + rule +
                '}';
    }
}
