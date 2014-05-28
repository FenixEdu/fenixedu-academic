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
package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.curricularRules.executors.verifyExecutors.VerifyRuleExecutor;
import net.sourceforge.fenixedu.domain.curricularRules.executors.verifyExecutors.VerifyRuleLevel;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
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
            if (rule.getEnd() == null) {
                return null;
            }
            if (executionSemester == null || rule.getEnd().isAfter(executionSemester)) {
                executionSemester = rule.getEnd();
            }
        }
        return executionSemester;
    }

    private ExecutionSemester getBeginExecutionPeriod(CurricularRule... curricularRules) {
        ExecutionSemester executionSemester = null;
        for (CurricularRule rule : curricularRules) {
            if (executionSemester == null || rule.getBegin().isBefore(executionSemester)) {
                executionSemester = rule.getBegin();
            }
        }
        return executionSemester;
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
        final Iterator<CurricularRule> curricularRulesIterator = getCurricularRules().iterator();
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
    public RuleResult evaluate(final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
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
                "error.net.sourceforge.fenixedu.domain.curricularRules.CompositeRule.does.not.support.createVerifyRuleExecutor");
    }

    @Override
    public boolean appliesToContext(Context context) {
        for (CurricularRule curricularRule : this.getCurricularRules()) {
            if (!curricularRule.appliesToContext(context)) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void removeOwnParameters() {
        for (; !getCurricularRules().isEmpty(); getCurricularRules().iterator().next().delete()) {
            ;
        }
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.curricularRules.CurricularRule> getCurricularRules() {
        return getCurricularRulesSet();
    }

    @Deprecated
    public boolean hasAnyCurricularRules() {
        return !getCurricularRulesSet().isEmpty();
    }

    @Deprecated
    public boolean hasCompositeRuleType() {
        return getCompositeRuleType() != null;
    }

}
