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
package net.sourceforge.fenixedu.presentationTier.Action.teacher.executionCourse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.DeleteProfessorshipWithPerson;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.ManageExecutionCourseDA;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Input;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/teachersManagerDA", module = "teacher", functionality = ManageExecutionCourseDA.class)
public class TeachersManagerDA extends ExecutionCourseBaseAction {

    public ActionForward viewTeachersByProfessorship(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return forward(request, "/teacher/viewTeachers_bd.jsp");
    }

    public ActionForward viewProfessorshipProperties(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("professorship", getDomainObject(request, "teacherOID"));
        return forward(request, "/teacher/viewProfessorshipProperties.jsp");
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
        return forward(request, "/teacher/associateTeacher_bd.jsp");
    }

    public ActionForward associateTeacher(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        if (isCancelled(request)) {
            RenderUtils.invalidateViewState();
            return prepareAssociateTeacher(mapping, form, request, response);
        }

        Person person = Person.readPersonByUsername(request.getParameter("teacherId"));

        if (person != null) {
            try {
                final ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester();
                if ((person.getTeacher() != null && (person.getTeacher().getTeacherAuthorization(executionSemester) != null || person
                        .hasRole(RoleType.TEACHER)))) {
                    Professorship professorship = Professorship.create(false, getExecutionCourse(request), person, 0.0);
                    request.setAttribute("teacherOID", professorship.getExternalId());
                } else if (person.getTeacher() == null || person.getTeacher().getCategoryByPeriod(executionSemester) == null) {
                    final ActionMessages actionErrors = new ActionErrors();
                    actionErrors.add("error", new ActionMessage("label.invalid.teacher.without.auth"));
                    saveErrors(request, actionErrors);
                    return prepareAssociateTeacher(mapping, form, request, response);
                }
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
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
