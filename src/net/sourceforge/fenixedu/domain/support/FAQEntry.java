package net.sourceforge.fenixedu.domain.support;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class FAQEntry extends FAQEntry_Base {

    public FAQEntry() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
	removeRootDomainObject();
	super.deleteDomainObject();
    }

}
