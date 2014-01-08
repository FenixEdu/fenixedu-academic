package pt.utl.ist.scripts.process.cron;

import net.sourceforge.fenixedu.domain.QueueJob;
import net.sourceforge.fenixedu.domain.QueueJobResult;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.CallableWithoutException;
import pt.ist.fenixframework.FenixFramework;

public class JobWorker {

    private static final Logger logger = LoggerFactory.getLogger(JobWorker.class);

    private final String queueJobOid;
    private final QueueJobResult queueJobResult = new QueueJobResult();

    public JobWorker(String queueJobOid) {
        this.queueJobOid = queueJobOid;
    }

    public QueueJobResult getQueueJobResult() {
        return queueJobResult;
    }

    public void run() {
        FenixFramework.getTransactionManager().withTransaction(new CallableWithoutException<Void>() {

            @Override
            public Void call() {
                final QueueJob queueJob = FenixFramework.getDomainObject(queueJobOid);
                queueJobResult.setDone(queueJob.getDone());
                queueJobResult.setFailedCounter(queueJob.getFailedCounter());
                queueJobResult.setJobEndTime(queueJob.getJobEndTime());
                queueJobResult.setJobStartTime(queueJob.getJobStartTime());
                queueJobResult.setRequestDate(queueJob.getRequestDate());

                logger.info("Starting job " + queueJob.getExternalId());
                if (queueJob.getFailedCounter() > 0) {
                    logger.info(" (" + queueJob.getFailedCounter().intValue() + " failures)");
                } else {
                    logger.info("");
                }

                queueJobResult.setJobStartTime(new DateTime());
                try {
                    final QueueJobResult result = queueJob.execute();
                    if (result != null) {
                        queueJobResult.setContentType(result.getContentType());
                        queueJobResult.setContent(result.getContent());
                    }
                    queueJobResult.setDone(Boolean.TRUE);
                } catch (Throwable t) {
                    logger.error(t.getMessage(), t);
                    logger.info("Failed queued job execution!");
                    logger.info("Job OID: " + queueJob.getExternalId() + " User: " + queueJob.getPerson().getIstUsername()
                            + " Start Time: " + queueJob.getJobStartTime());
                    queueJobResult.setThrowable(t);
                    queueJobResult.setFailedCounter(queueJob.getFailedCounter() + 1);
                    logger.info(" Failure Count: " + queueJob.getFailedCounter() + " times");
                    queueJobResult.setDone(Boolean.FALSE);
                }
                return null;
            }

        });
    }

}
