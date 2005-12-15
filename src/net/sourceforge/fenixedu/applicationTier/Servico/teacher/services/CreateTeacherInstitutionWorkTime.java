/**
 * Nov 24, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.services;

import net.sourceforge.fenixedu.dataTransferObject.teacher.workTime.InstitutionWorkTimeDTO;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.ITeacherService;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class CreateTeacherInstitutionWorkTime implements IService {

    public void run(Integer teacherID, Integer executioPeriodID,
            InstitutionWorkTimeDTO institutionWorkTimeDTO) throws ExcepcaoPersistencia {

        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        ITeacher teacher = (ITeacher) persistentSupport.getIPersistentTeacher().readByOID(Teacher.class,
                teacherID);
        IExecutionPeriod executionPeriod = (IExecutionPeriod) persistentSupport
                .getIPersistentExecutionPeriod().readByOID(ExecutionPeriod.class, executioPeriodID);

        ITeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionPeriod);
        if (teacherService == null) {
            teacherService = DomainFactory.makeTeacherService(teacher, executionPeriod);
        }
        DomainFactory.makeInstitutionWorkTime(teacherService, institutionWorkTimeDTO.getStartTime(),
                institutionWorkTimeDTO.getEndTime(), institutionWorkTimeDTO.getWeekDay());
    }

}
