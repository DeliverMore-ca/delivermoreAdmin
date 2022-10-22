package ca.admin.delivermore.data.report;

import java.time.LocalDate;

public interface DriverPayoutInterface {
    Long getFleetId();

    void setFleetId(Long fleetId);

    LocalDate getPayoutDate();

    void setPayoutDate(LocalDate payoutDate);

    String getFleetName();

    void setFleetName(String fleetName);

    Integer getTaskCount();

    void setTaskCount(Integer taskCount);

    Double getDriverPay();

    void setDriverPay(Double driverPay);

    Double getCardTip();

    void setCardTip(Double cardTip);

    Double getTip();

    void setTip(Double tip);

    Double getDriverIncome();

    void setDriverIncome(Double driverIncome);

    Double getDriverCash();

    void setDriverCash(Double driverCash);

    Double getDriverPayout();

    void setDriverPayout(Double driverPayout);
}
