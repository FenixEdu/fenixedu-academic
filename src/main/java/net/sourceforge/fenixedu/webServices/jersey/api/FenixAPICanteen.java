/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.webServices.jersey.api;

import javax.ws.rs.ProcessingException;
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
            String canteenUrl = FenixConfigurationManager.getConfiguration().getFenixApiCanteenUrl();
            try {
                Response response =
                        HTTP_CLIENT.target(canteenUrl).request(MediaType.APPLICATION_JSON)
                                .header("Authorization", getServiceAuth()).get();

                if (response.getStatus() == 200) {
                    canteenInfo = response.readEntity(String.class);
                    day = new DateTime();
                    return canteenInfo;
                } else {
                    return new JsonObject().toString();
                }
            } catch (ProcessingException e) {
                return new JsonObject().toString();
            }
        }

        return canteenInfo;
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
