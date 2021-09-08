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

import java.util.List;
import java.util.Locale;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.curricularRules.ICurricularRule;
import org.fenixedu.academic.dto.GenericPair;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.I18N;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CurricularRuleLabelFormatter {

    public static String getLabel(ICurricularRule curricularRule) {
        return buildLabel(curricularRule, I18N.getLocale());
    }
    
    public static String getLabel(ICurricularRule curricularRule, ExecutionSemester executionSemester) {
        return buildLabel(curricularRule, I18N.getLocale(), executionSemester);
    }

    public static String getLabel(ICurricularRule curricularRule, Locale locale) {
        return buildLabel(curricularRule, locale);
    }
    
    public static String getLabel(ICurricularRule curricularRule, Locale locale, ExecutionSemester executionSemester) {
        return buildLabel(curricularRule, locale, executionSemester);
    }

    private static String buildLabel(ICurricularRule curricularRule, Locale locale) {
        return buildLabel(curricularRule, locale, null);
    }
    
    private static String buildLabel(ICurricularRule curricularRule, Locale locale, ExecutionSemester executionSemester) {
        List<GenericPair<Object, Boolean>> labelList = curricularRule.getLabel(executionSemester);
        StringBuilder labelResult = new StringBuilder();
        for (GenericPair<Object, Boolean> labelElement : labelList) {
            if (labelElement.getRight() == true) {
                labelResult.append(BundleUtil.getString(Bundle.BOLONHA, locale, labelElement.getLeft().toString()));
            } else {
                labelResult.append(labelElement.getLeft());
            }
        }

        return labelResult.toString();
    }

}
