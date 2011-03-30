package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;

public class InquiryAnswer extends InquiryAnswer_Base {

    public InquiryAnswer() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setResponseDateTime(new DateTime());
    }

    public QuestionAnswer getQuestionAnswer(InquiryQuestion inquiryQuestion) {
	for (QuestionAnswer questionAnswer : getQuestionAnswers()) {
	    if (questionAnswer.getInquiryQuestion() == inquiryQuestion) {
		return questionAnswer;
	    }
	}
	return null;
    }
}
