package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.ExecutionCourse;

public abstract class ExecutionCourseGroup extends DomainBackedGroup<ExecutionCourse> {

    public ExecutionCourseGroup(ExecutionCourse executionCourse) {
        super(executionCourse);
    }
    
    public ExecutionCourse getExecutionCourse() {
        return getObject();
    }
}
