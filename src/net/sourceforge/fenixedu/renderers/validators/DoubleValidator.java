package net.sourceforge.fenixedu.renderers.validators;

import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

public class DoubleValidator extends HtmlValidator {

    public DoubleValidator(HtmlChainValidator htmlChainValidator) {
	super(htmlChainValidator);
    }

    @Override
    public String getErrorMessage() {
	return RenderUtils.getResourceString("renderers.validator.number");
    }

    @Override
    public void performValidation() {
	String numberText = getComponent().getValue().trim();

	try {
	    Double.parseDouble(numberText);
	    setValid(true);
	} catch (NumberFormatException e) {
	    setValid(false);
	}
    }
}