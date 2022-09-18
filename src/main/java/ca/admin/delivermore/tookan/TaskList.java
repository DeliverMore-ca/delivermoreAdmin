
package ca.admin.delivermore.tookan;

import java.util.ArrayList;
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
        "message",
        "status",
        "data",
        "total_page_count",
        "requested_page",
        "records_per_page"
})
@Generated("jsonschema2pojo")
public class TaskList {

    @JsonProperty("message")
    private String message;
    @JsonProperty("status")
    private Long status;
    @JsonProperty("data")
    private List<Task> data = null;
    @JsonProperty("total_page_count")
    private Long totalPageCount;
    @JsonProperty("requested_page")
    private Long requestedPage;
    @JsonProperty("records_per_page")
    private Long recordsPerPage;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     *
     */
    public TaskList() {
    }

    /**
     *
     * @param requestedPage
     * @param data
     * @param totalPageCount
     * @param recordsPerPage
     * @param message
     * @param status
     */
    public TaskList(String message, Long status, List<Task> data, Long totalPageCount, Long requestedPage, Long recordsPerPage) {
        super();
        this.message = message;
        this.status = status;
        this.data = data;
        this.totalPageCount = totalPageCount;
        this.requestedPage = requestedPage;
        this.recordsPerPage = recordsPerPage;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    public TaskList withMessage(String message) {
        this.message = message;
        return this;
    }

    @JsonProperty("status")
    public Long getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(Long status) {
        this.status = status;
    }

    public TaskList withStatus(Long status) {
        this.status = status;
        return this;
    }

    @JsonProperty("data")
    public List<Task> getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(List<Task> data) {
        this.data = data;
    }

    public TaskList withData(List<Task> data) {
        this.data = data;
        return this;
    }

    @JsonProperty("total_page_count")
    public Long getTotalPageCount() {
        return totalPageCount;
    }

    @JsonProperty("total_page_count")
    public void setTotalPageCount(Long totalPageCount) {
        this.totalPageCount = totalPageCount;
    }

    public TaskList withTotalPageCount(Long totalPageCount) {
        this.totalPageCount = totalPageCount;
        return this;
    }

    @JsonProperty("requested_page")
    public Long getRequestedPage() {
        return requestedPage;
    }

    @JsonProperty("requested_page")
    public void setRequestedPage(Long requestedPage) {
        this.requestedPage = requestedPage;
    }

    public TaskList withRequestedPage(Long requestedPage) {
        this.requestedPage = requestedPage;
        return this;
    }

    @JsonProperty("records_per_page")
    public Long getRecordsPerPage() {
        return recordsPerPage;
    }

    @JsonProperty("records_per_page")
    public void setRecordsPerPage(Long recordsPerPage) {
        this.recordsPerPage = recordsPerPage;
    }

    public TaskList withRecordsPerPage(Long recordsPerPage) {
        this.recordsPerPage = recordsPerPage;
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

    public TaskList withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(TaskList.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("message");
        sb.append('=');
        sb.append(((this.message == null)?"<null>":this.message));
        sb.append(',');
        sb.append("status");
        sb.append('=');
        sb.append(((this.status == null)?"<null>":this.status));
        sb.append(',');
        sb.append("data");
        sb.append('=');
        sb.append(((this.data == null)?"<null>":this.data));
        sb.append(',');
        sb.append("totalPageCount");
        sb.append('=');
        sb.append(((this.totalPageCount == null)?"<null>":this.totalPageCount));
        sb.append(',');
        sb.append("requestedPage");
        sb.append('=');
        sb.append(((this.requestedPage == null)?"<null>":this.requestedPage));
        sb.append(',');
        sb.append("recordsPerPage");
        sb.append('=');
        sb.append(((this.recordsPerPage == null)?"<null>":this.recordsPerPage));
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

    public List<Long> getTaskIDs(){
        List<Long> idList = new ArrayList<>();
        for (Task item: data ) {
            idList.add(item.getJobId());
        }
        return idList;
    }

}
