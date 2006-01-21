package net.sourceforge.fenixedu.domain.cms.website;

public class ExecutionCourseWebsite extends ExecutionCourseWebsite_Base {
    
    public ExecutionCourseWebsite() {
        super();
    }
    
    @Override
    public void delete()
    {
    	ExecutionCourseWebsiteRelation.remove(this,this.getExecutionCourse());
    	super.delete();
    }
    
}
