package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class InquiryGroupQuestion extends InquiryGroupQuestion_Base {

    public InquiryGroupQuestion() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setRequired(false);
    }

    public boolean isVisible(StudentInquiryRegistry studentInquiryRegistry) {
	for (QuestionCondition questionCondition : getQuestionConditions()) {
	    if (questionCondition instanceof ECTSVisibleCondition) {
		return ((ECTSVisibleCondition) questionCondition).isVisible(studentInquiryRegistry);
	    }
	}
	return true;
    }

    public String[] getConditionValues(StudentInquiryRegistry studentInquiryRegistry) {
	for (QuestionCondition questionCondition : getQuestionConditions()) {
	    if (questionCondition instanceof ECTSVisibleCondition) {
		return ((ECTSVisibleCondition) questionCondition).getConditionValues(studentInquiryRegistry);
	    }
	}
	return null;
    }

    public boolean isScaleGroup() {
	if (getInquiryQuestionHeader() != null && getInquiryQuestionHeader().getScaleHeaders() != null) {
	    return true;
	}
	for (InquiryQuestion inquiryQuestion : getInquiryQuestions()) {
	    if (inquiryQuestion.isScaleQuestion()) {
		return true;
	    }
	}
	return false;
    }

    public boolean isResultGroup() {
	return getInquiryBlock().getInquiry() instanceof ResultsInquiryTemplate;
    }

    public boolean isCheckbox() {
	for (InquiryQuestion inquiryQuestion : getInquiryQuestions()) {
	    if (inquiryQuestion instanceof InquiryCheckBoxQuestion) {
		return true;
	    }
	}
	return false;
    }

    public void delete() {
	removeInquiryBlock();
	removeInquiryQuestionHeader();
	removeRootDomainObject();
	super.deleteDomainObject();
    }
}
