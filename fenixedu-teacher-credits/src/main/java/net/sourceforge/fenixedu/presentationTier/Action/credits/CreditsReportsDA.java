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
package org.fenixedu.academic.ui.struts.action.credits;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.credits.AnnualCreditsState;
import org.fenixedu.academic.domain.credits.AnnualTeachingCredits;
import org.fenixedu.academic.domain.credits.util.DepartmentCreditsBean;
import org.fenixedu.academic.domain.organizationalStructure.PersonFunction;
import org.fenixedu.academic.domain.organizationalStructure.PersonFunctionShared;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.teacher.DegreeTeachingServiceCorrection;
import org.fenixedu.academic.domain.teacher.OtherService;
import org.fenixedu.academic.domain.teacher.TeacherService;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.scientificCouncil.ScientificCouncilApplication.ScientificCreditsApp;
import org.fenixedu.academic.util.Bundle;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.utl.ist.fenix.tools.util.excel.StyledExcelSpreadsheet;

@StrutsFunctionality(app = ScientificCreditsApp.class, path = "department-credits", titleKey = "label.department.credits",
        bundle = "TeacherCreditsSheetResources")
@Mapping(module = "scientificCouncil", path = "/exportCredits")
@Forwards({ @Forward(name = "exportDepartmentCourses", path = "/credits/export/exportDepartmentCourses.jsp"),
        @Forward(name = "exportDepartmentCredits", path = "/credits/export/exportDepartmentCredits.jsp") })
public class CreditsReportsDA extends FenixDispatchAction {

    public ActionForward prepareExportDepartmentCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException {
        DepartmentCreditsBean departmentCreditsBean = new DepartmentCreditsBean();
        departmentCreditsBean.setAvailableDepartments(Department.readActiveDepartments());
        request.setAttribute("departmentCreditsBean", departmentCreditsBean);
        return mapping.findForward("exportDepartmentCourses");
    }

    @EntryPoint
    public ActionForward prepareExportDepartmentCredits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        DepartmentCreditsBean departmentCreditsBean = new DepartmentCreditsBean();
        departmentCreditsBean.setAvailableDepartments(Department.readActiveDepartments());
        request.setAttribute("departmentCreditsBean", departmentCreditsBean);
        return mapping.findForward("exportDepartmentCredits");
    }

    public ActionForward exportDepartmentCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException, IOException {
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
            spreadsheet.addHeader(BundleUtil.getString(Bundle.TEACHER_CREDITS, "label.course"), 10000);
            spreadsheet.addHeader(BundleUtil.getString(Bundle.TEACHER_CREDITS, "label.degrees"));
            spreadsheet.addHeader(BundleUtil.getString(Bundle.TEACHER_CREDITS, "label.shift.type"));
            spreadsheet.addHeader(BundleUtil.getString(Bundle.TEACHER_CREDITS, "label.hasSchedule"));
            spreadsheet.addHeader(BundleUtil.getString(Bundle.TEACHER_CREDITS, "label.enrolmentsNumber"));
            for (ExecutionCourse executionCourse : department.getDepartmentUnit().getAllExecutionCoursesByExecutionPeriod(
                    departmentCreditsBean.getExecutionSemester())) {
                spreadsheet.newRow();
                spreadsheet.addCell(executionCourse.getName());
                spreadsheet.addCell(executionCourse.getDegreePresentationString());
                spreadsheet
                        .addCell(executionCourse.isDissertation() ? "DISS" : executionCourse.getProjectTutorialCourse() ? "A" : "B");
                spreadsheet.addCell(executionCourse.hasAnyLesson() ? "S" : "N");
                spreadsheet.addCell(executionCourse.getEnrolmentCount());
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
            HttpServletResponse response) throws NumberFormatException, FenixServiceException, IOException {
        DepartmentCreditsBean departmentCreditsBean = getRenderedObject();
        List<Department> departments = new ArrayList<Department>();
        if (departmentCreditsBean.getDepartment() != null) {
            departments.add(departmentCreditsBean.getDepartment());
        } else {
            departments.addAll(departmentCreditsBean.getAvailableDepartments());
        }
        StyledExcelSpreadsheet spreadsheet = new StyledExcelSpreadsheet();

        for (Department department : departments) {
            String sheetName = "Cargos_" + department.getAcronym();
            spreadsheet.getSheet(sheetName);
            spreadsheet.newHeaderRow();
            spreadsheet.addHeader(BundleUtil.getString(Bundle.TEACHER_CREDITS, "label.teacher.id", Unit.getInstitutionAcronym()));
            spreadsheet.addHeader(BundleUtil.getString(Bundle.TEACHER_CREDITS, "label.name"), 10000);
            spreadsheet.addHeader(BundleUtil.getString(Bundle.TEACHER_CREDITS, "label.managementPosition.position"), 10000);
            spreadsheet.addHeader(BundleUtil.getString(Bundle.TEACHER_CREDITS, "label.managementPosition.unit"), 10000);
            spreadsheet.addHeader(BundleUtil.getString(Bundle.TEACHER_CREDITS, "label.teacher-dfp-student.percentage"));
            spreadsheet.addHeader(BundleUtil.getString(Bundle.TEACHER_CREDITS, "label.managementPosition.credits"));
            for (Teacher teacher : department.getAllTeachers(departmentCreditsBean.getExecutionSemester())) {

                for (PersonFunction personFunction : PersonFunction.getPersonFuntions(teacher.getPerson(), departmentCreditsBean
                        .getExecutionSemester().getBeginDateYearMonthDay(), departmentCreditsBean.getExecutionSemester()
                        .getEndDateYearMonthDay())) {
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

    public ActionForward exportDepartmentCredits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException, IOException {
        DepartmentCreditsBean departmentCreditsBean = getRenderedObject();
        List<Department> departments = new ArrayList<Department>();
        if (departmentCreditsBean.getDepartment() != null) {
            departments.add(departmentCreditsBean.getDepartment());
        } else {
            departments.addAll(departmentCreditsBean.getAvailableDepartments());
        }
        StyledExcelSpreadsheet spreadsheet = new StyledExcelSpreadsheet();
        for (Department department : departments) {
            spreadsheet.getSheet(department.getAcronym());
            spreadsheet.newHeaderRow();

            spreadsheet.addHeader(BundleUtil.getString(Bundle.TEACHER_CREDITS, "label.teacher.id", Unit.getInstitutionAcronym()));
            spreadsheet.addHeader(BundleUtil.getString(Bundle.TEACHER_CREDITS, "label.name"), 10000);

            spreadsheet.addHeader(BundleUtil.getString(Bundle.TEACHER_CREDITS, "label.credits.teachingCredits.simpleCode"));
            //spreadsheet.addHeader("CL correcções");
            spreadsheet.addHeader(BundleUtil.getString(Bundle.TEACHER_CREDITS, "label.credits.masterDegreeTheses.simpleCode"));
            spreadsheet.addHeader(BundleUtil.getString(Bundle.TEACHER_CREDITS, "label.credits.phdDegreeTheses.simpleCode"));
            spreadsheet.addHeader(BundleUtil.getString(Bundle.TEACHER_CREDITS, "label.credits.projectsAndTutorials.simpleCode"));

            spreadsheet.addHeader(BundleUtil.getString(Bundle.TEACHER_CREDITS, "label.credits.managementPositions.simpleCode"));
            spreadsheet.addHeader(BundleUtil.getString(Bundle.TEACHER_CREDITS, "label.credits.otherCredits.simpleCode"));
            spreadsheet.addHeader(BundleUtil.getString(Bundle.TEACHER_CREDITS, "label.credits.creditsReduction.simpleCode"));
            spreadsheet.addHeader(BundleUtil.getString(Bundle.TEACHER_CREDITS,
                    "label.credits.serviceExemptionSituations.simpleCode"));
            spreadsheet.addHeader(BundleUtil.getString(Bundle.TEACHER_CREDITS,
                    "label.credits.normalizedAcademicCredits.simpleCode"));
            spreadsheet.addHeader(BundleUtil.getString(Bundle.TEACHER_CREDITS, "label.credits.yearCredits.simpleCode"));
            spreadsheet.addHeader(BundleUtil.getString(Bundle.TEACHER_CREDITS, "label.credits.finalCredits.simpleCode"));
            spreadsheet.addHeader(BundleUtil.getString(Bundle.TEACHER_CREDITS, "label.credits.accumulatedCredits.simpleCode"));

            AnnualCreditsState annualCreditsState =
                    AnnualCreditsState.getAnnualCreditsState(departmentCreditsBean.getExecutionYear());
            if (annualCreditsState.getIsFinalCreditsCalculated()) {
                for (AnnualTeachingCredits annualTeachingCredits : annualCreditsState.getAnnualTeachingCreditsSet()) {
                    Teacher teacher = annualTeachingCredits.getTeacher();
                    Department teacherDepartment =
                            teacher.getLastDepartment(departmentCreditsBean.getExecutionYear().getAcademicInterval());
                    if (teacherDepartment != null && teacherDepartment.equals(department)) {
                        spreadsheet.newRow();
                        spreadsheet.addCell(teacher.getTeacherId());
                        spreadsheet.addCell(teacher.getPerson().getName());
                        spreadsheet.addCell(annualTeachingCredits.getTeachingCredits().setScale(2, BigDecimal.ROUND_HALF_UP)
                                .doubleValue());

//                        BigDecimal correcredCL = getCorrectedCL(departmentCreditsBean, teacher);
//                        spreadsheet.addCell(correcredCL.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

                        spreadsheet.addCell(annualTeachingCredits.getMasterDegreeThesesCredits().doubleValue());
                        spreadsheet.addCell(annualTeachingCredits.getPhdDegreeThesesCredits().doubleValue());
                        spreadsheet.addCell(annualTeachingCredits.getProjectsTutorialsCredits().doubleValue());
                        spreadsheet.addCell(annualTeachingCredits.getManagementFunctionCredits()
                                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        spreadsheet.addCell(annualTeachingCredits.getOthersCredits().setScale(2, BigDecimal.ROUND_HALF_UP)
                                .doubleValue());
                        spreadsheet.addCell("-");
                        spreadsheet.addCell(annualTeachingCredits.getServiceExemptionCredits()
                                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        spreadsheet.addCell(annualTeachingCredits.getAnnualTeachingLoad().doubleValue());
                        spreadsheet.addCell(annualTeachingCredits.getYearCredits().doubleValue());
                        spreadsheet.addCell(annualTeachingCredits.getFinalCredits().doubleValue());
                        spreadsheet.addCell(annualTeachingCredits.getAccumulatedCredits());
                    }
                }
            }
        }

        response.setContentType("text/plain");
        StringBuilder filename = new StringBuilder("creditos");
        filename.append((departments.size() == 1 ? departments.iterator().next().getAcronym() : "Departamentos"));
        filename.append("_").append(departmentCreditsBean.getExecutionYear().getQualifiedName().replaceAll("/", "_"))
                .append(".xls");
        response.setHeader("Content-disposition", "attachment; filename=" + filename.toString());
        final ServletOutputStream writer = response.getOutputStream();
        spreadsheet.getWorkbook().write(writer);
        writer.flush();
        response.flushBuffer();
        return null;
    }

    protected BigDecimal getCorrectedCL(DepartmentCreditsBean departmentCreditsBean, Teacher teacher) {
        BigDecimal correcredCL = BigDecimal.ZERO;
        for (ExecutionSemester executionSemester : departmentCreditsBean.getExecutionYear().getExecutionPeriodsSet()) {
            TeacherService teacherService = TeacherService.getTeacherServiceByExecutionPeriod(teacher, executionSemester);
            if (teacherService != null) {
                for (OtherService otherService : teacherService.getOtherServices()) {
                    if (otherService instanceof DegreeTeachingServiceCorrection) {
                        DegreeTeachingServiceCorrection degreeTeachingServiceCorrection =
                                (DegreeTeachingServiceCorrection) otherService;
                        if ((!degreeTeachingServiceCorrection.getProfessorship().getExecutionCourse().isDissertation())
                                && (!degreeTeachingServiceCorrection.getProfessorship().getExecutionCourse()
                                        .getProjectTutorialCourse())) {
                            correcredCL =
                                    correcredCL.add(degreeTeachingServiceCorrection.getCorrection().multiply(
                                            degreeTeachingServiceCorrection.getProfessorship().getExecutionCourse()
                                                    .getUnitCreditValue()));
                        }
                    }
                }
            }
        }
        return correcredCL;
    }
}