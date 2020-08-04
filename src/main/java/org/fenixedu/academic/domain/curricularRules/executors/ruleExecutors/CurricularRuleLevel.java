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
package org.fenixedu.academic.domain.curricularRules.executors.ruleExecutors;

public enum CurricularRuleLevel {

    ENROLMENT_WITH_RULES(true),

    @Deprecated
    ENROLMENT_WITH_RULES_AND_TEMPORARY_ENROLMENT(true),

    ENROLMENT_NO_RULES(true),

    ENROLMENT_VERIFICATION_WITH_RULES(true),

    @Deprecated
    IMPROVEMENT_ENROLMENT(false),

    @Deprecated
    SPECIAL_SEASON_ENROLMENT(false),

    EXTRA_ENROLMENT(false),

    PROPAEUDEUTICS_ENROLMENT(false),

    STANDALONE_ENROLMENT(false),

    STANDALONE_ENROLMENT_NO_RULES(false),

    NULL_LEVEL(false),

    ENROLMENT_PREFILTER(true);

    private boolean isNormal;

    private CurricularRuleLevel(boolean isNormal) {
        this.isNormal = isNormal;
    }

    public String getName() {
        return name();
    }

    static public CurricularRuleLevel defaultLevel() {
        return ENROLMENT_WITH_RULES_AND_TEMPORARY_ENROLMENT;
    }

    public boolean isNormal() {
        return isNormal;
    }

}
