package net.sourceforge.fenixedu.presentationTier.Action.research.activity;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.research.activity.Cooperation;
import net.sourceforge.fenixedu.domain.research.activity.Event;
import net.sourceforge.fenixedu.domain.research.activity.Participation;
import net.sourceforge.fenixedu.domain.research.activity.ResearchActivity;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ActivitiesManagementDispatchAction extends FenixDispatchAction {
	
	/*
	 * LIST ACTIVITIES IN RESEARCH ACTIVITIES MAIN PAGE
	 */
    public ActionForward listActivities (ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Person person = getLoggedPerson(request);
        
        List<Event> events = new ArrayList<Event>();
        List<ScientificJournal> journals = new ArrayList<ScientificJournal>();
        List<Cooperation> cooperations = new ArrayList<Cooperation>();

        for(Participation participation : person.getParticipations()) {
            ResearchActivity researchActivity = (ResearchActivity) participation.getResearchActivity();
            
            if(researchActivity.isEvent() && !events.contains(researchActivity))
            {
            	events.add((Event)researchActivity);
            }
            else if(researchActivity.isScientificJournal() && !journals.contains(researchActivity))
            {
            	journals.add((ScientificJournal)researchActivity);
            }
            else if(researchActivity.isCooperation() && !cooperations.contains(researchActivity))
            {
            	cooperations.add((Cooperation)researchActivity);
            }
        }
        
        request.setAttribute("events", events);
        request.setAttribute("journals", journals);
        request.setAttribute("cooperations", cooperations);
        return mapping.findForward("ListActivities");  
    }
    
    /*
     * DELETE A RESEARCH ACTIVITY
     */
    public ActionForward prepareDelete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		ResearchActivity activity = getResearchActivity(request);
		request.setAttribute("researchActivity", activity);
		
		String forwardTo = (String)request.getAttribute("forwardTo");
		if(forwardTo == null)
			forwardTo = request.getParameter("forwardTo");
		
		request.setAttribute("confirm", "yes");

		return mapping.findForward(forwardTo);
	}
    
    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	Person person = getLoggedPerson(request);
    	String forwardTo = request.getParameter("forwardTo");
    	ResearchActivity activity = getResearchActivity(request);
    	
        if(activity != null) {
			if(request.getParameter("cancel") != null) {
				request.setAttribute("loggedPerson", person);
				request.setAttribute("researchActivity", activity);
			}
			else if(request.getParameter("confirm") != null) {
	        	try {
	        		executeService(request, "RemoveResearchActivityParticipation", new Object[] { person, activity });
	        	} catch (Exception e) {
					addActionMessage(request, e.getMessage());
				}
	        	return listActivities(mapping, form, request, response);
	        }
			
			return mapping.findForward(forwardTo);
        }
        else {
        	return listActivities(mapping, form, request, response);
        }
    }
    
    protected ResearchActivity getResearchActivity(HttpServletRequest request) {
    	ResearchActivity activity = (ResearchActivity)request.getAttribute("researchActivity");
    	
    	if(activity == null) {
    		final Integer oid = Integer.parseInt(request.getParameter("researchActivityId"));
    		activity = (ResearchActivity) rootDomainObject.readResearchActivityByOID(oid);
    	}
    	
    	if(activity == null && RenderUtils.getViewState() != null) {
    		return (ResearchActivity) RenderUtils.getViewState("researchActivity").getMetaObject().getObject();
    	}
    	
    	return activity;
    	
    	//return (ResearchActivity) ((activity != null) ? activity : RenderUtils.getViewState("researchActivity").getMetaObject().getObject());
    }
}