package org.fenixedu.academic.ui.spring.controller;

import org.fenixedu.academic.domain.accounting.AccountingTransaction;
import org.fenixedu.academic.domain.accounting.Discount;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.Exemption;
import org.fenixedu.academic.domain.accounting.calculator.DebtInterestCalculator;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.accounting.AnnulAccountingTransactionBean;
import org.fenixedu.academic.dto.accounting.DepositAmountBean;
import org.fenixedu.academic.predicate.AcademicPredicates;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.service.services.accounting.AnnulAccountingTransaction;
import org.fenixedu.academic.service.services.accounting.DeleteExemption;
import org.fenixedu.academic.ui.spring.service.AccountingManagementAccessControlService;
import org.fenixedu.academic.ui.spring.service.AccountingManagementService;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;
import pt.ist.fenixframework.DomainObject;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


/**
 * Created by Sérgio Silva (hello@fenixedu.org).
 */
//@SpringApplication(path = "accounting", hint = "Accounting", group = "#managers", title = "title.manage.countries")
@Controller
@SpringFunctionality(app = AcademicAdministrationSpringApplication.class, title = "title.accounting.management")
@RequestMapping(AccountingEventsPaymentManagerController.REQUEST_MAPPING)
public class AccountingEventsPaymentManagerController extends AccountingController {
    static final String REQUEST_MAPPING = "/accounting-management";

    @Autowired
    public AccountingEventsPaymentManagerController(AccountingManagementService accountingManagementService, AccountingManagementAccessControlService accountingManagementAccessControlService, ServletContext servletContext) {
        super(accountingManagementService, accountingManagementAccessControlService, servletContext);
    }

    @Override
    public String entrypointUrl() {
        return REQUEST_MAPPING + "/back/{user}";
    }

    @RequestMapping("{event}/summary")
    public String summary(@PathVariable Event event, User user, Model model) {
        accessControlService.checkPaymentManager(event, user);
        final DebtInterestCalculator debtInterestCalculator = event.getDebtInterestCalculator(new DateTime());
        model.addAttribute("entrypointUrl", entrypointUrl());
        model.addAttribute("eventUsername", event.getPerson().getUsername());
        model.addAttribute("creditEntries", debtInterestCalculator.getCreditEntries());
        model.addAttribute("debtCalculator", debtInterestCalculator);
        return view("event-summary");
    }

    @RequestMapping("back/{user}")
    public void view(@PathVariable User user, HttpSession httpSession, HttpServletResponse response) {
        //TODO : fix this with new entrypoint interface for manager (lets deprecate it)
        final String url = "/academicAdministration/paymentsManagement.do?method=showEvents&personId=" + user.getPerson().getExternalId();
        try {
            response.sendRedirect(servletContext.getContextPath() + GenericChecksumRewriter.injectChecksumInUrl(servletContext.getContextPath(), url, httpSession));
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    @RequestMapping("{event}/delete/{transaction}")
    public String delete(@PathVariable DomainObject transaction, @PathVariable Event event, User user, Model model) {
        accessControlService.checkPaymentManager(event, user);
        if (transaction instanceof AccountingTransaction) {
            model.addAttribute("annulAccountingTransactionBean", new AnnulAccountingTransactionBean((AccountingTransaction) transaction));
            model.addAttribute("event", event);
            return view("annulTransaction");
        }
        else if (transaction instanceof Exemption){
            try {
                DeleteExemption.run((Exemption) transaction);
            }
            catch (DomainException e) {
                model.addAttribute("error", e.getLocalizedMessage());
            }
        }
        else if (transaction instanceof Discount) {
            try {
                AccessControl.check(AcademicPredicates.MANAGE_STUDENT_PAYMENTS);
                ((Discount) transaction).delete();
            } catch (DomainException e) {
                model.addAttribute("error", e.getLocalizedMessage());
            }
        }
        else {
            throw new UnsupportedOperationException(String.format("Can't delete unknown transaction %s%n", transaction.getClass
                    ().getSimpleName()));
        }
        return redirectToEventDetails(event);
    }

    @RequestMapping(value = "{event}/deleteTransaction", method = RequestMethod.POST)
    public String deleteTransaction(@PathVariable Event event, User user, Model model,
            @ModelAttribute AnnulAccountingTransactionBean annulAccountingTransactionBean){
        accessControlService.checkPaymentManager(event, user);
        try {
            AnnulAccountingTransaction.run(annulAccountingTransactionBean);
        }
        catch (DomainException e){
            model.addAttribute("error", e.getLocalizedMessage());
        }
        return redirectToEventDetails(event);
    }


    @RequestMapping(value = "{event}/deposit", method = RequestMethod.GET)
    public String deposit(@PathVariable Event event, User user, Model model){
        accessControlService.checkPaymentManager(event, user);

        model.addAttribute("person", event.getPerson());
        model.addAttribute("event", event);
        model.addAttribute("depositAmountBean", new DepositAmountBean());

        return view("event-deposit");
    }

    @RequestMapping(value = "{event}/depositAmount", method = RequestMethod.POST)
    public String depositAmount(@PathVariable Event event, User user, Model model, @ModelAttribute DepositAmountBean depositAmountBean) {
        accessControlService.checkPaymentManager(event, user);

        try {
            accountingManagementService.depositAmount(event, user, depositAmountBean);
        }
        catch (DomainException e) {
            model.addAttribute("error", e.getLocalizedMessage());
            return deposit(event, user, model);
        }

        return redirectToEventDetails(event);
    }

    @RequestMapping(value = "{event}/cancel", method = RequestMethod.GET)
    public String cancel(@PathVariable Event event, User user, Model model) {
        accessControlService.checkPaymentManager(event, user);

        model.addAttribute("person", event.getPerson());
        model.addAttribute("event", event);

        return view("event-cancel");
    }

    @RequestMapping(value = "{event}/cancelEvent", method = RequestMethod.POST)
    public String cancelEvent(@PathVariable Event event, User user, Model model, @RequestParam String justification) {
        accessControlService.checkPaymentManager(event, user);

        try {
            accountingManagementService.cancelEvent(event, user.getPerson(), justification);
        }
        catch (DomainException e) {
            model.addAttribute("error", e.getLocalizedMessage());
            return cancel(event, user, model);
        }

        return redirectToEventDetails(event);
    }

    private String redirectToEventDetails(@PathVariable Event event) {
        return String.format("redirect:/%s/%s/details", REQUEST_MAPPING, event.getExternalId());
    }
}