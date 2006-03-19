package net.sourceforge.fenixedu.domain.cms.website;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class ExecutionCourseWebsite extends ExecutionCourseWebsite_Base {
    
    public ExecutionCourseWebsite() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
}
