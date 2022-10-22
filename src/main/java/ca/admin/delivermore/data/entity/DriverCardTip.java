package ca.admin.delivermore.data.entity;

import ca.admin.delivermore.collector.data.tookan.Driver;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
public class DriverCardTip {
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
    private Long fleetId;

    private String fleetName;

    private Double cardTipAmount = 0.0;

    public DriverCardTip() {
    }

    public DriverCardTip(Long fleetId, String fleetName, Double cardTipAmount) {
        this.fleetId = fleetId;
        this.fleetName = fleetName;
        this.cardTipAmount = cardTipAmount;
    }

    public Long getFleetId() {
        return fleetId;
    }

    public void setFleetId(Long fleetId) {
        this.fleetId = fleetId;
    }

    public String getFleetName(){
        return fleetName;
    }

    public void setFleetName(String fleetName) {
        this.fleetName = fleetName;
    }

    public Double getCardTipAmount() {
        return cardTipAmount;
    }

    public void setCardTipAmount(Double cardTipAmount) {
        this.cardTipAmount = cardTipAmount;
    }

    public String getCardTipAmountFmt(){
        if(getCardTipAmount().equals(0.0)){
            return "";
        }
        return String.format("%.2f",getCardTipAmount());
    }

    @Override
    public String toString() {
        return "DriverCardTip{" +
                "id=" + id +
                ", fleetId=" + fleetId +
                ", fleetName='" + fleetName + '\'' +
                ", cardTipAmount=" + cardTipAmount +
                ", cardTipAmountFmt='" + getCardTipAmountFmt() + '\'' +
                '}';
    }
}
