package net.sourceforge.fenixedu.applicationTier.utils;

import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.util.kerberos.UpdateKerberos;

import org.apache.commons.lang.RandomStringUtils;

public class GeneratePasswordKerberos implements IGeneratePassword {

	public String generatePassword(IPerson person){
		try {
			String password = RandomStringUtils.randomAlphanumeric(8);
	    	if(person.getIstUsername() != null) {
	    		if (person.getIsPassInKerberos()) {
					UpdateKerberos.changeKerberosPass(person.getIstUsername(), password);
				} else {
					UpdateKerberos.createUser(person.getIstUsername(), password);
				}
			}
	    	return password;
		}catch (Exception rte) {
			throw new RuntimeException(rte);
		}
	}

}
