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
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;

/**
 * @author nmgo
 * @author lmre
 */
public class ReadActiveCurricularCourseScopeByDegreeCurricularPlanAndExecutionYear extends
        ReadDegreeCurricularPlanBaseService {

    public InfoDegreeCurricularPlanWithCurricularCourseScopes run(Integer degreeCurricularPlanID,
            Integer executioYearID) throws FenixServiceException {
        try {
            ISuportePersistente suportePersistente = PersistenceSupportFactory.getDefaultPersistenceSupport();
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