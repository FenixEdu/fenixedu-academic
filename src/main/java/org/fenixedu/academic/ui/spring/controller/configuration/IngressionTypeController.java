/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.spring.controller.configuration;

import static pt.ist.fenixframework.FenixFramework.atomic;

import java.util.Comparator;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.candidacy.IngressionType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.ui.spring.ControllerHelper;
import org.fenixedu.academic.ui.spring.controller.AcademicAdministrationSpringApplication;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.commons.i18n.LocalizedString;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@SpringFunctionality(app = AcademicAdministrationSpringApplication.class, title = "label.title.candidacy.manageIngressionType",
        accessGroup = "#managers")
@RequestMapping("/academic/configuration/ingression-type")
public class IngressionTypeController {

    @RequestMapping
    public String list(Model model) {
        model.addAttribute("ingressionTypes",
                Bennu.getInstance().getIngressionTypesSet().stream().sorted(Comparator.comparing(IngressionType::getDescription))
                        .collect(Collectors.toList()));
        return "fenixedu-academic/configuration/ingression-type/search";
    }

    @RequestMapping(value = "/{ingressionType}")
    public String read(@PathVariable IngressionType ingressionType, Model model) {
        model.addAttribute("ingressionType", ingressionType);
        return "fenixedu-academic/configuration/ingression-type/read";
    }

    @RequestMapping(value = "/{ingressionType}/delete", method = RequestMethod.POST)
    public String delete(@PathVariable IngressionType ingressionType, Model model, RedirectAttributes redirectAttributes) {
        try {
            atomic(() -> ingressionType.delete());
        } catch (DomainException e) {
            ControllerHelper.addErrorMessage(e.getLocalizedMessage(), model);
            return ControllerHelper.redirect("/academic/configuration/ingression-type", model, redirectAttributes);
        }

        ControllerHelper.addInfoMessage(BundleUtil.getString(Bundle.APPLICATION, "label.success.ingressionType.delete"), model);
        return ControllerHelper.redirect("/academic/configuration/ingression-type", model, redirectAttributes);
    }

    @RequestMapping(value = "/{ingressionType}/edit", method = RequestMethod.GET)
    public String edit(@PathVariable IngressionType ingressionType, Model model) {
        model.addAttribute("ingressionType", ingressionType);
        return "fenixedu-academic/configuration/ingression-type/update";
    }

    @RequestMapping(value = "/{ingressionType}/edit", method = RequestMethod.POST)
    public String edit(@PathVariable IngressionType ingressionType, @RequestParam String code,
            @RequestParam LocalizedString description, @RequestParam(required = false) boolean hasEntryPhase, @RequestParam(
                    required = false) boolean directAccessFrom1stCycle,
            @RequestParam(required = false) boolean externalDegreeChange,
            @RequestParam(required = false) boolean firstCycleAttribution,
            @RequestParam(required = false) boolean handicappedContingent,
            @RequestParam(required = false) boolean internal2ndCycleAccess,
            @RequestParam(required = false) boolean internal3rdCycleAccess,
            @RequestParam(required = false) boolean internalDegreeChange,
            @RequestParam(required = false) boolean isolatedCurricularUnits,
            @RequestParam(required = false) boolean middleAndSuperiorCourses, @RequestParam(required = false) boolean over23,
            @RequestParam(required = false) boolean reIngression, @RequestParam(required = false) boolean transfer, Model model,
            RedirectAttributes redirectAttributes) {

        try {
            atomic(() -> {
                ingressionType.setCode(code);
                ingressionType.setDescription(description);
                ingressionType.editState(hasEntryPhase, directAccessFrom1stCycle, externalDegreeChange, firstCycleAttribution,
                        handicappedContingent, internal2ndCycleAccess, internal3rdCycleAccess, internalDegreeChange,
                        isolatedCurricularUnits, middleAndSuperiorCourses, over23, reIngression, transfer);
            });
        } catch (DomainException e) {
            ControllerHelper.addErrorMessage(e.getLocalizedMessage(), model);
            model.addAttribute("ingressionType", ingressionType);
            return "fenixedu-academic/configuration/ingression-type/update";
        }

        return "redirect:/academic/configuration/ingression-type/" + ingressionType.getExternalId();
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
        return "fenixedu-academic/configuration/ingression-type/create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@RequestParam String code, @RequestParam LocalizedString description,
            @RequestParam(required = false) boolean hasEntryPhase,
            @RequestParam(required = false) boolean directAccessFrom1stCycle,
            @RequestParam(required = false) boolean externalDegreeChange,
            @RequestParam(required = false) boolean firstCycleAttribution,
            @RequestParam(required = false) boolean handicappedContingent,
            @RequestParam(required = false) boolean internal2ndCycleAccess,
            @RequestParam(required = false) boolean internal3rdCycleAccess,
            @RequestParam(required = false) boolean internalDegreeChange,
            @RequestParam(required = false) boolean isolatedCurricularUnits,
            @RequestParam(required = false) boolean middleAndSuperiorCourses, @RequestParam(required = false) boolean over23,
            @RequestParam(required = false) boolean reIngression, @RequestParam(required = false) boolean transfer, Model model,
            RedirectAttributes redirectAttributes) throws Exception {
        try {
            IngressionType ingressionType =
                    atomic(() -> {
                        IngressionType ingression = IngressionType.createIngressionType(code, description);
                        ingression.editState(hasEntryPhase, directAccessFrom1stCycle, externalDegreeChange,
                                firstCycleAttribution, handicappedContingent, internal2ndCycleAccess, internal3rdCycleAccess,
                                internalDegreeChange, isolatedCurricularUnits, middleAndSuperiorCourses, over23, reIngression,
                                transfer);
                        return ingression;
                    });
            return "redirect:/academic/configuration/ingression-type/" + ingressionType.getExternalId();
        } catch (DomainException e) {
            ControllerHelper.addErrorMessage(e.getLocalizedMessage(), model);
            return create(model);
        }
    }

}
