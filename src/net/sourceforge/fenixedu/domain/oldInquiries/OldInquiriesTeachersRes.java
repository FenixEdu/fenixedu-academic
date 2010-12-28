/*
 * Created on Nov 17, 2004
 *
 */
package net.sourceforge.fenixedu.domain.oldInquiries;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.oldInquiries.OldInquiriesTeachersRes_Base;

/**
 * @author Jo�o Fialho & Rita Ferreira
 * 
 */
public class OldInquiriesTeachersRes extends OldInquiriesTeachersRes_Base {

    public OldInquiriesTeachersRes() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
	removeExecutionPeriod();
	removeDegree();
	removeTeacher();
	deleteDomainObject();
    }

}
