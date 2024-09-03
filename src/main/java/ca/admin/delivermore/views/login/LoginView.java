package ca.admin.delivermore.views.login;

import ca.admin.delivermore.security.AuthenticatedUser;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PageTitle("Login")
@Route(value = "login")
@AnonymousAllowed
public class LoginView extends LoginOverlay implements BeforeEnterObserver {

    private Logger log = LoggerFactory.getLogger(LoginView.class);
    private final AuthenticatedUser authenticatedUser;

    public LoginView(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
        setAction("login");

        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle("DeliverMore");
        i18n.getHeader().setDescription("Login using your DeliverMore email address");
        i18n.setAdditionalInformation("Contact support@delivermore.ca if you're experiencing issues logging into your account");
        i18n.getForm().setUsername("Email");
        i18n.getForm().setForgotPassword("Forgot/Reset password");
        setI18n(i18n);

        setForgotPasswordButtonVisible(true);
        addForgotPasswordListener(e -> {
            log.info("LoginView: forgot/reset called");
            getUI().ifPresent(ui -> ui.navigate(PasswordReset.class));
        });
        setOpened(true);
    }



    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (authenticatedUser.get().isPresent()) {
            // Already logged in
            setOpened(false);
            event.forwardTo("");
        }

        setError(event.getLocation().getQueryParameters().getParameters().containsKey("error"));
    }
}
