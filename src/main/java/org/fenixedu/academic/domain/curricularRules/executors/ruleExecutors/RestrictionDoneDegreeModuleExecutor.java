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
package net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.RestrictionDoneDegreeModule;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleCourseGroup;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;

public class RestrictionDoneDegreeModuleExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentVerificationWithRules(final ICurricularRule curricularRule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {

        final RestrictionDoneDegreeModule rule = (RestrictionDoneDegreeModule) curricularRule;
        final ExecutionSemester executionSemester = enrolmentContext.getExecutionPeriod();

        if (!canApplyRule(enrolmentContext, rule)) {
            return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
        }

        final CurricularCourse curricularCourse = rule.getPrecedenceDegreeModule();
        if (isEnrolling(enrolmentContext, curricularCourse) || isEnroled(enrolmentContext, curricularCourse, executionSemester)) {
            return RuleResult
                    .createFalse(
                            sourceDegreeModuleToEvaluate.getDegreeModule(),
                            "curricularRules.ruleExecutors.RestrictionDoneDegreeModuleExecutor.cannot.enrol.simultaneously.to.degreeModule.and.precedenceDegreeModule",
                            rule.getDegreeModuleToApplyRule().getName(), rule.getPrecedenceDegreeModule().getName());
        }

        if (isApproved(enrolmentContext, curricularCourse)) {
            return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
        }

        /*
         * CurricularCourse is not approved If DegreeModule is Enroled in
         * current semester then Enrolment must be impossible
         */
        if (isEnroled(enrolmentContext, rule.getDegreeModuleToApplyRule())) {
            return createImpossibleRuleResult(rule, sourceDegreeModuleToEvaluate);
        }

        return createFalseRuleResult(rule, sourceDegreeModuleToEvaluate);
    }

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final ICurricularRule curricularRule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {

        final RestrictionDoneDegreeModule rule = (RestrictionDoneDegreeModule) curricularRule;
        final ExecutionSemester executionSemester = enrolmentContext.getExecutionPeriod();

        if (!canApplyRule(enrolmentContext, rule)) {
            return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
        }

        final CurricularCourse curricularCourse = rule.getPrecedenceDegreeModule();

        if (isEnrolling(enrolmentContext, curricularCourse) || isEnroled(enrolmentContext, curricularCourse, executionSemester)) {
            return RuleResult
                    .createFalse(
                            sourceDegreeModuleToEvaluate.getDegreeModule(),
                            "curricularRules.ruleExecutors.RestrictionDoneDegreeModuleExecutor.cannot.enrol.simultaneously.to.degreeModule.and.precedenceDegreeModule",
                            rule.getDegreeModuleToApplyRule().getName(), rule.getPrecedenceDegreeModule().getName());
        }

        if (isApproved(enrolmentContext, curricularCourse)) {
            return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
        }

        if (hasEnrolmentWithEnroledState(enrolmentContext, curricularCourse, executionSemester.getPreviousExecutionPeriod())) {
            return RuleResult.createTrue(EnrolmentResultType.TEMPORARY, sourceDegreeModuleToEvaluate.getDegreeModule());
        }

        /*
         * CurricularCourse is not approved and is not enroled in previous
         * semester If DegreeModule is Enroled in current semester then
         * Enrolment must be impossible
         */
        if (isEnroled(enrolmentContext, rule.getDegreeModuleToApplyRule(), executionSemester)) {
            return createImpossibleRuleResult(rule, sourceDegreeModuleToEvaluate);
        }

        return createFalseRuleResult(rule, sourceDegreeModuleToEvaluate);
    }

    private RuleResult createFalseRuleResult(final RestrictionDoneDegreeModule rule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate) {
        return RuleResult
                .createFalse(
                        sourceDegreeModuleToEvaluate.getDegreeModule(),
                        "curricularRules.ruleExecutors.RestrictionDoneDegreeModuleExecutor.student.is.not.approved.to.precendenceDegreeModule",
                        rule.getDegreeModuleToApplyRule().getName(), rule.getPrecedenceDegreeModule().getName());
    }

    private RuleResult createImpossibleRuleResult(final RestrictionDoneDegreeModule rule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate) {
        return RuleResult
                .createImpossible(
                        sourceDegreeModuleToEvaluate.getDegreeModule(),
                        "curricularRules.ruleExecutors.RestrictionDoneDegreeModuleExecutor.student.is.not.approved.to.precendenceDegreeModule",
                        rule.getDegreeModuleToApplyRule().getName(), rule.getPrecedenceDegreeModule().getName());
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
