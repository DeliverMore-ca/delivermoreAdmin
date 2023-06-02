package ca.admin.delivermore.data.service.intuit.controller;

import ca.admin.delivermore.collector.data.Config;
import ca.admin.delivermore.data.intuit.*;
import ca.admin.delivermore.data.service.intuit.domain.BearerTokenResponse;
import ca.admin.delivermore.data.service.intuit.domain.OAuth2Configuration;
import ca.admin.delivermore.data.service.intuit.helper.HttpHelper;
import ca.admin.delivermore.data.service.intuit.service.RefreshTokenService;
import ca.admin.delivermore.data.service.intuit.service.ValidationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Controller
public class QBOController {

    private static final Logger logger = LoggerFactory.getLogger(QBOController.class);
    private static final HttpClient CLIENT = HttpClientBuilder.create().build();

    @Autowired
    public OAuth2Configuration oAuth2Configuration;

    @Autowired
    public ValidationService validationService;

    @Autowired
    public HttpHelper httpHelper;

    @Autowired
    public RefreshTokenService refreshTokenService;

    public QBOController() {
        logger.info("QBOController constructor: init here");
    }

    public QBOResult getCompanyInfo(){
        logger.info("QBOController: getCompanyInfo");
        QBOResult qboResult = new QBOResult("QBO Process Result");
        if(hasTokens()){
            String realmId = Config.getInstance().getQBORealmId();
            if (StringUtils.isEmpty(realmId)) {
                qboResult.setFailed("QBO Process Failed","No QBO connection information found.  Please connect first from Utilities menu.");
            }else{
                String companyInfoEndpoint = String.format("%s/v3/company/%s/companyinfo/%s", oAuth2Configuration.getAccountingAPIHost(), realmId, realmId);

                HttpGet companyInfoReq = new HttpGet(companyInfoEndpoint);

                companyInfoReq.setHeader("Accept", "application/json");
                String accessToken = Config.getInstance().getQBOToken();
                companyInfoReq.setHeader("Authorization","Bearer " + accessToken);

                try {

                    HttpResponse response = CLIENT.execute(companyInfoReq);

                    logger.info("Response Code : "+ response.getStatusLine().getStatusCode());

                    /*
                     * Handle 401 status code -
                     * If a 401 response is received, refresh tokens should be used to get a new access token,
                     * and the API call should be tried again.
                     */
                    if (response.getStatusLine().getStatusCode() == 401) {
                        StringBuffer result = httpHelper.getResult(response);
                        logger.info("raw result for 401 companyInfo= " + result);

                        //refresh tokens
                        logger.info("received 401 during companyinfo call, refreshing tokens now");
                        BearerTokenResponse bearerTokenResponse = refreshTokenService.refresh();
                        if(bearerTokenResponse==null){
                            logger.info("executeQuery: failed refreshing token");
                            qboResult.setFailed("QBO Process Failed","Failed refreshing token");
                            return qboResult;
                        }
                        Config.getInstance().setQBOToken(bearerTokenResponse.getAccessToken());
                        Config.getInstance().setQBORefreshToken(bearerTokenResponse.getRefreshToken());

                        //call company info again using new tokens
                        logger.info("calling companyinfo using new tokens");
                        companyInfoReq.setHeader("Authorization","Bearer " + bearerTokenResponse.getAccessToken());
                        response = CLIENT.execute(companyInfoReq);
                    }

                    if (response.getStatusLine().getStatusCode() != 200){
                        logger.info("failed getting companyInfo");
                        qboResult.setFailed("QBO Process Failed","Failed to get company info. Code:" + response.getStatusLine().getStatusCode());
                    }else{
                        StringBuffer result = httpHelper.getResult(response);
                        logger.info("raw result for companyInfo= " + result);
                        qboResult.setResult(result.toString());
                        qboResult.setSuccess("Company Info:" + qboResult.getResult());
                    }

                } catch (Exception ex) {
                    logger.error("Exception while getting company info ", ex);
                    qboResult.setFailed("QBO Process Failed","Exception while getting company info:" + ex);
                } finally {
                    companyInfoReq.releaseConnection();
                }
            }
        }
        return qboResult;
    }

    public QBOResult createJournalEntry(String journalEntryJSON){
        logger.info("QBOController: createJournalEntry");
        QBOResult qboResult = new QBOResult("QBO Process Result");
        if(hasTokens()){
            String realmId = Config.getInstance().getQBORealmId();
            if (StringUtils.isEmpty(realmId)) {
                qboResult.setFailed("QBO Process Failed","No QBO connection information found.  Please connect first from Utilities menu.");
            }else{
                String minorVersion = Config.getInstance().getQBOMinorVersion();
                String journalEntryEndpoint = String.format("%s/v3/company/%s/journalentry?minorversion=%s", oAuth2Configuration.getAccountingAPIHost(), realmId, minorVersion);

                HttpPost journalEntryReq = new HttpPost(journalEntryEndpoint);

                journalEntryReq.setHeader("Accept", "application/json");
                String accessToken = Config.getInstance().getQBOToken();
                journalEntryReq.setHeader("Authorization","Bearer " + accessToken);
                journalEntryReq.setHeader("Content-Type","application/json");

                StringEntity params = null;
                try {
                    //TODO:: need to generate a JE JSON - do I need to urlEncode this too ??
                    logger.info("createJournalEntry: setting params:" + journalEntryJSON);
                    params = new StringEntity(journalEntryJSON);
                    logger.info("createJournalEntry: params:"+ params);
                } catch (UnsupportedEncodingException e) {
                    logger.info("createJournalEntry: setting params FAILED");
                    throw new RuntimeException(e);
                }
                logger.info("createJournalEntry: setting entity");
                journalEntryReq.setEntity(params);

                try {

                    logger.info("createJournalEntry: calling HttpResponse with journalEntryReq:" + journalEntryReq);
                    HttpResponse response = CLIENT.execute(journalEntryReq);

                    logger.info("createJournalEntry: Response Code : "+ response.getStatusLine().getStatusCode());

                    /*
                     * Handle 401 status code -
                     * If a 401 response is received, refresh tokens should be used to get a new access token,
                     * and the API call should be tried again.
                     */
                    if (response.getStatusLine().getStatusCode() == 401) {
                        StringBuffer result = httpHelper.getResult(response);
                        logger.info("createJournalEntry: raw result for 401:" + result);

                        //refresh tokens
                        logger.info("createJournalEntry: received 401 during call, refreshing tokens now");
                        BearerTokenResponse bearerTokenResponse = refreshTokenService.refresh();
                        if(bearerTokenResponse==null){
                            logger.info("executeQuery: failed refreshing token");
                            qboResult.setFailed("QBO Process Failed","Failed refreshing token");
                            return qboResult;
                        }
                        Config.getInstance().setQBOToken(bearerTokenResponse.getAccessToken());
                        Config.getInstance().setQBORefreshToken(bearerTokenResponse.getRefreshToken());

                        //call company info again using new tokens
                        logger.info("createJournalEntry: calling using new tokens");
                        journalEntryReq.setHeader("Authorization","Bearer " + bearerTokenResponse.getAccessToken());
                        response = CLIENT.execute(journalEntryReq);
                    }

                    if (response.getStatusLine().getStatusCode() != 200){
                        logger.info("createJournalEntry: failed with code:" + response.getStatusLine().getStatusCode() + " message:" + response.getStatusLine().getReasonPhrase());
                        qboResult.setFailed("QBO Process Failed","Failed to create Journal Entry. Code:" + response.getStatusLine().getStatusCode());
                    }else{
                        StringBuffer result = httpHelper.getResult(response);
                        logger.info("createJournalEntry: raw result for createJournalEntry= " + result);
                        ObjectMapper objectMapper = new ObjectMapper();
                        JournalEntryResult journalEntryResult = null;
                        try {
                            journalEntryResult = objectMapper.readValue(result.toString(),JournalEntryResult.class);
                        } catch (JsonProcessingException e) {
                            logger.info("createJournalEntry: failed creating JournalEntryResult from result:" + result );
                            e.printStackTrace();
                        }
                        if(journalEntryResult==null){
                            logger.info("createJournalEntry: failed creating JournalEntryResult: null returned");
                            qboResult.setFailed("QBO Process Failed","Failed to create Journal Entry. Null returned");
                        }else{
                            logger.info("createJournalEntry: success:" + journalEntryResult);
                            qboResult.setResult(journalEntryResult.getJournalEntry().toString());
                            qboResult.setSuccess("QBO Journal Entry", "Journal Entry: " + journalEntryResult.getJournalEntry().getDocNumber() + " created and posted successfully.");
                        }
                    }

                } catch (Exception ex) {
                    logger.error("createJournalEntry: Exception while creating journal entry ", ex);
                    qboResult.setFailed("QBO Process Failed","Exception while creating journal entry:" + ex);
                } finally {
                    journalEntryReq.releaseConnection();
                }
            }
        }
        return qboResult;
    }

    public void createJournalEntryTest(){
        //TODO: move this to where we need to create Journal Entries for Restaurant and Drivers
        JournalEntry journalEntry = new JournalEntry();
        journalEntry.setDocNumber("VendorPay_2022-09-11");
        journalEntry.setPrivateNote("Memo field is here");
        journalEntry.setTxnDate("2022-09-10"); //format is YYYY-MM-DD
        journalEntry.addLine(200.0, JournalEntry.PostingType.Debit,"COGS Sales","description here 1");
        journalEntry.addLine(1.0, JournalEntry.PostingType.Debit,"Sales Tax Payable","description here 2");
        journalEntry.addLine(100.5, JournalEntry.PostingType.Credit,"Chequing","Vendor payout 4", Intuit.EntityType.Vendor,"A&W");
        journalEntry.addLine(100.5, JournalEntry.PostingType.Credit,"Wise Card Account","Vendor payout 3", Intuit.EntityType.Vendor,"Smiley's");

        QBOResult qboResult = new QBOResult();
        qboResult = journalEntry.save(new File(System.getProperty("user.dir"), journalEntry.getDocNumber() + ".csv"));
        logger.info("createJournalEntryTest: save results:" + qboResult.getMessage());

        qboResult = journalEntry.post();
        logger.info("createJournalEntryTest: post results:" + qboResult.getMessage());
    }

    public Boolean hasTokens(){
        String accessToken = Config.getInstance().getQBOToken();
        if(accessToken==null || accessToken.isEmpty()){
            logger.info("QBOController: no access Token - need to connect first");
            showQBOMessageDialog("QBO Process Failed","No QBO connection information found.  Please connect first from Utilities menu.");
            return false;
        }else{
            logger.info("QBOController: accessToken:" + accessToken);
            return true;
        }
    }

    public void connectToQBO(){
        //clear tokens prior as this is how we know success or failure
        clearTokens();
        String url = prepareUrl(oAuth2Configuration.getC2QBScope(), generateCSRFToken());
        logger.info("inside connectToQBO: url:" + url);
        //TODO:: perhaps open this connect process in another window/tab
        UI.getCurrent().getPage().executeJavaScript("window.location.replace(\"" + url + "\");");
    }

    private void clearTokens(){
        Config.getInstance().setQBOToken(null);
        Config.getInstance().setQBORefreshToken(null);
    }

    private String prepareUrl(String scope, String csrfToken)  {
        try {
            return oAuth2Configuration.getIntuitAuthorizationEndpoint()
                    + "?client_id=" + oAuth2Configuration.getAppClientId()
                    + "&response_type=code&scope=" + URLEncoder.encode(scope, "UTF-8")
                    + "&redirect_uri=" + URLEncoder.encode(oAuth2Configuration.getAppRedirectUri(), "UTF-8")
                    + "&state=" + csrfToken;
        } catch (UnsupportedEncodingException e) {
            logger.error("Exception while preparing url for redirect ", e);
        }
        return null;
    }

    // the state is used only for the single call to further ensure it is from our app
    private String generateCSRFToken()  {
        String csrfToken = UUID.randomUUID().toString();
        Config.getInstance().setQBOState(csrfToken);
        return csrfToken;
    }

    public void showQBOMessageDialog(String header, String message){
        showQBOMessageDialog(header,message,false);
    }
    public void showQBOMessageDialog(String header, String message, Boolean asHTML){
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader(header);
        if(asHTML){
            dialog.setText(new Html(message));
        }else{
            dialog.setText(message);
        }
        dialog.setConfirmText("OK");
        dialog.open();
    }

    public QBOResult executeQuery(String query){
        logger.info("QBOController: executeQuery:" + query);
        QBOResult qboResult = new QBOResult("QBO Query Result");
        if(hasTokens()){
            String realmId = Config.getInstance().getQBORealmId();
            if (StringUtils.isEmpty(realmId)) {
                logger.info("executeQuery: No QBO connection information found.  Please connect first from Utilities menu.");
                qboResult.setFailed("QBO Process Failed","No QBO connection information found.  Please connect first from Utilities menu.");
            }else{
                String minorVersion = Config.getInstance().getQBOMinorVersion();
                //encode the select statement
                String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
                String queryEndpoint = String.format("%s/v3/company/%s/query?query=%s&minorversion=%s", oAuth2Configuration.getAccountingAPIHost(), realmId, encodedQuery, minorVersion);

                HttpGet queryReq = new HttpGet(queryEndpoint);

                queryReq.setHeader("Accept", "application/json");
                String accessToken = Config.getInstance().getQBOToken();
                queryReq.setHeader("Authorization","Bearer " + accessToken);
                queryReq.setHeader("Content-Type","application/json");

                logger.info("executeQuery: prior to calling CLIENT.execute - outside of try - queryReq : " + queryReq.toString());

                try {

                    HttpResponse response = CLIENT.execute(queryReq);

                    logger.info("executeQuery: Response Code : "+ response.getStatusLine().getStatusCode());

                    /*
                     * Handle 401 status code -
                     * If a 401 response is received, refresh tokens should be used to get a new access token,
                     * and the API call should be tried again.
                     */
                    if (response.getStatusLine().getStatusCode() == 401) {
                        StringBuffer result = httpHelper.getResult(response);
                        logger.info("executeQuery: raw result for 401:" + result);

                        //refresh tokens
                        logger.info("executeQuery: received 401 during call, refreshing tokens now");
                        BearerTokenResponse bearerTokenResponse = refreshTokenService.refresh();
                        logger.info("executeQuery: after refresh tokens:" + bearerTokenResponse.toString());
                        if(bearerTokenResponse==null){
                            logger.info("executeQuery: failed refreshing token");
                            qboResult.setFailed("QBO Process Failed","Failed refreshing token");
                            return qboResult;
                        }
                        logger.info("executeQuery: before setQBOToken:" + bearerTokenResponse.getAccessToken());
                        Config.getInstance().setQBOToken(bearerTokenResponse.getAccessToken());
                        logger.info("executeQuery: before setQBORefreshToken:" + bearerTokenResponse.getRefreshToken());
                        Config.getInstance().setQBORefreshToken(bearerTokenResponse.getRefreshToken());
                        logger.info("executeQuery: after setQBORefreshToken");

                        //call company info again using new tokens
                        logger.info("executeQuery: calling using new tokens");
                        queryReq.setHeader("Authorization","Bearer " + bearerTokenResponse.getAccessToken());
                        response = CLIENT.execute(queryReq);
                    }

                    if (response.getStatusLine().getStatusCode() != 200){
                        logger.info("executeQuery: failed with code:" + response.getStatusLine().getStatusCode() + " message:" + response.getStatusLine().getReasonPhrase());
                        qboResult.setFailed("QBO Process Failed","Failed to perform query:" + query + " response:" + response.getStatusLine().getStatusCode());
                    }else{
                        StringBuffer result = httpHelper.getResult(response);
                        logger.info("executeQuery: raw result for executeQuery= " + result);
                        qboResult.setResult(result.toString());
                        qboResult.setSuccess("Query results returned:" + qboResult.getResult());
                    }

                } catch (Exception ex) {
                    logger.error("executeQuery: Exception while running query:" + query, ex);
                    qboResult.setFailed("QBO Process Failed","Exception while running query:" + query + " ex:" + ex);
                } finally {
                    queryReq.releaseConnection();
                }
            }
        }
        return qboResult;
    }

    public Map<String,NamedItem> getNamedItems(Intuit.EntityType entityType){
        Map<String, NamedItem> namedItems = new TreeMap<>();
        namedItems = Map.of();
        String query = "";
        if(entityType.equals(Intuit.EntityType.Vendor)){
            query = "select displayname,id from Vendor MAXRESULTS 1000";
        }else if(entityType.equals(Intuit.EntityType.Employee)){
            query = "select displayname,id from Employee MAXRESULTS 1000";
        }else if(entityType.equals(Intuit.EntityType.Account)){
            query = "select FullyQualifiedName,id from Account MAXRESULTS 1000";
        }else if(entityType.equals(Intuit.EntityType.Customer)){
            query = "select displayname,id from Customer MAXRESULTS 1000";
        }else if(entityType.equals(Intuit.EntityType.PaymentMethod)){
            query = "select name,id from PaymentMethod MAXRESULTS 1000";
        }else if(entityType.equals(Intuit.EntityType.Item)){
            query = "select FullyQualifiedName,id from Item MAXRESULTS 1000";
        }else{
            logger.info("getNamedItems: invalid EntityType passed:" + entityType.name());
            return namedItems;
        }
        QBOResult qboResult = executeQuery(query);
        if(!qboResult.getSuccess()){
            logger.info("getNamedItems: query failed: returned:" + qboResult);
            return namedItems;
        }
        String queryResultString = qboResult.getResult();
        if(queryResultString==null){
            logger.info("getNamedItems: query failed: null returned");
            return namedItems;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        QueryResult queryResult = null;
        try {
            queryResult = objectMapper.readValue(queryResultString,QueryResult.class);
        } catch (JsonProcessingException e) {
            logger.info("getNamedItems: failed mapping query result to QueryResult class:" + queryResultString );
            e.printStackTrace();
        }
        if(queryResult==null){
            logger.info("getNamedItems: failed mapping query to QueryResult class: null returned");
            //showQBOMessageDialog("QBO Process Failed","Failed to get query. Null returned: query:" + query);
        }else{
            logger.info("getNamedItems: success:" + queryResult);
            namedItems = queryResult.getQueryResponse().getNamedItemMap();
            /*
            String resultItems = "<p>" + namedItemType.name() + ":<br>";
            for (NamedItem namedItem: namedItems.values()) {
                resultItems+= namedItem.getDisplayName() + "(" + namedItem.getId() + ")<br>";
            }
            resultItems+= "</p>";
            showQBOMessageDialog("QBO Query", "Retrieved list of: " + resultItems, true );
             */
        }
        return namedItems;
    }

    public QBOResult getSalesReceipt(String salesReceiptNumber){
        logger.info("QBOController: getSalesReceipt");
        QBOResult qboResult = new QBOResult("QBO Process Result");
        if(hasTokens()){
            String realmId = Config.getInstance().getQBORealmId();
            if (StringUtils.isEmpty(realmId)) {
                qboResult.setFailed("QBO Process Failed","No QBO connection information found.  Please connect first from Utilities menu.");
            }else{
                String minorVersion = Config.getInstance().getQBOMinorVersion();
                String salesReceiptEndpoint = String.format("%s/v3/company/%s/salesreceipt/%s?minorversion=%s", oAuth2Configuration.getAccountingAPIHost(), realmId, salesReceiptNumber, minorVersion);
                logger.info("getSalesReceipt: salesReceiptEndpoint:" + salesReceiptEndpoint);
                HttpGet salesReceiptReq = new HttpGet(salesReceiptEndpoint);

                salesReceiptReq.setHeader("Accept", "application/json");
                String accessToken = Config.getInstance().getQBOToken();
                salesReceiptReq.setHeader("Authorization","Bearer " + accessToken);

                try {

                    HttpResponse response = CLIENT.execute(salesReceiptReq);

                    logger.info("Response Code : "+ response.getStatusLine().getStatusCode());

                    /*
                     * Handle 401 status code -
                     * If a 401 response is received, refresh tokens should be used to get a new access token,
                     * and the API call should be tried again.
                     */
                    if (response.getStatusLine().getStatusCode() == 401) {
                        StringBuffer result = httpHelper.getResult(response);
                        logger.info("raw result for 401 salesReceipt= " + result);

                        //refresh tokens
                        logger.info("received 401 during salesReceipt call, refreshing tokens now");
                        BearerTokenResponse bearerTokenResponse = refreshTokenService.refresh();
                        if(bearerTokenResponse==null){
                            logger.info("executeQuery: failed refreshing token");
                            qboResult.setFailed("QBO Process Failed","Failed refreshing token");
                            return qboResult;
                        }
                        Config.getInstance().setQBOToken(bearerTokenResponse.getAccessToken());
                        Config.getInstance().setQBORefreshToken(bearerTokenResponse.getRefreshToken());

                        //call salesReceipt again using new tokens
                        logger.info("calling salesReceipt using new tokens");
                        salesReceiptReq.setHeader("Authorization","Bearer " + bearerTokenResponse.getAccessToken());
                        response = CLIENT.execute(salesReceiptReq);
                    }

                    if (response.getStatusLine().getStatusCode() != 200){
                        logger.info("failed getting salesReceipt. Code:" + response.getStatusLine().getStatusCode());
                        StringBuffer result = httpHelper.getResult(response);
                        logger.info("raw result for salesReceipt fail:" + result);
                        qboResult.setFailed("QBO Process Failed","Failed to get salesReceipt. Code:" + response.getStatusLine().getStatusCode());
                    }else{
                        StringBuffer result = httpHelper.getResult(response);
                        logger.info("raw result for salesReceipt= " + result);


                        ObjectMapper objectMapper = new ObjectMapper();
                        SalesReceiptResponse salesReceiptResponse = null;
                        try {
                            salesReceiptResponse = objectMapper.readValue(result.toString(),SalesReceiptResponse.class);
                        } catch (JsonProcessingException e) {
                            logger.info("getSalesReceipt: failed creating SalesReceiptResponse from result:" + result );
                            e.printStackTrace();
                        }
                        if(salesReceiptResponse==null){
                            logger.info("getSalesReceipt: failed creating SalesReceiptResponse: null returned");
                            qboResult.setFailed("QBO Process Failed","Failed to get Sales Receipt. Null returned");
                        }else{
                            logger.info("getSalesReceipt: success:" + salesReceiptResponse);
                            qboResult.setResult(salesReceiptResponse.getSalesReceipt().toString());
                            qboResult.setSuccess("QBO Journal Entry", "Sales Receipt: " + salesReceiptResponse.getSalesReceipt().getDocNumber() + " retrieved successfully.");
                        }
                    }

                } catch (Exception ex) {
                    logger.error("Exception while getting salesReceipt ", ex);
                    qboResult.setFailed("QBO Process Failed","Exception while getting salesReceipt:" + ex);
                } finally {
                    salesReceiptReq.releaseConnection();
                }
            }
        }
        return qboResult;
    }

    public QBOResult createSalesReceipt(String jsonString) {
        logger.info("QBOController: createSalesReceipt");
        QBOResult qboResult = new QBOResult("QBO Process Result");
        //TODO: create sales receipt
        logger.info("createSalesReceipt: jsonString:" + jsonString);

        if(hasTokens()){
            String realmId = Config.getInstance().getQBORealmId();
            if (StringUtils.isEmpty(realmId)) {
                qboResult.setFailed("QBO Process Failed","No QBO connection information found.  Please connect first from Utilities menu.");
            }else{
                String minorVersion = Config.getInstance().getQBOMinorVersion();
                String salesReceiptEndpoint = String.format("%s/v3/company/%s/salesreceipt?minorversion=%s", oAuth2Configuration.getAccountingAPIHost(), realmId, minorVersion);

                HttpPost salesReceiptReq = new HttpPost(salesReceiptEndpoint);

                salesReceiptReq.setHeader("Accept", "application/json");
                String accessToken = Config.getInstance().getQBOToken();
                salesReceiptReq.setHeader("Authorization","Bearer " + accessToken);
                salesReceiptReq.setHeader("Content-Type","application/json");

                StringEntity params = null;
                try {
                    //TODO:: need to generate a JE JSON - do I need to urlEncode this too ??
                    logger.info("createSalesReceipt: setting params:" + jsonString);
                    params = new StringEntity(jsonString);
                    logger.info("createSalesReceipt: params:"+ params);
                } catch (UnsupportedEncodingException e) {
                    logger.info("createSalesReceipt: setting params FAILED");
                    throw new RuntimeException(e);
                }
                logger.info("createSalesReceipt: setting entity");
                salesReceiptReq.setEntity(params);

                try {

                    logger.info("createSalesReceipt: calling HttpResponse with:" + salesReceiptReq);
                    HttpResponse response = CLIENT.execute(salesReceiptReq);

                    logger.info("createSalesReceipt: Response Code : "+ response.getStatusLine().getStatusCode());

                    /*
                     * Handle 401 status code -
                     * If a 401 response is received, refresh tokens should be used to get a new access token,
                     * and the API call should be tried again.
                     */
                    if (response.getStatusLine().getStatusCode() == 401) {
                        StringBuffer result = httpHelper.getResult(response);
                        logger.info("createSalesReceipt: raw result for 401:" + result);

                        //refresh tokens
                        logger.info("createSalesReceipt: received 401 during call, refreshing tokens now");
                        BearerTokenResponse bearerTokenResponse = refreshTokenService.refresh();
                        if(bearerTokenResponse==null){
                            logger.info("executeQuery: failed refreshing token");
                            qboResult.setFailed("QBO Process Failed","Failed refreshing token");
                            return qboResult;
                        }
                        Config.getInstance().setQBOToken(bearerTokenResponse.getAccessToken());
                        Config.getInstance().setQBORefreshToken(bearerTokenResponse.getRefreshToken());

                        //call company info again using new tokens
                        logger.info("createSalesReceipt: calling using new tokens");
                        salesReceiptReq.setHeader("Authorization","Bearer " + bearerTokenResponse.getAccessToken());
                        response = CLIENT.execute(salesReceiptReq);
                    }

                    if (response.getStatusLine().getStatusCode() != 200){
                        logger.info("createSalesReceipt: failed with code:" + response.getStatusLine().getStatusCode() + " message:" + response.getStatusLine().getReasonPhrase());
                        qboResult.setFailed("QBO Process Failed","Failed to create Sales Receipt. Code:" + response.getStatusLine().getStatusCode());
                    }else{
                        StringBuffer result = httpHelper.getResult(response);
                        logger.info("createSalesReceipt: raw result for createSalesReceipt= " + result);
                        ObjectMapper objectMapper = new ObjectMapper();
                        SalesReceiptResponse salesReceiptResponse = null;
                        try {
                            salesReceiptResponse = objectMapper.readValue(result.toString(),SalesReceiptResponse.class);
                        } catch (JsonProcessingException e) {
                            logger.info("createSalesReceipt: failed creating SalesReceiptResponse from result:" + result );
                            e.printStackTrace();
                        }
                        if(salesReceiptResponse==null){
                            logger.info("createSalesReceipt: failed creating SalesReceiptResponse: null returned");
                            qboResult.setFailed("QBO Process Failed","Failed to create Sales Receipt. Null returned");
                        }else{
                            logger.info("createSalesReceipt: success:" + salesReceiptResponse);
                            qboResult.setResult(salesReceiptResponse.getSalesReceipt().toString());
                            qboResult.setSuccess("QBO Sales Receipt", "Sales Receipt: " + salesReceiptResponse.getSalesReceipt().getDocNumber() + " created and posted successfully.");
                        }
                    }

                } catch (Exception ex) {
                    logger.error("createSalesReceipt: Exception while creating sales receipt ", ex);
                    qboResult.setFailed("QBO Process Failed","Exception while creating sales receipt:" + ex);
                } finally {
                    salesReceiptReq.releaseConnection();
                }
            }
        }
        return qboResult;
    }
}
