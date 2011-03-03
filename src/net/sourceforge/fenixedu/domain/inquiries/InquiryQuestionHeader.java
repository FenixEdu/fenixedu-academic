package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class InquiryQuestionHeader extends InquiryQuestionHeader_Base {

    public InquiryQuestionHeader() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
	removeInquiryBlock();
	removeInquiryGroupQuestion();
	removeInquiryQuestion();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

}
