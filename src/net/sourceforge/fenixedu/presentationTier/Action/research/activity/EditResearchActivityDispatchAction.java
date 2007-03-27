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

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	Person person = getLoggedPerson(request);
	Participation participation = getParticipation(request);

	ResearchActivityEditionBean bean = null;
	String schema = null;
	if (participation.isCooperationParticipation()) {
	    bean = new ResearchCooperationEditionBean();
	    Cooperation cooperation = ((CooperationParticipation) participation).getCooperation();
	    ((ResearchCooperationEditionBean) bean).setCooperation(cooperation);
	    schema = "cooperation.view-defaults";
	    fillParticipations(bean, person, cooperation.getParticipations());
	    request.setAttribute("researchActivity", cooperation);
	} else if (participation.isEventParticipation()) {
	    bean = new ResearchEventEditionBean();
	    Event event = ((EventParticipation) participation).getEvent();
	    ((ResearchEventEditionBean) bean).setEvent(event);
	    schema = "event.view-defaults";
	    fillParticipations(bean, person, event.getParticipations());
	    request.setAttribute("researchActivity", event);
	} else if (participation.isScientificJournaltParticipation()) {
	    bean = new ResearchScientificJournalEditionBean();
	    ScientificJournal journal = ((ScientificJournalParticipation) participation)
		    .getScientificJournal();
	    ((ResearchScientificJournalEditionBean) bean).setScientificJournal(journal);
	    schema = "journal.view-defaults";
	    fillParticipations(bean, person, journal.getParticipations());
	    request.setAttribute("researchActivity", journal);
	}

	request.setAttribute("schema", schema);
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

    public ActionForward prepareEditData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Participation participation = getParticipation(request);

	if (participation.isCooperationParticipation()) {
	    request.setAttribute("schema", "cooperation.edit-defaults");
	    request.setAttribute("researchActivity", ((CooperationParticipation) participation)
		    .getCooperation());
	} else if (participation.isEventParticipation()) {
	    request.setAttribute("schema", "event.edit-defaults");
	    request.setAttribute("researchActivity", ((EventParticipation) participation).getEvent());
	} else if (participation.isScientificJournaltParticipation()) {
	    request.setAttribute("schema", "journal.edit-defaults");
	    request.setAttribute("researchActivity", ((ScientificJournalParticipation) participation)
		    .getScientificJournal());
	}

	return mapping.findForward("EditData");
    }

    public ActionForward prepareEditParticipants(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	Participation participation = getParticipation(request);
	if (participation.isLastParticipation()) {
	    request.setAttribute("lastRole", "yes");
	}

	setRequestAttributes(request);
	return mapping.findForward("EditParticipants");
    }

    public ActionForward editParticipants(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

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

	return prepareEditParticipants(mapping, form, request, response);
    }

    public ActionForward prepareCreateNewParticipationRole(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	Participation participation = getParticipation(request);
	ParticipantBean participantBean = getBeanForParticipation(getLoggedPerson(request), participation);

	request.setAttribute("participationRoleBean", participantBean);

	return prepareEditParticipants(mapping, form, request, response);
    }

    private ParticipantBean getBeanForParticipation(Person person, Participation participation) {
	ParticipantBean bean = null;
	if (participation.isEventParticipation()) {
	    bean = new EventParticipantBean();
	    bean.setPerson(person);
	    ((EventParticipantBean) bean).setEvent(((EventParticipation) participation).getEvent());
	} else if (participation.isScientificJournaltParticipation()) {
	    bean = new ScientificJournalParticipantBean();
	    bean.setPerson(person);
	    ((ScientificJournalParticipantBean) bean)
		    .setScientificJournal(((ScientificJournalParticipation) participation)
			    .getScientificJournal());
	} else if (participation.isCooperationParticipation()) {
	    bean = new CooperationParticipantBean();
	    bean.setPerson(person);
	    ((CooperationParticipantBean) bean).setCooperation(((CooperationParticipation) participation)
		    .getCooperation());
	}

	return bean;
    }

     public ActionForward createNewParticipationRole(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

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

	return prepareEditParticipants(mapping, form, request, response);
    }

    public ActionForward removeParticipation(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	Person person = getLoggedPerson(request);

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

	return prepareEditParticipants(mapping, form, request, response);
    }

    protected void setRequestAttributes(HttpServletRequest request) {
	Person person = getLoggedPerson(request);
	Participation participation = getParticipation(request);
	if (participation.isCooperationParticipation()) {
	    Cooperation cooperation = ((CooperationParticipation) participation).getCooperation();
	    request.setAttribute("participantBeans",
		    createRoleBeans(cooperation.getParticipationsFor(person)));
	    request.setAttribute("researchActivity", cooperation);
	} else if (participation.isEventParticipation()) {
	    Event event = ((EventParticipation) participation).getEvent();
	    request.setAttribute("participantBeans", createRoleBeans(event.getParticipationsFor(person)));
	    request.setAttribute("researchActivity", event);
	} else if (participation.isScientificJournaltParticipation()) {

	    ScientificJournal scientificJournal = ((ScientificJournalParticipation) participation)
		    .getScientificJournal();
	    request.setAttribute("participantBeans", createRoleBeans(scientificJournal
		    .getParticipationsFor(person)));
	    request.setAttribute("researchActivity", scientificJournal);
	}

	request.setAttribute("loggedPerson", person);
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