
package ca.admin.delivermore.data.intuit;

import jakarta.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "QueryResponse",
    "time"
})
@Generated("jsonschema2pojo")
public class QueryResult {

    @JsonProperty("QueryResponse")
    private QueryResponse queryResponse;
    @JsonProperty("time")
    private String time;

    @JsonProperty("QueryResponse")
    public QueryResponse getQueryResponse() {
        return queryResponse;
    }

    @JsonProperty("QueryResponse")
    public void setQueryResponse(QueryResponse queryResponse) {
        this.queryResponse = queryResponse;
    }

    @JsonProperty("time")
    public String getTime() {
        return time;
    }

    @JsonProperty("time")
    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "QueryResult{" +
                "queryResponse=" + queryResponse +
                ", time='" + time + '\'' +
                '}';
    }
}
