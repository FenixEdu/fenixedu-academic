package net.sourceforge.fenixedu.presentationTier.Action.candidacy.standalone;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.standalone.StandaloneCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.standalone.StandaloneIndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.standalone.StandaloneIndividualCandidacyResultBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.period.StandaloneCandidacyPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.candidacy.CandidacyProcessDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;
import pt.utl.ist.fenix.tools.util.i18n.Language;

@Mapping(path = "/caseHandlingStandaloneCandidacyProcess", module = "academicAdminOffice", formBeanClass = StandaloneCandidacyProcessDA.StandaloneCandidacyProcessForm.class)
@Forwards({

	@Forward(name = "intro", path = "/candidacy/standalone/mainCandidacyProcess.jsp", tileProperties = @Tile(title = "private.academicadministrativeoffice.applications.isolatedcurriculum")),
	@Forward(name = "prepare-create-new-process", path = "/candidacy/createCandidacyPeriod.jsp", tileProperties = @Tile(title = "private.academicadministrativeoffice.applications.isolatedcurriculum")),
	@Forward(name = "prepare-edit-candidacy-period", path = "/candidacy/editCandidacyPeriod.jsp", tileProperties = @Tile(title = "private.academicadministrativeoffice.applications.isolatedcurriculum")),
	@Forward(name = "send-to-coordinator", path = "/candidacy/sendToCoordinator.jsp", tileProperties = @Tile(title = "private.academicadministrativeoffice.applications.isolatedcurriculum")),
	@Forward(name = "view-candidacy-results", path = "/candidacy/standalone/viewCandidacyResults.jsp", tileProperties = @Tile(title = "private.academicadministrativeoffice.applications.isolatedcurriculum")),
	@Forward(name = "insert-candidacy-results", path = "/candidacy/standalone/introduceCandidacyResults.jsp", tileProperties = @Tile(title = "private.academicadministrativeoffice.applications.isolatedcurriculum")),
	@Forward(name = "create-registrations", path = "/candidacy/createRegistrations.jsp", tileProperties = @Tile(title = "private.academicadministrativeoffice.applications.isolatedcurriculum"))

})
public class StandaloneCandidacyProcessDA extends CandidacyProcessDA {

    static public class StandaloneCandidacyProcessForm extends CandidacyProcessForm {
	private Integer selectedProcessId;

	public Integer getSelectedProcessId() {
	    return selectedProcessId;
	}

	public void setSelectedProcessId(Integer selectedProcessId) {
	    this.selectedProcessId = selectedProcessId;
	}
    }

    @Override
    protected Class getProcessType() {
	return StandaloneCandidacyProcess.class;
    }

    @Override
    protected Class getChildProcessType() {
	return StandaloneIndividualCandidacyProcess.class;
    }

    @Override
    protected Class getCandidacyPeriodType() {
	return StandaloneCandidacyPeriod.class;
    }

    @Override
    protected StandaloneCandidacyProcess getProcess(HttpServletRequest request) {
	return (StandaloneCandidacyProcess) super.getProcess(request);
    }

    @Override
    protected void setStartInformation(ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
	if (!hasExecutionInterval(request)) {
	    final List<ExecutionInterval> executionIntervals = getExecutionIntervalsWithCandidacyPeriod();

	    if (executionIntervals.size() == 1) {
		final ExecutionInterval executionInterval = executionIntervals.get(0);
		final List<StandaloneCandidacyProcess> candidacyProcesses = getCandidacyProcesses(executionInterval);

		if (candidacyProcesses.size() == 1) {
		    setCandidacyProcessInformation(request, candidacyProcesses.get(0));
		    setCandidacyProcessInformation(actionForm, getProcess(request));
		    request.setAttribute("candidacyProcesses", candidacyProcesses);
		    return;
		}
	    }

	    request.setAttribute("canCreateProcess", canCreateProcess(getProcessType().getName()));
	    request.setAttribute("executionIntervals", executionIntervals);

	} else {
	    final ExecutionInterval executionInterval = getExecutionInterval(request);
	    final StandaloneCandidacyProcess candidacyProcess = getCandidacyProcess(request, executionInterval);

	    if (candidacyProcess != null) {
		setCandidacyProcessInformation(request, candidacyProcess);
		setCandidacyProcessInformation(actionForm, getProcess(request));
	    } else {
		final List<StandaloneCandidacyProcess> candidacyProcesses = getCandidacyProcesses(executionInterval);

		if (candidacyProcesses.size() == 1) {
		    setCandidacyProcessInformation(request, candidacyProcesses.get(0));
		    setCandidacyProcessInformation(actionForm, getProcess(request));
		    request.setAttribute("candidacyProcesses", candidacyProcesses);
		    return;
		}

		request.setAttribute("canCreateProcess", canCreateProcess(getProcessType().getName()));
		request.setAttribute("executionIntervals", getExecutionIntervalsWithCandidacyPeriod());
	    }
	    request.setAttribute("candidacyProcesses", getCandidacyProcesses(executionInterval));
	}
    }

    private List<ExecutionInterval> getExecutionIntervalsWithCandidacyPeriod() {
	return ExecutionInterval.readExecutionIntervalsWithCandidacyPeriod(getCandidacyPeriodType());
    }

    private List<StandaloneCandidacyProcess> getCandidacyProcesses(final ExecutionInterval executionInterval) {
	final List<StandaloneCandidacyProcess> result = new ArrayList<StandaloneCandidacyProcess>();
	for (final StandaloneCandidacyPeriod period : executionInterval.getStandaloneCandidacyPeriods()) {
	    result.add(period.getStandaloneCandidacyProcess());
	}
	return result;
    }

    private void setCandidacyProcessInformation(final ActionForm actionForm, final StandaloneCandidacyProcess process) {
	final StandaloneCandidacyProcessForm form = (StandaloneCandidacyProcessForm) actionForm;
	form.setSelectedProcessId(process.getIdInternal());
	form.setExecutionIntervalId(process.getCandidacyExecutionInterval().getIdInternal());
    }

    @Override
    protected StandaloneCandidacyProcess getCandidacyProcess(final HttpServletRequest request,
	    final ExecutionInterval executionInterval) {
	final Integer selectedProcessId = getIntegerFromRequest(request, "selectedProcessId");
	if (selectedProcessId != null) {
	    for (final StandaloneCandidacyPeriod candidacyPeriod : executionInterval.getStandaloneCandidacyPeriods()) {
		if (candidacyPeriod.getStandaloneCandidacyProcess().getIdInternal().equals(selectedProcessId)) {
		    return candidacyPeriod.getStandaloneCandidacyProcess();
		}
	    }
	}
	return null;
    }

    @Override
    protected ActionForward introForward(ActionMapping mapping) {
	return mapping.findForward("intro");
    }

    @Override
    public ActionForward listProcessAllowedActivities(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	setCandidacyProcessInformation(request, getProcess(request));
	setCandidacyProcessInformation(form, getProcess(request));
	request.setAttribute("candidacyProcesses", getCandidacyProcesses(getProcess(request).getCandidacyExecutionInterval()));
	return introForward(mapping);
    }

    @Override
    public ActionForward prepareCreateNewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("candidacyProcessBean", new CandidacyProcessBean(ExecutionSemester.readActualExecutionSemester()));
	return mapping.findForward("prepare-create-new-process");
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
	response.setHeader("Content-disposition", "attachment; filename=" + getReportFilename());

	final ServletOutputStream writer = response.getOutputStream();
	writeReport(getProcess(request), writer);
	writer.flush();
	response.flushBuffer();
	return null;
    }

    private void writeReport(final StandaloneCandidacyProcess process, final ServletOutputStream writer) throws IOException {
	final Spreadsheet spreadsheet = createSpreadSheet();
	for (final StandaloneIndividualCandidacyProcess candidacy : process
		.getSortedStandaloneIndividualCandidaciesThatCanBeSendToJury()) {
	    addRow(spreadsheet, candidacy);
	}
	spreadsheet.exportToXLSSheet(writer);
    }

    private void addRow(final Spreadsheet spreadsheet, final StandaloneIndividualCandidacyProcess candidacy) {
	final Row row = spreadsheet.addRow();
	row.setCell(candidacy.getPersonalDetails().getName());
	row.setCell(candidacy.getPersonalDetails().getDocumentIdNumber());

	final StringBuilder names = new StringBuilder();
	final Iterator<CurricularCourse> elements = candidacy.getCurricularCourses().iterator();
	while (elements.hasNext()) {
	    names.append(elements.next().getName(candidacy.getCandidacyExecutionInterval()));
	    names.append(elements.hasNext() ? ", " : "");
	}
	row.setCell(names.toString());
    }

    private Spreadsheet createSpreadSheet() {
	final ResourceBundle bundle = ResourceBundle.getBundle("resources/ApplicationResources", Language.getLocale());
	final Spreadsheet spreadsheet = new Spreadsheet("Candidacies");

	spreadsheet.setHeaders(new String[] {

	bundle.getString("label.name"),

	bundle.getString("label.identificationNumber"),

	bundle.getString("label.curricularCourses"),

	});
	return spreadsheet;
    }

    static public class StandaloneCandidacyDegreeBean extends CandidacyDegreeBean {

	public StandaloneCandidacyDegreeBean(final StandaloneIndividualCandidacyProcess process) {
	    setPersonalDetails(process.getPersonalDetails());
	    setDegree(process.getCandidacySelectedDegree());
	    setState(process.getCandidacyState());
	    setRegistrationCreated(process.hasRegistrationForCandidacy());
	}
    }

    @Override
    protected List<CandidacyDegreeBean> createCandidacyDegreeBeans(HttpServletRequest request) {
	final StandaloneCandidacyProcess process = getProcess(request);
	final List<CandidacyDegreeBean> result = new ArrayList<CandidacyDegreeBean>();
	for (final StandaloneIndividualCandidacyProcess element : process.getAcceptedStandaloneIndividualCandidacies()) {
	    result.add(new StandaloneCandidacyDegreeBean(element));
	}
	return result;
    }

    public ActionForward prepareExecuteIntroduceCandidacyResults(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	setInformationToIntroduceCandidacyResults(request);
	return mapping.findForward("view-candidacy-results");
    }

    private void setInformationToIntroduceCandidacyResults(HttpServletRequest request) {
	final StandaloneCandidacyProcess process = getProcess(request);
	final List<StandaloneIndividualCandidacyResultBean> beans = new ArrayList<StandaloneIndividualCandidacyResultBean>();
	for (final StandaloneIndividualCandidacyProcess candidacy : process
		.getSortedStandaloneIndividualCandidaciesThatCanBeSendToJury()) {
	    beans.add(new StandaloneIndividualCandidacyResultBean(candidacy));
	}
	request.setAttribute("standaloneIndividualCandidacyResultBeans", beans);
    }

    public ActionForward prepareIntroduceCandidacyResults(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	setInformationToIntroduceCandidacyResults(request);
	return mapping.findForward("insert-candidacy-results");
    }

    public ActionForward executeIntroduceCandidacyResults(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	try {
	    executeActivity(getProcess(request), "IntroduceCandidacyResults",
		    getRenderedObject("standaloneIndividualCandidacyResultBeans"));
	} catch (final DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    request.setAttribute("standaloneIndividualCandidacyResultBeans",
		    getRenderedObject("standaloneIndividualCandidacyResultBeans"));
	    return mapping.findForward("insert-candidacy-results");
	}
	return listProcessAllowedActivities(mapping, actionForm, request, response);
    }

    public ActionForward executeIntroduceCandidacyResultsInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("standaloneIndividualCandidacyResultBeans",
		getRenderedObject("standaloneIndividualCandidacyResultBeans"));
	return mapping.findForward("insert-candidacy-results");
    }

    @Override
    protected Spreadsheet buildIndividualCandidacyReport(Spreadsheet spreadsheet,
	    IndividualCandidacyProcess individualCandidacyProcess) {
	// TODO Auto-generated method stub
	return null;
    }
}
