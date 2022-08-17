package ca.admin.delivermore.data.entity;

import javax.persistence.Entity;

@Entity
public class Orders extends AbstractEntity {

    private Integer taskid;
    private Integer storeid;
    private String storename;
    private String street;
    private String postalCode;
    private String city;
    private String state;
    private String country;
    private Integer subtotal;

    public Integer getTaskid() {
        return taskid;
    }
    public void setTaskid(Integer taskid) {
        this.taskid = taskid;
    }
    public Integer getStoreid() {
        return storeid;
    }
    public void setStoreid(Integer storeid) {
        this.storeid = storeid;
    }
    public String getStorename() {
        return storename;
    }
    public void setStorename(String storename) {
        this.storename = storename;
    }
    public String getStreet() {
        return street;
    }
    public void setStreet(String street) {
        this.street = street;
    }
    public String getPostalCode() {
        return postalCode;
    }
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public Integer getSubtotal() {
        return subtotal;
    }
    public void setSubtotal(Integer subtotal) {
        this.subtotal = subtotal;
    }

}
