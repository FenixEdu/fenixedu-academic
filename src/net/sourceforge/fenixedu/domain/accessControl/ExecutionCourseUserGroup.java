package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.ExecutionCourse;

public abstract class ExecutionCourseUserGroup extends DomainBackedGroup<ExecutionCourse> {

    public ExecutionCourseUserGroup(ExecutionCourse executionCourse) {
        super(executionCourse);
    }
    
    public ExecutionCourse getExecutionCourse() {
        return getObject();
    }
}
