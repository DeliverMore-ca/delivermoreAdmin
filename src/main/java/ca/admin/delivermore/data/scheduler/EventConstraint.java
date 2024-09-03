package ca.admin.delivermore.data.scheduler;

import java.util.List;
import jakarta.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import elemental.json.Json;
import elemental.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "resourceIds"
})
@Generated("jsonschema2pojo")
public class EventConstraint {

    private Logger log = LoggerFactory.getLogger(EventConstraint.class);

    @JsonProperty("resourceIds")
    private List<String> resourceIds;

    @JsonProperty("resourceIds")
    public List<String> getResourceIds() {
        return resourceIds;
    }

    @JsonProperty("resourceIds")
    public void setResourceIds(List<String> resourceIds) {
        this.resourceIds = resourceIds;
    }

    @JsonIgnore
    public JsonObject getJsonObject(){
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String jsonString = objectMapper.writeValueAsString(this);
            log.info("EventContraint: json from object:" + jsonString);
            if(jsonString!=null){
                return Json.parse(jsonString);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public String toString() {
        return "EventConstraint{" +
                "resourceIds=" + resourceIds +
                '}';
    }
}