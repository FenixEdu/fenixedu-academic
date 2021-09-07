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
import java.util.List;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.curricularRules.executors.verifyExecutors.EnrolmentToBeApprovedByCoordinatorVerifier;
import org.fenixedu.academic.domain.curricularRules.executors.verifyExecutors.VerifyRuleExecutor;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.dto.GenericPair;

public class EnrolmentToBeApprovedByCoordinator extends EnrolmentToBeApprovedByCoordinator_Base {

    private EnrolmentToBeApprovedByCoordinator() {
        super();
        setCurricularRuleType(CurricularRuleType.ENROLMENT_TO_BE_APPROVED_BY_COORDINATOR);
    }

    protected EnrolmentToBeApprovedByCoordinator(final CurricularCourse toApplyRule, final CourseGroup contextCourseGroup,
            final ExecutionSemester begin, final ExecutionSemester end) {

        this();
        init(toApplyRule, contextCourseGroup, begin, end);
    }

    protected void edit(CourseGroup contextCourseGroup) {
        setContextCourseGroup(contextCourseGroup);
    }

    @Override
    public List<GenericPair<Object, Boolean>> getLabel() {
        return getLabel(null);
    }

    @Override
    public List<GenericPair<Object, Boolean>> getLabel(final ExecutionSemester executionSemester) {
        final List<GenericPair<Object, Boolean>> labelList = new ArrayList<GenericPair<Object, Boolean>>(1);
        labelList.add(new GenericPair<Object, Boolean>("label.enrolmentToBeApprovedByCoordinator", true));
        if (getContextCourseGroup() != null) {
            labelList.add(new GenericPair<Object, Boolean>(", ", false));
            labelList.add(new GenericPair<Object, Boolean>("label.inGroup", true));
            labelList.add(new GenericPair<Object, Boolean>(" ", false));
            labelList.add(new GenericPair<Object, Boolean>(getContextCourseGroup().getOneFullName(executionSemester), false));
        }
        return labelList;
    }

    @Override
    protected void removeOwnParameters() {
        // no domain parameters
    }

    @Override
    public VerifyRuleExecutor createVerifyRuleExecutor() {
        return new EnrolmentToBeApprovedByCoordinatorVerifier();
    }

}
