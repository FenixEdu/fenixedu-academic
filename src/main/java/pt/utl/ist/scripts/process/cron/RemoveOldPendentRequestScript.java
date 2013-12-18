package pt.utl.ist.scripts.process.cron;

import net.sourceforge.fenixedu.domain.PendingRequest;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.scheduler.CronTask;
import org.fenixedu.bennu.scheduler.annotation.Task;
import org.joda.time.DateTime;

@Task(englishTitle = "RemoveOldPendentRequestScript")
public class RemoveOldPendentRequestScript extends CronTask {

    @Override
    public void runTask() {
        final DateTime fiveMinitusAgo = new DateTime().minusMinutes(5);
        for (PendingRequest pendentRequest : Bennu.getInstance().getPendingRequestSet()) {
            if (pendentRequest.getGenerationDate().isBefore(fiveMinitusAgo)) {
                pendentRequest.delete();
            }
        }
    }
}
