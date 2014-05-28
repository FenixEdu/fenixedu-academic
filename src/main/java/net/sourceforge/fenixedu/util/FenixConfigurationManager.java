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
package net.sourceforge.fenixedu.util;

import java.util.Map;

import org.fenixedu.commons.configuration.ConfigurationInvocationHandler;
import org.fenixedu.commons.configuration.ConfigurationManager;
import org.fenixedu.commons.configuration.ConfigurationProperty;

public class FenixConfigurationManager {
    @ConfigurationManager(description = "General Fenix Configuration")
    public interface ConfigurationProperties {

        @ConfigurationProperty(key = "app.institution.AES128.secretKey",
                description = "Secret for Institution ID card generation", defaultValue = "aa0bbfaf79654df4")
        public String appInstitutionAES128SecretKey();

        @ConfigurationProperty(key = "app.institution.PIN", description = "PIN for Institution ID card generation",
                defaultValue = "0000")
        public String appInstitutionPIN();

        @ConfigurationProperty(key = "barra.as.authentication.broker",
                description = "CAS ticket validation through barra: https://fenix-ashes.ist.utl.pt/fenixWiki/Barra",
                defaultValue = "false")
        public Boolean barraAsAuthenticationBroker();

        @ConfigurationProperty(key = "barra.loginUrl",
                description = "Login URL to use when barra is set as authentication broker")
        public String barraLoginUrl();

        @ConfigurationProperty(key = "ciistCostCenterCode", description = "Deprecated, to be removed", defaultValue = "8431")
        public Integer getCIISTCostCenterCode();

        @ConfigurationProperty(key = "ciist.sms.gateway.url")
        public String getCIISTSMSGatewayUrl();

        @ConfigurationProperty(key = "ciist.sms.password")
        public String getCIISTSMSPassword();

        @ConfigurationProperty(key = "ciist.sms.username")
        public String getCIISTSMSUsername();

        @ConfigurationProperty(key = "consult.roles.admin.allowed.hosts", defaultValue = "127.0.0.1,localhost,192.168.1.101")
        public String getConsultRolesAdminAllowedHosts();

        @ConfigurationProperty(key = "consult.roles.admin.password", defaultValue = "xPtO")
        public String getConsultRolesAdminPassword();

        @ConfigurationProperty(
                key = "email.admin.allowed.hosts",
                description = "comma seperated values of hosts and/or ip addresses that are allowed to call external email administration",
                defaultValue = "")
        public String getEmailAdminAllowedHosts();

        @ConfigurationProperty(key = "email.admin.password",
                description = "password required to call external email administration", defaultValue = "xPtO!)&#.")
        public String getEmailAdminPassword();

        @ConfigurationProperty(key = "externalServices.AEIST.password")
        public String getExternalServicesAEISTPassword();

        @ConfigurationProperty(key = "externalServices.AEIST.username")
        public String getExternalServicesAEISTUsername();

        @ConfigurationProperty(key = "externalServices.ISTConnect.password")
        public String getExternalServicesISTConnectPassword();

        @ConfigurationProperty(key = "externalServices.ISTConnect.username")
        public String getExternalServicesISTConnectUsername();

        @ConfigurationProperty(key = "externalServices.koha.password")
        public String getExternalServicesKohaPassword();

        @ConfigurationProperty(key = "externalServices.koha.username")
        public String getExternalServicesKohaUsername();

        @ConfigurationProperty(key = "fenix.api.events.rss.url")
        public String getFenixApiEventsRSSUrl();

        @ConfigurationProperty(key = "fenix.api.news.rss.url")
        public String getFenixApiNewsRSSUrl();

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

        @ConfigurationProperty(key = "google.analytics.snippet",
                description = "Google Analytics snippet configuration (without <script> tags)", defaultValue = "")
        public String getGoogleAnalyticsSnippet();

        @ConfigurationProperty(
                key = "gratuity.situation.creator.task.hour",
                description = "Hour at which the task will be launched to create the gratuity situations. set to -1 to deactivate.",
                defaultValue = "1")
        public String getGratuitySituationCreatorTaskHour();

        @ConfigurationProperty(
                key = "host.control.name.*",
                description = "Comma separated hostname values that are allowed to access the url host.control.name. See HostAccessControl.isAllowed(name, request)")
        public Map<String, String> getHostControlName();

        @ConfigurationProperty(key = "index.page.redirect",
                description = "host specific initial page to be displayed from applications root",
                defaultValue = "fenixEduIndex.do")
        public String getIndexPageRedirect();

        @ConfigurationProperty(key = "lastSemesterForCredits")
        public String getLastSemesterForCredits();

        @ConfigurationProperty(key = "lastYearForCredits", defaultValue = "2010/2011")
        public String getLastYearForCredits();

        @ConfigurationProperty(key = "login.page", description = "absolute path to the login page",
                defaultValue = "http://localhost:8080/fenix/privado")
        public String getLoginPage();

        @ConfigurationProperty(key = "mailingList.host.name")
        public String getMailingListHostName();

        @ConfigurationProperty(key = "mailSender.max.recipients", defaultValue = "50")
        public String getMailSenderMaxRecipients();

        @ConfigurationProperty(key = "mail.smtp.host", description = "The host of the SMTP server used to send Emails")
        public String getMailSmtpHost();

        @ConfigurationProperty(key = "mail.smtp.name", description = "The name of the SMTP server used to send Emails")
        public String getMailSmtpName();

        @ConfigurationProperty(key = "markSheet.printers.*")
        public Map<String, String> getMarkSheetPrinters();

        @ConfigurationProperty(key = "merge.units.emails",
                description = "comma separated emails of persons who want to receive emails about merge of units.")
        public String getMergeUnitsEmails();

        @ConfigurationProperty(key = "nameresolution.name", defaultValue = "fenixRemoteRequests")
        public String getNameResolutionName();

        @ConfigurationProperty(key = "nameresolution.password")
        public String getNameResolutionPassword();

        @ConfigurationProperty(key = "phd.public.candidacy.submission.link")
        public String getPhdPublicCandidacySubmissionLink();

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

        @ConfigurationProperty(
                key = "sibs.destinationInstitutionId",
                description = "used in header payments file and represents entity service identification (i.e. sibs). Default value '50000000' (sibs identification)",
                defaultValue = "50000000")
        public String getSibsDestinationInstitutionId();

        @ConfigurationProperty(key = "sibs.entityCode",
                description = "institution entity code to be used in atm machines with reference to perform payments",
                defaultValue = "1111")
        public String getSibsEntityCode();

        @ConfigurationProperty(
                key = "sibs.sourceInstitutionId",
                description = "used in header payments file, and represents institution identification accordding to transfer service (i.e. sibs). Must be given by the entity that is peforming this service. Format: '9XXXXXXX'",
                defaultValue = "11111111")
        public String getSibsSourceInstitutionId();

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

    private static HostAccessControl hostAccessControl = new HostAccessControl(getConfiguration().getHostControlName());

    private static final class PrinterManagerHolder {
        private static PrinterManager printerManager = new PrinterManager(getConfiguration().getMarkSheetPrinters());
    }

    private static Boolean barraAsAuthenticationBroker = getConfiguration().barraAsAuthenticationBroker();

    public static ConfigurationProperties getConfiguration() {
        return ConfigurationInvocationHandler.getConfiguration(ConfigurationProperties.class);
    }

    public static HostAccessControl getHostAccessControl() {
        return hostAccessControl;
    }

    public static PrinterManager getPrinterManager() {
        return PrinterManagerHolder.printerManager;
    }

    public static Boolean isBarraAsAuthenticationBroker() {
        return barraAsAuthenticationBroker;
    }

    public static void setBarraAsAuthenticationBroker(Boolean state) {
        barraAsAuthenticationBroker = state;
    }

}
