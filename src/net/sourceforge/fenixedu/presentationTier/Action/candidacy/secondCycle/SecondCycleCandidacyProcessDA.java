package net.sourceforge.fenixedu.presentationTier.Action.candidacy.secondCycle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Map.Entry;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleIndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleIndividualCandidacyResultBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.period.SecondCycleCandidacyPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.candidacy.CandidacyProcessDA;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Forward;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Forwards;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import net.sourceforge.fenixedu.util.report.Spreadsheet;
import net.sourceforge.fenixedu.util.report.SpreadsheetXLSExporter;
import net.sourceforge.fenixedu.util.report.Spreadsheet.Row;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.LocalDate;

@Mapping(path = "/caseHandlingSecondCycleCandidacyProcess", module = "academicAdminOffice", formBeanClass = CandidacyProcessDA.CandidacyProcessForm.class)
@Forwards( { @Forward(name = "intro", path = "/candidacy/mainCandidacyProcess.jsp"),
	@Forward(name = "prepare-create-new-process", path = "/candidacy/createCandidacyPeriod.jsp"),
	@Forward(name = "prepare-edit-candidacy-period", path = "/candidacy/editCandidacyPeriod.jsp"),
	@Forward(name = "send-to-coordinator", path = "/candidacy/secondCycle/sendToCoordinator.jsp"),
	@Forward(name = "introduce-candidacy-results", path = "/candidacy/secondCycle/introduceCandidacyResults.jsp"),
	@Forward(name = "send-to-scientificCouncil", path = "/candidacy/secondCycle/sendToScientificCouncil.jsp")

})
public class SecondCycleCandidacyProcessDA extends CandidacyProcessDA {

    @Override
    protected Class getProcessType() {
	return SecondCycleCandidacyProcess.class;
    }

    @Override
    protected Class getChildProcessType() {
	return SecondCycleIndividualCandidacyProcess.class;
    }

    @Override
    protected Class getCandidacyPeriodType() {
	return SecondCycleCandidacyPeriod.class;
    }

    @Override
    protected SecondCycleCandidacyProcess getProcess(HttpServletRequest request) {
	return (SecondCycleCandidacyProcess) super.getProcess(request);
    }

    @Override
    protected void setStartInformation(ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
	if (!hasExecutionInterval(request)) {
	    request.setAttribute("executionInterval", ExecutionYear.readCurrentExecutionYear());
	}
	setCandidacyProcessInformation(request, getCandidacyProcess(getExecutionInterval(request)));
    }

    @Override
    protected ActionForward introForward(ActionMapping mapping) {
	return mapping.findForward("intro");
    }

    @Override
    protected SecondCycleCandidacyProcess getCandidacyProcess(final ExecutionInterval executionInterval) {
	return executionInterval.hasSecondCycleCandidacyPeriod() ? executionInterval.getSecondCycleCandidacyPeriod()
		.getSecondCycleCandidacyProcess() : null;
    }

    public ActionForward prepareExecuteSendToCoordinator(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	return mapping.findForward("send-to-coordinator");
    }

    public ActionForward executeSendToCoordinator(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	try {
	    executeActivity(getProcess(request), "SendToCoordinator");
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    return prepareExecuteSendToCoordinator(mapping, actionForm, request, response);
	}
	return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    public ActionForward prepareExecutePrintCandidacies(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {

	response.setContentType("application/vnd.ms-excel");
	response.setHeader("Content-disposition", "attachment; filename=Candidaturas_2_Ciclo_"
		+ new LocalDate().toString("ddMMyyyy") + ".xls");

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

    public ActionForward prepareExecuteSendToScientificCouncil(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	return mapping.findForward("send-to-scientificCouncil");
    }

    public ActionForward executeSendToScientificCouncil(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	try {
	    executeActivity(getProcess(request), "SendToScientificCouncil");
	} catch (final DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    return prepareExecuteSendToScientificCouncil(mapping, actionForm, request, response);
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
	final Spreadsheet spreadsheet = new CandidacyReport(degree.getSigla(), getHeader());

	for (final SecondCycleIndividualCandidacyProcess process : values) {
	    final Row row = spreadsheet.addRow();
	    row.setCell(process.getCandidacyPerson().getName());
	    row.setCell(process.getCandidacyPrecedentDegreeInformation().getConclusionGrade());
	    row.setCell(process.getCandidacyProfessionalExperience() != null ? process.getCandidacyProfessionalExperience()
		    .toString() : " ");
	    row.setCell(process.getCandidacyPrecedentDegreeInformation().getDegreeDesignation() + " / "
		    + process.getCandidacyPrecedentDegreeInformation().getInstitution().getName());
	    row.setCell(process.getCandidacyAffinity() != null ? process.getCandidacyAffinity().toString() : " ");
	    row.setCell(process.getCandidacyDegreeNature() != null ? process.getCandidacyDegreeNature().toString() : " ");
	    row.setCell(process.getCandidacyGrade() != null ? process.getCandidacyGrade().toString() : " ");
	    row.setCell(process.getCandidacyInterviewGrade() != null ? process.getCandidacyInterviewGrade() : " ");
	    row.setCell(process.getCandidacySeriesGrade() != null ? process.getCandidacySeriesGrade().toString() : " ");
	    if (process.isCandidacyAccepted() || process.isCandidacyRejected()) {
		row.setCell(ResourceBundle.getBundle("resources/EnumerationResources", Language.getLocale()).getString(
			process.getCandidacyState().getQualifiedName()));
	    } else {
		row.setCell(" ");
	    }
	}

	return spreadsheet;
    }

    private List<Object> getHeader() {
	final ResourceBundle bundle = ResourceBundle.getBundle("resources/ApplicationResources", Language.getLocale());
	final List<Object> result = new ArrayList<Object>();
	result.add(bundle.getString("label.name"));
	result.add(bundle.getString("label.candidacy.mfc"));
	result.add(bundle.getString("label.candidacy.professionalExperience"));
	result.add(bundle.getString("label.candidacy.degree.and.school"));
	result.add(bundle.getString("label.candidacy.affinity"));
	result.add(bundle.getString("label.candidacy.degreeNature"));
	result.add(bundle.getString("label.candidacy.grade"));
	result.add(bundle.getString("label.candidacy.interviewGrade"));
	result.add(bundle.getString("label.candidacy.seriesGrade"));
	result.add(bundle.getString("label.candidacy.result"));
	return result;
    }

    private class CandidacyReport extends Spreadsheet {

	public CandidacyReport(final String name, final List<Object> header) {
	    super(name, header);
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
