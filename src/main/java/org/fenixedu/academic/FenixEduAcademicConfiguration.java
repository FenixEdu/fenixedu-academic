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

        @ConfigurationProperty(key = "dges.username.prefix",
                description = "The prefix for the username of students created via the DGES Student Importation Process.",
                defaultValue = "ist1")
        public String dgesUsernamePrefix();

        @ConfigurationProperty(key = "ciist.sms.gateway.url")
        public String getCIISTSMSGatewayUrl();

        @ConfigurationProperty(key = "ciist.sms.password")
        public String getCIISTSMSPassword();

        @ConfigurationProperty(key = "ciist.sms.username")
        public String getCIISTSMSUsername();

        @ConfigurationProperty(key = "fenix.api.events.rss.url.pt")
        public String getFenixApiEventsRSSUrlPt();

        @ConfigurationProperty(key = "fenix.api.news.rss.url.pt")
        public String getFenixApiNewsRSSUrlPt();

        @ConfigurationProperty(key = "fenix.api.events.rss.url.en")
        public String getFenixApiEventsRSSUrlEn();

        @ConfigurationProperty(key = "fenix.api.news.rss.url.en")
        public String getFenixApiNewsRSSUrlEn();

        @ConfigurationProperty(key = "fenix.api.canteen.url", defaultValue = "")
        public String getFenixApiCanteenUrl();

        @ConfigurationProperty(key = "fenix.api.canteen.user", defaultValue = "")
        public String getFenixApiCanteenUser();

        @ConfigurationProperty(key = "fenix.api.canteen.secret", defaultValue = "")
        public String getFenixApiCanteenSecret();

        @ConfigurationProperty(key = "generic.application.email.confirmation.link",
                defaultValue = "http://localhost:8080/fenix/publico/genericApplications.do?method=confirmEmail&confirmationCode=")
        public String getGenericApplicationEmailConfirmationLink();

        @ConfigurationProperty(
                key = "generic.application.email.recommendation.link",
                defaultValue = "http://localhost:8080/fenix/publico/genericApplications.do?method=uploadRecommendation&confirmationCode=")
        public String getGenericApplicationEmailRecommendationLink();

        @ConfigurationProperty(key = "lastSemesterForCredits")
        public String getLastSemesterForCredits();

        @ConfigurationProperty(key = "lastYearForCredits", defaultValue = "2010/2011")
        public String getLastYearForCredits();

        @ConfigurationProperty(key = "mailSender.max.recipients", defaultValue = "50")
        public String getMailSenderMaxRecipients();

        @ConfigurationProperty(key = "mail.smtp.host", description = "The host of the SMTP server used to send Emails")
        public String getMailSmtpHost();

        @ConfigurationProperty(key = "mail.smtp.name", description = "The name of the SMTP server used to send Emails")
        public String getMailSmtpName();

        @ConfigurationProperty(key = "phd.public.candidacy.submission.link")
        public String getPhdPublicCandidacySubmissionLink();

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

        @ConfigurationProperty(key = "startExecutionYearForAllOptionalCurricularCoursesWithLessTenEnrolments",
                defaultValue = "2006/2007")
        public String getStartExecutionYearForAllOptionalCurricularCoursesWithLessTenEnrolments();

        @ConfigurationProperty(key = "start.semester.for.bolonha.degrees", defaultValue = "1")
        public String getStartSemesterForBolonhaDegrees();

        @ConfigurationProperty(key = "start.semester.for.bolonha.transition", defaultValue = "1")
        public String getStartSemesterForBolonhaTransition();

        @ConfigurationProperty(key = "startSemesterForCredits", defaultValue = "2")
        public String getStartSemesterForCredits();

        @ConfigurationProperty(key = "start.year.for.bolonha.degrees", defaultValue = "2006/2007")
        public String getStartYearForBolonhaDegrees();

        @ConfigurationProperty(key = "start.year.for.bolonha.transition", defaultValue = "2007/2008")
        public String getStartYearForBolonhaTransition();

        @ConfigurationProperty(key = "startYearForCredits", defaultValue = "2002/2003")
        public String getStartYearForCredits();

        @ConfigurationProperty(key = "twilio.from.number")
        public String getTwilioFromNumber();

        @ConfigurationProperty(key = "twilio.sid")
        public String getTwilioSid();

        @ConfigurationProperty(key = "twilio.stoken")
        public String getTwilioStoken();

        @ConfigurationProperty(key = "webServices.internationalRegistration.username")
        public String getWebServicesInternationalRegistrationUsername();

        @ConfigurationProperty(key = "webServices.internationalRegistration.password")
        public String getWebServicesInternationalRegistrationPassword();

        @ConfigurationProperty(key = "webServices.internationalRegistration.url")
        public String getWebServicesInternationalRegistrationUrl();

        @ConfigurationProperty(key = "year.for.from.enrolments", defaultValue = "2004/2005")
        public String getYearForFromEnrolments();

        @ConfigurationProperty(key = "year.for.from.mark.sheet.managment",
                description = "identifies the execution period after which mark sheet are to be managed in the fenix system.",
                defaultValue = "2005/2006")
        public String getYearForFromMarkSheetManagment();
    }

    public static ConfigurationProperties getConfiguration() {
        return ConfigurationInvocationHandler.getConfiguration(ConfigurationProperties.class);
    }

}
