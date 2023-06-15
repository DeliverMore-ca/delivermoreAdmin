package ca.admin.delivermore.data.entity;

import ca.admin.delivermore.collector.data.Utility;
import ca.admin.delivermore.data.service.GiftCardRepository;
import ca.admin.delivermore.data.service.GiftCardTranactionRepository;
import ca.admin.delivermore.data.service.Registry;
import org.apache.commons.lang.RandomStringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Entity
public class GiftCardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    private LocalDate issued;

    @NotNull
    private Double amount = 0.0;

    private String code = "";
    private String customerName = "";
    private String customerEmail = "";
    private String notes = "";

    private Boolean asGift = Boolean.FALSE;
    private String giftName = "";
    private String giftEmail = "";
    private String giftNote = "";

    public GiftCardEntity() {
        //this.code = getUniqueCode();
        this.issued = LocalDate.now();
    }

    public LocalDate getIssued() {
        return issued;
    }

    public void setIssued(LocalDate issued) {
        this.issued = issued;
    }

    public Double getAmount() {
        return Utility.getInstance().round(amount,2);
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getRedeemed() {
        GiftCardTranactionRepository giftCardTranactionRepository = Registry.getBean(GiftCardTranactionRepository.class);
        List<GiftCardTranactionEntity> giftCardTranactionList = giftCardTranactionRepository.findByCodeOrderByTransactionDateTimeDesc(getCode());
        if(giftCardTranactionList==null || giftCardTranactionList.size()==0){
            return 0.0;
        }
        Double redeemed = 0.0;
        for (GiftCardTranactionEntity transaction: giftCardTranactionList) {
            redeemed += transaction.getAmount();
        }
        return Utility.getInstance().round(redeemed,2);
    }

    public List<GiftCardTranactionEntity> getTransactions(){
        GiftCardTranactionRepository giftCardTranactionRepository = Registry.getBean(GiftCardTranactionRepository.class);
        List<GiftCardTranactionEntity> giftCardTranactionList = giftCardTranactionRepository.findByCodeOrderByTransactionDateTimeDesc(getCode());
        if(giftCardTranactionList==null || giftCardTranactionList.size()==0){
            return List.of();
        }
        return giftCardTranactionList;
    }

    public String getCode() {
        return code.toUpperCase();
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getAsGift() {
        if(asGift==null) return Boolean.FALSE;
        return asGift;
    }

    public void setAsGift(Boolean asGift) {
        this.asGift = asGift;
    }

    public String getGiftEmail() {
        return giftEmail;
    }

    public void setGiftEmail(String giftEmail) {
        this.giftEmail = giftEmail;
    }

    public String getGiftNote() {
        return giftNote;
    }

    public void setGiftNote(String giftNote) {
        this.giftNote = giftNote;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public Double getBalance() {
        return Utility.getInstance().round(getAmount() - getRedeemed(),2);
    }

    public String getUniqueCode(){
        String uniqueCode = null;
        GiftCardRepository giftCardRepository = Registry.getBean(GiftCardRepository.class);
        do {
            // code block to be executed
            String tempCode = "DM" + RandomStringUtils.randomAlphanumeric(5);
            if(!giftCardRepository.existsByCodeIgnoreCase(tempCode)){
                uniqueCode = tempCode.toUpperCase();
            }
        }
        while (uniqueCode==null);
        return uniqueCode;
    }

    public String getEmailAddresses(){
        String emailAddresses = customerEmail;
        if(asGift!=null && asGift){
            emailAddresses = giftEmail;
        }
        return emailAddresses;
    }

    public Boolean hasGiftNote(){
        if(asGift==null || !asGift || giftNote==null || giftNote.isEmpty()) return Boolean.FALSE;
        return Boolean.TRUE;
    }

    public String getEmailFullAddress(){
        String email = "support@delivermore.ca";
        if(!getEmailAddresses().isEmpty()){
            email += "," + getEmailAddresses();
        }
        return email;
    }

    public String getEmailSubject(){
        String subject = "DeliverMore Gift Card " + getCode() + " details";
        return subject;
    }

    public String getEmailBody(){
        String gcUse = "Note: to use this gift card please make sure to have the Code mentioned above available to present to the driver upon delivery.<br><br>";
        String balanceInfo = "";
        if(getBalance()>0){
            balanceInfo = "&nbsp;Balance $" + getBalance() + ".<br><br>";
        }else{
            balanceInfo = "&nbsp;Balance - your card no longer has a balance.<br><br>";
            gcUse = "";
        }
        String thanks = "Thanks for using DeliverMore!<br><br>Visit us at <a href=\"https://delivermore.ca\">delivermore.ca</a>";
        String customerInfo = "Purchased by:" + getCustomerName() + "<br>";
        if(getAsGift()){
            customerInfo += "&nbsp;For:" + getGiftName() + "<br>";
            if(hasGiftNote()){
                customerInfo += "&nbsp;Note:" + getGiftNote() + "<br>";
            }
        }
        customerInfo += "<br>";
        String gcCode = "DeliverMore Gift Card Code: " + getCode();

        //include any transactions
        String transactionList = "";
        List<GiftCardTranactionEntity> transactions = getTransactions();
        if(transactions.size()>0){
            transactionList = "&nbsp;Past transactions:<br>";
            for (GiftCardTranactionEntity tItem: transactions) {
                transactionList += "&nbsp;&nbsp;Date:" + tItem.getTransactionDateTimeFmt() + " Amount: $" + tItem.getAmount() + "<br>";
            }
            transactionList += "<br>";
        }

        String body = "<p>" + gcCode + "<br><br>" + customerInfo + "Here are the details of your gift card.<br>&nbsp;Original amount: $" + getAmount() + "<br>" + balanceInfo + transactionList + gcUse + thanks + "</p>";
        return body;
    }

    @Override
    public String toString() {
        return "GiftCardEntity{" +
                "id=" + id +
                ", issued=" + issued +
                ", amount=" + amount +
                ", code='" + code + '\'' +
                ", customerName='" + customerName + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                ", notes='" + notes + '\'' +
                ", asGift=" + asGift +
                ", giftName='" + giftName + '\'' +
                ", giftEmail='" + giftEmail + '\'' +
                ", giftNote='" + giftNote + '\'' +
                '}';
    }
}
