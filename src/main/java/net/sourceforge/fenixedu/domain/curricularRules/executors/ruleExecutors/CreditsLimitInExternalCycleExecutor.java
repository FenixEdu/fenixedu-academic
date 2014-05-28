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
import net.sourceforge.fenixedu.domain.curricularRules.CreditsLimitInExternalCycle;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.enrolment.EnroledCurriculumModuleWrapper;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumLine;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalCurriculumGroup;

public class CreditsLimitInExternalCycleExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentInEnrolmentEvaluation(ICurricularRule curricularRule,
            IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, EnrolmentContext enrolmentContext) {

        return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(ICurricularRule curricularRule,
            IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, EnrolmentContext enrolmentContext) {
        final CreditsLimitInExternalCycle creditsLimitInExternalCycle = (CreditsLimitInExternalCycle) curricularRule;
        final ExternalCurriculumGroup externalCurriculumGroup = creditsLimitInExternalCycle.getExternalCurriculumGroup();

        if (!isToApply(sourceDegreeModuleToEvaluate, enrolmentContext, externalCurriculumGroup)) {
            return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
        }

        if (isEnrolingDissertation(enrolmentContext, externalCurriculumGroup)) {
            return createRuleResultForEnrolingDissertation(sourceDegreeModuleToEvaluate, creditsLimitInExternalCycle);
        }

        final CycleCurriculumGroup previousCycleCurriclumGroup = creditsLimitInExternalCycle.getPreviousCycleCurriculumGroup();
        final Double totalCreditsInPreviousCycle = previousCycleCurriclumGroup.getAprovedEctsCredits();

        if (!creditsLimitInExternalCycle.creditsInPreviousCycleSufficient(totalCreditsInPreviousCycle)) {
            return createRuleResultForNotSatisfiedCreditsForPreviousCycle(sourceDegreeModuleToEvaluate,
                    creditsLimitInExternalCycle, previousCycleCurriclumGroup);
        }

        final Double totalCredits = calculateApprovedAndEnrollingTotalCredits(enrolmentContext, externalCurriculumGroup);
        if (creditsLimitInExternalCycle.creditsExceedMaximumInExternalCycle(totalCredits, totalCreditsInPreviousCycle)) {
            return createRuleResultForMaxCreditsExceededInExternalCycle(sourceDegreeModuleToEvaluate,
                    creditsLimitInExternalCycle, totalCredits, totalCreditsInPreviousCycle);
        }

        final Double totalEctsWithEnroledEctsCreditsFromPreviousPeriod =
                totalCredits
                        + externalCurriculumGroup.getEnroledEctsCredits(enrolmentContext.getExecutionPeriod()
                                .getPreviousExecutionPeriod());
        if (creditsLimitInExternalCycle.creditsExceedMaximumInExternalCycle(totalEctsWithEnroledEctsCreditsFromPreviousPeriod,
                totalCreditsInPreviousCycle)) {
            return RuleResult.createTrue(EnrolmentResultType.TEMPORARY, sourceDegreeModuleToEvaluate.getDegreeModule(),
                    "curricularRules.ruleExecutors.CreditsLimitInExternalCycleExecutor.external.cycle.limit.exceeded",
                    creditsLimitInExternalCycle.getExternalCurriculumGroup().getName().getContent(),
                    totalEctsWithEnroledEctsCreditsFromPreviousPeriod.toString(), creditsLimitInExternalCycle
                            .getMaxCreditsInExternalCycle(totalCreditsInPreviousCycle).toString(), totalCreditsInPreviousCycle
                            .toString());
        }

        return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    private RuleResult createRuleResultForNotSatisfiedCreditsForPreviousCycle(
            IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final CreditsLimitInExternalCycle creditsLimitInExternalCycle,
            final CycleCurriculumGroup previousCycleCurriclumGroup) {
        if (sourceDegreeModuleToEvaluate.isEnroled() && sourceDegreeModuleToEvaluate.isLeaf()) {
            return RuleResult
                    .createImpossible(
                            sourceDegreeModuleToEvaluate.getDegreeModule(),
                            "curricularRules.ruleExecutors.CreditsLimitInExternalCycleExecutor.previous.cycle.minimum.credits.not.fulfilled",
                            creditsLimitInExternalCycle.getExternalCurriculumGroup().getName().getContent(),
                            creditsLimitInExternalCycle.getMinCreditsInPreviousCycle().toString(), previousCycleCurriclumGroup
                                    .getName().getContent());
        }

        return RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule(),
                "curricularRules.ruleExecutors.CreditsLimitInExternalCycleExecutor.previous.cycle.minimum.credits.not.fulfilled",
                creditsLimitInExternalCycle.getExternalCurriculumGroup().getName().getContent(), creditsLimitInExternalCycle
                        .getMinCreditsInPreviousCycle().toString(), previousCycleCurriclumGroup.getName().getContent());
    }

    private boolean isToApply(IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext,
            final ExternalCurriculumGroup externalCurriculumGroup) {
        if (!sourceDegreeModuleToEvaluate.isLeaf()) {
            return false;
        }

        for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext.getDegreeModulesToEvaluate()) {
            if (externalCurriculumGroup.hasCurriculumModule(degreeModuleToEvaluate.getCurriculumGroup())
                    && (isEnrolingInSemester(enrolmentContext, degreeModuleToEvaluate))) {
                return true;
            }
        }

        return false;

    }

    private boolean isEnroledIn(IDegreeModuleToEvaluate degreeModuleToEvaluate, ExecutionSemester executionSemester) {
        if (degreeModuleToEvaluate.isLeaf()) {
            final EnroledCurriculumModuleWrapper curriculumModuleEnroledWrapper =
                    (EnroledCurriculumModuleWrapper) degreeModuleToEvaluate;
            final CurriculumLine curriculumLine = (CurriculumLine) (curriculumModuleEnroledWrapper).getCurriculumModule();

            if (curriculumLine.isEnrolment()) {
                return curriculumLine.getExecutionPeriod() == executionSemester;
            }

            return false;
        }

        return false;
    }

    private Double calculateApprovedAndEnrollingTotalCredits(final EnrolmentContext enrolmentContext,
            final ExternalCurriculumGroup externalCurriculumGroup) {
        double result = 0;
        final ExecutionSemester executionSemester = enrolmentContext.getExecutionPeriod();
        for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext.getDegreeModulesToEvaluate()) {
            if (degreeModuleToEvaluate.isLeaf()
                    && externalCurriculumGroup.hasCurriculumModule(degreeModuleToEvaluate.getCurriculumGroup())) {
                result += degreeModuleToEvaluate.getEctsCredits(executionSemester);
            }
        }

        result += externalCurriculumGroup.getAprovedEctsCredits();

        return result;
    }

    @Override
    protected RuleResult executeEnrolmentVerificationWithRules(ICurricularRule curricularRule,
            IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, EnrolmentContext enrolmentContext) {

        final CreditsLimitInExternalCycle creditsLimitInExternalCycle = (CreditsLimitInExternalCycle) curricularRule;
        final ExternalCurriculumGroup externalCurriculumGroup = creditsLimitInExternalCycle.getExternalCurriculumGroup();

        if (!isToApply(sourceDegreeModuleToEvaluate, enrolmentContext, externalCurriculumGroup)) {
            return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
        }

        if (isEnrolingDissertation(enrolmentContext, externalCurriculumGroup)) {
            return createRuleResultForEnrolingDissertation(sourceDegreeModuleToEvaluate, creditsLimitInExternalCycle);
        }

        final CycleCurriculumGroup previousCycleCurriclumGroup = creditsLimitInExternalCycle.getPreviousCycleCurriculumGroup();
        final Double totalCreditsInPreviousCycle = previousCycleCurriclumGroup.getAprovedEctsCredits();

        if (!creditsLimitInExternalCycle.creditsInPreviousCycleSufficient(totalCreditsInPreviousCycle)) {
            return createRuleResultForNotSatisfiedCreditsForPreviousCycle(sourceDegreeModuleToEvaluate,
                    creditsLimitInExternalCycle, previousCycleCurriclumGroup);
        }

        final Double totalCreditsInExternalCycle =
                calculateApprovedAndEnrollingTotalCredits(enrolmentContext, externalCurriculumGroup);
        if (creditsLimitInExternalCycle.creditsExceedMaximumInExternalCycle(totalCreditsInExternalCycle,
                totalCreditsInPreviousCycle)) {
            return createRuleResultForMaxCreditsExceededInExternalCycle(sourceDegreeModuleToEvaluate,
                    creditsLimitInExternalCycle, totalCreditsInExternalCycle, totalCreditsInPreviousCycle);
        }

        return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());

    }

    private RuleResult createRuleResultForEnrolingDissertation(final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate,
            final CreditsLimitInExternalCycle creditsLimitInExternalCycle) {

        if (sourceDegreeModuleToEvaluate.isEnroled() && sourceDegreeModuleToEvaluate.isLeaf()) {
            return RuleResult.createImpossible(sourceDegreeModuleToEvaluate.getDegreeModule(),
                    "curricularRules.ruleExecutors.CreditsLimitInExternalCycleExecutor.enroling.dissertation");
        }
        return RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule(),
                "curricularRules.ruleExecutors.CreditsLimitInExternalCycleExecutor.enroling.dissertation");
    }

    private boolean isEnrolingDissertation(final EnrolmentContext enrolmentContext,
            final ExternalCurriculumGroup externalCurriculumGroup) {

        for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext.getDegreeModulesToEvaluate()) {
            if (degreeModuleToEvaluate.isDissertation()
                    && externalCurriculumGroup.hasCurriculumModule(degreeModuleToEvaluate.getCurriculumGroup())
                    && isEnrolingInSemester(enrolmentContext, degreeModuleToEvaluate)) {
                return true;
            }
        }
        return false;
    }

    private boolean isEnrolingInSemester(final EnrolmentContext enrolmentContext,
            final IDegreeModuleToEvaluate degreeModuleToEvaluate) {
        return degreeModuleToEvaluate.isEnroling() || isEnroledIn(degreeModuleToEvaluate, enrolmentContext.getExecutionPeriod());
    }

    private RuleResult createRuleResultForMaxCreditsExceededInExternalCycle(IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate,
            final CreditsLimitInExternalCycle creditsLimitInExternalCycle, final Double totalCreditsInExternalCycle,
            final Double totalCreditsInPreviousCycle) {
        if (sourceDegreeModuleToEvaluate.isEnroled() && sourceDegreeModuleToEvaluate.isLeaf()) {
            return RuleResult.createImpossible(sourceDegreeModuleToEvaluate.getDegreeModule(),
                    "curricularRules.ruleExecutors.CreditsLimitInExternalCycleExecutor.external.cycle.limit.exceeded",
                    creditsLimitInExternalCycle.getExternalCurriculumGroup().getName().getContent(),
                    totalCreditsInExternalCycle.toString(),
                    creditsLimitInExternalCycle.getMaxCreditsInExternalCycle(totalCreditsInPreviousCycle).toString(),
                    totalCreditsInPreviousCycle.toString());
        }
        return RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule(),
                "curricularRules.ruleExecutors.CreditsLimitInExternalCycleExecutor.external.cycle.limit.exceeded",
                creditsLimitInExternalCycle.getExternalCurriculumGroup().getName().getContent(),
                totalCreditsInExternalCycle.toString(),
                creditsLimitInExternalCycle.getMaxCreditsInExternalCycle(totalCreditsInPreviousCycle).toString(),
                totalCreditsInPreviousCycle.toString());
    }

    @Override
    protected boolean canBeEvaluated(ICurricularRule curricularRule, IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate,
            EnrolmentContext enrolmentContext) {
        return true;
    }

}
