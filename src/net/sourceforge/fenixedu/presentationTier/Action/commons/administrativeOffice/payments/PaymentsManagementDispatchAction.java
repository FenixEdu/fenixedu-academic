/*
 * Created on Jun 26, 2006
 */
package net.sourceforge.fenixedu.presentationTier.Action.commons.administrativeOffice.payments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.accounting.PaymentsManagementDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.SelectableEntryBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.PaymentMode;
import net.sourceforge.fenixedu.domain.accounting.Receipt;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public abstract class PaymentsManagementDispatchAction extends FenixDispatchAction {

    protected PaymentsManagementDTO searchNotPayedEventsForPerson(HttpServletRequest request, Person person) {

	final PaymentsManagementDTO paymentsManagementDTO = new PaymentsManagementDTO(person);
	for (final Event event : person.getNotPayedEventsPayableOn(getAdministrativeOffice(request))) {
	    paymentsManagementDTO.addEntryDTOs(event.calculateEntries());
	}

	return paymentsManagementDTO;
    }

    public ActionForward showEvents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	try {
	    request.setAttribute("paymentsManagementDTO", searchNotPayedEventsForPerson(request, getPerson(request)));

	} catch (DomainException e) {
	    addActionMessage(request, e.getKey(), e.getArgs());
	    return showOperations(mapping, form, request, response);
	}

	return mapping.findForward("showEvents");
    }

    protected List<SelectableEntryBean> getSelectableEntryBeans(final Set<Entry> entries, final Collection<Entry> entriesToSelect) {
	final List<SelectableEntryBean> selectableEntryBeans = new ArrayList<SelectableEntryBean>();
	for (final Entry entry : entries) {
	    selectableEntryBeans.add(new SelectableEntryBean(entriesToSelect.contains(entry) ? true : false, entry));
	}
	return selectableEntryBeans;
    }

    public ActionForward preparePayment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final PaymentsManagementDTO paymentsManagementDTO = (PaymentsManagementDTO) RenderUtils.getViewState(
		"paymentsManagementDTO").getMetaObject().getObject();

	paymentsManagementDTO.setPaymentDate(new DateTime());

	request.setAttribute("paymentsManagementDTO", paymentsManagementDTO);

	if (paymentsManagementDTO.getSelectedEntries().isEmpty()) {
	    addActionMessage("context", request, "error.payments.payment.entries.selection.is.required");
	    return mapping.findForward("showEvents");

	} else {
	    return mapping.findForward("preparePayment");
	}
    }

    public ActionForward preparePaymentUsingContributorPartyPostback(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	final PaymentsManagementDTO paymentsManagementDTO = (PaymentsManagementDTO) getObjectFromViewState("paymentsManagementDTO-edit");

	RenderUtils.invalidateViewState("paymentsManagementDTO-edit");

	paymentsManagementDTO.setContributorParty(null);
	paymentsManagementDTO.setContributorNumber(null);
	paymentsManagementDTO.setContributorName(null);

	request.setAttribute("paymentsManagementDTO", paymentsManagementDTO);

	return mapping.findForward("preparePayment");
    }

    @SuppressWarnings("unchecked")
    public ActionForward doPayment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final PaymentsManagementDTO paymentsManagementDTO = (PaymentsManagementDTO) RenderUtils.getViewState(
		"paymentsManagementDTO-edit").getMetaObject().getObject();

	if (paymentsManagementDTO.getSelectedEntries().isEmpty()) {

	    addActionMessage("context", request, "error.payments.payment.entries.selection.is.required");
	    request.setAttribute("paymentsManagementDTO", paymentsManagementDTO);

	    return mapping.findForward("preparePayment");
	}

	try {
	    final Receipt receipt = (Receipt) executeService("CreatePaymentsForEvents", new Object[] {
		    getUserView(request).getPerson().getUser(), paymentsManagementDTO.getSelectedEntries(), PaymentMode.CASH,
		    paymentsManagementDTO.isDifferedPayment(), paymentsManagementDTO.getPaymentDate(),
		    paymentsManagementDTO.getPerson(), paymentsManagementDTO.getContributorParty(),
		    paymentsManagementDTO.getContributorName(), getReceiptCreatorUnit(request), getReceiptOwnerUnit(request) });

	    request.setAttribute("personId", paymentsManagementDTO.getPerson().getIdInternal());
	    request.setAttribute("receiptID", receipt.getIdInternal());

	    return mapping.findForward("showReceipt");

	} catch (DomainExceptionWithLabelFormatter ex) {
	    addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex.getLabelFormatterArgs()));
	    request.setAttribute("paymentsManagementDTO", paymentsManagementDTO);
	    return mapping.findForward("preparePayment");
	} catch (DomainException ex) {

	    addActionMessage(request, ex.getKey(), ex.getArgs());
	    request.setAttribute("paymentsManagementDTO", paymentsManagementDTO);
	    return mapping.findForward("preparePayment");
	}

    }

    protected Person getPerson(HttpServletRequest request) {
	return (Person) rootDomainObject.readPartyByOID(getIntegerFromRequest(request, "personId"));
    }

    protected Unit getCurrentUnit(HttpServletRequest request) {
	return getUserView(request).getPerson().getEmployee().getCurrentWorkingPlace();
    }

    public ActionForward preparePaymentInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("paymentsManagementDTO", RenderUtils.getViewState("paymentsManagementDTO-edit").getMetaObject()
		.getObject());
	return mapping.findForward("preparePayment");
    }

    public ActionForward prepareShowEventsInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("paymentsManagementDTO", RenderUtils.getViewState("paymentsManagementDTO").getMetaObject()
		.getObject());

	return mapping.findForward("showEvents");
    }

    public ActionForward backToShowOperations(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	setContextInformation(request);
	return findMainForward(mapping);
    }

    protected void setContextInformation(HttpServletRequest request) {
	request.setAttribute("person", getPerson(request));
    }

    protected ActionForward findMainForward(final ActionMapping mapping) {
	return mapping.findForward("showOperations");
    }

    public ActionForward showOperations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("person", getPerson(request));

	return mapping.findForward("showOperations");
    }

    public ActionForward showEventsWithPayments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("person", getPerson(request));

	return mapping.findForward("showEventsWithPayments");
    }

    public ActionForward showPaymentsForEvent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("person", getPerson(request));
	request.setAttribute("event", getEvent(request));

	return mapping.findForward("showPaymentsForEvent");
    }

    protected Event getEvent(HttpServletRequest request) {
	return (Event) RootDomainObject.readDomainObjectByOID(Event.class, getIntegerFromRequest(request, "eventId"));
    }

    public ActionForward preparePrintGuide(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	return mapping.findForward("preparePrintGuide");
    }

    abstract protected AdministrativeOffice getAdministrativeOffice(final HttpServletRequest request);

    abstract protected Unit getReceiptOwnerUnit(HttpServletRequest request);

    abstract protected Unit getReceiptCreatorUnit(HttpServletRequest request);

}
