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
import com.twilio.type.PhoneNumber;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.FenixEduAcademicConfiguration;
import org.fenixedu.bennu.core.api.SystemResource;
import org.fenixedu.bennu.core.rest.Healthcheck;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.stream.Stream;

public class PhoneValidationUtils {
    private static final Logger logger = LoggerFactory.getLogger(PhoneValidationUtils.class);

    private TwilioRestClient TWILIO_CLIENT;

    private static PhoneValidationUtils instance;
    private static final String TWILIO_SID = FenixEduAcademicConfiguration.getConfiguration().getTwilioSid();
    private static final String TWILIO_STOKEN = FenixEduAcademicConfiguration.getConfiguration().getTwilioStoken();
    private static final String TWILIO_FROM_NUMBER = FenixEduAcademicConfiguration.getConfiguration().getTwilioFromNumber();

    public static PhoneValidationUtils getInstance() {
        if (instance == null) {
            instance = new PhoneValidationUtils();
        }
        return instance;
    }

    private boolean canRun() {
        return TWILIO_CLIENT != null && !CoreConfiguration.getConfiguration().developmentMode();
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
        if (canRun()) {
            logger.info("Twilio Initialized:\n\tfrom number {} \n\thost: {} \n", TWILIO_FROM_NUMBER, CoreConfiguration
                    .getConfiguration().applicationUrl());
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

}
