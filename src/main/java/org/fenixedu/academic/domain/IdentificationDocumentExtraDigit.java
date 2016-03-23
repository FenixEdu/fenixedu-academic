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

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.exceptions.DomainException;

public class IdentificationDocumentExtraDigit extends IdentificationDocumentExtraDigit_Base {

    public IdentificationDocumentExtraDigit() {
        super();
    }

    public IdentificationDocumentExtraDigit(final Person person, final String identificationDocumentExtraDigit) {
        setPerson(person);
        setValue(identificationDocumentExtraDigit);
    }

    public static void validate(final String documentId, final String identificationDocumentExtraDigit) {
        if (identificationDocumentExtraDigit == null || identificationDocumentExtraDigit.isEmpty()
                || identificationDocumentExtraDigit.length() != 1 || !StringUtils.isNumeric(identificationDocumentExtraDigit)) {
            throw new DomainException("label.identificationDocumentExtraDigit.invalid.format");
        }
        if (!isValidBI(documentId + identificationDocumentExtraDigit)) {
            throw new DomainException("label.identificationDocumentExtraDigit.invalid");
        }
    }

    @Override
    public void setValue(String value) {
        validate(getPerson().getDocumentIdNumber(), value);
        super.setValue(value);
    }

    private static boolean isValidBI(final String num) {
        final int l = num.length();
        if (l == 9) {
            int sum = 0;
            for (int i = 1; i <= l; sum += toInt(num.charAt(l - i)) * i++) {
                if (!Character.isDigit(num.charAt(l - i))) {
                    return false;
                }
            }
            return sum % 11 == 0;
        }
        return l == 8 && isValidBI("0" + num);
    }

    private static int toInt(final char c) {
        return Character.isDigit(c) ? Character.getNumericValue(c) : ((int) c) - ((int) 'A') + 10;
    }
}
