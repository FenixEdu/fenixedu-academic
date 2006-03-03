/**
 * Jan 23, 2006
 */
package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.credits;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.credits.ReadTeachersCreditsResumeByPeriodAndUnit.TeacherCreditsReportDTO;
import net.sourceforge.fenixedu.commons.OrderedIterator;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.NumberUtils;
import net.sourceforge.fenixedu.util.PeriodState;
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

        IUserView userView = SessionUtils.getUserView(request);
        List<ExecutionPeriod> executionPeriods = (List<ExecutionPeriod>) ServiceUtils.executeService(
                userView, "ReadAllDomainObjects", new Object[] { ExecutionPeriod.class });
        List filteredExecutionPeriods = filterExecutionPeriods(executionPeriods);
        Collections.sort(filteredExecutionPeriods, new BeanComparator("value"));
        request.setAttribute("executionPeriods", filteredExecutionPeriods);

        List<Department> departments = (List<Department>) ServiceUtils.executeService(userView,
                "ReadAllDomainObjects", new Object[] { Department.class });
        Iterator departmentOrderedIterator = new OrderedIterator(departments.iterator(),
                new BeanComparator("name"));
        request.setAttribute("departments", departmentOrderedIterator);

//        DynaActionForm dynaForm = (DynaActionForm) form;
//        if (dynaForm.get("departmentID") != null && !((Integer) dynaForm.get("departmentID")).equals(0)) {
//            Integer departmentID = (Integer) dynaForm.get("departmentID");
//            Department department = (Department) ServiceUtils.executeService(userView,
//                    "ReadDomainObject", new Object[] { Department.class, departmentID });
//            List<Unit> units = department.getUnit().getSubUnits();
//            List<Unit> filteredUnits = (List<Unit>) CollectionUtils.select(units, new Predicate() {
//                public boolean evaluate(Object object) {
//                    Unit unit = (Unit) object;
//                    return unit.getType() != null
//                            && (unit.getType().equals(PartyTypeEnum.SECTION) || unit.getType().equals(
//                                    PartyTypeEnum.SCIENTIFIC_AREA));
//                }
//            });
//            Iterator unitsOrderedIter = new OrderedIterator(filteredUnits.iterator(),
//                    new BeanComparator("name"));
//            request.setAttribute("units", unitsOrderedIter);
//        }
        return mapping.findForward("prepare");
    }

    public ActionForward viewCreditsReport(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException, InvalidPeriodException {

        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm dynaForm = (DynaActionForm) form;

        Integer fromExecutionPeriodID = Integer.parseInt(dynaForm.getString("fromExecutionPeriodID"));
        Integer untilExecutionPeriodID = Integer.parseInt(dynaForm.getString("untilExecutionPeriodID"));
        request.setAttribute("fromExecutionPeriodID", fromExecutionPeriodID);
        request.setAttribute("untilExecutionPeriodID", untilExecutionPeriodID);

        ExecutionPeriod fromExecutionPeriod = (ExecutionPeriod) ServiceUtils.executeService(userView,
                "ReadDomainObject", new Object[] { ExecutionPeriod.class, fromExecutionPeriodID });
        ExecutionPeriod untilExecutionPeriod = (ExecutionPeriod) ServiceUtils.executeService(userView,
                "ReadDomainObject", new Object[] { ExecutionPeriod.class, untilExecutionPeriodID });

        if(!validExecutionPeriodsChoice(fromExecutionPeriod,untilExecutionPeriod)){
            throw new InvalidPeriodException();
        }
        List<TeacherCreditsReportDTO> teacherCreditsReportList = new ArrayList<TeacherCreditsReportDTO>();
        Integer departmentID = (Integer) dynaForm.get("departmentID");
        Map<Department, List<TeacherCreditsReportDTO>> teachersCreditsByDepartment = new HashMap<Department, List<TeacherCreditsReportDTO>>();
        if (departmentID == 0) {
            List<Department> departments = (List<Department>) ServiceUtils.executeService(userView,
                    "ReadAllDomainObjects", new Object[] { Department.class });
            for (Department department : departments) {
                Unit unit = department.getUnit();
                teacherCreditsReportList = (List<TeacherCreditsReportDTO>) ServiceUtils.executeService(
                        userView, "ReadTeachersCreditsResumeByPeriodAndUnit", new Object[] { unit,
                                fromExecutionPeriod, untilExecutionPeriod });
                teachersCreditsByDepartment.put(department, teacherCreditsReportList);
            }
            request.setAttribute("departmentID", 0);
            request.setAttribute("unitID", 0);
        } else {
            Integer unitID = (Integer) dynaForm.get("unitID");
            Unit unit = null;

            Department department = (Department) ServiceUtils.executeService(userView,
                    "ReadDomainObject", new Object[] { Department.class, departmentID });
            
            if (unitID == null || unitID == 0) {
                unit = department.getUnit();

            } else {
                unit = (Unit) ServiceUtils.executeService(userView, "ReadDomainObject", new Object[] {
                        Unit.class, unitID });
            }
            teacherCreditsReportList = (List<TeacherCreditsReportDTO>) ServiceUtils.executeService(
                    userView, "ReadTeachersCreditsResumeByPeriodAndUnit", new Object[] { unit,
                            fromExecutionPeriod, untilExecutionPeriod });
            teachersCreditsByDepartment.put(department, teacherCreditsReportList);
            request.setAttribute("department", department);
            request.setAttribute("departmentID", department.getIdInternal());
            request.setAttribute("unitID", unit.getIdInternal());
        }

        if (teachersCreditsByDepartment != null && !teachersCreditsByDepartment.isEmpty()) {
            Map<Department, Map<Unit, List>> teachersCreditsDisplayMap = new TreeMap<Department, Map<Unit, List>>(
                    new BeanComparator("name"));
            for (Department department : teachersCreditsByDepartment.keySet()) {
                Map<Unit, List> teachersCreditsByUnit = new TreeMap<Unit,List>(new BeanComparator("name"));
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
        }
        return mapping.findForward("showCreditsReport");
    }

    /**
     * @param fromExecutionPeriod
     * @param untilExecutionPeriod
     * @return
     */
    private boolean validExecutionPeriodsChoice(ExecutionPeriod fromExecutionPeriod, ExecutionPeriod untilExecutionPeriod) {
        ExecutionPeriod tempExecutionPeriod = fromExecutionPeriod;
        if(fromExecutionPeriod == untilExecutionPeriod){
            return true;
        }
        while(tempExecutionPeriod != untilExecutionPeriod && tempExecutionPeriod != null){
            tempExecutionPeriod = tempExecutionPeriod.getNextExecutionPeriod();
            if(tempExecutionPeriod == untilExecutionPeriod){
                return true;
            }
        }
        return false;
    }

    public ActionForward exportToExcel(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
            FenixFilterException {
        
        Map<Department, Map<Unit, List>> teachersCreditsByDepartment = getTeachersCreditsMap(request);

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

    /**
     * @param teachersCreditsByDepartment
     * @param writer
     * @throws IOException
     */
    private void exportToXls(Map<Department, Map<Unit, List>> teachersCreditsByDepartment,
            ServletOutputStream outputStream) throws IOException {
        final Spreadsheet spreadsheet = new Spreadsheet("Relatório de Créditos");

        final HSSFWorkbook workbook = new HSSFWorkbook();
        final ExcelStyle excelStyle = new ExcelStyle(workbook);
        
        for (Department department : teachersCreditsByDepartment.keySet()) {
            Set<ExecutionPeriod> executionPeriods = null;   
            String deptName = department.getName();
            deptName = deptName.replaceAll("DEPARTAMENTO","DEP. ");
            deptName = deptName.replaceAll("ENGENHARIA", "ENG. ");
            spreadsheet.setName(deptName);
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
                setHeaders(executionPeriods,spreadsheet);                
                unitTotalCredits += fillSpreadSheet(teachersCreditReportDTOs, spreadsheet);
            }              
            spreadsheet.exportToXLSSheet(workbook, excelStyle.getHeaderStyle(), excelStyle.getStringStyle());
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
            Double pastCredits = NumberUtils.formatNumber((Double) teacherCreditsReportDTO.getPastCredits(),2);
            row.setCell(pastCredits.toString());            
            Set<ExecutionPeriod> executionPeriods = teacherCreditsReportDTO
                    .getCreditsByExecutionPeriod().keySet();
            Double totalCredits = 0.0;
            numberOfCells = 2;
            for (ExecutionPeriod executionPeriod : executionPeriods) {
                numberOfCells += 1;
                Double credits = teacherCreditsReportDTO.getCreditsByExecutionPeriod().get(
                        executionPeriod);
                credits = NumberUtils.formatNumber(credits,2);
                row.setCell(credits.toString());
                totalCredits += credits;
            }
            numberOfCells += 1;
            totalCredits += teacherCreditsReportDTO.getPastCredits();
            totalCredits = NumberUtils.formatNumber(totalCredits,2);
            row.setCell(totalCredits.toString());
            listTotalCredits += totalCredits;            
        }
        final Row row = spreadsheet.addRow();
        row.setCell(numberOfCells-1,"Total Unidade");
        row.setCell(numberOfCells,NumberUtils.formatNumber(listTotalCredits,2).toString());
        return listTotalCredits;
    }

    private void setHeaders(Set<ExecutionPeriod> executionPeriods, Spreadsheet spreadsheet) {
        final Row row = spreadsheet.addRow();
        row.setCell("Número");
        row.setCell("Nome");
        row.setCell("Passado");        
        for (ExecutionPeriod executionPeriod : executionPeriods) {
            String semester = null;
            if(executionPeriod.getName().equalsIgnoreCase("1 Semestre")){
                semester = "1º Sem - ";
            } else {
                semester = "2º Sem - ";
            }
            StringBuilder stringBuilder = new StringBuilder(semester);
            stringBuilder.append(executionPeriod.getExecutionYear().getYear());
            row.setCell(stringBuilder.toString());
        }
        row.setCell("Total");
    }

    /**
     * @param request
     * @param dynaForm
     * @return
     * @throws FenixServiceException
     * @throws FenixFilterException
     */
    private Map<Department, Map<Unit, List>> getTeachersCreditsMap(HttpServletRequest request)
            throws FenixFilterException, FenixServiceException {

        IUserView userView = SessionUtils.getUserView(request);
        Integer fromExecutionPeriodID = Integer.parseInt(request.getParameter("fromExecutionPeriodID"));
        Integer untilExecutionPeriodID = Integer
                .parseInt(request.getParameter("untilExecutionPeriodID"));

        ExecutionPeriod fromExecutionPeriod = (ExecutionPeriod) ServiceUtils.executeService(userView,
                "ReadDomainObject", new Object[] { ExecutionPeriod.class, fromExecutionPeriodID });
        ExecutionPeriod untilExecutionPeriod = (ExecutionPeriod) ServiceUtils.executeService(userView,
                "ReadDomainObject", new Object[] { ExecutionPeriod.class, untilExecutionPeriodID });

        List<TeacherCreditsReportDTO> teacherCreditsReportList = new ArrayList<TeacherCreditsReportDTO>();
        Integer departmentID = Integer.parseInt(request.getParameter("departmentID"));
        Map<Department, List<TeacherCreditsReportDTO>> teachersCreditsByDepartment = new HashMap<Department, List<TeacherCreditsReportDTO>>();
        if (departmentID == 0) {
            List<Department> departments = (List<Department>) ServiceUtils.executeService(userView,
                    "ReadAllDomainObjects", new Object[] { Department.class });
            for (Department department : departments) {
                Unit unit = department.getUnit();
                teacherCreditsReportList = (List<TeacherCreditsReportDTO>) ServiceUtils.executeService(
                        userView, "ReadTeachersCreditsResumeByPeriodAndUnit", new Object[] { unit,
                                fromExecutionPeriod, untilExecutionPeriod });
                teachersCreditsByDepartment.put(department, teacherCreditsReportList);
            }
        } else {
            Integer unitID = Integer.parseInt(request.getParameter("unitID"));
            Unit unit = null;

            Department department = (Department) ServiceUtils.executeService(userView,
                    "ReadDomainObject", new Object[] { Department.class, departmentID });
            
            if (unitID == null || unitID == 0) {
                unit = department.getUnit();

            } else {
                unit = (Unit) ServiceUtils.executeService(userView, "ReadDomainObject", new Object[] {
                        Unit.class, unitID });
            }
            teacherCreditsReportList = (List<TeacherCreditsReportDTO>) ServiceUtils.executeService(
                    userView, "ReadTeachersCreditsResumeByPeriodAndUnit", new Object[] { unit,
                            fromExecutionPeriod, untilExecutionPeriod });
            teachersCreditsByDepartment.put(department, teacherCreditsReportList);
            request.setAttribute("department", department);
        }

        if (teachersCreditsByDepartment != null && !teachersCreditsByDepartment.isEmpty()) {
            Map<Department, Map<Unit, List>> teachersCreditsDisplayMap = new TreeMap<Department, Map<Unit, List>>(
                    new BeanComparator("name"));            
            for (Department department : teachersCreditsByDepartment.keySet()) {
                Map<Unit, List> teachersCreditsByUnit = new TreeMap(new BeanComparator("name"));
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
            return teachersCreditsDisplayMap;
        }
        return null;
    }

    /**
     * @param executionPeriods
     * @return
     */
    private List filterExecutionPeriods(List<ExecutionPeriod> executionPeriods) {
        List filteredExecutionPeriods = new ArrayList();
        for (ExecutionPeriod executionPeriod : executionPeriods) {
            if (!executionPeriod.getState().equals(PeriodState.CLOSED)
                    && !executionPeriod.getIdInternal().equals(1)) {
                String label = executionPeriod.getName() + " - "
                        + executionPeriod.getExecutionYear().getYear();
                filteredExecutionPeriods.add(new LabelValueBean(label, executionPeriod.getIdInternal()
                        .toString()));
            }
        }
        return filteredExecutionPeriods;
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
}
