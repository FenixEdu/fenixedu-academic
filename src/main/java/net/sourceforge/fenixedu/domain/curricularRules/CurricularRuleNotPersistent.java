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

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.CurricularRuleExecutorFactory;
import net.sourceforge.fenixedu.domain.curricularRules.executors.verifyExecutors.VerifyRuleLevel;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;

import org.joda.time.YearMonthDay;

abstract public class CurricularRuleNotPersistent implements ICurricularRule {

    @Override
    public boolean equals(Object obj) {
        if (!getClass().equals(obj.getClass())) {
            return false;
        }

        CurricularRuleNotPersistent curricularRuleNotPersistent = null;
        if (obj instanceof CurricularRuleNotPersistent) {
            curricularRuleNotPersistent = (CurricularRuleNotPersistent) obj;
        } else {
            return false;
        }

        return this.getDegreeModuleToApplyRule() == curricularRuleNotPersistent.getDegreeModuleToApplyRule()
                && this.getCurricularRuleType() == curricularRuleNotPersistent.getCurricularRuleType();
    }

    @Override
    public int hashCode() {
        final StringBuilder builder = new StringBuilder();
        if (getDegreeModuleToApplyRule() != null) {
            builder.append(String.valueOf(getDegreeModuleToApplyRule().hashCode()));
            builder.append('@');
        }
        builder.append(String.valueOf(getCurricularRuleType().hashCode()));
        return builder.toString().hashCode();
    }

    @Override
    public boolean appliesToContext(Context context) {
        return context == null || this.appliesToCourseGroup(context.getParentCourseGroup());
    }

    @Override
    public boolean appliesToCourseGroup(CourseGroup courseGroup) {
        return (this.getContextCourseGroup() == null || this.getContextCourseGroup() == courseGroup);
    }

    @Override
    public boolean hasContextCourseGroup() {
        return getContextCourseGroup() != null;
    }

    @Override
    public boolean hasCurricularRuleType(CurricularRuleType ruleType) {
        return getCurricularRuleType() == ruleType;
    }

    @Override
    public boolean isCompositeRule() {
        return getCurricularRuleType() == null;
    }

    @Override
    public boolean isValid(ExecutionSemester executionSemester) {
        return (getBegin().isBeforeOrEquals(executionSemester) && (getEnd() == null || getEnd()
                .isAfterOrEquals(executionSemester)));
    }

    @Override
    public boolean isValid(ExecutionYear executionYear) {
        for (ExecutionSemester executionSemester : executionYear.getExecutionPeriods()) {
            if (isValid(executionSemester)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public boolean isActive() {
        return getEnd() == null || getEnd().containsDay(new YearMonthDay());
    }

    @Override
    public RuleResult evaluate(final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, EnrolmentContext enrolmentContext) {
        return CurricularRuleExecutorFactory.findExecutor(this).execute(this, sourceDegreeModuleToEvaluate, enrolmentContext);
    }

    @Override
    public RuleResult verify(final VerifyRuleLevel level, final EnrolmentContext enrolmentContext,
            final DegreeModule degreeModuleToVerify, final CourseGroup parentCourseGroup) {
        return createVerifyRuleExecutor().verify(this, level, enrolmentContext, degreeModuleToVerify, parentCourseGroup);
    }

}
