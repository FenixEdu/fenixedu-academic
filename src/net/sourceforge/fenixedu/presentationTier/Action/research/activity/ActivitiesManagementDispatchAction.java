package net.sourceforge.fenixedu.presentationTier.Action.research.activity;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.research.activity.Cooperation;
import net.sourceforge.fenixedu.domain.research.activity.Event;
import net.sourceforge.fenixedu.domain.research.activity.EventEdition;
import net.sourceforge.fenixedu.domain.research.activity.JournalIssue;
import net.sourceforge.fenixedu.domain.research.activity.Participation;
import net.sourceforge.fenixedu.domain.research.activity.ParticipationsInterface;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;
import net.sourceforge.fenixedu.domain.research.result.publication.ScopeType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ActivitiesManagementDispatchAction extends FenixDispatchAction {

    public ActionForward listActivities(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Person person = getLoggedPerson(request);

	request.setAttribute("national-events", new ArrayList<Event>(person
		.getAssociatedEvents(ScopeType.NATIONAL)));
	request.setAttribute("international-events", new ArrayList<Event>(person
		.getAssociatedEvents(ScopeType.INTERNATIONAL)));
	request.setAttribute("international-eventEditions", new ArrayList<EventEdition>(person
		.getAssociatedEventEditions(ScopeType.INTERNATIONAL)));
	request.setAttribute("national-eventEditions", new ArrayList<EventEdition>(person
		.getAssociatedEventEditions(ScopeType.NATIONAL)));
	request.setAttribute("national-journals", new ArrayList<ScientificJournal>(person
		.getAssociatedScientificJournals(ScopeType.NATIONAL)));
	request.setAttribute("international-journals", new ArrayList<ScientificJournal>(person
		.getAssociatedScientificJournals(ScopeType.INTERNATIONAL)));
	request.setAttribute("cooperations", new ArrayList<Cooperation>(person.getAssociatedCooperations()));
	request.setAttribute("national-issues", new ArrayList<JournalIssue>(person
		.getAssociatedJournalIssues(ScopeType.NATIONAL)));
	request.setAttribute("international-issues", new ArrayList<JournalIssue>(person
		.getAssociatedJournalIssues(ScopeType.INTERNATIONAL)));
	return mapping.findForward("ListActivities");
    }

    public ActionForward prepareDeleteEventParticipations(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	return prepareDelete(mapping, form, request, response, getEventFromRequest(request));
    }

    public ActionForward prepareDeleteEventEditionParticipations(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	return prepareDelete(mapping, form, request, response, getEventEditionFromRequest(request));
    }

    public ActionForward prepareDeleteScientificJournalParticipations(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	return prepareDelete(mapping, form, request, response, getScientificJournalFromRequest(request));
    }

    public ActionForward prepareDeleteJournalIssueParticipations(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	return prepareDelete(mapping, form, request, response, getIssueFromRequest(request));
    }

    public ActionForward prepareDeleteCooperationParticipations(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	return prepareDelete(mapping, form, request, response, getCooperationFromRequest(request));
    }

    private ActionForward prepareDelete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response, ParticipationsInterface objectWithParticipations) {

	request.setAttribute("researchActivity", objectWithParticipations);
	String forwardTo = (String) request.getAttribute("forwardTo");
	if (forwardTo == null)
	    forwardTo = request.getParameter("forwardTo");

	request.setAttribute("confirm", "yes");

	return mapping.findForward(forwardTo);
    }

    public ActionForward deleteEventParticipations(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	return delete(mapping, form, request, response, getEventFromRequest(request));
    }

    public ActionForward deleteEventEditionParticipations(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	return delete(mapping, form, request, response, getEventEditionFromRequest(request));
    }

    public ActionForward deleteScientificJournalParticipations(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	return delete(mapping, form, request, response, getScientificJournalFromRequest(request));
    }

    public ActionForward deleteJournalIssueParticipations(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	return delete(mapping, form, request, response, getIssueFromRequest(request));
    }

    public ActionForward deleteCooperationParticipations(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	return delete(mapping, form, request, response, getCooperationFromRequest(request));
    }

    private ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response, ParticipationsInterface objectWithParticipations) throws Exception {

	String forwardTo = request.getParameter("forwardTo");

	if (request.getParameter("cancel") != null) {
	    request.setAttribute("loggedPerson", getLoggedPerson(request));
	    request.setAttribute("researchActivity", objectWithParticipations);
	} else if (request.getParameter("confirm") != null) {
	    try {
		for (Participation participation : objectWithParticipations
			.getParticipationsFor(getLoggedPerson(request))) {
		    executeService(request, "RemoveResearchActivityParticipation",
			    new Object[] { participation });
		}
	    } catch (Exception e) {
		addActionMessage(request, e.getMessage());
	    }
	    return listActivities(mapping, form, request, response);
	}

	return mapping.findForward(forwardTo);
    }

    protected Cooperation getCooperationFromRequest(HttpServletRequest request) {
	return (Cooperation) RootDomainObject.readDomainObjectByOID(Cooperation.class, Integer
		.valueOf(request.getParameter("activityId")));
    }

    protected JournalIssue getIssueFromRequest(HttpServletRequest request) {
	return (JournalIssue) RootDomainObject.readDomainObjectByOID(JournalIssue.class, Integer
		.valueOf(request.getParameter("activityId")));
    }

    protected ScientificJournal getScientificJournalFromRequest(HttpServletRequest request) {
	return (ScientificJournal) RootDomainObject.readDomainObjectByOID(ScientificJournal.class, Integer
		.valueOf(request.getParameter("activityId")));
    }

    protected Event getEventFromRequest(HttpServletRequest request) {
	return (Event) RootDomainObject.readDomainObjectByOID(Event.class, Integer.valueOf(request
		.getParameter("activityId")));
    }

    protected EventEdition getEventEditionFromRequest(HttpServletRequest request) {
	return (EventEdition) RootDomainObject.readDomainObjectByOID(EventEdition.class, Integer
		.valueOf(request.getParameter("activityId")));
    }

}