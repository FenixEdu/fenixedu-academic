package pt.utl.ist.scripts.process.cron;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import net.sourceforge.fenixedu.domain.QueueJob;
import net.sourceforge.fenixedu.domain.QueueJobResult;
import net.sourceforge.fenixedu.domain.QueueJobResultFile;
import net.sourceforge.fenixedu.domain.QueueJobWithFile;
import net.sourceforge.fenixedu.domain.util.Email;
import net.sourceforge.fenixedu.util.ByteArray;

import org.joda.time.DateTime;

import pt.ist.fenixframework.CallableWithoutException;
import pt.ist.fenixframework.FenixFramework;

public class JobResultSaver extends Thread {

    private static final int MAX_ALLOWED_PACKET_SIZE = 16777216;

    private final String queueJobOid;
    private final QueueJobResult queueJobResult;

    public JobResultSaver(String queueJobOid, QueueJobResult queueJobResult) {
        this.queueJobOid = queueJobOid;
        this.queueJobResult = queueJobResult;
    }

    @Override
    public void run() {
        super.run();
        FenixFramework.getTransactionManager().withTransaction(new CallableWithoutException<Void>() {

            @Override
            public Void call() {
                try {

                    final QueueJob queueJob = FenixFramework.getDomainObject(queueJobOid);

                    queueJob.setDone(queueJobResult.getDone());
                    queueJob.setFailedCounter(queueJobResult.getFailedCounter());
                    queueJob.setJobEndTime(queueJobResult.getJobEndTime());
                    queueJob.setJobStartTime(queueJobResult.getJobStartTime());
                    queueJob.setRequestDate(queueJobResult.getRequestDate());

                    if (queueJob instanceof QueueJobWithFile) {
                        final QueueJobWithFile queueJobWithFile = (QueueJobWithFile) queueJob;
                        queueJobWithFile.setContentType(queueJobResult.getContentType());
                        final ByteArray byteArray = new ByteArray(queueJobResult.getContent());

                        QueueJobResultFile.store(queueJobWithFile, queueJobWithFile.getPerson(), queueJobWithFile.getFilename(),
                                byteArray.getBytes());
                    }

                    if (queueJobResult.getDone()) {
                        queueJob.setRootDomainObjectQueueUndone(null);
                        queueJob.setJobEndTime(new DateTime());
                        notifyUser(queueJob);
                    }

                    if (queueJob.getFailedCounter() == 3) {
                        Properties properties;
                        File file = new File("/var/local/fenix/scripts/build.properties");

                        if (!file.exists()) {
                            file = new File("build.properties");
                            if (!file.exists()) {
                                file = new File("/build.properties");
                            }
                        }
                        if (!file.exists()) {
                            throw new Error("build.properties was not found.");
                        }

                        try {
                            final FileInputStream fileInputStream = new FileInputStream(file);
                            properties = new Properties();
                            properties.load(fileInputStream);
                        } catch (final IOException e) {
                            throw new Error(e);
                        }
                        String[] emails = properties.getProperty("jobQueueDispatcher.failedJobs.email").split(",");

                        final Throwable throwable = queueJobResult.getThrowable();
                        final String stackTrace = throwable == null ? null : throwable.toString();

                        new Email("jobQueueDispatcher@fenix-scripts", "noreply@suporte.ist.utl.pt", new String[0],
                                new ArrayList<String>(Arrays.asList(emails)), Collections.EMPTY_LIST, Collections.EMPTY_LIST,
                                "Job Failed after 3 restarts", "Viva\n\n" + "O trabalho com o InternalID de "
                                        + queueJob.getExternalId() + " falhou mais de 3 vezes.\n\n" + "Request Time : "
                                        + queueJob.getRequestDate() + "\n" + "Start Time : " + queueJob.getJobStartTime() + "\n"
                                        + "User : " + queueJob.getPerson().getExternalId() + "\n" + "\n\n Error Stack Trace:\n"
                                        + stackTrace);
                    }

                } finally {
                }
                return null;
            }
        });
    }

    public void notifyUser(final QueueJob queueJob) {
        if (queueJob.getPerson() != null) {
            List<String> emails = new ArrayList<String>();
            emails.add(queueJob.getPerson().getInstitutionalOrDefaultEmailAddressValue());

            new Email("noreply@ist.utl.pt", "noreply@ist.utl.pt", new String[0], emails, Collections.EMPTY_LIST,
                    Collections.EMPTY_LIST, "Pedido de " + queueJob.getDescription() + " concluido", "O seu pedido de "
                            + queueJob.getDescription() + " já se encontra disponível no sistema Fénix.");
        }
    }

}
