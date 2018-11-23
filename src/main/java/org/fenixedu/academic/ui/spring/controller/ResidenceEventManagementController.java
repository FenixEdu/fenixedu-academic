package org.fenixedu.academic.ui.spring.controller;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.ResidenceEvent;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.residence.ResidenceMonth;
import org.fenixedu.academic.domain.residence.ResidenceYear;
import org.fenixedu.academic.dto.residenceManagement.ResidenceEventBean;
import org.fenixedu.academic.dto.residenceManagement.ResidentListsHolderBean;
import org.fenixedu.academic.ui.spring.service.ResidenceEventManagementService;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@SpringFunctionality(app = ResidenceManagementSpringApplication.class, title = "title.residence.event.management")
@RequestMapping(ResidenceEventManagementController.REQUEST_MAPPING)
public class ResidenceEventManagementController {
    public static final String REQUEST_MAPPING = "/residence-event-management";
    private static final String MULTIPLE_PAYMENTS_ATTRIBUTE = "listHolder_";

    private final ResidenceEventManagementService residenceEventManagementService;

    public ResidenceEventManagementController(final ResidenceEventManagementService residenceEventManagementService) {
        this.residenceEventManagementService = residenceEventManagementService;
    }

    @RequestMapping()
    public String entrypoint(){
        return "redirect:" +  REQUEST_MAPPING + "/searchEvents";
    }

    @RequestMapping(value = "searchEvents", method = RequestMethod.GET)
    public String searchEvents(Model model, @RequestParam(required = false) ResidenceYear residenceYear,
            @RequestParam(required = false) ResidenceMonth residenceMonth) {
        if (residenceYear != null && residenceMonth != null && residenceMonth.getYear().equals(residenceYear)){
            model.addAttribute("events", getEventsSortedByPersonName(residenceMonth));
        }

        List<ResidenceYear> residenceYears = Bennu.getInstance().getResidenceYearsSet().stream().sorted(Comparator.comparing(ResidenceYear::getYear)).collect(Collectors.toList());
        model.addAttribute("residenceYears", residenceYears);
        model.addAttribute("selectedResidenceYear", residenceYear);
        model.addAttribute("selectedResidenceMonth", residenceMonth);

        return view("residence-events-search");
    }

    @RequestMapping(value = "{person}", method = RequestMethod.GET)
    public String managePersonEvents(@PathVariable Person person, Model model){

        model.addAttribute("person", person);
        model.addAttribute("personEvents", getPersonEventsMostRecentFirst(person));

        return view("residence-events-for-person");
    }

    @RequestMapping(value = "{person}/cancel", method = RequestMethod.POST)
    public String cancelEvent(@PathVariable Person person, Model model, @RequestParam ResidenceEvent event, User loggedUser) {
        try {
            residenceEventManagementService.cancelResidenceEvent(event, loggedUser.getPerson());
        }
        catch (DomainException e) {
            model.addAttribute("error", e.getLocalizedMessage());
        }

        return "redirect:" + REQUEST_MAPPING + "/" + person.getExternalId();
    }

    @RequestMapping(value = "{person}/pay", method = RequestMethod.POST)
    public String payEvent(@PathVariable Person person, Model model, @RequestParam ResidenceEvent event, User loggedUser) {
        try {
            residenceEventManagementService.payResidenceEvent(event, loggedUser);
        }
        catch (DomainException e) {
            model.addAttribute("error", e.getLocalizedMessage());
        }

        return "redirect:" + REQUEST_MAPPING + "/" + person.getExternalId();
    }
    @RequestMapping(value = "importEvents", method = RequestMethod.GET)
    public String prepareImportEvents(Model model, @RequestParam("residenceMonth") ResidenceMonth residenceMonth,
            @ModelAttribute("importList") ResidentListsHolderBean importList, @ModelAttribute("error") String error) {


        model.addAttribute("residenceMonth", residenceMonth);
        model.addAttribute("importList", importList);
        model.addAttribute("error", error);

        return view("residence-events-import");
    }

    @RequestMapping(value = "importEvents/upload", method = RequestMethod.POST)
    public String uploadFile(HttpSession httpSession, @RequestParam ResidenceMonth residenceMonth, @RequestParam MultipartFile file, RedirectAttributes redirectAttributes) {
        try {

            List<ResidenceEventBean> successful = new ArrayList<>();
            List<ResidenceEventBean> unsuccessful = new ArrayList<>();
            for (ResidenceEventBean eventBean : residenceEventManagementService.process(residenceMonth, file)) {
                if (eventBean.getStatus()) {
                    successful.add(eventBean);
                } else {
                    unsuccessful.add(eventBean);
                }
            }

            ResidentListsHolderBean listHolder = new ResidentListsHolderBean(successful, unsuccessful);
            redirectAttributes.addFlashAttribute("importList", listHolder);

            final String uuid = UUID.randomUUID().toString();
            httpSession.setAttribute(MULTIPLE_PAYMENTS_ATTRIBUTE + uuid, listHolder);
            redirectAttributes.addFlashAttribute("identifier", uuid);
        }
        catch (ResidenceEventManagementService.InvalidSpreadSheetName exception) {
            redirectAttributes.addFlashAttribute("error",
                    BundleUtil.getString(Bundle.APPLICATION,"label.error.invalid.spreadsheetname", exception.getRequestedSheet()));
        }
        catch (IOException exception) {
            redirectAttributes.addFlashAttribute("error",
                    BundleUtil.getString(Bundle.APPLICATION, "label.error.invalid.table"));
        }
        redirectAttributes.addAttribute("residenceMonth", residenceMonth.getExternalId());
        return  "redirect:" + REQUEST_MAPPING + "/importEvents";
    }

    @RequestMapping(value = "importEvents/generateDebts", method = RequestMethod.POST)
    public String generateDebts(HttpSession httpSession, @RequestParam String identifier, @RequestParam ResidenceMonth residenceMonth, RedirectAttributes redirectAttributes){

        final String attribute = MULTIPLE_PAYMENTS_ATTRIBUTE + identifier;
        ResidentListsHolderBean listsHolderBean = (ResidentListsHolderBean) httpSession.getAttribute(attribute);

        try {
            residenceEventManagementService.createResidenceEvents(listsHolderBean.getSuccessfulEvents(), residenceMonth);
        }
        catch (Exception e){
            redirectAttributes.addFlashAttribute("error", e.getLocalizedMessage());
            redirectAttributes.addFlashAttribute("importList", listsHolderBean);
            redirectAttributes.addAttribute("residenceMonth", residenceMonth.getExternalId());
            return "redirect:" + REQUEST_MAPPING + "/importEvents";
        }

        redirectAttributes.addAttribute("residenceMonth", residenceMonth.getExternalId());
        redirectAttributes.addAttribute("residenceYear", residenceMonth.getYear().getExternalId());
        return "redirect:" + REQUEST_MAPPING + "/searchEvents";
    }

    private List<Event> getPersonEventsMostRecentFirst(@PathVariable final Person person) {
        return person.getResidencePaymentEvents().stream()
                .sorted(Comparator.comparing(Event::getWhenOccured).reversed())
                .collect(Collectors.toList());
    }

    private List<ResidenceEvent> getEventsSortedByPersonName(final ResidenceMonth residenceMonth) {
        return residenceMonth.getEventsSet().stream()
                .sorted(Comparator.comparing(event -> event.getPerson().getName()))
                .collect(Collectors.toList());
    }

    private String view(String view) {
        return "fenixedu-academic/residenceManagement/" + view;
    }
}
