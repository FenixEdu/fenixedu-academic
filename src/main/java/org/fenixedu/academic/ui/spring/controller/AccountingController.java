package org.fenixedu.academic.ui.spring.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.AccountingTransaction;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.Exemption;
import org.fenixedu.academic.domain.accounting.calculator.CreditEntry;
import org.fenixedu.academic.domain.accounting.calculator.Debt;
import org.fenixedu.academic.domain.accounting.calculator.DebtInterestCalculator;
import org.fenixedu.academic.ui.spring.service.AccountingManagementAccessControlService;
import org.fenixedu.academic.ui.spring.service.AccountingManagementService;
import org.fenixedu.academic.ui.spring.service.AccountingManagementService.PaymentSummary;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;

/**
 * Created by Sérgio Silva (hello@fenixedu.org).
 */
public abstract class AccountingController {


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

    @RequestMapping("{person}")
    public String events(@PathVariable Person person, Model model, User loggedUser) {
        final User user = person.getUser();
        
        if (loggedUser == user || accessControlService.isPaymentManager(loggedUser)) {
            model.addAttribute("person", person);

            model.addAttribute("openEvents", person.getEventsSet().stream().filter(Event::isOpen).sorted(Comparator
                    .comparing(Event::getWhenOccured).reversed()).collect(Collectors.toList()));

            model.addAttribute("otherEvents", person.getEventsSet().stream().filter(e -> !e.isOpen()).sorted(Comparator
                    .comparing(Event::getWhenOccured).reversed()).collect(Collectors.toList()));

            model.addAttribute("isPaymentManager", accessControlService.isPaymentManager(loggedUser));

            return view("events");
        }
        throw new UnsupportedOperationException("Unauthorized");
    }

    @RequestMapping("{event}/details")
    public String details(@PathVariable Event event, @RequestParam(value = "date", defaultValue = "#{new org.joda.time.DateTime()}") DateTime date,  Model model, User loggedUser) {
        accessControlService.checkEventOwnerOrPaymentManager(event, loggedUser);

        final DebtInterestCalculator debtInterestCalculator = event.getDebtInterestCalculator(date);
        final List<CreditEntry> creditEntries = debtInterestCalculator.getCreditEntries();
        Collections.reverse(creditEntries);

        model.addAttribute("entrypointUrl", entrypointUrl());

        model.addAttribute("creditEntries", creditEntries);
        model.addAttribute("event", event);
        model.addAttribute("eventId", event.getExternalId());
        model.addAttribute("currentDate", date.toLocalDate());

        model.addAttribute("debts", debtInterestCalculator.getDebtsOrderedByDueDate());
        model.addAttribute("eventTotalAmountToPay", debtInterestCalculator.getTotalDueAmount());
        model.addAttribute("eventDebtAmountToPay", debtInterestCalculator.getDueAmount());
        model.addAttribute("eventInterestAmountToPay", debtInterestCalculator.getDueInterestAmount());
        model.addAttribute("eventFineAmountToPay", debtInterestCalculator.getDueFineAmount());
        model.addAttribute("eventOriginalAmountToPay", event.getOriginalAmountToPay());

        model.addAttribute("isEventOwner", accessControlService.isEventOwner(event, loggedUser));
        model.addAttribute("isPaymentManager", accessControlService.isPaymentManager(event, loggedUser));
        model.addAttribute("isAdvancedPaymentManager", accessControlService.isAdvancedPaymentManager(event, loggedUser));

        return view("event-details");
    }

    @RequestMapping("{event}/debt/{debtDueDate}/details")
    public String debtDetails(@PathVariable Event event, @PathVariable LocalDate debtDueDate, Model model, User loggedUser) {
        accessControlService.checkEventOwnerOrPaymentManager(event, loggedUser);

        final DebtInterestCalculator debtInterestCalculator = event.getDebtInterestCalculator(new DateTime());
        List<Debt> debtsOrderedByDueDate = debtInterestCalculator.getDebtsOrderedByDueDate();
        Debt debt = debtsOrderedByDueDate.stream().filter(d -> d.getDueDate().equals(debtDueDate)).findAny()
                .orElseThrow(UnsupportedOperationException::new);

        final List<PaymentSummary> paymentSummaries = accountingManagementService.createPaymentSummaries(debt);
        paymentSummaries.sort(Comparator.comparing(PaymentSummary::getCreated)
                .thenComparing(PaymentSummary::getDate)
                .thenComparing(PaymentSummary::getId).reversed());

        model.addAttribute("eventId", event.getExternalId());
        model.addAttribute("event", event);
        model.addAttribute("eventDescription", event.getDescription());
        model.addAttribute("debtIndex", debtsOrderedByDueDate.indexOf(debt) + 1);
        model.addAttribute("debt", debt);
        model.addAttribute("payments", paymentSummaries);

        model.addAttribute("isEventOwner", accessControlService.isEventOwner(event, loggedUser));

        return view("event-debt-details");
    }

    @RequestMapping("{event}/creditEntry/{creditEntryId}/details")
    public String creditEntryDetails(@PathVariable Event event, @PathVariable String creditEntryId, Model model, User loggedUser) {
        accessControlService.checkEventOwnerOrPaymentManager(event, loggedUser);

        final DebtInterestCalculator calculator = event.getDebtInterestCalculator(new DateTime());
        final CreditEntry creditEntry = calculator.getCreditEntryById(creditEntryId).orElseThrow(UnsupportedOperationException::new);
        final List<Debt> debtsOrderedByDueDate = calculator.getDebtsOrderedByDueDate();

        model.addAttribute("eventId", event.getExternalId());
        model.addAttribute("creditEntry", creditEntry);
        model.addAttribute("processedDate", creditEntry.getCreated());
        model.addAttribute("registeredDate", creditEntry.getDate());
        model.addAttribute("amount", creditEntry.getAmount());
        model.addAttribute("debtsOrderedByDueDate", debtsOrderedByDueDate);
        model.addAttribute("payments", accountingManagementService.createPaymentSummaries(creditEntry));

        final DomainObject transactionDetail = FenixFramework.getDomainObject(creditEntryId);
        if (transactionDetail instanceof AccountingTransaction) {
            model.addAttribute("transactionDetail", transactionDetail);
            return view("event-payment-details");
        }
        else if (transactionDetail instanceof Exemption){
            model.addAttribute("exemption", transactionDetail);
            model.addAttribute("exemptionValue", ((Exemption) transactionDetail).getExemptionAmount(new Money(calculator.getDebtAmount())));
            return view("event-exemption-details");
        }
        else {
            throw new UnsupportedOperationException("Unknown transaction type: not a payment or exemption");
        }

    }

    protected String view(String view) {
        return "fenixedu-academic/accounting/" + view;
    }
}
