package net.sourceforge.fenixedu.domain.inquiries;

import org.fenixedu.bennu.core.domain.Bennu;

public class QuestionCondition extends QuestionCondition_Base {

    public QuestionCondition() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public void delete() {
        setInquiryDependentQuestion(null);
        setInquiryGroupQuestion(null);
        setInquiryQuestion(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasInquiryDependentQuestion() {
        return getInquiryDependentQuestion() != null;
    }

    @Deprecated
    public boolean hasInquiryQuestion() {
        return getInquiryQuestion() != null;
    }

    @Deprecated
    public boolean hasInquiryGroupQuestion() {
        return getInquiryGroupQuestion() != null;
    }

}
