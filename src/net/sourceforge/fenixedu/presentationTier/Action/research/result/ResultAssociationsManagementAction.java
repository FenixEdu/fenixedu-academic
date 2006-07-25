package net.sourceforge.fenixedu.presentationTier.Action.research.result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultEventAssociationFullCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultEventAssociationSimpleCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultUnitAssociationCreateBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.domain.research.result.ResultEventAssociation;
import net.sourceforge.fenixedu.domain.research.result.patent.ResultPatent;
import net.sourceforge.fenixedu.domain.research.result.publication.ResultPublication;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ResultAssociationsManagementAction extends FenixDispatchAction {
    
    /**
     *  Actions for Result Unit Associations 
     */
    public ActionForward prepareEditUnitAssociations(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        final Result result = readResultFromRequest(request);
        if(result==null){
            return backToResultList(mapping, form, request, response);
        }
        
        final ResultUnitAssociationCreateBean bean = new ResultUnitAssociationCreateBean();
        
        request.setAttribute("bean", bean);
        return mapping.findForward("editUnitAssociations");
    }
    
    public ActionForward createUnitAssociation(ActionMapping mapping,ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final ResultUnitAssociationCreateBean bean = (ResultUnitAssociationCreateBean)RenderUtils.getViewState().getMetaObject().getObject();
        final Integer oid = Integer.parseInt(request.getParameter("resultId"));
        final Object[] args = { bean, oid, getUserView(request).getPerson().getName() };
        
        try {
            ServiceUtils.executeService(getUserView(request), "CreateResultUnitAssociation", args);
        }
        catch (InvalidArgumentsServiceException e) {
            addActionMessage(request, "error.Result.not.found");
        }
        catch (FenixServiceException e) {
            addActionMessage(request, e.getMessage());
        }
        catch (DomainException e) {
            addActionMessage(request, e.getKey());
        }
        
        return prepareEditUnitAssociations(mapping, form, request, response);
    }

    public ActionForward removeUnitAssociation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Integer associationId = Integer.parseInt(request.getParameter("associationId"));
        final Object[] args = { associationId, getUserView(request).getPerson().getName() };

        try {
            ServiceUtils.executeService(getUserView(request), "DeleteResultUnitAssociation", args);    
        }
        catch (InvalidArgumentsServiceException e) {
            addActionMessage(request, "error.ResultUnitAssociation.not.found");
        }

        return prepareEditUnitAssociations(mapping, form, request, response);
    }
    
    /**
     *  Actions for Result Event Associations 
     */
    public ActionForward prepareEditEventAssociations(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final Result result = readResultFromRequest(request);
        if(result==null){
            return backToResultList(mapping, form, request, response);
        }
        
        final ResultEventAssociationSimpleCreationBean simpleBean = new ResultEventAssociationSimpleCreationBean();
        
        if (request.getAttribute("fullBean") == null) {
            request.setAttribute("simpleBean", simpleBean);
        }
        return mapping.findForward("editEventAssociations");
    }
    
    public ActionForward createEventAssociationSimple(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final IUserView userView = getUserView(request);
        final Object renderedObject = RenderUtils.getViewState().getMetaObject().getObject();
        
        if(renderedObject instanceof ResultEventAssociationSimpleCreationBean){
            final ResultEventAssociationSimpleCreationBean simpleBean = (ResultEventAssociationSimpleCreationBean) renderedObject;
            
            if(simpleBean.getEvent() != null) {
                final Integer oid = Integer.parseInt(request.getParameter("resultId"));
                final Object[] args = {simpleBean, oid, userView.getPerson().getName() };
                
                try {
                    ServiceUtils.executeService(userView, "CreateResultEventAssociation", args);    
                }
                catch (InvalidArgumentsServiceException e) {
                    addActionMessage(request, "error.Result.not.found");
                }
                catch (FenixServiceException e) {
                    addActionMessage(request, e.getMessage());
                }
                catch (DomainException e) {
                    addActionMessage(request, e.getKey(),e.getArgs());
                }
            }
            else {
                ResultEventAssociationFullCreationBean fullBean = new ResultEventAssociationFullCreationBean();
                fullBean.setEventName(new MultiLanguageString(simpleBean.getEventName()));
                fullBean.setRole(simpleBean.getRole());
                
                request.setAttribute("fullBean", fullBean);    
            }
        }
        else {
            request.setAttribute("fullBean", (ResultEventAssociationFullCreationBean) renderedObject);
        }   
        return prepareEditEventAssociations(mapping, form, request, response);
    }
    
    public ActionForward createEventAssociationFull(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final ResultEventAssociationFullCreationBean fullBean = (ResultEventAssociationFullCreationBean) RenderUtils.getViewState().getMetaObject().getObject();
        final IUserView userView = getUserView(request);
        final Integer oid = Integer.parseInt(request.getParameter("resultId"));
        final Object[] args = { fullBean, oid, userView.getPerson().getName() };
        
        try {
            ServiceUtils.executeService(userView, "CreateResultEventAssociation", args);    
        }
        catch (InvalidArgumentsServiceException e) {
            addActionMessage(request, "error.Result.not.found");
        }
        catch (FenixServiceException e) {
            addActionMessage(request, e.getMessage());
        }
        catch (DomainException e) {
            addActionMessage(request, e.getKey());
        }
        
        return prepareEditEventAssociations(mapping, form, request, response);
    }       
    
    public ActionForward removeEventAssociation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Integer associationId = Integer.parseInt(request.getParameter("associationId"));
        final IUserView userView = getUserView(request);
        final Object[] args = { associationId, userView.getPerson().getName() };
        
        try {
            ServiceUtils.executeService(userView, "DeleteResultEventAssociation", args);
        }
        catch (InvalidArgumentsServiceException e) {
            addActionMessage(request, "error.ResultEventAssociation.not.found");
        }
        
        return prepareEditEventAssociations(mapping, form, request, response);
    }
    
    /**
     * Common actions and methods 
     */
    public ActionForward backToResult(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final Result result = readResultFromRequest(request);
        if(result==null){
            return backToResultList(mapping, form, request, response);
        }
        
        String forwardTo = null;
        
        if (result instanceof ResultPatent){
            request.setAttribute("patentId", result.getIdInternal());
            forwardTo = new String("editPatent");    
        }
        else if (result instanceof ResultPublication){
            request.setAttribute("publicationId", result.getIdInternal());
            forwardTo = new String("viewEditPublication");
        }
        
        return mapping.findForward(forwardTo);
    }
    
    private ActionForward backToResultList(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final String resultType = request.getParameterMap().containsKey("resultType") ?
                request.getParameter("resultType") : (String) request.getAttribute("resultType");
        String forwardTo = null;
        
        if (!(resultType == null || resultType.equals(""))) {
            if(resultType.compareTo(ResultPatent.class.getSimpleName())==0){
                forwardTo = new String("listPatents");
            }
            else {
                forwardTo = new String("ListPublications");
            }
        }
        return mapping.findForward(forwardTo);
    }
    
    private Result readResultFromRequest(HttpServletRequest request) throws Exception {
        final Integer resultId = Integer.valueOf(request.getParameter("resultId"));
        final Result result = rootDomainObject.readResultByOID(resultId);
        
        if (result == null) {
            addActionMessage(request, "error.Result.not.found");
        }
        else {
            request.setAttribute("result", result);
        }
        return result;
    }
}
