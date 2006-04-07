package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ReadActiveCurricularCourseScopesByDegreeCurricularPlanIDAndExecutionYearID extends Service {

    public Set<CurricularCourseScope> run(Integer degreeCurricularPlanId, Integer executionYearID) throws ExcepcaoPersistencia {
        final ExecutionYear executionYear = rootDomainObject.readExecutionYearByOID(executionYearID);
        final DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanId);
        return degreeCurricularPlan.findCurricularCourseScopesIntersectingPeriod(executionYear.getBeginDate(), executionYear.getEndDate());
    }

}
