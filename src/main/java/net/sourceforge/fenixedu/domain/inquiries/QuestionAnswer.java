package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class QuestionAnswer extends QuestionAnswer_Base {

    public QuestionAnswer(InquiryAnswer inquiryAnswer, InquiryQuestion inquiryQuestion, String value) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setInquiryAnswer(inquiryAnswer);
        setInquiryQuestion(inquiryQuestion);
        setAnswer(value);
    }

    public void delete() {
        setInquiryQuestion(null);
        setInquiryAnswer(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }
    @Deprecated
    public boolean hasInquiryAnswer() {
        return getInquiryAnswer() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasAnswer() {
        return getAnswer() != null;
    }

    @Deprecated
    public boolean hasInquiryQuestion() {
        return getInquiryQuestion() != null;
    }

}
