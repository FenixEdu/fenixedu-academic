package net.sourceforge.fenixedu.domain.inquiries;

import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public class CoordinatorInquiryTemplate extends CoordinatorInquiryTemplate_Base {

    public CoordinatorInquiryTemplate(DateTime begin, DateTime end, Boolean shared) {
	super();
	init(begin, end);
	setShared(shared);
    }

    public static CoordinatorInquiryTemplate getTemplateByExecutionPeriod(ExecutionSemester executionSemester) {
	final List<InquiryTemplate> inquiryTemplates = RootDomainObject.getInstance().getInquiryTemplates();
	for (final InquiryTemplate inquiryTemplate : inquiryTemplates) {
	    if (inquiryTemplate instanceof CoordinatorInquiryTemplate
		    && executionSemester == inquiryTemplate.getExecutionPeriod()) {
		return (CoordinatorInquiryTemplate) inquiryTemplate;
	    }
	}
	return null;
    }

    public static CoordinatorInquiryTemplate getCurrentTemplate() {
	final List<InquiryTemplate> inquiryTemplates = RootDomainObject.getInstance().getInquiryTemplates();
	for (final InquiryTemplate inquiryTemplate : inquiryTemplates) {
	    if (inquiryTemplate instanceof CoordinatorInquiryTemplate && inquiryTemplate.isOpen()) {
		return (CoordinatorInquiryTemplate) inquiryTemplate;
	    }
	}
	return null;
    }

    public void delete() {
	canBeDeleted();
	getInquiryBlocks().clear();
	removeExecutionPeriod();
	removeRootDomainObject();
	deleteDomainObject();
    }

    private void canBeDeleted() {
	for (InquiryBlock inquiryBlock : getInquiryBlocks()) {
	    for (InquiryGroupQuestion groupQuestion : inquiryBlock.getInquiryGroupsQuestions()) {
		for (InquiryQuestion inquiryQuestion : groupQuestion.getInquiryQuestions()) {
		    for (QuestionAnswer questionAnswer : inquiryQuestion.getQuestionAnswers()) {
			InquiryCoordinatorAnswer coordinatorAnswer = (InquiryCoordinatorAnswer) questionAnswer.getInquiryAnswer();
			if (coordinatorAnswer.getExecutionSemester() == getExecutionPeriod()) {
			    throw new DomainException("error.CoordinatorInquiryTemplate.hasGivenAnswers");
			}
		    }
		}
	    }
	}

    }
}
