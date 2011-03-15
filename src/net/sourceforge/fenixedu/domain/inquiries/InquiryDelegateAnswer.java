package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.student.YearDelegate;

public class InquiryDelegateAnswer extends InquiryDelegateAnswer_Base {

    public InquiryDelegateAnswer(YearDelegate yearDelegate, ExecutionCourse executionCourse, ExecutionDegree executionDegree) {
	super();
	setDelegate(yearDelegate);
	setExecutionCourse(executionCourse);
	setExecutionDegree(executionDegree);
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
