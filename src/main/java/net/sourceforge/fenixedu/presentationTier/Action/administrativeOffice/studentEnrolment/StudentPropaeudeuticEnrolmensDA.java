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

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.StudentPropaeudeuticEnrolmentsBean;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.studentCurriculum.NoCourseGroupCurriculumGroupType;
import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.SearchForStudentsDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/studentPropaeudeuticEnrolments", module = "academicAdministration", functionality = SearchForStudentsDA.class)
@Forwards({
        @Forward(name = "showExtraEnrolments", path = "/academicAdminOffice/showNoCourseGroupCurriculumGroupEnrolments.jsp"),
        @Forward(name = "chooseExtraEnrolment", path = "/academicAdminOffice/chooseNoCourseGroupCurriculumGroupEnrolment.jsp"),
        @Forward(name = "showDegreeModulesToEnrol",
                path = "/academicAdministration/studentEnrolments.do?method=prepareFromExtraEnrolment") })
public class StudentPropaeudeuticEnrolmensDA extends NoCourseGroupCurriculumGroupEnrolmentsDA {

    @Override
    protected StudentPropaeudeuticEnrolmentsBean createNoCourseGroupEnrolmentBean(StudentCurricularPlan studentCurricularPlan,
            ExecutionSemester executionSemester) {
        return new StudentPropaeudeuticEnrolmentsBean(studentCurricularPlan, executionSemester);
    }

    @Override
    protected String getActionName() {
        return "studentPropaeudeuticEnrolments";
    }

    @Override
    protected NoCourseGroupCurriculumGroupType getGroupType() {
        return NoCourseGroupCurriculumGroupType.PROPAEDEUTICS;
    }

}
