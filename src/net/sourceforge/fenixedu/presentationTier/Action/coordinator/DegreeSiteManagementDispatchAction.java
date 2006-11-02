package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeInfo;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.presentationTier.Action.utils.RequestUtils;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Tânia Pousão Created on 31/Out/2003
 */
public class DegreeSiteManagementDispatchAction extends FenixDispatchAction {

    public ActionForward subMenu(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        Integer degreeCurricularPlanId = Integer.valueOf(request.getParameter("degreeCurricularPlanID"));
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);
        return mapping.findForward("degreeSiteMenu");
    }

    public ActionForward viewInformation(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
        RequestUtils.getAndSetStringToRequest(request, "info");
        
        Integer degreeCurricularPlanID = Integer.valueOf(RequestUtils.getAndSetStringToRequest(request, "degreeCurricularPlanID"));
        DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanID);
        
        ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
        DegreeInfo currentDegreeInfo = currentExecutionYear.getDegreeInfo(degreeCurricularPlan.getDegree());
        
        if (currentDegreeInfo == null) {
            final IUserView userView = SessionUtils.getUserView(request);
            
            if (!userView.getPerson().isCoordinatorFor(degreeCurricularPlan, currentExecutionYear)
                    && !userView.getPerson().isCoordinatorFor(degreeCurricularPlan, currentExecutionYear.getNextExecutionYear())) {
                final ActionErrors errors = new ActionErrors();
                errors.add("notAuthorized", new ActionError("error.exception.notAuthorized2"));
                saveErrors(request, errors);
                return new ActionForward(mapping.getInput());
            }

            final Object[] args = { degreeCurricularPlan.getDegree() };
            currentDegreeInfo = (DegreeInfo) ServiceManagerServiceFactory.executeService(userView, "CreateCurrentDegreeInfo", args);
        }
        
        request.setAttribute("currentDegreeInfo", currentDegreeInfo);
        
        return mapping.findForward("viewInformation");
    }

    public ActionForward editDegreeInformation(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        RequestUtils.getAndSetStringToRequest(request, "info");
        RequestUtils.getAndSetStringToRequest(request, "degreeCurricularPlanID");

        return mapping.findForward("editOK");
    }

    public ActionForward viewDescriptionCurricularPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        Integer degreeCurricularPlanID = Integer.valueOf(RequestUtils.getAndSetStringToRequest(request, "degreeCurricularPlanID"));
        DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanID);
        if (degreeCurricularPlan == null) {
            final ActionErrors errors = new ActionErrors();
            errors.add("noDegreeCurricularPlan", new ActionError("error.coordinator.chosenDegree"));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        request.setAttribute("degreeCurricularPlan", degreeCurricularPlan);
        
        return mapping.findForward("viewDescriptionCurricularPlan");
    }

    public ActionForward editDescriptionDegreeCurricularPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        RequestUtils.getAndSetStringToRequest(request, "degreeCurricularPlanID");
        return mapping.findForward("editOK");
    }

    public ActionForward viewHistoric(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FenixFilterException {
        // read execution degree
        Integer degreeCurricularPlanID = Integer.valueOf(RequestUtils.getAndSetStringToRequest(request, "degreeCurricularPlanID"));
        DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanID);
        
        if (degreeCurricularPlan.hasAnyExecutionDegrees()) {
            request.setAttribute("executionDegrees", SessionUtils.getUserView(request).getPerson().getCoordinatedExecutionDegrees(degreeCurricularPlan));    
        }

        return mapping.findForward("viewHistoric");
    }

}
