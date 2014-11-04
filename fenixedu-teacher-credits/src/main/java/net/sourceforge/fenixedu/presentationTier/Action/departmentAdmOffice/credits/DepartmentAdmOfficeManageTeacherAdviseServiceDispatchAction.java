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
/**
 * Nov 24, 2005
 */
package org.fenixedu.academic.ui.struts.action.departmentAdmOffice.credits;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.ui.struts.action.credits.ManageTeacherAdviseServiceDispatchAction;
import org.fenixedu.academic.ui.struts.action.credits.departmentAdmOffice.DepartmentAdmOfficeViewTeacherCreditsDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.ExceptionHandling;
import org.fenixedu.bennu.struts.annotations.Exceptions;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixframework.FenixFramework;

/**
 * @author Ricardo Rodrigues
 * 
 */

@Mapping(module = "departmentAdmOffice", path = "/teacherAdviseServiceManagement",
        input = "/teacherAdviseServiceManagement.do?method=showTeacherAdvises&page=0",
        formBean = "teacherDegreeFinalProjectStudentForm", functionality = DepartmentAdmOfficeViewTeacherCreditsDA.class)
@Forwards(value = {
        @Forward(name = "list-teacher-advise-services", path = "/credits/adviseServices/showTeacherAdviseServices.jsp"),
        @Forward(name = "successfull-delete",
                path = "/departmentAdmOffice/teacherAdviseServiceManagement.do?method=showTeacherAdvises&page=0"),
        @Forward(name = "successfull-edit",
                path = "/departmentAdmOffice/teacherAdviseServiceManagement.do?method=showTeacherAdvises&page=0"),
        @Forward(name = "teacher-not-found",
                path = "/departmentAdmOffice/showAllTeacherCreditsResume.do?method=showTeacherCreditsResume&page=0") })
@Exceptions(
        value = {
                @ExceptionHandling(
                        type = org.fenixedu.academic.service.services.exceptions.FenixServiceException.class,
                        handler = org.fenixedu.academic.ui.struts.config.FenixExceptionMessageHandler.class,
                        scope = "request"),
                @ExceptionHandling(type = org.fenixedu.academic.domain.exceptions.DomainException.class,
                        handler = org.fenixedu.academic.ui.struts.config.FenixDomainExceptionHandler.class,
                        scope = "request") })
public class DepartmentAdmOfficeManageTeacherAdviseServiceDispatchAction extends ManageTeacherAdviseServiceDispatchAction {

    public ActionForward showTeacherAdvises(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException {

        DynaActionForm dynaForm = (DynaActionForm) form;
        User userView = Authenticate.getUser();

        final ExecutionSemester executionSemester = getDomainObject(dynaForm, "executionPeriodId");

        Teacher teacher = FenixFramework.getDomainObject(dynaForm.getString("teacherId"));
        Collection<Department> manageableDepartments = userView.getPerson().getManageableDepartmentCreditsSet();

        if (teacher == null || teacher.getDepartment() == null
                || !manageableDepartments.contains(teacher.getDepartment())) {
            request.setAttribute("teacherNotFound", "teacherNotFound");
            return mapping.findForward("teacher-not-found");
        }

        getAdviseServices(request, dynaForm, executionSemester, teacher);
        return mapping.findForward("list-teacher-advise-services");
    }

    public ActionForward editAdviseService(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException {

        return editAdviseService(form, request, mapping, RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE);
    }

    public ActionForward deleteAdviseService(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException {

        deleteAdviseService(request, RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE);
        return mapping.findForward("successfull-delete");

    }
}
