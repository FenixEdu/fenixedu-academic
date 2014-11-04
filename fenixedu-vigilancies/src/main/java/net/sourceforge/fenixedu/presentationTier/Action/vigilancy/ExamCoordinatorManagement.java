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
package org.fenixedu.academic.ui.struts.action.vigilancy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.person.vigilancy.AddExamCoordinator;
import org.fenixedu.academic.service.services.person.vigilancy.DeleteExamCoordinator;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.vigilancy.ExamCoordinator;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.departmentAdmOffice.DepartmentAdmOfficeExamsApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = DepartmentAdmOfficeExamsApp.class, path = "manage-coordinators",
        titleKey = "label.vigilancy.manageExamCoordinator")
@Mapping(module = "departmentAdmOffice", path = "/vigilancy/examCoordinatorManagement")
@Forwards({ @Forward(name = "prepareExamCoordinator", path = "/departmentAdmOffice/vigilancy/manageExamCoordinator.jsp"),
        @Forward(name = "editExamCoordinator", path = "/departmentAdmOffice/vigilancy/editExamCoordinator.jsp") })
public class ExamCoordinatorManagement extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepareExamCoordinator(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IViewState viewState = RenderUtils.getViewState("preserveState");
        if (viewState == null) {
            prepareManagementBean(request);
        } else {
            VigilantGroupBean bean = (VigilantGroupBean) viewState.getMetaObject().getObject();
            request.setAttribute("bean", bean);
        }
        return mapping.findForward("prepareExamCoordinator");
    }

    public ActionForward addExamCoordinator(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IViewState viewState = RenderUtils.getViewState("preserveState");
        VigilantGroupBean bean = (VigilantGroupBean) viewState.getMetaObject().getObject();
        request.setAttribute("bean", bean);

        String username = bean.getUsername();
        User user = User.findByUsername(username);
        if (user != null && user.getPerson() != null) {

            AddExamCoordinator.run(user.getPerson(), bean.getExecutionYear(), bean.getSelectedUnit());
        } else {
            addActionMessage(request, "label.vigilancy.inexistingUsername");
        }

        return mapping.findForward("prepareExamCoordinator");
    }

    public ActionForward deleteExamCoordinator(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String oid = request.getParameter("oid");
        String departmentId = request.getParameter("deparmentId");
        String unitId = request.getParameter("unitId");

        ExamCoordinator coordinator = (ExamCoordinator) FenixFramework.getDomainObject(oid);

        DeleteExamCoordinator.run(coordinator);

        Department deparment = (Department) FenixFramework.getDomainObject(departmentId);
        Unit unit = (Unit) FenixFramework.getDomainObject(unitId);

        VigilantGroupBean bean = new VigilantGroupBean();
        bean.setSelectedDepartment(deparment);
        bean.setSelectedUnit(unit);
        bean.setExecutionYear(ExecutionYear.readCurrentExecutionYear());

        request.setAttribute("bean", bean);
        return mapping.findForward("prepareExamCoordinator");
    }

    public ActionForward prepareAddExamCoordinatorWithState(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String departmentId = request.getParameter("deparmentId");
        String unitId = request.getParameter("unitId");

        Department deparment = FenixFramework.getDomainObject(departmentId);
        Unit unit = FenixFramework.getDomainObject(unitId);

        VigilantGroupBean bean = new VigilantGroupBean();
        bean.setSelectedDepartment(deparment);
        bean.setSelectedUnit(unit);
        bean.setExecutionYear(ExecutionYear.readCurrentExecutionYear());

        request.setAttribute("bean", bean);
        return mapping.findForward("prepareExamCoordinator");
    }

    public ActionForward editExamCoordinators(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String departmentId = request.getParameter("deparmentId");
        String unitId = request.getParameter("unitId");

        Department deparment = (Department) FenixFramework.getDomainObject(departmentId);
        Unit unit = (Unit) FenixFramework.getDomainObject(unitId);

        VigilantGroupBean bean = new VigilantGroupBean();
        bean.setSelectedDepartment(deparment);
        bean.setSelectedUnit(unit);
        ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
        bean.setExecutionYear(executionYear);
        bean.setExamCoordinators(ExamCoordinator.getExamCoordinatorsForGivenYear(unit, executionYear));
        request.setAttribute("bean", bean);
        return mapping.findForward("editExamCoordinator");
    }

    public ActionForward selectUnitForCoordinator(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        VigilantGroupBean bean = (VigilantGroupBean) RenderUtils.getViewState("selectUnit").getMetaObject().getObject();
        request.setAttribute("bean", bean);
        RenderUtils.invalidateViewState("selectUnit");
        return mapping.findForward("prepareExamCoordinator");
    }

    private void prepareManagementBean(HttpServletRequest request) throws FenixServiceException {

        VigilantGroupBean bean = new VigilantGroupBean();
        ExecutionYear currentYear = ExecutionYear.readCurrentExecutionYear();
        bean.setExecutionYear(currentYear);
        Person person = getLoggedPerson(request);
        if (person.getTeacher() != null) {
            bean.setSelectedDepartment(person.getTeacher().getDepartment());
        } else if (person.getEmployee() != null) {
            bean.setSelectedDepartment(person.getEmployee().getCurrentDepartmentWorkingPlace());
        }
        request.setAttribute("bean", bean);
    }

}