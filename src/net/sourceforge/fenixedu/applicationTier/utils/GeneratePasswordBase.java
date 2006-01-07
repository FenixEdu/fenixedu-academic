package net.sourceforge.fenixedu.applicationTier.utils;

import net.sourceforge.fenixedu.domain.Person;

import org.apache.commons.lang.RandomStringUtils;

public class GeneratePasswordBase implements IGeneratePassword {

	public String generatePassword(Person person) {
		return RandomStringUtils.randomAlphanumeric(8);	
	}
}
