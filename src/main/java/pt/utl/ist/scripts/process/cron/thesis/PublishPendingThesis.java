package pt.utl.ist.scripts.process.cron.thesis;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.scheduler.CronTask;
import org.fenixedu.bennu.scheduler.annotation.Task;

import net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.thesis.ApproveThesisDiscussion;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant;

@Task(englishTitle = "PublishPendingThesis")
public class PublishPendingThesis extends CronTask {

    @Override
    public void runTask() {
        for (final Thesis thesis : Bennu.getInstance().getThesesPendingPublicationSet()) {
            final ThesisEvaluationParticipant approver = thesis.getEvaluationApprover();
            if (approver != null) {
                final Person person = approver.getPerson();
                if (person != null) {
                    try {
                        Authenticate.mock(person.getUser());
                        ApproveThesisDiscussion.createResult(thesis);
                        thesis.setRootDomainObjectFromPendingPublication(null);
                        break;
                    } finally {
                        Authenticate.unmock();
                    }
                }
            }
        }
    }

}
