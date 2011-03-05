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

    public List<Integer> getUncommentedMandatoryIssues() {
	List<Integer> uncommentedIssues = new ArrayList<Integer>();
	for (InquiryResult inquiryResult : getResultBlocks()) {
	    uncommentedIssues.add(uncommentedIssues.size(), getNumberOfUncommentedIssues(inquiryResult));
	}
	return uncommentedIssues;
    }

    private int getNumberOfUncommentedIssues(InquiryResult inquiryResult) {
	int count = 0;
	for (InquiryBlock inquiryBlock : inquiryResult.getInquiryQuestion().getAssociatedBlocks()) {
	    for (InquiryGroupQuestion inquiryGroupQuestion : inquiryBlock.getInquiryGroupsQuestions()) {
		for (InquiryQuestion inquiryQuestion : inquiryGroupQuestion.getInquiryQuestions()) {
		    InquiryResult inquiryResultQuestion = getInquiryResultByQuestion(inquiryQuestion);
		    if (inquiryResultQuestion != null
			    && inquiryResultQuestion.getResultClassification().isMandatoryComment()
			    && inquiryResultQuestion.getInquiryResultComment(getYearDelegate().getRegistration().getPerson(),
				    ResultPersonCategory.DELEGATE) == null) {
			count++;
		    }
		}
	    }
	}
	return count;
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