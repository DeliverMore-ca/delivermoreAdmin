
package ca.admin.delivermore.tookan;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import ca.admin.delivermore.data.entity.AbstractEntity;
import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "fleet_id",
    "device_type",
    "total_rating",
    "total_rated_tasks",
    "is_active",
    "has_gps_accuracy",
    "username",
    "name",
    "login_id",
    "transport_type",
    "transport_desc",
    "license",
    "email",
    "phone",
    "battery_level",
    "registration_status",
    "latitude",
    "longitude",
    "tags",
    "fleet_thumb_image",
    "fleet_image",
    "status",
    "timezone",
    "fleet_type",
    "block_reason",
    "is_available"
})
@Generated("jsonschema2pojo")
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class Driver {

    @JsonProperty("fleet_id")
    @Id
    private Long fleetId;
    @JsonProperty("device_type")
    private Long deviceType;
    @JsonProperty("total_rating")
    private Long totalRating;
    @JsonProperty("total_rated_tasks")
    private Long totalRatedTasks;
    @JsonProperty("is_active")
    private Long isActive;
    @JsonProperty("has_gps_accuracy")
    private Long hasGpsAccuracy;
    @JsonProperty("username")
    private String username;
    @JsonProperty("name")
    private String name;
    @JsonProperty("login_id")
    private String loginId;
    @JsonProperty("transport_type")
    private Long transportType;
    @JsonProperty("transport_desc")
    private String transportDesc;
    @JsonProperty("license")
    private String license;
    @JsonProperty("email")
    private String email;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("battery_level")
    private Long batteryLevel;
    @JsonProperty("registration_status")
    private Long registrationStatus;
    @JsonProperty("latitude")
    private String latitude;
    @JsonProperty("longitude")
    private String longitude;
    @JsonProperty("tags")
    private String tags;
    @JsonProperty("fleet_thumb_image")
    private String fleetThumbImage;
    @JsonProperty("fleet_image")
    private String fleetImage;
    @JsonProperty("status")
    private Long status;
    @JsonProperty("timezone")
    private String timezone;
    @JsonProperty("fleet_type")
    private Long fleetType;
    /*
    @JsonProperty("block_reason")
    @OneToOne(targetEntity = BlockReason.class)
    private BlockReason blockReason;
     */
    @JsonProperty("is_available")
    private Long isAvailable;
    /*
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
     */

    /**
     * No args constructor for use in serialization
     * 
     */
    public Driver() {
    }

    /**
     * 
     * @param isAvailable
     * @param registrationStatus
     * @param loginId
     * @param timezone
     * @param latitude
     * @param totalRatedTasks
     * @param transportDesc
     * @param fleetId
     * @param isActive
     * @param fleetType
     * @param transportType
     * @param fleetImage
     * @param blockReason
     * @param email
     * @param batteryLevel
     * @param longitude
     * @param deviceType
     * @param totalRating
     * @param hasGpsAccuracy
     * @param tags
     * @param license
     * @param phone
     * @param name
     * @param fleetThumbImage
     * @param username
     * @param status
     */
    public Driver(Long fleetId, Long deviceType, Long totalRating, Long totalRatedTasks, Long isActive, Long hasGpsAccuracy, String username, String name, String loginId, Long transportType, String transportDesc, String license, String email, String phone, Long batteryLevel, Long registrationStatus, String latitude, String longitude, String tags, String fleetThumbImage, String fleetImage, Long status, String timezone, Long fleetType, Long isAvailable) {
        super();
        this.fleetId = fleetId;
        this.deviceType = deviceType;
        this.totalRating = totalRating;
        this.totalRatedTasks = totalRatedTasks;
        this.isActive = isActive;
        this.hasGpsAccuracy = hasGpsAccuracy;
        this.username = username;
        this.name = name;
        this.loginId = loginId;
        this.transportType = transportType;
        this.transportDesc = transportDesc;
        this.license = license;
        this.email = email;
        this.phone = phone;
        this.batteryLevel = batteryLevel;
        this.registrationStatus = registrationStatus;
        this.latitude = latitude;
        this.longitude = longitude;
        this.tags = tags;
        this.fleetThumbImage = fleetThumbImage;
        this.fleetImage = fleetImage;
        this.status = status;
        this.timezone = timezone;
        this.fleetType = fleetType;
        this.isAvailable = isAvailable;
    }

    @JsonProperty("fleet_id")
    public Long getFleetId() {
        return fleetId;
    }

    @JsonProperty("fleet_id")
    public void setFleetId(Long fleetId) {
        this.fleetId = fleetId;
    }

    @JsonProperty("device_type")
    public Long getDeviceType() {
        return deviceType;
    }

    @JsonProperty("device_type")
    public void setDeviceType(Long deviceType) {
        this.deviceType = deviceType;
    }

    @JsonProperty("total_rating")
    public Long getTotalRating() {
        return totalRating;
    }

    @JsonProperty("total_rating")
    public void setTotalRating(Long totalRating) {
        this.totalRating = totalRating;
    }

    @JsonProperty("total_rated_tasks")
    public Long getTotalRatedTasks() {
        return totalRatedTasks;
    }

    @JsonProperty("total_rated_tasks")
    public void setTotalRatedTasks(Long totalRatedTasks) {
        this.totalRatedTasks = totalRatedTasks;
    }

    @JsonProperty("is_active")
    public Long getIsActive() {
        return isActive;
    }

    @JsonProperty("is_active")
    public void setIsActive(Long isActive) {
        this.isActive = isActive;
    }

    @JsonProperty("has_gps_accuracy")
    public Long getHasGpsAccuracy() {
        return hasGpsAccuracy;
    }

    @JsonProperty("has_gps_accuracy")
    public void setHasGpsAccuracy(Long hasGpsAccuracy) {
        this.hasGpsAccuracy = hasGpsAccuracy;
    }

    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    @JsonProperty("username")
    public void setUsername(String username) {
        this.username = username;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("login_id")
    public String getLoginId() {
        return loginId;
    }

    @JsonProperty("login_id")
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    @JsonProperty("transport_type")
    public Long getTransportType() {
        return transportType;
    }

    @JsonProperty("transport_type")
    public void setTransportType(Long transportType) {
        this.transportType = transportType;
    }

    @JsonProperty("transport_desc")
    public String getTransportDesc() {
        return transportDesc;
    }

    @JsonProperty("transport_desc")
    public void setTransportDesc(String transportDesc) {
        this.transportDesc = transportDesc;
    }

    @JsonProperty("license")
    public String getLicense() {
        return license;
    }

    @JsonProperty("license")
    public void setLicense(String license) {
        this.license = license;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("phone")
    public String getPhone() {
        return phone;
    }

    @JsonProperty("phone")
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @JsonProperty("battery_level")
    public Long getBatteryLevel() {
        return batteryLevel;
    }

    @JsonProperty("battery_level")
    public void setBatteryLevel(Long batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    @JsonProperty("registration_status")
    public Long getRegistrationStatus() {
        return registrationStatus;
    }

    @JsonProperty("registration_status")
    public void setRegistrationStatus(Long registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    @JsonProperty("latitude")
    public String getLatitude() {
        return latitude;
    }

    @JsonProperty("latitude")
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @JsonProperty("longitude")
    public String getLongitude() {
        return longitude;
    }

    @JsonProperty("longitude")
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @JsonProperty("tags")
    public String getTags() {
        return tags;
    }

    @JsonProperty("tags")
    public void setTags(String tags) {
        this.tags = tags;
    }

    @JsonProperty("fleet_thumb_image")
    public String getFleetThumbImage() {
        return fleetThumbImage;
    }

    @JsonProperty("fleet_thumb_image")
    public void setFleetThumbImage(String fleetThumbImage) {
        this.fleetThumbImage = fleetThumbImage;
    }

    @JsonProperty("fleet_image")
    public String getFleetImage() {
        return fleetImage;
    }

    @JsonProperty("fleet_image")
    public void setFleetImage(String fleetImage) {
        this.fleetImage = fleetImage;
    }

    @JsonProperty("status")
    public Long getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(Long status) {
        this.status = status;
    }

    @JsonProperty("timezone")
    public String getTimezone() {
        return timezone;
    }

    @JsonProperty("timezone")
    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    @JsonProperty("fleet_type")
    public Long getFleetType() {
        return fleetType;
    }

    @JsonProperty("fleet_type")
    public void setFleetType(Long fleetType) {
        this.fleetType = fleetType;
    }

    /*
    @JsonProperty("block_reason")
    public BlockReason getBlockReason() {
        return blockReason;
    }

    @JsonProperty("block_reason")
    public void setBlockReason(BlockReason blockReason) {
        this.blockReason = blockReason;
    }

     */

    @JsonProperty("is_available")
    public Long getIsAvailable() {
        return isAvailable;
    }

    @JsonProperty("is_available")
    public void setIsAvailable(Long isAvailable) {
        this.isAvailable = isAvailable;
    }

    /*
    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
     */

    @Override
    public String toString() {
        return "Driver{" +
                "fleetId=" + fleetId +
                ", deviceType=" + deviceType +
                ", totalRating=" + totalRating +
                ", totalRatedTasks=" + totalRatedTasks +
                ", isActive=" + isActive +
                ", hasGpsAccuracy=" + hasGpsAccuracy +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", loginId='" + loginId + '\'' +
                ", transportType=" + transportType +
                ", transportDesc='" + transportDesc + '\'' +
                ", license='" + license + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", batteryLevel=" + batteryLevel +
                ", registrationStatus=" + registrationStatus +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", tags='" + tags + '\'' +
                ", fleetThumbImage='" + fleetThumbImage + '\'' +
                ", fleetImage='" + fleetImage + '\'' +
                ", status=" + status +
                ", timezone='" + timezone + '\'' +
                ", fleetType=" + fleetType +
                ", isAvailable=" + isAvailable +
                '}';
    }
}
