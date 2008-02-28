package net.sourceforge.fenixedu.renderers.validators;

import net.sourceforge.fenixedu.renderers.components.Validatable;
import net.sourceforge.fenixedu.util.EMail;

public class EmailValidator extends RegexpValidator {

    public EmailValidator(Validatable component) {
	super(component, EMail.W3C_EMAIL_SINTAX_VALIDATOR);
	
	setMessage("renderers.validator.email");
    }
}
