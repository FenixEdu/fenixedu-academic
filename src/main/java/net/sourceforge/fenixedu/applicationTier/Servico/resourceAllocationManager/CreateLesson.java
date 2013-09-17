/* 
 *
 * Created on 2003/08/12
 */
package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomOccupationEditor;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.util.DiaSemana;

import org.joda.time.YearMonthDay;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class CreateLesson {

    @Atomic
    public static void run(DiaSemana weekDay, Calendar begin, Calendar end, FrequencyType frequency,
            InfoRoomOccupationEditor infoRoomOccupation, InfoShift infoShift, YearMonthDay beginDate, YearMonthDay endDate)
            throws FenixServiceException {
        check(RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE);

        final ExecutionSemester executionSemester =
                FenixFramework.getDomainObject(infoShift.getInfoDisciplinaExecucao().getInfoExecutionPeriod()
                        .getExternalId());

        final Shift shift = FenixFramework.getDomainObject(infoShift.getExternalId());

        AllocatableSpace room = null;
        if (infoRoomOccupation != null) {
            room =
                    infoRoomOccupation.getInfoRoom() != null ? AllocatableSpace
                            .findAllocatableSpaceForEducationByName(infoRoomOccupation.getInfoRoom().getNome()) : null;
        }

        new Lesson(weekDay, begin, end, shift, frequency, executionSemester, beginDate, endDate, room);
    }
}