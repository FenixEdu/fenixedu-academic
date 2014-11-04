/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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
        if (StringUtils.isEmpty(value)) {
            return null;
        }

        if (value.length() < 2) {
            return null;
        }

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

            if (result.isAfter(DateTimeFormat.forPattern("yyyy").parseDateTime("1920").toLocalDate())
                    && result.isBefore(DateTimeFormat.forPattern("yyy").parseDateTime("2020").toLocalDate())) {
                return result;
            }
        }

        throw new IncorrectDateFormatException(value);
    }

    static public Gender parseGender(String value) {

        if ("M".equals(value)) {
            return Gender.MALE;
        }

        if ("F".equals(value)) {
            return Gender.FEMALE;
        }

        throw new InvalidGenderValueException();
    }

    static public Country translateNationality(String value) {
        return NationalityTranslator.translate(value);
    }
}
