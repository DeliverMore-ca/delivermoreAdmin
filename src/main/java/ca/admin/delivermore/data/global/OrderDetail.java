package ca.admin.delivermore.data.global;

import ca.admin.delivermore.data.entity.AbstractEntity;
import com.opencsv.bean.CsvBindByName;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
public class OrderDetail extends AbstractEntity {

    @NotNull
    @CsvBindByName(column = "Restaurant ID")
    private Long restaurantId;

    @NotNull
    @CsvBindByName(column = "Order ID")
    private Long orderId;

    @CsvBindByName(column = "Subtotal")
    private Double subtotal;
    @CsvBindByName(column = "Delivery fee")
    private Double deliveryFee;

    @CsvBindByName(column = "Service fees on subtotal")
    private Double serviceFee;

    @CsvBindByName(column = "Total taxes")
    private Double totalTaxes;
    @CsvBindByName(column = "Total")
    private Double total;
    @CsvBindByName(column = "Payment method")
    private String paymentMethod = "";

    //Restaurant ID,Restaurant name,Order ID,Subtotal,Delivery fee,Total taxes,Total,Payment method,Fulfillment date (YYYY-MM-DD),Fulfillment time


    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(Double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public Double getTotalTaxes() {
        return totalTaxes;
    }

    public void setTotalTaxes(Double totalTaxes) {
        this.totalTaxes = totalTaxes;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Double getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(Double serviceFee) {
        this.serviceFee = serviceFee;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "restaurantId=" + restaurantId +
                ", orderId=" + orderId +
                ", subtotal=" + subtotal +
                ", deliveryFee=" + deliveryFee +
                ", serviceFee=" + serviceFee +
                ", totalTaxes=" + totalTaxes +
                ", total=" + total +
                ", paymentMethod='" + paymentMethod + '\'' +
                '}';
    }
}
