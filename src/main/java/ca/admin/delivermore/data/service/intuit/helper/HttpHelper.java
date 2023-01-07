package ca.admin.delivermore.data.service.intuit.helper;

import ca.admin.delivermore.collector.data.Config;
import ca.admin.delivermore.data.service.intuit.domain.OAuth2Configuration;
import com.vaadin.flow.server.VaadinSession;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class HttpHelper {

    @Autowired
    public OAuth2Configuration oAuth2Configuration;

    public HttpPost addHeader(HttpPost post) {
        String base64ClientIdSec = Base64.encodeBase64String((oAuth2Configuration.getAppClientId() + ":" + oAuth2Configuration.getAppClientSecret()).getBytes());
        post.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        post.setHeader("Authorization", "Basic " + base64ClientIdSec);
        post.setHeader("Accept", "application/json");
        return post;
    }

    public List<NameValuePair> getUrlParameters(String action) {
        List<NameValuePair> urlParameters = new ArrayList<>();
        String refreshToken = Config.getInstance().getQBORefreshToken();
        if (action == "revoke") {
            urlParameters.add(new BasicNameValuePair("token", refreshToken));
        } else if (action == "refresh") {
            urlParameters.add(new BasicNameValuePair("refresh_token", refreshToken));
            urlParameters.add(new BasicNameValuePair("grant_type", "refresh_token"));
        } else {
            String auth_code = Config.getInstance().getQBOAuthCode();
            urlParameters.add(new BasicNameValuePair("code", auth_code));
            urlParameters.add(new BasicNameValuePair("redirect_uri", oAuth2Configuration.getAppRedirectUri()));
            urlParameters.add(new BasicNameValuePair("grant_type", "authorization_code"));
        }
        return urlParameters;
    }

    public StringBuffer getResult(HttpResponse response) throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        return result;
    }

}
