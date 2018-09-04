package org.fenixedu.academic.ui.spring.controller;

import org.fenixedu.academic.domain.accounting.InterestRate;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.ui.spring.controller.manager.InterestRateService;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.bennu.spring.portal.SpringApplication;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
@SpringApplication(path = "interestRates", hint = "Manager", group = "#managers", title = "title.manage.interestRate")
@SpringFunctionality(app = ManageInterestRateController.class, title = "title.manage.interestRate")
@RequestMapping("/interest-management")
public class ManageInterestRateController {

    @Autowired
    private InterestRateService interestRateService;

    private String redirectHome() {
        return "redirect:/interest-management";
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("interestRates", interestRateService.getAllInterestRates());
        return "manager/interestRates/list";
    }

    @RequestMapping(value="create", method = RequestMethod.GET)
    public String create(Model model) {
        return "manager/interestRates/create";
    }

    @RequestMapping(value="create", method = RequestMethod.POST)
    public String create(Model model,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @RequestParam LocalDate startDate,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @RequestParam LocalDate endDate,
            @RequestParam BigDecimal value) {
        try {
            interestRateService.createInterestRate(startDate, endDate, value);
            return redirectHome();
        } catch (FenixActionException fe) {
            return create(model);
        } catch (DomainException de) {
            model.addAttribute("error", de.getLocalizedMessage());
            return create(model);
        }
    }

    @RequestMapping(value="{interestRate}", method = RequestMethod.GET)
    public String edit(Model model, @PathVariable InterestRate interestRate) {
        model.addAttribute("interestRate", interestRate);
        return "manager/interestRates/create";
    }

    @RequestMapping(value="{interestRate}", method = RequestMethod.POST)
    public String edit(Model model,
            @PathVariable InterestRate interestRate,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @RequestParam LocalDate startDate,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @RequestParam LocalDate endDate,
            @RequestParam BigDecimal value) {
        try {
            interestRateService.editInterestRate(interestRate, startDate, endDate, value);
            return redirectHome();
        } catch (FenixActionException fe) {
            return edit(model, interestRate);
        } catch (DomainException de) {
            model.addAttribute("error", de.getLocalizedMessage());
            return edit(model, interestRate);
        }
    }

    @RequestMapping(value="logs", method = RequestMethod.GET)
    public String listLogs(Model model) {
        model.addAttribute("interestRatesLogs", interestRateService.getAllInterestRatesLogs());
        return "manager/interestRates/listLogs";
    }
}