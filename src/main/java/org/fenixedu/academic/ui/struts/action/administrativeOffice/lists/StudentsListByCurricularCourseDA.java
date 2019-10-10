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
/**
 * 
 */
package org.fenixedu.academic.ui.struts.action.administrativeOffice.lists;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.contacts.PartyContact;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.groups.PermissionService;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumModule;
import org.fenixedu.academic.dto.academicAdministration.SearchStudentsByCurricularCourseParametersBean;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.action.academicAdministration.AcademicAdministrationApplication.AcademicAdminListingsApp;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.fenixedu.commons.spreadsheet.Spreadsheet;
import org.fenixedu.commons.spreadsheet.Spreadsheet.Row;
import org.fenixedu.commons.spreadsheet.StyledExcelSpreadsheet;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

/**
 * @author - Angela Almeida (argelina@ist.utl.pt)
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@StrutsFunctionality(app = AcademicAdminListingsApp.class, path = "students-by-curricular-course", titleKey = "link.studentsListByCurricularCourse", accessGroup = "academic(STUDENT_LISTINGS)")
@Mapping(path = "/studentsListByCurricularCourse", module = "academicAdministration")
@Forwards({ @Forward(name = "chooseCurricularCourse", path = "/academicAdminOffice/lists/chooseCurricularCourses.jsp"),
		@Forward(name = "studentByCurricularCourse", path = "/academicAdminOffice/lists/studentsByCurricularCourses.jsp") })
public class StudentsListByCurricularCourseDA extends FenixDispatchAction {

	@EntryPoint
	public ActionForward prepareByCurricularCourse(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

		request.setAttribute("searchBean", getOrCreateSearchBean());
		return mapping.findForward("chooseCurricularCourse");
	}

	public ActionForward chooseExecutionYearPostBack(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) {

		SearchStudentsByCurricularCourseParametersBean searchBean = getOrCreateSearchBean();
		RenderUtils.invalidateViewState();
		request.setAttribute("searchBean", searchBean);

		return mapping.findForward("chooseCurricularCourse");
	}

	private SearchStudentsByCurricularCourseParametersBean getOrCreateSearchBean() {
		SearchStudentsByCurricularCourseParametersBean bean = getRenderedObject("searchBean");
		if (bean == null) {
			Set<Degree> degrees = AcademicAccessRule
					.getDegreesAccessibleToFunction(AcademicOperationType.STUDENT_LISTINGS, Authenticate.getUser())
					.collect(Collectors.toSet());
			degrees.addAll(PermissionService.getDegrees("STUDENT_LISTINGS", Authenticate.getUser()));
			bean = new SearchStudentsByCurricularCourseParametersBean(degrees);
		}
		return bean;
	}

	public ActionForward showActiveCurricularCourseScope(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws FenixActionException, FenixServiceException {

		final SearchStudentsByCurricularCourseParametersBean searchBean = getOrCreateSearchBean();

		final List<Context> contexts = searchBean.getDegreeCurricularPlan().getCurricularCoursesSet().stream()
				.flatMap(cc -> cc.getParentContextsByExecutionYear(searchBean.getExecutionYear()).stream())
				.sorted(Context.COMPARATOR_BY_DEGREE_MODULE_NAME).collect(Collectors.toList());

		if (contexts.isEmpty()) {
			addActionMessage("message", request, "error.nonExisting.AssociatedCurricularCourses");
		} else {
			request.setAttribute("contexts", contexts);
		}

		request.setAttribute("searchBean", searchBean);
		return mapping.findForward("chooseCurricularCourse");
	}

	public ActionForward searchByCurricularCourse(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) {

		final CurricularCourse curricularCourse = getDomainObject(request, "curricularCourseCode");
		final Integer semester = getIntegerFromRequest(request, "semester");
		final ExecutionYear executionYear = getDomainObject(request, "executionYearID");

		request.setAttribute("semester", semester);
		request.setAttribute("year", getIntegerFromRequest(request, "year"));
		request.setAttribute("curricularYear", executionYear);
		request.setAttribute("enrolmentList", searchStudentByCriteria(executionYear, curricularCourse, semester));
		SearchStudentsByCurricularCourseParametersBean bean = getOrCreateSearchBean();
		bean.setExecutionYear(executionYear);
		bean.setCurricularCourse(curricularCourse);
		bean.setDegreeCurricularPlan(curricularCourse.getDegreeCurricularPlan());
		request.setAttribute("searchBean", bean);

		return mapping.findForward("studentByCurricularCourse");
	}

	private List<Enrolment> searchStudentByCriteria(final ExecutionYear executionYear,
			final CurricularCourse curricularCourse, final Integer semester) {
		final List<Enrolment> result = new ArrayList<Enrolment>();

		final ExecutionSemester executionSemester = executionYear.getExecutionSemesterFor(semester);
		for (final Enrolment enrolment : curricularCourse.getEnrolmentsByExecutionPeriod(executionSemester)) {
			result.add(enrolment);
		}
		Collections.sort(result, new BeanComparator("studentCurricularPlan.registration.number"));

		return result;
	}

	public ActionForward exportInfoToExcel(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws FenixServiceException {
		return doExportInfoToExcel(mapping, actionForm, request, response, false);
	}

	public ActionForward exportDetailedInfoToExcel(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {
		return doExportInfoToExcel(mapping, actionForm, request, response, true);
	}

	public ActionForward doExportInfoToExcel(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response, Boolean detailed) throws FenixServiceException {

		final CurricularCourse curricularCourse = getDomainObject(request, "curricularCourseCode");
		final Integer semester = getIntegerFromRequest(request, "semester");
		final ExecutionYear executionYear = ExecutionYear
				.readExecutionYearByName((String) getFromRequest(request, "curricularYear"));
		final String year = (String) getFromRequest(request, "year");

		try {
			String filename = getResourceMessage("label.students") + "_" + curricularCourse.getName() + "_("
					+ curricularCourse.getDegreeCurricularPlan().getName() + ")_" + executionYear.getYear();

			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition",
					"attachment; filename=\"" + filename.replace(" ", "_") + ".xls\"");
			ServletOutputStream writer = response.getOutputStream();

			exportToXls(searchStudentByCriteria(executionYear, curricularCourse, semester), writer, executionYear,
					curricularCourse, year, semester.toString(), detailed);
			writer.flush();
			response.flushBuffer();

		} catch (IOException e) {
			throw new FenixServiceException();
		}

		return null;

	}

	private void exportToXls(List<Enrolment> registrations, OutputStream outputStream, ExecutionYear executionYear,
			CurricularCourse curricularCourse, String year, String semester, Boolean detailed) throws IOException {

		final StyledExcelSpreadsheet spreadsheet = new StyledExcelSpreadsheet(
				getResourceMessage("lists.studentByCourse.unspaced"));
		fillSpreadSheetFilters(executionYear, curricularCourse, year, semester, spreadsheet);
		fillSpreadSheetResults(registrations, spreadsheet, executionYear, detailed);
		spreadsheet.getWorkbook().write(outputStream);
	}

	private void fillSpreadSheetFilters(ExecutionYear executionYear, CurricularCourse curricularCourse, String year,
			String semester, final StyledExcelSpreadsheet spreadsheet) {
		spreadsheet.newHeaderRow();
		spreadsheet.addHeader(curricularCourse.getDegree().getNameFor(executionYear) + " - "
				+ curricularCourse.getName() + " - " + executionYear.getYear() + " - " + year + " "
				+ getResourceMessage("label.year") + " " + semester + " " + getResourceMessage("label.semester"));
	}

	private void fillSpreadSheetResults(List<Enrolment> registrations, final StyledExcelSpreadsheet spreadsheet,
			ExecutionYear executionYear, Boolean detailed) {
		spreadsheet.newRow();
		spreadsheet.newRow();
		spreadsheet.addCell(registrations.size() + " " + getResourceMessage("label.students"));

		setHeaders(spreadsheet, detailed);
		for (Enrolment registrationWithStateForExecutionYearBean : registrations) {
			Registration registration = registrationWithStateForExecutionYearBean.getRegistration();
			spreadsheet.newRow();
			spreadsheet.addCell(registration.getNumber().toString());
			spreadsheet.addCell(registration.getPerson().getName());
			spreadsheet.addCell(registration.getRegistrationProtocol().getCode());
			Degree degree = registration.getDegree();
			spreadsheet.addCell(!(StringUtils.isEmpty(degree.getSigla())) ? degree.getSigla()
					: degree.getNameFor(executionYear).getContent());
			spreadsheet.addCell(registrationWithStateForExecutionYearBean.getEnrollmentState().getDescription());
			spreadsheet.addCell(registrationWithStateForExecutionYearBean.getEvaluationSeason().getName().getContent());
			if (detailed) {
				spreadsheet.addCell(registration.getPerson().hasDefaultEmailAddress()
						? registration.getPerson().getDefaultEmailAddressValue()
						: "-");
				spreadsheet.addCell(registration.getPerson().hasInstitutionalEmailAddress()
						? registration.getPerson().getInstitutionalEmailAddressValue()
						: "-");
				PartyContact mobileContact = getMobileContact(registration.getPerson());
				spreadsheet.addCell(mobileContact != null ? mobileContact.getPresentationValue() : "-");

			}

		}
	}

	private PartyContact getMobileContact(final Person person) {
		for (PartyContact contact : person.getPartyContactsSet()) {
			if (contact.isMobile()) {
				return contact;
			}
		}
		return null;
	}

	private void setHeaders(final StyledExcelSpreadsheet spreadsheet, Boolean detailed) {
		spreadsheet.newHeaderRow();
		spreadsheet.addHeader(getResourceMessage("label.student.number"));
		spreadsheet.addHeader(getResourceMessage("label.name"));
		spreadsheet.addHeader(getResourceMessage("label.registrationAgreement"));
		spreadsheet.addHeader(getResourceMessage("label.degree"));
		spreadsheet.addHeader(getResourceMessage("label.state"));
		spreadsheet.addHeader(getResourceMessage("label.epoch"));
		if (detailed) {
			spreadsheet.addHeader(getResourceMessage("label.email"));
			spreadsheet.addHeader(getResourceMessage("label.institutional.email"));
			spreadsheet.addHeader(getResourceMessage("label.mobile"));
		}
	}

	public ActionForward downloadStatistics(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ExecutionYear executionYear = getDomainObject(request, "executionYearId");
		Set<Degree> degreesToInclude = AcademicAccessRule
				.getDegreesAccessibleToFunction(AcademicOperationType.STUDENT_LISTINGS, Authenticate.getUser())
				.collect(Collectors.toSet());
		degreesToInclude.addAll(PermissionService.getDegrees("STUDENT_LISTINGS", Authenticate.getUser()));

		final String filename = getResourceMessage("label.statistics") + "_"
				+ executionYear.getName().replace('/', '-');
		final Spreadsheet spreadsheet = new Spreadsheet(filename);
		addStatisticsHeaders(spreadsheet);
		addStatisticsInformation(spreadsheet, executionYear, degreesToInclude);

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment; filename=" + filename + ".xls");
		ServletOutputStream writer = response.getOutputStream();

		spreadsheet.exportToXLSSheet(writer);
		writer.flush();
		response.flushBuffer();

		return null;
	}

	private void addStatisticsHeaders(final Spreadsheet spreadsheet) {
		spreadsheet.setHeader(getResourceMessage("label.degree.acronym"));
		spreadsheet.setHeader(getResourceMessage("label.degree.name"));
		spreadsheet.setHeader(getResourceMessage("label.curricularCourse.name"));
		spreadsheet.setHeader(getResourceMessage("label.degree.numberOfEnrolments"));
		spreadsheet.setHeader(getResourceMessage("label.degree.numberOfEnrolments.first.semester"));
		spreadsheet.setHeader(getResourceMessage("label.degree.numberOfEnrolments.second.semester"));
	}

	private void addStatisticsInformation(final Spreadsheet spreadsheet, final ExecutionYear executionYear,
			final Set<Degree> degreesToInclude) {

		executionYear.getExecutionDegreesSet().stream().map(ed -> ed.getDegreeCurricularPlan())
				.forEach(degreeCurricularPlan -> {
					final Degree degree = degreeCurricularPlan.getDegree();
					if (degreesToInclude == null || degreesToInclude.contains(degree)) {
						for (final CurricularCourse curricularCourse : degreeCurricularPlan.getAllCurricularCourses()) {
							if (curricularCourse.isActive(executionYear)) {
								final int enrolmentCount = countEnrolments(curricularCourse, executionYear);
								final Row row = spreadsheet.addRow();
								row.setCell(degree.getSigla());
								row.setCell(degree.getPresentationName(executionYear));
								row.setCell(curricularCourse.getName());
								row.setCell(Integer.toString(enrolmentCount));
								row.setCell(Integer.toString(
										countEnrolments(curricularCourse, executionYear.getFirstExecutionPeriod())));
								row.setCell(Integer.toString(
										countEnrolments(curricularCourse, executionYear.getLastExecutionPeriod())));
							}
						}
					}
				});

	}

	private int countEnrolments(final CurricularCourse curricularCourse, final ExecutionInterval interval) {
		int c = 0;
		for (final CurriculumModule curriculumModule : curricularCourse.getCurriculumModulesSet()) {
			if (curriculumModule.isEnrolment()) {
				final Enrolment enrolment = (Enrolment) curriculumModule;
				final ExecutionInterval executionInterval = enrolment.getExecutionInterval();
				if (interval == executionInterval || interval == executionInterval.getExecutionYear()) {
					c++;
				}
			}
		}
		return c;
	}

	static private String getResourceMessage(String key) {
		return BundleUtil.getString(Bundle.ACADEMIC, key);
	}

	protected Set<DegreeType> getAdministratedDegreeTypes() {
		Set<DegreeType> programs = AcademicAccessRule
				.getDegreeTypesAccessibleToFunction(AcademicOperationType.STUDENT_LISTINGS, Authenticate.getUser())
				.collect(Collectors.toSet());
		programs.addAll(PermissionService.getDegreeTypes("STUDENT_LISTINGS", Authenticate.getUser()));
		return programs;
	}
}
