package net.sourceforge.fenixedu.util;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import pt.utl.ist.fenix.tools.util.StringNormalizer;

public class FenixStringTools {

    public static boolean verifyContainsWithEquality(String originalString, String stringToCompare) {
        if (originalString == null || stringToCompare == null) {
            return false;
        }
        String[] stringOriginalArray = getStrings(originalString);
        String[] stringToCompareArray = getStrings(stringToCompare);
        normalizeStringArray(stringOriginalArray);
        normalizeStringArray(stringToCompareArray);
        return verifyContainsWithEquality(stringOriginalArray, stringToCompareArray);
    }

    private static boolean verifyContainsWithEquality(String[] originalString, String[] stringToCompare) {
        if (stringToCompare == null) {
            return true;
        }

        if (originalString != null) {
            int j, i;
            for (i = 0; i < stringToCompare.length; i++) {
                if (!stringToCompare[i].equals("")) {
                    for (j = 0; j < originalString.length; j++) {
                        if (originalString[j].equals(stringToCompare[i])) {
                            break;
                        }
                    }
                    if (j == originalString.length) {
                        return false;
                    }
                }
            }
            if (i == stringToCompare.length) {
                return true;
            }
        }
        return false;
    }

    private static void normalizeStringArray(String[] string) {
        for (int i = 0; i < string.length; i++) {
            string[i] = StringNormalizer.normalize(string[i]);
        }
    }

    private static String[] getStrings(String string) {
        String[] strings = null;
        if (string != null && !string.trim().equals("")) {
            strings = string.trim().split(" ");
            normalizeStringArray(strings);
        }
        return strings;
    }

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
