package ca.admin.delivermore.data.report;

import ca.admin.delivermore.collector.data.Utility;

import java.time.LocalDateTime;

public class RestSaleSummary {

    private Double sale = 0.0;
    private Double tax = 0.0;
    private Double deliveryFee = 0.0;
    private Double serviceFee = 0.0;
    private Double tip = 0.0;
    private Double cashSale = 0.0;
    private Double cardSale = 0.0;
    private Double onlineSale = 0.0;
    private Integer count = 0;
    private LocalDateTime dateTime = null;
    private Long jobId = null;
    public RestSaleSummary() {
    }

    public Double getSale() {
        return Utility.getInstance().round(sale,2);
    }

    public void setSale(Double sale) {
        this.sale = sale;
    }

    public Double getTax() {
        return Utility.getInstance().round(tax,2);
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public Double getDeliveryFee() {
        return Utility.getInstance().round(deliveryFee,2);
    }

    public void setDeliveryFee(Double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public Double getServiceFee() {
        return Utility.getInstance().round(serviceFee,2);
    }

    public void setServiceFee(Double serviceFee) {
        this.serviceFee = serviceFee;
    }

    public Double getTip() {
        return Utility.getInstance().round(tip,2);
    }

    public void setTip(Double tip) {
        this.tip = tip;
    }

    public Double getCashSale() {
        return Utility.getInstance().round(cashSale,2);
    }

    public void setCashSale(Double cashSale) {
        this.cashSale = cashSale;
    }

    public Double getCardSale() {
        return Utility.getInstance().round(cardSale,2);
    }

    public void setCardSale(Double cardSale) {
        this.cardSale = cardSale;
    }

    public Double getOnlineSale() {
        return Utility.getInstance().round(onlineSale,2);
    }

    public void setOnlineSale(Double onlineSale) {
        this.onlineSale = onlineSale;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Double getFundsTotal(){
        return Utility.getInstance().round(cashSale + cardSale + onlineSale,2);
    }

    public Double getSalesTotal(){
        return Utility.getInstance().round(sale + tax + deliveryFee + serviceFee + tip,2);
    }

    public Double getSalesMinusFundsTotal(){
        return Utility.getInstance().round(getSalesTotal() - getFundsTotal(),2);
    }

    @Override
    public String toString() {
        return "RestSaleSummary{" +
                "sale=" + sale +
                ", tax=" + tax +
                ", deliveryFee=" + deliveryFee +
                ", serviceFee=" + serviceFee +
                ", tip=" + tip +
                ", cashSale=" + cashSale +
                ", cardSale=" + cardSale +
                ", onlineSale=" + onlineSale +
                ", count=" + count +
                ", fundsTotal=" + getFundsTotal() +
                ", salesTotal=" + getSalesTotal() +
                ", salesMinusFundsTotal=" + getSalesMinusFundsTotal() +
                '}';
    }
}
