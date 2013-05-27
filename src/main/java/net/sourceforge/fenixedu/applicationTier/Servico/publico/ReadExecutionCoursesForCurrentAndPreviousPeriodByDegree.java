package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseView;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.util.PeriodState;
import pt.ist.fenixWebFramework.services.Service;

public class ReadExecutionCoursesForCurrentAndPreviousPeriodByDegree {

    @Service
    public static Set<ExecutionCourseView> run(final Degree degree) {
        final ExecutionSemester currentExecutionPeriod = ExecutionSemester.readActualExecutionSemester();
        final ExecutionSemester nextExecutionSemester = currentExecutionPeriod.getNextExecutionPeriod();
        final ExecutionSemester previousExecutionPeriod;
        if (nextExecutionSemester != null && nextExecutionSemester.getState().equals(PeriodState.OPEN)) {
            previousExecutionPeriod = nextExecutionSemester;
        } else {
            previousExecutionPeriod = currentExecutionPeriod.getPreviousExecutionPeriod();
        }

        final Set<ExecutionCourseView> result = new HashSet<ExecutionCourseView>();
        for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlans()) {
            if (degreeCurricularPlan.isActive()) {
                degreeCurricularPlan.addExecutionCourses(result, currentExecutionPeriod, previousExecutionPeriod);
            }
        }
        return result;
    }

}