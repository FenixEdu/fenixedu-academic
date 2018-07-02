package org.fenixedu.academic.ui.spring.controller;

import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.calculator.DebtInterestCalculator;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Sérgio Silva (hello@fenixedu.org).
 */
//@SpringApplication(path = "accounting", hint = "Accounting", group = "#managers", title = "title.manage.countries")
@Controller
@SpringFunctionality(app = AcademicAdministrationSpringApplication.class, title = "title.accounting.management")
@RequestMapping("/accounting-management")
public class AccountingEventSummaryController {

    @RequestMapping("{event}/summary")
    public String summary(@PathVariable Event event, Model model) {
        final DebtInterestCalculator debtInterestCalculator = event.getDebtInterestCalculator(new DateTime());

        model.addAttribute("event", event);
        model.addAttribute("creditEntries", debtInterestCalculator.getAccountingEntries());
        model.addAttribute("debtCalculator", debtInterestCalculator);

        return "fenixedu-academic/accounting/event-summary";
    }

}
