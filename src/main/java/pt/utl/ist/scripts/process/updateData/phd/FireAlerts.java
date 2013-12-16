package pt.utl.ist.scripts.process.updateData.phd;

import net.sourceforge.fenixedu.domain.Alert;
import pt.ist.bennu.core.domain.Bennu;
import pt.ist.bennu.scheduler.CronTask;
import pt.ist.bennu.scheduler.annotation.Task;

@Task(englishTitle = "FireAlerts")
public class FireAlerts extends CronTask {

    @Override
    public void runTask() {
        for (final Alert alert : Bennu.getInstance().getActiveAlertsSet()) {
            alert.fire();
        }
    }

}
