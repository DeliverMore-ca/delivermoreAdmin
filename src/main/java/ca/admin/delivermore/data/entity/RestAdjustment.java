package ca.admin.delivermore.data.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
public class RestAdjustment {
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
    private Long restaurantId;
    private String restaurantName;
    private String adjustmentNote = "";
    private Double adjustmentAmount = 0.0;

    public RestAdjustment() {
    }

    public RestAdjustment(LocalDate adjustmentDate, Long restaurantId, String restaurantName, String adjustmentNote, Double adjustmentAmount) {
        this.adjustmentDate = adjustmentDate;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.adjustmentNote = adjustmentNote;
        this.adjustmentAmount = adjustmentAmount;
    }

    public LocalDate getAdjustmentDate() {
        return adjustmentDate;
    }

    public void setAdjustmentDate(LocalDate adjustmentDate) {
        this.adjustmentDate = adjustmentDate;
    }

    public String getAdjustmentDateFmt(){
        return DateTimeFormatter.ofPattern("MM/dd").format(adjustmentDate);
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
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

    public String getAdjustmentAmountFmt(){
        if(getAdjustmentAmount().equals(0.0)){
            return "";
        }
        return String.format("%.2f",getAdjustmentAmount());
    }

    public void setAdjustmentAmount(Double adjustmentAmount) {
        this.adjustmentAmount = adjustmentAmount;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantName(){
        return restaurantName;
    }

    @Override
    public String toString() {
        return "RestAdjustment{" +
                "adjustmentDate=" + adjustmentDate +
                ", restaurantId=" + restaurantId +
                ", restaurantName='" + restaurantName + '\'' +
                ", adjustmentNote='" + adjustmentNote + '\'' +
                ", adjustmentAmount=" + adjustmentAmount +
                ", adjustmentAmountFmt='" + getAdjustmentAmountFmt() + '\'' +
                '}';
    }
}
