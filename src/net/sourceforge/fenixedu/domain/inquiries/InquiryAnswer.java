package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class InquiryAnswer extends InquiryAnswer_Base {

    public InquiryAnswer() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

}
