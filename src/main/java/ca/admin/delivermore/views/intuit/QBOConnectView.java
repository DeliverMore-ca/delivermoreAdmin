package ca.admin.delivermore.views.intuit;

import ca.admin.delivermore.data.service.intuit.controller.QBOController;
import ca.admin.delivermore.views.MainLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.annotation.security.RolesAllowed;

@Route(value = "qboconnect", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class QBOConnectView extends VerticalLayout {

    private static final Logger logger = LoggerFactory.getLogger(QBOConnectView.class);

    @Autowired
    private QBOController qboController;

    public QBOConnectView(QBOController qboController) {
        this.qboController = qboController;
        qboController.connectToQBO();
    }
}
