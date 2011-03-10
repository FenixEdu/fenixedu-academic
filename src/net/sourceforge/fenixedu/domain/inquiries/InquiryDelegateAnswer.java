package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.student.YearDelegate;

public class InquiryDelegateAnswer extends InquiryDelegateAnswer_Base {

    public InquiryDelegateAnswer(YearDelegate yearDelegate, ExecutionCourse executionCourse) {
	super();
	setDelegate(yearDelegate);
	setExecutionCourse(executionCourse);
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
