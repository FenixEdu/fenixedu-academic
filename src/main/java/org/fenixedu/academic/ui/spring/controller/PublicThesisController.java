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
