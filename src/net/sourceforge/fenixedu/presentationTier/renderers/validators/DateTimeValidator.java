package net.sourceforge.fenixedu.presentationTier.renderers.validators;

import net.sourceforge.fenixedu.presentationTier.renderers.DateTimeInputRenderer.DateTimeConverter;
import pt.ist.fenixWebFramework.renderers.components.HtmlSimpleValueComponent;
import pt.ist.fenixWebFramework.renderers.validators.HtmlChainValidator;
import pt.ist.fenixWebFramework.renderers.validators.HtmlValidator;

public class DateTimeValidator extends HtmlValidator {

    private boolean required;

    public DateTimeValidator(HtmlChainValidator htmlChainValidator) {
	super(htmlChainValidator);
	setKey(true);
    }

    public boolean isRequired() {
	return this.required;
    }

    public void setRequired(boolean required) {
	this.required = required;
    }

    @Override
    public void performValidation() {
	HtmlSimpleValueComponent component = (HtmlSimpleValueComponent) getComponent();

	String value = component.getValue();

	if (value == null || value.length() == 0) {
	    setMessage("renderers.validator.dateTime.required");
	    setValid(!isRequired());
	} else {
	    if (value.equals(DateTimeConverter.INVALID)) {
		setMessage("renderers.validator.dateTime.invalid");
		setValid(false);
	    } else if (value.equals(DateTimeConverter.INCOMPLETE)) {
		setMessage("renderers.validator.dateTime.incomplete");
		setValid(false);
	    } else {
		int indexOfT = value.indexOf('T');
		System.out.println("value: " + value);
		String[] dateParts = value.substring(0, indexOfT).split("-");
		String[] timeParts = value.substring(indexOfT + 1).split(":");

		try {
		    int year = Integer.valueOf(dateParts[0]);
		    if (year < 1000 || year > 9999) {
			setMessage("renderers.validator.dateTime.invalid");
			setValid(false);
		    } else {
			int hours = Integer.parseInt(timeParts[0]);
			int minutes = Integer.parseInt(timeParts[1]);
			if (inRange(hours, 0, 23) && inRange(minutes, 0, 59)) {
			    setValid(true);
			} else {
			    setMessage("renderers.validator.dateTime.notInRange");
			    setValid(false);
			}
		    }
		} catch (NumberFormatException e) {
		    setMessage("renderers.validator.dateTime.notNumbers");
		    setValid(false);
		}
	    }
	}
    }

    private boolean inRange(int value, int min, int max) {
	return value >= min && value <= max;
    }

}
