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
package org.fenixedu.academic.ui.struts.action.administrativeOffice.student;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.dto.student.StudentsSearchBean;
import org.fenixedu.academic.ui.struts.action.academicAdministration.AcademicAdministrationApplication.AcademicAdminStudentsApp;
import org.fenixedu.academic.ui.struts.action.administrativeOffice.student.EditCandidacyInformationDA.ChooseRegistrationOrPhd;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

@StrutsFunctionality(app = AcademicAdminStudentsApp.class, path = "search-for-students",
        titleKey = "link.studentOperations.viewStudents", accessGroup = "academic(scope=OFFICE)")
@Mapping(path = "/students", module = "academicAdministration")
@Forwards({ @Forward(name = "viewStudentDetails", path = "/academicAdminOffice/student/viewStudentDetails.jsp"),
        @Forward(name = "search", path = "/academicAdminOffice/searchStudents.jsp") })
public class SearchForStudentsDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepareSearch(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        StudentsSearchBean studentsSearchBean = getRenderedObject();

        if (studentsSearchBean == null) { // 1st time
            studentsSearchBean = new StudentsSearchBean();
        } else {

            final Set<Student> students = studentsSearchBean.search();

            if (students.size() == 1) {
                Student student = students.iterator().next();
                request.setAttribute("student", student);
                request.setAttribute("choosePhdOrRegistration", new ChooseRegistrationOrPhd(student));
                return mapping.findForward("viewStudentDetails");
            }
            request.setAttribute("students", students);
        }

        request.setAttribute("studentsSearchBean", studentsSearchBean);
        return mapping.findForward("search");
    }

}
