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
package org.fenixedu.academic.ui.spring.controller.manager;

import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.domain.Bennu;
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
@RequestMapping("/department-management")
public class DepartmentsManagementController extends JsonAwareResource {

    @RequestMapping(value = "/departments")
    public String getDepartmentsSet() {
        return view(Bennu.getInstance().getDepartmentsSet());
    }

    @RequestMapping(value = "/department/{department}")
    public String getDepartment(@PathVariable Department department) {
        return view(department);
    }

    @RequestMapping(value = "/createDepartment", method = RequestMethod.POST)
    public ResponseEntity<String> addDepartment(@RequestBody String departmentJson) {
        try {
            create(departmentJson, Department.class);
            return new ResponseEntity<String>(HttpStatus.OK);
        } catch (DomainException dme) {
            return new ResponseEntity<String>(createErrorJson(dme.getLocalizedMessage()), HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/editDepartment/{department}", method = RequestMethod.POST)
    public ResponseEntity<String> editDepartment(@RequestBody String departmentJson, @PathVariable Department department) {
        try {
            update(departmentJson, department);
            return new ResponseEntity<String>(HttpStatus.OK);
        } catch (DomainException dme) {
            return new ResponseEntity<String>(createErrorJson(dme.getLocalizedMessage()), HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/deleteDepartment/{department}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteDepartment(@PathVariable Department department) {
        try {
            FenixFramework.atomic(() -> {
                department.getDepartmentUnit().delete();
                department.delete();
            });
            return new ResponseEntity<String>(HttpStatus.OK);
        } catch (DomainException dme) {
            return new ResponseEntity<String>(createErrorJson(dme.getLocalizedMessage()), HttpStatus.FORBIDDEN);
        }
    }

    private String createErrorJson(String message) {
        JsonObject object = new JsonObject();
        object.addProperty("message", message);
        return object.toString();
    }
}
