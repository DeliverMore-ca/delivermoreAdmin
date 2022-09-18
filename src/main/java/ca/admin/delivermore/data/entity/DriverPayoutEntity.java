package ca.admin.delivermore.data.entity;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class DriverPayoutEntity extends AbstractEntity{
    @CsvBindByName(column = "jobId")
    private Long jobId = 0L;

    //@NotEmpty
    @CsvBindByName(column = "restaurantName")
    private String restaurantName = "";

    @CsvBindByName(column = "customerUsername")
    private String customerUsername = "";

    //@NotNull
    @CsvDate("yyyy/MM/dd")
    @CsvBindByName(column = "creationDate")
    private LocalDate creationDate;

    //@NotNull
    @CsvDate("yyyy/MM/dd hh:mm:ss")
    @CsvBindByName(column = "creationDateTime")
    private LocalDateTime creationDateTime;

    @CsvBindByName(column = "tip")
    private Double tip = 0.0;

    @CsvBindByName(column = "notes")
    private String notes = "";

    @CsvBindByName(column = "tipInNotesIssue")
    private Boolean tipInNotesIssue;

    @CsvBindByName(column = "paymentMethod")
    private String paymentMethod = "";

    @CsvBindByName(column = "driverPay")
    private Double driverPay = 0.0;

    @CsvBindByName(column = "totalSale")
    private Double totalSale = 0.0;

    @CsvBindByName(column = "fleetId")
    private Long fleetId;
    @CsvBindByName(column = "fleetName")
    private String fleetName;

    @CsvBindByName(column = "driverIncome")
    private Double driverIncome = 0.0;
    @CsvBindByName(column = "driverCash")
    private Double driverCash = 0.0;
    @CsvBindByName(column = "driverPayout")
    private Double driverPayout = 0.0;

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getCustomerUsername() {
        return customerUsername;
    }

    public void setCustomerUsername(String customerUsername) {
        this.customerUsername = customerUsername;
    }

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(LocalDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public LocalDate getCreationDate(){
        return creationDateTime.toLocalDate();
    }

    public Double getTip() {
        return tip;
    }

    public void setTip(Double tip) {
        this.tip = tip;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getTipInNotesIssue() {
        return tipInNotesIssue;
    }

    public void setTipInNotesIssue(Boolean tipInNotesIssue) {
        this.tipInNotesIssue = tipInNotesIssue;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Double getDriverPay() {
        return driverPay;
    }

    public void setDriverPay(Double driverPay) {
        this.driverPay = driverPay;
    }

    public Double getTotalSale() {
        return totalSale;
    }

    public void setTotalSale(Double totalSale) {
        this.totalSale = totalSale;
    }

    public Long getFleetId() {
        return fleetId;
    }

    public void setFleetId(Long fleetId) {
        this.fleetId = fleetId;
    }

    public String getFleetName() {
        return fleetName;
    }

    public void setFleetName(String fleetName) {
        this.fleetName = fleetName;
    }

    public Double getDriverIncome() {
        return driverIncome;
    }

    public void setDriverIncome(Double driverIncome) {
        this.driverIncome = driverIncome;
    }

    public Double getDriverCash() {
        return driverCash;
    }

    public void setDriverCash(Double driverCash) {
        this.driverCash = driverCash;
    }

    public Double getDriverPayout() {
        return driverPayout;
    }

    public void setDriverPayout(Double driverPayout) {
        this.driverPayout = driverPayout;
    }

    @Override
    public String toString() {
        return "DriverPayoutEntity{" +
                "jobId=" + jobId +
                ", restaurantName='" + restaurantName + '\'' +
                ", customerUsername='" + customerUsername + '\'' +
                ", creationDate=" + creationDate +
                ", tip=" + tip +
                ", notes='" + notes + '\'' +
                ", tipInNotesIssue=" + tipInNotesIssue +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", driverPay=" + driverPay +
                ", totalSale=" + totalSale +
                ", fleetId=" + fleetId +
                ", fleetName='" + fleetName + '\'' +
                ", driverIncome=" + driverIncome +
                ", driverCash=" + driverCash +
                ", driverPayout=" + driverPayout +
                '}';
    }


}
