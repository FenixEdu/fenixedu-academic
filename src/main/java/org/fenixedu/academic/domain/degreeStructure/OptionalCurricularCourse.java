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
package org.fenixedu.academic.domain.degreeStructure;

import java.util.List;

import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.curricularPeriod.CurricularPeriod;
import org.fenixedu.academic.domain.curricularRules.AnyCurricularCourse;
import org.fenixedu.academic.domain.curricularRules.CreditsLimit;
import org.fenixedu.academic.domain.curricularRules.CurricularRuleType;
import org.fenixedu.academic.domain.curriculum.CurricularCourseType;
import org.fenixedu.commons.i18n.LocalizedString;

public class OptionalCurricularCourse extends OptionalCurricularCourse_Base {

    protected OptionalCurricularCourse() {
        super();
    }

    /**
     * - This constructor is used to create a 'special' curricular course that
     * will represent any curricular course accordding to a rule
     */
    public OptionalCurricularCourse(CourseGroup parentCourseGroup, String name, String nameEn, CurricularPeriod curricularPeriod,
            ExecutionInterval beginExecutionPeriod, ExecutionInterval endExecutionPeriod) {

        this();
        setName(name);
        setNameEn(nameEn);
        new Context(parentCourseGroup, this, curricularPeriod, beginExecutionPeriod, endExecutionPeriod);
    }

    @Override
    public boolean isOptionalCurricularCourse() {
        return true;
    }

    @Override
    public Double getMaxEctsCredits(final ExecutionInterval executionInterval) {
        final CreditsLimit creditsLimitRule = getCreditsLimitRule(executionInterval);
        if (creditsLimitRule != null) {
            return creditsLimitRule.getMaximumCredits();
        }
        final AnyCurricularCourse anyCurricularCourseRule = getAnyCurricularCourseRule(executionInterval);
        if (anyCurricularCourseRule != null) {
            return anyCurricularCourseRule.hasCredits() ? anyCurricularCourseRule.getCredits() : 0;
        }
        return Double.valueOf(0d);
    }

    @Override
    public Double getMinEctsCredits(ExecutionInterval executionInterval) {
        final CreditsLimit creditsLimitRule = getCreditsLimitRule(executionInterval);
        if (creditsLimitRule != null) {
            return creditsLimitRule.getMinimumCredits();
        }
        final AnyCurricularCourse anyCurricularCourseRule = getAnyCurricularCourseRule(executionInterval);
        if (anyCurricularCourseRule != null) {
            return anyCurricularCourseRule.hasCredits() ? anyCurricularCourseRule.getCredits() : 0;
        }
        return Double.valueOf(0d);
    }

    private AnyCurricularCourse getAnyCurricularCourseRule(final ExecutionInterval executionInterval) {
        final List<AnyCurricularCourse> result =
                (List<AnyCurricularCourse>) getCurricularRules(CurricularRuleType.ANY_CURRICULAR_COURSE, executionInterval);
        // must have only one
        return result.isEmpty() ? null : (AnyCurricularCourse) result.iterator().next();
    }

    @Override
    public LocalizedString getObjectivesI18N(ExecutionInterval interval) {
        return new LocalizedString();
    }

    @Override
    public LocalizedString getProgramI18N(ExecutionInterval interval) {
        return new LocalizedString();
    }

    @Override
    public LocalizedString getEvaluationMethodI18N(ExecutionInterval interval) {
        return new LocalizedString();
    }

    @Override
    public String getName() {
        return getBaseName();
    }

    @Override
    public String getNameEn() {
        return getBaseNameEn();
    }

    @Override
    public String getName(ExecutionInterval interval) {
        return getBaseName();
    }

    @Override
    public String getNameEn(ExecutionInterval interval) {
        return getBaseNameEn();
    }

//    @Override
//    public CurricularCourseType getType() {
//        return CurricularCourseType.OPTIONAL_COURSE;
//    }

}
