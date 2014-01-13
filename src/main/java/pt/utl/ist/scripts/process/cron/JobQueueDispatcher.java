package pt.utl.ist.scripts.process.cron;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.QueueJob;
import net.sourceforge.fenixedu.domain.QueueJobResult;
import net.sourceforge.fenixedu.domain.QueueJobResultFile;
import net.sourceforge.fenixedu.domain.QueueJobWithFile;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.scheduler.CronTask;
import org.fenixedu.bennu.scheduler.annotation.Task;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

@Task(englishTitle = "JobQueueDispatcher", readOnly = true)
public class JobQueueDispatcher extends CronTask {
    private class QueueComparator implements Comparator<QueueJob> {
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
    }

    @Override
    public void runTask() {
        final Set<QueueJob> undoneJobs = Bennu.getInstance().getQueueJobUndoneSet();
        final QueueJob queueJob = undoneJobs.isEmpty() ? null : Collections.min(undoneJobs, new QueueComparator());
        if (queueJob != null) {
            try {
                taskLog("Started: %s (%s) requested by %s on %s\n", queueJob.getClass().getName(), queueJob.getExternalId(),
                        queueJob.getPerson() != null ? queueJob.getPerson().getName() + "(" + queueJob.getPerson().getUsername()
                                + ")" : "system", queueJob.getRequestDate());
                runJob(queueJob);
                taskLog("Finished Successfully\n");
            } catch (Throwable e) {
                fail(queueJob, e);
                taskLog("Failed %s times\n", queueJob.getFailedCounter());
                throw new Error(e);
            }
        }
    }

    @Atomic(mode = TxMode.WRITE)
    protected void runJob(QueueJob job) throws Exception {
        job.setJobStartTime(new DateTime());
        QueueJobResult result = job.execute();
        if (job instanceof QueueJobWithFile) {
            final QueueJobWithFile queueJobWithFile = (QueueJobWithFile) job;
            queueJobWithFile.setContentType(result.getContentType());
            QueueJobResultFile.store(queueJobWithFile, queueJobWithFile.getPerson(), queueJobWithFile.getFilename(),
                    result.getContent());
        }
        job.setDone(true);
        job.setRootDomainObjectQueueUndone(null);
        job.setJobEndTime(new DateTime());
        if (job.getPerson() != null) {
            List<String> emails = new ArrayList<String>();
            emails.add(job.getPerson().getInstitutionalOrDefaultEmailAddressValue());
            String subject = "Pedido de " + job.getDescription() + " concluido";
            String body = "O seu pedido de " + job.getDescription() + " já se encontra disponível no sistema Fénix.";
            new Message(Bennu.getInstance().getSystemSender(), job.getPerson().getEmailForSendingEmails(), subject, body);
        }
    }

    @Atomic(mode = TxMode.WRITE)
    protected void fail(final QueueJob job, Throwable t) {
        job.setFailedCounter(job.getFailedCounter() + 1);
        job.setJobEndTime(new DateTime());
        if (job.getFailedCounter() == 3) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            String subject = "Job " + job.getClass().getName() + "failed 3 times";
            String body =
                    "Viva\n\n" + "O trabalho com o externalId de " + job.getExternalId() + " falhou mais de 3 vezes.\n\n"
                            + "Request Time : " + job.getRequestDate() + "\n" + "Start Time : " + job.getJobStartTime() + "\n"
                            + "User : " + job.getPerson().getName() + "(" + job.getPerson().getUsername() + ")\n"
                            + "\n\n Error Stack Trace:\n" + sw.toString();
            new Message(Bennu.getInstance().getSystemSender(), Recipient.newInstance(new RoleGroup(RoleType.MANAGER)), subject,
                    body);
        }
    }
}