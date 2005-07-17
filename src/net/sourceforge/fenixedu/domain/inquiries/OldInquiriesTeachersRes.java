/*
 * Created on Nov 17, 2004
 *
 */
package net.sourceforge.fenixedu.domain.inquiries;



/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class OldInquiriesTeachersRes extends OldInquiriesTeachersRes_Base {

	public void delete() {
		removeExecutionPeriod();
		removeDegree();
		removeTeacher();
		deleteDomainObject();
	}
	
}
