package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.LessonPlanning;

public class ExecutionCourseShiftTypesToEditLessonPlanningProvider extends ExecutionCourseShiftTypesToCreateLessonPlanningProvider {

    @Override
    public ExecutionCourse getExecutionCourse(Object source) {
	return ((LessonPlanning) source).getExecutionCourse();
    }
}
