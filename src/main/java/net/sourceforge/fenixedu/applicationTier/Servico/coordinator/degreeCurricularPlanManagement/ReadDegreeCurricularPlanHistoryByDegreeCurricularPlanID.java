package net.sourceforge.fenixedu.applicationTier.Servico.coordinator.degreeCurricularPlanManagement;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * 
 * @author <a href="mailto:amam@mega.ist.utl.pt">Amin Amirali</a>
 * @author <a href="mailto:frnp@mega.ist.utl.pt">Francisco Paulo</a>
 * 
 */
public class ReadDegreeCurricularPlanHistoryByDegreeCurricularPlanID {
    @Checked("RolePredicates.COORDINATOR_PREDICATE")
    @Service
    public static InfoDegreeCurricularPlan run(Integer degreeCurricularPlanID) throws FenixServiceException {

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = null;

        DegreeCurricularPlan degreeCurricularPlan = AbstractDomainObject.fromExternalId(degreeCurricularPlanID);

        if (degreeCurricularPlan != null) {
            List<CurricularCourse> allCurricularCourses = degreeCurricularPlan.getCurricularCourses();

            if (allCurricularCourses != null && !allCurricularCourses.isEmpty()) {
                infoDegreeCurricularPlan = createInfoDegreeCurricularPlan(degreeCurricularPlan, allCurricularCourses);
            }
        }

        return infoDegreeCurricularPlan;
    }

    private static InfoDegreeCurricularPlan createInfoDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan,
            List allCurricularCourses) {
        return InfoDegreeCurricularPlan.newInfoFromDomain(degreeCurricularPlan);
    }

}