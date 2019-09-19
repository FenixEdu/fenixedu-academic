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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.curricularRules.executors.RuleResult;
import org.fenixedu.academic.domain.curricularRules.executors.verifyExecutors.VerifyRuleExecutor;
import org.fenixedu.academic.domain.curricularRules.executors.verifyExecutors.VerifyRuleLevel;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.enrolment.EnrolmentContext;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.GenericPair;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public abstract class CompositeRule extends CompositeRule_Base {

    static {
        getRelationCurricularRuleCompositeRule().addListener(new RelationAdapter<CompositeRule, CurricularRule>() {
            @Override
            public void beforeAdd(CompositeRule compositeRule, CurricularRule curricularRule) {
                if (curricularRule.getNotRule() != null) {
                    throw new DomainException("error.curricular.rule.invalid.state");
                }
            }
        });

    }

    protected CompositeRule() {
    }

    protected void initCompositeRule(CurricularRule... curricularRules) {
        if (curricularRules.length < 2) {
            throw new DomainException("curricular.rule.invalid.parameters");
        }

        if (!haveAllSameDegreeModule(curricularRules)) {
            throw new DomainException("curricular.rule.invalid.parameters");
        }

        this.setDegreeModuleToApplyRule(curricularRules[0].getDegreeModuleToApplyRule());

        this.setBegin(getBeginExecutionPeriod(curricularRules));
        this.setEnd(getEndExecutionPeriod(curricularRules));

        for (CurricularRule rule : curricularRules) {
            rule.setDegreeModuleToApplyRule(null);
            rule.setParentCompositeRule(this);
        }
    }

    private ExecutionSemester getEndExecutionPeriod(CurricularRule[] curricularRules) {
        ExecutionSemester executionSemester = null;
        for (CurricularRule rule : curricularRules) {
            if (rule.getEndInterval() == null) {
                return null;
            }
            if (executionSemester == null || rule.getEndInterval().isAfter(executionSemester)) {
                executionSemester = rule.getEndInterval().convert(ExecutionSemester.class);
            }
        }
        return executionSemester;
    }

    private ExecutionInterval getBeginExecutionPeriod(CurricularRule... curricularRules) {
        ExecutionInterval executionInterval = null;
        for (CurricularRule rule : curricularRules) {
            if (executionInterval == null || rule.getBeginInterval().isBefore(executionInterval)) {
                executionInterval = rule.getBeginInterval();
            }
        }
        return executionInterval;
    }

    private boolean haveAllSameDegreeModule(CurricularRule... curricularRules) {
        DegreeModule degreeModule = curricularRules[0].getDegreeModuleToApplyRule();
        for (CurricularRule rule : curricularRules) {
            if (!rule.getDegreeModuleToApplyRule().equals(degreeModule)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public abstract List<GenericPair<Object, Boolean>> getLabel();

    public List<GenericPair<Object, Boolean>> getLabel(final String operator) {
        final List<GenericPair<Object, Boolean>> labelList = new ArrayList<GenericPair<Object, Boolean>>();
        labelList.add(new GenericPair<Object, Boolean>("( ", false));
        final Iterator<CurricularRule> curricularRulesIterator = getCurricularRulesSet().iterator();
        while (curricularRulesIterator.hasNext()) {
            labelList.addAll(curricularRulesIterator.next().getLabel());
            if (curricularRulesIterator.hasNext()) {
                labelList.add(new GenericPair<Object, Boolean>(" ", false));
                labelList.add(new GenericPair<Object, Boolean>(operator, true));
                labelList.add(new GenericPair<Object, Boolean>(" ", false));
            }
        }
        labelList.add(new GenericPair<Object, Boolean>(" )", false));
        return labelList;
    }

    @Override
    public RuleResult evaluate(final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate,
            final EnrolmentContext enrolmentContext) {
        throw new DomainException("unsupported.composite.rule");
    }

    @Override
    public RuleResult verify(VerifyRuleLevel verifyRuleLevel, EnrolmentContext enrolmentContext,
            DegreeModule degreeModuleToVerify, CourseGroup parentCourseGroup) {
        throw new DomainException("unsupported.composite.rule");
    }

    @Override
    public VerifyRuleExecutor createVerifyRuleExecutor() {
        throw new DomainException(
                "error.org.fenixedu.academic.domain.curricularRules.CompositeRule.does.not.support.createVerifyRuleExecutor");
    }

    @Override
    public boolean appliesToContext(Context context) {
        for (CurricularRule curricularRule : this.getCurricularRulesSet()) {
            if (!curricularRule.appliesToContext(context)) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void removeOwnParameters() {
        for (; !getCurricularRulesSet().isEmpty(); getCurricularRulesSet().iterator().next().delete()) {
            ;
        }
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public boolean isRulePreventingAutomaticEnrolment() {
        for (final CurricularRule iter : getCurricularRulesSet()) {
            if (iter.isRulePreventingAutomaticEnrolment()) {
                return true;
            }
        }

        return false;
    }

}
