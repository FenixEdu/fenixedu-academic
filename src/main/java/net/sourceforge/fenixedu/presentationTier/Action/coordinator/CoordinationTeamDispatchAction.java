package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator.CoordinatedDegreeInfo;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * 
 * @author João Mota
 * 
 */
public class CoordinationTeamDispatchAction extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        CoordinatedDegreeInfo.setCoordinatorContext(request);
        return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward chooseExecutionYear(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException, FenixFilterException {

        IUserView userView = UserView.getUser();

        Integer degreeCurricularPlanID = null;
        if (request.getParameter("degreeCurricularPlanID") != null) {
            degreeCurricularPlanID = new Integer(request.getParameter("degreeCurricularPlanID"));
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        }

        Object[] args = { degreeCurricularPlanID };
        List executionDegrees = (List) ServiceManagerServiceFactory.executeService("ReadExecutionDegreesByDegreeCurricularPlanID", args);

        request.setAttribute("executionDegrees", executionDegrees);

        return mapping.findForward("chooseExecutionYear");
    }

    public ActionForward viewTeam(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException, FenixServiceException, FenixFilterException {

        IUserView userView = getUserView(request);

        Integer degreeCurricularPlanID = null;
        if (request.getParameter("degreeCurricularPlanID") != null) {
            degreeCurricularPlanID = new Integer(request.getParameter("degreeCurricularPlanID"));
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        }

        Integer executionDegreeID = new Integer(request.getParameter("infoExecutionDegreeId"));
        request.setAttribute("infoExecutionDegreeId", executionDegreeID);

        ActionErrors actionErrors = new ActionErrors();
        Object[] args = { executionDegreeID };
        List coordinators = new ArrayList();
        try {
            coordinators = (List) ServiceManagerServiceFactory.executeService("ReadCoordinationTeam", args);
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
        Object[] args1 = { executionDegreeID, userView };
        try {
            result = (Boolean) ServiceManagerServiceFactory.executeService("ReadCoordinationResponsibility", args1);
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
            HttpServletResponse response) throws FenixActionException, FenixFilterException {
        IUserView userView = getUserView(request);

        Integer degreeCurricularPlanID = new Integer(Integer.parseInt(request.getParameter("degreeCurricularPlanID")));
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);

        String infoExecutionDegreeIdString = request.getParameter("infoExecutionDegreeId");
        Integer infoExecutionDegreeId = new Integer(infoExecutionDegreeIdString);
        request.setAttribute("infoExecutionDegreeId", infoExecutionDegreeId);
        Boolean result = new Boolean(false);
        Object[] args1 = { infoExecutionDegreeId, userView };
        try {
            result = (Boolean) ServiceManagerServiceFactory.executeService("ReadCoordinationResponsibility", args1);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("isResponsible", result);

        return mapping.findForward("addCoordinator");

    }

    public ActionForward AddCoordinator(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {
        IUserView userView = getUserView(request);
        DynaActionForm teacherForm = (DynaActionForm) form;
        String istUsername = new String((String) teacherForm.get("newCoordinatorIstUsername"));
        String infoExecutionDegreeIdString = request.getParameter("infoExecutionDegreeId");
        Integer infoExecutionDegreeId = new Integer(infoExecutionDegreeIdString);
        request.setAttribute("infoExecutionDegreeId", infoExecutionDegreeId);
        Object[] args = { infoExecutionDegreeId, istUsername };
        try {
            ServiceManagerServiceFactory.executeService("AddCoordinator", args);
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
                actionErrors.add("unknownTeacher", new ActionError(e.getMessage(), istUsername));
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
            HttpServletResponse response) throws FenixActionException, FenixServiceException, FenixFilterException {
        IUserView userView = getUserView(request);
        DynaActionForm removeCoordinatorsForm = (DynaActionForm) form;
        Integer[] coordinatorsIds = (Integer[]) removeCoordinatorsForm.get("coordinatorsIds");
        List coordinators = Arrays.asList(coordinatorsIds);

        String infoExecutionDegreeIdString = request.getParameter("infoExecutionDegreeId");
        Integer infoExecutionDegreeId = new Integer(infoExecutionDegreeIdString);
        request.setAttribute("infoExecutionDegreeId", infoExecutionDegreeId);
        Object[] args = { infoExecutionDegreeId, coordinators };
        try {
            ServiceManagerServiceFactory.executeService("RemoveCoordinators", args);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return viewTeam(mapping, form, request, response);
    }

}