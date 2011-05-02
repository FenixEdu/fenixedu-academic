/**
 * Nov 24, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.services;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.teacher.workTime.InstitutionWorkTimeDTO;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.teacher.InstitutionWorkTime;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class CreateTeacherInstitutionWorkTime extends FenixService {

    public void run(Teacher teacher, Integer executioPeriodID, InstitutionWorkTimeDTO institutionWorkTimeDTO, RoleType roleType) {

	ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(executioPeriodID);

	TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionSemester);
	if (teacherService == null) {
	    teacherService = new TeacherService(teacher, executionSemester);
	}
	new InstitutionWorkTime(teacherService, institutionWorkTimeDTO.getStartTime(), institutionWorkTimeDTO.getEndTime(),
		institutionWorkTimeDTO.getWeekDay(), roleType);
    }
}
