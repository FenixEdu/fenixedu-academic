package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.inquiries.InquiryCheckBoxQuestion;
import net.sourceforge.fenixedu.domain.inquiries.InquiryDelegateAnswer;
import net.sourceforge.fenixedu.domain.inquiries.InquiryQuestion;
import net.sourceforge.fenixedu.domain.inquiries.InquiryTextBoxQuestion;
import net.sourceforge.fenixedu.domain.inquiries.QuestionAnswer;
import net.sourceforge.fenixedu.domain.inquiries.StudentInquiryRegistry;

public class InquiryQuestionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private StudentInquiryRegistry studentInquiryRegistry;
    private InquiryQuestion inquiryQuestion;
    private QuestionAnswer questionAnswer;
    private String responseValue;
    private boolean isVisible;
    private String[] conditionValues;

    public InquiryQuestionDTO(InquiryQuestion inquiryQuestion, StudentInquiryRegistry studentInquiryRegistry) {
	setInquiryQuestion(inquiryQuestion);
	setStudentInquiryRegistry(studentInquiryRegistry);
	setConditionOptions(studentInquiryRegistry);
    }

    public InquiryQuestionDTO(InquiryQuestion inquiryQuestion, InquiryDelegateAnswer inquiryDelegateAnswer) {
	setInquiryQuestion(inquiryQuestion);
	setVisible(true);
	setQuestionAnswer(inquiryDelegateAnswer != null ? inquiryDelegateAnswer.getQuestionAnswer(inquiryQuestion) : null);
	if (getQuestionAnswer() != null) {
	    setResponseValue(getQuestionAnswer().getAnswer());
	}
    }

    private void setConditionOptions(StudentInquiryRegistry studentInquiryRegistry) {
	setVisible(getInquiryQuestion().isVisible(studentInquiryRegistry));
	setConditionValues(getInquiryQuestion().getConditionValues(studentInquiryRegistry));
    }

    public void setInquiryQuestion(InquiryQuestion inquiryQuestion) {
	this.inquiryQuestion = inquiryQuestion;
    }

    public InquiryQuestion getInquiryQuestion() {
	return inquiryQuestion;
    }

    public String getResponseValue() {
	return responseValue;
    }

    public void setResponseValue(String responseValue) {
	this.responseValue = responseValue;
    }

    public String getFinalValue() {
	if (getResponseValue() != null) {
	    if (getInquiryQuestion() instanceof InquiryCheckBoxQuestion) {
		if (getResponseValue().equals("on")) {
		    return "1";
		}
		return getResponseValue();
	    }
	    if (getInquiryQuestion() instanceof InquiryTextBoxQuestion) {
		return getResponseValue().isEmpty() ? null : getResponseValue();
	    }
	}
	return getResponseValue();
    }

    public void setVisible(boolean isVisible) {
	this.isVisible = isVisible;
    }

    public boolean isVisible() {
	return isVisible;
    }

    public void setConditionValues(String[] conditionValues) {
	this.conditionValues = conditionValues;
    }

    public String[] getConditionValues() {
	return conditionValues;
    }

    public void setStudentInquiryRegistry(StudentInquiryRegistry studentInquiryRegistry) {
	this.studentInquiryRegistry = studentInquiryRegistry;
    }

    public StudentInquiryRegistry getStudentInquiryRegistry() {
	return studentInquiryRegistry;
    }

    public void setQuestionAnswer(QuestionAnswer questionAnswer) {
	this.questionAnswer = questionAnswer;
    }

    public QuestionAnswer getQuestionAnswer() {
	return questionAnswer;
    }
}
