package ca.admin.delivermore.data.report;

import ca.admin.delivermore.collector.data.Utility;
import ca.admin.delivermore.collector.data.service.DriversRepository;
import ca.admin.delivermore.collector.data.service.TaskDetailRepository;
import ca.admin.delivermore.collector.data.tookan.Driver;
import ca.admin.delivermore.data.entity.DriverAdjustment;
import ca.admin.delivermore.collector.data.entity.DriverPayoutEntity;
import ca.admin.delivermore.data.service.DriverAdjustmentRepository;
import ca.admin.delivermore.data.service.Registry;
import com.vaadin.flow.data.binder.Binder;

import javax.persistence.*;
import java.io.File;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
public class DriverPayoutWeek implements DriverPayoutInterface, Serializable {
    @Id
    private Long fleetId;
    @Id
    private LocalDate payoutDate;

    private LocalDate weekEndDate;

    private String fleetName;
    private Integer taskCount = 0;
    private Double driverPay = 0.0;
    private Double tip = 0.0;

    private Double cardTip = 0.0;
    private Double driverIncome = 0.0;
    private Double driverCash = 0.0;
    private Double driverAdjustment = 0.0;
    private Double driverPayout = 0.0;
    private Double driverCost = 0.0;

    private File pdfFile = null;

    @OneToMany
    private List<DriverPayoutDay> driverPayoutDayList = new ArrayList<>();
    @OneToMany
    private List<DriverAdjustment> driverAdjustmentList = new ArrayList<>();

    private Binder<DriverPayoutWeek> weekBinder = new Binder<>(DriverPayoutWeek.class);

    public DriverPayoutWeek(Long fleetId, LocalDate payoutDate, LocalDate weekEndDate){
        //take a list of DriverPayoutDay for the same week and driver and summarize
        this.fleetId = fleetId;
        this.payoutDate = payoutDate;
        this.weekEndDate = weekEndDate;

        TaskDetailRepository taskDetailRepository = Registry.getBean(TaskDetailRepository.class);

        Stream<LocalDate> dates = payoutDate.datesUntil(weekEndDate.plusDays(1));
        List<LocalDate> payoutDays = dates.collect(Collectors.toList());
        for (LocalDate payoutDay: payoutDays) {
            List<DriverPayoutEntity> driverPayoutEntities = taskDetailRepository.getDriverPayoutByFleetId(fleetId, payoutDay.atStartOfDay(),payoutDay.atTime(23,59,59) );
            if(driverPayoutEntities.size()>0){
                driverPayoutDayList.add(new DriverPayoutDay(driverPayoutEntities));
            }
        }

        //process the adjustments for this driver for this week
        DriverAdjustmentRepository driverAdjustmentRepository = Registry.getBean(DriverAdjustmentRepository.class);
        driverAdjustmentList = driverAdjustmentRepository.findByFleetIdAndAdjustmentDateBetween(fleetId,payoutDate,weekEndDate);
        for (DriverAdjustment driverAdjustmentItem: driverAdjustmentList) {
            this.setDriverAdjustment(this.getDriverAdjustment() + driverAdjustmentItem.getAdjustmentAmount());
        }

        Integer taskCounter = 0;
        for (DriverPayoutInterface driverPayoutDay: driverPayoutDayList) {
            if(taskCounter.equals(0)){
                //set all one time fields
                this.setFleetId(driverPayoutDay.getFleetId());
                this.setFleetName(driverPayoutDay.getFleetName());
                taskCounter++;
            }
            this.setTaskCount(this.getTaskCount() + driverPayoutDay.getTaskCount());
            this.setDriverPay(this.getDriverPay() + driverPayoutDay.getDriverPay());
            this.setTip(this.getTip() + driverPayoutDay.getTip());
            this.setCardTip(this.getCardTip() + driverPayoutDay.getCardTip());
            this.setDriverIncome(this.getDriverIncome() + driverPayoutDay.getDriverIncome());
            this.setDriverCash(this.getDriverCash() + driverPayoutDay.getDriverCash());
            this.setDriverPayout(this.getDriverPayout() + driverPayoutDay.getDriverPayout());
        }

        //add the adjustments to the payout total
        this.setDriverPayout(this.getDriverPayout() + this.getDriverAdjustment());

        //add the adjustments to the driver cost
        this.setDriverCost(this.getDriverPay() + this.getDriverAdjustment());

        //if this payoutWeek only contains adjustments then the fleetName will be null
        if(fleetName==null){
            DriversRepository driversRepository = Registry.getBean(DriversRepository.class);
            Driver driver = driversRepository.getDriverByFleetId(fleetId);
            if(driver==null){
                fleetName = "NotFound:"+fleetId;
            }else{
                fleetName = driver.getName();
            }
        }

    }

    public DriverPayoutWeek() {

    }

    public List<DriverAdjustment> getDriverAdjustmentList() {
        return driverAdjustmentList;
    }

    public LocalDate getWeekEndDate() {
        return weekEndDate;
    }

    public void setWeekEndDate(LocalDate weekEndDate) {
        this.weekEndDate = weekEndDate;
    }

    @Override
    public String toString() {
        return "DriverPayoutWeek{" +
                "fleetId=" + getFleetId() +
                ", fleetName='" + getFleetName() + '\'' +
                ", payoutStartDate=" + getPayoutDate() +
                ", payoutEndDate=" + weekEndDate +
                ", taskCount=" + getTaskCount() +
                ", driverPay=" + getDriverPay() +
                ", tip=" + getTip() +
                ", cardTip=" + getCardTip() +
                ", driverIncome=" + getDriverIncome() +
                ", driverCash=" + getDriverCash() +
                ", driverAdjustment=" + getDriverAdjustment() +
                ", driverPayout=" + getDriverPayout() +
                '}';
    }

    public List<DriverPayoutDay> getDriverPayoutDayList() {
        return driverPayoutDayList;
    }

    @Override
    public Long getFleetId() {
        return fleetId;
    }

    @Override
    public void setFleetId(Long fleetId) {
        this.fleetId = fleetId;
    }

    @Override
    public LocalDate getPayoutDate() {
        return payoutDate;
    }

    @Override
    public void setPayoutDate(LocalDate payoutDate) {
        this.payoutDate = payoutDate;
    }

    @Override
    public String getFleetName() {
        return fleetName;
    }

    @Override
    public void setFleetName(String fleetName) {
        this.fleetName = fleetName;
    }

    @Override
    public Integer getTaskCount() {
        return taskCount;
    }

    @Override
    public void setTaskCount(Integer taskCount) {
        this.taskCount = taskCount;
    }

    @Override
    public Double getDriverPay() {
        return Utility.getInstance().round(driverPay,2);
    }

    @Override
    public void setDriverPay(Double driverPay) {
        this.driverPay = driverPay;
    }

    @Override
    public Double getCardTip() {
        return cardTip;
    }

    @Override
    public void setCardTip(Double cardTip) {
        this.cardTip = cardTip;
    }

    @Override
    public Double getTip() {
        return Utility.getInstance().round(tip,2);
    }

    @Override
    public void setTip(Double tip) {
        this.tip = tip;
    }

    @Override
    public Double getDriverIncome() {
        return Utility.getInstance().round(driverIncome,2);
    }

    @Override
    public void setDriverIncome(Double driverIncome) {
        this.driverIncome = driverIncome;
    }

    @Override
    public Double getDriverCash() {
        return Utility.getInstance().round(driverCash,2);
    }

    @Override
    public void setDriverCash(Double driverCash) {
        this.driverCash = driverCash;
    }

    public Double getDriverAdjustment() {
        return Utility.getInstance().round(driverAdjustment,2);
    }

    public void setDriverAdjustment(Double driverAdjustment) {
        this.driverAdjustment = driverAdjustment;
    }

    @Override
    public Double getDriverPayout() {
        return Utility.getInstance().round(driverPayout,2);
    }

    @Override
    public void setDriverPayout(Double driverPayout) {
        this.driverPayout = driverPayout;
    }

    public Double getDriverCost() {
        return Utility.getInstance().round(driverCost,2);
    }

    public void setDriverCost(Double driverCost) {
        this.driverCost = driverCost;
    }

    public Binder<DriverPayoutWeek> getWeekBinder() {
        return weekBinder;
    }

    public void updateBinder(){
        weekBinder.readBean(this);
    }

    public File getPdfFile() {
        return pdfFile;
    }

    public void setPdfFile(File pdfFile) {
        this.pdfFile = pdfFile;
    }

    public Driver getDriver(){
        DriversRepository driversRepository = Registry.getBean(DriversRepository.class);
        Driver driver = driversRepository.getDriverByFleetId(fleetId);
        return driver;
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

    public String getDriverCostFmt(){
        return String.format("%.2f",getDriverCost());
    }


}
