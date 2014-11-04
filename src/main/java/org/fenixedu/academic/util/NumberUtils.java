/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.util;

import java.text.FieldPosition;
import java.text.NumberFormat;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class NumberUtils {

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