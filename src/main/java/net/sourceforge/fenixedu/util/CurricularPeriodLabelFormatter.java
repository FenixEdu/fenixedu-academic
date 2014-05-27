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
/**
 * 
 */
package net.sourceforge.fenixedu.util;

import java.util.Locale;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;

import org.fenixedu.commons.i18n.I18N;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CurricularPeriodLabelFormatter {

    public static String getLabel(CurricularPeriod curricularPeriod, boolean abbreviated) {
        final Locale locale = I18N.getLocale();
        ResourceBundle bundle = ResourceBundle.getBundle("resources/EnumerationResources", locale);
        return getLabel(curricularPeriod, bundle, abbreviated);
    }

    public static String getFullLabel(CurricularPeriod curricularPeriod, boolean abbreviated) {
        final Locale locale = I18N.getLocale();
        ResourceBundle bundle = ResourceBundle.getBundle("resources/EnumerationResources", locale);
        return getFullLabel(curricularPeriod, bundle, abbreviated);
    }

    public static String getFullLabelI18N(CurricularPeriod curricularPeriod, boolean abbreviated, Locale locale) {

        ResourceBundle bundle = ResourceBundle.getBundle("resources/EnumerationResources", locale);
        return getFullLabel(curricularPeriod, bundle, abbreviated);
    }

    private static String getLabel(CurricularPeriod curricularPeriod, ResourceBundle bundle, boolean abbreviated) {
        StringBuilder result = new StringBuilder();
        buildLabel(curricularPeriod, bundle, result, abbreviated);
        return result.toString();
    }

    private static String getFullLabel(CurricularPeriod curricularPeriod, final ResourceBundle bundle, boolean abbreviated) {
        StringBuilder result = new StringBuilder();
        while (curricularPeriod.hasParent()) {
            buildLabel(curricularPeriod, bundle, result, abbreviated);

            curricularPeriod = curricularPeriod.getParent();
            if (curricularPeriod.hasParent()) {
                result.insert(0, ", ");
            }
        }
        return result.toString();
    }

    private static void buildLabel(CurricularPeriod curricularPeriod, ResourceBundle bundle, StringBuilder result,
            boolean abbreviated) {
        result.insert(0, bundle
                .getString((abbreviated) ? curricularPeriod.getAcademicPeriod().getAbbreviatedName() : curricularPeriod
                        .getAcademicPeriod().getName()));
        result.insert(0, " ");
        result.insert(0, curricularPeriod.getChildOrder());
    }

}
