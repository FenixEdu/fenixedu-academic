package net.sourceforge.fenixedu.presentationTier.Action.candidacy.secondCycle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleIndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleIndividualCandidacyResultBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.casehandling.CaseHandlingDispatchAction;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Forward;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Forwards;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Mapping;
import net.sourceforge.fenixedu.util.report.Spreadsheet;
import net.sourceforge.fenixedu.util.report.SpreadsheetXLSExporter;
import net.sourceforge.fenixedu.util.report.Spreadsheet.Row;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.YearMonthDay;

@Mapping(path = "/caseHandlingSecondCycleCandidacyProcess", module = "academicAdminOffice", formBean = "candidacyForm")
@Forwards( { @Forward(name = "intro", path = "/candidacy/secondCycle/intro.jsp"),
	@Forward(name = "list-processes", path = "/academicAdminOffice/caseHandling/listProcesses.jsp"),
	@Forward(name = "list-allowed-activities", path = "/academicAdminOffice/caseHandling/listActivities.jsp"),
	@Forward(name = "prepare-create-new-process", path = "/candidacy/secondCycle/createCandidacyPeriod.jsp"),
	@Forward(name = "prepare-edit-candidacy-period", path = "/candidacy/secondCycle/editCandidacyPeriod.jsp"),
	@Forward(name = "introduce-candidacy-results", path = "/candidacy/secondCycle/introduceCandidacyResults.jsp")

})
public class SecondCycleCandidacyProcessDA extends CaseHandlingDispatchAction {

    @Override
    protected Class getProcessType() {
	return SecondCycleCandidacyProcess.class;
    }

    @Override
    protected SecondCycleCandidacyProcess getProcess(HttpServletRequest request) {
	return (SecondCycleCandidacyProcess) super.getProcess(request);
    }

    public ActionForward intro(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	return mapping.findForward("intro");
    }

    @Override
    public ActionForward prepareCreateNewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("secondCycleCandidacyProcessBean", new SecondCycleCandidacyProcessBean(ExecutionYear
		.readCurrentExecutionYear()));
	return mapping.findForward("prepare-create-new-process");
    }

    @Override
    public ActionForward createNewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	try {
	    return super.createNewProcess(mapping, form, request, response);
	} catch (final DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    request.setAttribute("secondCycleCandidacyProcessBean", getRenderedObject("secondCycleCandidacyProcessBean"));
	    return mapping.findForward("prepare-create-new-process");
	}
    }

    public ActionForward createNewProcessInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("secondCycleCandidacyProcessBean", getRenderedObject("secondCycleCandidacyProcessBean"));
	return mapping.findForward("prepare-create-new-process");
    }

    public ActionForward prepareExecuteEditCandidacyPeriod(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	final SecondCycleCandidacyProcess process = getProcess(request);
	request.setAttribute("secondCycleCandidacyProcessBean", new SecondCycleCandidacyProcessBean(process));
	return mapping.findForward("prepare-edit-candidacy-period");
    }

    public ActionForward executeEditCandidacyPeriod(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	try {
	    executeActivity(getProcess(request), "EditCandidacyPeriod", getRenderedObject("secondCycleCandidacyProcessBean"));
	} catch (final DomainException e) {
	    addActionMessage(request, e.getMessage());
	    request.setAttribute("secondCycleCandidacyProcessBean", getRenderedObject("secondCycleCandidacyProcessBean"));
	    return mapping.findForward("prepare-edit-candidacy-period");
	}
	return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    public ActionForward executeEditCandidacyPeriodInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("secondCycleCandidacyProcessBean", getRenderedObject("secondCycleCandidacyProcessBean"));
	return mapping.findForward("prepare-edit-candidacy-period");
    }

    public ActionForward prepareExecuteSendToCoordinator(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	try {
	    executeActivity(getProcess(request), "SendToCoordinator");
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	}
	return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    public ActionForward prepareExecutePrintCandidacies(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {

	response.setContentType("application/vnd.ms-excel");
	response.setHeader("Content-disposition", "attachment; filename=Candidaturas_2_Ciclo_"
		+ new YearMonthDay().toString("ddMMyyyy") + ".xls");

	final ServletOutputStream writer = response.getOutputStream();
	writeReport(getProcess(request), writer);
	writer.flush();
	response.flushBuffer();
	return null;
    }

    public ActionForward prepareExecuteIntroduceCandidacyResults(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	final SecondCycleCandidacyProcess process = getProcess(request);
	final List<SecondCycleIndividualCandidacyResultBean> beans = new ArrayList<SecondCycleIndividualCandidacyResultBean>();
	for (final SecondCycleIndividualCandidacyProcess candidacyProcess : process.getValidSecondCycleIndividualCandidacies()) {
	    beans.add(new SecondCycleIndividualCandidacyResultBean(candidacyProcess));
	}
	request.setAttribute("secondCycleIndividualCandidacyResultBeans", beans);
	return mapping.findForward("introduce-candidacy-results");
    }

    public ActionForward executeIntroduceCandidacyResultsInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("secondCycleIndividualCandidacyResultBeans",
		getRenderedObject("secondCycleIndividualCandidacyResultBeans"));
	return mapping.findForward("introduce-candidacy-results");
    }

    public ActionForward executeIntroduceCandidacyResults(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	try {
	    executeActivity(getProcess(request), "IntroduceCandidacyResults",
		    getRenderedObject("secondCycleIndividualCandidacyResultBeans"));
	} catch (final DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    request.setAttribute("secondCycleIndividualCandidacyResultBeans",
		    getRenderedObject("secondCycleIndividualCandidacyResultBeans"));
	    return mapping.findForward("introduce-candidacy-results");
	}

	return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    private void writeReport(final SecondCycleCandidacyProcess process, final ServletOutputStream writer) throws IOException {
	final List<Spreadsheet> spreadsheets = new ArrayList<Spreadsheet>();
	for (final Entry<Degree, List<SecondCycleIndividualCandidacyProcess>> entry : process
		.getValidSecondCycleIndividualCandidaciesByDegree().entrySet()) {
	    spreadsheets.add(buildReport(entry.getKey(), entry.getValue()));
	}
	new SpreadsheetXLSExporter().exportToXLSSheets(writer, spreadsheets);
    }

    private Spreadsheet buildReport(final Degree degree, final List<SecondCycleIndividualCandidacyProcess> values) {
	final Spreadsheet spreadsheet = new CandidacyReport(degree.getSigla());
	addHeader(spreadsheet);

	for (final SecondCycleIndividualCandidacyProcess process : values) {
	    final Row row = spreadsheet.addRow();
	    row.setCell(process.getCandidacyPerson().getName());
	    row.setCell(process.getCandidacyPrecedentDegreeInformation().getConclusionGrade());
	    row.setCell(" "); // EP
	    row.setCell(process.getCandidacyPrecedentDegreeInformation().getDegreeDesignation() + " / "
		    + process.getCandidacyPrecedentDegreeInformation().getInstitution().getName());
	    row.setCell(" "); // Affinity
	    row.setCell(" "); // DegreeNature
	    row.setCell(" "); // Grade
	    row.setCell(" "); // Interview Grade
	    row.setCell(" "); // Grade
	    row.setCell(" "); // Result
	}

	return spreadsheet;
    }

    private void addHeader(final Spreadsheet spreadsheet) {
	// TODO temporary
	spreadsheet.setHeaders(new String[] { "Nome", "Média Final Curso (MFC)", "Exp. Prof. (EP)", "Curso / Escola",
		"Afinidade", "Natureza", "Nota", "Entrevista", "Nota de seriação", "Resultado" });
    }

    private class CandidacyReport extends Spreadsheet {

	public CandidacyReport(final String name) {
	    super(name);
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
