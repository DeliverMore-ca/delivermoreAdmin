
package ca.admin.delivermore.data.intuit;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Id",
    "Line1",
    "Line2"
})
@Generated("jsonschema2pojo")
public class ShipFromAddr {

    @JsonProperty("Id")
    private String id;
    @JsonProperty("Line1")
    private String line1;
    @JsonProperty("Line2")
    private String line2;

    @JsonProperty("Id")
    public String getId() {
        return id;
    }

    @JsonProperty("Id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("Line1")
    public String getLine1() {
        return line1;
    }

    @JsonProperty("Line1")
    public void setLine1(String line1) {
        this.line1 = line1;
    }

    @JsonProperty("Line2")
    public String getLine2() {
        return line2;
    }

    @JsonProperty("Line2")
    public void setLine2(String line2) {
        this.line2 = line2;
    }

    @Override
    public String toString() {
        return "ShipFromAddr{" +
                "id='" + id + '\'' +
                ", line1='" + line1 + '\'' +
                ", line2='" + line2 + '\'' +
                '}';
    }
}
