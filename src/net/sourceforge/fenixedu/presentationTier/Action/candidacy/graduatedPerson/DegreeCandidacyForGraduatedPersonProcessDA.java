package net.sourceforge.fenixedu.presentationTier.Action.candidacy.graduatedPerson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.SortedSet;
import java.util.Map.Entry;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.graduatedPerson.DegreeCandidacyForGraduatedPersonIndividualProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.graduatedPerson.DegreeCandidacyForGraduatedPersonProcess;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.period.DegreeCandidacyForGraduatedPersonCandidacyPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.candidacy.CandidacyProcessDA;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Forward;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Forwards;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Mapping;
import net.sourceforge.fenixedu.util.report.Spreadsheet;
import net.sourceforge.fenixedu.util.report.SpreadsheetXLSExporter;
import net.sourceforge.fenixedu.util.report.Spreadsheet.Row;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.utl.ist.fenix.tools.util.i18n.Language;

@Mapping(path = "/caseHandlingDegreeCandidacyForGraduatedPersonProcess", module = "academicAdminOffice", formBeanClass = CandidacyProcessDA.CandidacyProcessForm.class)
@Forwards( { @Forward(name = "intro", path = "/candidacy/mainCandidacyProcess.jsp"),
	@Forward(name = "prepare-create-new-process", path = "/candidacy/createCandidacyPeriod.jsp"),
	@Forward(name = "prepare-edit-candidacy-period", path = "/candidacy/editCandidacyPeriod.jsp"),
	@Forward(name = "send-to-coordinator", path = "/candidacy/graduatedPerson/sendToCoordinator.jsp"),
	@Forward(name = "send-to-scientificCouncil", path = "/candidacy/graduatedPerson/sendToScientificCouncil.jsp")

})
public class DegreeCandidacyForGraduatedPersonProcessDA extends CandidacyProcessDA {

    @Override
    protected Class getProcessType() {
	return DegreeCandidacyForGraduatedPersonProcess.class;
    }

    @Override
    protected Class getChildProcessType() {
	return DegreeCandidacyForGraduatedPersonIndividualProcess.class;
    }

    @Override
    protected Class getCandidacyPeriodType() {
	return DegreeCandidacyForGraduatedPersonCandidacyPeriod.class;
    }

    @Override
    protected DegreeCandidacyForGraduatedPersonProcess getProcess(HttpServletRequest request) {
	return (DegreeCandidacyForGraduatedPersonProcess) super.getProcess(request);
    }

    @Override
    protected CandidacyProcess getCandidacyProcess(final ExecutionInterval executionInterval) {
	return executionInterval.hasDegreeCandidacyForGraduatedPersonCandidacyPeriod() ? executionInterval
		.getDegreeCandidacyForGraduatedPersonCandidacyPeriod().getDegreeCandidacyForGraduatedPersonProcess() : null;
    }

    @Override
    protected ActionForward introForward(final ActionMapping mapping) {
	return mapping.findForward("intro");
    }

    @Override
    protected void setStartInformation(ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
	if (!hasExecutionInterval(request)) {
	    request.setAttribute("executionInterval", ExecutionYear.readCurrentExecutionYear());
	}
	setCandidacyProcessInformation(request, getCandidacyProcess(getExecutionInterval(request)));
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

    public ActionForward prepareExecutePrintCandidacies(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {

	response.setContentType("application/vnd.ms-excel");
	response.setHeader("Content-disposition", "attachment; filename=" + getReportFilename());

	writeReport(getProcess(request), response.getOutputStream());
	response.getOutputStream().flush();
	response.flushBuffer();
	return null;
    }

    private void writeReport(final DegreeCandidacyForGraduatedPersonProcess process, final ServletOutputStream outputStream)
	    throws IOException {
	final List<Spreadsheet> spreadsheets = new ArrayList<Spreadsheet>();
	for (final Entry<Degree, SortedSet<DegreeCandidacyForGraduatedPersonIndividualProcess>> entry : process
		.getValidDegreeCandidaciesForGraduatedPersonsByDegree().entrySet()) {
	    spreadsheets.add(buildReport(entry.getKey(), entry.getValue()));
	}
	new SpreadsheetXLSExporter().exportToXLSSheets(outputStream, spreadsheets);
    }

    private Spreadsheet buildReport(final Degree degree,
	    final SortedSet<DegreeCandidacyForGraduatedPersonIndividualProcess> candidacyProcesses) {

	final Spreadsheet spreadsheet = new Spreadsheet(degree.getSigla(), getHeader());
	for (final DegreeCandidacyForGraduatedPersonIndividualProcess process : candidacyProcesses) {
	    final Row row = spreadsheet.addRow();
	    row.setCell(process.getCandidacyPerson().getName());
	    row.setCell(process.getCandidacyPrecedentDegreeInformation().getDegreeAndInstitutionName());
	    row.setCell(process.getCandidacyAffinity());
	    row.setCell(process.getCandidacyDegreeNature());
	    row.setCell(process.getCandidacyPrecedentDegreeInformation().getConclusionGrade());
	    row.setCell(process.getCandidacyGrade());
	    if (process.isCandidacyAccepted() || process.isCandidacyRejected()) {
		row.setCell(ResourceBundle.getBundle("resources/EnumerationResources", Language.getLocale()).getString(
			process.getCandidacyState().getQualifiedName()));
	    } else {
		row.setCell("");
	    }
	}

	return spreadsheet;
    }

    private List<Object> getHeader() {
	final ResourceBundle bundle = ResourceBundle.getBundle("resources/ApplicationResources", Language.getLocale());
	final List<Object> result = new ArrayList<Object>();
	result.add(bundle.getString("label.name"));
	result.add(bundle.getString("label.candidacy.degree.and.school"));
	result.add(bundle.getString("label.candidacy.affinity"));
	result.add(bundle.getString("label.candidacy.degreeNature"));
	result.add(bundle.getString("label.candidacy.mfc"));
	result.add(bundle.getString("label.candidacy.grade"));
	result.add(bundle.getString("label.candidacy.result"));
	return result;
    }

}
