package ca.admin.delivermore.data.report;

import ca.admin.delivermore.collector.data.Utility;
import ca.admin.delivermore.collector.data.entity.TaskEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RestPayoutItem {

    enum ItemType {
        PAID,
        PAYOUT,
        CANCELLED
    }

    enum SaleType {
        DIRECT,
        PHONEIN
    }

    private Long restaurantId;
    private String restaurantName;
    private Long orderId;
    private ItemType itemType;
    private SaleType saleType;
    private LocalDateTime creationDateTime;
    private Double sale = 0.0;
    private Double taxes = 0.0;
    private Double totalSale = 0.0;
    private Double deliveryFee = 0.0;
    private Double deliveryFeeFromVendor = 0.0;
    private String paymentMethod;
    private Double commissionPerDelivery = 0.0;

    public RestPayoutItem(TaskEntity taskEntity) {
        this.restaurantId = taskEntity.getRestaurantId();
        this.restaurantName = taskEntity.getRestaurantName();
        this.orderId = taskEntity.getLongOrderId();
        if(taskEntity.getPosPayment()){
            this.itemType = ItemType.PAID;
        }else{
            this.itemType = ItemType.PAYOUT;
        }
        this.creationDateTime = taskEntity.getCreationDate();
        if(taskEntity.getCreatedBy().equals(43L)){
            this.sale = taskEntity.getGlobalSubtotal();
            this.saleType = SaleType.DIRECT;
        }else{
            this.sale = taskEntity.getReceiptTotal();
            this.saleType = SaleType.PHONEIN;
        }
        if(taskEntity.getGlobalTotalTaxes()!=null){
            this.taxes = taskEntity.getGlobalTotalTaxes();
        }
        this.totalSale = this.sale + this.taxes;
        this.deliveryFee = taskEntity.getDeliveryFee();
        //need to set deliveryFeeFromVendor from restaurant info NOT from entity
        //TODO:: perhaps remove from entity
        //this.deliveryFeeFromVendor = taskEntity.getDeliveryFeeFromVendor();
        this.paymentMethod = taskEntity.getPaymentMethod();
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public SaleType getSaleType() {
        return saleType;
    }

    public void setSaleType(SaleType saleType) {
        this.saleType = saleType;
    }

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(LocalDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public String getCreationDateTimeFmt(){
        return DateTimeFormatter.ofPattern("MM/dd HH:mm").format(creationDateTime);
    }

    public Double getSale() {
        return Utility.getInstance().round(sale,2);
    }

    public void setSale(Double sale) {
        this.sale = sale;
    }

    public Double getTaxes() {
        return Utility.getInstance().round(taxes,2);
    }

    public void setTaxes(Double taxes) {
        this.taxes = taxes;
    }

    public Double getTotalSale() {
        return Utility.getInstance().round(totalSale,2);
    }

    public void setTotalSale(Double totalSale) {
        this.totalSale = totalSale;
    }

    public Double getDeliveryFee() {
        return Utility.getInstance().round(deliveryFee,2);
    }

    public void setDeliveryFee(Double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public Double getDeliveryFeeFromVendor() {
        return Utility.getInstance().round(deliveryFeeFromVendor,2);
    }

    public void setDeliveryFeeFromVendor(Double deliveryFeeFromVendor) {
        this.deliveryFeeFromVendor = deliveryFeeFromVendor;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Double getCommissionPerDelivery() {
        return Utility.getInstance().round(commissionPerDelivery,2);
    }

    public void setCommissionPerDelivery(Double commissionPerDelivery) {
        this.commissionPerDelivery = commissionPerDelivery;
    }

    @Override
    public String toString() {
        return "RestPayoutItem{" +
                "restaurantId=" + restaurantId +
                ", restaurantName='" + restaurantName + '\'' +
                ", orderId=" + orderId +
                ", itemType=" + itemType +
                ", creationDateTime=" + creationDateTime +
                ", sale=" + getSale() +
                ", taxes=" + getTaxes() +
                ", totalSale=" + getTotalSale() +
                ", deliveryFee=" + getDeliveryFee() +
                ", deliveryFeeFromVendor=" + getDeliveryFeeFromVendor() +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", commissionPerDelivery=" + getCommissionPerDelivery() +
                '}';
    }
}
