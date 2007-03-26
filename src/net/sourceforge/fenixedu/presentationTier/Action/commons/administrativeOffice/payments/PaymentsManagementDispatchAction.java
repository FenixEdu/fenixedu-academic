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
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.PaymentMode;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;

public abstract class PaymentsManagementDispatchAction extends FenixDispatchAction {

    protected PaymentsManagementDTO searchNotPayedEventsForPerson(HttpServletRequest request,
	    Person person, boolean withInstallments) {

	final PaymentsManagementDTO paymentsManagementDTO = new PaymentsManagementDTO(person);
	for (final Event event : person.getNotPayedEventsPayableOn(getAdministrativeOffice(request),
		withInstallments)) {
	    paymentsManagementDTO.addEntryDTOs(event.calculateEntries());
	}

	return paymentsManagementDTO;
    }

    public ActionForward showEvents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("paymentsManagementDTO", searchNotPayedEventsForPerson(request,
		getPerson(request), false));

	return mapping.findForward("showEvents");
    }

    public ActionForward showEventsWithInstallments(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("paymentsManagementDTO", searchNotPayedEventsForPerson(request,
		getPerson(request), true));

	return mapping.findForward("showEventsWithInstallments");
    }

    protected List<SelectableEntryBean> getSelectableEntryBeans(final Set<Entry> entries,
	    final Collection<Entry> entriesToSelect) {
	final List<SelectableEntryBean> selectableEntryBeans = new ArrayList<SelectableEntryBean>();
	for (final Entry entry : entries) {
	    selectableEntryBeans.add(new SelectableEntryBean(entriesToSelect.contains(entry) ? true
		    : false, entry));
	}
	return selectableEntryBeans;
    }

    public ActionForward preparePayment(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	return internalPreparePayment(mapping, request, "showEvents");

    }

    public ActionForward preparePaymentWithInstallments(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	return internalPreparePayment(mapping, request, "showEventsWithInstallments");

    }

    private ActionForward internalPreparePayment(ActionMapping mapping, HttpServletRequest request,
	    String errorForward) {
	final PaymentsManagementDTO paymentsManagementDTO = (PaymentsManagementDTO) RenderUtils
		.getViewState("paymentsManagementDTO").getMetaObject().getObject();

	paymentsManagementDTO.setPaymentDate(new DateTime());

	request.setAttribute("paymentsManagementDTO", paymentsManagementDTO);

	if (paymentsManagementDTO.getSelectedEntries().isEmpty()) {
	    addActionMessage("context", request, "error.payments.payment.entries.selection.is.required");
	    return mapping.findForward(errorForward);

	} else {
	    return mapping.findForward("preparePayment");
	}
    }

    public ActionForward doPayment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final PaymentsManagementDTO paymentsManagementDTO = (PaymentsManagementDTO) RenderUtils
		.getViewState("paymentsManagementDTO-edit").getMetaObject().getObject();

	if (paymentsManagementDTO.getSelectedEntries().isEmpty()) {

	    addActionMessage("context", request, "error.payments.payment.entries.selection.is.required");
	    request.setAttribute("paymentsManagementDTO", paymentsManagementDTO);

	    return mapping.findForward("showEvents");
	}

	try {
	    final Collection<Entry> resultingEntries = (Collection<Entry>) ServiceUtils.executeService(
		    getUserView(request), "CreatePaymentsForEvents", new Object[] {
			    getUserView(request).getPerson().getUser(),
			    paymentsManagementDTO.getSelectedEntries(), PaymentMode.CASH,
			    paymentsManagementDTO.isDifferedPayment(),
			    paymentsManagementDTO.getPaymentDate() });

	    request.setAttribute("person", paymentsManagementDTO.getPerson());
	    request.setAttribute("entriesToSelect", resultingEntries);

	    return mapping.findForward("paymentConfirmed");

	} catch (DomainExceptionWithLabelFormatter ex) {
	    addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex
		    .getLabelFormatterArgs()));
	    request.setAttribute("paymentsManagementDTO", paymentsManagementDTO);
	    return mapping.findForward("showEvents");
	} catch (DomainException ex) {

	    addActionMessage(request, ex.getKey(), ex.getArgs());
	    request.setAttribute("paymentsManagementDTO", paymentsManagementDTO);
	    return mapping.findForward("showEvents");
	}

    }

    protected Person getPerson(HttpServletRequest request) {
	return (Person) rootDomainObject.readPartyByOID(getIntegerFromRequest(request, "personId"));
    }

    protected Unit getCurrentUnit(HttpServletRequest request) {
	return getUserView(request).getPerson().getEmployee().getCurrentWorkingPlace();
    }

    public ActionForward preparePaymentInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("paymentsManagementDTO", RenderUtils.getViewState(
		"paymentsManagementDTO-edit").getMetaObject().getObject());
	return mapping.findForward("preparePayment");
    }

    public ActionForward prepareShowEventsInvalid(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("paymentsManagementDTO", RenderUtils.getViewState("paymentsManagementDTO")
		.getMetaObject().getObject());

	return mapping.findForward("showEvents");
    }

    public ActionForward prepareShowEventsWithInstallmentsInvalid(ActionMapping mapping,
	    ActionForm form, HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("paymentsManagementDTO", RenderUtils.getViewState("paymentsManagementDTO")
		.getMetaObject().getObject());

	return mapping.findForward("showEventsWithInstallments");
    }

    public ActionForward backToShowOperations(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	setContextInformation(request);
	return findMainForward(mapping);
    }

    protected void setContextInformation(HttpServletRequest request) {
	request.setAttribute("person", getPerson(request));
    }

    protected ActionForward findMainForward(final ActionMapping mapping) {
	return mapping.findForward("showOperations");
    }

    public ActionForward showOperations(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("person", getPerson(request));

	return mapping.findForward("showOperations");
    }

    public ActionForward showEventsWithPayments(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("person", getPerson(request));

	return mapping.findForward("showEventsWithPayments");
    }

    protected Event getEvent(HttpServletRequest request) {
	return rootDomainObject.readEventByOID(getIntegerFromRequest(request, "eventId"));
    }

    public ActionForward preparePrintGuide(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	return mapping.findForward("preparePrintGuide");
    }

    public ActionForward preparePrintGuideWithInstallments(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	return mapping.findForward("preparePrintGuideWithInstallments");
    }

    abstract protected AdministrativeOffice getAdministrativeOffice(final HttpServletRequest request);

}
