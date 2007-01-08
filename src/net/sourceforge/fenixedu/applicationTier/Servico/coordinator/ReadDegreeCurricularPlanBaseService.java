package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Fernanda Quitério 5/Dez/2003
 * 
 * @modified <a href="mailto:amam@mega.ist.utl.pt">Amin Amirali</a> 23/11/2004
 * @modified <a href="mailto:frnp@mega.ist.utl.pt">Francisco Paulo</a>
 *           23/11/2004
 * 
 */
abstract public class ReadDegreeCurricularPlanBaseService extends Service {

    protected List<InfoCurricularCourseScope> readActiveCurricularCourseScopes(final Integer degreeCurricularPlanId)
            throws ExcepcaoPersistencia {
        List<InfoCurricularCourseScope> infoActiveScopes = null;

        if (degreeCurricularPlanId != null) {

            DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanId);
            List<CurricularCourseScope> allActiveScopes =  degreeCurricularPlan.getActiveCurricularCourseScopes();

            if (allActiveScopes != null && allActiveScopes.size() > 0) {
                infoActiveScopes = new ArrayList<InfoCurricularCourseScope>();

                CollectionUtils.collect(allActiveScopes, new Transformer() {
                    public Object transform(Object input) {
                        CurricularCourseScope curricularCourseScope = (CurricularCourseScope) input;

                        return InfoCurricularCourseScope
                                .newInfoFromDomain(curricularCourseScope);
                    }
                }, infoActiveScopes);
            }
        }

        return infoActiveScopes;

    }

    // Read all curricular course scope of this year
    protected List<InfoCurricularCourseScope> readActiveCurricularCourseScopesInExecutionYear(
	    final DegreeCurricularPlan degreeCurricularPlan, final ExecutionYear executionYear) {
	final List<InfoCurricularCourseScope> result = new ArrayList<InfoCurricularCourseScope>();

	if (degreeCurricularPlan != null) {
	    for (final CurricularCourseScope curricularCourseScope : degreeCurricularPlan
		    .findCurricularCourseScopesIntersectingPeriod(executionYear.getBeginDate(),
			    executionYear.getEndDate())) {
		result.add(InfoCurricularCourseScope.newInfoFromDomain(curricularCourseScope));
	    }
	}

	return result;
    }

    // Read all curricular course scope of this year and curricular year
    protected List<InfoCurricularCourseScope> readActiveCurricularCourseScopesInCurricularYearAndExecutionPeriodAndExecutionDegree(
	    final ExecutionPeriod executionPeriod, final ExecutionDegree executionDegree,
	    final Integer curricularYear) {
	final List<InfoCurricularCourseScope> result = new ArrayList<InfoCurricularCourseScope>();

	if (executionPeriod != null) {
	    final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
	    final ExecutionYear executionYear = executionPeriod.getExecutionYear();
	    final Set<CurricularCourseScope> curricularCourseScopes = degreeCurricularPlan
		    .findCurricularCourseScopesIntersectingPeriod(executionYear.getBeginDate(),
			    executionYear.getEndDate());

	    for (final CurricularCourseScope curricularCourseScope : curricularCourseScopes) {
		if (curricularCourseScope.getCurricularSemester().getCurricularYear().getYear().equals(
			curricularYear)) {
		    result.add(InfoCurricularCourseScope.newInfoFromDomain(curricularCourseScope));
		}
	    }
	}

	return result;
    }

}
