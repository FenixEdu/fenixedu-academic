package pt.utl.ist.scripts.process.cron;

import net.sourceforge.fenixedu.domain.PendingRequest;

import org.joda.time.DateTime;

import pt.ist.bennu.core.domain.Bennu;
import pt.ist.bennu.scheduler.CronTask;
import pt.ist.bennu.scheduler.annotation.Task;

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
