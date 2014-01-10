package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import java.util.Collections;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.InternalPhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdParticipant;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService.AlertMessage;
import net.sourceforge.fenixedu.domain.phd.log.PhdLog;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.SystemSender;
import net.sourceforge.fenixedu.util.phd.PhdProperties;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;

abstract public class PhdThesisActivity extends Activity<PhdThesisProcess> {

    @Override
    final public void checkPreConditions(final PhdThesisProcess process, final User userView) {
        processPreConditions(process, userView);
        activityPreConditions(process, userView);
    }

    protected void processPreConditions(final PhdThesisProcess process, final User userView) {
    }

    abstract protected void activityPreConditions(final PhdThesisProcess process, final User userView);

    public static String getAccessInformation(PhdIndividualProgramProcess process, PhdParticipant participant,
            String coordinatorMessage, String teacherMessage) {

        if (!participant.isInternal()) {
            return AlertMessage.get("message.phd.external.access", PhdProperties.getPhdExternalAccessLink(),
                    participant.getAccessHashCode(), participant.getPassword());

        } else {
            final Person person = ((InternalPhdParticipant) participant).getPerson();

            if (process.isCoordinatorForPhdProgram(person)) {
                return AlertMessage.get(coordinatorMessage);

            } else if (process.isGuiderOrAssistentGuider(person) || person.hasTeacher()) {
                return AlertMessage.get(teacherMessage);
            }
        }

        throw new DomainException("error.PhdThesisProcess.unexpected.participant.type");
    }

    protected void email(String email, String subject, String body) {
        final SystemSender sender = Bennu.getInstance().getSystemSender();
        new Message(sender, sender.getConcreteReplyTos(), null, null, null, subject, body, Collections.singleton(email));
    }

    @Override
    protected void log(PhdThesisProcess process, User userView, Object object) {
        PhdLog.logActivity(this, process, userView, object);
    }

}