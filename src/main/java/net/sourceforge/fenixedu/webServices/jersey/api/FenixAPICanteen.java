package net.sourceforge.fenixedu.webServices.jersey.api;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.sourceforge.fenixedu.util.FenixConfigurationManager;
import net.sourceforge.fenixedu.util.FenixConfigurationManager.ConfigurationProperties;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import com.google.common.io.BaseEncoding;
import com.google.gson.JsonObject;

public class FenixAPICanteen {

    private static final Client HTTP_CLIENT = ClientBuilder.newClient();

    private static String canteenInfo;
    private static DateTime day;

    public static String get() {

        if (StringUtils.isEmpty(canteenInfo) || oldInformation()) {
            Response response =
                    HTTP_CLIENT.target(FenixConfigurationManager.getConfiguration().getFenixApiCanteenUrl())
                            .request(MediaType.APPLICATION_JSON).header("Authorization", getServiceAuth()).get();

            if (response.getStatus() == 200) {
                canteenInfo = response.readEntity(String.class);
                day = new DateTime();

                return canteenInfo;
            } else {
                return new JsonObject().toString();
            }
        } else {
            return canteenInfo;
        }
    }

    private static Boolean oldInformation() {
        DateTime now = new DateTime();
        return now.isAfter(day.plusHours(24));
    }

    private static String getServiceAuth() {
        ConfigurationProperties config = FenixConfigurationManager.getConfiguration();
        String userpass = config.getFenixApiCanteenUser() + ":" + config.getFenixApiCanteenSecret();
        String encoding = new String(BaseEncoding.base64().encode(userpass.getBytes()));
        return "Basic " + encoding;
    }
}
