package net.sourceforge.fenixedu.presentationTier.Action.residenceManagement;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.residenceManagement.ImportResidenceEventBean;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.ResidenceEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.residence.ResidenceMonth;
import net.sourceforge.fenixedu.domain.residence.ResidenceYear;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/residenceEventManagement", module = "residenceManagement")
@Forwards( { @Forward(name = "manageResidenceEvents", path = "residenceManagement-events-management"),
	@Forward(name = "viewPersonResidenceEvents", path = "view-person-residence-events") })
public class ResidenceEventManagementDispatchAction extends FenixDispatchAction {

    public ActionForward manageResidenceEvents(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	ImportResidenceEventBean importResidenceEventBean = (ImportResidenceEventBean) getRenderedObject("searchEventMonth");
	if (importResidenceEventBean == null) {
	    ResidenceMonth month = getResidenceMonth(request);
	    importResidenceEventBean = month != null ? new ImportResidenceEventBean(month) : new ImportResidenceEventBean();
	}

	RenderUtils.invalidateViewState();

	request.setAttribute("currentResidence", getLoggedPerson(request).getEmployee().getCurrentWorkingPlace());
	request.setAttribute("searchBean", importResidenceEventBean);
	return mapping.findForward("manageResidenceEvents");
    }

    public ActionForward generatePaymentCodes(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	ResidenceMonth month = getResidenceMonth(request);
	executeService("CreateResidencePaymentCodes", new Object[] { month.getEvents() });

	return manageResidenceEvents(mapping, actionForm, request, response);
    }

    private ResidenceMonth getResidenceMonth(HttpServletRequest request) {
	String oid = request.getParameter("monthOID");
	return oid == null ? null : (ResidenceMonth) DomainObject.fromOID(Long.valueOf(oid));
    }

    public ActionForward viewPersonResidenceEvents(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	ResidenceMonth month = getResidenceMonth(request);
	long personOID = Long.parseLong(request.getParameter("person"));

	Person person = (Person) DomainObject.fromOID(personOID);
	Set<Event> events = person.getNotPayedEventsPayableOn(null, ResidenceEvent.class, false);
	events.addAll(person.getNotPayedEventsPayableOn(null, ResidenceEvent.class, true));
	events.addAll(person.getPayedEvents(ResidenceEvent.class));

	request.setAttribute("month", month);
	request.setAttribute("person", person);
	request.setAttribute("residenceEvents", events);
	return mapping.findForward("viewPersonResidenceEvents");
    }

    public ActionForward cancelResidenceEvent(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	ResidenceEvent residenceEvent = (ResidenceEvent) DomainObject.fromOID(Long.parseLong(request.getParameter("event")));

	try {
	    executeService("CancelResidenceEvent", new Object[] { residenceEvent, AccessControl.getPerson().getEmployee() });
	} catch (DomainException e) {
	    addErrorMessage(request, e.getMessage(), e.getMessage(), null);
	}

	return viewPersonResidenceEvents(mapping, actionForm, request, response);
    }

    public ActionForward payResidenceEvent(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	ResidenceEvent residenceEvent = (ResidenceEvent) DomainObject.fromOID(Long.parseLong(request.getParameter("event")));

	try {
	    executeService("PayResidenceEvent", new Object[] { getLoggedPerson(request).getUser(), residenceEvent });
	} catch (DomainException e) {
	    addErrorMessage(request, e.getMessage(), e.getMessage(), null);
	}

	return viewPersonResidenceEvents(mapping, actionForm, request, response);
    }

}
