package ca.admin.delivermore.views.about;

import ca.admin.delivermore.collector.data.service.DriversRepository;
import ca.admin.delivermore.collector.data.tookan.Driver;
import ca.admin.delivermore.data.service.intuit.controller.QBOController;
import ca.admin.delivermore.data.service.webpush.WebPushService;
import ca.admin.delivermore.data.service.webpush.WebPushToggle;
import ca.admin.delivermore.security.AuthenticatedUser;
import ca.admin.delivermore.views.MainLayout;
import ca.admin.delivermore.views.UIUtilities;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import nl.martijndwars.webpush.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.annotation.security.RolesAllowed;
import java.util.Optional;

@PageTitle("About")
@Route(value = "about", layout = MainLayout.class)
@RolesAllowed("USER")
public class AboutView extends VerticalLayout {

    private Logger log = LoggerFactory.getLogger(AboutView.class);

    private final WebPushService webPushService;

    @Autowired
    QBOController QBOController;

    @Autowired
    DriversRepository driversRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    Environment env;

    Optional<Driver> signedInDriver;
    private AuthenticatedUser authenticatedUser;


    public AboutView(@Autowired Environment env, WebPushService webPushService,@Autowired AuthenticatedUser authenticatedUser) {
        this.env = env;
        this.webPushService = webPushService;
        this.authenticatedUser = authenticatedUser;

        signedInDriver = getSignedInDriver();
        log.info("signedInDriver:" + signedInDriver);


        setSpacing(false);

        String header = "DeliverMore Admin Application";
        String version = env.getProperty("DM_APPLICATION_RELEASE_VERSION");
        header+= " v" + version;
        add(UIUtilities.createStandardHeader(header,"DeliverMore.ca is a locally owned and operated online ordering and delivery service"));

        /*
        Button qboButton = new Button("Test QBO - get sales receipt");
        qboButton.addClickListener(e -> {
            log.info("TEST quickbooks online integration - remove for prod");
            QBOResult qboResult = QBOController.getSalesReceipt("41");
            QBOController.showQBOMessageDialog(qboResult.getMessageHeader(),qboResult.getMessage());
        });
        add(qboButton);

        Button qboButton2 = new Button("Test QBO - get company info");
        qboButton2.addClickListener(e -> {
            log.info("TEST quickbooks online integration - remove for prod");
            QBOResult qboResult = QBOController.getCompanyInfo();
            QBOController.showQBOMessageDialog(qboResult.getMessageHeader(),qboResult.getMessage());
        });
        add(qboButton2);

         */

        /*
        Button qboButton = new Button("Test QBO - get company info");
        qboButton.addClickListener(e -> {
            log.info("TEST quickbooks online integration - remove for prod");
            QBOResult qboResult = QBOController.getCompanyInfo();
            QBOController.showQBOMessageDialog(qboResult.getMessageHeader(),qboResult.getMessage());
        });
        Button qboListVendorButton = new Button("Test QBO Vendor List");
        qboListVendorButton.addClickListener(e -> {
            log.info("TEST quickbooks online integration - remove for prod");
            Map<String, NamedItem> namedItems = new TreeMap<>();
            namedItems = QBOController.getNamedItems(JournalEntry.EntityType.Vendor);
            String resultItems = "<p>Vendors from QBO:<br>";
            for (NamedItem namedItem: namedItems.values()) {
                resultItems+= namedItem.getDisplayName() + "(" + namedItem.getId() + ")<br>";
            }
            resultItems+= "</p>";
            QBOController.showQBOMessageDialog("QBO Query", "Retrieved list of: " + resultItems, true );

        });
        Button qboListEmployeeButton = new Button("Test QBO Employee List");
        qboListEmployeeButton.addClickListener(e -> {
            log.info("TEST quickbooks online integration - remove for prod");
            Map<String, NamedItem> namedItems = new TreeMap<>();
            namedItems = QBOController.getNamedItems(JournalEntry.EntityType.Employee);
            String resultItems = "<p>Employees from QBO:<br>";
            for (NamedItem namedItem: namedItems.values()) {
                resultItems+= namedItem.getDisplayName() + "(" + namedItem.getId() + ")<br>";
            }
            resultItems+= "</p>";
            QBOController.showQBOMessageDialog("QBO Query", "Retrieved list of: " + resultItems, true );
        });

        add(qboButton,qboListVendorButton,qboListEmployeeButton);

         */

        /*
        Button emailButton2 = new Button("Email preconfigured");
        emailButton2.addClickListener(e -> {
            log.info("TEST sending email preconfigured to support");
            emailService.sendPreConfiguredMail("testing testing 123");
        });

        add(emailButton, emailButton2);

         */

        //TODO:: testing webpush

        if(!signedInDriver.equals(Optional.empty()) && (signedInDriver.get().getName().equals("Test Admin") || signedInDriver.get().getName().equals("DeliverMore Test"))){
            var toggle = new WebPushToggle(webPushService.getPublicKey());
            var messageInput = new TextField("Message:");
            var sendButton = new Button("Notify all users!");

            add(
                    new H1("Web Push Notification Demo"),
                    toggle,
                    new HorizontalLayout(messageInput, sendButton) {{setDefaultVerticalComponentAlignment(Alignment.BASELINE);}}
            );

            toggle.addSubscribeListener(e -> {
                subscribe(e.getSubscription());
            });
            toggle.addUnsubscribeListener(e -> {
                unsubscribe(e.getSubscription());
            });

            sendButton.addClickListener(e -> {
                log.info("send message:" + messageInput.getValue());
                webPushService.notifyAll("Message from user", messageInput.getValue());
                log.info("send message:" + messageInput.getValue() + " AFTER");
            });

        }

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

    public void subscribe(Subscription subscription) {
        webPushService.subscribe(subscription);
    }

    public void unsubscribe(Subscription subscription) {
        webPushService.unsubscribe(subscription);
    }

    private Optional<Driver> getSignedInDriver() {
        log.info("getSignedInDriver:" + authenticatedUser.get());
        return authenticatedUser.get();
    }


}
