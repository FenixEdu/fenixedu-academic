package org.fenixedu.academic.ui.spring.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;

import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.calculator.Debt;
import org.fenixedu.academic.domain.accounting.calculator.DebtEntry;
import org.fenixedu.academic.domain.accounting.calculator.DebtInterestCalculator;
import org.fenixedu.academic.domain.accounting.calculator.Interest;
import org.fenixedu.academic.ui.spring.service.AccountingManagementAccessControlService;
import org.fenixedu.academic.ui.spring.service.AccountingManagementService;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Sérgio Silva (hello@fenixedu.org).
 */

@Controller
@SpringFunctionality(app = AcademicAdministrationSpringApplication.class, title = "title.accounting.management.entrypoint",
        accessGroup = "logged")
@RequestMapping(AccountingEventForOwnerController.REQUEST_MAPPING)
public class AccountingEventForOwnerController extends AccountingController {
    static final String REQUEST_MAPPING = "/owner-accounting-events";

    @Autowired
    public AccountingEventForOwnerController(AccountingManagementService accountingManagementService, AccountingManagementAccessControlService accountingManagementAccessControlService, ServletContext servletContext) {
        super(accountingManagementService, accountingManagementAccessControlService, servletContext);
    }

    @Override
    public String entrypointUrl() {
        return REQUEST_MAPPING + "/{user}";
    }

    @RequestMapping
    public String entrypoint() {
        return "redirect:" + REQUEST_MAPPING + "/" + Authenticate.getUser().getUsername();
    }

    @RequestMapping("{event}/pay")
    public String doPayment(@PathVariable Event event, Model model) {
        accessControlService.checkEventOwner(event, Authenticate.getUser());
        final DebtInterestCalculator debtInterestCalculator = event.getDebtInterestCalculator(new DateTime());
        final List<Interest> interests = debtInterestCalculator.getDebtsOrderedByDueDate().stream().flatMap(d -> d.getInterests
                ().stream()).filter(DebtEntry::isOpen).collect(Collectors.toList());

        List<Debt> debts = debtInterestCalculator.getDebtsOrderedByDueDate().stream()
                .filter(d -> d.getOpenAmount().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());
        model.addAttribute("eventId", event.getExternalId());
        model.addAttribute("description", event.getDescription());
        model.addAttribute("amount", debtInterestCalculator.getDebtAmount());
        model.addAttribute("creationDate", event.getWhenOccured());
        model.addAttribute("totalDueAmount", debtInterestCalculator.getTotalDueAmount());
        model.addAttribute("interests", interests);
        model.addAttribute("debts", debts);

        return view("event-payment-options");
    }
}