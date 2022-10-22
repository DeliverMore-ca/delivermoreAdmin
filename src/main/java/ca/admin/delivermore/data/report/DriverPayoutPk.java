package ca.admin.delivermore.data.report;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Objects;

public class DriverPayoutPk implements Serializable, Comparable<DriverPayoutPk> {
    private Long fleetId;
    private LocalDate payoutDate;

    public DriverPayoutPk() {
    }

    public DriverPayoutPk(Long fleetId, LocalDate payoutDate) {
        this.fleetId = fleetId;
        this.payoutDate = payoutDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DriverPayoutPk that = (DriverPayoutPk) o;
        return Objects.equals(fleetId, that.fleetId) && Objects.equals(payoutDate, that.payoutDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fleetId, payoutDate);
    }

    @Override
    public int compareTo(DriverPayoutPk o) {
        return Comparator.comparing(DriverPayoutPk::getFleetId)
                .thenComparing(DriverPayoutPk::getPayoutDate)
                .compare(this, o);
    }

    public Long getFleetId() {
        return fleetId;
    }

    public void setFleetId(Long fleetId) {
        this.fleetId = fleetId;
    }

    public LocalDate getPayoutDate() {
        return payoutDate;
    }

    public void setPayoutDate(LocalDate payoutDate) {
        this.payoutDate = payoutDate;
    }

    @Override
    public String toString() {
        return "DriverPayoutPk{" +
                "fleetId=" + fleetId +
                ", payoutDate=" + payoutDate +
                '}';
    }
}
