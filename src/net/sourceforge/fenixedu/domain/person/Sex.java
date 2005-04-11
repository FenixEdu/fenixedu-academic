package net.sourceforge.fenixedu.domain.person;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.struts.util.LabelValueBean;


public enum Sex {

	MALE, FEMALE;

	private static final Map<Locale, LabelValueBean[]> sexLabelValuesByLocale = new HashMap(2);

	public static LabelValueBean[] getSexLabelValues(Locale locale) {
		 if (locale == null) {
			 locale = Locale.getDefault();
		 }
		 LabelValueBean[] labelValueBeans = sexLabelValuesByLocale.get(locale);
		 if (labelValueBeans != null) {
			 return labelValueBeans;
		 }

		 final ResourceBundle resourceBundle = ResourceBundle.getBundle("ServidorApresentacao.ApplicationResources", locale);
		 labelValueBeans = new LabelValueBean[] {
				 new LabelValueBean(resourceBundle.getString(Sex.MALE.toString()), Sex.MALE.toString()),
				 new LabelValueBean(resourceBundle.getString(Sex.FEMALE.toString()), Sex.FEMALE.toString())
		 };
		 sexLabelValuesByLocale.put(locale, labelValueBeans);
		 return labelValueBeans;
	}

}
