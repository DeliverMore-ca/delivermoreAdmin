
package ca.admin.delivermore.data.intuit;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "ItemRef",
    "UnitPrice",
    "Qty",
    "ItemAccountRef"
})
@Generated("jsonschema2pojo")
public class SalesItemLineDetail {

    @JsonProperty("ItemRef")
    private ItemRef itemRef;
    @JsonProperty("UnitPrice")
    private Double unitPrice;
    @JsonProperty("Qty")
    private Integer qty;
    @JsonProperty("ItemAccountRef")
    private ItemAccountRef itemAccountRef;

    @JsonProperty("ItemRef")
    public ItemRef getItemRef() {
        return itemRef;
    }

    @JsonProperty("ItemRef")
    public void setItemRef(ItemRef itemRef) {
        this.itemRef = itemRef;
    }

    @JsonProperty("UnitPrice")
    public Double getUnitPrice() {
        return unitPrice;
    }

    @JsonProperty("UnitPrice")
    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    @JsonProperty("Qty")
    public Integer getQty() {
        return qty;
    }

    @JsonProperty("Qty")
    public void setQty(Integer qty) {
        this.qty = qty;
    }

    @JsonProperty("ItemAccountRef")
    public ItemAccountRef getItemAccountRef() {
        return itemAccountRef;
    }

    @JsonProperty("ItemAccountRef")
    public void setItemAccountRef(ItemAccountRef itemAccountRef) {
        this.itemAccountRef = itemAccountRef;
    }

    @Override
    public String toString() {
        return "SalesItemLineDetail{" +
                "itemRef=" + itemRef +
                ", unitPrice=" + unitPrice +
                ", qty=" + qty +
                ", itemAccountRef=" + itemAccountRef +
                '}';
    }
}
