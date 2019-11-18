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
/**
 * 
 */
package org.fenixedu.academic.util;

import java.util.Locale;

import org.fenixedu.academic.domain.curricularPeriod.CurricularPeriod;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.I18N;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CurricularPeriodLabelFormatter {

    public static String getFullLabelI18N(CurricularPeriod curricularPeriod, Integer term, boolean abbreviated, Locale locale) {
        StringBuilder result = new StringBuilder();
        while (curricularPeriod.getParent() != null) {
            buildLabel(curricularPeriod, term, result, abbreviated, locale);

            curricularPeriod = curricularPeriod.getParent();
            term = null;
            if (curricularPeriod.getParent() != null) {
                result.insert(0, ", ");
            }
        }
        return result.toString();
    }

    public static String getLabel(CurricularPeriod curricularPeriod, Integer term, boolean abbreviated) {
        StringBuilder result = new StringBuilder();
        buildLabel(curricularPeriod, term, result, abbreviated, I18N.getLocale());
        return result.toString();
    }

    public static String getFullLabel(CurricularPeriod curricularPeriod, Integer term, boolean abbreviated) {
        return getFullLabelI18N(curricularPeriod, term, abbreviated, I18N.getLocale());
    }

    private static void buildLabel(CurricularPeriod curricularPeriod, Integer term, StringBuilder result, boolean abbreviated, Locale locale) {
        if (term != null) {
            result.insert(0, term.toString());
            result.insert(0, " Q");
        }
        result.insert(0, BundleUtil.getString(Bundle.ENUMERATION, (abbreviated) ? curricularPeriod.getAcademicPeriod()
                .getAbbreviatedName() : curricularPeriod.getAcademicPeriod().getName()));
        result.insert(0, " ");
        result.insert(0, curricularPeriod.getChildOrder());
    }

}
