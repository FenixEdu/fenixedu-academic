package org.fenixedu.academic.ui.spring.controller;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;

import org.fenixedu.academic.FenixEduAcademicConfiguration;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.calculator.Debt;
import org.fenixedu.academic.domain.accounting.calculator.DebtInterestCalculator;
import org.fenixedu.academic.domain.accounting.calculator.Fine;
import org.fenixedu.academic.domain.accounting.calculator.Interest;
import org.fenixedu.academic.domain.accounting.paymentCodes.EventPaymentCodeEntry;
import org.fenixedu.academic.ui.spring.service.AccountingManagementAccessControlService;
import org.fenixedu.academic.ui.spring.service.AccountingManagementService;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Sérgio Silva (hello@fenixedu.org).
 */

@Controller
@SpringFunctionality(app = AcademicAdministrationSpringApplication.class, title = "title.accounting.management.entrypoint",
        accessGroup = "logged")
@RequestMapping(AccountingEventForOwnerController.REQUEST_MAPPING)
public class AccountingEventForOwnerController extends AccountingController {
    private static final Logger logger = LoggerFactory.getLogger(AccountingEventForOwnerController.class);
    static final String REQUEST_MAPPING = "/owner-accounting-events";

    @Autowired
    public AccountingEventForOwnerController(AccountingManagementService accountingManagementService,
            AccountingManagementAccessControlService accountingManagementAccessControlService, ServletContext servletContext,
            MessageSource messageSource) {
        super(accountingManagementService, accountingManagementAccessControlService, servletContext, messageSource);
    }

    @Override
    public String entrypointUrl() {
        return REQUEST_MAPPING + "/{person}";
    }

    @RequestMapping
    public String entrypoint(User loggedUser) {
        return "redirect:" + REQUEST_MAPPING + "/" + loggedUser.getPerson().getExternalId();
    }

    @RequestMapping(value = "{event}/pay", method = RequestMethod.GET)
    public String doPayment(@PathVariable Event event, Model model, User loggedUser) {
        accessControlService.checkEventOwner(event, loggedUser);
        final DebtInterestCalculator debtInterestCalculator = event.getDebtInterestCalculator(new DateTime());

        if (debtInterestCalculator.getTotalDueAmount().compareTo(BigDecimal.ZERO) < 1) {
            logger.warn("Hacky user {} tried to access payment interface for event {}",
                    Optional.ofNullable(loggedUser).map(User::getUsername).orElse("---"), event.getExternalId());
            return entrypoint(loggedUser);
        }

        if (debtInterestCalculator.hasDueInterestAmount()) {
            // has open interests
            final List<Interest> interests = getInterests(debtInterestCalculator);
            final List<Fine> fines = getFines(debtInterestCalculator);
            final List<Debt> debts = debtInterestCalculator.getDebtsOrderedByDueDate();
            final List<EventPaymentCodeEntry> paymentCodeEntries = getSortedEventPaymentCodeEntries(event);

            model.addAttribute("eventId", event.getExternalId());
            model.addAttribute("description", event.getDescription());
            model.addAttribute("amount", debtInterestCalculator.getDebtAmount());
            model.addAttribute("creationDate", event.getWhenOccured());
            model.addAttribute("totalDueAmount", debtInterestCalculator.getTotalDueAmount());
            model.addAttribute("interests", interests);
            model.addAttribute("fines", fines);
            model.addAttribute("debts", debts);
            model.addAttribute("paymentCodeEntries", paymentCodeEntries);

            return view("event-payment-options");
        }
        else {
            // no interests
            final EventPaymentCodeEntry paymentCodeEntry = EventPaymentCodeEntry.getOrCreateReusable(event);
            return String.format("redirect:%s/%s/paymentRef/%s", REQUEST_MAPPING, event.getExternalId(), paymentCodeEntry.getExternalId());
        }

    }

    @RequestMapping(value = "{event}/paymentRef/{paymentCodeEntry}")
    public String showPaymentReference(@PathVariable Event event, @PathVariable EventPaymentCodeEntry paymentCodeEntry, Model model, User loggedUser) {
        accessControlService.checkEventOwner(event, loggedUser);

        model.addAttribute("paymentCodeEntry", paymentCodeEntry);
        model.addAttribute("maxDaysBetweenPromiseAndPayment", FenixEduAcademicConfiguration.getConfiguration().getMaxDaysBetweenPromiseAndPayment());
        return view("payment-reference");
    }

    @RequestMapping(value = "{event}/pay", method = RequestMethod.POST)
    public String generatePaymentCodeEntry(@PathVariable Event event, @RequestParam BigDecimal totalAmount, Model model, User loggedUser) {
        accessControlService.checkEventOwner(event, loggedUser);

        final long currentNewCodes = event.getEventPaymentCodeEntrySet().stream().filter(entry -> entry.getPaymentCode().isNew()).count();
        final Integer maxNewPaymentCodesPerEvent = FenixEduAcademicConfiguration.getConfiguration().getMaxNewPaymentCodesPerEvent();

        if (currentNewCodes == maxNewPaymentCodesPerEvent) {
            model.addAttribute("error", BundleUtil.getString(Bundle.ACADEMIC, "block.entry.creation.max.new.payment.codes", maxNewPaymentCodesPerEvent.toString()));
            return doPayment(event, model, loggedUser);
        }
        else if (totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            model.addAttribute("error", BundleUtil.getString(Bundle.ACADEMIC, "block.entry.creation.not.positive.total.amount"));
            return doPayment(event, model, loggedUser);
        }
        else {
            final EventPaymentCodeEntry paymentCodeEntry = EventPaymentCodeEntry.create(event, new Money(totalAmount));
            return String.format("redirect:%s/%s/paymentRef/%s", REQUEST_MAPPING, event.getExternalId(), paymentCodeEntry.getExternalId());
        }
    }

    private List<EventPaymentCodeEntry> getSortedEventPaymentCodeEntries(Event event) {
        return event.getEventPaymentCodeEntrySet().stream()
                .sorted(Comparator.comparing(EventPaymentCodeEntry::getCreated).reversed())
                .collect(Collectors.toList());
    }

    private List<Interest> getInterests(DebtInterestCalculator debtInterestCalculator) {
        return debtInterestCalculator.getDebtsOrderedByDueDate().stream()
                .flatMap(d -> d.getInterests().stream())
                .collect(Collectors.toList());
    }

    private List<Fine> getFines(DebtInterestCalculator debtInterestCalculator) {
        return debtInterestCalculator.getDebtsOrderedByDueDate().stream()
                .flatMap(d -> d.getFines().stream())
                .collect(Collectors.toList());
    }
}