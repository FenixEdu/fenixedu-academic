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

import org.apache.commons.lang.StringUtils;

public class PersonNameFormatter extends StringFormatter {

    public static String prettyPrint(String uglyDuckling) {

        if (StringUtils.isEmpty(uglyDuckling)) {
            return uglyDuckling;
        }

        uglyDuckling = removeDuplicateSpaces(uglyDuckling.trim());
        String[] lowerCaseName = uglyDuckling.toLowerCase().split(" ");
        StringBuilder capitalizedName = new StringBuilder();

        for (int i = 0; i < lowerCaseName.length; i++) {

            if (!containsNoneSpecialChars(lowerCaseName[i]) && !allCapSet.contains(lowerCaseName[i])) {
                capitalizedName.append(capitalizeWordWithSpecChars(lowerCaseName[i]));
            } else {
                // Exception to the general case: if "A" is the last
                // word
                // converts to UPPERCASE
                // (needed for courses that occur in alternative
                // semesters)
                if (i == (lowerCaseName.length - 1) & lowerCaseName[i].equals("a")) {
                    capitalizedName.append(lowerCaseName[i].toUpperCase());
                } else {
                    capitalizedName.append(capitalizeWord(lowerCaseName[i], false));
                }
            }

            capitalizedName.append(" ");
        }

        return capitalizedName.toString().substring(0, capitalizedName.length() - 1);
    }

}
