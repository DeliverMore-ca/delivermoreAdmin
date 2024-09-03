package ca.admin.delivermore.data.scheduler;

import jakarta.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.stefan.fullcalendar.Resource;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "group",
        "title"
})
@Generated("jsonschema2pojo")
public class SchedulerResource {

    private Logger log = LoggerFactory.getLogger(SchedulerResource.class);

    @JsonProperty("id")
    private String id;
    @JsonProperty("group")
    private String group;
    @JsonProperty("title")
    private String title;

    @JsonProperty("display")
    private Boolean display = Boolean.TRUE;

    @JsonIgnore
    private Resource resource;

    public SchedulerResource() {
    }

    public SchedulerResource(String id, String group, String title) {
        this(id,group,title,Boolean.TRUE);
    }

    public SchedulerResource(String id, String group, String title, Boolean display) {
        this.id = id;
        this.group = group;
        this.title = title;
        this.display = display;
        this.resource = new Resource(id,title,Scheduler.defaultEventColor);
        this.resource.addExtendedProps("group", group);
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("group")
    public String getGroup() {
        return group;
    }

    @JsonProperty("group")
    public void setGroup(String group) {
        this.group = group;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    public Resource getResource() {
        return resource;
    }

    public Boolean getDisplay() {
        return display;
    }

    public void setDisplay(Boolean display) {
        this.display = display;
    }
}