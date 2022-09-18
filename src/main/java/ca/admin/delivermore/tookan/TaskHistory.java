
package ca.admin.delivermore.tookan;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "job_id",
        "fleet_id",
        "fleet_name",
        "latitude",
        "longitude",
        "type",
        "description",
        "extra_fields",
        "creation_datetime",
        "creation_date",
        "label_description"
})
@Generated("jsonschema2pojo")
public class TaskHistory {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("job_id")
    private Long jobId;
    @JsonProperty("fleet_id")
    private Long fleetId;
    @JsonProperty("fleet_name")
    private String fleetName;
    @JsonProperty("latitude")
    private String latitude;
    @JsonProperty("longitude")
    private String longitude;
    @JsonProperty("type")
    private String type;
    @JsonProperty("description")
    private String description;
    @JsonProperty("extra_fields")
    private Object extraFields;
    @JsonProperty("creation_datetime")
    private String creationDatetime;
    @JsonProperty("creation_date")
    private String creationDate;
    @JsonProperty("label_description")
    private String labelDescription;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     *
     */
    public TaskHistory() {
    }

    /**
     *
     * @param fleetName
     * @param jobId
     * @param latitude
     * @param description
     * @param id
     * @param fleetId
     * @param type
     * @param creationDate
     * @param creationDatetime
     * @param labelDescription
     * @param longitude
     * @param extraFields
     */
    public TaskHistory(Long id, Long jobId, Long fleetId, String fleetName, String latitude, String longitude, String type, String description, Object extraFields, String creationDatetime, String creationDate, String labelDescription) {
        super();
        this.id = id;
        this.jobId = jobId;
        this.fleetId = fleetId;
        this.fleetName = fleetName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.description = description;
        this.extraFields = extraFields;
        this.creationDatetime = creationDatetime;
        this.creationDate = creationDate;
        this.labelDescription = labelDescription;
    }

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Long id) {
        this.id = id;
    }

    public TaskHistory withId(Long id) {
        this.id = id;
        return this;
    }

    @JsonProperty("job_id")
    public Long getJobId() {
        return jobId;
    }

    @JsonProperty("job_id")
    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public TaskHistory withJobId(Long jobId) {
        this.jobId = jobId;
        return this;
    }

    @JsonProperty("fleet_id")
    public Long getFleetId() {
        return fleetId;
    }

    @JsonProperty("fleet_id")
    public void setFleetId(Long fleetId) {
        this.fleetId = fleetId;
    }

    public TaskHistory withFleetId(Long fleetId) {
        this.fleetId = fleetId;
        return this;
    }

    @JsonProperty("fleet_name")
    public String getFleetName() {
        return fleetName;
    }

    @JsonProperty("fleet_name")
    public void setFleetName(String fleetName) {
        this.fleetName = fleetName;
    }

    public TaskHistory withFleetName(String fleetName) {
        this.fleetName = fleetName;
        return this;
    }

    @JsonProperty("latitude")
    public String getLatitude() {
        return latitude;
    }

    @JsonProperty("latitude")
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public TaskHistory withLatitude(String latitude) {
        this.latitude = latitude;
        return this;
    }

    @JsonProperty("longitude")
    public String getLongitude() {
        return longitude;
    }

    @JsonProperty("longitude")
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public TaskHistory withLongitude(String longitude) {
        this.longitude = longitude;
        return this;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    public TaskHistory withType(String type) {
        this.type = type;
        return this;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    public TaskHistory withDescription(String description) {
        this.description = description;
        return this;
    }

    @JsonProperty("extra_fields")
    public Object getExtraFields() {
        return extraFields;
    }

    @JsonProperty("extra_fields")
    public void setExtraFields(Object extraFields) {
        this.extraFields = extraFields;
    }

    public TaskHistory withExtraFields(Object extraFields) {
        this.extraFields = extraFields;
        return this;
    }

    @JsonProperty("creation_datetime")
    public String getCreationDatetime() {
        return creationDatetime;
    }

    @JsonProperty("creation_datetime")
    public void setCreationDatetime(String creationDatetime) {
        this.creationDatetime = creationDatetime;
    }

    public TaskHistory withCreationDatetime(String creationDatetime) {
        this.creationDatetime = creationDatetime;
        return this;
    }

    @JsonProperty("creation_date")
    public String getCreationDate() {
        return creationDate;
    }

    @JsonProperty("creation_date")
    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public TaskHistory withCreationDate(String creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    @JsonProperty("label_description")
    public String getLabelDescription() {
        return labelDescription;
    }

    @JsonProperty("label_description")
    public void setLabelDescription(String labelDescription) {
        this.labelDescription = labelDescription;
    }

    public TaskHistory withLabelDescription(String labelDescription) {
        this.labelDescription = labelDescription;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public TaskHistory withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(TaskHistory.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null)?"<null>":this.id));
        sb.append(',');
        sb.append("jobId");
        sb.append('=');
        sb.append(((this.jobId == null)?"<null>":this.jobId));
        sb.append(',');
        sb.append("fleetId");
        sb.append('=');
        sb.append(((this.fleetId == null)?"<null>":this.fleetId));
        sb.append(',');
        sb.append("fleetName");
        sb.append('=');
        sb.append(((this.fleetName == null)?"<null>":this.fleetName));
        sb.append(',');
        sb.append("latitude");
        sb.append('=');
        sb.append(((this.latitude == null)?"<null>":this.latitude));
        sb.append(',');
        sb.append("longitude");
        sb.append('=');
        sb.append(((this.longitude == null)?"<null>":this.longitude));
        sb.append(',');
        sb.append("type");
        sb.append('=');
        sb.append(((this.type == null)?"<null>":this.type));
        sb.append(',');
        sb.append("description");
        sb.append('=');
        sb.append(((this.description == null)?"<null>":this.description));
        sb.append(',');
        sb.append("extraFields");
        sb.append('=');
        sb.append(((this.extraFields == null)?"<null>":this.extraFields));
        sb.append(',');
        sb.append("creationDatetime");
        sb.append('=');
        sb.append(((this.creationDatetime == null)?"<null>":this.creationDatetime));
        sb.append(',');
        sb.append("creationDate");
        sb.append('=');
        sb.append(((this.creationDate == null)?"<null>":this.creationDate));
        sb.append(',');
        sb.append("labelDescription");
        sb.append('=');
        sb.append(((this.labelDescription == null)?"<null>":this.labelDescription));
        sb.append(',');
        sb.append("additionalProperties");
        sb.append('=');
        sb.append(((this.additionalProperties == null)?"<null>":this.additionalProperties));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
