package ca.admin.delivermore.views.home;

import ca.admin.delivermore.views.MainLayout;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

@PageTitle("Home")
@Route(value = "home", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@AnonymousAllowed
public class HomeView extends HorizontalLayout {

    public HomeView(@Autowired Environment env) {
        String version = env.getProperty("DM_APPLICATION_RELEASE_VERSION");
        String header = "Welcome to DeliverMore Admin application (v" + version + ")";

        Text welcomeMessage = new Text(header);

        setMargin(true);
        //setVerticalComponentAlignment(Alignment.END, welcomeMessage);

        add(welcomeMessage);
    }

}
