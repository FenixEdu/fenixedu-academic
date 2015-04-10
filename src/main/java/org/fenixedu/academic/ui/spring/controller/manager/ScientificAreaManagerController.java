/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.spring.controller.manager;

import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.CompetenceCourseGroupUnit;
import org.fenixedu.academic.domain.organizationalStructure.ScientificAreaUnit;
import org.fenixedu.academic.json.adapters.ScientificAreaUnitJsonAdapter;
import org.fenixedu.bennu.core.rest.JsonAwareResource;
import org.fenixedu.bennu.spring.portal.BennuSpringController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pt.ist.fenixframework.FenixFramework;

import com.google.gson.JsonObject;

@RestController
@BennuSpringController(SchoolManagementController.class)
@RequestMapping("/scientificArea-manager")
public class ScientificAreaManagerController extends JsonAwareResource {

    @RequestMapping(value = "/scientificAreas/department/{department}", method = RequestMethod.GET)
    public String showScientificAreaUnits(@PathVariable Department department) {
        return view(department.getDepartmentUnit().getScientificAreaUnits());
    }

    @RequestMapping(value = "/scientificArea/{scientificArea}", method = RequestMethod.GET)
    public String showScientificAreaUnit(@PathVariable ScientificAreaUnit scientificArea) {
        return view(scientificArea);
    }

    @RequestMapping(value = "/scientificArea", method = RequestMethod.POST)
    public ResponseEntity<String> createScientificAreaUnit(@RequestBody String scientificAreaJson) {
        create(scientificAreaJson, ScientificAreaUnit.class);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = "/scientificArea/{scientificAreaUnit}", method = RequestMethod.POST)
    public ResponseEntity<String> editScientificAreaUnit(@RequestBody String scientificAreaJson,
            @PathVariable ScientificAreaUnit scientificAreaUnit) {
        update(scientificAreaJson, scientificAreaUnit, ScientificAreaUnitJsonAdapter.class);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = "/scientificArea/{competenceCourse}/delete", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteScientificAreaUnit(@PathVariable ScientificAreaUnit scientificAreaUnit) {
        try {
            FenixFramework.atomic(() -> scientificAreaUnit.delete());
            return new ResponseEntity<String>(HttpStatus.OK);
        } catch (DomainException dme) {
            return new ResponseEntity<String>(createErrorJson(dme.getLocalizedMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/scientificArea/competenceCourse/{competenceCourseGroupUnit}", method = RequestMethod.GET)
    public String showCompetenceCourse(@PathVariable CompetenceCourseGroupUnit competenceCourseGroupUnit) {
        return view(competenceCourseGroupUnit);
    }

    @RequestMapping(value = "/scientificArea/competenceCourse/", method = RequestMethod.POST)
    public ResponseEntity<String> createCompetenceCourseGroupUnit(@RequestBody String competenceCourseGroupUnitJson) {
        create(competenceCourseGroupUnitJson, CompetenceCourseGroupUnit.class);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = "/scientificArea/competenceCourse/{competenceCourseGroupUnit}", method = RequestMethod.POST)
    public ResponseEntity<String> updateCompetenceCourseGroupUnit(@RequestBody String competenceCourseGroupUnitJson,
            @PathVariable CompetenceCourseGroupUnit competenceCourseGroupUnit) {
        update(competenceCourseGroupUnitJson, competenceCourseGroupUnit);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = "/scientificArea/competenceCourse/{competenceCourseGroupUnit}/delete", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteCompetenceCourseGroupUnit(
            @PathVariable CompetenceCourseGroupUnit competenceCourseGroupUnit) {
        try {
            FenixFramework.atomic(() -> competenceCourseGroupUnit.delete());
            return new ResponseEntity<String>(HttpStatus.OK);
        } catch (DomainException dme) {
            return new ResponseEntity<String>(createErrorJson(dme.getLocalizedMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    private String createErrorJson(String message) {
        JsonObject object = new JsonObject();
        object.addProperty("message", message);
        return object.toString();
    }

}
