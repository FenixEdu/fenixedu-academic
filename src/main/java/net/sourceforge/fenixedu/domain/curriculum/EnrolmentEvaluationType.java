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
package net.sourceforge.fenixedu.domain.curriculum;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.struts.util.LabelValueBean;
import org.fenixedu.commons.i18n.I18N;

import java.util.Locale;

/**
 * @author dcs-rjao
 * 
 *         2/Abr/2003
 */
public enum EnrolmentEvaluationType {

    NORMAL,

    IMPROVEMENT,

    SPECIAL_SEASON,

    EQUIVALENCE;

    private static final Map<Locale, LabelValueBean[]> enrolmentEvaluationTypeLabelValuesByLocale =
            new HashMap<Locale, LabelValueBean[]>(2);

    public static LabelValueBean[] getLabelValues(Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        LabelValueBean[] labelValueBeans = enrolmentEvaluationTypeLabelValuesByLocale.get(locale);
        if (labelValueBeans != null) {
            return labelValueBeans;
        }

        final ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.EnumerationResources", locale);
        labelValueBeans =
                new LabelValueBean[] {
                        new LabelValueBean(resourceBundle.getString(NORMAL.getQualifiedName()), NORMAL.toString()),
                        new LabelValueBean(resourceBundle.getString(IMPROVEMENT.getQualifiedName()), IMPROVEMENT.toString()),
                        new LabelValueBean(resourceBundle.getString(SPECIAL_SEASON.getQualifiedName()), SPECIAL_SEASON.toString()),
                        new LabelValueBean(resourceBundle.getString(EQUIVALENCE.getQualifiedName()), EQUIVALENCE.toString()) };
        enrolmentEvaluationTypeLabelValuesByLocale.put(locale, labelValueBeans);

        return labelValueBeans;
    }

    public String getName() {
        return name();
    }

    public String getQualifiedName() {
        return EnrolmentEvaluationType.class.getSimpleName() + "." + name();
    }

    public String getAcronym() {
        return getQualifiedName() + ".acronym";
    }

    public String getDescription() {
        return ResourceBundle.getBundle("resources.EnumerationResources", I18N.getLocale()).getString(getQualifiedName());
    }

}
