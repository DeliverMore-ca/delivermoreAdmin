
package ca.admin.delivermore.data.intuit;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "SalesReceipt",
    "time"
})
@Generated("jsonschema2pojo")
public class SalesReceiptResponse {

    @JsonProperty("SalesReceipt")
    private SalesReceipt salesReceipt;
    @JsonProperty("time")
    private String time;

    @JsonProperty("SalesReceipt")
    public SalesReceipt getSalesReceipt() {
        return salesReceipt;
    }

    @JsonProperty("SalesReceipt")
    public void setSalesReceipt(SalesReceipt salesReceipt) {
        this.salesReceipt = salesReceipt;
    }

    @JsonProperty("time")
    public String getTime() {
        return time;
    }

    @JsonProperty("time")
    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "SalesReceiptResponse{" +
                "salesReceipt=" + salesReceipt +
                ", time='" + time + '\'' +
                '}';
    }
}
