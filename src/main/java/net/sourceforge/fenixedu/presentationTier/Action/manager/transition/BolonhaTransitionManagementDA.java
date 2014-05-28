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
package net.sourceforge.fenixedu.presentationTier.Action.manager.transition;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.commons.student.StudentNumberBean;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.commons.transition.AbstractBolonhaTransitionManagementDA;
import net.sourceforge.fenixedu.presentationTier.Action.manager.ManagerApplications.ManagerBolonhaTransitionApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = ManagerBolonhaTransitionApp.class, path = "student-curriculum", titleKey = "title.student.curriculum")
@Mapping(module = "manager", path = "/bolonhaTransitionManagement", attribute = "bolonhaTransitionManagementForm",
        formBean = "bolonhaTransitionManagementForm", scope = "request", parameter = "method")
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
