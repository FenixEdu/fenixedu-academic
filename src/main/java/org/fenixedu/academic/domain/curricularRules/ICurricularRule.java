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

import java.util.Comparator;
import java.util.List;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.curricularRules.executors.RuleResult;
import org.fenixedu.academic.domain.curricularRules.executors.verifyExecutors.VerifyRuleExecutor;
import org.fenixedu.academic.domain.curricularRules.executors.verifyExecutors.VerifyRuleLevel;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.enrolment.EnrolmentContext;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.academic.dto.GenericPair;

public interface ICurricularRule {

    static final public Comparator<ICurricularRule> COMPARATOR_BY_BEGIN = new Comparator<ICurricularRule>() {
        @Override
        public int compare(ICurricularRule o1, ICurricularRule o2) {
            return o1.getBegin().compareTo(o2.getBegin());
        }
    };

    public List<GenericPair<Object, Boolean>> getLabel();
    
    public default List<GenericPair<Object, Boolean>> getLabel(final ExecutionSemester executionSemester){
        return getLabel();
    }

    public DegreeModule getDegreeModuleToApplyRule();

    public CourseGroup getContextCourseGroup();

    public CompositeRule getParentCompositeRule();

    public CurricularRuleType getCurricularRuleType();

    public ExecutionSemester getBegin();

    public ExecutionSemester getEnd();

    public boolean appliesToContext(final Context context);

    public boolean appliesToCourseGroup(final CourseGroup courseGroup);

    public boolean hasContextCourseGroup();

    public boolean hasCurricularRuleType(final CurricularRuleType ruleType);

    public boolean isCompositeRule();

    public boolean isValid(ExecutionSemester executionSemester);

    public boolean isValid(ExecutionYear executionYear);

    public boolean isVisible();

    public boolean isActive();

    public RuleResult evaluate(final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext);

    public RuleResult verify(final VerifyRuleLevel verifyRuleLevel, final EnrolmentContext enrolmentContext,
            final DegreeModule degreeModuleToVerify, final CourseGroup parentCourseGroup);

    public VerifyRuleExecutor createVerifyRuleExecutor();
}
