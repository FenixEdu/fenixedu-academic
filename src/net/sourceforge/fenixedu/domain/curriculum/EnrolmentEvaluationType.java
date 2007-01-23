package net.sourceforge.fenixedu.domain.curriculum;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.struts.util.LabelValueBean;

/**
 * @author dcs-rjao
 * 
 * 2/Abr/2003
 */
public enum EnrolmentEvaluationType {

    NORMAL,
    
    IMPROVEMENT,
    
    SPECIAL_SEASON,
    
    EQUIVALENCE;
    
    private static final Map<Locale, LabelValueBean[]> enrolmentEvaluationTypeLabelValuesByLocale = new HashMap<Locale, LabelValueBean[]>(2);

    public static LabelValueBean[] getLabelValues(Locale locale) {
         if (locale == null) {
             locale = Locale.getDefault();
         }
         LabelValueBean[] labelValueBeans = enrolmentEvaluationTypeLabelValuesByLocale.get(locale);
         if (labelValueBeans != null) {
             return labelValueBeans;
         }

         final ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.EnumerationResources", locale);
         labelValueBeans = new LabelValueBean[] {
                 new LabelValueBean(resourceBundle.getString(NORMAL.getQualifiedName()), NORMAL.toString()),
                 new LabelValueBean(resourceBundle.getString(IMPROVEMENT.getQualifiedName()), IMPROVEMENT.toString()),
                 new LabelValueBean(resourceBundle.getString(SPECIAL_SEASON.getQualifiedName()), SPECIAL_SEASON.toString()),
                 new LabelValueBean(resourceBundle.getString(EQUIVALENCE.getQualifiedName()), EQUIVALENCE.toString()) 
         };
         enrolmentEvaluationTypeLabelValuesByLocale.put(locale, labelValueBeans);

         return labelValueBeans;
    }
    
    public String getName() {
	return name();
    }

    public String getQualifiedName() {
	return EnrolmentEvaluationType.class.getSimpleName() + "." + name();
    }

    public String getAcronym() {
	return getQualifiedName() + ".acronym";
    }
    
}
