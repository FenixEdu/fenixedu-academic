package net.sourceforge.fenixedu.applicationTier.utils;

import java.security.SecureRandom;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.Person;

import com.Ostermiller.util.PasswordVerifier;
import com.Ostermiller.util.RandPass;

public class GeneratePasswordBase implements IGeneratePassword {

    protected RandPass randPass;

    public GeneratePasswordBase() {
	inicializeRandandomPassGenerator();
    }

    private void inicializeRandandomPassGenerator() {
	try {
	    randPass = new RandPass(SecureRandom.getInstance("SHA1PRNG"));
	    randPass.setMaxRepetition(1);
	    randPass.addVerifier(getPasswordVerifier());
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }

    private PasswordVerifier getPasswordVerifier() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
	String stringClass = PropertiesManager.getProperty("passVerifier");
	if (stringClass != null) {
	    Class clazz = Class.forName(stringClass);
	    return (PasswordVerifier) clazz.newInstance();
	}
	return null;
    }

    public String generatePassword(Person person) {

	return randPass.getPass(PropertiesManager.getIntegerProperty("passSize"));

    }
}
