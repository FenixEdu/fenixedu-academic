package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.inquiries.CoordinatorInquiryTemplate;
import net.sourceforge.fenixedu.domain.inquiries.InquiryBlock;
import net.sourceforge.fenixedu.domain.inquiries.InquiryCoordinatorAnswer;
import net.sourceforge.fenixedu.domain.inquiries.QuestionAnswer;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;

public class CoordinatorInquiryBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Set<InquiryBlockDTO> coordinatorInquiryBlocks;
    private InquiryCoordinatorAnswer inquiryCoordinatorAnswer;
    private Coordinator coordinator;
    private ExecutionSemester executionSemester;
    private ExecutionDegree executionDegree;

    public CoordinatorInquiryBean(CoordinatorInquiryTemplate coordinatorInquiryTemplate, Coordinator coordinator,
	    InquiryCoordinatorAnswer inquiryCoordinatorAnswer, ExecutionSemester executionSemester,
	    ExecutionDegree executionDegree) {
	setCoordinator(coordinator);
	setExecutionSemester(executionSemester);
	setExecutionDegree(executionDegree);
	initCoordinatorInquiryBlocks(coordinatorInquiryTemplate, inquiryCoordinatorAnswer);
    }

    private void initCoordinatorInquiryBlocks(CoordinatorInquiryTemplate coordinatorInquiryTemplate,
	    InquiryCoordinatorAnswer inquiryCoordinatorAnswer) {
	setCoordinatorInquiryBlocks(new TreeSet<InquiryBlockDTO>(new BeanComparator("inquiryBlock.blockOrder")));
	setInquiryCoordinatorAnswer(inquiryCoordinatorAnswer);
	for (InquiryBlock inquiryBlock : coordinatorInquiryTemplate.getInquiryBlocks()) {
	    getCoordinatorInquiryBlocks().add(new InquiryBlockDTO(inquiryCoordinatorAnswer, inquiryBlock));
	}
    }

    public String validateInquiry() {
	String validationResult = null;
	for (InquiryBlockDTO inquiryBlockDTO : getCoordinatorInquiryBlocks()) {
	    validationResult = inquiryBlockDTO.validateMandatoryConditions(getCoordinatorInquiryBlocks());
	    if (!Boolean.valueOf(validationResult)) {
		return validationResult;
	    }
	}
	return Boolean.toString(true);
    }

    @Service
    public void saveInquiry() {
	for (InquiryBlockDTO blockDTO : getCoordinatorInquiryBlocks()) {
	    for (InquiryGroupQuestionBean groupQuestionBean : blockDTO.getInquiryGroups()) {
		for (InquiryQuestionDTO questionDTO : groupQuestionBean.getInquiryQuestions()) {
		    if (!StringUtils.isEmpty(questionDTO.getResponseValue()) || questionDTO.getQuestionAnswer() != null) {
			if (questionDTO.getQuestionAnswer() != null) {
			    questionDTO.getQuestionAnswer().setAnswer(questionDTO.getResponseValue());
			    questionDTO.getQuestionAnswer().getInquiryAnswer().setResponseDateTime(new DateTime());
			} else {
			    if (getInquiryCoordinatorAnswer() == null) {
				setInquiryCoordinatorAnswer(new InquiryCoordinatorAnswer(getExecutionDegree(),
					getExecutionSemester()));
			    }
			    new QuestionAnswer(getInquiryCoordinatorAnswer(), questionDTO.getInquiryQuestion(), questionDTO
				    .getFinalValue());
			    getInquiryCoordinatorAnswer().setResponseDateTime(new DateTime());
			}
		    }
		}
	    }
	}
    }

    public void setCoordinatorInquiryBlocks(Set<InquiryBlockDTO> coordinatorInquiryBlocks) {
	this.coordinatorInquiryBlocks = coordinatorInquiryBlocks;
    }

    public Set<InquiryBlockDTO> getCoordinatorInquiryBlocks() {
	return coordinatorInquiryBlocks;
    }

    public void setInquiryCoordinatorAnswer(InquiryCoordinatorAnswer inquiryCoordinatorAnswer) {
	this.inquiryCoordinatorAnswer = inquiryCoordinatorAnswer;
    }

    public InquiryCoordinatorAnswer getInquiryCoordinatorAnswer() {
	return inquiryCoordinatorAnswer;
    }

    public void setCoordinator(Coordinator coordinator) {
	this.coordinator = coordinator;
    }

    public Coordinator getCoordinator() {
	return coordinator;
    }

    public void setExecutionSemester(ExecutionSemester executionSemester) {
	this.executionSemester = executionSemester;
    }

    public ExecutionSemester getExecutionSemester() {
	return executionSemester;
    }

    public void setExecutionDegree(ExecutionDegree executionDegree) {
	this.executionDegree = executionDegree;
    }

    public ExecutionDegree getExecutionDegree() {
	return executionDegree;
    }

}
