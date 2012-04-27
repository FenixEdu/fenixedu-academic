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

    public int getNumberOfAnsweredRequiredQuestions() {
	int count = 0;
	for (QuestionAnswer questionAnswer : getQuestionAnswers()) {
	    if (!StringUtils.isEmpty(questionAnswer.getAnswer()) && questionAnswer.getInquiryQuestion().getRequired()) {
		count++;
	    }
	}
	return count;
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

    public boolean hasRequiredQuestionsToAnswer(InquiryTemplate inquiryTemplate) {
	return getNumberOfAnsweredRequiredQuestions() < inquiryTemplate.getNumberOfRequiredQuestions();
    }

	@Deprecated
	public java.util.Date getResponse(){
		org.joda.time.DateTime dt = getResponseDateTime();
		return (dt == null) ? null : new java.util.Date(dt.getMillis());
	}

	@Deprecated
	public void setResponse(java.util.Date date){
		if(date == null) setResponseDateTime(null);
		else setResponseDateTime(new org.joda.time.DateTime(date.getTime()));
	}


}
