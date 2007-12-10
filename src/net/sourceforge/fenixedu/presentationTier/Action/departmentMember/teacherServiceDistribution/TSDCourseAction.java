package net.sourceforge.fenixedu.presentationTier.Action.departmentMember.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.TeacherServiceDistributionDTOEntry;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCompetenceCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourseType;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCurricularCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCurricularCourseGroup;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhase;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDVirtualCourseGroup;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class TSDCourseAction extends FenixDispatchAction {

	public ActionForward prepareForTSDCourse(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {

		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;

		getFromRequestAndSetOnFormTSDProcessId(request, dynaForm);
		TSDProcess tsdProcess = getTSDProcess(userView, dynaForm);

		initializeVariables(userView.getPerson(), dynaForm, tsdProcess.getCurrentTSDProcessPhase());
		return loadTSDCourses(mapping, form, request, response);
	}

	public ActionForward loadTeacherServiceDistribution(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		DynaActionForm dynaForm = (DynaActionForm) form;

		dynaForm.set("competenceCourse", null);
		dynaForm.set("tsdCurricularCourse", null);
		dynaForm.set("tsdCurricularCourseGroup", null);
		
		dynaForm.set("tsdCourseType", TSDCourseType.NOT_DETERMINED.toString());

		return loadTSDCourses(mapping, form, request, response);
	}

	public ActionForward loadCompetenceCourse(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;

		TSDCourse selectedTSDCourse = getSelectedTSDCourse(
				userView,
				dynaForm,
				null);

		TeacherServiceDistribution tsd = getSelectedTeacherServiceDistribution(dynaForm);
		ExecutionPeriod selectedExecutionPeriod = getSelectedExecutionPeriod(userView, dynaForm, null);

		dynaForm.set("tsdCourseType", tsd.getTSDCourseType(selectedTSDCourse, selectedExecutionPeriod).toString());
		dynaForm.set("tsdCurricularCourse", null);
		dynaForm.set("tsdCurricularCourseGroup", null);
		
		return loadTSDCourses(mapping, form, request, response);
	}

	public ActionForward loadExecutionPeriod(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		DynaActionForm dynaForm = (DynaActionForm) form;

		dynaForm.set("competenceCourse", null);
		dynaForm.set("tsdCurricularCourse", null);
		dynaForm.set("tsdCurricularCourseGroup", null);
		
		dynaForm.set("tsdCourseType", TSDCourseType.NOT_DETERMINED.toString());

		return loadTSDCourses(mapping, form, request, response);
	}

	public ActionForward setTSDCourseType(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;

		CompetenceCourse selectedCompetenceCourse = getSelectedTSDCourse(
				userView,
				dynaForm,
				null).getCompetenceCourse();
		TSDCourseType selectedTSDCourseType = getSelectedTSDCourseType(dynaForm);
		ExecutionPeriod selectedExecutionPeriod = getSelectedExecutionPeriod(userView, dynaForm, null);
		TeacherServiceDistribution tsd = getSelectedTeacherServiceDistribution(dynaForm);

		Object[] parameters = new Object[] { selectedCompetenceCourse.getIdInternal(),
				tsd.getIdInternal(), selectedExecutionPeriod.getIdInternal(),
				selectedTSDCourseType.toString() };
		ServiceUtils.executeService(userView, "SetTSDCourseType", parameters);

		dynaForm.set("tsdCurricularCourse", null);
		dynaForm.set("tsdCurricularCourseGroup", null);

		return loadTSDCourses(mapping, form, request, response);
	}

	public ActionForward prepareForTSDCurricularCourseGroupCreation(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;

		TSDProcessPhase currentTSDProcessPhase = getTSDProcess(userView, dynaForm).getCurrentTSDProcessPhase();
		CompetenceCourse selectedCompetenceCourse = getSelectedTSDCourse(
				userView,
				dynaForm,
				null).getCompetenceCourse();
		TeacherServiceDistribution selectedTeacherServiceDistribution = getSelectedTeacherServiceDistribution(dynaForm);
		ExecutionPeriod selectedExecutionPeriod = getSelectedExecutionPeriod(userView, dynaForm, null);
		
		createDefaultTSDCurricularCourses(
				userView,
				selectedTeacherServiceDistribution,
				selectedCompetenceCourse,
				currentTSDProcessPhase,
				selectedExecutionPeriod,
				false);
		
		List<TSDCurricularCourse> availableTSDCurricularCourseToGroupList = selectedTeacherServiceDistribution.
			getTSDCurricularCoursesWithoutTSDCourseGroup(selectedCompetenceCourse, selectedExecutionPeriod);

		request.setAttribute(
				"availableTSDCurricularCourseToGroupList",
				availableTSDCurricularCourseToGroupList);
		request.setAttribute("tsdProcess", getTSDProcess(userView, dynaForm));

		return mapping.findForward("tsdCurricularCourseGroupCreationForm");
	}

	public ActionForward createTSDCurricularCourseGroup(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;

		TeacherServiceDistribution tsd = getSelectedTeacherServiceDistribution(dynaForm);
		Integer[] tsdCurricularCourseToGroupArray = obtainAssociatedTSDCurricularCoursesFromDynaActionForm(dynaForm);

		if (tsdCurricularCourseToGroupArray.length > 0) {
			TSDCurricularCourseGroup tsdCurricularCourseGroup = (TSDCurricularCourseGroup) ServiceUtils.executeService(
					userView,
					"CreateTSDCurricularCourseGroup",
					new Object[] { tsd.getIdInternal(), tsdCurricularCourseToGroupArray });

			dynaForm.set("tsdCurricularCourseGroup", tsdCurricularCourseGroup.getIdInternal());
		}

		return loadTSDCourses(mapping, form, request, response);
	}

	private Integer[] obtainAssociatedTSDCurricularCoursesFromDynaActionForm(DynaActionForm dynaForm) {
		String[] tsdCurricularCourseToGroupArrayString = (String[]) dynaForm.get("tsdCurricularCourseArray"); 
		Integer[] tsdCurricularCourseToGroupArray = new Integer[tsdCurricularCourseToGroupArrayString.length];
		
		for(int i = 0; i < tsdCurricularCourseToGroupArrayString.length; i++) {
			tsdCurricularCourseToGroupArray[i] = Integer.parseInt(tsdCurricularCourseToGroupArrayString[i]);
		}
		return tsdCurricularCourseToGroupArray;
	}
	
	public ActionForward deleteTSDCurricularCourseGroup(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;

		TSDCurricularCourseGroup selectedTSDCurricularCourseGroup = getSelectedTSDCurricularCourseGroup(
				dynaForm, null);

		Object[] parameters = new Object[] { selectedTSDCurricularCourseGroup.getIdInternal() };

		ServiceUtils.executeService(userView, "DeleteTSDCurricularCourseGroup", parameters);

		dynaForm.set("tsdCourseType", TSDCourseType.NOT_DETERMINED.toString());
		dynaForm.set("tsdCurricularCourseGroup", null);

		return loadTSDCourses(mapping, form, request, response);
	}

	public ActionForward loadTSDCourses(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {

		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm dynaForm = (DynaActionForm) form;

		TeacherServiceDistribution selectedTeacherServiceDistribution = getSelectedTeacherServiceDistribution(dynaForm);
		TSDProcessPhase currentTSDProcessPhase = getTSDProcess(userView, dynaForm).getCurrentTSDProcessPhase();
		List<ExecutionPeriod> executionPeriodList = getOrderedExecutionPeriods(userView, dynaForm);
		ExecutionPeriod selectedExecutionPeriod = getSelectedExecutionPeriod(userView, dynaForm, executionPeriodList);

		List<TSDCourse> activeTSDCourseList = getActiveTSDCourseList(
				selectedTeacherServiceDistribution,
				selectedExecutionPeriod);

		TSDCourseType selectedTSDCourseType = getSelectedTSDCourseType(dynaForm);

		if (!activeTSDCourseList.isEmpty()) {
			Collections.sort(activeTSDCourseList, new BeanComparator("name"));
			
			TSDCourse selectedTSDCompetenceCourse = getSelectedTSDCourse(
					userView,
					dynaForm,
					activeTSDCourseList);

			if (selectedTSDCourseType == TSDCourseType.NOT_DETERMINED) {
				selectedTSDCourseType = selectedTeacherServiceDistribution.getTSDCourseType(
						selectedTSDCompetenceCourse,
						selectedExecutionPeriod);
			}

			List<TSDCurricularCourse> tsdCurricularCourseList = null;
			TSDCurricularCourse selectedTSDCurricularCourse = null;
			List<TSDCurricularCourseGroup> tsdCurricularCourseGroupList = null;
			TSDCurricularCourseGroup selectedTSDCurricularCourseGroup = null;
			
			if (selectedTSDCourseType == TSDCourseType.CURRICULAR_COURSE_VALUATION) {
				createDefaultTSDCurricularCourses(
						userView,
						selectedTeacherServiceDistribution,
						selectedTSDCompetenceCourse.getCompetenceCourse(),
						currentTSDProcessPhase,
						selectedExecutionPeriod,
						true);
				
				tsdCurricularCourseList =  selectedTeacherServiceDistribution.
					getTSDCurricularCourses(selectedTSDCompetenceCourse.getCompetenceCourse(), selectedExecutionPeriod);
				selectedTSDCurricularCourse = getSelectedTSDCurricularCourse(dynaForm, tsdCurricularCourseList);

			} else if (selectedTSDCourseType == TSDCourseType.CURRICULAR_COURSE_VALUATION_GROUP) {
				createDefaultTSDCurricularCourses(
						userView,
						selectedTeacherServiceDistribution,
						selectedTSDCompetenceCourse.getCompetenceCourse(),
						currentTSDProcessPhase,
						selectedExecutionPeriod,
						false);
				
				tsdCurricularCourseGroupList = selectedTeacherServiceDistribution.getTSDCurricularCourseGroups(
						selectedTSDCompetenceCourse.getCompetenceCourse(), 
						selectedExecutionPeriod);
				selectedTSDCurricularCourseGroup = getSelectedTSDCurricularCourseGroup(
						dynaForm,
						tsdCurricularCourseGroupList);
			}

			fillFormAndRequest(
					request,
					dynaForm,
					currentTSDProcessPhase,
					selectedTeacherServiceDistribution,
					selectedTSDCourseType,
					activeTSDCourseList,
					selectedTSDCompetenceCourse,
					tsdCurricularCourseList,
					selectedTSDCurricularCourse,
					tsdCurricularCourseGroupList,
					selectedTSDCurricularCourseGroup,
					selectedExecutionPeriod);
		} else {
			request.setAttribute("notAvailableCompetenceCourses", true);
		}
		
		List<TeacherServiceDistributionDTOEntry> tsdOptionEntryList = TeacherServiceDistributionDTOEntry.getTeacherServiceDistributionOptionEntriesForPerson(currentTSDProcessPhase, userView.getPerson(), false, true);

		
		request.setAttribute("tsdProcess", getTSDProcess(userView, dynaForm));
		request.setAttribute("tsdOptionEntryList", tsdOptionEntryList );
		request.setAttribute("executionPeriodList", executionPeriodList);
		return mapping.findForward("tsdCourseForm");
	}

	private void createDefaultTSDCurricularCourses(
			IUserView userView,
			TeacherServiceDistribution tsd,
			CompetenceCourse competenceCourse,
			TSDProcessPhase currentTSDProcessPhase,
			ExecutionPeriod selectedExecutionPeriod,
			Boolean activateCourses) throws FenixServiceException, FenixFilterException {
		Object[] parameters = new Object[] { tsd.getIdInternal(), competenceCourse.getIdInternal(),
				currentTSDProcessPhase.getIdInternal(), selectedExecutionPeriod.getIdInternal(), activateCourses};

		ServiceUtils.executeService(userView, "CreateTSDCurricularCourses", parameters);
	}


	private List<ExecutionPeriod> getOrderedExecutionPeriods(IUserView userView, DynaActionForm dynaForm) throws FenixServiceException, FenixFilterException {
		List<ExecutionPeriod> executionPeriodList = new ArrayList<ExecutionPeriod>(getTSDProcess(
				userView,
				dynaForm).getExecutionPeriods());
		Collections.sort(executionPeriodList, new BeanComparator("semester"));
		return executionPeriodList;
	}

	private void fillFormAndRequest(
			HttpServletRequest request,
			DynaActionForm dynaForm,
			TSDProcessPhase tsdProcessPhase,
			TeacherServiceDistribution selectedTeacherServiceDistribution,
			TSDCourseType selectedTSDCourseType,
			List<TSDCourse> tsdCourseList,
			TSDCourse selectedTSDCompetenceCourse,
			List<TSDCurricularCourse> tsdCurricularCourseList,
			TSDCurricularCourse selectedTSDCurricularCourse,
			List<TSDCurricularCourseGroup> tsdCurricularCourseGroupList,
			TSDCurricularCourseGroup selectedTSDCurricularCourseGroup,
			ExecutionPeriod selectedExecutionPeriod) {

		request.setAttribute("selectedTSD", selectedTeacherServiceDistribution);
		request.setAttribute("competenceCourseList", tsdCourseList);
		request.setAttribute("selectedCompetenceCourse", selectedTSDCompetenceCourse);
		request.setAttribute("tsdCurricularCourseList", tsdCurricularCourseList);
		request.setAttribute("selectedCurricularCourse", selectedTSDCurricularCourse);
		request.setAttribute("tsdCurricularCourseGroupList", tsdCurricularCourseGroupList);
		request.setAttribute("selectedTSDCurricularCourseGroup", selectedTSDCurricularCourseGroup);
		request.setAttribute("selectedTSDCourseType", selectedTSDCourseType.toString());

		dynaForm.set("tsdCourseType", selectedTSDCourseType.toString());
		dynaForm.set("competenceCourse", selectedTSDCompetenceCourse.getIdInternal());

		if (selectedTSDCurricularCourse != null)
			dynaForm.set("tsdCurricularCourse", selectedTSDCurricularCourse.getIdInternal());
		if (selectedTSDCurricularCourseGroup != null)
			dynaForm.set("tsdCurricularCourseGroup", selectedTSDCurricularCourseGroup.getIdInternal());

		TSDCourse selectedTSDCourse = null;
		if (selectedTSDCourseType == TSDCourseType.COMPETENCE_COURSE_VALUATION) {
			selectedTSDCourse = selectedTSDCompetenceCourse;
		} else if (selectedTSDCourseType == TSDCourseType.CURRICULAR_COURSE_VALUATION) {
			selectedTSDCourse = selectedTSDCurricularCourse;
		} else if (selectedTSDCourseType == TSDCourseType.CURRICULAR_COURSE_VALUATION_GROUP) {
			selectedTSDCourse = selectedTSDCurricularCourseGroup;
			
			request.setAttribute("availableTSDCurricularCoursesToGroup",
						selectedTeacherServiceDistribution.getTSDCurricularCoursesWithoutTSDCourseGroup(
								selectedTSDCompetenceCourse.getCompetenceCourse(), 
								selectedExecutionPeriod));
		}
		
		if (selectedTSDCourse == null) {
			request.setAttribute("tsdCourseNotSelected", true);
		} else {
			request.setAttribute("selectedTSDCourse", selectedTSDCourse);
		}
		
		if(selectedTSDCompetenceCourse instanceof TSDVirtualCourseGroup){
			request.setAttribute("tsdVirtualCourseGroup", true);
		}

	}
	private TSDCurricularCourseGroup getSelectedTSDCurricularCourseGroup(
			DynaActionForm dynaForm,
			List<TSDCurricularCourseGroup> tsdCurricularCourseGroupList) throws FenixFilterException, FenixServiceException {
		TSDCurricularCourseGroup selectedTSDCurricularCourseGroup = (TSDCurricularCourseGroup) rootDomainObject.readTSDCourseByOID((Integer) dynaForm.get("tsdCurricularCourseGroup"));

		if (selectedTSDCurricularCourseGroup == null) {
			if (tsdCurricularCourseGroupList != null && !tsdCurricularCourseGroupList.isEmpty()) {
				return tsdCurricularCourseGroupList.get(0);
			} else {
				return null;
			}
		} else {
			return selectedTSDCurricularCourseGroup;
		}
	}
	
	private TSDCurricularCourse getSelectedTSDCurricularCourse(
			DynaActionForm dynaForm,
			List<TSDCurricularCourse> tsdCurricularCourseList) throws FenixFilterException, FenixServiceException {
		TSDCurricularCourse selectedTSDCurricularCourse = (TSDCurricularCourse) rootDomainObject.readTSDCourseByOID((Integer) dynaForm.get("tsdCurricularCourse"));

		if (selectedTSDCurricularCourse == null) {
			if (tsdCurricularCourseList != null && !tsdCurricularCourseList.isEmpty()) {
				return tsdCurricularCourseList.get(0);
			} else {
				return null;
			}
		} else {
			return selectedTSDCurricularCourse;
		}
	}

	private TSDCourse getSelectedTSDCourse(
			IUserView userView,
			DynaActionForm dynaForm,
			List<TSDCourse> competenceCourseList) throws FenixFilterException, FenixServiceException {
		TSDCourse selectedTSDCourse = rootDomainObject.readTSDCourseByOID((Integer) dynaForm.get("competenceCourse"));

		if (selectedTSDCourse == null) {
			if (competenceCourseList != null && !competenceCourseList.isEmpty()) {
				return competenceCourseList.get(0);
			} else {
				return null;
			}
		} else {
			return selectedTSDCourse;
		}
	}

	private TSDCourseType getSelectedTSDCourseType(DynaActionForm dynaForm) {
		return ((String) dynaForm.get("tsdCourseType") == null) ? TSDCourseType.NOT_DETERMINED
				: TSDCourseType.valueOf((String) dynaForm.get("tsdCourseType"));
	}

	private List<TSDCourse> getActiveTSDCourseList(
			TeacherServiceDistribution selectedTeacherServiceDistribution,
			ExecutionPeriod executionPeriod) {
		return new ArrayList<TSDCourse>(
				selectedTeacherServiceDistribution.getTSDCompetenceAndVirtualCoursesByExecutionPeriod(executionPeriod));
	}

	private void initializeVariables(Person person, DynaActionForm dynaForm, TSDProcessPhase phase) {
		List<TeacherServiceDistributionDTOEntry> tsdOptionEntryList = TeacherServiceDistributionDTOEntry.getTeacherServiceDistributionOptionEntriesForPerson(phase, person, false, true);
		
		dynaForm.set("tsd", tsdOptionEntryList.get(0).getTeacherServiceDistribution().getIdInternal());
		dynaForm.set("tsdCourseType", TSDCourseType.NOT_DETERMINED.toString());
		dynaForm.set("competenceCourse", null);
		dynaForm.set("tsdCurricularCourse", null);
		dynaForm.set("tsdCurricularCourseGroup", null);
	}

	private Integer getFromRequestAndSetOnFormTSDProcessId(
			HttpServletRequest request,
			DynaActionForm dynaForm) {
		dynaForm.set("tsdProcess", new Integer(request.getParameter("tsdProcess")));
		return new Integer(request.getParameter("tsdProcess"));
	}

	private TeacherServiceDistribution getSelectedTeacherServiceDistribution(DynaActionForm dynaForm) throws FenixFilterException, FenixServiceException {
		return rootDomainObject.readTeacherServiceDistributionByOID((Integer) dynaForm.get("tsd"));
	}

	private TSDProcess getTSDProcess(IUserView userView, DynaActionForm dynaForm) throws FenixServiceException, FenixFilterException {
		return rootDomainObject.readTSDProcessByOID((Integer) dynaForm.get("tsdProcess"));
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

	
	public ActionForward prepareLinkForTSDCourse(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {
		IUserView userView = SessionUtils.getUserView(request);
		
		DynaActionForm dynaForm = (DynaActionForm) form;
		getFromRequestAndSetOnFormTSDProcessId(request, dynaForm);
		TSDProcess tsdProcess = getTSDProcess(userView, dynaForm);
		
		initializeVariables(userView.getPerson(), dynaForm, tsdProcess.getCurrentTSDProcessPhase());
		
		Integer selectedTSDCourseId = new Integer(request.getParameter("tsdCourse"));
		TSDCourse tsdCourse = rootDomainObject.readTSDCourseByOID(selectedTSDCourseId);
		
		if(tsdCourse instanceof TSDCurricularCourse){
			dynaForm.set("tsdCurricularCourse", tsdCourse.getIdInternal());
			
		} else if(tsdCourse instanceof TSDCurricularCourseGroup){
			dynaForm.set("tsdCurricularCourseGroup", tsdCourse.getIdInternal());
			
		}
		
		dynaForm.set("tsd", new Integer(request.getParameter("tsd")));
		TeacherServiceDistribution tsd = getSelectedTeacherServiceDistribution(dynaForm);
		
		ExecutionPeriod period = tsdCourse.getExecutionPeriod();
		dynaForm.set("executionPeriod", period.getIdInternal());
		
		if(tsdCourse instanceof TSDCompetenceCourse || tsdCourse instanceof TSDVirtualCourseGroup){
			dynaForm.set("competenceCourse", tsdCourse.getIdInternal());
		} else {
			dynaForm.set("competenceCourse", tsd.getTSDCompetenceCourse(tsdCourse.getCompetenceCourse(), 
				tsdCourse.getExecutionPeriod()).getIdInternal());
		}
				
		if(request.getParameter("notTSDCourseViewLink") == null){
			dynaForm.set("tsdCourseViewLink", selectedTSDCourseId.toString());
		}
		
		return loadTSDCourses(mapping, form, request, response);
	}	
}
