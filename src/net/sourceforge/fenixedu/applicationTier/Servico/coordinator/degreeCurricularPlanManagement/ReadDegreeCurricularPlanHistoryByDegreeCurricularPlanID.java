package net.sourceforge.fenixedu.applicationTier.Servico.coordinator.degreeCurricularPlanManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScopeWithCurricularCourseAndBranchAndSemesterAndYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * 
 * @author <a href="mailto:amam@mega.ist.utl.pt">Amin Amirali</a>
 * @author <a href="mailto:frnp@mega.ist.utl.pt">Francisco Paulo</a>
 * 
 */
public class ReadDegreeCurricularPlanHistoryByDegreeCurricularPlanID extends Service {
    public InfoDegreeCurricularPlan run(Integer degreeCurricularPlanID) throws FenixServiceException, ExcepcaoPersistencia {

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = null;

        DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) persistentObject.readByOID(DegreeCurricularPlan.class,
                        degreeCurricularPlanID);

        if (degreeCurricularPlan != null) {
            List allCurricularCourses = degreeCurricularPlan.getCurricularCourses();

            if (allCurricularCourses != null && !allCurricularCourses.isEmpty()) {
                infoDegreeCurricularPlan = createInfoDegreeCurricularPlan(degreeCurricularPlan,
                        allCurricularCourses);
            }
        }

        return infoDegreeCurricularPlan;
    }

    private InfoDegreeCurricularPlan createInfoDegreeCurricularPlan(
            DegreeCurricularPlan degreeCurricularPlan, List allCurricularCourses) {

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = InfoDegreeCurricularPlan
                .newInfoFromDomain(degreeCurricularPlan);

        List allInfoCurricularCourses = new ArrayList();

        CollectionUtils.collect(allCurricularCourses, new Transformer() {
            public Object transform(Object arg0) {
                CurricularCourse curricularCourse = (CurricularCourse) arg0;
                List allInfoCurricularCourseScopes = new ArrayList();
                CollectionUtils.collect(curricularCourse.getScopes(), new Transformer() {
                    public Object transform(Object arg0) {
                        CurricularCourseScope curricularCourseScope = (CurricularCourseScope) arg0;

                        return InfoCurricularCourseScopeWithCurricularCourseAndBranchAndSemesterAndYear
                                .newInfoFromDomain(curricularCourseScope);
                    }
                }, allInfoCurricularCourseScopes);

                InfoCurricularCourse infoCurricularCourse = InfoCurricularCourse
                        .newInfoFromDomain(curricularCourse);
                infoCurricularCourse.setInfoScopes(allInfoCurricularCourseScopes);
                return infoCurricularCourse;
            }
        }, allInfoCurricularCourses);

        infoDegreeCurricularPlan.setCurricularCourses(allInfoCurricularCourses);
        return infoDegreeCurricularPlan;
    }
}
