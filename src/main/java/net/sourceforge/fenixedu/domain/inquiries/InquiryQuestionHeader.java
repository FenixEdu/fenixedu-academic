package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class InquiryQuestionHeader extends InquiryQuestionHeader_Base {

    public InquiryQuestionHeader() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
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
    public boolean hasRootDomainObject() {
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
