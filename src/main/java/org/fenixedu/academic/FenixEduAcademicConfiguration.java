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

import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.commons.configuration.ConfigurationInvocationHandler;
import org.fenixedu.commons.configuration.ConfigurationManager;
import org.fenixedu.commons.configuration.ConfigurationProperty;

public class FenixEduAcademicConfiguration {

    private static boolean useGlobalInterestRateTableForEventPenalties;

    static {
        setUseGlobalInterestRateTableForEventPenalties(getConfiguration().useGlobalInterestRateTableForEventPenalties());
    }
    
    @ConfigurationManager(description = "FenixEdu Academic Configuration")
    public interface ConfigurationProperties {

        @ConfigurationProperty(key = "ciist.sms.gateway.url")
        public String getCIISTSMSGatewayUrl();

        @ConfigurationProperty(key = "ciist.sms.password")
        public String getCIISTSMSPassword();

        @ConfigurationProperty(key = "ciist.sms.username")
        public String getCIISTSMSUsername();

        @ConfigurationProperty(key = "ciist.sms.shouldRun")
        public Boolean getCIISTSMSShouldRun();

        @ConfigurationProperty(key = "generic.application.email.confirmation.link",
                defaultValue = "http://localhost:8080/fenix/publico/genericApplications.do?method=confirmEmail&confirmationCode=")
        public String getGenericApplicationEmailConfirmationLink();

        @ConfigurationProperty(
                key = "generic.application.email.recommendation.link",
                defaultValue = "http://localhost:8080/fenix/publico/genericApplications.do?method=uploadRecommendation&confirmationCode=")
        public String getGenericApplicationEmailRecommendationLink();

        @ConfigurationProperty(key = "mailSender.max.recipients", defaultValue = "50")
        public String getMailSenderMaxRecipients();

        @ConfigurationProperty(key = "mail.smtp.host", description = "The host of the SMTP server used to send Emails")
        public String getMailSmtpHost();

        @ConfigurationProperty(key = "mail.smtp.name", description = "The name of the SMTP server used to send Emails")
        public String getMailSmtpName();

        @ConfigurationProperty(key = "phd.public.candidacy.submission.link")
        public String getPhdPublicCandidacySubmissionLink();

        @ConfigurationProperty(key = "physicalAddress.requiresValidation", defaultValue = "true")
        public boolean getPhysicalAddressRequiresValidation();

        @ConfigurationProperty(key = "raides.request.info", defaultValue = "false")
        public Boolean getRaidesRequestInfo();

        @ConfigurationProperty(key = "receipt.min.year.to.create", defaultValue = "2006")
        public Integer getReceiptMinYearToCreate();

        @ConfigurationProperty(key = "receipt.numberSeries.for.years", defaultValue = "")
        public String getReceiptNumberSeriesForYears();

        @ConfigurationProperty(key = "semester.for.from.enrolments", defaultValue = "1")
        public String getSemesterForFromEnrolments();

        @ConfigurationProperty(
                key = "semester.for.from.mark.sheet.managment",
                description = "indicates if pending request should be stored in database to recover current session after new login",
                defaultValue = "2")
        public String getSemesterForFromMarkSheetManagment();

        @ConfigurationProperty(key = "sibs.entityCode",
                description = "institution entity code to be used in atm machines with reference to perform payments",
                defaultValue = "1111")
        public String getSibsEntityCode();

        @ConfigurationProperty(key = "start.semester.for.bolonha.degrees", defaultValue = "1")
        public String getStartSemesterForBolonhaDegrees();

        @ConfigurationProperty(key = "start.semester.for.bolonha.transition", defaultValue = "1")
        public String getStartSemesterForBolonhaTransition();

        @ConfigurationProperty(key = "start.year.for.bolonha.degrees", defaultValue = "2006/2007")
        public String getStartYearForBolonhaDegrees();

        @ConfigurationProperty(key = "start.year.for.bolonha.transition", defaultValue = "2007/2008")
        public String getStartYearForBolonhaTransition();

        @ConfigurationProperty(key = "twilio.from.number")
        public String getTwilioFromNumber();

        @ConfigurationProperty(key = "twilio.sid")
        public String getTwilioSid();

        @ConfigurationProperty(key = "twilio.stoken")
        public String getTwilioStoken();

        @ConfigurationProperty(key = "twilio.default.messaging.service.sid")
        public String getTwilioDefaultMessagingServiceSid();

        @ConfigurationProperty(key = "webServices.internationalRegistration.username")
        public String getWebServicesInternationalRegistrationUsername();

        @ConfigurationProperty(key = "webServices.internationalRegistration.password")
        public String getWebServicesInternationalRegistrationPassword();

        @ConfigurationProperty(key = "webServices.internationalRegistration.url")
        public String getWebServicesInternationalRegistrationUrl();
        
        @ConfigurationProperty(key = "webServices.manageUser.username")
        public String getWebServicesManageUserUsername();

        @ConfigurationProperty(key = "webServices.manageUser.password")
        public String getWebServicesManageUserPassword();

        @ConfigurationProperty(key = "webServices.manageUser.url")
        public String getWebServicesManageUserUrl();

        @ConfigurationProperty(key = "year.for.from.enrolments", defaultValue = "2004/2005")
        public String getYearForFromEnrolments();

        @ConfigurationProperty(key = "year.for.from.mark.sheet.managment",
                description = "identifies the execution period after which mark sheet are to be managed in the fenix system.",
                defaultValue = "2005/2006")
        public String getYearForFromMarkSheetManagment();

        @ConfigurationProperty(key="maximum.number.of.credits.for.enrolment", defaultValue = "40.5")
        public double getMaximumNumberOfCreditsForEnrolment();

        @ConfigurationProperty(key="use.global.interest.rate.table.for.event.penalties", defaultValue = "false")
        public boolean useGlobalInterestRateTableForEventPenalties();

        @ConfigurationProperty(key="active.student.personal.data.authorization.choices", defaultValue = "NO_END")
        public String activeStudentPersonalDataAuthorizationChoices();

        @ConfigurationProperty(key = "max.new.payment.codes.per.event", defaultValue = "3")
        public Integer getMaxNewPaymentCodesPerEvent();

        @ConfigurationProperty(key = "max.days.between.promise.and.payment", defaultValue = "2")
        public Integer getMaxDaysBetweenPromiseAndPayment();

        @ConfigurationProperty(key = "mobility.default.erasmus.program", defaultValue = "ERASMUS")
        public String mobilityDefaultErasmusProgram();
        
        @ConfigurationProperty(key = "candidate.portal.url")
        public String getCandidatePortalUrl();
    }

    public static ConfigurationProperties getConfiguration() {
        return ConfigurationInvocationHandler.getConfiguration(ConfigurationProperties.class);
    }

    public static boolean isToUseGlobalInterestRateTableForEventPenalties(Event event) {
        return useGlobalInterestRateTableForEventPenalties && event.isToApplyInterest();
    }

    public static void setUseGlobalInterestRateTableForEventPenalties(boolean value) {
        useGlobalInterestRateTableForEventPenalties = value;
    }

}
