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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.domain.curricularRules.DegreeModulesSelectionLimit;
import org.fenixedu.academic.domain.curricularRules.ICurricularRule;
import org.fenixedu.academic.domain.curricularRules.executors.RuleResult;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.enrolment.EnroledCurriculumModuleWrapper;
import org.fenixedu.academic.domain.enrolment.EnrolmentContext;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;

public class DegreeModulesSelectionLimitExecutor extends CurricularRuleExecutor {

    private int countTotalDegreeModules(final EnrolmentContext enrolmentContext, final CourseGroup courseGroup,
            final CurriculumGroup curriculumGroup) {

        int numberOfDegreeModulesToEnrol = countNumberOfDegreeModulesToEnrol(enrolmentContext, courseGroup);
        int numberOfApprovedEnrolments = curriculumGroup.getNumberOfApprovedChildCurriculumLines();
        int numberOfEnrolments = countNumberOfChildEnrolments(enrolmentContext, curriculumGroup);
        int numberOfChildCurriculumGroups = curriculumGroup.getNumberOfChildCurriculumGroupsWithCourseGroup();

        return numberOfApprovedEnrolments + numberOfEnrolments + numberOfDegreeModulesToEnrol + numberOfChildCurriculumGroups;
    }

    private int countNumberOfChildEnrolments(final EnrolmentContext enrolmentContext, final CurriculumGroup curriculumGroup) {
        return enrolmentContext.isToEvaluateRulesByYear() ? curriculumGroup.getNumberOfChildEnrolments(enrolmentContext
                .getExecutionYear()) : curriculumGroup.getNumberOfChildEnrolments(enrolmentContext.getExecutionPeriod());
    }

    private int countPreviousPeriodNumberOfChildEnrolments(final EnrolmentContext enrolmentContext,
            final CurriculumGroup curriculumGroup) {
        return enrolmentContext.isToEvaluateRulesByYear() ? curriculumGroup
                .getNumberOfChildEnrolments(enrolmentContext.getExecutionYear().getPreviousExecutionYear()) : curriculumGroup
                        .getNumberOfChildEnrolments(enrolmentContext.getExecutionPeriod().getPrevious());
    }

    private RuleResult createFalseRuleResult(final DegreeModulesSelectionLimit rule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate) {
        if (rule.getMinimumLimit().equals(rule.getMaximumLimit())) {
            return RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule(),
                    "curricularRules.ruleExecutors.DegreeModulesSelectionLimitExecutor.limit.exceded",
                    rule.getDegreeModuleToApplyRule().getName(), rule.getMinimumLimit().toString());
        } else {
            return RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule(),
                    "curricularRules.ruleExecutors.DegreeModulesSelectionLimitExecutor.limits.exceded",
                    rule.getDegreeModuleToApplyRule().getName(), rule.getMinimumLimit().toString(),
                    rule.getMaximumLimit().toString());
        }
    }

    private RuleResult createImpossibleRuleResult(final DegreeModulesSelectionLimit rule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate) {
        if (rule.getMinimumLimit().equals(rule.getMaximumLimit())) {
            return RuleResult.createImpossible(sourceDegreeModuleToEvaluate.getDegreeModule(),
                    "curricularRules.ruleExecutors.DegreeModulesSelectionLimitExecutor.limit.exceded",
                    rule.getDegreeModuleToApplyRule().getName(), rule.getMinimumLimit().toString());
        } else {
            return RuleResult.createImpossible(sourceDegreeModuleToEvaluate.getDegreeModule(),
                    "curricularRules.ruleExecutors.DegreeModulesSelectionLimitExecutor.limits.exceded",
                    rule.getDegreeModuleToApplyRule().getName(), rule.getMinimumLimit().toString(),
                    rule.getMaximumLimit().toString());
        }
    }

    private int countNumberOfDegreeModulesToEnrol(final EnrolmentContext enrolmentContext, final CourseGroup courseGroup) {
        final Set<DegreeModule> result = new HashSet<>();
        for (final Context context : getValidChildContexts(enrolmentContext, courseGroup)) {
            if (isEnrolling(enrolmentContext, context.getChildDegreeModule())
                    && searchDegreeModuleToEvaluate(enrolmentContext, context.getChildDegreeModule()).getCurriculumGroup()
                            .getDegreeModule() == courseGroup) {
                result.add(context.getChildDegreeModule());
            }
        }

        return result.size();
    }

    private List<Context> getValidChildContexts(final EnrolmentContext enrolmentContext, final CourseGroup courseGroup) {
        return enrolmentContext.isToEvaluateRulesByYear() ? courseGroup.getValidChildContextsForExecutionAggregation(
                enrolmentContext.getExecutionYear()) : courseGroup.getValidChildContexts(enrolmentContext.getExecutionPeriod());
    }

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final ICurricularRule curricularRule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {

        final DegreeModulesSelectionLimit rule = (DegreeModulesSelectionLimit) curricularRule;

        if (!canApplyRule(enrolmentContext, rule)) {
            return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
        }

        final IDegreeModuleToEvaluate degreeModuleToEvaluate = searchDegreeModuleToEvaluate(enrolmentContext, rule);

        if (degreeModuleToEvaluate.isEnroled()) {

            final CourseGroup courseGroup = rule.getDegreeModuleToApplyRule();
            final EnroledCurriculumModuleWrapper moduleEnroledWrapper = (EnroledCurriculumModuleWrapper) degreeModuleToEvaluate;
            final CurriculumGroup curriculumGroup = (CurriculumGroup) moduleEnroledWrapper.getCurriculumModule();

            int total = countTotalDegreeModules(enrolmentContext, courseGroup, curriculumGroup);

            if (rule.numberOfDegreeModulesExceedMaximum(total)) {
                if (sourceDegreeModuleToEvaluate.isEnroled() && sourceDegreeModuleToEvaluate.isLeaf()) {
                    return createImpossibleRuleResult(rule, sourceDegreeModuleToEvaluate);
                } else {
                    return createFalseRuleResult(rule, sourceDegreeModuleToEvaluate);
                }
            }

            total += countPreviousPeriodNumberOfChildEnrolments(enrolmentContext, curriculumGroup);

            return rule.numberOfDegreeModulesExceedMaximum(total) ? RuleResult.createTrue(EnrolmentResultType.TEMPORARY,
                    sourceDegreeModuleToEvaluate.getDegreeModule()) : RuleResult
                            .createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
        }
        // is enrolling now
        return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    @Override
    protected RuleResult executeEnrolmentInEnrolmentEvaluation(final ICurricularRule curricularRule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
        return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    @Override
    protected RuleResult executeEnrolmentVerificationWithRules(ICurricularRule curricularRule,
            IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, EnrolmentContext enrolmentContext) {
        final DegreeModulesSelectionLimit rule = (DegreeModulesSelectionLimit) curricularRule;

        if (!canApplyRule(enrolmentContext, rule)) {
            return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
        }

        final IDegreeModuleToEvaluate degreeModuleToEvaluate = searchDegreeModuleToEvaluate(enrolmentContext, rule);

        if (degreeModuleToEvaluate.isEnroled()) {

            final EnroledCurriculumModuleWrapper moduleEnroledWrapper = (EnroledCurriculumModuleWrapper) degreeModuleToEvaluate;
            final CourseGroup courseGroup = rule.getDegreeModuleToApplyRule();
            final CurriculumGroup curriculumGroup = (CurriculumGroup) moduleEnroledWrapper.getCurriculumModule();

            int total = countTotalDegreeModules(enrolmentContext, courseGroup, curriculumGroup);
            if (rule.numberOfDegreeModulesExceedMaximum(total)) {
                if (sourceDegreeModuleToEvaluate.isEnroled() && sourceDegreeModuleToEvaluate.isLeaf()) {
                    return createImpossibleRuleResult(rule, sourceDegreeModuleToEvaluate);
                } else {
                    return createFalseRuleResult(rule, sourceDegreeModuleToEvaluate);
                }
            } else {
                return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
            }

        } // is enrolling now
        return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    @Override
    protected boolean canBeEvaluated(ICurricularRule curricularRule, IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate,
            EnrolmentContext enrolmentContext) {
        return true;
    }

}
