package net.sourceforge.fenixedu.presentationTier.Action.research.activity;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.activity.EventParticipantBean;
import net.sourceforge.fenixedu.dataTransferObject.research.activity.JournalIssueParticipantBean;
import net.sourceforge.fenixedu.dataTransferObject.research.activity.ParticipantBean;
import net.sourceforge.fenixedu.dataTransferObject.research.activity.ResearchActivityEditionBean;
import net.sourceforge.fenixedu.dataTransferObject.research.activity.ResearchActivityParticipantEditionBean;
import net.sourceforge.fenixedu.dataTransferObject.research.activity.ScientificJournalParticipantBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.activity.Event;
import net.sourceforge.fenixedu.domain.research.activity.JournalIssue;
import net.sourceforge.fenixedu.domain.research.activity.Participation;
import net.sourceforge.fenixedu.domain.research.activity.ParticipationsInterface;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class EditResearchActivityDispatchAction extends ActivitiesManagementDispatchAction {

    private ActionForward generalPrepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response, ParticipationsInterface objectWithParticipationObject) {

	ResearchActivityEditionBean bean = ResearchActivityEditionBean
		.getResearchActivityEditionBean(objectWithParticipationObject);
	Person loggedPerson = getLoggedPerson(request);
	fillParticipations(bean, loggedPerson, objectWithParticipationObject.getParticipations());
	request.setAttribute("researchActivity", objectWithParticipationObject);
	request.setAttribute("editionBean", bean);
	request.setAttribute("party", loggedPerson);
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

    public ActionForward prepareEvent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	return generalPrepare(mapping, form, request, response, getEventFromRequest(request));
    }

    public ActionForward prepareEventEdition(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	return generalPrepare(mapping, form, request, response, getEventEditionFromRequest(request));
    }

    public ActionForward prepareScientificJournal(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	return generalPrepare(mapping, form, request, response, getScientificJournalFromRequest(request));
    }

    public ActionForward prepareJournalIssue(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	return generalPrepare(mapping, form, request, response, getIssueFromRequest(request));
    }

    public ActionForward prepareCooperation(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	return generalPrepare(mapping, form, request, response, getCooperationFromRequest(request));
    }

    private ActionForward generalPrepareData(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response,
	    ParticipationsInterface objectWithParticipation) {

	request.setAttribute("researchActivity", objectWithParticipation);
	return mapping.findForward("EditData");
    }

    public ActionForward prepareEditEventData(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	return generalPrepareData(mapping, form, request, response, getEventFromRequest(request));

    }

    public ActionForward prepareEditEventEditionData(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	return generalPrepareData(mapping, form, request, response, getEventEditionFromRequest(request));

    }
    
    public ActionForward prepareEditScientificJournalData(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	
	ScientificJournal scientificJournal = getScientificJournalFromRequest(request);
	if(scientificJournal.canBeEditedByCurrentUser()) {
	    return generalPrepareData(mapping,form,request,response,scientificJournal);
	}
	else {
	    addActionMessage(request, "label.error.othersAssociatedToJournal");
	    return generalPrepare(mapping, form, request, response, scientificJournal);
	}
    }

    public ActionForward prepareEditJournalIssueData(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	JournalIssue issue = getIssueFromRequest(request);
	if(issue.canBeEditedByCurrentUser()) {
	    return generalPrepareData(mapping, form, request, response, issue);
	}
	else {
	    addActionMessage(request, "label.error.othersAssociatedToIssue");
	    return generalPrepare(mapping, form, request, response, issue);
	}
    }

    public ActionForward prepareEditCooperationData(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	return generalPrepareData(mapping, form, request, response, getCooperationFromRequest(request));
    }

    private ActionForward generalPrepareParticipants(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response,
	    ParticipationsInterface objectWithParticipations) {

	Person person = getLoggedPerson(request);
	if (objectWithParticipations.getParticipationsFor(person).size() == 1) {
	    request.setAttribute("lastRole", "yes");
	}
	request.setAttribute("loggedPerson", person);
	request.setAttribute("participantBeans", createRoleBeans(objectWithParticipations
		.getParticipationsFor(person)));
	request.setAttribute("researchActivity", objectWithParticipations);
	return mapping.findForward("EditParticipants");
    }

    public ActionForward prepareEditEventParticipants(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	return generalPrepareParticipants(mapping, form, request, response, getEventFromRequest(request));
    }

    public ActionForward prepareEditEventEditionParticipants(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	return generalPrepareParticipants(mapping, form, request, response, getEventEditionFromRequest(request));
    }

    public ActionForward prepareEditScientificJournalParticipants(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	return generalPrepareParticipants(mapping, form, request, response, getScientificJournalFromRequest(request));
    }

    public ActionForward prepareEditJournalIssueParticipants(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	return generalPrepareParticipants(mapping, form, request, response, getIssueFromRequest(request));
    }

    public ActionForward prepareEditCooperationParticipants(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	return generalPrepareParticipants(mapping, form, request, response, getCooperationFromRequest(request));
    }

    public ActionForward editParticipants(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

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
    
    private ActionForward generalPrepareCreateNewParticipatonRole (ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response, ParticipationsInterface objectWithParticipations) {
	
	
	String forwardTo = request.getParameter("forwardTo");
	ParticipantBean bean = ParticipantBean.getParticipantBean(objectWithParticipations);
	bean.setPerson(getLoggedPerson(request));
	request.setAttribute("participationRoleBean", bean);
	return mapping.findForward(forwardTo);
    }
    
    public ActionForward prepareCreateNewEventParticipationRole(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	
	return generalPrepareCreateNewParticipatonRole(mapping, form, request, response, getEventFromRequest(request));
    }

    public ActionForward prepareCreateNewEventEditionParticipationRole(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	
	return generalPrepareCreateNewParticipatonRole(mapping, form, request, response, getEventEditionFromRequest(request));
    }
    
    public ActionForward prepareCreateNewScientificJournalParticipationRole(ActionMapping mapping,
	    ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

	return generalPrepareCreateNewParticipatonRole(mapping, form, request, response, getScientificJournalFromRequest(request));
	
    }

    public ActionForward prepareCreateNewJournalIssueParticipationRole(ActionMapping mapping,
	    ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

	return generalPrepareCreateNewParticipatonRole(mapping, form, request, response, getIssueFromRequest(request));
    }

    public ActionForward createNewParticipationRole(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	String forwardTo = request.getParameter("forwardTo");

	if (RenderUtils.getViewState() != null) {
	    ParticipantBean participantBean = (ParticipantBean) RenderUtils.getViewState().getMetaObject()
		    .getObject();

	    try {
		executeService(request, "CreateResearchActivityParticipation",
			new Object[] { participantBean.getActivity(), participantBean.getRole(),
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