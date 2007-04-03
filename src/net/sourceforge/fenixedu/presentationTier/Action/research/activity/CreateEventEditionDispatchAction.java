package net.sourceforge.fenixedu.presentationTier.Action.research.activity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.activity.ResearchEventEditionCreationBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.activity.Event;
import net.sourceforge.fenixedu.domain.research.activity.EventEdition;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class CreateEventEditionDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("party", getLoggedPerson(request));
	return mapping.findForward("CreateEventEdition");
    }

    public ActionForward prepareEventEditionSearch(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	ResearchEventEditionCreationBean bean = getEventEditionBean(request);
	if (bean == null) {
	    bean = new ResearchEventEditionCreationBean();
	}

	request.setAttribute("eventEditionBean", bean);
	return prepare(mapping, form, request, response);
    }

    public ActionForward invalidCreate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	ResearchEventEditionCreationBean bean = (ResearchEventEditionCreationBean) getEventEditionBean(request);
	request.setAttribute("createNewEvent", "true");
	request.setAttribute("eventEditionBean", bean);
	return prepare(mapping, form, request, response);
    }

    public ActionForward prepareCreateEventEditionParticipation(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	ResearchEventEditionCreationBean bean = (ResearchEventEditionCreationBean) getEventEditionBean(request);
	if (bean == null) {
	    return prepareEventEditionSearch(mapping, form, request, response);
	}
	if (bean.getEventEdition() != null) {
	    return createExistentEventEditionParticipation(mapping, form, request, response);
	}
	if (request.getParameter("prepareCreateEvent") != null) {
	    request.setAttribute("createNewEvent", "true");
	}
	if (request.getParameter("newEvent") != null) {
	    return createInexistentEventEditionParticipation(mapping, form, request, response);
	}
	RenderUtils.invalidateViewState();
	request.setAttribute("eventEditionBean", bean);
	return prepare(mapping, form, request, response);

    }

    public ActionForward createInexistentEventEditionParticipation(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	ResearchEventEditionCreationBean bean = (ResearchEventEditionCreationBean) getEventEditionBean(request);

	try {
	    Event event = (Event) executeService("CreateResearchEvent", new Object[] { bean.getEventName(),
		    bean.getEventType(), bean.getLocationType(), bean.getUrl() });
	    EventEdition edition = (EventEdition) executeService("CreateResearchEventEdition", new Object[] {
		    event, bean });
	    bean.setEvent(event);
	    bean.setEventEdition(edition);
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage(), null);
	    return prepare(mapping, form, request, response);
	} 

	RenderUtils.invalidateViewState();
	request.setAttribute("eventEditionBean", bean);
	return prepare(mapping, form, request, response);
    }

    public ActionForward createExistentEventEditionParticipation(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	Person person = getLoggedPerson(request);
	ResearchEventEditionCreationBean bean = (ResearchEventEditionCreationBean) getEventEditionBean(request);
	if (bean == null)
	    return prepareEventEditionSearch(mapping, form, request, response);

	if (bean.getEditionRole() != null) {
	    try {
		executeService(request, "CreateResearchActivityParticipation", new Object[] {
			bean.getEventEdition(), bean.getEditionRole(), person });
	    } catch (DomainException e) {
		addActionMessage(request, e.getMessage(), null);
		request.setAttribute("existentEventBean", bean);
		request.setAttribute("eventCreationSchema", "eventCreation.existentEvent");
		return prepare(mapping, form, request, response);
	    }
	}

	return mapping.findForward("Success");
    }

    public ResearchEventEditionCreationBean getEventEditionBean(HttpServletRequest request) {
	ResearchEventEditionCreationBean bean = null;
	if (RenderUtils.getViewState() != null) {
	    bean = (ResearchEventEditionCreationBean) RenderUtils.getViewState().getMetaObject().getObject();
	    return bean;
	}
	return bean;
    }
}
