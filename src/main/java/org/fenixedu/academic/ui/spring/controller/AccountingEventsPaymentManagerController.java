package org.fenixedu.academic.ui.spring.controller;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.AccountingTransaction;
import org.fenixedu.academic.domain.accounting.Discount;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.Exemption;
import org.fenixedu.academic.domain.accounting.PaymentMethod;
import org.fenixedu.academic.domain.accounting.calculator.DebtInterestCalculator;
import org.fenixedu.academic.domain.accounting.events.EventExemptionJustificationType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.accounting.AnnulAccountingTransactionBean;
import org.fenixedu.academic.dto.accounting.CreateExemptionBean;
import org.fenixedu.academic.dto.accounting.CreateExemptionBean.ExemptionType;
import org.fenixedu.academic.dto.accounting.DepositAmountBean;
import org.fenixedu.academic.dto.accounting.EntryDTO;
import org.fenixedu.academic.dto.accounting.PaymentsManagementDTO;
import org.fenixedu.academic.predicate.AcademicPredicates;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.service.services.accounting.AnnulAccountingTransaction;
import org.fenixedu.academic.service.services.accounting.CreatePaymentsForEvents;
import org.fenixedu.academic.service.services.accounting.DeleteExemption;
import org.fenixedu.academic.ui.spring.service.AccountingManagementAccessControlService;
import org.fenixedu.academic.ui.spring.service.AccountingManagementService;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pt.ist.fenixframework.DomainObject;
/**
 * Created by Sérgio Silva (hello@fenixedu.org).
 */
//@SpringApplication(path = "accounting", hint = "Accounting", group = "#managers", title = "title.manage.countries")
@Controller
@SpringFunctionality(app = AcademicAdministrationSpringApplication.class, title = "title.accounting.management")
@RequestMapping(AccountingEventsPaymentManagerController.REQUEST_MAPPING)
public class AccountingEventsPaymentManagerController extends AccountingController {
    private static final Logger logger = LoggerFactory.getLogger(AccountingEventsPaymentManagerController.class);

    public static final String REQUEST_MAPPING = "/accounting-management";
    private static final String MULTIPLE_PAYMENTS_ATTRIBUTE = "multiplePayments_";

    @Autowired
    public AccountingEventsPaymentManagerController(AccountingManagementService accountingManagementService,
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

    @RequestMapping("{event}/summary")
    public String summary(@PathVariable Event event, User user, Model model) {
        accessControlService.checkPaymentManager(event, user);
        final DebtInterestCalculator debtInterestCalculator = event.getDebtInterestCalculator(new DateTime());
        model.addAttribute("event", event);
        model.addAttribute("entrypointUrl", entrypointUrl());
        model.addAttribute("creditEntries", debtInterestCalculator.getCreditEntries());
        model.addAttribute("debtCalculator", debtInterestCalculator);
        return view("event-summary");
    }

    @RequestMapping("{event}/delete/{transaction}")
    public String delete(@PathVariable DomainObject transaction, @PathVariable Event event, User user, Model model) {
        accessControlService.checkAdvancedPaymentManager(event, user);
        if (transaction instanceof AccountingTransaction) {
            model.addAttribute("annulAccountingTransactionBean", new AnnulAccountingTransactionBean((AccountingTransaction) transaction));
            model.addAttribute("event", event);
            return view("event-annul-transaction");
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
        accessControlService.checkAdvancedPaymentManager(event, user);
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

        model.addAttribute("paymentMethods", PaymentMethod.all());
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
        accessControlService.checkAdvancedPaymentManager(event, user);

        model.addAttribute("person", event.getPerson());
        model.addAttribute("event", event);

        return view("event-cancel");
    }

    @RequestMapping(value = "{event}/cancelEvent", method = RequestMethod.POST)
    public String cancelEvent(@PathVariable Event event, User user, Model model, @RequestParam String justification) {
        accessControlService.checkAdvancedPaymentManager(event, user);

        try {
            accountingManagementService.cancelEvent(event, user.getPerson(), justification);
        }
        catch (DomainException e) {
            model.addAttribute("error", e.getLocalizedMessage());
            return cancel(event, user, model);
        }

        return redirectToEventDetails(event);
    }

    @RequestMapping(value = "{event}/exempt", method = RequestMethod.GET)
    public String exempt(@PathVariable Event event, User user, Model model) {
        accessControlService.checkAdvancedPaymentManager(event, user);

        final DebtInterestCalculator calculator = event.getDebtInterestCalculator(new DateTime());

        if (calculator.getTotalDueAmount().compareTo(BigDecimal.ZERO) == 0) {
            return redirectToEventDetails(event);
        }

        model.addAttribute("event", event);
        model.addAttribute("person", event.getPerson());

        final Map<ExemptionType, BigDecimal> exemptionTypeAmountMap = new HashMap<>();
        exemptionTypeAmountMap.put(ExemptionType.DEBT, calculator.getDueAmount());
        exemptionTypeAmountMap.put(ExemptionType.INTEREST, calculator.getDueInterestAmount());
        exemptionTypeAmountMap.put(ExemptionType.FINE, calculator.getDueFineAmount());

        model.addAttribute("exemptionTypeAmountMap", exemptionTypeAmountMap);
        model.addAttribute("createExemptionBean", new CreateExemptionBean());
        model.addAttribute("eventExemptionJustificationTypes", EventExemptionJustificationType.values());

        return view("event-create-exemption");
    }

    @RequestMapping(value = "{event}/createExemption", method = RequestMethod.POST)
    public String createExemption(@PathVariable Event event, User user, Model model, @ModelAttribute CreateExemptionBean createExemptionBean){
        accessControlService.checkAdvancedPaymentManager(event, user);

        try {
            accountingManagementService.exemptEvent(event, user.getPerson(), createExemptionBean);
        }
        catch (DomainException e) {
            model.addAttribute("error", e.getLocalizedMessage());
            return exempt(event, user, model);
        }

        return redirectToEventDetails(event);
    }

    @RequestMapping(value = "{person}/multiplePayments/select", method = RequestMethod.GET)
    public String prepareMultiplePayments(@PathVariable Person person, HttpSession httpSession, Model model, User loggedUser){
        accessControlService.isPaymentManager(loggedUser);

        PaymentsManagementDTO paymentsManagementDTO = new PaymentsManagementDTO(person);
        person.getEventsSet().stream().filter(e -> !e.isCancelled()).map(Event::calculateEntries).forEach
                (paymentsManagementDTO::addEntryDTOs);

        if (paymentsManagementDTO.getTotalAmountToPay().lessOrEqualThan(Money.ZERO)) {
            logger.warn("Hacky user {} tried to access multiple payments interface for user {}",
                    Optional.ofNullable(loggedUser).map(User::getUsername).orElse("---"), person.getUsername());
            return "redirect:" + REQUEST_MAPPING + "/" + person.getExternalId();
        }
        // Show penalties first then order by due date
        paymentsManagementDTO.getEntryDTOs().sort(Comparator.comparing(EntryDTO::isForPenalty).reversed().thenComparing(EntryDTO::getDueDate));

        final String uuid = UUID.randomUUID().toString();

        httpSession.setAttribute(MULTIPLE_PAYMENTS_ATTRIBUTE + uuid, paymentsManagementDTO);
        model.addAttribute("multiplePayments", uuid);
        model.addAttribute("paymentMethods", PaymentMethod.all());
        model.addAttribute("paymentsManagementDTO", paymentsManagementDTO);
        model.addAttribute("person", person);

        return view("events-multiple-payments-prepare");
    }

    @RequestMapping(value = "{person}/multiplePayments/confirm", method = RequestMethod.POST)
    public String confirmMultiplePayments(@PathVariable Person person, HttpSession httpSession, Model model, User loggedUser,
            @RequestParam String identifier, @RequestParam PaymentMethod paymentMethod, @RequestParam String paymentReference,
            @RequestParam List<String> entries) {
        
        accessControlService.isPaymentManager(loggedUser);

        PaymentsManagementDTO paymentsManagementDTO = (PaymentsManagementDTO) httpSession.getAttribute(
                MULTIPLE_PAYMENTS_ATTRIBUTE + identifier);

        paymentsManagementDTO.setPaymentMethod(paymentMethod);
        paymentsManagementDTO.setPaymentReference(paymentReference);
        paymentsManagementDTO.select(entries);
        paymentsManagementDTO.setPaymentDate(new DateTime());

//        entryDTOS.setPaymentDate(DateTime.now());
        model.addAttribute("multiplePayments", identifier);
        model.addAttribute("paymentsManagementDTO", paymentsManagementDTO);
        model.addAttribute("person", person);

        return view("events-multiple-payments-confirm");
    }

    @RequestMapping(value = "{person}/multiplePayments/register", method = RequestMethod.POST)
    public String registerMultiplePayments(@PathVariable Person person, HttpSession httpSession, Model model, User loggedUser,
            @RequestParam String identifier, RedirectAttributes redirectAttributes) {

        accessControlService.isPaymentManager(loggedUser);

        final String attribute = MULTIPLE_PAYMENTS_ATTRIBUTE + identifier;

        PaymentsManagementDTO paymentsManagementDTO = (PaymentsManagementDTO) httpSession.getAttribute(
                attribute);

        CreatePaymentsForEvents.run(loggedUser, paymentsManagementDTO.getSelectedEntries(), paymentsManagementDTO
                .getPaymentMethod(), paymentsManagementDTO.getPaymentReference(), new DateTime());

        httpSession.removeAttribute(attribute);
        redirectAttributes.addFlashAttribute(paymentsManagementDTO);

        return "redirect:" + REQUEST_MAPPING + "/" + person.getExternalId() + "/multiplePayments/conclude";
    }

    @RequestMapping(value = "{person}/multiplePayments/conclude", method = RequestMethod.GET)
    public String concludeMultiplePayments(@PathVariable Person person, Model model, User loggedUser, @ModelAttribute
            PaymentsManagementDTO paymentsManagementDTO) {

        accessControlService.isPaymentManager(loggedUser);
        model.addAttribute("paymentsManagementDTO", paymentsManagementDTO);
        model.addAttribute("person", person);
        model.addAttribute("success", true);
        return view("events-multiple-payments-confirm");
    }


    private String redirectToEventDetails(@PathVariable Event event) {
        return String.format("redirect:%s/%s/details", REQUEST_MAPPING, event.getExternalId());
    }
}