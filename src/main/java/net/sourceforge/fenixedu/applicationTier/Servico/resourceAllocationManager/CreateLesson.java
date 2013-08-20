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

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class CreateLesson {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Service
    public static void run(DiaSemana weekDay, Calendar begin, Calendar end, FrequencyType frequency,
            InfoRoomOccupationEditor infoRoomOccupation, InfoShift infoShift, YearMonthDay beginDate, YearMonthDay endDate)
            throws FenixServiceException {

        final ExecutionSemester executionSemester =
                AbstractDomainObject.fromExternalId(infoShift.getInfoDisciplinaExecucao().getInfoExecutionPeriod()
                        .getExternalId());

        final Shift shift = AbstractDomainObject.fromExternalId(infoShift.getExternalId());

        AllocatableSpace room = null;
        if (infoRoomOccupation != null) {
            room =
                    infoRoomOccupation.getInfoRoom() != null ? AllocatableSpace
                            .findAllocatableSpaceForEducationByName(infoRoomOccupation.getInfoRoom().getNome()) : null;
        }

        new Lesson(weekDay, begin, end, shift, frequency, executionSemester, beginDate, endDate, room);
    }
}