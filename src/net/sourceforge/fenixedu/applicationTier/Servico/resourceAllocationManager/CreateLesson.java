/* 
 *
 * Created on 2003/08/12
 */
package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.FenixService;
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

public class CreateLesson extends FenixService {

	@Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
	@Service
	public static void run(DiaSemana weekDay, Calendar begin, Calendar end, FrequencyType frequency,
			InfoRoomOccupationEditor infoRoomOccupation, InfoShift infoShift, YearMonthDay beginDate, YearMonthDay endDate)
			throws FenixServiceException {

		final ExecutionSemester executionSemester =
				rootDomainObject.readExecutionSemesterByOID(infoShift.getInfoDisciplinaExecucao().getInfoExecutionPeriod()
						.getIdInternal());

		final Shift shift = rootDomainObject.readShiftByOID(infoShift.getIdInternal());

		AllocatableSpace room = null;
		if (infoRoomOccupation != null) {
			room =
					infoRoomOccupation.getInfoRoom() != null ? AllocatableSpace
							.findAllocatableSpaceForEducationByName(infoRoomOccupation.getInfoRoom().getNome()) : null;
		}

		new Lesson(weekDay, begin, end, shift, frequency, executionSemester, beginDate, endDate, room);
	}
}