package org.fenixedu.academic.ui.spring;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

public class AcademicBaseController {
    //The HTTP Request that can be used internally in the controller
    protected @Autowired HttpServletRequest request;

    //The entity in the Model

    // The list of INFO messages that can be showed on View
    protected void addInfoMessage(String message, Model m) {
        ((List<String>) m.asMap().get("infoMessages")).add(message);
    }

    // The list of WARNING messages that can be showed on View
    protected void addWarningMessage(String message, Model m) {
        ((List<String>) m.asMap().get("warningMessages")).add(message);
    }

    // The list of ERROR messages that can be showed on View
    protected void addErrorMessage(String message, Model m) {
        ((List<String>) m.asMap().get("errorMessages")).add(message);
    }

    @ModelAttribute
    protected void addModelProperties(Model model) {
        model.addAttribute("infoMessages", new ArrayList<String>());
        model.addAttribute("warningMessages", new ArrayList<String>());
        model.addAttribute("errorMessages", new ArrayList<String>());
    }
}
