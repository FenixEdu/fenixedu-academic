package net.sourceforge.fenixedu.domain.person;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.struts.util.LabelValueBean;

public class GenderHelper {

    public static final String GENDER_RESOURCE_BUNDLE = "resources.ApplicationResources";

    private GenderHelper() {
	super();
    }

    private static final Map<Locale, LabelValueBean[]> sexLabelValuesByLocale = new HashMap<Locale, LabelValueBean[]>(2);

    public static LabelValueBean[] getSexLabelValues(Locale locale) {
	if (locale == null) {
	    locale = Locale.getDefault();
	}
	LabelValueBean[] labelValueBeans = sexLabelValuesByLocale.get(locale);
	if (labelValueBeans != null) {
	    return labelValueBeans;
	}

	final ResourceBundle resourceBundle = ResourceBundle.getBundle(GENDER_RESOURCE_BUNDLE, locale);
	labelValueBeans = new LabelValueBean[] {
		new LabelValueBean(resourceBundle.getString(Gender.MALE.name()), Gender.MALE.name()),
		new LabelValueBean(resourceBundle.getString(Gender.FEMALE.name()), Gender.FEMALE.name()) };
	sexLabelValuesByLocale.put(locale, labelValueBeans);
	return labelValueBeans;
    }

    public static String toLocalizedString(Gender gender, Locale locale) {
	try {
	    final ResourceBundle resourceBundle = ResourceBundle.getBundle(GENDER_RESOURCE_BUNDLE, locale);
	    return resourceBundle.getString(gender.name());
	} catch (Exception swallowed) {
	    return null;
	}
    }

    public static String toLocalizedString(Gender gender) {
	return GenderHelper.toLocalizedString(gender, Locale.getDefault());
    }

    public static Gender parseGender(String name) {
	for (Gender g : Gender.values())
	    if (g.name().equalsIgnoreCase(name))
		return g;

	return null;
    }

}
