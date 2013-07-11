package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class QuestionCondition extends QuestionCondition_Base {

    public QuestionCondition() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
        setInquiryDependentQuestion(null);
        setInquiryGroupQuestion(null);
        setInquiryQuestion(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

}
