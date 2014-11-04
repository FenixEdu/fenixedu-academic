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
package org.fenixedu.academic.ui.struts.action.manager.transition;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.dto.commons.student.StudentNumberBean;
import org.fenixedu.academic.ui.struts.action.commons.transition.AbstractBolonhaTransitionManagementDA;
import org.fenixedu.academic.ui.struts.action.manager.ManagerApplications.ManagerBolonhaTransitionApp;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

@StrutsFunctionality(app = ManagerBolonhaTransitionApp.class, path = "student-curriculum", titleKey = "title.student.curriculum")
@Mapping(module = "manager", path = "/bolonhaTransitionManagement", formBean = "bolonhaTransitionManagementForm",
        scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "chooseStudent", path = "/manager/transition/bolonha/chooseStudent.jsp"),
        @Forward(name = "showStudentCurricularPlan", path = "/manager/transition/bolonha/showStudentCurricularPlan.jsp"),
        @Forward(name = "chooseRegistration", path = "/manager/transition/bolonha/chooseRegistration.jsp") })
public class BolonhaTransitionManagementDA extends AbstractBolonhaTransitionManagementDA {

    @EntryPoint
    public ActionForward prepareChooseStudent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("studentNumberBean", new StudentNumberBean());
        return mapping.findForward("chooseStudent");
    }

    public ActionForward chooseStudent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final StudentNumberBean studentNumberBean = (StudentNumberBean) getObjectFromViewState("student-number-bean");
        request.setAttribute("studentId", Student.readStudentByNumber(studentNumberBean.getNumber()).getExternalId());

        return prepare(mapping, form, request, response);
    }

    @Override
    protected List<Registration> getRegistrations(final HttpServletRequest request) {
        final Student student = getStudent(request);
        return student != null ? student.getTransitionRegistrations() : Collections.<Registration> emptyList();
    }

    private Student getStudent(final HttpServletRequest request) {
        return getDomainObject(request, "studentId");
    }

}
