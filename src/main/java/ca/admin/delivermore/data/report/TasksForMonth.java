package ca.admin.delivermore.data.report;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;

public class TasksForMonth implements Comparable{

    public static enum TasksForMonthType{
        COUNT, RECORD, AVERAGE, SUM
    }

    private TasksForMonthType monthType = TasksForMonthType.COUNT;

    private LocalDate startDate;
    private Long monthCount = 0L;
    private Long monthCounter = 0L;  //used for sum to ignore current and first month
    private String monthName = "";

    public TasksForMonth() {
    }

    public TasksForMonth(TasksForMonthType monthType) {
        this.monthType = monthType;
    }

    @Override
    public int compareTo(Object o) {
        TasksForMonth t = (TasksForMonth) o;
        return startDate.compareTo(t.startDate);
    }

    public void addToSum(LocalDate date, Long count){
        //add unless current month
        if(!isCurrentMonth(date)){
            monthCount+= count;
            monthCounter++;
        }
    }

    private Boolean isCurrentMonth(LocalDate givenDate){
        LocalDate ref = LocalDate.now();
        return Month.from(givenDate) == Month.from(ref) && Year.from(givenDate).equals(Year.from(ref));
    }

    public Long getMonthCount() {
        return monthCount;
    }

    public void setMonthCount(Long monthCount) {
        this.monthCount = monthCount;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Long getMonthCounter() {
        return monthCounter;
    }

    public String getMonthName() {
        if(this.monthType.equals(TasksForMonthType.AVERAGE)) return "Average";
        monthName = startDate.format(DateTimeFormatter.ofPattern("MMM yyyy"));
        monthName = monthName.replaceAll("\\.", "");
        if(this.monthType.equals(TasksForMonthType.RECORD)) return "Record:" + monthName;
        return monthName;
    }
}
