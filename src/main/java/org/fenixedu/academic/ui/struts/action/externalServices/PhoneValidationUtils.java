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
package net.sourceforge.fenixedu.presentationTier.Action.externalServices;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.util.FenixConfigurationManager;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.rest.Healthcheck;
import org.fenixedu.bennu.core.rest.SystemResource;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.CallFactory;
import com.twilio.sdk.resource.instance.Account;

public class PhoneValidationUtils {
    private static final Logger logger = LoggerFactory.getLogger(PhoneValidationUtils.class);

    public final String HOST = CoreConfiguration.getConfiguration().applicationUrl();

    private String TWILIO_FROM_NUMBER;
    private TwilioRestClient TWILIO_CLIENT;
    private String CIIST_SMS_GATEWAY_URL;
    private HttpClient CIIST_CLIENT;

    private static PhoneValidationUtils instance;

    public static PhoneValidationUtils getInstance() {
        if (instance == null) {
            instance = new PhoneValidationUtils();
        }
        return instance;
    }

    public boolean canRun() {
        return TWILIO_CLIENT != null && CIIST_CLIENT != null && !CoreConfiguration.getConfiguration().developmentMode();
    }

    private void initCIISTSMSGateway() {
        final String CIIST_SMS_USERNAME = FenixConfigurationManager.getConfiguration().getCIISTSMSUsername();
        final String CIIST_SMS_PASSWORD = FenixConfigurationManager.getConfiguration().getCIISTSMSPassword();
        CIIST_SMS_GATEWAY_URL = FenixConfigurationManager.getConfiguration().getCIISTSMSGatewayUrl();
        if (!StringUtils.isEmpty(CIIST_SMS_USERNAME) && !StringUtils.isEmpty(CIIST_SMS_PASSWORD)) {
            CIIST_CLIENT = new HttpClient();
            Credentials credentials = new UsernamePasswordCredentials(CIIST_SMS_USERNAME, CIIST_SMS_PASSWORD);
            CIIST_CLIENT.getState().setCredentials(AuthScope.ANY, credentials);
        }
    }

    private void initTwilio() {
        final String TWILIO_SID = FenixConfigurationManager.getConfiguration().getTwilioSid();
        final String TWILIO_STOKEN = FenixConfigurationManager.getConfiguration().getTwilioStoken();
        final String URL = "/" + TwilioRestClient.DEFAULT_VERSION + "/Accounts/" + TWILIO_SID + ".json";
        TWILIO_FROM_NUMBER = FenixConfigurationManager.getConfiguration().getTwilioFromNumber();
        if (!StringUtils.isEmpty(TWILIO_SID) && !StringUtils.isEmpty(TWILIO_STOKEN) && !StringUtils.isEmpty(TWILIO_FROM_NUMBER)) {
            TWILIO_CLIENT = new TwilioRestClient(TWILIO_SID, TWILIO_STOKEN);
            SystemResource.registerHealthcheck(new Healthcheck() {
                private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

                @Override
                public String getName() {
                    return "Twilio";
                }

                @Override
                protected Result check() throws Exception {
                    JsonElement json = new JsonParser().parse(TWILIO_CLIENT.get(URL).getResponseText());
                    return Result.healthy(gson.toJson(json));
                }
            });
        }
    }

    private PhoneValidationUtils() {
        initTwilio();
        initCIISTSMSGateway();
        if (canRun()) {
            logger.info("Twilio Initialized:\n\tfrom number {} \n\thost: {} \n", TWILIO_FROM_NUMBER, HOST);
            logger.info("DSI SMS Gateway Initialized: {}\n", CIIST_SMS_GATEWAY_URL);
        } else {
            logger.info("Twilio/DSI SMS Gateway not initialized");
        }
    }

    public boolean makeCall(String phoneNumber, String code, String lang) {
        if (canRun()) {
            final Account account = TWILIO_CLIENT.getAccount(); // Make a call
            CallFactory callFactory = account.getCallFactory();
            Map<String, String> callParams = new HashMap<String, String>();
            callParams.put("To", phoneNumber);
            callParams.put("From", TWILIO_FROM_NUMBER);
            callParams.put("Url", HOST + "/external/partyContactValidation.do?method=validatePhone&code=" + code + "&lang="
                    + lang);
            try {
                callFactory.create(callParams);
                return true;
            } catch (TwilioRestException e) {
                logger.error("Error makeCall: " + e.getMessage(), e);
                return false;
            }
        } else {
            logger.info("Call to >" + phoneNumber + "<: Bem-vindo ao sistema Fénix. Introduza o código " + code + " . Obrigado!");
            return true;
        }
    }

    public boolean sendSMS(String number, String token) {
        number = number.replace(" ", "");
        final String message = "Bem-vindo ao sistema Fenix. Introduza o codigo " + token + " . Obrigado!";
        if (canRun()) {
            PostMethod method = new PostMethod(CIIST_SMS_GATEWAY_URL);
            method.addParameter(new NameValuePair("number", number));
            method.addParameter(new NameValuePair("msg", message));
            try {
                CIIST_CLIENT.executeMethod(method);
                if (method.getStatusCode() != 200) {
                    return false;
                }
            } catch (HttpException e) {
                // TODO Auto-generated catch block
                logger.error(e.getMessage(), e);
                return false;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                logger.error(e.getMessage(), e);
                return false;
            }
        } else {
            logger.info("SMS to >" + number + "<: " + message);
        }
        return true;
    }
}
