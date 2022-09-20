
package ca.admin.delivermore.tookan;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;

import ca.admin.delivermore.data.Config;
import ca.admin.delivermore.data.entity.DriverPayoutEntity;
import ca.admin.delivermore.data.entity.Restaurant;
import ca.admin.delivermore.data.entity.TaskEntity;
import ca.admin.delivermore.data.global.OrderDetail;
import ca.admin.delivermore.data.service.DriversRepository;
import ca.admin.delivermore.data.service.OrderDetailRepository;
import ca.admin.delivermore.data.service.RestaurantRepository;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "job_id",
        "created_by",
        "order_id",
        "recurring_id",
        "recurring_count",
        "partner_order_id",
        "team_id",
        "vertical",
        "merchant_id",
        "geofence",
        "tags",
        "auto_assignment",
        "dispatcher_id",
        "job_hash",
        "has_pickup",
        "has_delivery",
        "is_routed",
        "pickup_delivery_relationship",
        "job_description",
        "job_pickup_datetime",
        "job_pickup_name",
        "job_pickup_phone",
        "job_delivery_datetime",
        "job_pickup_latitude",
        "job_pickup_longitude",
        "job_pickup_address",
        "job_pickup_email",
        "job_latitude",
        "job_longitude",
        "customer_username",
        "customer_email",
        "customer_phone",
        "job_address",
        "creation_datetime",
        "fleet_id",
        "user_id",
        "fleet_rating",
        "customer_comment",
        "is_customer_rated",
        "customer_id",
        "arrived_datetime",
        "started_datetime",
        "completed_datetime",
        "acknowledged_datetime",
        "job_status",
        "is_active",
        "job_type",
        "completed_by_admin",
        "open_tracking_link",
        "timezone",
        "job_time",
        "job_date",
        "job_time_utc",
        "job_date_utc",
        "total_distance_travelled",
        "form_id",
        "customer_rating",
        "driver_comment",
        "remarks",
        "barcode",
        "ride_type",
        "matched_pickup_delivery_relationship",
        "custom_field",
        "tracking_link",
        "task_history",
        "job_additional_info"
})
@Generated("jsonschema2pojo")
//@EnableJpaRepositories(basePackages = { "ca.admin.delivermore" })
public class TaskDetail {

    enum TaskType {
        CUSTOM,
        FORM,
        GLOBAL
    }

    private RestaurantRepository restaurantRepository;
    private OrderDetailRepository orderDetailRepository;
    private DriversRepository driversRepository;

    private TaskType taskType;
    private Restaurant restaurant = new Restaurant();
    private Restaurant unknownRestaurant = new Restaurant(1L, "Unknown", 0.0);
    private Driver driver = new Driver();
    private Config config = new Config();

    @JsonProperty("job_id")
    private Long jobId;
    @JsonProperty("created_by")
    private Long createdBy;
    @JsonProperty("order_id")
    private String orderId;
    @JsonProperty("recurring_id")
    private String recurringId;
    @JsonProperty("recurring_count")
    private Long recurringCount;
    @JsonProperty("partner_order_id")
    private Object partnerOrderId;
    @JsonProperty("team_id")
    private Long teamId;
    @JsonProperty("vertical")
    private Long vertical;
    @JsonProperty("merchant_id")
    private Long merchantId;
    @JsonProperty("geofence")
    private Long geofence;
    @JsonProperty("tags")
    private String tags;
    @JsonProperty("auto_assignment")
    private Long autoAssignment;
    @JsonProperty("dispatcher_id")
    private Long dispatcherId;
    @JsonProperty("job_hash")
    private String jobHash;
    @JsonProperty("has_pickup")
    private Long hasPickup;
    @JsonProperty("has_delivery")
    private Long hasDelivery;
    @JsonProperty("is_routed")
    private Long isRouted;
    @JsonProperty("pickup_delivery_relationship")
    private String pickupDeliveryRelationship;
    @JsonProperty("job_description")
    private String jobDescription;
    @JsonProperty("job_pickup_datetime")
    private String jobPickupDatetime;
    @JsonProperty("job_pickup_name")
    private String jobPickupName;
    @JsonProperty("job_pickup_phone")
    private String jobPickupPhone;
    @JsonProperty("job_delivery_datetime")
    private String jobDeliveryDatetime;
    @JsonProperty("job_pickup_latitude")
    private String jobPickupLatitude;
    @JsonProperty("job_pickup_longitude")
    private String jobPickupLongitude;
    @JsonProperty("job_pickup_address")
    private String jobPickupAddress;
    @JsonProperty("job_pickup_email")
    private Object jobPickupEmail;
    @JsonProperty("job_latitude")
    private String jobLatitude;
    @JsonProperty("job_longitude")
    private String jobLongitude;
    @JsonProperty("customer_username")
    private String customerUsername;
    @JsonProperty("customer_email")
    private String customerEmail;
    @JsonProperty("customer_phone")
    private String customerPhone;
    @JsonProperty("job_address")
    private String jobAddress;
    @JsonProperty("creation_datetime")
    private String creationDatetime;
    @JsonProperty("fleet_id")
    private Long fleetId;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("fleet_rating")
    private Long fleetRating;
    @JsonProperty("customer_comment")
    private Object customerComment;
    @JsonProperty("is_customer_rated")
    private Long isCustomerRated;
    @JsonProperty("customer_id")
    private Long customerId;
    @JsonProperty("arrived_datetime")
    private String arrivedDatetime;
    @JsonProperty("started_datetime")
    private String startedDatetime;
    @JsonProperty("completed_datetime")
    private String completedDatetime;
    @JsonProperty("acknowledged_datetime")
    private String acknowledgedDatetime;
    @JsonProperty("job_status")
    private Long jobStatus;
    @JsonProperty("is_active")
    private Long isActive;
    @JsonProperty("job_type")
    private Long jobType;
    @JsonProperty("completed_by_admin")
    private Long completedByAdmin;
    @JsonProperty("open_tracking_link")
    private Long openTrackingLink;
    @JsonProperty("timezone")
    private String timezone;
    @JsonProperty("job_time")
    private String jobTime;
    @JsonProperty("job_date")
    private String jobDate;
    @JsonProperty("job_time_utc")
    private String jobTimeUtc;
    @JsonProperty("job_date_utc")
    private String jobDateUtc;
    @JsonProperty("total_distance_travelled")
    private Long totalDistanceTravelled;
    @JsonProperty("form_id")
    private Long formId;
    @JsonProperty("customer_rating")
    private Object customerRating;
    @JsonProperty("driver_comment")
    private Object driverComment;
    @JsonProperty("remarks")
    private Object remarks;
    @JsonProperty("barcode")
    private Object barcode;
    @JsonProperty("ride_type")
    private Long rideType;
    @JsonProperty("matched_pickup_delivery_relationship")
    private Object matchedPickupDeliveryRelationship;

    @JsonProperty("custom_field")
    private List<CustomField> customField = null;

    @JsonProperty("tracking_link")
    private String trackingLink;

    @JsonProperty("task_history")
    private List<TaskHistory> taskHistory = null;

    @JsonProperty("job_additional_info")
    private JobAdditionalInfo jobAdditionalInfo;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    //derived fields
    private String restaurantIdString = null;
    private Boolean restaurantIsGlobal = Boolean.FALSE;
    private Boolean initComplete = Boolean.FALSE;
    private Double tipInNotes = 0.0;
    private String notes = null;
    private Boolean tipInNotesIssue = Boolean.FALSE;
    private Double tipGlobal = null;
    private Double deliveryFee = 0.0;
    private Double receiptTotal = null;
    private String paymentMethod = null;
    private Double paidToVendor = null;
    private Double serviceFeePercent = null;
    private Double serviceFeeAmount = 0.0;
    private Double totalWithFees = null;
    private TaskEntity taskEntity = new TaskEntity();
    private DriverPayoutEntity driverPayoutEntity = new DriverPayoutEntity();
    private Boolean taskEntityLoaded = Boolean.FALSE;
    private String templateId = null;
    private Boolean skipGlobal = Boolean.FALSE;

    /**
     * No args constructor for use in serialization
     *
     */
    public TaskDetail() {
    }

    /**
     *
     * @param driverComment
     * @param customerComment
     * @param orderId
     * @param jobLongitude
     * @param isCustomerRated
     * @param taskHistory
     * @param arrivedDatetime
     * @param jobLatitude
     * @param jobDeliveryDatetime
     * @param isActive
     * @param isRouted
     * @param totalDistanceTravelled
     * @param merchantId
     * @param jobPickupPhone
     * @param customerEmail
     * @param jobPickupDatetime
     * @param matchedPickupDeliveryRelationship
     * @param barcode
     * @param jobDateUtc
     * @param formId
     * @param completedByAdmin
     * @param partnerOrderId
     * @param geofence
     * @param jobPickupEmail
     * @param jobHash
     * @param customField
     * @param tags
     * @param jobId
     * @param hasPickup
     * @param openTrackingLink
     * @param jobPickupLongitude
     * @param fleetRating
     * @param jobTimeUtc
     * @param pickupDeliveryRelationship
     * @param jobDescription
     * @param dispatcherId
     * @param completedDatetime
     * @param jobStatus
     * @param rideType
     * @param timezone
     * @param hasDelivery
     * @param jobDate
     * @param vertical
     * @param fleetId
     * @param autoAssignment
     * @param customerPhone
     * @param jobPickupLatitude
     * @param jobAdditionalInfo
     * @param customerId
     * @param jobPickupName
     * @param customerUsername
     * @param jobType
     * @param jobTime
     * @param recurringId
     * @param trackingLink
     * @param jobPickupAddress
     * @param userId
     * @param acknowledgedDatetime
     * @param creationDatetime
     * @param jobAddress
     * @param customerRating
     * @param createdBy
     * @param teamId
     * @param startedDatetime
     * @param recurringCount
     * @param remarks
     */
    public TaskDetail(Long jobId, Long createdBy, String orderId, String recurringId, Long recurringCount, Object partnerOrderId, Long teamId, Long vertical, Long merchantId, Long geofence, String tags, Long autoAssignment, Long dispatcherId, String jobHash, Long hasPickup, Long hasDelivery, Long isRouted, String pickupDeliveryRelationship, String jobDescription, String jobPickupDatetime, String jobPickupName, String jobPickupPhone, String jobDeliveryDatetime, String jobPickupLatitude, String jobPickupLongitude, String jobPickupAddress, Object jobPickupEmail, String jobLatitude, String jobLongitude, String customerUsername, String customerEmail, String customerPhone, String jobAddress, String creationDatetime, Long fleetId, Long userId, Long fleetRating, Object customerComment, Long isCustomerRated, Long customerId, String arrivedDatetime, String startedDatetime, String completedDatetime, String acknowledgedDatetime, Long jobStatus, Long isActive, Long jobType, Long completedByAdmin, Long openTrackingLink, String timezone, String jobTime, String jobDate, String jobTimeUtc, String jobDateUtc, Long totalDistanceTravelled, Long formId, Object customerRating, Object driverComment, Object remarks, Object barcode, Long rideType, Object matchedPickupDeliveryRelationship, List<CustomField> customField, String trackingLink, List<TaskHistory> taskHistory, JobAdditionalInfo jobAdditionalInfo) {
        super();
        System.out.println("TaskDetail: full contructor called");
        this.jobId = jobId;
        this.createdBy = createdBy;
        this.orderId = orderId;
        this.recurringId = recurringId;
        this.recurringCount = recurringCount;
        this.partnerOrderId = partnerOrderId;
        this.teamId = teamId;
        this.vertical = vertical;
        this.merchantId = merchantId;
        this.geofence = geofence;
        this.tags = tags;
        this.autoAssignment = autoAssignment;
        this.dispatcherId = dispatcherId;
        this.jobHash = jobHash;
        this.hasPickup = hasPickup;
        this.hasDelivery = hasDelivery;
        this.isRouted = isRouted;
        this.pickupDeliveryRelationship = pickupDeliveryRelationship;
        this.jobDescription = jobDescription;
        this.jobPickupDatetime = jobPickupDatetime;
        this.jobPickupName = jobPickupName;
        this.jobPickupPhone = jobPickupPhone;
        this.jobDeliveryDatetime = jobDeliveryDatetime;
        this.jobPickupLatitude = jobPickupLatitude;
        this.jobPickupLongitude = jobPickupLongitude;
        this.jobPickupAddress = jobPickupAddress;
        this.jobPickupEmail = jobPickupEmail;
        this.jobLatitude = jobLatitude;
        this.jobLongitude = jobLongitude;
        this.customerUsername = customerUsername;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.jobAddress = jobAddress;
        this.creationDatetime = creationDatetime;
        this.fleetId = fleetId;
        this.userId = userId;
        this.fleetRating = fleetRating;
        this.customerComment = customerComment;
        this.isCustomerRated = isCustomerRated;
        this.customerId = customerId;
        this.arrivedDatetime = arrivedDatetime;
        this.startedDatetime = startedDatetime;
        this.completedDatetime = completedDatetime;
        this.acknowledgedDatetime = acknowledgedDatetime;
        this.jobStatus = jobStatus;
        this.isActive = isActive;
        this.jobType = jobType;
        this.completedByAdmin = completedByAdmin;
        this.openTrackingLink = openTrackingLink;
        this.timezone = timezone;
        this.jobTime = jobTime;
        this.jobDate = jobDate;
        this.jobTimeUtc = jobTimeUtc;
        this.jobDateUtc = jobDateUtc;
        this.totalDistanceTravelled = totalDistanceTravelled;
        this.formId = formId;
        this.customerRating = customerRating;
        this.driverComment = driverComment;
        this.remarks = remarks;
        this.barcode = barcode;
        this.rideType = rideType;
        this.matchedPickupDeliveryRelationship = matchedPickupDeliveryRelationship;
        this.customField = customField;
        this.trackingLink = trackingLink;
        this.taskHistory = taskHistory;
        this.jobAdditionalInfo = jobAdditionalInfo;
    }

    //initialize all derived fields
    public void init(){
        if(initComplete.equals(Boolean.FALSE)){
            //determine taskType to be used later
            if(createdBy.equals(0L) || createdBy.equals(1L)) { //Admin or Manager created task
                taskType = TaskType.CUSTOM;
            }else if(createdBy.equals(3L)) { //Form created tasks
                taskType = TaskType.FORM;
            }else if(createdBy.equals(5L)) { //API created tasks
                if(orderId.equals("0")){ //orderId is zero for custom API created tasks
                    taskType = TaskType.CUSTOM;
                }else{
                    taskType = TaskType.FORM;
                }
            }else if(createdBy.equals(43L)) { //Global created tasks
                taskType = TaskType.GLOBAL;
            }else{
                taskType = TaskType.CUSTOM;
            }

            //process common details
            if(taskType.equals(TaskType.CUSTOM)) { //Admin or Manager created task
                restaurantIsGlobal = Boolean.TRUE;
                restaurantIdString = "0";
                //restaurant = restaurantService.getByRestaurantId(0L);
            }else if(taskType.equals(TaskType.FORM)){ //Form created tasks
                restaurantIsGlobal = Boolean.FALSE;
                restaurantIdString = orderId;
                //make sure orderId is a long
                //restaurant = getRestaurant(orderId);
            }
            //process all taskHistory details
            Integer noteCounter = 0;
            for (TaskHistory historyItem: taskHistory) {
                //System.out.println("TaskDetail: init: historyItem:" + historyItem.getType());
                if(historyItem.getType().equals("text_added") || historyItem.getType().equals("text_updated")){
                    noteCounter++;
                    String thisNote = historyItem.getDescription();
                    if(thisNote.matches("^[ A-Za-z]+$")){
                        //skip a note field that is all letters
                        //System.out.println("TaskDetail: init:" + jobId + " tipInNotes count:" + noteCounter + " SKIPPING ALL ALPHA note:" + thisNote + " notes:" + notes);
                        if(noteCounter>1){
                            notes = notes + "\n" + thisNote;
                        }else{
                            notes = thisNote;
                        }
                    }else{
                        //System.out.println("TaskDetail: init: tipInNotes:" + thisNote);
                        if(noteCounter>1){
                            tipInNotesIssue = Boolean.TRUE;
                            notes = notes + "\n" + thisNote;
                            //use the newest (last) note for the tip when there is more than one but still mark it for checking
                            tipInNotes = getDoubleForNotes(thisNote);
                            //tipInNotes = 0.0;
                            if(tipInNotes.equals(-1.0)){
                                System.out.println("TaskDetail: init:" + jobId + " tipInNotes: failed getDouble - setting to 0.0 Counter:" + noteCounter);
                                tipInNotes = 0.0;
                            }
                            //System.out.println("TaskDetail: init:" + jobId + " tipInNotes count:" + noteCounter + " note:" + notes + " value:" + tipInNotes + " Marking as potential issue");
                        }else{
                            tipInNotes = getDoubleForNotes(thisNote);
                            if(tipInNotes.equals(-1.0)){
                                tipInNotesIssue = Boolean.TRUE;
                                System.out.println("TaskDetail: init:" + jobId + " tipInNotes: failed getDouble - setting to 0.0");
                                tipInNotes = 0.0;
                            }
                            notes = thisNote;
                        }
                    }
                }
            }
            //process all custom field details
            for (CustomField customItem: customField) {
                if(templateId==null || templateId.isEmpty()){
                    templateId = customItem.getTemplateId();
                }
                if(taskType.equals(TaskType.FORM) || taskType.equals(TaskType.CUSTOM)){ //Admin or Manager or Form or Api created tasks
                    if(customItem.getLabel().equals("Cost") || customItem.getLabel().equals("Receipt_Total") || customItem.getLabel().equals("Total")){
                        receiptTotal = getDouble(customItem.getData().getDataAsString());
                        //receiptTotal = getDouble(customItem.getFleetData().toString());
                    }
                    else if(customItem.getLabel().equals("Delivery_Fee")){
                        deliveryFee = getDouble(customItem.getData().getDataAsString());
                    }
                    else if(customItem.getLabel().equals("Paid_To_Vendor")){
                        paidToVendor = getDouble(customItem.getData().getDataAsString());
                    }
                    else if(customItem.getLabel().equals("Service_Fee_()")){
                        //older values were decimal percents so need to be converted
                        Double tPercent = getDouble(customItem.getData().getDataAsString());
                        if (tPercent < 1.0){
                            tPercent = tPercent * 100;
                        }
                        serviceFeePercent = tPercent;
                    }
                    else if(customItem.getLabel().startsWith("Service_Fee_($)")){
                        serviceFeeAmount = getDouble(customItem.getData().getDataAsString());
                    }
                    else if(customItem.getLabel().startsWith("Total_with_fees")){
                        totalWithFees = getDouble(customItem.getData().getDataAsString());
                    }
                    else if(customItem.getLabel().equals("Payment_Method")){
                        paymentMethod = customItem.getFleetData().toString().trim();
                    }
                }else if(taskType.equals(TaskType.GLOBAL)){ //GlobalFood create tasks
                    if(customItem.getLabel().equals("Payment")){
                        paymentMethod = customItem.getData().getdataString().trim();
                    }
                    else if(customItem.getLabel().equals("Tip")){
                        tipGlobal = getDouble(customItem.getData().getDataAsString());
                    }
                    else if(customItem.getLabel().equals("Delivery_Fee")){
                        deliveryFee = getDouble(customItem.getData().getDataAsString());
                    }
                    else if(customItem.getLabel().equals("Restaurant_Id")){
                        //make sure restaurantId is a long
                        restaurantIsGlobal = Boolean.TRUE;
                        restaurantIdString = customItem.getData().getDataAsString();

                        //restaurant = getRestaurant(customItem.getData().getDataAsString());
                    }
                    /*
                    else if(customItem.getLabel().equals("Restaurant_Name")){
                        restaurantName = customItem.getData().getdataString();
                    }
                     */
                }
            }
            //Set all TaskEntity fields
            taskEntity.setJobId(jobId);
            taskEntity.setCompletedDate(getCompletedDate());
            taskEntity.setCreationDate(getCreationDate());
            taskEntity.setCustomerUsername(customerUsername);
            taskEntity.setJobStatusName(getJobStatusName());
            taskEntity.setRestaurantName(getRestaurantName());
            taskEntity.setNotes(notes);
            taskEntity.setTip(tipInNotes);
            //for global tasks overwrite with the global tip as it is more accurate
            if(taskType.equals(TaskType.GLOBAL) && tipGlobal!=null) taskEntity.setTip(round(tipGlobal,2));
            taskEntity.setTipInNotesIssue(tipInNotesIssue);
            taskEntity.setTemplateId(templateId);
            taskEntity.setJobStatus(jobStatus);
            taskEntity.setRestaurantId(getRestaurantID());
            taskEntity.setCustomerEmail(customerEmail);
            taskEntity.setCustomerId(customerId);
            taskEntity.setCustomerPhone(customerPhone);
            taskEntity.setJobAddress(jobAddress);
            taskEntity.setJobDescription(jobDescription);
            taskEntity.setPaymentMethod(paymentMethod);
            taskEntity.setReceiptTotal(receiptTotal);
            taskEntity.setPaidToVendor(paidToVendor);
            taskEntity.setDeliveryFee(deliveryFee);
            taskEntity.setServiceFeePercent(serviceFeePercent);
            taskEntity.setServiceFee(serviceFeeAmount);
            taskEntity.setTotalSale(totalWithFees);
            taskEntity.setDispatcherId(dispatcherId);
            taskEntity.setTeamId(teamId);
            taskEntity.setFleetId(fleetId);
            taskEntity.setFormId(formId);
            taskEntity.setJobLatitude(jobLatitude);
            taskEntity.setJobLongitude(jobLongitude);
            taskEntity.setOrderId(orderId);
            taskEntity.setAutoAssignment(autoAssignment);
            taskEntity.setUserId(userId);
            taskEntity.setCreatedBy(createdBy);

            initComplete = Boolean.TRUE;
        }
    }

    private Restaurant getRestaurant(RestaurantRepository restaurantRepository){
        //System.out.println("******getRestaurant: jobId:" + jobId + " looking for:" + restaurantIdString);
        if(restaurantIdString==null){
            System.out.println("******getRestaurant: jobId:" + jobId + " id for restaurant was null");
            return unknownRestaurant;
            //return restaurantService.getUnknownRestaurant();
        }
        try {
            //sometimes the restaurant id is passed with a .0 at the end so is esentially a doube but we need a long
            Long tId = Double.valueOf(restaurantIdString).longValue();
            Restaurant tRestaurant;
            if(restaurantIsGlobal){
                //System.out.println("******getRestaurant: jobId:" + jobId + " returning for Global:" + restaurantRepository.findByRestaurantId(tId));
                tRestaurant = restaurantRepository.findByRestaurantId(tId);
            }else{
                //System.out.println("******getRestaurant: jobId:" + jobId + " returning for Form:" + restaurantRepository.findByFormId(tId));
                tRestaurant = restaurantRepository.findByFormId(tId);
            }
            if(tRestaurant==null) return unknownRestaurant;
            return tRestaurant;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println("******getRestaurant: jobId:" + jobId + " id was not a Long so returning unknown");
            return unknownRestaurant;
        }
    }

    private LocalDateTime convertJsonDate(String input){
        OffsetDateTime odt = OffsetDateTime.parse(input);
        Instant instant = odt.toInstant();
        OffsetDateTime odtMountain = instant.atOffset(ZoneOffset.of("-06:00"));
        return odtMountain.toLocalDateTime();
    }

    private Double getDouble(String input){
        if(input==null){
            return 0.0;
        }
        input = input.replaceAll("[^\\d.]", "");
        if(input.isEmpty()){
            return 0.0;
        }
        Double output = null;
        try {
            output = Double.parseDouble(input);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println("TaskDetail: jobId:" + jobId + " getDouble failed for input: '" + input + "'");
            //return -0.0 so we know there was an issue
            output = -0.0;
        }
        return output;
    }

    private Double getDoubleForNotes(String input){
        if(input==null){
            return 0.0;
        }
        //input = input.replaceAll("[^\\d.]", "");
        input = input.replaceAll("[^.0-9\\s]", "");
        input = input.trim();
        if(input.isEmpty()){
            return 0.0;
        }
        Double output = null;
        try {
            if(input.contains(".")){
                output = Double.parseDouble(input.replaceAll("\\s+",""));
            }else if(input.contains(" ")){
                output = Double.parseDouble(input.replace(" ","."));
                tipInNotesIssue = Boolean.TRUE;
                System.out.println("TaskDetail: jobId:" + jobId + " getDoubleForNotes: Marking potential issue: input:" + input + " output '" + output + "'");
            }else{
                output = Double.parseDouble(input);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println("TaskDetail: jobId:" + jobId + " getDoubleForNotes failed for input: '" + input + "'");
            //return -0.0 so we know there was an issue
            output = -1.0;
        }
        return round(output,2);
    }

    private Double round(Double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    //Derived field getters
    public Double getTipInNotes() {
        return tipInNotes;
    }

    public Double getReceiptTotal() {
        return receiptTotal;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public Double getTipGlobal() {
        return tipGlobal;
    }

    public Long getRestaurantID() {
        return restaurant.getRestaurantId();
    }

    public String getRestaurantName() {
        return restaurant.getName();
    }

    public Double getDeliveryFee() {
        return deliveryFee;
    }

    public Double getPaidToVendor() {
        return paidToVendor;
    }

    public Double getServiceFeePercent() {
        return serviceFeePercent;
    }

    public Double getServiceFeeAmount() {
        return serviceFeeAmount;
    }

    public Double getTotalWithFees() {
        return totalWithFees;
    }

    //for completed tasks use the completed date/time otherwise use the creation date/time
    //TODO: may want to add a different field for the creation and leave null if not ever completed
    public LocalDateTime getCompletedDate() {
        //System.out.println("getCompletedDate: job:" + jobId + " status:" + jobStatus + " completed:" + completedDatetime + " created:" + creationDatetime);
        if(jobStatus.equals(2L)){
            return convertJsonDate(completedDatetime);
        }
        return convertJsonDate(creationDatetime);
    }

    public LocalDateTime getCreationDate() {
        //System.out.println("getCompletedDate: job:" + jobId + " status:" + jobStatus + " completed:" + completedDatetime + " created:" + creationDatetime);
        return convertJsonDate(creationDatetime);
    }

    private void buildTaskEntity(){
        taskEntityLoaded = Boolean.TRUE;
        restaurant = getRestaurant(restaurantRepository);
        taskEntity.setRestaurantId(restaurant.getRestaurantId());
        taskEntity.setRestaurantName(restaurant.getName());
        if(fleetId!=null){
            //System.out.println("getTaskEntity: finding driver by id:" + fleetId);
            driver = driversRepository.findByFleetId(fleetId);
            //System.out.println("getTaskEntity: findByFleetId returned:" + driver);
            taskEntity.setFleetName(driver.getName());
        }

        //add data for GlobalFood tasks
        if(taskType.equals(TaskType.GLOBAL)){
            try {
                Long tOrderId = Long.valueOf(orderId);
                OrderDetail orderDetail = orderDetailRepository.findOrderDetailByOrderId(tOrderId);
                if(orderDetail!=null){
                    if(taskEntity.getPaymentMethod()==null || taskEntity.getPaymentMethod().isEmpty()){
                        taskEntity.setPaymentMethod(orderDetail.getPaymentMethod());
                    }
                    taskEntity.setGlobalSubtotal(orderDetail.getSubtotal());
                    taskEntity.setGlobalTotalTaxes(orderDetail.getTotalTaxes());
                    taskEntity.setServiceFee(orderDetail.getServiceFee());
                    //totalSale for Global is subTotal + taxes + serviceFee + deliveryFee
                    //taskEntity.setTotalSale(orderDetail.getTotal());
                    taskEntity.setTotalSale(round(orderDetail.getSubtotal() + orderDetail.getTotalTaxes() + orderDetail.getDeliveryFee() + orderDetail.getServiceFee(),2));
                }else{
                    System.out.println("getTaskEntity: jobId:" + jobId + " orderDetail was null for orderId:" + tOrderId);
                    skipGlobal = Boolean.TRUE;
                    taskEntity.setTotalSale(0.0);
                    taskEntity.setGlobalSubtotal(0.0);
                    taskEntity.setGlobalTotalTaxes(0.0);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        //Calculate commission
        if(restaurant.getCommissionPerDelivery()>0.0){
            //commission is per delivery
            taskEntity.setCommission(restaurant.getCommissionPerDelivery());
        }else if(taskType.equals(TaskType.GLOBAL)){
            //commission is GlobalSubtotal times commission
            if(taskEntity.getGlobalSubtotal()!=null){
                taskEntity.setCommission(round(restaurant.getCommissionRate() * taskEntity.getGlobalSubtotal(),2));
            }
        }else if(taskType.equals(TaskType.FORM)){
            //commission is receipt total less paid to vendor
            if(taskEntity.getPaidToVendor()!=null && taskEntity.getReceiptTotal()!=null){
                taskEntity.setCommission(round(taskEntity.getReceiptTotal() - taskEntity.getPaidToVendor(),2));
            }
        }

        //Calculate delivery fee payback from vendors
        if(taskType.equals(TaskType.GLOBAL)){
            if(deliveryFee.equals(0.0)){
                if(restaurant.getDeliveryFeeFromVendor() > 0.0){
                    taskEntity.setDeliveryFeeFromVendor(restaurant.getDeliveryFeeFromVendor());
                }
            }
        }

        //provide a calculated field for totalFees
        taskEntity.setTotalFees(round(taskEntity.getServiceFee() + taskEntity.getDeliveryFee() + taskEntity.getDeliveryFeeFromVendor(),2));

        //TODO: need to get the driverPay from a managed database config table
        taskEntity.setDriverPay(config.getDriverPayForDelivery());

        //provide a calculated field for the feeBalance
        taskEntity.setFeeBalance(round(taskEntity.getTotalFees() - taskEntity.getDriverPay(),2));

        //provide a calculated field for totalIncome
        taskEntity.setTotalIncome(round(taskEntity.getFeeBalance() + taskEntity.getCommission(),2));

        //provide calculated fields for driver payout
        //System.out.println("setDriverIncome for task:" + jobId + " getDriverPay:" + taskEntity.getDriverPay() + " getTip" + taskEntity.getTip());
        taskEntity.setDriverIncome(round(taskEntity.getDriverPay() + taskEntity.getTip(),2));
        if(taskEntity.getPaymentMethod()!=null && taskEntity.getPaymentMethod().equalsIgnoreCase("CASH")){
            //System.out.println("setDriverCash for task:" + jobId + " getTotalSale:" + taskEntity.getTotalSale() + " getTip" + taskEntity.getTip());
            taskEntity.setDriverCash(round(taskEntity.getTip() + taskEntity.getTotalSale(),2));
        }
        taskEntity.setDriverPayout(round(taskEntity.getDriverIncome() - taskEntity.getDriverCash(),2));

        //build the driverPayout Entity
        if(!skipGlobal){
            driverPayoutEntity.setDriverPayout(taskEntity.getDriverPayout());
            driverPayoutEntity.setDriverPay(taskEntity.getDriverPay());
            driverPayoutEntity.setDriverCash(taskEntity.getDriverCash());
            driverPayoutEntity.setDriverIncome(taskEntity.getDriverIncome());
            driverPayoutEntity.setCreationDateTime(taskEntity.getCreationDate());
            driverPayoutEntity.setCustomerUsername(taskEntity.getCustomerUsername());
            driverPayoutEntity.setFleetId(taskEntity.getFleetId());
            driverPayoutEntity.setFleetName(taskEntity.getFleetName());
            driverPayoutEntity.setJobId(taskEntity.getJobId());
            driverPayoutEntity.setNotes(taskEntity.getNotes());
            driverPayoutEntity.setPaymentMethod(taskEntity.getPaymentMethod());
            driverPayoutEntity.setRestaurantName(taskEntity.getRestaurantName());
            driverPayoutEntity.setTip(taskEntity.getTip());
            driverPayoutEntity.setTipInNotesIssue(taskEntity.getTipInNotesIssue());
            driverPayoutEntity.setTotalSale(taskEntity.getTotalSale());
        }

    }

    public TaskEntity getTaskEntity(RestaurantRepository restaurantRepository, OrderDetailRepository orderDetailRepository, DriversRepository driversRepository) {
        if(taskEntityLoaded){
            return taskEntity;
        }else{
            this.restaurantRepository = restaurantRepository;
            this.orderDetailRepository = orderDetailRepository;
            this.driversRepository = driversRepository;
            buildTaskEntity();
            return taskEntity;
        }
    }

    public DriverPayoutEntity getDriverPayoutEntity(RestaurantRepository restaurantRepository, OrderDetailRepository orderDetailRepository, DriversRepository driversRepository){
        if(skipGlobal){
            return null;
        }
        if(taskEntityLoaded){
            return driverPayoutEntity;
        }else{
            this.restaurantRepository = restaurantRepository;
            this.orderDetailRepository = orderDetailRepository;
            this.driversRepository = driversRepository;
            buildTaskEntity();
            return driverPayoutEntity;
        }
    }

    public String getNotes() {
        return notes;
    }

    public Boolean getTipInNotesIssue() {
        return tipInNotesIssue;
    }

    public String getTemplateId() {
        return templateId;
    }

    public String getJobStatusName() {
        if(jobStatus.equals(0L)) return "Assigned";
        if(jobStatus.equals(1L)) return "Started";
        if(jobStatus.equals(2L)) return "Successful";
        if(jobStatus.equals(3L)) return "Failed";
        if(jobStatus.equals(4L)) return "InProgress";
        //if(jobStatus.equals(5L)) return "";
        if(jobStatus.equals(6L)) return "Unassigned";
        if(jobStatus.equals(7L)) return "Accepted";
        if(jobStatus.equals(8L)) return "Decline";
        if(jobStatus.equals(9L)) return "Cancel";
        if(jobStatus.equals(10L)) return "Deleted";
        return "INVALID";
    }
/*
    public String getCreator(){
        if(createdBy.equals(0L)) return "ADMIN";
        if(createdBy.equals(1L)) return "MANAGER";
        if(createdBy.equals(3L)) return "FORM";
        if(createdBy.equals(5L)) return "API";
        if(createdBy.equals(43L)) return "GLOBAL";
        return "OTHER";
    }
     */

    @JsonProperty("job_id")
    public Long getJobId() {
        return jobId;
    }

    @JsonProperty("job_id")
    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public TaskDetail withJobId(Long jobId) {
        this.jobId = jobId;
        return this;
    }

    @JsonProperty("created_by")
    public Long getCreatedBy() {
        return createdBy;
    }

    @JsonProperty("created_by")
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public TaskDetail withCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
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

    public TaskDetail withOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    @JsonProperty("recurring_id")
    public String getRecurringId() {
        return recurringId;
    }

    @JsonProperty("recurring_id")
    public void setRecurringId(String recurringId) {
        this.recurringId = recurringId;
    }

    public TaskDetail withRecurringId(String recurringId) {
        this.recurringId = recurringId;
        return this;
    }

    @JsonProperty("recurring_count")
    public Long getRecurringCount() {
        return recurringCount;
    }

    @JsonProperty("recurring_count")
    public void setRecurringCount(Long recurringCount) {
        this.recurringCount = recurringCount;
    }

    public TaskDetail withRecurringCount(Long recurringCount) {
        this.recurringCount = recurringCount;
        return this;
    }

    @JsonProperty("partner_order_id")
    public Object getPartnerOrderId() {
        return partnerOrderId;
    }

    @JsonProperty("partner_order_id")
    public void setPartnerOrderId(Object partnerOrderId) {
        this.partnerOrderId = partnerOrderId;
    }

    public TaskDetail withPartnerOrderId(Object partnerOrderId) {
        this.partnerOrderId = partnerOrderId;
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

    public TaskDetail withTeamId(Long teamId) {
        this.teamId = teamId;
        return this;
    }

    @JsonProperty("vertical")
    public Long getVertical() {
        return vertical;
    }

    @JsonProperty("vertical")
    public void setVertical(Long vertical) {
        this.vertical = vertical;
    }

    public TaskDetail withVertical(Long vertical) {
        this.vertical = vertical;
        return this;
    }

    @JsonProperty("merchant_id")
    public Long getMerchantId() {
        return merchantId;
    }

    @JsonProperty("merchant_id")
    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public TaskDetail withMerchantId(Long merchantId) {
        this.merchantId = merchantId;
        return this;
    }

    @JsonProperty("geofence")
    public Long getGeofence() {
        return geofence;
    }

    @JsonProperty("geofence")
    public void setGeofence(Long geofence) {
        this.geofence = geofence;
    }

    public TaskDetail withGeofence(Long geofence) {
        this.geofence = geofence;
        return this;
    }

    @JsonProperty("tags")
    public String getTags() {
        return tags;
    }

    @JsonProperty("tags")
    public void setTags(String tags) {
        this.tags = tags;
    }

    public TaskDetail withTags(String tags) {
        this.tags = tags;
        return this;
    }

    @JsonProperty("auto_assignment")
    public Long getAutoAssignment() {
        return autoAssignment;
    }

    @JsonProperty("auto_assignment")
    public void setAutoAssignment(Long autoAssignment) {
        this.autoAssignment = autoAssignment;
    }

    public TaskDetail withAutoAssignment(Long autoAssignment) {
        this.autoAssignment = autoAssignment;
        return this;
    }

    @JsonProperty("dispatcher_id")
    public Long getDispatcherId() {
        return dispatcherId;
    }

    @JsonProperty("dispatcher_id")
    public void setDispatcherId(Long dispatcherId) {
        this.dispatcherId = dispatcherId;
    }

    public TaskDetail withDispatcherId(Long dispatcherId) {
        this.dispatcherId = dispatcherId;
        return this;
    }

    @JsonProperty("job_hash")
    public String getJobHash() {
        return jobHash;
    }

    @JsonProperty("job_hash")
    public void setJobHash(String jobHash) {
        this.jobHash = jobHash;
    }

    public TaskDetail withJobHash(String jobHash) {
        this.jobHash = jobHash;
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

    public TaskDetail withHasPickup(Long hasPickup) {
        this.hasPickup = hasPickup;
        return this;
    }

    @JsonProperty("has_delivery")
    public Long getHasDelivery() {
        return hasDelivery;
    }

    @JsonProperty("has_delivery")
    public void setHasDelivery(Long hasDelivery) {
        this.hasDelivery = hasDelivery;
    }

    public TaskDetail withHasDelivery(Long hasDelivery) {
        this.hasDelivery = hasDelivery;
        return this;
    }

    @JsonProperty("is_routed")
    public Long getIsRouted() {
        return isRouted;
    }

    @JsonProperty("is_routed")
    public void setIsRouted(Long isRouted) {
        this.isRouted = isRouted;
    }

    public TaskDetail withIsRouted(Long isRouted) {
        this.isRouted = isRouted;
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

    public TaskDetail withPickupDeliveryRelationship(String pickupDeliveryRelationship) {
        this.pickupDeliveryRelationship = pickupDeliveryRelationship;
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

    public TaskDetail withJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
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

    public TaskDetail withJobPickupDatetime(String jobPickupDatetime) {
        this.jobPickupDatetime = jobPickupDatetime;
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

    public TaskDetail withJobPickupName(String jobPickupName) {
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

    public TaskDetail withJobPickupPhone(String jobPickupPhone) {
        this.jobPickupPhone = jobPickupPhone;
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

    public TaskDetail withJobDeliveryDatetime(String jobDeliveryDatetime) {
        this.jobDeliveryDatetime = jobDeliveryDatetime;
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

    public TaskDetail withJobPickupLatitude(String jobPickupLatitude) {
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

    public TaskDetail withJobPickupLongitude(String jobPickupLongitude) {
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

    public TaskDetail withJobPickupAddress(String jobPickupAddress) {
        this.jobPickupAddress = jobPickupAddress;
        return this;
    }

    @JsonProperty("job_pickup_email")
    public Object getJobPickupEmail() {
        return jobPickupEmail;
    }

    @JsonProperty("job_pickup_email")
    public void setJobPickupEmail(Object jobPickupEmail) {
        this.jobPickupEmail = jobPickupEmail;
    }

    public TaskDetail withJobPickupEmail(Object jobPickupEmail) {
        this.jobPickupEmail = jobPickupEmail;
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

    public TaskDetail withJobLatitude(String jobLatitude) {
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

    public TaskDetail withJobLongitude(String jobLongitude) {
        this.jobLongitude = jobLongitude;
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

    public TaskDetail withCustomerUsername(String customerUsername) {
        this.customerUsername = customerUsername;
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

    public TaskDetail withCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
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

    public TaskDetail withCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
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

    public TaskDetail withJobAddress(String jobAddress) {
        this.jobAddress = jobAddress;
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

    public TaskDetail withCreationDatetime(String creationDatetime) {
        this.creationDatetime = creationDatetime;
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

    public TaskDetail withFleetId(Long fleetId) {
        this.fleetId = fleetId;
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

    public TaskDetail withUserId(Long userId) {
        this.userId = userId;
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

    public TaskDetail withFleetRating(Long fleetRating) {
        this.fleetRating = fleetRating;
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

    public TaskDetail withCustomerComment(Object customerComment) {
        this.customerComment = customerComment;
        return this;
    }

    @JsonProperty("is_customer_rated")
    public Long getIsCustomerRated() {
        return isCustomerRated;
    }

    @JsonProperty("is_customer_rated")
    public void setIsCustomerRated(Long isCustomerRated) {
        this.isCustomerRated = isCustomerRated;
    }

    public TaskDetail withIsCustomerRated(Long isCustomerRated) {
        this.isCustomerRated = isCustomerRated;
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

    public TaskDetail withCustomerId(Long customerId) {
        this.customerId = customerId;
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

    public TaskDetail withArrivedDatetime(String arrivedDatetime) {
        this.arrivedDatetime = arrivedDatetime;
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

    public TaskDetail withStartedDatetime(String startedDatetime) {
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

    public TaskDetail withCompletedDatetime(String completedDatetime) {
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

    public TaskDetail withAcknowledgedDatetime(String acknowledgedDatetime) {
        this.acknowledgedDatetime = acknowledgedDatetime;
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

    public TaskDetail withJobStatus(Long jobStatus) {
        this.jobStatus = jobStatus;
        return this;
    }

    @JsonProperty("is_active")
    public Long getIsActive() {
        return isActive;
    }

    @JsonProperty("is_active")
    public void setIsActive(Long isActive) {
        this.isActive = isActive;
    }

    public TaskDetail withIsActive(Long isActive) {
        this.isActive = isActive;
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

    public TaskDetail withJobType(Long jobType) {
        this.jobType = jobType;
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

    public TaskDetail withCompletedByAdmin(Long completedByAdmin) {
        this.completedByAdmin = completedByAdmin;
        return this;
    }

    @JsonProperty("open_tracking_link")
    public Long getOpenTrackingLink() {
        return openTrackingLink;
    }

    @JsonProperty("open_tracking_link")
    public void setOpenTrackingLink(Long openTrackingLink) {
        this.openTrackingLink = openTrackingLink;
    }

    public TaskDetail withOpenTrackingLink(Long openTrackingLink) {
        this.openTrackingLink = openTrackingLink;
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

    public TaskDetail withTimezone(String timezone) {
        this.timezone = timezone;
        return this;
    }

    @JsonProperty("job_time")
    public String getJobTime() {
        return jobTime;
    }

    @JsonProperty("job_time")
    public void setJobTime(String jobTime) {
        this.jobTime = jobTime;
    }

    public TaskDetail withJobTime(String jobTime) {
        this.jobTime = jobTime;
        return this;
    }

    @JsonProperty("job_date")
    public String getJobDate() {
        return jobDate;
    }

    @JsonProperty("job_date")
    public void setJobDate(String jobDate) {
        this.jobDate = jobDate;
    }

    public TaskDetail withJobDate(String jobDate) {
        this.jobDate = jobDate;
        return this;
    }

    @JsonProperty("job_time_utc")
    public String getJobTimeUtc() {
        return jobTimeUtc;
    }

    @JsonProperty("job_time_utc")
    public void setJobTimeUtc(String jobTimeUtc) {
        this.jobTimeUtc = jobTimeUtc;
    }

    public TaskDetail withJobTimeUtc(String jobTimeUtc) {
        this.jobTimeUtc = jobTimeUtc;
        return this;
    }

    @JsonProperty("job_date_utc")
    public String getJobDateUtc() {
        return jobDateUtc;
    }

    @JsonProperty("job_date_utc")
    public void setJobDateUtc(String jobDateUtc) {
        this.jobDateUtc = jobDateUtc;
    }

    public TaskDetail withJobDateUtc(String jobDateUtc) {
        this.jobDateUtc = jobDateUtc;
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

    public TaskDetail withTotalDistanceTravelled(Long totalDistanceTravelled) {
        this.totalDistanceTravelled = totalDistanceTravelled;
        return this;
    }

    @JsonProperty("form_id")
    public Long getFormId() {
        return formId;
    }

    @JsonProperty("form_id")
    public void setFormId(Long formId) {
        this.formId = formId;
    }

    public TaskDetail withFormId(Long formId) {
        this.formId = formId;
        return this;
    }

    @JsonProperty("customer_rating")
    public Object getCustomerRating() {
        return customerRating;
    }

    @JsonProperty("customer_rating")
    public void setCustomerRating(Object customerRating) {
        this.customerRating = customerRating;
    }

    public TaskDetail withCustomerRating(Object customerRating) {
        this.customerRating = customerRating;
        return this;
    }

    @JsonProperty("driver_comment")
    public Object getDriverComment() {
        return driverComment;
    }

    @JsonProperty("driver_comment")
    public void setDriverComment(Object driverComment) {
        this.driverComment = driverComment;
    }

    public TaskDetail withDriverComment(Object driverComment) {
        this.driverComment = driverComment;
        return this;
    }

    @JsonProperty("remarks")
    public Object getRemarks() {
        return remarks;
    }

    @JsonProperty("remarks")
    public void setRemarks(Object remarks) {
        this.remarks = remarks;
    }

    public TaskDetail withRemarks(Object remarks) {
        this.remarks = remarks;
        return this;
    }

    @JsonProperty("barcode")
    public Object getBarcode() {
        return barcode;
    }

    @JsonProperty("barcode")
    public void setBarcode(Object barcode) {
        this.barcode = barcode;
    }

    public TaskDetail withBarcode(Object barcode) {
        this.barcode = barcode;
        return this;
    }

    @JsonProperty("ride_type")
    public Long getRideType() {
        return rideType;
    }

    @JsonProperty("ride_type")
    public void setRideType(Long rideType) {
        this.rideType = rideType;
    }

    public TaskDetail withRideType(Long rideType) {
        this.rideType = rideType;
        return this;
    }

    @JsonProperty("matched_pickup_delivery_relationship")
    public Object getMatchedPickupDeliveryRelationship() {
        return matchedPickupDeliveryRelationship;
    }

    @JsonProperty("matched_pickup_delivery_relationship")
    public void setMatchedPickupDeliveryRelationship(Object matchedPickupDeliveryRelationship) {
        this.matchedPickupDeliveryRelationship = matchedPickupDeliveryRelationship;
    }

    public TaskDetail withMatchedPickupDeliveryRelationship(Object matchedPickupDeliveryRelationship) {
        this.matchedPickupDeliveryRelationship = matchedPickupDeliveryRelationship;
        return this;
    }

    @JsonProperty("custom_field")
    public List<CustomField> getCustomField() {
        return customField;
    }

    @JsonProperty("custom_field")
    public void setCustomField(List<CustomField> customField) {
        this.customField = customField;
    }

    public TaskDetail withCustomField(List<CustomField> customField) {
        this.customField = customField;
        return this;
    }

    @JsonProperty("tracking_link")
    public String getTrackingLink() {
        return trackingLink;
    }

    @JsonProperty("tracking_link")
    public void setTrackingLink(String trackingLink) {
        this.trackingLink = trackingLink;
    }

    public TaskDetail withTrackingLink(String trackingLink) {
        this.trackingLink = trackingLink;
        return this;
    }

    @JsonProperty("task_history")
    public List<TaskHistory> getTaskHistory() {
        return taskHistory;
    }

    @JsonProperty("task_history")
    public void setTaskHistory(List<TaskHistory> taskHistory) {
        this.taskHistory = taskHistory;
    }

    public TaskDetail withTaskHistory(List<TaskHistory> taskHistory) {
        this.taskHistory = taskHistory;
        return this;
    }

    @JsonProperty("job_additional_info")
    public JobAdditionalInfo getJobAdditionalInfo() {
        return jobAdditionalInfo;
    }

    @JsonProperty("job_additional_info")
    public void setJobAdditionalInfo(JobAdditionalInfo jobAdditionalInfo) {
        this.jobAdditionalInfo = jobAdditionalInfo;
    }

    public TaskDetail withJobAdditionalInfo(JobAdditionalInfo jobAdditionalInfo) {
        this.jobAdditionalInfo = jobAdditionalInfo;
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

    public TaskDetail withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(TaskDetail.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("jobId");
        sb.append('=');
        sb.append(((this.jobId == null)?"<null>":this.jobId));
        sb.append(',');
        sb.append("createdBy");
        sb.append('=');
        sb.append(((this.createdBy == null)?"<null>":this.createdBy));
        sb.append(',');
        sb.append("orderId");
        sb.append('=');
        sb.append(((this.orderId == null)?"<null>":this.orderId));
        sb.append(',');
        sb.append("recurringId");
        sb.append('=');
        sb.append(((this.recurringId == null)?"<null>":this.recurringId));
        sb.append(',');
        sb.append("recurringCount");
        sb.append('=');
        sb.append(((this.recurringCount == null)?"<null>":this.recurringCount));
        sb.append(',');
        sb.append("partnerOrderId");
        sb.append('=');
        sb.append(((this.partnerOrderId == null)?"<null>":this.partnerOrderId));
        sb.append(',');
        sb.append("teamId");
        sb.append('=');
        sb.append(((this.teamId == null)?"<null>":this.teamId));
        sb.append(',');
        sb.append("vertical");
        sb.append('=');
        sb.append(((this.vertical == null)?"<null>":this.vertical));
        sb.append(',');
        sb.append("merchantId");
        sb.append('=');
        sb.append(((this.merchantId == null)?"<null>":this.merchantId));
        sb.append(',');
        sb.append("geofence");
        sb.append('=');
        sb.append(((this.geofence == null)?"<null>":this.geofence));
        sb.append(',');
        sb.append("tags");
        sb.append('=');
        sb.append(((this.tags == null)?"<null>":this.tags));
        sb.append(',');
        sb.append("autoAssignment");
        sb.append('=');
        sb.append(((this.autoAssignment == null)?"<null>":this.autoAssignment));
        sb.append(',');
        sb.append("dispatcherId");
        sb.append('=');
        sb.append(((this.dispatcherId == null)?"<null>":this.dispatcherId));
        sb.append(',');
        sb.append("jobHash");
        sb.append('=');
        sb.append(((this.jobHash == null)?"<null>":this.jobHash));
        sb.append(',');
        sb.append("hasPickup");
        sb.append('=');
        sb.append(((this.hasPickup == null)?"<null>":this.hasPickup));
        sb.append(',');
        sb.append("hasDelivery");
        sb.append('=');
        sb.append(((this.hasDelivery == null)?"<null>":this.hasDelivery));
        sb.append(',');
        sb.append("isRouted");
        sb.append('=');
        sb.append(((this.isRouted == null)?"<null>":this.isRouted));
        sb.append(',');
        sb.append("pickupDeliveryRelationship");
        sb.append('=');
        sb.append(((this.pickupDeliveryRelationship == null)?"<null>":this.pickupDeliveryRelationship));
        sb.append(',');
        sb.append("jobDescription");
        sb.append('=');
        sb.append(((this.jobDescription == null)?"<null>":this.jobDescription));
        sb.append(',');
        sb.append("jobPickupDatetime");
        sb.append('=');
        sb.append(((this.jobPickupDatetime == null)?"<null>":this.jobPickupDatetime));
        sb.append(',');
        sb.append("jobPickupName");
        sb.append('=');
        sb.append(((this.jobPickupName == null)?"<null>":this.jobPickupName));
        sb.append(',');
        sb.append("jobPickupPhone");
        sb.append('=');
        sb.append(((this.jobPickupPhone == null)?"<null>":this.jobPickupPhone));
        sb.append(',');
        sb.append("jobDeliveryDatetime");
        sb.append('=');
        sb.append(((this.jobDeliveryDatetime == null)?"<null>":this.jobDeliveryDatetime));
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
        sb.append("jobPickupEmail");
        sb.append('=');
        sb.append(((this.jobPickupEmail == null)?"<null>":this.jobPickupEmail));
        sb.append(',');
        sb.append("jobLatitude");
        sb.append('=');
        sb.append(((this.jobLatitude == null)?"<null>":this.jobLatitude));
        sb.append(',');
        sb.append("jobLongitude");
        sb.append('=');
        sb.append(((this.jobLongitude == null)?"<null>":this.jobLongitude));
        sb.append(',');
        sb.append("customerUsername");
        sb.append('=');
        sb.append(((this.customerUsername == null)?"<null>":this.customerUsername));
        sb.append(',');
        sb.append("customerEmail");
        sb.append('=');
        sb.append(((this.customerEmail == null)?"<null>":this.customerEmail));
        sb.append(',');
        sb.append("customerPhone");
        sb.append('=');
        sb.append(((this.customerPhone == null)?"<null>":this.customerPhone));
        sb.append(',');
        sb.append("jobAddress");
        sb.append('=');
        sb.append(((this.jobAddress == null)?"<null>":this.jobAddress));
        sb.append(',');
        sb.append("creationDatetime");
        sb.append('=');
        sb.append(((this.creationDatetime == null)?"<null>":this.creationDatetime));
        sb.append(',');
        sb.append("fleetId");
        sb.append('=');
        sb.append(((this.fleetId == null)?"<null>":this.fleetId));
        sb.append(',');
        sb.append("userId");
        sb.append('=');
        sb.append(((this.userId == null)?"<null>":this.userId));
        sb.append(',');
        sb.append("fleetRating");
        sb.append('=');
        sb.append(((this.fleetRating == null)?"<null>":this.fleetRating));
        sb.append(',');
        sb.append("customerComment");
        sb.append('=');
        sb.append(((this.customerComment == null)?"<null>":this.customerComment));
        sb.append(',');
        sb.append("isCustomerRated");
        sb.append('=');
        sb.append(((this.isCustomerRated == null)?"<null>":this.isCustomerRated));
        sb.append(',');
        sb.append("customerId");
        sb.append('=');
        sb.append(((this.customerId == null)?"<null>":this.customerId));
        sb.append(',');
        sb.append("arrivedDatetime");
        sb.append('=');
        sb.append(((this.arrivedDatetime == null)?"<null>":this.arrivedDatetime));
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
        sb.append("jobStatus");
        sb.append('=');
        sb.append(((this.jobStatus == null)?"<null>":this.jobStatus));
        sb.append(',');
        sb.append("isActive");
        sb.append('=');
        sb.append(((this.isActive == null)?"<null>":this.isActive));
        sb.append(',');
        sb.append("jobType");
        sb.append('=');
        sb.append(((this.jobType == null)?"<null>":this.jobType));
        sb.append(',');
        sb.append("completedByAdmin");
        sb.append('=');
        sb.append(((this.completedByAdmin == null)?"<null>":this.completedByAdmin));
        sb.append(',');
        sb.append("openTrackingLink");
        sb.append('=');
        sb.append(((this.openTrackingLink == null)?"<null>":this.openTrackingLink));
        sb.append(',');
        sb.append("timezone");
        sb.append('=');
        sb.append(((this.timezone == null)?"<null>":this.timezone));
        sb.append(',');
        sb.append("jobTime");
        sb.append('=');
        sb.append(((this.jobTime == null)?"<null>":this.jobTime));
        sb.append(',');
        sb.append("jobDate");
        sb.append('=');
        sb.append(((this.jobDate == null)?"<null>":this.jobDate));
        sb.append(',');
        sb.append("jobTimeUtc");
        sb.append('=');
        sb.append(((this.jobTimeUtc == null)?"<null>":this.jobTimeUtc));
        sb.append(',');
        sb.append("jobDateUtc");
        sb.append('=');
        sb.append(((this.jobDateUtc == null)?"<null>":this.jobDateUtc));
        sb.append(',');
        sb.append("totalDistanceTravelled");
        sb.append('=');
        sb.append(((this.totalDistanceTravelled == null)?"<null>":this.totalDistanceTravelled));
        sb.append(',');
        sb.append("formId");
        sb.append('=');
        sb.append(((this.formId == null)?"<null>":this.formId));
        sb.append(',');
        sb.append("customerRating");
        sb.append('=');
        sb.append(((this.customerRating == null)?"<null>":this.customerRating));
        sb.append(',');
        sb.append("driverComment");
        sb.append('=');
        sb.append(((this.driverComment == null)?"<null>":this.driverComment));
        sb.append(',');
        sb.append("remarks");
        sb.append('=');
        sb.append(((this.remarks == null)?"<null>":this.remarks));
        sb.append(',');
        sb.append("barcode");
        sb.append('=');
        sb.append(((this.barcode == null)?"<null>":this.barcode));
        sb.append(',');
        sb.append("rideType");
        sb.append('=');
        sb.append(((this.rideType == null)?"<null>":this.rideType));
        sb.append(',');
        sb.append("matchedPickupDeliveryRelationship");
        sb.append('=');
        sb.append(((this.matchedPickupDeliveryRelationship == null)?"<null>":this.matchedPickupDeliveryRelationship));
        sb.append(',');
        sb.append("customField");
        sb.append('=');
        sb.append(((this.customField == null)?"<null>":this.customField));
        sb.append(',');
        sb.append("trackingLink");
        sb.append('=');
        sb.append(((this.trackingLink == null)?"<null>":this.trackingLink));
        sb.append(',');
        sb.append("taskHistory");
        sb.append('=');
        sb.append(((this.taskHistory == null)?"<null>":this.taskHistory));
        sb.append(',');
        sb.append("jobAdditionalInfo");
        sb.append('=');
        sb.append(((this.jobAdditionalInfo == null)?"<null>":this.jobAdditionalInfo));
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
