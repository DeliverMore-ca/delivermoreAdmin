package ca.admin.delivermore.data.service;

import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.WebBrowser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientSettings {
    private Logger log = LoggerFactory.getLogger(ClientSettings.class);
    private String clientID = null;

    public ClientSettings() {
        createClientID();
    }

    /* Create a repeatable clientID to store settings with so they can also be retrieved
     *
     */
    //TODO: add copy function to copy all properties from one clientID to another
    private void createClientID(){
        WebBrowser browser = VaadinSession.getCurrent().getBrowser();
        //build the clientID using
        String OS = null;
        if(browser.isAndroid()) OS = "Android";
        else if(browser.isChromeOS()) OS = "ChromeOS";
        else if(browser.isMacOSX()) OS = "MacOSX";
        else if(browser.isWindows()) OS = "Windows";
        else if(browser.isWindowsPhone()) OS = "WinPhone";
        else if(browser.isLinux()) OS = "Linux";
        else if(browser.isIPhone()) OS = "IOS";
        else OS = "OS";

        String extra = null;
        if(browser.isChrome()) extra = "Chrome";
        else if(browser.isEdge()) extra = "Edge";
        else if(browser.isFirefox()) extra = "Firefox";
        else if(browser.isIE()) extra = "IE";
        else if(browser.isOpera()) extra = "Opera";
        else if(browser.isSafari()) extra = "Safari";
        else extra = "Other";

        String IP = browser.getAddress();
        if(IP.equals("[0:0:0:0:0:0:0:1]") || IP.equals("0:0:0:0:0:0:0:1") || IP.equals("127.0.0.1")) IP = "local";
        IP = IP.replace(".", "_");

        clientID = OS + "_" + extra + "_" + IP;
        log.info("createClientID: clientID:" + clientID);

    }

    public String getClientID() {
        log.info("getClientID: clientID:" + clientID);
        return clientID;
    }
    public String getCurrentClientID() {
        createClientID();
        log.info("getClientID: clientID:" + clientID);
        return clientID;
    }

}
