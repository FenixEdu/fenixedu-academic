package pt.utl.ist.scripts.process.updateData;

import java.util.Set;

import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.scheduler.CronTask;
import org.fenixedu.bennu.scheduler.annotation.Task;

import pt.ist.fenixframework.Atomic;

@Task(englishTitle = "Check if an Unit is internal and is marked as external")
public class CheckIfIsInternalUnitTask extends CronTask {

    @Override
    @Atomic
    public void runTask() throws Exception {
        Set<UnitName> units = Bennu.getInstance().getUnitNameSet();
        int count = 0;
        for (UnitName unitName : units) {
            if (unitName.getIsExternalUnit() && unitName.getUnit().isInternal()) {
                taskLog(unitName.getExternalId() + " -> " + unitName.getName());
                count++;
                unitName.setIsExternalUnit(false);
            }
        }
        taskLog(count + " changes.");
    }

}
