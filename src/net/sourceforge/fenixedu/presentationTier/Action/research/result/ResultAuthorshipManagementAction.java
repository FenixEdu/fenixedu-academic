package net.sourceforge.fenixedu.presentationTier.Action.research.result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultAuthorshipFullCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultAuthorshipSimpleCreationBean;
import net.sourceforge.fenixedu.domain.research.result.Patent;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ResultAuthorshipManagementAction extends FenixDispatchAction {

    public ActionForward prepareEditAuthorship(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final Integer resultId = Integer.valueOf(request.getParameter("resultId"));
        
        Result result = rootDomainObject.readResultByOID(resultId);
        request.setAttribute("result", result);
        
        return mapping.findForward("editAuthorship");
    }
    
    public ActionForward changeAuthorsOrder(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        final Integer resultId = Integer.valueOf(request.getParameter("resultId"));
        final Integer authorshipId= Integer.valueOf(request.getParameter("oid"));
        final Integer offset = Integer.valueOf(request.getParameter("offset"));
        
        Object[] args = { resultId, authorshipId, offset };
        ServiceUtils.executeService(getUserView(request), "ChangeResultAuthorshipsOrder", args);

        return prepareEditAuthorship(mapping,form,request,response);
    }

    public ActionForward removeAuthorship(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        final Integer resultId = Integer.valueOf(request.getParameter("resultId"));
        final String[] authorIdsToRemove = request.getParameterValues("authorIds");
        
        Object[] args = { resultId, authorIdsToRemove };
        ServiceUtils.executeService(getUserView(request), "DeleteResultAuthorships", args);
      
        return prepareEditAuthorship(mapping,form,request,response);
    }
    
    public ActionForward prepareEditAuthorshipWithSimpleBean(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ResultAuthorshipSimpleCreationBean simpleBean = new ResultAuthorshipSimpleCreationBean();
        request.setAttribute("simpleBean", simpleBean);
        
        mantainExternalStatus(request);
        
        return prepareEditAuthorship(mapping,form,request,response);
    }
    
    public ActionForward prepareEditAuthorshipWithFullBean(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ResultAuthorshipFullCreationBean fullBean = new ResultAuthorshipFullCreationBean();
        request.setAttribute("fullBean", fullBean);
        
        mantainExternalStatus(request);
        
        return prepareEditAuthorship(mapping,form,request,response);  
    }

    public ActionForward createAuthorshipInternalPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ResultAuthorshipSimpleCreationBean simpleBean = (ResultAuthorshipSimpleCreationBean) RenderUtils.getViewState().getMetaObject().getObject();
        
        if(simpleBean.getPerson() != null) {
            Integer oid = Integer.parseInt(request.getParameter("resultId"));
            ServiceUtils.executeService(getUserView(request), "CreateResultAuthorship", new Object[] {simpleBean, oid });
            
            mantainExternalStatus(request);
            return prepareEditAuthorship(mapping,form,request,response);
        }
        else {
            //The application should never reach this point: the user may be
            //creating an external person not on pourpose
            throw new RuntimeException ();
        }
    }
    
    public ActionForward createAuthorshipExternalPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final Integer oid = Integer.parseInt(request.getParameter("resultId"));
        
        if (RenderUtils.getViewState().getMetaObject().getObject() instanceof ResultAuthorshipSimpleCreationBean) {
            ResultAuthorshipSimpleCreationBean simpleBean = (ResultAuthorshipSimpleCreationBean) RenderUtils.getViewState().getMetaObject().getObject();
            
            if (simpleBean.getPerson() != null){

                ServiceUtils.executeService(getUserView(request), "CreateResultAuthorship", new Object[] { simpleBean, oid });
            }
            else {
                ResultAuthorshipFullCreationBean fullBean = new ResultAuthorshipFullCreationBean();
                fullBean.setPersonName(simpleBean.getPersonName());
                request.setAttribute("fullBean", fullBean);
                
                mantainExternalStatus(request);
                
                return prepareEditAuthorship(mapping,form,request,response);                
            }
        }
        else if (RenderUtils.getViewState().getMetaObject().getObject() instanceof ResultAuthorshipFullCreationBean) {
            ResultAuthorshipFullCreationBean fullBean = (ResultAuthorshipFullCreationBean) RenderUtils.getViewState().getMetaObject().getObject();
            ServiceUtils.executeService(getUserView(request), "CreateResultAuthorship", new Object[] { fullBean, oid });
        }
        return prepareEditAuthorship(mapping,form,request,response);
    }
    
    public ActionForward backToResult(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        final Integer resultId = Integer.valueOf(request.getParameter("resultId"));
        Result result = rootDomainObject.readResultByOID(resultId);
        
        if (result instanceof Patent){
            request.setAttribute("patentId", resultId);
            return mapping.findForward("editPatent");    
        }
        else {
            //request.setAttribute("publicationId", resultId);
            //return mapping.findForward("editPublication");
            return null;
        }
    }
    
    private void mantainExternalStatus(HttpServletRequest request) {
        final String external = request.getParameter("external");
        if (external != null) {
            request.setAttribute("external", external);
        }
    }
}