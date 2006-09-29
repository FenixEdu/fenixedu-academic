package net.sourceforge.fenixedu.util;

public class StringUtils {

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

    public static boolean verifyContainsWithEquality(String[] originalString, String[] stringToCompare) {
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
            string[i] = StringNormalizer.normalize(string[i]).toLowerCase();
        }
    }

    public static String[] getStrings(String string) {
        String[] strings = null;
        if (string != null && !string.trim().equals("")) {
            strings = string.trim().split(" ");
            normalizeStringArray(strings);
        }
        return strings;
    }

}
