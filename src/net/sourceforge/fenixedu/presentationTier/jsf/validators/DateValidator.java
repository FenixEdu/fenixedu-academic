/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.jsf.validators;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * 
 * @author naat
 * 
 */
public class DateValidator implements Validator, StateHolder {
    private String format;

    private Boolean strict;

    private boolean _transient = false;

    private final String INVALID_DATE = "net.sourceforge.fenixedu.presentationTier.jsf.validators.dateValidator.INVALID_DATE";

    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

	if (value != null) {
	    String val = value.toString();
	    if (org.apache.commons.validator.DateValidator.getInstance().isValid(val, this.getFormat(), this.getStrict()) == false) {
		ResourceBundle resourceBundle = ResourceBundle.getBundle(context.getApplication().getMessageBundle(), context
			.getViewRoot().getLocale());

		String errorMessage = MessageFormat.format(resourceBundle.getString(INVALID_DATE), new Object[] { this
			.getFormat() });

		throw new ValidatorException(new FacesMessage(errorMessage));
	    }
	}
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

    public Object saveState(FacesContext context) {
	Object[] state = new Object[2];
	state[0] = this.getFormat();
	state[1] = this.getStrict();
	return state;
    }

    public void restoreState(FacesContext context, Object storedState) {
	Object[] state = (Object[]) storedState;
	setFormat((String) state[0]);
	setStrict((Boolean) state[1]);
    }

    public boolean isTransient() {
	return _transient;
    }

    public void setTransient(boolean arg0) {
	_transient = arg0;
    }
}
