package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.payments;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.accounting.CreateCreditNoteBean;
import net.sourceforge.fenixedu.dataTransferObject.accounting.CreateOtherPartyPaymentBean;
import net.sourceforge.fenixedu.domain.accounting.CreditNote;
import net.sourceforge.fenixedu.domain.accounting.CreditNoteState;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.presentationTier.Action.commons.administrativeOffice.payments.PaymentsManagementDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * TODO: Refactor each subject to its separate class (e.g. Exemptions)
 * 
 */
public class AcademicAdminOfficePaymentsManagementDispatchAction extends
	PaymentsManagementDispatchAction {

    @Override
    protected AdministrativeOffice getAdministrativeOffice(HttpServletRequest request) {
	return getUserView(request).getPerson().getEmployee().getAdministrativeOffice();
    }

    public ActionForward showCreditNotes(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("receipt", getReceiptFromViewState("receipt"));

	return mapping.findForward("creditNotes.list");
    }

    public ActionForward showCreditNote(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("creditNote", getCreditNote(request));
	((DynaActionForm) form).set("creditNoteState", getCreditNote(request).getState().name());

	return mapping.findForward("creditNotes.show");
    }

    private CreditNote getCreditNote(HttpServletRequest request) {
	return rootDomainObject.readCreditNoteByOID(getIntegerFromRequest(request, "creditNoteId"));
    }

    public ActionForward prepareCreateCreditNote(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	request.setAttribute("createCreditNoteBean", new CreateCreditNoteBean(
		getReceiptFromViewState("receipt")));

	return mapping.findForward("creditNotes.create");

    }

    public ActionForward createCreditNote(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	final CreateCreditNoteBean createCreditNoteBean = (CreateCreditNoteBean) RenderUtils
		.getViewState("create-credit-note").getMetaObject().getObject();

	try {
	    executeService("CreateCreditNote", new Object[] {
		    getUserView(request).getPerson().getEmployee(), createCreditNoteBean });

	} catch (DomainExceptionWithLabelFormatter ex) {
	    addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex
		    .getLabelFormatterArgs()));
	    request.setAttribute("createCreditNoteBean", createCreditNoteBean);
	    return mapping.findForward("creditNotes.create");

	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey());
	    request.setAttribute("createCreditNoteBean", createCreditNoteBean);
	    return mapping.findForward("creditNotes.create");

	}

	request.setAttribute("receipt", createCreditNoteBean.getReceipt());

	return mapping.findForward("creditNotes.list");

    }

    public ActionForward changeCreditNoteState(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {
	final CreditNote creditNote = getCreditNoteFromViewState();
	final CreditNoteState creditNoteState = CreditNoteState.valueOf(((DynaActionForm) form)
		.getString("creditNoteState"));

	try {
	    executeService("ChangeCreditNoteState", new Object[] {
		    getUserView(request).getPerson().getEmployee(), creditNote, creditNoteState });

	} catch (DomainExceptionWithLabelFormatter ex) {
	    addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex
		    .getLabelFormatterArgs()));
	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey());
	}

	request.setAttribute("creditNote", creditNote);

	return mapping.findForward("creditNotes.show");

    }

    private CreditNote getCreditNoteFromViewState() {
	final CreditNote creditNote = (CreditNote) RenderUtils.getViewState("creditNote")
		.getMetaObject().getObject();
	return creditNote;
    }

    public ActionForward printCreditNote(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("creditNote", getCreditNoteFromViewState());
	request.setAttribute("currentUnit", getCurrentUnit(request));

	return mapping.findForward("creditNotes.print");
    }

    public ActionForward showEventsForOtherPartyPayment(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("person", getPerson(request));

	return mapping.findForward("otherPartyPayment.showEvents");

    }

    public ActionForward showOtherPartyPaymentsForEvent(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("event", getEvent(request));

	return mapping.findForward("otherPartyPayment.showPaymentsForEvent");
    }

    public ActionForward prepareCreateOtherPartyPayment(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	final CreateOtherPartyPaymentBean createOtherPartyPaymentBean = new CreateOtherPartyPaymentBean(
		getEvent(request));

	request.setAttribute("createOtherPartyPaymentBean", createOtherPartyPaymentBean);

	return mapping.findForward("otherPartyPayment.prepareCreate");
    }

    public ActionForward confirmCreateOtherPartyPayment(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("createOtherPartyPaymentBean",
		getCreateOtherPartyBeanFromViewState("createOtherPartyPayment"));

	return mapping.findForward("otherPartyPayment.confirmCreate");

    }

    protected Event getEvent(HttpServletRequest request) {
	return rootDomainObject.readEventByOID(getIntegerFromRequest(request, "eventId"));
    }

    public ActionForward createOtherPartyPayment(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	final CreateOtherPartyPaymentBean createOtherPartyPaymentBean = getCreateOtherPartyBeanFromViewState("confirmCreateOtherPartyPayment");

	try {
	    executeService(request, "CreateOtherPartyPayment", new Object[] {
		    getUserView(request).getPerson(), createOtherPartyPaymentBean });
	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey(), ex.getArgs());

	    request.setAttribute("createOtherPartyPaymentBean", createOtherPartyPaymentBean);

	    return mapping.findForward("otherPartyPayment.prepareCreate");
	}

	return showEventsForOtherPartyPayment(mapping, form, request, response);

    }

    private CreateOtherPartyPaymentBean getCreateOtherPartyBeanFromViewState(String name) {
	return (CreateOtherPartyPaymentBean) RenderUtils.getViewState(name).getMetaObject().getObject();
    }

    public ActionForward preparePrintGuideForOtherParty(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("createOtherPartyPayment",
		getObjectFromViewState("createOtherPartyPayment"));
	return mapping.findForward("otherPartyPayment.showGuide");
    }

    public ActionForward printGuideForOtherParty(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("currentUnit", getCurrentUnit(request));
	request.setAttribute("createOtherPartyPayment",
		getObjectFromViewState("createOtherPartyPayment"));

	return mapping.findForward("otherPartyPayment.printGuide");
    }

    public ActionForward prepareCreateOtherPartyPaymentInvalid(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("createOtherPartyPaymentBean",
		getObjectFromViewState("createOtherPartyPayment"));
	return mapping.findForward("otherPartyPayment.prepareCreate");
    }

    public ActionForward showEventsWithPayments(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("person", getPerson(request));

	return mapping.findForward("extract.showEventsWithPayments");
    }

    public ActionForward showOperations(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("person", getPerson(request));

	return mapping.findForward("showOperations");
    }

}
