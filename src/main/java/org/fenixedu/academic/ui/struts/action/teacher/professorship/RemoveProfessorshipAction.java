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
/*
 * Created on Dec 11, 2003 by jpvl
 *
 */
package org.fenixedu.academic.ui.struts.action.teacher.professorship;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.action.ExceptionHandler;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.service.services.exceptions.notAuthorizedServiceDeleteException;
import org.fenixedu.academic.service.services.teacher.RemoveProfessorshipWithPerson;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.departmentAdmOffice.TeacherSearchForExecutionCourseAssociation;
import org.fenixedu.bennu.struts.annotations.ExceptionHandling;
import org.fenixedu.bennu.struts.annotations.Exceptions;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixframework.FenixFramework;

/**
 * @author jpvl
 */
@Mapping(module = "departmentAdmOffice", path = "/removeProfessorship", input = "/showTeacherProfessorshipsForManagement.do",
        formBean = "teacherExecutionCourseForm", validate = false,
        functionality = TeacherSearchForExecutionCourseAssociation.class)
@Forwards({ @Forward(name = "successfull-delete", path = "/departmentAdmOffice/showTeacherProfessorshipsForManagement.do") })
@Exceptions(value = { @ExceptionHandling(type = notAuthorizedServiceDeleteException.class,
        key = "message.professorship.isResponsibleFor", handler = ExceptionHandler.class,
        path = "/showTeacherProfessorshipsForManagement.do", scope = "request") })
public class RemoveProfessorshipAction extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        DynaActionForm teacherExecutionCourseForm = (DynaActionForm) form;

        String id = (String) teacherExecutionCourseForm.get("teacherId");
        String executionCourseId = (String) teacherExecutionCourseForm.get("executionCourseId");

        ActionMessages actionMessages = getMessages(request);
        try {
            RemoveProfessorshipWithPerson.run(Person.readPersonByUsername(id),
                    FenixFramework.<ExecutionCourse> getDomainObject(executionCourseId));
        } catch (DomainException de) {
            actionMessages.add(de.getMessage(), new ActionMessage(de.getMessage()));
            saveMessages(request, actionMessages);
        }
        return mapping.findForward("successfull-delete");
    }
}