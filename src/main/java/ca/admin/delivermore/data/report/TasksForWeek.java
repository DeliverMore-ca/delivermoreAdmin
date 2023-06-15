package ca.admin.delivermore.data.report;


import ca.admin.delivermore.collector.data.Utility;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class TasksForWeek implements Comparable {
    public static enum TasksForWeekType{
        COUNT, RECORD, AVERAGE, SUM
    }

    private TasksForWeekType weekType = TasksForWeekType.COUNT;

    private LocalDate startDate;
    private LocalDate endDate;
    private String dowCountSunday = "";
    private String dowCountMonday = "";
    private String dowCountTuesday = "";
    private String dowCountWednesday = "";
    private String dowCountThursday = "";
    private String dowCountFriday = "";
    private String dowCountSaturday = "";
    private Long weekCount = 0L;

    private Long counterSunday = 0L;
    private Long counterMonday = 0L;
    private Long counterTuesday = 0L;
    private Long counterWednesday = 0L;
    private Long counterThursday = 0L;
    private Long counterFriday = 0L;
    private Long counterSaturday = 0L;

    public TasksForWeek() {
    }

    public TasksForWeek(TasksForWeekType weekType) {
        this.weekType = weekType;
        if(!this.weekType.equals(TasksForWeekType.COUNT)){
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

    public void addToSum(LocalDate date, Long count){
        weekCount = weekCount + count;
        if(date.getDayOfWeek().equals(DayOfWeek.SUNDAY)){
            if(!date.equals(LocalDate.now())){
                dowCountSunday = Long.toString(Long.valueOf(dowCountSunday) + count);
                counterSunday++;
            }
        }else if(date.getDayOfWeek().equals(DayOfWeek.MONDAY)){
            if(!date.equals(LocalDate.now())){
                dowCountMonday = Long.toString(Long.valueOf(dowCountMonday) + count);
                counterMonday++;
            }
        }else if(date.getDayOfWeek().equals(DayOfWeek.TUESDAY)){
            if(!date.equals(LocalDate.now())){
                dowCountTuesday = Long.toString(Long.valueOf(dowCountTuesday) + count);
                counterTuesday++;
            }
        }else if(date.getDayOfWeek().equals(DayOfWeek.WEDNESDAY)){
            if(!date.equals(LocalDate.now())){
                dowCountWednesday = Long.toString(Long.valueOf(dowCountWednesday) + count);
                counterWednesday++;
            }
        }else if(date.getDayOfWeek().equals(DayOfWeek.THURSDAY)){
            if(!date.equals(LocalDate.now())){
                dowCountThursday = Long.toString(Long.valueOf(dowCountThursday) + count);
                counterThursday++;
            }
        }else if(date.getDayOfWeek().equals(DayOfWeek.FRIDAY)){
            if(!date.equals(LocalDate.now())){
                dowCountFriday = Long.toString(Long.valueOf(dowCountFriday) + count);
                counterFriday++;
            }
        }else if(date.getDayOfWeek().equals(DayOfWeek.SATURDAY)){
            if(!date.equals(LocalDate.now())){
                dowCountSaturday = Long.toString(Long.valueOf(dowCountSaturday) + count);
                counterSaturday++;
            }
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

    public void setWeekCount(Long weekCount) {
        this.weekCount = weekCount;
    }

    public Long getWeekCountLong() {
        return weekCount;
    }

    public Long getDOWCountLongSunday(){
        return getLongFromDOW(dowCountSunday);
    }
    public Long getDOWCountLongMonday(){
        return getLongFromDOW(dowCountMonday);
    }
    public Long getDOWCountLongTuesday(){
        return getLongFromDOW(dowCountTuesday);
    }
    public Long getDOWCountLongWednesday(){
        return getLongFromDOW(dowCountWednesday);
    }
    public Long getDOWCountLongThursday(){
        return getLongFromDOW(dowCountThursday);
    }
    public Long getDOWCountLongFriday(){
        return getLongFromDOW(dowCountFriday);
    }
    public Long getDOWCountLongSaturday(){
        return getLongFromDOW(dowCountSaturday);
    }

    private Long getLongFromDOW(String dowCount){
        if(dowCount==null || dowCount.isEmpty()) return 0L;
        return Long.valueOf(dowCount);
    }

    public Long getCounterSunday() {
        return counterSunday;
    }

    public Long getCounterMonday() {
        return counterMonday;
    }

    public Long getCounterTuesday() {
        return counterTuesday;
    }

    public Long getCounterWednesday() {
        return counterWednesday;
    }

    public Long getCounterThursday() {
        return counterThursday;
    }

    public Long getCounterFriday() {
        return counterFriday;
    }

    public Long getCounterSaturday() {
        return counterSaturday;
    }

    public String getWeekName(){
        if(this.weekType.equals(TasksForWeekType.RECORD)){
            return "Records";
        }else if(this.weekType.equals(TasksForWeekType.AVERAGE)){
            return "Averages";
        }else if(this.weekType.equals(TasksForWeekType.SUM)){
            return "Sums";
        }
        return Utility.dateRangeFormatted(startDate,endDate);
    }

}
