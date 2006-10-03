package net.sourceforge.fenixedu.presentationTier.renderers.validators;

import net.sourceforge.fenixedu.renderers.components.HtmlSimpleValueComponent;
import net.sourceforge.fenixedu.renderers.components.Validatable;

import org.joda.time.DateTime;

public class FutureDateTimeValidator extends DateTimeValidator {

	public FutureDateTimeValidator(Validatable component) {
		super(component);
	}

	@Override
	public void performValidation() {
        HtmlSimpleValueComponent component = (HtmlSimpleValueComponent) getComponent();
        
        String value = component.getValue();
        
		if (value == null || value.length() == 0) {
            setMessage("renderers.validator.dateTime.required");
            setValid(! isRequired());
        }
        else {
        	super.performValidation();
        	
            if(isValid()) {
            	DateTime dateTime = new DateTime(value);
            	
            	if(dateTime.isBeforeNow()) {
            		setMessage("renderers.validator.dateTime.beforeNow");
            		setValid(false);
            	}
            }
        }
	}

}
