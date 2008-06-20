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

    private List<InquiriesQuestion> questions = new ArrayList<InquiriesQuestion>();

    private Map<String, InquiriesQuestion> questionsMap = new HashMap<String, InquiriesQuestion>();

    public InquiriesBlock() {
    }

    public InquiriesBlock(String title) {
	this.header = new QuestionHeader(title);
    }

    public InquiriesBlock(String title, String... scaleHeaders) {
	this.header = new QuestionHeader(title, scaleHeaders);
    }

    public void addQuestion(InquiriesQuestion question) {
	this.questionsMap.put(question.getLabel(), question);
	this.questions.add(question);
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

}
