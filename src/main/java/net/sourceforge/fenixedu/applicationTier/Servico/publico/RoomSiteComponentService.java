package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.Calendar;

import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.RoomKey;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import pt.ist.fenixframework.Atomic;

public class RoomSiteComponentService {
    @Atomic
    public static Object run(ISiteComponent bodyComponent, RoomKey roomKey, Calendar day) throws Exception {
        return RoomSiteComponentServiceByExecutionPeriodID.runService(bodyComponent, roomKey, day,
                ExecutionSemester.readActualExecutionSemester());
    }
}