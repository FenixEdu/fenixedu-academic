/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ReadActiveCurricularCourseScopesByDegreeCurricularPlanIDAndExecutionYearID extends Service {

    public List run(Integer degreeCurricularPlanId, Integer executionYearID) throws ExcepcaoPersistencia {
        ExecutionYear executionYear = (ExecutionYear) persistentSupport.getIPersistentExecutionYear().readByOID(
                ExecutionYear.class, executionYearID);

        return persistentSupport.getIPersistentCurricularCourseScope()
                .readCurricularCourseScopesByDegreeCurricularPlanInExecutionYear(degreeCurricularPlanId,
                        executionYear.getBeginDate(), executionYear.getEndDate());

    }

}
