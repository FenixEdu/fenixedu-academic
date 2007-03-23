package net.sourceforge.fenixedu.presentationTier.Action.research.activity;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.research.activity.ResearchActivityEditionBean;
import net.sourceforge.fenixedu.dataTransferObject.research.activity.ResearchActivityParticipantBean;
import net.sourceforge.fenixedu.dataTransferObject.research.activity.ResearchActivityParticipantEditionBean;
import net.sourceforge.fenixedu.dataTransferObject.research.activity.ResearchCooperationEditionBean;
import net.sourceforge.fenixedu.dataTransferObject.research.activity.ResearchEventEditionBean;
import net.sourceforge.fenixedu.dataTransferObject.research.activity.ResearchScientificJournalEditionBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.activity.Cooperation;
import net.sourceforge.fenixedu.domain.research.activity.Event;
import net.sourceforge.fenixedu.domain.research.activity.Participation;
import net.sourceforge.fenixedu.domain.research.activity.ResearchActivity;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class EditResearchActivityDispatchAction extends ActivitiesManagementDispatchAction {
	
	public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Person person = getLoggedPerson(request);
		
		ResearchActivity activity = getResearchActivity(request);
		
		/*
		 * Logged person participations and other participations
		 */
		List<Participation> participations = new ArrayList<Participation>();
		List<Participation> otherParticipations = new ArrayList<Participation>();
		
		for (Participation participation : activity.getParticipations()) {
			if (participation.getParty().isPerson() && ((Person)participation.getParty()).equals(person)) {
				participations.add(participation);
			}
			else if (participation.getParty().isPerson() && !((Person)participation.getParty()).equals(person)) {
				otherParticipations.add(participation);
			}
		}
		
		ResearchActivityEditionBean bean = null;
		String schema = null;
		if(activity.isCooperation()) {
			bean = new ResearchCooperationEditionBean();
			((ResearchCooperationEditionBean)bean).setCooperation((Cooperation)activity);
			schema = "cooperation.view-defaults";
		}
		else if(activity.isEvent()) {
			bean = new ResearchEventEditionBean();
			((ResearchEventEditionBean)bean).setEvent((Event)activity);
			schema = "event.view-defaults";
		}
		else if(activity.isScientificJournal()) {
			bean = new ResearchScientificJournalEditionBean();
			((ResearchScientificJournalEditionBean)bean).setScientificJournal((ScientificJournal)activity);
			schema = "journal.view-defaults";
		}
		
		bean.setParticipations(participations);
		bean.setOtherParticipations(otherParticipations);
		
		request.setAttribute("schema", schema);
		request.setAttribute("editionBean", bean);
		request.setAttribute("party", getLoggedPerson(request));
		request.setAttribute("researchActivity", activity);
		
		return mapping.findForward("EditResearchActivity");

	}
	
	// ***************************************
	// DATA
	// ***************************************

	public ActionForward prepareEditData(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		ResearchActivity activity = getResearchActivity(request);
		request.setAttribute("researchActivity", activity);
		
		if(activity.isCooperation()) {
			request.setAttribute("schema", "cooperation.edit-defaults");
		}
		else if(activity.isEvent()) {
			request.setAttribute("schema", "event.edit-defaults");
		}
		else if(activity.isScientificJournal()) {
			request.setAttribute("schema", "journal.edit-defaults");
		}
		
		return mapping.findForward("EditData");
	}	
	 
	public ActionForward prepareEditParticipants(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		if(getResearchActivity(request).getParticipationsCountFor(getLoggedPerson(request)) == 1) {
			request.setAttribute("lastRole", "yes");
		}
		
		setRequestAttributes(request);
		return mapping.findForward("EditParticipants");
	}
	
	public ActionForward editParticipants(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {		
		
		if(RenderUtils.getViewState() != null){
			ResearchActivity activity = getResearchActivity(request);
			
			List<ResearchActivityParticipantEditionBean> beans = (List<ResearchActivityParticipantEditionBean>) RenderUtils.getViewState("participantsTable").getMetaObject().getObject();
			RenderUtils.invalidateViewState("participantsTable");
			
			List<ResearchActivityParticipantEditionBean> notEditedParticipants = null;
			try {
				notEditedParticipants = (List<ResearchActivityParticipantEditionBean>) executeService(request, "EditResearchActivityParticipants", new Object[] { beans });
			} catch (DomainException e) {
	        	addActionMessage(request, e.getMessage(), null);
	        }
			
			request.setAttribute("unableToEdit", notEditedParticipants);
		}
    	
    	return prepareEditParticipants(mapping, form, request, response);
	}
	
	public ActionForward prepareCreateNewParticipationRole(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ResearchActivity researchActivity = getResearchActivity(request);
		
		ResearchActivityParticipantBean participantBean = new ResearchActivityParticipantBean();
		participantBean.setPerson(getLoggedPerson(request));
		participantBean.setResearchActivity(researchActivity);
		
		request.setAttribute("participationRoleBean", participantBean);
		
		return prepareEditParticipants(mapping, form, request, response);
	}
	
	public ActionForward createNewParticipationRole(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		if(RenderUtils.getViewState() != null){
			ResearchActivityParticipantBean participantBean = (ResearchActivityParticipantBean)RenderUtils.getViewState().getMetaObject().getObject();
		
			try {
				executeService(request, "CreateResearchActivityParticipation", new Object[] { participantBean.getResearchActivity(), participantBean.getRole(), participantBean.getPerson() });
			}catch (DomainException e) {
				addActionMessage(request, e.getMessage(), null);
				request.setAttribute("participationRoleBean", participantBean);
			}
		}
		
		return prepareEditParticipants(mapping, form, request, response);
	}	
	
	public ActionForward removeParticipation(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		Person person = getLoggedPerson(request);
		ResearchActivity researchActivity = getResearchActivity(request);
		
		final Integer oid = Integer.parseInt(request.getParameter("participationId"));
		Participation participation = (Participation) RootDomainObject.readDomainObjectByOID(Participation.class, oid);
		
		if(participation != null) {
			try{
				executeService(request, "RemoveResearchActivityParticipation", new Object[] { person, participation, researchActivity});
			} catch (DomainException e) {
	        	addActionMessage(request, e.getMessage(), null);
	        }
		}
		
		return prepareEditParticipants(mapping, form, request, response);
	}
	
	protected void setRequestAttributes(HttpServletRequest request) {
		Person person = getLoggedPerson(request);
		ResearchActivity activity = getResearchActivity(request);
		
		List<ResearchActivityParticipantEditionBean> participantBeans = new ArrayList<ResearchActivityParticipantEditionBean>();
		for(Participation participation : activity.getParticipations()){
			if (participation.getParty().isPerson() && ((Person)participation.getParty()).equals(person)) {
	    		ResearchActivityParticipantEditionBean bean = new ResearchActivityParticipantEditionBean();
	    		bean.setParticipation(participation);
	    		bean.setRole(participation.getRole());
	    		participantBeans.add(bean);
			}
		}
		
		request.setAttribute("loggedPerson", person);
		request.setAttribute("participantBeans", participantBeans);
		request.setAttribute("researchActivity", activity);
	}
    
	// ***************************************
	// PARTICIPANTS
	// ***************************************
//	 public ActionForward prepareParticipantType (ActionMapping mapping, ActionForm form, HttpServletRequest request,
//			HttpServletResponse response) throws Exception {
//    
//    	ResearchActivity activity = getResearchActivity(request);
//    	
//    	ResearchActivityParticipantBean participantBean = new ResearchActivityParticipantBean();
//    	participantBean.setResearchActivity(activity);
//    	request.setAttribute("participantTypeBean", participantBean);
//    	
//    	return prepareEditAssociations(mapping, form, request, response);
//    }
	
	// ***************************************
	// UNITS
	// ***************************************
//	public ActionForward prepareUnitParticipantType (ActionMapping mapping, ActionForm form, HttpServletRequest request,
//			HttpServletResponse response) throws Exception {
//    
//    	ResearchActivity activity = getResearchActivity(request);
//    	
//    	ResearchActivityUnitParticipantBean participantUnitBean = new ResearchActivityUnitParticipantBean();
//    	participantUnitBean.setResearchActivity(activity);
//    	request.setAttribute("participantUnitTypeBean", participantUnitBean);
//    	
//    	return prepareEditAssociations(mapping, form, request, response);
//    }
//	
//	public ActionForward prepareEditParticipantUnits(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response) throws Exception {
//
//		ResearchActivity researchActivity = getResearchActivity(request);
//
//		List<ResearchActivityParticipantEditionBean> beans = new ArrayList<ResearchActivityParticipantEditionBean>();
//		for (Participation participation : researchActivity.getParticipations()) {
//			if (participation.getParty() instanceof Unit) {
//				ResearchActivityParticipantEditionBean bean = new ResearchActivityParticipantEditionBean();
//				bean.setParticipation(participation);
//				bean.setRole(participation.getRole());
//				beans.add(bean);
//			}
//		}
//		
//		String forwardTo = (String)request.getParameter("forwardTo");
//    	request.setAttribute("forwardTo", forwardTo);
//		request.setAttribute("participantUnitBeans", beans);
//		request.setAttribute("researchActivity", researchActivity);
//
//		return mapping.findForward(forwardTo);
//	}
//	
//	public ActionForward editUnitParticipants(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response) throws Exception {		
//		ResearchActivity researchActivity = getResearchActivity(request);
//		
//		request.setAttribute("researchActivity", researchActivity);
//		
//		List<ResearchActivityParticipantEditionBean> beans = (List<ResearchActivityParticipantEditionBean>) RenderUtils.getViewState("participantsUnitsTable").getMetaObject().getObject();
//		
//		List<ResearchActivityParticipantEditionBean> notEditedParticipants = (List<ResearchActivityParticipantEditionBean>) executeService(request, "EditResearchActivityParticipants", new Object[] { beans });
//		
//		RenderUtils.invalidateViewState("participantsUnitsTable");
//		
//		request.setAttribute("unableToEditUnits", notEditedParticipants);
//		
//		return prepareEditAssociations(mapping, form, request, response);
//	}

}