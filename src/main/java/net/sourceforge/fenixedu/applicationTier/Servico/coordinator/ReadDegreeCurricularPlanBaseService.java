package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author Fernanda Quitério 5/Dez/2003
 * 
 * @modified <a href="mailto:amam@mega.ist.utl.pt">Amin Amirali</a> 23/11/2004
 * @modified <a href="mailto:frnp@mega.ist.utl.pt">Francisco Paulo</a>
 *           23/11/2004
 * 
 */
abstract public class ReadDegreeCurricularPlanBaseService {

    protected List<InfoCurricularCourseScope> readActiveCurricularCourseScopes(final String degreeCurricularPlanId) {
        List<InfoCurricularCourseScope> infoActiveScopes = null;

        if (degreeCurricularPlanId != null) {

            DegreeCurricularPlan degreeCurricularPlan = AbstractDomainObject.fromExternalId(degreeCurricularPlanId);
            List<CurricularCourseScope> allActiveScopes = degreeCurricularPlan.getActiveCurricularCourseScopes();

            if (allActiveScopes != null && allActiveScopes.size() > 0) {
                infoActiveScopes = new ArrayList<InfoCurricularCourseScope>();

                CollectionUtils.collect(allActiveScopes, new Transformer() {
                    @Override
                    public Object transform(Object input) {
                        CurricularCourseScope curricularCourseScope = (CurricularCourseScope) input;

                        return InfoCurricularCourseScope.newInfoFromDomain(curricularCourseScope);
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
                    .findCurricularCourseScopesIntersectingPeriod(executionYear.getBeginDate(), executionYear.getEndDate())) {
                result.add(InfoCurricularCourseScope.newInfoFromDomain(curricularCourseScope));
            }
        }

        return result;
    }

    // Read all curricular course scope of this year and curricular year
    protected List<InfoCurricularCourseScope> readActiveCurricularCourseScopesInCurricularYearAndExecutionPeriodAndExecutionDegree(
            final ExecutionSemester executionSemester, final ExecutionDegree executionDegree, final Integer curricularYear) {
        final List<InfoCurricularCourseScope> result = new ArrayList<InfoCurricularCourseScope>();

        if (executionSemester != null) {
            final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
            final ExecutionYear executionYear = executionSemester.getExecutionYear();
            final Set<CurricularCourseScope> curricularCourseScopes =
                    degreeCurricularPlan.findCurricularCourseScopesIntersectingPeriod(executionYear.getBeginDate(),
                            executionYear.getEndDate());

            for (final CurricularCourseScope curricularCourseScope : curricularCourseScopes) {
                if (curricularCourseScope.getCurricularSemester().getCurricularYear().getYear().equals(curricularYear)) {
                    result.add(InfoCurricularCourseScope.newInfoFromDomain(curricularCourseScope));
                }
            }
        }

        return result;
    }

}
