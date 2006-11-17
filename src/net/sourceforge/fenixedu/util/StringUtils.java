package net.sourceforge.fenixedu.util;

import java.util.Arrays;
import java.util.List;

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

    public static String normalize(String string) {
        return StringNormalizer.normalize(string);
    }

    public static String multipleLineRightPad(int LINE_LENGTH, String field, char fillPaddingWith) {
	if (field.length() <= LINE_LENGTH) {
	    return org.apache.commons.lang.StringUtils.rightPad(field + " ", LINE_LENGTH, fillPaddingWith);    
	} else {
	    final List<String> words = Arrays.asList(field.split(" "));
	    int currentLineLength = 0;
	    String result = org.apache.commons.lang.StringUtils.EMPTY;
	    
	    for (final String word : words) {
		final String toAdd = word + " ";
		if (words.lastIndexOf(word) != (words.size() - 1)
			&& (currentLineLength + toAdd.length()) > LINE_LENGTH) {
		    int spacesLength = LINE_LENGTH - currentLineLength;
		    result = org.apache.commons.lang.StringUtils.rightPad(result, result.length() + spacesLength, ' ');
		    
		    currentLineLength = toAdd.length();
		} else {
		    currentLineLength += toAdd.length();
		}
		
		result += toAdd;
	    }
	    
	    if (currentLineLength <= LINE_LENGTH) {
		return org.apache.commons.lang.StringUtils.rightPad(result, result.length() + (LINE_LENGTH - currentLineLength), fillPaddingWith);
	    } 
	    
	    return result;
	}
    }
    
}
