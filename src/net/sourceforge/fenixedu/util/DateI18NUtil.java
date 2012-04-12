package net.sourceforge.fenixedu.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class DateI18NUtil {
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
	if (10 == mult || 1 == mult)
	    return " e ";
	return " ";
    }

    public static void main(String[] args) {
	for (int i = 0; i <= 2050; i++) {
	    System.out.println(verboseNumber(i, ResourceBundle.getBundle("EnumerationResources", new Locale("pt"))));
	}
    }
}
