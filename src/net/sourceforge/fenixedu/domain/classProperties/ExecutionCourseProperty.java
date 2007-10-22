package net.sourceforge.fenixedu.domain.classProperties;

import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * @author David Santos in Apr 7, 2004
 */

public class ExecutionCourseProperty extends ExecutionCourseProperty_Base {

    public ExecutionCourseProperty() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
	removeExecutionCourse();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

}
