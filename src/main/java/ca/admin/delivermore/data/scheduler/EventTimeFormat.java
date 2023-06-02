
package ca.admin.delivermore.data.scheduler;

import javax.annotation.Generated;

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
    "hour",
    "minute",
    "timeZoneName",
    "meridiem",
    "hour12"
})
@Generated("jsonschema2pojo")
public class EventTimeFormat {

    private Logger log = LoggerFactory.getLogger(EventTimeFormat.class);
    @JsonProperty("hour")
    private String hour;
    @JsonProperty("minute")
    private String minute;
    @JsonProperty("timeZoneName")
    private String timeZoneName;
    @JsonProperty("meridiem")
    private Boolean meridiem;
    @JsonProperty("hour12")
    private Boolean hour12;

    @JsonProperty("hour")
    public String getHour() {
        return hour;
    }

    @JsonProperty("hour")
    public void setHour(String hour) {
        this.hour = hour;
    }

    @JsonProperty("minute")
    public String getMinute() {
        return minute;
    }

    @JsonProperty("minute")
    public void setMinute(String minute) {
        this.minute = minute;
    }

    @JsonProperty("timeZoneName")
    public String getTimeZoneName() {
        return timeZoneName;
    }

    @JsonProperty("timeZoneName")
    public void setTimeZoneName(String timeZoneName) {
        this.timeZoneName = timeZoneName;
    }

    @JsonProperty("meridiem")
    public Boolean getMeridiem() {
        return meridiem;
    }

    @JsonProperty("meridiem")
    public void setMeridiem(Boolean meridiem) {
        this.meridiem = meridiem;
    }

    @JsonProperty("hour12")
    public Boolean getHour12() {
        return hour12;
    }

    @JsonProperty("hour12")
    public void setHour12(Boolean hour12) {
        this.hour12 = hour12;
    }

    @JsonIgnore
    public JsonObject getJsonObject(){
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String jsonString = objectMapper.writeValueAsString(this);
            log.info("getJsonObject: json from object:" + jsonString);
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
        return "EventTimeFormat{" +
                "hour='" + hour + '\'' +
                ", minute='" + minute + '\'' +
                ", timeZoneName='" + timeZoneName + '\'' +
                ", meridiem=" + meridiem +
                ", hour12=" + hour12 +
                '}';
    }
}
