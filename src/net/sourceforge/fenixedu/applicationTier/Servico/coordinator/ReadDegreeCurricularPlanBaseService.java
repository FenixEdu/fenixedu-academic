package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScopeWithCurricularCourseAndBranchAndSemesterAndYear;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseScope;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Fernanda Quitério 5/Dez/2003
 * 
 * @modified <a href="mailto:amam@mega.ist.utl.pt">Amin Amirali</a> 23/11/2004
 * @modified <a href="mailto:frnp@mega.ist.utl.pt">Francisco Paulo</a> 23/11/2004
 *  
 */
abstract public class ReadDegreeCurricularPlanBaseService implements IService {

    protected List readActiveCurricularCourseScopes(IDegreeCurricularPlan degreeCurricularPlan,
            ISuportePersistente sp) throws ExcepcaoPersistencia {

        return readActiveCurricularCourseScopes(degreeCurricularPlan.getIdInternal());

    }

    protected List readActiveCurricularCourseScopes(final Integer degreeCurricularPlanId)
            throws ExcepcaoPersistencia {
        List infoActiveScopes = null;
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentCurricularCourseScope persistentCurricularCourseScope = sp
                .getIPersistentCurricularCourseScope();

        if (degreeCurricularPlanId != null) {

            List allActiveScopes = persistentCurricularCourseScope
                    .readActiveCurricularCourseScopesByDegreeCurricularPlanId(degreeCurricularPlanId);

            if (allActiveScopes != null && allActiveScopes.size() > 0) {
                infoActiveScopes = new ArrayList();

                CollectionUtils.collect(allActiveScopes, new Transformer() {
                    public Object transform(Object input) {
                        ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) input;
                        //CLONER
                        return InfoCurricularCourseScopeWithCurricularCourseAndBranchAndSemesterAndYear
                                .newInfoFromDomain(curricularCourseScope);
                        //return
                        // Cloner.copyICurricularCourseScope2InfoCurricularCourseScope(
                        //	curricularCourseScope);
                    }
                }, infoActiveScopes);
            }
        }

        return infoActiveScopes;

    }

    //Read all curricular course scope of this year
    protected List readActiveCurricularCourseScopesInExecutionYear(
            IDegreeCurricularPlan degreeCurricularPlan, IExecutionYear executionYear,
            ISuportePersistente sp) throws FenixServiceException {
        List infoActiveScopes = null;
        try {

            IPersistentCurricularCourseScope persistentCurricularCourseScope = sp
                    .getIPersistentCurricularCourseScope();

            if (degreeCurricularPlan != null) {

                List allActiveScopes = persistentCurricularCourseScope
                        .readCurricularCourseScopesByDegreeCurricularPlanInExecutionYear(
                                degreeCurricularPlan, executionYear);
                if (allActiveScopes != null && allActiveScopes.size() > 0) {
                    infoActiveScopes = new ArrayList();

                    CollectionUtils.collect(allActiveScopes, new Transformer() {
                        public Object transform(Object input) {
                            ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) input;
                            return Cloner
                                    .copyICurricularCourseScope2InfoCurricularCourseScope(curricularCourseScope);
                        }
                    }, infoActiveScopes);
                }
            }
        } catch (ExcepcaoPersistencia e) {

            throw new FenixServiceException(e);
        }
        return infoActiveScopes;

    }

    //Read all curricular course scope of this year and curricular year
    protected List readActiveCurricularCourseScopesInCurricularYearAndExecutionPeriodAndExecutionDegree(
            IExecutionPeriod executionPeriod, IExecutionDegree executionDegree, Integer curricularYear,
            ISuportePersistente sp) throws FenixServiceException {
        List infoActiveScopes = null;
        try {

            IPersistentCurricularCourseScope persistentCurricularCourseScope = sp
                    .getIPersistentCurricularCourseScope();
            if (executionPeriod != null) {

                List allActiveExecution = persistentCurricularCourseScope
                        .readActiveCurricularCourseScopesByDegreeCurricularPlanAndCurricularYear(
                                executionDegree.getCurricularPlan(), curricularYear, executionPeriod
                                        .getExecutionYear());

                if (allActiveExecution != null && allActiveExecution.size() > 0) {
                    infoActiveScopes = new ArrayList();
                    CollectionUtils.collect(allActiveExecution, new Transformer() {
                        public Object transform(Object input) {
                            ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) input;
                            return Cloner
                                    .copyICurricularCourseScope2InfoCurricularCourseScope(curricularCourseScope);
                        }
                    }, infoActiveScopes);
                }
            }
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        return infoActiveScopes;

    }
}