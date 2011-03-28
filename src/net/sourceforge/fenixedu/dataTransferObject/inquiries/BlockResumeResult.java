package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.inquiries.InquiryAnswer;
import net.sourceforge.fenixedu.domain.inquiries.InquiryBlock;
import net.sourceforge.fenixedu.domain.inquiries.InquiryGroupQuestion;
import net.sourceforge.fenixedu.domain.inquiries.InquiryQuestion;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResponseState;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResultComment;
import net.sourceforge.fenixedu.domain.inquiries.QuestionAnswer;
import net.sourceforge.fenixedu.domain.inquiries.ResultPersonCategory;

import org.apache.commons.lang.StringUtils;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public abstract class BlockResumeResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private Person person;
    private ResultPersonCategory personCategory;
    private String firstHeaderKey;
    private String firstPresentationName;
    private Set<InquiryResult> resultBlocks;

    protected abstract void initResultBlocks();

    protected abstract InquiryAnswer getInquiryAnswer();

    protected abstract int getNumberOfInquiryQuestions();

    protected abstract List<InquiryResult> getInquiryResultsByQuestion(InquiryQuestion inquiryQuestion);

    public List<Integer> getMandatoryIssues() {
	List<Integer> mandatory = new ArrayList<Integer>();
	for (InquiryResult inquiryResult : getResultBlocks()) {
	    mandatory.add(mandatory.size(), getNumberOfMandatoryIssues(inquiryResult));
	}
	return mandatory;
    }

    private int getNumberOfMandatoryIssues(InquiryResult inquiryResult) {
	int count = 0;
	List<InquiryBlock> associatedBlocks = getAssociatedBlocks(inquiryResult);
	for (InquiryBlock inquiryBlock : associatedBlocks) {
	    for (InquiryGroupQuestion inquiryGroupQuestion : inquiryBlock.getInquiryGroupsQuestions()) {
		for (InquiryQuestion inquiryQuestion : inquiryGroupQuestion.getInquiryQuestions()) {
		    List<InquiryResult> inquiryResultsQuestion = getInquiryResultsByQuestion(inquiryQuestion);
		    for (InquiryResult inquiryResultQuestion : inquiryResultsQuestion) {
			if (inquiryResultQuestion != null && inquiryResultQuestion.getResultClassification().isMandatoryComment()) {
			    count++;
			}
		    }
		}
	    }
	}
	return count;
    }

    private int getCommentedfMandatoryIssues(InquiryResult inquiryResult) {
	int count = 0;
	List<InquiryBlock> associatedBlocks = getAssociatedBlocks(inquiryResult);
	for (InquiryBlock inquiryBlock : associatedBlocks) {
	    for (InquiryGroupQuestion inquiryGroupQuestion : inquiryBlock.getInquiryGroupsQuestions()) {
		for (InquiryQuestion inquiryQuestion : inquiryGroupQuestion.getInquiryQuestions()) {
		    List<InquiryResult> inquiryResultsQuestion = getInquiryResultsByQuestion(inquiryQuestion);
		    for (InquiryResult inquiryResultQuestion : inquiryResultsQuestion) {
			InquiryResultComment inquiryResultComment = inquiryResultQuestion != null ? inquiryResultQuestion
				.getInquiryResultComment(getPerson(), getPersonCategory()) : null;
			if (inquiryResultQuestion != null && inquiryResultQuestion.getResultClassification().isMandatoryComment()
				&& inquiryResultComment != null && !StringUtils.isEmpty(inquiryResultComment.getComment())) {
			    count++;
			}
		    }
		}
	    }
	}
	return count;
    }

    public String getCompletionState() {
	return getCompletionStateType().getLocalizedName();
    }

    public InquiryResponseState getCompletionStateType() {
	int mandatoryIssues = 0;
	int mandatoryCommentedIssues = 0;
	for (InquiryResult inquiryResult : getResultBlocks()) {
	    mandatoryIssues += getNumberOfMandatoryIssues(inquiryResult);
	    mandatoryCommentedIssues += getCommentedfMandatoryIssues(inquiryResult);
	}

	InquiryAnswer inquiryAnswer = getInquiryAnswer();
	int numberOfQuestions = getNumberOfInquiryQuestions();

	if ((mandatoryIssues > 0 && mandatoryCommentedIssues == 0 && inquiryAnswer == null)
		|| (mandatoryIssues == 0 && inquiryAnswer == null)) {
	    return InquiryResponseState.EMPTY;
	} else if (mandatoryIssues - mandatoryCommentedIssues > 0) {
	    return InquiryResponseState.INCOMPLETE;
	} else if (inquiryAnswer == null || getNumberOfAnsweredQuestions(inquiryAnswer) < numberOfQuestions) {
	    return InquiryResponseState.PARTIALLY_FILLED;
	} else {
	    return InquiryResponseState.COMPLETE;
	}
    }

    private int getNumberOfAnsweredQuestions(InquiryAnswer inquiryAnswer) {
	int count = 0;
	for (QuestionAnswer questionAnswer : inquiryAnswer.getQuestionAnswers()) {
	    if (!StringUtils.isEmpty(questionAnswer.getAnswer())) {
		count++;
	    }
	}
	return count;
    }

    private List<InquiryBlock> getAssociatedBlocks(InquiryResult inquiryResult) {
	List<InquiryBlock> associatedBlocks = inquiryResult.getInquiryQuestion().getAssociatedBlocks();
	if (!inquiryResult.getInquiryQuestion().getAssociatedResultBlocks().isEmpty()) {
	    associatedBlocks = new ArrayList<InquiryBlock>();
	    for (InquiryBlock inquiryBlock : inquiryResult.getInquiryQuestion().getAssociatedResultBlocks()) {
		for (InquiryGroupQuestion groupQuestion : inquiryBlock.getInquiryGroupsQuestions()) {
		    for (InquiryQuestion inquiryQuestion : groupQuestion.getInquiryQuestions()) {
			associatedBlocks.addAll(inquiryQuestion.getAssociatedBlocks());
		    }
		}
	    }
	}
	return associatedBlocks;
    }

    public Person getPerson() {
	return person;
    }

    public void setPerson(Person person) {
	this.person = person;
    }

    public ResultPersonCategory getPersonCategory() {
	return personCategory;
    }

    public void setPersonCategory(ResultPersonCategory personCategory) {
	this.personCategory = personCategory;
    }

    public Set<InquiryResult> getResultBlocks() {
	return resultBlocks;
    }

    public void setResultBlocks(Set<InquiryResult> resultBlocks) {
	this.resultBlocks = resultBlocks;
    }

    public void setFirstHeaderKey(String firstHeaderKey) {
	this.firstHeaderKey = firstHeaderKey;
    }

    public String getFirstHeaderKey() {
	return firstHeaderKey;
    }

    public void setFirstPresentationName(String firstPresentationName) {
	this.firstPresentationName = firstPresentationName;
    }

    public String getFirstPresentationName() {
	return firstPresentationName;
    }

    public String getFirstHeaderName() {
	return RenderUtils.getResourceString("INQUIRIES_RESOURCES", getFirstHeaderKey());
    }
}