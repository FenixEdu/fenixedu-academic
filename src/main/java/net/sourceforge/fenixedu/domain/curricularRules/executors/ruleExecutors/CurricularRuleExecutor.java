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

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;

abstract public class CurricularRuleExecutor {

    protected CurricularRuleExecutor() {
    }

    public RuleResult execute(final ICurricularRule curricularRule, final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate,
            final EnrolmentContext enrolmentContext) {

        if (!canBeEvaluated(curricularRule, sourceDegreeModuleToEvaluate, enrolmentContext)) {
            return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
        }

        switch (enrolmentContext.getCurricularRuleLevel()) {
        case ENROLMENT_WITH_RULES:
            return executeEnrolmentWithRules(curricularRule, sourceDegreeModuleToEvaluate, enrolmentContext);

        case ENROLMENT_VERIFICATION_WITH_RULES:
            return executeEnrolmentVerificationWithRules(curricularRule, sourceDegreeModuleToEvaluate, enrolmentContext);

        case ENROLMENT_WITH_RULES_AND_TEMPORARY_ENROLMENT:
            return executeEnrolmentWithRulesAndTemporaryEnrolment(curricularRule, sourceDegreeModuleToEvaluate, enrolmentContext);

        case ENROLMENT_NO_RULES:
            return executeEnrolmentWithNoRules(curricularRule, sourceDegreeModuleToEvaluate, enrolmentContext);

        case IMPROVEMENT_ENROLMENT:
        case SPECIAL_SEASON_ENROLMENT:
            return executeEnrolmentInEnrolmentEvaluation(curricularRule, sourceDegreeModuleToEvaluate, enrolmentContext);

        default:
            throw new DomainException("error.curricularRules.RuleExecutor.unimplemented.rule.level");
        }
    }

    private RuleResult executeEnrolmentWithRules(final ICurricularRule curricularRule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
        final RuleResult result =
                executeEnrolmentVerificationWithRules(curricularRule, sourceDegreeModuleToEvaluate, enrolmentContext);
        if (result.hasAnyImpossibleEnrolment()) {
            return RuleResult.createFalse(sourceDegreeModuleToEvaluate.getDegreeModule()).and(result);
        }

        return result;
    }

    protected IDegreeModuleToEvaluate searchDegreeModuleToEvaluate(final EnrolmentContext enrolmentContext,
            final DegreeModule degreeModule) {
        for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext.getDegreeModulesToEvaluate()) {
            if (degreeModuleToEvaluate.isFor(degreeModule)) {
                return degreeModuleToEvaluate;
            }
        }
        return null;
    }

    protected IDegreeModuleToEvaluate searchDegreeModuleToEvaluate(final EnrolmentContext enrolmentContext,
            final ICurricularRule curricularRule) {
        return searchDegreeModuleToEvaluate(enrolmentContext, curricularRule.getDegreeModuleToApplyRule());
    }

    protected Collection<IDegreeModuleToEvaluate> collectDegreeModuleToEnrolFromCourseGroup(
            final EnrolmentContext enrolmentContext, final CourseGroup courseGroup) {
        final Collection<IDegreeModuleToEvaluate> result = new ArrayList<IDegreeModuleToEvaluate>();
        for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext.getDegreeModulesToEvaluate()) {
            if (!degreeModuleToEvaluate.isEnroled() && degreeModuleToEvaluate.getContext().getParentCourseGroup() == courseGroup) {
                result.add(degreeModuleToEvaluate);
            }
        }
        return result;
    }

    protected boolean canApplyRule(final EnrolmentContext enrolmentContext, final ICurricularRule curricularRule) {
        if (curricularRule.getDegreeModuleToApplyRule().isRoot()) {
            return true;
        }
        return curricularRule.appliesToContext(searchDegreeModuleToEvaluate(enrolmentContext, curricularRule).getContext());
    }

    protected CurriculumModule searchCurriculumModule(final EnrolmentContext enrolmentContext, final DegreeModule degreeModule) {
        if (degreeModule.isLeaf()) {
            return enrolmentContext.getStudentCurricularPlan().findEnrolmentFor((CurricularCourse) degreeModule,
                    enrolmentContext.getExecutionPeriod());
        } else {
            return enrolmentContext.getStudentCurricularPlan().findCurriculumGroupFor((CourseGroup) degreeModule);
        }
    }

    protected CurriculumModule searchCurriculumModule(final EnrolmentContext enrolmentContext,
            final ICurricularRule curricularRule) {
        return searchCurriculumModule(enrolmentContext, curricularRule.getDegreeModuleToApplyRule());
    }

    protected boolean isApproved(final EnrolmentContext enrolmentContext, final CurricularCourse curricularCourse) {
        return enrolmentContext.getStudentCurricularPlan().isApproved(curricularCourse);
    }

    protected boolean isApproved(final EnrolmentContext enrolmentContext, final CurricularCourse curricularCourse,
            final ExecutionSemester executionSemester) {
        return enrolmentContext.getStudentCurricularPlan().isApproved(curricularCourse, executionSemester);
    }

    protected boolean isEnroled(final EnrolmentContext enrolmentContext, final DegreeModule degreeModule) {
        return degreeModule.isLeaf() ? isEnroled(enrolmentContext, (CurricularCourse) degreeModule) : isEnroled(enrolmentContext,
                (CourseGroup) degreeModule);
    }

    private boolean isEnroled(final EnrolmentContext enrolmentContext, final CurricularCourse curricularCourse) {
        final ExecutionSemester executionSemester = enrolmentContext.getExecutionPeriod();
        return enrolmentContext.getStudentCurricularPlan().isEnroledInExecutionPeriod(curricularCourse, executionSemester);
    }

    private boolean isEnroled(final EnrolmentContext enrolmentContext, final CourseGroup courseGroup) {
        return enrolmentContext.getStudentCurricularPlan().hasDegreeModule(courseGroup);
    }

    protected boolean isEnroled(final EnrolmentContext enrolmentContext, final CurricularCourse curricularCourse,
            final ExecutionSemester executionSemester) {
        return enrolmentContext.getStudentCurricularPlan().isEnroledInExecutionPeriod(curricularCourse, executionSemester);
    }

    protected boolean hasEnrolmentWithEnroledState(final EnrolmentContext enrolmentContext,
            final CurricularCourse curricularCourse, final ExecutionSemester executionSemester) {
        return enrolmentContext.getStudentCurricularPlan().getRoot()
                .hasEnrolmentWithEnroledState(curricularCourse, executionSemester);
    }

    protected boolean isEnrolling(final EnrolmentContext enrolmentContext, final DegreeModule degreeModule) {
        final IDegreeModuleToEvaluate degreeModuleToEvaluate = searchDegreeModuleToEvaluate(enrolmentContext, degreeModule);
        return degreeModuleToEvaluate != null && !degreeModuleToEvaluate.isEnroled();
    }

    protected boolean isApproved(final EnrolmentContext enrolmentContext, final CurricularCourse curricularCourse,
            final CourseGroup parentCourseGroup) {
        final CurriculumGroup curriculumGroup =
                enrolmentContext.getStudentCurricularPlan().findCurriculumGroupFor(parentCourseGroup);
        return curriculumGroup != null ? curriculumGroup.isApproved(curricularCourse) : false;
    }

    protected RuleResult executeEnrolmentWithNoRules(final ICurricularRule curricularRule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
        final RuleResult ruleResult =
                executeEnrolmentWithRulesAndTemporaryEnrolment(curricularRule, sourceDegreeModuleToEvaluate, enrolmentContext);
        if (ruleResult.isFalse() || (ruleResult.isTrue() && isTemporaryOrImpossible(sourceDegreeModuleToEvaluate, ruleResult))) {
            return RuleResult.createWarning(sourceDegreeModuleToEvaluate.getDegreeModule(), ruleResult.getMessages());
        } else {
            return ruleResult;
        }
    }

    private boolean isTemporaryOrImpossible(final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate,
            final RuleResult ruleResult) {
        return ruleResult.isTemporaryEnrolmentResultType(sourceDegreeModuleToEvaluate.getDegreeModule())
                || ruleResult.isImpossibleEnrolmentResultType(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    abstract protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final ICurricularRule curricularRule,
            IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext);

    abstract protected RuleResult executeEnrolmentInEnrolmentEvaluation(final ICurricularRule curricularRule,
            IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext);

    abstract protected RuleResult executeEnrolmentVerificationWithRules(final ICurricularRule curricularRule,
            IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext);

    abstract protected boolean canBeEvaluated(ICurricularRule curricularRule,
            IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, EnrolmentContext enrolmentContext);

}
