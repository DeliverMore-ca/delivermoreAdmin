package ca.admin.delivermore.data.report;


import ca.admin.delivermore.collector.data.Utility;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class TasksForWeek implements Comparable {
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean maxRecord = Boolean.FALSE;
    private String dowCountSunday = "";
    private String dowCountMonday = "";
    private String dowCountTuesday = "";
    private String dowCountWednesday = "";
    private String dowCountThursday = "";
    private String dowCountFriday = "";
    private String dowCountSaturday = "";
    private Long weekCount = 0L;

    public TasksForWeek() {
    }

    public TasksForWeek(Boolean maxRecord) {
        this.maxRecord = maxRecord;
        if(maxRecord){
            dowCountSunday = "0";
            dowCountMonday = "0";
            dowCountTuesday = "0";
            dowCountWednesday = "0";
            dowCountThursday = "0";
            dowCountFriday = "0";
            dowCountSaturday = "0";
        }
    }

    @Override
    public int compareTo(Object o) {
        TasksForWeek t = (TasksForWeek) o;
        return startDate.compareTo(t.startDate);
    }

    public void add(LocalDate date, Long count){
        weekCount = weekCount + count;
        if(date.getDayOfWeek().equals(DayOfWeek.SUNDAY)){
            dowCountSunday = count.toString();
        }else if(date.getDayOfWeek().equals(DayOfWeek.MONDAY)){
            dowCountMonday = count.toString();
        }else if(date.getDayOfWeek().equals(DayOfWeek.TUESDAY)){
            dowCountTuesday = count.toString();
        }else if(date.getDayOfWeek().equals(DayOfWeek.WEDNESDAY)){
            dowCountWednesday = count.toString();
        }else if(date.getDayOfWeek().equals(DayOfWeek.THURSDAY)){
            dowCountThursday = count.toString();
        }else if(date.getDayOfWeek().equals(DayOfWeek.FRIDAY)){
            dowCountFriday = count.toString();
        }else if(date.getDayOfWeek().equals(DayOfWeek.SATURDAY)){
            dowCountSaturday = count.toString();
        }else{
            //do nothing
        }
    }

    public void addIfHigher(LocalDate date, Long count){
        if(date.getDayOfWeek().equals(DayOfWeek.SUNDAY)){
            if(!dowCountSunday.isEmpty() && count>Long.valueOf(dowCountSunday)) dowCountSunday = count.toString();
        }else if(date.getDayOfWeek().equals(DayOfWeek.MONDAY)){
            if(!dowCountMonday.isEmpty() && count>Long.valueOf(dowCountMonday)) dowCountMonday = count.toString();
        }else if(date.getDayOfWeek().equals(DayOfWeek.TUESDAY)){
            if(!dowCountTuesday.isEmpty() && count>Long.valueOf(dowCountTuesday)) dowCountTuesday = count.toString();
        }else if(date.getDayOfWeek().equals(DayOfWeek.WEDNESDAY)){
            if(!dowCountWednesday.isEmpty() && count>Long.valueOf(dowCountWednesday)) dowCountWednesday = count.toString();
        }else if(date.getDayOfWeek().equals(DayOfWeek.THURSDAY)){
            if(!dowCountThursday.isEmpty() && count>Long.valueOf(dowCountThursday)) dowCountThursday = count.toString();
        }else if(date.getDayOfWeek().equals(DayOfWeek.FRIDAY)){
            if(!dowCountFriday.isEmpty() && count>Long.valueOf(dowCountFriday)) dowCountFriday = count.toString();
        }else if(date.getDayOfWeek().equals(DayOfWeek.SATURDAY)){
            if(!dowCountSaturday.isEmpty() && count>Long.valueOf(dowCountSaturday)) dowCountSaturday = count.toString();
        }else{
            //do nothing
        }
    }

    public void addWeekIfHigher(Long count){
        if(count>weekCount) weekCount = count;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getDowCountSunday() {
        return dowCountSunday;
    }

    public void setDowCountSunday(String dowCountSunday) {
        this.dowCountSunday = dowCountSunday;
    }

    public String getDowCountMonday() {
        return dowCountMonday;
    }

    public void setDowCountMonday(String dowCountMonday) {
        this.dowCountMonday = dowCountMonday;
    }

    public String getDowCountTuesday() {
        return dowCountTuesday;
    }

    public void setDowCountTuesday(String dowCountTuesday) {
        this.dowCountTuesday = dowCountTuesday;
    }

    public String getDowCountWednesday() {
        return dowCountWednesday;
    }

    public void setDowCountWednesday(String dowCountWednesday) {
        this.dowCountWednesday = dowCountWednesday;
    }

    public String getDowCountThursday() {
        return dowCountThursday;
    }

    public void setDowCountThursday(String dowCountThursday) {
        this.dowCountThursday = dowCountThursday;
    }

    public String getDowCountFriday() {
        return dowCountFriday;
    }

    public void setDowCountFriday(String dowCountFriday) {
        this.dowCountFriday = dowCountFriday;
    }

    public String getDowCountSaturday() {
        return dowCountSaturday;
    }

    public void setDowCountSaturday(String dowCountSaturday) {
        this.dowCountSaturday = dowCountSaturday;
    }

    public String getWeekCount() {
        return weekCount.toString();
    }

    public Long getWeekCountLong() {
        return weekCount;
    }

    public String getWeekName(){
        if(maxRecord){
            return "Records";
        }
        return Utility.dateRangeFormatted(startDate,endDate);
    }

    public void setMaxRecord(Boolean maxRecord) {
        this.maxRecord = maxRecord;
    }
}
