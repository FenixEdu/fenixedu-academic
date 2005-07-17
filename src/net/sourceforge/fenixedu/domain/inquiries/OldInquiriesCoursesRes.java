/*
 * Created on Nov 17, 2004
 *
 */
package net.sourceforge.fenixedu.domain.inquiries;



/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class OldInquiriesCoursesRes extends OldInquiriesCoursesRes_Base {

	public void delete() {
		removeExecutionPeriod();
		removeDegree();
		deleteDomainObject();
	}
	
}
