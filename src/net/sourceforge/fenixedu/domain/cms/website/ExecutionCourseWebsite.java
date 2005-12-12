package net.sourceforge.fenixedu.domain.cms.website;

import relations.ExecutionCourseWebsiteRelation;
import net.sourceforge.fenixedu.stm.VBox;
import net.sourceforge.fenixedu.stm.RelationList;
import net.sourceforge.fenixedu.stm.OJBFunctionalSetWrapper;
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
