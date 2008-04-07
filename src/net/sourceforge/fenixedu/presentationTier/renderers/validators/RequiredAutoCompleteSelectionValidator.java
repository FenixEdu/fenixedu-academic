package net.sourceforge.fenixedu.presentationTier.renderers.validators;

import net.sourceforge.fenixedu.presentationTier.renderers.AutoCompleteInputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlSimpleValueComponent;
import net.sourceforge.fenixedu.renderers.validators.HtmlChainValidator;
import net.sourceforge.fenixedu.renderers.validators.HtmlValidator;

public class RequiredAutoCompleteSelectionValidator extends HtmlValidator {

    public RequiredAutoCompleteSelectionValidator(HtmlChainValidator htmlChainValidator) {
	super(htmlChainValidator);

	setMessage("renderers.validator.autoComplete.required");
    }

    @Override
    public void performValidation() {
	HtmlSimpleValueComponent component = (HtmlSimpleValueComponent) getComponent();

	String value = component.getValue();
	if (value == null || value.length() == 0 || value.equals(AutoCompleteInputRenderer.TYPING_VALUE)) {
	    setValid(false);
	} else {
	    setValid(true);
	}
    }

}
