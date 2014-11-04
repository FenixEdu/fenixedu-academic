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
package org.fenixedu.academic.ui.struts.action.externalSupervision.consult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.ui.struts.action.administrativeOffice.student.CurriculumDispatchAction;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/viewCurriculum", module = "externalSupervision", formBean = "studentCurricularPlanAndEnrollmentsSelectionForm",
        functionality = ExternalSupervisorViewStudentDA.class)
@Forwards({ @Forward(name = "chooseRegistration", path = "/externalSupervision/consult/chooseRegistration.jsp"),
        @Forward(name = "ShowStudentCurriculum", path = "/externalSupervision/consult/showStudentCurriculum.jsp") })
public class ShowStudentCurriculum extends CurriculumDispatchAction {

    public ActionForward prepareForSupervisor(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        RenderUtils.invalidateViewState();

        final String personId = request.getParameter("personId");
        final Person personStudent = FenixFramework.getDomainObject(personId);
        final Student student = personStudent.getStudent();

        request.setAttribute("student", student);
        return mapping.findForward("chooseRegistration");
    }

    public ActionForward showCurriculum(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        RenderUtils.invalidateViewState();

        Registration registration = null;

        final String registrationId = request.getParameter("registrationId");
        registration = FenixFramework.getDomainObject(registrationId);

        if (registration == null) {
            return mapping.findForward("NotAuthorized");
        } else {
            return getStudentCPForSupervisor(registration, mapping, (DynaActionForm) form, request);
        }
    }
}
