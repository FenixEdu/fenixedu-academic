package net.sourceforge.fenixedu.presentationTier.Action.candidacy.degreeChange;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
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
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyPrecedentDegreeInformation;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.degreeChange.DegreeChangeCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.degreeChange.DegreeChangeIndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.degreeChange.DegreeChangeIndividualCandidacyResultBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.period.DegreeChangeCandidacyPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.candidacy.CandidacyProcessDA;
import net.sourceforge.fenixedu.util.report.StyledExcelSpreadsheet;

import org.apache.poi.hssf.util.Region;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.i18n.Language;

@Mapping(path = "/caseHandlingDegreeChangeCandidacyProcess", module = "academicAdminOffice", formBeanClass = CandidacyProcessDA.CandidacyProcessForm.class)
@Forwards( { @Forward(name = "intro", path = "/candidacy/mainCandidacyProcess.jsp"),
	@Forward(name = "prepare-create-new-process", path = "/candidacy/createCandidacyPeriod.jsp"),
	@Forward(name = "prepare-edit-candidacy-period", path = "/candidacy/editCandidacyPeriod.jsp"),
	@Forward(name = "send-to-coordinator", path = "/candidacy/sendToCoordinator.jsp"),
	@Forward(name = "send-to-scientificCouncil", path = "/candidacy/sendToScientificCouncil.jsp"),
	@Forward(name = "view-candidacy-results", path = "/candidacy/degreeChange/viewCandidacyResults.jsp"),
	@Forward(name = "introduce-candidacy-results", path = "/candidacy/degreeChange/introduceCandidacyResults.jsp"),
	@Forward(name = "create-registrations", path = "/candidacy/createRegistrations.jsp")

})
public class DegreeChangeCandidacyProcessDA extends CandidacyProcessDA {

    private static final int MAX_GRADE_VALUE = 20;

    @Override
    protected Class getProcessType() {
	return DegreeChangeCandidacyProcess.class;
    }

    @Override
    protected Class getCandidacyPeriodType() {
	return DegreeChangeCandidacyPeriod.class;
    }

    @Override
    protected Class getChildProcessType() {
	return DegreeChangeIndividualCandidacyProcess.class;
    }

    @Override
    protected CandidacyProcess getCandidacyProcess(final ExecutionInterval executionInterval) {
	return executionInterval.hasDegreeChangeCandidacyPeriod() ? executionInterval.getDegreeChangeCandidacyPeriod()
		.getDegreeChangeCandidacyProcess() : null;
    }

    @Override
    protected DegreeChangeCandidacyProcess getProcess(HttpServletRequest request) {
	return (DegreeChangeCandidacyProcess) super.getProcess(request);
    }

    @Override
    protected ActionForward introForward(ActionMapping mapping) {
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

    public ActionForward prepareExecutePrintCandidaciesFromInstitutionDegrees(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws IOException {

	response.setContentType("application/vnd.ms-excel");
	response.setHeader("Content-disposition", "attachment; filename="
		+ getLabel("label.candidacy.degreeChange.institution.report.filename") + ".xls");
	writeReportForInstitutionDegrees(getProcess(request), response.getOutputStream());
	response.getOutputStream().flush();
	response.flushBuffer();
	return null;
    }

    private void writeReportForInstitutionDegrees(final DegreeChangeCandidacyProcess process,
	    final ServletOutputStream outputStream) throws IOException {
	final StyledExcelSpreadsheet excelSpreadsheet = new StyledExcelSpreadsheet();
	for (final Entry<Degree, SortedSet<DegreeChangeIndividualCandidacyProcess>> entry : process
		.getValidInstitutionIndividualCandidacyProcessesByDegree().entrySet()) {
	    createSpreadsheet(excelSpreadsheet, entry.getKey(), entry.getValue());
	}
	excelSpreadsheet.getWorkbook().write(outputStream);
    }

    public ActionForward prepareExecutePrintCandidaciesFromExternalDegrees(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws IOException {

	response.setContentType("application/vnd.ms-excel");
	response.setHeader("Content-disposition", "attachment; filename="
		+ getLabel("label.candidacy.degreeChange.external.report.filename") + ".xls");
	writeReportForExternalDegrees(getProcess(request), response.getOutputStream());
	response.getOutputStream().flush();
	response.flushBuffer();
	return null;
    }

    private String getLabel(final String key) {
	return ResourceBundle.getBundle("resources/ApplicationResources", Language.getLocale()).getString(key);
    }

    private void writeReportForExternalDegrees(final DegreeChangeCandidacyProcess process, final ServletOutputStream outputStream)
	    throws IOException {
	final StyledExcelSpreadsheet excelSpreadsheet = new StyledExcelSpreadsheet();
	for (final Entry<Degree, SortedSet<DegreeChangeIndividualCandidacyProcess>> entry : process
		.getValidExternalIndividualCandidacyProcessesByDegree().entrySet()) {
	    createSpreadsheet(excelSpreadsheet, entry.getKey(), entry.getValue());
	}
	excelSpreadsheet.getWorkbook().write(outputStream);
    }

    private void createSpreadsheet(final StyledExcelSpreadsheet excelSpreadsheet, final Degree degree,
	    final SortedSet<DegreeChangeIndividualCandidacyProcess> candidacies) {
	excelSpreadsheet.getSheet(degree.getSigla());
	createHeader(excelSpreadsheet, degree);
	createBody(excelSpreadsheet, candidacies);
    }

    private void createBody(final StyledExcelSpreadsheet excelSpreadsheet,
	    final SortedSet<DegreeChangeIndividualCandidacyProcess> candidacies) {
	for (final DegreeChangeIndividualCandidacyProcess process : candidacies) {
	    excelSpreadsheet.newRow();
	    if (process.hasCandidacyStudent()) {
		excelSpreadsheet.addCell(process.getCandidacyStudent().getNumber());
	    } else {
		excelSpreadsheet.addCell("-");
	    }
	    excelSpreadsheet.addCell(process.getCandidacyPerson().getName());
	    final CandidacyPrecedentDegreeInformation information = process.getCandidacyPrecedentDegreeInformation();
	    excelSpreadsheet.addCell(information.getDegreeAndInstitutionName());
	    excelSpreadsheet.addCell(getValue(process.getCandidacyAffinity()));
	    excelSpreadsheet.addCell(getValue(process.getCandidacyDegreeNature()));
	    excelSpreadsheet.addCell(getValue(information.getNumberOfApprovedCurricularCourses()));
	    excelSpreadsheet.addCell(getValue(information.getGradeSum()));
	    excelSpreadsheet.addCell(getValue(information.getApprovedEcts()));
	    excelSpreadsheet.addCell(getValue(information.getEnroledEcts()));
	    excelSpreadsheet.addCell(getValue(calculateA(process, true)));
	    excelSpreadsheet.addCell(getValue(calculateB(process, true)));
	    excelSpreadsheet.addCell(getValue(calculateC(process)));
	    if (process.isCandidacyAccepted() || process.isCandidacyRejected()) {
		excelSpreadsheet.addCell(ResourceBundle.getBundle("resources/EnumerationResources", Language.getLocale())
			.getString(process.getCandidacyState().getQualifiedName()).toUpperCase());
	    } else {
		excelSpreadsheet.addCell("");
	    }
	}
    }

    private String getValue(final Object value) {
	return value != null ? value.toString() : "";
    }

    private String getValue(final BigDecimal value) {
	return value != null ? value.toPlainString() : "";
    }

    private BigDecimal calculateA(final DegreeChangeIndividualCandidacyProcess process, final boolean setScale) {
	if (process.getCandidacyApprovedEctsRate() != null) {
	    return process.getCandidacyApprovedEctsRate();
	}
	
	final BigDecimal approvedEcts = process.getCandidacyPrecedentDegreeInformation().getApprovedEcts();
	final BigDecimal enroledEcts = process.getCandidacyPrecedentDegreeInformation().getEnroledEcts();
	if (approvedEcts != null && enroledEcts != null && enroledEcts.signum() > 0) {
	    final BigDecimal result = approvedEcts.divide(enroledEcts, MathContext.DECIMAL32);
	    return setScale ? result.setScale(2, RoundingMode.HALF_EVEN) : result;
	}
	return null;
    }

    private BigDecimal calculateB(final DegreeChangeIndividualCandidacyProcess process, final boolean setScale) {
	if (process.getCandidacyGradeRate() != null) {
	    return process.getCandidacyGradeRate();
	}
	
	final Integer total = process.getCandidacyPrecedentDegreeInformation().getNumberOfApprovedCurricularCourses();
	final BigDecimal gradeSum = process.getCandidacyPrecedentDegreeInformation().getGradeSum();
	if (gradeSum != null && total != null && total.intValue() != 0) {
	    final BigDecimal result = gradeSum.divide(new BigDecimal(total.intValue() * MAX_GRADE_VALUE), MathContext.DECIMAL32);
	    return setScale ? result.setScale(2, RoundingMode.HALF_EVEN) : result;
	}
	return null;
    }

    private BigDecimal calculateC(final DegreeChangeIndividualCandidacyProcess process) {
	if (process.getCandidacySeriesCandidacyGrade() != null) {
	    return process.getCandidacySeriesCandidacyGrade().setScale(2, RoundingMode.HALF_EVEN);
	}
	
	final BigDecimal affinity = process.getCandidacyAffinity();
	final Integer nature = process.getCandidacyDegreeNature();
	final BigDecimal valueA = calculateA(process, false);
	final BigDecimal valueB = calculateB(process, false);
	if (valueA != null && valueB != null && affinity != null && nature != null) {
	    final BigDecimal value03 = new BigDecimal("0.3");
	    final BigDecimal aff = new BigDecimal(affinity.toString()).multiply(new BigDecimal("0.4"), MathContext.DECIMAL32);
	    final BigDecimal nat = new BigDecimal(nature).multiply(value03).divide(new BigDecimal(5), MathContext.DECIMAL32);
	    final BigDecimal abp = valueA.add(valueB).multiply(value03).divide(new BigDecimal(2), MathContext.DECIMAL32);
	    return aff.add(nat).add(abp).multiply(new BigDecimal(200)).setScale(2, RoundingMode.HALF_EVEN);
	}
	return null;
    }

    private void createHeader(final StyledExcelSpreadsheet spreadsheet, final Degree degree) {
	final ResourceBundle bundle = ResourceBundle.getBundle("resources/ApplicationResources", Language.getLocale());

	// title
	spreadsheet.newHeaderRow();
	spreadsheet.addCell(degree.getName(), spreadsheet.getExcelStyle().getTitleStyle());

	// empty row
	spreadsheet.newHeaderRow();

	// table header
	spreadsheet.newHeaderRow();
	spreadsheet.addHeader(bundle.getString("label.candidacy.identification"));
	spreadsheet.addHeader(2, bundle.getString("label.candidacy.degree.and.school"));
	spreadsheet.addHeader(bundle.getString("label.candidacy.affinity"));
	spreadsheet.addHeader(bundle.getString("label.candidacy.degreeNature"));
	spreadsheet.addHeader(bundle.getString("label.candidacy.concludedUCs"));
	spreadsheet.addHeader(8, "");
	spreadsheet.addHeader(bundle.getString("label.candidacy.approvedEctsRate"));
	spreadsheet.addHeader(bundle.getString("label.candidacy.gradeRate"));
	spreadsheet.addHeader(bundle.getString("label.candidacy.degreeChange.seriesCandidacyGrade"));
	spreadsheet.addHeader(bundle.getString("label.candidacy.result"));

	spreadsheet.newHeaderRow();
	spreadsheet.addHeader(bundle.getString("label.number"));
	spreadsheet.addHeader(bundle.getString("label.name"));
	spreadsheet.addHeader(5, bundle.getString("label.number"));
	spreadsheet.addHeader(bundle.getString("label.candidacy.gradeSum.abbr"));
	spreadsheet.addHeader(bundle.getString("label.candidacy.approvedEcts"));
	spreadsheet.addHeader(bundle.getString("label.candidacy.enroledEcts"));

	// Id + Nº + Nome merge
	spreadsheet.getSheet().addMergedRegion(new Region(2, (short) 0, 2, (short) 1));
	// Degree name merge
	spreadsheet.getSheet().addMergedRegion(new Region(2, (short) 2, 3, (short) 2));
	// affinity merge
	spreadsheet.getSheet().addMergedRegion(new Region(2, (short) 3, 3, (short) 3));
	// degreeNature merge
	spreadsheet.getSheet().addMergedRegion(new Region(2, (short) 4, 3, (short) 4));
	// UCs merge
	spreadsheet.getSheet().addMergedRegion(new Region(2, (short) 5, 2, (short) 7));
	// A merge
	spreadsheet.getSheet().addMergedRegion(new Region(2, (short) 9, 3, (short) 9));
	// B merge
	spreadsheet.getSheet().addMergedRegion(new Region(2, (short) 10, 3, (short) 10));
	// C merge
	spreadsheet.getSheet().addMergedRegion(new Region(2, (short) 11, 3, (short) 11));
	// result merge
	spreadsheet.getSheet().addMergedRegion(new Region(2, (short) 12, 3, (short) 12));
    }

    public ActionForward prepareExecuteIntroduceCandidacyResults(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("individualCandidaciesByDegree", getProcess(request)
		.getValidDegreeChangeIndividualCandidacyProcessesByDegree());
	return mapping.findForward("view-candidacy-results");
    }

    public ActionForward prepareExecuteIntroduceCandidacyResultsForDegree(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	final List<DegreeChangeIndividualCandidacyResultBean> beans = new ArrayList<DegreeChangeIndividualCandidacyResultBean>();
	for (DegreeChangeIndividualCandidacyProcess process : getProcess(request)
		.getValidDegreeChangeIndividualCandidacyProcesses(getAndSetDegree(request))) {
	    beans.add(new DegreeChangeIndividualCandidacyResultBean(process));
	}
	request.setAttribute("individualCandidacyResultBeans", beans);
	return mapping.findForward("introduce-candidacy-results");
    }

    private Degree getAndSetDegree(final HttpServletRequest request) {
	final Degree degree = rootDomainObject.readDegreeByOID(getIntegerFromRequest(request, "degreeId"));
	request.setAttribute("degree", degree);
	return degree;
    }

    public ActionForward executeIntroduceCandidacyResultsInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	getAndSetDegree(request);
	request.setAttribute("individualCandidacyResultBeans", getRenderedObject("individualCandidacyResultBeans"));
	return mapping.findForward("introduce-candidacy-results");
    }

    public ActionForward executeIntroduceCandidacyResults(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	try {
	    executeActivity(getProcess(request), "IntroduceCandidacyResults", getRenderedObject("individualCandidacyResultBeans"));
	} catch (final DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	    return executeIntroduceCandidacyResultsInvalid(mapping, actionForm, request, response);
	}
	return prepareExecuteIntroduceCandidacyResults(mapping, actionForm, request, response);
    }

    static public class DegreeChangeCandidacyDegreeBean extends CandidacyDegreeBean {
	DegreeChangeCandidacyDegreeBean(final DegreeChangeIndividualCandidacyProcess process) {
	    setPerson(process.getCandidacyPerson());
	    setDegree(process.getCandidacySelectedDegree());
	    setState(process.getCandidacyState());
	    setRegistrationCreated(process.hasRegistrationForCandidacy());
	}
    }

    @Override
    protected List<CandidacyDegreeBean> createCandidacyDegreeBeans(HttpServletRequest request) {
	final List<CandidacyDegreeBean> result = new ArrayList<CandidacyDegreeBean>();
	for (final DegreeChangeIndividualCandidacyProcess child : getProcess(request)
		.getAcceptedDegreeChangeIndividualCandidacyProcesses()) {
	    result.add(new DegreeChangeCandidacyDegreeBean(child));
	}
	return result;
    }
}
