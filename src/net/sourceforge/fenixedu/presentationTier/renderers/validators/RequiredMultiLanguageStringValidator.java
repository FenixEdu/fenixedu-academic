package net.sourceforge.fenixedu.presentationTier.renderers.validators;

import java.util.Collection;

import net.sourceforge.fenixedu.presentationTier.renderers.MultiLanguageStringInputRenderer.LanguageBean;
import pt.ist.fenixWebFramework.renderers.components.HtmlSimpleValueComponent;
import pt.ist.fenixWebFramework.renderers.validators.HtmlChainValidator;

public class RequiredMultiLanguageStringValidator extends MultiLanguageStringValidator {

    public RequiredMultiLanguageStringValidator(HtmlChainValidator htmlChainValidator) {
	super(htmlChainValidator);

	setMessage("renderers.validator.language.required");
    }

    @Override
    public void performValidation() {
	super.performValidation();

	if (!isValid()) {
	    return;
	}

	HtmlSimpleValueComponent component = (HtmlSimpleValueComponent) getComponent();
	Collection<LanguageBean> beans = LanguageBean.importAllFromString(component.getValue());

	for (LanguageBean bean : beans) {
	    if (bean.value != null && bean.value.length() > 0) {
		setValid(true);
		return;
	    }
	}

	setValid(false);
    }
}
