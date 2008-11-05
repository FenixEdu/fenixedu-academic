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

    private boolean integer = false;

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

    @Override
    public Integer getValueAsInteger() {
	try {
	    return getValue() != null ? Integer.valueOf(getValue()) : null;
	} catch (final NumberFormatException ex) {
	    return null;
	}
    }

    @Override
    public void setValue(String value) {
	if (integer) {
	    try {
		Integer.parseInt(value);
	    } catch (NumberFormatException e) {
		return;
	    }
	}
	super.setValue(value);
    }

    public boolean isInteger() {
	return integer;
    }

    public TextBoxQuestion setInteger(boolean integer) {
	this.integer = integer;
	return this;
    }

}
