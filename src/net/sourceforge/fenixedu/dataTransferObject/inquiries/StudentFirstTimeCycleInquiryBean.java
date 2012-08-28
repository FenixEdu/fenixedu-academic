package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.domain.inquiries.InquiryBlock;
import net.sourceforge.fenixedu.domain.inquiries.InquiryQuestion;
import net.sourceforge.fenixedu.domain.inquiries.InquiryStudent1rstCycleAnswer;
import net.sourceforge.fenixedu.domain.inquiries.MandatoryCondition;
import net.sourceforge.fenixedu.domain.inquiries.QuestionAnswer;
import net.sourceforge.fenixedu.domain.inquiries.QuestionCondition;
import net.sourceforge.fenixedu.domain.inquiries.StudentCycleInquiryTemplate;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;

public class StudentFirstTimeCycleInquiryBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Set<InquiryBlockDTO> studentInquiryBlocks;
    private Registration registration;
    private StudentCandidacy candidacy;

    public StudentFirstTimeCycleInquiryBean(StudentCycleInquiryTemplate studentInquiryTemplate, Registration registration) {
	initStudentInquiry(studentInquiryTemplate, registration);
	setGroupsVisibility();
    }

    private void initStudentInquiry(StudentCycleInquiryTemplate studentInquiryTemplate, Registration registration) {
	setRegistration(registration);
	setStudentInquiryBlocks(new TreeSet<InquiryBlockDTO>(new BeanComparator("inquiryBlock.blockOrder")));
	for (InquiryBlock inquiryBlock : studentInquiryTemplate.getInquiryBlocks()) {
	    getStudentInquiryBlocks().add(new InquiryBlockDTO(inquiryBlock));
	}

    }

    public void setGroupsVisibility() {
	for (InquiryBlockDTO inquiryBlockDTO : getStudentInquiryBlocks()) {
	    Set<InquiryGroupQuestionBean> groups = inquiryBlockDTO.getInquiryGroups();
	    for (InquiryGroupQuestionBean group : groups) {
		setGroupVisibility(getStudentInquiryBlocks(), group);
	    }
	}
    }

    private void setGroupVisibility(Set<InquiryBlockDTO> inquiryBlocks, InquiryGroupQuestionBean groupQuestionBean) {
	for (QuestionCondition questionCondition : groupQuestionBean.getInquiryGroupQuestion().getQuestionConditions()) {
	    if (questionCondition instanceof MandatoryCondition) {
		MandatoryCondition condition = (MandatoryCondition) questionCondition;
		InquiryQuestionDTO inquiryDependentQuestionBean = getInquiryQuestionBean(condition.getInquiryDependentQuestion(),
			inquiryBlocks);
		boolean isMandatory = inquiryDependentQuestionBean.getFinalValue() == null ? false : condition
			.getConditionValuesAsList().contains(inquiryDependentQuestionBean.getFinalValue());
		if (isMandatory) {
		    groupQuestionBean.setVisible(true);
		} else {
		    groupQuestionBean.setVisible(false);
		    for (InquiryQuestionDTO questionDTO : groupQuestionBean.getInquiryQuestions()) {
			questionDTO.setResponseValue(null);
		    }
		}
	    }
	}
    }

    private InquiryQuestionDTO getInquiryQuestionBean(InquiryQuestion inquiryQuestion, Set<InquiryBlockDTO> inquiryBlocks) {
	for (InquiryBlockDTO blockDTO : inquiryBlocks) {
	    for (InquiryGroupQuestionBean groupQuestionBean : blockDTO.getInquiryGroups()) {
		for (InquiryQuestionDTO inquiryQuestionDTO : groupQuestionBean.getInquiryQuestions()) {
		    if (inquiryQuestionDTO.getInquiryQuestion() == inquiryQuestion) {
			return inquiryQuestionDTO;
		    }
		}
	    }
	}
	return null;
    }

    public String validateInquiry() {
	String validationResult = null;
	for (InquiryBlockDTO inquiryBlockDTO : getStudentInquiryBlocks()) {
	    validationResult = inquiryBlockDTO.validate(getStudentInquiryBlocks());
	    if (!Boolean.valueOf(validationResult)) {
		return validationResult;
	    }
	}
	return Boolean.toString(true);
    }

    @Service
    public void saveAnswers() {
	InquiryStudent1rstCycleAnswer inquiryStudent1rstCycleAnswer = null;
	for (InquiryBlockDTO blockDTO : getStudentInquiryBlocks()) {
	    for (InquiryGroupQuestionBean groupQuestionBean : blockDTO.getInquiryGroups()) {
		for (InquiryQuestionDTO questionDTO : groupQuestionBean.getInquiryQuestions()) {
		    if (!StringUtils.isEmpty(questionDTO.getResponseValue()) || questionDTO.getQuestionAnswer() != null) {
			if (inquiryStudent1rstCycleAnswer == null) {
			    inquiryStudent1rstCycleAnswer = new InquiryStudent1rstCycleAnswer(getRegistration());
			    inquiryStudent1rstCycleAnswer.setResponseDateTime(new DateTime());
			}
			new QuestionAnswer(inquiryStudent1rstCycleAnswer, questionDTO.getInquiryQuestion(),
				questionDTO.getFinalValue());
		    }
		}
	    }
	}
    }

    public Registration getRegistration() {
	return registration;
    }

    public void setRegistration(Registration registration) {
	this.registration = registration;
    }

    public void setStudentInquiryBlocks(Set<InquiryBlockDTO> studentInquiryBlocks) {
	this.studentInquiryBlocks = studentInquiryBlocks;
    }

    public Set<InquiryBlockDTO> getStudentInquiryBlocks() {
	return studentInquiryBlocks;
    }

    public void setCandidacy(StudentCandidacy candidacy) {
	this.candidacy = candidacy;
    }

    public StudentCandidacy getCandidacy() {
	return candidacy;
    }
}
