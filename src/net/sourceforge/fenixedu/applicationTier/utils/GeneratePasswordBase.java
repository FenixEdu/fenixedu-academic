package net.sourceforge.fenixedu.applicationTier.utils;

import net.sourceforge.fenixedu.domain.IPerson;

import org.apache.commons.lang.RandomStringUtils;

public class GeneratePasswordBase implements IGeneratePassword {

	public String generatePassword(IPerson person) {
		return RandomStringUtils.randomAlphanumeric(8);	
	}
}
