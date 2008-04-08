package net.sourceforge.fenixedu.renderers.validators;

import org.apache.commons.lang.StringUtils;

import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

public class NumberValidator extends HtmlValidator {

    private int base;

    public NumberValidator(HtmlChainValidator htmlChainValidator) {
	super(htmlChainValidator);
	setBase(10);
    }

    public NumberValidator(HtmlChainValidator htmlChainValidator, int base) {
	this(htmlChainValidator);

	setBase(base);
    }

    public int getBase() {
	return this.base;
    }

    public void setBase(int base) {
	this.base = base;
    }

    @Override
    public String getErrorMessage() {
	return RenderUtils.getResourceString("renderers.validator.number");
    }

    @Override
    public void performValidation() {

	String numberText = getComponent().getValue().trim();

	if (!StringUtils.isEmpty(numberText)) {
	    try {
		Integer.parseInt(numberText, getBase());
		setValid(true);
	    } catch (NumberFormatException e) {
		setValid(false);
	    }
	}
    }
}