package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseView;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import pt.ist.fenixWebFramework.services.Service;

public class ReadExecutionCoursesForCurrentAndPreviousPeriodByDegree extends FenixService {

    @Service
    public static Set<ExecutionCourseView> run(final Degree degree) {
	final ExecutionSemester currentExecutionPeriod = ExecutionSemester.readActualExecutionSemester();
	final ExecutionSemester previousExecutionPeriod = currentExecutionPeriod.getPreviousExecutionPeriod();

	final Set<ExecutionCourseView> result = new HashSet<ExecutionCourseView>();
	for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlans()) {
	    if (degreeCurricularPlan.isActive()) {
		degreeCurricularPlan.addExecutionCourses(result, currentExecutionPeriod, previousExecutionPeriod);
	    }
	}
	return result;
    }

}