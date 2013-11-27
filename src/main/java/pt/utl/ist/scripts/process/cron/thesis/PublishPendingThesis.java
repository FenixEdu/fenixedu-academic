package pt.utl.ist.scripts.process.cron.thesis;

import net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.thesis.ApproveThesisDiscussion;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant;
import pt.ist.bennu.core.domain.Bennu;
import pt.ist.bennu.core.security.Authenticate;
import pt.ist.bennu.core.security.UserSession;
import pt.ist.bennu.scheduler.CronTask;
import pt.ist.bennu.scheduler.annotation.Task;

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
                        Authenticate.setUser(person.getUser());
                        ApproveThesisDiscussion.createResult(thesis);
                        thesis.setRootDomainObjectFromPendingPublication(null);
                        break;
                    } finally {
                        Authenticate.setUser((UserSession) null);
                    }
                }
            }
        }
    }

}
