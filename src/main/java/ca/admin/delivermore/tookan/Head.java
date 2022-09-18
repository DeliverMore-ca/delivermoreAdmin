
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
        "app_side",
        "arth",
        "id",
        "label",
        "display_name",
        "required",
        "show",
        "type"
})
@Generated("jsonschema2pojo")
public class Head {

    @JsonProperty("app_side")
    private Long appSide;
    @JsonProperty("arth")
    private String arth;
    @JsonProperty("id")
    private Long id;
    @JsonProperty("label")
    private String label;
    @JsonProperty("display_name")
    private String displayName;
    @JsonProperty("required")
    private Long required;
    @JsonProperty("show")
    private Long show;
    @JsonProperty("type")
    private String type;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     *
     */
    public Head() {
    }

    /**
     *
     * @param appSide
     * @param displayName
     * @param arth
     * @param show
     * @param id
     * @param label
     * @param type
     * @param required
     */
    public Head(Long appSide, String arth, Long id, String label, String displayName, Long required, Long show, String type) {
        super();
        this.appSide = appSide;
        this.arth = arth;
        this.id = id;
        this.label = label;
        this.displayName = displayName;
        this.required = required;
        this.show = show;
        this.type = type;
    }

    @JsonProperty("app_side")
    public Long getAppSide() {
        return appSide;
    }

    @JsonProperty("app_side")
    public void setAppSide(Long appSide) {
        this.appSide = appSide;
    }

    public Head withAppSide(Long appSide) {
        this.appSide = appSide;
        return this;
    }

    @JsonProperty("arth")
    public String getArth() {
        return arth;
    }

    @JsonProperty("arth")
    public void setArth(String arth) {
        this.arth = arth;
    }

    public Head withArth(String arth) {
        this.arth = arth;
        return this;
    }

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Long id) {
        this.id = id;
    }

    public Head withId(Long id) {
        this.id = id;
        return this;
    }

    @JsonProperty("label")
    public String getLabel() {
        return label;
    }

    @JsonProperty("label")
    public void setLabel(String label) {
        this.label = label;
    }

    public Head withLabel(String label) {
        this.label = label;
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

    public Head withDisplayName(String displayName) {
        this.displayName = displayName;
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

    public Head withRequired(Long required) {
        this.required = required;
        return this;
    }

    @JsonProperty("show")
    public Long getShow() {
        return show;
    }

    @JsonProperty("show")
    public void setShow(Long show) {
        this.show = show;
    }

    public Head withShow(Long show) {
        this.show = show;
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

    public Head withType(String type) {
        this.type = type;
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

    public Head withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Head.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("appSide");
        sb.append('=');
        sb.append(((this.appSide == null)?"<null>":this.appSide));
        sb.append(',');
        sb.append("arth");
        sb.append('=');
        sb.append(((this.arth == null)?"<null>":this.arth));
        sb.append(',');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null)?"<null>":this.id));
        sb.append(',');
        sb.append("label");
        sb.append('=');
        sb.append(((this.label == null)?"<null>":this.label));
        sb.append(',');
        sb.append("displayName");
        sb.append('=');
        sb.append(((this.displayName == null)?"<null>":this.displayName));
        sb.append(',');
        sb.append("required");
        sb.append('=');
        sb.append(((this.required == null)?"<null>":this.required));
        sb.append(',');
        sb.append("show");
        sb.append('=');
        sb.append(((this.show == null)?"<null>":this.show));
        sb.append(',');
        sb.append("type");
        sb.append('=');
        sb.append(((this.type == null)?"<null>":this.type));
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
