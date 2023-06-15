package ca.admin.delivermore.data.entity;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "select_1",
        "number_1",
        "name_1",
        "email_1",
        "radio_1",
        "name_2",
        "email_2",
        "textarea_1",
        "paymentid",
        "paymentmethod",
        "subscriptionid",
        "hidden_1",
        "referer_url",
        "_wp_http_referer",
        "page_id",
        "form_type",
        "current_url",
        "render_id",
        "calculation_3",
        "stripe_1",
        "_forminator_user_ip",
        "form_title",
        "entry_time"
})
@Generated("jsonschema2pojo")


public class GiftCardWordpress {

    @JsonProperty("select_1")
    private String select1;
    @JsonProperty("number_1")
    private String number1;
    @JsonProperty("name_1")
    private String name1;
    @JsonProperty("email_1")
    private String email1;
    @JsonProperty("radio_1")
    private String radio1;
    @JsonProperty("name_2")
    private String name2;
    @JsonProperty("email_2")
    private String email2;
    @JsonProperty("textarea_1")
    private String textarea_1;
    @JsonProperty("paymentid")
    private String paymentid;
    @JsonProperty("paymentmethod")
    private String paymentmethod;
    @JsonProperty("subscriptionid")
    private String subscriptionid;
    @JsonProperty("hidden_1")
    private String hidden1;
    @JsonProperty("referer_url")
    private String refererUrl;
    @JsonProperty("_wp_http_referer")
    private String wpHttpReferer;
    @JsonProperty("page_id")
    private String pageId;
    @JsonProperty("form_type")
    private String formType;
    @JsonProperty("current_url")
    private String currentUrl;
    @JsonProperty("render_id")
    private String renderId;
    @JsonProperty("calculation_3")
    private String calculation3;
    @JsonProperty("stripe_1")
    private String stripe1;
    @JsonProperty("_forminator_user_ip")
    private Object forminatorUserIp;
    @JsonProperty("form_title")
    private String formTitle;
    @JsonProperty("entry_time")
    private String entryTime;

    @JsonProperty("select_1")
    public String getSelect1() {
        return select1;
    }

    @JsonProperty("select_1")
    public void setSelect1(String select1) {
        this.select1 = select1;
    }

    @JsonProperty("number_1")
    public String getNumber1() {
        return number1;
    }

    @JsonProperty("number_1")
    public void setNumber1(String number1) {
        this.number1 = number1;
    }

    @JsonProperty("name_1")
    public String getName1() {
        return name1;
    }

    @JsonProperty("name_1")
    public void setName1(String name1) {
        this.name1 = name1;
    }

    @JsonProperty("email_1")
    public String getEmail1() {
        return email1;
    }

    @JsonProperty("email_1")
    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    @JsonProperty("radio_1")
    public String getRadio1() {
        return radio1;
    }

    @JsonProperty("radio_1")
    public void setRadio1(String radio1) {
        this.radio1 = radio1;
    }

    @JsonProperty("name_2")
    public String getName2() {
        return name2;
    }

    @JsonProperty("name_2")
    public void setName2(String name2) {
        this.name2 = name2;
    }

    @JsonProperty("email_2")
    public String getEmail2() {
        return email2;
    }

    @JsonProperty("email_2")
    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    @JsonProperty("paymentid")
    public String getPaymentid() {
        return paymentid;
    }

    @JsonProperty("paymentid")
    public void setPaymentid(String paymentid) {
        this.paymentid = paymentid;
    }

    @JsonProperty("paymentmethod")
    public String getPaymentmethod() {
        return paymentmethod;
    }

    @JsonProperty("paymentmethod")
    public void setPaymentmethod(String paymentmethod) {
        this.paymentmethod = paymentmethod;
    }

    @JsonProperty("subscriptionid")
    public String getSubscriptionid() {
        return subscriptionid;
    }

    @JsonProperty("subscriptionid")
    public void setSubscriptionid(String subscriptionid) {
        this.subscriptionid = subscriptionid;
    }

    @JsonProperty("hidden_1")
    public String getHidden1() {
        return hidden1;
    }

    @JsonProperty("hidden_1")
    public void setHidden1(String hidden1) {
        this.hidden1 = hidden1;
    }

    @JsonProperty("referer_url")
    public String getRefererUrl() {
        return refererUrl;
    }

    @JsonProperty("referer_url")
    public void setRefererUrl(String refererUrl) {
        this.refererUrl = refererUrl;
    }

    @JsonProperty("_wp_http_referer")
    public String getWpHttpReferer() {
        return wpHttpReferer;
    }

    @JsonProperty("_wp_http_referer")
    public void setWpHttpReferer(String wpHttpReferer) {
        this.wpHttpReferer = wpHttpReferer;
    }

    @JsonProperty("page_id")
    public String getPageId() {
        return pageId;
    }

    @JsonProperty("page_id")
    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    @JsonProperty("form_type")
    public String getFormType() {
        return formType;
    }

    @JsonProperty("form_type")
    public void setFormType(String formType) {
        this.formType = formType;
    }

    @JsonProperty("current_url")
    public String getCurrentUrl() {
        return currentUrl;
    }

    @JsonProperty("current_url")
    public void setCurrentUrl(String currentUrl) {
        this.currentUrl = currentUrl;
    }

    @JsonProperty("render_id")
    public String getRenderId() {
        return renderId;
    }

    @JsonProperty("render_id")
    public void setRenderId(String renderId) {
        this.renderId = renderId;
    }

    @JsonProperty("calculation_3")
    public String getCalculation3() {
        return calculation3;
    }

    @JsonProperty("calculation_3")
    public void setCalculation3(String calculation3) {
        this.calculation3 = calculation3;
    }

    @JsonProperty("stripe_1")
    public String getStripe1() {
        return stripe1;
    }

    @JsonProperty("stripe_1")
    public void setStripe1(String stripe1) {
        this.stripe1 = stripe1;
    }

    @JsonProperty("_forminator_user_ip")
    public Object getForminatorUserIp() {
        return forminatorUserIp;
    }

    @JsonProperty("_forminator_user_ip")
    public void setForminatorUserIp(Object forminatorUserIp) {
        this.forminatorUserIp = forminatorUserIp;
    }

    @JsonProperty("form_title")
    public String getFormTitle() {
        return formTitle;
    }

    @JsonProperty("form_title")
    public void setFormTitle(String formTitle) {
        this.formTitle = formTitle;
    }

    @JsonProperty("entry_time")
    public String getEntryTime() {
        return entryTime;
    }

    @JsonProperty("entry_time")
    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }

    public String getTextarea_1() {
        return textarea_1;
    }

    public void setTextarea_1(String textarea_1) {
        this.textarea_1 = textarea_1;
    }

    public Double getAmount(){
        if(select1==null) return 0.0;
        if(select1.equals("gc-25")) return 25.0;
        else if(select1.equals("gc-50")) return 50.0;
        else if(select1.equals("gc-75")) return 50.0;
        else if(select1.equals("gc-100")) return 50.0;

        return 0.0;
    }

    public Integer getCount(){
        if(number1==null) return 0;
        return Integer.valueOf(number1);
    }

    public String getSecret(){
        if(hidden1==null) return "";
        return hidden1;
    }

    public String getName(){
        if(name1==null) return "";
        return name1;
    }

    public String getEmail(){
        if(email1==null) return "";
        return email1;
    }

    public Boolean hasRecipient(){
        if(radio1==null) return Boolean.FALSE;
        if(radio1.equals("Yes")) return Boolean.TRUE;
        return Boolean.FALSE;
    }

    public String getRecipient(){
        if(name2==null) return "";
        return name2;
    }

    public String getRecipientEmail(){
        if(email2==null) return "";
        return email2;
    }

    public String getRecipientNote(){
        if(textarea_1==null) return "";
        return textarea_1;
    }

    @Override
    public String toString() {
        return "GiftCardWordpress{" +
                "amount=" + getAmount() +
                ", count=" + getCount() +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", hasRecipient=" + hasRecipient() +
                ", recipient='" + getRecipient() + '\'' +
                ", recipientEmail='" + getRecipientEmail() + '\'' +
                ", recipientNote='" + getRecipientNote() + '\'' +
                '}';
    }
}