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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.dto.student.StudentPortalBean;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsApplication;

@StrutsApplication(bundle = "TitlesResources", path = "student", titleKey = "private.student",
        accessGroup = StudentApplication.ACCESS_GROUP, hint = StudentApplication.HINT)
@Mapping(module = "student", path = "/showStudentPortal")
@Forwards(value = { @Forward(name = "studentPortal", path = "/student/main_bd.jsp") })
public class ShowStudentPortalDA extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        List<StudentPortalBean> studentPortalBeans = new ArrayList<StudentPortalBean>();
        List<String> genericDegreeWarnings = new ArrayList<String>();

        final Student student = Authenticate.getUser().getPerson().getStudent();
        if (student != null) {
            for (Registration registration : student.getAllRegistrations()) {

                final StudentCurricularPlan scp = registration.getLastStudentCurricularPlan();
                if (scp != null) {
                    DegreeCurricularPlan degreeCurricularPlan = scp.getDegreeCurricularPlan();
                    if (registration.getAttendingExecutionCoursesForCurrentExecutionPeriod().isEmpty() == false) {
                        studentPortalBeans.add(new StudentPortalBean(registration.getDegree(), student,
                                registration.getAttendingExecutionCoursesForCurrentExecutionPeriod(), degreeCurricularPlan));
                    }
                }
            }
        }

        request.setAttribute("genericDegreeWarnings", genericDegreeWarnings);
        request.setAttribute("studentPortalBeans", studentPortalBeans);
        return mapping.findForward("studentPortal");
    }

}