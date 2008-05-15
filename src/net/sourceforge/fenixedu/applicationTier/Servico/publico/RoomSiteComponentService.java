package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.RoomKey;
import net.sourceforge.fenixedu.domain.ExecutionSemester;

public class RoomSiteComponentService extends Service {
    public static Object run(ISiteComponent bodyComponent, RoomKey roomKey, Calendar day) throws Exception {
        return RoomSiteComponentServiceByExecutionPeriodID.runService(bodyComponent, roomKey, day,
                ExecutionSemester.readActualExecutionSemester());
    }
}