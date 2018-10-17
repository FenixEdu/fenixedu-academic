package org.fenixedu.academic.ui.spring.controller;

import java.util.Collection;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.person.IDDocumentType;
import org.fenixedu.academic.dto.person.SimpleSearchPersonWithStudentBean;
import org.fenixedu.academic.ui.spring.service.AccountingManagementAccessControlService;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Sérgio Silva (hello@fenixedu.org).
 */

@Controller
@SpringFunctionality(app = AcademicAdministrationSpringApplication.class, title = "title.accounting.management.search",
        accessGroup = "academic(MANAGE_STUDENT_PAYMENTS)")
@RequestMapping("/search-accounting-events")
public class SearchAccountingPersonController {

    private final AccountingManagementAccessControlService accountingManagementAccessControlService;

    @Autowired
    public SearchAccountingPersonController(AccountingManagementAccessControlService accountingManagementAccessControlService) {
        this.accountingManagementAccessControlService = accountingManagementAccessControlService;
    }


    @RequestMapping(method = RequestMethod.GET)
    public String home(Model model, User loggedUser) {
        if (!this.accountingManagementAccessControlService.isPaymentManager(loggedUser)) {
            throw new DomainException(Response.Status.UNAUTHORIZED, "return.key.argument", "not.authorized");
        }
        model.addAttribute("searchBean", new SimpleSearchPersonWithStudentBean());
        model.addAttribute("idDocumentTypes", IDDocumentType.values());
        return view("event-search-person");
    }

    @RequestMapping(method = RequestMethod.POST)
    public String search(@ModelAttribute SimpleSearchPersonWithStudentBean searchBean, User loggedUser, Model model) {
        if (!this.accountingManagementAccessControlService.isPaymentManager(loggedUser)) {
            throw new DomainException(Response.Status.UNAUTHORIZED, "return.key.argument", "not.authorized");
        }
        
        Collection<Person> persons = searchBean.search();

        if (persons.size() == 1) {
            final Person person = persons.iterator().next();
            return "redirect:" + AccountingEventsPaymentManagerController.REQUEST_MAPPING + "/" + person.getExternalId();
        }

        if (persons.size() > 50) {
            persons = persons.stream().limit(50).collect(Collectors.toSet());
            model.addAttribute("sizeWarning", BundleUtil.getString(Bundle.ACADEMIC, "warning.need.to.filter.candidates"));
        }
        model.addAttribute("searchBean", searchBean);
        model.addAttribute("persons", persons);
        return view("event-search-person");
    }

    private String view(String view) {
        return "fenixedu-academic/accounting/" + view;
    }
    
}
