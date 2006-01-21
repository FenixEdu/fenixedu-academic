package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DefineExamComment extends Service {

    public void run(String executionCourseInitials, Integer executionPeriodId, String comment)
            throws FenixServiceException, ExcepcaoPersistencia {
        final ExecutionCourse executionCourse = persistentSupport.getIPersistentExecutionCourse()
                .readByExecutionCourseInitialsAndExecutionPeriodId(executionCourseInitials,
                        executionPeriodId);
        if (executionCourse == null) {
            throw new FenixServiceException("error.noExecutionCourse");
        }
        executionCourse.setComment(comment);
    }

}
