/**
 * Copyright © 2020 Instituto Superior Técnico
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

import org.fenixedu.academic.domain.accounting.AccountingTransaction;
import org.fenixedu.academic.domain.accounting.ProofOfPayment;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@SpringFunctionality(app = AcademicAdministrationSpringApplication.class, title = "label.accounting.management.proof.of.payment.title",
        accessGroup = "academic(CREATE_SIBS_PAYMENTS_REPORT)")
@RequestMapping("/accounting-management/proof-of-payment")
public class AccountingProofOfPaymentController {

    @RequestMapping(method = RequestMethod.GET)
    public String home(Model model) {
        return "fenixedu-academic/accounting/proofsOfPayment";
    }

    @RequestMapping(path = "/{proofOfPayment}/markAsProcessed", method = RequestMethod.GET)
    public String prepareMarkAsProcessed(final Model model, final @PathVariable ProofOfPayment proofOfPayment) {
        return "redirect:/accounting-management/proof-of-payment#transactions" + proofOfPayment.getExternalId();
    }

    @RequestMapping(path = "/{proofOfPayment}/markAsProcessed/{accountingTransaction}", method = RequestMethod.POST)
    public String markAsProcessed(final Model model, final @PathVariable ProofOfPayment proofOfPayment,
                                  final @PathVariable AccountingTransaction accountingTransaction) {
        proofOfPayment.markAsProcessed(accountingTransaction);
        return "redirect:/accounting-management/proof-of-payment";
    }

    @RequestMapping(path = "/{proofOfPayment}/reject", method = RequestMethod.POST)
    public String reject(final Model model, final @PathVariable ProofOfPayment proofOfPayment) {
        proofOfPayment.reject(true);
        return "redirect:/accounting-management/proof-of-payment";
    }

}
