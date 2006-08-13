package net.sourceforge.fenixedu.applicationTier.Servico.coordinator.degreeCurricularPlanManagement;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * 
 * @author <a href="mailto:amam@mega.ist.utl.pt">Amin Amirali</a>
 * @author <a href="mailto:frnp@mega.ist.utl.pt">Francisco Paulo</a>
 * 
 */
public class ReadDegreeCurricularPlanHistoryByDegreeCurricularPlanID extends Service {
    public InfoDegreeCurricularPlan run(Integer degreeCurricularPlanID) throws FenixServiceException, ExcepcaoPersistencia {

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = null;

        DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(
                        degreeCurricularPlanID);

        if (degreeCurricularPlan != null) {
            List<CurricularCourse> allCurricularCourses = degreeCurricularPlan.getCurricularCourses(); 

            if (allCurricularCourses != null && !allCurricularCourses.isEmpty()) {
                infoDegreeCurricularPlan = createInfoDegreeCurricularPlan(degreeCurricularPlan,
                        allCurricularCourses);
            }
        }

        return infoDegreeCurricularPlan;
    }

    private InfoDegreeCurricularPlan createInfoDegreeCurricularPlan(
            DegreeCurricularPlan degreeCurricularPlan, List allCurricularCourses) {
        return InfoDegreeCurricularPlan.newInfoFromDomain(degreeCurricularPlan);
    }

}
