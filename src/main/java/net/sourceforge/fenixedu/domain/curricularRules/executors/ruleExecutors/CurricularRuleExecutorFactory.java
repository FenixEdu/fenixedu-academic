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

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.domain.curricularRules.AnyCurricularCourse;
import net.sourceforge.fenixedu.domain.curricularRules.AssertUniqueApprovalInCurricularCourseContexts;
import net.sourceforge.fenixedu.domain.curricularRules.CreditsLimit;
import net.sourceforge.fenixedu.domain.curricularRules.CreditsLimitInExternalCycle;
import net.sourceforge.fenixedu.domain.curricularRules.DegreeModulesSelectionLimit;
import net.sourceforge.fenixedu.domain.curricularRules.EnrolmentInSpecialSeasonEvaluation;
import net.sourceforge.fenixedu.domain.curricularRules.EnrolmentToBeApprovedByCoordinator;
import net.sourceforge.fenixedu.domain.curricularRules.EvenOddRule;
import net.sourceforge.fenixedu.domain.curricularRules.Exclusiveness;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.ImprovementOfApprovedEnrolment;
import net.sourceforge.fenixedu.domain.curricularRules.MaximumNumberOfCreditsForEnrolmentPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.MaximumNumberOfECTSInSpecialSeasonEvaluation;
import net.sourceforge.fenixedu.domain.curricularRules.MaximumNumberOfEctsInStandaloneCurriculumGroup;
import net.sourceforge.fenixedu.domain.curricularRules.MinimumNumberOfCreditsToEnrol;
import net.sourceforge.fenixedu.domain.curricularRules.PreviousYearsEnrolmentCurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.RestrictionBetweenDegreeModules;
import net.sourceforge.fenixedu.domain.curricularRules.RestrictionDoneDegreeModule;
import net.sourceforge.fenixedu.domain.curricularRules.RestrictionEnroledDegreeModule;
import net.sourceforge.fenixedu.domain.curricularRules.SeniorStatuteSpecialSeasonEnrolmentScope;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.enrolments.PhdValidCurricularCoursesExecutor;
import net.sourceforge.fenixedu.domain.phd.enrolments.PhdValidCurricularCoursesRule;

public class CurricularRuleExecutorFactory {

    private static Map<Class<? extends ICurricularRule>, CurricularRuleExecutor> executors =
            new HashMap<Class<? extends ICurricularRule>, CurricularRuleExecutor>();

    static {
        executors.put(RestrictionDoneDegreeModule.class, new RestrictionDoneDegreeModuleExecutor());
        executors.put(RestrictionEnroledDegreeModule.class, new RestrictionEnroledDegreeModuleExecutor());
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
