/*
 * Created on Oct 7, 2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons.curriculumHistoric;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.coordinator.ReadDegreeCurricularPlanBaseService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlanWithCurricularCourseScopes;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;

/**
 * @author nmgo
 * @author lmre
 */
public class ReadActiveCurricularCourseScopeByDegreeCurricularPlanAndExecutionYear extends
        ReadDegreeCurricularPlanBaseService {

    public InfoDegreeCurricularPlanWithCurricularCourseScopes run(Integer degreeCurricularPlanID,
            Integer executioYearID) throws FenixServiceException, ExcepcaoPersistencia {
        IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = persistentSupport.getIPersistentDegreeCurricularPlan();
        DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) persistentDegreeCurricularPlan
                .readByOID(DegreeCurricularPlan.class, degreeCurricularPlanID);

        IPersistentExecutionYear persistentExecutionYear = persistentSupport.getIPersistentExecutionYear();
        ExecutionYear executionYear = (ExecutionYear) persistentExecutionYear.readByOID(
                ExecutionYear.class, executioYearID);
        List scopes = super.readActiveCurricularCourseScopesInExecutionYear(degreeCurricularPlan
                .getIdInternal(), executionYear);
        InfoDegreeCurricularPlan infoDegreeCurricularPlan = InfoDegreeCurricularPlan
                .newInfoFromDomain(degreeCurricularPlan);
        InfoDegreeCurricularPlanWithCurricularCourseScopes curricularPlanWithCurricularCourseScopes = new InfoDegreeCurricularPlanWithCurricularCourseScopes();
        curricularPlanWithCurricularCourseScopes.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
        curricularPlanWithCurricularCourseScopes.setScopes(scopes);
        return curricularPlanWithCurricularCourseScopes;
    }

}
