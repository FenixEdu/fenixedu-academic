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
package org.fenixedu.academic.domain.curricularRules;

import java.util.List;

import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.curricularRules.executors.RuleResult;
import org.fenixedu.academic.domain.curricularRules.executors.ruleExecutors.CurricularRuleExecutorFactory;
import org.fenixedu.academic.domain.curricularRules.executors.verifyExecutors.VerifyRuleLevel;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.enrolment.EnrolmentContext;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.util.LogicOperator;
import org.fenixedu.academic.dto.GenericPair;
import org.fenixedu.bennu.core.domain.Bennu;

public abstract class CurricularRule extends CurricularRule_Base implements ICurricularRule {

    protected CurricularRule() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    protected void init(final DegreeModule degreeModuleToApplyRule, final CourseGroup contextCourseGroup,
            final ExecutionInterval begin, final ExecutionInterval end, final CurricularRuleType type) {

        // TODO assure only one rule of a certain type for a given execution
        // period

        init(degreeModuleToApplyRule, contextCourseGroup, begin, end);
        checkCurricularRuleType(type);
        setCurricularRuleType(type);
    }

    private void checkCurricularRuleType(final CurricularRuleType type) {
        if (type == null) {
            throw new DomainException("curricular.rule.invalid.parameters");
        }
    }

    protected void init(final DegreeModule degreeModuleToApplyRule, final CourseGroup contextCourseGroup,
            final ExecutionInterval begin, final ExecutionInterval end) {

        checkParameters(degreeModuleToApplyRule, begin);
        checkExecutionPeriods(begin, end);
        setDegreeModuleToApplyRule(degreeModuleToApplyRule);
        setContextCourseGroup(contextCourseGroup);
        setBegin(begin);
        setEnd(end);
    }

    protected void checkParameters(final DegreeModule degreeModuleToApplyRule, final ExecutionInterval begin) {
        if (degreeModuleToApplyRule == null || begin == null) {
            throw new DomainException("curricular.rule.invalid.parameters");
        }
    }

    protected void edit(ExecutionInterval beginExecutionPeriod, ExecutionInterval endExecutionPeriod) {
        checkExecutionPeriods(beginExecutionPeriod, endExecutionPeriod);
        setBegin(beginExecutionPeriod);
        setEnd(endExecutionPeriod);
    }

    public void delete() {
        super.setCurricularPeriod(null);
        removeOwnParameters();
        removeCommonParameters();
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    protected void removeCommonParameters() {
        setDegreeModuleToApplyRule(null);
        setBegin(null);
        setEnd(null);
        setParentCompositeRule(null);
        setContextCourseGroup(null);
        setRootDomainObject(null);
    }

    @Override
    public boolean appliesToContext(final Context context) {
        return (context == null || appliesToCourseGroup(context.getParentCourseGroup()))
                && (getCurricularPeriod() == null || getCurricularPeriod() == context.getCurricularPeriod());
    }

    @Override
    public boolean appliesToCourseGroup(final CourseGroup courseGroup) {
        return (!hasContextCourseGroup() || getContextCourseGroup() == courseGroup);
    }

    @Override
    public boolean isCompositeRule() {
        return getCurricularRuleType() == null;
    }

    protected boolean belongsToCompositeRule() {
        return getParentCompositeRule() != null;
    }

    /**
     * @deprecated use {@link #getBeginInterval()} instead.
     */
    @Deprecated
    @Override
    public ExecutionInterval getBegin() {
        return belongsToCompositeRule() ? getParentCompositeRule().getBegin() : super.getBegin();
    }

    public ExecutionInterval getBeginInterval() {
        return belongsToCompositeRule() ? getParentCompositeRule().getBeginInterval() : super.getBegin();
    }

    /**
     * @deprecated use {@link #getEndInterval()} instead.
     */
    @Deprecated
    @Override
    public ExecutionInterval getEnd() {
        return belongsToCompositeRule() ? getParentCompositeRule().getEnd() : super.getEnd();
    }

    public ExecutionInterval getEndInterval() {
        return belongsToCompositeRule() ? getParentCompositeRule().getEndInterval() : super.getEnd();
    }

    @Override
    public DegreeModule getDegreeModuleToApplyRule() {
        return belongsToCompositeRule() ? getParentCompositeRule()
                .getDegreeModuleToApplyRule() : super.getDegreeModuleToApplyRule();
    }

    @Override
    public CourseGroup getContextCourseGroup() {
        return belongsToCompositeRule() ? getParentCompositeRule().getContextCourseGroup() : super.getContextCourseGroup();
    }

    public boolean hasContextCourseGroup(final CourseGroup parent) {
        return getContextCourseGroup() == parent;
    }

    @Override
    public boolean hasCurricularRuleType(final CurricularRuleType ruleType) {
        return getCurricularRuleType() == ruleType;
    }

    @Override
    public boolean isValid(ExecutionInterval executionInterval) {
        return (getBeginInterval().isBeforeOrEquals(executionInterval)
                && (getEndInterval() == null || getEndInterval().isAfterOrEquals(executionInterval)));
    }

    @Override
    public boolean isValid(ExecutionYear executionYear) {
        for (ExecutionInterval executionInterval : executionYear.getChildIntervals()) {
            if (isValid(executionInterval)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isActive() {
        return getEndInterval() == null || getEndInterval().getAcademicInterval().containsNow();
    }

    protected void checkExecutionPeriods(ExecutionInterval beginExecutionInterval, ExecutionInterval endExecutionInterval) {
        if (endExecutionInterval != null && beginExecutionInterval.isAfter(endExecutionInterval)) {
            throw new DomainException("curricular.rule.begin.is.after.end.execution.period");
        }
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public RuleResult evaluate(final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate,
            final EnrolmentContext enrolmentContext) {
        return CurricularRuleExecutorFactory.findExecutor(this).execute(this, sourceDegreeModuleToEvaluate, enrolmentContext);
    }

    @Override
    public RuleResult verify(final VerifyRuleLevel level, final EnrolmentContext enrolmentContext,
            final DegreeModule degreeModuleToVerify, final CourseGroup parentCourseGroup) {
        return createVerifyRuleExecutor().verify(this, level, enrolmentContext, degreeModuleToVerify, parentCourseGroup);
    }

    abstract protected void removeOwnParameters();

    abstract public boolean isLeaf();

    abstract public boolean isRulePreventingAutomaticEnrolment();

    @Override
    abstract public List<GenericPair<Object, Boolean>> getLabel();

    static public CurricularRule createCurricularRule(final LogicOperator logicOperator,
            final CurricularRule... curricularRules) {
        switch (logicOperator) {
        case AND:
            return new AndRule(curricularRules);
        case OR:
            return new OrRule(curricularRules);
        case NOT:
            if (curricularRules.length != 1) {
                throw new DomainException("error.invalid.notRule.parameters");
            }
            return new NotRule(curricularRules[0]);
        default:
            throw new DomainException("error.unsupported.logic.operator");
        }
    }

    @Override
    public boolean hasContextCourseGroup() {
        return getContextCourseGroup() != null;
    }

}
