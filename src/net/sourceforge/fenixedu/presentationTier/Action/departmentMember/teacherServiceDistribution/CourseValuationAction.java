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
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.CourseValuation;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.CourseValuationType;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.CurricularCourseValuation;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.CurricularCourseValuationGroup;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationCompetenceCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationCurricularCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationGrouping;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationPhase;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationValueType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class CourseValuationAction extends FenixDispatchAction {

	public ActionForward prepareForCourseValuation(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {

		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;

		getFromRequestAndSetOnFormTeacherServiceDistributionId(request, dynaForm);
		TeacherServiceDistribution teacherServiceDistribution = getTeacherServiceDistribution(userView, dynaForm);

		initializeVariables(userView.getPerson(), dynaForm, teacherServiceDistribution.getCurrentValuationPhase());
		return loadCourseValuations(mapping, form, request, response);
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
		dynaForm.set("suppressRedundantHoursTypes", true);

		dynaForm.set("courseValuationType", CourseValuationType.NOT_DETERMINED.toString());

		return loadCourseValuations(mapping, form, request, response);
	}

	public ActionForward loadValuationCompetenceCourse(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;

		TeacherServiceDistribution teacherServiceDistribution = getTeacherServiceDistribution(userView, dynaForm);
		ValuationCompetenceCourse selectedValuationCompetenceCourse = getSelectedValuationCompetenceCourse(
				userView,
				dynaForm,
				null);
		ValuationPhase currentValuationPhase = teacherServiceDistribution.getCurrentValuationPhase();
		ExecutionPeriod selectedExecutionPeriod = getSelectedExecutionPeriod(userView, dynaForm, null);

		dynaForm.set("courseValuationType", selectedValuationCompetenceCourse.getCourseValuationType(
				currentValuationPhase,
				selectedExecutionPeriod).toString());
		dynaForm.set("valuationCurricularCourse", null);
		dynaForm.set("curricularCourseValuationGroup", null);
		dynaForm.set("suppressRedundantHoursTypes", true);

		return loadCourseValuations(mapping, form, request, response);
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
		dynaForm.set("suppressRedundantHoursTypes", true);

		dynaForm.set("courseValuationType", CourseValuationType.NOT_DETERMINED.toString());

		return loadCourseValuations(mapping, form, request, response);
	}

	public ActionForward setCourseValuationType(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;

		TeacherServiceDistribution teacherServiceDistribution = getTeacherServiceDistribution(userView, dynaForm);
		ValuationCompetenceCourse selectedValuationCompetenceCourse = getSelectedValuationCompetenceCourse(
				userView,
				dynaForm,
				null);
		CourseValuationType selectedCourseValuationType = getSelectedCourseValuationType(dynaForm);
		ValuationPhase currentValuationPhase = teacherServiceDistribution.getCurrentValuationPhase();
		ExecutionPeriod selectedExecutionPeriod = getSelectedExecutionPeriod(userView, dynaForm, null);

		Object[] parameters = new Object[] { selectedValuationCompetenceCourse.getIdInternal(),
				currentValuationPhase.getIdInternal(), selectedExecutionPeriod.getIdInternal(),
				selectedCourseValuationType.toString() };
		ServiceUtils.executeService(userView, "SetCourseValuationType", parameters);

		dynaForm.set("valuationCurricularCourse", null);
		dynaForm.set("curricularCourseValuationGroup", null);

		return loadCourseValuations(mapping, form, request, response);
	}

	public ActionForward setCourseValuation(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;

		TeacherServiceDistribution teacherServiceDistribution = getTeacherServiceDistribution(userView, dynaForm);
		CourseValuationType selectedCourseValuationType = getSelectedCourseValuationType(dynaForm);
		ValuationPhase currentValuationPhase = teacherServiceDistribution.getCurrentValuationPhase();
		ValuationCompetenceCourse selectedValuationCompetenceCourse = getSelectedValuationCompetenceCourse(
				userView,
				dynaForm,
				null);
		ExecutionPeriod selectedExecutionPeriod = getSelectedExecutionPeriod(userView, dynaForm, null);

		Map<String, Object> courseValuationParameters = obtainCourseParametersFromForm(
				dynaForm,
				selectedCourseValuationType);

		if (selectedCourseValuationType == CourseValuationType.COMPETENCE_COURSE_VALUATION) {
			setCompetenceCourseValuation(
					userView,
					currentValuationPhase,
					selectedValuationCompetenceCourse,
					selectedExecutionPeriod,
					courseValuationParameters);
		} else if (selectedCourseValuationType == CourseValuationType.CURRICULAR_COURSE_VALUATION) {
			setCurricularCourseValuation(
					userView,
					dynaForm,
					currentValuationPhase,
					selectedExecutionPeriod,
					courseValuationParameters);
		} else if (selectedCourseValuationType == CourseValuationType.CURRICULAR_COURSE_VALUATION_GROUP) {
			setCurricularCourseValuationGroup(userView, dynaForm, courseValuationParameters);
		}

		return loadCourseValuations(mapping, form, request, response);
	}

	private void setCurricularCourseValuationGroup(
			IUserView userView,
			DynaActionForm dynaForm,
			Map<String, Object> courseValuationParameters) throws FenixFilterException, FenixServiceException {
		CurricularCourseValuationGroup selectedCurricularCourseValuationGroup = getSelectedCurricularCourseValuationGroup(
				userView,
				dynaForm,
				null);
		Object[] parameters = new Object[] { selectedCurricularCourseValuationGroup.getIdInternal(),
				courseValuationParameters };
		ServiceUtils.executeService(userView, "SetCurricularCourseValuationGroup", parameters);
	}

	private void setCurricularCourseValuation(
			IUserView userView,
			DynaActionForm dynaForm,
			ValuationPhase currentValuationPhase,
			ExecutionPeriod selectedExecutionPeriod,
			Map<String, Object> courseValuationParameters) throws FenixFilterException, FenixServiceException {
		ValuationCurricularCourse selectedValuationCurricularCourse = getSelectedValuationCurricularCourse(
				userView,
				dynaForm,
				null);
		Object[] parameters = new Object[] { selectedValuationCurricularCourse.getIdInternal(),
				currentValuationPhase.getIdInternal(), selectedExecutionPeriod.getIdInternal(),
				courseValuationParameters };
		ServiceUtils.executeService(userView, "SetCurricularCourseValuation", parameters);
	}

	private void setCompetenceCourseValuation(
			IUserView userView,
			ValuationPhase currentValuationPhase,
			ValuationCompetenceCourse selectedValuationCompetenceCourse,
			ExecutionPeriod selectedExecutionPeriod,
			Map<String, Object> courseValuationParameters) throws FenixServiceException, FenixFilterException {
		Object[] parameters = new Object[] { selectedValuationCompetenceCourse.getIdInternal(),
				currentValuationPhase.getIdInternal(), selectedExecutionPeriod.getIdInternal(),
				courseValuationParameters };
		ServiceUtils.executeService(userView, "SetCompetenceCourseValuation", parameters);
	}

	public ActionForward prepareForCurricularCourseValuationGroupCreation(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;

		ValuationPhase currentValuationPhase = getTeacherServiceDistribution(userView, dynaForm).getCurrentValuationPhase();
		ValuationCompetenceCourse selectedValuationCompetenceCourse = getSelectedValuationCompetenceCourse(
				userView,
				dynaForm,
				null);
		ExecutionPeriod selectedExecutionPeriod = getSelectedExecutionPeriod(userView, dynaForm, null);

		Object[] parameters = new Object[] { selectedValuationCompetenceCourse.getIdInternal(),
				currentValuationPhase.getIdInternal(), selectedExecutionPeriod.getIdInternal(),
				getDummyCourseValuationParameters() };

		ServiceUtils.executeService(userView, "CreateCurricularCourseValuations", parameters);

		List<CurricularCourseValuation> availableCurricularCourseValuationToGroupList = selectedValuationCompetenceCourse.getCurricularCourseValuationsNotBelongingToValuationGroup(
				currentValuationPhase,
				selectedExecutionPeriod);

		request.setAttribute(
				"availableCurricularCourseValuationToGroupList",
				availableCurricularCourseValuationToGroupList);
		request.setAttribute("teacherServiceDistribution", getTeacherServiceDistribution(userView, dynaForm));

		return mapping.findForward("curricularCourseValuationGroupCreationForm");
	}

	public ActionForward createCurricularCourseValuationGroup(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;

		Integer[] curricularCourseValuationToGroupArray = obtainAssociatedCurricularCourseValuationsFromDynaActionForm(dynaForm);


		if (curricularCourseValuationToGroupArray.length > 0) {
			TeacherServiceDistribution teacherServiceDistribution = getTeacherServiceDistribution(userView, dynaForm);

			Object[] parameters = new Object[] { curricularCourseValuationToGroupArray,
					getDummyCourseValuationParameters() };

			CurricularCourseValuationGroup curricularCourseValuationGroup = (CurricularCourseValuationGroup) ServiceUtils.executeService(
					userView,
					"CreateCurricularCourseValuationGroup",
					parameters);

			dynaForm.set("curricularCourseValuationGroup", curricularCourseValuationGroup.getIdInternal());
		}

		return loadCourseValuations(mapping, form, request, response);
	}

	private Integer[] obtainAssociatedCurricularCourseValuationsFromDynaActionForm(DynaActionForm dynaForm) {
		String[] curricularCourseValuationToGroupArrayString = (String[]) dynaForm.get("curricularCourseValuationArray"); 
		Integer[] curricularCourseValuationToGroupArray = new Integer[curricularCourseValuationToGroupArrayString.length];
		
		for(int i = 0; i < curricularCourseValuationToGroupArrayString.length; i++) {
			curricularCourseValuationToGroupArray[i] = Integer.parseInt(curricularCourseValuationToGroupArrayString[i]);
		}
		return curricularCourseValuationToGroupArray;
	}
	
	public ActionForward deleteCurricularCourseValuationGroup(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;

		CurricularCourseValuationGroup selectedCurricularCourseValuationGroup = getSelectedCurricularCourseValuationGroup(
				userView,
				dynaForm,
				null);

		Object[] parameters = new Object[] { selectedCurricularCourseValuationGroup.getIdInternal() };

		ServiceUtils.executeService(userView, "DeleteCurricularCourseValuationGroup", parameters);

		dynaForm.set("courseValuationType", CourseValuationType.NOT_DETERMINED.toString());
		dynaForm.set("curricularCourseValuationGroup", null);

		return loadCourseValuations(mapping, form, request, response);
	}

	public ActionForward removeCurricularCourseValuationFromValuationGroup(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;

		CurricularCourseValuationGroup selectedCurricularCourseValuationGroup = getSelectedCurricularCourseValuationGroup(
				userView,
				dynaForm,
				null);
		CurricularCourseValuation selectedCurricularCourseValuation = getSelectedCurricularCourseValuation(
				userView,
				dynaForm);

		Object[] parameters = new Object[] { selectedCurricularCourseValuationGroup.getIdInternal(),
				selectedCurricularCourseValuation.getIdInternal() };

		ServiceUtils.executeService(userView, "RemoveCurricularCourseValuationFromValuationGroup", parameters);

		return loadCourseValuations(mapping, form, request, response);
	}

	public ActionForward addCurricularCourseValuationFromValuationGroup(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;

		CurricularCourseValuationGroup selectedCurricularCourseValuationGroup = getSelectedCurricularCourseValuationGroup(
				userView,
				dynaForm,
				null);
		CurricularCourseValuation selectedCurricularCourseValuation = getSelectedCurricularCourseValuation(
				userView,
				dynaForm);

		Object[] parameters = new Object[] { selectedCurricularCourseValuationGroup.getIdInternal(),
				selectedCurricularCourseValuation.getIdInternal() };

		ServiceUtils.executeService(userView, "AddCurricularCourseValuationFromValuationGroup", parameters);

		return loadCourseValuations(mapping, form, request, response);
	}

	public ActionForward loadCourseValuations(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {

		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;

		ValuationGrouping selectedValuationGrouping = getSelectedValuationGrouping(userView, dynaForm);
		ValuationPhase currentValuationPhase = getTeacherServiceDistribution(userView, dynaForm).getCurrentValuationPhase();
		List<ExecutionPeriod> executionPeriodList = getOrderedExecutionPeriods(userView, dynaForm);
		ExecutionPeriod selectedExecutionPeriod = getSelectedExecutionPeriod(userView, dynaForm, executionPeriodList);

		List<ValuationCompetenceCourse> activeValuationCompetenceCourseList = getActiveValuationCompetenceCourseList(
				selectedValuationGrouping,
				selectedExecutionPeriod);

		CourseValuationType selectedCourseValuationType = getSelectedCourseValuationType(dynaForm);

		if (!activeValuationCompetenceCourseList.isEmpty()) {
			Collections.sort(activeValuationCompetenceCourseList, new BeanComparator("name"));
			ValuationCompetenceCourse selectedValuationCompetenceCourse = getSelectedValuationCompetenceCourse(
					userView,
					dynaForm,
					activeValuationCompetenceCourseList);

			if (selectedCourseValuationType == CourseValuationType.NOT_DETERMINED) {
				selectedCourseValuationType = selectedValuationCompetenceCourse.getCourseValuationType(
						currentValuationPhase,
						selectedExecutionPeriod);
			}

			List<ValuationCurricularCourse> valuationCurricularCourseList = null;
			ValuationCurricularCourse selectedValuationCurricularCourse = null;
			List<CurricularCourseValuationGroup> curricularCourseValuationGroupList = null;
			CurricularCourseValuationGroup selectedCurricularCourseValuationGroup = null;
			if (selectedCourseValuationType == CourseValuationType.COMPETENCE_COURSE_VALUATION) {
				if (selectedValuationCompetenceCourse.getCompetenceCourseValuationByValuationPhaseAndExecutionPeriod(
						currentValuationPhase,
						selectedExecutionPeriod) == null) {
					setDefaultCompetenceCourseValuation(
							userView,
							currentValuationPhase,
							selectedExecutionPeriod,
							selectedValuationCompetenceCourse);
				}
			} else if (selectedCourseValuationType == CourseValuationType.CURRICULAR_COURSE_VALUATION) {
				valuationCurricularCourseList = selectedValuationCompetenceCourse.getValuationCurricularCourses(selectedExecutionPeriod);
				selectedValuationCurricularCourse = getSelectedValuationCurricularCourse(
						userView,
						dynaForm,
						valuationCurricularCourseList);

				if (selectedValuationCurricularCourse.getCurricularCourseValuationByValuationPhaseAndExecutionPeriod(
						currentValuationPhase,
						selectedExecutionPeriod) == null) {
					setDefaultCurricularCourseValuation(
							userView,
							currentValuationPhase,
							selectedExecutionPeriod,
							selectedValuationCurricularCourse);
				}

			} else if (selectedCourseValuationType == CourseValuationType.CURRICULAR_COURSE_VALUATION_GROUP) {
				Object[] parameters = new Object[] { selectedValuationCompetenceCourse.getIdInternal(),
						currentValuationPhase.getIdInternal(), selectedExecutionPeriod.getIdInternal(),
						getDummyCourseValuationParameters() };

				ServiceUtils.executeService(userView, "CreateCurricularCourseValuations", parameters);
				
				curricularCourseValuationGroupList = selectedValuationCompetenceCourse.getCurricularCourseValuationGroupsByValuationPhaseAndExecutionPeriod(
						currentValuationPhase,
						selectedExecutionPeriod);
				selectedCurricularCourseValuationGroup = getSelectedCurricularCourseValuationGroup(
						userView,
						dynaForm,
						curricularCourseValuationGroupList);
			}

			fillFormAndRequest(
					request,
					dynaForm,
					currentValuationPhase,
					selectedValuationGrouping,
					selectedCourseValuationType,
					activeValuationCompetenceCourseList,
					selectedValuationCompetenceCourse,
					valuationCurricularCourseList,
					selectedValuationCurricularCourse,
					curricularCourseValuationGroupList,
					selectedCurricularCourseValuationGroup,
					selectedExecutionPeriod);
		} else {
			request.setAttribute("notAvailableValuationCompetenceCourses", true);
		}
		
		List<ValuationGroupingDTOEntry> valuationGroupingOptionEntryList = ValuationGroupingDTOEntry.getValuationGroupingOptionEntriesForPerson(currentValuationPhase, userView.getPerson(), false, true);

		
		request.setAttribute("teacherServiceDistribution", getTeacherServiceDistribution(userView, dynaForm));
		request.setAttribute("valuationGroupingOptionEntryList", valuationGroupingOptionEntryList );
		request.setAttribute("executionPeriodList", executionPeriodList);
		return mapping.findForward("courseValuationForm");
	}

	private void setDefaultCurricularCourseValuation(
			IUserView userView,
			ValuationPhase currentValuationPhase,
			ExecutionPeriod selectedExecutionPeriod,
			ValuationCurricularCourse selectedValuationCurricularCourse) throws FenixServiceException, FenixFilterException {
		Object[] parameters = new Object[] { selectedValuationCurricularCourse.getIdInternal(),
				currentValuationPhase.getIdInternal(), selectedExecutionPeriod.getIdInternal(),
				getDummyCourseValuationParameters() };

		ServiceUtils.executeService(userView, "SetCurricularCourseValuation", parameters);
	}

	private void setDefaultCompetenceCourseValuation(
			IUserView userView,
			ValuationPhase currentValuationPhase,
			ExecutionPeriod selectedExecutionPeriod,
			ValuationCompetenceCourse selectedValuationCompetenceCourse) throws FenixServiceException, FenixFilterException {
		Object[] parameters = new Object[] { selectedValuationCompetenceCourse.getIdInternal(),
				currentValuationPhase.getIdInternal(), selectedExecutionPeriod.getIdInternal(),
				getDummyCourseValuationParameters() };
		ServiceUtils.executeService(userView, "SetCompetenceCourseValuation", parameters);
	}

	private List<ExecutionPeriod> getOrderedExecutionPeriods(IUserView userView, DynaActionForm dynaForm) throws FenixServiceException, FenixFilterException {
		List<ExecutionPeriod> executionPeriodList = new ArrayList<ExecutionPeriod>(getTeacherServiceDistribution(
				userView,
				dynaForm).getExecutionPeriods());
		Collections.sort(executionPeriodList, new BeanComparator("semester"));
		return executionPeriodList;
	}

	private void fillFormAndRequest(
			HttpServletRequest request,
			DynaActionForm dynaForm,
			ValuationPhase valuationPhase,
			ValuationGrouping selectedValuationGrouping,
			CourseValuationType selectedCourseValuationType,
			List<ValuationCompetenceCourse> valuationCompetenceCourseList,
			ValuationCompetenceCourse selectedValuationCompetenceCourse,
			List<ValuationCurricularCourse> valuationCurricularCourseList,
			ValuationCurricularCourse selectedValuationCurricularCourse,
			List<CurricularCourseValuationGroup> curricularCourseValuationGroupList,
			CurricularCourseValuationGroup selectedCurricularCourseValuationGroup,
			ExecutionPeriod selectedExecutionPeriod) {

		request.setAttribute("valuationCompetenceCourseList", valuationCompetenceCourseList);
		request.setAttribute("selectedValuationCompetenceCourse", selectedValuationCompetenceCourse);
		request.setAttribute("valuationCurricularCourseList", valuationCurricularCourseList);
		request.setAttribute("selectedValuationCurricularCourse", selectedValuationCurricularCourse);
		request.setAttribute("curricularCourseValuationGroupList", curricularCourseValuationGroupList);
		request.setAttribute("selectedCurricularCourseValuationGroup", selectedCurricularCourseValuationGroup);
		request.setAttribute("selectedCourseValuationType", selectedCourseValuationType.toString());

		dynaForm.set("courseValuationType", selectedCourseValuationType.toString());
		dynaForm.set("valuationCompetenceCourse", selectedValuationCompetenceCourse.getIdInternal());

		if (selectedValuationCurricularCourse != null)
			dynaForm.set("valuationCurricularCourse", selectedValuationCurricularCourse.getIdInternal());
		if (selectedCurricularCourseValuationGroup != null)
			dynaForm.set("curricularCourseValuationGroup", selectedCurricularCourseValuationGroup.getIdInternal());

		CourseValuation selectedCourseValuation = null;
		if (selectedCourseValuationType == CourseValuationType.COMPETENCE_COURSE_VALUATION) {
			selectedCourseValuation = selectedValuationCompetenceCourse.getCompetenceCourseValuationByValuationPhaseAndExecutionPeriod(
					valuationPhase,
					selectedExecutionPeriod);
		} else if (selectedCourseValuationType == CourseValuationType.CURRICULAR_COURSE_VALUATION) {
			selectedCourseValuation = selectedValuationCurricularCourse.getCurricularCourseValuationByValuationPhaseAndExecutionPeriod(
					valuationPhase,
					selectedExecutionPeriod);
		} else if (selectedCourseValuationType == CourseValuationType.CURRICULAR_COURSE_VALUATION_GROUP) {
			selectedCourseValuation = selectedCurricularCourseValuationGroup;
			request.setAttribute(
					"availableCurricularCourseValuationsToGroup",
					selectedValuationCompetenceCourse.getCurricularCourseValuationsNotBelongingToValuationGroup(
							valuationPhase,
							selectedExecutionPeriod));
		}
		if (selectedCourseValuation == null) {
			request.setAttribute("courseValuationNotSelected", true);
		} else {
			request.setAttribute("selectedCourseValuation", selectedCourseValuation);
		}

		if(request.getParameter("fromValidation") == null) {
			fillFormWithCourseValuationParameters(dynaForm, selectedCourseValuation);
		}
	}

	private void fillFormWithCourseValuationParameters(DynaActionForm dynaForm, CourseValuation selectedCourseValuation) {
		if (selectedCourseValuation != null) {
			dynaForm.set(
					"firstTimeEnrolledStudentsManual",
					selectedCourseValuation.getFirstTimeEnrolledStudentsManual().toString());
			dynaForm.set(
					"firstTimeEnrolledStudentsType",
					selectedCourseValuation.getFirstTimeEnrolledStudentsType().toString());
			dynaForm.set(
					"secondTimeEnrolledStudentsManual",
					selectedCourseValuation.getSecondTimeEnrolledStudentsManual().toString());
			dynaForm.set(
					"secondTimeEnrolledStudentsType",
					selectedCourseValuation.getSecondTimeEnrolledStudentsType().toString());
			dynaForm.set(
					"studentsPerTheoreticalShiftManual",
					selectedCourseValuation.getStudentsPerTheoreticalShiftManual().toString());
			dynaForm.set(
					"studentsPerTheoreticalShiftType",
					selectedCourseValuation.getStudentsPerTheoreticalShiftType().toString());
			dynaForm.set("studentsPerPraticalShiftManual", selectedCourseValuation.getStudentsPerPraticalShiftManual().toString());
			dynaForm.set(
					"studentsPerPraticalShiftType",
					selectedCourseValuation.getStudentsPerPraticalShiftType().toString());
			dynaForm.set("studentsPerTheoPratShiftManual", selectedCourseValuation.getStudentsPerTheoPratShiftManual().toString());
			dynaForm.set(
					"studentsPerTheoPratShiftType",
					selectedCourseValuation.getStudentsPerTheoPratShiftType().toString());
			dynaForm.set(
					"studentsPerLaboratorialShiftManual",
					selectedCourseValuation.getStudentsPerLaboratorialShiftManual().toString());
			dynaForm.set(
					"studentsPerLaboratorialShiftType",
					selectedCourseValuation.getStudentsPerLaboratorialShiftType().toString());
			dynaForm.set(
					"weightFirstTimeEnrolledStudentsPerTheoShiftManual",
					selectedCourseValuation.getWeightFirstTimeEnrolledStudentsPerTheoShiftManual().toString().toString());
			dynaForm.set(
					"weightFirstTimeEnrolledStudentsPerTheoShiftType",
					selectedCourseValuation.getWeightFirstTimeEnrolledStudentsPerTheoShiftType().toString());
			dynaForm.set(
					"weightFirstTimeEnrolledStudentsPerPratShiftManual",
					selectedCourseValuation.getWeightFirstTimeEnrolledStudentsPerPratShiftManual().toString());
			dynaForm.set(
					"weightFirstTimeEnrolledStudentsPerPratShiftType",
					selectedCourseValuation.getWeightFirstTimeEnrolledStudentsPerPratShiftType().toString());
			dynaForm.set(
					"weightFirstTimeEnrolledStudentsPerTheoPratShiftManual",
					selectedCourseValuation.getWeightFirstTimeEnrolledStudentsPerTheoPratShiftManual().toString());
			dynaForm.set(
					"weightFirstTimeEnrolledStudentsPerTheoPratShiftType",
					selectedCourseValuation.getWeightFirstTimeEnrolledStudentsPerTheoPratShiftType().toString());
			dynaForm.set(
					"weightFirstTimeEnrolledStudentsPerLabShiftManual",
					selectedCourseValuation.getWeightFirstTimeEnrolledStudentsPerLabShiftManual().toString());
			dynaForm.set(
					"weightFirstTimeEnrolledStudentsPerLabShiftType",
					selectedCourseValuation.getWeightFirstTimeEnrolledStudentsPerLabShiftType().toString());
			dynaForm.set(
					"weightSecondTimeEnrolledStudentsPerTheoShiftManual",
					selectedCourseValuation.getWeightSecondTimeEnrolledStudentsPerTheoShiftManual().toString());
			dynaForm.set(
					"weightSecondTimeEnrolledStudentsPerTheoShiftType",
					selectedCourseValuation.getWeightSecondTimeEnrolledStudentsPerTheoShiftType().toString());
			dynaForm.set(
					"weightSecondTimeEnrolledStudentsPerPratShiftManual",
					selectedCourseValuation.getWeightSecondTimeEnrolledStudentsPerPratShiftManual().toString());
			dynaForm.set(
					"weightSecondTimeEnrolledStudentsPerPratShiftType",
					selectedCourseValuation.getWeightSecondTimeEnrolledStudentsPerPratShiftType().toString());
			dynaForm.set(
					"weightSecondTimeEnrolledStudentsPerTheoPratShiftManual",
					selectedCourseValuation.getWeightSecondTimeEnrolledStudentsPerTheoPratShiftManual().toString());
			dynaForm.set(
					"weightSecondTimeEnrolledStudentsPerTheoPratShiftType",
					selectedCourseValuation.getWeightSecondTimeEnrolledStudentsPerTheoPratShiftType().toString());
			dynaForm.set(
					"weightSecondTimeEnrolledStudentsPerLabShiftManual",
					selectedCourseValuation.getWeightSecondTimeEnrolledStudentsPerLabShiftManual().toString());
			dynaForm.set(
					"weightSecondTimeEnrolledStudentsPerLabShiftType",
					selectedCourseValuation.getWeightSecondTimeEnrolledStudentsPerLabShiftType().toString());
			dynaForm.set("isActive", selectedCourseValuation.getIsActive());
			dynaForm.set("theoreticalHoursManual", selectedCourseValuation.getTheoreticalHoursManual().toString());
			dynaForm.set("theoreticalHoursType", selectedCourseValuation.getTheoreticalHoursType().toString());
			dynaForm.set("praticalHoursManual", selectedCourseValuation.getPraticalHoursManual().toString());
			dynaForm.set("praticalHoursType", selectedCourseValuation.getPraticalHoursType().toString());
			dynaForm.set("theoPratHoursManual", selectedCourseValuation.getTheoPratHoursManual().toString());
			dynaForm.set("theoPratHoursType", selectedCourseValuation.getTheoPratHoursType().toString());
			dynaForm.set("laboratorialHoursManual", selectedCourseValuation.getLaboratorialHoursManual().toString());
			dynaForm.set("laboratorialHoursType", selectedCourseValuation.getLaboratorialHoursType().toString());

			if (selectedCourseValuation instanceof CurricularCourseValuationGroup) {
				dynaForm.set(
						"usingCurricularCourseValuations",
						((CurricularCourseValuationGroup) selectedCourseValuation).getUsingCurricularCourseValuations());
				
				String[] curricularCourseValuationArray = new String[((CurricularCourseValuationGroup) selectedCourseValuation).getCurricularCourseValuationsCount()];
				for(int i = 0; i < ((CurricularCourseValuationGroup) selectedCourseValuation).getCurricularCourseValuationsCount(); i++) {
					curricularCourseValuationArray[i] = ((CurricularCourseValuationGroup) selectedCourseValuation).getCurricularCourseValuations().get(i).getIdInternal().toString();
				}
				
				dynaForm.set("curricularCourseValuationArray", curricularCourseValuationArray);

			}
		} else {
			String zeroString = new String("0");
			String oneString = new String("1");
			
			dynaForm.set("firstTimeEnrolledStudentsManual", zeroString);
			dynaForm.set("firstTimeEnrolledStudentsType", ValuationValueType.MANUAL_VALUE.toString());
			dynaForm.set("secondTimeEnrolledStudentsManual", zeroString);
			dynaForm.set("secondTimeEnrolledStudentsType", ValuationValueType.MANUAL_VALUE.toString());
			dynaForm.set("studentsPerTheoreticalShiftManual", zeroString);
			dynaForm.set("studentsPerTheoreticalShiftType", ValuationValueType.MANUAL_VALUE.toString());
			dynaForm.set("studentsPerPraticalShiftManual", zeroString);
			dynaForm.set("studentsPerPraticalShiftType", ValuationValueType.MANUAL_VALUE.toString());
			dynaForm.set("studentsPerTheoPratShiftManual", zeroString);
			dynaForm.set("studentsPerTheoPratShiftType", ValuationValueType.MANUAL_VALUE.toString());
			dynaForm.set("studentsPerLaboratorialShiftManual", zeroString);
			dynaForm.set("studentsPerLaboratorialShiftType", ValuationValueType.MANUAL_VALUE.toString());
			dynaForm.set("weightFirstTimeEnrolledStudentsPerTheoShiftManual", oneString);
			dynaForm.set("weightFirstTimeEnrolledStudentsPerTheoShiftType", ValuationValueType.MANUAL_VALUE.toString());
			dynaForm.set("weightFirstTimeEnrolledStudentsPerPratShiftManual", oneString);
			dynaForm.set("weightFirstTimeEnrolledStudentsPerPratShiftType", ValuationValueType.MANUAL_VALUE.toString());
			dynaForm.set("weightFirstTimeEnrolledStudentsPerTheoPratShiftManual", oneString);
			dynaForm.set(
					"weightFirstTimeEnrolledStudentsPerTheoPratShiftType",
					ValuationValueType.MANUAL_VALUE.toString());
			dynaForm.set("weightFirstTimeEnrolledStudentsPerLabShiftManual", oneString);
			dynaForm.set("weightFirstTimeEnrolledStudentsPerLabShiftType", ValuationValueType.MANUAL_VALUE.toString());
			dynaForm.set("weightSecondTimeEnrolledStudentsPerTheoShiftManual", oneString);
			dynaForm.set("weightSecondTimeEnrolledStudentsPerTheoShiftType", ValuationValueType.MANUAL_VALUE.toString());
			dynaForm.set("weightSecondTimeEnrolledStudentsPerPratShiftManual", oneString);
			dynaForm.set("weightSecondTimeEnrolledStudentsPerPratShiftType", ValuationValueType.MANUAL_VALUE.toString());
			dynaForm.set("weightSecondTimeEnrolledStudentsPerTheoPratShiftManual", oneString);
			dynaForm.set(
					"weightSecondTimeEnrolledStudentsPerTheoPratShiftType",
					ValuationValueType.MANUAL_VALUE.toString());
			dynaForm.set("weightSecondTimeEnrolledStudentsPerLabShiftManual", oneString);
			dynaForm.set("weightSecondTimeEnrolledStudentsPerLabShiftType", ValuationValueType.MANUAL_VALUE.toString());
			dynaForm.set("isActive", true);
			dynaForm.set("theoreticalHoursManual", zeroString);
			dynaForm.set("theoreticalHoursType", ValuationValueType.MANUAL_VALUE.toString());
			dynaForm.set("praticalHoursManual", zeroString);
			dynaForm.set("praticalHoursType", ValuationValueType.MANUAL_VALUE.toString());
			dynaForm.set("theoPratHoursManual", zeroString);
			dynaForm.set("theoPratHoursType", ValuationValueType.MANUAL_VALUE.toString());
			dynaForm.set("laboratorialHoursManual", zeroString);
			dynaForm.set("laboratorialHoursType", ValuationValueType.MANUAL_VALUE.toString());
			dynaForm.set("usingCurricularCourseValuations", false);
		}
	}

	private Map<String, Object> getDummyCourseValuationParameters() {
		Map<String, Object> courseValuationParameters = new HashMap<String, Object>();

		courseValuationParameters.put("firstTimeEnrolledStudentsManual", 0);
		courseValuationParameters.put("firstTimeEnrolledStudentsType", ValuationValueType.MANUAL_VALUE.toString());
		courseValuationParameters.put("secondTimeEnrolledStudentsManual", 0);
		courseValuationParameters.put("secondTimeEnrolledStudentsType", ValuationValueType.MANUAL_VALUE.toString());
		courseValuationParameters.put("studentsPerTheoreticalShiftManual", 0);
		courseValuationParameters.put("studentsPerTheoreticalShiftType", ValuationValueType.MANUAL_VALUE.toString());
		courseValuationParameters.put("studentsPerPraticalShiftManual", 0);
		courseValuationParameters.put("studentsPerPraticalShiftType", ValuationValueType.MANUAL_VALUE.toString());
		courseValuationParameters.put("studentsPerTheoPratShiftManual", 0);
		courseValuationParameters.put("studentsPerTheoPratShiftType", ValuationValueType.MANUAL_VALUE.toString());
		courseValuationParameters.put("studentsPerLaboratorialShiftManual", 0);
		courseValuationParameters.put("studentsPerLaboratorialShiftType", ValuationValueType.MANUAL_VALUE.toString());
		courseValuationParameters.put("weightFirstTimeEnrolledStudentsPerTheoShiftManual", 1.0);
		courseValuationParameters.put(
				"weightFirstTimeEnrolledStudentsPerTheoShiftType",
				ValuationValueType.MANUAL_VALUE.toString());
		courseValuationParameters.put("weightFirstTimeEnrolledStudentsPerPratShiftManual", 1.0);
		courseValuationParameters.put(
				"weightFirstTimeEnrolledStudentsPerPratShiftType",
				ValuationValueType.MANUAL_VALUE.toString());
		courseValuationParameters.put("weightFirstTimeEnrolledStudentsPerTheoPratShiftManual", 1.0);
		courseValuationParameters.put(
				"weightFirstTimeEnrolledStudentsPerTheoPratShiftType",
				ValuationValueType.MANUAL_VALUE.toString());
		courseValuationParameters.put("weightFirstTimeEnrolledStudentsPerLabShiftManual", 1.0);
		courseValuationParameters.put(
				"weightFirstTimeEnrolledStudentsPerLabShiftType",
				ValuationValueType.MANUAL_VALUE.toString());
		courseValuationParameters.put("weightSecondTimeEnrolledStudentsPerTheoShiftManual", 1.0);
		courseValuationParameters.put(
				"weightSecondTimeEnrolledStudentsPerTheoShiftType",
				ValuationValueType.MANUAL_VALUE.toString());
		courseValuationParameters.put("weightSecondTimeEnrolledStudentsPerPratShiftManual", 1.0);
		courseValuationParameters.put(
				"weightSecondTimeEnrolledStudentsPerPratShiftType",
				ValuationValueType.MANUAL_VALUE.toString());
		courseValuationParameters.put("weightSecondTimeEnrolledStudentsPerTheoPratShiftManual", 1.0);
		courseValuationParameters.put(
				"weightSecondTimeEnrolledStudentsPerTheoPratShiftType",
				ValuationValueType.MANUAL_VALUE.toString());
		courseValuationParameters.put("weightSecondTimeEnrolledStudentsPerLabShiftManual", 1.0);
		courseValuationParameters.put(
				"weightSecondTimeEnrolledStudentsPerLabShiftType",
				ValuationValueType.MANUAL_VALUE.toString());
		courseValuationParameters.put("isActive", true);
		courseValuationParameters.put("theoreticalHoursManual", 0d);
		courseValuationParameters.put("theoreticalHoursType", ValuationValueType.MANUAL_VALUE.toString());
		courseValuationParameters.put("praticalHoursManual", 0d);
		courseValuationParameters.put("praticalHoursType", ValuationValueType.MANUAL_VALUE.toString());
		courseValuationParameters.put("theoPratHoursManual", 0d);
		courseValuationParameters.put("theoPratHoursType", ValuationValueType.MANUAL_VALUE.toString());
		courseValuationParameters.put("laboratorialHoursManual", 0d);
		courseValuationParameters.put("laboratorialHoursType", ValuationValueType.MANUAL_VALUE.toString());
		courseValuationParameters.put("usingCurricularCourseValuations", false);

		return courseValuationParameters;
	}

	private Map<String, Object> obtainCourseParametersFromForm(
			DynaActionForm dynaForm,
			CourseValuationType selectedCourseValuationType) {
		Map<String, Object> courseValuationParameters = new HashMap<String, Object>();

		courseValuationParameters.put(
				"firstTimeEnrolledStudentsManual",
				Integer.parseInt((String) dynaForm.get("firstTimeEnrolledStudentsManual")));
		courseValuationParameters.put("firstTimeEnrolledStudentsType", dynaForm.get("firstTimeEnrolledStudentsType"));
		courseValuationParameters.put(
				"secondTimeEnrolledStudentsManual",
				Integer.parseInt((String) dynaForm.get("secondTimeEnrolledStudentsManual")));
		courseValuationParameters.put("secondTimeEnrolledStudentsType", dynaForm.get("secondTimeEnrolledStudentsType"));
		courseValuationParameters.put(
				"studentsPerTheoreticalShiftManual",
				Integer.parseInt((String) dynaForm.get("studentsPerTheoreticalShiftManual")));
		courseValuationParameters.put(
				"studentsPerTheoreticalShiftType",
				dynaForm.get("studentsPerTheoreticalShiftType"));
		courseValuationParameters.put("studentsPerPraticalShiftManual", Integer.parseInt((String) dynaForm.get("studentsPerPraticalShiftManual")));
		courseValuationParameters.put("studentsPerPraticalShiftType", dynaForm.get("studentsPerPraticalShiftType"));
		courseValuationParameters.put("studentsPerTheoPratShiftManual", Integer.parseInt((String) dynaForm.get("studentsPerTheoPratShiftManual")));
		courseValuationParameters.put("studentsPerTheoPratShiftType", dynaForm.get("studentsPerTheoPratShiftType"));
		courseValuationParameters.put(
				"studentsPerLaboratorialShiftManual",
				Integer.parseInt((String) dynaForm.get("studentsPerLaboratorialShiftManual")));
		courseValuationParameters.put(
				"studentsPerLaboratorialShiftType",
				dynaForm.get("studentsPerLaboratorialShiftType"));
		courseValuationParameters.put(
				"weightFirstTimeEnrolledStudentsPerTheoShiftManual",
				Double.parseDouble((String) dynaForm.get("weightFirstTimeEnrolledStudentsPerTheoShiftManual")));
		courseValuationParameters.put(
				"weightFirstTimeEnrolledStudentsPerTheoShiftType",
				dynaForm.get("weightFirstTimeEnrolledStudentsPerTheoShiftType"));
		courseValuationParameters.put(
				"weightFirstTimeEnrolledStudentsPerPratShiftManual",
				Double.parseDouble((String) dynaForm.get("weightFirstTimeEnrolledStudentsPerPratShiftManual")));
		courseValuationParameters.put(
				"weightFirstTimeEnrolledStudentsPerPratShiftType",
				dynaForm.get("weightFirstTimeEnrolledStudentsPerPratShiftType"));
		courseValuationParameters.put(
				"weightFirstTimeEnrolledStudentsPerTheoPratShiftManual",
				Double.parseDouble((String) dynaForm.get("weightFirstTimeEnrolledStudentsPerTheoPratShiftManual")));
		courseValuationParameters.put(
				"weightFirstTimeEnrolledStudentsPerTheoPratShiftType",
				dynaForm.get("weightFirstTimeEnrolledStudentsPerTheoPratShiftType"));
		courseValuationParameters.put(
				"weightFirstTimeEnrolledStudentsPerLabShiftManual",
				Double.parseDouble((String) dynaForm.get("weightFirstTimeEnrolledStudentsPerLabShiftManual")));
		courseValuationParameters.put(
				"weightFirstTimeEnrolledStudentsPerLabShiftType",
				dynaForm.get("weightFirstTimeEnrolledStudentsPerLabShiftType"));
		courseValuationParameters.put(
				"weightSecondTimeEnrolledStudentsPerTheoShiftManual",
				Double.parseDouble((String) dynaForm.get("weightSecondTimeEnrolledStudentsPerTheoShiftManual")));
		courseValuationParameters.put(
				"weightSecondTimeEnrolledStudentsPerTheoShiftType",
				dynaForm.get("weightSecondTimeEnrolledStudentsPerTheoShiftType"));
		courseValuationParameters.put(
				"weightSecondTimeEnrolledStudentsPerPratShiftManual",
				Double.parseDouble((String) dynaForm.get("weightSecondTimeEnrolledStudentsPerPratShiftManual")));
		courseValuationParameters.put(
				"weightSecondTimeEnrolledStudentsPerPratShiftType",
				dynaForm.get("weightSecondTimeEnrolledStudentsPerPratShiftType"));
		courseValuationParameters.put(
				"weightSecondTimeEnrolledStudentsPerTheoPratShiftManual",
				Double.parseDouble((String) dynaForm.get("weightSecondTimeEnrolledStudentsPerTheoPratShiftManual")));
		courseValuationParameters.put(
				"weightSecondTimeEnrolledStudentsPerTheoPratShiftType",
				dynaForm.get("weightSecondTimeEnrolledStudentsPerTheoPratShiftType"));
		courseValuationParameters.put(
				"weightSecondTimeEnrolledStudentsPerLabShiftManual",
				Double.parseDouble((String) dynaForm.get("weightSecondTimeEnrolledStudentsPerLabShiftManual")));
		courseValuationParameters.put(
				"weightSecondTimeEnrolledStudentsPerLabShiftType",
				dynaForm.get("weightSecondTimeEnrolledStudentsPerLabShiftType"));
		courseValuationParameters.put("isActive", true);
		courseValuationParameters.put("theoreticalHoursManual", Double.parseDouble((String) dynaForm.get("theoreticalHoursManual")));
		courseValuationParameters.put("theoreticalHoursType", dynaForm.get("theoreticalHoursType"));
		courseValuationParameters.put("praticalHoursManual", Double.parseDouble((String) dynaForm.get("praticalHoursManual")));
		courseValuationParameters.put("praticalHoursType", dynaForm.get("praticalHoursType"));
		courseValuationParameters.put("theoPratHoursManual", Double.parseDouble((String) dynaForm.get("theoPratHoursManual")));
		courseValuationParameters.put("theoPratHoursType", dynaForm.get("theoPratHoursType"));
		courseValuationParameters.put("laboratorialHoursManual", Double.parseDouble((String) dynaForm.get("laboratorialHoursManual")));
		courseValuationParameters.put("laboratorialHoursType", dynaForm.get("laboratorialHoursType"));

		if (selectedCourseValuationType == CourseValuationType.CURRICULAR_COURSE_VALUATION_GROUP) {
			if (dynaForm.get("usingCurricularCourseValuations") == null) {
				dynaForm.set("usingCurricularCourseValuations", false);
			}

			courseValuationParameters.put(
					"usingCurricularCourseValuations",
					dynaForm.get("usingCurricularCourseValuations"));
		}

		return courseValuationParameters;
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

	private CurricularCourseValuation getSelectedCurricularCourseValuation(IUserView userView, DynaActionForm dynaForm) throws FenixFilterException, FenixServiceException {
		return (CurricularCourseValuation) rootDomainObject.readCourseValuationByOID((Integer) dynaForm.get("curricularCourseValuation"));
	}

	private ValuationCompetenceCourse getSelectedValuationCompetenceCourse(
			IUserView userView,
			DynaActionForm dynaForm,
			List<ValuationCompetenceCourse> valuationCompetenceCourseList) throws FenixFilterException, FenixServiceException {
		ValuationCompetenceCourse selectedValuationCompetenceCourse = rootDomainObject.readValuationCompetenceCourseByOID((Integer) dynaForm.get("valuationCompetenceCourse"));

		if (selectedValuationCompetenceCourse == null) {
			if (valuationCompetenceCourseList != null && !valuationCompetenceCourseList.isEmpty()) {
				return valuationCompetenceCourseList.get(0);
			} else {
				return null;
			}
		} else {
			return selectedValuationCompetenceCourse;
		}
	}

	private CourseValuationType getSelectedCourseValuationType(DynaActionForm dynaForm) {
		return ((String) dynaForm.get("courseValuationType") == null) ? CourseValuationType.NOT_DETERMINED
				: CourseValuationType.valueOf((String) dynaForm.get("courseValuationType"));
	}

	private List<ValuationCompetenceCourse> getActiveValuationCompetenceCourseList(
			ValuationGrouping selectedValuationGrouping,
			ExecutionPeriod executionPeriod) {
		return new ArrayList<ValuationCompetenceCourse>(
				selectedValuationGrouping.getValuationCompetenceCoursesByExecutionPeriod(executionPeriod));
	}

	private void initializeVariables(Person person, DynaActionForm dynaForm, ValuationPhase phase) {
		List<ValuationGroupingDTOEntry> valuationGroupingOptionEntryList = ValuationGroupingDTOEntry.getValuationGroupingOptionEntriesForPerson(phase, person, false, true);
		
		dynaForm.set("valuationGrouping", valuationGroupingOptionEntryList.get(0).getValuationGrouping().getIdInternal());
		dynaForm.set("courseValuationType", CourseValuationType.NOT_DETERMINED.toString());
		dynaForm.set("valuationCompetenceCourse", null);
		dynaForm.set("valuationCurricularCourse", null);
		dynaForm.set("curricularCourseValuationGroup", null);
		dynaForm.set("suppressRedundantHoursTypes", true);
	}

	private Integer getFromRequestAndSetOnFormTeacherServiceDistributionId(
			HttpServletRequest request,
			DynaActionForm dynaForm) {
		dynaForm.set("teacherServiceDistribution", new Integer(request.getParameter("teacherServiceDistribution")));
		return new Integer(request.getParameter("teacherServiceDistribution"));
	}

	private ValuationGrouping getSelectedValuationGrouping(IUserView userView, DynaActionForm dynaForm) throws FenixFilterException, FenixServiceException {
		return rootDomainObject.readValuationGroupingByOID((Integer) dynaForm.get("valuationGrouping"));
	}

	private TeacherServiceDistribution getTeacherServiceDistribution(IUserView userView, DynaActionForm dynaForm) throws FenixServiceException, FenixFilterException {
		return rootDomainObject.readTeacherServiceDistributionByOID((Integer) dynaForm.get("teacherServiceDistribution"));
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

	
	public ActionForward prepareLinkForCourseValuation(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		IUserView userView = SessionUtils.getUserView(request);
		
		DynaActionForm dynaForm = (DynaActionForm) form;
		getFromRequestAndSetOnFormTeacherServiceDistributionId(request, dynaForm);
		TeacherServiceDistribution teacherServiceDistribution = getTeacherServiceDistribution(userView, dynaForm);
		
		initializeVariables(userView.getPerson(), dynaForm, teacherServiceDistribution.getCurrentValuationPhase());
		
		Integer selectedCourseValuationId = new Integer(request.getParameter("courseValuation"));
		
		CourseValuation courseValuation = rootDomainObject.readCourseValuationByOID(selectedCourseValuationId);
		
		if(courseValuation instanceof CurricularCourseValuation){
			dynaForm.set("curricularCourseValuation", courseValuation.getIdInternal());
			
		} else if(courseValuation instanceof CurricularCourseValuationGroup){
			dynaForm.set("curricularCourseValuationGroup", courseValuation.getIdInternal());
			
		}
		
		ValuationCompetenceCourse valuationCompetenceCourse = courseValuation.getValuationCompetenceCourse();
		dynaForm.set("valuationCompetenceCourse", valuationCompetenceCourse.getIdInternal());
		dynaForm.set("executionPeriod", courseValuation.getExecutionPeriod().getIdInternal());
		
		dynaForm.set("courseValuationViewLink", selectedCourseValuationId.toString());
		
		return loadCourseValuations(mapping, form, request, response);
	}	
}
