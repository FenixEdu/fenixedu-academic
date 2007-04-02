package net.sourceforge.fenixedu.presentationTier.Action.research.activity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.research.activity.ResearchEventEditionCreationBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class CreateEventEditionDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	
        request.setAttribute("party", getLoggedPerson(request));
        return mapping.findForward("CreateEventEdition");  
    }

    public ActionForward prepareEventEditionSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
	ResearchEventEditionCreationBean bean = getEventEditionBean(request);
    	if(bean == null) {
    		bean = new ResearchEventEditionCreationBean();
    	}
        
        request.setAttribute("eventEditionBean", bean);
        request.setAttribute("eventEditionCreationSchema", "eventEditionCreation.eventName");
        
        return prepare(mapping, form, request, response);
    }
    
    public ActionForward prepareCreateEventEditionParticipation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	
	ResearchEventEditionCreationBean bean = (ResearchEventEditionCreationBean) getEventEditionBean(request);
    	if(bean == null)
    		return prepareEventEditionSearch(mapping, form, request, response);
    
    	request.setAttribute("eventEditionBean", bean);
    	request.setAttribute("eventEditionCreationSchema", "eventEditionCreation.selectEdition");
    	return prepare(mapping, form, request, response);
    	
    	
    }
    
    public ActionForward createExistentEventEditionParticipation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
    	Person person = getLoggedPerson(request);
    	ResearchEventEditionCreationBean bean = (ResearchEventEditionCreationBean) getEventEditionBean(request);
    	if(bean == null)
    		return prepareEventEditionSearch(mapping, form, request, response);
    	
    	if(bean.getRole() != null) {
    		try {
            	executeService(request, "CreateResearchActivityParticipation", new Object[] {bean.getEventEdition(), bean.getRole(), person });
            } catch (DomainException e) {
            	addActionMessage(request, e.getMessage(), null);
            	request.setAttribute("existentEventBean", bean);
            	request.setAttribute("eventCreationSchema", "eventCreation.existentEvent");
            	return prepare(mapping,form,request,response);
            }
    	}
        
    	return mapping.findForward("Success");
    }
    
   
   
    public ResearchEventEditionCreationBean getEventEditionBean(HttpServletRequest request) {
	ResearchEventEditionCreationBean bean = null;
    	if(RenderUtils.getViewState() != null){
    		bean = (ResearchEventEditionCreationBean) RenderUtils.getViewState().getMetaObject().getObject();
    		return bean;
    	}
    	return bean;
    }
}
