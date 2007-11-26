package net.sourceforge.fenixedu.renderers.validators;

import net.sourceforge.fenixedu.renderers.components.HtmlSimpleValueComponent;
import net.sourceforge.fenixedu.renderers.components.Validatable;

public class DegreeIdCardNameValidator extends HtmlValidator {

    public DegreeIdCardNameValidator(final Validatable component) {
	super(component);
	setMessage("renderers.validator.degree.id.card.name");
    }

    @Override
    public void performValidation() {
	final Validatable component = getComponent();
	if (component instanceof HtmlSimpleValueComponent) {
	    final String value = component.getValue();
	    setValid(value != null && value.length() > 0 && value.length() <= 42);
	} else {
	    setValid(false);
	}
    }

}
