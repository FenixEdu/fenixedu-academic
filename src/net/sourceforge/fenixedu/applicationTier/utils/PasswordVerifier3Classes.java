package net.sourceforge.fenixedu.applicationTier.utils;

import com.Ostermiller.util.PasswordVerifier;

public class PasswordVerifier3Classes implements PasswordVerifier {

    public boolean verify(char[] password) {
	return PasswordVerifierUtil.has3ClassesOfCharacters(password);
    }

}
