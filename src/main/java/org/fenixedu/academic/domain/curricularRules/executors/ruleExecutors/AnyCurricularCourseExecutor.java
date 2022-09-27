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

import org.fenixedu.academic.domain.CompetenceCourse;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.curricularRules.AnyCurricularCourse;
import org.fenixedu.academic.domain.curricularRules.ICurricularRule;
import org.fenixedu.academic.domain.curricularRules.executors.RuleResult;
import org.fenixedu.academic.domain.degreeStructure.CompetenceCourseLevelType;
import org.fenixedu.academic.domain.enrolment.EnroledOptionalEnrolment;
import org.fenixedu.academic.domain.enrolment.EnrolmentContext;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.academic.domain.enrolment.OptionalDegreeModuleToEnrol;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.CurricularRuleLabelFormatter;

public class AnyCurricularCourseExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final ICurricularRule curricularRule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
        return executeEnrolmentVerificationWithRules(curricularRule, sourceDegreeModuleToEvaluate, enrolmentContext);
    }

    @Override
    protected RuleResult executeEnrolmentInEnrolmentEvaluation(final ICurricularRule curricularRule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
        return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    @Override
    protected RuleResult executeEnrolmentVerificationWithRules(final ICurricularRule curricularRule,
            IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {

        final AnyCurricularCourse rule = (AnyCurricularCourse) curricularRule;

        if (!rule.appliesToContext(sourceDegreeModuleToEvaluate.getContext())) {
            return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
        }

        final CurricularCourse curricularCourseToEnrol;
        if (sourceDegreeModuleToEvaluate.isEnroling()) {
            final OptionalDegreeModuleToEnrol optionalDegreeModuleToEnrol =
                    (OptionalDegreeModuleToEnrol) sourceDegreeModuleToEvaluate;
            curricularCourseToEnrol = optionalDegreeModuleToEnrol.getCurricularCourse();

            if (isApproved(enrolmentContext, curricularCourseToEnrol) || isEnroled(enrolmentContext, curricularCourseToEnrol)
                    || isApproved(enrolmentContext, optionalDegreeModuleToEnrol.getCurricularCourse())
                    || isEnroled(enrolmentContext, optionalDegreeModuleToEnrol.getCurricularCourse())) {

                return RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule(),
                        "curricularRules.ruleExecutors.AnyCurricularCourseExecutor.already.approved.or.enroled",
                        curricularCourseToEnrol.getName(), rule.getDegreeModuleToApplyRule().getName());
            }

        } else if (sourceDegreeModuleToEvaluate.isEnroled()) {
            curricularCourseToEnrol = (CurricularCourse) ((EnroledOptionalEnrolment) sourceDegreeModuleToEvaluate)
                    .getCurriculumModule().getDegreeModule();
        } else {
            throw new DomainException(
                    "error.curricularRules.executors.ruleExecutors.AnyCurricularCourseExecutor.unexpected.degree.module.to.evaluate");
        }

        final ExecutionInterval executionInterval = enrolmentContext.getExecutionPeriod();
        final Degree degree = curricularCourseToEnrol.getDegree();

        boolean result = true;

        result &= matchesMinCredits(rule, curricularCourseToEnrol, executionInterval);
        result &= matchesMaxCredits(rule, curricularCourseToEnrol, executionInterval);
        result &= matchesDegreesAndDegreeTypes(rule, degree);
        result &= matchesCompetenceCoursesAndLevels(rule, curricularCourseToEnrol, executionInterval);
        result &= matchesUnits(rule, curricularCourseToEnrol, executionInterval);

        if (Boolean.TRUE.equals(rule.getNegation())) {
            result = !result;
        }

        if (result) {
            return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
        } else {
            if (sourceDegreeModuleToEvaluate.isEnroled()) {
                return RuleResult.createImpossibleWithLiteralMessage(sourceDegreeModuleToEvaluate.getDegreeModule(),
                        CurricularRuleLabelFormatter.getLabel(rule));
            } else {
                return RuleResult.createFalseWithLiteralMessage(sourceDegreeModuleToEvaluate.getDegreeModule(),
                        CurricularRuleLabelFormatter.getLabel(rule));
            }
        }

    }

    @Override
    protected RuleResult executeEnrolmentPrefilter(ICurricularRule curricularRule,
            IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, EnrolmentContext enrolmentContext) {

        if (sourceDegreeModuleToEvaluate instanceof OptionalDegreeModuleToEnrol) {
            final RuleResult result =
                    executeEnrolmentVerificationWithRules(curricularRule, sourceDegreeModuleToEvaluate, enrolmentContext);
            return result.isTrue() ? result : RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule());
        }

        return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    private boolean matchesMinCredits(final AnyCurricularCourse rule, final CurricularCourse curricularCourseToEnrol,
            final ExecutionInterval executionInterval) {
        return rule.hasMinimumCredits() ? rule.getMinimumCredits() <= curricularCourseToEnrol
                .getEctsCredits(executionInterval) : true;
    }

    private boolean matchesMaxCredits(final AnyCurricularCourse rule, final CurricularCourse curricularCourseToEnrol,
            final ExecutionInterval executionInterval) {
        return rule.hasMaximumCredits() ? rule.getMaximumCredits() >= curricularCourseToEnrol
                .getEctsCredits(executionInterval) : true;
    }

    private boolean matchesCompetenceCoursesAndLevels(AnyCurricularCourse rule, CurricularCourse courseToEnrol,
            ExecutionInterval executionInterval) {

        final CompetenceCourse competenceCourse = courseToEnrol.getCompetenceCourse();
        if (!rule.getCompetenceCoursesSet().isEmpty()) {
            return rule.getCompetenceCoursesSet().contains(competenceCourse);
        }

        final CompetenceCourseLevelType levelType =
                competenceCourse.findInformationMostRecentUntil(executionInterval).getLevelType();
        return rule.getCompetenceCourseLevelTypesSet().isEmpty()
                || (levelType != null && rule.getCompetenceCourseLevelTypesSet().contains(levelType));
    }

    private boolean matchesDegreesAndDegreeTypes(AnyCurricularCourse rule, Degree degree) {
        if (!rule.getDegreesSet().isEmpty()) {
            return rule.getDegreesSet().contains(degree);
        }

        return rule.getDegreeTypesSet().isEmpty() || rule.getDegreeTypesSet().contains(degree.getDegreeType());
    }

    private boolean matchesUnits(AnyCurricularCourse rule, CurricularCourse courseToEnrol, ExecutionInterval executionInterval) {
        if (rule.getUnitsSet().isEmpty()) {
            return true;
        }

        return courseToEnrol.getCompetenceCourse().getParentUnits(u -> rule.getUnitsSet().contains(u), executionInterval)
                .findAny().isPresent();
    }

}
