package net.sourceforge.fenixedu.presentationTier.Action.research.result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultParticipationFullCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultParticipationSimpleCreationBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.domain.research.result.patent.ResultPatent;
import net.sourceforge.fenixedu.domain.research.result.publication.ResultPublication;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ResultParticipationManagementAction extends FenixDispatchAction {

    public ActionForward prepareEditParticipation(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final Result result = readResultFromRequest(request);
        if(result==null){
            return backToResultList(mapping, form, request, response);
        }
        
        if(result instanceof ResultPublication)
        {
            if(((ResultPublication)result).haveResultPublicationRole())
                request.setAttribute("participationsSchema","result.participationsWithRole");
        }
        
        final Person person = getUserView(request).getPerson();
        
        if(!result.hasPersonParticipation(person)) {
            addActionMessage(request, "researcher.result.editResult.participation.warning");
        }
        if(result.getResultParticipationsCount()==1){
            addActionMessage(request, "researcher.result.lastConnection.warning");
        }
        
        return mapping.findForward("editParticipation");
    }
    
    public ActionForward changePersonsOrder(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final Integer participationId = Integer.valueOf(request.getParameter("oid"));
        final Integer offset = Integer.valueOf(request.getParameter("offset"));
        final Object[] args = { participationId, offset, getUserView(request).getPerson().getName() };
        
        try {
            ServiceUtils.executeService(getUserView(request), "ChangeResultParticipationsOrder", args);            
        }
        catch (FenixServiceException e) {
            addActionMessage(request, e.getMessage());
        }

        return prepareEditParticipation(mapping,form,request,response);
    }

    public ActionForward removeParticipation(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final Integer participationId = Integer.parseInt(request.getParameter("participationId"));
        final Object[] args = { participationId, getUserView(request).getPerson().getName() };
        
        try {
            ServiceUtils.executeService(getUserView(request), "DeleteResultParticipation", args);    
        }
        catch (FenixServiceException e) {
            addActionMessage(request, "researcher.result.error.participationNotFound");
        }
        
        return prepareEditParticipation(mapping,form,request,response);
    }
    
    public ActionForward prepareEditParticipationWithSimpleBean(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        final ResultParticipationSimpleCreationBean simpleBean = new ResultParticipationSimpleCreationBean();
        request.setAttribute("simpleBean", simpleBean);
        mantainExternalStatus(request);
        
        //verify if field resultParticipationRole is to be active
        final Result result = readResultFromRequest(request);
        if(result==null){
            return backToResultList(mapping, form, request, response);
        }
        
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

        final ResultParticipationFullCreationBean fullBean = new ResultParticipationFullCreationBean();
        request.setAttribute("fullBean", fullBean);
        mantainExternalStatus(request);
        
        //verify if field resultParticipationRole is to be active
        final Result result = readResultFromRequest(request);
        if(result==null){
            return backToResultList(mapping, form, request, response);
        }
        
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
        final ResultParticipationSimpleCreationBean simpleBean = (ResultParticipationSimpleCreationBean) RenderUtils.getViewState().getMetaObject().getObject();
        
        if(simpleBean.getPerson() != null) {
            final Integer oid = Integer.parseInt(request.getParameter("resultId"));
            final Object[] args = {simpleBean, oid, getUserView(request).getPerson().getName() };
            
            try {
                ServiceUtils.executeService(getUserView(request), "CreateResultParticipation", args);
            }
            catch (FenixServiceException e) {
                addActionMessage(request, "researcher.result.error.resultNotFound");
            }
            
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
        final Result result = readResultFromRequest(request);
        if(result==null){
            return backToResultList(mapping, form, request, response);
        }
        
        if (RenderUtils.getViewState().getMetaObject().getObject() instanceof ResultParticipationSimpleCreationBean) {
            final ResultParticipationSimpleCreationBean simpleBean = (ResultParticipationSimpleCreationBean) RenderUtils.getViewState().getMetaObject().getObject();

            if (simpleBean.getPerson() != null){
                final Object[] args = {simpleBean, result.getIdInternal(), getUserView(request).getPerson().getName() };
                
                try {
                    ServiceUtils.executeService(getUserView(request), "CreateResultParticipation", args);
                }
                catch (FenixServiceException e) {
                    addActionMessage(request, "researcher.result.error.resultNotFound");
                }
            }
            else {
                final ResultParticipationFullCreationBean fullBean = new ResultParticipationFullCreationBean();
                fullBean.setPersonName(simpleBean.getPersonName());
                fullBean.setResultParticipationRole(simpleBean.getResultParticipationRole());
                request.setAttribute("fullBean", fullBean);
                mantainExternalStatus(request);
                
                //verify if field resultParticipationRole is to be active  
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
            final ResultParticipationFullCreationBean fullBean = (ResultParticipationFullCreationBean) RenderUtils.getViewState().getMetaObject().getObject();
            final Object[] args = {fullBean, result.getIdInternal(), getUserView(request).getPerson().getName() };
          
            try {
                ServiceUtils.executeService(getUserView(request), "CreateResultParticipation", args);
            }
            catch (FenixServiceException e) {
                addActionMessage(request, "researcher.result.error.resultNotFound");
            }
        }
        return prepareEditParticipation(mapping,form,request,response);
    }
    
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
            addActionMessage(request, "researcher.result.error.resultNotFound");
        }
        else {
            request.setAttribute("result", result);
        }
        
        return result;
    }
    
    private void mantainExternalStatus(HttpServletRequest request) {
        final String external = request.getParameter("external");
        if (external != null) {
            request.setAttribute("external", external);
        }
    }
}