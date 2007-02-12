package net.sourceforge.fenixedu.presentationTier.Action.research.activity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.research.activity.ResearchEventCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.activity.ResearchScientificJournalCreationBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class CreateScientificJournalDispatchAction extends FenixDispatchAction {
    
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        request.setAttribute("party", getLoggedPerson(request));
        return mapping.findForward("CreateScientificJournal");  
    }
    
    public ActionForward prepareJournalSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
    	ResearchScientificJournalCreationBean bean = getJournalBean(request);
    	if(bean == null) {
    		bean = new ResearchScientificJournalCreationBean();
    	}
        
        request.setAttribute("journalBean", bean);
        request.setAttribute("journalCreationSchema", "scientificJournalCreation.journalName");
        
        return prepare(mapping, form, request, response);
    }
    
    public ActionForward prepareCreateScientificJournalParticipation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	
    	ResearchScientificJournalCreationBean bean = getJournalBean(request);
    	if(bean == null)
    		return prepareJournalSearch(mapping, form, request, response);
    	
    	if(bean.getScientificJournal() != null) {
    		request.setAttribute("existentJournalBean", bean);
    		request.setAttribute("journalCreationSchema", "journalCreation.existentJournal");
    		return prepare(mapping, form, request, response);
    	}
    	else {
        	request.setAttribute("inexistentJournalBean", bean);
        	request.setAttribute("journalCreationSchema", "journalCreation.inexistentJournal");
        	return prepare(mapping, form, request, response);
    	}
    }
    
    public ActionForward createExistentJournalParticipation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
    	Person person = getLoggedPerson(request);
    	ResearchScientificJournalCreationBean bean = (ResearchScientificJournalCreationBean) getJournalBean(request);
    	if(bean == null)
    		return prepareJournalSearch(mapping, form, request, response);
    	
    	if(bean.getRole() != null) {
    		try {
            	executeService(request, "CreateResearchActivityParticipation", new Object[] {bean.getScientificJournal(), bean.getRole(), person });
            } catch (DomainException e) {
            	addActionMessage(request, e.getMessage(), null);
            	request.setAttribute("existentJournalBean", bean);
            	request.setAttribute("journalCreationSchema", "journalCreation.existentJourna");
            	return prepare(mapping,form,request,response);
            }
    	}
        
    	return mapping.findForward("Success");
    }
    
    public ActionForward createInexistentJournalParticipation (ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	Person person = getLoggedPerson(request);
    	
    	ResearchScientificJournalCreationBean bean = (ResearchScientificJournalCreationBean) getJournalBean(request);
    	if(bean == null)
    		return prepareJournalSearch(mapping, form, request, response);
        
    	ScientificJournal journal = null;
        try {
        	journal = (ScientificJournal) executeService(request, "CreateScientificJournal", new Object[] {bean.getScientificJournalName(), bean.getLocationType() } );
        	executeService(request,"CreateResearchActivityParticipation", new Object[] { journal, bean.getRole(), person});
        } catch (DomainException e) {
        	addActionMessage(request, e.getMessage(), null);
        	request.setAttribute("inexistentJournalBean", bean);
        	request.setAttribute("journalCreationSchema", "journalCreation.inexistentJournal");
        	return prepare(mapping,form,request,response);
        }
        
        return mapping.findForward("Success");
    }
    
    public ResearchScientificJournalCreationBean getJournalBean(HttpServletRequest request) {
    	ResearchScientificJournalCreationBean bean = null;
    	if(RenderUtils.getViewState() != null){
    		bean = (ResearchScientificJournalCreationBean) RenderUtils.getViewState().getMetaObject().getObject();
    		return bean;
    	}
    	return bean;
    }
   
}