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

import java.util.Locale;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateI18NUtil {

    private static final Logger logger = LoggerFactory.getLogger(DateI18NUtil.class);

    public static String verboseNumber(int number, ResourceBundle bundle) {
        String verbose = verboseNumber(number, 1, bundle);
        if (verbose.startsWith(" e ")) {
            return verbose.substring(3);
        }
        if (verbose.startsWith(" ")) {
            return verbose.substring(1);
        }
        return verbose;
    }

    private static String verboseNumber(int number, int mult, ResourceBundle bundle) {
        if (mult == 1 && (number % 100) < 20) {
            if (number / 100 > 0) {
                return verboseNumber(number / 100, mult * 100, bundle) + convertPiece(number % 100, mult, bundle);
            }
            return convertPiece(number % 100, mult, bundle);
        }
        if (number / 10 > 0) {
            return verboseNumber(number / 10, mult * 10, bundle) + convertPiece(number % 10, mult, bundle);
        }
        return convertPiece(number, mult, bundle);
    }

    private static String convertPiece(int number, int mult, ResourceBundle bundle) {
        if (number > 0) {
            return getLinkage(mult) + bundle.getString(Integer.toString(number * mult));
        }
        return "";
    }

    private static String getLinkage(int mult) {
        if (10 == mult || 1 == mult) {
            return " e ";
        }
        return " ";
    }

    public static void main(String[] args) {
        for (int i = 0; i <= 2050; i++) {
            logger.info(verboseNumber(i, ResourceBundle.getBundle("EnumerationResources", new Locale("pt"))));
        }
    }
}
