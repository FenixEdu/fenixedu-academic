package net.sourceforge.fenixedu.presentationTier.Action.candidacy.over23;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.candidacyProcess.over23.Over23CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.over23.Over23IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.over23.Over23IndividualCandidacyResultBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.period.Over23CandidacyPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.candidacy.CandidacyProcessDA;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Forward;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Forwards;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Mapping;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.report.Spreadsheet;
import net.sourceforge.fenixedu.util.report.SpreadsheetXLSExporter;
import net.sourceforge.fenixedu.util.report.Spreadsheet.Row;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.LocalDate;

@Mapping(path = "/caseHandlingOver23CandidacyProcess", module = "academicAdminOffice", formBeanClass = CandidacyProcessDA.CandidacyProcessForm.class)
@Forwards( { @Forward(name = "intro", path = "/candidacy/mainCandidacyProcess.jsp"),
	@Forward(name = "prepare-create-new-process", path = "/candidacy/createCandidacyPeriod.jsp"),
	@Forward(name = "prepare-edit-candidacy-period", path = "/candidacy/editCandidacyPeriod.jsp"),
	@Forward(name = "send-to-jury", path = "/candidacy/over23/sendToJury.jsp"),
	@Forward(name = "insert-candidacy-results-from-jury", path = "/candidacy/over23/insertCandidacyResultsFromJury.jsp")

})
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
	    request.setAttribute("executionInterval", ExecutionYear.readCurrentExecutionYear());
	}
	setCandidacyProcessInformation(request, getCandidacyProcess(getExecutionInterval(request)));
    }

    protected ActionForward introForward(ActionMapping mapping) {
	return mapping.findForward("intro");
    }

    @Override
    protected Over23CandidacyProcess getCandidacyProcess(final ExecutionInterval executionInterval) {
	return executionInterval.hasOver23CandidacyPeriod() ? executionInterval.getOver23CandidacyPeriod()
		.getOver23CandidacyProcess() : null;
    }

    public ActionForward prepareExecuteSendInformationToJury(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	return mapping.findForward("send-to-jury");
    }

    public ActionForward executeSendInformationToJury(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
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
	response.setHeader("Content-disposition", "attachment; filename=Candidaturas_Maiores_23_"
		+ new LocalDate().toString("ddMMyyyy") + ".xls");

	final ServletOutputStream writer = response.getOutputStream();
	final Over23CandidacyProcess process = getProcess(request);
	final Spreadsheet spreadsheet = buildReport(process.getOver23IndividualCandidaciesThatCanBeSendToJury());

	spreadsheet.exportToXLSSheet(writer);
	writer.flush();
	response.flushBuffer();

	return null;
    }

    private Spreadsheet buildReport(final List<Over23IndividualCandidacyProcess> over23IndividualCandidacies) {
	final ResourceBundle bundle = ResourceBundle.getBundle("resources/ApplicationResources", LanguageUtils.getLocale());
	final Spreadsheet result = new CandidacyReport("candidacies");

	result.setHeaders(new String[] { bundle.getString("label.name"), bundle.getString("label.identificationNumber"),
		bundle.getString("label.degrees") });

	for (final Over23IndividualCandidacyProcess candidacy : over23IndividualCandidacies) {
	    final Row row = result.addRow();
	    row.setCell(candidacy.getCandidacyPerson().getName());
	    row.setCell(candidacy.getCandidacyPerson().getDocumentIdNumber());

	    int count = 1;
	    String degrees = "";
	    for (final Degree degree : candidacy.getSelectedDegreesSortedByOrder()) {
		degrees += count++ + " - " + degree.getName() + "\n";
	    }
	    row.setCell(degrees);
	}

	return result;
    }

    public ActionForward prepareExecuteInsertResultsFromJury(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	final Over23CandidacyProcess process = getProcess(request);
	final List<Over23IndividualCandidacyResultBean> beans = new ArrayList<Over23IndividualCandidacyResultBean>();
	for (final Over23IndividualCandidacyProcess candidacy : process.getOver23IndividualCandidaciesThatCanBeSendToJury()) {
	    beans.add(new Over23IndividualCandidacyResultBean(candidacy));
	}
	request.setAttribute("over23IndividualCandidacyResultBeans", beans);
	return mapping.findForward("insert-candidacy-results-from-jury");
    }

    public ActionForward executeInsertResultsFromJury(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	try {
	    executeActivity(getProcess(request), "InsertResultsFromJury",
		    getRenderedObject("over23IndividualCandidacyResultBeans"));
	} catch (final DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    request.setAttribute("over23IndividualCandidacyResultBeans",
		    getRenderedObject("over23IndividualCandidacyResultBeans"));
	    return mapping.findForward("insert-candidacy-results-from-jury");
	}
	return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    public ActionForward executeInsertResultsFromJuryInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("over23IndividualCandidacyResultBeans", getRenderedObject("over23IndividualCandidacyResultBeans"));
	return mapping.findForward("insert-candidacy-results-from-jury");
    }

    public ActionForward prepareExecutePublishCandidacyResults(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
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

    public ActionForward prepareExecuteCreateRegistrations(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	try {
	    executeActivity(getProcess(request), "CreateRegistrations");
	} catch (final DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	}
	return listProcessAllowedActivities(mapping, actionForm, request, response);
    }
}
