
package ca.admin.delivermore.data.intuit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.Generated;

import ca.admin.delivermore.collector.data.Utility;
import ca.admin.delivermore.data.service.Registry;
import ca.admin.delivermore.data.service.intuit.controller.QBOController;
import ca.admin.delivermore.data.service.intuit.controller.QBOResult;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "Adjustment",
    "TotalAmt",
    "HomeTotalAmt",
    "domain",
    "sparse",
    "Id",
    "SyncToken",
    "MetaData",
    "DocNumber",
    "PrivateNote",
    "TxnDate",
    "CurrencyRef",
    "ExchangeRate",
    "Line",
    "TxnTaxDetail"
})
@Generated("jsonschema2pojo")
public class JournalEntry {

    public enum PostingType {
        Debit, Credit
    }

    private static final Logger log = LoggerFactory.getLogger(JournalEntry.class);

    @JsonProperty("Adjustment")
    private Boolean adjustment;
    @JsonProperty("TotalAmt")
    private Integer totalAmt;
    @JsonProperty("HomeTotalAmt")
    private Integer homeTotalAmt;
    @JsonProperty("domain")
    private String domain;
    @JsonProperty("sparse")
    private Boolean sparse;
    @JsonProperty("Id")
    private String id;
    @JsonProperty("SyncToken")
    private String syncToken;
    @JsonProperty("MetaData")
    private MetaData metaData;
    @JsonProperty("DocNumber")
    private String docNumber;
    @JsonProperty("PrivateNote")
    private String privateNote;
    @JsonProperty("TxnDate")
    private String txnDate;
    @JsonProperty("CurrencyRef")
    private CurrencyRef currencyRef;
    @JsonProperty("ExchangeRate")
    private Integer exchangeRate;
    @JsonProperty("Line")
    private List<Line> line = null;
    @JsonProperty("TxnTaxDetail")
    private TxnTaxDetail txnTaxDetail;

    private Map<String, NamedItem> entityVendorMap = null;
    private Map<String, NamedItem> entityEmployeeMap = null;
    private Map<String, NamedItem> accountMap = null;

    private Double debitTotal = 0.0;
    private Double creditTotal = 0.0;

    @JsonIgnore
    private String fileName = "journal_entry.csv";

    @JsonIgnore
    private String journalDateFormat = "yyyy-MM-dd";

    @JsonIgnore
    private Boolean errorProcessing = Boolean.FALSE;

    public JournalEntry() {
    }

    @JsonProperty("Adjustment")
    public Boolean getAdjustment() {
        return adjustment;
    }

    @JsonProperty("Adjustment")
    public void setAdjustment(Boolean adjustment) {
        this.adjustment = adjustment;
    }

    @JsonProperty("TotalAmt")
    public Integer getTotalAmt() {
        return totalAmt;
    }

    @JsonProperty("TotalAmt")
    public void setTotalAmt(Integer totalAmt) {
        this.totalAmt = totalAmt;
    }

    @JsonProperty("HomeTotalAmt")
    public Integer getHomeTotalAmt() {
        return homeTotalAmt;
    }

    @JsonProperty("HomeTotalAmt")
    public void setHomeTotalAmt(Integer homeTotalAmt) {
        this.homeTotalAmt = homeTotalAmt;
    }

    @JsonProperty("domain")
    public String getDomain() {
        return domain;
    }

    @JsonProperty("domain")
    public void setDomain(String domain) {
        this.domain = domain;
    }

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

    @JsonProperty("SyncToken")
    public String getSyncToken() {
        return syncToken;
    }

    @JsonProperty("SyncToken")
    public void setSyncToken(String syncToken) {
        this.syncToken = syncToken;
    }

    @JsonProperty("MetaData")
    public MetaData getMetaData() {
        return metaData;
    }

    @JsonProperty("MetaData")
    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    @JsonProperty("DocNumber")
    public String getDocNumber() {
        return docNumber;
    }

    @JsonProperty("DocNumber")
    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }

    public void setDocNumber(String prefix, LocalDate end){
        this.docNumber = prefix + end.format(DateTimeFormatter.ofPattern(journalDateFormat));
    }

    @JsonProperty("PrivateNote")
    public String getPrivateNote() {
        return privateNote;
    }

    @JsonProperty("PrivateNote")
    public void setPrivateNote(String privateNote) {
        this.privateNote = privateNote;
    }

    public void setPrivateNote(String prefix, LocalDate start, LocalDate end){
        this.privateNote = prefix + " " + start + " - " + end;
    }

    @JsonProperty("TxnDate")
    public String getTxnDate() {
        return txnDate;
    }

    @JsonProperty("TxnDate")
    public void setTxnDate(String txnDate) {
        this.txnDate = txnDate;
    }

    public void setTxnDate(LocalDate date){
        this.txnDate = date.format(DateTimeFormatter.ofPattern(journalDateFormat));
    }

    @JsonProperty("CurrencyRef")
    public CurrencyRef getCurrencyRef() {
        return currencyRef;
    }

    @JsonProperty("CurrencyRef")
    public void setCurrencyRef(CurrencyRef currencyRef) {
        this.currencyRef = currencyRef;
    }

    @JsonProperty("ExchangeRate")
    public Integer getExchangeRate() {
        return exchangeRate;
    }

    @JsonProperty("ExchangeRate")
    public void setExchangeRate(Integer exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    @JsonProperty("Line")
    public List<Line> getLine() {
        return line;
    }

    @JsonProperty("Line")
    public void setLine(List<Line> line) {
        this.line = line;
    }

    @JsonProperty("TxnTaxDetail")
    public TxnTaxDetail getTxnTaxDetail() {
        return txnTaxDetail;
    }

    @JsonProperty("TxnTaxDetail")
    public void setTxnTaxDetail(TxnTaxDetail txnTaxDetail) {
        this.txnTaxDetail = txnTaxDetail;
    }

    @JsonIgnore
    public String getFileName() {
        return fileName;
    }

    @JsonIgnore
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @JsonIgnore
    public void setFileName(String prefix, LocalDate start, LocalDate end){
        this.fileName = prefix + start + " - " + end + ".csv";
    }

    @JsonIgnore
    public Boolean addLine(Double amount, PostingType postingType, String accountName, String description){
        return addLine(amount,postingType,accountName,description,null,null);
    }

    @JsonIgnore
    public Boolean addLine(Double amount, PostingType postingType, String accountName, String description, Intuit.EntityType entityType, String entityName){
        if(line==null){
            line = new ArrayList<>();
        }
        Line newLine = new Line();
        line.add(newLine);
        if(amount==null) amount = 0.0;
        newLine.setAmount(amount);
        if(postingType.equals(PostingType.Debit)){
            debitTotal = debitTotal + Utility.getInstance().round(amount,2);
        }else if(postingType.equals(PostingType.Credit)){
            creditTotal = creditTotal + Utility.getInstance().round(amount,2);
        }
        if(description!=null) newLine.setDescription(description);
        newLine.setDetailType("JournalEntryLineDetail"); //only type supported
        JournalEntryLineDetail journalEntryLineDetail = new JournalEntryLineDetail();
        newLine.setJournalEntryLineDetail(journalEntryLineDetail);
        journalEntryLineDetail.setPostingType(postingType.name());
        if(accountName==null){
            log.info("addLine failed: accountName was null");
            this.errorProcessing = Boolean.TRUE;
            return false;
        }
        //get the accountNumber based on name
        if(accountMap==null){
            accountMap = new TreeMap<>();
            QBOController qboController = Registry.getBean(QBOController.class);
            accountMap = qboController.getNamedItems(Intuit.EntityType.Account);
        }
        if(!accountMap.containsKey(accountName)){
            log.info("addLine failed: accountNumber was null for accountName:" + accountName);
            this.errorProcessing = Boolean.TRUE;
            return false;
        }
        String accountNumber = accountMap.get(accountName).getId();
        journalEntryLineDetail.setAccountRef(new AccountRef(accountName,accountNumber));
        //add Entity info if provided
        if(entityType!=null){
            if(entityName==null){
                log.info("addLine failed: entityName was null for entityType:" + entityType);
                this.errorProcessing = Boolean.TRUE;
                return false;
            }
            Entity entity = new Entity();
            if(entityType.equals(Intuit.EntityType.Vendor)){
                entity.setType(entityType.name());
                //get the map of vendors only once
                if(entityVendorMap==null){
                    entityVendorMap = new TreeMap<>();
                    QBOController qboController = Registry.getBean(QBOController.class);
                    entityVendorMap = qboController.getNamedItems(Intuit.EntityType.Vendor);
                }
                if(!entityVendorMap.containsKey(entityName)){
                    log.info("addLine failed: entityNumber was null for entiyName:" + entityName);
                    this.errorProcessing = Boolean.TRUE;
                    return false;
                }
                String entityNumber = entityVendorMap.get(entityName).getId();
                entity.setEntityRef(new EntityRef(entityName,entityNumber));
            }else if(entityType.equals(Intuit.EntityType.Employee)){
                entity.setType(entityType.name());
                //get the map of employees only once
                if(entityEmployeeMap==null){
                    entityEmployeeMap = new TreeMap<>();
                    QBOController qboController = Registry.getBean(QBOController.class);
                    entityEmployeeMap = qboController.getNamedItems(Intuit.EntityType.Employee);
                }
                if(!entityEmployeeMap.containsKey(entityName)){
                    log.info("addLine failed: entityNumber was null for entiyName:" + entityName);
                    this.errorProcessing = Boolean.TRUE;
                    return false;
                }
                String entityNumber = entityEmployeeMap.get(entityName).getId();
                entity.setEntityRef(new EntityRef(entityName,entityNumber));
            }else{
                log.info("addLine failed: entityType not valid:" + entityType);
                this.errorProcessing = Boolean.TRUE;
                return false;
            }
            journalEntryLineDetail.setEntity(entity);
        }
        return true;
    }

    @JsonIgnore
    public QBOResult post(){
        QBOResult qboResult = new QBOResult("QBO Journal Entry");
        if(isValid()){
            String jsonString = "";
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                jsonString = objectMapper.writeValueAsString(this);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            if(jsonString!=null){
                QBOController qboController = Registry.getBean(QBOController.class);
                return qboController.createJournalEntry(jsonString);
            }
        }else{
            qboResult.setFailed( "Failed to post Journal Entry '" + getDocNumber() + "'. Debits and Credits did not balance.");
            log.info("post: failed for journalEntry:" + getDocNumber() + "'. Debits and Credits did not balance.");
        }
        return qboResult;
    }

    @JsonIgnore
    public QBOResult save(File file){
        QBOResult qboResult = new QBOResult("QBO Save Journal Entry");
        if(isValid()){
            String headerString = "JournalNo,JournalDate,Memo,AccountName,Debits,Credits,Description,Name,Location,Class";
            try {
                FileWriter outputfile = new FileWriter(file);
                log.info("save: outputFile:" + outputfile.toString());
                CSVWriter writer = new CSVWriter(outputfile);
                String[] headers = headerString.split(",");
                writer.writeNext(headers);
                for (Line journalItem: line ) {
                    writer.writeNext(getCsvRow(journalItem));
                }

                writer.close();
            } catch (IOException e) {
                log.error("Problem generating export", e);
                qboResult.setFailed("Problem saving csv file:" + file.getName());
                return qboResult;
            }
            qboResult.setSuccess("Journal Entry:" + getDocNumber() + " saved to: " + file.getName());
        }else{
            qboResult.setFailed( "Failed to save Journal Entry '" + getDocNumber() + "'. Debits and Credits did not balance.");
            log.info("save: failed for journalEntry:" + getDocNumber() + "'. Debits and Credits did not balance.");
        }
        return qboResult;
    }

    @JsonIgnore
    private String[] getCsvRow(Line journalItem){
        String[] row = new String[10];
        row[0] = getDocNumber();
        row[1] = getTxnDate();
        row[2] = getPrivateNote(); //source for memo
        row[3] = journalItem.getJournalEntryLineDetail().getAccountRef().getName();
        if(journalItem.getJournalEntryLineDetail().getPostingType().equals(PostingType.Debit.name())){
            row[4] = journalItem.getAmount().toString();
            row[5] = "";
        }else{
            row[4] = "";
            row[5] = journalItem.getAmount().toString();
        }
        row[6] = journalItem.getDescription();
        if(journalItem.getJournalEntryLineDetail().getEntity()!=null){
            row[7] = journalItem.getJournalEntryLineDetail().getEntity().getEntityRef().getName();
        }else{
            row[7] = "";
        }
        row[8] = ""; //no source for location
        row[9] = ""; //no source for class
        return row;
    }

    @JsonIgnore
    public Boolean isValid(){
        log.info("isValid");
        if(this.line==null){
            log.info("isValid: null line items");
            return false;
        }
        if(this.line.size()==0){
            log.info("isValid: no line items");
            return false;
        }
        if(Double.compare(Utility.getInstance().round(debitTotal,2),Utility.getInstance().round(creditTotal,2))==0){
            log.info("isValid: valid: debits:" + debitTotal + " credits:" + creditTotal);
            return true;
        }
        log.info("isValid: failed validation: debits:" + debitTotal + " credits:" + creditTotal);
        return false;
    }

    public Boolean getErrorProcessing() {
        return errorProcessing;
    }

    public String formattedString() {
        String headerString = "JournalNo,JournalDate,Memo,AccountName,Debits,Credits,Description,Name,Location,Class";
        String out = "";
        out+= this.docNumber + " Date:" + txnDate + " Memo:" + privateNote + "\n";
        for (Line thisLine: line) {
            out+= thisLine.getJournalEntryLineDetail().getAccountRef().getName() + " ";
            out+= thisLine.getJournalEntryLineDetail().getPostingType().toString() + " ";
            out+= thisLine.getAmount() + " ";
            if(thisLine.getJournalEntryLineDetail().getEntity()!=null){
                out+= thisLine.getJournalEntryLineDetail().getEntity().toString() + "\n";
            }else{
                out+= "\n";
            }
        }
        return out;
    }

    @Override
    public String toString() {
        return "JournalEntry{" +
                "adjustment=" + adjustment +
                ", totalAmt=" + totalAmt +
                ", homeTotalAmt=" + homeTotalAmt +
                ", domain='" + domain + '\'' +
                ", sparse=" + sparse +
                ", id='" + id + '\'' +
                ", syncToken='" + syncToken + '\'' +
                ", metaData=" + metaData +
                ", docNumber='" + docNumber + '\'' +
                ", txnDate='" + txnDate + '\'' +
                ", currencyRef=" + currencyRef +
                ", exchangeRate=" + exchangeRate +
                ", line=" + line +
                ", txnTaxDetail=" + txnTaxDetail +
                '}';
    }
}
