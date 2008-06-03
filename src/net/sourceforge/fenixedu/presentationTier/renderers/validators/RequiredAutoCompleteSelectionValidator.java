package net.sourceforge.fenixedu.presentationTier.renderers.validators;

import net.sourceforge.fenixedu.presentationTier.renderers.AutoCompleteInputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlSimpleValueComponent;
import pt.ist.fenixWebFramework.renderers.validators.HtmlChainValidator;
import pt.ist.fenixWebFramework.renderers.validators.HtmlValidator;

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
