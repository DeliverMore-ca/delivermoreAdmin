
package ca.admin.delivermore.data.intuit;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "Id",
    "LineNum",
    "Description",
    "Amount",
    "DetailType",
    "JournalEntryLineDetail",
    "SalesItemLineDetail",
    "SubTotalLineDetail"
})
@Generated("jsonschema2pojo")
public class Line {

    @JsonProperty("Id")
    private String id;
    @JsonProperty("LineNum")
    private Integer lineNum;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("Amount")
    private Double amount;
    @JsonProperty("DetailType")
    private String detailType;
    @JsonProperty("JournalEntryLineDetail")
    private JournalEntryLineDetail journalEntryLineDetail;
    @JsonProperty("SalesItemLineDetail")
    private SalesItemLineDetail salesItemLineDetail;
    @JsonProperty("SubTotalLineDetail")
    private SubTotalLineDetail subTotalLineDetail;
    @JsonProperty("Id")
    public String getId() {
        return id;
    }

    @JsonProperty("Id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("LineNum")
    public Integer getLineNum() {
        return lineNum;
    }

    @JsonProperty("LineNum")
    public void setLineNum(Integer lineNum) {
        this.lineNum = lineNum;
    }

    @JsonProperty("Description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("Description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("Amount")
    public Double getAmount() {
        return amount;
    }

    @JsonProperty("Amount")
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @JsonProperty("DetailType")
    public String getDetailType() {
        return detailType;
    }

    @JsonProperty("DetailType")
    public void setDetailType(String detailType) {
        this.detailType = detailType;
    }

    @JsonProperty("JournalEntryLineDetail")
    public JournalEntryLineDetail getJournalEntryLineDetail() {
        return journalEntryLineDetail;
    }

    @JsonProperty("JournalEntryLineDetail")
    public void setJournalEntryLineDetail(JournalEntryLineDetail journalEntryLineDetail) {
        this.journalEntryLineDetail = journalEntryLineDetail;
    }

    @JsonProperty("SalesItemLineDetail")
    public SalesItemLineDetail getSalesItemLineDetail() {
        return salesItemLineDetail;
    }

    @JsonProperty("SalesItemLineDetail")
    public void setSalesItemLineDetail(SalesItemLineDetail salesItemLineDetail) {
        this.salesItemLineDetail = salesItemLineDetail;
    }

    @JsonProperty("SubTotalLineDetail")
    public SubTotalLineDetail getSubTotalLineDetail() {
        return subTotalLineDetail;
    }

    @Override
    public String toString() {
        return "Line{" +
                "id='" + id + '\'' +
                ", lineNum=" + lineNum +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", detailType='" + detailType + '\'' +
                ", journalEntryLineDetail=" + journalEntryLineDetail +
                ", salesItemLineDetail=" + salesItemLineDetail +
                ", subTotalLineDetail=" + subTotalLineDetail +
                '}';
    }
}
