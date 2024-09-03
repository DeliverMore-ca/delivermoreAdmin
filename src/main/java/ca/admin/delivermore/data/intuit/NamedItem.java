
package ca.admin.delivermore.data.intuit;

import jakarta.annotation.Generated;

import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "sparse",
    "Id",
    "DisplayName"
})
@Generated("jsonschema2pojo")
public class NamedItem {

    @JsonProperty("sparse")
    private Boolean sparse;
    @JsonProperty("Id")
    private String id;
    @JsonProperty("DisplayName")
    @JsonAlias({"FullyQualifiedName", "Name"})
    private String displayName;

    @JsonProperty("sparse")
    public Boolean getSparse() {
        return sparse;
    }

    @JsonProperty("sparse")
    public void setSparse(Boolean sparse) {
        this.sparse = sparse;
    }

    @JsonProperty("Id")
    public String getId() {
        return id;
    }

    @JsonProperty("Id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("DisplayName")
    @JsonAlias({"FullyQualifiedName", "Name"})
    public String getDisplayName() {
        return displayName;
    }

    @JsonProperty("DisplayName")
    @JsonAlias({"FullyQualifiedName", "Name"})
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return "NamedItem{" +
                ", displayName='" + displayName + '\'' +
                "id='" + id + '\'' +
                '}';
    }
}
