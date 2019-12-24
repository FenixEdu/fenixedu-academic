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

import java.util.Collections;
import java.util.List;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.curricularRules.executors.verifyExecutors.VerifyRuleExecutor;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.GenericPair;

public class AssertUniqueApprovalInCurricularCourseContexts extends CurricularRuleNotPersistent {

    private CurricularCourse toApply;

    public AssertUniqueApprovalInCurricularCourseContexts(final CurricularCourse toApply) {
        if (toApply == null) {
            throw new DomainException("curricular.rule.invalid.parameters");
        } else {
            this.toApply = toApply;
        }
    }

    @Override
    public List<GenericPair<Object, Boolean>> getLabel() {
        return Collections
                .singletonList(new GenericPair<Object, Boolean>("label.assertUniqueApprovalInCurricularCourseContexts", true));
    }

    @Override
    public DegreeModule getDegreeModuleToApplyRule() {
        return toApply;
    }

    @Override
    public CourseGroup getContextCourseGroup() {
        return null;
    }

    @Override
    public CompositeRule getParentCompositeRule() {
        return null;
    }

    @Override
    public CurricularRuleType getCurricularRuleType() {
        return CurricularRuleType.ASSERT_UNIQUE_APPROVAL_IN_CURRICULAR_COURSE_CONTEXTS;
    }

    @Override
    public ExecutionInterval getBegin() {
        return ExecutionInterval.findFirstCurrentChild(toApply.getDegree().getCalendar());
    }

    @Override
    public ExecutionInterval getEnd() {
        return null;
    }

    @Override
    public ExecutionInterval getBeginInterval() {
        return getBegin();
    }

    @Override
    public ExecutionInterval getEndInterval() {
        return null;
    }

    @Override
    public VerifyRuleExecutor createVerifyRuleExecutor() {
        return VerifyRuleExecutor.NULL_VERIFY_EXECUTOR;
    }

}
