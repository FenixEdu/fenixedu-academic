/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.inquiries;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class TextBoxQuestion extends InquiriesQuestion {

    private Boolean textArea;

    public TextBoxQuestion(String label, Boolean textArea) {
	super(label);
	this.textArea = textArea;
    }

    public TextBoxQuestion(String label, QuestionHeader header) {
	super(label, header);
    }

    public Boolean isTextArea() {
	return textArea;
    }

}
