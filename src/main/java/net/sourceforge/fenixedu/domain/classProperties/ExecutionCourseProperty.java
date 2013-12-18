package net.sourceforge.fenixedu.domain.classProperties;

import org.fenixedu.bennu.core.domain.Bennu;

/**
 * @author David Santos in Apr 7, 2004
 */

public class ExecutionCourseProperty extends ExecutionCourseProperty_Base {

    public ExecutionCourseProperty() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public void delete() {
        setExecutionCourse(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Deprecated
    public boolean hasExecutionCourse() {
        return getExecutionCourse() != null;
    }

}
