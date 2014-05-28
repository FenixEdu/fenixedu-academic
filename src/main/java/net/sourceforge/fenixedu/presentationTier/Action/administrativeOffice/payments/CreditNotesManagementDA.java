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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sourceforge.fenixedu.applicationTier.Servico.accounting.ChangeCreditNoteState;
import net.sourceforge.fenixedu.applicationTier.Servico.accounting.CreateCreditNote;
import net.sourceforge.fenixedu.dataTransferObject.accounting.CreateCreditNoteBean;
import net.sourceforge.fenixedu.domain.accounting.CreditNote;
import net.sourceforge.fenixedu.domain.accounting.CreditNoteState;
import net.sourceforge.fenixedu.domain.accounting.Receipt;
import net.sourceforge.fenixedu.domain.documents.CreditNoteGeneratedDocument;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.SearchForStudentsDA;
import net.sourceforge.fenixedu.presentationTier.docs.accounting.CreditNoteDocument;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;
import net.sourceforge.fenixedu.util.report.ReportsUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

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
            HttpServletResponse response) throws JRException, IOException {

        final CreditNote creditNote = getCreditNoteFromViewState();

        try {

            final CreditNoteDocument original = new CreditNoteDocument(creditNote, getMessageResourceProvider(request), true);
            final CreditNoteDocument duplicate = new CreditNoteDocument(creditNote, getMessageResourceProvider(request), false);

            final byte[] data = ReportsUtils.exportMultipleToPdfAsByteArray(original, duplicate);

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