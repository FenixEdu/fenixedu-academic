package net.sourceforge.fenixedu.domain.support;

import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * @author Luis Cruz
 *  
 */
public class FAQSection extends FAQSection_Base {

	public FAQSection() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

}