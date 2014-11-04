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
package org.fenixedu.academic.domain.student;

import java.util.Locale;

import org.fenixedu.academic.domain.organizationalStructure.UniversityUnit;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.I18N;

public enum MobilityProgram {

    SOCRATES,

    ERASMUS,

    MINERVA,

    COVENANT_WITH_AZORES,

    COVENANT_WITH_INSTITUTION {

        @Override
        protected String getSpecificDescription(final Locale locale) {
            return BundleUtil.getString(Bundle.ENUMERATION, getQualifiedName())
                    + UniversityUnit.getInstitutionsUniversityUnit().getName();
        }

    };

    public String getQualifiedName() {
        Class<?> enumClass = this.getClass();
        if (!enumClass.isEnum() && Enum.class.isAssignableFrom(enumClass)) {
            enumClass = enumClass.getEnclosingClass();
        }

        return enumClass.getSimpleName() + "." + name();
    }

    protected String getSpecificDescription(final Locale locale) {
        return BundleUtil.getString(Bundle.ENUMERATION, locale, getQualifiedName());
    }

    public String getDescription() {
        return getSpecificDescription(I18N.getLocale());
    }

    public String getDescription(final Locale locale) {
        return getSpecificDescription(locale);
    }

}
