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

import java.util.Collection;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.curricularRules.ICurricularRule;
import org.fenixedu.academic.domain.curricularRules.RestrictionBetweenDegreeModules;
import org.fenixedu.academic.domain.curricularRules.executors.RuleResult;
import org.fenixedu.academic.domain.degreeStructure.CycleCourseGroup;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.enrolment.EnrolmentContext;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumModule;
import org.fenixedu.academic.domain.studentCurriculum.CycleCurriculumGroup;

public class RestrictionBetweenDegreeModulesExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentVerificationWithRules(final ICurricularRule curricularRule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {

        final RestrictionBetweenDegreeModules rule = (RestrictionBetweenDegreeModules) curricularRule;
        if (!canApplyRule(enrolmentContext, rule)) {
            return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
        }

        final DegreeModule precedenceDegreeModule = rule.getPrecedenceDegreeModule();

        if (isEnrolling(enrolmentContext, precedenceDegreeModule)) {
            return rule.hasMinimumCredits() ? createFalseRuleResultWithInvalidEcts(rule, sourceDegreeModuleToEvaluate) : RuleResult
                    .createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());

        } else if (isEnroled(enrolmentContext, precedenceDegreeModule)) {

            final CurriculumModule curriculumModule = searchCurriculumModule(enrolmentContext, precedenceDegreeModule);

            if (!rule.hasMinimumCredits() || rule.allowCredits(curriculumModule.getAprovedEctsCredits())) {
                return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
            } else {
                if (sourceDegreeModuleToEvaluate.isEnroled() && sourceDegreeModuleToEvaluate.isLeaf()) {
                    return createImpossibleRuleResultWithInvalidEcts(rule, sourceDegreeModuleToEvaluate);
                } else {
                    return createFalseRuleResultWithInvalidEcts(rule, sourceDegreeModuleToEvaluate);
                }
            }
        }

        return RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule(),
                "curricularRules.ruleExecutors.RestrictionBetweenDegreeModulesExecutor.student.has.not.precedence.degreeModule",
                rule.getDegreeModuleToApplyRule().getName(), rule.getPrecedenceDegreeModule().getName());
    }

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final ICurricularRule curricularRule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {

        final RestrictionBetweenDegreeModules rule = (RestrictionBetweenDegreeModules) curricularRule;
        if (!canApplyRule(enrolmentContext, rule)) {
            return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
        }

        // final CourseGroup courseGroup = rule.getPrecedenceDegreeModule();
        final DegreeModule precedenceDegreeModule = rule.getPrecedenceDegreeModule();

        if (isEnrolling(enrolmentContext, precedenceDegreeModule)) {
            return rule.hasMinimumCredits() ? createFalseRuleResultWithInvalidEcts(rule, sourceDegreeModuleToEvaluate) : RuleResult
                    .createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());

        } else if (isEnroled(enrolmentContext, precedenceDegreeModule)) {

            if (!rule.hasMinimumCredits()) {
                return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
            }

            final CurriculumModule curriculumModule = searchCurriculumModule(enrolmentContext, precedenceDegreeModule);
            Double ectsCredits = curriculumModule.getAprovedEctsCredits();

            if (rule.allowCredits(ectsCredits)) {
                return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
            }

            ectsCredits =
                    Double.valueOf(ectsCredits.doubleValue()
                            + calculatePreviousPeriodEnroledEctsCredits(enrolmentContext, curriculumModule).doubleValue());

            if (rule.allowCredits(ectsCredits)) {
                return RuleResult.createTrue(EnrolmentResultType.TEMPORARY, sourceDegreeModuleToEvaluate.getDegreeModule());

            } else {
                if (sourceDegreeModuleToEvaluate.isEnroled() && sourceDegreeModuleToEvaluate.isLeaf()) {
                    return createImpossibleRuleResultWithInvalidEcts(rule, sourceDegreeModuleToEvaluate);
                } else {
                    return createFalseRuleResultWithInvalidEcts(rule, sourceDegreeModuleToEvaluate);
                }
            }
        }

        return RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule(),
                "curricularRules.ruleExecutors.RestrictionBetweenDegreeModulesExecutor.student.has.not.precedence.degreeModule",
                rule.getDegreeModuleToApplyRule().getName(), rule.getPrecedenceDegreeModule().getName());
    }

    protected Double calculatePreviousPeriodEnroledEctsCredits(final EnrolmentContext enrolmentContext,
            final CurriculumModule curriculumModule) {
        return enrolmentContext.isToEvaluateRulesByYear() ? curriculumModule.getEnroledEctsCredits(enrolmentContext
                .getExecutionYear().getPreviousExecutionYear()) : curriculumModule.getEnroledEctsCredits(enrolmentContext
                .getExecutionPeriod().getPreviousExecutionPeriod());
    }

    private RuleResult createFalseRuleResultWithInvalidEcts(final RestrictionBetweenDegreeModules rule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate) {
        return RuleResult
                .createFalse(
                        sourceDegreeModuleToEvaluate.getDegreeModule(),
                        "curricularRules.ruleExecutors.RestrictionBetweenDegreeModulesExecutor.invalid.ects.credits.in.precedence.degreeModule",
                        rule.getDegreeModuleToApplyRule().getName(), rule.getPrecedenceDegreeModule().getName(), rule
                                .getMinimumCredits().toString());
    }

    private RuleResult createImpossibleRuleResultWithInvalidEcts(final RestrictionBetweenDegreeModules rule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate) {
        return RuleResult
                .createImpossible(
                        sourceDegreeModuleToEvaluate.getDegreeModule(),
                        "curricularRules.ruleExecutors.RestrictionBetweenDegreeModulesExecutor.invalid.ects.credits.in.precedence.degreeModule",
                        rule.getDegreeModuleToApplyRule().getName(), rule.getPrecedenceDegreeModule().getName(), rule
                                .getMinimumCredits().toString());
    }

    @Override
    protected RuleResult executeEnrolmentInEnrolmentEvaluation(final ICurricularRule curricularRule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
        return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    @Override
    protected boolean canBeEvaluated(ICurricularRule curricularRule, IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate,
            EnrolmentContext enrolmentContext) {

        RestrictionBetweenDegreeModules restrictionBetweenDegreeModules = (RestrictionBetweenDegreeModules) curricularRule;

        Collection<CycleCourseGroup> cycleCourseGroups =
                restrictionBetweenDegreeModules.getPrecedenceDegreeModule().getParentCycleCourseGroups();
        for (CycleCourseGroup cycleCourseGroup : cycleCourseGroups) {
            CycleCurriculumGroup cycleCurriculumGroup =
                    (CycleCurriculumGroup) enrolmentContext.getStudentCurricularPlan().findCurriculumGroupFor(cycleCourseGroup);
            if (cycleCurriculumGroup != null) {
                return true;
            }
        }

        return false;
    }

}
