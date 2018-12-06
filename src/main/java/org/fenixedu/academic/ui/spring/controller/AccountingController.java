package org.fenixedu.academic.ui.spring.controller;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.AccountingTransaction;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.Exemption;
import org.fenixedu.academic.domain.accounting.Refund;
import org.fenixedu.academic.domain.accounting.calculator.AccountingEntry;
import org.fenixedu.academic.domain.accounting.calculator.ExcessRefund;
import org.fenixedu.academic.domain.accounting.calculator.CreditEntry;
import org.fenixedu.academic.domain.accounting.calculator.Debt;
import org.fenixedu.academic.domain.accounting.calculator.DebtEntry;
import org.fenixedu.academic.domain.accounting.calculator.DebtInterestCalculator;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.ui.spring.service.AccountingManagementAccessControlService;
import org.fenixedu.academic.ui.spring.service.AccountingManagementService;
import org.fenixedu.academic.ui.spring.service.AccountingManagementService.AccountingEntrySummary;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.base.Strings;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;

/**
 * Created by Sérgio Silva (hello@fenixedu.org).
 */
public abstract class AccountingController {

    public static final Comparator<Event> MOST_RECENT_EVENTS_FIRST = Comparator.comparing(Event::getWhenOccured).reversed();
    protected final AccountingManagementService accountingManagementService;
    protected final AccountingManagementAccessControlService accessControlService;
    protected final ServletContext servletContext;
    protected final MessageSource messageSource;

    public AccountingController(AccountingManagementService accountingManagementService, AccountingManagementAccessControlService accountingManagementAccessControlService, ServletContext servletContext, MessageSource messageSource) {
        this.accountingManagementService = accountingManagementService;
        this.accessControlService = accountingManagementAccessControlService;
        this.servletContext = servletContext;
        this.messageSource = messageSource;
    }

    public abstract String entrypointUrl();

    public abstract String getEventDetailsUrl(Event event);

    @RequestMapping("{person}")
    public String events(@PathVariable Person person, Model model, User loggedUser) {
        final User user = person.getUser();
        
        if (loggedUser == user || accessControlService.isPaymentManager(loggedUser)) {
            model.addAttribute("person", person);

            Set<Event> events = person.getEventsSet();
            HashMap<Event, String> invalidEventsMap = getInvalidEventsMap(events);
            Set<Event> validEvents = events.stream().filter(event -> !invalidEventsMap.keySet().contains(event)).collect(Collectors.toSet());

            model.addAttribute("openEvents", validEvents.stream().filter(Event::isOpen).sorted(MOST_RECENT_EVENTS_FIRST).collect(Collectors.toList()));
            model.addAttribute("otherEvents", validEvents.stream().filter(e -> !e.isOpen()).sorted(MOST_RECENT_EVENTS_FIRST).collect(Collectors.toList()));
            model.addAttribute("invalidEvents", invalidEventsMap);
            model.addAttribute("isPaymentManager", accessControlService.isPaymentManager(loggedUser));

            return view("events");
        }
        throw new UnsupportedOperationException("Unauthorized");
    }

    @RequestMapping("{event}/details")
    public String details(@PathVariable Event event, @RequestParam(value = "date", defaultValue = "#{new org.joda.time.DateTime()}") DateTime date,  Model model, User loggedUser) {
        accessControlService.checkEventOwnerOrPaymentManager(event, loggedUser);

        final DebtInterestCalculator debtInterestCalculator = event.getDebtInterestCalculator(date);

        model.addAttribute("entrypointUrl", entrypointUrl());

        model.addAttribute("transactions", accountingManagementService.getAccountingEntries(debtInterestCalculator));
        model.addAttribute("event", event);
        model.addAttribute("eventDetailsUrl", getEventDetailsUrl(event));
        model.addAttribute("eventId", event.getExternalId());
        model.addAttribute("currentDate", date.toLocalDate());

        model.addAttribute("debts", debtInterestCalculator.getDebtsOrderedByDueDate());
        model.addAttribute("eventTotalAmountToPay", debtInterestCalculator.getTotalDueAmount());
        model.addAttribute("eventPaidUnusedAmount", debtInterestCalculator.getPaidUnusedAmount());
        model.addAttribute("eventDebtAmountToPay", debtInterestCalculator.getDueAmount());
        model.addAttribute("eventInterestAmountToPay", debtInterestCalculator.getDueInterestAmount());
        model.addAttribute("eventFineAmountToPay", debtInterestCalculator.getDueFineAmount());
        model.addAttribute("eventOriginalAmountToPay", event.getOriginalAmountToPay());

        model.addAttribute("payedDebtAmount", debtInterestCalculator.getPaidDebtAmount());

        model.addAttribute("isEventOwner", accessControlService.isEventOwner(event, loggedUser));
        model.addAttribute("isPaymentManager", accessControlService.isPaymentManager(event, loggedUser));
        model.addAttribute("isAdvancedPaymentManager", accessControlService.isAdvancedPaymentManager(event, loggedUser));

        return view("event-details");
    }

    @RequestMapping("{event}/transaction/{id}/details")
    public String transactionDetails(@PathVariable Event event, @PathVariable String id, @RequestParam(value = "date",
            defaultValue = "#{new org.joda.time.DateTime()}") DateTime date, Model model, User loggedUser) {

        DebtInterestCalculator debtInterestCalculator = event.getDebtInterestCalculator(date);

        AccountingEntry accountingEntry = debtInterestCalculator.getAccountingEntry(id).orElseThrow
                (UnsupportedOperationException::new);

        if (accountingEntry instanceof DebtEntry) {
            return debtEntryDetails(event, (DebtEntry) accountingEntry, model, loggedUser);
        }

        if (accountingEntry instanceof CreditEntry) {
            return creditEntryDetails(event, debtInterestCalculator, (CreditEntry) accountingEntry, model, loggedUser);
        }

        throw new UnsupportedOperationException("Can't find resolver for transaction with id " + id);
    }

    private String debtEntryDetails(Event event, DebtEntry debtEntry, Model model, User loggedUser) {
        accessControlService.checkEventOwnerOrPaymentManager(event, loggedUser);

        final List<AccountingManagementService.AccountingEntrySummary> paymentSummaries = accountingManagementService.createAccountingEntrySummaries(debtEntry);
        
        paymentSummaries.sort(Comparator.comparing(AccountingManagementService.AccountingEntrySummary::getCreated)
                .thenComparing(AccountingEntrySummary::getDate)
                .thenComparing(AccountingEntrySummary::getId).reversed());

        model.addAttribute("eventId", event.getExternalId());
        model.addAttribute("event", event);
        model.addAttribute("eventDetailsUrl", getEventDetailsUrl(event));
        model.addAttribute("eventDescription", event.getDescription());
        model.addAttribute("payments", paymentSummaries);
        model.addAttribute("isEventOwner", accessControlService.isEventOwner(event, loggedUser));

        if (debtEntry instanceof Debt) {
            model.addAttribute("debt", debtEntry);
            return view("event-debt-details");
        }

        if (debtEntry instanceof ExcessRefund) {
            final ExcessRefund excessRefund = (ExcessRefund) debtEntry;
            final String paymentId = excessRefund.getTargetPaymentId();
            if (!Strings.isNullOrEmpty(paymentId)) {
                AccountingTransaction accountingTransaction = FenixFramework.getDomainObject(paymentId);
                model.addAttribute("targetPaymentId", accountingTransaction);
            }
            model.addAttribute("excessRefund", excessRefund);
            return view("event-excessRefund-details");
        }

        throw new UnsupportedOperationException("Unknown debt entry type " + debtEntry.getClass().getName());
    }

    private String creditEntryDetails(Event event, DebtInterestCalculator calculator, CreditEntry creditEntry, Model model, User
            loggedUser) {
        accessControlService.checkEventOwnerOrPaymentManager(event, loggedUser);

        model.addAttribute("eventDetailsUrl", getEventDetailsUrl(event));
        model.addAttribute("eventId", event.getExternalId());
        model.addAttribute("creditEntry", creditEntry);
        model.addAttribute("processedDate", creditEntry.getCreated());
        model.addAttribute("registeredDate", creditEntry.getDate());
        model.addAttribute("amount", creditEntry.getAmount());
        model.addAttribute("payments", accountingManagementService.createAccountingEntrySummaries(creditEntry));

        final DomainObject domainCredit = FenixFramework.getDomainObject(creditEntry.getId());
        if (domainCredit instanceof Refund) {
            model.addAttribute("refund", domainCredit);
            return view("event-refund-details");
        } else if (domainCredit instanceof AccountingTransaction) {
            model.addAttribute("sourceRefund",
                    Optional.ofNullable(((AccountingTransaction) domainCredit).getRefund()).orElse(null));
            model.addAttribute("transactionDetail", domainCredit);
            return view("event-payment-details");
        }
        else if (domainCredit instanceof Exemption){
            model.addAttribute("exemption", domainCredit);
            model.addAttribute("exemptionValue", ((Exemption) domainCredit).getExemptionAmount(new Money(calculator.getDebtAmount())));
            return view("event-exemption-details");
        }
        else {
            throw new UnsupportedOperationException("Unknown transaction type: not a payment or exemption");
        }

    }

    @RequestMapping(value = "{event}/depositAdvancement", method = RequestMethod.POST)
    public String depositAdvancement(final @PathVariable Event event, final User user, final Model model,
            @RequestParam final Event eventToRefund) {
        accessControlService.checkEventOwnerOrPaymentManager(eventToRefund, user);

        try {
            accountingManagementService.depositAdvancement(event, eventToRefund, user);
        }
        catch (DomainException e) {
            e.printStackTrace();
            model.addAttribute("error", e.getLocalizedMessage());
            return depositAdvancementInput(event, user, model);
        }

        return redirectToEventDetails(event);
    }

    protected abstract String depositAdvancementInput(Event event, User user, Model model);

    protected abstract String redirectToEventDetails(@PathVariable Event event);

    private HashMap<Event, String> getInvalidEventsMap(Set<Event> events) {
        HashMap<Event, String> invalidEventsMap = new HashMap<>();
        events.forEach(event -> {
            try {
                event.getDebtInterestCalculator(new DateTime());
            }
            catch (DomainException e){
                invalidEventsMap.put(event, e.getLocalizedMessage());
            }
        });
        return invalidEventsMap;
    }

    protected String view(String view) {
        return "fenixedu-academic/accounting/" + view;
    }

}
