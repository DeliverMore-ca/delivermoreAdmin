
package ca.admin.delivermore.data.intuit;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "domain",
    "sparse",
    "Id",
    "SyncToken",
    "MetaData",
    "CustomField",
    "DocNumber",
    "TxnDate",
    "CurrencyRef",
    "PrivateNote",
    "Line",
    "CustomerRef",
    "CustomerMemo",
    "BillAddr",
    "ShipFromAddr",
    "ShipAddr",
    "FreeFormAddress",
    "GlobalTaxCalculation",
    "TotalAmt",
    "PrintStatus",
    "EmailStatus",
    "Balance",
    "PaymentMethodRef",
    "DepositToAccountRef"
})
@Generated("jsonschema2pojo")
public class SalesReceipt {

    private static final Logger log = LoggerFactory.getLogger(SalesReceipt.class);

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
    @JsonProperty("CustomField")
    private List<Object> customField = null;
    @JsonProperty("DocNumber")
    private String docNumber;
    @JsonProperty("TxnDate")
    private String txnDate;
    @JsonProperty("CurrencyRef")
    private CurrencyRef currencyRef;
    @JsonProperty("PrivateNote")
    private String privateNote;
    @JsonProperty("Line")
    private List<Line> line = null;
    @JsonProperty("CustomerRef")
    private CustomerRef customerRef;
    @JsonProperty("CustomerMemo")
    private CustomerMemo customerMemo;
    @JsonProperty("BillAddr")
    private BillAddr billAddr;
    @JsonProperty("ShipAddr")
    private ShipAddr shipAddr;
    @JsonProperty("ShipFromAddr")
    private ShipFromAddr shipFromAddr;
    @JsonProperty("FreeFormAddress")
    private Boolean freeFormAddress;
    @JsonProperty("GlobalTaxCalculation")
    private String globalTaxCalculation;
    @JsonProperty("TotalAmt")
    private Double totalAmt;
    @JsonProperty("PrintStatus")
    private String printStatus;
    @JsonProperty("EmailStatus")
    private String emailStatus;
    @JsonProperty("Balance")
    private Double balance;
    @JsonProperty("PaymentMethodRef")
    private PaymentMethodRef paymentMethodRef;
    @JsonProperty("DepositToAccountRef")
    private DepositToAccountRef depositToAccountRef;

    @JsonIgnore
    private String receiptDateFormat = "yyyy-MM-dd";

    private Map<String, NamedItem> customerMap = null;
    private Map<String, NamedItem> paymentMethodMap = null;
    private Map<String, NamedItem> accountMap = null;
    private Map<String, NamedItem> itemMap = null;

    @JsonIgnore
    private Boolean errorProcessing = Boolean.FALSE;

    @JsonIgnore
    private Double totalOfLines = 0.0;

    public SalesReceipt() {
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

    @JsonProperty("CustomField")
    public List<Object> getCustomField() {
        return customField;
    }

    @JsonProperty("CustomField")
    public void setCustomField(List<Object> customField) {
        this.customField = customField;
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
        this.docNumber = prefix + end.format(DateTimeFormatter.ofPattern(receiptDateFormat));
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
        this.txnDate = date.format(DateTimeFormatter.ofPattern(receiptDateFormat));
    }

    @JsonProperty("CurrencyRef")
    public CurrencyRef getCurrencyRef() {
        return currencyRef;
    }

    @JsonProperty("CurrencyRef")
    public void setCurrencyRef(CurrencyRef currencyRef) {
        this.currencyRef = currencyRef;
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
        //customer memo is the same value so set it here
        CustomerMemo newMemo = new CustomerMemo();
        newMemo.setValue(this.privateNote);
        setCustomerMemo(newMemo);
    }

    @JsonProperty("Line")
    public List<Line> getLine() {
        return line;
    }

    @JsonProperty("Line")
    public void setLine(List<Line> line) {
        this.line = line;
    }

    @JsonProperty("CustomerRef")
    public CustomerRef getCustomerRef() {
        return customerRef;
    }

    @JsonProperty("CustomerRef")
    public void setCustomerRef(CustomerRef customerRef) {
        this.customerRef = customerRef;
    }

    public Boolean setCustomerRef(String customerName){
        //get the customer based on name
        if(customerMap==null){
            customerMap = new TreeMap<>();
            QBOController qboController = Registry.getBean(QBOController.class);
            customerMap = qboController.getNamedItems(Intuit.EntityType.Customer);
        }
        if(!customerMap.containsKey(customerName)){
            log.info("setCustomerRef failed: customerNumber was null for customerName:" + customerName);
            this.errorProcessing = Boolean.TRUE;
            return false;
        }
        String customerNumber = customerMap.get(customerName).getId();
        CustomerRef newCustomerRef = new CustomerRef();
        newCustomerRef.setValue(customerNumber);
        newCustomerRef.setName(customerName);
        setCustomerRef(newCustomerRef);
        return Boolean.TRUE;
    }

    @JsonProperty("CustomerMemo")
    public CustomerMemo getCustomerMemo() {
        return customerMemo;
    }

    @JsonProperty("CustomerMemo")
    public void setCustomerMemo(CustomerMemo customerMemo) {
        this.customerMemo = customerMemo;
    }

    @JsonProperty("BillAddr")
    public BillAddr getBillAddr() {
        return billAddr;
    }

    @JsonProperty("BillAddr")
    public void setBillAddr(BillAddr billAddr) {
        this.billAddr = billAddr;
    }

    @JsonProperty("ShipAddr")
    public ShipAddr getShipAddr() {
        return shipAddr;
    }

    @JsonProperty("ShipAddr")
    public void setShipAddr(ShipAddr shipAddr) {
        this.shipAddr = shipAddr;
    }

    @JsonProperty("ShipFromAddr")
    public ShipFromAddr getShipFromAddr() {
        return shipFromAddr;
    }

    @JsonProperty("ShipFromAddr")
    public void setShipFromAddr(ShipFromAddr shipFromAddr) {
        this.shipFromAddr = shipFromAddr;
    }

    @JsonProperty("FreeFormAddress")
    public Boolean getFreeFormAddress() {
        return freeFormAddress;
    }

    @JsonProperty("FreeFormAddress")
    public void setFreeFormAddress(Boolean freeFormAddress) {
        this.freeFormAddress = freeFormAddress;
    }

    @JsonProperty("GlobalTaxCalculation")
    public String getGlobalTaxCalculation() {
        return globalTaxCalculation;
    }

    @JsonProperty("GlobalTaxCalculation")
    public void setGlobalTaxCalculation(String globalTaxCalculation) {
        this.globalTaxCalculation = globalTaxCalculation;
    }

    @JsonProperty("TotalAmt")
    public Double getTotalAmt() {
        return totalAmt;
    }

    @JsonProperty("TotalAmt")
    public void setTotalAmt(Double totalAmt) {
        this.totalAmt = totalAmt;
    }

    @JsonProperty("PrintStatus")
    public String getPrintStatus() {
        return printStatus;
    }

    @JsonProperty("PrintStatus")
    public void setPrintStatus(String printStatus) {
        this.printStatus = printStatus;
    }

    @JsonProperty("EmailStatus")
    public String getEmailStatus() {
        return emailStatus;
    }

    @JsonProperty("EmailStatus")
    public void setEmailStatus(String emailStatus) {
        this.emailStatus = emailStatus;
    }

    @JsonProperty("Balance")
    public Double getBalance() {
        return balance;
    }

    @JsonProperty("Balance")
    public void setBalance(Double balance) {
        this.balance = balance;
    }

    @JsonProperty("PaymentMethodRef")
    public PaymentMethodRef getPaymentMethodRef() {
        return paymentMethodRef;
    }

    @JsonProperty("PaymentMethodRef")
    public void setPaymentMethodRef(PaymentMethodRef paymentMethodRef) {
        this.paymentMethodRef = paymentMethodRef;
    }

    public Boolean setPaymentMethodRef(String name){
        //get the payment method based on name
        if(paymentMethodMap==null){
            paymentMethodMap = new TreeMap<>();
            QBOController qboController = Registry.getBean(QBOController.class);
            paymentMethodMap = qboController.getNamedItems(Intuit.EntityType.PaymentMethod);
        }
        if(!paymentMethodMap.containsKey(name)){
            log.info("setPaymentMethodRef failed: refNumber was null for name:" + name);
            this.errorProcessing = Boolean.TRUE;
            return false;
        }
        String refNumber = paymentMethodMap.get(name).getId();
        PaymentMethodRef newRef = new PaymentMethodRef();
        newRef.setValue(refNumber);
        newRef.setName(name);
        setPaymentMethodRef(newRef);
        return Boolean.TRUE;
    }

    @JsonProperty("DepositToAccountRef")
    public DepositToAccountRef getDepositToAccountRef() {
        return depositToAccountRef;
    }

    @JsonProperty("DepositToAccountRef")
    public void setDepositToAccountRef(DepositToAccountRef depositToAccountRef) {
        this.depositToAccountRef = depositToAccountRef;
    }

    public Boolean setDepositToAccountRef(String name){
        //get the depost account based on name
        if(accountMap==null){
            accountMap = new TreeMap<>();
            QBOController qboController = Registry.getBean(QBOController.class);
            accountMap = qboController.getNamedItems(Intuit.EntityType.Account);
        }
        if(!accountMap.containsKey(name)){
            log.info("setDepositToAccountRef failed: refNumber was null for name:" + name);
            this.errorProcessing = Boolean.TRUE;
            return false;
        }
        String refNumber = accountMap.get(name).getId();
        DepositToAccountRef newRef = new DepositToAccountRef();
        newRef.setValue(refNumber);
        newRef.setName(name);
        setDepositToAccountRef(newRef);
        return Boolean.TRUE;
    }

    @JsonIgnore
    public Boolean addLine(Integer lineNum, Double amount, String itemName, String accountName, String description){
        if(line==null){
            line = new ArrayList<>();
            totalOfLines = 0.0;
        }
        Line newLine = new Line();
        line.add(newLine);
        newLine.setLineNum(lineNum);
        if(amount==null) amount = 0.0;
        newLine.setAmount(amount);
        this.totalOfLines+= amount;
        //log.info("addLine: amount:" + amount + " totalOfLines:" + totalOfLines);
        if(description!=null) newLine.setDescription(description);
        newLine.setDetailType("SalesItemLineDetail");

        SalesItemLineDetail salesItemLineDetail = new SalesItemLineDetail();
        newLine.setSalesItemLineDetail(salesItemLineDetail);
        salesItemLineDetail.setUnitPrice(amount);
        salesItemLineDetail.setQty(1);

        if(itemName==null){
            log.info("addLine failed: itemName was null");
            this.errorProcessing = Boolean.TRUE;
            return false;
        }
        //get the itemNumber based on name
        if(itemMap==null){
            itemMap = new TreeMap<>();
            QBOController qboController = Registry.getBean(QBOController.class);
            itemMap = qboController.getNamedItems(Intuit.EntityType.Item);
        }
        if(!itemMap.containsKey(itemName)){
            log.info("addLine failed: itemNumber was null for itemName:" + itemName);
            this.errorProcessing = Boolean.TRUE;
            return false;
        }
        String itemNumber = itemMap.get(itemName).getId();
        salesItemLineDetail.setItemRef(new ItemRef(itemName,itemNumber));

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
        salesItemLineDetail.setItemAccountRef(new ItemAccountRef(accountName,accountNumber));
        //log.info("addLine:" + newLine.toString());
        return true;
    }

    @JsonIgnore
    public QBOResult post(){
        QBOResult qboResult = new QBOResult("QBO Sales Receipt");
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
                return qboController.createSalesReceipt(jsonString);
            }
        }else{
            qboResult.setFailed( "Failed to post Sales Receipt '" + getDocNumber() + "'. Total of all lines was not zero (0.0).");
            log.info("post: failed for Sales Receipt:" + getDocNumber() + "'. Total of all lines was not zero (0.0).");
        }
        return qboResult;
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
        if(Double.compare(Utility.getInstance().round(totalOfLines,2),Utility.getInstance().round(0.0,2))==0){
            log.info("isValid: valid: total of lines is zero (0.0)");
            return true;
        }
        log.info("isValid: failed validation: totalOfLines is not zero:" + totalOfLines);
        return false;
    }

    public Boolean getErrorProcessing() {
        return errorProcessing;
    }

    @Override
    public String toString() {
        return "SalesReceipt{" +
                "domain='" + domain + '\'' +
                ", sparse=" + sparse +
                ", id='" + id + '\'' +
                ", syncToken='" + syncToken + '\'' +
                ", metaData=" + metaData +
                ", customField=" + customField +
                ", docNumber='" + docNumber + '\'' +
                ", txnDate='" + txnDate + '\'' +
                ", currencyRef=" + currencyRef +
                ", privateNote='" + privateNote + '\'' +
                ", line=" + line +
                ", customerRef=" + customerRef +
                ", customerMemo=" + customerMemo +
                ", billAddr=" + billAddr +
                ", shipAddr=" + shipAddr +
                ", shipFromAddr=" + shipFromAddr +
                ", freeFormAddress=" + freeFormAddress +
                ", globalTaxCalculation='" + globalTaxCalculation + '\'' +
                ", totalAmt=" + totalAmt +
                ", printStatus='" + printStatus + '\'' +
                ", emailStatus='" + emailStatus + '\'' +
                ", balance=" + balance +
                ", paymentMethodRef=" + paymentMethodRef +
                ", depositToAccountRef=" + depositToAccountRef +
                ", receiptDateFormat='" + receiptDateFormat + '\'' +
                ", customerMap=" + customerMap +
                ", paymentMethodMap=" + paymentMethodMap +
                ", accountMap=" + accountMap +
                ", itemMap=" + itemMap +
                ", errorProcessing=" + errorProcessing +
                ", totalOfLines=" + totalOfLines +
                '}';
    }
}
