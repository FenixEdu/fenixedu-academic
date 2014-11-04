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
package org.fenixedu.academic.ui.struts.action.student;

import org.fenixedu.academic.ui.struts.action.administrativeOffice.student.CurriculumDispatchAction;
import org.fenixedu.academic.ui.struts.action.student.StudentApplication.StudentViewApp;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

@StrutsFunctionality(app = StudentViewApp.class, path = "curriculum", titleKey = "link.student.curriculum")
@Mapping(path = "/viewStudentCurriculum", module = "student", formBean = "studentCurricularPlanAndEnrollmentsSelectionForm")
@Forwards({ @Forward(name = "chooseRegistration", path = "/student/curriculum/chooseRegistration.jsp"),
        @Forward(name = "ShowStudentCurriculum", path = "/student/curriculum/displayStudentCurriculum_bd.jsp"),
        @Forward(name = "NotAuthorized", path = "/student/notAuthorized_bd.jsp") })
public class CurriculumDispatchActionForStudent extends CurriculumDispatchAction {

}
