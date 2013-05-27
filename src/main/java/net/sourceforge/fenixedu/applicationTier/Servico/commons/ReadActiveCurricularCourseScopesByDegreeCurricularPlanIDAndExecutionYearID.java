package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ReadActiveCurricularCourseScopesByDegreeCurricularPlanIDAndExecutionYearID {

    @Service
    public static Set<DegreeModuleScope> run(Integer degreeCurricularPlanId, Integer executionYearID) {
        final ExecutionYear executionYear = RootDomainObject.getInstance().readExecutionYearByOID(executionYearID);
        final DegreeCurricularPlan degreeCurricularPlan = RootDomainObject.getInstance().readDegreeCurricularPlanByOID(degreeCurricularPlanId);
        final Set<DegreeModuleScope> degreeModuleScopes = degreeCurricularPlan.getDegreeModuleScopes();
        final Set<DegreeModuleScope> result =
                new TreeSet<DegreeModuleScope>(
                        DegreeModuleScope.COMPARATOR_BY_CURRICULAR_YEAR_AND_SEMESTER_AND_CURRICULAR_COURSE_NAME);
        for (final DegreeModuleScope degreeModuleScope : degreeModuleScopes) {
            if (degreeModuleScope.isActiveForExecutionYear(executionYear)) {
                result.add(degreeModuleScope);
            }
        }
        return result;
    }

}