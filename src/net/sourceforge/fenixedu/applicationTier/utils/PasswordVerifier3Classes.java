package net.sourceforge.fenixedu.applicationTier.utils;

import com.Ostermiller.util.PasswordVerifier;

public class PasswordVerifier3Classes implements PasswordVerifier {

	public boolean verify(char[] password) {
		boolean isDigit = false;
		boolean isUpper = false;
		boolean isLower = false;
	
		for (char c : password) {
			isDigit |= Character.isDigit(c);
			isUpper |= Character.isUpperCase(c);
			isLower |= Character.isLowerCase(c);
			
			if(isDigit && isLower && isUpper) {
				return true;
			}
		}
		return false;
	}

}
