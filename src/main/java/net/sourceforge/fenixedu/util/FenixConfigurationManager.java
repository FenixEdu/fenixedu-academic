package net.sourceforge.fenixedu.util;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.commons.configuration.ConfigurationInvocationHandler;
import org.fenixedu.commons.configuration.ConfigurationManager;
import org.fenixedu.commons.configuration.ConfigurationProperty;

public class FenixConfigurationManager {
    @ConfigurationManager(description = "General Fenix Configuration")
    public interface ConfigurationProperties {
        @ConfigurationProperty(
                key = "app.context",
                description = "Must be changed to match tomcat context. If you're app is running in http://localhost:8080/xpto, app.context must be xpto.",
                defaultValue = "fenix")
        public String appContext();

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

        @ConfigurationProperty(key = "context.filter.exceptions", defaultValue = "/bennu,/bankai,/theme")
        public String getContextFilterExceptions();

        @ConfigurationProperty(key = "dspace.password")
        public String getDspacePassword();

        @ConfigurationProperty(key = "dspace.serverUrl", defaultValue = "https://localhost:8443/dspace")
        public String getDspaceServerUrl();

        @ConfigurationProperty(key = "dspace.username")
        public String getDspaceUsername();

        @ConfigurationProperty(
                key = "email.admin.allowed.hosts",
                description = "comma seperated values of hosts and/or ip addresses that are allowed to call external email administration",
                defaultValue = "")
        public String getEmailAdminAllowedHosts();

        @ConfigurationProperty(key = "email.admin.password",
                description = "password required to call external email administration", defaultValue = "xPtO!)&#.")
        public String getEmailAdminPassword();

        @ConfigurationProperty(key = "exportParkingData.password")
        public String getExportParkingDataPassword();

        @ConfigurationProperty(key = "exportParkingData.username")
        public String getExportParkingDataUsername();

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

        @ConfigurationProperty(key = "file.download.url.local.content",
                defaultValue = "http://localhost:8080/fenix/downloadFile/")
        public String getFileDownloadUrlLocalContent();

        @ConfigurationProperty(
                key = "generic.application.email.confirmation.link",
                defaultValue = "http://localhost:8080/fenix/publico/genericApplications.do?method=confirmEmail&contentContextPath_PATH=/candidaturas&confirmationCode=")
        public String getGenericApplicationEmailConfirmationLink();

        @ConfigurationProperty(
                key = "generic.application.email.recommendation.link",
                defaultValue = "http://localhost:8080/fenix/publico/genericApplications.do?method=uploadRecommendation&contentContextPath_PATH=/candidaturas&confirmationCode=")
        public String getGenericApplicationEmailRecommendationLink();

        @ConfigurationProperty(key = "google.analytics.snippet",
                description = "Google Analytics snippet configuration (without <script> tags)")
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

        @ConfigurationProperty(
                key = "http.host",
                description = "Twillio host where the application will run. This is used by twilio so it can connect back to fenix",
                defaultValue = "localhost")
        public String getHTTPHost();

        @ConfigurationProperty(key = "http.port", description = "Twillio port on which the application will be available",
                defaultValue = "8080")
        public String getHTTPPort();

        @ConfigurationProperty(key = "http.protocol",
                description = "Twillio http or https protocol where the application will run", defaultValue = "http")
        public String getHTTPProtocol();

        @ConfigurationProperty(key = "index.page.redirect",
                description = "host specific initial page to be displayed from applications root",
                defaultValue = "fenixEduIndex.do")
        public String getIndexPageRedirect();

        @ConfigurationProperty(key = "jersey.password")
        public String getJerseyPassword();

        @ConfigurationProperty(key = "jersey.username")
        public String getJerseyUsername();

        @ConfigurationProperty(key = "lastSemesterForCredits")
        public String getLastSemesterForCredits();

        @ConfigurationProperty(key = "lastYearForCredits", defaultValue = "2010/2011")
        public String getLastYearForCredits();

        @ConfigurationProperty(key = "ldap.user.importation.service.password")
        public String getLdapUserImportationServicePassword();

        @ConfigurationProperty(key = "ldap.user.importation.service.url")
        public String getLdapUserImportationServiceUrl();

        @ConfigurationProperty(key = "ldap.user.importation.service.username")
        public String getLdapUserImportationServiceUsername();

        @ConfigurationProperty(key = "login.page", description = "absolute path to the login page",
                defaultValue = "http://localhost:8080/fenix/privado")
        public String getLoginPage();

        @ConfigurationProperty(key = "mailingList.host.name")
        public String getMailingListHostName();

        @ConfigurationProperty(key = "mailSender.max.recipients", defaultValue = "50")
        public String getMailSenderMaxRecipients();

        @ConfigurationProperty(key = "mail.smtp.host", description = "Cms Configuration")
        public String getMailSmtpHost();

        @ConfigurationProperty(key = "mail.smtp.name", description = "Cms Configuration")
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

        @ConfigurationProperty(key = "parkingCardId.admin.password")
        public String getParkingCardIdAdminPassword();

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

        @ConfigurationProperty(
                key = "store.pending.request",
                description = "indicates if pending request should be stored in database to recover current session after new login",
                defaultValue = "true")
        public Boolean getStorePendingRequest();

        @ConfigurationProperty(key = "tsdProcess.chart.height", defaultValue = "480")
        public String getTSDProcessChartHeight();

        @ConfigurationProperty(key = "tsdProcess.chart.width", defaultValue = "640")
        public String getTSDProcessChartWidth();

        @ConfigurationProperty(key = "twilio.from.number")
        public String getTwilioFromNumber();

        @ConfigurationProperty(key = "twilio.sid")
        public String getTwilioSid();

        @ConfigurationProperty(key = "twilio.stoken")
        public String getTwilioStoken();

        @ConfigurationProperty(key = "webServices.LibraryManagement.password")
        public String getWebServicesLibraryManagementPassword();

        @ConfigurationProperty(key = "webServices.LibraryManagement.username")
        public String getWebServicesLibraryManagementUsername();

        @ConfigurationProperty(key = "webServices.PaymentManagement.password")
        public String getWebServicesPaymentManagementPassword();

        @ConfigurationProperty(key = "webServices.PaymentManagement.username")
        public String getWebServicesPaymentManagementUsername();

        @ConfigurationProperty(key = "webServices.PersonInformation.getPersonInformation.password")
        public String getWebServicesPersonInformationGetPersonInformationPassword();

        @ConfigurationProperty(key = "webServices.PersonInformation.getPersonInformation.username")
        public String getWebServicesPersonInformationGetPersonInformationUsername();

        @ConfigurationProperty(key = "webServices.PersonManagement.getPersonInformation.password")
        public String getWebServicesPersonManagementgetPersonInformationPassword();

        @ConfigurationProperty(key = "webServices.PersonManagement.getPersonInformation.password")
        public String getWebServicesPersonManagementGetPersonInformationPassword();

        @ConfigurationProperty(key = "webServices.PersonManagement.getPersonInformation.username")
        public String getWebServicesPersonManagementgetPersonInformationUsername();

        @ConfigurationProperty(key = "webServices.PersonManagement.getPersonInformation.username")
        public String getWebServicesPersonManagementGetPersonInformationUsername();

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

        @ConfigurationProperty(key = "scaleRatio", defaultValue = "1200")
        public String scaleRatio();

        @ConfigurationProperty(key = "fontSize", defaultValue = "0.007")
        public String fontSize();

        @ConfigurationProperty(key = "padding", defaultValue = "0.025")
        public String padding();

        @ConfigurationProperty(key = "xAxisOffset", defaultValue = "0.075")
        public String xAxisOffset();

        @ConfigurationProperty(key = "yAxisOffset", defaultValue = "0.3")
        public String yAxisOffset();

        @ConfigurationProperty(key = "sotisURL", defaultValue = "../../sotis")
        public String sotisURL();
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

    /**
     * The absolute URL for the Fenix instalation.
     * It ends with the defined appContext without slash at the end.
     * 
     */
    public static String getFenixUrl() {
        final String appName = FenixConfigurationManager.getConfiguration().getHTTPHost();
        final String appContext = FenixConfigurationManager.getConfiguration().appContext();
        final String httpPort = FenixConfigurationManager.getConfiguration().getHTTPPort();
        final String httpProtocol = FenixConfigurationManager.getConfiguration().getHTTPProtocol();
        String HOST = null;
        if (StringUtils.isEmpty(httpPort)) {
            HOST = String.format("%s://%s/", httpProtocol, appName);
        } else {
            HOST = String.format("%s://%s:%s/", httpProtocol, appName, httpPort);
        }
        if (!StringUtils.isEmpty(appContext)) {
            HOST += appContext;
        }
        HOST = StringUtils.removeEnd(HOST, "/");
        return HOST;
    }
}
