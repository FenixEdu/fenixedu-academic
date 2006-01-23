package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;

/**
 * @author Tânia Pousão Create on 18/Dez/2003
 */
public class TeacherResponsibleByExecutionCourse extends Service {

    public Boolean run(String teacherUserName, Integer executionCourseCode, Integer curricularCourseCode)
            throws FenixServiceException, ExcepcaoPersistencia {
        boolean result = false;

        result = ExecutionCourseResponsibleTeacher(teacherUserName, executionCourseCode)
                && CurricularCourseNotBasic(curricularCourseCode);

        return new Boolean(result);
    }

    private boolean ExecutionCourseResponsibleTeacher(String teacherUserName, Integer executionCourseCode)
            throws ExcepcaoPersistencia {
        boolean result = false;

        IPersistentTeacher persistentTeacher = persistentSupport.getIPersistentTeacher();

        Teacher teacher = persistentTeacher.readTeacherByUsername(teacherUserName);
        Professorship responsibleFor = teacher.responsibleFor(executionCourseCode);
        if (responsibleFor == null) {
            result = false;
        } else {
            result = true;
        }
        return result;
    }

    private boolean CurricularCourseNotBasic(Integer curricularCourseCode) throws ExcepcaoPersistencia {
        boolean result = false;
        CurricularCourse curricularCourse = null;
        curricularCourse = (CurricularCourse) persistentObject.readByOID(
                CurricularCourse.class, curricularCourseCode);
        result = curricularCourse.getBasic().equals(Boolean.FALSE);

        return result;
    }
}