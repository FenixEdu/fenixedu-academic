/**
 * Nov 24, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.services;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.teacher.workTime.InstitutionWorkTimeDTO;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.teacher.InstitutionWorkTime;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class CreateTeacherInstitutionWorkTime extends Service {

    public void run(Integer teacherID, Integer executioPeriodID,
            InstitutionWorkTimeDTO institutionWorkTimeDTO, RoleType roleType) throws ExcepcaoPersistencia {

        Teacher teacher = rootDomainObject.readTeacherByOID(teacherID);
        ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(executioPeriodID);

        TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionPeriod);
        if (teacherService == null) {
            teacherService = new TeacherService(teacher, executionPeriod);
        }
        new InstitutionWorkTime(teacherService, institutionWorkTimeDTO.getStartTime(),
                institutionWorkTimeDTO.getEndTime(), institutionWorkTimeDTO.getWeekDay(), roleType);
    }
}
