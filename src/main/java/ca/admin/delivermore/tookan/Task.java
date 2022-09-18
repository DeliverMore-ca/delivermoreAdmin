
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
        "fleet_id",
        "fleet_name",
        "fleet_phone",
        "fleet_latitude",
        "fleet_longitude",
        "fleet_image",
        "order_id",
        "job_pickup_name",
        "job_pickup_phone",
        "user_id",
        "timezone",
        "job_latitude",
        "job_longitude",
        "job_address",
        "job_status",
        "job_description",
        "has_pickup",
        "pickup_delivery_relationship",
        "completed_by_admin",
        "job_pickup_datetime",
        "job_id",
        "job_delivery_datetime",
        "job_type",
        "creation_datetime",
        "customer_comment",
        "job_pickup_latitude",
        "job_pickup_longitude",
        "job_pickup_address",
        "customer_id",
        "customer_username",
        "customer_phone",
        "customer_email",
        "days_started",
        "started_datetime",
        "completed_datetime",
        "acknowledged_datetime",
        "arrived_datetime",
        "total_distance_travelled",
        "team_id",
        "fleet_rating"
})
@Generated("jsonschema2pojo")
public class Task {

    @JsonProperty("fleet_id")
    private Long fleetId;
    @JsonProperty("fleet_name")
    private String fleetName;
    @JsonProperty("fleet_phone")
    private String fleetPhone;
    @JsonProperty("fleet_latitude")
    private String fleetLatitude;
    @JsonProperty("fleet_longitude")
    private String fleetLongitude;
    @JsonProperty("fleet_image")
    private String fleetImage;
    @JsonProperty("order_id")
    private String orderId;
    @JsonProperty("job_pickup_name")
    private String jobPickupName;
    @JsonProperty("job_pickup_phone")
    private String jobPickupPhone;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("timezone")
    private String timezone;
    @JsonProperty("job_latitude")
    private String jobLatitude;
    @JsonProperty("job_longitude")
    private String jobLongitude;
    @JsonProperty("job_address")
    private String jobAddress;
    @JsonProperty("job_status")
    private Long jobStatus;
    @JsonProperty("job_description")
    private String jobDescription;
    @JsonProperty("has_pickup")
    private Long hasPickup;
    @JsonProperty("pickup_delivery_relationship")
    private String pickupDeliveryRelationship;
    @JsonProperty("completed_by_admin")
    private Long completedByAdmin;
    @JsonProperty("job_pickup_datetime")
    private String jobPickupDatetime;
    @JsonProperty("job_id")
    private Long jobId;
    @JsonProperty("job_delivery_datetime")
    private String jobDeliveryDatetime;
    @JsonProperty("job_type")
    private Long jobType;
    @JsonProperty("creation_datetime")
    private String creationDatetime;
    @JsonProperty("customer_comment")
    private Object customerComment;
    @JsonProperty("job_pickup_latitude")
    private String jobPickupLatitude;
    @JsonProperty("job_pickup_longitude")
    private String jobPickupLongitude;
    @JsonProperty("job_pickup_address")
    private String jobPickupAddress;
    @JsonProperty("customer_id")
    private Long customerId;
    @JsonProperty("customer_username")
    private String customerUsername;
    @JsonProperty("customer_phone")
    private String customerPhone;
    @JsonProperty("customer_email")
    private String customerEmail;
    @JsonProperty("days_started")
    private String daysStarted;
    @JsonProperty("started_datetime")
    private String startedDatetime;
    @JsonProperty("completed_datetime")
    private String completedDatetime;
    @JsonProperty("acknowledged_datetime")
    private String acknowledgedDatetime;
    @JsonProperty("arrived_datetime")
    private String arrivedDatetime;
    @JsonProperty("total_distance_travelled")
    private Long totalDistanceTravelled;
    @JsonProperty("team_id")
    private Long teamId;
    @JsonProperty("fleet_rating")
    private Long fleetRating;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     *
     */
    public Task() {
    }

    /**
     *
     * @param fleetName
     * @param jobStatus
     * @param customerComment
     * @param orderId
     * @param jobLongitude
     * @param timezone
     * @param arrivedDatetime
     * @param jobLatitude
     * @param fleetId
     * @param jobDeliveryDatetime
     * @param totalDistanceTravelled
     * @param customerPhone
     * @param jobPickupLatitude
     * @param jobPickupPhone
     * @param customerEmail
     * @param fleetPhone
     * @param customerId
     * @param fleetImage
     * @param jobPickupName
     * @param customerUsername
     * @param jobPickupDatetime
     * @param jobType
     * @param fleetLongitude
     * @param completedByAdmin
     * @param jobPickupAddress
     * @param daysStarted
     * @param userId
     * @param acknowledgedDatetime
     * @param creationDatetime
     * @param jobAddress
     * @param jobId
     * @param hasPickup
     * @param jobPickupLongitude
     * @param fleetRating
     * @param pickupDeliveryRelationship
     * @param startedDatetime
     * @param teamId
     * @param fleetLatitude
     * @param jobDescription
     * @param completedDatetime
     */
    public Task(Long fleetId, String fleetName, String fleetPhone, String fleetLatitude, String fleetLongitude, String fleetImage, String orderId, String jobPickupName, String jobPickupPhone, Long userId, String timezone, String jobLatitude, String jobLongitude, String jobAddress, Long jobStatus, String jobDescription, Long hasPickup, String pickupDeliveryRelationship, Long completedByAdmin, String jobPickupDatetime, Long jobId, String jobDeliveryDatetime, Long jobType, String creationDatetime, Object customerComment, String jobPickupLatitude, String jobPickupLongitude, String jobPickupAddress, Long customerId, String customerUsername, String customerPhone, String customerEmail, String daysStarted, String startedDatetime, String completedDatetime, String acknowledgedDatetime, String arrivedDatetime, Long totalDistanceTravelled, Long teamId, Long fleetRating) {
        super();
        this.fleetId = fleetId;
        this.fleetName = fleetName;
        this.fleetPhone = fleetPhone;
        this.fleetLatitude = fleetLatitude;
        this.fleetLongitude = fleetLongitude;
        this.fleetImage = fleetImage;
        this.orderId = orderId;
        this.jobPickupName = jobPickupName;
        this.jobPickupPhone = jobPickupPhone;
        this.userId = userId;
        this.timezone = timezone;
        this.jobLatitude = jobLatitude;
        this.jobLongitude = jobLongitude;
        this.jobAddress = jobAddress;
        this.jobStatus = jobStatus;
        this.jobDescription = jobDescription;
        this.hasPickup = hasPickup;
        this.pickupDeliveryRelationship = pickupDeliveryRelationship;
        this.completedByAdmin = completedByAdmin;
        this.jobPickupDatetime = jobPickupDatetime;
        this.jobId = jobId;
        this.jobDeliveryDatetime = jobDeliveryDatetime;
        this.jobType = jobType;
        this.creationDatetime = creationDatetime;
        this.customerComment = customerComment;
        this.jobPickupLatitude = jobPickupLatitude;
        this.jobPickupLongitude = jobPickupLongitude;
        this.jobPickupAddress = jobPickupAddress;
        this.customerId = customerId;
        this.customerUsername = customerUsername;
        this.customerPhone = customerPhone;
        this.customerEmail = customerEmail;
        this.daysStarted = daysStarted;
        this.startedDatetime = startedDatetime;
        this.completedDatetime = completedDatetime;
        this.acknowledgedDatetime = acknowledgedDatetime;
        this.arrivedDatetime = arrivedDatetime;
        this.totalDistanceTravelled = totalDistanceTravelled;
        this.teamId = teamId;
        this.fleetRating = fleetRating;
    }

    @JsonProperty("fleet_id")
    public Long getFleetId() {
        return fleetId;
    }

    @JsonProperty("fleet_id")
    public void setFleetId(Long fleetId) {
        this.fleetId = fleetId;
    }

    public Task withFleetId(Long fleetId) {
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

    public Task withFleetName(String fleetName) {
        this.fleetName = fleetName;
        return this;
    }

    @JsonProperty("fleet_phone")
    public String getFleetPhone() {
        return fleetPhone;
    }

    @JsonProperty("fleet_phone")
    public void setFleetPhone(String fleetPhone) {
        this.fleetPhone = fleetPhone;
    }

    public Task withFleetPhone(String fleetPhone) {
        this.fleetPhone = fleetPhone;
        return this;
    }

    @JsonProperty("fleet_latitude")
    public String getFleetLatitude() {
        return fleetLatitude;
    }

    @JsonProperty("fleet_latitude")
    public void setFleetLatitude(String fleetLatitude) {
        this.fleetLatitude = fleetLatitude;
    }

    public Task withFleetLatitude(String fleetLatitude) {
        this.fleetLatitude = fleetLatitude;
        return this;
    }

    @JsonProperty("fleet_longitude")
    public String getFleetLongitude() {
        return fleetLongitude;
    }

    @JsonProperty("fleet_longitude")
    public void setFleetLongitude(String fleetLongitude) {
        this.fleetLongitude = fleetLongitude;
    }

    public Task withFleetLongitude(String fleetLongitude) {
        this.fleetLongitude = fleetLongitude;
        return this;
    }

    @JsonProperty("fleet_image")
    public String getFleetImage() {
        return fleetImage;
    }

    @JsonProperty("fleet_image")
    public void setFleetImage(String fleetImage) {
        this.fleetImage = fleetImage;
    }

    public Task withFleetImage(String fleetImage) {
        this.fleetImage = fleetImage;
        return this;
    }

    @JsonProperty("order_id")
    public String getOrderId() {
        return orderId;
    }

    @JsonProperty("order_id")
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Task withOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    @JsonProperty("job_pickup_name")
    public String getJobPickupName() {
        return jobPickupName;
    }

    @JsonProperty("job_pickup_name")
    public void setJobPickupName(String jobPickupName) {
        this.jobPickupName = jobPickupName;
    }

    public Task withJobPickupName(String jobPickupName) {
        this.jobPickupName = jobPickupName;
        return this;
    }

    @JsonProperty("job_pickup_phone")
    public String getJobPickupPhone() {
        return jobPickupPhone;
    }

    @JsonProperty("job_pickup_phone")
    public void setJobPickupPhone(String jobPickupPhone) {
        this.jobPickupPhone = jobPickupPhone;
    }

    public Task withJobPickupPhone(String jobPickupPhone) {
        this.jobPickupPhone = jobPickupPhone;
        return this;
    }

    @JsonProperty("user_id")
    public Long getUserId() {
        return userId;
    }

    @JsonProperty("user_id")
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Task withUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    @JsonProperty("timezone")
    public String getTimezone() {
        return timezone;
    }

    @JsonProperty("timezone")
    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Task withTimezone(String timezone) {
        this.timezone = timezone;
        return this;
    }

    @JsonProperty("job_latitude")
    public String getJobLatitude() {
        return jobLatitude;
    }

    @JsonProperty("job_latitude")
    public void setJobLatitude(String jobLatitude) {
        this.jobLatitude = jobLatitude;
    }

    public Task withJobLatitude(String jobLatitude) {
        this.jobLatitude = jobLatitude;
        return this;
    }

    @JsonProperty("job_longitude")
    public String getJobLongitude() {
        return jobLongitude;
    }

    @JsonProperty("job_longitude")
    public void setJobLongitude(String jobLongitude) {
        this.jobLongitude = jobLongitude;
    }

    public Task withJobLongitude(String jobLongitude) {
        this.jobLongitude = jobLongitude;
        return this;
    }

    @JsonProperty("job_address")
    public String getJobAddress() {
        return jobAddress;
    }

    @JsonProperty("job_address")
    public void setJobAddress(String jobAddress) {
        this.jobAddress = jobAddress;
    }

    public Task withJobAddress(String jobAddress) {
        this.jobAddress = jobAddress;
        return this;
    }

    @JsonProperty("job_status")
    public Long getJobStatus() {
        return jobStatus;
    }

    @JsonProperty("job_status")
    public void setJobStatus(Long jobStatus) {
        this.jobStatus = jobStatus;
    }

    public Task withJobStatus(Long jobStatus) {
        this.jobStatus = jobStatus;
        return this;
    }

    @JsonProperty("job_description")
    public String getJobDescription() {
        return jobDescription;
    }

    @JsonProperty("job_description")
    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public Task withJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
        return this;
    }

    @JsonProperty("has_pickup")
    public Long getHasPickup() {
        return hasPickup;
    }

    @JsonProperty("has_pickup")
    public void setHasPickup(Long hasPickup) {
        this.hasPickup = hasPickup;
    }

    public Task withHasPickup(Long hasPickup) {
        this.hasPickup = hasPickup;
        return this;
    }

    @JsonProperty("pickup_delivery_relationship")
    public String getPickupDeliveryRelationship() {
        return pickupDeliveryRelationship;
    }

    @JsonProperty("pickup_delivery_relationship")
    public void setPickupDeliveryRelationship(String pickupDeliveryRelationship) {
        this.pickupDeliveryRelationship = pickupDeliveryRelationship;
    }

    public Task withPickupDeliveryRelationship(String pickupDeliveryRelationship) {
        this.pickupDeliveryRelationship = pickupDeliveryRelationship;
        return this;
    }

    @JsonProperty("completed_by_admin")
    public Long getCompletedByAdmin() {
        return completedByAdmin;
    }

    @JsonProperty("completed_by_admin")
    public void setCompletedByAdmin(Long completedByAdmin) {
        this.completedByAdmin = completedByAdmin;
    }

    public Task withCompletedByAdmin(Long completedByAdmin) {
        this.completedByAdmin = completedByAdmin;
        return this;
    }

    @JsonProperty("job_pickup_datetime")
    public String getJobPickupDatetime() {
        return jobPickupDatetime;
    }

    @JsonProperty("job_pickup_datetime")
    public void setJobPickupDatetime(String jobPickupDatetime) {
        this.jobPickupDatetime = jobPickupDatetime;
    }

    public Task withJobPickupDatetime(String jobPickupDatetime) {
        this.jobPickupDatetime = jobPickupDatetime;
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

    public Task withJobId(Long jobId) {
        this.jobId = jobId;
        return this;
    }

    @JsonProperty("job_delivery_datetime")
    public String getJobDeliveryDatetime() {
        return jobDeliveryDatetime;
    }

    @JsonProperty("job_delivery_datetime")
    public void setJobDeliveryDatetime(String jobDeliveryDatetime) {
        this.jobDeliveryDatetime = jobDeliveryDatetime;
    }

    public Task withJobDeliveryDatetime(String jobDeliveryDatetime) {
        this.jobDeliveryDatetime = jobDeliveryDatetime;
        return this;
    }

    @JsonProperty("job_type")
    public Long getJobType() {
        return jobType;
    }

    @JsonProperty("job_type")
    public void setJobType(Long jobType) {
        this.jobType = jobType;
    }

    public Task withJobType(Long jobType) {
        this.jobType = jobType;
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

    public Task withCreationDatetime(String creationDatetime) {
        this.creationDatetime = creationDatetime;
        return this;
    }

    @JsonProperty("customer_comment")
    public Object getCustomerComment() {
        return customerComment;
    }

    @JsonProperty("customer_comment")
    public void setCustomerComment(Object customerComment) {
        this.customerComment = customerComment;
    }

    public Task withCustomerComment(Object customerComment) {
        this.customerComment = customerComment;
        return this;
    }

    @JsonProperty("job_pickup_latitude")
    public String getJobPickupLatitude() {
        return jobPickupLatitude;
    }

    @JsonProperty("job_pickup_latitude")
    public void setJobPickupLatitude(String jobPickupLatitude) {
        this.jobPickupLatitude = jobPickupLatitude;
    }

    public Task withJobPickupLatitude(String jobPickupLatitude) {
        this.jobPickupLatitude = jobPickupLatitude;
        return this;
    }

    @JsonProperty("job_pickup_longitude")
    public String getJobPickupLongitude() {
        return jobPickupLongitude;
    }

    @JsonProperty("job_pickup_longitude")
    public void setJobPickupLongitude(String jobPickupLongitude) {
        this.jobPickupLongitude = jobPickupLongitude;
    }

    public Task withJobPickupLongitude(String jobPickupLongitude) {
        this.jobPickupLongitude = jobPickupLongitude;
        return this;
    }

    @JsonProperty("job_pickup_address")
    public String getJobPickupAddress() {
        return jobPickupAddress;
    }

    @JsonProperty("job_pickup_address")
    public void setJobPickupAddress(String jobPickupAddress) {
        this.jobPickupAddress = jobPickupAddress;
    }

    public Task withJobPickupAddress(String jobPickupAddress) {
        this.jobPickupAddress = jobPickupAddress;
        return this;
    }

    @JsonProperty("customer_id")
    public Long getCustomerId() {
        return customerId;
    }

    @JsonProperty("customer_id")
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Task withCustomerId(Long customerId) {
        this.customerId = customerId;
        return this;
    }

    @JsonProperty("customer_username")
    public String getCustomerUsername() {
        return customerUsername;
    }

    @JsonProperty("customer_username")
    public void setCustomerUsername(String customerUsername) {
        this.customerUsername = customerUsername;
    }

    public Task withCustomerUsername(String customerUsername) {
        this.customerUsername = customerUsername;
        return this;
    }

    @JsonProperty("customer_phone")
    public String getCustomerPhone() {
        return customerPhone;
    }

    @JsonProperty("customer_phone")
    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public Task withCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
        return this;
    }

    @JsonProperty("customer_email")
    public String getCustomerEmail() {
        return customerEmail;
    }

    @JsonProperty("customer_email")
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public Task withCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
        return this;
    }

    @JsonProperty("days_started")
    public String getDaysStarted() {
        return daysStarted;
    }

    @JsonProperty("days_started")
    public void setDaysStarted(String daysStarted) {
        this.daysStarted = daysStarted;
    }

    public Task withDaysStarted(String daysStarted) {
        this.daysStarted = daysStarted;
        return this;
    }

    @JsonProperty("started_datetime")
    public String getStartedDatetime() {
        return startedDatetime;
    }

    @JsonProperty("started_datetime")
    public void setStartedDatetime(String startedDatetime) {
        this.startedDatetime = startedDatetime;
    }

    public Task withStartedDatetime(String startedDatetime) {
        this.startedDatetime = startedDatetime;
        return this;
    }

    @JsonProperty("completed_datetime")
    public String getCompletedDatetime() {
        return completedDatetime;
    }

    @JsonProperty("completed_datetime")
    public void setCompletedDatetime(String completedDatetime) {
        this.completedDatetime = completedDatetime;
    }

    public Task withCompletedDatetime(String completedDatetime) {
        this.completedDatetime = completedDatetime;
        return this;
    }

    @JsonProperty("acknowledged_datetime")
    public String getAcknowledgedDatetime() {
        return acknowledgedDatetime;
    }

    @JsonProperty("acknowledged_datetime")
    public void setAcknowledgedDatetime(String acknowledgedDatetime) {
        this.acknowledgedDatetime = acknowledgedDatetime;
    }

    public Task withAcknowledgedDatetime(String acknowledgedDatetime) {
        this.acknowledgedDatetime = acknowledgedDatetime;
        return this;
    }

    @JsonProperty("arrived_datetime")
    public String getArrivedDatetime() {
        return arrivedDatetime;
    }

    @JsonProperty("arrived_datetime")
    public void setArrivedDatetime(String arrivedDatetime) {
        this.arrivedDatetime = arrivedDatetime;
    }

    public Task withArrivedDatetime(String arrivedDatetime) {
        this.arrivedDatetime = arrivedDatetime;
        return this;
    }

    @JsonProperty("total_distance_travelled")
    public Long getTotalDistanceTravelled() {
        return totalDistanceTravelled;
    }

    @JsonProperty("total_distance_travelled")
    public void setTotalDistanceTravelled(Long totalDistanceTravelled) {
        this.totalDistanceTravelled = totalDistanceTravelled;
    }

    public Task withTotalDistanceTravelled(Long totalDistanceTravelled) {
        this.totalDistanceTravelled = totalDistanceTravelled;
        return this;
    }

    @JsonProperty("team_id")
    public Long getTeamId() {
        return teamId;
    }

    @JsonProperty("team_id")
    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Task withTeamId(Long teamId) {
        this.teamId = teamId;
        return this;
    }

    @JsonProperty("fleet_rating")
    public Long getFleetRating() {
        return fleetRating;
    }

    @JsonProperty("fleet_rating")
    public void setFleetRating(Long fleetRating) {
        this.fleetRating = fleetRating;
    }

    public Task withFleetRating(Long fleetRating) {
        this.fleetRating = fleetRating;
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

    public Task withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Task.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("fleetId");
        sb.append('=');
        sb.append(((this.fleetId == null)?"<null>":this.fleetId));
        sb.append(',');
        sb.append("fleetName");
        sb.append('=');
        sb.append(((this.fleetName == null)?"<null>":this.fleetName));
        sb.append(',');
        sb.append("fleetPhone");
        sb.append('=');
        sb.append(((this.fleetPhone == null)?"<null>":this.fleetPhone));
        sb.append(',');
        sb.append("fleetLatitude");
        sb.append('=');
        sb.append(((this.fleetLatitude == null)?"<null>":this.fleetLatitude));
        sb.append(',');
        sb.append("fleetLongitude");
        sb.append('=');
        sb.append(((this.fleetLongitude == null)?"<null>":this.fleetLongitude));
        sb.append(',');
        sb.append("fleetImage");
        sb.append('=');
        sb.append(((this.fleetImage == null)?"<null>":this.fleetImage));
        sb.append(',');
        sb.append("orderId");
        sb.append('=');
        sb.append(((this.orderId == null)?"<null>":this.orderId));
        sb.append(',');
        sb.append("jobPickupName");
        sb.append('=');
        sb.append(((this.jobPickupName == null)?"<null>":this.jobPickupName));
        sb.append(',');
        sb.append("jobPickupPhone");
        sb.append('=');
        sb.append(((this.jobPickupPhone == null)?"<null>":this.jobPickupPhone));
        sb.append(',');
        sb.append("userId");
        sb.append('=');
        sb.append(((this.userId == null)?"<null>":this.userId));
        sb.append(',');
        sb.append("timezone");
        sb.append('=');
        sb.append(((this.timezone == null)?"<null>":this.timezone));
        sb.append(',');
        sb.append("jobLatitude");
        sb.append('=');
        sb.append(((this.jobLatitude == null)?"<null>":this.jobLatitude));
        sb.append(',');
        sb.append("jobLongitude");
        sb.append('=');
        sb.append(((this.jobLongitude == null)?"<null>":this.jobLongitude));
        sb.append(',');
        sb.append("jobAddress");
        sb.append('=');
        sb.append(((this.jobAddress == null)?"<null>":this.jobAddress));
        sb.append(',');
        sb.append("jobStatus");
        sb.append('=');
        sb.append(((this.jobStatus == null)?"<null>":this.jobStatus));
        sb.append(',');
        sb.append("jobDescription");
        sb.append('=');
        sb.append(((this.jobDescription == null)?"<null>":this.jobDescription));
        sb.append(',');
        sb.append("hasPickup");
        sb.append('=');
        sb.append(((this.hasPickup == null)?"<null>":this.hasPickup));
        sb.append(',');
        sb.append("pickupDeliveryRelationship");
        sb.append('=');
        sb.append(((this.pickupDeliveryRelationship == null)?"<null>":this.pickupDeliveryRelationship));
        sb.append(',');
        sb.append("completedByAdmin");
        sb.append('=');
        sb.append(((this.completedByAdmin == null)?"<null>":this.completedByAdmin));
        sb.append(',');
        sb.append("jobPickupDatetime");
        sb.append('=');
        sb.append(((this.jobPickupDatetime == null)?"<null>":this.jobPickupDatetime));
        sb.append(',');
        sb.append("jobId");
        sb.append('=');
        sb.append(((this.jobId == null)?"<null>":this.jobId));
        sb.append(',');
        sb.append("jobDeliveryDatetime");
        sb.append('=');
        sb.append(((this.jobDeliveryDatetime == null)?"<null>":this.jobDeliveryDatetime));
        sb.append(',');
        sb.append("jobType");
        sb.append('=');
        sb.append(((this.jobType == null)?"<null>":this.jobType));
        sb.append(',');
        sb.append("creationDatetime");
        sb.append('=');
        sb.append(((this.creationDatetime == null)?"<null>":this.creationDatetime));
        sb.append(',');
        sb.append("customerComment");
        sb.append('=');
        sb.append(((this.customerComment == null)?"<null>":this.customerComment));
        sb.append(',');
        sb.append("jobPickupLatitude");
        sb.append('=');
        sb.append(((this.jobPickupLatitude == null)?"<null>":this.jobPickupLatitude));
        sb.append(',');
        sb.append("jobPickupLongitude");
        sb.append('=');
        sb.append(((this.jobPickupLongitude == null)?"<null>":this.jobPickupLongitude));
        sb.append(',');
        sb.append("jobPickupAddress");
        sb.append('=');
        sb.append(((this.jobPickupAddress == null)?"<null>":this.jobPickupAddress));
        sb.append(',');
        sb.append("customerId");
        sb.append('=');
        sb.append(((this.customerId == null)?"<null>":this.customerId));
        sb.append(',');
        sb.append("customerUsername");
        sb.append('=');
        sb.append(((this.customerUsername == null)?"<null>":this.customerUsername));
        sb.append(',');
        sb.append("customerPhone");
        sb.append('=');
        sb.append(((this.customerPhone == null)?"<null>":this.customerPhone));
        sb.append(',');
        sb.append("customerEmail");
        sb.append('=');
        sb.append(((this.customerEmail == null)?"<null>":this.customerEmail));
        sb.append(',');
        sb.append("daysStarted");
        sb.append('=');
        sb.append(((this.daysStarted == null)?"<null>":this.daysStarted));
        sb.append(',');
        sb.append("startedDatetime");
        sb.append('=');
        sb.append(((this.startedDatetime == null)?"<null>":this.startedDatetime));
        sb.append(',');
        sb.append("completedDatetime");
        sb.append('=');
        sb.append(((this.completedDatetime == null)?"<null>":this.completedDatetime));
        sb.append(',');
        sb.append("acknowledgedDatetime");
        sb.append('=');
        sb.append(((this.acknowledgedDatetime == null)?"<null>":this.acknowledgedDatetime));
        sb.append(',');
        sb.append("arrivedDatetime");
        sb.append('=');
        sb.append(((this.arrivedDatetime == null)?"<null>":this.arrivedDatetime));
        sb.append(',');
        sb.append("totalDistanceTravelled");
        sb.append('=');
        sb.append(((this.totalDistanceTravelled == null)?"<null>":this.totalDistanceTravelled));
        sb.append(',');
        sb.append("teamId");
        sb.append('=');
        sb.append(((this.teamId == null)?"<null>":this.teamId));
        sb.append(',');
        sb.append("fleetRating");
        sb.append('=');
        sb.append(((this.fleetRating == null)?"<null>":this.fleetRating));
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
