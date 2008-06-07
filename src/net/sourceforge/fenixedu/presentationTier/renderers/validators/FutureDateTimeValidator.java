package net.sourceforge.fenixedu.presentationTier.renderers.validators;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.renderers.components.HtmlSimpleValueComponent;
import pt.ist.fenixWebFramework.renderers.validators.HtmlChainValidator;

public class FutureDateTimeValidator extends DateTimeValidator {

    public FutureDateTimeValidator(HtmlChainValidator htmlChainValidator) {
	super(htmlChainValidator);
    }

    @Override
    public void performValidation() {
	HtmlSimpleValueComponent component = (HtmlSimpleValueComponent) getComponent();

	String value = component.getValue();

	if (value == null || value.length() == 0) {
	    setMessage("renderers.validator.dateTime.required");
	    setValid(!isRequired());
	} else {
	    super.performValidation();

	    if (isValid()) {
		DateTime dateTime = new DateTime(value);

		if (dateTime.isBeforeNow()) {
		    setMessage("renderers.validator.dateTime.beforeNow");
		    setValid(false);
		}
	    }
	}
    }

}
