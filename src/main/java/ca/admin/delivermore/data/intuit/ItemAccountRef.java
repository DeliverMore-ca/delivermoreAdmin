
package ca.admin.delivermore.data.intuit;

import jakarta.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "value",
    "name"
})
@Generated("jsonschema2pojo")
public class ItemAccountRef {

    @JsonProperty("value")
    private String value;
    @JsonProperty("name")
    private String name;

    public ItemAccountRef() {
    }

    public ItemAccountRef(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @JsonProperty("value")
    public String getValue() {
        return value;
    }

    @JsonProperty("value")
    public void setValue(String value) {
        this.value = value;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ItemAccountRef{" +
                "value='" + value + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
