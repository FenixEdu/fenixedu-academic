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

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.ui.spring.service.AccountingReportService;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.commons.spreadsheet.Spreadsheet;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Created by Ricardo Rodrigues (hello@fenixedu.org).
 */

@Controller
@SpringFunctionality(app = AcademicAdministrationSpringApplication.class, title = "label.accounting.management.report.title",
        accessGroup = "academic(CREATE_SIBS_PAYMENTS_REPORT)")
@RequestMapping("/accounting-management/payment-report")
public class AccountingListReportController {

    private final AccountingReportService accountingReportService;

    @Autowired
    public AccountingListReportController(AccountingReportService accountingReportService) {
        this.accountingReportService = accountingReportService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String home(Model model) {
        return "fenixedu-academic/accounting/transaction-report-list";
    }

    @RequestMapping(method = RequestMethod.GET, value = "transaction-report-list/generate")
    public String report(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start, @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end, RedirectAttributes attrs, HttpServletResponse response) {

        if (end.isBefore(start)) {
            attrs.addFlashAttribute("message", BundleUtil.getString(Bundle.ACCOUNTING, "error.transaction.report.wrong.dates", start
                    .toString("dd/MM/yyyy"), end.toString("dd/MM/yyyy")));
            return "redirect:/accounting-management/payment-report";
        }

        final String filename = BundleUtil.getString(Bundle.ACCOUNTING, "label.transaction.report.filename", start.toString
                ("dd_MM_yyyy"), end.toString("dd_MM_yyyy"), new DateTime().toString("dd_MM_yyyy-HH_mm_ss"));

        try {
            final Spreadsheet report = accountingReportService.reportList(start, end);
            response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s.xlsx\"", filename));
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            report.exportToXLSXSheet(response.getOutputStream());
            response.flushBuffer();
            return null;
        } catch (IOException e) {
            throw new Error(e);
        }
    }
}
