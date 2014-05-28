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
/*
 * Created on Jun 26, 2006
 */
package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.payments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.accounting.CreatePaymentsForEvents;
import net.sourceforge.fenixedu.dataTransferObject.accounting.PaymentsManagementDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.SelectableEntryBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.PaymentMode;
import net.sourceforge.fenixedu.domain.accounting.Receipt;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.SearchForStudentsDA;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/payments", module = "academicAdministration", formBeanClass = FenixActionForm.class,
        functionality = SearchForStudentsDA.class)
@Forwards({ @Forward(name = "showOperations", path = "/academicAdminOffice/payments/showOperations.jsp"),
        @Forward(name = "showEvents", path = "/academicAdminOffice/payments/showEvents.jsp"),
        @Forward(name = "showEventsWithPayments", path = "/academicAdminOffice/payments/showEventsWithPayments.jsp"),
        @Forward(name = "showPaymentsForEvent", path = "/academicAdminOffice/payments/showPaymentsForEvent.jsp"),
        @Forward(name = "preparePayment", path = "/academicAdminOffice/payments/preparePayment.jsp"),
        @Forward(name = "preparePrintGuide", path = "/academicAdministration/guides.do?method=preparePrintGuide"),
        @Forward(name = "showReceipt", path = "/academicAdministration/receipts.do?method=prepareShowReceipt"),
        @Forward(name = "showEventsWithPaymentCodes", path = "/academicAdminOffice/payments/showEventsWithPaymentCodes.jsp"),
        @Forward(name = "showPaymentCodesForEvent", path = "/academicAdminOffice/payments/showPaymentCodesForEvent.jsp") })
public class PaymentsManagementDispatchAction extends FenixDispatchAction {

    protected PaymentsManagementDTO searchNotPayedEventsForPerson(HttpServletRequest request, Person person) {

        final PaymentsManagementDTO paymentsManagementDTO = new PaymentsManagementDTO(person);
        for (final Event event : person.getNotPayedEvents()) {
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

        final PaymentsManagementDTO paymentsManagementDTO =
                (PaymentsManagementDTO) RenderUtils.getViewState("paymentsManagementDTO").getMetaObject().getObject();

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
        final PaymentsManagementDTO paymentsManagementDTO =
                (PaymentsManagementDTO) getObjectFromViewState("paymentsManagementDTO-edit");

        RenderUtils.invalidateViewState("paymentsManagementDTO-edit");

        paymentsManagementDTO.setContributorParty(null);
        paymentsManagementDTO.setContributorNumber(null);
        paymentsManagementDTO.setContributorName(null);

        request.setAttribute("paymentsManagementDTO", paymentsManagementDTO);

        return mapping.findForward("preparePayment");
    }

    public ActionForward doPayment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final PaymentsManagementDTO paymentsManagementDTO =
                (PaymentsManagementDTO) RenderUtils.getViewState("paymentsManagementDTO-edit").getMetaObject().getObject();

        if (paymentsManagementDTO.getSelectedEntries().isEmpty()) {

            addActionMessage("context", request, "error.payments.payment.entries.selection.is.required");
            request.setAttribute("paymentsManagementDTO", paymentsManagementDTO);

            return mapping.findForward("preparePayment");
        }

        //This is here to force the load of the relation to debug a possible bug in FenixFramework
        paymentsManagementDTO.getPerson().getReceiptsSet().size();
        try {
            final Receipt receipt =
                    CreatePaymentsForEvents.run(getUserView(request).getPerson().getUser(),
                            paymentsManagementDTO.getSelectedEntries(), PaymentMode.CASH,
                            paymentsManagementDTO.isDifferedPayment(), paymentsManagementDTO.getPaymentDate(),
                            paymentsManagementDTO.getPerson(), paymentsManagementDTO.getContributorParty(),
                            paymentsManagementDTO.getContributorName());

            request.setAttribute("personId", paymentsManagementDTO.getPerson().getExternalId());
            request.setAttribute("receiptID", receipt.getExternalId());

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
        return getDomainObject(request, "personId");
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
        return getDomainObject(request, "eventId");
    }

    public ActionForward preparePrintGuide(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("preparePrintGuide");
    }

    public ActionForward showEventsWithPaymentCodes(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final Person person = getPerson(request);
        request.setAttribute("person", person);
        request.setAttribute("eventsWithPaymentCodes", searchOpenEventsWithPaymentCodes(request, person));

        return mapping.findForward("showEventsWithPaymentCodes");
    }

    public ActionForward showPaymentCodesForEvent(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final Event event = getEvent(request);
        request.setAttribute("event", event);
        request.setAttribute("accountingEventPaymentCodes", event.getNonProcessedPaymentCodes());

        return mapping.findForward("showPaymentCodesForEvent");
    }

    private Collection<Event> searchOpenEventsWithPaymentCodes(HttpServletRequest request, final Person person) {
        final Collection<Event> events = new HashSet<Event>();
        for (final Event event : person.getNotPayedEvents()) {
            if (event.isOpen() && event.hasNonProcessedPaymentCodes()) {
                events.add(event);
            }
        }
        return events;
    }

    // protected Unit getReceiptOwnerUnit(HttpServletRequest request) {
    // return
    // getUserView(request).getPerson().getEmployee().getCurrentWorkingPlace();
    // }

}
