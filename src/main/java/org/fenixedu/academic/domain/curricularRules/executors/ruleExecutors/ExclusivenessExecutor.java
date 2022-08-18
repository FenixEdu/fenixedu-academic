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

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.curricularRules.Exclusiveness;
import org.fenixedu.academic.domain.curricularRules.ICurricularRule;
import org.fenixedu.academic.domain.curricularRules.executors.RuleResult;
import org.fenixedu.academic.domain.degreeStructure.CycleCourseGroup;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.enrolment.EnrolmentContext;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.academic.domain.studentCurriculum.CycleCurriculumGroup;

public class ExclusivenessExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentVerificationWithRules(final ICurricularRule curricularRule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {

        final Exclusiveness rule = (Exclusiveness) curricularRule;

        if (!canApplyRule(enrolmentContext, rule)) {
            return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
        }

        final DegreeModule degreeModule = rule.getExclusiveDegreeModule();

        if (degreeModule.isLeaf()) {
            final CurricularCourse curricularCourse = (CurricularCourse) degreeModule;

            if (isApproved(enrolmentContext, curricularCourse)
                    || hasPreviousPeriodEnrolmentWithEnroledState(enrolmentContext, curricularCourse)
                    || isEnroled(enrolmentContext, curricularCourse)) {

                if (isEnroled(enrolmentContext, (CurricularCourse) rule.getDegreeModuleToApplyRule())) {
                    return createImpossibleRuleResult(rule, sourceDegreeModuleToEvaluate);
                }

                return createFalseRuleResult(rule, sourceDegreeModuleToEvaluate);
            }
        }

        if (isEnroled(enrolmentContext, degreeModule) || isEnrolling(enrolmentContext, degreeModule)) {
            return createFalseRuleResult(rule, sourceDegreeModuleToEvaluate);
        }

        return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    protected boolean hasPreviousPeriodEnrolmentWithEnroledState(final EnrolmentContext enrolmentContext,
            final CurricularCourse curricularCourse) {

        if (enrolmentContext.isToEvaluateRulesByYear()) {
            return hasEnrolmentWithEnroledState(enrolmentContext, curricularCourse,
                    enrolmentContext.getExecutionYear().getPreviousExecutionYear());
        }

        return hasEnrolmentWithEnroledState(enrolmentContext, curricularCourse,
                enrolmentContext.getExecutionPeriod().getPrevious());
    }

    private RuleResult createFalseRuleResult(final Exclusiveness rule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate) {
        return RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule(),
                "curricularRules.ruleExecutors.ExclusivenessExecutor.exclusive.degreeModule",
                rule.getDegreeModuleToApplyRule().getName(), rule.getExclusiveDegreeModule().getName());
    }

    private RuleResult createImpossibleRuleResult(final Exclusiveness rule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate) {
        return RuleResult.createImpossible(sourceDegreeModuleToEvaluate.getDegreeModule(),
                "curricularRules.ruleExecutors.ExclusivenessExecutor.exclusive.degreeModule",
                rule.getDegreeModuleToApplyRule().getName(), rule.getExclusiveDegreeModule().getName());
    }

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final ICurricularRule curricularRule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {

        final Exclusiveness rule = (Exclusiveness) curricularRule;

        if (!canApplyRule(enrolmentContext, rule)) {
            return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
        }

        final DegreeModule degreeModule = rule.getExclusiveDegreeModule();
        if (degreeModule.isLeaf()) {
            final CurricularCourse curricularCourse = (CurricularCourse) degreeModule;

            if (isApproved(enrolmentContext, curricularCourse)) {
                if (isEnroled(enrolmentContext, (CurricularCourse) rule.getDegreeModuleToApplyRule())) {
                    return createImpossibleRuleResult(rule, sourceDegreeModuleToEvaluate);
                }
                return createFalseRuleResult(rule, sourceDegreeModuleToEvaluate);
            }

            if (hasPreviousPeriodEnrolmentWithEnroledState(enrolmentContext, curricularCourse)) {
                return RuleResult.createTrue(EnrolmentResultType.TEMPORARY, sourceDegreeModuleToEvaluate.getDegreeModule());
            }
        }

        if (isEnroled(enrolmentContext, degreeModule) || isEnrolling(enrolmentContext, degreeModule)) {
            return createFalseRuleResult(rule, sourceDegreeModuleToEvaluate);
        }

        return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    @Override
    protected RuleResult executeEnrolmentInEnrolmentEvaluation(final ICurricularRule curricularRule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
        return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    @Override
    protected RuleResult executeEnrolmentPrefilter(ICurricularRule curricularRule,
            IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, EnrolmentContext enrolmentContext) {
        final Exclusiveness rule = (Exclusiveness) curricularRule;
        final DegreeModule degreeModule = rule.getExclusiveDegreeModule();

        if (degreeModule.isLeaf() && isApproved(enrolmentContext, (CurricularCourse) degreeModule)) {
            //allow unenrol when other is approved and this is enroled
            return sourceDegreeModuleToEvaluate.isEnroled() ? RuleResult.createTrue(
                    sourceDegreeModuleToEvaluate.getDegreeModule()) : createFalseRuleResult(rule, sourceDegreeModuleToEvaluate);
        }

        if ((isEnroled(enrolmentContext, degreeModule) || isEnrolling(enrolmentContext, degreeModule))
                && !sourceDegreeModuleToEvaluate.isEnroled()) {
            return createFalseRuleResult(rule, sourceDegreeModuleToEvaluate);
        }

        return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    @Override
    protected boolean canBeEvaluated(ICurricularRule curricularRule, IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate,
            EnrolmentContext enrolmentContext) {
        Exclusiveness exclusivenessRule = (Exclusiveness) curricularRule;
        
        if (!enrolmentContext.getStudentCurricularPlan().getDegreeType().hasAnyCycleTypes()) {
            return true;
        }

        Collection<CycleCourseGroup> cycleCourseGroups =
                exclusivenessRule.getExclusiveDegreeModule().getParentCycleCourseGroups();
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
