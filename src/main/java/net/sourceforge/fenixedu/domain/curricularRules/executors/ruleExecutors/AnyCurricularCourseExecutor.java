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

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.curricularRules.AnyCurricularCourse;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.enrolment.EnroledOptionalEnrolment;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.enrolment.OptionalDegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.util.CurricularRuleLabelFormatter;

public class AnyCurricularCourseExecutor extends CurricularRuleExecutor {

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

    /**
     * -> if getDegree() == null ? getBolonhaDegreeType() == null ? any degree
     * from IST ? getBolonhaDegreeType() != null ? any degree with same
     * DegreeType -> else ? check selected degree -> if departmentUnit != null ?
     * CurricularCourse from CompetenceCourse that belong to that Department
     */
    @Override
    protected RuleResult executeEnrolmentVerificationWithRules(final ICurricularRule curricularRule,
            IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {

        final AnyCurricularCourse rule = (AnyCurricularCourse) curricularRule;

        if (!rule.appliesToContext(sourceDegreeModuleToEvaluate.getContext())) {
            return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
        }

        final CurricularCourse curricularCourseToEnrol;
        if (sourceDegreeModuleToEvaluate.isEnroling()) {
            final OptionalDegreeModuleToEnrol optionalDegreeModuleToEnrol =
                    (OptionalDegreeModuleToEnrol) sourceDegreeModuleToEvaluate;
            curricularCourseToEnrol = optionalDegreeModuleToEnrol.getCurricularCourse();

            if (isApproved(enrolmentContext, curricularCourseToEnrol) || isEnroled(enrolmentContext, curricularCourseToEnrol)
                    || isApproved(enrolmentContext, optionalDegreeModuleToEnrol.getCurricularCourse())
                    || isEnroled(enrolmentContext, optionalDegreeModuleToEnrol.getCurricularCourse())) {

                return RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule(),
                        "curricularRules.ruleExecutors.AnyCurricularCourseExecutor.already.approved.or.enroled",
                        curricularCourseToEnrol.getName(), rule.getDegreeModuleToApplyRule().getName());
            }

        } else if (sourceDegreeModuleToEvaluate.isEnroled()) {
            curricularCourseToEnrol =
                    (CurricularCourse) ((EnroledOptionalEnrolment) sourceDegreeModuleToEvaluate).getCurriculumModule()
                            .getDegreeModule();
        } else {
            throw new DomainException(
                    "error.curricularRules.executors.ruleExecutors.AnyCurricularCourseExecutor.unexpected.degree.module.to.evaluate");
        }

        final ExecutionSemester executionSemester = enrolmentContext.getExecutionPeriod();
        final Degree degree = curricularCourseToEnrol.getDegree();

        boolean result = true;

        result &=
                rule.hasMinimumCredits() ? rule.getMinimumCredits() <= curricularCourseToEnrol.getEctsCredits(executionSemester) : true;

        result &=
                rule.hasMaximumCredits() ? rule.getMaximumCredits() >= curricularCourseToEnrol.getEctsCredits(executionSemester) : true;

        result &=
                rule.hasDegree() ? rule.getDegree() == degree : rule.hasBolonhaDegreeType() ? degree.getDegreeType() == rule
                        .getBolonhaDegreeType() : true;

        if (rule.hasDepartmentUnit()) {
            final DepartmentUnit departmentUnit =
                    curricularCourseToEnrol.getCompetenceCourse().getDepartmentUnit(executionSemester);
            result &= departmentUnit != null && departmentUnit.equals(rule.getDepartmentUnit());
        }

        if (result) {
            return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());
        } else {
            if (sourceDegreeModuleToEvaluate.isEnroled()) {
                return RuleResult.createImpossibleWithLiteralMessage(sourceDegreeModuleToEvaluate.getDegreeModule(),
                        CurricularRuleLabelFormatter.getLabel(rule));
            } else {
                return RuleResult.createFalseWithLiteralMessage(sourceDegreeModuleToEvaluate.getDegreeModule(),
                        CurricularRuleLabelFormatter.getLabel(rule));
            }
        }

    }

    @Override
    protected boolean canBeEvaluated(ICurricularRule curricularRule, IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate,
            EnrolmentContext enrolmentContext) {
        return true;
    }

}
