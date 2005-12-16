/*
 * Created on Oct 20, 2005
 *  by jdnf
 */
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.ICurricularYear;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadExecutionCoursesByDegreeCurricularPlanAndExecutionPeriodAndCurricularYear implements
        IService {

    public List<IExecutionCourse> run(Integer degreeCurricularPlanID, Integer executionPeriodID,
            Integer curricularYearID) throws ExcepcaoPersistencia, FenixServiceException {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();

        final IExecutionPeriod executionPeriod = (IExecutionPeriod) persistentSupport
                .getIPersistentExecutionPeriod().readByOID(ExecutionPeriod.class, executionPeriodID);
        if (executionPeriod == null) {
            throw new FenixServiceException("error.no.executionPeriod");
        }
        final IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) persistentSupport
                .getIPersistentDegreeCurricularPlan().readByOID(DegreeCurricularPlan.class,
                        degreeCurricularPlanID);
        if (degreeCurricularPlan == null) {
            throw new FenixServiceException("error.coordinator.noDegreeCurricularPlan");
        }
        ICurricularYear curricularYear = null;
        if (curricularYearID != 0) {
            curricularYear = (ICurricularYear) persistentSupport.getIPersistentCurricularYear()
                    .readByOID(CurricularYear.class, curricularYearID);
            if (curricularYear == null) {
                throw new FenixServiceException("error.no.curYear");
            }
        }

        final List<IExecutionCourse> result = new ArrayList();
        for (final IExecutionCourse executionCourse : executionPeriod.getAssociatedExecutionCourses()) {
            if (belongToDegreeCurricularPlanAndCurricularYear(executionCourse, degreeCurricularPlan,
                    curricularYear)) {
                result.add(executionCourse);
            }
        }
        return result;
    }

    private boolean belongToDegreeCurricularPlanAndCurricularYear(
            final IExecutionCourse executionCourse, final IDegreeCurricularPlan degreeCurricularPlan,
            final ICurricularYear curricularYear) {

        for (final ICurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
            if (curricularCourse.getDegreeCurricularPlan() == degreeCurricularPlan) {
                for (final ICurricularCourseScope curricularCourseScope : curricularCourse.getScopes()) {
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
