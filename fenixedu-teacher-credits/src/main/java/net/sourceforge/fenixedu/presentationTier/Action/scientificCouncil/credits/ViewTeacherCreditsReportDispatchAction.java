/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * Jan 23, 2006
 */
package org.fenixedu.academic.ui.struts.action.scientificCouncil.credits;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.scientificCouncil.credits.ReadDepartmentTotalCreditsByPeriod;
import org.fenixedu.academic.service.services.scientificCouncil.credits.ReadDepartmentTotalCreditsByPeriod.PeriodCreditsReportDTO;
import org.fenixedu.academic.service.services.scientificCouncil.credits.ReadTeachersCreditsResumeByPeriodAndUnit;
import org.fenixedu.academic.service.services.scientificCouncil.credits.ReadTeachersCreditsResumeByPeriodAndUnit.TeacherCreditsReportDTO;
import net.sourceforge.fenixedu.commons.OrderedIterator;
import org.fenixedu.academic.dto.GenericPair;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.academic.ui.struts.action.scientificCouncil.ScientificCouncilApplication.ScientificCreditsApp;
import org.fenixedu.academic.util.NumberUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.util.LabelValueBean;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.ExceptionHandling;
import org.fenixedu.bennu.struts.annotations.Exceptions;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.spreadsheet.Formula;
import pt.utl.ist.fenix.tools.spreadsheet.SheetData;
import pt.utl.ist.fenix.tools.spreadsheet.SpreadsheetBuilder;
import pt.utl.ist.fenix.tools.spreadsheet.WorkbookExportFormat;
import pt.utl.ist.fenix.tools.util.excel.ExcelStyle;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

/**
 * @author Ricardo Rodrigues
 * 
 */
@StrutsFunctionality(app = ScientificCreditsApp.class, path = "view-report", titleKey = "link.credits.viewReport")
@Mapping(module = "scientificCouncil", path = "/creditsReport", formBean = "creditsReportForm")
@Forwards({
        @Forward(name = "showCreditsReport", path = "/scientificCouncil/credits/showTeachersCreditsReport.jsp"),
        @Forward(name = "showDepartmentCreditsReport", path = "/scientificCouncil/credits/showDepartmentGlobalCreditsReport.jsp"),
        @Forward(name = "prepare", path = "/scientificCouncil/credits/selectReportParameters.jsp") })
@Exceptions(@ExceptionHandling(type = ViewTeacherCreditsReportDispatchAction.InvalidPeriodException.class,
        key = "error.credits.chooseExecutionPeriods", handler = ExceptionHandler.class,
        path = "/creditsReport.do?method=prepare", scope = "request"))
public class ViewTeacherCreditsReportDispatchAction extends FenixDispatchAction {

    private static final Logger logger = LoggerFactory.getLogger(ViewTeacherCreditsReportDispatchAction.class);

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixServiceException {

        Collection<ExecutionYear> executionYears = rootDomainObject.getExecutionYearsSet();
        List filteredExecutionYears = filterExecutionYears(executionYears);

        Collection<Department> departments = rootDomainObject.getDepartmentsSet();
        Iterator departmentOrderedIterator = new OrderedIterator(departments.iterator(), new BeanComparator("name"));

        request.setAttribute("executionYears", filteredExecutionYears);
        request.setAttribute("departments", departmentOrderedIterator);
        return mapping.findForward("prepare");
    }

    public ActionForward viewDetailedCreditsReport(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, InvalidPeriodException {

        User userView = Authenticate.getUser();
        DynaActionForm dynaForm = (DynaActionForm) form;

        String fromExecutionYearID = dynaForm.getString("fromExecutionYearID");
        String untilExecutionYearID = dynaForm.getString("untilExecutionYearID");
        String departmentID = (String) dynaForm.get("departmentID");

        getDetailedTeachersCreditsMap(request, userView, fromExecutionYearID, untilExecutionYearID, departmentID);
        return mapping.findForward("showCreditsReport");
    }

    private Map<Department, Map<Unit, List>> getDetailedTeachersCreditsMap(HttpServletRequest request, User userView,
            String fromExecutionYearID, String untilExecutionYearID, String departmentID) throws InvalidPeriodException,
            FenixServiceException {

        request.setAttribute("fromExecutionYearID", fromExecutionYearID);
        request.setAttribute("untilExecutionYearID", untilExecutionYearID);

        ExecutionYear fromExecutionYear = FenixFramework.getDomainObject(fromExecutionYearID);
        ExecutionYear untilExecutionYear = FenixFramework.getDomainObject(untilExecutionYearID);

        ExecutionSemester fromExecutionPeriod = fromExecutionYear.getExecutionSemesterFor(1);
        ExecutionSemester untilExecutionPeriod = untilExecutionYear.getExecutionSemesterFor(2);

        if (!validExecutionPeriodsChoice(fromExecutionPeriod, untilExecutionPeriod)) {
            throw new InvalidPeriodException();
        }

        List<TeacherCreditsReportDTO> teacherCreditsReportList = new ArrayList<TeacherCreditsReportDTO>();
        Map<Department, List<TeacherCreditsReportDTO>> teachersCreditsByDepartment =
                new HashMap<Department, List<TeacherCreditsReportDTO>>();
        if (StringUtils.isEmpty(departmentID)) {
            Collection<Department> departments = rootDomainObject.getDepartmentsSet();
            for (Department department : departments) {
                Unit unit = department.getDepartmentUnit();
                teacherCreditsReportList =
                        ReadTeachersCreditsResumeByPeriodAndUnit.run(unit, fromExecutionPeriod, untilExecutionPeriod);
                teachersCreditsByDepartment.put(department, teacherCreditsReportList);
            }
            request.setAttribute("departmentID", null);
        } else {
            Department department = FenixFramework.getDomainObject(departmentID);
            Unit departmentUnit = department.getDepartmentUnit();
            teacherCreditsReportList =
                    ReadTeachersCreditsResumeByPeriodAndUnit.run(departmentUnit, fromExecutionPeriod, untilExecutionPeriod);
            teachersCreditsByDepartment.put(department, teacherCreditsReportList);
            request.setAttribute("department", department);
            request.setAttribute("departmentID", department.getExternalId());
        }

        if (teachersCreditsByDepartment != null && !teachersCreditsByDepartment.isEmpty()) {
            Map<Department, Map<Unit, List>> teachersCreditsDisplayMap =
                    new TreeMap<Department, Map<Unit, List>>(new BeanComparator("name"));
            for (Department department : teachersCreditsByDepartment.keySet()) {
                Map<Unit, List> teachersCreditsByUnit = new TreeMap<Unit, List>(new BeanComparator("name"));
                List<TeacherCreditsReportDTO> list = teachersCreditsByDepartment.get(department);
                for (TeacherCreditsReportDTO creditsReportDTO : list) {
                    List mapLineList = teachersCreditsByUnit.get(creditsReportDTO.getUnit());
                    if (mapLineList == null) {
                        mapLineList = new ArrayList<TeacherCreditsReportDTO>();
                        mapLineList.add(creditsReportDTO);
                        teachersCreditsByUnit.put(creditsReportDTO.getUnit(), mapLineList);
                    } else {
                        mapLineList.add(creditsReportDTO);
                    }
                }
                for (List teachersCreditMapLine : teachersCreditsByUnit.values()) {
                    Collections.sort(teachersCreditMapLine, new BeanComparator("teacher.person.username"));
                }
                teachersCreditsDisplayMap.put(department, teachersCreditsByUnit);
            }
            if (!teacherCreditsReportList.isEmpty()) {
                request.setAttribute("executionPeriodHeader", teacherCreditsReportList.iterator().next()
                        .getCreditsByExecutionPeriod());
            }
            request.setAttribute("teachersCreditsDisplayMap", teachersCreditsDisplayMap);
            return teachersCreditsDisplayMap;
        }
        return null;
    }

    public ActionForward viewGlobalCreditsReport(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, InvalidPeriodException, ParseException {

        User userView = Authenticate.getUser();
        DynaActionForm dynaForm = (DynaActionForm) form;

        String fromExecutionYearID = dynaForm.getString("fromExecutionYearID");
        String untilExecutionYearID = dynaForm.getString("untilExecutionYearID");
        String departmentID = (String) dynaForm.get("departmentID");

        getGlobalDepartmentCreditsMap(request, userView, fromExecutionYearID, untilExecutionYearID, departmentID);

        return mapping.findForward("showDepartmentCreditsReport");
    }

    private void setTotals(HttpServletRequest request, ExecutionYear untilExecutionYear,
            SortedMap<Department, Map<ExecutionYear, PeriodCreditsReportDTO>> departmentTotalCredits) {

        int totalTeachersSize = 0, totalCareerTeachersSize = 0, totalNotCareerTeachersSize = 0;
        SortedMap<ExecutionYear, GenericPair<Double, GenericPair<Double, Double>>> executionYearTotals =
                new TreeMap<ExecutionYear, GenericPair<Double, GenericPair<Double, Double>>>(ExecutionYear.COMPARATOR_BY_YEAR);
        for (Department department : departmentTotalCredits.keySet()) {
            for (ExecutionYear executionYear : departmentTotalCredits.get(department).keySet()) {
                if (!executionYearTotals.containsKey(executionYear)) {
                    executionYearTotals.put(executionYear, new GenericPair(0.0, new GenericPair(0.0, 0.0)));
                }

                GenericPair genericPair = executionYearTotals.get(executionYear);
                genericPair.setLeft(round(departmentTotalCredits.get(department).get(executionYear).getCredits()
                        + executionYearTotals.get(executionYear).getLeft()));
                ((GenericPair) genericPair.getRight()).setLeft(round(departmentTotalCredits.get(department).get(executionYear)
                        .getCareerCategoryTeacherCredits()
                        + executionYearTotals.get(executionYear).getRight().getLeft()));
                ((GenericPair) genericPair.getRight()).setRight(round(departmentTotalCredits.get(department).get(executionYear)
                        .getNotCareerCategoryTeacherCredits()
                        + executionYearTotals.get(executionYear).getRight().getRight()));

                if (executionYear.equals(untilExecutionYear)) {
                    totalTeachersSize += departmentTotalCredits.get(department).get(executionYear).getTeachersSize();
                    totalCareerTeachersSize += departmentTotalCredits.get(department).get(executionYear).getCareerTeachersSize();
                    totalNotCareerTeachersSize +=
                            departmentTotalCredits.get(department).get(executionYear).getNotCareerTeachersSize();
                }
            }
        }

        request.setAttribute("totalCareerTeachersSize", totalCareerTeachersSize);
        request.setAttribute("totalNotCareerTeachersSize", totalNotCareerTeachersSize);
        request.setAttribute("totalCareerTeachersBalance", round(executionYearTotals.get(untilExecutionYear).getRight().getLeft()
                / totalCareerTeachersSize));
        request.setAttribute("totalNotCareerTeachersBalance", round(executionYearTotals.get(untilExecutionYear).getRight()
                .getRight()
                / totalNotCareerTeachersSize));
        request.setAttribute("totalBalance", round(executionYearTotals.get(untilExecutionYear).getLeft() / totalTeachersSize));
        request.setAttribute("totalTeachersSize", totalTeachersSize);
        request.setAttribute("executionYearTotals", executionYearTotals);
    }

    private boolean validExecutionPeriodsChoice(ExecutionSemester fromExecutionPeriod, ExecutionSemester untilExecutionPeriod) {
        ExecutionSemester tempExecutionPeriod = fromExecutionPeriod;
        if (fromExecutionPeriod == untilExecutionPeriod) {
            return true;
        }
        while (tempExecutionPeriod != untilExecutionPeriod && tempExecutionPeriod != null) {
            tempExecutionPeriod = tempExecutionPeriod.getNextExecutionPeriod();
            if (tempExecutionPeriod == untilExecutionPeriod) {
                return true;
            }
        }
        return false;
    }

    public ActionForward exportGlobalToExcel(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, InvalidPeriodException, ParseException {

        final String fromExecutionYearID = request.getParameter("fromExecutionYearID");
        final String untilExecutionYearID = request.getParameter("untilExecutionYearID");
        final String departmentID = request.getParameter("departmentID");

        final ExecutionYear beginExecutionYear = FenixFramework.getDomainObject(fromExecutionYearID);
        final ExecutionYear endExecutionYear = FenixFramework.getDomainObject(untilExecutionYearID);
        final ExecutionSemester beginExecutionSemester;
        final ExecutionSemester endExecutionSemester = endExecutionYear.getExecutionSemesterFor(2);
        if (beginExecutionYear.getPreviousExecutionYear().equals(
                ExecutionSemester.readStartExecutionSemesterForCredits().getExecutionYear())) {
            beginExecutionSemester = ExecutionSemester.readStartExecutionSemesterForCredits();
        } else {
            beginExecutionSemester = beginExecutionYear.getPreviousExecutionYear().getExecutionSemesterFor(1);
        }

        Set<Department> departments = new TreeSet<Department>(Department.COMPARATOR_BY_NAME);
        if (departmentID == null) {
            departments.addAll(rootDomainObject.getDepartmentsSet());
        } else {
            departments.add(FenixFramework.<Department> getDomainObject(departmentID));
        }

        SheetData<Department> data = new SheetData<Department>(departments) {
            @Override
            protected void makeLine(Department department) {
                addCell("Departamento", department.getRealName(), "Total");

                Map<ExecutionYear, PeriodCreditsReportDTO> departmentPeriodTotalCredits = null;
                try {
                    departmentPeriodTotalCredits =
                            ReadDepartmentTotalCreditsByPeriod.run(department.getDepartmentUnit(), beginExecutionSemester,
                                    endExecutionSemester);
                } catch (ParseException e) {
                    logger.error(e.getMessage(), e);
                }
                ExecutionYear lastExecutionYear = null;
                for (ExecutionYear executionYear = beginExecutionSemester.getExecutionYear(); executionYear != null
                        && executionYear.isBeforeOrEquals(endExecutionYear); executionYear = executionYear.getNextExecutionYear()) {
                    PeriodCreditsReportDTO periodCreditsReportDTO = departmentPeriodTotalCredits.get(executionYear);
                    addCell(executionYear.getYear(), (short) 3, "Docentes Carreira", (short) 1,
                            periodCreditsReportDTO == null ? null : periodCreditsReportDTO.getCareerCategoryTeacherCredits(),
                            (short) 1, Formula.SUM_FOOTER, (short) 1);
                    addCell("Restantes Categorias",
                            periodCreditsReportDTO == null ? null : periodCreditsReportDTO.getNotCareerCategoryTeacherCredits(),
                            Formula.SUM_FOOTER);
                    addCell("Saldo Final", periodCreditsReportDTO == null ? null : periodCreditsReportDTO.getCredits(),
                            Formula.SUM_FOOTER);
                    lastExecutionYear = executionYear;
                }
                if (lastExecutionYear != null) {
                    PeriodCreditsReportDTO periodCreditsReportDTO = departmentPeriodTotalCredits.get(lastExecutionYear);
                    addCell("Nº Docentes " + lastExecutionYear.getYear(), (short) 3, "Docentes Carreira", (short) 1,
                            periodCreditsReportDTO == null ? null : periodCreditsReportDTO.getCareerTeachersSize(), (short) 1,
                            Formula.SUM_FOOTER, (short) 1);
                    addCell("Restantes Categorias",
                            periodCreditsReportDTO == null ? null : periodCreditsReportDTO.getNotCareerTeachersSize(),
                            Formula.SUM_FOOTER);
                    addCell("Saldo Final", periodCreditsReportDTO == null ? null : periodCreditsReportDTO.getTeachersSize(),
                            Formula.SUM_FOOTER);

                    addCell("Saldo per capita " + lastExecutionYear.getYear(), (short) 3, "Docentes Carreira", (short) 1,
                            periodCreditsReportDTO == null ? null : periodCreditsReportDTO.getCareerTeachersBalance(), (short) 1,
                            Formula.SUM_FOOTER, (short) 1);
                    addCell("Restantes Categorias",
                            periodCreditsReportDTO == null ? null : periodCreditsReportDTO.getNotCareerTeachersBalance(),
                            Formula.SUM_FOOTER);
                    addCell("Saldo Final", periodCreditsReportDTO == null ? null : periodCreditsReportDTO.getBalance(),
                            Formula.SUM_FOOTER);
                }
            }
        };

        try {
            String filename = "RelatorioCreditos:" + getFileName(Calendar.getInstance().getTime());
            final ServletOutputStream writer = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment; filename=" + filename + ".xls");
            new SpreadsheetBuilder().addSheet("RelatorioCreditos", data).build(WorkbookExportFormat.EXCEL, writer);
            writer.flush();
            response.flushBuffer();

        } catch (IOException e) {
            throw new FenixServiceException();
        }
        return null;
    }

    private SortedMap<Department, Map<ExecutionYear, PeriodCreditsReportDTO>> getGlobalDepartmentCreditsMap(
            HttpServletRequest request, User userView, String fromExecutionYearID, String untilExecutionYearID,
            String departmentID) throws ParseException, InvalidPeriodException, FenixServiceException {

        request.setAttribute("fromExecutionYearID", fromExecutionYearID);
        request.setAttribute("untilExecutionYearID", untilExecutionYearID);

        ExecutionYear fromExecutionYear = FenixFramework.getDomainObject(fromExecutionYearID);
        ExecutionYear untilExecutionYear = FenixFramework.getDomainObject(untilExecutionYearID);

        ExecutionSemester fromExecutionPeriod = null;
        if (fromExecutionYear.getPreviousExecutionYear().equals(
                ExecutionSemester.readStartExecutionSemesterForCredits().getExecutionYear())) {
            fromExecutionPeriod = ExecutionSemester.readStartExecutionSemesterForCredits();
        } else {
            fromExecutionPeriod = fromExecutionYear.getPreviousExecutionYear().getExecutionSemesterFor(1);
        }

        ExecutionSemester untilExecutionPeriod = untilExecutionYear.getExecutionSemesterFor(2);

        if (!validExecutionPeriodsChoice(fromExecutionPeriod, untilExecutionPeriod)) {
            throw new InvalidPeriodException();
        }

        Map<ExecutionYear, PeriodCreditsReportDTO> departmentPeriodTotalCredits =
                new HashMap<ExecutionYear, PeriodCreditsReportDTO>();
        SortedMap<Department, Map<ExecutionYear, PeriodCreditsReportDTO>> departmentTotalCredits =
                new TreeMap<Department, Map<ExecutionYear, PeriodCreditsReportDTO>>(new BeanComparator("code"));

        if (departmentID == null) {
            Collection<Department> departments = rootDomainObject.getDepartmentsSet();
            for (Department department : departments) {
                Unit unit = department.getDepartmentUnit();
                departmentPeriodTotalCredits =
                        ReadDepartmentTotalCreditsByPeriod.run(unit, fromExecutionPeriod, untilExecutionPeriod);
                departmentTotalCredits.put(department, departmentPeriodTotalCredits);
            }
            request.setAttribute("departmentID", null);
        } else {
            Department department = FenixFramework.getDomainObject(departmentID);
            Unit departmentUnit = department.getDepartmentUnit();
            departmentPeriodTotalCredits =
                    ReadDepartmentTotalCreditsByPeriod.run(departmentUnit, fromExecutionPeriod, untilExecutionPeriod);

            departmentTotalCredits.put(department, departmentPeriodTotalCredits);
            request.setAttribute("department", department);
            request.setAttribute("departmentID", department.getExternalId());
        }

        setTotals(request, untilExecutionYear, departmentTotalCredits);
        request.setAttribute("departmentTotalCredits", departmentTotalCredits);
        return departmentTotalCredits;
    }

    public ActionForward exportToExcel(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException, InvalidPeriodException {

        User userView = Authenticate.getUser();

        String fromExecutionYearID = request.getParameter("fromExecutionYearID");
        String untilExecutionYearID = request.getParameter("untilExecutionYearID");
        String departmentID = request.getParameter("departmentID");

        Map<Department, Map<Unit, List>> teachersCreditsByDepartment =
                getDetailedTeachersCreditsMap(request, userView, fromExecutionYearID, untilExecutionYearID, departmentID);

        try {
            String filename = "RelatorioCreditos:" + getFileName(Calendar.getInstance().getTime());
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment; filename=" + filename + ".xls");

            ServletOutputStream writer = response.getOutputStream();
            exportToXls(teachersCreditsByDepartment, writer);

            writer.flush();
            response.flushBuffer();

        } catch (IOException e) {
            throw new FenixServiceException();
        }
        return null;
    }

    private void exportToXls(Map<Department, Map<Unit, List>> teachersCreditsByDepartment, ServletOutputStream outputStream)
            throws IOException {

        final Spreadsheet spreadsheet = new Spreadsheet("Relatório de Créditos");

        final HSSFWorkbook workbook = new HSSFWorkbook();
        final ExcelStyle excelStyle = new ExcelStyle(workbook);

        for (Department department : teachersCreditsByDepartment.keySet()) {
            Set<ExecutionSemester> executionSemesters = null;
            String deptName = department.getName();
            deptName = deptName.replaceAll("DEPARTAMENTO", "DEP. ");
            deptName = deptName.replaceAll("ENGENHARIA", "ENG. ");
            spreadsheet.setName(deptName);
            final Row initialRow = spreadsheet.addRow();
            initialRow.setCell(department.getRealName());
            spreadsheet.addRow();
            Map<Unit, List> creditsByUnit = teachersCreditsByDepartment.get(department);
            Double unitTotalCredits = 0.0;
            for (Unit unit : creditsByUnit.keySet()) {
                List<TeacherCreditsReportDTO> teachersCreditReportDTOs = creditsByUnit.get(unit);
                if (executionSemesters == null) {
                    executionSemesters = teachersCreditReportDTOs.iterator().next().getCreditsByExecutionPeriod().keySet();
                }
                spreadsheet.addRow();
                final Row row = spreadsheet.addRow();
                row.setCell(unit.getName());
                setHeaders(executionSemesters, spreadsheet);
                unitTotalCredits += fillSpreadSheet(teachersCreditReportDTOs, spreadsheet);
            }
            spreadsheet.exportToXLSSheet(workbook, excelStyle.getHeaderStyle(), excelStyle.getStringStyle());
            spreadsheet.setRows(new ArrayList<Row>());
        }
        workbook.write(outputStream);
    }

    private Double fillSpreadSheet(final List<TeacherCreditsReportDTO> allListElements, final Spreadsheet spreadsheet) {

        Double listTotalCredits = 0.0;
        int numberOfCells = 0;
        for (final TeacherCreditsReportDTO teacherCreditsReportDTO : allListElements) {
            final Row row = spreadsheet.addRow();
            row.setCell(teacherCreditsReportDTO.getTeacher().getPerson().getUsername());
            row.setCell(teacherCreditsReportDTO.getTeacher().getPerson().getName());
            Double pastCredits = NumberUtils.formatNumber(teacherCreditsReportDTO.getPastCredits(), 2);
            row.setCell(pastCredits.toString().replace('.', ','));
            Set<ExecutionSemester> executionSemesters = teacherCreditsReportDTO.getCreditsByExecutionPeriod().keySet();
            Double totalCredits = 0.0;
            totalCredits += teacherCreditsReportDTO.getPastCredits();
            numberOfCells = 2;
            for (ExecutionSemester executionSemester : executionSemesters) {
                numberOfCells += 1;
                Double credits = teacherCreditsReportDTO.getCreditsByExecutionPeriod().get(executionSemester);
                credits = NumberUtils.formatNumber(credits, 2);
                row.setCell(credits.toString().replace('.', ','));
                totalCredits += credits;
                if (executionSemester.getSemester() == 2) {
                    numberOfCells += 1;
                    totalCredits = NumberUtils.formatNumber(totalCredits, 2);
                    row.setCell(totalCredits.toString().replace('.', ','));
                }
            }
            listTotalCredits += totalCredits;
        }
        final Row row = spreadsheet.addRow();
        row.setCell(numberOfCells - 1, "Total Unidade");
        row.setCell(numberOfCells, NumberUtils.formatNumber(listTotalCredits, 2).toString().replace('.', ','));
        return listTotalCredits;
    }

    private void setHeaders(Set<ExecutionSemester> executionSemesters, Spreadsheet spreadsheet) {
        final Row row = spreadsheet.addRow();
        row.setCell("Número");
        row.setCell("Nome");
        row.setCell("Saldo até " + executionSemesters.iterator().next().getPreviousExecutionPeriod().getExecutionYear().getYear());
        for (ExecutionSemester executionSemester : executionSemesters) {
            String semester = null;
            if (executionSemester.getName().equalsIgnoreCase("1 Semestre")) {
                semester = "1º Sem - ";
            } else {
                semester = "2º Sem - ";
            }
            StringBuilder stringBuilder = new StringBuilder(semester);
            stringBuilder.append(executionSemester.getExecutionYear().getYear());
            row.setCell(stringBuilder.toString());
            if (executionSemester.getSemester() == 2) {
                row.setCell("Saldo Final " + executionSemester.getExecutionYear().getYear());
            }
        }
    }

    private List filterExecutionYears(Collection<ExecutionYear> executionYears) {
        List filteredExecutionYears = new ArrayList();
        ExecutionYear executionYear0304 = ExecutionYear.readExecutionYearByName("2003/2004");
        ExecutionYear lastExecutionYear = ExecutionSemester.readLastExecutionSemesterForCredits().getExecutionYear();

        for (ExecutionYear executionYear : executionYears) {
            if (!executionYear.getBeginDateYearMonthDay().isBefore(executionYear0304.getBeginDateYearMonthDay())
                    && !executionYear.isAfter(lastExecutionYear)) {
                String label = executionYear.getYear();
                filteredExecutionYears.add(new LabelValueBean(label, executionYear.getExternalId().toString()));
            }
        }
        Collections.sort(filteredExecutionYears, new BeanComparator("label"));
        return filteredExecutionYears;
    }

    private String getFileName(Date date) throws FenixServiceException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        return (day + "-" + month + "-" + year + "_" + hour + ":" + minutes);
    }

    public class InvalidPeriodException extends FenixActionException {
    }

    private Double round(double n) {
        return Math.round((n * 100.0)) / 100.0;
    }
}