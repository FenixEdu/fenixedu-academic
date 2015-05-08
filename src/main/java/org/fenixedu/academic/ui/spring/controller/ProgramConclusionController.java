package org.fenixedu.academic.ui.spring.controller;

import org.fenixedu.academic.domain.accounting.EventTypes;
import org.fenixedu.academic.domain.degreeStructure.ProgramConclusion;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationStateType;
import org.fenixedu.academic.ui.spring.service.ProgramConclusionService;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.commons.i18n.LocalizedString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Strings;

/**
 * Program Conclusion Management Functionality
 * 
 * 
 * @author Sérgio Silva (sergio.silva@tecnico.ulisboa.pt)
 *
 */
@SpringFunctionality(app = AcademicAdministrationSpringApplication.class, title = "program.conclusion.title",
        accessGroup = "academic(MANAGE_DEGREE_CURRICULAR_PLANS)")
@RequestMapping("/program-conclusion-management")
public class ProgramConclusionController {

    @Autowired
    ProgramConclusionService service;

    private String view(String jspfile) {
        return "fenixedu-academic/program-conclusion/" + jspfile;
    }

    protected String redirectHome() {
        return "redirect:/program-conclusion-management";
    }

    @RequestMapping(method = RequestMethod.GET)
    public String home(Model model) {
        model.addAttribute("conclusions", service.getProgramConclusions());
        return view("show");
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
        model.addAttribute("registrationStates", RegistrationStateType.values());
        return view("create");
    }

    private RegistrationStateType getRegistrationStateType(String targetState) {
        return Strings.isNullOrEmpty(targetState) ? null : RegistrationStateType.valueOf(targetState);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/create")
    public String create(Model model, @RequestParam LocalizedString name,
            @RequestParam(defaultValue = "{}") LocalizedString description,
            @RequestParam(defaultValue = "{}") LocalizedString graduationTitle,
            @RequestParam(defaultValue = "{}") LocalizedString graduationLevel,
            @RequestParam(defaultValue = "false") boolean isAverageEditable,
            @RequestParam(defaultValue = "false") boolean isAlumniProvider,
            @RequestParam(defaultValue = "false") boolean isSkipValidation, @RequestParam(defaultValue = "") String targetState) {
        try {
            service.createProgramConclusion(name, description, graduationTitle, graduationLevel, isAverageEditable,
                    isAlumniProvider, isSkipValidation, getRegistrationStateType(targetState), new EventTypes());
            return "redirect:/program-conclusion-management";
        } catch (DomainException de) {
            model.addAttribute("error", de.getLocalizedMessage());
            return create(model);
        }
    }

    @RequestMapping(value = "/{programConclusion}", method = RequestMethod.GET)
    public String edit(Model model, @PathVariable ProgramConclusion programConclusion) {
        model.addAttribute("registrationStates", RegistrationStateType.values());
        model.addAttribute("programConclusion", programConclusion);
        return view("create");
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{programConclusion}")
    public String edit(Model model, @PathVariable ProgramConclusion programConclusion, @RequestParam LocalizedString name,
            @RequestParam(defaultValue = "{}") LocalizedString description,
            @RequestParam(defaultValue = "{}") LocalizedString graduationTitle,
            @RequestParam(defaultValue = "{}") LocalizedString graduationLevel,
            @RequestParam(defaultValue = "false") boolean isAverageEditable,
            @RequestParam(defaultValue = "false") boolean isAlumniProvider,
            @RequestParam(defaultValue = "false") boolean isSkipValidation, @RequestParam(defaultValue = "") String targetState) {
        try {
            service.editProgramConclusion(programConclusion, name, description, graduationTitle, graduationLevel,
                    isAverageEditable, isAlumniProvider, isSkipValidation, getRegistrationStateType(targetState));
            return "redirect:/program-conclusion-management";
        } catch (DomainException de) {
            model.addAttribute("error", de.getLocalizedMessage());
            return edit(model, programConclusion);
        }
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, value = "/{programConclusion}")
    public ResponseEntity<String> delete(Model model, @PathVariable ProgramConclusion programConclusion) {
        try {
            service.delete(programConclusion);
            return new ResponseEntity<String>(HttpStatus.ACCEPTED);
        } catch (DomainException de) {
            return new ResponseEntity<String>(de.getLocalizedMessage(), HttpStatus.PRECONDITION_FAILED);
        }
    }
}
