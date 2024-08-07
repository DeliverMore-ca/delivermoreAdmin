
package ca.admin.delivermore.data.intuit;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "PostingType",
    "AccountRef",
    "Entity"
})
@Generated("jsonschema2pojo")
public class JournalEntryLineDetail {

    @JsonProperty("PostingType")
    private String postingType;
    @JsonProperty("AccountRef")
    private AccountRef accountRef;
    @JsonProperty("Entity")
    private Entity entity;

    @JsonProperty("PostingType")
    public String getPostingType() {
        return postingType;
    }

    @JsonProperty("PostingType")
    public void setPostingType(String postingType) {
        this.postingType = postingType;
    }

    @JsonProperty("AccountRef")
    public AccountRef getAccountRef() {
        return accountRef;
    }

    @JsonProperty("AccountRef")
    public void setAccountRef(AccountRef accountRef) {
        this.accountRef = accountRef;
    }

    @JsonProperty("Entity")
    public Entity getEntity() {
        return entity;
    }

    @JsonProperty("Entity")
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    @Override
    public String toString() {
        return "JournalEntryLineDetail{" +
                "postingType='" + postingType + '\'' +
                ", accountRef=" + accountRef +
                ", entity=" + entity +
                '}';
    }
}
