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
package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class IdentificationDocumentSeriesNumber extends IdentificationDocumentSeriesNumber_Base {

    public IdentificationDocumentSeriesNumber() {
        super();
    }

    public IdentificationDocumentSeriesNumber(final Person person, final String identificationDocumentSeriesNumber) {
        setPerson(person);
        if (identificationDocumentSeriesNumber != null && !identificationDocumentSeriesNumber.isEmpty()) {
            final String trimmedValue = identificationDocumentSeriesNumber.trim().replace(" ", "");
            if (trimmedValue.length() == 4 && Character.isDigit(trimmedValue.charAt(0))
                    && Character.isLetter(trimmedValue.charAt(1)) && Character.isLetter(trimmedValue.charAt(2))
                    && Character.isDigit(trimmedValue.charAt(3))) {
                setValue(trimmedValue);
            } else {
                throw new DomainException("label.identificationDocumentSeriesNumber.invalid.format");
            }
        } else {
            throw new DomainException("label.identificationDocumentSeriesNumber.invalid.format");
        }
    }

}
