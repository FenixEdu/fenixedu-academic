/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ReadActiveCurricularCourseScopesByDegreeCurricularPlanIDAndExecutionYearID extends Service {

    public List run(Integer degreeCurricularPlanId, Integer executionYearID) throws ExcepcaoPersistencia {

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        ExecutionYear executionYear = (ExecutionYear) sp.getIPersistentExecutionYear().readByOID(
                ExecutionYear.class, executionYearID);

        return sp.getIPersistentCurricularCourseScope()
                .readCurricularCourseScopesByDegreeCurricularPlanInExecutionYear(degreeCurricularPlanId,
                        executionYear.getBeginDate(), executionYear.getEndDate());

    }

}
