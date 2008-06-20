/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public abstract class InquiriesQuestion implements Serializable {

    private String label;

    private String value;

    private QuestionHeader header;

    public InquiriesQuestion(final String label) {
	super();
	this.label = label;
    }

    public InquiriesQuestion(final String label, QuestionHeader header) {
	this(label);
	this.header = header;
    }

    public String getValue() {
	return value;
    }

    public Boolean getValueAsBoolean() {
	throw new DomainException("Value not convertible do Boolean");
    }

    public Integer getValueAsInteger() {
	throw new DomainException("Value not convertible do Integer");
    }

    public void setValue(String value) {
	this.value = value;
    }

    public String getLabel() {
	return label;
    }

    public void setLabel(String label) {
	this.label = label;
    }

    public QuestionHeader getHeader() {
	return header;
    }

    public void setHeader(QuestionHeader header) {
	this.header = header;
    }

    public boolean hasHeader() {
	return this.header != null;
    }

}
