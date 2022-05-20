package org.fenixedu.academic.ui.spring.controller.academicAdministration;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.ui.spring.controller.AcademicAdministrationSpringApplication;
import org.fenixedu.academic.ui.spring.service.AccountingManagementAccessControlService;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.commons.spreadsheet.Spreadsheet;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@SpringFunctionality(app = AcademicAdministrationSpringApplication.class, title = "label.accounting.management.overdue.payments.title")

@RequestMapping("/accounting-management/overdue-payments")
public class OverduePaymentsController {

    
    private final AccountingManagementAccessControlService accountingManagementAccessControlService;

    @Autowired
    public OverduePaymentsController(AccountingManagementAccessControlService accountingManagementAccessControlService) {
        this.accountingManagementAccessControlService = accountingManagementAccessControlService;
    }
    
    @Autowired
    OverduePaymentsService paymentsOverdueService;

    private String view(String string) {
        return "fenixedu-academic/accounting/" + string;
    }
    
    
    
    @RequestMapping(method = RequestMethod.GET)
    public String home(Model model) {
        if (!this.accountingManagementAccessControlService.isPaymentManager(Authenticate.getUser())) {
            throw new DomainException(Response.Status.UNAUTHORIZED, "return.key.argument", "not.authorized");
        }
        OverduePaymentsParameters  bean = new OverduePaymentsParameters(paymentsOverdueService.getAllOpenedPayments());       
        model.addAttribute("bean",bean);
        return  "fenixedu-academic/accounting/payments-Overdue-List";
    }
    
    @RequestMapping(method = RequestMethod.GET, value="{eventId}")
    public String details(Model model, @PathVariable final String eventId) {
       
        return  "fenixedu-academic/accounting/payments-Overdue-List";
    }
       
    @RequestMapping(method = RequestMethod.GET, value = "generate")
    public String generate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start, @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end, RedirectAttributes attrs, HttpServletResponse response) {

        if (end.isBefore(start)) {
            attrs.addFlashAttribute("message", BundleUtil.getString(Bundle.ACCOUNTING, "error.transaction.report.wrong.dates", start
                    .toString("dd/MM/yyyy"), end.toString("dd/MM/yyyy")));
            return view("payments-Overdue-List");
        }

        final String filename = BundleUtil.getString(Bundle.ACCOUNTING, "label.transaction.report.filename", start.toString
                ("dd_MM_yyyy"), end.toString("dd_MM_yyyy"), new DateTime().toString("dd_MM_yyyy-HH_mm_ss"));

        try {
            final Spreadsheet report = paymentsOverdueService.reportList(start, end);
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
