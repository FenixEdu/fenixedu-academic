package net.sourceforge.fenixedu.applicationTier.Servico.teacher;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Tânia Pousão Create on 18/Dez/2003
 */
public class TeacherResponsibleByExecutionCourse {

    @Service
    public static Boolean run(String teacherUserName, Integer executionCourseCode, Integer curricularCourseCode)
            throws FenixServiceException {
        return Boolean.valueOf(ExecutionCourseResponsibleTeacher(teacherUserName, executionCourseCode)
                && CurricularCourseNotBasic(curricularCourseCode));
    }

    private static boolean ExecutionCourseResponsibleTeacher(String teacherUserName, Integer executionCourseCode) {
        final Teacher teacher = Teacher.readTeacherByUsername(teacherUserName);
        final ExecutionCourse executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID(executionCourseCode);
        return teacher.isResponsibleFor(executionCourse) != null;
    }

    private static boolean CurricularCourseNotBasic(Integer curricularCourseCode) {
        CurricularCourse curricularCourse = (CurricularCourse) RootDomainObject.getInstance().readDegreeModuleByOID(curricularCourseCode);
        return curricularCourse.getBasic() == Boolean.FALSE;
    }

}