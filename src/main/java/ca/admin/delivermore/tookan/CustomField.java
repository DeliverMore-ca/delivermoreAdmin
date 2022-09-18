
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
        "label",
        "data",
        "fleet_data",
        "display_name",
        "value",
        "app_side",
        "data_type",
        "input",
        "required",
        "template_id",
        "appCheck"
})
@Generated("jsonschema2pojo")
public class CustomField {

    @JsonProperty("label")
    private String label;
    @JsonProperty("data")
    private Data data;
    @JsonProperty("fleet_data")
    private Object fleetData;
    @JsonProperty("display_name")
    private String displayName;
    @JsonProperty("value")
    private Object value;
    @JsonProperty("app_side")
    private Object appSide;
    @JsonProperty("data_type")
    private String dataType;
    @JsonProperty("input")
    private Data input;
    @JsonProperty("required")
    private Long required;
    @JsonProperty("template_id")
    private String templateId;
    @JsonProperty("appCheck")
    private Boolean appCheck;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     *
     */
    public CustomField() {
    }

    /**
     *
     * @param input
     * @param data
     * @param fleetData
     * @param appSide
     * @param displayName
     * @param dataType
     * @param label
     * @param templateId
     * @param appCheck
     * @param value
     * @param required
     */
    public CustomField(String label, Data data, Object fleetData, String displayName, Long value, Long appSide, String dataType, Data input, Long required, String templateId, Boolean appCheck) {
        super();
        this.label = label;
        this.data = data;
        this.fleetData = fleetData;
        this.displayName = displayName;
        this.value = value;
        this.appSide = appSide;
        this.dataType = dataType;
        this.input = input;
        this.required = required;
        this.templateId = templateId;
        this.appCheck = appCheck;
    }

    @JsonProperty("label")
    public String getLabel() {
        return label;
    }

    @JsonProperty("label")
    public void setLabel(String label) {
        this.label = label;
    }

    public CustomField withLabel(String label) {
        this.label = label;
        return this;
    }

    @JsonProperty("data")
    public Data getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(Data data) {
        this.data = data;
    }

    public CustomField withData(Data data) {
        this.data = data;
        return this;
    }

    @JsonProperty("fleet_data")
    public Object getFleetData() {
        return fleetData;
    }

    @JsonProperty("fleet_data")
    public void setFleetData(Object fleetData) {
        this.fleetData = fleetData;
    }

    public CustomField withFleetData(Object fleetData) {
        this.fleetData = fleetData;
        return this;
    }

    @JsonProperty("display_name")
    public String getDisplayName() {
        return displayName;
    }

    @JsonProperty("display_name")
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public CustomField withDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    @JsonProperty("value")
    public Object getValue() {
        return value;
    }

    @JsonProperty("value")
    public void setValue(Object value) {
        this.value = value;
    }

    public CustomField withValue(Object value) {
        this.value = value;
        return this;
    }

    @JsonProperty("app_side")
    public Object getAppSide() {
        return appSide;
    }

    @JsonProperty("app_side")
    public void setAppSide(Object appSide) {
        this.appSide = appSide;
    }

    public CustomField withAppSide(Object appSide) {
        this.appSide = appSide;
        return this;
    }

    @JsonProperty("data_type")
    public String getDataType() {
        return dataType;
    }

    @JsonProperty("data_type")
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public CustomField withDataType(String dataType) {
        this.dataType = dataType;
        return this;
    }

    @JsonProperty("input")
    public Data getInput() {
        return input;
    }

    @JsonProperty("input")
    public void setInput(Data input) {
        this.input = input;
    }

    public CustomField withInput(Data input) {
        this.input = input;
        return this;
    }

    @JsonProperty("required")
    public Long getRequired() {
        return required;
    }

    @JsonProperty("required")
    public void setRequired(Long required) {
        this.required = required;
    }

    public CustomField withRequired(Long required) {
        this.required = required;
        return this;
    }

    @JsonProperty("template_id")
    public String getTemplateId() {
        return templateId;
    }

    @JsonProperty("template_id")
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public CustomField withTemplateId(String templateId) {
        this.templateId = templateId;
        return this;
    }

    @JsonProperty("appCheck")
    public Boolean getAppCheck() {
        return appCheck;
    }

    @JsonProperty("appCheck")
    public void setAppCheck(Boolean appCheck) {
        this.appCheck = appCheck;
    }

    public CustomField withAppCheck(Boolean appCheck) {
        this.appCheck = appCheck;
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

    public CustomField withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(CustomField.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("label");
        sb.append('=');
        sb.append(((this.label == null)?"<null>":this.label));
        sb.append(',');
        sb.append("data");
        sb.append('=');
        sb.append(((this.data == null)?"<null>":this.data));
        sb.append(',');
        sb.append("displayName");
        sb.append('=');
        sb.append(((this.displayName == null)?"<null>":this.displayName));
        sb.append(',');
        sb.append("value");
        sb.append('=');
        sb.append(((this.value == null)?"<null>":this.value));
        sb.append(',');
        sb.append("appSide");
        sb.append('=');
        sb.append(((this.appSide == null)?"<null>":this.appSide));
        sb.append(',');
        sb.append("dataType");
        sb.append('=');
        sb.append(((this.dataType == null)?"<null>":this.dataType));
        sb.append(',');
        sb.append("input");
        sb.append('=');
        sb.append(((this.input == null)?"<null>":this.input));
        sb.append(',');
        sb.append("required");
        sb.append('=');
        sb.append(((this.required == null)?"<null>":this.required));
        sb.append(',');
        sb.append("templateId");
        sb.append('=');
        sb.append(((this.templateId == null)?"<null>":this.templateId));
        sb.append(',');
        sb.append("appCheck");
        sb.append('=');
        sb.append(((this.appCheck == null)?"<null>":this.appCheck));
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
