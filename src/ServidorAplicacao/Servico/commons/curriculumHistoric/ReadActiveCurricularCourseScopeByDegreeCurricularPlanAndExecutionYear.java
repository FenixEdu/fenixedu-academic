/*
 * Created on Oct 7, 2004
 */
package ServidorAplicacao.Servico.commons.curriculumHistoric;

import java.util.List;

import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoDegreeCurricularPlanWithCurricularCourseScopes;
import Dominio.DegreeCurricularPlan;
import Dominio.ExecutionYear;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionYear;
import ServidorAplicacao.Servico.coordinator.ReadDegreeCurricularPlanBaseService;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author nmgo
 * @author lmre
 */
public class ReadActiveCurricularCourseScopeByDegreeCurricularPlanAndExecutionYear extends
        ReadDegreeCurricularPlanBaseService {

    public InfoDegreeCurricularPlanWithCurricularCourseScopes run(Integer degreeCurricularPlanID,
            Integer executioYearID) throws FenixServiceException {
        try {
            ISuportePersistente suportePersistente = SuportePersistenteOJB.getInstance();
            IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = suportePersistente
                    .getIPersistentDegreeCurricularPlan();
            IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) persistentDegreeCurricularPlan
                    .readByOID(DegreeCurricularPlan.class, degreeCurricularPlanID);

            IPersistentExecutionYear persistentExecutionYear = suportePersistente
                    .getIPersistentExecutionYear();
            IExecutionYear executionYear = (IExecutionYear) persistentExecutionYear.readByOID(
                    ExecutionYear.class, executioYearID);
            List scopes = super.readActiveCurricularCourseScopesInExecutionYear(degreeCurricularPlan,
                    executionYear, suportePersistente);
            InfoDegreeCurricularPlan infoDegreeCurricularPlan = InfoDegreeCurricularPlan
                    .newInfoFromDomain(degreeCurricularPlan);
            InfoDegreeCurricularPlanWithCurricularCourseScopes curricularPlanWithCurricularCourseScopes = new InfoDegreeCurricularPlanWithCurricularCourseScopes();
            curricularPlanWithCurricularCourseScopes
                    .setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
            curricularPlanWithCurricularCourseScopes.setScopes(scopes);
            return curricularPlanWithCurricularCourseScopes;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}