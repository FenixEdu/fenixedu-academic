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
package org.fenixedu.academic.ui.struts.action.academicAdministration.accounting.reports;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.accounting.SibsPaymentFileProcessReport;
import org.fenixedu.academic.ui.struts.action.academicAdministration.AcademicAdministrationApplication.AcademicAdminPaymentsApp;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.fenixedu.commons.spreadsheet.SheetData;
import org.fenixedu.commons.spreadsheet.SpreadsheetBuilder;
import org.fenixedu.commons.spreadsheet.WorkbookExportFormat;
import org.fenixedu.commons.spreadsheet.converters.CellConverter;
import org.joda.time.LocalDate;

@StrutsFunctionality(app = AcademicAdminPaymentsApp.class, path = "sibs-reports", titleKey = "label.payments.sibs.reports",
        accessGroup = "academic(CREATE_SIBS_PAYMENTS_REPORT)")
@Mapping(path = "/sibsReports", module = "academicAdministration")
@Forwards({ @Forward(name = "report-by-year-month", path = "/academicAdminOffice/accounting/reports/paymentsByYearAndMonth.jsp") })
public class ExportSibsPaymentsReport extends FenixDispatchAction {

    static public class SibsPaymentsReportBean implements Serializable {
        static private final long serialVersionUID = -1884368228152926134L;

        private String year;
        private String month;

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        private int getYearAsInt() {
            return Integer.parseInt(getYear());
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        private int getMonthAsInt() {
            return Integer.parseInt(getMonth());
        }

    }

    @EntryPoint
    public ActionForward prepareReportByYearAndMonth(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("reportBean", new SibsPaymentsReportBean());
        return mapping.findForward("report-by-year-month");
    }

    public ActionForward reportByYearAndMonth(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        final SibsPaymentsReportBean bean = getRenderedObject("reportBean");
        request.setAttribute("reportBean", bean);
        final SheetData<SibsPaymentFileProcessReport> spreadsheet = buildSpreadsheet(bean);

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment; filename=" + getReportFilename(bean));

        final ServletOutputStream writer = response.getOutputStream();
        new SpreadsheetBuilder().addSheet(getReportFilename(bean), spreadsheet).addConverter(Money.class, new CellConverter() {
            @Override
            public Object convert(Object source) {
                return (source != null) ? ((Money) source).getAmount().doubleValue() : Double.valueOf(0d);
            }
        }).build(WorkbookExportFormat.EXCEL, writer);
        writer.flush();
        response.flushBuffer();

        return null;
    }

    private SheetData<SibsPaymentFileProcessReport> buildSpreadsheet(final SibsPaymentsReportBean bean) {
        final SheetData<SibsPaymentFileProcessReport> spreadsheet =
                new SheetData<SibsPaymentFileProcessReport>(getSibsPaymentFileProcessReports(bean)) {

                    private String getString(final String key) {
                        return BundleUtil.getString(Bundle.MANAGER, key);
                    }

                    @Override
                    protected void makeLine(SibsPaymentFileProcessReport line) {
                        addCell(getString("label.reports.date"), line.getWhenProcessedBySibs());
                        addCell(getString("label.reports.version"), line.getFileVersion());
                        addCell(getString("label.reports.gratuity.lic"), line.getDegreeGratuityTotalAmount());
                        addCell(getString("label.reports.gratuity.lb"), line.getBolonhaDegreeGratuityTotalAmount());
                        addCell(getString("label.reports.gratuity.mi"),
                                line.getIntegratedBolonhaMasterDegreeGratuityTotalAmount());
                        addCell(getString("label.reports.gratuity.mb"), line.getBolonhaMasterDegreeGratuityTotalAmount());
                        addCell(getString("label.reports.grad.fee"), line.getAdministrativeOfficeTaxTotalAmount());
                        addCell(getString("label.reports.insurance"), line.getGraduationInsuranceTotalAmount());
                        addCell(getString("label.reports.proesp"), line.getSpecializationGratuityTotalAmount());
                        addCell(getString("label.reports.propmest"), line.getMasterDegreeGratuityTotalAmount());
                        addCell(getString("label.reports.propdout"), line.getPhdGratuityTotalAmount());
                        addCell(getString("label.reports.propdfa"), line.getDfaGratuityTotalAmount());
                        addCell(getString("label.reports.pgrad.fee"), line.getAfterGraduationInsuranceTotalAmount());
                        addCell(getString("label.reports.residence"), line.getResidencePayment());
                        addCell(getString("label.reports.degreeCandidacyForGraduatedPerson"),
                                line.getDegreeCandidacyForGraduatedPersonAmount());
                        addCell(getString("label.reports.degreeChangeIndividualCandidacy"),
                                line.getDegreeChangeIndividualCandidacyAmount());
                        addCell(getString("label.reports.degreeTransferIndividualCandidacy"),
                                line.getDegreeTransferIndividualCandidacyAmount());
                        addCell(getString("label.reports.secondCycleIndividualCandidacy"),
                                line.getSecondCycleIndividualCandidacyAmount());
                        addCell(getString("label.reports.standaloneEnrolmentGratuityEvent"),
                                line.getStandaloneEnrolmentGratuityEventAmount());
                        addCell(getString("label.reports.over23IndividualCandidacy"),
                                line.getOver23IndividualCandidacyEventAmount());
                        addCell(getString("label.reports.institutionAffiliation"), line.getInstitutionAffiliationEventAmount());
                        addCell(getString("label.reports.phdProgramCandidacy"), line.getPhdProgramCandidacyEventAmount());
                        addCell(getString("label.reports.rectorate"), line.getRectorateAmount());
                        addCell(getString("label.reports.specialSeason"), line.getSpecialSeasonEnrolmentEventAmount());
                        addCell(getString("label.reports.totrans"), line.getTransactionsTotalAmount());
                        addCell(getString("label.reports.totarif"), line.getTotalCost());
                    }
                };

        return spreadsheet;
    }

    private List<SibsPaymentFileProcessReport> getSibsPaymentFileProcessReports(final SibsPaymentsReportBean bean) {
        final LocalDate startDate = new LocalDate(bean.getYearAsInt(), bean.getMonthAsInt(), 1);
        final LocalDate endDate = startDate.plusMonths(1).minusDays(1);
        final List<SibsPaymentFileProcessReport> files = SibsPaymentFileProcessReport.readAllBetween(startDate, endDate);
        Collections.sort(files, SibsPaymentFileProcessReport.COMPARATOR_BY_SIBS_PROCESS_DATE);
        return files;
    }

    private String getReportFilename(final SibsPaymentsReportBean bean) {
        return "SIBS-PAYMENTS-REPORTS-" + bean.getYear() + "-" + bean.getMonth() + ".xls";
    }

}