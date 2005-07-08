package net.sourceforge.fenixedu.presentationTier.Action.manager.precedences;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author David Santos in Jul 28, 2004
 */

public class MakePrecedenceConjunctionForDegreeCurricularPlanDA extends FenixDispatchAction {

    public ActionForward showFirstPage(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);

        Integer degreeID = new Integer(request.getParameter("degreeId"));
        Integer degreeCurricularPlanID = new Integer(request.getParameter("degreeCurricularPlanId"));

        if (degreeID == null && degreeCurricularPlanID == null) {
            degreeID = (Integer) request.getAttribute("degreeId");
            degreeCurricularPlanID = (Integer) request.getAttribute("degreeCurricularPlanId");
        }

        Object args[] = { degreeCurricularPlanID };

        try {
            Map result = (Map) ServiceManagerServiceFactory.executeService(userView,
                    "ReadPrecedencesFromDegreeCurricularPlan", args);
            request.setAttribute("precedences", result);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("degreeId", degreeID);
        request.setAttribute("degreeCurricularPlanId", degreeCurricularPlanID);

        return mapping.findForward("showFirstPage");
    }

    public ActionForward showSecondPage(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {

        DynaActionForm mergePrecedencesForm = (DynaActionForm) form;
        IUserView userView = SessionUtils.getUserView(request);

        Integer degreeID = (Integer) mergePrecedencesForm.get("degreeId");
        Integer degreeCurricularPlanID = (Integer) mergePrecedencesForm.get("degreeCurricularPlanId");
        Integer firstPrecedenceID = (Integer) mergePrecedencesForm.get("firstPrecedence");

        Object args[] = { degreeCurricularPlanID };

        try {
            Map result = (Map) ServiceManagerServiceFactory.executeService(userView,
                    "ReadPrecedencesFromDegreeCurricularPlan", args);
            request.setAttribute("precedences", result);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        mergePrecedencesForm.set("firstPrecedence", firstPrecedenceID);
        request.setAttribute("degreeId", degreeID);
        request.setAttribute("degreeCurricularPlanId", degreeCurricularPlanID);

        return mapping.findForward("showSecondPage");
    }

    public ActionForward merge(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        ActionErrors errors = new ActionErrors();
        DynaActionForm mergePrecedencesForm = (DynaActionForm) form;
        IUserView userView = SessionUtils.getUserView(request);

        Integer degreeID = (Integer) mergePrecedencesForm.get("degreeId");
        Integer degreeCurricularPlanID = (Integer) mergePrecedencesForm.get("degreeCurricularPlanId");
        Integer firstPrecedenceID = (Integer) mergePrecedencesForm.get("firstPrecedence");
        Integer secondPrecedenceID = (Integer) mergePrecedencesForm.get("secondPrecedence");

        Object args1[] = { firstPrecedenceID, secondPrecedenceID };
        Object args2[] = { secondPrecedenceID };
        Object args3[] = { degreeCurricularPlanID };

        try {
            ServiceManagerServiceFactory.executeService(userView,
                    "MergePrecedencesForDegreeCurricularPlan", args1);
            ServiceManagerServiceFactory.executeService(userView,
                    "DeletePrecedenceFromDegreeCurricularPlan", args2);
            Map result = (Map) ServiceManagerServiceFactory.executeService(userView,
                    "ReadPrecedencesFromDegreeCurricularPlan", args3);
            request.setAttribute("precedences", result);
        } catch (InvalidArgumentsServiceException e) {
            errors.add(e.getMessage(), new ActionError(e.getMessage()));
            saveErrors(request, errors);
            return mapping.getInputForward();
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("degreeId", degreeID);
        request.setAttribute("degreeCurricularPlanId", degreeCurricularPlanID);

        return mapping.findForward("backToBeginig");
    }
}