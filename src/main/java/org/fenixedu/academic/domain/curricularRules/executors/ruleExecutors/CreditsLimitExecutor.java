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

import java.math.BigDecimal;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.curricularRules.CreditsLimit;
import org.fenixedu.academic.domain.curricularRules.ICurricularRule;
import org.fenixedu.academic.domain.curricularRules.executors.RuleResult;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.enrolment.EnroledCurriculumModuleWrapper;
import org.fenixedu.academic.domain.enrolment.EnrolmentContext;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.academic.domain.enrolment.OptionalDegreeModuleToEnrol;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumModule;

public class CreditsLimitExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentVerificationWithRules(ICurricularRule curricularRule,
            IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, EnrolmentContext enrolmentContext) {
        final CreditsLimit rule = (CreditsLimit) curricularRule;

        if (!canApplyRule(enrolmentContext, rule)) {
            return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
        }

        final DegreeModule degreeModule = rule.getDegreeModuleToApplyRule();
        if (degreeModule.isLeaf() && ((CurricularCourse) degreeModule).isOptionalCurricularCourse()) {
            return evaluateIfCanEnrolToOptionalDegreeModule(enrolmentContext, rule, sourceDegreeModuleToEvaluate);

        } else {

            final IDegreeModuleToEvaluate degreeModuleToEvaluate = searchDegreeModuleToEvaluate(enrolmentContext, rule);
            if (degreeModuleToEvaluate.isEnroled()) {

                final EnroledCurriculumModuleWrapper moduleEnroledWrapper =
                        (EnroledCurriculumModuleWrapper) degreeModuleToEvaluate;
                final CurriculumModule curriculumModule = moduleEnroledWrapper.getCurriculumModule();

                final Double ectsCredits =
                        curriculumModule.getAprovedEctsCredits() + calculateEnroledEctsCredits(enrolmentContext, curriculumModule)
                                + calculateEctsCreditsFromToEnrolCurricularCourses(enrolmentContext, curriculumModule);

                if (rule.creditsExceedMaximum(ectsCredits)) {
                    if (sourceDegreeModuleToEvaluate.isEnroled() && sourceDegreeModuleToEvaluate.isLeaf()) {
                        return createImpossibleResult(rule, sourceDegreeModuleToEvaluate, ectsCredits);
                    } else {
                        return createFalseRuleResult(rule, sourceDegreeModuleToEvaluate, ectsCredits);
                    }
                } else {
                    return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
                }

            } else { // is enrolling now
                return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
            }
        }

    }

    private Double calculateEnroledEctsCredits(EnrolmentContext enrolmentContext, final CurriculumModule curriculumModule) {
        return enrolmentContext.isToEvaluateRulesByYear() ? curriculumModule.getEnroledEctsCredits(enrolmentContext
                .getExecutionYear()) : curriculumModule.getEnroledEctsCredits(enrolmentContext.getExecutionPeriod());
    }

    private Double calculatePreviousPeriodEnroledEctsCredits(EnrolmentContext enrolmentContext,
            final CurriculumModule curriculumModule) {
        return enrolmentContext.isToEvaluateRulesByYear() ? curriculumModule
                .getEnroledEctsCredits(enrolmentContext.getExecutionYear().getPreviousExecutionYear()) : curriculumModule
                        .getEnroledEctsCredits(enrolmentContext.getExecutionPeriod().getPrevious());
    }

    private RuleResult evaluateIfCanEnrolToOptionalDegreeModule(final EnrolmentContext enrolmentContext, final CreditsLimit rule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate) {
        final CurricularCourse curricularCourse;
        if (sourceDegreeModuleToEvaluate.isEnroled()) {
            curricularCourse = (CurricularCourse) sourceDegreeModuleToEvaluate.getDegreeModule();
        } else {
            curricularCourse = ((OptionalDegreeModuleToEnrol) sourceDegreeModuleToEvaluate).getCurricularCourse();
        }

        final ExecutionInterval executionInterval = enrolmentContext.getExecutionPeriod();
        Double ectsCredits = curricularCourse.getEctsCredits(executionInterval);
        return rule.allowCredits(ectsCredits) ? RuleResult.createTrue(sourceDegreeModuleToEvaluate
                .getDegreeModule()) : createFalseRuleResult(rule, sourceDegreeModuleToEvaluate, ectsCredits);
    }

    private Double calculateEctsCreditsFromToEnrolCurricularCourses(final EnrolmentContext enrolmentContext,
            final CurriculumModule parentCurriculumModule) {
        final ExecutionInterval executionInterval = enrolmentContext.getExecutionPeriod();

        BigDecimal result = BigDecimal.ZERO;
        for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext.getDegreeModulesToEvaluate()) {
            if (degreeModuleToEvaluate.isEnroling()
                    && parentCurriculumModule.hasCurriculumModule(degreeModuleToEvaluate.getCurriculumGroup())) {
                result = result.add(BigDecimal.valueOf(degreeModuleToEvaluate.getEctsCredits(executionInterval)));
            }
        }

        return Double.valueOf(result.doubleValue());
    }

    private RuleResult createFalseRuleResult(final CreditsLimit rule, final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate,
            final Double ectsCredits) {
        if (rule.getMinimumCredits().equals(rule.getMaximumCredits())) {
            return RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule(),
                    "curricularRules.ruleExecutors.CreditsLimitExecutor.limit.not.fulfilled",
                    rule.getDegreeModuleToApplyRule().getName(), rule.getMinimumCredits().toString(), ectsCredits.toString());
        } else {
            return RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule(),
                    "curricularRules.ruleExecutors.CreditsLimitExecutor.limits.not.fulfilled",
                    rule.getDegreeModuleToApplyRule().getName(), rule.getMinimumCredits().toString(),
                    rule.getMaximumCredits().toString(), ectsCredits.toString());
        }
    }

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final ICurricularRule curricularRule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {

        final CreditsLimit rule = (CreditsLimit) curricularRule;

        if (!canApplyRule(enrolmentContext, rule)) {
            return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
        }

        final DegreeModule degreeModule = rule.getDegreeModuleToApplyRule();
        if (degreeModule.isLeaf() && ((CurricularCourse) degreeModule).isOptionalCurricularCourse()) {
            return evaluateIfCanEnrolToOptionalDegreeModule(enrolmentContext, rule, sourceDegreeModuleToEvaluate);

        } else {

            final IDegreeModuleToEvaluate degreeModuleToEvaluate = searchDegreeModuleToEvaluate(enrolmentContext, rule);
            if (degreeModuleToEvaluate.isEnroled()) {
                final EnroledCurriculumModuleWrapper moduleEnroledWrapper =
                        (EnroledCurriculumModuleWrapper) degreeModuleToEvaluate;
                final CurriculumModule curriculumModule = moduleEnroledWrapper.getCurriculumModule();

                Double ectsCredits =
                        curriculumModule.getAprovedEctsCredits() + calculateEnroledEctsCredits(enrolmentContext, curriculumModule)
                                + calculateEctsCreditsFromToEnrolCurricularCourses(enrolmentContext, curriculumModule);

                if (rule.creditsExceedMaximum(ectsCredits)) {
                    if (sourceDegreeModuleToEvaluate.isEnroled() && sourceDegreeModuleToEvaluate.isLeaf()) {
                        return createImpossibleResult(rule, sourceDegreeModuleToEvaluate, ectsCredits);
                    } else {
                        return createFalseRuleResult(rule, sourceDegreeModuleToEvaluate, ectsCredits);
                    }
                }

                ectsCredits = Double.valueOf(ectsCredits.doubleValue()
                        + calculatePreviousPeriodEnroledEctsCredits(enrolmentContext, curriculumModule).doubleValue());

                // TODO: remove duplicated ects from anual CurricularCourses

                if (rule.creditsExceedMaximum(ectsCredits)) {
                    return RuleResult.createTrue(EnrolmentResultType.TEMPORARY, sourceDegreeModuleToEvaluate.getDegreeModule(),
                            "curricularRules.ruleExecutors.CreditsLimitExecutor.exceeded.maximum.credits.limit",
                            ectsCredits.toString(), rule.getMaximumCredits().toString(), curriculumModule.getName().getContent());
                } else {
                    return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
                }

            } else { // is enrolling now
                return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
            }
        }
    }

    private RuleResult createImpossibleResult(final CreditsLimit rule, final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate,
            final Double ectsCredits) {
        if (rule.getMinimumCredits().equals(rule.getMaximumCredits())) {
            return RuleResult.createImpossible(sourceDegreeModuleToEvaluate.getDegreeModule(),
                    "curricularRules.ruleExecutors.CreditsLimitExecutor.limit.not.fulfilled",
                    rule.getDegreeModuleToApplyRule().getName(), rule.getMinimumCredits().toString(), ectsCredits.toString());
        } else {
            return RuleResult.createImpossible(sourceDegreeModuleToEvaluate.getDegreeModule(),
                    "curricularRules.ruleExecutors.CreditsLimitExecutor.limits.not.fulfilled",
                    rule.getDegreeModuleToApplyRule().getName(), rule.getMinimumCredits().toString(),
                    rule.getMaximumCredits().toString(), ectsCredits.toString());
        }
    }

    @Override
    protected RuleResult executeEnrolmentInEnrolmentEvaluation(final ICurricularRule curricularRule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
        return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    @Override
    protected boolean canBeEvaluated(ICurricularRule curricularRule, IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate,
            EnrolmentContext enrolmentContext) {
        return true;
    }

}
