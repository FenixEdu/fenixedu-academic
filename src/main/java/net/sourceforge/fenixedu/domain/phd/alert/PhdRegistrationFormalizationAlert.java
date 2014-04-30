package net.sourceforge.fenixedu.domain.phd.alert;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgramCalendarUtil;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;

import org.fenixedu.bennu.core.groups.Group;
import org.joda.time.LocalDate;

import java.util.Locale;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

import com.google.common.collect.FluentIterable;

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
        return new MultiLanguageString(Locale.getDefault(), AlertService.getSubjectPrefixed(process,
                "message.phd.alert.registration.formalization.subject"));
    }

    private MultiLanguageString buildBody(final PhdIndividualProgramProcess process) {
        return new MultiLanguageString(Locale.getDefault(), AlertService.getBodyText(process,
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
        return getProcess().getCandidacyProcess().getWhenRatified() != null && !new LocalDate().isBefore(getWhenToFire());
    }

    @Override
    public boolean isSystemAlert() {
        return true;
    }

    @Override
    protected void generateMessage() {
        final Group group =
                AcademicAuthorizationGroup.get(AcademicOperationType.MANAGE_PHD_PROCESSES, this
        .getProcess().getPhdProgram());

        Set<Person> members = FluentIterable.from(group.getMembers()).transform(Person.userToPerson).toSet();
        new PhdAlertMessage(getProcess(), members, getFormattedSubject(), getFormattedBody());

        new Message(getSender(), new Recipient(group), buildMailSubject(), buildMailBody());

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
