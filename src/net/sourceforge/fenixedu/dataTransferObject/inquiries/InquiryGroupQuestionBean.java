package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.inquiries.InquiryGroupQuestion;
import net.sourceforge.fenixedu.domain.inquiries.InquiryQuestion;
import net.sourceforge.fenixedu.domain.inquiries.MandatoryCondition;
import net.sourceforge.fenixedu.domain.inquiries.QuestionCondition;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;

public class InquiryGroupQuestionBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private InquiryBlockDTO inquiryBlockDTO;
    private InquiryGroupQuestion inquiryGroupQuestion;
    private SortedSet<InquiryQuestionDTO> inquiryQuestions;
    private Integer order;
    private boolean joinUp;

    public InquiryGroupQuestionBean(InquiryGroupQuestion groupQuestion, InquiryBlockDTO inquiryBlockDTO) {
	setInquiryGroupQuestion(groupQuestion);
	setInquiryBlockDTO(inquiryBlockDTO);
	setInquiriyQuestions(new TreeSet<InquiryQuestionDTO>(new BeanComparator("inquiryQuestion.questionOrder")));
	for (InquiryQuestion inquiryQuestion : groupQuestion.getInquiryQuestions()) {
	    getInquiryQuestions().add(new InquiryQuestionDTO(inquiryQuestion));
	}
	setOrder(groupQuestion.getGroupOrder());
	setJoinUp(false);
    }

    //validação para parte curricular do aluno, qd depender de resultados de respostas tem q se adaptar isto
    public boolean validate() {
	Set<InquiryQuestionDTO> questions = getInquiryQuestions();
	boolean isGroupFilledIn = false;
	for (InquiryQuestionDTO inquiryQuestionDTO : questions) {
	    InquiryQuestion inquiryQuestion = inquiryQuestionDTO.getInquiryQuestion();
	    if (StringUtils.isEmpty(inquiryQuestionDTO.getResponseValue())) {
		if (inquiryQuestion.getRequired()) {
		    return false;
		}
		for (QuestionCondition questionCondition : inquiryQuestion.getQuestionConditions()) {
		    if (questionCondition instanceof MandatoryCondition) {
			MandatoryCondition condition = (MandatoryCondition) questionCondition;
			//TODO so esta a ir buscar perguntas dentro do mesmo grupo!!
			InquiryQuestionDTO inquiryDependentQuestionBean = getInquiryQuestionBean(condition
				.getInquiryDependentQuestion());
			boolean isMandatory = condition.getConditionValuesAsList().contains(
				inquiryDependentQuestionBean.getFinalValue());
			if (isMandatory) {
			    return false;
			}
		    }
		}
	    } else {
		isGroupFilledIn = true;
	    }
	}
	if (getInquiryGroupQuestion().getRequired() && !isGroupFilledIn) {
	    return false;
	}
	for (QuestionCondition questionCondition : getInquiryGroupQuestion().getQuestionConditions()) {
	    if (questionCondition instanceof MandatoryCondition) {
		MandatoryCondition condition = (MandatoryCondition) questionCondition;
		//TODO so esta a ir buscar perguntas dentro do mesmo bloco!!
		InquiryQuestionDTO inquiryDependentQuestionBean = getInquiryQuestionBean(condition.getInquiryDependentQuestion());
		boolean isMandatory = condition.getConditionValuesAsList().contains(inquiryDependentQuestionBean.getFinalValue());
		if (isMandatory && !isGroupFilledIn) {
		    return false;
		}
	    }
	}
	return true;
    }

    //	if (inquiryQuestionBean.getInquiryQuestion() instanceof InquiryCheckBoxQuestion) {
    //			    //TODO VER ISTO!!!
    //			    //			    for (String option : inquiryQuestionBean.getCheckboxResponses()) {
    //			    //				if (condition.getConditionValuesAsList().contains(option)) {
    //			    //				    isMandatory = true;
    //			    //				    break;
    //			    //				}
    //			    //			    }
    //			    //			    if (isMandatory && inquiryQuestionDTO.getCheckboxResponses().isEmpty()) {
    //			    //				return false;
    //			    //			    }
    //			} else {

    private InquiryQuestionDTO getInquiryQuestionBean(InquiryQuestion inquiryQuestion) {
	for (InquiryGroupQuestionBean groupQuestionBean : getInquiryBlockDTO().getInquiryGroups()) {
	    for (InquiryQuestionDTO inquiryQuestionDTO : groupQuestionBean.getInquiryQuestions()) {
		if (inquiryQuestionDTO.getInquiryQuestion() == inquiryQuestion) {
		    return inquiryQuestionDTO;
		}
	    }
	}
	return null;
    }

    public SortedSet<InquiryQuestionDTO> getInquiryQuestions() {
	return inquiryQuestions;
    }

    public void setInquiriyQuestions(SortedSet<InquiryQuestionDTO> inquiryQuestions) {
	this.inquiryQuestions = inquiryQuestions;
    }

    public void setInquiryBlockDTO(InquiryBlockDTO inquiryBlockDTO) {
	this.inquiryBlockDTO = inquiryBlockDTO;
    }

    public InquiryBlockDTO getInquiryBlockDTO() {
	return inquiryBlockDTO;
    }

    public void setInquiryGroupQuestion(InquiryGroupQuestion inquiryGroupQuestion) {
	this.inquiryGroupQuestion = inquiryGroupQuestion;
    }

    public InquiryGroupQuestion getInquiryGroupQuestion() {
	return inquiryGroupQuestion;
    }

    public void setJoinUp(boolean joinUp) {
	this.joinUp = joinUp;
    }

    public boolean isJoinUp() {
	return joinUp;
    }

    public void setOrder(Integer order) {
	this.order = order;
    }

    public Integer getOrder() {
	return order;
    }
}
