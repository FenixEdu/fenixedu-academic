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
/*
 * Created on Dec 8, 2005
 */
package org.fenixedu.academic.domain.degreeStructure;

import java.util.Locale;

import org.fenixedu.academic.domain.time.calendarStructure.AcademicPeriod;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.I18N;

public enum RegimeType {

    SEMESTRIAL,

    ANUAL;

    public String getName() {
        return name();
    }

    public String getLocalizedName() {
        return getLocalizedName(I18N.getLocale());
    }

    public String getLocalizedName(final Locale locale) {
        return BundleUtil.getString(Bundle.ENUMERATION, locale, getName());
    }

    public String getAcronym() {
        return getAcronym(I18N.getLocale());
    }

    private String getAcronym(final Locale locale) {
        return BundleUtil.getString(Bundle.ENUMERATION, locale, getName() + ".ACRONYM");
    }

    public AcademicPeriod convertToAcademicPeriod() {
        return this == RegimeType.ANUAL ? AcademicPeriod.YEAR : AcademicPeriod.SEMESTER;
    }
}
