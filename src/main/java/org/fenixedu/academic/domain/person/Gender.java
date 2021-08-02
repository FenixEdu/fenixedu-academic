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
package org.fenixedu.academic.domain.person;

import java.util.Locale;

import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.I18N;

import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;

public enum Gender implements IPresentableEnum {

    MALE, FEMALE, OTHER;

    /**
     * Returns a localized String of Genders
     * 
     * @see GenderHelper#toLocalizedString(Gender, Locale)
     * 
     * @param locale
     *            The Locale for which to get the localized version
     * @return A string representation based on chosen locale
     */
    public String toLocalizedString(Locale locale) {
        return BundleUtil.getString(Bundle.APPLICATION, locale, name());
    }

    /**
     * Returns a localized String of Genders based on the default locale
     * 
     * @see GenderHelper#toLocalizedString(Gender)
     * 
     * @return A string representation based on chosen locale
     */
    public String toLocalizedString() {
        return BundleUtil.getString(Bundle.APPLICATION, I18N.getLocale(), name());
    }

    /**
     * Returns a Gender based on the name parameter... This name should be equal
     * to the enum's member name
     * 
     * @see GenderHelper#parseGender(String)
     * 
     * @param name
     *            The enum's member name
     * @return A Gender parsed from the name or null if none is found
     */
    public static Gender parseGender(String name) {
        return Gender.valueOf(name.toUpperCase());
    }

    @Override
    public String getLocalizedName() {
        return BundleUtil.getString(Bundle.ENUMERATION, this.getClass().getName() + "." + name());
    }
}
