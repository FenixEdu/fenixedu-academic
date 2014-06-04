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
package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.Locale;

import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.I18N;

public enum NoCourseGroupCurriculumGroupType {

    PROPAEDEUTICS(CurricularRuleLevel.PROPAEUDEUTICS_ENROLMENT),

    EXTRA_CURRICULAR(CurricularRuleLevel.EXTRA_ENROLMENT),

    STANDALONE(CurricularRuleLevel.STANDALONE_ENROLMENT),

    INTERNAL_CREDITS_SOURCE_GROUP(CurricularRuleLevel.NULL_LEVEL);

    private CurricularRuleLevel level;

    private NoCourseGroupCurriculumGroupType() {
        level = null;
    }

    private NoCourseGroupCurriculumGroupType(final CurricularRuleLevel level) {
        this.level = level;
    }

    public CurricularRuleLevel getCurricularRuleLevel() {
        return level;
    }

    public String getLocalizedName() {
        return getLocalizedName(I18N.getLocale());
    }

    public String getLocalizedName(final Locale locale) {
        return BundleUtil.getString(Bundle.ENUMERATION, locale, getQualifiedName());
    }

    public String getQualifiedName() {
        return getClass().getSimpleName() + "." + name();
    }

}
