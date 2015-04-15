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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.accounting.CreditNote;
import org.fenixedu.academic.domain.accounting.CreditNoteState;
import org.fenixedu.academic.domain.accounting.Receipt;
import org.fenixedu.academic.domain.documents.CreditNoteGeneratedDocument;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.exceptions.DomainExceptionWithLabelFormatter;
import org.fenixedu.academic.dto.accounting.CreateCreditNoteBean;
import org.fenixedu.academic.report.accounting.CreditNoteDocument;
import org.fenixedu.academic.service.services.accounting.ChangeCreditNoteState;
import org.fenixedu.academic.service.services.accounting.CreateCreditNote;
import org.fenixedu.academic.ui.struts.FenixActionForm;
import org.fenixedu.academic.ui.struts.action.administrativeOffice.student.SearchForStudentsDA;
import org.fenixedu.academic.util.report.ReportsUtils;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@Mapping(path = "/creditNotes", module = "academicAdministration",
        formBeanClass = CreditNotesManagementDA.CreditNotesActionForm.class, functionality = SearchForStudentsDA.class)
@Forwards({ @Forward(name = "list", path = "/academicAdminOffice/payments/creditNotes/listCreditNotes.jsp"),
        @Forward(name = "create", path = "/academicAdminOffice/payments/creditNotes/createCreditNote.jsp"),
        @Forward(name = "show", path = "/academicAdminOffice/payments/creditNotes/showCreditNote.jsp"),
        @Forward(name = "prepareShowReceipt", path = "/academicAdministration/receipts.do?method=prepareShowReceipt") })
public class CreditNotesManagementDA extends PaymentsManagementDispatchAction {

    public static class CreditNotesActionForm extends FenixActionForm {

        /**
	 * 
	 */
        private static final long serialVersionUID = 1L;

        private String creditNoteState;

        public String getCreditNoteState() {
            return creditNoteState;
        }

        public void setCreditNoteState(String creditNoteState) {
            this.creditNoteState = creditNoteState;
        }

    }

    public ActionForward showCreditNotes(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("receipt", getReceiptFromViewState("receipt"));

        return mapping.findForward("list");
    }

    private Receipt getReceiptFromViewState(String viewStateId) {
        return (Receipt) getObjectFromViewState(viewStateId);
    }

    public ActionForward showCreditNote(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("creditNote", getCreditNote(request));
        ((CreditNotesActionForm) form).setCreditNoteState(getCreditNote(request).getState().name());

        return mapping.findForward("show");
    }

    private CreditNote getCreditNote(HttpServletRequest request) {
        return getDomainObject(request, "creditNoteId");
    }

    public ActionForward prepareCreateCreditNote(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("createCreditNoteBean", new CreateCreditNoteBean(getReceiptFromViewState("receipt")));

        return mapping.findForward("create");

    }

    public ActionForward createCreditNote(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final CreateCreditNoteBean createCreditNoteBean =
                (CreateCreditNoteBean) RenderUtils.getViewState("create-credit-note").getMetaObject().getObject();

        try {
            CreateCreditNote.run(getUserView(request).getPerson(), createCreditNoteBean);

        } catch (DomainExceptionWithLabelFormatter ex) {
            addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex.getLabelFormatterArgs()));
            request.setAttribute("createCreditNoteBean", createCreditNoteBean);
            return mapping.findForward("create");

        } catch (DomainException ex) {
            addActionMessage(request, ex.getKey());
            request.setAttribute("createCreditNoteBean", createCreditNoteBean);
            return mapping.findForward("create");

        }

        request.setAttribute("receipt", createCreditNoteBean.getReceipt());

        return mapping.findForward("list");

    }

    public ActionForward changeCreditNoteState(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final CreditNote creditNote = getCreditNoteFromViewState();
        final CreditNoteState creditNoteState = CreditNoteState.valueOf(((CreditNotesActionForm) form).getCreditNoteState());

        try {
            ChangeCreditNoteState.run(getUserView(request).getPerson(), creditNote, creditNoteState);

        } catch (DomainExceptionWithLabelFormatter ex) {
            addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex.getLabelFormatterArgs()));
        } catch (DomainException ex) {
            addActionMessage(request, ex.getKey());
        }

        request.setAttribute("creditNote", creditNote);

        return mapping.findForward("show");

    }

    private CreditNote getCreditNoteFromViewState() {
        final CreditNote creditNote = (CreditNote) RenderUtils.getViewState("creditNote").getMetaObject().getObject();
        return creditNote;
    }

    public ActionForward printCreditNote(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        final CreditNote creditNote = getCreditNoteFromViewState();

        try {

            final CreditNoteDocument original = new CreditNoteDocument(creditNote, true);
            final CreditNoteDocument duplicate = new CreditNoteDocument(creditNote, false);

            final byte[] data = ReportsUtils.generateReport(original, duplicate).getData();

            CreditNoteGeneratedDocument.store(creditNote, original.getReportFileName() + ".pdf", data);
            response.setContentLength(data.length);
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", String.format("attachment; filename=%s.pdf", original.getReportFileName()));

            response.getOutputStream().write(data);

            return null;

        } catch (DomainException e) {
            addActionMessage(request, e.getKey(), e.getArgs());

            request.setAttribute("creditNoteId", creditNote.getExternalId());

            return showCreditNote(mapping, form, request, response);
        }

    }

    public ActionForward prepareShowReceipt(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        return mapping.findForward("prepareShowReceipt");

    }

}