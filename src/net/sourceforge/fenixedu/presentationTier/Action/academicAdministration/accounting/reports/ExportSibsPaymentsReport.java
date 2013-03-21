package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.accounting.reports;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.accounting.SibsPaymentFileProcessReport;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.Money;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.spreadsheet.SheetData;
import pt.utl.ist.fenix.tools.spreadsheet.SpreadsheetBuilder;
import pt.utl.ist.fenix.tools.spreadsheet.WorkbookExportFormat;
import pt.utl.ist.fenix.tools.spreadsheet.converters.CellConverter;
import pt.utl.ist.fenix.tools.util.i18n.Language;

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
                    @Override
                    protected void makeLine(SibsPaymentFileProcessReport line) {
                        final ResourceBundle bundle =
                                ResourceBundle.getBundle("resources.ManagerResources", Language.getLocale());
                        addCell(bundle.getString("label.reports.date"), line.getWhenProcessedBySibs());
                        addCell(bundle.getString("label.reports.version"), line.getFileVersion());
                        addCell(bundle.getString("label.reports.gratuity.lic"), line.getDegreeGratuityTotalAmount());
                        addCell(bundle.getString("label.reports.gratuity.lb"), line.getBolonhaDegreeGratuityTotalAmount());
                        addCell(bundle.getString("label.reports.gratuity.mi"),
                                line.getIntegratedBolonhaMasterDegreeGratuityTotalAmount());
                        addCell(bundle.getString("label.reports.gratuity.mb"), line.getBolonhaMasterDegreeGratuityTotalAmount());
                        addCell(bundle.getString("label.reports.grad.fee"), line.getAdministrativeOfficeTaxTotalAmount());
                        addCell(bundle.getString("label.reports.insurance"), line.getGraduationInsuranceTotalAmount());
                        addCell(bundle.getString("label.reports.proesp"), line.getSpecializationGratuityTotalAmount());
                        addCell(bundle.getString("label.reports.propmest"), line.getMasterDegreeGratuityTotalAmount());
                        addCell(bundle.getString("label.reports.propdout"), line.getPhdGratuityTotalAmount());
                        addCell(bundle.getString("label.reports.propdfa"), line.getDfaGratuityTotalAmount());
                        addCell(bundle.getString("label.reports.pgrad.fee"), line.getAfterGraduationInsuranceTotalAmount());
                        addCell(bundle.getString("label.reports.residence"), line.getResidencePayment());
                        addCell(bundle.getString("label.reports.degreeCandidacyForGraduatedPerson"),
                                line.getDegreeCandidacyForGraduatedPersonAmount());
                        addCell(bundle.getString("label.reports.degreeChangeIndividualCandidacy"),
                                line.getDegreeChangeIndividualCandidacyAmount());
                        addCell(bundle.getString("label.reports.degreeTransferIndividualCandidacy"),
                                line.getDegreeTransferIndividualCandidacyAmount());
                        addCell(bundle.getString("label.reports.secondCycleIndividualCandidacy"),
                                line.getSecondCycleIndividualCandidacyAmount());
                        addCell(bundle.getString("label.reports.standaloneEnrolmentGratuityEvent"),
                                line.getStandaloneEnrolmentGratuityEventAmount());
                        addCell(bundle.getString("label.reports.over23IndividualCandidacy"),
                                line.getOver23IndividualCandidacyEventAmount());
                        addCell(bundle.getString("label.reports.institutionAffiliation"),
                                line.getInstitutionAffiliationEventAmount());
                        addCell(bundle.getString("label.reports.phdProgramCandidacy"), line.getPhdProgramCandidacyEventAmount());
                        addCell(bundle.getString("label.reports.rectorate"), line.getRectorateAmount());
                        addCell(bundle.getString("label.reports.totrans"), line.getTransactionsTotalAmount());
                        addCell(bundle.getString("label.reports.totarif"), line.getTotalCost());
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