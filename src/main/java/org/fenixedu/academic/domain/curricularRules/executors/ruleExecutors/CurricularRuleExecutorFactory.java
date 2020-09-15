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

import java.util.HashMap;
import java.util.Map;

import org.fenixedu.academic.domain.curricularRules.AnyCurricularCourse;
import org.fenixedu.academic.domain.curricularRules.AssertUniqueApprovalInCurricularCourseContexts;
import org.fenixedu.academic.domain.curricularRules.CreditsLimit;
import org.fenixedu.academic.domain.curricularRules.CreditsLimitInExternalCycle;
import org.fenixedu.academic.domain.curricularRules.DegreeModulesSelectionLimit;
import org.fenixedu.academic.domain.curricularRules.EnrolmentInSpecialSeasonEvaluation;
import org.fenixedu.academic.domain.curricularRules.EnrolmentToBeApprovedByCoordinator;
import org.fenixedu.academic.domain.curricularRules.EvenOddRule;
import org.fenixedu.academic.domain.curricularRules.Exclusiveness;
import org.fenixedu.academic.domain.curricularRules.ICurricularRule;
import org.fenixedu.academic.domain.curricularRules.ImprovementOfApprovedEnrolment;
import org.fenixedu.academic.domain.curricularRules.LastDigitSplitRule;
import org.fenixedu.academic.domain.curricularRules.MaximumNumberOfCreditsForEnrolmentPeriod;
import org.fenixedu.academic.domain.curricularRules.MaximumNumberOfECTSInSpecialSeasonEvaluation;
import org.fenixedu.academic.domain.curricularRules.MaximumNumberOfEctsInStandaloneCurriculumGroup;
import org.fenixedu.academic.domain.curricularRules.MinimumNumberOfCreditsToEnrol;
import org.fenixedu.academic.domain.curricularRules.PreviousYearsEnrolmentCurricularRule;
import org.fenixedu.academic.domain.curricularRules.RestrictionBetweenDegreeModules;
import org.fenixedu.academic.domain.curricularRules.RestrictionDoneDegreeModule;
import org.fenixedu.academic.domain.curricularRules.RestrictionEnroledDegreeModule;
import org.fenixedu.academic.domain.curricularRules.RestrictionNotEnroledDegreeModule;
import org.fenixedu.academic.domain.curricularRules.SeniorStatuteSpecialSeasonEnrolmentScope;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.enrolments.PhdValidCurricularCoursesExecutor;
import org.fenixedu.academic.domain.phd.enrolments.PhdValidCurricularCoursesRule;

public class CurricularRuleExecutorFactory {

    private static Map<Class<? extends ICurricularRule>, CurricularRuleExecutor> executors =
            new HashMap<Class<? extends ICurricularRule>, CurricularRuleExecutor>();

    static {
        executors.put(RestrictionDoneDegreeModule.class, new RestrictionDoneDegreeModuleExecutor());
        executors.put(RestrictionEnroledDegreeModule.class, new RestrictionEnroledDegreeModuleExecutor());
        executors.put(RestrictionNotEnroledDegreeModule.class, new RestrictionNotEnroledDegreeModuleExecutor());
        executors.put(RestrictionBetweenDegreeModules.class, new RestrictionBetweenDegreeModulesExecutor());
        executors.put(EnrolmentToBeApprovedByCoordinator.class, new EnrolmentToBeApprovedByCoordinatorExecutor());
        executors.put(Exclusiveness.class, new ExclusivenessExecutor());
        executors.put(MinimumNumberOfCreditsToEnrol.class, new MinimumNumberOfCreditsToEnrolExecutor());
        executors.put(DegreeModulesSelectionLimit.class, new DegreeModulesSelectionLimitExecutor());
        executors.put(CreditsLimit.class, new CreditsLimitExecutor());
        executors.put(AnyCurricularCourse.class, new AnyCurricularCourseExecutor());
        executors.put(MaximumNumberOfCreditsForEnrolmentPeriod.class, new MaximumNumberOfCreditsForEnrolmentPeriodExecutor());
        executors.put(PreviousYearsEnrolmentCurricularRule.class, new PreviousYearsEnrolmentExecutor());
        executors.put(AssertUniqueApprovalInCurricularCourseContexts.class,
                new AssertUniqueApprovalInCurricularCourseContextsExecutor());
        executors.put(ImprovementOfApprovedEnrolment.class, new ImprovementOfApprovedEnrolmentExecutor());
        executors.put(EnrolmentInSpecialSeasonEvaluation.class, new EnrolmentInSpecialSeasonEvaluationExecutor());
        executors.put(MaximumNumberOfECTSInSpecialSeasonEvaluation.class,
                new MaximumNumberOfECTSInSpecialSeasonEvaluationExecutor());
        executors.put(CreditsLimitInExternalCycle.class, new CreditsLimitInExternalCycleExecutor());
        executors.put(EvenOddRule.class, new EvenOddExecuter());
        executors.put(LastDigitSplitRule.class, new LastDigitSplitExecuter());
        executors.put(MaximumNumberOfEctsInStandaloneCurriculumGroup.class,
                new MaximumNumberOfEctsInStandaloneCurriculumGroupExecutor());
        executors.put(PhdValidCurricularCoursesRule.class, new PhdValidCurricularCoursesExecutor());
        executors.put(SeniorStatuteSpecialSeasonEnrolmentScope.class, new SeniorStatuteSpecialSeasonEnrolmentScopeExecutor());
    }

    public static CurricularRuleExecutor findExecutor(final ICurricularRule curricularRule) {
        return findExecutor(curricularRule.getClass());
    }

    public static CurricularRuleExecutor findExecutor(final Class<? extends ICurricularRule> clazz) {
        if (!executors.containsKey(clazz)) {
            throw new DomainException("error.curricularRules.RuleFactory.cannot.find.RuleExecutor.for.class", clazz.getName());
        }
        return executors.get(clazz);
    }

}
