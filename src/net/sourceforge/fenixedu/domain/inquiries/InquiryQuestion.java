package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class InquiryQuestion extends InquiryQuestion_Base {

	public InquiryQuestion() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
		setRequired(false);
		setAutofit(false);
		setNewRow(false);
		setShowRequiredMark(false);
		setHasClassification(false);
		setPresentResults(true);
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
		if (!getInquiryResults().isEmpty()) {
			throw new DomainException("error.inquiryQuestion.can.not.delete.hasAssociatedResults");
		}
		if (!getQuestionAnswers().isEmpty()) {
			throw new DomainException("error.inquiryQuestion.can.not.delete.hasAssociatedAnswers");
		}
		for (; !getQuestionConditions().isEmpty(); getQuestionConditions().get(0).delete()) {
			;
		}
		if (hasInquiryQuestionHeader()) {
			getInquiryQuestionHeader().delete();
		}
		for (InquiryBlock inquiryBlock : getAssociatedBlocks()) {
			removeAssociatedBlocks(inquiryBlock);
		}
		for (InquiryBlock inquiryBlock : getAssociatedResultBlocks()) {
			removeAssociatedResultBlocks(inquiryBlock);
		}
		removeCheckboxGroupQuestion();
		removeDependentQuestionCondition();
		removeInquiryGroupQuestion();
		removeRootDomainObject();
		super.deleteDomainObject();
	}

	public boolean isScaleQuestion() {
		return ((getInquiryGroupQuestion().getInquiryQuestionHeader() != null && getInquiryGroupQuestion()
				.getInquiryQuestionHeader().getScaleHeaders() != null) || (getInquiryQuestionHeader() != null && getInquiryQuestionHeader()
				.getScaleHeaders() != null));
	}

	public boolean isResultQuestion(ExecutionSemester executionSemester) {
		return getInquiryGroupQuestion().getInquiryBlock().isResultBlock(executionSemester);
	}

	public boolean hasGroupDependentQuestionCondition() {
		return getDependentQuestionCondition() != null && getDependentQuestionCondition().hasInquiryGroupQuestion();
	}
}
