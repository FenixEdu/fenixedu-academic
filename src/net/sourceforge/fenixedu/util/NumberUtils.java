package net.sourceforge.fenixedu.util;

import java.text.FieldPosition;
import java.text.NumberFormat;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class NumberUtils extends FenixUtil {

    /**
     * 
     * @param numberToFormat
     * @param decimalPlacement
     * @return the number with the desired decimal placements
     */
    public static Double formatNumber(Double numberToFormat, int decimalPlacement) {

        if (decimalPlacement == 0) {
            return new Double(Math.round(numberToFormat.floatValue()));
        }

        NumberFormat numberFormat = NumberFormat.getInstance();
        StringBuffer stringBuffer = new StringBuffer();
        FieldPosition fieldPosition = new FieldPosition(0);

        numberFormat.setGroupingUsed(false);
        numberFormat.setMaximumFractionDigits(decimalPlacement);

        numberFormat.format(numberToFormat, stringBuffer, fieldPosition);

        int commaPosition = stringBuffer.indexOf(",");

        if (commaPosition != -1) {
            stringBuffer = stringBuffer.replace(commaPosition, commaPosition + 1, ".");
        }

        numberToFormat = new Double(stringBuffer.toString());
        return numberToFormat;
    }

}