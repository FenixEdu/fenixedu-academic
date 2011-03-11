package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.inquiries.InquiryBlock;
import net.sourceforge.fenixedu.domain.inquiries.InquiryConnectionType;
import net.sourceforge.fenixedu.domain.inquiries.InquiryDelegateAnswer;
import net.sourceforge.fenixedu.domain.inquiries.InquiryGroupQuestion;
import net.sourceforge.fenixedu.domain.inquiries.InquiryQuestion;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import net.sourceforge.fenixedu.domain.inquiries.ResultPersonCategory;
import net.sourceforge.fenixedu.domain.student.YearDelegate;

import org.apache.commons.beanutils.BeanComparator;

public class CurricularCourseResumeResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private ExecutionCourse executionCourse;
    private ExecutionDegree executionDegree;
    private YearDelegate yearDelegate;
    private Set<InquiryResult> resultBlocks;

    public CurricularCourseResumeResult(ExecutionCourse executionCourse, ExecutionDegree executionDegree,
	    YearDelegate yearDelegate) {
	setExecutionCourse(executionCourse);
	setExecutionDegree(executionDegree);
	setYearDelegate(yearDelegate);
    }

    public void setExecutionCourse(ExecutionCourse executionCourse) {
	this.executionCourse = executionCourse;
    }

    public ExecutionCourse getExecutionCourse() {
	return executionCourse;
    }

    public void setExecutionDegree(ExecutionDegree executionDegree) {
	this.executionDegree = executionDegree;
    }

    public ExecutionDegree getExecutionDegree() {
	return executionDegree;
    }

    public void setYearDelegate(YearDelegate yearDelegate) {
	this.yearDelegate = yearDelegate;
    }

    public YearDelegate getYearDelegate() {
	return yearDelegate;
    }

    public Set<InquiryResult> getCurricularBlocks() {
	setResultBlocks(new TreeSet<InquiryResult>(new BeanComparator("inquiryQuestion.questionOrder")));
	for (InquiryResult inquiryResult : getExecutionCourse().getInquiryResults()) {
	    if ((inquiryResult.getExecutionDegree() == getExecutionDegree() || inquiryResult.getExecutionDegree() == null)
		    && InquiryConnectionType.GROUP.equals(inquiryResult.getConnectionType())) { //change to COURSE_EVALUATION
		getResultBlocks().add(inquiryResult);
	    }
	}
	return getResultBlocks();
    }

    private Set<InquiryResult> getResultBlocks() {
	return resultBlocks;
    }

    private void setResultBlocks(TreeSet<InquiryResult> resultBlocks) {
	this.resultBlocks = resultBlocks;
    }

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
		    InquiryResult inquiryResultQuestion = getInquiryResultByQuestion(inquiryQuestion);
		    if (inquiryResultQuestion != null && inquiryResultQuestion.getResultClassification().isMandatoryComment()) {
			count++;
		    }
		}
	    }
	}
	return count;
    }

    public String getCompletionState() {
	int mandatoryIssues = 0;
	int mandatoryCommentedIssues = 0;
	for (InquiryResult inquiryResult : getResultBlocks()) {
	    mandatoryIssues += getNumberOfMandatoryIssues(inquiryResult);
	    mandatoryCommentedIssues += getCommentedfMandatoryIssues(inquiryResult);
	}

	InquiryDelegateAnswer inquiryDelegateAnswer = null;
	for (InquiryDelegateAnswer delegateAnswer : getYearDelegate().getInquiryDelegateAnswers()) {
	    if (delegateAnswer.getExecutionCourse() == getExecutionCourse()) {
		inquiryDelegateAnswer = delegateAnswer;
	    }
	}
	if ((mandatoryIssues > 0 && mandatoryCommentedIssues == 0 && inquiryDelegateAnswer == null)
		|| (mandatoryIssues == 0 && inquiryDelegateAnswer == null)) {
	    return "Por preencher";
	} else if ((mandatoryIssues - mandatoryCommentedIssues > 1)
		|| (inquiryDelegateAnswer != null && inquiryDelegateAnswer.getQuestionAnswers().size() > 0 && inquiryDelegateAnswer
			.getQuestionAnswers().size() < 3) || (inquiryDelegateAnswer == null && mandatoryCommentedIssues > 1)) {
	    return "Incompleto";
	} else if (mandatoryIssues == mandatoryCommentedIssues && inquiryDelegateAnswer != null
		&& inquiryDelegateAnswer.getQuestionAnswers().size() == 3) { //TODO check the number of questions the inquiry has
	    return "Completo";
	}
	return "-";
    }

    private int getCommentedfMandatoryIssues(InquiryResult inquiryResult) {
	int count = 0;
	List<InquiryBlock> associatedBlocks = getAssociatedBlocks(inquiryResult);
	for (InquiryBlock inquiryBlock : associatedBlocks) {
	    for (InquiryGroupQuestion inquiryGroupQuestion : inquiryBlock.getInquiryGroupsQuestions()) {
		for (InquiryQuestion inquiryQuestion : inquiryGroupQuestion.getInquiryQuestions()) {
		    InquiryResult inquiryResultQuestion = getInquiryResultByQuestion(inquiryQuestion);
		    if (inquiryResultQuestion != null
			    && inquiryResultQuestion.getResultClassification().isMandatoryComment()
			    && inquiryResultQuestion.getInquiryResultComment(getYearDelegate().getPerson(),
				    ResultPersonCategory.DELEGATE) != null) {
			count++;
		    }
		}
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

    private InquiryResult getInquiryResultByQuestion(InquiryQuestion inquiryQuestion) {
	for (InquiryResult inquiryResult : getExecutionCourse().getInquiryResults()) {
	    if (inquiryResult.getExecutionDegree() == getExecutionDegree()
		    || (inquiryResult.getExecutionDegree() == null && inquiryResult.getShiftType() != null)) {
		if (inquiryResult.getInquiryQuestion() == inquiryQuestion && inquiryResult.getResultClassification() != null) {
		    return inquiryResult;
		}
	    }
	}
	return null;
    }
}