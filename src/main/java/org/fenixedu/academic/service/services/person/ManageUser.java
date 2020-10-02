package org.fenixedu.academic.service.services.person;

import java.nio.charset.StandardCharsets;

import org.apache.commons.httpclient.HttpStatus;
import org.fenixedu.academic.FenixEduAcademicConfiguration;

import com.google.common.io.BaseEncoding;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManageUser {

    private static final Logger LOGGER = LoggerFactory.getLogger(ManageUser.class);

    public static boolean isPasswordDefined(String username) {
        final HttpResponse<String> response = Unirest.get(FenixEduAcademicConfiguration.getConfiguration().getWebServicesManageUserUrl() + username)
                .header("Authorization", getServiceAuth())
                .asString();
        LOGGER.info("Checking if password is defined for user -> " + username + " -> with response status -> " + response.getStatus());
        if (response.getStatus() == 200) {
            try {
                final JsonObject body = new JsonParser().parse(response.getBody()).getAsJsonObject();
                final boolean isPasswordSet = body.get("isPasswordSet").getAsBoolean();
                LOGGER.info("isPasswordSet -> " + isPasswordSet + " -> for user " + username);
                return isPasswordSet;
            } catch (final IllegalStateException ex) {
                LOGGER.error("bad response checking if isPasswordSet");
                return false;
            }
        }
        return false;
    }

    public static boolean isUserExported(String username) {
        final HttpResponse<String> response = Unirest.get(FenixEduAcademicConfiguration.getConfiguration().getWebServicesManageUserUrl() + username)
                .header("Authorization", getServiceAuth())
                .asString();

        LOGGER.info("Checking if user '" + username + "' is exported -> with response status: " + response.getStatus());

        // NOT_FOUND
        return response.getStatus() != 404;
    }

    public static boolean exportUser(String username) {
        final HttpResponse<String> response = Unirest.post(FenixEduAcademicConfiguration.getConfiguration().getWebServicesManageUserUrl() + username)
                .header("Authorization", getServiceAuth())
                .asString();

        LOGGER.info("Exporting user " + username + " ...");
        LOGGER.info("Export response for user " + username + " is -> " + response.getStatus());

        // SC_CONFLICT -> user already exists
        return response.getStatus() == HttpStatus.SC_CONFLICT || response.getStatus() == HttpStatus.SC_CREATED;
    }

    private static String getServiceAuth() {
        final String userpass = FenixEduAcademicConfiguration.getConfiguration().getWebServicesManageUserUsername() + ":"
                            + FenixEduAcademicConfiguration.getConfiguration().getWebServicesManageUserPassword();
        final String encoding = new String(BaseEncoding.base64().encode(userpass.getBytes(StandardCharsets.UTF_8)));
        return "Basic " + encoding;
    }
}
