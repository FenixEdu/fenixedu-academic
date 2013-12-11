package net.sourceforge.fenixedu.presentationTier.Action.departmentMember.teacherServiceDistribution;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution.ReadTSDCoursesFromTSDProcesses;
import net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution.ReadTSDTeachersFromTSDProcesses;
import net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.TSDCourseDTOEntry;
import net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.TSDTeacherDTOEntry;
import net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.TeacherServiceDistributionDTOEntry;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhase;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.teacherServiceDistribution.report.TeacherServiceDistributionChart;
import net.sourceforge.fenixedu.util.teacherServiceDistribution.report.TeacherServiceDistributionSpreadsheet;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.bennu.core.security.Authenticate;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.Pair;
import pt.utl.ist.fenix.tools.util.excel.StyledExcelSpreadsheet;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class TSDProcessValuationAction extends FenixDispatchAction {

    private static final Integer VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_TEACHERS = 0;
    private static final Integer VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_COURSES = 1;
    private static final Integer VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_TEACHERS_AND_COURSES = 2;
    private static final Integer VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_CHARTS = 4;
    private static final Integer VIEW_TEACHER_SERVICE_DISTRIBUTION_PLANNING = 5;

    public ActionForward prepareForTSDProcessValuation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        DynaActionForm dynaForm = (DynaActionForm) form;

        getFromRequestAndSetOnFormTSDProcessId(request, dynaForm);
        initializeVariables(dynaForm);

        return loadTSDProcess(mapping, form, request, response);
    }

    public ActionForward loadTSDProcessPhase(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        User userView = Authenticate.getUser();
        DynaActionForm dynaForm = (DynaActionForm) form;

        TSDProcessPhase selectedTSDProcessPhase = getSelectedTSDProcessPhase(userView, dynaForm);
        dynaForm.set("tsd", selectedTSDProcessPhase.getRootTSD().getExternalId());

        return loadTSDProcess(mapping, form, request, response);
    }

    public ActionForward changeToViewCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        DynaActionForm dynaForm = (DynaActionForm) form;

        dynaForm.set("viewType", VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_COURSES);

        return loadTSDProcess(mapping, form, request, response);
    }

    public ActionForward changeToViewTeachers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        DynaActionForm dynaForm = (DynaActionForm) form;

        dynaForm.set("viewType", VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_TEACHERS);

        return loadTSDProcess(mapping, form, request, response);
    }

    public ActionForward changeToViewTeacherAndCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        DynaActionForm dynaForm = (DynaActionForm) form;
        dynaForm.set("viewType", VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_TEACHERS_AND_COURSES);

        return loadTSDProcess(mapping, form, request, response);
    }

    public ActionForward changeToViewCharts(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        DynaActionForm dynaForm = (DynaActionForm) form;
        dynaForm.set("viewType", VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_CHARTS);

        return loadTSDProcess(mapping, form, request, response);
    }

    public ActionForward changeToViewPlanning(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        DynaActionForm dynaForm = (DynaActionForm) form;
        dynaForm.set("viewType", VIEW_TEACHER_SERVICE_DISTRIBUTION_PLANNING);

        return loadTSDProcess(mapping, form, request, response);
    }

    @SuppressWarnings("unchecked")
    public ActionForward loadTSDProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        User userView = Authenticate.getUser();
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
            dynaForm.set("tsdProcessPhase", selectedTSDProcessPhase.getExternalId());
        }
        TeacherServiceDistribution rootTeacherServiceDistribution = selectedTSDProcessPhase.getRootTSD();
        TeacherServiceDistribution selectedTSD =
                getSelectedTeacherServiceDistribution(userView, dynaForm, rootTeacherServiceDistribution);

        List<TSDCourseDTOEntry> tsdCourseDTOEntryList = null;
        List<TSDTeacherDTOEntry> tsdTeacherDTOEntryList = null;
        Integer viewType = getViewType(dynaForm);

        if (viewType.equals(VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_COURSES)) {
            tsdCourseDTOEntryList =
                    getTSDCourseDTOEntries(userView, selectedTSDProcessPhase, selectedTSD, selectedExecutionPeriod);
            List<TSDCourse> nonValuatedTSDCourses =
                    new ArrayList(selectedTSD.getCompetenceCoursesWithoutActiveTSDCourses(selectedExecutionPeriodList));
            if (!nonValuatedTSDCourses.isEmpty()) {
                Collections.sort(nonValuatedTSDCourses, new BeanComparator("name"));
                request.setAttribute("nonValuatedTSDCourses", nonValuatedTSDCourses);
            }
        } else if (viewType.equals(VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_TEACHERS_AND_COURSES)) {
            tsdCourseDTOEntryList =
                    getTSDCourseDTOEntries(userView, selectedTSDProcessPhase, selectedTSD, selectedExecutionPeriod);
            tsdTeacherDTOEntryList =
                    getTSDTeacherDTOEntries(userView, selectedTSDProcessPhase, selectedTSD, selectedExecutionPeriod);
        } else if (viewType.equals(VIEW_TEACHER_SERVICE_DISTRIBUTION_VALUATION_BY_TEACHERS)) {
            tsdTeacherDTOEntryList =
                    getTSDTeacherDTOEntries(userView, selectedTSDProcessPhase, selectedTSD, selectedExecutionPeriod);
        } else if (viewType.equals(VIEW_TEACHER_SERVICE_DISTRIBUTION_PLANNING)) {
            tsdCourseDTOEntryList =
                    getTSDCourseDTOEntries(userView, selectedTSDProcessPhase, selectedTSD, selectedExecutionPeriod);
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
        request.setAttribute("tsdOptionEntryList",
                TeacherServiceDistributionDTOEntry.getTeacherServiceDistributionOptionEntries(selectedTSDProcessPhase));
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

    public ActionForward exportTeacherServiceDistributionPlanningToExcel(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {

        User userView = Authenticate.getUser();
        DynaActionForm dynaForm = (DynaActionForm) form;

        TSDProcess tsdProcess = getTSDProcess(userView, dynaForm);
        TSDProcessPhase selectedTSDProcessPhase = getSelectedTSDProcessPhase(userView, dynaForm);

        List<ShiftType> selectedShiftTypes = getSelectedShiftTypes(dynaForm);
        List<ExecutionSemester> executionPeriodList = new ArrayList<ExecutionSemester>(tsdProcess.getExecutionPeriods());
        ExecutionSemester selectedExecutionPeriod = getSelectedExecutionPeriod(userView, dynaForm, null);
        List<ExecutionSemester> selectedExecutionPeriodList = new ArrayList<ExecutionSemester>();
        if (selectedExecutionPeriod == null) {
            selectedExecutionPeriodList.addAll(executionPeriodList);
        } else {
            selectedExecutionPeriodList.add(selectedExecutionPeriod);
        }

        if (selectedTSDProcessPhase == null) {
            selectedTSDProcessPhase = tsdProcess.getCurrentTSDProcessPhase();
            dynaForm.set("tsdProcessPhase", selectedTSDProcessPhase.getExternalId());
        }
        TeacherServiceDistribution rootTeacherServiceDistribution = selectedTSDProcessPhase.getRootTSD();
        TeacherServiceDistribution selectedTSD =
                getSelectedTeacherServiceDistribution(userView, dynaForm, rootTeacherServiceDistribution);

        List<TSDCourseDTOEntry> tsdCourseDTOEntryList =
                getTSDCourseDTOEntries(userView, selectedTSDProcessPhase, selectedTSD, selectedExecutionPeriod);

        Collections.sort(tsdCourseDTOEntryList, new BeanComparator("TSDCourse.name"));
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment; filename=" + tsdProcess.getName().replace(" ", "") + ".xls");

            ServletOutputStream writer = response.getOutputStream();

            export(writer, tsdCourseDTOEntryList, selectedShiftTypes, tsdProcess.getName());
            writer.flush();
            response.flushBuffer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    private void export(ServletOutputStream writer, List<TSDCourseDTOEntry> entries, List<ShiftType> selectedShiftType,
            String name) throws IOException {

        StyledExcelSpreadsheet spreadsheet = new StyledExcelSpreadsheet(name, false);
        ResourceBundle bundle = ResourceBundle.getBundle("resources.DepartmentAdmOfficeResources", Language.getLocale());

        spreadsheet.newHeaderRow();
        spreadsheet.addHeader(bundle.getString("label.teacherService.course.name"), 10000);
        spreadsheet.addHeader(bundle.getString("label.teacherServiceDistribution.acronym"));
        spreadsheet.addHeader(bundle.getString("label.teacherServiceDistribution.courses"), 5000);
        spreadsheet.addHeader(bundle.getString("label.teacherService.course.semester"));

        spreadsheet.mergeCells(0, 1, 0, 0);
        spreadsheet.mergeCells(0, 1, 1, 1);
        spreadsheet.mergeCells(0, 1, 2, 2);
        spreadsheet.mergeCells(0, 1, 3, 3);

        if (!selectedShiftType.isEmpty()) {
            int i = 0;
            spreadsheet.newHeaderRow();

            for (ShiftType shiftType : selectedShiftType) {
                int colNumber = 4 + (4 * i);
                spreadsheet.addHeader(0, colNumber, shiftType.getFullNameTipoAula());
                spreadsheet.addHeader(1, colNumber,
                        bundle.getString("label.teacherServiceDistribution.numberOfSchoolClasses.acronym"));
                spreadsheet.addHeader(1, colNumber + 1,
                        bundle.getString("label.teacherServiceDistribution.shiftDuration.acronym"));
                spreadsheet.addHeader(1, colNumber + 2,
                        bundle.getString("label.teacherServiceDistribution.shiftFrequency.acronym"));
                spreadsheet.addHeader(1, colNumber + 3,
                        bundle.getString("label.teacherServiceDistribution.numberOfShifts.acronym"));

                spreadsheet.mergeCells(0, 0, colNumber, colNumber + 3);
                i++;
            }
        }
        for (TSDCourseDTOEntry entry : entries) {

            spreadsheet.newRow();
            TSDCourse tsdCourse = entry.getTSDCourse();
            spreadsheet.addCell(tsdCourse.getName());
            spreadsheet.addCell(tsdCourse.getAcronym());
            StringBuilder buffer = new StringBuilder("");
            for (Degree degree : tsdCourse.getDegrees()) {
                if (buffer.length() > 0) {
                    buffer.append(", ");
                }
                buffer.append(degree.getSigla());
            }
            spreadsheet.addCell(buffer.toString());
            spreadsheet.addCell(tsdCourse.getExecutionPeriod().getSemester());
            for (ShiftType shiftType : selectedShiftType) {
                spreadsheet.addCell(tsdCourse.getNumberOfSchoolClasses(shiftType));
                spreadsheet.addCell(tsdCourse.getHoursPerShift(shiftType));
                spreadsheet.addCell(tsdCourse.getShiftFrequency(shiftType));
                spreadsheet.addCell(tsdCourse.getNumberOfShifts(shiftType));
            }
        }

        spreadsheet.getWorkbook().write(writer);
    }

    public ActionForward exportTSDProcessValuationToExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        User userView = Authenticate.getUser();
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

        TeacherServiceDistribution selectedTeacherServiceDistribution =
                getSelectedTeacherServiceDistribution(userView, dynaForm, selectedTSDProcessPhase.getRootTSD());

        List<TSDCourseDTOEntry> tsdCourseDTOEntryList =
                getTSDCourseDTOEntries(userView, selectedTSDProcessPhase, selectedTeacherServiceDistribution,
                        selectedExecutionPeriod);
        List<TSDTeacherDTOEntry> tsdTeacherDTOEntryList =
                getTSDTeacherDTOEntries(userView, selectedTSDProcessPhase, selectedTeacherServiceDistribution,
                        selectedExecutionPeriod);

        try {
            String filename =
                    tsdProcess.getName() + "-" + tsdProcess.getExecutionYear().getYear() + "-"
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
            HttpServletResponse response, TeacherServiceDistributionChart tsdProcessChart) throws FenixServiceException {

        String tsdId = request.getParameter("tsd");
        TeacherServiceDistribution tsd = FenixFramework.getDomainObject(tsdId);

        String executionPeriodId = request.getParameter("executionPeriod");
        ExecutionSemester selectedExecutionPeriod = FenixFramework.getDomainObject(executionPeriodId);

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
            HttpServletResponse response) throws FenixServiceException {

        return generateTSDProcessValuationChart(mapping, form, request, response,
                TeacherServiceDistributionChart.generateValuatedHoursPerGroupingChart());
    }

    public ActionForward generateValuatedNumberStudentsPerGrouping(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {
        return generateTSDProcessValuationChart(mapping, form, request, response,
                TeacherServiceDistributionChart.generateValuatedNumberStudentsPerGroupingChart());
    }

    public ActionForward generateValuatedHoursPerGroupingPieChart(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {
        return generateTSDProcessValuationChart(mapping, form, request, response,
                TeacherServiceDistributionChart.generateValuatedHoursPerGroupingPieChart());
    }

    public ActionForward generateValuatedNumberStudentsPerGroupingPieChart(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {
        return generateTSDProcessValuationChart(mapping, form, request, response,
                TeacherServiceDistributionChart.generateValuatedNumberStudentsPerGroupingPieChart());
    }

    private void exportToXls(final List<TSDCourseDTOEntry> tsdCourseDTOEntryList,
            final List<TSDTeacherDTOEntry> tsdTeacherDTOEntryList, final String name, OutputStream outputStream)
            throws IOException {
        final TeacherServiceDistributionSpreadsheet spreadsheet =
                new TeacherServiceDistributionSpreadsheet(tsdCourseDTOEntryList, tsdTeacherDTOEntryList, name);

        spreadsheet.exportToXLSSheet(outputStream);
    }

    private TeacherServiceDistribution getSelectedTeacherServiceDistribution(User userView, DynaActionForm dynaForm,
            TeacherServiceDistribution rootTeacherServiceDistribution) {
        TeacherServiceDistribution selectedTeacherServiceDistribution = getDomainObject(dynaForm, "tsd");

        if (selectedTeacherServiceDistribution == null) {
            return rootTeacherServiceDistribution;
        }

        return selectedTeacherServiceDistribution;
    }

    private TSDProcessPhase getSelectedTSDProcessPhase(User userView, DynaActionForm dynaForm) {
        return getDomainObject(dynaForm, "tsdProcessPhase");
    }

    private TSDProcess getTSDProcess(User userView, DynaActionForm dynaForm) {
        return getDomainObject(dynaForm, "tsdProcess");
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

    private String getFromRequestAndSetOnFormTSDProcessId(HttpServletRequest request, DynaActionForm dynaForm) {
        String tsdProcessId = request.getParameter("tsdProcess");
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

    private List<TSDTeacherDTOEntry> getTSDTeacherDTOEntries(User userView, TSDProcessPhase selectedTSDProcessPhase,
            TeacherServiceDistribution selectedTeacherServiceDistribution, ExecutionSemester executionSemester)
            throws FenixServiceException {
        Map<String, Pair<String, String>> tsdProcessIdMap = new HashMap<String, Pair<String, String>>();
        tsdProcessIdMap.put(selectedTSDProcessPhase.getExternalId(),
                new Pair<String, String>(selectedTeacherServiceDistribution.getExternalId(),
                        (executionSemester == null) ? null : executionSemester.getExternalId()));

        return ReadTSDTeachersFromTSDProcesses.runReadTSDTeachersFromTSDProcesses(tsdProcessIdMap);
    }

    private List<TSDCourseDTOEntry> getTSDCourseDTOEntries(User userView, TSDProcessPhase selectedTSDProcessPhase,
            TeacherServiceDistribution selectedTeacherServiceDistribution, ExecutionSemester executionSemester)
            throws FenixServiceException {
        Map<String, Pair<String, String>> tsdProcessIdMap = new HashMap<String, Pair<String, String>>();
        tsdProcessIdMap.put(selectedTSDProcessPhase.getExternalId(),
                new Pair<String, String>(selectedTeacherServiceDistribution.getExternalId(),
                        (executionSemester == null) ? null : executionSemester.getExternalId()));

        return ReadTSDCoursesFromTSDProcesses.runReadTSDCoursesFromTSDProcesses(tsdProcessIdMap);
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

    private ExecutionSemester getSelectedExecutionPeriod(User userView, DynaActionForm dynaForm,
            List<ExecutionSemester> executionPeriodList) {
        ExecutionSemester selectedExecutionPeriod = getDomainObject(dynaForm, "executionPeriod");

        if (selectedExecutionPeriod == null) {
            if (executionPeriodList != null && executionPeriodList.size() > 0) {
                return executionPeriodList.iterator().next();
            } else {
                return null;
            }
        }

        return selectedExecutionPeriod;
    }

    public ActionForward prepareForTSDProcessValuationByTeachers(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {

        DynaActionForm dynaForm = (DynaActionForm) form;

        getFromRequestAndSetOnFormTSDProcessId(request, dynaForm);
        initializeVariables(dynaForm);

        return changeToViewTeachers(mapping, form, request, response);
    }
}
