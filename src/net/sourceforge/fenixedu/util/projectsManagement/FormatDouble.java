/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.util.projectsManagement;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import net.sourceforge.fenixedu.util.FenixUtil;

/**
 * @author Susana Fernandes
 * 
 */
public class FormatDouble extends FenixUtil {
    public static String convertDoubleToString(double value) {
        DecimalFormatSymbols s = new DecimalFormatSymbols();
        s.setGroupingSeparator('.');
        s.setDecimalSeparator(',');
        DecimalFormat nf = new DecimalFormat("#,##0.00", s);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        String stringValue = nf.format(value);
        return stringValue;
    }

    public static Double parseDouble(String value) {
        double doubleValue = 0;

        if (value != null && !value.equals("")) {
            doubleValue = Double.parseDouble(value);
        }
        return new Double(doubleValue);
    }

    public static double round(double value) {
        DecimalFormatSymbols s = new DecimalFormatSymbols();
        s.setDecimalSeparator('.');
        DecimalFormat nf = new DecimalFormat("#.00", s);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        String stringValue = nf.format(value);
        return parseDouble(stringValue).doubleValue();
    }
}
