package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;

/**
 * @author Luis Cruz
 */
public class ReadExecutionCourseOIDByCodeInLatestPeriod extends Service {

    public Integer run(String executionCourseCode) throws ExcepcaoPersistencia {
        IPersistentExecutionCourse executionCourseDAO = persistentSupport.getIPersistentExecutionCourse();
        IPersistentExecutionPeriod executionPeriodDAO = persistentSupport.getIPersistentExecutionPeriod();

        ExecutionPeriod executionPeriod = executionPeriodDAO.readActualExecutionPeriod();
        ExecutionCourse executionCourse = executionCourseDAO
                .readByExecutionCourseInitialsAndExecutionPeriodId(executionCourseCode, executionPeriod.getIdInternal());

        if (executionCourse != null) {
            return executionCourse.getIdInternal();
        }

        return null;
    }

}