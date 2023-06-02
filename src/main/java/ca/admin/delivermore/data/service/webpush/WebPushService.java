package ca.admin.delivermore.data.service.webpush;


import ca.admin.delivermore.collector.data.tookan.Driver;
import ca.admin.delivermore.data.entity.SubscriptionEntity;
import ca.admin.delivermore.data.service.ClientSettings;
import ca.admin.delivermore.security.AuthenticatedUser;
import ca.admin.delivermore.views.about.AboutView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Subscription;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.Security;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
public class WebPushService {

    private String publicKey;
    private String privateKey;
    private String subject;

    private PushService pushService;
    private ClientSettings clientSettings;

    private Logger log = LoggerFactory.getLogger(WebPushService.class);

    @Autowired
    Environment env;

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    private AuthenticatedUser authenticatedUser;

    public WebPushService(@Autowired Environment env, @Autowired SubscriptionRepository subscriptionRepository,@Autowired AuthenticatedUser authenticatedUser) {
        this.env = env;
        this.subscriptionRepository = subscriptionRepository;
        this.authenticatedUser = authenticatedUser;
        this.publicKey = env.getProperty("VAPID_PUBLIC_KEY");
        this.privateKey = env.getProperty("VAPID_PRIVATE_KEY");
        this.subject = env.getProperty("VAPID_SUBJECT");
    }

    @PostConstruct
    private void init() throws GeneralSecurityException {
        Security.addProvider(new BouncyCastleProvider());
        pushService = new PushService(publicKey, privateKey, subject);
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void sendNotification(Subscription subscription, String messageJson) {
        try {
            HttpResponse response = pushService.send(new Notification(subscription, messageJson));
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 201) {
                System.out.println("Server error, status code:" + statusCode);
                InputStream content = response.getEntity().getContent();
                List<String> strings = IOUtils.readLines(content, "UTF-8");
                System.out.println(strings);
            }
        } catch (GeneralSecurityException | IOException | JoseException | ExecutionException
                 | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void subscribe(Subscription subscription) {
        if(clientSettings==null){
            this.clientSettings = new ClientSettings();
        }
        Long driverId = getSignedInDriver().getFleetId();
        String clientId = clientSettings.getCurrentClientID();
        if(driverId!=null){
            log.info("subscribe: driverId:" + driverId + " clientId:" + clientId + " endpoint:" + subscription.endpoint + " subscription:" + subscription.toString());
            subscriptionRepository.save(new SubscriptionEntity(driverId, clientId, subscription));
        }else{
            log.info("subscribe failed as driverId was null");
        }
    }

    public void unsubscribe(Subscription subscription) {
        if(clientSettings==null){
            this.clientSettings = new ClientSettings();
        }
        log.info("unsubscribe: endpoint:" + subscription.endpoint + " subscription:" + subscription.toString() + " auth:" + subscription.keys.auth);
        Long driverId = getSignedInDriver().getFleetId();
        String clientId = clientSettings.getCurrentClientID();
        if(driverId!=null){
            log.info("unsubscribe: driverId:" + driverId + "clientId:" + clientId + " endpoint:" + subscription.endpoint + " subscription:" + subscription.toString());
            subscriptionRepository.deleteByDriverIdAndClientId(driverId, clientId);
        }else{
            log.info("subscribe failed as driverId was null");
        }
    }


    ObjectMapper mapper = new ObjectMapper();

    public void notifyDriver(Long driverId, String title, String body){
        log.info("notifyDriver: driverId:" + driverId + " title:" + title + " body:" + body);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        try {
            log.info("notifyDriver: start of try: title:" + title + " body:" + body);
            String msg = mapper.writeValueAsString(new Message(title, body));
            log.info("notifyDriver: msg:" + msg);

            for (SubscriptionEntity subscriptionEntity: subscriptionRepository.findByDriverId(driverId)) {
                log.info("notifyDriver: sendNotification to subscription:" + subscriptionEntity.getEndpoint() + " msg:" + msg + " driverId:" + subscriptionEntity.getDriverId() + " clientId:" + subscriptionEntity.getClientId());
                sendNotification(subscriptionEntity.getSubscription(), msg);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void notifyAll(String title, String body) {
        log.info("notifyAll: title:" + title + " body:" + body);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        try {
            log.info("notifyAll: start of try: title:" + title + " body:" + body);
            String msg = mapper.writeValueAsString(new Message(title, body));
            log.info("notifyAll: msg:" + msg);

            for (SubscriptionEntity subscriptionEntity: subscriptionRepository.findAll()) {
                log.info("notifyAll: sendNotification to subscription:" + subscriptionEntity.getEndpoint() + " msg:" + msg + " driverId:" + subscriptionEntity.getDriverId() + " clientId:" + subscriptionEntity.getClientId());
                sendNotification(subscriptionEntity.getSubscription(), msg);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Driver getSignedInDriver() {
        return authenticatedUser.get().stream().findFirst().orElse(null);
    }

    public class Message {
        private String title;
        private String body;
        private String icon = "";

        public Message(String title, String body) {
            this.title = title;
            this.body = body;
        }

        public Message(String title, String body, String icon) {
            this.title = title;
            this.body = body;
            this.icon = icon;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        @Override
        public String toString() {
            return "Message{" +
                    "title='" + title + '\'' +
                    ", body='" + body + '\'' +
                    ", icon='" + icon + '\'' +
                    '}';
        }
    }
}
