package net.sourceforge.fenixedu.domain.inquiries;

import pt.ist.bennu.core.domain.Bennu;

public class InquiryQuestionHeader extends InquiryQuestionHeader_Base {

    public InquiryQuestionHeader() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public void delete() {
        setInquiryBlock(null);
        setInquiryGroupQuestion(null);
        setInquiryQuestion(null);
        setResultGroupQuestion(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasInquiryBlock() {
        return getInquiryBlock() != null;
    }

    @Deprecated
    public boolean hasResultGroupQuestion() {
        return getResultGroupQuestion() != null;
    }

    @Deprecated
    public boolean hasInquiryQuestion() {
        return getInquiryQuestion() != null;
    }

    @Deprecated
    public boolean hasScaleHeaders() {
        return getScaleHeaders() != null;
    }

    @Deprecated
    public boolean hasInquiryGroupQuestion() {
        return getInquiryGroupQuestion() != null;
    }

    @Deprecated
    public boolean hasToolTip() {
        return getToolTip() != null;
    }

    @Deprecated
    public boolean hasTitle() {
        return getTitle() != null;
    }

}
