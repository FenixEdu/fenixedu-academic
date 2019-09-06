/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.externalServices;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import com.twilio.Twilio;
import com.twilio.exception.AuthenticationException;
import com.twilio.exception.TwilioException;
import com.twilio.http.HttpMethod;
import com.twilio.http.Request;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.FenixEduAcademicConfiguration;
import org.fenixedu.bennu.core.api.SystemResource;
import org.fenixedu.bennu.core.rest.Healthcheck;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.stream.Stream;

public class PhoneValidationUtils {
    private static final Logger logger = LoggerFactory.getLogger(PhoneValidationUtils.class);

    private TwilioRestClient TWILIO_CLIENT;
    private String CIIST_SMS_GATEWAY_URL;
    private HttpClient CIIST_CLIENT;

    private static PhoneValidationUtils instance;
    private static final String TWILIO_SID = FenixEduAcademicConfiguration.getConfiguration().getTwilioSid();
    private static final String TWILIO_STOKEN = FenixEduAcademicConfiguration.getConfiguration().getTwilioStoken();
    private static final String TWILIO_DEFAULT_MESSAGING_SERVICE_SID = FenixEduAcademicConfiguration.getConfiguration().getTwilioDefaultMessagingServiceSid();
    private static final String TWILIO_FROM_NUMBER = FenixEduAcademicConfiguration.getConfiguration().getTwilioFromNumber();

    public static PhoneValidationUtils getInstance() {
        if (instance == null) {
            instance = new PhoneValidationUtils();
        }
        return instance;
    }

    public boolean canRun() {
        return TWILIO_CLIENT != null && CIIST_CLIENT != null && !CoreConfiguration.getConfiguration().developmentMode();
    }

    public boolean shouldRun() {
        Boolean ciistsmsShouldRun = FenixEduAcademicConfiguration.getConfiguration().getCIISTSMSShouldRun();
        //defaults as true to keep old behaviour
        return ciistsmsShouldRun != null ? ciistsmsShouldRun : true;
    }

    private void initCIISTSMSGateway() {
        final String CIIST_SMS_USERNAME = FenixEduAcademicConfiguration.getConfiguration().getCIISTSMSUsername();
        final String CIIST_SMS_PASSWORD = FenixEduAcademicConfiguration.getConfiguration().getCIISTSMSPassword();
        CIIST_SMS_GATEWAY_URL = FenixEduAcademicConfiguration.getConfiguration().getCIISTSMSGatewayUrl();
        if (!StringUtils.isEmpty(CIIST_SMS_USERNAME) && !StringUtils.isEmpty(CIIST_SMS_PASSWORD)) {
            CIIST_CLIENT = new HttpClient();
            Credentials credentials = new UsernamePasswordCredentials(CIIST_SMS_USERNAME, CIIST_SMS_PASSWORD);
            CIIST_CLIENT.getState().setCredentials(AuthScope.ANY, credentials);
        }
    }

    private void initTwilio() {
        try {
            Twilio.init(TWILIO_SID, TWILIO_STOKEN);
            TWILIO_CLIENT = Twilio.getRestClient();
        }
        catch (AuthenticationException e) {
            logger.error("Failed authenticate on Twilio initialization: " + e.getMessage(), e);
            return;
        }

        if (Stream.of(TWILIO_SID, TWILIO_STOKEN, TWILIO_FROM_NUMBER).noneMatch(StringUtils::isEmpty)) {
            final String TWILIO_ACCOUNT_URL = String.format("/2010-04-01/Accounts/%s.json", TWILIO_SID);

            SystemResource.registerHealthcheck(new Healthcheck() {
                private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

                @Override public String getName() {
                    return "Twilio";
                }

                @Override protected Result check() throws Exception {
                    JsonElement json = new JsonParser()
                            .parse(TWILIO_CLIENT.request(new Request(HttpMethod.GET, TWILIO_ACCOUNT_URL)).getContent());
                    return Result.healthy(gson.toJson(json));
                }
            });
        }
    }

    private PhoneValidationUtils() {
        initTwilio();
        initCIISTSMSGateway();
        if (canRun()) {
            logger.info("Twilio Initialized:\n\tfrom number {} \n\thost: {} \n", TWILIO_FROM_NUMBER, CoreConfiguration
                    .getConfiguration().applicationUrl());
            logger.info("DSI SMS Gateway Initialized: {}\n", CIIST_SMS_GATEWAY_URL);
        } else {
            logger.debug("Twilio/DSI SMS Gateway not initialized");
        }
    }

    public boolean makeCall(String phoneNumber, String code, String lang) {
        if (canRun()) {
            try {
                Call.creator(new PhoneNumber(phoneNumber), new PhoneNumber(TWILIO_FROM_NUMBER),
                        new URI(CoreConfiguration.getConfiguration().applicationUrl()
                                + "/external/partyContactValidation.do?method=validatePhone&code=" + code + "&lang=" + lang))
                        .create(TWILIO_CLIENT);
                return true;
            }
            catch (URISyntaxException | TwilioException e) {
                logger.error("Error makeCall: " + e.getMessage(), e);
                return false;
            }
        }
        else {
            logger.info("Call to >" + phoneNumber + "<: Bem-vindo ao sistema Fénix. Introduza o código " + code + " . Obrigado!");
            return true;
        }
    }

    /**
     *
     * Send an SMS using the Twilio API via a specific Messaging Service
     *
     * @param toNumber The phone number of the recipient
     * @param from The phone number or custom name of the sender
     * @param messagingServiceSid The Messaging Service SID
     * @param message The body of the message
     *
     * @return the SID of the created message or null if no message was sent
     *
     */
    public String sendTwilioSMS(String toNumber, String fromName, String messagingServiceSid, String message) {
        if (canRun()) {
            return Message.creator(new PhoneNumber(toNumber), new PhoneNumber(fromName), message)
                    .setMessagingServiceSid(messagingServiceSid)
                    .create(TWILIO_CLIENT).getSid();
        }
        return null;
    }

    /**
     *
     * Send an SMS using the TWilio API
     *
     * @param toNumber The phone number of the recipient
     * @param from The phone number or custom name of the sender
     * @param message The body of the message
     *
     * @return the SID of the created message or null if no message was sent
     *
     */
    public String sendTwilioSMS(String toNumber, String from, String message) {
        return sendTwilioSMS(toNumber, from, TWILIO_DEFAULT_MESSAGING_SERVICE_SID, message);
    }

    public boolean sendSMSMessage(String number, String message) {
        number = number.replace(" ", "");
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
                logger.error(e.getMessage(), e);
                return false;
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                return false;
            }
        } else {
            logger.info("SMS to >" + number + "<: " + message);
        }
        return true;
    }

    public boolean sendSMS(String number, String token) {
        final String message = "Bem-vindo ao sistema Fenix. Introduza o codigo " + token + " . Obrigado!";
        return sendSMSMessage(number, message);
    }
}
