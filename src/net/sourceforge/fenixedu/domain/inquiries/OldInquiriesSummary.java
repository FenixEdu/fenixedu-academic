/*
 * Created on Nov 15, 2004
 *
 */
package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * @author Jo�o Fialho & Rita Ferreira
 * 
 */
public class OldInquiriesSummary extends OldInquiriesSummary_Base {

    public OldInquiriesSummary() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
	removeExecutionPeriod();
	removeDegree();
	deleteDomainObject();
    }

}
