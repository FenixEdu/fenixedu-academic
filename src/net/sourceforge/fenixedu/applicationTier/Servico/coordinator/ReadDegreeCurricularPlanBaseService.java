package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScopeWithCurricularCourseAndDegreeAndBranchAndSemesterAndYear;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseScope;

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

    protected List readActiveCurricularCourseScopes(final Integer degreeCurricularPlanId)
            throws ExcepcaoPersistencia {
        List infoActiveScopes = null;
        IPersistentCurricularCourseScope persistentCurricularCourseScope = persistentSupport
                .getIPersistentCurricularCourseScope();

        if (degreeCurricularPlanId != null) {

            List allActiveScopes = persistentCurricularCourseScope
                    .readActiveCurricularCourseScopesByDegreeCurricularPlanId(degreeCurricularPlanId);

            if (allActiveScopes != null && allActiveScopes.size() > 0) {
                infoActiveScopes = new ArrayList();

                CollectionUtils.collect(allActiveScopes, new Transformer() {
                    public Object transform(Object input) {
                        CurricularCourseScope curricularCourseScope = (CurricularCourseScope) input;

                        return InfoCurricularCourseScopeWithCurricularCourseAndDegreeAndBranchAndSemesterAndYear
                                .newInfoFromDomain(curricularCourseScope);
                    }
                }, infoActiveScopes);
            }
        }

        return infoActiveScopes;

    }

    // Read all curricular course scope of this year
    protected List readActiveCurricularCourseScopesInExecutionYear(Integer degreeCurricularPlanId,
            ExecutionYear executionYear) throws FenixServiceException, ExcepcaoPersistencia {
        List infoActiveScopes = null;

        IPersistentCurricularCourseScope persistentCurricularCourseScope = persistentSupport
                .getIPersistentCurricularCourseScope();

        if (degreeCurricularPlanId != null) {
            List allActiveScopes = persistentCurricularCourseScope
                    .readCurricularCourseScopesByDegreeCurricularPlanInExecutionYear(
                            degreeCurricularPlanId, executionYear.getBeginDate(), executionYear
                                    .getEndDate());
            if (allActiveScopes != null && allActiveScopes.size() > 0) {
                infoActiveScopes = new ArrayList();

                CollectionUtils.collect(allActiveScopes, new Transformer() {
                    public Object transform(Object input) {
                        CurricularCourseScope curricularCourseScope = (CurricularCourseScope) input;
                        return InfoCurricularCourseScopeWithCurricularCourseAndDegreeAndBranchAndSemesterAndYear
                                .newInfoFromDomain(curricularCourseScope);
                    }

                }, infoActiveScopes);
            }
        }

        return infoActiveScopes;

    }

    // Read all curricular course scope of this year and curricular year
    protected List readActiveCurricularCourseScopesInCurricularYearAndExecutionPeriodAndExecutionDegree(
            ExecutionPeriod executionPeriod, ExecutionDegree executionDegree, Integer curricularYear)
            throws FenixServiceException, ExcepcaoPersistencia {
        List infoActiveScopes = null;

        IPersistentCurricularCourseScope persistentCurricularCourseScope = persistentSupport
                .getIPersistentCurricularCourseScope();
        if (executionPeriod != null) {

            List allActiveExecution = persistentCurricularCourseScope
                    .readActiveCurricularCourseScopesByDegreeCurricularPlanAndCurricularYear(
                            executionDegree.getDegreeCurricularPlan().getIdInternal(), curricularYear,
                            executionPeriod.getExecutionYear().getBeginDate(), executionPeriod
                                    .getExecutionYear().getEndDate());

            if (allActiveExecution != null && allActiveExecution.size() > 0) {
                infoActiveScopes = new ArrayList();
                CollectionUtils.collect(allActiveExecution, new Transformer() {
                    public Object transform(Object input) {
                        CurricularCourseScope curricularCourseScope = (CurricularCourseScope) input;
                        return InfoCurricularCourseScopeWithCurricularCourseAndDegreeAndBranchAndSemesterAndYear
                                .newInfoFromDomain(curricularCourseScope);
                    }
                }, infoActiveScopes);
            }
        }

        return infoActiveScopes;

    }

}
