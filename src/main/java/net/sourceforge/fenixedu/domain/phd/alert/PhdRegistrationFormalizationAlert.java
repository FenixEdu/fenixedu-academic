package net.sourceforge.fenixedu.domain.phd.alert;

import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgramCalendarUtil;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;

import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class PhdRegistrationFormalizationAlert extends PhdRegistrationFormalizationAlert_Base {

    public PhdRegistrationFormalizationAlert(PhdIndividualProgramProcess process, final int maxDays) {
        super();
        init(process);
        setMaxDays(maxDays);
    }

    private void init(final PhdIndividualProgramProcess process) {
        super.init(process, buildSubject(process), buildBody(process));
    }

    private MultiLanguageString buildSubject(final PhdIndividualProgramProcess process) {
        return new MultiLanguageString(Language.getDefaultLanguage(), AlertService.getSubjectPrefixed(process,
                "message.phd.alert.registration.formalization.subject"));
    }

    private MultiLanguageString buildBody(final PhdIndividualProgramProcess process) {
        return new MultiLanguageString(Language.getDefaultLanguage(), AlertService.getBodyText(process,
                "message.phd.alert.registration.formalization.body"));
    }

    @Override
    public String getDescription() {
        return getResourceBundle().getString("message.phd.alert.registration.formalization.description");
    }

    private LocalDate getWhenToFire() {
        return PhdProgramCalendarUtil.addWorkDaysTo(getProcess().getCandidacyProcess().getWhenRatified(), getMaxDays());
    }

    @Override
    protected boolean isToDiscard() {
        return hasFireDate() || getProcess().isRegistrationFormalized();
    }

    @Override
    protected boolean isToFire() {
        return !new LocalDate().isBefore(getWhenToFire());
    }

    @Override
    public boolean isSystemAlert() {
        return true;
    }

    @Override
    protected void generateMessage() {
        final Group academicOfficeGroup =
                new AcademicAuthorizationGroup(AcademicOperationType.MANAGE_PHD_PROCESSES, this.getProcess().getPhdProgram());

        new PhdAlertMessage(getProcess(), academicOfficeGroup.getElements(), getFormattedSubject(), getFormattedBody());

        new Message(getSender(), new Recipient(academicOfficeGroup.getElements()), buildMailSubject(), buildMailBody());

    }

    @Override
    public boolean isToSendMail() {
        return true;
    }

    @Deprecated
    public boolean hasMaxDays() {
        return getMaxDays() != null;
    }

}
