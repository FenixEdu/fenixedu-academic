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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.EmptyDegree;
import org.fenixedu.academic.domain.EmptyDegreeCurricularPlan;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.json.adapters.AdministrativeOfficeJsonAdapter;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.ui.struts.action.academicAdministration.AuthorizationGroupBean;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.groups.UserGroup;
import org.fenixedu.bennu.core.rest.JsonAwareResource;
import org.fenixedu.bennu.spring.portal.BennuSpringController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pt.ist.fenixframework.FenixFramework;

import com.google.gson.JsonObject;

@RestController
@BennuSpringController(SchoolManagementController.class)
@RequestMapping("/administrative-office-management")
public class AdministrativeOfficesController extends JsonAwareResource {

    @RequestMapping(value = "/offices", method = RequestMethod.GET)
    public String showOffices() {
        return view(Bennu.getInstance().getAdministrativeOfficesSet());
    }

    @RequestMapping(value = "/office/{office}", method = RequestMethod.GET)
    public String showOffice(Model model, @PathVariable AdministrativeOffice office) {
        return view(office);
    }

    @RequestMapping(value = "/resetAuthorizations", method = RequestMethod.POST)
    public void resetAuthorizations() {
        FenixFramework.atomic(() -> {
            List<AuthorizationGroupBean> groups =
                    AcademicAccessRule.accessRules()
                            .filter(r -> r.getOperation().equals(AcademicOperationType.MANAGE_AUTHORIZATIONS))
                            .map(AuthorizationGroupBean::new).collect(Collectors.toList());
            AuthorizationGroupBean authorization = new AuthorizationGroupBean();
            authorization.setWhoCanAccess(UserGroup.of(AccessControl.getPerson().getUser()));
            authorization.create(AcademicOperationType.MANAGE_AUTHORIZATIONS, Collections.emptySet());
            groups.add(0, authorization);
        });
    }

    @RequestMapping(value = "/emptyDegreeOfficeId", method = RequestMethod.GET)
    public ResponseEntity<String> getEmptyDegreeOfficeId() {
        try {
            return new ResponseEntity<String>(FenixFramework.atomic(() -> {
                if (EmptyDegree.getInstance() != null) {
                    return EmptyDegree.getInstance().getAdministrativeOffice().getExternalId();
                } else {
                    return "";
                }
            }), HttpStatus.OK);
        } catch (DomainException dme) {
            return new ResponseEntity<String>(createErrorJson(dme.getLocalizedMessage()), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<String>(HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/setEmptyDegree/{office}", method = RequestMethod.POST)
    public void setEmptyDegree(@PathVariable AdministrativeOffice office) {
        FenixFramework.atomic(() -> {
            EmptyDegree emptyDegree = EmptyDegree.getInstance();
            if (emptyDegree == null) {
                EmptyDegree.init();
            }
            EmptyDegree.getInstance().setAdministrativeOffice(office);
            EmptyDegreeCurricularPlan emptyDCP = EmptyDegreeCurricularPlan.getInstance();
            if (emptyDCP == null) {
                EmptyDegreeCurricularPlan.init();
            }
        });
    }

    @RequestMapping(value = "/office/", method = RequestMethod.POST)
    public ResponseEntity<String> saveOffice(@RequestBody String officeJson) {
        try {
            create(officeJson, AdministrativeOffice.class);
            return new ResponseEntity<String>(HttpStatus.OK);
        } catch (DomainException dme) {
            return new ResponseEntity<String>(createErrorJson(dme.getLocalizedMessage()), HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/office/{office}", method = RequestMethod.POST)
    public ResponseEntity<String> saveOffice(@PathVariable AdministrativeOffice office, @RequestBody String officeJson) {
        try {
            update(officeJson, office, AdministrativeOfficeJsonAdapter.class);
            return new ResponseEntity<String>(HttpStatus.OK);
        } catch (DomainException dme) {
            return new ResponseEntity<String>(createErrorJson(dme.getLocalizedMessage()), HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/office/{office}/delete", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteOffice(@PathVariable AdministrativeOffice office) {
        try {
            FenixFramework.atomic(() -> office.delete());
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
