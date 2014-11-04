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

import java.util.Collections;
import java.util.List;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.curricularRules.CompositeRule;
import org.fenixedu.academic.domain.curricularRules.CurricularRuleNotPersistent;
import org.fenixedu.academic.domain.curricularRules.CurricularRuleType;
import org.fenixedu.academic.domain.curricularRules.executors.verifyExecutors.VerifyRuleExecutor;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.alert.AlertService;
import org.fenixedu.academic.dto.GenericPair;

public class PhdValidCurricularCoursesRule extends CurricularRuleNotPersistent {

    transient private CurricularCourse toApply;

    public PhdValidCurricularCoursesRule(final CurricularCourse toApply) {
        if (toApply == null) {
            throw new DomainException("curricular.rule.invalid.parameters");
        }
        this.toApply = toApply;
    }

    @Override
    public VerifyRuleExecutor createVerifyRuleExecutor() {
        return VerifyRuleExecutor.NULL_VERIFY_EXECUTOR;
    }

    @Override
    public ExecutionSemester getBegin() {
        return ExecutionSemester.readActualExecutionSemester();
    }

    @Override
    public ExecutionSemester getEnd() {
        return null;
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
        // label not add to resources
        return CurricularRuleType.PHD_VALID_CURRICULAR_COURSES;
    }

    @Override
    public CurricularCourse getDegreeModuleToApplyRule() {
        return toApply;
    }

    @Override
    public List<GenericPair<Object, Boolean>> getLabel() {
        return Collections.singletonList(new GenericPair<Object, Boolean>(AlertService
                .getMessageFromResource("label.phd.enrolments.PhdValidCurricularCoursesRule"), false));
    }
}
