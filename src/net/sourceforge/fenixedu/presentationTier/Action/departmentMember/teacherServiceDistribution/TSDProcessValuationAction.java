package net.sourceforge.fenixedu.presentationTier.Action.departmentMember.teacherServiceDistribution;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.TSDCourseDTOEntry;
import net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.TSDTeacherDTOEntry;
import net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.TeacherServiceDistributionDTOEntry;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhase;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.util.teacherServiceDistribution.report.TeacherServiceDistributionChart;
import net.sourceforge.fenixedu.util.teacherServiceDistribution.report.TeacherServiceDistributionSpreadsheet;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;
import pt.utl.ist.fenix.tools.util.Pair;

public class TSDProcessValuationAction extends FenixDispatchAction {

    private static final Integer VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_TEACHERS = 0;
    private static final Integer VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_COURSES = 1;
    private static final Integer VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_TEACHERS_AND_COURSES = 2;
    private static final Integer VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_CHARTS = 4;
    private static final Integer VIEW_TEACHER_SERVICE_DISTRIBUTION_PLANNING = 5;

    public ActionForward prepareForTSDProcessValuation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	DynaActionForm dynaForm = (DynaActionForm) form;

	getFromRequestAndSetOnFormTSDProcessId(request, dynaForm);
	initializeVariables(dynaForm);

	return loadTSDProcess(mapping, form, request, response);
    }

    public ActionForward loadTSDProcessPhase(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	IUserView userView = UserView.getUser();
	DynaActionForm dynaForm = (DynaActionForm) form;

	TSDProcessPhase selectedTSDProcessPhase = getSelectedTSDProcessPhase(userView, dynaForm);
	dynaForm.set("tsd", selectedTSDProcessPhase.getRootTSD().getIdInternal());

	return loadTSDProcess(mapping, form, request, response);
    }

    public ActionForward changeToViewCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	DynaActionForm dynaForm = (DynaActionForm) form;

	dynaForm.set("viewType", VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_COURSES);

	return loadTSDProcess(mapping, form, request, response);
    }

    public ActionForward changeToViewTeachers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	DynaActionForm dynaForm = (DynaActionForm) form;

	dynaForm.set("viewType", VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_TEACHERS);

	return loadTSDProcess(mapping, form, request, response);
    }

    public ActionForward changeToViewTeacherAndCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	DynaActionForm dynaForm = (DynaActionForm) form;
	dynaForm.set("viewType", VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_TEACHERS_AND_COURSES);

	return loadTSDProcess(mapping, form, request, response);
    }

    public ActionForward changeToViewCharts(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	DynaActionForm dynaForm = (DynaActionForm) form;
	dynaForm.set("viewType", VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_CHARTS);

	return loadTSDProcess(mapping, form, request, response);
    }

    public ActionForward changeToViewPlanning(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	DynaActionForm dynaForm = (DynaActionForm) form;
	dynaForm.set("viewType", VIEW_TEACHER_SERVICE_DISTRIBUTION_PLANNING);

	return loadTSDProcess(mapping, form, request, response);
    }

    @SuppressWarnings("unchecked")
    public ActionForward loadTSDProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	IUserView userView = UserView.getUser();
	DynaActionForm dynaForm = (DynaActionForm) form;

	TSDProcess tsdProcess = getTSDProcess(userView, dynaForm);
	TSDProcessPhase selectedTSDProcessPhase = getSelectedTSDProcessPhase(userView, dynaForm);

	List<ExecutionSemester> executionPeriodList = new ArrayList<ExecutionSemester>(tsdProcess.getExecutionPeriods());
	Collections.sort(executionPeriodList, new BeanComparator("semester"));
	ExecutionSemester selectedExecutionPeriod = getSelectedExecutionPeriod(userView, dynaForm, null);
	List<ExecutionSemester> selectedExecutionPeriodList = new ArrayList<ExecutionSemester>();
	if (selectedExecutionPeriod == null) {
	    selectedExecutionPeriodList.addAll(executionPeriodList);
	} else {
	    selectedExecutionPeriodList.add(selectedExecutionPeriod);
	}

	if (selectedTSDProcessPhase == null) {
	    selectedTSDProcessPhase = tsdProcess.getCurrentTSDProcessPhase();
	    dynaForm.set("tsdProcessPhase", selectedTSDProcessPhase.getIdInternal());
	}
	TeacherServiceDistribution rootTeacherServiceDistribution = selectedTSDProcessPhase.getRootTSD();
	TeacherServiceDistribution selectedTSD = getSelectedTeacherServiceDistribution(userView, dynaForm,
		rootTeacherServiceDistribution);

	List<TSDCourseDTOEntry> tsdCourseDTOEntryList = null;
	List<TSDTeacherDTOEntry> tsdTeacherDTOEntryList = null;
	Integer viewType = getViewType(dynaForm);

	if (viewType.equals(VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_COURSES)) {
	    tsdCourseDTOEntryList = getTSDCourseDTOEntries(userView, selectedTSDProcessPhase, selectedTSD,
		    selectedExecutionPeriod);
	    List<TSDCourse> nonValuatedTSDCourses = new ArrayList(selectedTSD
		    .getCompetenceCoursesWithoutActiveTSDCourses(selectedExecutionPeriodList));
	    if (!nonValuatedTSDCourses.isEmpty()) {
		Collections.sort(nonValuatedTSDCourses, new BeanComparator("name"));
		request.setAttribute("nonValuatedTSDCourses", nonValuatedTSDCourses);
	    }
	} else if (viewType.equals(VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_TEACHERS_AND_COURSES)) {
	    tsdCourseDTOEntryList = getTSDCourseDTOEntries(userView, selectedTSDProcessPhase, selectedTSD,
		    selectedExecutionPeriod);
	    tsdTeacherDTOEntryList = getTSDTeacherDTOEntries(userView, selectedTSDProcessPhase, selectedTSD,
		    selectedExecutionPeriod);
	} else if (viewType.equals(VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_TEACHERS)) {
	    tsdTeacherDTOEntryList = getTSDTeacherDTOEntries(userView, selectedTSDProcessPhase, selectedTSD,
		    selectedExecutionPeriod);
	} else if (viewType.equals(VIEW_TEACHER_SERVICE_DISTRIBUTION_PLANNING)) {
	    tsdCourseDTOEntryList = getTSDCourseDTOEntries(userView, selectedTSDProcessPhase, selectedTSD,
		    selectedExecutionPeriod);
	    request.setAttribute("shifts", ShiftType.values());
	}

	String comparatorStr = "name";
	if (viewType.equals(VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_TEACHERS_AND_COURSES)) {
	    comparatorStr = "acronym";
	}

	if (tsdCourseDTOEntryList != null) {
	    Collections.sort(tsdCourseDTOEntryList, new BeanComparator("TSDCourse." + comparatorStr));
	}
	if (tsdTeacherDTOEntryList != null) {
	    Collections.sort(tsdTeacherDTOEntryList, new BeanComparator(comparatorStr));
	}

	request.setAttribute("tsdCourseDTOEntryList", tsdCourseDTOEntryList);
	request.setAttribute("tsdTeacherDTOEntryList", tsdTeacherDTOEntryList);
	request.setAttribute("tsdProcess", tsdProcess);
	request.setAttribute("tsdProcessPhaseList", tsdProcess.getOrderedTSDProcessPhases());
	request.setAttribute("tsdOptionEntryList", TeacherServiceDistributionDTOEntry
		.getTeacherServiceDistributionOptionEntries(selectedTSDProcessPhase));
	request.setAttribute("executionPeriodList", executionPeriodList);
	request.setAttribute("selectedExecutionPeriodList", selectedExecutionPeriodList);
	request.setAttribute("shiftsList", ShiftType.values());
	request.setAttribute("selectedShiftTypes", getSelectedShiftTypes(dynaForm));
	request.setAttribute("selectedTSD", selectedTSD);

	setInformationTableParameters(dynaForm, request);

	if (viewType.equals(VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_COURSES)) {
	    return mapping.findForward("showTSDProcessValuation");
	} else if (viewType.equals(VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_TEACHERS_AND_COURSES)) {
	    return mapping.findForward("showTSDProcessValuationByTeachersAndCourses");
	} else if (viewType.equals(VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_TEACHERS)) {
	    return mapping.findForward("showTSDProcessValuationByTeachers");
	} else if (viewType.equals(VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_CHARTS)) {
	    return mapping.findForward("showTSDProcessValuationByCharts");
	} else if (viewType.equals(VIEW_TEACHER_SERVICE_DISTRIBUTION_PLANNING)) {
	    return mapping.findForward("showPlanning");
	} else {
	    return mapping.findForward("showTSDProcessValuation");
	}
    }

    public ActionForward exportTSDProcessValuationToExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	IUserView userView = UserView.getUser();
	DynaActionForm dynaForm = (DynaActionForm) form;

	TSDProcess tsdProcess = getTSDProcess(userView, dynaForm);
	TSDProcessPhase selectedTSDProcessPhase = getSelectedTSDProcessPhase(userView, dynaForm);

	List<ExecutionSemester> executionPeriodList = new ArrayList<ExecutionSemester>(tsdProcess.getExecutionPeriods());
	Collections.sort(executionPeriodList, new BeanComparator("semester"));
	ExecutionSemester selectedExecutionPeriod = getSelectedExecutionPeriod(userView, dynaForm, null);
	List<ExecutionSemester> selectedExecutionPeriodList = new ArrayList<ExecutionSemester>();
	if (selectedExecutionPeriod == null) {
	    selectedExecutionPeriodList.addAll(executionPeriodList);
	} else {
	    selectedExecutionPeriodList.add(selectedExecutionPeriod);
	}

	TeacherServiceDistribution selectedTeacherServiceDistribution = getSelectedTeacherServiceDistribution(userView, dynaForm,
		selectedTSDProcessPhase.getRootTSD());

	List<TSDCourseDTOEntry> tsdCourseDTOEntryList = getTSDCourseDTOEntries(userView, selectedTSDProcessPhase,
		selectedTeacherServiceDistribution, selectedExecutionPeriod);
	List<TSDTeacherDTOEntry> tsdTeacherDTOEntryList = getTSDTeacherDTOEntries(userView, selectedTSDProcessPhase,
		selectedTeacherServiceDistribution, selectedExecutionPeriod);

	try {
	    String filename = tsdProcess.getName() + "-" + tsdProcess.getExecutionYear().getYear() + "-"
		    + selectedTSDProcessPhase.getName();

	    if (selectedTeacherServiceDistribution != null) {
		filename += "-" + selectedTeacherServiceDistribution.getName();
	    }

	    response.setContentType("application/vnd.ms-excel");
	    response.setHeader("Content-disposition", "attachment; filename=" + filename.replace(" ", "") + ".xls");

	    ServletOutputStream writer = response.getOutputStream();

	    exportToXls(tsdCourseDTOEntryList, tsdTeacherDTOEntryList, filename, writer);

	    writer.flush();
	    response.flushBuffer();
	} catch (IOException e) {
	    throw new FenixServiceException();
	}

	return null;
    }

    private ActionForward generateTSDProcessValuationChart(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response, TeacherServiceDistributionChart tsdProcessChart) throws FenixFilterException,
	    FenixServiceException {

	Integer tsdId = new Integer(request.getParameter("tsd"));
	TeacherServiceDistribution tsd = rootDomainObject.readTeacherServiceDistributionByOID(tsdId);

	Integer executionPeriodId = new Integer(request.getParameter("executionPeriod"));
	ExecutionSemester selectedExecutionPeriod = rootDomainObject.readExecutionSemesterByOID(executionPeriodId);

	List<ExecutionSemester> executionPeriodList = new ArrayList<ExecutionSemester>();
	if (selectedExecutionPeriod != null) {
	    executionPeriodList.add(selectedExecutionPeriod);
	} else {
	    executionPeriodList.addAll(tsd.getTSDProcessPhase().getTSDProcess().getExecutionPeriods());
	}

	try {
	    response.setContentType("image/png");
	    ServletOutputStream writer = response.getOutputStream();

	    tsdProcessChart.setTeacherServiceDistribution(tsd);
	    tsdProcessChart.setExecutionPeriodList(executionPeriodList);

	    tsdProcessChart.execute(writer);

	    writer.flush();
	    response.flushBuffer();
	} catch (IOException e) {
	    throw new FenixServiceException();
	}

	return null;
    }

    public ActionForward generateValuatedHoursPerGrouping(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	return generateTSDProcessValuationChart(mapping, form, request, response, TeacherServiceDistributionChart
		.generateValuatedHoursPerGroupingChart());
    }

    public ActionForward generateValuatedNumberStudentsPerGrouping(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	return generateTSDProcessValuationChart(mapping, form, request, response, TeacherServiceDistributionChart
		.generateValuatedNumberStudentsPerGroupingChart());
    }

    public ActionForward generateValuatedHoursPerGroupingPieChart(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	return generateTSDProcessValuationChart(mapping, form, request, response, TeacherServiceDistributionChart
		.generateValuatedHoursPerGroupingPieChart());
    }

    public ActionForward generateValuatedNumberStudentsPerGroupingPieChart(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	return generateTSDProcessValuationChart(mapping, form, request, response, TeacherServiceDistributionChart
		.generateValuatedNumberStudentsPerGroupingPieChart());
    }

    private void exportToXls(final List<TSDCourseDTOEntry> tsdCourseDTOEntryList,
	    final List<TSDTeacherDTOEntry> tsdTeacherDTOEntryList, final String name, OutputStream outputStream)
	    throws IOException {
	final TeacherServiceDistributionSpreadsheet spreadsheet = new TeacherServiceDistributionSpreadsheet(
		tsdCourseDTOEntryList, tsdTeacherDTOEntryList, name);

	spreadsheet.exportToXLSSheet(outputStream);
    }

    private TeacherServiceDistribution getSelectedTeacherServiceDistribution(IUserView userView, DynaActionForm dynaForm,
	    TeacherServiceDistribution rootTeacherServiceDistribution) throws FenixFilterException, FenixServiceException {
	Integer selectedTeacherServiceDistributionId = (Integer) dynaForm.get("tsd");
	TeacherServiceDistribution selectedTeacherServiceDistribution = rootDomainObject
		.readTeacherServiceDistributionByOID(selectedTeacherServiceDistributionId);

	if (selectedTeacherServiceDistribution == null)
	    return rootTeacherServiceDistribution;

	return selectedTeacherServiceDistribution;
    }

    private TSDProcessPhase getSelectedTSDProcessPhase(IUserView userView, DynaActionForm dynaForm) throws FenixFilterException,
	    FenixServiceException {
	Integer selectedTSDProcessPhaseId = (Integer) dynaForm.get("tsdProcessPhase");
	TSDProcessPhase selectedTSDProcessPhase = rootDomainObject.readTSDProcessPhaseByOID(selectedTSDProcessPhaseId);

	return selectedTSDProcessPhase;
    }

    private TSDProcess getTSDProcess(IUserView userView, DynaActionForm dynaForm) throws FenixServiceException,
	    FenixFilterException {
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
	return viewType;
    }

    private Integer getFromRequestAndSetOnFormTSDProcessId(HttpServletRequest request, DynaActionForm dynaForm) {
	Integer tsdProcessId = new Integer(request.getParameter("tsdProcess"));
	dynaForm.set("tsdProcess", tsdProcessId);
	return tsdProcessId;
    }

    private void initializeVariables(DynaActionForm dynaForm) {
	dynaForm.set("tsd", null);
	dynaForm.set("tsdProcessPhase", null);
	dynaForm.set("viewType", VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_COURSES);
	dynaForm.set("viewCurricularInformation", false);
	dynaForm.set("viewStudentsEnrolments", false);
	dynaForm.set("viewShiftHours", false);
	dynaForm.set("viewStudentsEnrolmentsPerShift", false);
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

    private void setInformationTableParameters(DynaActionForm dynaForm, HttpServletRequest request) {
	Boolean viewCurricularInformation = (Boolean) dynaForm.get("viewCurricularInformation");
	Boolean viewStudentsEnrolments = (Boolean) dynaForm.get("viewStudentsEnrolments");
	Boolean viewShiftHours = (Boolean) dynaForm.get("viewShiftHours");
	Boolean viewStudentsEnrolmentsPerShift = (Boolean) dynaForm.get("viewStudentsEnrolmentsPerShift");

	request.setAttribute("viewCurricularInformation", viewCurricularInformation);
	request.setAttribute("viewStudentsEnrolments", viewStudentsEnrolments);
	request.setAttribute("viewShiftHours", viewShiftHours);
	request.setAttribute("viewStudentsEnrolmentsPerShift", viewStudentsEnrolmentsPerShift);
    }

    private ExecutionSemester getSelectedExecutionPeriod(IUserView userView, DynaActionForm dynaForm,
	    List<ExecutionSemester> executionPeriodList) throws FenixServiceException, FenixFilterException {
	Integer selectedExecutionPeriodId = (Integer) dynaForm.get("executionPeriod");
	ExecutionSemester selectedExecutionPeriod = rootDomainObject.readExecutionSemesterByOID(selectedExecutionPeriodId);

	if (selectedExecutionPeriod == null) {
	    if (executionPeriodList != null && executionPeriodList.size() > 0) {
		return executionPeriodList.get(0);
	    } else {
		return null;
	    }
	}

	return selectedExecutionPeriod;
    }

    public ActionForward prepareForTSDProcessValuationByTeachers(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	DynaActionForm dynaForm = (DynaActionForm) form;

	getFromRequestAndSetOnFormTSDProcessId(request, dynaForm);
	initializeVariables(dynaForm);

	return changeToViewTeachers(mapping, form, request, response);
    }
}
