/*
 * Created on Oct 11, 2005
 *  by jdnf
 */
package net.sourceforge.fenixedu.presentationTier.jsf.validators;

import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

public class RegexValidator implements Validator, StateHolder {
    private String regex;

    private boolean _transient = false;

    private final String INVALID_INPUT = "net.sourceforge.fenixedu.presentationTier.jsf.validators.INVALID_INPUT";

    public void validate(FacesContext _context, UIComponent _component, Object _value) throws ValidatorException {

	String val = "";
	if (_value != null) {
	    val = _value.toString();
	}
	if (!val.matches(regex)) {
	    ResourceBundle resourceBundle = ResourceBundle.getBundle(_context.getApplication().getMessageBundle(), _context
		    .getViewRoot().getLocale());
	    throw new ValidatorException(new FacesMessage(resourceBundle.getString(INVALID_INPUT)));
	}
    }

    public String getRegex() {
	return regex;
    }

    public void setRegex(String regex) {
	this.regex = regex;
    }

    public Object saveState(FacesContext context) {
	return getRegex();
    }

    public void restoreState(FacesContext context, Object state) {
	setRegex((String) state);
    }

    public boolean isTransient() {
	return _transient;
    }

    public void setTransient(boolean arg0) {
	_transient = arg0;
    }
}
