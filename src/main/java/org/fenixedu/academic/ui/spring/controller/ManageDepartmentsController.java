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

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.ui.spring.controller.manager.DepartmentBean;
import org.fenixedu.academic.ui.spring.controller.manager.DepartmentService;
import org.fenixedu.bennu.core.rest.JsonAwareResource;
import org.fenixedu.bennu.spring.portal.SpringApplication;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.commons.i18n.LocalizedString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pt.ist.fenixframework.Atomic;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@SpringApplication(path = "departments", hint = "Manager", group = "#managers", title = "label.manageDepartments")
@SpringFunctionality(app = ManageDepartmentsController.class, title = "title.departments")
@RequestMapping("/department-management")
public class ManageDepartmentsController extends JsonAwareResource {

    @Autowired
    private DepartmentService departmentService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        return "manager/departments/manageDepartments";
    }

    @RequestMapping(value = "departments.json")
    public @ResponseBody JsonElement getDepartmentsSet() {
        return view(DepartmentService.getAllDepartments());
    }

    @RequestMapping(value = "/createDepartment", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<String> addDepartment(@RequestBody String departmentJson) {
        try {
            departmentService.addDepartment(createDepartmentBeanFromJSON(departmentJson));
            return new ResponseEntity<String>(HttpStatus.OK);
        } catch (DomainException dme) {
            return new ResponseEntity<String>(createErrorJson(dme.getLocalizedMessage()), HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/editDepartment", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<String> editDepartment(@RequestBody String departmentJson) {
        try {
            departmentService.editDepartment(createDepartmentBeanFromJSON(departmentJson));
            return new ResponseEntity<String>(HttpStatus.OK);
        } catch (DomainException dme) {
            return new ResponseEntity<String>(createErrorJson(dme.getLocalizedMessage()), HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/deleteDepartment", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<String> deleteDepartment(@RequestBody String departmentJson) {
        try {
            departmentService.deleteDepartment(createDepartmentBeanFromJSON(departmentJson));
            return new ResponseEntity<String>(HttpStatus.OK);
        } catch (DomainException dme) {
            return new ResponseEntity<String>(createErrorJson(dme.getLocalizedMessage()), HttpStatus.FORBIDDEN);
        }
    }

    @Atomic
    private String createErrorJson(String message) {
        JsonObject object = new JsonObject();
        object.addProperty("message", message);
        return object.toString();
    }

    private DepartmentBean createDepartmentBeanFromJSON(String departmentJson) {
        JsonObject json = (JsonObject) new JsonParser().parse(departmentJson);
        boolean active = json.get("active").getAsBoolean();
        String code = json.get("code").getAsString();
        String acronym = json.get("acronym").getAsString();
        LocalizedString name = LocalizedString.fromJson(json.get("name"));
        // externalId is null before department's creation
        String externalId = (json.get("externalId") == null) ? null : json.get("externalId").getAsString();
        return new DepartmentBean(active, code, name, acronym, externalId);
    }
}
