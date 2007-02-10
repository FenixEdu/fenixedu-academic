/**
 * 
 */
package net.sourceforge.fenixedu.util;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CurricularRuleLabelFormatter {

    public static String getLabel(CurricularRule curricularRule) {
        ResourceBundle bundle = ResourceBundle.getBundle("resources/BolonhaManagerResources", LanguageUtils.getLocale());
        return buildLabel(curricularRule, bundle);
    }

    public static String getLabel(CurricularRule curricularRule, Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("resources/BolonhaManagerResources", locale);
        return buildLabel(curricularRule, bundle);
    }

    private static String buildLabel(CurricularRule curricularRule, ResourceBundle bundle) {
        List<GenericPair<Object, Boolean>> labelList = curricularRule.getLabel();
        StringBuilder labelResult = new StringBuilder();
        for (GenericPair<Object, Boolean> labelElement : labelList) {
            if (labelElement.getRight() == true) {
                labelResult.append(bundle.getString(labelElement.getLeft().toString()));
            } else {
                labelResult.append(labelElement.getLeft());
            }
        }

        return labelResult.toString();
    }

}
