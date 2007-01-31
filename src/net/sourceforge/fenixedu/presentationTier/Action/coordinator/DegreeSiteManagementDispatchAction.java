package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeInfo;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.manager.SiteManagementDA;
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
public class DegreeSiteManagementDispatchAction extends SiteManagementDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
        if (degreeCurricularPlan != null) {
            request.setAttribute("degreeCurricularPlan", degreeCurricularPlan);
        }
        
        return super.execute(mapping, actionForm, request, response);
    }

    @Override
    protected Site getSite(HttpServletRequest request) {
        DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
        if (degreeCurricularPlan != null) {
            return degreeCurricularPlan.getDegree().getSite();
        }
        else {
            return null;
        }
    }
    
    public DegreeCurricularPlan getDegreeCurricularPlan(HttpServletRequest request) {
        String parameter = request.getParameter("degreeCurricularPlanID");
        
        if (parameter == null) {
            return null;
        }
        
        try {
            Integer oid = new Integer(parameter);
            return RootDomainObject.getInstance().readDegreeCurricularPlanByOID(oid);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    public Unit getUnit(HttpServletRequest request) {
        DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
        if (degreeCurricularPlan == null) {
            return null;
        }
        else {
            return degreeCurricularPlan.getDegree().getUnit();
        }
    }
    
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

    @Override
    protected String getAuthorNameForFile(HttpServletRequest request, Item item) {
        Unit unit = getUnit(request);
        if (unit == null) {
            return null;
        }
        else {
            return unit.getName();
        }
    }

    @Override
    protected String getItemLocationForFile(HttpServletRequest request, Item item, Section section) {
        return getDirectLinkContext(request);
    }

    @Override
    protected String getDirectLinkContext(HttpServletRequest request) {
        DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
        
        if (degreeCurricularPlan == null) {
            return null;
        }
        
        Degree degree = degreeCurricularPlan.getDegree();
        
        if (degree == null) {
            return null;
        }

        if (degree.getSigla() == null) {
            return null;
        }
        
        try {
            return org.apache.struts.util.RequestUtils.absoluteURL(request, "/" + degree.getSigla().toLowerCase()).toString();
        } catch (MalformedURLException e) {
            return null;
        }
    }

}
