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

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.RestrictionBetweenDegreeModules;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleCourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;

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

            final ExecutionSemester executionSemester = enrolmentContext.getExecutionPeriod();
            ectsCredits =
                    Double.valueOf(ectsCredits.doubleValue()
                            + curriculumModule.getEnroledEctsCredits(executionSemester.getPreviousExecutionPeriod())
                                    .doubleValue());

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
