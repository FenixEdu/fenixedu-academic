package net.sourceforge.fenixedu.applicationTier.utils;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.util.kerberos.Script;

import org.apache.commons.lang.RandomStringUtils;

public class GeneratePasswordKerberos implements IGeneratePassword {

	public String generatePassword(Person person){
		try {
			String password = RandomStringUtils.randomAlphanumeric(8);
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
