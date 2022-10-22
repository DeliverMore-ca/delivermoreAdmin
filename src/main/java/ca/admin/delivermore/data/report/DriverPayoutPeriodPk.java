package ca.admin.delivermore.data.report;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class DriverPayoutPeriodPk implements Serializable {

    private String location;
    private LocalDate payoutPeriodStart;

    public DriverPayoutPeriodPk() {
    }

    public DriverPayoutPeriodPk(String location, LocalDate payoutPeriodStart) {
        this.location = location;
        this.payoutPeriodStart = payoutPeriodStart;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DriverPayoutPeriodPk that = (DriverPayoutPeriodPk) o;
        return Objects.equals(location, that.location) && Objects.equals(payoutPeriodStart, that.payoutPeriodStart);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, payoutPeriodStart);
    }
}
