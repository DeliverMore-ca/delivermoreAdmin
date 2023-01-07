package ca.admin.delivermore.views.about;

import ca.admin.delivermore.collector.data.service.EmailService;
import ca.admin.delivermore.data.intuit.JournalEntry;
import ca.admin.delivermore.data.intuit.NamedItem;
import ca.admin.delivermore.data.service.intuit.controller.QBOController;
import ca.admin.delivermore.data.service.intuit.controller.QBOResult;
import ca.admin.delivermore.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.TreeMap;

@PageTitle("About")
@Route(value = "about", layout = MainLayout.class)
@AnonymousAllowed
public class AboutView extends VerticalLayout {

    private Logger log = LoggerFactory.getLogger(AboutView.class);
    @Autowired
    private EmailService emailService;

    @Autowired
    QBOController QBOController;

    public AboutView() {
        setSpacing(false);

        Image img = new Image("images/delivermorelogo.png", "DeliverMore Admin");
        img.setWidth("400px");
        add(img);

        add(new H2("DeliverMore Admin Application"));
        add(new Paragraph("DeliverMore.ca is a locally owned and operated online ordering and delivery service"));

        Button emailButton = new Button("Email test from tara");
        emailButton.addClickListener(e -> {
            log.info("TEST sending email");
            String body = "line 1\nline 2\nline 3";
            emailService.sendMail("usjusjoken@gmail.com", "Test", body);
        });
        Button qboButton = new Button("Test QBO - get company info");
        qboButton.addClickListener(e -> {
            log.info("TEST quickbooks online integration - remove for prod");
            QBOResult qboResult = QBOController.getCompanyInfo();
            QBOController.showQBOMessageDialog(qboResult.getMessageHeader(),qboResult.getMessage());
        });
        Button qboJEButton = new Button("Test QBO Journal Entry");
        qboJEButton.addClickListener(e -> {
            log.info("TEST quickbooks online integration - remove for prod");
            QBOController.createJournalEntryTest();
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

        add(emailButton,qboButton,qboJEButton,qboListVendorButton,qboListEmployeeButton);

        /*
        Button emailButton2 = new Button("Email preconfigured");
        emailButton2.addClickListener(e -> {
            log.info("TEST sending email preconfigured to support");
            emailService.sendPreConfiguredMail("testing testing 123");
        });

        add(emailButton, emailButton2);

         */

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

}
