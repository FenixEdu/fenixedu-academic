package net.sourceforge.fenixedu.presentationTier.Action.commons.administrativeOffice.payments;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.accounting.EditReceipt;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.accounting.CreateReceiptBean;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.Receipt;
import net.sourceforge.fenixedu.domain.documents.ReceiptGeneratedDocument;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartySocialSecurityNumber;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.docs.accounting.ReceiptDocument;
import net.sourceforge.fenixedu.util.report.ReportsUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Forwards({
	@Forward(name = "showReceipts", path = "/academicAdminOffice/payments/receipts/showReceipts.jsp", tileProperties = @Tile(title = "private.academicadministrativeoffice.studentoperations.viewstudents")),
	@Forward(name = "showReceipt", path = "/academicAdminOffice/payments/receipts/showReceipt.jsp", tileProperties = @Tile(title = "private.academicadministrativeoffice.studentoperations.viewstudents")),
	@Forward(name = "showPaymentsWithoutReceipt", path = "/academicAdminOffice/payments/receipts/showPaymentsWithoutReceipt.jsp", tileProperties = @Tile(title = "private.academicadministrativeoffice.studentoperations.viewstudents")),
	@Forward(name = "confirmCreateReceipt", path = "/academicAdminOffice/payments/receipts/confirmCreateReceipt.jsp", tileProperties = @Tile(title = "private.academicadministrativeoffice.studentoperations.viewstudents")),
	@Forward(name = "editReceipt", path = "/academicAdminOffice/payments/receipts/editReceipt.jsp", tileProperties = @Tile(title = "private.academicadministrativeoffice.studentoperations.viewstudents")) })
public abstract class ReceiptsManagementDA extends PaymentsManagementDispatchAction {

    public static class EditReceiptBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1140016139503995375L;

	private Receipt receipt;

	private Party contributorParty;

	private Employee employee;

	private String contributorNumber;

	private String contributorName;

	private boolean usingContributorParty;

	public EditReceiptBean(final Receipt receipt, final Employee employee) {
	    setReceipt(receipt);
	    setEmployee(employee);
	    setUsingContributorParty(true);
	}

	public Receipt getReceipt() {
	    return this.receipt;
	}

	public void setReceipt(Receipt receipt) {
	    this.receipt = receipt;
	}

	public Party getContributorParty() {
	    return (this.contributorParty != null) ? this.contributorParty : StringUtils.isEmpty(this.contributorNumber) ? null
		    : Party.readByContributorNumber(this.contributorNumber);
	}

	public void setContributorParty(Party contributorParty) {
	    this.contributorParty = contributorParty;
	}

	public void setContributorPartySocialSecurityNumber(PartySocialSecurityNumber partySocialSecurityNumber) {
	    this.contributorParty = (partySocialSecurityNumber != null) ? partySocialSecurityNumber.getParty() : null;
	}

	public PartySocialSecurityNumber getContributorPartySocialSecurityNumber() {
	    return this.contributorParty != null ? this.contributorParty.getPartySocialSecurityNumber() : null;
	}

	public Employee getEmployee() {
	    return this.employee;
	}

	public void setEmployee(Employee employee) {
	    this.employee = employee;
	}

	public String getContributorNumber() {
	    return contributorNumber;
	}

	public void setContributorNumber(String contributorNumber) {
	    this.contributorNumber = contributorNumber;
	}

	public String getContributorName() {
	    return contributorName;
	}

	public void setContributorName(String contributorName) {
	    this.contributorName = contributorName;
	}

	public boolean isUsingContributorParty() {
	    return usingContributorParty;
	}

	public void setUsingContributorParty(boolean usingContributorParty) {
	    this.usingContributorParty = usingContributorParty;
	}

    }

    public ActionForward showPaymentsWithoutReceipt(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final Person person = getPerson(request);
	final CreateReceiptBean receiptBean = new CreateReceiptBean();
	IViewState viewState = RenderUtils.getViewState("entriesToSelect");
	final Collection<Entry> entriesToSelect = (Collection<Entry>) ((viewState != null) ? viewState.getMetaObject()
		.getObject() : null);

	receiptBean.setPerson(person);
	receiptBean.setEntries(getSelectableEntryBeans(
		person.getPaymentsWithoutReceiptByAdministrativeOffice(getAdministrativeOffice(request)),
		(entriesToSelect != null) ? entriesToSelect : new HashSet<Entry>()));

	request.setAttribute("createReceiptBean", receiptBean);

	return mapping.findForward("showPaymentsWithoutReceipt");
    }

    public ActionForward backToShowPaymentsWithoutReceipt(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("createReceiptBean", getRenderedObject("createReceiptBeanConfirm"));

	return mapping.findForward("showPaymentsWithoutReceipt");
    }

    public ActionForward confirmCreateReceipt(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final CreateReceiptBean createReceiptBean = (CreateReceiptBean) RenderUtils.getViewState("createReceiptBean")
		.getMetaObject().getObject();

	if (createReceiptBean.getSelectedEntries().isEmpty()) {
	    addActionMessage("context", request, "error.payments.receipt.entries.selection.is.required");

	    request.setAttribute("personId", createReceiptBean.getPerson().getIdInternal());
	    return showPaymentsWithoutReceipt(mapping, actionForm, request, response);
	}

	request.setAttribute("createReceiptBean", createReceiptBean);
	return mapping.findForward("confirmCreateReceipt");
    }

    public ActionForward createReceipt(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final CreateReceiptBean createReceiptBean = (CreateReceiptBean) RenderUtils.getViewState("createReceiptBeanConfirm")
		.getMetaObject().getObject();

	try {
	    final Receipt receipt = (Receipt) executeService(
		    "CreateReceipt",
		    new Object[] { getUserView(request).getPerson().getEmployee(), createReceiptBean.getPerson(),
			    createReceiptBean.getContributorParty(), createReceiptBean.getContributorName(),
			    createReceiptBean.getYear(), getReceiptCreatorUnit(request), getReceiptOwnerUnit(request),
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

    public ActionForward showReceipts(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("person", getPerson(request));
	request.setAttribute("receiptsForAdministrativeOffice",
		getPerson(request).getReceiptsByAdministrativeOffice(getAdministrativeOffice(request)));

	return mapping.findForward("showReceipts");
    }

    public ActionForward prepareShowPaymentsWithoutReceiptInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("createReceiptBean", RenderUtils.getViewState("createReceiptBean").getMetaObject().getObject());
	return mapping.findForward("showPaymentsWithoutReceipt");
    }

    public ActionForward printReceipt(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException, IOException, JRException {

	final Receipt receipt = getRenderedObject("receipt");
	try {

	    final ReceiptDocument original = new ReceiptDocument(receipt, getMessageResourceProvider(request), true);
	    final ReceiptDocument duplicate = new ReceiptDocument(receipt, getMessageResourceProvider(request), false);

	    final byte[] data = ReportsUtils.exportMultipleToPdfAsByteArray(original, duplicate);

	    ReceiptGeneratedDocument.store(receipt, original.getReportFileName() + ".pdf", data);
	    executeService("RegisterReceiptPrint", new Object[] { receipt, getUserView(request).getPerson().getEmployee() });

	    response.setContentLength(data.length);
	    response.setContentType("application/pdf");
	    response.addHeader("Content-Disposition", String.format("attachment; filename=%s.pdf", original.getReportFileName()));

	    response.getOutputStream().write(data);

	    return null;

	} catch (DomainException e) {
	    addActionMessage(request, e.getKey(), e.getArgs());

	    request.setAttribute("personId", receipt.getPerson().getIdInternal());
	    request.setAttribute("receiptID", receipt.getIdInternal());

	    return prepareShowReceipt(mapping, actionForm, request, response);
	}

    }

    protected String buildContextParameters(final HttpServletRequest request) {
	return "personId=" + getPerson(request).getIdInternal() + "&administrativeOfficeId="
		+ getAdministrativeOffice(request).getIdInternal();
    }

    public ActionForward prepareShowReceipt(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

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

    public ActionForward prepareEditReceipt(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("editReceiptBean", new EditReceiptBean(getReceipt(request), AccessControl.getPerson().getEmployee()));

	return mapping.findForward("editReceipt");
    }

    public ActionForward prepareEditReceiptInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("editReceiptBean", getObjectFromViewState("editReceiptBean"));

	return mapping.findForward("editReceipt");
    }

    public ActionForward editReceipt(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final EditReceiptBean editReceiptBean = (EditReceiptBean) getObjectFromViewState("editReceiptBean");

	try {
	    EditReceipt.run(editReceiptBean.getReceipt(), editReceiptBean.getEmployee(), editReceiptBean.getContributorParty(),
		    editReceiptBean.getContributorName());
	} catch (DomainException e) {
	    request.setAttribute("editReceiptBean", editReceiptBean);
	    addActionMessage(request, e.getKey(), e.getArgs());
	    return mapping.findForward("editReceipt");
	}

	request.setAttribute("personId", editReceiptBean.getReceipt().getPerson().getIdInternal());

	return showReceipts(mapping, form, request, response);
    }

    protected Receipt getReceipt(final HttpServletRequest request) {
	return rootDomainObject.readReceiptByOID(getIntegerFromRequest(request, "receiptID"));
    }

    protected Receipt getReceiptFromViewState(String viewStateName) {
	return (Receipt) RenderUtils.getViewState(viewStateName).getMetaObject().getObject();
    }

    public ActionForward editUsingContributorPartyPostback(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final EditReceiptBean editReceiptBean = (EditReceiptBean) getObjectFromViewState("editReceiptBean");

	RenderUtils.invalidateViewState("editReceiptBean");

	editReceiptBean.setContributorParty(null);
	editReceiptBean.setContributorNumber(null);
	editReceiptBean.setContributorName(null);

	request.setAttribute("editReceiptBean", editReceiptBean);

	return mapping.findForward("editReceipt");
    }

    public ActionForward createUsingContributorPartyPostback(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final CreateReceiptBean createReceiptBean = (CreateReceiptBean) getObjectFromViewState("createReceiptBean");

	RenderUtils.invalidateViewState("createReceiptBean");

	createReceiptBean.setContributorParty(null);
	createReceiptBean.setContributorNumber(null);
	createReceiptBean.setContributorName(null);

	request.setAttribute("createReceiptBean", createReceiptBean);

	return mapping.findForward("showPaymentsWithoutReceipt");
    }

}