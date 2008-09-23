package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.assiduousness.Schedule;

public class DeleteSchedule extends FenixService {

    public void run(Schedule schedule) {
	schedule.delete();
    }

}
