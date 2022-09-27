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

import org.fenixedu.academic.domain.curricularRules.AnyCurricularCourseExceptions;
import org.fenixedu.academic.domain.curricularRules.ICurricularRule;
import org.fenixedu.academic.domain.curricularRules.executors.RuleResult;
import org.fenixedu.academic.domain.enrolment.EnrolmentContext;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.academic.domain.enrolment.OptionalDegreeModuleToEnrol;

public class AnyCurricularCourseExceptionsExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final ICurricularRule curricularRule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
        return executeEnrolmentVerificationWithRules(curricularRule, sourceDegreeModuleToEvaluate, enrolmentContext);
    }

    @Override
    protected RuleResult executeEnrolmentInEnrolmentEvaluation(final ICurricularRule curricularRule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
        return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    @Override
    protected boolean canBeEvaluated(final ICurricularRule curricularRule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
        return true;
    }

    @Override
    protected RuleResult executeEnrolmentVerificationWithRules(final ICurricularRule curricularRule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {

        final AnyCurricularCourseExceptions rule = (AnyCurricularCourseExceptions) curricularRule;
        if (!rule.appliesToContext(sourceDegreeModuleToEvaluate.getContext())) {
            return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
        }

        return getLogic().executeEnrolmentVerificationWithRules(curricularRule, sourceDegreeModuleToEvaluate, enrolmentContext);
    }

    @Override
    protected RuleResult executeEnrolmentPrefilter(ICurricularRule curricularRule,
            IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, EnrolmentContext enrolmentContext) {

        if (sourceDegreeModuleToEvaluate instanceof OptionalDegreeModuleToEnrol) {
            final RuleResult result =
                    executeEnrolmentVerificationWithRules(curricularRule, sourceDegreeModuleToEvaluate, enrolmentContext);
            return result.isTrue() ? result : RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule());
        }

        return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

}
