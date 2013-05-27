package net.sourceforge.fenixedu.applicationTier.Servico.publico;


import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Luis Cruz
 */
public class ReadExecutionCourseOIDByCodeInLatestPeriod {

    @Service
    public static Integer run(String executionCourseCode) {
        final ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester();
        ExecutionCourse executionCourse = executionSemester.getExecutionCourseByInitials(executionCourseCode);

        if (executionCourse != null) {
            return executionCourse.getIdInternal();
        }

        return null;
    }

}