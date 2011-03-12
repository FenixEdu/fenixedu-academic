package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public abstract class InquiryTemplate extends InquiryTemplate_Base {

    public void validateInquiryPeriod(DateTime begin, DateTime end) {
	if (begin == null || begin.isAfter(end)) {
	    throw new DomainException("error.invalid.period.defined");
	}
    }

    public boolean isOpen() {
	return !getResponsePeriodBegin().isAfterNow() && !getResponsePeriodEnd().isBeforeNow();
    }

    public int getNumberOfQuestions() {
	int count = 0;
	for (InquiryBlock inquiryBlock : getInquiryBlocks()) {
	    for (InquiryGroupQuestion groupQuestion : inquiryBlock.getInquiryGroupsQuestions()) {
		count += groupQuestion.getInquiryQuestionsCount();
	    }
	}
	return count;
    }

    public static InquiryTemplate getInquiryTemplateByTypeAndExecutionSemester(ExecutionSemester executionSemester,
	    InquiryResponsePeriodType type) {

	switch (type) {
	case STUDENT:
	    return CurricularCourseInquiryTemplate.getTemplateByExecutionPeriod(executionSemester);
	case DELEGATE:
	    return DelegateInquiryTemplate.getTemplateByExecutionPeriod(executionSemester);
	case TEACHING:
	    return TeacherInquiryTemplate.getTemplateByExecutionPeriod(executionSemester);
	default:
	    return null;
	}
    }
}
