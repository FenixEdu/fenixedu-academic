package net.sourceforge.fenixedu.renderers.validators;

import org.apache.commons.lang.StringUtils;

public class DateValidator extends HtmlValidator {

    private String dateFormat;

    /**
     * Required constructor.
     */
    public DateValidator(HtmlChainValidator htmlChainValidator) {
	this(htmlChainValidator, "dd/MM/yyyy");
    }

    public DateValidator(HtmlChainValidator htmlChainValidator, String dateFormat) {
	super(htmlChainValidator);

	setDateFormat(dateFormat);
	setKey(true);

    }

    public String getDateFormat() {
	return this.dateFormat;
    }

    public void setDateFormat(String dateFormat) {
	this.dateFormat = dateFormat;
    }

    @Override
    public void performValidation() {

	String text = getComponent().getValue();
	
	if (!StringUtils.isEmpty(text)) {
	    setValid(org.apache.commons.validator.DateValidator.getInstance().isValid(text, getDateFormat(), true));
	}

	if (!isValid()) {
	    setMessage("renderers.validator.date");
	}
    }
}
