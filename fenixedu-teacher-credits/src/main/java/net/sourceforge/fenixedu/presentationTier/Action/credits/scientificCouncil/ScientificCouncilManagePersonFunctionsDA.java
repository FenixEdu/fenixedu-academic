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
package org.fenixedu.academic.ui.struts.action.credits.scientificCouncil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.credits.util.DepartmentCreditsBean;
import org.fenixedu.academic.domain.credits.util.PersonFunctionBean;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.PersonFunction;
import org.fenixedu.academic.ui.struts.action.credits.ManagePersonFunctionsDA;
import org.fenixedu.academic.ui.struts.action.scientificCouncil.ScientificCouncilApplication.ScientificCreditsApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = ScientificCreditsApp.class, path = "person-functions",
        titleKey = "link.managementPositions.management")
@Mapping(path = "/managePersonFunctionsShared", module = "scientificCouncil")
@Forwards({ @Forward(name = "addPersonFunction", path = "/credits/personFunction/addPersonFunction.jsp"),
        @Forward(name = "viewAnnualTeachingCredits", path = "/scientificCouncil/credits.do?method=viewAnnualTeachingCredits"),
        @Forward(name = "showDepartmentPersonFunctions", path = "/credits/showDepartmentPersonFunctions.jsp") })
public class ScientificCouncilManagePersonFunctionsDA extends ManagePersonFunctionsDA {

    public ActionForward prepareToAddPersonFunction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException, Exception {
        PersonFunctionBean personFunctionBean = getRenderedObject();
        if (personFunctionBean == null) {
            Teacher teacher = FenixFramework.getDomainObject((String) getFromRequest(request, "teacherOid"));
            ExecutionSemester executionSemester =
                    FenixFramework.getDomainObject((String) getFromRequest(request, "executionPeriodOid"));
            personFunctionBean = new PersonFunctionBean(teacher, executionSemester);
        }
        request.setAttribute("personFunctionBean", personFunctionBean);
        return mapping.findForward("addPersonFunction");
    }

    public ActionForward prepareToEditPersonFunction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException, Exception {
        PersonFunction personFunction = FenixFramework.getDomainObject((String) getFromRequest(request, "personFunctionOid"));
        ExecutionSemester executionSemester =
                FenixFramework.getDomainObject((String) getFromRequest(request, "executionPeriodOid"));
        request.setAttribute("personFunctionBean", new PersonFunctionBean(personFunction, executionSemester));
        return mapping.findForward("addPersonFunction");
    }

    public ActionForward editPersonFunction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException, Exception {
        PersonFunctionBean personFunctionBean = getRenderedObject();
        try {
            personFunctionBean.createOrEditPersonFunction();
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
            request.setAttribute("personFunctionBean", personFunctionBean);
            return mapping.findForward("addPersonFunction");
        }
        request.setAttribute("teacherOid", personFunctionBean.getTeacher().getExternalId());
        request.setAttribute("executionYearOid", personFunctionBean.getExecutionSemester().getExecutionYear().getExternalId());
        return mapping.findForward("viewAnnualTeachingCredits");
    }

    @EntryPoint
    public ActionForward showDepartmentPersonFunctions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException {
        DepartmentCreditsBean departmentCreditsBean = getRenderedObject();
        if (departmentCreditsBean == null) {
            departmentCreditsBean = new DepartmentCreditsBean();
            departmentCreditsBean.setAvailableDepartments(Department.readActiveDepartments());
        }
        request.setAttribute("departmentCreditsBean", departmentCreditsBean);
        request.setAttribute("canViewCredits", "true");
        return mapping.findForward("showDepartmentPersonFunctions");
    }

}
