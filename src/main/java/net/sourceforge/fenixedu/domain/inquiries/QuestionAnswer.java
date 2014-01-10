package net.sourceforge.fenixedu.domain.inquiries;

import org.fenixedu.bennu.core.domain.Bennu;

public class QuestionAnswer extends QuestionAnswer_Base {

    public QuestionAnswer(InquiryAnswer inquiryAnswer, InquiryQuestion inquiryQuestion, String value) {
        super();
        setRootDomainObject(Bennu.getInstance());
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
    public boolean hasBennu() {
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
