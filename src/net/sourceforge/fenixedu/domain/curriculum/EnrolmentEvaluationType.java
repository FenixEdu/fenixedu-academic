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
    
    EXTERNAL,
    
    EQUIVALENCE,
    
    CLOSED;
    
    //FIRST_SEASON,
    
    //só para a classe Enrolment Evaluation, usada apenas na migracao dos
    //Enrolments para guardar histórico, não usar para mais nada!
    //SECOND_SEASON,
    
    //  só para a classe Enrolment Evaluation, usada apenas na migracao dos
    //  Enrolments para guardar histórico, não usar para mais nada!
    //NO_SEASON;
    
    private static final Map<Locale, LabelValueBean[]> enrolmentEvaluationTypeLabelValuesByLocale = new HashMap(2);

    public static LabelValueBean[] getSexLabelValues(Locale locale) {
         if (locale == null) {
             locale = Locale.getDefault();
         }
         LabelValueBean[] labelValueBeans = enrolmentEvaluationTypeLabelValuesByLocale.get(locale);
         if (labelValueBeans != null) {
             return labelValueBeans;
         }

         final ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.EnumerationResources", locale);
         labelValueBeans = new LabelValueBean[] {
                 new LabelValueBean(resourceBundle.getString(NORMAL.toString()), NORMAL.toString()),
                 new LabelValueBean(resourceBundle.getString(IMPROVEMENT.toString()), IMPROVEMENT.toString()),
                 new LabelValueBean(resourceBundle.getString(SPECIAL_SEASON.toString()), SPECIAL_SEASON.toString()),
                 new LabelValueBean(resourceBundle.getString(EQUIVALENCE.toString()), EQUIVALENCE.toString()) 
         };
         enrolmentEvaluationTypeLabelValuesByLocale.put(locale, labelValueBeans);
         return labelValueBeans;
    }
    
    public String getName() {
	return name();
    }
}
