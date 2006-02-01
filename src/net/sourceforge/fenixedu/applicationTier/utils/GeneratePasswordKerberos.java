package net.sourceforge.fenixedu.applicationTier.utils;

import java.security.SecureRandom;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.util.kerberos.Script;

import org.apache.commons.lang.RandomStringUtils;

import com.Ostermiller.util.PasswordVerifier;
import com.Ostermiller.util.RandPass;

public class GeneratePasswordKerberos implements IGeneratePassword {
	
	private RandPass randPass;
	
	public GeneratePasswordKerberos() {
		inicializeRandandomPassGenerator();
	}

	private void inicializeRandandomPassGenerator() {
		try {
			randPass = new RandPass(SecureRandom.getInstance("SHA1PRNG"));
			randPass.setMaxRepetition(1);
			randPass.addVerifier(getPasswordVerifier());
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	private PasswordVerifier getPasswordVerifier() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		String stringClass = PropertiesManager.getProperty("passVerifier");
		if(stringClass != null) {
			Class clazz = Class.forName(stringClass);
			return (PasswordVerifier) clazz.newInstance();
		}
		return null;
	}

	public String generatePassword(Person person){
		try {
			String password = randPass.getPass(PropertiesManager.getIntegerProperty("passSize"));
	    	if(person.getIstUsername() != null) {
	    		if (person.getIsPassInKerberos()) {
					Script.changeKerberosPass(person.getIstUsername(), password);
				} else {
					Script.createUser(person.getIstUsername(), password);
					person.setIsPassInKerberos(true);
				}
			}
	    	return password;
		}catch (Exception rte) {
			throw new RuntimeException(rte);
		}
	}
}
