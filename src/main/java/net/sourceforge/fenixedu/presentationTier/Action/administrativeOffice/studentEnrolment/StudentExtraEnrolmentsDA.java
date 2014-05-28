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
package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.studentEnrolment;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.StudentExtraEnrolmentBean;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.studentCurriculum.NoCourseGroupCurriculumGroupType;
import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.SearchForStudentsDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/studentExtraEnrolments", module = "academicAdministration", functionality = SearchForStudentsDA.class)
@Forwards({
        @Forward(name = "showExtraEnrolments", path = "/academicAdminOffice/showNoCourseGroupCurriculumGroupEnrolments.jsp"),
        @Forward(name = "chooseExtraEnrolment", path = "/academicAdminOffice/chooseNoCourseGroupCurriculumGroupEnrolment.jsp"),
        @Forward(name = "showDegreeModulesToEnrol",
                path = "/academicAdministration/studentEnrolments.do?method=prepareFromExtraEnrolment") })
public class StudentExtraEnrolmentsDA extends NoCourseGroupCurriculumGroupEnrolmentsDA {

    @Override
    protected StudentExtraEnrolmentBean createNoCourseGroupEnrolmentBean(final StudentCurricularPlan studentCurricularPlan,
            final ExecutionSemester executionSemester) {
        return new StudentExtraEnrolmentBean(studentCurricularPlan, executionSemester);
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
