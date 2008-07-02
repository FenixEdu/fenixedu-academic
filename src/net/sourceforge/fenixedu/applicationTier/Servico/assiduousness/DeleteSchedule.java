package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.assiduousness.Schedule;

public class DeleteSchedule extends Service {

    public void run(Schedule schedule) {
	schedule.delete();
    }

}
