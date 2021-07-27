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

import java.util.stream.Collectors;

import org.fenixedu.academic.domain.student.StatuteType;
import org.fenixedu.academic.ui.spring.ControllerHelper;
import org.fenixedu.academic.ui.spring.controller.AcademicAdministrationSpringApplication;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.commons.i18n.LocalizedString;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@SpringFunctionality(app = AcademicAdministrationSpringApplication.class, title = "label.title.StatuteTypeManagement",
        accessGroup = "#managers")
@RequestMapping("/academic/configuration/statutes")
public class StatuteTypeController {

    @RequestMapping
    public String list(Model model) {
        model.addAttribute("statutes", StatuteType.readAll().sorted(StatuteType.COMPARATOR_BY_NAME).collect(Collectors.toList()));
        return "fenixedu-academic/configuration/statutes/search";
    }

    @RequestMapping(value = "/{statuteType}")
    public String read(@PathVariable StatuteType statuteType, Model model) {
        model.addAttribute("statuteType", statuteType);
        return "fenixedu-academic/configuration/statutes/read";
    }

    @RequestMapping(value = "/{statuteType}/delete", method = RequestMethod.POST)
    public String delete(@PathVariable StatuteType statuteType, Model model, RedirectAttributes redirectAttributes) {
        try {
            atomic(() -> statuteType.delete());
        } catch (DomainException ex) {
            //Add error messages to the list
            ControllerHelper.addErrorMessage(
                    BundleUtil.getString(Bundle.APPLICATION, "error.studentStatute.delete") + ex.getMessage(), model);
            return "/academic/configuration/statutes/";
        }

        ControllerHelper.addInfoMessage(BundleUtil.getString(Bundle.APPLICATION, "success.studentStatute.delete"), model);
        return ControllerHelper.redirect("/academic/configuration/statutes/", model, redirectAttributes);
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
        return "fenixedu-academic/configuration/statutes/create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@RequestParam String code,
                         @RequestParam LocalizedString name,
                         @RequestParam(required = false) boolean active,
                         @RequestParam(required = false) boolean visible,
                         @RequestParam(required = false) boolean specialSeasonGranted,
                         @RequestParam(required = false) boolean extraordinarySeasonGranted,
                         @RequestParam(required = false) boolean explicitCreation,
                         @RequestParam(required = false) boolean workingStudentStatute,
                         @RequestParam(required = false) boolean associativeLeaderStatute,
                         @RequestParam(required = false) boolean specialSeasonGrantedByRequest,
                         @RequestParam(required = false) boolean grantOwnerStatute,
                         @RequestParam(required = false) boolean seniorStatute,
                         @RequestParam(required = false) boolean handicappedStatute,
                         Model model) throws Exception {

        try {
            StatuteType statuteType =
                    atomic(() -> new StatuteType(code, name, workingStudentStatute, associativeLeaderStatute,
                            specialSeasonGrantedByRequest, grantOwnerStatute, seniorStatute, handicappedStatute, active,
                            explicitCreation, visible, specialSeasonGranted, extraordinarySeasonGranted));
            return "redirect:/academic/configuration/statutes/" + statuteType.getExternalId();
        } catch (DomainException ex) {
            ControllerHelper.addErrorMessage(ex.getLocalizedMessage(), model);
            return create(model);
        }
    }

    @RequestMapping(value = "/{statuteType}/edit", method = RequestMethod.GET)
    public String edit(@PathVariable StatuteType statuteType, Model model) {
        model.addAttribute("statuteType", statuteType);
        return "fenixedu-academic/configuration/statutes/update";
    }

    @RequestMapping(value = "/{statuteType}/edit", method = RequestMethod.POST)
    public String edit(@PathVariable StatuteType statuteType, @RequestParam String code, @RequestParam LocalizedString name,
            @RequestParam(required = false) boolean active, @RequestParam(required = false) boolean visible, @RequestParam(
                    required = false) boolean specialSeasonGranted, @RequestParam(required = false) boolean explicitCreation,
            @RequestParam(required = false) boolean workingStudentStatute,
            @RequestParam(required = false) boolean associativeLeaderStatute,
            @RequestParam(required = false) boolean specialSeasonGrantedByRequest,
            @RequestParam(required = false) boolean grantOwnerStatute, @RequestParam(required = false) boolean seniorStatute,
            @RequestParam(required = false) boolean handicappedStatute, Model model, RedirectAttributes redirectAttributes) {

        try {
            atomic(() -> {
                statuteType.setCode(code);
                statuteType.setName(name);
                statuteType.setActive(active);
                statuteType.setVisible(visible);
                statuteType.setSpecialSeasonGranted(specialSeasonGranted);
                statuteType.setExplicitCreation(explicitCreation);
                statuteType.setWorkingStudentStatute(workingStudentStatute);
                statuteType.setAssociativeLeaderStatute(associativeLeaderStatute);
                statuteType.setSpecialSeasonGrantedByRequest(specialSeasonGrantedByRequest);
                statuteType.setGrantOwnerStatute(grantOwnerStatute);
                statuteType.setSeniorStatute(seniorStatute);
                statuteType.setHandicappedStatute(handicappedStatute);
            });
            return "redirect:/academic/configuration/statutes/" + statuteType.getExternalId();
        } catch (DomainException ex) {
            ControllerHelper.addErrorMessage(ex.getLocalizedMessage(), model);
            return edit(statuteType, model);
        }
    }

}
