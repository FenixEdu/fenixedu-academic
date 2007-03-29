package net.sourceforge.fenixedu.presentationTier.Action.research.activity;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.research.activity.Cooperation;
import net.sourceforge.fenixedu.domain.research.activity.CooperationParticipation;
import net.sourceforge.fenixedu.domain.research.activity.Event;
import net.sourceforge.fenixedu.domain.research.activity.EventParticipation;
import net.sourceforge.fenixedu.domain.research.activity.Participation;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournalParticipation;
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
	request.setAttribute("national-journals", new ArrayList<ScientificJournal>(person
		.getAssociatedScientificJournals(ScopeType.NATIONAL)));
	request.setAttribute("international-journals", new ArrayList<ScientificJournal>(person
		.getAssociatedScientificJournals(ScopeType.INTERNATIONAL)));
	request.setAttribute("cooperations", new ArrayList<Cooperation>(person.getAssociatedCooperations()));
	return mapping.findForward("ListActivities");
    }

    public ActionForward prepareDeleteEventParticipations(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	Event event = (Event) RootDomainObject.readDomainObjectByOID(Event.class, Integer.valueOf(request
		.getParameter("activityId")));
	request.setAttribute("researchActivity", event);

	return prepareDelete(mapping, form, request, response);
    }

    public ActionForward prepareDeleteJournalParticipations(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	ScientificJournal event = (ScientificJournal) RootDomainObject.readDomainObjectByOID(
		ScientificJournal.class, Integer.valueOf(request.getParameter("activityId")));
	request.setAttribute("researchActivity", event);

	return prepareDelete(mapping, form, request, response);
    }

    public ActionForward prepareDeleteCooperationParticipations(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	Cooperation event = (Cooperation) RootDomainObject.readDomainObjectByOID(Cooperation.class, Integer
		.valueOf(request.getParameter("activityId")));
	request.setAttribute("researchActivity", event);

	return prepareDelete(mapping, form, request, response);
    }

    private ActionForward prepareDelete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	String forwardTo = (String) request.getAttribute("forwardTo");
	if (forwardTo == null)
	    forwardTo = request.getParameter("forwardTo");

	request.setAttribute("confirm", "yes");

	return mapping.findForward(forwardTo);
    }

    public ActionForward deleteEventParticipations(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	Person person = getLoggedPerson(request);
	Event event = (Event) RootDomainObject.readDomainObjectByOID(Event.class, Integer.valueOf(request
		.getParameter("activityId")));
	request.setAttribute("researchActivity",event);
	List<EventParticipation> participationsForUser = event.getParticipationsFor(person);
	return delete(mapping, form, request, response, participationsForUser);
    }

    public ActionForward deleteJournalParticipations(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	Person person = getLoggedPerson(request);
	ScientificJournal journal = (ScientificJournal) RootDomainObject.readDomainObjectByOID(ScientificJournal.class, Integer.valueOf(request
		.getParameter("activityId")));
	request.setAttribute("researchActivity",journal);
	List<ScientificJournalParticipation> participationsForUser = journal.getParticipationsFor(person);
	return delete(mapping, form, request, response, participationsForUser);
    }
    
    public ActionForward deleteCooperationParticipations(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	Person person = getLoggedPerson(request);
	Cooperation cooperation = (Cooperation) RootDomainObject.readDomainObjectByOID(Cooperation.class, Integer.valueOf(request
		.getParameter("activityId")));
	request.setAttribute("researchActivity",cooperation);
	List<CooperationParticipation> participationsForUser = cooperation.getParticipationsFor(person);
	return delete(mapping, form, request, response, participationsForUser);
    }
    
    private ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response, List<? extends Participation> participationsToRemove)
	    throws Exception {

	String forwardTo = request.getParameter("forwardTo");

	if (request.getParameter("cancel") != null) {
	    request.setAttribute("loggedPerson", getLoggedPerson(request));
	} else if (request.getParameter("confirm") != null) {
	    try {
		for (Participation participation : participationsToRemove) {
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

}