package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Teacher;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Tânia Pousão Create on 18/Dez/2003
 */
public class TeacherResponsibleByExecutionCourse {

    @Atomic
    public static Boolean run(String teacherUserName, String executionCourseCode, String curricularCourseCode)
            throws FenixServiceException {
        return Boolean.valueOf(ExecutionCourseResponsibleTeacher(teacherUserName, executionCourseCode)
                && CurricularCourseNotBasic(curricularCourseCode));
    }

    private static boolean ExecutionCourseResponsibleTeacher(String teacherUserName, String executionCourseCode) {
        final Teacher teacher = Teacher.readTeacherByUsername(teacherUserName);
        final ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseCode);
        return teacher.isResponsibleFor(executionCourse) != null;
    }

    private static boolean CurricularCourseNotBasic(String curricularCourseCode) {
        CurricularCourse curricularCourse = (CurricularCourse) FenixFramework.getDomainObject(curricularCourseCode);
        return curricularCourse.getBasic() == Boolean.FALSE;
    }

}