package ca.admin.delivermore.security;

import ca.admin.delivermore.views.home.HomeView;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.router.RouteNotFoundError;
import com.vaadin.flow.router.internal.DefaultErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletResponse;

@DefaultErrorHandler
public class DMNoRouteHandler extends RouteNotFoundError {

    private static final Logger log = LoggerFactory.getLogger(DMNoRouteHandler.class);

    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> parameter) {
        log.info("setErrorParameter: Not existing view requested with name: /" + event.getLocation().getPath());
        log.info("setErrorParameter: Redirect user to /home");
        event.forwardTo(HomeView.class);
        return HttpServletResponse.SC_NOT_FOUND;
    }
}
