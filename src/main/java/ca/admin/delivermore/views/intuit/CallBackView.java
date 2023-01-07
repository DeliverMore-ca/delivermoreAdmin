package ca.admin.delivermore.views.intuit;

import ca.admin.delivermore.collector.data.Config;
import ca.admin.delivermore.data.service.intuit.controller.QBOController;
import ca.admin.delivermore.data.service.intuit.domain.BearerTokenResponse;
import ca.admin.delivermore.data.service.intuit.domain.OAuth2Configuration;
import ca.admin.delivermore.data.service.intuit.helper.HttpHelper;
import ca.admin.delivermore.data.service.intuit.service.ValidationService;
import ca.admin.delivermore.views.MainLayout;
import ca.admin.delivermore.views.home.HomeView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.RequestHandler;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinResponse;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Route(value = "oauth2redirect", layout = MainLayout.class)
@AnonymousAllowed
public class CallBackView extends VerticalLayout implements RequestHandler, HasUrlParameter<String> {

    private static final Logger logger = LoggerFactory.getLogger(CallBackView.class);
    private static final HttpClient CLIENT = HttpClientBuilder.create().build();
    private static ObjectMapper mapper = new ObjectMapper();
    @Autowired
    public OAuth2Configuration oAuth2Configuration;

    @Autowired
    public ValidationService validationService;

    @Autowired
    public HttpHelper httpHelper;

    @Autowired
    private QBOController qboController;

    public CallBackView() {
        logger.info("CallBackView called:");
    }

    private void processRequest(String authCode, String realmId, String state){
        if(authCode==null || realmId==null || state==null){
            logger.info("processRequest: null passed. Cannot process authCode:" + authCode + " realmId:" + realmId + " state:" + state);
            showResultDialog(true);
            return;
        }
        //String csrfToken = (String) session.getAttribute("csrfToken");
        logger.info("processRequest: processing authCode:" + authCode + " realmId:" + realmId + " state:" + state);
        String csrfToken = Config.getInstance().getQBOState();
        if (csrfToken.equals(state)) {
            Config.getInstance().setQBORealmId(realmId);
            Config.getInstance().setQBOAuthCode(authCode);
            BearerTokenResponse bearerTokenResponse = retrieveBearerTokens(authCode);

            logger.info("processRequest: saving token:" + bearerTokenResponse.getAccessToken());
            Config.getInstance().setQBOToken(bearerTokenResponse.getAccessToken());
            logger.info("processRequest: saving refresh token:" + bearerTokenResponse.getRefreshToken());
            Config.getInstance().setQBORefreshToken(bearerTokenResponse.getRefreshToken());
            Config.getInstance().setQBORealmId(realmId);


        }else{
            logger.info("processRequest: csrf token mismatch. Not continuing" );
        }
        //go to the connected view
        logger.info("processRequest: navigating to connected view");
        showResultDialog(false);

    }

    private void showResultDialog(Boolean failed){
        String message = "";
        if(failed){
            message = "Could not make connection to QBO.  Try again later.";
        }else{
            String accessToken = Config.getInstance().getQBOToken();
            if(accessToken==null || accessToken.isEmpty()){
                logger.info("QBOConnected: access token not found");
                message =  "Access token was not set.  Connection to QBO Failed.  Please try again.";
            }else{
                logger.info("QBOConnected: access token found");
                message =  "Connection to QBO succeeded.";
            }
        }
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("QBO Connection");
        dialog.setText(message);
        dialog.setConfirmText("OK");
        dialog.open();

        UI.getCurrent().navigate(HomeView.class);

    }

    @Override
    public boolean handleRequest(VaadinSession vaadinSession, VaadinRequest vaadinRequest, VaadinResponse vaadinResponse) throws IOException {
        logger.debug("inside oauth2redirect"  );

        return false;
    }

    private BearerTokenResponse retrieveBearerTokens(String auth_code) {
        logger.info("inside bearer tokens");

        HttpPost post = new HttpPost(oAuth2Configuration.getIntuitBearerTokenEndpoint());

        // add header
        post = httpHelper.addHeader(post);
        List<NameValuePair> urlParameters = httpHelper.getUrlParameters("");

        try {
            post.setEntity(new UrlEncodedFormEntity(urlParameters));
            HttpResponse response = CLIENT.execute(post);

            logger.info("Response Code : "+ response.getStatusLine().getStatusCode());
            if (response.getStatusLine().getStatusCode() != 200) {
                logger.info("failed getting access token");
                return null;
            }

            StringBuffer result = httpHelper.getResult(response);
            logger.debug("raw result for bearer tokens= " + result);

            return mapper.readValue(result.toString(), BearerTokenResponse.class);

        } catch (Exception ex) {
            logger.error("Exception while retrieving bearer tokens", ex);
        }
        return null;
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String parameter) {
        logger.info("CallBackView: setParameter");
        Location location = beforeEvent.getLocation();
        QueryParameters queryParameters = location.getQueryParameters();

        Map<String, List<String>> parametersMap = queryParameters
                .getParameters();

        String authCode = getParamFromList(parametersMap,"code");
        String state = getParamFromList(parametersMap,"state");
        String realmId = getParamFromList(parametersMap,"realmId");

        processRequest(authCode,realmId,state);
    }

    private String getParamFromList(Map<String, List<String>> map, String item){
        if(map.containsKey(item)){
            return map.get(item).get(0);
        }else{
            return null;
        }
    }
}
