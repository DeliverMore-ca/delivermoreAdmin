
package ca.admin.delivermore.data.intuit;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "JournalEntry",
    "time"
})
@Generated("jsonschema2pojo")
public class JournalEntryResult {

    @JsonProperty("JournalEntry")
    private JournalEntry journalEntry;
    @JsonProperty("time")
    private String time;

    @JsonProperty("JournalEntry")
    public JournalEntry getJournalEntry() {
        return journalEntry;
    }

    @JsonProperty("JournalEntry")
    public void setJournalEntry(JournalEntry journalEntry) {
        this.journalEntry = journalEntry;
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
        return "JournalEntryResult{" +
                "journalEntry=" + journalEntry +
                ", time='" + time + '\'' +
                '}';
    }
}
