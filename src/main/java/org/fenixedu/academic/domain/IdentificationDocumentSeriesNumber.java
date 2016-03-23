/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.fenixedu.academic.domain.exceptions.DomainException;

public class IdentificationDocumentSeriesNumber extends IdentificationDocumentSeriesNumber_Base {

    public IdentificationDocumentSeriesNumber() {
        super();
    }

    public IdentificationDocumentSeriesNumber(final Person person, final String identificationDocumentSeriesNumber) {
        setPerson(person);
        setValue(identificationDocumentSeriesNumber);
    }

    public static void validate(final String documentId, final String identificationDocumentSeriesNumber) {
        if (identificationDocumentSeriesNumber != null && !identificationDocumentSeriesNumber.isEmpty()) {
            Pattern pattern = Pattern.compile("^[0-9][A-Z,0-9][A-Z,0-9][0-9]$");
            Matcher matcher = pattern.matcher(identificationDocumentSeriesNumber);
            if (matcher.matches()) {
                if (!isValidCC(documentId + identificationDocumentSeriesNumber)) {
                    throw new DomainException("label.identificationDocumentSeriesNumber.invalid");
                }
            } else {
                throw new DomainException("label.identificationDocumentSeriesNumber.invalid.format");
            }
        } else {
            throw new DomainException("label.identificationDocumentSeriesNumber.invalid.format");
        }
    }

    @Override
    public void setValue(String value) {
        validate(getPerson().getDocumentIdNumber(), value);
        super.setValue(value);
    }

    private static boolean isValidCC(final String num) {
        final int l = num.length();
        if (l == 12) {
            int sum = 0;
            for (int i = 0; i < l; i++, i++) {
                final char c0 = num.charAt(i);
                final char c1 = num.charAt(i + 1);

                if (i != 8 && !Character.isDigit(c1)) {
                    return false;
                }
                if (i != 10 && !Character.isDigit(c0)) {
                    return false;
                }

                final int d0 = toInt(c0) * 2;
                final int d1 = toInt(c1);

                final int d09 = d0 > 9 ? d0 - 9 : d0;
                sum += d09 + d1;
            }
            return sum % 10 == 0;
        }
        return false;
    }

    private static int toInt(final char c) {
        return Character.isDigit(c) ? Character.getNumericValue(c) : ((int) c) - ((int) 'A') + 10;
    }
}
