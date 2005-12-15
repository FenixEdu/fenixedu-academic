/**
 * Nov 21, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ReadProfessorshipByTeacherIDAndExecutionCourseID implements IService {

    public IProfessorship run(final Integer teacherID, final Integer executionCourseID)
            throws ExcepcaoPersistencia {

        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        ITeacher teacher = (ITeacher) persistentSupport.getIPersistentTeacher().readByOID(Teacher.class,
                teacherID);
        IExecutionCourse executionCourse = (IExecutionCourse) persistentSupport
                .getIPersistentExecutionCourse().readByOID(ExecutionCourse.class, executionCourseID);

        return teacher.getProfessorshipByExecutionCourse(executionCourse);
    }
}
