package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseView;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;

public class ReadExecutionCoursesForCurrentAndPreviousPeriodByDegree extends Service {

    public Set<ExecutionCourseView> run(final Degree degree) {
	final ExecutionPeriod currentExecutionPeriod = ExecutionPeriod.readActualExecutionPeriod();
	final ExecutionPeriod previousExecutionPeriod = currentExecutionPeriod.getPreviousExecutionPeriod();

	final Set<ExecutionCourseView> result = new HashSet<ExecutionCourseView>();
	for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlans()) {
	    if (degreeCurricularPlan.isActive()) {
		degreeCurricularPlan.addExecutionCourses(result, currentExecutionPeriod, previousExecutionPeriod);
	    }
	}
	return result;
    }

}
