package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.inquiries.CurricularCourseInquiryTemplate;
import net.sourceforge.fenixedu.domain.inquiries.DelegateInquiryTemplate;
import net.sourceforge.fenixedu.domain.inquiries.InquiryBlock;
import net.sourceforge.fenixedu.domain.inquiries.InquiryDelegateAnswer;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResultComment;
import net.sourceforge.fenixedu.domain.inquiries.QuestionAnswer;
import net.sourceforge.fenixedu.domain.inquiries.ResultPersonCategory;
import net.sourceforge.fenixedu.domain.student.YearDelegate;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;

import pt.ist.fenixWebFramework.services.Service;

public class DelegateInquiryBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<TeacherShiftTypeResultsBean> teachersResults;
    private List<BlockResultsSummaryBean> curricularBlockResults;
    private Set<InquiryBlockDTO> delegateInquiryBlocks;
    private InquiryDelegateAnswer inquiryDelegateAnswer;
    private YearDelegate yearDelegate;
    private ExecutionCourse executionCourse;

    public DelegateInquiryBean(ExecutionCourse executionCourse, DelegateInquiryTemplate delegateInquiryTemplate,
	    List<InquiryResult> results, YearDelegate yearDelegate, InquiryDelegateAnswer inquiryDelegateAnswer) {
	setYearDelegate(yearDelegate);
	setExecutionCourse(executionCourse);
	initCurricularBlocksResults(executionCourse, results, yearDelegate.getPerson());
	initTeachersResults(executionCourse, yearDelegate.getPerson());
	initDelegateInquiry(delegateInquiryTemplate, yearDelegate, executionCourse, inquiryDelegateAnswer);
    }

    private void initCurricularBlocksResults(ExecutionCourse executionCourse, List<InquiryResult> results, Person person) {
	CurricularCourseInquiryTemplate courseInquiryTemplate = CurricularCourseInquiryTemplate
		.getTemplateByExecutionPeriod(executionCourse.getExecutionPeriod());
	setCurricularBlockResults(new ArrayList<BlockResultsSummaryBean>());
	for (InquiryBlock inquiryBlock : courseInquiryTemplate.getInquiryBlocks()) {
	    getCurricularBlockResults().add(
		    new BlockResultsSummaryBean(inquiryBlock, results, person, ResultPersonCategory.DELEGATE));
	}
	Collections.sort(getCurricularBlockResults(), new BeanComparator("inquiryBlock.blockOrder"));
    }

    private void initTeachersResults(ExecutionCourse executionCourse, Person person) {
	setTeachersResults(new ArrayList<TeacherShiftTypeResultsBean>());
	for (Professorship professorship : executionCourse.getProfessorships()) {
	    List<InquiryResult> professorshipResults = professorship.getInquiriyResults();
	    if (!professorshipResults.isEmpty()) {
		for (ShiftType shiftType : getShiftTypes(professorshipResults)) {
		    List<InquiryResult> teacherShiftResults = professorship.getInquiriyResults(shiftType);
		    if (!teacherShiftResults.isEmpty()) {
			getTeachersResults().add(
				new TeacherShiftTypeResultsBean(professorship, shiftType, executionCourse.getExecutionPeriod(),
					teacherShiftResults, person));
		    }
		}
	    }
	}

	Collections.sort(getTeachersResults(), new BeanComparator("professorship.person.name"));
	Collections.sort(getTeachersResults(), new BeanComparator("shiftType"));
    }

    private void initDelegateInquiry(DelegateInquiryTemplate delegateInquiryTemplate, YearDelegate yearDelegate,
	    ExecutionCourse executionCourse, InquiryDelegateAnswer inquiryDelegateAnswer) {
	setDelegateInquiryBlocks(new TreeSet<InquiryBlockDTO>(new BeanComparator("inquiryBlock.blockOrder")));
	setInquiryDelegateAnswer(inquiryDelegateAnswer);
	for (InquiryBlock inquiryBlock : delegateInquiryTemplate.getInquiryBlocks()) {
	    getDelegateInquiryBlocks().add(new InquiryBlockDTO(inquiryDelegateAnswer, inquiryBlock));
	}
    }

    private Set<ShiftType> getShiftTypes(List<InquiryResult> professorshipResults) {
	Set<ShiftType> shiftTypes = new HashSet<ShiftType>();
	for (InquiryResult inquiryResult : professorshipResults) {
	    shiftTypes.add(inquiryResult.getShiftType());
	}
	return shiftTypes;
    }

    public List<TeacherShiftTypeResultsBean> getTeachersResults() {
	return teachersResults;
    }

    public void setTeachersResults(List<TeacherShiftTypeResultsBean> teachersResults) {
	this.teachersResults = teachersResults;
    }

    public List<BlockResultsSummaryBean> getCurricularBlockResults() {
	return curricularBlockResults;
    }

    public void setCurricularBlockResults(List<BlockResultsSummaryBean> curricularBlockResults) {
	this.curricularBlockResults = curricularBlockResults;
    }

    public Set<InquiryBlockDTO> getDelegateInquiryBlocks() {
	return delegateInquiryBlocks;
    }

    public void setDelegateInquiryBlocks(Set<InquiryBlockDTO> delegateInquiryBlocks) {
	this.delegateInquiryBlocks = delegateInquiryBlocks;
    }

    public boolean isValid() {
	// TODO Auto-generated method stub
	return true;
    }

    public void setInquiryDelegateAnswer(InquiryDelegateAnswer inquiryDelegateAnswer) {
	this.inquiryDelegateAnswer = inquiryDelegateAnswer;
    }

    public InquiryDelegateAnswer getInquiryDelegateAnswer() {
	return inquiryDelegateAnswer;
    }

    public void setExecutionCourse(ExecutionCourse executionCourse) {
	this.executionCourse = executionCourse;
    }

    public ExecutionCourse getExecutionCourse() {
	return executionCourse;
    }

    public YearDelegate getYearDelegate() {
	return yearDelegate;
    }

    public void setYearDelegate(YearDelegate yearDelegate) {
	this.yearDelegate = yearDelegate;
    }

    @Service
    public void saveChanges(Person person, ResultPersonCategory delegate) {
	saveComments(person, delegate, getCurricularBlockResults());
	for (TeacherShiftTypeResultsBean teacherShiftTypeResultsBean : getTeachersResults()) {
	    saveComments(person, delegate, teacherShiftTypeResultsBean.getBlockResults());
	}
	for (InquiryBlockDTO blockDTO : getDelegateInquiryBlocks()) {
	    for (InquiryGroupQuestionBean groupQuestionBean : blockDTO.getInquiryGroups()) {
		for (InquiryQuestionDTO questionDTO : groupQuestionBean.getInquiryQuestions()) {
		    if (!StringUtils.isEmpty(questionDTO.getResponseValue())) {
			if (questionDTO.getQuestionAnswer() != null) {
			    questionDTO.getQuestionAnswer().setAnswer(questionDTO.getResponseValue());
			} else {
			    if (getInquiryDelegateAnswer() == null) {
				setInquiryDelegateAnswer(new InquiryDelegateAnswer(getYearDelegate(), getExecutionCourse()));
			    }
			    new QuestionAnswer(getInquiryDelegateAnswer(), questionDTO.getInquiryQuestion(), questionDTO
				    .getFinalValue());
			}
		    }
		}
	    }
	}
    }

    private void saveComments(Person person, ResultPersonCategory delegate, List<BlockResultsSummaryBean> blocksResults) {
	for (BlockResultsSummaryBean blockResultsSummaryBean : blocksResults) {
	    for (GroupResultsSummaryBean groupResultsSummaryBean : blockResultsSummaryBean.getGroupsResults()) {
		for (QuestionResultsSummaryBean questionResultsSummaryBean : groupResultsSummaryBean.getQuestionsResults()) {
		    InquiryResult questionResult = questionResultsSummaryBean.getQuestionResult();
		    if (questionResult != null) {
			InquiryResultComment inquiryResultComment = questionResultsSummaryBean.getQuestionResult()
				.getInquiryResultComment(person, delegate);
			if (!StringUtils.isEmpty(questionResultsSummaryBean.getEditableComment()) || inquiryResultComment != null) {
			    if (inquiryResultComment == null) {
				inquiryResultComment = new InquiryResultComment(questionResult, person, delegate,
					questionResultsSummaryBean.getQuestionResult().getInquiryResultComments().size() + 1);
			    }
			    inquiryResultComment.setComment(questionResultsSummaryBean.getEditableComment());
			}
		    }
		}
	    }
	}
    }
}
