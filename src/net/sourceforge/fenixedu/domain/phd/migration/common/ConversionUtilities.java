package net.sourceforge.fenixedu.domain.phd.migration.common;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.phd.migration.common.exceptions.InvalidGenderValueException;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;

public class ConversionUtilities {

    static public Gender parseGender(String value) {
	if (StringUtils.isEmpty(value)) {
	    return null;
	}

	if ("M".equals(value)) {
	    return Gender.MALE;
	}

	if ("F".equals(value)) {
	    return Gender.FEMALE;
	}

	throw new InvalidGenderValueException();
    }

    static public LocalDate parseLocalDate(String value) {
	return null;
    }

    static public Country translateNationality(String value) {
	return NationalityTranslator.translate(value);
    }
}
