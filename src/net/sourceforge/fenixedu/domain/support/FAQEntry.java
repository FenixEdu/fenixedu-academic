package net.sourceforge.fenixedu.domain.support;

import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * @author Luis Cruz
 *  
 */
public class FAQEntry extends FAQEntry_Base {

	public FAQEntry() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

}