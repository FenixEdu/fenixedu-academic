package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseView;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;

public class ReadExecutionCoursesForCurrentAndPreviousPeriodByDegree extends Service {

    public List<ExecutionCourseView> run(final Degree degree) {
	final ExecutionPeriod currentExecutionPeriod = ExecutionPeriod.readActualExecutionPeriod();
	final ExecutionPeriod previousExecutionPeriod = currentExecutionPeriod.getPreviousExecutionPeriod();

	final Set<CurricularCourse> curricularCourses = new HashSet<CurricularCourse>();
	for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlans()) {
	    curricularCourses.addAll(degreeCurricularPlan.getCurricularCourses(currentExecutionPeriod));
	    curricularCourses.addAll(degreeCurricularPlan.getCurricularCourses(previousExecutionPeriod));
	}

	final List<ExecutionCourseView> result = new ArrayList<ExecutionCourseView>();
	for (final CurricularCourse curricularCourse : curricularCourses) {
	    buildExecutionCourseViews(currentExecutionPeriod, result, curricularCourse);
	    buildExecutionCourseViews(previousExecutionPeriod, result, curricularCourse);
	}

	return result;
    }

    private void buildExecutionCourseViews(final ExecutionPeriod executionPeriod, final List<ExecutionCourseView> result, final CurricularCourse curricularCourse) {
	final Set<String> processedExecutionCourses = new HashSet<String>();
	for (final ExecutionCourse executionCourse : curricularCourse.getExecutionCoursesByExecutionPeriod(executionPeriod)) {
	    for (final DegreeModuleScope degreeModuleScope : curricularCourse.getDegreeModuleScopes()) {
		final String key = generateExecutionCourseKey(executionCourse, degreeModuleScope);

		if (!processedExecutionCourses.contains(key)) {
		    processedExecutionCourses.add(key);

		    final ExecutionCourseView executionCourseView = new ExecutionCourseView(executionCourse);
		    executionCourseView.setCurricularYear(degreeModuleScope.getCurricularYear());
		    executionCourseView.setAnotation(degreeModuleScope.getAnotation());
		    executionCourseView.setDegreeCurricularPlanAnotation(curricularCourse.getDegreeCurricularPlan().getAnotation());

		    result.add(executionCourseView);
		}
	    }
	}
    }

    private String generateExecutionCourseKey(ExecutionCourse executionCourse, DegreeModuleScope degreeModuleScope) {
	StringBuilder key = new StringBuilder();

	key.append(degreeModuleScope.getCurricularYear());
	key.append('-');
	key.append(executionCourse.getIdInternal());

	return key.toString();
    }

}
