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
package net.sourceforge.fenixedu.presentationTier.Action.credits.departmentMember;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.credits.ManageDegreeTeachingServicesDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "departmentMember", path = "/degreeTeachingServiceManagement",
        input = "/degreeTeachingServiceManagement.do?method=showTeachingServiceDetails",
        attribute = "teacherExecutionCourseShiftProfessorshipForm", formBean = "teacherExecutionCourseShiftProfessorshipForm",
        functionality = DepartmentMemberViewTeacherCreditsDA.class)
@Forwards(value = {
        @Forward(name = "teacher-not-found", path = "/departmentMember/credits.do?method=viewAnnualTeachingCredits"),
        @Forward(name = "sucessfull-edit", path = "/departmentMember/credits.do?method=viewAnnualTeachingCredits"),
        @Forward(name = "show-teaching-service-percentages",
                path = "/credits/degreeTeachingService/showTeachingServicePercentages.jsp") })
@Exceptions(value = { @ExceptionHandling(type = java.lang.NumberFormatException.class,
        key = "message.invalid.professorship.percentage", handler = org.apache.struts.action.ExceptionHandler.class,
        path = "/degreeTeachingServiceManagement.do?method=showTeachingServiceDetails&page=0", scope = "request") })
public class DepartmentMemberManageDegreeTeachingServicesDispatchAction extends ManageDegreeTeachingServicesDispatchAction {

    public ActionForward showTeachingServiceDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException {

        DynaActionForm dynaForm = (DynaActionForm) form;
        Professorship professorship = getDomainObject(dynaForm, "professorshipID");

        if (professorship == null || professorship.getTeacher() != getLoggedTeacher(request)) {
            return mapping.findForward("teacher-not-found");
        }

        teachingServiceDetailsProcess(professorship, request, dynaForm);
        return mapping.findForward("show-teaching-service-percentages");
    }

    private Teacher getLoggedTeacher(HttpServletRequest request) {
        User userView = Authenticate.getUser();
        return userView.getPerson().getTeacher();
    }

    public ActionForward updateTeachingServices(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException {
        return updateTeachingServices(mapping, form, request, RoleType.DEPARTMENT_MEMBER);
    }
}
