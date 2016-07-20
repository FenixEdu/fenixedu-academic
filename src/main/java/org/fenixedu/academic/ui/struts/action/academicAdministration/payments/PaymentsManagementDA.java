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
package org.fenixedu.academic.ui.struts.action.academicAdministration.payments;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.AccountingTransaction;
import org.fenixedu.academic.domain.accounting.Discount;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.Receipt;
import org.fenixedu.academic.domain.accounting.events.gratuity.ExternalScholarshipGratuityExemption;
import org.fenixedu.academic.domain.accounting.events.gratuity.ExternalScholarshipGratuityExemptionJustificationType;
import org.fenixedu.academic.domain.accounting.events.gratuity.GratuityEvent;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.exceptions.DomainExceptionWithLabelFormatter;
import org.fenixedu.academic.dto.TransferDebtBean;
import org.fenixedu.academic.dto.accounting.AnnulAccountingTransactionBean;
import org.fenixedu.academic.dto.accounting.CancelEventBean;
import org.fenixedu.academic.dto.accounting.DepositAmountBean;
import org.fenixedu.academic.dto.accounting.TransferPaymentsToOtherEventAndCancelBean;
import org.fenixedu.academic.dto.person.SimpleSearchPersonWithStudentBean;
import org.fenixedu.academic.predicate.AcademicPredicates;
import org.fenixedu.academic.service.services.accounting.AnnulAccountingTransaction;
import org.fenixedu.academic.service.services.accounting.AnnulReceipt;
import org.fenixedu.academic.service.services.accounting.CancelEvent;
import org.fenixedu.academic.service.services.accounting.DepositAmountOnEvent;
import org.fenixedu.academic.service.services.accounting.OpenEvent;
import org.fenixedu.academic.service.services.accounting.TransferPaymentsToOtherEventAndCancel;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.action.academicAdministration.AcademicAdministrationApplication.AcademicAdminPaymentsApp;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

@StrutsFunctionality(app = AcademicAdminPaymentsApp.class, path = "manage-payments", titleKey = "label.payments.management",
        accessGroup = "academic(MANAGE_STUDENT_PAYMENTS_ADV)")
@Mapping(path = "/paymentsManagement", module = "academicAdministration")
@Forwards({
        @Forward(name = "searchPersons", path = "/academicAdministration/payments/events/searchPersons.jsp"),
        @Forward(name = "transferDebt", path = "/academicAdministration/payments/events/transferDebt.jsp"),
        @Forward(name = "showEvents", path = "/academicAdministration/payments/events/showEvents.jsp"),
        @Forward(name = "editCancelEventJustification",
                path = "/academicAdministration/payments/events/editCancelEventJustification.jsp"),
        @Forward(name = "showPaymentsForEvent", path = "/academicAdministration/payments/events/showPaymentsForEvent.jsp"),
        @Forward(name = "chooseTargetEventForPaymentsTransfer",
                path = "/academicAdministration/payments/events/chooseTargetEventForPaymentsTransfer.jsp"),
        @Forward(name = "annulTransaction", path = "/academicAdministration/payments/events/annulTransaction.jsp"),
        @Forward(name = "showOperations", path = "/academicAdministration/payments/showOperations.jsp"),
        @Forward(name = "showReceipts", path = "/academicAdministration/payments/receipts/showReceipts.jsp"),
        @Forward(name = "showReceipt", path = "/academicAdministration/payments/receipts/showReceipt.jsp"),
        @Forward(name = "depositAmount", path = "/academicAdministration/payments/events/depositAmount.jsp"),
        @Forward(name = "viewCodes", path = "/academicAdministration/payments/codes/viewCodes.jsp"),
        @Forward(name = "createPaymentCodeMapping", path = "/academicAdministration/payments/codes/createPaymentCodeMapping.jsp"),
        @Forward(name = "changePaymentPlan", path = "/academicAdministration/payments/events/changePaymentPlan.jsp"),
        @Forward(name = "viewEventsForCancellation",
                path = "/academicAdministration/payments/events/viewEventsForCancellation.jsp") })
public class PaymentsManagementDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepareSearchPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("searchPersonBean", new SimpleSearchPersonWithStudentBean());

        return mapping.findForward("searchPersons");
    }

    public ActionForward prepareSearchPersonInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("searchPersonBean", getObjectFromViewState("searchPersonBean"));

        return mapping.findForward("searchPersons");
    }

    public ActionForward searchPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        final SimpleSearchPersonWithStudentBean searchPersonBean =
                (SimpleSearchPersonWithStudentBean) getObjectFromViewState("searchPersonBean");
        request.setAttribute("searchPersonBean", searchPersonBean);

        Collection<Person> persons = searchPersonBean.search();
        request.removeAttribute("sizeWarning");
        if (persons.size() == 1) {
            request.setAttribute("personId", persons.iterator().next().getExternalId());

            return showOperations(mapping, form, request, response);

        }
        if (persons.size() > 50) {
            persons = persons.stream().limit(50).collect(Collectors.toSet());
            request.setAttribute("sizeWarning", BundleUtil.getString(Bundle.ACADEMIC, "warning.need.to.filter.candidates"));
        }
        request.setAttribute("persons", persons);
        return mapping.findForward("searchPersons");
    }

    public ActionForward showEvents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        request.setAttribute("person", getPerson(request));

        return mapping.findForward("showEvents");

    }

    public ActionForward showPaymentsForEvent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final Event event = getEvent(request);
        request.setAttribute("event", event);

        if (!StringUtils.isEmpty(event.getCreatedBy())) {
            User responsible = User.findByUsername(event.getCreatedBy());
            request.setAttribute("responsible", responsible.getPerson());
        }

        if (event.isOpen()) {
            request.setAttribute("entryDTOs", event.calculateEntries());
            request.setAttribute("accountingEventPaymentCodes", event.getNonProcessedPaymentCodes());
        }

        return mapping.findForward("showPaymentsForEvent");
    }

    public ActionForward prepareCancelEvent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("cancelEventBean", new CancelEventBean(getEvent(request), getLoggedPerson(request)));

        return mapping.findForward("editCancelEventJustification");
    }

    public ActionForward cancelEvent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final CancelEventBean cancelEventBean = getCancelEventBean();

        try {
            CancelEvent.run(cancelEventBean.getEvent(), cancelEventBean.getResponsible(), cancelEventBean.getJustification());
        } catch (DomainExceptionWithLabelFormatter ex) {

            addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex.getLabelFormatterArgs()));
            request.setAttribute("cancelEventBean", cancelEventBean);

            return mapping.findForward("editCancelEventJustification");
        } catch (DomainException ex) {

            addActionMessage(request, ex.getKey(), ex.getArgs());
            request.setAttribute("cancelEventBean", cancelEventBean);

            return mapping.findForward("editCancelEventJustification");
        }

        request.setAttribute("person", cancelEventBean.getEvent().getPerson());

        return mapping.findForward("showEvents");

    }

    public ActionForward openEvent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        try {
            OpenEvent.run(getEvent(request));
        } catch (DomainExceptionWithLabelFormatter ex) {
            addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex.getLabelFormatterArgs()));
        } catch (DomainException ex) {
            addActionMessage(request, ex.getKey(), ex.getArgs());
        }

        request.setAttribute("personId", getEvent(request).getPerson().getExternalId());

        return showEvents(mapping, form, request, response);

    }

    private CancelEventBean getCancelEventBean() {
        final CancelEventBean cancelEventBean = (CancelEventBean) getObjectFromViewState("cancelEventBean");
        return cancelEventBean;
    }

    public ActionForward backToShowEvents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("person", getPerson(request));

        return mapping.findForward("showEvents");

    }

    public ActionForward prepareTransferDebt(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        Event event = getEvent(request);

        System.out.println("" + event.getExternalId());

        TransferDebtBean transferDebtBean = getRenderedObject();
        if (transferDebtBean == null) {
            transferDebtBean = new TransferDebtBean();
            transferDebtBean.setEvent(event);
        }

        request.setAttribute("transferDebtBean", transferDebtBean);
        request.setAttribute("entryDTOs", event.calculateEntries());

        return mapping.findForward("transferDebt");

    }

    public ActionForward transferDebt(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        TransferDebtBean transferDebtBean = getRenderedObject();
        if(!Bennu.getInstance().getExternalScholarshipProviderSet().stream().anyMatch(p->p == transferDebtBean.getCreditor())) {
            addActionMessage(request, "error.events.transfer.unauthorized.entity");
            request.setAttribute("transferDebtBean", transferDebtBean);
            request.setAttribute("entryDTOs", transferDebtBean.getEvent().calculateEntries());
            return mapping.findForward("transferDebt");
        }
        if(!(transferDebtBean.getEvent() instanceof  GratuityEvent)) {
            addActionMessage(request, "error.events.transfer.unallowed.event");
            request.setAttribute("person",transferDebtBean.getEvent().getPerson());
            return mapping.findForward("showEvents");
        }
        GratuityEvent event = (GratuityEvent) transferDebtBean.getEvent();

        Money value = event.getAmountToPay();
        atomic(() -> new ExternalScholarshipGratuityExemption(Authenticate.getUser().getPerson(), event, value,
                ExternalScholarshipGratuityExemptionJustificationType.THIRD_PARTY_CONTRIBUTION, transferDebtBean.getReason(),
                transferDebtBean.getCreditor(), transferDebtBean.getFileName(), transferDebtBean.getFile()));

        request.setAttribute("person", event.getPerson());
        return mapping.findForward("showEvents");
    }

    protected Person getPerson(HttpServletRequest request) {
        return getDomainObject(request, "personId");
    }

    private Event getEvent(HttpServletRequest request) {
        return getDomainObject(request, "eventId");
    }

    public ActionForward showExternalEvents(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        Person person = getPerson(request);

        request.setAttribute("person", person);
        request.setAttribute("events", person.getGratuityEvents().stream().map(GratuityEvent::getExternalScholarshipGratuityExemption).filter(Objects::isNull)
                .map(ExternalScholarshipGratuityExemption::getExternalScholarshipGratuityContributionEvent)
                .collect(Collectors.toSet()));

        return mapping.findForward("showExternalEvents");

    }

    public ActionForward prepareTransferPaymentsToOtherEventAndCancel(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        final Event event = getEvent(request);

        final TransferPaymentsToOtherEventAndCancelBean transferPaymentsBean =
                new TransferPaymentsToOtherEventAndCancelBean(event, getLoggedPerson(request));

        request.setAttribute("transferPaymentsBean", transferPaymentsBean);

        return mapping.findForward("chooseTargetEventForPaymentsTransfer");

    }

    public ActionForward transferPaymentsToOtherEventAndCancel(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {

        final TransferPaymentsToOtherEventAndCancelBean transferPaymentsBean =
                (TransferPaymentsToOtherEventAndCancelBean) getObjectFromViewState("transferPaymentsBean");

        try {
            TransferPaymentsToOtherEventAndCancel.run(transferPaymentsBean.getResponsible(),
                    transferPaymentsBean.getSourceEvent(), transferPaymentsBean.getTargetEvent(),
                    transferPaymentsBean.getCancelJustification());
        } catch (DomainExceptionWithLabelFormatter ex) {

            addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex.getLabelFormatterArgs()));
            request.setAttribute("transferPaymentsBean", transferPaymentsBean);

            return mapping.findForward("chooseTargetEventForPaymentsTransfer");
        } catch (DomainException ex) {

            addActionMessage(request, ex.getKey(), ex.getArgs());
            request.setAttribute("transferPaymentsBean", transferPaymentsBean);

            return mapping.findForward("chooseTargetEventForPaymentsTransfer");
        }

        request.setAttribute("event", transferPaymentsBean.getSourceEvent());

        return mapping.findForward("showPaymentsForEvent");

    }

    public ActionForward prepareAnnulTransaction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("annulAccountingTransactionBean", new AnnulAccountingTransactionBean(getTransaction(request)));

        return mapping.findForward("annulTransaction");

    }

    public ActionForward prepareAnnulTransactionInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("annulAccountingTransactionBean", getObjectFromViewState("annulAccountingTransactionBean"));

        return mapping.findForward("annulTransaction");
    }

    public ActionForward annulTransaction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        final AnnulAccountingTransactionBean annulAccountingTransactionBean =
                (AnnulAccountingTransactionBean) getObjectFromViewState("annulAccountingTransactionBean");
        try {

            AnnulAccountingTransaction.run(annulAccountingTransactionBean);
        } catch (DomainExceptionWithLabelFormatter ex) {

            addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex.getLabelFormatterArgs()));
            request.setAttribute("annulAccountingTransactionBean", annulAccountingTransactionBean);

            return mapping.findForward("annulTransaction");
        } catch (DomainException ex) {

            addActionMessage(request, ex.getKey(), ex.getArgs());
            request.setAttribute("annulAccountingTransactionBean", annulAccountingTransactionBean);

            return mapping.findForward("annulTransaction");
        }

        return showEvents(mapping, form, request, response);
    }

    private AccountingTransaction getTransaction(HttpServletRequest request) {
        return getDomainObject(request, "transactionId");
    }

    public ActionForward showOperations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("permission", getPermissionForAcademicAdministration(getPerson(request)));
        request.setAttribute("person", getPerson(request));

        return mapping.findForward("showOperations");
    }

    private boolean getPermissionForAcademicAdministration(Person person) {
        return AcademicPredicates.MANAGE_STUDENT_PAYMENTS_ADV.evaluate(person);
    }

    public ActionForward showReceipts(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("person", getPerson(request));

        return mapping.findForward("showReceipts");
    }

    public ActionForward showReceipt(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("receipt", getReceipt(request));

        return mapping.findForward("showReceipt");
    }

    public ActionForward annulReceipt(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        try {
            AnnulReceipt.run(getLoggedPerson(request), getReceipt(request));
        } catch (DomainExceptionWithLabelFormatter ex) {
            addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex.getLabelFormatterArgs()));
        } catch (DomainException ex) {
            addActionMessage(request, ex.getKey(), ex.getArgs());
        }

        return showReceipts(mapping, form, request, response);
    }

    private Receipt getReceipt(HttpServletRequest request) {
        return getDomainObject(request, "receiptId");
    }

    public ActionForward prepareDepositAmount(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("depositAmountBean", new DepositAmountBean(getEvent(request)));

        return mapping.findForward("depositAmount");
    }

    public ActionForward prepareDepositAmountInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("depositAmountBean", getRenderedObject("depositAmountBean"));

        return mapping.findForward("depositAmount");
    }

    public ActionForward depositAmount(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final DepositAmountBean renderedObject = getRenderedObject("depositAmountBean");
        try {
            DepositAmountOnEvent.run(renderedObject);
        } catch (DomainException e) {
            addActionMessage(request, e.getKey(), e.getArgs());

            request.setAttribute("depositAmountBean", renderedObject);

            return mapping.findForward("depositAmount");
        }

        return showEvents(mapping, form, request, response);
    }

    public ActionForward prepareChangePaymentPlan(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("event", getEvent(request));
        return mapping.findForward("changePaymentPlan");
    }

    public ActionForward deleteDiscount(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final Discount discount = getDomainObject(request, "discountOid");
        request.setAttribute("eventId", discount.getEvent().getExternalId());

        try {
            discount.delete();
        } catch (final DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
        }

        return showPaymentsForEvent(mapping, actionForm, request, response);
    }

    public ActionForward prepareViewEventsToCancel(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) {
        Person person = getDomainObject(request, "personId");
        request.setAttribute("person", person);

        return mapping.findForward("viewEventsForCancellation");
    }

}