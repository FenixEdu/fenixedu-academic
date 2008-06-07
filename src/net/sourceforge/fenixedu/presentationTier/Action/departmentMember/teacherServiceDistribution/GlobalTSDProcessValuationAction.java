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
import net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.TSDCourseDTOEntry;
import net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.TSDTeacherDTOEntry;
import net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.TeacherServiceDistributionDTOEntry;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhase;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.Predicate;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;
import pt.utl.ist.fenix.tools.util.Pair;

public class GlobalTSDProcessValuationAction extends FenixDispatchAction {
    private static final Integer VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_TEACHERS = 0;
    private static final Integer VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_COURSES = 1;
    private static final Integer VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_TEACHERS_AND_COURSES = 2;

    public ActionForward prepareForGlobalTSDProcessValuation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	IUserView userView = UserView.getUser();

	DynaActionForm globalForm = (DynaActionForm) form;

	List<ExecutionYear> executionYearList = new ArrayList<ExecutionYear>(ExecutionYear.readNotClosedExecutionYears());
	Collections.sort(executionYearList, new BeanComparator("year"));

	ExecutionYear selectedExecutionYear = getSelectedExecutionYear(userView, globalForm);

	List<ExecutionSemester> executionPeriodList = new ArrayList<ExecutionSemester>();
	if (selectedExecutionYear != null) {
	    executionPeriodList.addAll(selectedExecutionYear.getExecutionPeriods());
	    Collections.sort(executionPeriodList, new BeanComparator("semester"));
	}

	List<TSDProcess> tsdProcessList;
	ExecutionSemester selectedExecutionPeriod = getSelectedExecutionPeriod(globalForm);
	Department selectedDepartment = userView.getPerson().getEmployee().getCurrentDepartmentWorkingPlace();

	if (selectedExecutionPeriod != null) {
	    tsdProcessList = selectedDepartment.getTSDProcessesByExecutionPeriod(selectedExecutionPeriod);
	} else if (selectedExecutionYear != null) {
	    tsdProcessList = selectedDepartment.getTSDProcessesByExecutionYear(selectedExecutionYear);
	} else {
	    tsdProcessList = selectedDepartment.getTSDProcesses();
	}

	tsdProcessList = selectOnlyTSDProcesssWithPublishedPhases(tsdProcessList);
	Collections.sort(tsdProcessList, new BeanComparator("name"));

	request.setAttribute("executionYearList", executionYearList);
	request.setAttribute("executionPeriodsList", executionPeriodList);
	request.setAttribute("tsdProcessList", tsdProcessList);
	request.setAttribute("departmentName", selectedDepartment.getRealName());

	return mapping.findForward("showGlobalTSDProcesss");
    }

    public ActionForward showTSDProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	DynaActionForm globalForm = (DynaActionForm) form;

	initializeFormVariables(request, globalForm);

	return viewGlobalTSDProcessValuation(mapping, form, request, response);
    }

    private void initializeFormVariables(HttpServletRequest request, DynaActionForm dynaForm) throws FenixFilterException,
	    FenixServiceException {
	Integer tsdProcessId = new Integer(request.getParameter("tsdProcess"));
	dynaForm.set("tsdProcess", tsdProcessId);

	TSDProcess process = getTSDProcess(dynaForm);
	TSDProcessPhase phase = process.getOrderedPublishedTSDProcessPhases().get(0);
	TeacherServiceDistribution rootTSD = phase.getRootTSD();

	dynaForm.set("tsd", rootTSD.getIdInternal());
	dynaForm.set("tsdProcessPhase", phase.getIdInternal());
    }

    public ActionForward changeToViewTeachers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	DynaActionForm globalForm = (DynaActionForm) form;
	globalForm.set("viewType", VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_TEACHERS);

	return viewGlobalTSDProcessValuation(mapping, form, request, response);
    }

    public ActionForward changeToViewCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	DynaActionForm globalForm = (DynaActionForm) form;
	globalForm.set("viewType", VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_COURSES);

	return viewGlobalTSDProcessValuation(mapping, form, request, response);
    }

    public ActionForward changeToViewTeacherAndCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	DynaActionForm globalForm = (DynaActionForm) form;
	globalForm.set("viewType", VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_TEACHERS_AND_COURSES);

	return viewGlobalTSDProcessValuation(mapping, form, request, response);
    }

    public ActionForward viewGlobalTSDProcessValuation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	IUserView userView = UserView.getUser();
	DynaActionForm globalForm = (DynaActionForm) form;

	TSDProcess selectedTSDProcess = getTSDProcess(globalForm);
	ExecutionSemester selectedExecutionPeriod = getSelectedExecutionPeriod(globalForm);
	TeacherServiceDistribution selectedTSD = getSelectedTeacherServiceDistribution(globalForm);
	TSDProcessPhase selectedTSDProcessPhase = getSelectedTSDProcessPhase(globalForm);

	List<TSDCourseDTOEntry> tsdCourseDTOEntryList = null;
	List<TSDTeacherDTOEntry> tsdTeacherDTOEntryList = null;
	Integer viewType = getViewType(globalForm);

	if (viewType.equals(VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_COURSES)
		|| viewType.equals(VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_TEACHERS_AND_COURSES)) {
	    tsdCourseDTOEntryList = getTSDCourseDTOEntries(userView, selectedTSDProcessPhase, selectedTSD,
		    selectedExecutionPeriod);
	}

	if (viewType.equals(VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_TEACHERS)
		|| viewType.equals(VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_TEACHERS_AND_COURSES)) {
	    tsdTeacherDTOEntryList = getTSDTeacherDTOEntries(userView, selectedTSDProcessPhase, selectedTSD,
		    selectedExecutionPeriod);
	}

	String comparatorStr = "name";
	if (viewType.equals(VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_TEACHERS_AND_COURSES)) {
	    comparatorStr = "acronym";
	}

	if (tsdCourseDTOEntryList != null && !tsdCourseDTOEntryList.isEmpty()) {
	    Collections.sort(tsdCourseDTOEntryList, new BeanComparator("TSDCourse." + comparatorStr));
	}
	if (tsdTeacherDTOEntryList != null && !tsdTeacherDTOEntryList.isEmpty()) {
	    Collections.sort(tsdTeacherDTOEntryList, new BeanComparator(comparatorStr));
	}

	List<ExecutionSemester> executionPeriodList = new ArrayList<ExecutionSemester>(selectedTSDProcess.getExecutionPeriods());
	Collections.sort(executionPeriodList, new BeanComparator("semester"));

	request.setAttribute("tsdCourseDTOEntryList", tsdCourseDTOEntryList);
	request.setAttribute("tsdTeacherDTOEntryList", tsdTeacherDTOEntryList);
	request.setAttribute("tsdProcessPhaseList", selectedTSDProcess.getOrderedPublishedTSDProcessPhases());
	request.setAttribute("tsdOptionEntryList", TeacherServiceDistributionDTOEntry
		.getTeacherServiceDistributionOptionEntries(selectedTSDProcessPhase));
	request.setAttribute("executionPeriodList", executionPeriodList);
	request.setAttribute("shiftsList", ShiftType.values());
	request.setAttribute("selectedShiftTypes", getSelectedShiftTypes(globalForm));

	if (viewType.equals(VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_COURSES)) {
	    return mapping.findForward("showGlobalTSDProcesssValuationByCourses");
	} else if (viewType.equals(VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_TEACHERS_AND_COURSES)) {
	    return mapping.findForward("showGlobalTSDProcesssValuationByTeachersAndCourses");
	} else if (viewType.equals(VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_TEACHERS)) {
	    return mapping.findForward("showGlobalTSDProcesssValuationByTeachers");
	} else {
	    return mapping.findForward("showGlobalTSDProcesssValuationByCourses");
	}

    }

    @SuppressWarnings("unchecked")
    private List<TSDProcess> selectOnlyTSDProcesssWithPublishedPhases(List<TSDProcess> tsdProcessList) {
	return (List<TSDProcess>) CollectionUtils.select(tsdProcessList, new Predicate() {

	    public boolean evaluate(Object arg0) {
		TSDProcess tsdProcess = (TSDProcess) arg0;
		return !tsdProcess.getOrderedPublishedTSDProcessPhases().isEmpty();
	    }

	});
    }

    @SuppressWarnings("unchecked")
    private List<TSDTeacherDTOEntry> getTSDTeacherDTOEntries(IUserView userView, TSDProcessPhase selectedTSDProcessPhase,
	    TeacherServiceDistribution selectedTeacherServiceDistribution, ExecutionSemester executionSemester)
	    throws FenixFilterException, FenixServiceException {
	Map<Integer, Pair<Integer, Integer>> tsdProcessIdMap = new HashMap<Integer, Pair<Integer, Integer>>();
	tsdProcessIdMap.put(selectedTSDProcessPhase.getIdInternal(), new Pair<Integer, Integer>(
		selectedTeacherServiceDistribution.getIdInternal(), (executionSemester == null) ? 0 : executionSemester
			.getIdInternal()));

	return (List<TSDTeacherDTOEntry>) ServiceUtils.executeService("ReadTSDTeachersFromTSDProcesses",
		new Object[] { tsdProcessIdMap });
    }

    @SuppressWarnings("unchecked")
    private List<TSDCourseDTOEntry> getTSDCourseDTOEntries(IUserView userView, TSDProcessPhase selectedTSDProcessPhase,
	    TeacherServiceDistribution selectedTeacherServiceDistribution, ExecutionSemester executionSemester)
	    throws FenixFilterException, FenixServiceException {
	Map<Integer, Pair<Integer, Integer>> tsdProcessIdMap = new HashMap<Integer, Pair<Integer, Integer>>();
	tsdProcessIdMap.put(selectedTSDProcessPhase.getIdInternal(), new Pair<Integer, Integer>(
		selectedTeacherServiceDistribution.getIdInternal(), (executionSemester == null) ? 0 : executionSemester
			.getIdInternal()));

	return (List<TSDCourseDTOEntry>) ServiceUtils.executeService("ReadTSDCoursesFromTSDProcesses",
		new Object[] { tsdProcessIdMap });
    }

    private TeacherServiceDistribution getSelectedTeacherServiceDistribution(DynaActionForm dynaForm)
	    throws FenixFilterException, FenixServiceException {
	Integer selectedTeacherServiceDistributionId = (Integer) dynaForm.get("tsd");
	return rootDomainObject.readTeacherServiceDistributionByOID(selectedTeacherServiceDistributionId);
    }

    private TSDProcessPhase getSelectedTSDProcessPhase(DynaActionForm dynaForm) throws FenixFilterException,
	    FenixServiceException {
	Integer selectedTSDProcessPhaseId = (Integer) dynaForm.get("tsdProcessPhase");
	TSDProcessPhase selectedTSDProcessPhase = rootDomainObject.readTSDProcessPhaseByOID(selectedTSDProcessPhaseId);

	return selectedTSDProcessPhase;
    }

    private TSDProcess getTSDProcess(DynaActionForm dynaForm) throws FenixServiceException, FenixFilterException {
	Integer tsdProcessId = (Integer) dynaForm.get("tsdProcess");
	TSDProcess tsdProcess = rootDomainObject.readTSDProcessByOID(tsdProcessId);

	return tsdProcess;
    }

    private List<ShiftType> getSelectedShiftTypes(DynaActionForm dynaForm) {
	List<ShiftType> typesList = new ArrayList<ShiftType>();
	String[] typesArray = (String[]) dynaForm.get("shiftTypeArray");

	for (String typeStr : typesArray) {
	    typesList.add(ShiftType.valueOf(typeStr));
	}

	return typesList;
    }

    private Integer getViewType(DynaActionForm dynaForm) {
	Integer viewType = (Integer) dynaForm.get("viewType");
	return viewType == null ? VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_TEACHERS : viewType;
    }

    private ExecutionYear getSelectedExecutionYear(IUserView userView, DynaActionForm globalForm) throws FenixServiceException,
	    FenixFilterException {
	Integer selectedExecutionYearId = (Integer) globalForm.get("executionYear");

	return rootDomainObject.readExecutionYearByOID(selectedExecutionYearId);
    }

    private ExecutionSemester getSelectedExecutionPeriod(DynaActionForm dynaForm) throws FenixServiceException,
	    FenixFilterException {
	Integer selectedExecutionPeriodId = (Integer) dynaForm.get("executionPeriod");
	return rootDomainObject.readExecutionSemesterByOID(selectedExecutionPeriodId);
    }

}
