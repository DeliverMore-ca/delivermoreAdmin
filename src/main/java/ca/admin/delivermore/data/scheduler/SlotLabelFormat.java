package ca.admin.delivermore.data.scheduler;

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

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "month",
        "day"
})
@Generated("jsonschema2pojo")
public class SlotLabelFormat {

    private Logger log = LoggerFactory.getLogger(SlotLabelFormat.class);

    @JsonProperty("weekday")
    private String weekday;
    @JsonProperty("month")
    private String month;
    @JsonProperty("day")
    private String day;

    @JsonProperty("hour")
    private String hour;

    @JsonProperty("minute")
    private String minute;

    @JsonProperty("omitZeroMinute")
    private boolean omitZeroMinute;

    @JsonProperty("meridiem")
    private String meridiem;

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public boolean getOmitZeroMinute() {
        return omitZeroMinute;
    }

    public void setOmitZeroMinute(boolean omitZeroMinute) {
        this.omitZeroMinute = omitZeroMinute;
    }

    public String getMeridiem() {
        return meridiem;
    }

    public void setMeridiem(String meridiem) {
        this.meridiem = meridiem;
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
        return "SlotLabelFormat{" +
                "weekday='" + weekday + '\'' +
                ", month='" + month + '\'' +
                ", day='" + day + '\'' +
                '}';
    }
}
