package net.sourceforge.fenixedu.renderers.validators;

import net.sourceforge.fenixedu.util.EMail;

public class EmailValidator extends RegexpValidator {

    public EmailValidator(HtmlChainValidator htmlChainValidator) {
	super(htmlChainValidator, EMail.W3C_EMAIL_SINTAX_VALIDATOR);

	setMessage("renderers.validator.email");
    }
}
