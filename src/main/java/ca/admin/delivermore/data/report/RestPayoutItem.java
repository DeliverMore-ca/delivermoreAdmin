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
        PHONEIN,
        WEBORDERONLINE,
        WEBORDER
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
    private Double prePaidTotalSale = 0.0;

    private Double paidToVendor = 0.0;

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
            if(taskEntity.getWebOrder()){
                if(taskEntity.getPaymentMethod().equals("ONLINE")){
                    this.saleType = SaleType.WEBORDERONLINE;
                    this.sale = taskEntity.getReceiptTotal();
                    this.prePaidTotalSale = taskEntity.getReceiptTotal();
                }else{
                    this.saleType = SaleType.WEBORDER;
                    this.sale = taskEntity.getReceiptTotal();
                }
            }else if(taskEntity.getFeesOnly()){ //custom order fees only has no SALE value so set to 0
                this.saleType = SaleType.PHONEIN;
                this.sale = 0.0;
            }else{
                this.saleType = SaleType.PHONEIN;
                this.sale = taskEntity.getReceiptTotal();
            }
        }
        if(taskEntity.getGlobalTotalTaxes()!=null){
            this.taxes = taskEntity.getGlobalTotalTaxes();
        }
        this.totalSale = this.sale + this.taxes;
        if(this.saleType.equals(SaleType.WEBORDERONLINE)){
            this.deliveryFee = 0.0;
        }else if(this.saleType.equals(SaleType.WEBORDER)){
            this.deliveryFee = 0.0;
        }else{
            this.deliveryFee = taskEntity.getDeliveryFee();
        }
        //need to set deliveryFeeFromVendor from restaurant info NOT from entity
        //TODO:: perhaps remove from entity
        //this.deliveryFeeFromVendor = taskEntity.getDeliveryFeeFromVendor();
        this.paymentMethod = taskEntity.getPaymentMethod();
        if(taskEntity.getPaidToVendor()!=null){
            paidToVendor = taskEntity.getPaidToVendor();
        }
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

    public Double getPrePaidTotalSale() {
        return prePaidTotalSale;
    }

    public void setPrePaidTotalSale(Double prePaidTotalSale) {
        this.prePaidTotalSale = prePaidTotalSale;
    }

    public Double getPaidToVendor() {
        return paidToVendor;
    }

    public void setPaidToVendor(Double paidToVendor) {
        this.paidToVendor = paidToVendor;
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
