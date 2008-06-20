/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.inquiries;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CheckBoxQuestion extends InquiriesQuestion {

    public CheckBoxQuestion(String label) {
	super(label);
    }

    public CheckBoxQuestion(String label, QuestionHeader header) {
	super(label, header);
    }

    public Boolean getValueAsBoolean() {
	return getValue() != null && (Boolean.valueOf(getValue()) || getValue().equalsIgnoreCase("on"));
    }

}
