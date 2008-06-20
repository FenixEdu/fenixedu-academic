/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.inquiries;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class TextBoxQuestion extends InquiriesQuestion {

    public TextBoxQuestion(String label) {
	super(label);
    }

    public TextBoxQuestion(String label, QuestionHeader header) {
	super(label, header);
    }

}
