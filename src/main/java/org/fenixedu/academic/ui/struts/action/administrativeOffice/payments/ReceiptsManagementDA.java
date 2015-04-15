/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.administrativeOffice.payments;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.accounting.Entry;
import org.fenixedu.academic.domain.accounting.Receipt;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.documents.ReceiptGeneratedDocument;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.Party;
import org.fenixedu.academic.domain.organizationalStructure.PartySocialSecurityNumber;
import org.fenixedu.academic.dto.accounting.CreateReceiptBean;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.report.accounting.ReceiptDocument;
import org.fenixedu.academic.service.services.accounting.CreateReceipt;
import org.fenixedu.academic.service.services.accounting.EditReceipt;
import org.fenixedu.academic.service.services.accounting.RegisterReceiptPrint;
import org.fenixedu.academic.ui.struts.FenixActionForm;
import org.fenixedu.academic.ui.struts.action.administrativeOffice.student.SearchForStudentsDA;
import org.fenixedu.academic.util.report.ReportsUtils;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

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

        private String contributorAddress;

        private boolean usingContributorParty;

        public EditReceiptBean(final Receipt receipt, final Person responsible) {
            setReceipt(receipt);
            setResponsible(responsible);
            setContributorAddress(receipt.getContributorAddress());
            setContributorName(receipt.getContributorName());
            setContributorNumber(receipt.getContributorNumber());
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
            if (contributorParty != null) {
                this.contributorName = contributorParty.getName();
                this.contributorNumber = contributorParty.getSocialSecurityNumber();
                this.contributorAddress =
                        contributorParty.getAddress()
                                + (!StringUtils.isEmpty(contributorParty.getAreaCode()) ? contributorParty.getAreaCode() + " "
                                        + contributorParty.getAreaOfAreaCode() : null);
            }
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

        public String getContributorAddress() {
            return contributorAddress;
        }

        public void setContributorAddress(String contributorAddress) {
            this.contributorAddress = contributorAddress;
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
                            createReceiptBean.getContributorName(), createReceiptBean.getContributorNumber(),
                            createReceiptBean.getContributorAddress(), createReceiptBean.getYear(),
                            createReceiptBean.getSelectedEntries());

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
            HttpServletResponse response) throws IOException {

        final Receipt receipt = getRenderedObject("receipt");
        try {

            final ReceiptDocument original = new ReceiptDocument(receipt, true);
            final ReceiptDocument duplicate = new ReceiptDocument(receipt, false);

            final byte[] data = ReportsUtils.generateReport(original, duplicate).getData();

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
            EditReceipt.run(editReceiptBean.getReceipt(), editReceiptBean.getResponsible(), editReceiptBean.getContributorName(),
                    editReceiptBean.getContributorNumber(), editReceiptBean.getContributorAddress());
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
        return AcademicAccessRule.getOfficesAccessibleToFunction(AcademicOperationType.MANAGE_STUDENT_PAYMENTS,
                Authenticate.getUser()).collect(Collectors.toSet());
    }

}