/**
 * Copyright © 2017 Instituto Superior Técnico
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

package org.fenixedu.academic.ui.spring.controller;

import org.fenixedu.academic.domain.Country;
import org.fenixedu.academic.ui.spring.controller.manager.CountryService;
import org.fenixedu.bennu.spring.portal.SpringApplication;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.commons.i18n.LocalizedString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

@SpringApplication(path = "countries", hint = "Manager", group = "#managers", title = "title.manage.countries")
@SpringFunctionality(app = ManageCountriesController.class, title = "title.manage.countries")
@RequestMapping("/country-management")
public class ManageCountriesController {

    @Autowired
    private CountryService countryService;

    @Autowired
    private MessageSource messageService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("countries", countryService.getAllCountries());
        return "manager/countries/list";
    }

    @RequestMapping(value = "/{country}/edit", method = RequestMethod.GET)
    public String showCountry(Model model, @PathVariable Country country) {
        model.addAttribute("country", country);
        return "manager/countries/edit";
    }

    @RequestMapping(value = "/{country}/edit", method = RequestMethod.POST)
    public String edit(Locale loc, @PathVariable Country country,
            @RequestParam(value = "localizedName") LocalizedString localizedName,
            @RequestParam(value = "countryNationality") LocalizedString countryNationality,
            RedirectAttributes attributes) {
        countryService.editCountry(country, localizedName, countryNationality);
        attributes.addFlashAttribute("success", messageService.getMessage("label.country.saved", null, loc));
        return "redirect:/country-management/" + country.getExternalId() + "/edit";
    }

}
