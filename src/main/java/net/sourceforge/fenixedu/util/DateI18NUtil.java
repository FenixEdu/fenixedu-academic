/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.util;

import org.fenixedu.bennu.core.i18n.BundleUtil;

public class DateI18NUtil {

    public static String verboseNumber(int number) {
        String verbose = verboseNumber(number, 1);
        if (verbose.startsWith(" e ")) {
            return verbose.substring(3);
        }
        return verbose.startsWith(" ") ? verbose.substring(1) : verbose;
    }

    private static String verboseNumber(int number, int mult) {
        if (mult == 1 && (number % 100) < 20) {
            if (number / 100 > 0) {
                return verboseNumber(number / 100, mult * 100) + convertPiece(number % 100, mult);
            }
            return convertPiece(number % 100, mult);
        }
        if (number / 10 > 0) {
            return verboseNumber(number / 10, mult * 10) + convertPiece(number % 10, mult);
        }
        return convertPiece(number, mult);
    }

    private static String convertPiece(int number, int mult) {
        return number > 0 ? getLinkage(mult) + BundleUtil.getString(Bundle.ENUMERATION, Integer.toString(number * mult)) : "";
    }

    private static String getLinkage(int mult) {
        return 10 == mult || 1 == mult ? " e " : " ";
    }

}
