package net.sourceforge.fenixedu.presentationTier.Action.commons.administrativeOffice.lists;

import static net.sourceforge.fenixedu.util.StringUtils.EMPTY;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists.SearchStudentsByDegreeParametersBean;
import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean;
import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationWithStateForExecutionYearBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.StringUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.utl.ist.fenix.tools.util.excel.StyledExcelSpreadsheet;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * @author - Ângela Almeida (argelina@ist.utl.pt)
 * 
 */

public abstract class StudentListByDegreeDA extends FenixDispatchAction {

    protected static final String RESOURCE_MODULE = "academicAdminOffice";

    private static final String YMD_FORMAT = "yyyy-MM-dd";

    public ActionForward prepareByDegree(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("searchParametersBean", getOrCreateSearchParametersBean());
	return mapping.findForward("searchRegistrations");
    }

    public ActionForward postBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final SearchStudentsByDegreeParametersBean searchParametersBean = getOrCreateSearchParametersBean();
	RenderUtils.invalidateViewState();
	request.setAttribute("searchParametersBean", searchParametersBean);

	return mapping.findForward("searchRegistrations");
    }

    private SearchStudentsByDegreeParametersBean getOrCreateSearchParametersBean() {
	SearchStudentsByDegreeParametersBean bean = (SearchStudentsByDegreeParametersBean) getRenderedObject("searchParametersBean");
	if (bean == null) {
	    bean = new SearchStudentsByDegreeParametersBean(getAdministratedDegreeTypes(), getAdministratedDegrees());
	}
	return bean;
    }

    public ActionForward searchByDegree(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final SearchStudentsByDegreeParametersBean searchBean = getOrCreateSearchParametersBean();

	final List<RegistrationWithStateForExecutionYearBean> registrations = search(searchBean);

	request.setAttribute("searchParametersBean", searchBean);
	request.setAttribute("studentCurricularPlanList", registrations);

	return mapping.findForward("searchRegistrations");
    }

    private static List<RegistrationWithStateForExecutionYearBean> search(final SearchStudentsByDegreeParametersBean searchbean) {

	final Set<Registration> registrations = new TreeSet<Registration>(Registration.NUMBER_COMPARATOR);

	final Degree chosenDegree = searchbean.getDegree();
	final DegreeType chosenDegreeType = searchbean.getDegreeType();
	final ExecutionYear executionYear = searchbean.getExecutionYear();
	for (final ExecutionDegree executionDegree : executionYear.getExecutionDegreesSet()) {
	    final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
	    if ((chosenDegreeType != null && degreeCurricularPlan.getDegreeType() != chosenDegreeType)) {
		continue;
	    }
	    if (chosenDegree != null && degreeCurricularPlan.getDegree() != chosenDegree) {
		continue;
	    }
	    if (degreeCurricularPlan.getDegreeType() != DegreeType.EMPTY) {
		if (!searchbean.getAdministratedDegreeTypes().contains(degreeCurricularPlan.getDegreeType())) {
		    continue;
		}
		if (!searchbean.getAdministratedDegrees().contains(degreeCurricularPlan.getDegree())) {
		    continue;
		}
	    }
	    degreeCurricularPlan.getRegistrations(executionYear, registrations);
	}

	return filterResults(searchbean, registrations, executionYear);
    }

    private static List<RegistrationWithStateForExecutionYearBean> filterResults(SearchStudentsByDegreeParametersBean searchbean,
	    final Set<Registration> registrations, final ExecutionYear executionYear) {

	final List<RegistrationWithStateForExecutionYearBean> result = new ArrayList<RegistrationWithStateForExecutionYearBean>();
	for (final Registration registration : registrations) {

	    if (!searchbean.getRegistrationAgreements().isEmpty()
		    && !searchbean.getRegistrationAgreements().contains(registration.getRegistrationAgreement())) {
		continue;
	    }

	    if (searchbean.hasAnyStudentStatuteType() && !hasStudentStatuteType(searchbean, registration)) {
		continue;
	    }

	    final RegistrationState lastRegistrationState = registration.getLastRegistrationState(executionYear);
	    if (lastRegistrationState == null
		    || (!searchbean.getRegistrationStateTypes().isEmpty() && !searchbean.getRegistrationStateTypes().contains(
			    lastRegistrationState.getStateType()))) {
		continue;
	    }

	    if (searchbean.getActiveEnrolments() && !registration.hasAnyEnrolmentsIn(executionYear)) {
		continue;
	    }

	    if (searchbean.getStandaloneEnrolments() && !registration.hasAnyStandaloneEnrolmentsIn(executionYear)) {
		continue;
	    }

	    if ((searchbean.getRegime() != null) && (registration.getRegimeType(executionYear) != searchbean.getRegime())) {
		continue;
	    }

	    if ((searchbean.getNationality() != null) && (registration.getPerson().getCountry() != searchbean.getNationality())) {
		continue;
	    }

	    result.add(new RegistrationWithStateForExecutionYearBean(registration, lastRegistrationState.getStateType()));
	}

	return result;
    }

    static private boolean hasStudentStatuteType(final SearchStudentsByDegreeParametersBean searchbean,
	    final Registration registration) {
	return CollectionUtils.containsAny(searchbean.getStudentStatuteTypes(), registration.getStudent()
		.getStatutesTypesValidOnAnyExecutionSemesterFor(searchbean.getExecutionYear()));
    }

    public ActionForward exportInfoToExcel(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixServiceException, FenixFilterException {

	final SearchStudentsByDegreeParametersBean searchBean = getOrCreateSearchParametersBean();
	if (searchBean != null) {
	    final List<RegistrationWithStateForExecutionYearBean> registrations = search(searchBean);

	    try {
		String filename;

		ExecutionYear executionYear = searchBean.getExecutionYear();
		if (searchBean.getDegree() == null) {
		    filename = executionYear.getYear();
		} else {
		    filename = searchBean.getDegree().getNameFor(executionYear).getContent().replace(' ', '_') + "_"
			    + executionYear.getYear();
		}

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment; filename=" + filename + ".xls");
		ServletOutputStream writer = response.getOutputStream();

		final String param = request.getParameter("extendedInfo");
		boolean extendedInfo = param != null && param.length() > 0 && Boolean.valueOf(param).booleanValue();

		exportToXls(registrations, writer, searchBean.getExecutionYear(), searchBean.getDegree(), extendedInfo);
		writer.flush();
		response.flushBuffer();

	    } catch (IOException e) {
		throw new FenixServiceException();
	    }
	}
	return null;
    }

    private void exportToXls(List<RegistrationWithStateForExecutionYearBean> registrationWithStateForExecutionYearBean,
	    OutputStream outputStream, ExecutionYear executionYear, Degree degree, boolean extendedInfo) throws IOException {

	final StyledExcelSpreadsheet spreadsheet = new StyledExcelSpreadsheet(
		getResourceMessage("lists.studentByDegree.unspaced"));
	spreadsheet.newHeaderRow();
	if (degree == null) {
	    spreadsheet.addHeader(executionYear.getYear());
	} else {
	    spreadsheet.addHeader(degree.getNameFor(executionYear) + " - " + executionYear.getYear());
	}
	spreadsheet.newRow();
	spreadsheet.newRow();
	spreadsheet.addCell(registrationWithStateForExecutionYearBean.size() + " " + getResourceMessage("label.students"));
	fillSpreadSheet(registrationWithStateForExecutionYearBean, spreadsheet, executionYear, extendedInfo);
	spreadsheet.getWorkbook().write(outputStream);
    }

    private void fillSpreadSheet(List<RegistrationWithStateForExecutionYearBean> registrations,
	    final StyledExcelSpreadsheet spreadsheet, ExecutionYear executionYear, boolean extendedInfo) {
	setHeaders(spreadsheet, extendedInfo);
	for (RegistrationWithStateForExecutionYearBean registrationWithStateForExecutionYearBean : registrations) {
	    Registration registration = registrationWithStateForExecutionYearBean.getRegistration();
	    spreadsheet.newRow();
	    Degree degree = registration.getDegree();
	    spreadsheet.addCell(!(StringUtils.isEmpty(degree.getSigla())) ? degree.getSigla() : degree.getNameFor(executionYear)
		    .toString());
	    spreadsheet.addCell(registration.getNumber().toString());
	    final Person person = registration.getPerson();
	    spreadsheet.addCell(person.getName());
	    final RegistrationState lastRegistrationState = registration.getLastRegistrationState(executionYear);

	    spreadsheet.addCell(lastRegistrationState.getStateType().getDescription());
	    spreadsheet.addCell(registration.getRegistrationAgreement().getName());

	    if (extendedInfo) {
		spreadsheet.addCell(registration.getPerson().getCountry().getName());
		spreadsheet.addCell(person.getDefaultEmailAddress() == null ? EMPTY : person.getDefaultEmailAddress().getValue());
		spreadsheet.addCell(person.getGender().toLocalizedString());
		spreadsheet.addCell(person.getDateOfBirthYearMonthDay() == null ? EMPTY : person.getDateOfBirthYearMonthDay()
			.toString(YMD_FORMAT));
		spreadsheet.addCell(registration.getEnrolmentsExecutionYears().size());
		spreadsheet.addCell(registration.getCurricularYear(executionYear));
		spreadsheet.addCell(registration.getEnrolments(executionYear).size());
		spreadsheet.addCell(getEnumNameFromResources(registration.getRegimeType(executionYear)));

		fillSpreadSheetPreBolonhaInfo(spreadsheet, registration);
		if (getAdministratedCycleTypes().contains(CycleType.FIRST_CYCLE)) {
		    fillSpreadSheetBolonhaInfo(spreadsheet, registration, registration.getLastStudentCurricularPlan().getCycle(
			    CycleType.FIRST_CYCLE));
		}
		if (getAdministratedCycleTypes().contains(CycleType.SECOND_CYCLE)) {
		    fillSpreadSheetBolonhaInfo(spreadsheet, registration, registration.getLastStudentCurricularPlan().getCycle(
			    CycleType.SECOND_CYCLE));
		}
		if (getAdministratedCycleTypes().contains(CycleType.THIRD_CYCLE)) {
		    fillSpreadSheetBolonhaInfo(spreadsheet, registration, registration.getLastStudentCurricularPlan().getCycle(
			    CycleType.THIRD_CYCLE));
		}
	    }
	}
    }

    private void fillSpreadSheetPreBolonhaInfo(StyledExcelSpreadsheet spreadsheet, Registration registration) {
	if (!registration.isBolonha()) {
	    RegistrationConclusionBean registrationConclusionBean = new RegistrationConclusionBean(registration);
	    fillSpreadSheetRegistrationInfo(spreadsheet, registrationConclusionBean, registration.hasConcluded());
	} else {
	    fillSpreadSheetEmptyCells(spreadsheet);
	}
    }

    private void fillSpreadSheetBolonhaInfo(StyledExcelSpreadsheet spreadsheet, Registration registration,
	    CycleCurriculumGroup cycle) {
	if ((cycle != null) && (!cycle.isExternal())) {
	    RegistrationConclusionBean registrationConclusionBean = new RegistrationConclusionBean(registration, cycle);
	    fillSpreadSheetRegistrationInfo(spreadsheet, registrationConclusionBean, registrationConclusionBean.isConcluded());
	} else {
	    fillSpreadSheetEmptyCells(spreadsheet);
	}
    }

    private void fillSpreadSheetRegistrationInfo(StyledExcelSpreadsheet spreadsheet,
	    RegistrationConclusionBean registrationConclusionBean, boolean isConcluded) {
	spreadsheet.addCell(getResourceMessage("label." + (isConcluded ? "yes" : "no") + ".capitalized"));
	spreadsheet.addCell(isConcluded ? registrationConclusionBean.getConclusionDate().toString(YMD_FORMAT) : EMPTY);
	spreadsheet.addCell(registrationConclusionBean.getAverage().toString());
    }

    private void fillSpreadSheetEmptyCells(StyledExcelSpreadsheet spreadsheet) {
	spreadsheet.addCell(EMPTY);
	spreadsheet.addCell(EMPTY);
	spreadsheet.addCell(EMPTY);
    }

    private void setHeaders(final StyledExcelSpreadsheet spreadsheet, final boolean extendedInfo) {
	spreadsheet.newHeaderRow();
	spreadsheet.addHeader(getResourceMessage("label.degree"));
	spreadsheet.addHeader(getResourceMessage("label.number"));
	spreadsheet.addHeader(getResourceMessage("label.name"));
	spreadsheet.addHeader(getResourceMessage("label.registration.state"));
	spreadsheet.addHeader(getResourceMessage("label.registrationAgreement"));
	if (extendedInfo) {
	    spreadsheet.addHeader(getResourceMessage("label.nationality"));
	    spreadsheet.addHeader(getResourceMessage("label.email"));
	    spreadsheet.addHeader(getResourceMessage("label.gender"));
	    spreadsheet.addHeader(getResourceMessage("label.dateOfBirth"));
	    spreadsheet.addHeader(getResourceMessage("label.registration.enrolments.number.short"));
	    spreadsheet.addHeader(getResourceMessage("curricular.year"));
	    spreadsheet.addHeader(getResourceMessage("label.student.enrolments.number.short"));
	    spreadsheet.addHeader(getResourceMessage("registration.regime"));
	    spreadsheet.addHeader(getResourceMessage("degree.concluded"));
	    spreadsheet.addHeader(getResourceMessage("label.conclusionDate"));
	    spreadsheet.addHeader(getResourceMessage("degree.average"));
	    if (getAdministratedCycleTypes().contains(CycleType.FIRST_CYCLE)) {
		spreadsheet.addHeader(getResourceMessage("label.firstCycle.concluded"));
		spreadsheet.addHeader(getResourceMessage("label.firstCycle.conclusionDate"));
		spreadsheet.addHeader(getResourceMessage("label.firstCycle.average"));
	    }
	    if (getAdministratedCycleTypes().contains(CycleType.SECOND_CYCLE)) {
		spreadsheet.addHeader(getResourceMessage("label.secondCycle.concluded"));
		spreadsheet.addHeader(getResourceMessage("label.secondCycle.conclusionDate"));
		spreadsheet.addHeader(getResourceMessage("label.secondCycle.average"));
	    }
	    if (getAdministratedCycleTypes().contains(CycleType.THIRD_CYCLE)) {
		spreadsheet.addHeader(getResourceMessage("label.thirdCycle.concluded"));
		spreadsheet.addHeader(getResourceMessage("label.thirdCycle.conclusionDate"));
		spreadsheet.addHeader(getResourceMessage("label.thirdCycle.average"));
	    }
	}
    }

    protected String getResourceMessage(String key) {
	return getResourceMessageFromModuleOrApplication(RESOURCE_MODULE, key);
    }

    protected abstract Set<CycleType> getAdministratedCycleTypes();

    protected abstract Set<DegreeType> getAdministratedDegreeTypes();

    protected abstract Set<Degree> getAdministratedDegrees();
}