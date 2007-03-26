package net.sourceforge.fenixedu.presentationTier.Action.commons.administrativeOffice.payments;

import java.util.Collection;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.accounting.CreateReceiptBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.Receipt;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public abstract class ReceiptsManagementDA extends PaymentsManagementDispatchAction {

    public ActionForward showPaymentsWithoutReceipt(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	final Person person = getPerson(request);
	final CreateReceiptBean receiptBean = new CreateReceiptBean();
	final IViewState viewState = RenderUtils.getViewState("entriesToSelect");
	final Collection<Entry> entriesToSelect = (Collection<Entry>) ((viewState != null) ? viewState
		.getMetaObject().getObject() : null);

	receiptBean.setPerson(person);
	receiptBean.setEntries(getSelectableEntryBeans(person
		.getPaymentsWithoutReceiptByAdministrativeOffice(getAdministrativeOffice(request)),
		(entriesToSelect != null) ? entriesToSelect : new HashSet<Entry>()));

	request.setAttribute("createReceiptBean", receiptBean);

	return mapping.findForward("showPaymentsWithoutReceipt");
    }

    public ActionForward confirmCreateReceipt(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	final CreateReceiptBean createReceiptBean = (CreateReceiptBean) RenderUtils.getViewState(
		"createReceiptBean").getMetaObject().getObject();

	if (createReceiptBean.getContributorParty() == null) {
	    final Party contributor = Party.readByContributorNumber(createReceiptBean
		    .getContributorNumber());
	    if (contributor == null) {
		addActionMessage("context", request, "error.payments.receipt.contributor.does.not.exist");

		request.setAttribute("personId", createReceiptBean.getPerson().getIdInternal());
		return showPaymentsWithoutReceipt(mapping, actionForm, request, response);

	    } else {
		createReceiptBean.setContributorParty(contributor);
	    }
	}

	if (createReceiptBean.getSelectedEntries().isEmpty()) {
	    addActionMessage("context", request, "error.payments.receipt.entries.selection.is.required");

	    request.setAttribute("personId", createReceiptBean.getPerson().getIdInternal());
	    return showPaymentsWithoutReceipt(mapping, actionForm, request, response);
	}

	request.setAttribute("createReceiptBean", createReceiptBean);
	return mapping.findForward("confirmCreateReceipt");
    }

    public ActionForward createReceipt(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	final CreateReceiptBean createReceiptBean = (CreateReceiptBean) RenderUtils.getViewState(
		"createReceiptBeanConfirm").getMetaObject().getObject();

	try {
	    final Receipt receipt = (Receipt) ServiceUtils.executeService(getUserView(request),
		    "CreateReceipt", new Object[] { getUserView(request).getPerson().getEmployee(),
			    createReceiptBean.getPerson(), createReceiptBean.getContributorParty(),
			    getReceiptCreatorUnit(request), getReceiptOwnerUnit(request),
			    createReceiptBean.getSelectedEntries() });

	    request.setAttribute("personId", receipt.getPerson().getIdInternal());
	    request.setAttribute("receiptID", receipt.getIdInternal());

	    return prepareShowReceipt(mapping, form, request, response);

	} catch (DomainException ex) {

	    addActionMessage(request, ex.getKey(), ex.getArgs());
	    request.setAttribute("createReceiptBean", createReceiptBean);
	    return mapping.findForward("confirmCreateReceipt");
	}
    }

    public ActionForward showReceipts(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("person", getPerson(request));
	request.setAttribute("receiptsForAdministrativeOffice", getPerson(request)
		.getReceiptsByAdministrativeOffice(getAdministrativeOffice(request)));

	return mapping.findForward("showReceipts");
    }

    public ActionForward prepareShowPaymentsWithoutReceiptInvalid(ActionMapping mapping,
	    ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("createReceiptBean", RenderUtils.getViewState("createReceiptBean")
		.getMetaObject().getObject());
	return mapping.findForward("showPaymentsWithoutReceipt");
    }

    public ActionForward printReceipt(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	final Receipt receipt = (Receipt) RenderUtils.getViewState("receipt").getMetaObject()
		.getObject();
	final SortedSet<Entry> sortedEntries = new TreeSet<Entry>(
		Entry.COMPARATOR_BY_MOST_RECENT_WHEN_REGISTERED);
	sortedEntries.addAll(receipt.getEntries());

	request.setAttribute("receipt", receipt);
	request.setAttribute("sortedEntries", sortedEntries);
	request.setAttribute("currentUnit", getCurrentUnit(request));

	try {

	    ServiceUtils.executeService(getUserView(request), "CreateReceiptPrintVersion", new Object[] {
		    receipt, getUserView(request).getPerson().getEmployee() });

	    return mapping.findForward("printReceipt");

	} catch (InvalidArgumentsServiceException e) {
	    addActionMessage(request, e.getMessage());

	    return prepareShowReceipt(mapping, actionForm, request, response);
	}

    }

    public ActionForward backToShowOperations(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	final ActionForward actionForward = new ActionForward(mapping.findForward("showOperations")
		.getPath()
		+ "&" + buildContextParameters(request));

	actionForward.setRedirect(true);

	return actionForward;
    }

    protected String buildContextParameters(final HttpServletRequest request) {
	return "personId=" + getPerson(request).getIdInternal() + "&administrativeOfficeId="
		+ getAdministrativeOffice(request).getIdInternal();
    }

    public ActionForward prepareShowReceipt(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	final Person person = getPerson(request);
	final Receipt receipt = getReceipt(request);

	if (receipt == null) {
	    addActionMessage("context", request, "error.payments.receipt.not.found");
	    request.setAttribute("person", person);
	    return mapping.findForward("showReceipts");
	}
	if (!person.getReceiptsSet().contains(receipt)) {
	    addActionMessage("context", request, "error.payments.person.doesnot.contain.receipt");
	    request.setAttribute("person", person);
	    return mapping.findForward("showReceipts");
	}

	request.setAttribute("receipt", receipt);
	return mapping.findForward("showReceipt");
    }

    protected Receipt getReceipt(final HttpServletRequest request) {
	return rootDomainObject.readReceiptByOID(getIntegerFromRequest(request, "receiptID"));
    }

    public ActionForward prepareCancelReceipt(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {

	final Receipt receipt = getReceipt(request);
	request.setAttribute("receipt", receipt);
	request.setAttribute("person", receipt.getPerson());

	return mapping.findForward("confirmCancelReceipt");
    }

    protected Receipt getReceiptFromViewState(String viewStateName) {
	final Receipt receipt = (Receipt) RenderUtils.getViewState(viewStateName).getMetaObject()
		.getObject();
	return receipt;
    }

    public ActionForward cancelReceipt(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	final Receipt receipt = getReceiptFromViewState("receiptToCancel");
	try {
	    executeService(request, "CancelReceipt", new Object[] {
		    getUserView(request).getPerson().getEmployee(), receipt });

	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey(), ex.getArgs());
	    request.setAttribute("receipt", receipt);
	    request.setAttribute("person", receipt.getPerson());

	    return mapping.findForward("confirmCancelReceipt");
	}

	request.setAttribute("personId", receipt.getPerson().getIdInternal());

	return showReceipts(mapping, form, request, response);
    }

    abstract protected Unit getReceiptOwnerUnit(HttpServletRequest request);

    abstract protected Unit getReceiptCreatorUnit(HttpServletRequest request);

}
