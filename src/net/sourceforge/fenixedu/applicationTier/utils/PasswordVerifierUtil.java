package net.sourceforge.fenixedu.applicationTier.utils;

import net.sourceforge.fenixedu._development.PropertiesManager;

public class PasswordVerifierUtil {
    private static Integer MIN_PASSWORD_LENGTH = PropertiesManager.getIntegerProperty("passSize");

    public static boolean isValid(String password) {
	return (has3ClassesOfCharacters(password.toCharArray()) && hasMinLength(password.toCharArray()));
    }

    public static boolean has3ClassesOfCharacters(char[] password) {

	boolean isDigit = false;
	boolean isUpper = false;
	boolean isLower = false;

	for (char c : password) {
	    isDigit |= Character.isDigit(c);
	    isUpper |= Character.isUpperCase(c);
	    isLower |= Character.isLowerCase(c);

	    if (isDigit && isLower && isUpper) {
		return true;
	    }
	}

	return false;
    }

    public static boolean hasMinLength(char[] password) {
	return (password.length >= MIN_PASSWORD_LENGTH);
    }

}
