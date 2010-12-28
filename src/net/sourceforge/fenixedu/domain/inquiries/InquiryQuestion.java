package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class InquiryQuestion extends InquiryQuestion_Base {

    public InquiryQuestion() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setRequired(false);
	setAutofit(false);
	setNewRow(false);
	setShowRequiredMark(false);
    }

    public void delete() {
	removeInquiryGroupQuestion();
	removeInquiryQuestionHeader();
	removeRootDomainObject();
	super.deleteDomainObject();
    }
}
