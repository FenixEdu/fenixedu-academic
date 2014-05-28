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
package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.coordinator.AddCoordinator;
import net.sourceforge.fenixedu.applicationTier.Servico.coordinator.ReadCoordinationResponsibility;
import net.sourceforge.fenixedu.applicationTier.Servico.coordinator.ReadCoordinationTeam;
import net.sourceforge.fenixedu.applicationTier.Servico.coordinator.RemoveCoordinators;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.commons.ReadExecutionDegreesByDegreeCurricularPlanID;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.core.domain.User;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

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
        String istUsername = new String((String) teacherForm.get("newCoordinatorIstUsername"));
        String infoExecutionDegreeIdString = request.getParameter("infoExecutionDegreeId");
        request.setAttribute("infoExecutionDegreeId", infoExecutionDegreeIdString);
        try {
            AddCoordinator.runAddCoordinator(infoExecutionDegreeIdString, istUsername);
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
            if (e.getMessage().equals("error.noEmployeeForIstUsername")) {
                actionErrors.add("unknownTeacher", new ActionError(e.getMessage(), istUsername, Unit.getInstitutionAcronym()));
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