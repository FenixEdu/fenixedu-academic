package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Luis Cruz
 */
public class ReadExecutionCourseOIDByCodeInLatestPeriod extends Service {

    public Integer run(String executionCourseCode) throws ExcepcaoPersistencia {
        final ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester();
        ExecutionCourse executionCourse = executionSemester.getExecutionCourseByInitials(executionCourseCode);

        if (executionCourse != null) {
            return executionCourse.getIdInternal();
        }

        return null;
    }

}