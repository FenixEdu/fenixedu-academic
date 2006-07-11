package net.sourceforge.fenixedu.presentationTier.Action.research.result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultParticipationFullCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultParticipationSimpleCreationBean;
import net.sourceforge.fenixedu.domain.research.result.patent.ResultPatent;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.domain.research.result.publication.ResultPublication;
import net.sourceforge.fenixedu.domain.research.result.publication.ResultPublication.ResultPublicationType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ResultParticipationManagementAction extends FenixDispatchAction {

    public ActionForward prepareEditParticipation(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final Integer resultId = Integer.valueOf(request.getParameter("resultId"));
        
        //verify if field resultParticipationRole is to be active
        Result result = rootDomainObject.readResultByOID(resultId);
        
        if(result==null) {
            return mapping.findForward("editParticipation");    
        }
        request.setAttribute("result", result);
        
        if(result instanceof ResultPublication)
        {
            if(((ResultPublication)result).haveResultPublicationRole())
                request.setAttribute("participationsSchema","result.participationsWithRole");
        }
        
        return mapping.findForward("editParticipation");
    }
    
    public ActionForward changePersonsOrder(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        final Integer resultId = Integer.valueOf(request.getParameter("resultId"));
        final Integer participationId= Integer.valueOf(request.getParameter("oid"));
        final Integer offset = Integer.valueOf(request.getParameter("offset"));
        
        Object[] args = { resultId, participationId, offset };
        ServiceUtils.executeService(getUserView(request), "ChangeResultParticipationsOrder", args);

        return prepareEditParticipation(mapping,form,request,response);
    }

    public ActionForward removeParticipation(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        final Integer participationId = Integer.parseInt(request.getParameter("participationId"));
        final Integer resultId = Integer.parseInt(request.getParameter("resultId"));
        final String resultClassName = rootDomainObject.readResultByOID(resultId).getClass().getName();
        
        Object[] args = { participationId };
        ServiceUtils.executeService(getUserView(request), "DeleteResultParticipation", args);
      
        final Result result = rootDomainObject.readResultByOID(resultId);
        
        if (result == null) {
            request.setAttribute("resultClassName", resultClassName);
            return backToResult(mapping,form,request,response);
        }
        
        return prepareEditParticipation(mapping,form,request,response);
    }
    
    public ActionForward prepareEditParticipationWithSimpleBean(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ResultParticipationSimpleCreationBean simpleBean = new ResultParticipationSimpleCreationBean();
        request.setAttribute("simpleBean", simpleBean);
        mantainExternalStatus(request);
        
        //verify if field resultParticipationRole is to be active
        final Integer resultId = Integer.valueOf(request.getParameter("resultId"));
        Result result = rootDomainObject.readResultByOID(resultId);
        
        if(result instanceof ResultPublication)
        {
            if(((ResultPublication)result).haveResultPublicationRole())
            {
                final String external = request.getParameter("external");
                if(external.equals("false"))
                    request.setAttribute("schemaInternalPersonCreation","resultParticipation.internalPerson.creationWithRole");
                else
                    request.setAttribute("schemaExternalPersonSimpleCreation","resultParticipation.externalPerson.simpleCreationWithRole");
            }
        }

        return prepareEditParticipation(mapping,form,request,response);
    }
    
    public ActionForward prepareEditParticipationWithFullBean(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ResultParticipationFullCreationBean fullBean = new ResultParticipationFullCreationBean();
        request.setAttribute("fullBean", fullBean);
        mantainExternalStatus(request);
        
        //verify if field resultParticipationRole is to be active
        final Integer resultId = Integer.valueOf(request.getParameter("resultId"));
        Result result = rootDomainObject.readResultByOID(resultId);
        if(result instanceof ResultPublication)
        {
            if(((ResultPublication)result).haveResultPublicationRole())
            {
                request.setAttribute("schemaExternalPersonFullCreation","resultParticipation.externalPerson.fullCreationWithRole");
            }
        }
        
        return prepareEditParticipation(mapping,form,request,response);  
    }

    public ActionForward createParticipationInternalPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ResultParticipationSimpleCreationBean simpleBean = (ResultParticipationSimpleCreationBean) RenderUtils.getViewState().getMetaObject().getObject();
        
        if(simpleBean.getPerson() != null) {
            Integer oid = Integer.parseInt(request.getParameter("resultId"));
            ServiceUtils.executeService(getUserView(request), "CreateResultParticipation", new Object[] {simpleBean, oid });
            
            mantainExternalStatus(request);
            return prepareEditParticipation(mapping,form,request,response);
        }
        else {
            //The application should never reach this point: the user may be
            //creating an external person not on pourpose
            throw new RuntimeException ();
        }
    }
    
    public ActionForward createParticipationExternalPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final Integer oid = Integer.parseInt(request.getParameter("resultId"));
        
        if (RenderUtils.getViewState().getMetaObject().getObject() instanceof ResultParticipationSimpleCreationBean) {
            ResultParticipationSimpleCreationBean simpleBean = (ResultParticipationSimpleCreationBean) RenderUtils.getViewState().getMetaObject().getObject();
            
            if (simpleBean.getPerson() != null){

                ServiceUtils.executeService(getUserView(request), "CreateResultParticipation", new Object[] { simpleBean, oid });
            }
            else {
                ResultParticipationFullCreationBean fullBean = new ResultParticipationFullCreationBean();
                fullBean.setPersonName(simpleBean.getPersonName());
                fullBean.setResultParticipationRole(simpleBean.getResultParticipationRole());
                request.setAttribute("fullBean", fullBean);
                mantainExternalStatus(request);
                
                //verify if field resultParticipationRole is to be active
                final Integer resultId = Integer.valueOf(request.getParameter("resultId"));
                Result result = rootDomainObject.readResultByOID(resultId);
                if(result instanceof ResultPublication)
                {
                    if(((ResultPublication)result).haveResultPublicationRole())
                    {
                        request.setAttribute("schemaExternalPersonFullCreation","resultParticipation.externalPerson.fullCreationWithRole");
                    }
                }
                
                return prepareEditParticipation(mapping,form,request,response);                
            }
        }
        else if (RenderUtils.getViewState().getMetaObject().getObject() instanceof ResultParticipationFullCreationBean) {
            ResultParticipationFullCreationBean fullBean = (ResultParticipationFullCreationBean) RenderUtils.getViewState().getMetaObject().getObject();
            ServiceUtils.executeService(getUserView(request), "CreateResultParticipation", new Object[] { fullBean, oid });
        }
        return prepareEditParticipation(mapping,form,request,response);
    }
    
    public ActionForward backToResult(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        final Integer resultId = Integer.valueOf(request.getParameter("resultId"));
        final Result result = rootDomainObject.readResultByOID(resultId);
        
        if(result==null){
            final String resultClassName = request.getAttribute("resultClassName").toString();
            if (!(resultClassName == null || resultClassName.equals(""))) {
                if(resultClassName.compareTo(ResultPatent.class.getCanonicalName())==0){
                    return mapping.findForward("listPatents");
                }
                else {
                    return mapping.findForward("ListPublications");
                }
            }
        }
        
        if (result instanceof ResultPatent){
            request.setAttribute("patentId", resultId);
            return mapping.findForward("editPatent");    
        }
        else if (result instanceof ResultPublication){
            request.setAttribute("publicationId", resultId);
            return mapping.findForward("viewEditPublication");
        }

        return null;
    }
    
    private void mantainExternalStatus(HttpServletRequest request) {
        final String external = request.getParameter("external");
        if (external != null) {
            request.setAttribute("external", external);
        }
    }
}