package ca.admin.delivermore.data.entity;

import ca.admin.delivermore.collector.data.tookan.Driver;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
public class DriverAdjustment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    private LocalDate adjustmentDate;
    @NotNull
    private Long fleetId;
    private String adjustmentNote = "";
    private Double adjustmentAmount = 0.0;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "fleetId", nullable = false, insertable = false, updatable = false)
    private Driver driver;

    public DriverAdjustment() {
    }

    public DriverAdjustment(LocalDate adjustmentDate, Long fleetId, String adjustmentNote, Double adjustmentAmount) {
        this.adjustmentDate = adjustmentDate;
        this.fleetId = fleetId;
        this.adjustmentNote = adjustmentNote;
        this.adjustmentAmount = adjustmentAmount;
    }

    public LocalDate getAdjustmentDate() {
        return adjustmentDate;
    }

    public void setAdjustmentDate(LocalDate adjustmentDate) {
        this.adjustmentDate = adjustmentDate;
    }

    public Long getFleetId() {
        return fleetId;
    }

    public void setFleetId(Long fleetId) {
        this.fleetId = fleetId;
    }

    public String getAdjustmentNote() {
        return adjustmentNote;
    }

    public void setAdjustmentNote(String adjustmentNote) {
        this.adjustmentNote = adjustmentNote;
    }

    public Double getAdjustmentAmount() {
        return adjustmentAmount;
    }

    public void setAdjustmentAmount(Double adjustmentAmount) {
        this.adjustmentAmount = adjustmentAmount;
    }

    public String getAdjustmentAmountFmt(){
        if(getAdjustmentAmount().equals(0.0)){
            return "";
        }
        return String.format("%.2f",getAdjustmentAmount());
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public String getFleetName(){
        return driver.getName();
    }

    public DriverAdjustmentTemplate getAdjustmentTemplate(){
        return new DriverAdjustmentTemplate(getAdjustmentNote(), getAdjustmentAmount());
    }

    @Override
    public String toString() {
        return "DriverAdjustment{" +
                "id=" + id +
                ", adjustmentDate=" + adjustmentDate +
                ", fleetId=" + fleetId +
                ", adjustmentNote='" + adjustmentNote + '\'' +
                ", adjustmentAmount=" + adjustmentAmount +
                '}';
    }
}
