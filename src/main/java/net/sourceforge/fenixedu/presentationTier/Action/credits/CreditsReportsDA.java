package net.sourceforge.fenixedu.presentationTier.Action.credits;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.credits.AnnualCreditsState;
import net.sourceforge.fenixedu.domain.credits.util.DepartmentCreditsBean;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunctionShared;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.BundleUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.utl.ist.fenix.tools.util.excel.StyledExcelSpreadsheet;

@Mapping(module = "scientificCouncil", path = "/exportCredits", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "exportDepartmentCourses", path = "/credits/export/exportDepartmentCourses.jsp",
        tileProperties = @Tile(title = "private.department.coursestypes")) })
public class CreditsReportsDA extends FenixDispatchAction {

    public ActionForward prepareExportDepartmentCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException {
        DepartmentCreditsBean departmentCreditsBean = new DepartmentCreditsBean();
        departmentCreditsBean.setAvailableDepartments(Department.readActiveDepartments());
        request.setAttribute("departmentCreditsBean", departmentCreditsBean);
        return mapping.findForward("exportDepartmentCourses");
    }

    public ActionForward exportDepartmentCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException, IOException {
        DepartmentCreditsBean departmentCreditsBean = getRenderedObject();
        List<Department> departments = new ArrayList<Department>();
        if (departmentCreditsBean.getDepartment() != null) {
            departments.add(departmentCreditsBean.getDepartment());
        } else {
            departments.addAll(departmentCreditsBean.getAvailableDepartments());
        }
        StyledExcelSpreadsheet spreadsheet = new StyledExcelSpreadsheet();
        for (Department department : departments) {
            String sheetName = "Disciplinas_" + department.getAcronym();
            spreadsheet.getSheet(sheetName);
            spreadsheet.newHeaderRow();
            spreadsheet.addHeader(
                    BundleUtil.getStringFromResourceBundle("resources.TeacherCreditsSheetResources", "label.course"), 10000);
            spreadsheet.addHeader(BundleUtil.getStringFromResourceBundle("resources.TeacherCreditsSheetResources",
                    "label.degrees"));
            spreadsheet.addHeader(BundleUtil.getStringFromResourceBundle("resources.TeacherCreditsSheetResources",
                    "label.shift.type"));
            spreadsheet.addHeader(BundleUtil.getStringFromResourceBundle("resources.TeacherCreditsSheetResources",
                    "label.hasSchedule"));
            spreadsheet.addHeader(BundleUtil.getStringFromResourceBundle("resources.TeacherCreditsSheetResources",
                    "label.enrolmentsNumber"));
            for (CompetenceCourse competenceCourse : department.getDepartmentUnit().getCompetenceCourses()) {
                for (ExecutionCourse executionCourse : competenceCourse
                        .getExecutionCoursesByExecutionPeriod(departmentCreditsBean.getExecutionSemester())) {
                    spreadsheet.newRow();
                    spreadsheet.addCell(executionCourse.getName());
                    spreadsheet.addCell(executionCourse.getDegreePresentationString());
                    spreadsheet
                            .addCell(executionCourse.isDissertation() ? "DISS" : executionCourse.getProjectTutorialCourse() ? "A" : "B");
                    spreadsheet.addCell(executionCourse.hasAnyLesson() ? "S" : "N");
                    spreadsheet.addCell(executionCourse.getEnrolmentCount());
                }
            }
        }

        response.setContentType("text/plain");
        response.setHeader("Content-disposition", "attachment; filename=Disciplinas.xls");
        final ServletOutputStream writer = response.getOutputStream();
        spreadsheet.getWorkbook().write(writer);
        writer.flush();
        response.flushBuffer();
        return null;
    }

    public ActionForward exportDepartmentPersonFunctions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException, IOException {
        DepartmentCreditsBean departmentCreditsBean = getRenderedObject();
        List<Department> departments = new ArrayList<Department>();
        if (departmentCreditsBean.getDepartment() != null) {
            departments.add(departmentCreditsBean.getDepartment());
        } else {
            departments.addAll(departmentCreditsBean.getAvailableDepartments());
        }
        StyledExcelSpreadsheet spreadsheet = new StyledExcelSpreadsheet();

        AnnualCreditsState annualCreditsState =
                AnnualCreditsState.getAnnualCreditsState(departmentCreditsBean.getExecutionSemester().getExecutionYear());
        for (Department department : departments) {
            String sheetName = "Cargos_" + department.getAcronym();
            spreadsheet.getSheet(sheetName);
            spreadsheet.newHeaderRow();
            spreadsheet.addHeader(BundleUtil.getStringFromResourceBundle("resources.TeacherCreditsSheetResources",
                    "label.teacher.id"));
            spreadsheet.addHeader(BundleUtil.getStringFromResourceBundle("resources.TeacherCreditsSheetResources", "label.name"),
                    10000);
            spreadsheet.addHeader(BundleUtil.getStringFromResourceBundle("resources.TeacherCreditsSheetResources",
                    "label.managementPosition.position"), 10000);
            spreadsheet.addHeader(BundleUtil.getStringFromResourceBundle("resources.TeacherCreditsSheetResources",
                    "label.managementPosition.unit"), 10000);
            spreadsheet.addHeader(BundleUtil.getStringFromResourceBundle("resources.TeacherCreditsSheetResources",
                    "label.teacher-dfp-student.percentage"));
            spreadsheet.addHeader(BundleUtil.getStringFromResourceBundle("resources.TeacherCreditsSheetResources",
                    "label.managementPosition.credits"));
            for (Teacher teacher : department.getAllTeachers(departmentCreditsBean.getExecutionSemester().getAcademicInterval())) {

                for (PersonFunction personFunction : teacher.getPerson().getPersonFuntions(
                        departmentCreditsBean.getExecutionSemester().getBeginDateYearMonthDay(),
                        departmentCreditsBean.getExecutionSemester().getEndDateYearMonthDay())) {
                    spreadsheet.newRow();
                    spreadsheet.addCell(personFunction.getPerson().getUsername());
                    spreadsheet.addCell(personFunction.getPerson().getName());
                    spreadsheet.addCell(personFunction.getFunction().getName());
                    spreadsheet.addCell(personFunction.getFunction().getUnit().getPresentationName());
                    spreadsheet.addCell(personFunction.isPersonFunctionShared() ? ((PersonFunctionShared) personFunction)
                            .getPercentage() : "-");
                    spreadsheet.addCell(personFunction.getCredits());
                }
            }
        }

        response.setContentType("text/plain");
        String filename = "cargos_" + departmentCreditsBean.getExecutionSemester().getQualifiedName().replaceAll(" ", "_");
        response.setHeader("Content-disposition", "attachment; filename=" + filename + ".xls");
        final ServletOutputStream writer = response.getOutputStream();
        spreadsheet.getWorkbook().write(writer);
        writer.flush();
        response.flushBuffer();
        return null;
    }
}