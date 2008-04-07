package net.sourceforge.fenixedu.presentationTier.renderers.validators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.presentationTier.renderers.MultiLanguageStringInputRenderer.LanguageBean;
import net.sourceforge.fenixedu.renderers.components.HtmlSimpleValueComponent;
import net.sourceforge.fenixedu.renderers.validators.HtmlChainValidator;
import net.sourceforge.fenixedu.renderers.validators.HtmlValidator;

public class MultiLanguageStringValidator extends HtmlValidator {

    public MultiLanguageStringValidator(HtmlChainValidator htmlChainValidator) {
	super(htmlChainValidator);
    }

    @Override
    public void performValidation() {
	HtmlSimpleValueComponent component = (HtmlSimpleValueComponent) getComponent();

	Collection<LanguageBean> beans = LanguageBean.importAllFromString(component.getValue());

	boolean hasRepeatedLanguage = false;
	boolean hasNullLanguage = false;

	List<Language> languages = new ArrayList<Language>();

	for (LanguageBean bean : beans) {
	    // only consider fields not empty
	    if (bean.value != null && bean.value.trim().length() > 0) {
		if (bean.language == null) {
		    hasNullLanguage = true;
		} else if (languages.contains(bean.language)) {
		    hasRepeatedLanguage = true;
		} else {
		    languages.add(bean.language);
		}
	    }
	}

	if (hasRepeatedLanguage) {
	    invalidate("renderers.validator.language.repeated");
	    return;
	}

	if (hasNullLanguage) {
	    invalidate("renderers.validator.language.null");
	    return;
	}

	setValid(true);
    }

    private void invalidate(String message) {
	setValid(false);
	setMessage(message);
    }

}
