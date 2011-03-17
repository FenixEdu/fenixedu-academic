package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.inquiries.InquiryBlock;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResultComment;
import net.sourceforge.fenixedu.domain.inquiries.InquiryTeacherAnswer;
import net.sourceforge.fenixedu.domain.inquiries.QuestionAnswer;
import net.sourceforge.fenixedu.domain.inquiries.ResultPersonCategory;
import net.sourceforge.fenixedu.domain.inquiries.TeacherInquiryTemplate;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;

import pt.ist.fenixWebFramework.services.Service;

public class TeacherInquiryBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<TeacherShiftTypeResultsBean> teachersResults;
    private Set<InquiryBlockDTO> teacherInquiryBlocks;
    private InquiryTeacherAnswer inquiryTeacherAnswer;
    private Professorship professorship;

    public TeacherInquiryBean(TeacherInquiryTemplate teacherInquiryTemplate, Professorship professorship,
	    InquiryTeacherAnswer inquiryTeacherAnswer) {
	setProfessorship(professorship);
	initTeachersResults(professorship, professorship.getPerson());
	initTeacherInquiry(teacherInquiryTemplate, professorship, inquiryTeacherAnswer);
    }

    private void initTeacherInquiry(TeacherInquiryTemplate teacherInquiryTemplate, Professorship professorship,
	    InquiryTeacherAnswer inquiryTeacherAnswer) {
	setTeacherInquiryBlocks(new TreeSet<InquiryBlockDTO>(new BeanComparator("inquiryBlock.blockOrder")));
	setInquiryTeacherAnswer(inquiryTeacherAnswer);
	for (InquiryBlock inquiryBlock : teacherInquiryTemplate.getInquiryBlocks()) {
	    getTeacherInquiryBlocks().add(new InquiryBlockDTO(inquiryTeacherAnswer, inquiryBlock));
	}

    }

    private void initTeachersResults(Professorship professorship, Person person) {
	setTeachersResults(new ArrayList<TeacherShiftTypeResultsBean>());
	List<InquiryResult> professorshipResults = professorship.getInquiryResults();
	if (!professorshipResults.isEmpty()) {
	    for (ShiftType shiftType : getShiftTypes(professorshipResults)) {
		List<InquiryResult> teacherShiftResults = professorship.getInquiryResults(shiftType);
		if (!teacherShiftResults.isEmpty()) {
		    getTeachersResults().add(
			    new TeacherShiftTypeResultsBean(professorship, shiftType, professorship.getExecutionCourse()
				    .getExecutionPeriod(), teacherShiftResults, person, ResultPersonCategory.TEACHER));
		}
	    }
	}
	Collections.sort(getTeachersResults(), new BeanComparator("professorship.person.name"));
	Collections.sort(getTeachersResults(), new BeanComparator("shiftType"));
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

    public boolean isValid() {
	// TODO Auto-generated method stub
	return true;
    }

    @Service
    public void saveChanges(Person person, ResultPersonCategory teacher) {
	for (TeacherShiftTypeResultsBean teacherShiftTypeResultsBean : getTeachersResults()) {
	    saveComments(person, teacher, teacherShiftTypeResultsBean.getBlockResults());
	}
	for (InquiryBlockDTO blockDTO : getTeacherInquiryBlocks()) {
	    for (InquiryGroupQuestionBean groupQuestionBean : blockDTO.getInquiryGroups()) {
		for (InquiryQuestionDTO questionDTO : groupQuestionBean.getInquiryQuestions()) {
		    if (!StringUtils.isEmpty(questionDTO.getResponseValue()) || questionDTO.getQuestionAnswer() != null) {
			if (questionDTO.getQuestionAnswer() != null) {
			    questionDTO.getQuestionAnswer().setAnswer(questionDTO.getResponseValue());
			} else {
			    if (getInquiryTeacherAnswer() == null) {
				setInquiryTeacherAnswer(new InquiryTeacherAnswer(getProfessorship()));
			    }
			    new QuestionAnswer(getInquiryTeacherAnswer(), questionDTO.getInquiryQuestion(), questionDTO
				    .getFinalValue());
			}
		    }
		}
	    }
	}
    }

    private void saveComments(Person person, ResultPersonCategory teacher, List<BlockResultsSummaryBean> blocksResults) {
	for (BlockResultsSummaryBean blockResultsSummaryBean : blocksResults) {
	    for (GroupResultsSummaryBean groupResultsSummaryBean : blockResultsSummaryBean.getGroupsResults()) {
		for (QuestionResultsSummaryBean questionResultsSummaryBean : groupResultsSummaryBean.getQuestionsResults()) {
		    InquiryResult questionResult = questionResultsSummaryBean.getQuestionResult();
		    if (questionResult != null) {
			InquiryResultComment inquiryResultComment = questionResultsSummaryBean.getQuestionResult()
				.getInquiryResultComment(person, teacher);
			if (!StringUtils.isEmpty(questionResultsSummaryBean.getEditableComment()) || inquiryResultComment != null) {
			    if (inquiryResultComment == null) {
				inquiryResultComment = new InquiryResultComment(questionResult, person, teacher,
					questionResultsSummaryBean.getQuestionResult().getInquiryResultComments().size() + 1);
			    }
			    inquiryResultComment.setComment(questionResultsSummaryBean.getEditableComment());
			}
		    }
		}
	    }
	}
    }

    public void setProfessorship(Professorship professorship) {
	this.professorship = professorship;
    }

    public Professorship getProfessorship() {
	return professorship;
    }

    public void setTeacherInquiryBlocks(Set<InquiryBlockDTO> teacherInquiryBlocks) {
	this.teacherInquiryBlocks = teacherInquiryBlocks;
    }

    public Set<InquiryBlockDTO> getTeacherInquiryBlocks() {
	return teacherInquiryBlocks;
    }

    public void setInquiryTeacherAnswer(InquiryTeacherAnswer inquiryTeacherAnswer) {
	this.inquiryTeacherAnswer = inquiryTeacherAnswer;
    }

    public InquiryTeacherAnswer getInquiryTeacherAnswer() {
	return inquiryTeacherAnswer;
    }
}
