package net.sourceforge.fenixedu.presentationTier.Action.departmentMember.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.ValuationGroupingDTOEntry;
import net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.ValuationTeacherDTOEntry;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.CourseValuation;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.CourseValuationType;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.CurricularCourseValuation;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.CurricularCourseValuationGroup;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ProfessorshipValuation;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationCompetenceCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationCurricularCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationGrouping;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationPhase;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationTeacher;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationValueType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class ProfessorshipValuationAction extends FenixDispatchAction {

	private static final Integer VIEW_PROFESSORSHIP_VALUATION_BY_TEACHERS = 0;

	private static final Integer VIEW_PROFESSORSHIP_VALUATION_BY_COURSES = 1;

	public ActionForward prepareForProfessorshipValuation(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		getFromRequestAndSetOnFormTeacherServiceDistributionId(request, (DynaActionForm) form);
		initializeVariables((DynaActionForm) form);

		return loadProfessorshipValuations(mapping, form, request, response);
	}

	public ActionForward loadValuationGrouping(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		DynaActionForm dynaForm = (DynaActionForm) form;

		dynaForm.set("valuationCompetenceCourse", null);
		dynaForm.set("valuationCurricularCourse", null);
		dynaForm.set("curricularCourseValuationGroup", null);
		dynaForm.set("valuationTeacher", null);

		return loadProfessorshipValuations(mapping, form, request, response);
	}

	public ActionForward loadValuationCompetenceCourse(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		DynaActionForm dynaForm = (DynaActionForm) form;
		
		dynaForm.set("valuationCurricularCourse", null);
		dynaForm.set("curricularCourseValuationGroup", null);

		return loadProfessorshipValuations(mapping, form, request, response);
	}

	public ActionForward setProfessorshipValuation(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;

		TeacherServiceDistribution teacherServiceDistribution = getTeacherServiceDistribution(userView, dynaForm);

		ValuationPhase currentValuationPhase = teacherServiceDistribution.getCurrentValuationPhase();
		ValuationCompetenceCourse selectedValuationCompetenceCourse = getSelectedValuationCompetenceCourse(
				userView,
				dynaForm,
				null);
		ValuationTeacher selectedValuationTeacher = getSelectedValuationTeacher(userView, dynaForm, null);
		ExecutionPeriod selectedExecutionPeriod = getSelectedExecutionPeriod(userView, dynaForm, null);

		Map<String, Object> professorshipValuationParameters = obtainProfessorshipParametersFromForm(dynaForm);
		CourseValuation courseValuation = null;
		CourseValuationType courseValuationType = selectedValuationCompetenceCourse.getCourseValuationType(
				currentValuationPhase,
				selectedExecutionPeriod);

		if (courseValuationType == CourseValuationType.COMPETENCE_COURSE_VALUATION) {
			courseValuation = selectedValuationCompetenceCourse.getCompetenceCourseValuationByValuationPhaseAndExecutionPeriod(
					currentValuationPhase,
					selectedExecutionPeriod);
		} else if (courseValuationType == CourseValuationType.CURRICULAR_COURSE_VALUATION) {
			ValuationCurricularCourse selectedValuationCurricularCourse = getSelectedValuationCurricularCourse(
					userView,
					dynaForm,
					null);
			courseValuation = selectedValuationCurricularCourse.getCurricularCourseValuationByValuationPhaseAndExecutionPeriod(
					currentValuationPhase,
					selectedExecutionPeriod);
		} else if (courseValuationType == CourseValuationType.CURRICULAR_COURSE_VALUATION_GROUP) {
			courseValuation = getSelectedCurricularCourseValuationGroup(userView, dynaForm, null);
		}

		Object[] parameters = new Object[] { courseValuation.getIdInternal(), selectedValuationTeacher.getIdInternal(),
				professorshipValuationParameters };

		ServiceUtils.executeService(userView, "SetProfessorshipValuation", parameters);

		return loadProfessorshipValuations(mapping, form, request, response);
	}

	public ActionForward removeProfessorshipValuation(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;

		ProfessorshipValuation selectedProfessorshipValuation = getSelectedProfessorshipValuation(userView, dynaForm);

		ServiceUtils.executeService(
				userView,
				"DeleteProfessorshipValuation",
				new Object[] { selectedProfessorshipValuation.getIdInternal() });

		return loadProfessorshipValuations(mapping, form, request, response);
	}

	public ActionForward loadExecutionPeriod(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		DynaActionForm dynaForm = (DynaActionForm) form;

		dynaForm.set("valuationCompetenceCourse", null);
		dynaForm.set("valuationCurricularCourse", null);
		dynaForm.set("curricularCourseValuationGroup", null);

		return loadProfessorshipValuations(mapping, form, request, response);
	}

	public ActionForward setExtraCredits(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;

		ValuationTeacher selectedValuationTeacher = getSelectedValuationTeacher(userView, dynaForm, null);

		String extraCreditsName = (String) dynaForm.get("extraCreditsName");
		Double extraCreditsValue = Double.parseDouble((String) dynaForm.get("extraCreditsValue"));
		Boolean usingExtraCredits = dynaForm.get("usingExtraCredits") == null ? false
				: (Boolean) dynaForm.get("usingExtraCredits");

		Object[] arguments = new Object[] {
				selectedValuationTeacher.getIdInternal(),
				extraCreditsName,
				extraCreditsValue,
				usingExtraCredits };

		ServiceUtils.executeService(userView, "SetExtraCreditsToValuationTeacher", arguments);
		
		return loadProfessorshipValuations(mapping, form, request, response);
	}

	public ActionForward loadProfessorshipValuations(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;

		TeacherServiceDistribution teacherServiceDistribution = getTeacherServiceDistribution(userView, dynaForm);
		ValuationPhase currentValuationPhase = teacherServiceDistribution.getCurrentValuationPhase();
		ValuationGrouping rootValuationGrouping = currentValuationPhase.getRootValuationGrouping();
		List<ValuationGroupingDTOEntry> valuationGroupingDTOEntryList = ValuationGroupingDTOEntry.getValuationGroupingOptionEntriesForPerson(currentValuationPhase, userView.getPerson(), false, true);
		ValuationGrouping selectedValuationGrouping = getSelectedValuationGrouping(
				userView,
				dynaForm,
				valuationGroupingDTOEntryList.get(0).getValuationGrouping());
		List<ExecutionPeriod> executionPeriodList = new ArrayList<ExecutionPeriod>(
				teacherServiceDistribution.getExecutionPeriods());
		Collections.sort(executionPeriodList, new BeanComparator("semester"));
		ExecutionPeriod selectedExecutionPeriod = getSelectedExecutionPeriod(userView, dynaForm, executionPeriodList);

		List<ValuationCompetenceCourse> activeValuationCompetenceCourseList = getActiveValuationCompetenceCourseList(
				selectedValuationGrouping,
				selectedExecutionPeriod);
		List<ValuationTeacher> valuationTeacherList = getValuationTeacherList(selectedValuationGrouping);
		Collections.sort(valuationTeacherList, new BeanComparator("name"));

		if (!activeValuationCompetenceCourseList.isEmpty()) {
			Collections.sort(activeValuationCompetenceCourseList, new BeanComparator("name"));
			ValuationCompetenceCourse selectedValuationCompetenceCourse = getSelectedValuationCompetenceCourse(
					userView,
					dynaForm,
					activeValuationCompetenceCourseList);
			List<ValuationCurricularCourse> valuationCurricularCourseList = null;
			ValuationCurricularCourse selectedValuationCurricularCourse = null;
			List<CurricularCourseValuationGroup> curricularCourseValuationGroupList = null;
			CurricularCourseValuationGroup selectedCurricularCourseValuationGroup = null;
			ValuationTeacher selectedValuationTeacher = null;

			CourseValuationType courseValuationType = selectedValuationCompetenceCourse.getCourseValuationType(
					currentValuationPhase,
					selectedExecutionPeriod);

			if (courseValuationType == CourseValuationType.COMPETENCE_COURSE_VALUATION) {

			} else if (courseValuationType == CourseValuationType.CURRICULAR_COURSE_VALUATION) {
				valuationCurricularCourseList = selectedValuationCompetenceCourse.getValuationCurricularCourses(selectedExecutionPeriod);
				selectedValuationCurricularCourse = getSelectedValuationCurricularCourse(
						userView,
						dynaForm,
						valuationCurricularCourseList);

			} else if (courseValuationType == CourseValuationType.CURRICULAR_COURSE_VALUATION_GROUP) {
				curricularCourseValuationGroupList = selectedValuationCompetenceCourse.getCurricularCourseValuationGroupsByValuationPhaseAndExecutionPeriod(
						currentValuationPhase,
						selectedExecutionPeriod);
				selectedCurricularCourseValuationGroup = getSelectedCurricularCourseValuationGroup(
						userView,
						dynaForm,
						curricularCourseValuationGroupList);
			}

			if (!valuationTeacherList.isEmpty()) {
				selectedValuationTeacher = getSelectedValuationTeacher(userView, dynaForm, valuationTeacherList);
			} else {
				request.setAttribute("notAvailableValuationTeachers", true);
			}

			fillFormAndRequest(
					request,
					dynaForm,
					currentValuationPhase,
					selectedValuationGrouping,
					activeValuationCompetenceCourseList,
					selectedValuationCompetenceCourse,
					valuationCurricularCourseList,
					selectedValuationCurricularCourse,
					curricularCourseValuationGroupList,
					selectedCurricularCourseValuationGroup,
					valuationTeacherList,
					selectedValuationTeacher,
					selectedExecutionPeriod);
		} else {
			request.setAttribute("notAvailableCompetenceCourses", true);
		}
		
		if (valuationTeacherList.isEmpty()) {
			request.setAttribute("notAvailableValuationTeachers", true);
		}
		
		request.setAttribute("valuationTeacherList", valuationTeacherList);
		request.setAttribute("selectedValuationTeacher", new ValuationTeacherDTOEntry(
				getSelectedValuationTeacher(userView, dynaForm, valuationTeacherList),
				selectedValuationGrouping.getValuationPhase().getTeacherServiceDistribution().getExecutionPeriods()));			

		request.setAttribute("teacherServiceDistribution", teacherServiceDistribution);
		request.setAttribute("valuationGroupingOptionEntryList", valuationGroupingDTOEntryList);
		request.setAttribute("executionPeriodList", executionPeriodList);

		Integer viewType = (Integer) dynaForm.get("viewType");

		if (viewType.equals(VIEW_PROFESSORSHIP_VALUATION_BY_COURSES)) {
			return mapping.findForward("professorshipValuationForm");
		} else {
			return mapping.findForward("professorshipValuationFormByValuationTeacher");
		}
	}

	private void fillFormAndRequest(
			HttpServletRequest request,
			DynaActionForm dynaForm,
			ValuationPhase valuationPhase,
			ValuationGrouping selectedValuationGrouping,
			List<ValuationCompetenceCourse> valuationCompetenceCourseList,
			ValuationCompetenceCourse selectedValuationCompetenceCourse,
			List<ValuationCurricularCourse> valuationCurricularCourseList,
			ValuationCurricularCourse selectedValuationCurricularCourse,
			List<CurricularCourseValuationGroup> curricularCourseValuationGroupList,
			CurricularCourseValuationGroup selectedCurricularCourseValuationGroup,
			List<ValuationTeacher> valuationTeacherList,
			ValuationTeacher selectedValuationTeacher,
			ExecutionPeriod executionPeriod) {

		request.setAttribute("valuationCompetenceCourseList", valuationCompetenceCourseList);
		request.setAttribute("selectedValuationCompetenceCourse", selectedValuationCompetenceCourse);
		request.setAttribute("valuationCurricularCourseList", valuationCurricularCourseList);
		request.setAttribute("selectedValuationCurricularCourse", selectedValuationCurricularCourse);
		request.setAttribute("curricularCourseValuationGroupList", curricularCourseValuationGroupList);
		request.setAttribute("selectedCurricularCourseValuationGroup", selectedCurricularCourseValuationGroup);

		request.setAttribute("valuationTeacherList", valuationTeacherList);

		List<ExecutionPeriod> executionPeriodList = new ArrayList<ExecutionPeriod>();
		executionPeriodList.add(executionPeriod);

		request.setAttribute("selectedValuationTeacher", new ValuationTeacherDTOEntry(
				selectedValuationTeacher,
				selectedValuationGrouping.getValuationPhase().getTeacherServiceDistribution().getExecutionPeriods()));	

		dynaForm.set("valuationCompetenceCourse", selectedValuationCompetenceCourse.getIdInternal());

		if (selectedValuationCurricularCourse != null) {
			dynaForm.set("valuationCurricularCourse", selectedValuationCurricularCourse.getIdInternal());
		}

		if (selectedCurricularCourseValuationGroup != null) {
			dynaForm.set("curricularCourseValuationGroup", selectedCurricularCourseValuationGroup.getIdInternal());
		}

		if (selectedValuationTeacher != null) {
			dynaForm.set("valuationTeacher", selectedValuationTeacher.getIdInternal());
		}

		CourseValuationType selectedCourseValuationType = selectedValuationCompetenceCourse.getCourseValuationType(
				valuationPhase,
				executionPeriod);
		request.setAttribute("selectedCourseValuationType", selectedCourseValuationType.toString());

		CourseValuation selectedCourseValuation = null;
		if (selectedCourseValuationType == CourseValuationType.COMPETENCE_COURSE_VALUATION) {
			selectedCourseValuation = selectedValuationCompetenceCourse.getCompetenceCourseValuationByValuationPhaseAndExecutionPeriod(
					valuationPhase,
					executionPeriod);
		} else if (selectedCourseValuationType == CourseValuationType.CURRICULAR_COURSE_VALUATION) {
			selectedCourseValuation = selectedValuationCurricularCourse.getCurricularCourseValuationByValuationPhaseAndExecutionPeriod(
					valuationPhase,
					executionPeriod);
		} else if (selectedCourseValuationType == CourseValuationType.CURRICULAR_COURSE_VALUATION_GROUP) {
			selectedCourseValuation = selectedCurricularCourseValuationGroup;
		}
		if (selectedCourseValuation == null) {
			request.setAttribute("courseValuationNotSelected", true);
		} else {
			request.setAttribute("selectedCourseValuation", selectedCourseValuation);
		}

		if (selectedValuationTeacher == null) {
			request.setAttribute("valuationTeacherNotSelected", true);
		}

		fillFormWithProfessorshipValuationParameters(dynaForm, selectedCourseValuation, selectedValuationTeacher);
	}

	private void fillFormWithProfessorshipValuationParameters(
			DynaActionForm dynaForm,
			CourseValuation selectedCourseValuation,
			ValuationTeacher selectedValuationTeacher) {

		if(selectedValuationTeacher != null) {
			dynaForm.set("extraCreditsName", selectedValuationTeacher.getExtraCreditsName());
			dynaForm.set("extraCreditsValue", selectedValuationTeacher.getExtraCreditsValue().toString());
			dynaForm.set("usingExtraCredits", selectedValuationTeacher.getUsingExtraCredits());
		}

		if (selectedCourseValuation == null || selectedValuationTeacher == null)
			return;

		ProfessorshipValuation professorshipValuation = selectedCourseValuation.getProfessorshipValuationByValuationTeacher(selectedValuationTeacher);

		if (professorshipValuation != null) {
			dynaForm.set("theoreticalHoursManual", professorshipValuation.getTheoreticalHoursManual().toString());
			dynaForm.set("theoreticalHoursType", professorshipValuation.getTheoreticalHoursType().toString());
			dynaForm.set("praticalHoursManual", professorshipValuation.getPraticalHoursManual().toString());
			dynaForm.set("praticalHoursType", professorshipValuation.getPraticalHoursType().toString());
			dynaForm.set("theoPratHoursManual", professorshipValuation.getTheoPratHoursManual().toString());
			dynaForm.set("theoPratHoursType", professorshipValuation.getTheoPratHoursType().toString());
			dynaForm.set("laboratorialHoursManual", professorshipValuation.getLaboratorialHoursManual().toString());
			dynaForm.set("laboratorialHoursType", professorshipValuation.getLaboratorialHoursType().toString());
		} else {
			dynaForm.set("theoreticalHoursManual", ((Double) 0d).toString());
			dynaForm.set("theoreticalHoursType", ValuationValueType.MANUAL_VALUE.toString());
			dynaForm.set("praticalHoursManual", ((Double) 0d).toString());
			dynaForm.set("praticalHoursType", ValuationValueType.MANUAL_VALUE.toString());
			dynaForm.set("theoPratHoursManual", ((Double) 0d).toString());
			dynaForm.set("theoPratHoursType", ValuationValueType.MANUAL_VALUE.toString());
			dynaForm.set("laboratorialHoursManual", ((Double) 0d).toString());
			dynaForm.set("laboratorialHoursType", ValuationValueType.MANUAL_VALUE.toString());
		}
	}

	private void initializeVariables(DynaActionForm dynaForm) {
		dynaForm.set("valuationGrouping", null);
		dynaForm.set("valuationCompetenceCourse", null);
		dynaForm.set("valuationCurricularCourse", null);
		dynaForm.set("curricularCourseValuationGroup", null);
		dynaForm.set("valuationTeacher", null);
		dynaForm.set("distributionViewAnchor", 0);
		dynaForm.set("viewType", VIEW_PROFESSORSHIP_VALUATION_BY_COURSES);
	}

	private Integer getFromRequestAndSetOnFormTeacherServiceDistributionId(
			HttpServletRequest request,
			DynaActionForm dynaForm) {
		Integer teacherServiceDistributionId = new Integer(request.getParameter("teacherServiceDistribution"));
		dynaForm.set("teacherServiceDistribution", teacherServiceDistributionId);
		return teacherServiceDistributionId;
	}

	private TeacherServiceDistribution getTeacherServiceDistribution(IUserView userView, DynaActionForm dynaForm) throws FenixServiceException, FenixFilterException {
		return rootDomainObject.readTeacherServiceDistributionByOID((Integer) dynaForm.get("teacherServiceDistribution"));
	}

	private ValuationGrouping getSelectedValuationGrouping(
			IUserView userView,
			DynaActionForm dynaForm,
			ValuationGrouping rootValuationGrouping) throws FenixFilterException, FenixServiceException {
		ValuationGrouping selectedValuationGrouping = rootDomainObject.readValuationGroupingByOID((Integer) dynaForm.get("valuationGrouping"));
		return (selectedValuationGrouping == null) ? rootValuationGrouping : selectedValuationGrouping;
	}

	private List<ValuationCompetenceCourse> getActiveValuationCompetenceCourseList(
			ValuationGrouping selectedValuationGrouping,
			ExecutionPeriod executionPeriod) {
		return new ArrayList<ValuationCompetenceCourse>(
				selectedValuationGrouping.getValuationCompetenceCoursesByExecutionPeriod(executionPeriod));
	}

	private List<ValuationTeacher> getValuationTeacherList(ValuationGrouping selectedValuationGrouping) {
		return new ArrayList<ValuationTeacher>(selectedValuationGrouping.getValuationTeachers());
	}

	private ValuationCompetenceCourse getSelectedValuationCompetenceCourse(
			IUserView userView,
			DynaActionForm dynaForm,
			List<ValuationCompetenceCourse> valuationCompetenceCourseList) throws FenixFilterException, FenixServiceException {
		ValuationCompetenceCourse valuationCompetenceCourse = rootDomainObject.readValuationCompetenceCourseByOID((Integer) dynaForm.get("valuationCompetenceCourse"));

		return (valuationCompetenceCourse == null) ? (valuationCompetenceCourseList != null
				&& !valuationCompetenceCourseList.isEmpty() ? valuationCompetenceCourseList.get(0) : null)
				: valuationCompetenceCourse;
	}

	private ValuationCurricularCourse getSelectedValuationCurricularCourse(
			IUserView userView,
			DynaActionForm dynaForm,
			List<ValuationCurricularCourse> valuationCurricularCourseList) throws FenixFilterException, FenixServiceException {
		ValuationCurricularCourse selectedValuationCurricularCourse = rootDomainObject.readValuationCurricularCourseByOID((Integer) dynaForm.get("valuationCurricularCourse"));

		if (selectedValuationCurricularCourse == null) {
			if (valuationCurricularCourseList != null && !valuationCurricularCourseList.isEmpty()) {
				return valuationCurricularCourseList.get(0);
			} else {
				return null;
			}
		} else {
			return selectedValuationCurricularCourse;
		}
	}

	private CurricularCourseValuationGroup getSelectedCurricularCourseValuationGroup(
			IUserView userView,
			DynaActionForm dynaForm,
			List<CurricularCourseValuationGroup> curricularCourseValuationGroupList) throws FenixFilterException, FenixServiceException {
		CurricularCourseValuationGroup selectedCurricularCourseValuationGroup = (CurricularCourseValuationGroup) rootDomainObject.readCourseValuationByOID((Integer) dynaForm.get("curricularCourseValuationGroup"));

		if (selectedCurricularCourseValuationGroup == null) {
			if (curricularCourseValuationGroupList != null && !curricularCourseValuationGroupList.isEmpty()) {
				return curricularCourseValuationGroupList.get(0);
			} else {
				return null;
			}
		} else {
			return selectedCurricularCourseValuationGroup;
		}
	}

	private ValuationTeacher getSelectedValuationTeacher(
			IUserView userView,
			DynaActionForm dynaForm,
			List<ValuationTeacher> valuationTeacherList) throws FenixFilterException, FenixServiceException {
		ValuationTeacher selectedValuationTeacher = rootDomainObject.readValuationTeacherByOID((Integer) dynaForm.get("valuationTeacher"));

		if (selectedValuationTeacher == null) {
			if (!valuationTeacherList.isEmpty()) {
				return valuationTeacherList.get(0);
			} else {
				return null;
			}
		} else {
			return selectedValuationTeacher;
		}
	}

	private ProfessorshipValuation getSelectedProfessorshipValuation(IUserView userView, DynaActionForm dynaForm) throws FenixFilterException, FenixServiceException {
		return rootDomainObject.readProfessorshipValuationByOID((Integer) dynaForm.get("professorshipValuation"));
	}

	private Map<String, Object> obtainProfessorshipParametersFromForm(DynaActionForm dynaForm) {
		Map<String, Object> courseValuationParameters = new HashMap<String, Object>();

		courseValuationParameters.put("theoreticalHoursManual", Double.parseDouble((String) dynaForm.get("theoreticalHoursManual")));
		courseValuationParameters.put("theoreticalHoursType", dynaForm.get("theoreticalHoursType"));
		courseValuationParameters.put("praticalHoursManual", Double.parseDouble((String) dynaForm.get("praticalHoursManual")));
		courseValuationParameters.put("praticalHoursType", dynaForm.get("praticalHoursType"));
		courseValuationParameters.put("theoPratHoursManual", Double.parseDouble((String) dynaForm.get("theoPratHoursManual")));
		courseValuationParameters.put("theoPratHoursType", dynaForm.get("theoPratHoursType"));
		courseValuationParameters.put("laboratorialHoursManual", Double.parseDouble((String) dynaForm.get("laboratorialHoursManual")));
		courseValuationParameters.put("laboratorialHoursType", dynaForm.get("laboratorialHoursType"));

		return courseValuationParameters;
	}

	public ActionForward prepareLinkForProfessorshipValuationByCourse(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {

		return prepareLinkForProfessorshipValuation(mapping, form, request, response, 
				VIEW_PROFESSORSHIP_VALUATION_BY_COURSES);
	}
	
	public ActionForward prepareLinkForProfessorshipValuationByTeacher(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {

		return prepareLinkForProfessorshipValuation(mapping, form, request, response, 
				VIEW_PROFESSORSHIP_VALUATION_BY_TEACHERS);
	}
	
	private ActionForward prepareLinkForProfessorshipValuation(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response,
			Integer viewType) throws FenixFilterException, FenixServiceException {

		DynaActionForm dynaForm = (DynaActionForm) form;
		getFromRequestAndSetOnFormTeacherServiceDistributionId(request, dynaForm);
		
		initializeVariables(dynaForm);
		
		Integer selectedCourseValuationId = new Integer(request.getParameter("courseValuation"));	
		CourseValuation courseValuation = rootDomainObject.readCourseValuationByOID(selectedCourseValuationId);

		if(courseValuation instanceof CurricularCourseValuation){
			dynaForm.set("valuationCurricularCourse", courseValuation.getIdInternal());
		} else if(courseValuation instanceof CurricularCourseValuationGroup){
			dynaForm.set("curricularCourseValuationGroup", courseValuation.getIdInternal());
			
		} 
		
		Integer selectedValuationTeacherId = new Integer(request.getParameter("valuationTeacher"));	
		ValuationCompetenceCourse valuationCompetenceCourse = courseValuation.getValuationCompetenceCourse();
		
		dynaForm.set("valuationCompetenceCourse", valuationCompetenceCourse.getIdInternal());
		dynaForm.set("valuationTeacher", selectedValuationTeacherId);
		dynaForm.set("executionPeriod", courseValuation.getExecutionPeriod().getIdInternal());
		dynaForm.set("viewType", viewType);
		
		if(viewType == VIEW_PROFESSORSHIP_VALUATION_BY_COURSES){
			dynaForm.set("distributionViewAnchor", selectedCourseValuationId);
		} else {
			dynaForm.set("distributionViewAnchor", selectedValuationTeacherId);
		}
		
		return loadProfessorshipValuations(mapping, form, request, response);
	}

	private ExecutionPeriod getSelectedExecutionPeriod(
			IUserView userView,
			DynaActionForm dynaForm,
			List<ExecutionPeriod> executionPeriodList) throws FenixServiceException, FenixFilterException {
		ExecutionPeriod selectedExecutionPeriod = rootDomainObject.readExecutionPeriodByOID((Integer) dynaForm.get("executionPeriod"));

		if (selectedExecutionPeriod == null) {
			if (executionPeriodList != null && executionPeriodList.size() > 0) {
				return executionPeriodList.get(0);
			} else {
				return null;
			}
		}

		return selectedExecutionPeriod;
	}	
}
