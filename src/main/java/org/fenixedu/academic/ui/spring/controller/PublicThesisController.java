package org.fenixedu.academic.ui.spring.controller;

import org.fenixedu.academic.domain.thesis.Thesis;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;

@Controller
@RequestMapping("/thesis")
public class PublicThesisController {

    @RequestMapping("/{thesisId}")
    public String showThesis(@PathVariable String thesisId, Model model) {
        DomainObject obj = FenixFramework.getDomainObject(thesisId);
        if (obj instanceof Thesis) {
            model.addAttribute("thesis", obj);
        }
        return "fenixedu-academic/public/showThesis";
    }
}
