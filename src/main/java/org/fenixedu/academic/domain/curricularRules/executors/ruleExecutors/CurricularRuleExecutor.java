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

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.curricularRules.ICurricularRule;
import org.fenixedu.academic.domain.curricularRules.executors.RuleResult;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.enrolment.EnrolmentContext;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumModule;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.I18N;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract public class CurricularRuleExecutor {

    static final private Logger logger = LoggerFactory.getLogger(CurricularRuleExecutor.class);

    private CurricularRuleExecutorLogic logic;

    protected CurricularRuleExecutorLogic getLogic() {
        if (this.logic == null) {
            throw new DomainException("curricularRules.ruleExecutors.logic.unavailable",
                    BundleUtil.getString(Bundle.BOLONHA, I18N.getLocale(), "label.enrolmentPeriodRestrictions"));
        }

        return logic;
    }

    protected void setLogic(final CurricularRuleExecutorLogic input) {
        if (input == null) {
            throw new DomainException("curricularRules.ruleExecutors.logic.unavailable",
                    BundleUtil.getString(Bundle.BOLONHA, I18N.getLocale(), "label.enrolmentPeriodRestrictions"));
        }

        this.logic = input;
    }

    /**
     * Allows delegating execution logic to external class
     */
    public static interface CurricularRuleExecutorLogic {

        public RuleResult executeEnrolmentVerificationWithRules(final ICurricularRule curricularRule,
                final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext);
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

        case ENROLMENT_PREFILTER:
            return executeEnrolmentPrefilter(curricularRule, sourceDegreeModuleToEvaluate, enrolmentContext);

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
            if (!degreeModuleToEvaluate.isEnroled()
                    && degreeModuleToEvaluate.getContext().getParentCourseGroup() == courseGroup) {
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

    static public interface CurricularRuleApprovalExecutor {

        boolean isApproved(final EnrolmentContext enrolmentContext, final CurricularCourse curricularCourse);

        boolean isApproved(final EnrolmentContext enrolmentContext, final CurricularCourse curricularCourse,
                final ExecutionInterval executionInterval);
    }

    static public CurricularRuleApprovalExecutor getCurricularRuleApprovalExecutor() {
        return CURRICULAR_RULE_APPROVAL_EXECUTOR.get();
    }

    static public void setCurricularRuleApprovalExecutor(final Supplier<CurricularRuleApprovalExecutor> input) {
        if (input != null && input.get() != null) {
            CURRICULAR_RULE_APPROVAL_EXECUTOR = input;
        } else {
            logger.error("Could not set CURRICULAR_RULE_APPROVAL_EXECUTOR to null");
        }
    }

    static private Supplier<CurricularRuleApprovalExecutor> CURRICULAR_RULE_APPROVAL_EXECUTOR =
            () -> new CurricularRuleApprovalExecutor() {

                @Override
                public boolean isApproved(final EnrolmentContext enrolmentContext, final CurricularCourse curricularCourse) {

                    return enrolmentContext.getStudentCurricularPlan().isApproved(curricularCourse);
                }

                @Override
                public boolean isApproved(final EnrolmentContext enrolmentContext, final CurricularCourse curricularCourse,
                        final ExecutionInterval executionInterval) {

                    return enrolmentContext.getStudentCurricularPlan().isApproved(curricularCourse, executionInterval);
                }
            };

    protected boolean isApproved(final EnrolmentContext enrolmentContext, final CurricularCourse curricularCourse) {
        return getCurricularRuleApprovalExecutor().isApproved(enrolmentContext, curricularCourse);
    }

    protected boolean isApproved(final EnrolmentContext enrolmentContext, final CurricularCourse curricularCourse,
            final ExecutionInterval executionInterval) {
        return getCurricularRuleApprovalExecutor().isApproved(enrolmentContext, curricularCourse, executionInterval);
    }

    protected boolean isEnroled(final EnrolmentContext enrolmentContext, final DegreeModule degreeModule) {
        return degreeModule.isLeaf() ? isEnroled(enrolmentContext, (CurricularCourse) degreeModule) : isEnroled(enrolmentContext,
                (CourseGroup) degreeModule);
    }

    private boolean isEnroled(final EnrolmentContext enrolmentContext, final CurricularCourse curricularCourse) {
        for (final ExecutionInterval executionInterval : enrolmentContext.getExecutionIntervalsToEvaluate()) {
            if (enrolmentContext.getStudentCurricularPlan().isEnroledInExecutionPeriod(curricularCourse, executionInterval)) {
                return true;
            }
        }

        return false;
    }

    private boolean isEnroled(final EnrolmentContext enrolmentContext, final CourseGroup courseGroup) {
        return enrolmentContext.getStudentCurricularPlan().hasDegreeModule(courseGroup);
    }

    protected boolean isEnroled(final EnrolmentContext enrolmentContext, final CurricularCourse curricularCourse,
            final ExecutionInterval executionInterval) {
        return enrolmentContext.getStudentCurricularPlan().isEnroledInExecutionPeriod(curricularCourse, executionInterval);
    }

    protected boolean isEnroled(final EnrolmentContext enrolmentContext, final CurricularCourse curricularCourse,
            final ExecutionYear executionYear) {
        for (final ExecutionInterval executionInterval : executionYear.getChildIntervals()) {
            if (enrolmentContext.getStudentCurricularPlan().isEnroledInExecutionPeriod(curricularCourse, executionInterval)) {
                return true;
            }
        }

        return false;
    }

    protected boolean hasEnrolmentWithEnroledState(final EnrolmentContext enrolmentContext,
            final CurricularCourse curricularCourse, final ExecutionInterval executionInterval) {
        return enrolmentContext.getStudentCurricularPlan().getRoot().hasEnrolmentWithEnroledState(curricularCourse,
                executionInterval);
    }

    protected boolean hasEnrolmentWithEnroledState(final EnrolmentContext enrolmentContext,
            final CurricularCourse curricularCourse, final ExecutionYear executionYear) {

        for (final ExecutionInterval executionInterval : executionYear.getChildIntervals()) {
            if (enrolmentContext.getStudentCurricularPlan().getRoot().hasEnrolmentWithEnroledState(curricularCourse,
                    executionInterval)) {
                return true;
            }
        }

        return false;
    }

    protected boolean isEnrolling(final EnrolmentContext enrolmentContext, final DegreeModule degreeModule) {
        final IDegreeModuleToEvaluate degreeModuleToEvaluate = searchDegreeModuleToEvaluate(enrolmentContext, degreeModule);
        return degreeModuleToEvaluate != null && !degreeModuleToEvaluate.isEnroled();
    }

    protected boolean isEnrolling(final EnrolmentContext enrolmentContext, final DegreeModule degreeModule,
            final ExecutionInterval executionInterval) {
        final IDegreeModuleToEvaluate degreeModuleToEvaluate = searchDegreeModuleToEvaluate(enrolmentContext, degreeModule);
        return degreeModuleToEvaluate != null && !degreeModuleToEvaluate.isEnroled()
                && degreeModuleToEvaluate.getExecutionInterval() == executionInterval;
    }

    protected RuleResult executeEnrolmentWithNoRules(final ICurricularRule curricularRule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
        final RuleResult ruleResult = executeEnrolmentWithRules(curricularRule, sourceDegreeModuleToEvaluate, enrolmentContext);
        if (ruleResult.isFalse() || (ruleResult.isTrue() && isTemporaryOrImpossible(sourceDegreeModuleToEvaluate, ruleResult))) {
            return RuleResult.createWarning(sourceDegreeModuleToEvaluate.getDegreeModule(), ruleResult.getMessages());
        } else {
            return ruleResult;
        }
    }

    
    protected RuleResult executeEnrolmentPrefilter(ICurricularRule curricularRule,
            IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, EnrolmentContext enrolmentContext) {
        return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
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
