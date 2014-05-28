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
package net.sourceforge.fenixedu.presentationTier.Action.externalSupervision.consult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.CurriculumDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
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
