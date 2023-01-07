package ca.admin.delivermore.data.report;

import ca.admin.delivermore.collector.data.Utility;
import ca.admin.delivermore.collector.data.entity.DriverPayoutEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@IdClass(DriverPayoutPk.class)
public class DriverPayoutDay implements DriverPayoutInterface, Serializable {
    @Id
    private Long fleetId;
    @Id
    private LocalDate payoutDate;

    private String fleetName;
    private Integer taskCount = 0;
    private Double driverPay = 0.0;
    private Double tip = 0.0;

    private Double cardTip = 0.0;
    private Double driverIncome = 0.0;
    private Double driverCash = 0.0;
    private Double driverPayout = 0.0;

    public DriverPayoutDay(List<DriverPayoutEntity> driverPayoutEntities){
        //take a list of DriverPayoutEntity for the same day and driver and summarize
        Integer taskCounter = 0;
        for (DriverPayoutEntity driverPayoutEntity: driverPayoutEntities) {
            taskCounter++;
            if(taskCounter.equals(1)){
                //set all one time fields
                this.fleetId = driverPayoutEntity.getFleetId();
                this.fleetName = driverPayoutEntity.getFleetName();
                this.payoutDate = driverPayoutEntity.getCreationDateTime().toLocalDate();
            }
            this.taskCount++;
            this.driverPay = this.driverPay + Utility.getInstance().round(driverPayoutEntity.getDriverPay(),2);
            this.tip = this.tip + Utility.getInstance().round(driverPayoutEntity.getTip(),2);
            this.driverIncome = this.driverIncome + Utility.getInstance().round(driverPayoutEntity.getDriverIncome(),2);
            this.driverCash = this.driverCash + Utility.getInstance().round(driverPayoutEntity.getDriverCash(),2);
            this.driverPayout = this.driverPayout + Utility.getInstance().round(driverPayoutEntity.getDriverPayout(),2);
            if(driverPayoutEntity.getPaymentMethod().equalsIgnoreCase("CARD")){
                this.cardTip = this.cardTip + Utility.getInstance().round(driverPayoutEntity.getTip(),2);
            }
        }
    }

    public DriverPayoutDay() {

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

    @Override
    public Double getDriverPayout() {
        return Utility.getInstance().round(driverPayout,2);
    }

    @Override
    public void setDriverPayout(Double driverPayout) {
        this.driverPayout = driverPayout;
    }

    @Override
    public String toString() {
        return "DriverPayoutDay{" +
                "fleetId=" + fleetId +
                ", payoutDate=" + payoutDate +
                ", fleetName='" + fleetName + '\'' +
                ", taskCount=" + taskCount +
                ", driverPay=" + getDriverPay() +
                ", tip=" + getTip() +
                ", cardTip=" + getCardTip() +
                ", driverIncome=" + getDriverIncome() +
                ", driverCash=" + getDriverCash() +
                ", driverPayout=" + getDriverIncome() +
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
    public String getDriverPayoutFmt(){
        return String.format("%.2f",getDriverPayout());
    }
    public DriverPayoutPk getDriverPayoutPk(){
        return new DriverPayoutPk(fleetId,payoutDate);
    }

}
