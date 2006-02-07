/**
 * 
 */
package net.sourceforge.fenixedu.util;

import java.util.Locale;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CurricularPeriodLabelFormatter {

    public static String getLabel(CurricularPeriod curricularPeriod) {
        ResourceBundle bundle = ResourceBundle.getBundle("resources/EnumerationResources");
        return getLabel(curricularPeriod, bundle);
    }

    public static String getLabel(CurricularPeriod curricularPeriod, Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("resources/EnumerationResources",
                locale);
        return getLabel(curricularPeriod, bundle);
    }

    public static String getFullLabel(CurricularPeriod curricularPeriod) {
        ResourceBundle bundle = ResourceBundle.getBundle("resources/EnumerationResources");
        return getFullLabel(curricularPeriod, bundle);
    }

    public static String getFullLabel(CurricularPeriod curricularPeriod, Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("resources/EnumerationResources",
                locale);
        return getFullLabel(curricularPeriod, bundle);
    }

    private static String getLabel(CurricularPeriod curricularPeriod, ResourceBundle bundle) {
        StringBuilder result = new StringBuilder();
        buildLabel(curricularPeriod, bundle, result);
        return result.toString();
    }

    private static String getFullLabel(CurricularPeriod curricularPeriod, final ResourceBundle bundle) {
        StringBuilder result = new StringBuilder();
        while (curricularPeriod.hasParent()) {
            buildLabel(curricularPeriod, bundle, result);
            
            curricularPeriod = (CurricularPeriod) curricularPeriod.getParent();
            if (curricularPeriod.hasParent()) {
                result.insert(0, ", ");
            }
        }
        return result.toString();
    }

    private static void buildLabel(CurricularPeriod curricularPeriod, ResourceBundle bundle,
            StringBuilder result) {
        result.insert(0, curricularPeriod.getOrder());
        result.insert(0, " ");
        result.insert(0, bundle.getString(curricularPeriod.getPeriodType().name()));        
    }

}
