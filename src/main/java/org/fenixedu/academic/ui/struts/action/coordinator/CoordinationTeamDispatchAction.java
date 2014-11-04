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
package org.fenixedu.academic.ui.struts.action.coordinator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.dto.InfoExecutionDegree;
import org.fenixedu.academic.service.services.coordinator.AddCoordinator;
import org.fenixedu.academic.service.services.coordinator.ReadCoordinationResponsibility;
import org.fenixedu.academic.service.services.coordinator.ReadCoordinationTeam;
import org.fenixedu.academic.service.services.coordinator.RemoveCoordinators;
import org.fenixedu.academic.service.services.exceptions.ExistingServiceException;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.InvalidArgumentsServiceException;
import org.fenixedu.academic.service.services.exceptions.NonExistingServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.service.services.masterDegree.administrativeOffice.commons.ReadExecutionDegreesByDegreeCurricularPlanID;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

/**
 *
 * @author João Mota
 *
 */
@Mapping(path = "/viewCoordinationTeam", module = "coordinator", formBean = "addCoordinator",
        functionality = DegreeCoordinatorIndex.class)
@Forwards({ @Forward(name = "chooseExecutionYear", path = "/coordinator/coordinationTeam/chooseExecutionYear.jsp"),
        @Forward(name = "coordinationTeam", path = "/coordinator/coordinationTeam/viewCoordinationTeam.jsp"),
        @Forward(name = "addCoordinator", path = "/coordinator/coordinationTeam/addCoordinator.jsp"),
        @Forward(name = "sucess", path = "/coordinator/viewCoordinationTeam.do?method=viewTeam"),
        @Forward(name = "noAuthorization", path = "/coordinator/student/notAuthorized_bd.jsp") })
public class CoordinationTeamDispatchAction extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DegreeCoordinatorIndex.setCoordinatorContext(request);
        return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward chooseExecutionYear(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {

        String degreeCurricularPlanID = null;
        if (request.getParameter("degreeCurricularPlanID") != null) {
            degreeCurricularPlanID = request.getParameter("degreeCurricularPlanID");
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        }

        List<InfoExecutionDegree> executionDegrees =
                ReadExecutionDegreesByDegreeCurricularPlanID
                        .runReadExecutionDegreesByDegreeCurricularPlanID(degreeCurricularPlanID);

        request.setAttribute("executionDegrees", executionDegrees);

        return mapping.findForward("chooseExecutionYear");
    }

    public ActionForward viewTeam(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException, FenixServiceException {

        User userView = getUserView(request);

        String degreeCurricularPlanID = null;
        if (request.getParameter("degreeCurricularPlanID") != null) {
            degreeCurricularPlanID = request.getParameter("degreeCurricularPlanID");
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        }

        String executionDegreeID = request.getParameter("infoExecutionDegreeId");
        request.setAttribute("infoExecutionDegreeId", executionDegreeID);

        ActionErrors actionErrors = new ActionErrors();
        Object[] args = { executionDegreeID };
        List coordinators = new ArrayList();
        try {
            coordinators = ReadCoordinationTeam.runReadCoordinationTeam(executionDegreeID);
        } catch (NotAuthorizedException e) {
            actionErrors.add("error", new ActionError("noAuthorization"));
            saveErrors(request, actionErrors);
            return mapping.findForward("noAuthorization");
        } catch (FenixServiceException e) {
            actionErrors.add("error", new ActionError(e.getMessage()));
            saveErrors(request, actionErrors);
            return mapping.findForward("noAuthorization");
        }
        Boolean result = Boolean.FALSE;
        try {
            result = ReadCoordinationResponsibility.runReadCoordinationResponsibility(executionDegreeID, userView);
        } catch (FenixServiceException e) {
            actionErrors.add("error", new ActionError(e.getMessage()));
            saveErrors(request, actionErrors);
            return mapping.findForward("noAuthorization");
        }

        request.setAttribute("isResponsible", result);
        request.setAttribute("coordinators", coordinators);
        return mapping.findForward("coordinationTeam");
    }

    public ActionForward prepareAddCoordinator(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        User userView = getUserView(request);

        String degreeCurricularPlanID = request.getParameter("degreeCurricularPlanID");
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);

        String infoExecutionDegreeIdString = request.getParameter("infoExecutionDegreeId");
        request.setAttribute("infoExecutionDegreeId", infoExecutionDegreeIdString);
        Boolean result = new Boolean(false);
        try {
            result = ReadCoordinationResponsibility.runReadCoordinationResponsibility(infoExecutionDegreeIdString, userView);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("isResponsible", result);

        return mapping.findForward("addCoordinator");

    }

    public ActionForward AddCoordinator(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        User userView = getUserView(request);
        DynaActionForm teacherForm = (DynaActionForm) form;
        String username = new String((String) teacherForm.get("newCoordinatorUsername"));
        String infoExecutionDegreeIdString = request.getParameter("infoExecutionDegreeId");
        request.setAttribute("infoExecutionDegreeId", infoExecutionDegreeIdString);
        try {
            AddCoordinator.runAddCoordinator(infoExecutionDegreeIdString, username);
        } catch (NonExistingServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("unknownTeacher", new ActionError("error.nonExistingTeacher"));
            saveErrors(request, actionErrors);
            return prepareAddCoordinator(mapping, form, request, response);
        } catch (InvalidArgumentsServiceException e) {
            throw new FenixActionException(e);
        } catch (ExistingServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("existingCoordinator", new ActionError("error.existingTeacher"));
            saveErrors(request, actionErrors);
            return prepareAddCoordinator(mapping, form, request, response);
        } catch (FenixServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            if (e.getMessage().equals("error.noUserForUsername")) {
                actionErrors.add("unknownTeacher", new ActionError(e.getMessage(), username, Unit.getInstitutionAcronym()));
            } else {
                actionErrors.add("unknownTeacher", new ActionError(e.getMessage()));
            }
            saveErrors(request, actionErrors);
            return prepareAddCoordinator(mapping, form, request, response);
            // throw new FenixActionException(e);
        }
        return mapping.findForward("sucess");
    }

    public ActionForward removeCoordinators(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {
        DynaActionForm removeCoordinatorsForm = (DynaActionForm) form;
        String[] coordinatorsIds = (String[]) removeCoordinatorsForm.get("coordinatorsIds");
        List<String> coordinators = Arrays.asList(coordinatorsIds);

        String infoExecutionDegreeIdString = request.getParameter("infoExecutionDegreeId");
        request.setAttribute("infoExecutionDegreeId", infoExecutionDegreeIdString);
        try {
            RemoveCoordinators.runRemoveCoordinators(infoExecutionDegreeIdString, coordinators);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return viewTeam(mapping, form, request, response);
    }

}