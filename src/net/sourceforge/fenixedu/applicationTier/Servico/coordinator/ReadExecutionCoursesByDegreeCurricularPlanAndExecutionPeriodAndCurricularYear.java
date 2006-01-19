/*
 * Created on Oct 20, 2005
 *  by jdnf
 */
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

public class ReadExecutionCoursesByDegreeCurricularPlanAndExecutionPeriodAndCurricularYear extends
        Service {

    public List<ExecutionCourse> run(Integer degreeCurricularPlanID, Integer executionPeriodID,
            Integer curricularYearID) throws ExcepcaoPersistencia, FenixServiceException {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();

        final ExecutionPeriod executionPeriod = (ExecutionPeriod) persistentSupport
                .getIPersistentExecutionPeriod().readByOID(ExecutionPeriod.class, executionPeriodID);
        if (executionPeriod == null) {
            throw new FenixServiceException("error.no.executionPeriod");
        }
        final DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) persistentSupport
                .getIPersistentDegreeCurricularPlan().readByOID(DegreeCurricularPlan.class,
                        degreeCurricularPlanID);
        if (degreeCurricularPlan == null) {
            throw new FenixServiceException("error.coordinator.noDegreeCurricularPlan");
        }
        CurricularYear curricularYear = null;
        if (curricularYearID != 0) {
            curricularYear = (CurricularYear) persistentSupport.getIPersistentCurricularYear()
                    .readByOID(CurricularYear.class, curricularYearID);
            if (curricularYear == null) {
                throw new FenixServiceException("error.no.curYear");
            }
        }

        final List<ExecutionCourse> result = new ArrayList();
        for (final ExecutionCourse executionCourse : executionPeriod.getAssociatedExecutionCourses()) {
            if (belongToDegreeCurricularPlanAndCurricularYear(executionCourse, degreeCurricularPlan,
                    curricularYear)) {
                result.add(executionCourse);
            }
        }
        return result;
    }

    private boolean belongToDegreeCurricularPlanAndCurricularYear(
            final ExecutionCourse executionCourse, final DegreeCurricularPlan degreeCurricularPlan,
            final CurricularYear curricularYear) {

        for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
            if (curricularCourse.getDegreeCurricularPlan() == degreeCurricularPlan) {
                for (final CurricularCourseScope curricularCourseScope : curricularCourse.getScopes()) {
                    if (curricularCourseScope.getCurricularSemester().getSemester().equals(
                            executionCourse.getExecutionPeriod().getSemester())
                            && (curricularYear == null || curricularCourseScope.getCurricularSemester()
                                    .getCurricularYear() == curricularYear)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
