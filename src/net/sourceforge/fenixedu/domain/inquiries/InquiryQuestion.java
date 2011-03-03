package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class InquiryQuestion extends InquiryQuestion_Base {

    public InquiryQuestion() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setRequired(false);
	setAutofit(false);
	setNewRow(false);
	setShowRequiredMark(false);
	setHasClassification(false);
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

    public void delete() {
	removeInquiryGroupQuestion();
	removeInquiryQuestionHeader();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    public boolean isScaleQuestion() {
	return ((getInquiryGroupQuestion().getInquiryQuestionHeader() != null && getInquiryGroupQuestion()
		.getInquiryQuestionHeader().getScaleHeaders() != null) || (getInquiryQuestionHeader() != null && getInquiryQuestionHeader()
		.getScaleHeaders() != null));
    }
}
