/*
 * Created on Nov 17, 2004
 *
 */
package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.RootDomainObject;



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
