package net.sourceforge.fenixedu.presentationTier.Action.research.activity;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.research.activity.CooperationParticipantBean;
import net.sourceforge.fenixedu.dataTransferObject.research.activity.EventParticipantBean;
import net.sourceforge.fenixedu.dataTransferObject.research.activity.ParticipantBean;
import net.sourceforge.fenixedu.dataTransferObject.research.activity.ResearchActivityEditionBean;
import net.sourceforge.fenixedu.dataTransferObject.research.activity.ResearchActivityParticipantEditionBean;
import net.sourceforge.fenixedu.dataTransferObject.research.activity.ResearchCooperationEditionBean;
import net.sourceforge.fenixedu.dataTransferObject.research.activity.ResearchEventEditionBean;
import net.sourceforge.fenixedu.dataTransferObject.research.activity.ResearchScientificJournalEditionBean;
import net.sourceforge.fenixedu.dataTransferObject.research.activity.ScientificJournalParticipantBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.activity.Cooperation;
import net.sourceforge.fenixedu.domain.research.activity.CooperationParticipation;
import net.sourceforge.fenixedu.domain.research.activity.Event;
import net.sourceforge.fenixedu.domain.research.activity.EventParticipation;
import net.sourceforge.fenixedu.domain.research.activity.Participation;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournalParticipation;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class EditResearchActivityDispatchAction extends ActivitiesManagementDispatchAction {

    
    public ActionForward prepareEvent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	
	Person person = getLoggedPerson(request);
	Event event = (Event) RootDomainObject.readDomainObjectByOID(Event.class, Integer.valueOf(request.getParameter("activityId")));
	ResearchEventEditionBean bean = new ResearchEventEditionBean();
	bean.setEvent(event);
	request.setAttribute("schema", "event.view-defaults");
	fillParticipations(bean, person, event.getParticipations());
	request.setAttribute("researchActivity", event);
	
	return generalPrepare(mapping, form, request, response, bean, person);
    }

    public ActionForward prepareScientificJournal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	
	Person person = getLoggedPerson(request);
	ScientificJournal journal = (ScientificJournal) RootDomainObject.readDomainObjectByOID(ScientificJournal.class, Integer.valueOf(request.getParameter("activityId")));
	ResearchScientificJournalEditionBean bean = new ResearchScientificJournalEditionBean();
	request.setAttribute("schema", "journal.view-defaults");
	bean.setScientificJournal(journal);
	fillParticipations(bean, person, journal.getParticipations());
	request.setAttribute("researchActivity", journal);
	
	return generalPrepare(mapping, form, request, response, bean, person);
    }
    
    public ActionForward prepareCooperation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	
	Person person = getLoggedPerson(request);
	Cooperation cooperation = (Cooperation) RootDomainObject.readDomainObjectByOID(Cooperation.class, Integer.valueOf(request.getParameter("activityId")));
	ResearchCooperationEditionBean bean = new ResearchCooperationEditionBean();
	bean.setCooperation(cooperation);
	request.setAttribute("schema", "cooperation.view-defaults");
	fillParticipations(bean, person, cooperation.getParticipations());
	request.setAttribute("researchActivity", cooperation);
	
	return generalPrepare(mapping, form, request, response, bean, person);
    }
    
    private ActionForward generalPrepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response, ResearchActivityEditionBean bean, Person person) throws Exception {

	request.setAttribute("editionBean", bean);
	request.setAttribute("party", person);
	return mapping.findForward("EditResearchActivity");
    }

    private void fillParticipations(ResearchActivityEditionBean bean, Person person,
	    List<? extends Participation> participations) {

	List<Participation> ownParticipations = new ArrayList<Participation>();
	List<Participation> otherParticipations = new ArrayList<Participation>();

	for (Participation participation : participations) {
	    if (participation.isPersonParticipation() && person.equals(participation.getParty())) {
		ownParticipations.add(participation);
	    } else if (participation.isPersonParticipation() && !person.equals(participation.getParty())) {
		otherParticipations.add(participation);
	    }
	}
	bean.setParticipations(ownParticipations);
	bean.setOtherParticipations(otherParticipations);
    }

    public ActionForward prepareEditEventData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	
	Event event = (Event) RootDomainObject.readDomainObjectByOID(Event.class, Integer.valueOf(request.getParameter("activityId")));
	request.setAttribute("schema", "event.edit-defaults");
	request.setAttribute("researchActivity",event);
	return mapping.findForward("EditData");
    }
    
    public ActionForward prepareEditScientificJournalData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	
	ScientificJournal journal = (ScientificJournal) RootDomainObject.readDomainObjectByOID(ScientificJournal.class, Integer.valueOf(request.getParameter("activityId")));
	request.setAttribute("schema", "journal.edit-defaults");
	request.setAttribute("researchActivity",journal);
	return mapping.findForward("EditData");
    }
    
    public ActionForward prepareEditCooperationData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	
	Cooperation cooperation = (Cooperation) RootDomainObject.readDomainObjectByOID(Cooperation.class, Integer.valueOf(request.getParameter("activityId")));
	request.setAttribute("schema", "cooperation.edit-defaults");
	request.setAttribute("researchActivity",cooperation);
	return mapping.findForward("EditData");
    }
    
    
    public ActionForward prepareEditEventParticipants(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	Event event = (Event) RootDomainObject.readDomainObjectByOID(Event.class, Integer.valueOf(request.getParameter("activityId")));
	Person person = getLoggedPerson(request);
	if (event.getParticipationsFor(person).size()==1) {
	    request.setAttribute("lastRole", "yes");
	}

	request.setAttribute("loggedPerson",person);
	request.setAttribute("participantBeans", createRoleBeans(event.getParticipationsFor(person)));
	request.setAttribute("researchActivity",event);
	return mapping.findForward("EditParticipants");
    }

    public ActionForward prepareEditScientificJournalParticipants(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	ScientificJournal journal = (ScientificJournal) RootDomainObject.readDomainObjectByOID(ScientificJournal.class, Integer.valueOf(request.getParameter("activityId")));
	Person person = getLoggedPerson(request);
	if (journal.getParticipationsFor(person).size()==1) {
	    request.setAttribute("lastRole", "yes");
	}

	request.setAttribute("loggedPerson",person);
	request.setAttribute("participantBeans", createRoleBeans(journal.getParticipationsFor(person)));
	request.setAttribute("researchActivity",journal);
	return mapping.findForward("EditParticipants");
    }

    
    public ActionForward prepareEditCooperationParticipants(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	Cooperation cooperation = (Cooperation) RootDomainObject.readDomainObjectByOID(Cooperation.class, Integer.valueOf(request.getParameter("activityId")));
	Person person = getLoggedPerson(request);
	if (cooperation.getParticipationsFor(person).size()==1) {
	    request.setAttribute("lastRole", "yes");
	}

	request.setAttribute("loggedPerson",person);
	request.setAttribute("participantBeans", createRoleBeans(cooperation.getParticipationsFor(person)));
	request.setAttribute("researchActivity",cooperation);
	return mapping.findForward("EditParticipants");
    }

    
    public ActionForward editParticipants(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	String forwardTo = request.getParameter("forwardTo");
	
	if (RenderUtils.getViewState() != null) {

	    List<ResearchActivityParticipantEditionBean> beans = (List<ResearchActivityParticipantEditionBean>) RenderUtils
		    .getViewState("participantsTable").getMetaObject().getObject();
	    RenderUtils.invalidateViewState("participantsTable");

	    List<ResearchActivityParticipantEditionBean> notEditedParticipants = null;
	    try {
		notEditedParticipants = (List<ResearchActivityParticipantEditionBean>) executeService(
			request, "EditResearchActivityParticipants", new Object[] { beans });
	    } catch (DomainException e) {
		addActionMessage(request, e.getMessage(), null);
	    }

	    request.setAttribute("unableToEdit", notEditedParticipants);
	}

	return mapping.findForward(forwardTo);
    }

    public ActionForward prepareCreateNewEventParticipationRole(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	String forwardTo = request.getParameter("forwardTo");
	Event event = (Event) RootDomainObject.readDomainObjectByOID(Event.class, Integer.valueOf(request.getParameter("activityId")));
	EventParticipantBean bean = new EventParticipantBean();
	bean.setEvent(event);
	bean.setPerson(getLoggedPerson(request));
	
	request.setAttribute("participationRoleBean", bean);
	return mapping.findForward(forwardTo);
    }
    			
    public ActionForward prepareCreateNewScientificJournalParticipationRole(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	String forwardTo = request.getParameter("forwardTo");
	ScientificJournal journal = (ScientificJournal) RootDomainObject.readDomainObjectByOID(ScientificJournal.class, Integer.valueOf(request.getParameter("activityId")));
	ScientificJournalParticipantBean bean = new ScientificJournalParticipantBean();
	bean.setScientificJournal(journal);
	bean.setPerson(getLoggedPerson(request));
	
	request.setAttribute("participationRoleBean", bean);
	return mapping.findForward(forwardTo);
    }
    
     public ActionForward createNewParticipationRole(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	 String forwardTo = request.getParameter("forwardTo");
	 
	if (RenderUtils.getViewState() != null) {
	    ParticipantBean participantBean = (ParticipantBean) RenderUtils.getViewState().getMetaObject()
		    .getObject();

	    try {
		executeService(request, "CreateResearchActivityParticipation", new Object[] {
			participantBean.getActivity(), participantBean.getRole(),
			participantBean.getPerson() });
	    } catch (DomainException e) {
		addActionMessage(request, e.getMessage(), null);
		request.setAttribute("participationRoleBean", participantBean);
	    }
	}

	return mapping.findForward(forwardTo);
    }

    public ActionForward removeParticipation(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	
	String forwardTo = request.getParameter("forwardTo");
	
	final Integer oid = Integer.parseInt(request.getParameter("participationId"));
	Participation participation = (Participation) RootDomainObject.readDomainObjectByOID(
		Participation.class, oid);

	if (participation != null) {
	    try {
		executeService(request, "RemoveResearchActivityParticipation", new Object[] { participation });
	    } catch (DomainException e) {
		addActionMessage(request, e.getMessage(), null);
	    }
	}

	return mapping.findForward(forwardTo);
    }

    private List<ResearchActivityParticipantEditionBean> createRoleBeans(
	    List<? extends Participation> participations) {
	List<ResearchActivityParticipantEditionBean> participantBeans = new ArrayList<ResearchActivityParticipantEditionBean>();
	for (Participation participation : participations) {
	    participantBeans.add(new ResearchActivityParticipantEditionBean(participation, participation
		    .getRole()));
	}
	return participantBeans;
    }
}