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
