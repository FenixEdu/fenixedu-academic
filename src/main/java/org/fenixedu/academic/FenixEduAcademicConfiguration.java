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
package org.fenixedu.academic;

import org.fenixedu.commons.configuration.ConfigurationInvocationHandler;
import org.fenixedu.commons.configuration.ConfigurationManager;
import org.fenixedu.commons.configuration.ConfigurationProperty;

public class FenixEduAcademicConfiguration {

    @ConfigurationManager(description = "FenixEdu Academic Configuration")
    public interface ConfigurationProperties {

        @ConfigurationProperty(key = "ciist.sms.gateway.url")
        public String getCIISTSMSGatewayUrl();

        @ConfigurationProperty(key = "ciist.sms.password")
        public String getCIISTSMSPassword();

        @ConfigurationProperty(key = "ciist.sms.shouldRun")
        public Boolean getCIISTSMSShouldRun();

        @ConfigurationProperty(key = "ciist.sms.username")
        public String getCIISTSMSUsername();

        @ConfigurationProperty(key = "domain.academic.enrolments.AllowStudentToChooseAffinityCycle", defaultValue = "true")
        public Boolean getEnrolmentsAllowStudentToChooseAffinityCycle();

        @ConfigurationProperty(key = "domain.academic.enrolments.AllowStudentToEnrolInAffinityCycle", defaultValue = "true")
        public Boolean getEnrolmentsAllowStudentToEnrolInAffinityCycle();

        @ConfigurationProperty(key = "domain.academic.enrolments.AllowStudentToCreateRegistrationForAffinityCycle",
                defaultValue = "true")
        public Boolean getEnrolmentsAllowStudentToCreateRegistrationForAffinityCycle();

        @ConfigurationProperty(key = "domain.academic.enrolments.SpecialSeasonEvaluationsInduceEnrolmentVariables",
                defaultValue = "true")
        public Boolean getEnrolmentsInSpecialSeasonEvaluationsInduceEnrolmentVariables();

        @ConfigurationProperty(key = "default.social.security.number",
                description = "Identifies the default social security number to be used in the country (e.g. for Portugal it is 999999990)")
        public String getDefaultSocialSecurityNumber();

        @ConfigurationProperty(key = "mailSender.max.recipients", defaultValue = "50")
        public String getMailSenderMaxRecipients();

        @ConfigurationProperty(key = "mail.smtp.host", description = "The host of the SMTP server used to send Emails")
        public String getMailSmtpHost();

        @ConfigurationProperty(key = "mail.smtp.name", description = "The name of the SMTP server used to send Emails")
        public String getMailSmtpName();

        @ConfigurationProperty(key = "physicalAddress.requiresValidation")
        public Boolean getPhysicalAddressRequiresValidation();

        @ConfigurationProperty(key = "twilio.from.number")
        public String getTwilioFromNumber();

        @ConfigurationProperty(key = "twilio.sid")
        public String getTwilioSid();

        @ConfigurationProperty(key = "twilio.stoken")
        public String getTwilioStoken();

        @ConfigurationProperty(key = "maximum.number.of.credits.for.enrolment", defaultValue = "40.5")
        public double getMaximumNumberOfCreditsForEnrolment();
    }

    public static ConfigurationProperties getConfiguration() {
        return ConfigurationInvocationHandler.getConfiguration(ConfigurationProperties.class);
    }

    public static boolean getPhysicalAddressRequiresValidation() {
        Boolean physicalAddressRequiresValidation = getConfiguration().getPhysicalAddressRequiresValidation();
        //keep old behaviour if property is not configured
        return physicalAddressRequiresValidation != null ? physicalAddressRequiresValidation : true;
    }

}
