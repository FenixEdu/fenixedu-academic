package net.sourceforge.fenixedu.domain.precedences;

import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * @author David Santos in Jun 9, 2004
 */

public abstract class RestrictionByCurricularCourse extends RestrictionByCurricularCourse_Base {

    public RestrictionByCurricularCourse() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
	removePrecedentCurricularCourse();
	super.delete();
    }

}