package net.sourceforge.fenixedu.domain.precedences;

import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * @author David Santos in Jun 9, 2004
 */

public abstract class RestrictionByNumberOfCurricularCourses extends RestrictionByNumberOfCurricularCourses_Base {

    public RestrictionByNumberOfCurricularCourses() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

}