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
package org.fenixedu.academic.domain.phd.enrolments;

import java.util.Collection;

import org.fenixedu.academic.domain.CompetenceCourse;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.curricularRules.ICurricularRule;
import org.fenixedu.academic.domain.curricularRules.executors.RuleResult;
import org.fenixedu.academic.domain.curricularRules.executors.ruleExecutors.CurricularRuleExecutor;
import org.fenixedu.academic.domain.enrolment.EnrolmentContext;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.academic.domain.enrolment.OptionalDegreeModuleToEnrol;

public class PhdValidCurricularCoursesExecutor extends CurricularRuleExecutor {

    @Override
    protected boolean canBeEvaluated(ICurricularRule curricularRule, IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate,
            EnrolmentContext enrolmentContext) {
        return true;
    }

    @Override
    protected RuleResult executeEnrolmentInEnrolmentEvaluation(ICurricularRule curricularRule,
            IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, EnrolmentContext enrolmentContext) {
        return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    @Override
    protected RuleResult executeEnrolmentVerificationWithRules(ICurricularRule curricularRule,
            IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, EnrolmentContext enrolmentContext) {

        final CurricularCourse curricularCourse = getCurricularCourse(sourceDegreeModuleToEvaluate, curricularRule);

        if (!curricularCourse.hasAnyActiveContext(enrolmentContext.getExecutionPeriod())) {
            return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
        }

        final Collection<CompetenceCourse> collection = getCompetenceCoursesAvailableToEnrol(enrolmentContext);

        if (!collection.contains(curricularCourse.getCompetenceCourse())) {

            if (isEnrolling(enrolmentContext, curricularCourse)) {

                return RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule(),
                        "curricularRules.ruleExecutors.PhdValidCurricularCoursesExecutor.invalid.curricularCourse",
                        curricularCourse.getName());

            } else if (isApproved(enrolmentContext, curricularCourse) || isEnroled(enrolmentContext, curricularCourse)) {

                return RuleResult.createImpossible(sourceDegreeModuleToEvaluate.getDegreeModule(),
                        "curricularRules.ruleExecutors.PhdValidCurricularCoursesExecutor.invalid.curricularCourse",
                        curricularCourse.getName());
            }
        }

        return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    private CurricularCourse getCurricularCourse(IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate,
            ICurricularRule curricularRule) {

        if (sourceDegreeModuleToEvaluate.isOptional()) {
            return ((OptionalDegreeModuleToEnrol) sourceDegreeModuleToEvaluate).getCurricularCourse();
        } else {
            return (CurricularCourse) curricularRule.getDegreeModuleToApplyRule();
        }
    }

    private Collection<CompetenceCourse> getCompetenceCoursesAvailableToEnrol(final EnrolmentContext context) {
        return context.getRegistration().getPhdIndividualProgramProcess().getCompetenceCoursesAvailableToEnrol();
    }

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(ICurricularRule curricularRule,
            IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, EnrolmentContext enrolmentContext) {
        return executeEnrolmentVerificationWithRules(curricularRule, sourceDegreeModuleToEvaluate, enrolmentContext);
    }

}
