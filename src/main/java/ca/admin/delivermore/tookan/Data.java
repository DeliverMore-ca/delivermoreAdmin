
package ca.admin.delivermore.tookan;

import java.util.HashMap;
import java.util.List;
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
        "head",
        "body"
})
@Generated("jsonschema2pojo")
public class Data {

    @JsonProperty("head")
    private List<Head> head = null;
    @JsonProperty("body")
    private List<List<Body>> body = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    private String dataString = null;
    private Double dataDouble = null;
    private Integer dataInteger = null;
    private Long dataLong = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public Data() {
    }

    /**
     *
     * @param dataString
     */
    public Data(String dataString) {
        super();
        this.dataString = dataString;
    }

    /**
     *
     * @param dataDouble
     */
    public Data(Double dataDouble) {
        super();
        this.dataDouble = dataDouble;
    }

    /**
     *
     * @param dataInteger
     */
    public Data(Integer dataInteger) {
        super();
        this.dataInteger = dataInteger;
    }

    /**
     *
     * @param dataLong
     */
    public Data(Long dataLong) {
        super();
        this.dataLong = dataLong;
    }

    /**
     *
     * @param head
     * @param body
     */
    public Data(List<Head> head, List<List<Body>> body) {
        super();
        this.head = head;
        this.body = body;
    }

    public String getdataString() {
        return dataString;
    }
    public void setdataString(String dataString){
        this.dataString = dataString;
    }
    public Data withdataString(String dataString){
        this.dataString = dataString;
        return this;
    }

    public Double getdataDouble() {
        return dataDouble;
    }
    public void setdataDouble(Double dataDouble){
        this.dataDouble = dataDouble;
    }
    public Data withdataDouble(Double dataDouble){
        this.dataDouble = dataDouble;
        return this;
    }

    public Integer getdataInteger() {
        return dataInteger;
    }
    public void setdataInteger(Integer dataInteger){
        this.dataInteger = dataInteger;
    }
    public Data withdataInteger(Integer dataInteger){
        this.dataInteger = dataInteger;
        return this;
    }

    public Long getdataLong() {
        return dataLong;
    }
    public void setdataLong(Long dataLong){
        this.dataLong = dataLong;
    }
    public Data withdataLong(Long dataLong){
        this.dataLong = dataLong;
        return this;
    }

    public String getDataAsString(){
        if(dataString!=null) return dataString;
        if(dataDouble!=null) return String.valueOf(dataDouble);
        if(dataInteger!=null) return String.valueOf(dataInteger);
        if(dataLong!=null) return String.valueOf(dataLong);
        return null;
    }


    @JsonProperty("head")
    public List<Head> getHead() {
        return head;
    }

    @JsonProperty("head")
    public void setHead(List<Head> head) {
        this.head = head;
    }

    public Data withHead(List<Head> head) {
        this.head = head;
        return this;
    }

    @JsonProperty("body")
    public List<List<Body>> getBody() {
        return body;
    }

    @JsonProperty("body")
    public void setBody(List<List<Body>> body) {
        this.body = body;
    }

    public Data withBody(List<List<Body>> body) {
        this.body = body;
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

    public Data withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Data.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("head");
        sb.append('=');
        sb.append(((this.head == null)?"<null>":this.head));
        sb.append(',');
        sb.append("body");
        sb.append('=');
        sb.append(((this.body == null)?"<null>":this.body));
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
