package org.fenixedu.academic.service.services.person;

import java.nio.charset.StandardCharsets;

import org.apache.commons.httpclient.HttpStatus;
import org.fenixedu.academic.FenixEduAcademicConfiguration;

import com.google.common.io.BaseEncoding;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

public class ManageUser {
   
    public static boolean isPasswordDefined(String username) {
        final HttpResponse<String> response = Unirest.get(FenixEduAcademicConfiguration.getConfiguration().getWebServicesManageUserUrl() + username)
                .header("Authorization", getServiceAuth())
                .asString();
        
        if (response.getStatus() == 200) {
            JsonObject body = new JsonParser().parse(response.getBody()).getAsJsonObject();
            return body.get("isPasswordSet").getAsBoolean();
        } else {
            return false;
        }
    }
    
    public static boolean isUserExported(String username) {
        final HttpResponse<String> response = Unirest.get(FenixEduAcademicConfiguration.getConfiguration().getWebServicesManageUserUrl() + username)
                .header("Authorization", getServiceAuth())
                .asString();
        
        // NOT_FOUND
        if (response.getStatus() == 404) {           
            return false;
        } else {
            return true;
        }
    }
    
    public static boolean exportUser(String username) {
        final HttpResponse<String> response = Unirest.post(FenixEduAcademicConfiguration.getConfiguration().getWebServicesManageUserUrl() + username)
                .header("Authorization", getServiceAuth())
                .asString();
        
        // SC_CONFLICT -> user already exists
        if (response.getStatus() == HttpStatus.SC_CONFLICT || response.getStatus() == HttpStatus.SC_CREATED) {           
            return true;
        } else {
            return false;
        }
    }
    
    private static String getServiceAuth() {
        String userpass = FenixEduAcademicConfiguration.getConfiguration().getWebServicesManageUserUsername() + ":" 
                            + FenixEduAcademicConfiguration.getConfiguration().getWebServicesManageUserPassword();
        String encoding = new String(BaseEncoding.base64().encode(userpass.getBytes(StandardCharsets.UTF_8)));
        return "Basic " + encoding;
    }
}
