package ServidorAplicacao.Servico.coordinator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoCurricularCourseScopeWithCurricularCourseAndBranchAndSemesterAndYear;
import DataBeans.util.Cloner;
import Dominio.ICurricularCourseScope;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.ISuportePersistente;

/**
 * @author Fernanda Quitério 5/Dez/2003
 *  
 */
abstract public class ReadDegreeCurricularPlanBaseService implements IService {

    protected List readActiveCurricularCourseScopes(IDegreeCurricularPlan degreeCurricularPlan,
            ISuportePersistente sp) throws FenixServiceException {
        List infoActiveScopes = null;
        try {

            IPersistentCurricularCourseScope persistentCurricularCourseScope = sp
                    .getIPersistentCurricularCourseScope();

            if (degreeCurricularPlan != null) {

                List allActiveScopes = persistentCurricularCourseScope
                        .readActiveCurricularCourseScopesByDegreeCurricularPlan(degreeCurricularPlan);

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
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
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
            IExecutionPeriod executionPeriod, ICursoExecucao executionDegree, Integer curricularYear,
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