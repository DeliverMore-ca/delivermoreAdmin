package ca.admin.delivermore.data.entity;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;

@Entity
public class Restaurant extends AbstractEntity{

    public Restaurant() {
        super();
    }

    public Restaurant(Long restaurantId, String name, Double commissionRate) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.commissionRate = commissionRate;
    }

    public Restaurant(Long restaurantId, String name, Double commissionRate, Long formId) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.formId = formId;
        this.commissionRate = commissionRate;
        this.deliveryFeeFromVendor = deliveryFeeFromVendor;
    }

    public Restaurant(Long restaurantId, String name, Double commissionRate, Double deliveryFeeFromVendor) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.commissionRate = commissionRate;
        this.deliveryFeeFromVendor = deliveryFeeFromVendor;
    }

    public Restaurant(Long restaurantId, String name, Double commissionRate, Double deliveryFeeFromVendor, Long formId) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.formId = formId;
        this.commissionRate = commissionRate;
        this.deliveryFeeFromVendor = deliveryFeeFromVendor;
    }

    public Restaurant(Long restaurantId, String name, Double commissionRate, Double commissionPerDelivery, Double deliveryFeeFromVendor, Long formId) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.formId = formId;
        this.commissionRate = commissionRate;
        this.commissionPerDelivery = commissionPerDelivery;
        this.deliveryFeeFromVendor = deliveryFeeFromVendor;
    }


    //@NotEmpty
    private Long restaurantId;

    @NotEmpty
    private String name = "";

    private Long formId = 0L;

    private Double commissionRate = 0.0;

    private Double commissionPerDelivery = 0.0;

    private Double deliveryFeeFromVendor = 0.0;

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getFormId() {
        return formId;
    }

    public void setFormId(Long formId) {
        this.formId = formId;
    }

    public Double getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(Double commissionRate) {
        this.commissionRate = commissionRate;
    }

    public Double getDeliveryFeeFromVendor() {
        return deliveryFeeFromVendor;
    }

    public void setDeliveryFeeFromVendor(Double deliveryFeeFromVendor) {
        this.deliveryFeeFromVendor = deliveryFeeFromVendor;
    }

    public Double getCommissionPerDelivery() {
        return commissionPerDelivery;
    }

    public void setCommissionPerDelivery(Double commissionPerDelivery) {
        this.commissionPerDelivery = commissionPerDelivery;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "restaurantId=" + restaurantId +
                ", name='" + name + '\'' +
                ", formId=" + formId +
                ", commissionRate=" + commissionRate +
                ", commissionPerDelivery=" + commissionPerDelivery +
                ", deliveryFeeFromVendor=" + deliveryFeeFromVendor +
                '}';
    }
}
