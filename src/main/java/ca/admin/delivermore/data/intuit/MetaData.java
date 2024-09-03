
package ca.admin.delivermore.data.intuit;

import jakarta.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "CreateTime",
    "LastUpdatedTime"
})
@Generated("jsonschema2pojo")
public class MetaData {

    @JsonProperty("CreateTime")
    private String createTime;
    @JsonProperty("LastUpdatedTime")
    private String lastUpdatedTime;

    @JsonProperty("CreateTime")
    public String getCreateTime() {
        return createTime;
    }

    @JsonProperty("CreateTime")
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @JsonProperty("LastUpdatedTime")
    public String getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    @JsonProperty("LastUpdatedTime")
    public void setLastUpdatedTime(String lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    @Override
    public String toString() {
        return "MetaData{" +
                "createTime='" + createTime + '\'' +
                ", lastUpdatedTime='" + lastUpdatedTime + '\'' +
                '}';
    }
}
