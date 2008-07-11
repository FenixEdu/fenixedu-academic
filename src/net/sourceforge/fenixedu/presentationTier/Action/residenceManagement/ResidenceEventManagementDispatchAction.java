package net.sourceforge.fenixedu.presentationTier.Action.residenceManagement;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.residenceManagement.ImportResidenceEventBean;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ResidenceEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Forward;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Forwards;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Mapping;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@Mapping(path = "/residenceEventManagement", module = "residenceManagement")
@Forwards( { @Forward(name = "manageResidenceEvents", path = "residenceManagement-events-management"),
	@Forward(name = "viewPersonResidenceEvents", path = "view-person-residence-events") })
public class ResidenceEventManagementDispatchAction extends FenixDispatchAction {

    public ActionForward manageResidenceEvents(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	ImportResidenceEventBean importResidenceEventBean = (ImportResidenceEventBean) getRenderedObject("searchEventMonth");
	if (importResidenceEventBean == null) {
	    importResidenceEventBean = new ImportResidenceEventBean();
	}

	RenderUtils.invalidateViewState();
	request.setAttribute("searchBean", importResidenceEventBean);
	return mapping.findForward("manageResidenceEvents");
    }
    
    public ActionForward generatePaymentCodes(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	long oid = Long.valueOf(request.getParameter("selectedMonth"));
	ResidenceMonth month = (ResidenceMonth) DomainObject.fromOID(oid);
	executeService("CreateResidencePaymentCodes", new Object[] { month.getEvents() });

	return manageResidenceEvents(mapping, actionForm, request, response);
    }    

    public ActionForward viewPersonResidenceEvents(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	long personOID = Long.parseLong(request.getParameter("person"));
	long monthOID = Long.parseLong(request.getParameter("month"));

	Person person = (Person) DomainObject.fromOID(personOID);
	Set<ResidenceEvent> events = (Set<ResidenceEvent>) person.getEventsByEventType(EventType.RESIDENCE_PAYMENT);

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
}
