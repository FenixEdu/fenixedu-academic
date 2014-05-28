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

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.MinimumNumberOfCreditsToEnrol;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.RootCurriculumGroup;

public class MinimumNumberOfCreditsToEnrolExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentVerificationWithRules(final ICurricularRule curricularRule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {

        final MinimumNumberOfCreditsToEnrol rule = (MinimumNumberOfCreditsToEnrol) curricularRule;
        if (!canApplyRule(enrolmentContext, curricularRule)) {
            return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
        }

        final Double totalEctsCredits =
                getTotalEctsCredits(enrolmentContext.getStudentCurricularPlan().getRoot(), enrolmentContext.getExecutionPeriod()
                        .getExecutionYear());

        if (rule.allowCredits(totalEctsCredits)) {
            return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
        }

        if (sourceDegreeModuleToEvaluate.isEnroled() && sourceDegreeModuleToEvaluate.isLeaf()) {
            return createImpossibleRuleResult(rule, totalEctsCredits, sourceDegreeModuleToEvaluate);
        } else {
            return createFalseRuleResult(rule, totalEctsCredits, sourceDegreeModuleToEvaluate);
        }
    }

    private Double getTotalEctsCredits(final RootCurriculumGroup root, ExecutionYear executionYear) {
        Double res = 0d;
        for (CycleType cycleType : root.getDegreeType().getOrderedCycleTypes()) {
            CycleCurriculumGroup cycleCurriculumGroup = root.getCycleCurriculumGroup(cycleType);
            if (cycleCurriculumGroup == null) {
                res += root.getCycleCourseGroup(cycleType).getDefaultEcts(executionYear);
            } else {
                res += root.getCreditsConcluded(executionYear);
                break;
            }
        }
        return res;
    }

    private RuleResult createFalseRuleResult(final MinimumNumberOfCreditsToEnrol rule, final Double ectsCredits,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate) {
        return RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule(),
                "curricularRules.ruleExecutors.MinimumNumberOfCreditsToEnrolExecutor.student.has.not.minimum.number.of.credits",
                ectsCredits.toString(), rule.getMinimumCredits().toString(), rule.getDegreeModuleToApplyRule().getName());
    }

    private RuleResult createImpossibleRuleResult(final MinimumNumberOfCreditsToEnrol rule, final Double ectsCredits,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate) {
        return RuleResult.createImpossible(sourceDegreeModuleToEvaluate.getDegreeModule(),
                "curricularRules.ruleExecutors.MinimumNumberOfCreditsToEnrolExecutor.student.has.not.minimum.number.of.credits",
                ectsCredits.toString(), rule.getMinimumCredits().toString(), rule.getDegreeModuleToApplyRule().getName());
    }

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final ICurricularRule curricularRule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {

        final MinimumNumberOfCreditsToEnrol rule = (MinimumNumberOfCreditsToEnrol) curricularRule;
        if (!canApplyRule(enrolmentContext, curricularRule)) {
            return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
        }

        final CurriculumGroup curriculumGroup = enrolmentContext.getStudentCurricularPlan().getRoot();
        Double totalEctsCredits = curriculumGroup.getAprovedEctsCredits();

        if (rule.allowCredits(totalEctsCredits)) {
            return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
        }

        final ExecutionSemester previousExecutionPeriod = enrolmentContext.getExecutionPeriod().getPreviousExecutionPeriod();
        totalEctsCredits =
                Double.valueOf(totalEctsCredits.doubleValue()
                        + curriculumGroup.getEnroledEctsCredits(previousExecutionPeriod).doubleValue());

        if (rule.allowCredits(totalEctsCredits)) {
            return RuleResult.createTrue(EnrolmentResultType.TEMPORARY, sourceDegreeModuleToEvaluate.getDegreeModule());
        }

        if (sourceDegreeModuleToEvaluate.isEnroled() && sourceDegreeModuleToEvaluate.isLeaf()) {
            return createImpossibleRuleResult(rule, totalEctsCredits, sourceDegreeModuleToEvaluate);
        } else {
            return createFalseRuleResult(rule, totalEctsCredits, sourceDegreeModuleToEvaluate);
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
