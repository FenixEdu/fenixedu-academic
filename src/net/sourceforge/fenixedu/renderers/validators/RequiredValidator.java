package net.sourceforge.fenixedu.renderers.validators;

import net.sourceforge.fenixedu.renderers.components.HtmlSimpleValueComponent;
import net.sourceforge.fenixedu.renderers.components.Validatable;

public class RequiredValidator extends HtmlValidator {

    public RequiredValidator(HtmlChainValidator htmlChainValidator) {
	super(htmlChainValidator);

	setMessage("renderers.validator.required");
    }

    @Override
    public void performValidation() {
	Validatable component = getComponent();

	// TODO: cfgi, clear the semantic and uses of the Validatable interface
	// try to use only the interface instead of a check on the type

	if (component instanceof HtmlSimpleValueComponent) {
	    if (component.getValue() == null) {
		setValid(false);
	    } else {
		setValid(!component.getValue().equals(""));
	    }
	} else {
	    String[] values = component.getValues();

	    if (values == null) {
		setValid(false);
	    } else {
		setValid(values.length > 0);
	    }
	}
    }
}
