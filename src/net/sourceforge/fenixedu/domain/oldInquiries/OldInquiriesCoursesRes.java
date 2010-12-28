/*
 * Created on Nov 17, 2004
 *
 */
package net.sourceforge.fenixedu.domain.oldInquiries;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.oldInquiries.OldInquiriesCoursesRes_Base;

/**
 * @author Jo�o Fialho & Rita Ferreira
 * 
 */
public class OldInquiriesCoursesRes extends OldInquiriesCoursesRes_Base {

    public OldInquiriesCoursesRes() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
	removeExecutionPeriod();
	removeDegree();
	deleteDomainObject();
    }

}
