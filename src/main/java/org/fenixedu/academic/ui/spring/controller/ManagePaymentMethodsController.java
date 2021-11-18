/**
 * Copyright © 2018 Instituto Superior Técnico
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

import org.fenixedu.academic.domain.accounting.PaymentMethod;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.ui.spring.controller.manager.PaymentMethodService;
import org.fenixedu.bennu.spring.portal.SpringApplication;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.commons.i18n.LocalizedString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Tiago Pinho (a@fenixedu.org)
 */
@Controller
@SpringApplication(path = "paymentMethods", hint = "Manager", group = "#managers", title = "title.manage.paymentMethods")
@SpringFunctionality(app = ManagePaymentMethodsController.class, title = "title.manage.paymentMethods")
@RequestMapping("/payment-methods-management")
public class ManagePaymentMethodsController {

    @Autowired
    private PaymentMethodService paymentMethodService;

    private String redirectHome() {
        return "redirect:/payment-methods-management";
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("paymentMethods", paymentMethodService.getAllPaymentMethods());
        return "manager/paymentMethods/list";
    }

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String create(Model model) {
        return "manager/paymentMethods/create";
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String create(Model model, @RequestParam String code, @RequestParam LocalizedString description,
            @RequestParam String paymentReferenceFormat, @RequestParam boolean allowManualUse) {
        try {
            paymentMethodService.createPaymentMethod(code, description, paymentReferenceFormat, allowManualUse);
            return redirectHome();
        } catch (DomainException de) {
            model.addAttribute("error", de.getLocalizedMessage());
            return create(model);
        }
    }

    @RequestMapping(value = "{paymentMethod}", method = RequestMethod.GET)
    public String edit(Model model, @PathVariable PaymentMethod paymentMethod) {
        model.addAttribute("paymentMethod", paymentMethod);
        return "manager/paymentMethods/create";
    }

    @RequestMapping(value = "{paymentMethod}", method = RequestMethod.POST)
    public String edit(Model model, @PathVariable PaymentMethod paymentMethod, @RequestParam String code,
            @RequestParam LocalizedString description, @RequestParam String paymentReferenceFormat,
            @RequestParam boolean allowManualUse) {
        try {
            paymentMethodService.editPaymentMethod(paymentMethod, code, description, paymentReferenceFormat, allowManualUse);
            return redirectHome();
        } catch (DomainException de) {
            model.addAttribute("error", de.getLocalizedMessage());
            return edit(model, paymentMethod);
        }
    }

    @RequestMapping(value = "{paymentMethod}", method = RequestMethod.DELETE)
    public ResponseEntity<String> delete(Model model, @PathVariable PaymentMethod paymentMethod) {
        try {
            paymentMethodService.deletePaymentMethod(paymentMethod);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (DomainException de) {
            return new ResponseEntity<>(de.getLocalizedMessage(), HttpStatus.PRECONDITION_FAILED);
        }
    }

    @RequestMapping(value = "manageDefaults", method = RequestMethod.GET)
    public String manageDefaults(Model model) {
        model.addAttribute("paymentMethods", paymentMethodService.getAllPaymentMethods());
        return "manager/paymentMethods/manageDefaults";
    }

    @RequestMapping(value = "manageDefaults", method = RequestMethod.POST)
    public String manageDefaults(Model model, @RequestParam PaymentMethod defaultCashPaymentMethod,
            @RequestParam PaymentMethod defaultSibsPaymentMethod, @RequestParam PaymentMethod defaultRefundPaymentMethod) {
        try {
            paymentMethodService.setDefaultPaymentMethods(defaultCashPaymentMethod, defaultSibsPaymentMethod, defaultRefundPaymentMethod);
            return redirectHome();
        } catch (DomainException de) {
            return manageDefaults(model);
        }
    }

    @RequestMapping(value = "logs", method = RequestMethod.GET)
    public String listLogs(Model model) {
        model.addAttribute("paymentMethodsLogs", paymentMethodService.getAllPaymentMethodsLogs());
        return "manager/paymentMethods/listLogs";
    }

}
