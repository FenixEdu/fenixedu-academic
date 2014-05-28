/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.payments;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sourceforge.fenixedu.applicationTier.Servico.accounting.CreateReceipt;
import net.sourceforge.fenixedu.applicationTier.Servico.accounting.EditReceipt;
import net.sourceforge.fenixedu.applicationTier.Servico.accounting.RegisterReceiptPrint;
import net.sourceforge.fenixedu.dataTransferObject.accounting.CreateReceiptBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.Receipt;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.documents.ReceiptGeneratedDocument;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartySocialSecurityNumber;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.SearchForStudentsDA;
import net.sourceforge.fenixedu.presentationTier.docs.accounting.ReceiptDocument;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;
import net.sourceforge.fenixedu.util.report.ReportsUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/receipts", module = "academicAdministration", formBeanClass = FenixActionForm.class,
        functionality = SearchForStudentsDA.class)
@Forwards({
        @Forward(name = "showReceipts", path = "/academicAdminOffice/payments/receipts/showReceipts.jsp"),
        @Forward(name = "showReceipt", path = "/academicAdminOffice/payments/receipts/showReceipt.jsp"),
        @Forward(name = "showPaymentsWithoutReceipt",
                path = "/academicAdminOffice/payments/receipts/showPaymentsWithoutReceipt.jsp"),
        @Forward(name = "confirmCreateReceipt", path = "/academicAdminOffice/payments/receipts/confirmCreateReceipt.jsp"),
        @Forward(name = "showOperations", path = "/academicAdministration/payments.do?method=showOperations"),
        @Forward(name = "editReceipt", path = "/academicAdminOffice/payments/receipts/editReceipt.jsp") })
public class ReceiptsManagementDA extends PaymentsManagementDispatchAction {

    public static class EditReceiptBean implements Serializable {
        /**
        * 
        */
        private static final long serialVersionUID = -1140016139503995375L;

        private Receipt receipt;

        private Party contributorParty;

        private Person responsible;

        private String contributorNumber;

        private String contributorName;

        private boolean usingContributorParty;

        public EditReceiptBean(final Receipt receipt, final Person responsible) {
            setReceipt(receipt);
            setResponsible(responsible);
            setUsingContributorParty(true);
        }

        public Receipt getReceipt() {
            return this.receipt;
        }

        public void setReceipt(Receipt receipt) {
            this.receipt = receipt;
        }

        public Party getContributorParty() {
            return (this.contributorParty != null) ? this.contributorParty : StringUtils.isEmpty(this.contributorNumber) ? null : Party
                    .readByContributorNumber(this.contributorNumber);
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

        public Person getResponsible() {
            return responsible;
        }

        public void setResponsible(Person responsible) {
            this.responsible = responsible;
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

    @SuppressWarnings("unchecked")
    public ActionForward showPaymentsWithoutReceipt(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final Person person = getPerson(request);
        final CreateReceiptBean receiptBean = new CreateReceiptBean();
        IViewState viewState = RenderUtils.getViewState("entriesToSelect");
        final Collection<Entry> entriesToSelect =
                (Collection<Entry>) ((viewState != null) ? viewState.getMetaObject().getObject() : null);

        receiptBean.setPerson(person);
        receiptBean.setEntries(getSelectableEntryBeans(
                person.getPaymentsWithoutReceiptByAdministrativeOffices(getAdministrativeOffices()),
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

        final CreateReceiptBean createReceiptBean =
                (CreateReceiptBean) RenderUtils.getViewState("createReceiptBean").getMetaObject().getObject();

        if (createReceiptBean.getSelectedEntries().isEmpty()) {
            addActionMessage("context", request, "error.payments.receipt.entries.selection.is.required");

            request.setAttribute("personId", createReceiptBean.getPerson().getExternalId());
            return showPaymentsWithoutReceipt(mapping, actionForm, request, response);
        }

        request.setAttribute("createReceiptBean", createReceiptBean);
        return mapping.findForward("confirmCreateReceipt");
    }

    public ActionForward createReceipt(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final CreateReceiptBean createReceiptBean =
                (CreateReceiptBean) RenderUtils.getViewState("createReceiptBeanConfirm").getMetaObject().getObject();

        //This is here to force the load of the relation to debug a possible bug in FenixFramework
        createReceiptBean.getPerson().getReceiptsSet().size();
        try {
            final Receipt receipt =
                    CreateReceipt.run(getUserView(request).getPerson(), createReceiptBean.getPerson(),
                            createReceiptBean.getContributorParty(), createReceiptBean.getContributorName(),
                            createReceiptBean.getYear(), createReceiptBean.getSelectedEntries());

            request.setAttribute("personId", receipt.getPerson().getExternalId());
            request.setAttribute("receiptID", receipt.getExternalId());

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
                getPerson(request).getReceiptsByAdministrativeOffices(getAdministrativeOffices()));

        return mapping.findForward("showReceipts");
    }

    public ActionForward prepareShowPaymentsWithoutReceiptInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("createReceiptBean", RenderUtils.getViewState("createReceiptBean").getMetaObject().getObject());
        return mapping.findForward("showPaymentsWithoutReceipt");
    }

    public ActionForward printReceipt(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws IOException, JRException {

        final Receipt receipt = getRenderedObject("receipt");
        try {

            final ReceiptDocument original = new ReceiptDocument(receipt, getMessageResourceProvider(request), true);
            final ReceiptDocument duplicate = new ReceiptDocument(receipt, getMessageResourceProvider(request), false);

            final byte[] data = ReportsUtils.exportMultipleToPdfAsByteArray(original, duplicate);

            ReceiptGeneratedDocument.store(receipt, original.getReportFileName() + ".pdf", data);

            RegisterReceiptPrint.run(receipt, getUserView(request).getPerson());

            response.setContentLength(data.length);
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", String.format("attachment; filename=%s.pdf", original.getReportFileName()));

            response.getOutputStream().write(data);

            return null;

        } catch (DomainException e) {
            addActionMessage(request, e.getKey(), e.getArgs());

            request.setAttribute("personId", receipt.getPerson().getExternalId());
            request.setAttribute("receiptID", receipt.getExternalId());

            return prepareShowReceipt(mapping, actionForm, request, response);
        }

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
        if (receipt.getPerson() != person) {
            addActionMessage("context", request, "error.payments.person.doesnot.contain.receipt");
            request.setAttribute("person", person);
            return mapping.findForward("showReceipts");
        }

        request.setAttribute("receipt", receipt);
        return mapping.findForward("showReceipt");
    }

    public ActionForward prepareEditReceipt(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("editReceiptBean", new EditReceiptBean(getReceipt(request), AccessControl.getPerson()));

        return mapping.findForward("editReceipt");
    }

    public ActionForward prepareEditReceiptInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("editReceiptBean", getObjectFromViewState("editReceiptBean"));

        return mapping.findForward("editReceipt");
    }

    public ActionForward editReceipt(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final EditReceiptBean editReceiptBean = (EditReceiptBean) getObjectFromViewState("editReceiptBean");

        try {
            EditReceipt.run(editReceiptBean.getReceipt(), editReceiptBean.getResponsible(),
                    editReceiptBean.getContributorParty(), editReceiptBean.getContributorName());
        } catch (DomainException e) {
            request.setAttribute("editReceiptBean", editReceiptBean);
            addActionMessage(request, e.getKey(), e.getArgs());
            return mapping.findForward("editReceipt");
        }

        request.setAttribute("personId", editReceiptBean.getReceipt().getPerson().getExternalId());

        return showReceipts(mapping, form, request, response);
    }

    protected Receipt getReceipt(final HttpServletRequest request) {
        return getDomainObject(request, "receiptID");
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

    protected Set<AdministrativeOffice> getAdministrativeOffices() {
        return AcademicAuthorizationGroup.getOfficesForOperation(AccessControl.getPerson(), AcademicOperationType.MANAGE_STUDENT_PAYMENTS);
    }

}