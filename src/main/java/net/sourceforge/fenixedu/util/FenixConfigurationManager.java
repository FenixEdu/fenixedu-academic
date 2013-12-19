package net.sourceforge.fenixedu.util;

import java.util.Map;

import org.fenixedu.bennu.core.annotation.ConfigurationManager;
import org.fenixedu.bennu.core.annotation.ConfigurationProperty;
import org.fenixedu.bennu.core.util.ConfigurationInvocationHandler;

public class FenixConfigurationManager {
    @ConfigurationManager(description = "General Fenix Configuration")
    public interface ConfigurationProperties {
        @ConfigurationProperty(key = "app.context")
        public String appContext();

        @ConfigurationProperty(key = "app.institution.AES128.secretKey")
        public String appInstitutionAES128SecretKey();

        @ConfigurationProperty(key = "app.institution.PIN")
        public String appInstitutionPIN();

        @ConfigurationProperty(key = "barra.as.authentication.broker", defaultValue = "true")
        public Boolean barraAsAuthenticationBroker();

        @ConfigurationProperty(key = "barra.loginUrl")
        public String barraLoginUrl();

        @ConfigurationProperty(key = "ciistCostCenterCode")
        public Integer getCIISTCostCenterCode();

        @ConfigurationProperty(key = "ciist.sms.gateway.url")
        public String getCIISTSMSGatewayUrl();

        @ConfigurationProperty(key = "ciist.sms.password")
        public String getCIISTSMSPassword();

        @ConfigurationProperty(key = "ciist.sms.username")
        public String getCIISTSMSUsername();

        @ConfigurationProperty(key = "consult.roles.admin.allowed.hosts")
        public String getConsultRolesAdminAllowedHosts();

        @ConfigurationProperty(key = "consult.roles.admin.password")
        public String getConsultRolesAdminPassword();

        @ConfigurationProperty(key = "context.filter.exceptions", defaultValue = "/api")
        public String getContextFilterExceptions();

        @ConfigurationProperty(key = "debug.actions", defaultValue = "false")
        public Boolean getDebugActions();

        @ConfigurationProperty(key = "dspace.downloadUriFormat")
        public String getDspaceDownloadUriFormat();

        @ConfigurationProperty(key = "dspace.password")
        public String getDspacePassword();

        @ConfigurationProperty(key = "dspace.rmi.server.name")
        public String getDspaceRMIServerName();

        @ConfigurationProperty(key = "dspace.serverUrl")
        public String getDspaceServerUrl();

        @ConfigurationProperty(key = "dspace.username")
        public String getDspaceUsername();

        @ConfigurationProperty(key = "email.admin.allowed.hosts")
        public String getEmailAdminAllowedHosts();

        @ConfigurationProperty(key = "email.admin.password")
        public String getEmailAdminPassword();

        @ConfigurationProperty(key = "exportParkingData.password")
        public String getExportParkingDataPassword();

        @ConfigurationProperty(key = "export.parking.data.report.input.file")
        public String getExportParkingDataReportInputFile();

        @ConfigurationProperty(key = "exportParkingData.username")
        public String getExportParkingDataUsername();

        @ConfigurationProperty(key = "external.application.workflow.equivalences.uri")
        public String getExternalApplicationWorkflowEquivalencesUri();

        @ConfigurationProperty(key = "external.application.workflow.equivalences.uri.secret")
        public String getExternalApplicationWorkflowEquivalencesUriSecret();

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

        @ConfigurationProperty(key = "file.download.url.local.content")
        public String getFileDownloadUrlLocalContent();

        @ConfigurationProperty(key = "passSize", defaultValue = "8")
        public Integer getGeneratedPasswordSize();

        @ConfigurationProperty(key = "generic.application.email.confirmation.link")
        public String getGenericApplicationEmailConfirmationLink();

        @ConfigurationProperty(key = "generic.application.email.recommendation.link")
        public String getGenericApplicationEmailRecommendationLink();

        @ConfigurationProperty(key = "google.analytics.snippet")
        public String getGoogleAnalyticsSnippet();

        @ConfigurationProperty(key = "gratuity.situation.creator.task.hour")
        public String getGratuitySituationCreatorTaskHour();

        @ConfigurationProperty(key = "host.control.name.*")
        public Map<String, String> getHostControlName();

        @ConfigurationProperty(key = "http.host")
        public String getHTTPHost();

        @ConfigurationProperty(key = "http.port")
        public String getHTTPPort();

        @ConfigurationProperty(key = "http.protocol")
        public String getHTTPProtocol();

        @ConfigurationProperty(key = "index.html.link.*")
        public Map<String, String> getIndexHtmlLink();

        @ConfigurationProperty(key = "institution.url")
        public String getInstitutionURL();

        @ConfigurationProperty(key = "jersey.password")
        public String getJerseyPassword();

        @ConfigurationProperty(key = "jersey.username")
        public String getJerseyUsername();

        @ConfigurationProperty(key = "jndi.properties.file")
        public String getJNDIPropertiesFile();

        @ConfigurationProperty(key = "lastSemesterForCredits")
        public String getLastSemesterForCredits();

        @ConfigurationProperty(key = "lastYearForCredits")
        public String getLastYearForCredits();

        @ConfigurationProperty(key = "ldap.user.importation.service.password")
        public String getLdapUserImportationServicePassword();

        @ConfigurationProperty(key = "ldap.user.importation.service.url")
        public String getLdapUserImportationServiceUrl();

        @ConfigurationProperty(key = "ldap.user.importation.service.username")
        public String getLdapUserImportationServiceUsername();

        @ConfigurationProperty(key = "log.image.directory")
        public String getLogImageDirectory();

        @ConfigurationProperty(key = "login.html.link.*")
        public Map<String, String> getLoginHtmlLink();

        @ConfigurationProperty(key = "login.page")
        public String getLoginPage();

        @ConfigurationProperty(key = "log.profile.dir")
        public String getLogProfileDir();

        @ConfigurationProperty(key = "log.profile.filename")
        public String getLogProfileFilename();

        @ConfigurationProperty(key = "mailingList.host.name")
        public String getMailingListHostName();

        @ConfigurationProperty(key = "mailSender.max.recipients")
        public String getMailSenderMaxRecipients();

        @ConfigurationProperty(key = "mail.smtp.host")
        public String getMailSmtpHost();

        @ConfigurationProperty(key = "mail.smtp.name")
        public String getMailSmtpName();

        @ConfigurationProperty(key = "markSheet.printers.*")
        public Map<String, String> getMarkSheetPrinters();

        @ConfigurationProperty(key = "merge.units.emails")
        public String getMergeUnitsEmails();

        @ConfigurationProperty(key = "monthlySmsLimit")
        public Integer getMonthlySmsLimit();

        @ConfigurationProperty(key = "nameresolution.name")
        public String getNameResolutionName();

        @ConfigurationProperty(key = "nameresolution.password")
        public String getNameResolutionPassword();

        @ConfigurationProperty(key = "parkingCardId.admin.password")
        public String getParkingCardIdAdminPassword();

        @ConfigurationProperty(key = "phd.public.candidacy.submission.link")
        public String getPhdPublicCandidacySubmissionLink();

        @ConfigurationProperty(key = "receipt.min.year.to.create", defaultValue = "2006")
        public Integer getReceiptMinYearToCreate();

        @ConfigurationProperty(key = "receipt.numberSeries.for.years")
        public String getReceiptNumberSeriesForYears();

        @ConfigurationProperty(key = "rmi.port")
        public String getRMIPort();

        @ConfigurationProperty(key = "rmi.registry.port")
        public String getRMIRegistryPort();

        @ConfigurationProperty(key = "rmi.ssl")
        public String getRMISSL();

        @ConfigurationProperty(key = "rmi.ssl.truststore")
        public String getRMISSLTruststore();

        @ConfigurationProperty(key = "rmi.ssl.truststore.password")
        public String getRMISSLTruststorePassword();

        @ConfigurationProperty(key = "rmi.stream.bytes.block")
        public String getRMIStreamBytesBlock();

        @ConfigurationProperty(key = "rmi.stream.bytes.max")
        public String getRMIStreamBytesMax();

        @ConfigurationProperty(key = "rmi.stream.bytes.min")
        public String getRMIStreamBytesMin();

        @ConfigurationProperty(key = "script.isAlive.check.db", defaultValue = "false")
        public Boolean getScriptIsAliveCheckDB();

        @ConfigurationProperty(key = "semester.for.from.enrolments")
        public String getSemesterForFromEnrolments();

        @ConfigurationProperty(key = "semester.for.from.mark.sheet.managment")
        public String getSemesterForFromMarkSheetManagment();

        @ConfigurationProperty(key = "sibs.destinationInstitutionId")
        public String getSibsDestinationInstitutionId();

        @ConfigurationProperty(key = "sibs.entityCode")
        public String getSibsEntityCode();

        @ConfigurationProperty(key = "sibs.sourceInstitutionId")
        public String getSibsSourceInstitutionId();

        @ConfigurationProperty(key = "sms.delivery.host")
        public String getSMSDeliveryHost();

        @ConfigurationProperty(key = "sms.delivery.password")
        public String getSMSDeliveryPassword();

        @ConfigurationProperty(key = "sms.delivery.port")
        public Integer getSMSDeliveryPort();

        @ConfigurationProperty(key = "sms.delivery.protocol")
        public String getSMSDeliveryProtocol();

        @ConfigurationProperty(key = "sms.delivery.uri")
        public String getSMSDeliveryUri();

        @ConfigurationProperty(key = "sms.delivery.username")
        public String getSMSDeliveryUsername();

        @ConfigurationProperty(key = "sms.gateway.host")
        public String getSMSGatewayHost();

        @ConfigurationProperty(key = "sms.gateway.password")
        public String getSMSGatewayPassword();

        @ConfigurationProperty(key = "sms.gateway.port")
        public Integer getSMSGatewayPort();

        @ConfigurationProperty(key = "sms.gateway.protocol")
        public String getSMSGatewayProtocol();

        @ConfigurationProperty(key = "sms.gateway.uri")
        public String getSMSGatewayUri();

        @ConfigurationProperty(key = "sms.gateway.username")
        public String getSMSGatewayUsername();

        @ConfigurationProperty(key = "startExecutionYearForAllOptionalCurricularCoursesWithLessTenEnrolments")
        public String getStartExecutionYearForAllOptionalCurricularCoursesWithLessTenEnrolments();

        @ConfigurationProperty(key = "start.semester.for.bolonha.degrees")
        public String getStartSemesterForBolonhaDegrees();

        @ConfigurationProperty(key = "start.semester.for.bolonha.transition")
        public String getStartSemesterForBolonhaTransition();

        @ConfigurationProperty(key = "startSemesterForCredits")
        public String getStartSemesterForCredits();

        @ConfigurationProperty(key = "start.year.for.bolonha.degrees")
        public String getStartYearForBolonhaDegrees();

        @ConfigurationProperty(key = "start.year.for.bolonha.transition")
        public String getStartYearForBolonhaTransition();

        @ConfigurationProperty(key = "startYearForCredits")
        public String getStartYearForCredits();

        @ConfigurationProperty(key = "store.pending.request", defaultValue = "false")
        public Boolean getStorePendingRequest();

        @ConfigurationProperty(key = "tsdProcess.chart.height")
        public String getTSDProcessChartHeight();

        @ConfigurationProperty(key = "tsdProcess.chart.width")
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

        @ConfigurationProperty(key = "year.for.from.enrolments")
        public String getYearForFromEnrolments();

        @ConfigurationProperty(key = "year.for.from.mark.sheet.managment")
        public String getYearForFromMarkSheetManagment();
    }

    private static HostAccessControl hostAccessControl = new HostAccessControl(getConfiguration().getHostControlName());

    private static HostRedirector hostRedirector = new HostRedirector(getConfiguration().getIndexHtmlLink(), getConfiguration()
            .getLoginHtmlLink());

    private static PrinterManager printerManager = new PrinterManager(getConfiguration().getMarkSheetPrinters());

    private static Boolean barraAsAuthenticationBroker = getConfiguration().barraAsAuthenticationBroker();

    public static ConfigurationProperties getConfiguration() {
        return ConfigurationInvocationHandler.getConfiguration(ConfigurationProperties.class);
    }

    public static HostAccessControl getHostAccessControl() {
        return hostAccessControl;
    }

    public static HostRedirector getHostRedirector() {
        return hostRedirector;
    }

    public static PrinterManager getPrinterManager() {
        return printerManager;
    }

    public static Boolean isBarraAsAuthenticationBroker() {
        return barraAsAuthenticationBroker;
    }

    public static void setBarraAsAuthenticationBroker(Boolean state) {
        barraAsAuthenticationBroker = state;
    }
}
