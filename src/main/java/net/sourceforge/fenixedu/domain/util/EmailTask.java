package net.sourceforge.fenixedu.domain.util;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.scheduler.CronTask;
import org.fenixedu.bennu.scheduler.annotation.Task;

@Task(englishTitle = "EmailTask", readOnly = true)
public class EmailTask extends CronTask {
    @Override
    public void runTask() {
        int sentCounter = 0;
        for (final Email email : Bennu.getInstance().getEmailQueueSet()) {
            email.deliver();
            getLogger().debug("Sent email: {} succeeded: {} failed: {}", email.getExternalId(),
                    email.getConfirmedAddresses() != null ? email.getConfirmedAddresses().size() : 0,
                    email.getFailedAddresses() != null ? email.getFailedAddresses().size() : 0);
            sentCounter++;
        }
        taskLog("Sent %d email batches\n", sentCounter);
    }
}
