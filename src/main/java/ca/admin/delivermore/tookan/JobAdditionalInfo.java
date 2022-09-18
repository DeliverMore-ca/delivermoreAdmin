
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
        "job_id",
        "job_otp",
        "is_job_otp_verified"
})
@Generated("jsonschema2pojo")
public class JobAdditionalInfo {

    @JsonProperty("job_id")
    private Long jobId;
    @JsonProperty("job_otp")
    private Object jobOtp;
    @JsonProperty("is_job_otp_verified")
    private Object isJobOtpVerified;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     *
     */
    public JobAdditionalInfo() {
    }

    /**
     *
     * @param jobId
     * @param jobOtp
     * @param isJobOtpVerified
     */
    public JobAdditionalInfo(Long jobId, Object jobOtp, Object isJobOtpVerified) {
        super();
        this.jobId = jobId;
        this.jobOtp = jobOtp;
        this.isJobOtpVerified = isJobOtpVerified;
    }

    @JsonProperty("job_id")
    public Long getJobId() {
        return jobId;
    }

    @JsonProperty("job_id")
    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public JobAdditionalInfo withJobId(Long jobId) {
        this.jobId = jobId;
        return this;
    }

    @JsonProperty("job_otp")
    public Object getJobOtp() {
        return jobOtp;
    }

    @JsonProperty("job_otp")
    public void setJobOtp(Object jobOtp) {
        this.jobOtp = jobOtp;
    }

    public JobAdditionalInfo withJobOtp(Object jobOtp) {
        this.jobOtp = jobOtp;
        return this;
    }

    @JsonProperty("is_job_otp_verified")
    public Object getIsJobOtpVerified() {
        return isJobOtpVerified;
    }

    @JsonProperty("is_job_otp_verified")
    public void setIsJobOtpVerified(Object isJobOtpVerified) {
        this.isJobOtpVerified = isJobOtpVerified;
    }

    public JobAdditionalInfo withIsJobOtpVerified(Object isJobOtpVerified) {
        this.isJobOtpVerified = isJobOtpVerified;
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

    public JobAdditionalInfo withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(JobAdditionalInfo.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("jobId");
        sb.append('=');
        sb.append(((this.jobId == null)?"<null>":this.jobId));
        sb.append(',');
        sb.append("jobOtp");
        sb.append('=');
        sb.append(((this.jobOtp == null)?"<null>":this.jobOtp));
        sb.append(',');
        sb.append("isJobOtpVerified");
        sb.append('=');
        sb.append(((this.isJobOtpVerified == null)?"<null>":this.isJobOtpVerified));
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
