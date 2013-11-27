package pt.utl.ist.scripts.process.exportData.santanderCardGeneration;

import pt.ist.bennu.scheduler.CronTask;
import pt.ist.bennu.scheduler.annotation.Task;

@Task(englishTitle = "CronSantanderBatchFiller")
public class CronSantanderBatchFiller extends CronTask {

    @Override
    public void runTask() {
        final SantanderBatchFillerWorker santanderWorker = new SantanderBatchFillerWorker(getLogger());
        try {
            santanderWorker.run();
        } catch (Exception e) {
            throw new Error(e);
        }
    }

}