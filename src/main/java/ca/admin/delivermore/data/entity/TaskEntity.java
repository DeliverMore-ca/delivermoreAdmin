package ca.admin.delivermore.data.entity;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class TaskEntity{
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskEntity that = (TaskEntity) o;
        return Objects.equals(jobId, that.jobId) && Objects.equals(restaurantName, that.restaurantName) && Objects.equals(jobStatusName, that.jobStatusName) && Objects.equals(customerUsername, that.customerUsername) && Objects.equals(completedDate, that.completedDate) && Objects.equals(creationDate, that.creationDate) && Objects.equals(tip, that.tip) && Objects.equals(notes, that.notes) && Objects.equals(tipInNotesIssue, that.tipInNotesIssue) && Objects.equals(templateId, that.templateId) && Objects.equals(jobStatus, that.jobStatus) && Objects.equals(restaurantId, that.restaurantId) && Objects.equals(customerEmail, that.customerEmail) && Objects.equals(customerId, that.customerId) && Objects.equals(customerPhone, that.customerPhone) && Objects.equals(jobAddress, that.jobAddress) && Objects.equals(jobDescription, that.jobDescription) && Objects.equals(paymentMethod, that.paymentMethod) && Objects.equals(receiptTotal, that.receiptTotal) && Objects.equals(paidToVendor, that.paidToVendor) && Objects.equals(deliveryFee, that.deliveryFee) && Objects.equals(serviceFeePercent, that.serviceFeePercent) && Objects.equals(serviceFee, that.serviceFee) && Objects.equals(totalFees, that.totalFees) && Objects.equals(driverPay, that.driverPay) && Objects.equals(feeBalance, that.feeBalance) && Objects.equals(totalSale, that.totalSale) && Objects.equals(dispatcherId, that.dispatcherId) && Objects.equals(teamId, that.teamId) && Objects.equals(fleetId, that.fleetId) && Objects.equals(fleetName, that.fleetName) && Objects.equals(formId, that.formId) && Objects.equals(jobLatitude, that.jobLatitude) && Objects.equals(jobLongitude, that.jobLongitude) && Objects.equals(orderId, that.orderId) && Objects.equals(autoAssignment, that.autoAssignment) && Objects.equals(userId, that.userId) && Objects.equals(createdBy, that.createdBy) && Objects.equals(globalSubtotal, that.globalSubtotal) && Objects.equals(globalTotalTaxes, that.globalTotalTaxes) && Objects.equals(commission, that.commission) && Objects.equals(totalIncome, that.totalIncome) && Objects.equals(deliveryFeeFromVendor, that.deliveryFeeFromVendor) && Objects.equals(driverIncome, that.driverIncome) && Objects.equals(driverCash, that.driverCash) && Objects.equals(driverPayout, that.driverPayout);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobId, restaurantName, jobStatusName, customerUsername, completedDate, creationDate, tip, notes, tipInNotesIssue, templateId, jobStatus, restaurantId, customerEmail, customerId, customerPhone, jobAddress, jobDescription, paymentMethod, receiptTotal, paidToVendor, deliveryFee, serviceFeePercent, serviceFee, totalFees, driverPay, feeBalance, totalSale, dispatcherId, teamId, fleetId, fleetName, formId, jobLatitude, jobLongitude, orderId, autoAssignment, userId, createdBy, globalSubtotal, globalTotalTaxes, commission, totalIncome, deliveryFeeFromVendor, driverIncome, driverCash, driverPayout);
    }

    @Id
    @CsvBindByName(column = "jobId")
    private Long jobId = 0L;

    //@NotEmpty
    @CsvBindByName(column = "restaurantName")
    private String restaurantName = "";

    @NotEmpty
    @CsvBindByName(column = "jobStatusName")
    private String jobStatusName = "";

    //@NotEmpty
    @CsvBindByName(column = "customerUsername")
    private String customerUsername = "";

    @NotNull
    @CsvDate("yyyy/MM/dd hh:mm:ss")
    @CsvBindByName(column = "completedDate")
    private LocalDateTime completedDate;

    @NotNull
    @CsvDate("yyyy/MM/dd hh:mm:ss")
    @CsvBindByName(column = "creationDate")
    private LocalDateTime creationDate;

    @CsvBindByName(column = "tip")
    private Double tip = 0.0;

    @CsvBindByName(column = "notes")
    private String notes = "";

    @CsvBindByName(column = "tipInNotesIssue")
    private Boolean tipInNotesIssue;

    @CsvBindByName(column = "templateId")
    private String templateId = "";
    @CsvBindByName(column = "jobStatus")
    private Long jobStatus;
    @CsvBindByName(column = "restaurantId")
    private Long restaurantId;
    @CsvBindByName(column = "customerEmail")
    private String customerEmail = "";
    @CsvBindByName(column = "customerId")
    private Long customerId;
    @CsvBindByName(column = "customerPhone")
    private String customerPhone = "";
    @CsvBindByName(column = "jobAddress")
    private String jobAddress = "";
    @Lob
    @CsvBindByName(column = "jobDescription")
    private String jobDescription = "";
    @CsvBindByName(column = "paymentMethod")
    private String paymentMethod = "";
    @CsvBindByName(column = "receiptTotal")
    private Double receiptTotal;
    @CsvBindByName(column = "paidToVendor")
    private Double paidToVendor;
    @CsvBindByName(column = "deliveryFee")
    private Double deliveryFee = 0.0;
    @CsvBindByName(column = "serviceFeePercent")
    private Double serviceFeePercent;
    @CsvBindByName(column = "serviceFee")
    private Double serviceFee = 0.0;
    @CsvBindByName(column = "totalFees")
    private Double totalFees = 0.0;
    @CsvBindByName(column = "driverPay")
    private Double driverPay = 0.0;
    @CsvBindByName(column = "feeBalance")
    private Double feeBalance = 0.0;
    @CsvBindByName(column = "totalSale")
    private Double totalSale = 0.0;
    @CsvBindByName(column = "dispatcherId")
    private Long dispatcherId;
    @CsvBindByName(column = "teamId")
    private Long teamId;
    @CsvBindByName(column = "fleetId")
    private Long fleetId;
    @CsvBindByName(column = "fleetName")
    private String fleetName;
    @CsvBindByName(column = "formId")
    private Long formId;
    @CsvBindByName(column = "jobLatitude")
    private String jobLatitude = "";
    @CsvBindByName(column = "jobLongitude")
    private String jobLongitude = "";
    @CsvBindByName(column = "orderId")
    private String orderId = "";
    @CsvBindByName(column = "autoAssignment")
    private Long autoAssignment;
    @CsvBindByName(column = "userId")
    private Long userId;
    @CsvBindByName(column = "createdBy")
    private Long createdBy;

    @CsvBindByName(column = "globalSubtotal")
    private Double globalSubtotal;
    @CsvBindByName(column = "globalTotalTaxes")
    private Double globalTotalTaxes;
    @CsvBindByName(column = "commission")
    private Double commission = 0.0;
    @CsvBindByName(column = "totalIncome")
    private Double totalIncome = 0.0;
    @CsvBindByName(column = "deliveryFeeFromVendor")
    private Double deliveryFeeFromVendor = 0.0;

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

    public LocalDateTime getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(LocalDateTime completedDate) {
        this.completedDate = completedDate;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getJobStatusName() {
        return jobStatusName;
    }

    public void setJobStatusName(String jobStatusName) {
        this.jobStatusName = jobStatusName;
    }

    public String getCustomerUsername() {
        return customerUsername;
    }

    public void setCustomerUsername(String customerUsername) {
        this.customerUsername = customerUsername;
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

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public Long getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(Long jobStatus) {
        this.jobStatus = jobStatus;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getJobAddress() {
        return jobAddress;
    }

    public void setJobAddress(String jobAddress) {
        this.jobAddress = jobAddress;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Double getReceiptTotal() {
        return receiptTotal;
    }

    public void setReceiptTotal(Double receiptTotal) {
        this.receiptTotal = receiptTotal;
    }

    public Double getPaidToVendor() {
        return paidToVendor;
    }

    public void setPaidToVendor(Double paidToVendor) {
        this.paidToVendor = paidToVendor;
    }

    public Double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(Double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public Double getServiceFeePercent() {
        return serviceFeePercent;
    }

    public void setServiceFeePercent(Double serviceFeePercent) {
        this.serviceFeePercent = serviceFeePercent;
    }

    public Double getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(Double serviceFee) {
        this.serviceFee = serviceFee;
    }

    public Double getTotalFees() {
        return totalFees;
    }

    public void setTotalFees(Double totalFees) {
        this.totalFees = totalFees;
    }

    public Double getDriverPay() {
        return driverPay;
    }

    public void setDriverPay(Double driverPay) {
        this.driverPay = driverPay;
    }

    public Double getFeeBalance() {
        return feeBalance;
    }

    public void setFeeBalance(Double feeBalance) {
        this.feeBalance = feeBalance;
    }

    public Double getTotalSale() {
        return totalSale;
    }

    public void setTotalSale(Double totalSale) {
        this.totalSale = totalSale;
    }

    public Long getDispatcherId() {
        return dispatcherId;
    }

    public void setDispatcherId(Long dispatcherId) {
        this.dispatcherId = dispatcherId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Long getFleetId() {
        return fleetId;
    }

    public void setFleetId(Long fleetId) {
        this.fleetId = fleetId;
    }

    public Object getFormId() {
        return formId;
    }

    public void setFormId(Long formId) {
        this.formId = formId;
    }

    public String getJobLatitude() {
        return jobLatitude;
    }

    public void setJobLatitude(String jobLatitude) {
        this.jobLatitude = jobLatitude;
    }

    public String getJobLongitude() {
        return jobLongitude;
    }

    public void setJobLongitude(String jobLongitude) {
        this.jobLongitude = jobLongitude;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Long getAutoAssignment() {
        return autoAssignment;
    }

    public void setAutoAssignment(Long autoAssignment) {
        this.autoAssignment = autoAssignment;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Double getGlobalSubtotal() {
        return globalSubtotal;
    }

    public void setGlobalSubtotal(Double globalSubtotal) {
        this.globalSubtotal = globalSubtotal;
    }

    public Double getGlobalTotalTaxes() {
        return globalTotalTaxes;
    }

    public void setGlobalTotalTaxes(Double globalTotalTaxes) {
        this.globalTotalTaxes = globalTotalTaxes;
    }

    public Double getCommission() {
        return commission;
    }

    public void setCommission(Double commission) {
        this.commission = commission;
    }

    public Double getDeliveryFeeFromVendor() {
        return deliveryFeeFromVendor;
    }

    public void setDeliveryFeeFromVendor(Double deliveryFeeFromVendor) {
        this.deliveryFeeFromVendor = deliveryFeeFromVendor;
    }

    public Double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(Double totalIncome) {
        this.totalIncome = totalIncome;
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
        return "TaskEntity{" +
                "jobId=" + jobId +
                ", restaurantName='" + restaurantName + '\'' +
                ", jobStatusName='" + jobStatusName + '\'' +
                ", customerUsername='" + customerUsername + '\'' +
                ", completedDate=" + completedDate +
                ", creationDate=" + creationDate +
                ", tip=" + tip +
                ", notes='" + notes + '\'' +
                ", tipInNotesIssue=" + tipInNotesIssue +
                ", templateId='" + templateId + '\'' +
                ", jobStatus=" + jobStatus +
                ", restaurantId=" + restaurantId +
                ", customerEmail='" + customerEmail + '\'' +
                ", customerId=" + customerId +
                ", customerPhone='" + customerPhone + '\'' +
                ", jobAddress='" + jobAddress + '\'' +
                ", jobDescription='" + jobDescription + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", receiptTotal=" + receiptTotal +
                ", paidToVendor=" + paidToVendor +
                ", deliveryFee=" + deliveryFee +
                ", serviceFeePercent=" + serviceFeePercent +
                ", serviceFee=" + serviceFee +
                ", totalFees=" + totalFees +
                ", driverPay=" + driverPay +
                ", feeBalance=" + feeBalance +
                ", totalSale=" + totalSale +
                ", dispatcherId=" + dispatcherId +
                ", teamId=" + teamId +
                ", fleetId=" + fleetId +
                ", fleetName='" + fleetName + '\'' +
                ", formId=" + formId +
                ", jobLatitude='" + jobLatitude + '\'' +
                ", jobLongitude='" + jobLongitude + '\'' +
                ", orderId='" + orderId + '\'' +
                ", autoAssignment=" + autoAssignment +
                ", userId=" + userId +
                ", createdBy=" + createdBy +
                ", globalSubtotal=" + globalSubtotal +
                ", globalTotalTaxes=" + globalTotalTaxes +
                ", commission=" + commission +
                ", totalIncome=" + totalIncome +
                ", deliveryFeeFromVendor=" + deliveryFeeFromVendor +
                ", driverIncome=" + driverIncome +
                ", driverCash=" + driverCash +
                ", driverPayout=" + driverPayout +
                '}';
    }
}
