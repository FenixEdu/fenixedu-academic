/**
 * Jan 23, 2006
 */
package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.credits;

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

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.credits.ReadDepartmentTotalCreditsByPeriod.PeriodCreditsReportDTO;
import net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.credits.ReadTeachersCreditsResumeByPeriodAndUnit.TeacherCreditsReportDTO;
import net.sourceforge.fenixedu.commons.OrderedIterator;
import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.NumberUtils;
import net.sourceforge.fenixedu.util.projectsManagement.ExcelStyle;
import net.sourceforge.fenixedu.util.report.Spreadsheet;
import net.sourceforge.fenixedu.util.report.Spreadsheet.Row;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ViewTeacherCreditsReportDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixFilterException, FenixServiceException {

        Collection<ExecutionYear> executionYears = rootDomainObject.getExecutionYears();
        List filteredExecutionYears = filterExecutionYears(executionYears);

        Collection<Department> departments = rootDomainObject.getDepartments();
        Iterator departmentOrderedIterator = new OrderedIterator(departments.iterator(),
                new BeanComparator("name"));

        request.setAttribute("executionYears", filteredExecutionYears);
        request.setAttribute("departments", departmentOrderedIterator);
        return mapping.findForward("prepare");
    }

    public ActionForward viewDetailedCreditsReport(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException, InvalidPeriodException {

        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm dynaForm = (DynaActionForm) form;

        Integer fromExecutionYearID = Integer.parseInt(dynaForm.getString("fromExecutionYearID"));
        Integer untilExecutionYearID = Integer.parseInt(dynaForm.getString("untilExecutionYearID"));
        Integer departmentID = (Integer) dynaForm.get("departmentID");
        
        getDetailedTeachersCreditsMap(request, userView, fromExecutionYearID, untilExecutionYearID, departmentID);
        return mapping.findForward("showCreditsReport");
    }

    private Map<Department, Map<Unit, List>> getDetailedTeachersCreditsMap(HttpServletRequest request, IUserView userView, Integer fromExecutionYearID, Integer untilExecutionYearID, Integer departmentID) throws InvalidPeriodException, FenixServiceException, FenixFilterException {
        
        request.setAttribute("fromExecutionYearID", fromExecutionYearID);
        request.setAttribute("untilExecutionYearID", untilExecutionYearID);

        ExecutionYear fromExecutionYear = rootDomainObject.readExecutionYearByOID(fromExecutionYearID);
        ExecutionYear untilExecutionYear = rootDomainObject.readExecutionYearByOID(untilExecutionYearID);

        ExecutionPeriod fromExecutionPeriod = fromExecutionYear.readExecutionPeriodForSemester(1);
        ExecutionPeriod untilExecutionPeriod = untilExecutionYear.readExecutionPeriodForSemester(2);

        if (!validExecutionPeriodsChoice(fromExecutionPeriod, untilExecutionPeriod)) {
            throw new InvalidPeriodException();
        }

        List<TeacherCreditsReportDTO> teacherCreditsReportList = new ArrayList<TeacherCreditsReportDTO>();        
        Map<Department, List<TeacherCreditsReportDTO>> teachersCreditsByDepartment = new HashMap<Department, List<TeacherCreditsReportDTO>>();
        if (departmentID == 0) {
            Collection<Department> departments = rootDomainObject.getDepartments();
            for (Department department : departments) {
                Unit unit = department.getDepartmentUnit();
                teacherCreditsReportList = (List<TeacherCreditsReportDTO>) ServiceUtils.executeService(
                        userView, "ReadTeachersCreditsResumeByPeriodAndUnit", new Object[] { unit,
                                fromExecutionPeriod, untilExecutionPeriod });
                teachersCreditsByDepartment.put(department, teacherCreditsReportList);
            }
            request.setAttribute("departmentID", 0);
        } else {            
            Department department = rootDomainObject.readDepartmentByOID(departmentID);
            Unit departmentUnit = department.getDepartmentUnit();
            teacherCreditsReportList = (List<TeacherCreditsReportDTO>) ServiceUtils.executeService(
                    userView, "ReadTeachersCreditsResumeByPeriodAndUnit", new Object[] { departmentUnit,
                            fromExecutionPeriod, untilExecutionPeriod });
            teachersCreditsByDepartment.put(department, teacherCreditsReportList);
            request.setAttribute("department", department);
            request.setAttribute("departmentID", department.getIdInternal());
        }

        if (teachersCreditsByDepartment != null && !teachersCreditsByDepartment.isEmpty()) {
            Map<Department, Map<Unit, List>> teachersCreditsDisplayMap = new TreeMap<Department, Map<Unit, List>>(
                    new BeanComparator("name"));
            for (Department department : teachersCreditsByDepartment.keySet()) {
                Map<Unit, List> teachersCreditsByUnit = new TreeMap<Unit, List>(new BeanComparator(
                        "name"));
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
                    Collections.sort(teachersCreditMapLine, new BeanComparator("teacher.teacherNumber"));
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
    
    public ActionForward viewGlobalCreditsReport(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException, InvalidPeriodException, ParseException {

        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm dynaForm = (DynaActionForm) form;

        Integer fromExecutionYearID = Integer.parseInt(dynaForm.getString("fromExecutionYearID"));
        Integer untilExecutionYearID = Integer.parseInt(dynaForm.getString("untilExecutionYearID"));
        Integer departmentID = (Integer) dynaForm.get("departmentID");
        
        getGlobalDepartmentCreditsMap(request, userView, fromExecutionYearID, untilExecutionYearID, departmentID);

        return mapping.findForward("showDepartmentCreditsReport");
    }

    private void setTotals(HttpServletRequest request, ExecutionYear untilExecutionYear, SortedMap<Department, 
            Map<ExecutionYear, PeriodCreditsReportDTO>> departmentTotalCredits) {
        
        int totalTeachersSize = 0, totalCareerTeachersSize = 0, totalNotCareerTeachersSize = 0;
        SortedMap<ExecutionYear, GenericPair<Double, GenericPair<Double, Double>>> executionYearTotals = new TreeMap<ExecutionYear, GenericPair<Double, GenericPair<Double, Double>>>(ExecutionYear.EXECUTION_YEAR_COMPARATOR_BY_YEAR);        
        for (Department department : departmentTotalCredits.keySet()) {            
            for (ExecutionYear executionYear : departmentTotalCredits.get(department).keySet()) {
                if(!executionYearTotals.containsKey(executionYear)) {                                       
                    executionYearTotals.put(executionYear, new GenericPair(0.0, new GenericPair(0.0, 0.0)));                    
                }
                
                GenericPair genericPair = executionYearTotals.get(executionYear);                
                genericPair.setLeft(round(departmentTotalCredits.get(department).get(executionYear).getCredits() + executionYearTotals.get(executionYear).getLeft()));                
                ((GenericPair)genericPair.getRight()).setLeft(round(departmentTotalCredits.get(department).get(executionYear).getCareerCategoryTeacherCredits() + executionYearTotals.get(executionYear).getRight().getLeft()));
                ((GenericPair)genericPair.getRight()).setRight(round(departmentTotalCredits.get(department).get(executionYear).getNotCareerCategoryTeacherCredits() + executionYearTotals.get(executionYear).getRight().getRight()));
                
                if(executionYear.equals(untilExecutionYear)) {
                    totalTeachersSize += departmentTotalCredits.get(department).get(executionYear).getTeachersSize();
                    totalCareerTeachersSize += departmentTotalCredits.get(department).get(executionYear).getCareerTeachersSize();
                    totalNotCareerTeachersSize += departmentTotalCredits.get(department).get(executionYear).getNotCareerTeachersSize();
                }
            }         
        }  
                              
        request.setAttribute("totalCareerTeachersSize", totalCareerTeachersSize);
        request.setAttribute("totalNotCareerTeachersSize", totalNotCareerTeachersSize);
        request.setAttribute("totalCareerTeachersBalance", round(executionYearTotals.get(untilExecutionYear).getRight().getLeft() / totalCareerTeachersSize));
        request.setAttribute("totalNotCareerTeachersBalance", round(executionYearTotals.get(untilExecutionYear).getRight().getRight() / totalNotCareerTeachersSize));        
        request.setAttribute("totalBalance", round(executionYearTotals.get(untilExecutionYear).getLeft() / totalTeachersSize));
        request.setAttribute("totalTeachersSize", totalTeachersSize);
        request.setAttribute("executionYearTotals", executionYearTotals);
    }
    
    private boolean validExecutionPeriodsChoice(ExecutionPeriod fromExecutionPeriod, ExecutionPeriod untilExecutionPeriod) {        
        ExecutionPeriod tempExecutionPeriod = fromExecutionPeriod;
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

    public ActionForward exportGlobalToExcel(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException, InvalidPeriodException, ParseException {

        IUserView userView = SessionUtils.getUserView(request);
        
        Integer fromExecutionYearID = Integer.parseInt(request.getParameter("fromExecutionYearID"));
        Integer untilExecutionYearID = Integer.parseInt(request.getParameter("untilExecutionYearID"));
        Integer departmentID = Integer.parseInt(request.getParameter("departmentID"));
        
        SortedMap<Department, Map<ExecutionYear, PeriodCreditsReportDTO>> teachersCreditsByDepartment = 
            getGlobalDepartmentCreditsMap(request, userView, fromExecutionYearID, untilExecutionYearID, departmentID);

        try {
            String filename = "RelatorioCreditos:" + getFileName(Calendar.getInstance().getTime());
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment; filename=" + filename + ".xls");

            ServletOutputStream writer = response.getOutputStream();
            exportGlobalToXls(request, teachersCreditsByDepartment, writer);

            writer.flush();
            response.flushBuffer();

        } catch (IOException e) {
            throw new FenixServiceException();
        }
        return null;
    }
    
    private void exportGlobalToXls(HttpServletRequest request, SortedMap<Department, Map<ExecutionYear, PeriodCreditsReportDTO>> teachersCreditsByDepartment,
            ServletOutputStream outputStream) throws IOException {
        
        final Spreadsheet spreadsheet = new Spreadsheet("Relatório de Créditos");
        
        final HSSFWorkbook workbook = new HSSFWorkbook();
        final ExcelStyle excelStyle = new ExcelStyle(workbook);

        setGlobalHeaders(teachersCreditsByDepartment, spreadsheet);
        
        for (Department department : teachersCreditsByDepartment.keySet()) {
            Row row = spreadsheet.addRow();
            row.setCell(department.getRealName());
            ExecutionYear lastExecutionYear = null;
            for (ExecutionYear executionYear : teachersCreditsByDepartment.get(department).keySet()) {
                row.setCell(String.valueOf(teachersCreditsByDepartment.get(department).get(executionYear).getCredits()).replace('.', ','));
                row.setCell(String.valueOf(teachersCreditsByDepartment.get(department).get(executionYear).getCareerCategoryTeacherCredits()).replace('.', ','));
                row.setCell(String.valueOf(teachersCreditsByDepartment.get(department).get(executionYear).getNotCareerCategoryTeacherCredits()).replace('.', ','));
                lastExecutionYear = executionYear;
            }
            row.setCell(String.valueOf(teachersCreditsByDepartment.get(department).get(lastExecutionYear).getCareerTeachersSize()));
            row.setCell(String.valueOf(teachersCreditsByDepartment.get(department).get(lastExecutionYear).getNotCareerTeachersSize()));
            row.setCell(String.valueOf(teachersCreditsByDepartment.get(department).get(lastExecutionYear).getTeachersSize()));
            row.setCell(String.valueOf(teachersCreditsByDepartment.get(department).get(lastExecutionYear).getCareerTeachersBalance()).replace('.', ','));           
            row.setCell(String.valueOf(teachersCreditsByDepartment.get(department).get(lastExecutionYear).getNotCareerTeachersBalance()).replace('.', ','));            
            row.setCell(String.valueOf(teachersCreditsByDepartment.get(department).get(lastExecutionYear).getBalance()).replace('.', ','));            
        }
        
        if(teachersCreditsByDepartment.keySet().size() > 1) {
            Row row = spreadsheet.addRow();
            row.setCell("Totais");
            SortedMap<ExecutionYear, GenericPair<Double, GenericPair<Double, Double>>> executionYearTotals = (SortedMap<ExecutionYear, GenericPair<Double, GenericPair<Double, Double>>>) request.getAttribute("executionYearTotals");
            for (GenericPair<Double, GenericPair<Double, Double>> genericPair : executionYearTotals.values()) {
                row.setCell(String.valueOf(genericPair.getRight().getLeft()).replace('.', ','));
                row.setCell(String.valueOf(genericPair.getRight().getRight()).replace('.', ','));
                row.setCell(String.valueOf(genericPair.getLeft()).replace('.', ','));
            }
            row.setCell(String.valueOf(request.getAttribute("totalCareerTeachersSize")));
            row.setCell(String.valueOf(request.getAttribute("totalNotCareerTeachersSize")));
            row.setCell(String.valueOf(request.getAttribute("totalTeachersSize")));
            row.setCell(String.valueOf(request.getAttribute("totalCareerTeachersBalance")).replace('.', ','));           
            row.setCell(String.valueOf(request.getAttribute("totalNotCareerTeachersBalance")).replace('.', ','));              
            row.setCell(String.valueOf(request.getAttribute("totalBalance")).replace('.', ','));            
        }
        
        spreadsheet.exportToXLSSheet(workbook, excelStyle.getHeaderStyle(), excelStyle.getStringStyle());
        spreadsheet.setRows(new ArrayList<Row>());
        workbook.write(outputStream);
    }

    private void setGlobalHeaders(SortedMap<Department, Map<ExecutionYear, PeriodCreditsReportDTO>> teachersCreditsByDepartment, final Spreadsheet spreadsheet) {
        spreadsheet.setName("Relatório de Créditos Global");                        
        final Row initialRow = spreadsheet.addRow();
        initialRow.setCell("Departamento");

        ExecutionYear lastExecutionYear = null;
        for (Department department : teachersCreditsByDepartment.keySet()) {
            for (ExecutionYear executionYear : teachersCreditsByDepartment.get(department).keySet()) {                                               
                for (int i = 0; i < 3; i++) {
                    initialRow.setCell("Sum. " + executionYear.getYear());                
                }
                lastExecutionYear = executionYear;
            }                   
            break;
        }
        
        if(lastExecutionYear != null) {
            for (int i = 0; i < 3; i++) {
                initialRow.setCell("Nº Docentes " + lastExecutionYear.getYear());
            }
            for (int i = 0; i < 3; i++) {
                initialRow.setCell("Saldo per capita " + lastExecutionYear.getYear());
            }
        }
        
        final Row secondRow = spreadsheet.addRow();
        secondRow.setCell("");
        for (Department department : teachersCreditsByDepartment.keySet()) {
            for (int i = 0; i < teachersCreditsByDepartment.get(department).size(); i++) {                                               
                secondRow.setCell("Docentes Carreira");
                secondRow.setCell("Restantes Categorias");
                secondRow.setCell("Saldo Final");
            }                   
            break;
        }
                           
        for (int i = 0; i < 2; i++) {
            secondRow.setCell("Docentes Carreira");
            secondRow.setCell("Restantes Categorias");
            secondRow.setCell("Total");        
        }                                 
    }
       
    private SortedMap<Department, Map<ExecutionYear, PeriodCreditsReportDTO>> getGlobalDepartmentCreditsMap(HttpServletRequest request, IUserView userView, 
            Integer fromExecutionYearID, Integer untilExecutionYearID, Integer departmentID) throws ParseException, 
            InvalidPeriodException, FenixServiceException, FenixFilterException {
    
        request.setAttribute("fromExecutionYearID", fromExecutionYearID);
        request.setAttribute("untilExecutionYearID", untilExecutionYearID);
    
        ExecutionYear fromExecutionYear = rootDomainObject.readExecutionYearByOID(fromExecutionYearID);
        ExecutionYear untilExecutionYear = rootDomainObject.readExecutionYearByOID(untilExecutionYearID);
    
        ExecutionPeriod fromExecutionPeriod = null;
        if(fromExecutionYear.getPreviousExecutionYear().equals(TeacherService.getStartExecutionPeriodForCredits().getExecutionYear())) {
            fromExecutionPeriod = TeacherService.getStartExecutionPeriodForCredits();
        } else {
            fromExecutionPeriod = fromExecutionYear.getPreviousExecutionYear().readExecutionPeriodForSemester(1);
        }
        
        ExecutionPeriod untilExecutionPeriod = untilExecutionYear.readExecutionPeriodForSemester(2);
    
        if (!validExecutionPeriodsChoice(fromExecutionPeriod, untilExecutionPeriod)) {
            throw new InvalidPeriodException();
        }
    
        Map<ExecutionYear, PeriodCreditsReportDTO> departmentPeriodTotalCredits = new HashMap<ExecutionYear, PeriodCreditsReportDTO>();
        SortedMap<Department, Map<ExecutionYear, PeriodCreditsReportDTO>> departmentTotalCredits = new TreeMap<Department, Map<ExecutionYear, PeriodCreditsReportDTO>>(
                new BeanComparator("code"));
                     
        if (departmentID == 0) {
            Collection<Department> departments = rootDomainObject.getDepartments();
            for (Department department : departments) {
                Unit unit = department.getDepartmentUnit();
                departmentPeriodTotalCredits = (Map<ExecutionYear, PeriodCreditsReportDTO>) ServiceUtils.executeService(
                        userView, "ReadDepartmentTotalCreditsByPeriod", new Object[] { unit,
                                fromExecutionPeriod, untilExecutionPeriod });
                departmentTotalCredits.put(department, departmentPeriodTotalCredits);
            }
            request.setAttribute("departmentID", 0);
        } else {            
            Department department = rootDomainObject.readDepartmentByOID(departmentID);
            Unit departmentUnit = department.getDepartmentUnit();
            departmentPeriodTotalCredits = (Map<ExecutionYear, PeriodCreditsReportDTO>) ServiceUtils.executeService(
                    userView, "ReadDepartmentTotalCreditsByPeriod", new Object[] { departmentUnit,
                            fromExecutionPeriod, untilExecutionPeriod });
            
            departmentTotalCredits.put(department, departmentPeriodTotalCredits);            
            request.setAttribute("department", department);
            request.setAttribute("departmentID", department.getIdInternal());
        }              
                
        setTotals(request, untilExecutionYear, departmentTotalCredits);
        request.setAttribute("departmentTotalCredits", departmentTotalCredits);
        return departmentTotalCredits;
    }
    
    public ActionForward exportToExcel(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException, InvalidPeriodException {

        IUserView userView = SessionUtils.getUserView(request);

        Integer fromExecutionYearID = Integer.parseInt(request.getParameter("fromExecutionYearID"));
        Integer untilExecutionYearID = Integer.parseInt(request.getParameter("untilExecutionYearID"));
        Integer departmentID = Integer.parseInt(request.getParameter("departmentID"));
        
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

    private void exportToXls(Map<Department, Map<Unit, List>> teachersCreditsByDepartment,
            ServletOutputStream outputStream) throws IOException {
        
        final Spreadsheet spreadsheet = new Spreadsheet("Relatório de Créditos");
        
        final HSSFWorkbook workbook = new HSSFWorkbook();
        final ExcelStyle excelStyle = new ExcelStyle(workbook);

        for (Department department : teachersCreditsByDepartment.keySet()) {
            Set<ExecutionPeriod> executionPeriods = null;
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
                if (executionPeriods == null) {
                    executionPeriods = teachersCreditReportDTOs.get(0).getCreditsByExecutionPeriod().keySet();
                }
                spreadsheet.addRow();
                final Row row = spreadsheet.addRow();
                row.setCell(unit.getName());
                setHeaders(executionPeriods, spreadsheet);
                unitTotalCredits += fillSpreadSheet(teachersCreditReportDTOs, spreadsheet);
            }
            spreadsheet.exportToXLSSheet(workbook, excelStyle.getHeaderStyle(), excelStyle
                    .getStringStyle());
            spreadsheet.setRows(new ArrayList<Row>());
        }
        workbook.write(outputStream);
    }

    private Double fillSpreadSheet(final List<TeacherCreditsReportDTO> allListElements,
            final Spreadsheet spreadsheet) {
        
        Double listTotalCredits = 0.0;
        int numberOfCells = 0;
        for (final TeacherCreditsReportDTO teacherCreditsReportDTO : allListElements) {
            final Row row = spreadsheet.addRow();
            row.setCell(teacherCreditsReportDTO.getTeacher().getTeacherNumber().toString());
            row.setCell(teacherCreditsReportDTO.getTeacher().getPerson().getNome());
            Double pastCredits = NumberUtils.formatNumber((Double) teacherCreditsReportDTO
                    .getPastCredits(), 2);
            row.setCell(pastCredits.toString().replace('.', ','));
            Set<ExecutionPeriod> executionPeriods = teacherCreditsReportDTO
                    .getCreditsByExecutionPeriod().keySet();
            Double totalCredits = 0.0;
            totalCredits += teacherCreditsReportDTO.getPastCredits();
            numberOfCells = 2;
            for (ExecutionPeriod executionPeriod : executionPeriods) {
                numberOfCells += 1;
                Double credits = teacherCreditsReportDTO.getCreditsByExecutionPeriod().get(
                        executionPeriod);
                credits = NumberUtils.formatNumber(credits, 2);
                row.setCell(credits.toString().replace('.', ','));
                totalCredits += credits;
                if (executionPeriod.getSemester() == 2) {
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

    private void setHeaders(Set<ExecutionPeriod> executionPeriods, Spreadsheet spreadsheet) {
        final Row row = spreadsheet.addRow();
        row.setCell("Número");
        row.setCell("Nome");
        row.setCell("Saldo até " + executionPeriods.iterator().next().getPreviousExecutionPeriod().getExecutionYear().getYear());
        for (ExecutionPeriod executionPeriod : executionPeriods) {
            String semester = null;
            if (executionPeriod.getName().equalsIgnoreCase("1 Semestre")) {
                semester = "1º Sem - ";
            } else {
                semester = "2º Sem - ";
            }
            StringBuilder stringBuilder = new StringBuilder(semester);
            stringBuilder.append(executionPeriod.getExecutionYear().getYear());
            row.setCell(stringBuilder.toString());
            if (executionPeriod.getSemester() == 2) {
                row.setCell("Saldo Final " + executionPeriod.getExecutionYear().getYear());
            }
        }
    }
 
    private List filterExecutionYears(Collection<ExecutionYear> executionYears) {
        List filteredExecutionYears = new ArrayList();
        ExecutionYear executionYear0304 = ExecutionYear.readExecutionYearByName("2003/2004");
        for (ExecutionYear executionYear : executionYears) {
            if (!executionYear.getBeginDateYearMonthDay().isBefore(executionYear0304.getBeginDateYearMonthDay())) {
                String label = executionYear.getYear();
                filteredExecutionYears.add(new LabelValueBean(label, executionYear.getIdInternal().toString()));
            }
        }
        Collections.sort(filteredExecutionYears, new BeanComparator("label"));
        return filteredExecutionYears;
    }

    private String getFileName(Date date) throws FenixFilterException, FenixServiceException {
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
