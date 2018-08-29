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
/*
 * Created on Jun 26, 2006
 */
package org.fenixedu.academic.ui.struts.action.administrativeOffice.payments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.Entry;
import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.Exemption;
import org.fenixedu.academic.domain.accounting.PaymentMode;
import org.fenixedu.academic.domain.accounting.Receipt;
import org.fenixedu.academic.domain.accounting.events.gratuity.ExternalScholarshipGratuityContributionEvent;
import org.fenixedu.academic.domain.accounting.events.gratuity.ExternalScholarshipGratuityExemption;
import org.fenixedu.academic.domain.accounting.events.gratuity.GratuityEvent;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.exceptions.DomainExceptionWithLabelFormatter;
import org.fenixedu.academic.domain.phd.debts.ExternalScholarshipPhdGratuityContribuitionEvent;
import org.fenixedu.academic.domain.phd.debts.PhdGratuityExternalScholarshipExemption;
import org.fenixedu.academic.dto.accounting.AccountingTransactionDetailDTO;
import org.fenixedu.academic.dto.accounting.EntryDTO;
import org.fenixedu.academic.dto.accounting.PaymentsManagementDTO;
import org.fenixedu.academic.dto.accounting.SelectableEntryBean;
import org.fenixedu.academic.service.services.accounting.CreatePaymentsForEvents;
import org.fenixedu.academic.ui.struts.FenixActionForm;
import org.fenixedu.academic.ui.struts.action.administrativeOffice.payments.ExternalScholarshipManagementDebtsDA.AmountBean;
import org.fenixedu.academic.ui.struts.action.administrativeOffice.student.SearchForStudentsDA;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/payments", module = "academicAdministration", formBeanClass = FenixActionForm.class,
        functionality = SearchForStudentsDA.class)
@Forwards({ @Forward(name = "showOperations", path = "/academicAdminOffice/payments/showOperations.jsp"),
        @Forward(name = "showContribution", path = "/academicAdminOffice/payments/showContribution.jsp"),
        @Forward(name = "showExternalEvents", path = "/academicAdminOffice/payments/showExternalEvents.jsp"),
        @Forward(name = "showEvents", path = "/academicAdminOffice/payments/showEvents.jsp"),
        @Forward(name = "showEventsWithPayments", path = "/academicAdminOffice/payments/showEventsWithPayments.jsp"),
        @Forward(name = "showPaymentsForEvent", path = "/academicAdminOffice/payments/showPaymentsForEvent.jsp"),
        @Forward(name = "preparePayment", path = "/academicAdminOffice/payments/preparePayment.jsp"),
        @Forward(name = "preparePrintGuide", path = "/academicAdministration/guides.do?method=preparePrintGuide"),
        @Forward(name = "showReceipt", path = "/academicAdministration/receipts.do?method=prepareShowReceipt"),
        @Forward(name = "showEventsWithPaymentCodes", path = "/academicAdminOffice/payments/showEventsWithPaymentCodes.jsp"),
        @Forward(name = "showPaymentCodesForEvent", path = "/academicAdminOffice/payments/showPaymentCodesForEvent.jsp") })
public class PaymentsManagementDispatchAction extends FenixDispatchAction {

    protected PaymentsManagementDTO searchNotPayedEventsForPerson(Person person) {

        final PaymentsManagementDTO paymentsManagementDTO = new PaymentsManagementDTO(person);
        for (final Event event : person.getNotPayedEvents()) {
            paymentsManagementDTO.addEntryDTOs(event.calculateEntries());
        }

        return paymentsManagementDTO;
    }

    public ActionForward showEvents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        try {
            request.setAttribute("paymentsManagementDTO", searchNotPayedEventsForPerson(getPerson(request)));

        } catch (DomainException e) {
            addActionMessage(request, e.getKey(), e.getArgs());
            return showOperations(mapping, form, request, response);
        }

        return mapping.findForward("showEvents");
    }

    public ActionForward showExternalEvents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        Person person = getPerson(request);

        Set<Event> events = person.getGratuityEvents().stream().map(GratuityEvent::getExternalScholarshipGratuityExemption)
                .filter(Objects::nonNull)
                .map(ExternalScholarshipGratuityExemption::getExternalScholarshipGratuityContributionEvent).filter(Event::isOpen)
                .collect(Collectors.toSet());

        Set<Event> phdEvents =
                person.getEventsByEventType(EventType.PHD_GRATUITY).stream().flatMap(e -> e.getExemptionsSet().stream())
                        .filter(e -> e instanceof PhdGratuityExternalScholarshipExemption)
                        .map(e -> (PhdGratuityExternalScholarshipExemption) e)
                        .map(PhdGratuityExternalScholarshipExemption::getExternalScholarshipPhdGratuityContribuitionEvent)
                        .filter(Event::isOpen).collect(Collectors.toSet());

        request.setAttribute("events", events);
        request.setAttribute("phdEvents", phdEvents);
        request.setAttribute("person", person);
        return mapping.findForward("showExternalEvents");
    }

    public ActionForward prepareLiquidation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        String eventId = request.getParameter("eventId");
        Event event = FenixFramework.getDomainObject(eventId == null ? (String) request.getAttribute("eventId") : eventId);

        request.setAttribute("event", event);
        request.setAttribute("person", getOriginalDebtor(event));
        AmountBean bean = new AmountBean();
        bean.setValue(event.getAmountToPay());
        request.setAttribute("bean", bean);
        return mapping.findForward("showContribution");
    }

    @Atomic
    public ActionForward liquidate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        String eventId = request.getParameter("externalId");
        Event event = FenixFramework.getDomainObject(eventId == null ? (String) request.getAttribute("externalId") : eventId);

        AmountBean bean = getRenderedObject("bean");
        List<EntryDTO> list = new ArrayList<EntryDTO>();
        list.add(new EntryDTO(EntryType.EXTERNAL_SCOLARSHIP_PAYMENT, event, bean.getValue()));
        event.process(Authenticate.getUser(), list, new AccountingTransactionDetailDTO(bean.getPaymentDate(), PaymentMode.CASH));

        request.setAttribute("personId", getOriginalDebtor(event).getExternalId());
        return showExternalEvents(mapping, form, request, response);
    }

    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        String eventId = request.getParameter("externalId");
        Event event = FenixFramework.getDomainObject(eventId == null ? (String) request.getAttribute("externalId") : eventId);

        request.setAttribute("personId", getOriginalDebtor(event).getExternalId());
        return showExternalEvents(mapping, form, request, response);
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

        final List<EntryDTO> selectedEntries = paymentsManagementDTO.getSelectedEntries();

        if (selectedEntries.isEmpty()) {
            addActionMessage("context", request, "error.payments.payment.entries.selection.is.required");
            return mapping.findForward("showEvents");
        }

        final List<EntryDTO> eventPenaltyEntries = getEventPenaltyEntries(searchNotPayedEventsForPerson(getPerson(request))
                .getEntryDTOs());
        final List<EntryDTO> selectedEventPenaltyEntries = getEventPenaltyEntries(selectedEntries);

        final Set<Event> missingPenaltyEntriesEvents = selectedEntries.stream().map(EntryDTO::getEvent).distinct()
                .flatMap(e -> eventPenaltyEntries.stream().filter(pe -> pe.getEvent() == e))
                .filter(pe -> !selectedEventPenaltyEntries.contains(pe)).map(EntryDTO::getEvent).collect(Collectors.toSet());

        if (!missingPenaltyEntriesEvents.isEmpty()) {
            missingPenaltyEntriesEvents.forEach(e -> {
                addActionMessage("context", request, "error.payments.payment.penalty.entries.selection.is.required",e
                        .getDescription().toString());
            });
            return mapping.findForward("showEvents");
        }

        return mapping.findForward("preparePayment");
    }

    private List<EntryDTO> getEventPenaltyEntries(List<EntryDTO> entries) {
        return entries.stream().filter(EntryDTO::isForPenalty).collect(Collectors.toList());
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
                            paymentsManagementDTO.getPerson(), paymentsManagementDTO.getContributorName(),
                            paymentsManagementDTO.getContributorNumber(), paymentsManagementDTO.getContributorAddress());

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


    private Person getOriginalDebtor(Event event) {
        Exemption exemption =
                ((event instanceof ExternalScholarshipGratuityContributionEvent) ? ((ExternalScholarshipGratuityContributionEvent) event)
                        .getExternalScholarshipGratuityExemption() : ((ExternalScholarshipPhdGratuityContribuitionEvent) event)
                        .getPhdGratuityExternalScholarshipExemption());

        return exemption.getEvent().getPerson();
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
        return person.getNotPayedEvents().stream()
                .filter(event -> event.isOpen() && event.hasNonProcessedPaymentCodes())
                .collect(Collectors.toSet());
    }

}
