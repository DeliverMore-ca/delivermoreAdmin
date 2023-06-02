package ca.admin.delivermore.data.entity;

import ca.admin.delivermore.data.service.webpush.WebPushService;
import nl.martijndwars.webpush.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

@Entity
public class SubscriptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private Long driverId;
    private String clientId = null;

    private String subscriptionKey;
    private String auth;
    @Column(name = "endpoint", length = 10000)
    private String endpoint;

    @Transient
    private Logger log = LoggerFactory.getLogger(SubscriptionEntity.class);

    public SubscriptionEntity() {
    }

    public SubscriptionEntity(Long driverId, String clientId, Subscription subscription) {
        this.driverId = driverId;
        this.clientId = clientId;
        this.subscriptionKey = subscription.keys.p256dh;
        this.auth = subscription.keys.auth;
        this.endpoint = subscription.endpoint;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSubscriptionKey() {
        return subscriptionKey;
    }

    public void setSubscriptionKey(String subscriptionKey) {
        this.subscriptionKey = subscriptionKey;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public Subscription getSubscription(){
        Subscription subscription = new Subscription(this.endpoint,getKeys() );
        return subscription;
    }

    public Subscription.Keys getKeys(){
        return new Subscription.Keys(this.subscriptionKey, this.auth);
    }

    @Override
    public String toString() {
        return "SubscriptionEntity{" +
                "id=" + id +
                ", driverId=" + driverId +
                ", clientId='" + clientId + '\'' +
                ", subscriptionKey='" + subscriptionKey + '\'' +
                ", auth='" + auth + '\'' +
                ", endpoint='" + endpoint + '\'' +
                '}';
    }
}
