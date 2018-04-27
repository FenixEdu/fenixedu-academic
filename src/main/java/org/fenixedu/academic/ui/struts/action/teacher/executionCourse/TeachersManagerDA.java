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
package org.fenixedu.academic.ui.struts.action.teacher.executionCourse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.service.services.teacher.DeleteProfessorshipWithPerson;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.academic.ui.struts.action.teacher.ManageExecutionCourseDA;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Input;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@Mapping(path = "/teachersManagerDA", module = "teacher", functionality = ManageExecutionCourseDA.class)
@Forwards({ @Forward(name = "viewTeachers_bd", path = "/teacher/viewTeachers_bd.jsp"),
        @Forward(name = "viewProfessorshipProperties", path = "/teacher/viewProfessorshipProperties.jsp"),
        @Forward(name = "associateTeacher_bd", path = "/teacher/associateTeacher_bd.jsp") })
public class TeachersManagerDA extends ExecutionCourseBaseAction {

    public ActionForward viewTeachersByProfessorship(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("viewTeachers_bd");
    }

    public ActionForward viewProfessorshipProperties(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("professorship", getDomainObject(request, "teacherOID"));
        return mapping.findForward("viewProfessorshipProperties");
    }

    public ActionForward removeTeacher(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        Professorship professorship = getDomainObject(request, "teacherOID");
        try {
            DeleteProfessorshipWithPerson.run(professorship.getPerson(), getExecutionCourse(request));
        } catch (NotAuthorizedException e) {
            final ActionMessages actionErrors = new ActionErrors();
            actionErrors.add("error", new ActionMessage("label.not.authorized.action"));
            saveErrors(request, actionErrors);
        } catch (DomainException domainException) {
            final ActionMessages actionErrors = new ActionErrors();
            actionErrors.add("error", new ActionMessage(domainException.getMessage()));
            saveErrors(request, actionErrors);
        }
        return viewTeachersByProfessorship(mapping, form, request, response);
    }

    @Input
    public ActionForward prepareAssociateTeacher(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("associateTeacher_bd");
    }

    public ActionForward associateTeacher(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        if (isCancelled(request)) {
            RenderUtils.invalidateViewState();
            return prepareAssociateTeacher(mapping, form, request, response);
        }

        Person person = Person.readPersonByUsername(request.getParameter("teacherId"));
        final ExecutionCourse executionCourse = getExecutionCourse(request);

        if (person != null) {
            if (person.getTeacher() != null && person.getTeacher().hasTeacherAuthorization(executionCourse.getAcademicInterval())) {
                Professorship professorship = Professorship.create(false, executionCourse, person);
                request.setAttribute("teacherOID", professorship.getExternalId());
            } else {
                final ActionMessages actionErrors = new ActionErrors();
                actionErrors.add("error", new ActionMessage("label.invalid.teacher.without.auth"));
                saveErrors(request, actionErrors);
                return prepareAssociateTeacher(mapping, form, request, response);
            }
        } else {
            final ActionMessages actionErrors = new ActionErrors();
            actionErrors.add("error", new ActionMessage("label.invalid.teacher.number"));
            saveErrors(request, actionErrors);
            return prepareAssociateTeacher(mapping, form, request, response);
        }
        return viewProfessorshipProperties(mapping, form, request, response);
    }

}
