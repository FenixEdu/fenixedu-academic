package net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors;

import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;

public abstract class AbstractExecutionCourseContext extends ProcessingContext implements ExecutionCourseContext {

    public AbstractExecutionCourseContext(ProcessingContext parent) {
        super(parent);
    }

    public ExecutionCourse getExecutionCourse() {
        List<ExecutionCourse> executionCourses = getExecutionCourses();
        
        if (executionCourses.isEmpty()) {
            return null;
        }
        else {
            return executionCourses.get(executionCourses.size() - 1);
        }
    }

}
