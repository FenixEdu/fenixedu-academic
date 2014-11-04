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
package org.fenixedu.academic.api.infra;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.fenixedu.academic.util.FenixConfigurationManager;
import org.fenixedu.academic.util.FenixConfigurationManager.ConfigurationProperties;
import org.fenixedu.commons.i18n.I18N;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.google.common.io.BaseEncoding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class FenixAPICanteen {

    private static final Client HTTP_CLIENT = ClientBuilder.newClient();

    private static JsonObject canteenInfo;
    private static DateTime day;
    public static final String datePattern = "dd/MM/yyyy";

    public static String get(String daySearch) {

        String locale = I18N.getLocale().toString().replace("_", "-");
        if (canteenInfo == null || canteenInfo.isJsonNull() || oldInformation()) {

            String canteenUrl = FenixConfigurationManager.getConfiguration().getFenixApiCanteenUrl();
            try {
                Response response =
                        HTTP_CLIENT.target(canteenUrl).request(MediaType.APPLICATION_JSON)
                                .header("Authorization", getServiceAuth()).get();

                if (response.getStatus() == 200) {
                    JsonParser parser = new JsonParser();
                    canteenInfo = (JsonObject) parser.parse(response.readEntity(String.class));
                    day = new DateTime();
                } else {
                    return new JsonObject().toString();
                }
            } catch (ProcessingException e) {
                e.printStackTrace();
                return new JsonObject().toString();
            }
        }

        JsonArray jsonArrayWithLang = canteenInfo.getAsJsonArray(locale);

        DateTime dayToCompareStart;
        DateTime dayToCompareEnd;

        DateTime dateTime = DateTime.parse(daySearch, DateTimeFormat.forPattern(datePattern));
        int dayOfWeek = dateTime.getDayOfWeek();
        if (dayOfWeek != 7) {
            dayToCompareStart = dateTime.minusDays(dayOfWeek);
            dayToCompareEnd = dateTime.plusDays(7 - dayOfWeek);
        } else {
            dayToCompareStart = dateTime;
            dayToCompareEnd = dateTime.plusDays(7);
        }

        JsonArray jsonResult = new JsonArray();
        for (JsonElement jObj : jsonArrayWithLang) {

            DateTime dateToCompare =
                    DateTime.parse(((JsonObject) jObj).get("day").getAsString(), DateTimeFormat.forPattern(datePattern));

            if (dateToCompare.isAfter(dayToCompareStart) && dateToCompare.isBefore(dayToCompareEnd)) {
                jsonResult.add(jObj);
            }
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        return gson.toJson(jsonResult);

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