package net.sourceforge.fenixedu.renderers.validators;

import net.sourceforge.fenixedu.renderers.components.Validatable;

public class DateValidator extends RequiredValidator {

    private String dateFormat;

    private boolean required;

    /**
         * Required constructor.
         */
    public DateValidator(Validatable component) {
	this(component, "dd/MM/yyyy");
    }

    public DateValidator(Validatable component, String dateFormat) {
	super(component);

	setDateFormat(dateFormat);
	setRequired(true);
	// default messsage
	setKey(true);

    }

    public String getDateFormat() {
	return this.dateFormat;
    }

    public void setDateFormat(String dateFormat) {
	this.dateFormat = dateFormat;
    }

    public boolean isRequired() {
	return required;
    }

    public void setRequired(boolean required) {
	this.required = required;
    }

    @Override
    public void performValidation() {
	super.performValidation();

	if (isValid()) {
	    String text = getComponent().getValue();

	    setValid(org.apache.commons.validator.DateValidator.getInstance().isValid(text, getDateFormat(),
		    true));

	    final String[] nodes = text.split("/");
	    for (int i = 0; i < nodes.length; i++) {
		try {
		    Integer.parseInt(nodes[i]);
		} catch (Exception e) {
		    setMessage("renderers.validator.date");
		    setValid(false);
		    return;
		}
	    }
	    if (!isValid()) {
		setMessage("renderers.validator.date");
	    }
	} else {
	    setValid(!isRequired());
	}
    }
}
