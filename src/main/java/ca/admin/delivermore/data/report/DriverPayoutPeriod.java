package ca.admin.delivermore.data.report;

import ca.admin.delivermore.collector.data.Utility;
import ca.admin.delivermore.collector.data.service.TaskDetailRepository;
import ca.admin.delivermore.data.entity.DriverAdjustment;
import ca.admin.delivermore.data.entity.DriverCardTip;
import ca.admin.delivermore.collector.data.entity.DriverPayoutEntity;
import ca.admin.delivermore.data.service.DriverAdjustmentRepository;
import ca.admin.delivermore.data.service.Registry;

import javax.persistence.*;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Entity
@IdClass(DriverPayoutPeriodPk.class)
public class DriverPayoutPeriod {

    @Id
    private String location;
    @Id
    private LocalDate payoutPeriodStart;

    private LocalDate payoutPeriodEnd;

    private Integer taskCount = 0;
    private Double driverPay = 0.0;
    private Double tip = 0.0;

    private Double cardTip = 0.0;
    private Double driverIncome = 0.0;
    private Double driverCash = 0.0;
    private Double driverAdjustment = 0.0;
    private Double driverPayout = 0.0;

    private File pdfFile = null;
    private File csvFile = null;

    @OneToMany
    private List<DriverPayoutWeek> driverPayoutWeekList = new ArrayList<>();

    @OneToMany
    private List<DriverAdjustment> driverAdjustmentList = new ArrayList<>();

    @OneToMany
    private List<DriverCardTip> driverCardTipList = new ArrayList<>();

    public DriverPayoutPeriod(String location, LocalDate payoutPeriodStart, LocalDate payoutPeriodEnd) {
        this.location = location;
        this.payoutPeriodStart = payoutPeriodStart;
        this.payoutPeriodEnd = payoutPeriodEnd;
        buildPeriod();

    }

    private void buildPeriod(){
        TaskDetailRepository taskDetailRepository = Registry.getBean(TaskDetailRepository.class);
        //build the driverPayoutWeekList here from a query from TaskDetailRepository
        System.out.println("DriverPayoutPeriod constructor: payoutPeriodStart" + payoutPeriodStart + " payoutPeriodEnd:" + payoutPeriodEnd);
        List<Long> fleetIds = taskDetailRepository.findDistinctFleetIdBetweenDates(payoutPeriodStart.atStartOfDay(),payoutPeriodEnd.atTime(23,59,59));
        //find any fleetIds that only had adjustments - no deliveries
        DriverAdjustmentRepository driverAdjustmentRepository = Registry.getBean(DriverAdjustmentRepository.class);
        List<Long> adjustmentFleetIds = driverAdjustmentRepository.findDistinctFleetIdByAdjustmentDateBetween(payoutPeriodStart,payoutPeriodEnd);
        List<Long> newAdjustmentFleetIds = new ArrayList<>();
        for (Long fleetId: adjustmentFleetIds) {
            if(!fleetIds.contains(fleetId)){
                newAdjustmentFleetIds.add(fleetId);
            }
        }
        fleetIds.addAll(newAdjustmentFleetIds);
        //process all fleetId and build the week payout records
        for (Long fleetId: fleetIds) {
            if(fleetId!=null){
                System.out.println("DriverPayoutPeriod constructor: processing fleetId:" + fleetId);
                driverPayoutWeekList.add(new DriverPayoutWeek(fleetId, payoutPeriodStart, payoutPeriodEnd));
            }
        }

        //driverPayoutWeekList.add(new DriverPayoutWeek(1445460L, payoutPeriodStart, payoutPeriodEnd));

        for (DriverPayoutWeek driverPayoutWeek: driverPayoutWeekList) {
            this.setTaskCount(this.getTaskCount() + driverPayoutWeek.getTaskCount());
            this.setDriverPay(this.getDriverPay() + driverPayoutWeek.getDriverPay());
            this.setTip(this.getTip() + driverPayoutWeek.getTip());
            this.setCardTip(this.getCardTip() + driverPayoutWeek.getCardTip());
            this.setDriverIncome(this.getDriverIncome() + driverPayoutWeek.getDriverIncome());
            this.setDriverCash(this.getDriverCash() + driverPayoutWeek.getDriverCash());
            this.setDriverAdjustment(this.getDriverAdjustment() + driverPayoutWeek.getDriverAdjustment());
            this.setDriverPayout(this.getDriverPayout() + driverPayoutWeek.getDriverPayout());
            this.driverAdjustmentList.addAll(driverPayoutWeek.getDriverAdjustmentList());
            System.out.println("Adding Driver Card Tip: id:" + driverPayoutWeek.getFleetId() + " cardTip:" + driverPayoutWeek.getCardTip());
            this.driverCardTipList.add(new DriverCardTip(driverPayoutWeek.getFleetId(), driverPayoutWeek.getFleetName(),driverPayoutWeek.getCardTip()));
        }

    }

    public void refresh(){
        //clear all variables
        this.taskCount = 0;
        this.driverPay = 0.0;
        this.tip = 0.0;
        this.cardTip = 0.0;
        this.driverIncome = 0.0;
        this.driverCash = 0.0;
        this.driverAdjustment = 0.0;
        this.driverPayout = 0.0;
        this.driverPayoutWeekList.clear();
        this.driverAdjustmentList.clear();
        this.driverCardTipList.clear();

        this.pdfFile = null;
        this.csvFile = null;
        buildPeriod();
    }

    public DriverPayoutPeriod() {

    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getPayoutPeriodStart() {
        return payoutPeriodStart;
    }

    public void setPayoutPeriodStart(LocalDate payoutPeriodStart) {
        this.payoutPeriodStart = payoutPeriodStart;
    }

    public LocalDate getPayoutPeriodEnd() {
        return payoutPeriodEnd;
    }

    public void setPayoutPeriodEnd(LocalDate payoutPeriodEnd) {
        this.payoutPeriodEnd = payoutPeriodEnd;
    }

    public List<DriverPayoutWeek> getDriverPayoutWeekList() {
        return driverPayoutWeekList;
    }

    public void setDriverPayoutWeekList(List<DriverPayoutWeek> driverPayoutWeekList) {
        this.driverPayoutWeekList = driverPayoutWeekList;
    }

    public Integer getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(Integer taskCount) {
        this.taskCount = taskCount;
    }

    public Double getDriverPay() {
        return Utility.getInstance().round(driverPay,2);
    }

    public void setDriverPay(Double driverPay) {
        this.driverPay = driverPay;
    }

    public Double getTip() {
        return Utility.getInstance().round(tip,2);
    }

    public void setTip(Double tip) {
        this.tip = tip;
    }

    public Double getCardTip() {
        return Utility.getInstance().round(cardTip,2);
    }

    public void setCardTip(Double cardTip) {
        this.cardTip = cardTip;
    }

    public Double getDriverIncome() {
        return Utility.getInstance().round(driverIncome,2);
    }

    public void setDriverIncome(Double driverIncome) {
        this.driverIncome = driverIncome;
    }

    public Double getDriverCash() {
        return Utility.getInstance().round(driverCash,2);
    }

    public void setDriverCash(Double driverCash) {
        this.driverCash = driverCash;
    }

    public Double getDriverAdjustment() {
        return Utility.getInstance().round(driverAdjustment,2);
    }

    public void setDriverAdjustment(Double driverAdjustment) {
        this.driverAdjustment = driverAdjustment;
    }

    public Double getDriverPayout() {
        return Utility.getInstance().round(driverPayout,2);
    }

    public void setDriverPayout(Double driverPayout) {
        this.driverPayout = driverPayout;
    }

    public File getPdfFile() {
        return pdfFile;
    }

    public void setPdfFile(File pdfFile) {
        this.pdfFile = pdfFile;
    }

    public File getCsvFile() {
        return csvFile;
    }

    public void setCsvFile(File csvFile) {
        this.csvFile = csvFile;
    }

    @Override
    public String toString() {
        return "DriverPayoutPeriod{" +
                "location='" + location + '\'' +
                ", payoutPeriodStart=" + payoutPeriodStart +
                ", payoutPeriodEnd=" + payoutPeriodEnd +
                ", taskCount=" + taskCount +
                ", driverPay=" + getDriverPay() +
                ", tip=" + getTip() +
                ", driverIncome=" + getDriverIncome() +
                ", driverCash=" + getDriverCash() +
                ", driverAdjustment=" + getDriverAdjustment() +
                ", driverPayout=" + getDriverPayout() +
                ", cardTip=" + getCardTip() +
                ", driverPayoutWeekList=" + driverPayoutWeekList +
                '}';
    }
    public String getDriverPayFmt(){
        return String.format("%.2f",getDriverPay());
    }
    public String getTipFmt(){
        return String.format("%.2f",getTip());
    }
    public String getDriverIncomeFmt(){
        return String.format("%.2f",getDriverIncome());
    }
    public String getDriverCashFmt(){
        return String.format("%.2f",getDriverCash());
    }
    public String getDriverAdjustmentFmt(){
        return String.format("%.2f",getDriverAdjustment());
    }
    public String getDriverPayoutFmt(){
        return String.format("%.2f",getDriverPayout());
    }

    public List<PayoutDocument> getPayoutDocuments(){
        List<PayoutDocument> payoutDocumentList = new ArrayList<>();
        if(getCsvFile()==null){
            return payoutDocumentList;
        }
        payoutDocumentList.add(new PayoutDocument("All driver payout tasks (Excel)", getCsvFile(), ""));
        payoutDocumentList.add(new PayoutDocument("Driver payout summary statement (PDF)", getPdfFile(),""));
        for (DriverPayoutWeek driverPayoutWeek: getDriverPayoutWeekList()) {
            payoutDocumentList.add(new PayoutDocument("Statement for:" + driverPayoutWeek.getFleetName() + " (PDF)", driverPayoutWeek.getPdfFile(),driverPayoutWeek.getDriver().getEmail()));
        }
        return payoutDocumentList;
    }
    public List<DriverAdjustment> getDriverAdjustmentList() {
        driverAdjustmentList.sort(Comparator.comparing(DriverAdjustment::getFleetName).thenComparing(DriverAdjustment::getAdjustmentDate));
        return driverAdjustmentList;
    }

    public List<DriverCardTip> getDriverCardTipList() {
        return driverCardTipList;
    }

    public List<Long> getFleetIds() {
        List<Long> fleetIds = new ArrayList<>();
        for (DriverPayoutWeek driverPayoutWeek: driverPayoutWeekList) {
            fleetIds.add(driverPayoutWeek.getFleetId());
        }
        return fleetIds;
    }

    public List<DriverPayoutEntity> getTipIssues(){
        TaskDetailRepository taskDetailRepository = Registry.getBean(TaskDetailRepository.class);
        return taskDetailRepository.getDriverPayoutTipIssues(payoutPeriodStart.atStartOfDay(),payoutPeriodEnd.atTime(23,59,59));
    }
}
