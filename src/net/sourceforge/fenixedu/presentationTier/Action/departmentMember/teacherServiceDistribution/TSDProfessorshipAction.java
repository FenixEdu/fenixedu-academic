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
import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.TSDTeacherDTOEntry;
import net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.TeacherServiceDistributionDTOEntry;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourseType;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCurricularCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCurricularCourseGroup;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCurricularLoad;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhase;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProfessorship;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDTeacher;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDValueType;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.Transformer;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class TSDProfessorshipAction extends FenixDispatchAction {

    private static final Integer VIEW_PROFESSORSHIP_VALUATION_BY_TEACHERS = 0;

    private static final Integer VIEW_PROFESSORSHIP_VALUATION_BY_COURSES = 1;

    public ActionForward prepareForTSDProfessorship(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	getFromRequestAndSetOnFormTSDProcessId(request, (DynaActionForm) form);
	initializeVariables((DynaActionForm) form);

	return loadTSDProfessorships(mapping, form, request, response);
    }

    public ActionForward loadTeacherServiceDistribution(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	DynaActionForm dynaForm = (DynaActionForm) form;

	dynaForm.set("competenceCourse", null);
	dynaForm.set("tsdCurricularCourse", null);
	dynaForm.set("tsdCurricularCourseGroup", null);
	dynaForm.set("tsdTeacher", null);

	return loadTSDProfessorships(mapping, form, request, response);
    }

    public ActionForward loadCompetenceCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	DynaActionForm dynaForm = (DynaActionForm) form;

	dynaForm.set("tsdCurricularCourse", null);
	dynaForm.set("tsdCurricularCourseGroup", null);

	return loadTSDProfessorships(mapping, form, request, response);
    }

    public ActionForward setTSDProfessorship(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	IUserView userView = SessionUtils.getUserView(request);
	DynaActionForm dynaForm = (DynaActionForm) form;

	TSDCourse selectedTSDCompetenceCourse = getSelectedTSDCourse(userView, dynaForm, null);
	TeacherServiceDistribution selectedTeacherServiceDistribution = getSelectedTeacherServiceDistribution(userView, dynaForm,
		null);
	TSDTeacher selectedTSDTeacher = getSelectedTSDTeacher(userView, dynaForm, null);
	ExecutionSemester selectedExecutionPeriod = getSelectedExecutionPeriod(userView, dynaForm, null);

	Map<String, Object> tsdProfessorshipParameters = obtainProfessorshipParametersFromForm(dynaForm);
	TSDCourse tsdCourse = null;
	TSDCourseType tsdCourseType = selectedTeacherServiceDistribution.getTSDCourseType(selectedTSDCompetenceCourse,
		selectedExecutionPeriod);

	if (tsdCourseType == TSDCourseType.CURRICULAR_COURSE_VALUATION) {
	    tsdCourse = getSelectedTSDCurricularCourse(dynaForm, null);
	} else if (tsdCourseType == TSDCourseType.CURRICULAR_COURSE_VALUATION_GROUP) {
	    tsdCourse = getSelectedTSDCurricularCourseGroup(dynaForm, null);
	} else {
	    tsdCourse = selectedTSDCompetenceCourse;
	}

	Object[] parameters = new Object[] { tsdCourse.getIdInternal(), selectedTSDTeacher.getIdInternal(),
		tsdProfessorshipParameters };

	ServiceUtils.executeService(userView, "SetTSDProfessorship", parameters);

	return loadTSDProfessorships(mapping, form, request, response);
    }

    public ActionForward removeTSDProfessorships(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	IUserView userView = SessionUtils.getUserView(request);
	DynaActionForm dynaForm = (DynaActionForm) form;

	TSDProfessorship selectedTSDProfessorship = getSelectedTSDProfessorship(userView, dynaForm);

	ServiceUtils
		.executeService(userView, "DeleteTSDProfessorship", new Object[] { selectedTSDProfessorship.getIdInternal() });

	return loadTSDProfessorships(mapping, form, request, response);
    }

    public ActionForward loadExecutionPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	DynaActionForm dynaForm = (DynaActionForm) form;

	dynaForm.set("competenceCourse", null);
	dynaForm.set("tsdCurricularCourse", null);
	dynaForm.set("tsdCurricularCourseGroup", null);

	return loadTSDProfessorships(mapping, form, request, response);
    }

    public ActionForward setExtraCredits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	IUserView userView = SessionUtils.getUserView(request);
	DynaActionForm dynaForm = (DynaActionForm) form;

	TSDTeacher selectedTSDTeacher = getSelectedTSDTeacher(userView, dynaForm, null);

	String extraCreditsName = (String) dynaForm.get("extraCreditsName");
	Double extraCreditsValue = Double.parseDouble((String) dynaForm.get("extraCreditsValue"));
	Boolean usingExtraCredits = dynaForm.get("usingExtraCredits") == null ? false : (Boolean) dynaForm
		.get("usingExtraCredits");

	Object[] arguments = new Object[] { selectedTSDTeacher.getIdInternal(), extraCreditsName, extraCreditsValue,
		usingExtraCredits };

	ServiceUtils.executeService(userView, "SetExtraCreditsToTSDTeacher", arguments);

	return loadTSDProfessorships(mapping, form, request, response);
    }

    public ActionForward loadTSDProfessorships(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	IUserView userView = SessionUtils.getUserView(request);
	DynaActionForm dynaForm = (DynaActionForm) form;

	TSDProcess tsdProcess = getTSDProcess(userView, dynaForm);
	TSDProcessPhase currentTSDProcessPhase = tsdProcess.getCurrentTSDProcessPhase();
	// TeacherServiceDistribution rootTeacherServiceDistribution =
	// currentTSDProcessPhase.getRootTeacherServiceDistribution();

	List<TeacherServiceDistributionDTOEntry> tsdDTOEntryList = TeacherServiceDistributionDTOEntry
		.getTeacherServiceDistributionOptionEntriesForPerson(currentTSDProcessPhase, userView.getPerson(), false, true);
	TeacherServiceDistribution selectedTeacherServiceDistribution = getSelectedTeacherServiceDistribution(userView, dynaForm,
		tsdDTOEntryList.get(0).getTeacherServiceDistribution());

	List<ExecutionSemester> executionPeriodList = new ArrayList<ExecutionSemester>(tsdProcess.getExecutionPeriods());
	Collections.sort(executionPeriodList, new BeanComparator("semester"));
	ExecutionSemester selectedExecutionPeriod = getSelectedExecutionPeriod(userView, dynaForm, executionPeriodList);

	List<TSDCourse> activeTSDCourseList = getActiveTSDCourseList(selectedTeacherServiceDistribution, selectedExecutionPeriod);
	List<TSDTeacher> tsdTeacherList = getTSDTeacherList(selectedTeacherServiceDistribution);
	Collections.sort(tsdTeacherList, new BeanComparator("name"));

	if (!activeTSDCourseList.isEmpty()) {
	    Collections.sort(activeTSDCourseList, new BeanComparator("name"));
	    TSDCourse selectedTSDCompetenceCourse = getSelectedTSDCourse(userView, dynaForm, activeTSDCourseList);

	    List<TSDCurricularCourse> tsdCurricularCourseList = null;
	    TSDCurricularCourse selectedTSDCurricularCourse = null;
	    List<TSDCurricularCourseGroup> tsdCurricularCourseGroupList = null;
	    TSDCurricularCourseGroup selectedTSDCurricularCourseGroup = null;
	    TSDTeacher selectedTSDTeacher = null;

	    TSDCourseType tsdCourseType = selectedTeacherServiceDistribution.getTSDCourseType(selectedTSDCompetenceCourse,
		    selectedExecutionPeriod);

	    if (tsdCourseType == TSDCourseType.CURRICULAR_COURSE_VALUATION) {
		tsdCurricularCourseList = selectedTeacherServiceDistribution.getTSDCurricularCourses(selectedTSDCompetenceCourse
			.getCompetenceCourse(), selectedExecutionPeriod);

		selectedTSDCurricularCourse = getSelectedTSDCurricularCourse(dynaForm, tsdCurricularCourseList);

	    } else if (tsdCourseType == TSDCourseType.CURRICULAR_COURSE_VALUATION_GROUP) {
		tsdCurricularCourseGroupList = selectedTeacherServiceDistribution.getTSDCurricularCourseGroups(
			selectedTSDCompetenceCourse.getCompetenceCourse(), selectedExecutionPeriod);

		selectedTSDCurricularCourseGroup = getSelectedTSDCurricularCourseGroup(dynaForm, tsdCurricularCourseGroupList);
	    }

	    if (!tsdTeacherList.isEmpty()) {
		selectedTSDTeacher = getSelectedTSDTeacher(userView, dynaForm, tsdTeacherList);
	    } else {
		request.setAttribute("notAvailableTSDTeachers", true);
	    }

	    fillFormAndRequest(request, dynaForm, currentTSDProcessPhase, selectedTeacherServiceDistribution,
		    activeTSDCourseList, selectedTSDCompetenceCourse, tsdCurricularCourseList, selectedTSDCurricularCourse,
		    tsdCurricularCourseGroupList, selectedTSDCurricularCourseGroup, tsdTeacherList, selectedTSDTeacher,
		    selectedExecutionPeriod);
	} else {
	    request.setAttribute("notAvailableCompetenceCourses", true);
	}

	if (tsdTeacherList.isEmpty()) {
	    request.setAttribute("notAvailableTSDTeachers", true);
	}

	request.setAttribute("tsdTeacherList", tsdTeacherList);
	request.setAttribute("selectedTSDTeacher", new TSDTeacherDTOEntry(getSelectedTSDTeacher(userView, dynaForm,
		tsdTeacherList), selectedTeacherServiceDistribution.getTSDProcessPhase().getTSDProcess().getExecutionPeriods()));

	request.setAttribute("tsdProcess", tsdProcess);
	request.setAttribute("tsdOptionEntryList", tsdDTOEntryList);
	request.setAttribute("executionPeriodList", executionPeriodList);

	Integer viewType = (Integer) dynaForm.get("viewType");

	if (viewType.equals(VIEW_PROFESSORSHIP_VALUATION_BY_COURSES)) {
	    return mapping.findForward("tsdProfessorshipForm");
	} else {
	    return mapping.findForward("tsdProfessorshipFormByTSDTeacher");
	}
    }

    private void fillFormAndRequest(HttpServletRequest request, DynaActionForm dynaForm, TSDProcessPhase tsdProcessPhase,
	    TeacherServiceDistribution selectedTeacherServiceDistribution, List<TSDCourse> tsdCourseList,
	    TSDCourse selectedTSDCompetenceCourse, List<TSDCurricularCourse> tsdCurricularCourseList,
	    TSDCurricularCourse selectedTSDCurricularCourse, List<TSDCurricularCourseGroup> tsdCurricularCourseGroupList,
	    TSDCurricularCourseGroup selectedTSDCurricularCourseGroup, List<TSDTeacher> tsdTeacherList,
	    TSDTeacher selectedTSDTeacher, ExecutionSemester executionSemester) {

	request.setAttribute("competenceCourseList", tsdCourseList);
	request.setAttribute("selectedCompetenceCourse", selectedTSDCompetenceCourse);
	request.setAttribute("tsdCurricularCourseList", tsdCurricularCourseList);
	request.setAttribute("selectedCurricularCourse", selectedTSDCurricularCourse);
	request.setAttribute("tsdCurricularCourseGroupList", tsdCurricularCourseGroupList);
	request.setAttribute("selectedTSDCurricularCourseGroup", selectedTSDCurricularCourseGroup);
	request.setAttribute("tsdTeacherList", tsdTeacherList);
	request.setAttribute("selectedTeacher", selectedTSDTeacher);

	List<ExecutionSemester> executionPeriodList = new ArrayList<ExecutionSemester>();
	executionPeriodList.add(executionSemester);

	request.setAttribute("selectedTSDTeacher", new TSDTeacherDTOEntry(selectedTSDTeacher, selectedTeacherServiceDistribution
		.getTSDProcessPhase().getTSDProcess().getExecutionPeriods()));

	dynaForm.set("competenceCourse", selectedTSDCompetenceCourse.getIdInternal());

	if (selectedTSDCurricularCourse != null) {
	    dynaForm.set("tsdCurricularCourse", selectedTSDCurricularCourse.getIdInternal());
	}

	if (selectedTSDCurricularCourseGroup != null) {
	    dynaForm.set("tsdCurricularCourseGroup", selectedTSDCurricularCourseGroup.getIdInternal());
	}

	if (selectedTSDTeacher != null) {
	    dynaForm.set("tsdTeacher", selectedTSDTeacher.getIdInternal());
	}

	TSDCourseType selectedTSDCourseType = selectedTeacherServiceDistribution.getTSDCourseType(selectedTSDCompetenceCourse,
		executionSemester);
	request.setAttribute("selectedTSDCourseType", selectedTSDCourseType.toString());

	TSDCourse selectedTSDCourse = null;
	if (selectedTSDCourseType == TSDCourseType.COMPETENCE_COURSE_VALUATION) {
	    selectedTSDCourse = selectedTSDCompetenceCourse;
	} else if (selectedTSDCourseType == TSDCourseType.CURRICULAR_COURSE_VALUATION) {
	    selectedTSDCourse = selectedTSDCurricularCourse;
	} else if (selectedTSDCourseType == TSDCourseType.CURRICULAR_COURSE_VALUATION_GROUP) {
	    selectedTSDCourse = selectedTSDCurricularCourseGroup;
	}

	if (selectedTSDCourse == null) {
	    request.setAttribute("tsdCourseNotSelected", true);
	} else {
	    request.setAttribute("selectedTSDCourse", selectedTSDCourse);

	    ShiftType shiftType = getSelectedShiftType(dynaForm, selectedTSDCourse);
	    List<ShiftType> shiftsList = (List<ShiftType>) CollectionUtils.collect(selectedTSDCourse.getTSDCurricularLoads(),
		    new Transformer() {
			final public Object transform(Object arg0) {
			    TSDCurricularLoad tsdLoad = (TSDCurricularLoad) arg0;
			    return tsdLoad.getType();
			}
		    });

	    request.setAttribute("shiftType", shiftType);
	    request.setAttribute("shiftsList", shiftsList);
	}

	if (selectedTSDTeacher == null) {
	    request.setAttribute("tsdTeacherNotSelected", true);
	}

	fillFormWithTSDProfessorshipParameters(dynaForm, selectedTSDCourse, selectedTSDTeacher, null);
    }

    private void fillFormWithTSDProfessorshipParameters(DynaActionForm dynaForm, TSDCourse selectedTSDCourse,
	    TSDTeacher selectedTSDTeacher, ShiftType type) {

	if (selectedTSDTeacher != null) {
	    dynaForm.set("extraCreditsName", selectedTSDTeacher.getExtraCreditsName());
	    dynaForm.set("extraCreditsValue", selectedTSDTeacher.getExtraCreditsValue().toString());
	    dynaForm.set("usingExtraCredits", selectedTSDTeacher.getUsingExtraCredits());
	}

	if (selectedTSDCourse == null || selectedTSDTeacher == null)
	    return;

	TSDProfessorship tsdProfessorship = selectedTSDCourse.getTSDProfessorshipByTSDTeacherAndShiftType(selectedTSDTeacher,
		type);

	if (tsdProfessorship != null) {
	    dynaForm.set("hoursManual", tsdProfessorship.getHours().toString());
	    dynaForm.set("hoursType", tsdProfessorship.getHoursType().toString());
	} else {
	    dynaForm.set("hoursManual", ((Double) 0d).toString());
	    dynaForm.set("hoursType", TSDValueType.MANUAL_VALUE.toString());
	}
    }

    private void initializeVariables(DynaActionForm dynaForm) {
	dynaForm.set("tsd", null);
	dynaForm.set("competenceCourse", null);
	dynaForm.set("tsdCurricularCourse", null);
	dynaForm.set("tsdCurricularCourseGroup", null);
	dynaForm.set("tsdTeacher", null);
	dynaForm.set("distributionViewAnchor", 0);
	dynaForm.set("viewType", VIEW_PROFESSORSHIP_VALUATION_BY_COURSES);
    }

    private Integer getFromRequestAndSetOnFormTSDProcessId(HttpServletRequest request, DynaActionForm dynaForm) {
	Integer tsdProcessId = new Integer(request.getParameter("tsdProcess"));
	dynaForm.set("tsdProcess", tsdProcessId);
	return tsdProcessId;
    }

    private TSDProcess getTSDProcess(IUserView userView, DynaActionForm dynaForm) throws FenixServiceException,
	    FenixFilterException {
	return rootDomainObject.readTSDProcessByOID((Integer) dynaForm.get("tsdProcess"));
    }

    private TeacherServiceDistribution getSelectedTeacherServiceDistribution(IUserView userView, DynaActionForm dynaForm,
	    TeacherServiceDistribution rootTeacherServiceDistribution) throws FenixFilterException, FenixServiceException {
	TeacherServiceDistribution selectedTeacherServiceDistribution = rootDomainObject
		.readTeacherServiceDistributionByOID((Integer) dynaForm.get("tsd"));
	return (selectedTeacherServiceDistribution == null) ? rootTeacherServiceDistribution : selectedTeacherServiceDistribution;
    }

    private List<TSDCourse> getActiveTSDCourseList(TeacherServiceDistribution selectedTeacherServiceDistribution,
	    ExecutionSemester executionSemester) {
	return new ArrayList<TSDCourse>(selectedTeacherServiceDistribution
		.getTSDCompetenceAndVirtualCoursesByExecutionPeriod(executionSemester));
    }

    private List<TSDTeacher> getTSDTeacherList(TeacherServiceDistribution selectedTeacherServiceDistribution) {
	return new ArrayList<TSDTeacher>(selectedTeacherServiceDistribution.getTSDTeachers());
    }

    private TSDCourse getSelectedTSDCourse(IUserView userView, DynaActionForm dynaForm, List<TSDCourse> competenceCourseList)
	    throws FenixFilterException, FenixServiceException {
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

    private TSDCurricularCourse getSelectedTSDCurricularCourse(DynaActionForm dynaForm,
	    List<TSDCurricularCourse> tsdCurricularCourseList) throws FenixFilterException, FenixServiceException {
	TSDCurricularCourse selectedTSDCurricularCourse = (TSDCurricularCourse) rootDomainObject
		.readTSDCourseByOID((Integer) dynaForm.get("tsdCurricularCourse"));

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

    private TSDCurricularCourseGroup getSelectedTSDCurricularCourseGroup(DynaActionForm dynaForm,
	    List<TSDCurricularCourseGroup> tsdCurricularCourseGroupList) throws FenixFilterException, FenixServiceException {
	TSDCurricularCourseGroup selectedTSDCurricularCourseGroup = (TSDCurricularCourseGroup) rootDomainObject
		.readTSDCourseByOID((Integer) dynaForm.get("tsdCurricularCourseGroup"));

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

    private TSDTeacher getSelectedTSDTeacher(IUserView userView, DynaActionForm dynaForm, List<TSDTeacher> tsdTeacherList)
	    throws FenixFilterException, FenixServiceException {
	TSDTeacher selectedTSDTeacher = rootDomainObject.readTSDTeacherByOID((Integer) dynaForm.get("tsdTeacher"));

	if (selectedTSDTeacher == null) {
	    if (!tsdTeacherList.isEmpty()) {
		return tsdTeacherList.get(0);
	    } else {
		return null;
	    }
	} else {
	    return selectedTSDTeacher;
	}
    }

    private TSDProfessorship getSelectedTSDProfessorship(IUserView userView, DynaActionForm dynaForm)
	    throws FenixFilterException, FenixServiceException {
	return rootDomainObject.readTSDProfessorshipByOID((Integer) dynaForm.get("tsdProfessorship"));
    }

    private Map<String, Object> obtainProfessorshipParametersFromForm(DynaActionForm dynaForm) {
	Map<String, Object> tsdCourseParameters = new HashMap<String, Object>();

	tsdCourseParameters.put("hoursManual", Double.parseDouble((String) dynaForm.get("hoursManual")));
	tsdCourseParameters.put("hoursType", dynaForm.get("hoursType"));
	tsdCourseParameters.put("shiftType", dynaForm.get("shiftType"));

	return tsdCourseParameters;
    }

    public ActionForward prepareLinkForTSDProfessorshipByCourse(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	return prepareLinkForTSDProfessorship(mapping, form, request, response, VIEW_PROFESSORSHIP_VALUATION_BY_COURSES);
    }

    public ActionForward prepareLinkForTSDProfessorshipByTeacher(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	return prepareLinkForTSDProfessorship(mapping, form, request, response, VIEW_PROFESSORSHIP_VALUATION_BY_TEACHERS);
    }

    private ActionForward prepareLinkForTSDProfessorship(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response, Integer viewType) throws FenixFilterException, FenixServiceException {

	DynaActionForm dynaForm = (DynaActionForm) form;
	getFromRequestAndSetOnFormTSDProcessId(request, dynaForm);

	initializeVariables(dynaForm);

	Integer selectedTSDCourseId = new Integer(request.getParameter("tsdCourse"));
	TSDCourse tsdCourse = rootDomainObject.readTSDCourseByOID(selectedTSDCourseId);

	if (tsdCourse instanceof TSDCurricularCourse) {
	    dynaForm.set("tsdCurricularCourse", tsdCourse.getIdInternal());
	} else if (tsdCourse instanceof TSDCurricularCourseGroup) {
	    dynaForm.set("tsdCurricularCourseGroup", tsdCourse.getIdInternal());

	}

	Integer selectedTSDTeacherId = new Integer(request.getParameter("tsdTeacher"));
	CompetenceCourse competenceCourse = tsdCourse.getCompetenceCourse();

	dynaForm.set("competenceCourse", competenceCourse == null ? null : competenceCourse.getIdInternal());
	dynaForm.set("tsdTeacher", selectedTSDTeacherId);
	dynaForm.set("executionPeriod", tsdCourse.getExecutionPeriod().getIdInternal());
	dynaForm.set("viewType", viewType);

	if (viewType == VIEW_PROFESSORSHIP_VALUATION_BY_COURSES) {
	    dynaForm.set("distributionViewAnchor", selectedTSDCourseId);
	} else {
	    dynaForm.set("distributionViewAnchor", selectedTSDTeacherId);
	}

	return loadTSDProfessorships(mapping, form, request, response);
    }

    private ExecutionSemester getSelectedExecutionPeriod(IUserView userView, DynaActionForm dynaForm,
	    List<ExecutionSemester> executionPeriodList) throws FenixServiceException, FenixFilterException {
	ExecutionSemester selectedExecutionPeriod = rootDomainObject.readExecutionSemesterByOID((Integer) dynaForm
		.get("executionPeriod"));

	if (selectedExecutionPeriod == null) {
	    if (executionPeriodList != null && executionPeriodList.size() > 0) {
		return executionPeriodList.get(0);
	    } else {
		return null;
	    }
	}

	return selectedExecutionPeriod;
    }

    private ShiftType getSelectedShiftType(DynaActionForm dynaForm, TSDCourse course) {
	if (dynaForm.get("shiftType") == null || dynaForm.get("shiftType").equals("")) {
	    if (course.getTSDCurricularLoadsCount() > 0) {
		return course.getTSDCurricularLoads().get(0).getType();
	    } else {
		return null;
	    }
	}

	return ShiftType.valueOf((String) dynaForm.get("shiftType"));
    }

    public ActionForward loadTSDCurricularLoad(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	return loadTSDProfessorships(mapping, form, request, response);
    }
}
