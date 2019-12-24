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
package org.fenixedu.academic.ui.struts.action.administrativeOffice.studentEnrolment;

import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.studentCurriculum.NoCourseGroupCurriculumGroupType;
import org.fenixedu.academic.dto.administrativeOffice.studentEnrolment.StudentExtraEnrolmentBean;
import org.fenixedu.academic.ui.struts.action.administrativeOffice.student.SearchForStudentsDA;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

@Mapping(path = "/studentExtraEnrolments", module = "academicAdministration", functionality = SearchForStudentsDA.class)
@Forwards({ @Forward(name = "showExtraEnrolments", path = "/academicAdminOffice/showNoCourseGroupCurriculumGroupEnrolments.jsp"),
        @Forward(name = "chooseExtraEnrolment", path = "/academicAdminOffice/chooseNoCourseGroupCurriculumGroupEnrolment.jsp"),
        @Forward(name = "showDegreeModulesToEnrol",
                path = "/academicAdministration/studentEnrolments.do?method=prepareFromExtraEnrolment") })
public class StudentExtraEnrolmentsDA extends NoCourseGroupCurriculumGroupEnrolmentsDA {

    @Override
    protected StudentExtraEnrolmentBean createNoCourseGroupEnrolmentBean(final StudentCurricularPlan studentCurricularPlan,
            final ExecutionInterval executionInterval) {
        return new StudentExtraEnrolmentBean(studentCurricularPlan, executionInterval);
    }

    @Override
    protected String getActionName() {
        return "studentExtraEnrolments";
    }

    @Override
    protected NoCourseGroupCurriculumGroupType getGroupType() {
        return NoCourseGroupCurriculumGroupType.EXTRA_CURRICULAR;
    }

}
