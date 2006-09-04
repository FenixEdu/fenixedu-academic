package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Tânia Pousão Create on 18/Dez/2003
 */
public class TeacherResponsibleByExecutionCourse extends Service {

    public Boolean run(String teacherUserName, Integer executionCourseCode, Integer curricularCourseCode)
            throws FenixServiceException, ExcepcaoPersistencia {
        return Boolean.valueOf(
        		ExecutionCourseResponsibleTeacher(teacherUserName, executionCourseCode) && CurricularCourseNotBasic(curricularCourseCode));
    }

    private boolean ExecutionCourseResponsibleTeacher(String teacherUserName, Integer executionCourseCode)
            throws ExcepcaoPersistencia {
        final Teacher teacher = Teacher.readTeacherByUsername(teacherUserName);
        final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseCode);
        return teacher.isResponsibleFor(executionCourse) != null;
    }

    private boolean CurricularCourseNotBasic(Integer curricularCourseCode) throws ExcepcaoPersistencia {
        CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(curricularCourseCode);
        return curricularCourse.getBasic() == Boolean.FALSE;
    }

}