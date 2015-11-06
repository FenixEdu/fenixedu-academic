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
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.curricularRules.ICurricularRule;
import org.fenixedu.academic.domain.curricularRules.RestrictionDoneDegreeModule;
import org.fenixedu.academic.domain.curricularRules.executors.RuleResult;
import org.fenixedu.academic.domain.degreeStructure.CycleCourseGroup;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.enrolment.EnrolmentContext;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.academic.domain.studentCurriculum.CycleCurriculumGroup;

public class RestrictionDoneDegreeModuleExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentVerificationWithRules(final ICurricularRule curricularRule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {

        final RestrictionDoneDegreeModule rule = (RestrictionDoneDegreeModule) curricularRule;

        if (!canApplyRule(enrolmentContext, rule)) {
            return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
        }

        final CurricularCourse curricularCourse = rule.getPrecedenceDegreeModule();
        if (isApproved(enrolmentContext, curricularCourse)) {
            return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
        }

        if (isEnrolledOrEnrollingInSameSemester(enrolmentContext, rule)) {
            return createFalseOrImpossibleResult(
                    enrolmentContext,
                    rule,
                    sourceDegreeModuleToEvaluate,
                    "curricularRules.ruleExecutors.RestrictionDoneDegreeModuleExecutor.cannot.enrol.simultaneously.to.degreeModule.and.precedenceDegreeModule");
        }

        return enrolmentContext.isToEvaluateRulesByYear() ? evaluateByYear(sourceDegreeModuleToEvaluate, enrolmentContext, rule,
                curricularCourse) : evaluateBySemester(sourceDegreeModuleToEvaluate, enrolmentContext, rule, curricularCourse);
    }

    private RuleResult evaluateByYear(IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, EnrolmentContext enrolmentContext,
            RestrictionDoneDegreeModule rule, CurricularCourse curricularCourse) {

        if (isEnroled(enrolmentContext, curricularCourse)
                && !hasEnrolmentWithEnroledState(enrolmentContext, curricularCourse, enrolmentContext.getExecutionYear())) {
            //not currently in enroled state
            return createFalseOrImpossibleResult(enrolmentContext, rule, sourceDegreeModuleToEvaluate,
                    "curricularRules.ruleExecutors.RestrictionDoneDegreeModuleExecutor.student.is.not.approved.to.precendenceDegreeModule");
        }

        //precedent must be approved first
        if (isEnrolledOrEnrolling(enrolmentContext, curricularCourse, enrolmentContext.getExecutionYear()
                .getFirstExecutionPeriod())
                && isEnrolledOrEnrolling(enrolmentContext, rule.getDegreeModuleToApplyRule(), enrolmentContext.getExecutionYear()
                        .getLastExecutionPeriod())) {
            return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
        }

        return createFalseOrImpossibleResult(enrolmentContext, rule, sourceDegreeModuleToEvaluate,
                "curricularRules.ruleExecutors.RestrictionDoneDegreeModuleExecutor.student.is.not.approved.to.precendenceDegreeModule");
    }

    private boolean isEnrolledOrEnrollingInSameSemester(EnrolmentContext enrolmentContext, RestrictionDoneDegreeModule rule) {
        for (final ExecutionSemester executionPeriod : enrolmentContext.getExecutionYear().getExecutionPeriodsSet()) {
            if (isEnrolledOrEnrolling(enrolmentContext, rule.getDegreeModuleToApplyRule(), executionPeriod)
                    && isEnrolledOrEnrolling(enrolmentContext, rule.getPrecedenceDegreeModule(), executionPeriod)) {
                return true;
            }
        }
        return false;
    }

    private boolean isEnrolledOrEnrolling(EnrolmentContext enrolmentContext, DegreeModule degreeModule, ExecutionSemester period) {
        final CurricularCourse curricularCourse = (CurricularCourse) degreeModule;
        return hasEnrolmentWithEnroledState(enrolmentContext, curricularCourse, period)
                || isEnrolling(enrolmentContext, curricularCourse, period);
    }

    private RuleResult evaluateBySemester(final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate,
            final EnrolmentContext enrolmentContext, final RestrictionDoneDegreeModule rule,
            final CurricularCourse curricularCourse) {
        return createFalseOrImpossibleResult(enrolmentContext, rule, sourceDegreeModuleToEvaluate,
                "curricularRules.ruleExecutors.RestrictionDoneDegreeModuleExecutor.student.is.not.approved.to.precendenceDegreeModule");
    }

    private RuleResult createFalseOrImpossibleResult(final EnrolmentContext enrolmentContext,
            final RestrictionDoneDegreeModule rule, final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate,
            final String message) {

        if (isEnroled(enrolmentContext, rule.getDegreeModuleToApplyRule())) {
            return createImpossibleRuleResult(rule, sourceDegreeModuleToEvaluate, message);
        }

        return createFalseRuleResult(rule, sourceDegreeModuleToEvaluate, message);
    }

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final ICurricularRule curricularRule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {

        final RestrictionDoneDegreeModule rule = (RestrictionDoneDegreeModule) curricularRule;
        
        final RuleResult ruleResult =
                executeEnrolmentVerificationWithRules(curricularRule, sourceDegreeModuleToEvaluate, enrolmentContext);
        
        if (ruleResult.isFalse() || ruleResult.isImpossibleEnrolmentResultType(sourceDegreeModuleToEvaluate.getDegreeModule())) {
            if (hasPreviousPeriodEnrolmentWithEnroledState(enrolmentContext, rule.getPrecedenceDegreeModule())) {
                return RuleResult.createTrue(EnrolmentResultType.TEMPORARY, sourceDegreeModuleToEvaluate.getDegreeModule());
            }
        }

        return ruleResult;

    }

    private boolean hasPreviousPeriodEnrolmentWithEnroledState(EnrolmentContext enrolmentContext,
            CurricularCourse curricularCourse) {

        if (enrolmentContext.isToEvaluateRulesByYear()) {
            return hasEnrolmentWithEnroledState(enrolmentContext, curricularCourse, enrolmentContext.getExecutionYear()
                    .getPreviousExecutionYear());
        }

        return hasEnrolmentWithEnroledState(enrolmentContext, curricularCourse, enrolmentContext.getExecutionPeriod()
                .getPreviousExecutionPeriod());
    }

    private RuleResult createFalseRuleResult(final RestrictionDoneDegreeModule rule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final String message) {
        return RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule(), message, rule.getDegreeModuleToApplyRule()
                .getName(), rule.getPrecedenceDegreeModule().getName());
    }

    private RuleResult createImpossibleRuleResult(final RestrictionDoneDegreeModule rule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final String message) {
        return RuleResult.createImpossible(sourceDegreeModuleToEvaluate.getDegreeModule(), message, rule
                .getDegreeModuleToApplyRule().getName(), rule.getPrecedenceDegreeModule().getName());
    }

    @Override
    protected RuleResult executeEnrolmentInEnrolmentEvaluation(final ICurricularRule curricularRule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
        return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    @Override
    protected boolean canBeEvaluated(ICurricularRule curricularRule, IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate,
            EnrolmentContext enrolmentContext) {

        RestrictionDoneDegreeModule restrictionDoneDegreeModule = (RestrictionDoneDegreeModule) curricularRule;

        Collection<CycleCourseGroup> cycleCourseGroups =
                restrictionDoneDegreeModule.getPrecedenceDegreeModule().getParentCycleCourseGroups();
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
