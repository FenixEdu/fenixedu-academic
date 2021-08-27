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
import java.util.stream.Stream;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.curricularPeriod.CurricularPeriod;
import org.fenixedu.academic.domain.curricularRules.AndRule;
import org.fenixedu.academic.domain.curricularRules.AnyCurricularCourse;
import org.fenixedu.academic.domain.curricularRules.CreditsLimit;
import org.fenixedu.academic.domain.curricularRules.CurricularRule;
import org.fenixedu.academic.domain.curricularRules.CurricularRuleType;
import org.fenixedu.academic.domain.curricularRules.OrRule;
import org.fenixedu.academic.domain.curriculum.CurricularCourseType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.commons.i18n.LocalizedString;

public class OptionalCurricularCourse extends OptionalCurricularCourse_Base {

    protected OptionalCurricularCourse() {
        super();
    }

    /**
     * - This constructor is used to create a 'special' curricular course that
     * will represent any curricular course accordding to a rule
     */
    public OptionalCurricularCourse(CourseGroup parentCourseGroup, String name, String nameEn, CurricularStage curricularStage,
            CurricularPeriod curricularPeriod, Integer term, ExecutionSemester beginExecutionPeriod, ExecutionSemester endExecutionPeriod) {

        this();
        setName(name);
        setNameEn(nameEn);
        setCurricularStage(curricularStage);
        setType(CurricularCourseType.OPTIONAL_COURSE);
        new Context(parentCourseGroup, this, curricularPeriod, term, beginExecutionPeriod, endExecutionPeriod);
    }

    @Override
    public void edit(String name, String nameEn, CurricularStage curricularStage) {
        setName(name);
        setNameEn(nameEn);
        setCurricularStage(curricularStage);
    }

    @Override
    public boolean isOptionalCurricularCourse() {
        return true;
    }

    @Override
    public Double getMaxEctsCredits(final ExecutionSemester executionSemester) {
        final CreditsLimit creditsLimitRule = getCreditsLimitRule(executionSemester);
        if (creditsLimitRule != null) {
            return creditsLimitRule.getMaximumCredits();
        }
        final AnyCurricularCourse anyCurricularCourseRule = getAnyCurricularCourseRule(executionSemester);
        if (anyCurricularCourseRule != null) {
            return anyCurricularCourseRule.hasCredits() ? anyCurricularCourseRule.getCredits() : 0;
        }
        return Double.valueOf(0d);
    }

    @Override
    public Double getMinEctsCredits(ExecutionSemester executionSemester) {
        final CreditsLimit creditsLimitRule = getCreditsLimitRule(executionSemester);
        if (creditsLimitRule != null) {
            return creditsLimitRule.getMinimumCredits();
        }
        final AnyCurricularCourse anyCurricularCourseRule = getAnyCurricularCourseRule(executionSemester);
        if (anyCurricularCourseRule != null) {
            return anyCurricularCourseRule.hasCredits() ? anyCurricularCourseRule.getCredits() : 0;
        }
        return Double.valueOf(0d);
    }

    private AnyCurricularCourse getAnyCurricularCourseRule(final ExecutionSemester executionSemester) {
        final List<AnyCurricularCourse> result =
                (List<AnyCurricularCourse>) getCurricularRules(CurricularRuleType.ANY_CURRICULAR_COURSE, executionSemester);
        // must have only one
        return result.isEmpty() ? null : (AnyCurricularCourse) result.iterator().next();
    }

    @Override
    public LocalizedString getObjectivesI18N(ExecutionSemester period) {
        return new LocalizedString();
    }

    @Override
    public LocalizedString getProgramI18N(ExecutionSemester period) {
        return new LocalizedString();
    }

    @Override
    public LocalizedString getEvaluationMethodI18N(ExecutionSemester period) {
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
    public String getName(ExecutionSemester period) {
        return getBaseName();
    }

    @Override
    public String getNameEn(ExecutionSemester period) {
        return getBaseNameEn();
    }

    @Override
    protected Double calculateEctsCredits(final Integer order, final ExecutionSemester executionSemester) {
        if (getCompetenceCourse() != null) {
            return super.calculateEctsCredits(order, executionSemester);
        }
        return getCurricularRulesSet().stream()
                    .filter(rule -> executionSemester == null || rule.isValid(executionSemester))
                    .flatMap(rule -> explode(rule))
                    .filter(rule -> rule.hasCurricularRuleType(CurricularRuleType.ANY_CURRICULAR_COURSE))
                    .map(AnyCurricularCourse.class::cast)
                    .filter(rule -> rule.getMinimumCredits() != null)
                    .mapToDouble(rule -> rule.getMinimumCredits())
                    .filter(c -> c > 0.0d)
                    .min().orElse(0.0d);
    }

    private static Stream<CurricularRule> explode(final CurricularRule rule) {
        if (rule instanceof AndRule) {
            return ((AndRule) rule).getCurricularRulesSet().stream().flatMap(child -> explode(child));
        }
        if (rule instanceof OrRule) {
            return ((OrRule) rule).getCurricularRulesSet().stream().flatMap(child -> explode(child));
        }
        return Stream.of(rule);
    }

}
