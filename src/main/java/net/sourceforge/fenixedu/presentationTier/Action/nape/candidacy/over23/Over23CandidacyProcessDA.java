package net.sourceforge.fenixedu.presentationTier.Action.nape.candidacy.over23;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.over23.Over23CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.over23.Over23IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.over23.Over23IndividualCandidacyResultBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.period.Over23CandidacyPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.candidacy.CandidacyProcessDA;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;
import pt.utl.ist.fenix.tools.util.excel.SpreadsheetXLSExporter;
import pt.utl.ist.fenix.tools.util.i18n.Language;

@Mapping(path = "/caseHandlingOver23CandidacyProcess", module = "nape",
        formBeanClass = CandidacyProcessDA.CandidacyProcessForm.class)
@Forwards({ @Forward(name = "intro", path = "/candidacy/mainCandidacyProcess.jsp") })
public class Over23CandidacyProcessDA extends CandidacyProcessDA {

    @Override
    protected Class getProcessType() {
        return Over23CandidacyProcess.class;
    }

    @Override
    protected Class getChildProcessType() {
        return Over23IndividualCandidacyProcess.class;
    }

    @Override
    protected Class getCandidacyPeriodType() {
        return Over23CandidacyPeriod.class;
    }

    @Override
    protected void setStartInformation(ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        if (!hasExecutionInterval(request)) {
            final List<ExecutionInterval> executionIntervals =
                    ExecutionInterval.readExecutionIntervalsWithCandidacyPeriod(getCandidacyPeriodType());
            if (executionIntervals.size() == 1) {
                setCandidacyProcessInformation(request, getCandidacyProcess(request, executionIntervals.get(0)));
            } else {
                request.setAttribute("canCreateProcess", canCreateProcess(getProcessType().getName()));
                request.setAttribute("executionIntervals", executionIntervals);
            }
        } else {
            setCandidacyProcessInformation(request, getCandidacyProcess(request, getExecutionInterval(request)));
        }
    }

    @Override
    protected ActionForward introForward(ActionMapping mapping) {
        return mapping.findForward("intro");
    }

    @Override
    protected Over23CandidacyProcess getCandidacyProcess(HttpServletRequest request, final ExecutionInterval executionInterval) {
        return executionInterval.hasOver23CandidacyPeriod() ? executionInterval.getOver23CandidacyPeriod()
                .getOver23CandidacyProcess() : null;
    }

    public ActionForward prepareExecuteSendInformationToJury(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("send-to-jury");
    }

    public ActionForward executeSendInformationToJury(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {
        try {
            executeActivity(getProcess(request), "SendInformationToJury", null);
        } catch (final DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            return prepareExecuteSendInformationToJury(mapping, actionForm, request, response);
        }
        return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    public ActionForward prepareExecutePrintCandidacies(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment; filename=" + getReportFilename());

        final ServletOutputStream writer = response.getOutputStream();
        final Over23CandidacyProcess process = getProcess(request);
        final Spreadsheet spreadsheet = buildReport(process.getOver23IndividualCandidaciesThatCanBeSendToJury());

        spreadsheet.exportToXLSSheet(writer);
        writer.flush();
        response.flushBuffer();

        return null;
    }

    private Spreadsheet buildReport(final List<Over23IndividualCandidacyProcess> over23IndividualCandidacies) {
        final ResourceBundle bundle = ResourceBundle.getBundle("resources/ApplicationResources", Language.getLocale());
        final Spreadsheet result = new CandidacyReport("candidacies");

        result.setHeaders(new String[] { bundle.getString("label.name"), bundle.getString("label.identificationNumber"),
                bundle.getString("label.degrees") });

        for (final Over23IndividualCandidacyProcess candidacy : over23IndividualCandidacies) {
            final Row row = result.addRow();
            row.setCell(candidacy.getPersonalDetails().getName());
            row.setCell(candidacy.getPersonalDetails().getDocumentIdNumber());

            int count = 1;
            String degrees = "";
            for (final Degree degree : candidacy.getSelectedDegreesSortedByOrder()) {
                degrees += count++ + " - " + degree.getNameFor(candidacy.getCandidacyExecutionInterval()) + "\n";
            }
            row.setCell(degrees);
        }

        return result;
    }

    public ActionForward prepareExecuteIntroduceCandidacyResults(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        setInformationToIntroduceCandidacyResults(request);
        return mapping.findForward("view-candidacy-results");
    }

    private void setInformationToIntroduceCandidacyResults(HttpServletRequest request) {
        final Over23CandidacyProcess process = getProcess(request);
        final List<Over23IndividualCandidacyResultBean> beans = new ArrayList<Over23IndividualCandidacyResultBean>();
        for (final Over23IndividualCandidacyProcess candidacy : process.getOver23IndividualCandidaciesThatCanBeSendToJury()) {
            beans.add(new Over23IndividualCandidacyResultBean(candidacy));
        }
        request.setAttribute("over23IndividualCandidacyResultBeans", beans);
    }

    public ActionForward prepareIntroduceCandidacyResults(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        setInformationToIntroduceCandidacyResults(request);
        return mapping.findForward("insert-candidacy-results");
    }

    public ActionForward executeIntroduceCandidacyResults(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws  FenixServiceException {
        try {
            executeActivity(getProcess(request), "IntroduceCandidacyResults",
                    getRenderedObject("over23IndividualCandidacyResultBeans"));
        } catch (final DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            request.setAttribute("over23IndividualCandidacyResultBeans",
                    getRenderedObject("over23IndividualCandidacyResultBeans"));
            return mapping.findForward("insert-candidacy-results");
        }
        return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    public ActionForward executeIntroduceCandidacyResultsInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("over23IndividualCandidacyResultBeans", getRenderedObject("over23IndividualCandidacyResultBeans"));
        return mapping.findForward("insert-candidacy-results");
    }

    public ActionForward prepareExecutePublishCandidacyResults(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws  FenixServiceException {
        try {
            executeActivity(getProcess(request), "PublishCandidacyResults", null);
        } catch (final DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
        }
        return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    @Override
    protected Over23CandidacyProcess getProcess(HttpServletRequest request) {
        return (Over23CandidacyProcess) super.getProcess(request);
    }

    private class CandidacyXLSExporter extends SpreadsheetXLSExporter {

        @Override
        protected void exportXLSLine(final HSSFSheet sheet, final HSSFCellStyle cellStyle, final List<Object> cells,
                final int offset) {

            final HSSFRow row = sheet.createRow(sheet.getLastRowNum() + offset);
            cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

            int count = 0;
            for (final Object cellValue : cells) {
                if (++count == 3) {
                    cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
                } else {
                    cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                }
                addColumn(cellStyle, row, cellValue);
            }
        }
    }

    private class CandidacyReport extends Spreadsheet {

        public CandidacyReport(final String name) {
            super(name);
        }

        @Override
        public void exportToXLSSheet(final OutputStream outputStream) throws IOException {
            new CandidacyXLSExporter().exportToXLSSheet(this, outputStream);
        }
    }

    static public class Over23CandidacyDegreeBean extends CandidacyDegreeBean {
        public Over23CandidacyDegreeBean(final Over23IndividualCandidacyProcess process) {
            setPersonalDetails(process.getPersonalDetails());
            setDegree(process.getAcceptedDegree());
            setState(process.getCandidacyState());
            setRegistrationCreated(process.hasRegistrationForCandidacy());
        }
    }

    @Override
    protected List<CandidacyDegreeBean> createCandidacyDegreeBeans(final HttpServletRequest request) {
        final Over23CandidacyProcess process = getProcess(request);
        final List<CandidacyDegreeBean> candidacyDegreeBeans = new ArrayList<CandidacyDegreeBean>();
        for (final Over23IndividualCandidacyProcess child : process.getAcceptedOver23IndividualCandidacies()) {
            candidacyDegreeBeans.add(new Over23CandidacyDegreeBean(child));
        }
        Collections.sort(candidacyDegreeBeans);
        return candidacyDegreeBeans;
    }

    @Override
    protected Spreadsheet buildIndividualCandidacyReport(Spreadsheet spreadsheet,
            IndividualCandidacyProcess individualCandidacyProcess) {
        // TODO Auto-generated method stub
        return null;
    }

}
