/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and ServiÃ§os Partilhados da
 * Universidade de Lisboa:
 *  - Copyright Â© 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright Â© 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: xpto@qub-it.com
 *
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
package org.fenixedu.academic.ui.spring.manageservicerequesttypes;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.fenixedu.academic.domain.serviceRequests.ServiceRequestCategory;
import org.fenixedu.academic.domain.serviceRequests.ServiceRequestType;
import org.fenixedu.academic.domain.serviceRequests.ServiceRequestTypeOption;
import org.fenixedu.academic.ui.spring.controller.AcademicAdministrationSpringApplication;
import org.fenixedu.bennu.core.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.commons.i18n.LocalizedString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;

@SpringFunctionality(app = AcademicAdministrationSpringApplication.class, title = "label.title.manageServiceRequestTypes",
        accessGroup = "#managers")
@RequestMapping("/academic/manageservicerequesttypes/servicerequesttype")
public class ServiceRequestTypeController {

    private static final String BUNDLE = "resources.AcademicAdminOffice";

    @RequestMapping(method = GET)
    public String home(Model model) {
        model.addAttribute("searchservicerequesttypeResultsDataSet", ServiceRequestType.findAll().collect(Collectors.toList()));

        return "redirect:/academic/manageservicerequesttypes/servicerequesttype/search";
    }

    @RequestMapping(value = "/search", method = GET)
    public String search(Model model) {
        model.addAttribute("searchservicerequesttypeResultsDataSet", ServiceRequestType.findAll().collect(Collectors.toList()));
        model.addAttribute("serviceRequestCategoryValues", ServiceRequestCategory.values());

        return "academic/manageservicerequesttypes/servicerequesttype/search";
    }

    @RequestMapping(value = "/search/view/{serviceRequestTypeId}")
    public String processSearchToViewAction(@PathVariable("serviceRequestTypeId") final ServiceRequestType serviceRequestType,
            final Model model, final RedirectAttributes redirectAttributes) {

        return redirect("/academic/manageservicerequesttypes/servicerequesttype/read/" + serviceRequestType.getExternalId(),
                model, redirectAttributes);
    }

    @RequestMapping(value = "/search/delete/{serviceRequestTypeId}", method = RequestMethod.POST)
    public String processSearchToDeleteAction(@PathVariable("serviceRequestTypeId") final ServiceRequestType serviceRequestType,
            final Model model, final RedirectAttributes redirectAttributes) {
        try {
            serviceRequestType.delete();

            addInfoMessage(BundleUtil.getString(BUNDLE, "message.ServiceRequestType.removed.with.success"), model);
        } catch (DomainException ex) {
            addErrorMessage(BundleUtil.getString(BUNDLE, ex.getKey()), model);
        }

        return redirect("/academic/manageservicerequesttypes/servicerequesttype/", model, redirectAttributes);
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
        model.addAttribute("serviceRequestCategoryValues", ServiceRequestCategory.values());
        return "academic/manageservicerequesttypes/servicerequesttype/create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@RequestParam(value = "code", required = true) java.lang.String code, @RequestParam(value = "name",
            required = true) LocalizedString name, @RequestParam(value = "active", required = true) final boolean active,
            @RequestParam(value = "payable", required = true) final boolean payable, @RequestParam(
                    value = "serviceRequestCategory", required = true) final ServiceRequestCategory category, Model model,
            RedirectAttributes redirectAttributes) {
        try {

            final ServiceRequestType serviceRequestType = ServiceRequestType.create(code, name, active, payable, category);

            return redirect("/academic/manageservicerequesttypes/servicerequesttype/read/" + serviceRequestType.getExternalId(),
                    model, redirectAttributes);
        } catch (DomainException de) {
            addErrorMessage(BundleUtil.getString(BUNDLE, de.getKey()), model);
            return create(model);
        }
    }

    @RequestMapping(value = "/read/{serviceRequestTypeId}")
    public String read(@PathVariable("serviceRequestTypeId") final ServiceRequestType serviceRequestType, final Model model) {
        model.addAttribute("serviceRequestType", serviceRequestType);
        model.addAttribute("serviceRequestTypeOptionList",
                Lists.newArrayList(ServiceRequestTypeOption.findAll().collect(Collectors.toList())));

        return "academic/manageservicerequesttypes/servicerequesttype/read";
    }

    @RequestMapping(value = "/update/{serviceRequestTypeId}", method = RequestMethod.GET)
    public String update(@PathVariable("serviceRequestTypeId") final ServiceRequestType serviceRequestType, final Model model) {

        model.addAttribute("serviceRequestType", serviceRequestType);
        model.addAttribute("serviceRequestCategoryValues", ServiceRequestCategory.values());
        return "academic/manageservicerequesttypes/servicerequesttype/update";
    }

    @RequestMapping(value = "/update/{serviceRequestTypeId}", method = RequestMethod.POST)
    public String update(@PathVariable("serviceRequestTypeId") final ServiceRequestType serviceRequestType, @RequestParam(
            value = "code", required = true) java.lang.String code,
            @RequestParam(value = "name", required = true) LocalizedString name,
            @RequestParam(value = "active", required = true) final boolean active, @RequestParam(value = "payable",
                    required = true) final boolean payable,
            @RequestParam(value = "serviceRequestCategory", required = true) final ServiceRequestCategory category, Model model,
            RedirectAttributes redirectAttributes) {

        model.addAttribute("serviceRequestType", serviceRequestType);

        try {
            serviceRequestType.edit(code, name, active, payable, category);

            return redirect("/academic/manageservicerequesttypes/servicerequesttype/read/" + serviceRequestType.getExternalId(),
                    model, redirectAttributes);
        } catch (DomainException de) {
            addErrorMessage(BundleUtil.getString(BUNDLE, de.getKey()), model);

            return update(serviceRequestType, model);
        }
    }

    private String redirectToReadRequestType(final ServiceRequestType serviceRequestType, final Model model,
            RedirectAttributes redirectAttributes) {
        return redirect(
                "/academic/manageservicerequesttypes/servicerequesttype/search/view/" + serviceRequestType.getExternalId(),
                model, redirectAttributes);
    }

    @RequestMapping(value = "/dissociateoption/{serviceRequestTypeId}/{serviceRequestTypeOptionId}")
    public String dissociateOption(@PathVariable("serviceRequestTypeId") final ServiceRequestType serviceRequestType,
            @PathVariable("serviceRequestTypeOptionId") final ServiceRequestTypeOption serviceRequestTypeOption,
            final Model model, final RedirectAttributes redirectAttributes) {

        try {
            serviceRequestType.removeOption(serviceRequestTypeOption);

            addInfoMessage(BundleUtil.getString(BUNDLE, "message.ServiceRequestTypeOption.dissociation.success"), model);
        } catch (DomainException ex) {
            addErrorMessage(BundleUtil.getString(BUNDLE, ex.getKey()), model);
        }

        return redirectToReadRequestType(serviceRequestType, model, redirectAttributes);
    }

    @RequestMapping(value = "/associateoption/{serviceRequestTypeId}/{serviceRequestTypeOptionId}")
    public String associateOption(@PathVariable("serviceRequestTypeId") final ServiceRequestType serviceRequestType,
            @PathVariable("serviceRequestTypeOptionId") final ServiceRequestTypeOption serviceRequestTypeOption,
            final Model model, final RedirectAttributes redirectAttributes) {

        try {
            serviceRequestType.associateOption(serviceRequestTypeOption);

            addInfoMessage(BundleUtil.getString(BUNDLE, "message.ServiceRequestTypeOption.association.success"), model);
        } catch (DomainException de) {
            addErrorMessage(BundleUtil.getString(BUNDLE, de.getKey()), model);
        }

        return redirectToReadRequestType(serviceRequestType, model, redirectAttributes);
    }

    /* --- From Base --- */

    private static final String ERROR_MESSAGES = "errorMessages";
    private static final String WARNING_MESSAGES = "warningMessages";
    private static final String INFO_MESSAGES = "infoMessages";

    //The HTTP Request that can be used internally in the controller
    protected @Autowired HttpServletRequest request;

    //The entity in the Model

    // The list of INFO messages that can be showed on View
    protected void addInfoMessage(String message, Model model) {
        ((List<String>) model.asMap().get(INFO_MESSAGES)).add(message);
    }

    // The list of WARNING messages that can be showed on View
    protected void addWarningMessage(String message, Model model) {
        ((List<String>) model.asMap().get(WARNING_MESSAGES)).add(message);
    }

    // The list of ERROR messages that can be showed on View
    protected void addErrorMessage(String message, Model model) {
        ((List<String>) model.asMap().get(ERROR_MESSAGES)).add(message);
    }

    protected void clearMessages(Model model) {
        model.addAttribute(INFO_MESSAGES, new ArrayList<String>());
        model.addAttribute(WARNING_MESSAGES, new ArrayList<String>());
        model.addAttribute(ERROR_MESSAGES, new ArrayList<String>());
    }

    protected String redirect(String destinationAction, Model model, RedirectAttributes redirectAttributes) {
        if (model.containsAttribute(INFO_MESSAGES)) {
            redirectAttributes.addFlashAttribute(INFO_MESSAGES, model.asMap().get(INFO_MESSAGES));
        }
        if (model.containsAttribute(WARNING_MESSAGES)) {
            redirectAttributes.addFlashAttribute(WARNING_MESSAGES, model.asMap().get(WARNING_MESSAGES));
        }
        if (model.containsAttribute(ERROR_MESSAGES)) {
            redirectAttributes.addFlashAttribute(ERROR_MESSAGES, model.asMap().get(ERROR_MESSAGES));
        }

        return "redirect:" + destinationAction;
    }

    @ModelAttribute
    protected void addModelProperties(Model model) {
        if (!model.containsAttribute(INFO_MESSAGES)) {
            model.addAttribute(INFO_MESSAGES, new ArrayList<String>());
        }
        if (!model.containsAttribute(WARNING_MESSAGES)) {
            model.addAttribute(WARNING_MESSAGES, new ArrayList<String>());
        }
        if (!model.containsAttribute(ERROR_MESSAGES)) {
            model.addAttribute(ERROR_MESSAGES, new ArrayList<String>());
        }

        // Add here more attributes to the Model
        // model.addAttribute(<attr1Key>, <attr1Value>);
        // ....
    }

    /* --- From Base --- */
}
