package pt.utl.ist.scripts.process.updateData.phd;

import net.sourceforge.fenixedu.domain.Alert;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.scheduler.CronTask;
import org.fenixedu.bennu.scheduler.annotation.Task;

@Task(englishTitle = "FireAlerts")
public class FireAlerts extends CronTask {

    @Override
    public void runTask() {
        for (final Alert alert : Bennu.getInstance().getActiveAlertsSet()) {
            alert.fire();
        }
    }

}
