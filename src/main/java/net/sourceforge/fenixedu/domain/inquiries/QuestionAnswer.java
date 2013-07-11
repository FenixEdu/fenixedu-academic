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
}
