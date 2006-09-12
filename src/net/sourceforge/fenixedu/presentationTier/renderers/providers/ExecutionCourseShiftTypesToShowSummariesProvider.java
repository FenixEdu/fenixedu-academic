package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import net.sourceforge.fenixedu.dataTransferObject.ShowSummariesBean;
import net.sourceforge.fenixedu.domain.ExecutionCourse;

public class ExecutionCourseShiftTypesToShowSummariesProvider extends ExecutionCourseShiftTypesToCreateLessonPlanningProvider{

    @Override
    public ExecutionCourse getExecutionCourse(Object source) {
       return ((ShowSummariesBean)source).getExecutionCourse();
    }
}
