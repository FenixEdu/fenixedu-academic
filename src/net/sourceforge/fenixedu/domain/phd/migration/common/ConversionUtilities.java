package net.sourceforge.fenixedu.domain.phd.migration.common;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.phd.migration.common.exceptions.IncorrectDateFormatException;
import net.sourceforge.fenixedu.domain.phd.migration.common.exceptions.InvalidGenderValueException;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

public class ConversionUtilities {

    static private String[] CORRECT_DATE_PATTERNS = { "ddMMyyyy", "MMyyyy" };

    static public LocalDate parseDate(String value) {
	LocalDate result = null;
	String normalizedValue = value;

	if (value.length() == "dMMyyyy".length()) {
	    normalizedValue = "0".concat(value);
	}

	for (String pattern : CORRECT_DATE_PATTERNS) {
	    try {
		result = DateTimeFormat.forPattern(pattern).parseDateTime(normalizedValue).toLocalDate();
	    } catch (IllegalArgumentException e) {
		continue;
	    }

	    // might want to validate its time frame
	    return result;
	}

	throw new IncorrectDateFormatException();
    }

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
