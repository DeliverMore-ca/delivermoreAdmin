package ca.admin.delivermore.data.service.intuit.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource(value="classpath:/application.properties", ignoreResourceNotFound=true)
public class OAuth2Configuration {

    @Autowired
    Environment env;

    private String intuitIdTokenIssuer;
    private String intuitAuthorizationEndpoint;
    private String intuitBearerTokenEndpoint;
    private String intuitRevokeTokenEndpoint;
    private String intuitJsksURI;
    private String intuitUserProfileAPIHost;

    //Get ClientID, Secret and Redirect from System Environment Variables (not from application.properties)
    public String getAppClientId() {
        return env.getProperty("DM_QBO_APPCLIENTID");
    }

    public Boolean isConfigured(){
        if(env.getProperty("DM_QBO_APPCLIENTID")==null) return false;
        if(env.getProperty("DM_QBO_APPCLIENTSECRET")==null) return false;
        if(env.getProperty("DM_QBO_APPREDIRECTURI")==null) return false;
        return true;
    }

    public String getAppClientSecret() {
        return env.getProperty("DM_QBO_APPCLIENTSECRET");
    }

    public String getAppRedirectUri() {
        return env.getProperty("DM_QBO_APPREDIRECTURI");
    }

    public String getUserProfileApiHost() {
        return intuitUserProfileAPIHost;
    }

    public String getAccountingAPIHost() {
        return env.getProperty("IntuitAccountingAPIHost");
    }

    public String getDiscoveryAPIHost() {
        return env.getProperty("DiscoveryAPIHost");
    }

    public String getC2QBScope() {
        return env.getProperty("c2qbScope");
    }

    public String getSIWIScope() {
        return env.getProperty("siwiScope");
    }

    public String getAppNowScope() {
        return env.getProperty("getAppNowScope");
    }

    public String getIntuitIdTokenIssuer() {
        return intuitIdTokenIssuer;
    }

    public String getIntuitAuthorizationEndpoint() {
        return intuitAuthorizationEndpoint;
    }

    public String getIntuitBearerTokenEndpoint() {
        return intuitBearerTokenEndpoint;
    }

    public String getIntuitRevokeTokenEndpoint() {
        return intuitRevokeTokenEndpoint;
    }

    public String getIntuitJsksURI() {
        return intuitJsksURI;
    }

    public void setIntuitIdTokenIssuer(String intuitIdTokenIssuer) {
        this.intuitIdTokenIssuer = intuitIdTokenIssuer;
    }

    public void setIntuitAuthorizationEndpoint(String intuitAuthorizationEndpoint) {
        this.intuitAuthorizationEndpoint = intuitAuthorizationEndpoint;
    }

    public void setIntuitBearerTokenEndpoint(String intuitBearerTokenEndpoint) {
        this.intuitBearerTokenEndpoint = intuitBearerTokenEndpoint;
    }

    public void setIntuitRevokeTokenEndpoint(String intuitRevokeTokenEndpoint) {
        this.intuitRevokeTokenEndpoint = intuitRevokeTokenEndpoint;
    }

    public void setIntuitJsksURI(String intuitJsksURI) {
        this.intuitJsksURI = intuitJsksURI;
    }

    public String getIntuitUserProfileAPIHost() {
        return intuitUserProfileAPIHost;
    }

    public void setIntuitUserProfileAPIHost(String intuitUserProfileAPIHost) {
        this.intuitUserProfileAPIHost = intuitUserProfileAPIHost;
    }

}
