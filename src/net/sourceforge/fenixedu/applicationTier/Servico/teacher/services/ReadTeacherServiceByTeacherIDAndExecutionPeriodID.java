/**
 * Nov 24, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.services;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ReadTeacherServiceByTeacherIDAndExecutionPeriodID implements IService {

    public TeacherService run(Integer teacherID, Integer executionPeriodID) throws ExcepcaoPersistencia {

        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        Teacher teacher = (Teacher) persistentSupport.getIPersistentTeacher().readByOID(Teacher.class,
                teacherID);
        ExecutionPeriod executionPeriod = (ExecutionPeriod) persistentSupport
                .getIPersistentExecutionPeriod().readByOID(ExecutionPeriod.class, executionPeriodID);

        return teacher.getTeacherServiceByExecutionPeriod(executionPeriod);
    }

}
