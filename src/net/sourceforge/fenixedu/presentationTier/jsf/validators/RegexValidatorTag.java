/*
 * Created on Oct 11, 2005
 *  by jdnf
 */
package net.sourceforge.fenixedu.presentationTier.jsf.validators;

import javax.faces.validator.Validator;
import javax.faces.webapp.ValidatorTag;
import javax.servlet.jsp.JspException;

public class RegexValidatorTag extends ValidatorTag {

    private String regex;

    public RegexValidatorTag() {
	super();
	super.setValidatorId("regexValidator");
    }

    protected Validator createValidator() throws JspException {
	RegexValidator result = (RegexValidator) super.createValidator();
	result.setRegex(regex);
	return result;
    }

    public String getRegex() {
	return regex;
    }

    public void setRegex(String regex) {
	this.regex = regex;
    }

}
