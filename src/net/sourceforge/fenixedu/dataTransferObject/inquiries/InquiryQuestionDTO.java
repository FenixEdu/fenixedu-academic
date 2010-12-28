package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.inquiries.InquiryCheckBoxQuestion;
import net.sourceforge.fenixedu.domain.inquiries.InquiryQuestion;
import net.sourceforge.fenixedu.domain.inquiries.InquiryTextBoxQuestion;

public class InquiryQuestionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private InquiryQuestion inquiryQuestion;
    private String responseValue;

    public InquiryQuestionDTO(InquiryQuestion inquiryQuestion) {
	setInquiryQuestion(inquiryQuestion);
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
}
