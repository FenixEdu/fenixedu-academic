package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.ExecutionCourse;

public abstract class ExecutionCourseGroup extends DomainBackedGroup<ExecutionCourse> {

    private static final long serialVersionUID = 1L;
    
    public ExecutionCourseGroup(ExecutionCourse executionCourse) {
        super(executionCourse);
    }
    
    public ExecutionCourse getExecutionCourse() {
        return getObject();
    }
    
    public boolean hasExecutionCourse() {
	return getExecutionCourse() != null;
    }
}
