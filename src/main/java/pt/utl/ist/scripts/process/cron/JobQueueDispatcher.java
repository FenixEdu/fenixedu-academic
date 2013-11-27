package pt.utl.ist.scripts.process.cron;

import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

import net.sourceforge.fenixedu.domain.QueueJob;
import pt.ist.bennu.core.domain.Bennu;
import pt.ist.bennu.scheduler.CronTask;
import pt.ist.bennu.scheduler.annotation.Task;

@Task(englishTitle = "JobQueueDispatcher")
public class JobQueueDispatcher extends CronTask {

    @Override
    public void runTask() {
        final Set<QueueJob> undoneJobs = Bennu.getInstance().getQueueJobUndoneSet();
        final QueueJob queueJob = undoneJobs.isEmpty() ? null : Collections.min(undoneJobs, new Comparator<QueueJob>() {

            @Override
            public int compare(final QueueJob queueJob1, final QueueJob queueJob2) {
                final Integer fc1 = queueJob1.getFailedCounter();
                final Integer fc2 = queueJob1.getFailedCounter();
                if (fc1 != null && fc2 != null) {
                    final int result = fc1.compareTo(fc2);
                    if (result != 0) {
                        return result;
                    }
                }
                int result = queueJob1.getPriority().compareTo(queueJob2.getPriority());
                if (result != 0) {
                    return result;
                }
                return queueJob1.getExternalId().compareTo(queueJob2.getExternalId());
            }

        });
        if (queueJob != null) {
            getLogger().info("Running: " + queueJob.getExternalId() + queueJob.getClass().getName());
            getLogger().info("   description: " + queueJob.getDescription());
            getLogger().info("   requested date: " + queueJob.getRequestDate());
            getLogger().info("   person: " + (queueJob.hasPerson() ? queueJob.getPerson().getName() : ""));
            getLogger().info("   failed counter: " + queueJob.getFailedCounter());
            getLogger().info("   job start time: " + queueJob.getJobStartTime());
            getLogger().info("   job end time: " + queueJob.getJobEndTime());
            getLogger().info("   done: " + queueJob.getDone());
            JobWorker worker = new JobWorker(queueJob.getExternalId());
            worker.run();
//            final JobLauncher jobLauncher = new JobLauncher(queueJob);
//            jobLauncher.start();
        }
    }
}