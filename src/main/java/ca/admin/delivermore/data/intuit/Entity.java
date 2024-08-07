
package ca.admin.delivermore.data.intuit;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "Type",
    "EntityRef"
})
@Generated("jsonschema2pojo")
public class Entity {

    @JsonProperty("Type")
    private String type;
    @JsonProperty("EntityRef")
    private EntityRef entityRef;

    @JsonProperty("Type")
    public String getType() {
        return type;
    }

    @JsonProperty("Type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("EntityRef")
    public EntityRef getEntityRef() {
        return entityRef;
    }

    @JsonProperty("EntityRef")
    public void setEntityRef(EntityRef entityRef) {
        this.entityRef = entityRef;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "type='" + type + '\'' +
                ", entityRef=" + entityRef +
                '}';
    }
}
