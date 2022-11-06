package ca.admin.delivermore.views.about;

import ca.admin.delivermore.collector.data.service.EmailService;
import ca.admin.delivermore.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("About")
@Route(value = "about", layout = MainLayout.class)
@AnonymousAllowed
public class AboutView extends VerticalLayout {

    @Autowired
    private EmailService emailService;

    public AboutView() {
        setSpacing(false);

        Image img = new Image("images/delivermorelogo.png", "DeliverMore Admin");
        img.setWidth("400px");
        add(img);

        add(new H2("DeliverMore Admin Application"));
        add(new Paragraph("DeliverMore.ca is a locally owned and operated online ordering and delivery service"));

        Button emailButton = new Button("Email test from tara");
        emailButton.addClickListener(e -> {
            System.out.println("TEST sending email");
            emailService.sendMail("usjusjoken@gmail.com", "Test", "testing testing 123");
        });
        add(emailButton);

        /*
        Button emailButton2 = new Button("Email preconfigured");
        emailButton2.addClickListener(e -> {
            System.out.println("TEST sending email preconfigured to support");
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
