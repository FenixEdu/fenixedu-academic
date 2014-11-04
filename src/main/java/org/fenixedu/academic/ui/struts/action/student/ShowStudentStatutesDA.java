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
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.student.StudentStatute;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.student.StudentApplication.StudentViewApp;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

@StrutsFunctionality(app = StudentViewApp.class, descriptionKey = "label.student.statutes", path = "statutes",
        titleKey = "link.title.statutes")
@Mapping(path = "/ShowStudentStatutes", module = "student")
@Forwards({ @Forward(name = "studentStatutes", path = "/student/showStudentStatutes.jsp") })
public class ShowStudentStatutesDA extends FenixDispatchAction {

    @EntryPoint
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        User userView = getUserView(request);
        Student student = userView.getPerson().getStudent();
        ArrayList<StudentStatute> studentStatutes = new ArrayList<StudentStatute>(student.getStudentStatutesSet());
        Collections.sort(studentStatutes, new BeanComparator("beginExecutionPeriod.beginDateYearMonthDay"));
        request.setAttribute("studentStatutes", studentStatutes);
        return mapping.findForward("studentStatutes");
    }

}
