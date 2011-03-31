package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.apache.commons.lang.StringUtils;
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

    public int getNumberOfAnsweredQuestions() {
	int count = 0;
	for (QuestionAnswer questionAnswer : getQuestionAnswers()) {
	    if (!StringUtils.isEmpty(questionAnswer.getAnswer())) {
		count++;
	    }
	}
	return count;
    }
}
