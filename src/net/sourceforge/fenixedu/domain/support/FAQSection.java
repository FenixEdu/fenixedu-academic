package net.sourceforge.fenixedu.domain.support;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class FAQSection extends FAQSection_Base {

    public FAQSection() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
	for (; !getChildEntries().isEmpty(); getChildEntries().get(0).delete())
	    ;
	for (; !getChildSections().isEmpty(); getChildSections().get(0).delete())
	    ;
	removeRootDomainObject();
	super.deleteDomainObject();
    }

}
