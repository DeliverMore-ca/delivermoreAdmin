package ca.admin.delivermore.data.report;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TasksForMonth implements Comparable{

    private LocalDate startDate;
    private Boolean maxRecord = Boolean.FALSE;

    private Long monthCount = 0L;

    private String monthName = "";

    public TasksForMonth() {
    }

    public TasksForMonth(Boolean maxRecord) {
        this.maxRecord = maxRecord;
    }

    @Override
    public int compareTo(Object o) {
        TasksForMonth t = (TasksForMonth) o;
        return startDate.compareTo(t.startDate);
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

    public String getMonthName() {
        monthName = startDate.format(DateTimeFormatter.ofPattern("MMM yyyy"));
        monthName = monthName.replaceAll("\\.", "");
        if(maxRecord) return "Record:" + monthName;
        return monthName;
    }
}
