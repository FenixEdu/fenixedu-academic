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

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class FenixStringTools {

    private static String rightPad(String field, int LINE_LENGTH, char fillPaddingWith) {
        if (!StringUtils.isEmpty(field) && !field.endsWith(" ")) {
            field += " ";
        }

        return StringUtils.rightPad(field, LINE_LENGTH, fillPaddingWith);
    }

    public static String multipleLineRightPad(String field, int LINE_LENGTH, char fillPaddingWith) {
        if (!StringUtils.isEmpty(field) && !field.endsWith(" ")) {
            field += " ";
        }

        if (field.length() < LINE_LENGTH) {
            return StringUtils.rightPad(field, LINE_LENGTH, fillPaddingWith);
        } else {
            final List<String> words = Arrays.asList(field.split(" "));
            int currentLineLength = 0;
            String result = "";

            for (final String word : words) {
                final String toAdd = word + " ";

                if (currentLineLength + toAdd.length() > LINE_LENGTH) {
                    result = StringUtils.rightPad(result, LINE_LENGTH, fillPaddingWith) + '\n';
                    currentLineLength = toAdd.length();
                } else {
                    currentLineLength += toAdd.length();
                }

                result += toAdd;
            }

            if (currentLineLength < LINE_LENGTH) {
                return StringUtils.rightPad(result, result.length() + (LINE_LENGTH - currentLineLength), fillPaddingWith);
            }

            return result;
        }
    }

    public static String multipleLineRightPadWithSuffix(String field, int LINE_LENGTH, char fillPaddingWith, String suffix) {
        if (!StringUtils.isEmpty(field) && !field.endsWith(" ")) {
            field += " ";
        }

        if (StringUtils.isEmpty(suffix)) {
            return multipleLineRightPad(field, LINE_LENGTH, fillPaddingWith);
        } else if (!suffix.startsWith(" ")) {
            suffix = " " + suffix;
        }

        int LINE_LENGTH_WITH_SUFFIX = LINE_LENGTH - suffix.length();

        if (field.length() < LINE_LENGTH_WITH_SUFFIX) {
            return FenixStringTools.rightPad(field, LINE_LENGTH_WITH_SUFFIX, fillPaddingWith) + suffix;
        } else {
            final List<String> words = Arrays.asList(field.split(" "));
            int currentLineLength = 0;
            String result = "";

            for (final String word : words) {
                final String toAdd = word + " ";

                if (currentLineLength + toAdd.length() > LINE_LENGTH) {
                    result = StringUtils.rightPad(result, LINE_LENGTH, ' ') + '\n';
                    currentLineLength = toAdd.length();
                } else {
                    currentLineLength += toAdd.length();
                }

                result += toAdd;
            }

            if (currentLineLength < LINE_LENGTH_WITH_SUFFIX) {
                return FenixStringTools.rightPad(result, result.length() + (LINE_LENGTH_WITH_SUFFIX - currentLineLength),
                        fillPaddingWith) + suffix;
            } else {
                return FenixStringTools.rightPad(result, result.length() + (LINE_LENGTH - currentLineLength), fillPaddingWith)
                        + "\n" + StringUtils.leftPad(suffix, LINE_LENGTH, fillPaddingWith);
            }
        }
    }

}
