/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.util.email.Message;

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
                        getQueueJobResponsibleName(queueJob), queueJob.getRequestDate());
                runJob(queueJob);
                taskLog("Finished Successfully\n");
            } catch (Throwable e) {
                fail(queueJob, e);
                taskLog("Failed %s times\n", queueJob.getFailedCounter());
                throw new Error(e);
            }
        }
    }

    public String getQueueJobResponsibleName(final QueueJob queueJob) {
        return queueJob.getPerson() != null ? queueJob.getPerson().getName() + "(" + queueJob.getPerson().getUsername() + ")" : "system";
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
                            + "User : " + getQueueJobResponsibleName(job) + "\n" + "\n\n Error Stack Trace:\n" + sw.toString();
            new Message(Bennu.getInstance().getSystemSender(), Bennu.getInstance().getSystemSender()
                    .getRoleRecipient(RoleType.MANAGER), subject, body);
        }
    }
}