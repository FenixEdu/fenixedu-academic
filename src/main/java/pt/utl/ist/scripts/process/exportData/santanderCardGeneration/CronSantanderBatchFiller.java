package pt.utl.ist.scripts.process.exportData.santanderCardGeneration;

import org.fenixedu.bennu.scheduler.CronTask;
import org.fenixedu.bennu.scheduler.annotation.Task;

@Task(englishTitle = "CronSantanderBatchFiller", readOnly = true)
public class CronSantanderBatchFiller extends CronTask {

    @Override
    public void runTask() {
        final SantanderBatchFillerWorker santanderWorker = new SantanderBatchFillerWorker(getLogger());
        santanderWorker.run();
    }

}