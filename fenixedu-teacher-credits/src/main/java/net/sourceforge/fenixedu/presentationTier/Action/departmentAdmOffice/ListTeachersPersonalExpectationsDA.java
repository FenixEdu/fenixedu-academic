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
package org.fenixedu.academic.ui.struts.action.departmentAdmOffice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.dto.commons.ExecutionYearBean;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.personnelSection.contracts.ProfessionalCategory;
import org.fenixedu.academic.domain.teacher.TeacherPersonalExpectation;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.departmentAdmOffice.DepartmentAdmOfficeApp.DepartmentAdmOfficeExpectationsApp;
import org.fenixedu.academic.util.HtmlToTextConverterUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.components.converters.ConversionException;
import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

@StrutsFunctionality(app = DepartmentAdmOfficeExpectationsApp.class, path = "list-teachers-personal-expectations",
        titleKey = "link.see.teachers.personal.expectations")
@Mapping(module = "departmentAdmOffice", path = "/listTeachersPersonalExpectations")
@Forwards({
        @Forward(name = "seeTeacherPersonalExpectationsByYear",
                path = "/departmentAdmOffice/teacher/teacherPersonalExpectationsManagement/seeTeacherPersonalExpectations.jsp"),
        @Forward(name = "listTeacherPersonalExpectations",
                path = "/departmentAdmOffice/teacher/teacherPersonalExpectationsManagement/listTeacherPersonalExpectations.jsp") })
public class ListTeachersPersonalExpectationsDA extends FenixDispatchAction {

    public ActionForward listTeachersPersonalExpectationsForSelectedExecutionYear(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        IViewState viewState = RenderUtils.getViewState("executionYear");
        ExecutionYear executionYear = (ExecutionYear) viewState.getMetaObject().getObject();
        return readAndSetList(mapping, request, executionYear);
    }

    public ActionForward listTeachersPersonalExpectationsByExecutionYear(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        ExecutionYear executionYear = getExecutionYearFromParameter(request);
        return readAndSetList(mapping, request, executionYear);
    }

    @EntryPoint
    public ActionForward listTeachersPersonalExpectations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
        return readAndSetList(mapping, request, executionYear);
    }

    public ActionForward seeTeacherPersonalExpectation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        TeacherPersonalExpectation teacherPersonalExpectation = getTeacherPersonalExpectationFromParameter(request);
        Teacher teacher = teacherPersonalExpectation.getTeacher();
        ExecutionYear executionYear = teacherPersonalExpectation.getExecutionYear();

        Department teacherWorkingDepartment = teacher.getLastDepartment(executionYear.getAcademicInterval());
        Department employeeDepartment = getDepartment(request);

        if (teacherWorkingDepartment != null && teacherWorkingDepartment.equals(employeeDepartment)) {
            request.setAttribute("noEdit", true);
            request.setAttribute("teacherPersonalExpectation", teacherPersonalExpectation);
        }

        return mapping.findForward("seeTeacherPersonalExpectationsByYear");
    }

    public ActionForward exportToExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ExecutionYear executionYear = getExecutionYearFromParameter(request);
        Map<Teacher, TeacherPersonalExpectation> expectationsMap = getExpectationsMap(request, executionYear);

        try {
            String filename = getFileName(executionYear, request);
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment; filename=" + filename + ".xls");

            ServletOutputStream writer = response.getOutputStream();
            createAndFillExcelFile(expectationsMap, writer, request);

            writer.flush();
            response.flushBuffer();

        } catch (IOException e) {
            throw new FenixServiceException();
        }
        return null;
    }

    public ActionForward exportToCSV(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ExecutionYear executionYear = getExecutionYearFromParameter(request);
        Map<Teacher, TeacherPersonalExpectation> expectationsMap = getExpectationsMap(request, executionYear);

        try {
            String filename = getFileName(executionYear, request);
            response.setContentType("text/plain");
            response.setHeader("Content-disposition", "attachment; filename=" + filename + ".csv");

            ServletOutputStream writer = response.getOutputStream();
            createAndFillCSVFile(expectationsMap, writer, request);

            writer.flush();
            response.flushBuffer();

        } catch (IOException e) {
            throw new FenixServiceException();
        }
        return null;
    }

    // private methods

    protected ActionForward readAndSetList(ActionMapping mapping, HttpServletRequest request, ExecutionYear executionYear) {
        Map<Teacher, TeacherPersonalExpectation> result = getExpectationsMap(request, executionYear);
        request.setAttribute("executionYearBean", new ExecutionYearBean(executionYear));
        request.setAttribute("teachersPersonalExpectations", result);
        return mapping.findForward("listTeacherPersonalExpectations");
    }

    private Map<Teacher, TeacherPersonalExpectation> getExpectationsMap(HttpServletRequest request, ExecutionYear executionYear) {
        Department department = getDepartment(request);
        Map<Teacher, TeacherPersonalExpectation> result =
                new TreeMap<Teacher, TeacherPersonalExpectation>(Teacher.TEACHER_COMPARATOR_BY_CATEGORY_AND_NUMBER);
        if (executionYear != null && department != null) {
            List<Teacher> allCurrentTeachers = department.getAllTeachers(executionYear);
            for (Teacher teacher : allCurrentTeachers) {
                TeacherPersonalExpectation teacherPersonalExpectation =
                        TeacherPersonalExpectation.getTeacherPersonalExpectationByExecutionYear(teacher, executionYear);
                result.put(teacher, teacherPersonalExpectation);
            }
        }
        return result;
    }

    protected TeacherPersonalExpectation getTeacherPersonalExpectationFromParameter(final HttpServletRequest request) {
        final String teacherPersonalExpectationIDString = request.getParameter("teacherPersonalExpectationID");
        return FenixFramework.getDomainObject(teacherPersonalExpectationIDString);
    }

    protected ExecutionYear getExecutionYearFromParameter(final HttpServletRequest request) {
        final String executionYearIDString = request.getParameter("executionYearId");
        return FenixFramework.getDomainObject(executionYearIDString);
    }

    private Department getDepartment(HttpServletRequest request) {
        return getLoggedPerson(request).getEmployee().getCurrentDepartmentWorkingPlace();
    }

    private void createAndFillCSVFile(Map<Teacher, TeacherPersonalExpectation> expectationsMap, ServletOutputStream writer,
            HttpServletRequest request) throws IOException {
        final Spreadsheet spreadsheet = new Spreadsheet(getDepartment(request).getAcronym());
        createAndFillSpreadsheet(spreadsheet, expectationsMap, request);
        spreadsheet.exportToCSV(writer, ";");
    }

    private void createAndFillExcelFile(Map<Teacher, TeacherPersonalExpectation> expectationsMap, ServletOutputStream writer,
            HttpServletRequest request) throws IOException {
        final List<Object> headers = getSpreadsheetHeaders();
        final Spreadsheet spreadsheet = new Spreadsheet(getDepartment(request).getAcronym(), headers);
        createAndFillSpreadsheet(spreadsheet, expectationsMap, request);
        spreadsheet.exportToXLSSheet(writer);
    }

    private void createAndFillSpreadsheet(Spreadsheet spreadsheet, Map<Teacher, TeacherPersonalExpectation> expectationsMap,
            HttpServletRequest request) {
        for (Teacher teacher : expectationsMap.keySet()) {

            final Row row = spreadsheet.addRow();
            row.setCell(teacher.getPerson().getName());
            row.setCell(teacher.getPerson().getUsername());
            ProfessionalCategory professionalCategory = ProfessionalCategory.getCategory(teacher);
            String category = professionalCategory != null ? professionalCategory.getName().getContent() : null;
            row.setCell(category);

            TeacherPersonalExpectation expectation = expectationsMap.get(teacher);
            if (expectation != null) {

                HtmlToTextConverterUtil converter = new HtmlToTextConverterUtil();

                row.setCell(expectation.getGraduations().toString());
                row.setCell(getTextValue(expectation.getGraduationsDescription(), converter));
                row.setCell(expectation.getCientificPosGraduations().toString());
                row.setCell(getTextValue(expectation.getCientificPosGraduationsDescription(), converter));
                row.setCell(expectation.getProfessionalPosGraduations().toString());
                row.setCell(getTextValue(expectation.getProfessionalPosGraduationsDescription(), converter));
                row.setCell(expectation.getSeminaries().toString());
                row.setCell(getTextValue(expectation.getSeminariesDescription(), converter));
                row.setCell(getTextValue(expectation.getEducationMainFocus(), converter));

                row.setCell(expectation.getResearchAndDevProjects().toString());
                row.setCell(expectation.getJornalArticlePublications().toString());
                row.setCell(expectation.getBookPublications().toString());
                row.setCell(expectation.getConferencePublications().toString());
                row.setCell(expectation.getTechnicalReportPublications().toString());
                row.setCell(expectation.getPatentPublications().toString());
                row.setCell(expectation.getOtherPublications().toString());
                row.setCell(getTextValue(expectation.getOtherPublicationsDescription(), converter));
                row.setCell(getTextValue(expectation.getResearchAndDevMainFocus(), converter));

                row.setCell(expectation.getPhdOrientations().toString());
                row.setCell(expectation.getMasterDegreeOrientations().toString());
                row.setCell(expectation.getFinalDegreeWorkOrientations().toString());
                row.setCell(getTextValue(expectation.getOrientationsMainFocus(), converter));

                row.setCell(getTextValue(expectation.getDepartmentOrgans(), converter));
                row.setCell(getTextValue(expectation.getInstitutionOrgans(), converter));
                row.setCell(getTextValue(expectation.getUniversityOrgans(), converter));
                row.setCell(getTextValue(expectation.getUniversityServiceMainFocus(), converter));

                row.setCell(getTextValue(expectation.getCientificComunityService(), converter));
                row.setCell(getTextValue(expectation.getSocietyService(), converter));
                row.setCell(getTextValue(expectation.getConsulting(), converter));
                row.setCell(getTextValue(expectation.getCompanySocialOrgans(), converter));
                row.setCell(getTextValue(expectation.getCompanyPositions(), converter));
                row.setCell(getTextValue(expectation.getProfessionalActivityMainFocus(), converter));

                row.setCell(getTextValue(expectation.getAutoEvaluation(), converter));

            } else {
                for (int i = 1; i <= 33; i++) {
                    row.setCell("-");
                }
            }
        }
    }

    private String getTextValue(String html, HtmlToTextConverterUtil converter) {
        try {
            return converter.convertToText(html);
        } catch (ConversionException e) {
            return html;
        }
    }

    private String getFileName(ExecutionYear executionYear, HttpServletRequest request) {
        return "Expectativas(" + executionYear.getYear() + "):" + getDepartment(request).getAcronym();
    }

    private List<Object> getSpreadsheetHeaders() {
        List<Object> headers = new ArrayList<Object>();

        headers.add("Nome");
        headers.add("Identificação");
        headers.add("Categoria");

        headers.add("Graduações");
        headers.add("Descrição (Graduações)");
        headers.add("Pós-Graduações Científicas");
        headers.add("Descrição (Pós-Graduações Científicas)");
        headers.add("Pós-Graduações Profissionais");
        headers.add("Descrição (Pós-Graduações Profissionais)");
        headers.add("Seminários");
        headers.add("Descrição (Seminários)");
        headers.add("Foco Principal(Ensino)");

        headers.add("Projectos de I&D");
        headers.add("Publ. de Artigos de Jornal");
        headers.add("Publ. Livros");
        headers.add("Publ. Conferências");
        headers.add("Publ. Relatórios Técnicos");
        headers.add("Publ. Patentes");
        headers.add("Outras Publ.");
        headers.add("Descrição (Outras Publicações)");
        headers.add("Foco Principal(Publicações e Projectos)");

        headers.add("Orientações de Doutoramento");
        headers.add("Orientações de Mestrado");
        headers.add("Orientações de Trabalhos Finais de Curso");
        headers.add("Foco Principal(Orientações)");

        headers.add("Órgãos do Departamento");
        headers.add("Órgãos do " + Unit.getInstitutionAcronym());
        headers.add("Órgãos da Universidade Técnica");
        headers.add("Foco Principal (Serviço à Universidade)");

        headers.add("Serviços à Comunidade Científica");
        headers.add("Serviços à Sociedade");
        headers.add("Consultadoria");
        headers.add("Órgãos Sociais de Empresas");
        headers.add("Cargos em Empresas");
        headers.add("Foco Principal (Actividades Profissionais)");

        headers.add("Auto-Avaliação");

        return headers;
    }
}
