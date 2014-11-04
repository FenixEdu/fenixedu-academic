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

import java.util.List;
import java.util.Locale;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;

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

    public static String getLabel(ICurricularRule curricularRule, Locale locale) {
        return buildLabel(curricularRule, locale);
    }

    private static String buildLabel(ICurricularRule curricularRule, Locale locale) {
        List<GenericPair<Object, Boolean>> labelList = curricularRule.getLabel();
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
