/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.jsf.validators;

import javax.faces.validator.Validator;
import javax.faces.webapp.ValidatorTag;
import javax.servlet.jsp.JspException;

/**
 * 
 * @author naat
 * 
 */
public class DateValidatorTag extends ValidatorTag {

    /**
     * 
     */
    private static final long serialVersionUID = 2570906320417113147L;

    private String format;

    private Boolean strict;

    public DateValidatorTag() {
	super();
	super.setValidatorId("dateValidator");
    }

    protected Validator createValidator() throws JspException {
	DateValidator dateValidator = (DateValidator) super.createValidator();
	dateValidator.setFormat(getFormat());
	dateValidator.setStrict(getStrict());

	return dateValidator;
    }

    public String getFormat() {
	return format;
    }

    public void setFormat(String format) {
	this.format = format;
    }

    public Boolean getStrict() {
	return strict;
    }

    public void setStrict(Boolean strict) {
	this.strict = strict;
    }

}
