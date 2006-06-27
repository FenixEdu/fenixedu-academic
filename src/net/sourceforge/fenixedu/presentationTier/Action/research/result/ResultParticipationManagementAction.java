package net.sourceforge.fenixedu.presentationTier.Action.research.result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultParticipationFullCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultParticipationSimpleCreationBean;
import net.sourceforge.fenixedu.domain.research.result.ResultPatent;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.domain.research.result.ResultPublication;
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
        
        //verify if field bookRole is to be active
        Result result = rootDomainObject.readResultByOID(resultId);
        request.setAttribute("result", result);
        /*if((result instanceof ResultPublication) && (((ResultPublication)result).getResultPublicationType() != null))
        {
            PublicationType publicationType = ((ResultPublication)result).getPublicationType();
            if((publicationType.equals(PublicationType.BOOK)) || (publicationType.equals(PublicationType.BOOK_PART)))
                request.setAttribute("authorshipsSchema","result.authorships.editRole");
        }*/
        
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
        
        Object[] args = { participationId };
        ServiceUtils.executeService(getUserView(request), "DeleteResultParticipation", args);
      
        return prepareEditParticipation(mapping,form,request,response);
    }
    
    public ActionForward prepareEditParticipationWithSimpleBean(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ResultParticipationSimpleCreationBean simpleBean = new ResultParticipationSimpleCreationBean();
        request.setAttribute("simpleBean", simpleBean);
        mantainExternalStatus(request);
        
        //verify if field bookRole is to be active
        final Integer resultId = Integer.valueOf(request.getParameter("resultId"));
        Result result = rootDomainObject.readResultByOID(resultId);
        /*if((result instanceof ResultPublication) && (((ResultPublication)result).getResultPublicationType() != null))
        {
            PublicationType publicationType = ((ResultPublication)result).getPublicationType();
            final String external = request.getParameter("external");
            if((publicationType.equals(PublicationType.BOOK)) || (publicationType.equals(PublicationType.BOOK_PART)))
            {
                if(external.equals("false"))
                    request.setAttribute("schemaInternalPersonCreation","resultAuthorship.internalPerson.creationWithBookRole");
                else
                    request.setAttribute("schemaExternalPersonSimpleCreation","resultAuthorship.externalPerson.simpleCreationWithBookRole");
            }
        }*/
        
        return prepareEditParticipation(mapping,form,request,response);
    }
    
    public ActionForward prepareEditParticipationWithFullBean(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ResultParticipationFullCreationBean fullBean = new ResultParticipationFullCreationBean();
        request.setAttribute("fullBean", fullBean);
        mantainExternalStatus(request);
        
        //verify if field bookRole is to be active
        final Integer resultId = Integer.valueOf(request.getParameter("resultId"));
        Result result = rootDomainObject.readResultByOID(resultId);
        /*if((result instanceof ResultPublication) && (((ResultPublication)result).getResultPublicationType() != null))
        {
            PublicationType publicationType = ((ResultPublication)result).getPublicationType();
            if((publicationType.equals(PublicationType.BOOK)) || (publicationType.equals(PublicationType.BOOK_PART)))
                request.setAttribute("schemaExternalPersonFullCreation","resultAuthorship.externalPerson.fullCreationWithBookRole");
        }*/
        
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
                request.setAttribute("fullBean", fullBean);
                
                mantainExternalStatus(request);
                
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
        Result result = rootDomainObject.readResultByOID(resultId);
        
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