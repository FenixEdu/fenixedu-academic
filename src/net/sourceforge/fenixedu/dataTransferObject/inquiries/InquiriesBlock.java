/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class InquiriesBlock implements Serializable {

    private QuestionHeader header;

    private Boolean required;

    private List<InquiriesQuestion> questions = new ArrayList<InquiriesQuestion>();

    private Map<String, InquiriesQuestion> questionsMap = new HashMap<String, InquiriesQuestion>();

    public InquiriesBlock(String title, Boolean required) {
	this.header = new QuestionHeader(title);
	this.required = required;

    }

    public InquiriesBlock(String title, Boolean required, String... scaleHeaders) {
	this.header = new QuestionHeader(title, scaleHeaders);
	this.required = required;
    }

    public void addQuestion(InquiriesQuestion question) {
	this.questionsMap.put(question.getLabel(), question);
	this.questions.add(question);
	if (question.getRequired() == null) {
	    question.setRequired(getRequired());
	}
    }

    public InquiriesQuestion getQuestion(String label) {
	return questionsMap.get(label);
    }

    public Collection<InquiriesQuestion> getQuestions() {
	return questions;
    }

    public boolean hasHeader() {
	return this.header != null;
    }

    public QuestionHeader getHeader() {
	return header;
    }

    public Boolean getRequired() {
	return required;
    }

    public boolean validate() {
	for (InquiriesQuestion question : getQuestions()) {
	    if (question.getRequired() && question.isEmpty()) {
		return false;
	    }
	}
	return true;
    }

}
