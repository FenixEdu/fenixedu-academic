package net.sourceforge.fenixedu.presentationTier.Action.research.activity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.research.activity.Participation;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ActivitiesManagementDispatchAction extends FenixDispatchAction {

    public ActionForward listActivities(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Person person = getLoggedPerson(request);

	request.setAttribute("national-events", person.getAllNationalEventParticipations());
	request.setAttribute("international-events", person.getAllInternationalEventParticipations());
	request.setAttribute("national-journals", person.getAllNationalScientificJournalParticipations());
	request.setAttribute("international-journals", person.getAllInternationalScientificJournalParticipations());
	request.setAttribute("cooperations", person.getAllCooperationParticipations());
	return mapping.findForward("ListActivities");
    }

    public ActionForward prepareDelete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	Participation participation = getParticipation(request);
	request.setAttribute("participation", participation);

	String forwardTo = (String) request.getAttribute("forwardTo");
	if (forwardTo == null)
	    forwardTo = request.getParameter("forwardTo");

	request.setAttribute("confirm", "yes");

	return mapping.findForward(forwardTo);
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Person person = getLoggedPerson(request);
	String forwardTo = request.getParameter("forwardTo");
	Participation participation = getParticipation(request);

	if (participation != null) {
	    if (request.getParameter("cancel") != null) {
		request.setAttribute("loggedPerson", person);
		request.setAttribute("participation", participation);
	    } else if (request.getParameter("confirm") != null) {
		try {
		    executeService(request, "RemoveResearchActivityParticipation", new Object[] { participation });
		} catch (Exception e) {
		    addActionMessage(request, e.getMessage());
		}
		return listActivities(mapping, form, request, response);
	    }

	    return mapping.findForward(forwardTo);
	} else {
	    return listActivities(mapping, form, request, response);
	}
    }

    protected Participation getParticipation(HttpServletRequest request) {
	IViewState viewState = RenderUtils.getViewState("participation");
	Participation participation = null;

	if (viewState != null) {
	    participation = (Participation) viewState.getMetaObject().getObject();
	} else {
	    if (participation == null) {
		final Integer oid = Integer.parseInt(request.getParameter("participationId"));
		participation = (Participation) rootDomainObject.readParticipationByOID(oid);
	    }
	}
	request.setAttribute("participation", participation);
	return participation;

    }
}