package net.sourceforge.fenixedu.domain.accessControl;

import relations.ExecutionCourseUserGroupHook;

public abstract class ExecutionCourseUserGroup extends ExecutionCourseUserGroup_Base {
    
    public ExecutionCourseUserGroup() {
        super();
    }
    
    public void delete()
    {
    	ExecutionCourseUserGroupHook.remove(this.getExecutionCourse(),this);
    	super.delete();
    }
    
}
