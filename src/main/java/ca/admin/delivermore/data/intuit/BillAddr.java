
package ca.admin.delivermore.data.intuit;

import jakarta.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "Id"
})
@Generated("jsonschema2pojo")
public class BillAddr {

    @JsonProperty("Id")
    private String id;

    @JsonProperty("Id")
    public String getId() {
        return id;
    }

    @JsonProperty("Id")
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "BillAddr{" +
                "id='" + id + '\'' +
                '}';
    }
}
