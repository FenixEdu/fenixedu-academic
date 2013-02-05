package net.sourceforge.fenixedu.presentationTier.docs.phd.notification;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.postingRules.FixedAmountPR;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdParticipant;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;
import net.sourceforge.fenixedu.domain.phd.notification.PhdNotification;
import net.sourceforge.fenixedu.presentationTier.docs.FenixReport;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class PhdNotificationDocument extends FenixReport {

    private static final String DATE_FORMAT_PT = "dd/MM/yyyy";

    private static final String DATE_FORMAT_EN = "yyyy/MM/dd";

    private PhdNotification notification;

    private Language language;

    public PhdNotificationDocument(PhdNotification notification, Language language) {
        setNotification(notification);
        setLanguage(language);
        fillReport();
    }

    private PhdNotification getNotification() {
        return this.notification;
    }

    private void setNotification(PhdNotification notification) {
        this.notification = notification;
    }

    @Override
    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    @Override
    protected void fillReport() {

        final PhdProgramCandidacyProcess candidacyProcess = getNotification().getCandidacyProcess();
        final Person person = candidacyProcess.getPerson();
        final PhdIndividualProgramProcess individualProgramProcess = candidacyProcess.getIndividualProgramProcess();

        addParameter("administrativeOfficeCoordinator", individualProgramProcess.getPhdProgram().getAdministrativeOffice()
                .getUnit().getActiveUnitCoordinator().getFirstAndLastName());

        addParameter("name", person.getName());
        addParameter("address", person.getAddress());
        addParameter("areaCode", person.getAreaCode());
        addParameter("areaOfAreaCode", person.getAreaOfAreaCode());
        addParameter("programName", individualProgramProcess.getPhdProgram().getName().getContent(getLanguage()));

        addParameter("processNumber", individualProgramProcess.getProcessNumber());

        final LocalDate whenRatified = candidacyProcess.getWhenRatified();

        addParameter("ratificationDate", whenRatified != null ? whenRatified.toString(getDateFormat()) : "");

        addParameter("insuranceFee", getInsuranceFee(individualProgramProcess));
        addParameter("registrationFee", getRegistrationFee(individualProgramProcess, whenRatified));

        addParameter("date", new LocalDate().toString(getDateFormat()));
        addParameter("notificationNumber", getNotification().getNotificationNumber());

        addGuidingsParameter(individualProgramProcess);

    }

    private void addGuidingsParameter(final PhdIndividualProgramProcess individualProgramProcess) {
        if (!individualProgramProcess.getGuidings().isEmpty() && !individualProgramProcess.getAssistantGuidings().isEmpty()) {
            addParameter("guidingsInformation", MessageFormat.format(getMessageFromResource(getClass().getName()
                    + ".full.guidings.template"), buildGuidingsInformation(individualProgramProcess.getGuidings()),
                    buildGuidingsInformation(individualProgramProcess.getAssistantGuidings())));
        } else if (!individualProgramProcess.getGuidings().isEmpty()) {
            addParameter("guidingsInformation", MessageFormat.format(getMessageFromResource(getClass().getName()
                    + ".guidings.only.template"), buildGuidingsInformation(individualProgramProcess.getGuidings())));
        } else {
            addParameter("guidingsInformation", "");
        }
    }

    private String buildGuidingsInformation(final List<PhdParticipant> guidings) {
        final StringBuilder result = new StringBuilder();
        for (int i = 0; i < guidings.size(); i++) {
            final PhdParticipant guiding = guidings.get(i);
            result.append(guiding.getNameWithTitle());
            if (i == guidings.size() - 2) {
                result.append(" ").append(getMessageFromResource("label.and")).append(" ");
            } else {
                result.append(", ");
            }
        }

        if (result.length() > 0) {
            if (result.toString().endsWith(getMessageFromResource("label.and"))) {
                return result.substring(0, result.length() - getMessageFromResource("label.and").length());
            }

            if (result.toString().endsWith(", ")) {
                return result.substring(0, result.length() - 2);
            }
        }

        return result.toString();

    }

    private String getMessageFromResource(String key) {
        return ResourceBundle.getBundle("resources.PhdResources", new Locale(getLanguage().name())).getString(key);
    }

    private String getDateFormat() {
        return getLanguage() == Language.pt ? DATE_FORMAT_PT : DATE_FORMAT_EN;
    }

    private String getRegistrationFee(final PhdIndividualProgramProcess individualProgramProcess, final LocalDate whenRatified) {
        return whenRatified != null ? ((FixedAmountPR) individualProgramProcess.getPhdProgram().getServiceAgreementTemplate()
                .findPostingRuleByEventTypeAndDate(EventType.PHD_REGISTRATION_FEE, whenRatified.toDateTimeAtMidnight()))
                .getFixedAmount().toPlainString() : "";
    }

    private String getInsuranceFee(final PhdIndividualProgramProcess individualProgramProcess) {
        return ((FixedAmountPR) RootDomainObject
                .getInstance()
                .getInstitutionUnit()
                .getUnitServiceAgreementTemplate()
                .findPostingRuleBy(EventType.INSURANCE,
                        individualProgramProcess.getExecutionYear().getBeginDateYearMonthDay().toDateTimeAtMidnight(),
                        individualProgramProcess.getExecutionYear().getEndDateYearMonthDay().toDateTimeAtMidnight()))
                .getFixedAmount().toPlainString();
    }

    @Override
    public String getReportFileName() {
        return "Notification-" + getNotification().getNotificationNumber().replace("/", "-") + "-"
                + new DateTime().toString(YYYYMMDDHHMMSS);
    }

    @Override
    public String getReportTemplateKey() {
        return getClass().getName() + "." + getNotification().getType().name() + "." + getLanguage().name();
    }

}
