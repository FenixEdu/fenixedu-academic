package net.sourceforge.fenixedu.presentationTier.renderers.validators;

import pt.ist.fenixWebFramework.renderers.validators.EmailValidator;
import pt.ist.fenixWebFramework.renderers.validators.HtmlChainValidator;

public class RequiredEmailValidator extends EmailValidator {

    public RequiredEmailValidator(HtmlChainValidator htmlChainValidator) {
	super(htmlChainValidator);
    }

    @Override
    public void performValidation() {
	final String email = getComponent().getValue();
	if (email != null && email.length() > 0) {
	    super.performValidation();
	} else {
	    setValid(false);
	    setMessage("renderers.validator.required");
	}
    }

}
