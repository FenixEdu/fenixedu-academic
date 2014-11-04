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
package org.fenixedu.academic.ui.struts.action.departmentAdmOffice.lists;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.dto.academicAdministration.SearchStudentsByCurricularCourseParametersBean;
import org.fenixedu.academic.dto.administrativeOffice.lists.SearchCourseResponsiblesParametersBean;
import org.fenixedu.academic.domain.CompetenceCourse;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.departmentAdmOffice.DepartmentAdmOfficeApp.DepartmentAdmOfficeListingsApp;
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

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

@StrutsFunctionality(app = DepartmentAdmOfficeListingsApp.class, path = "list-course-responsibles",
        titleKey = "link.listCourseResponsibles")
@Mapping(path = "/listCourseResponsibles", module = "departmentAdmOffice")
@Forwards(@Forward(name = "chooseCurricularCourse", path = "/departmentAdmOffice/lists/listCourseResponsibles.jsp"))
public class ListCourseResponsibles extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepareByCurricularCourse(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("searchBean", getOrCreateSearchBean());
        return mapping.findForward("chooseCurricularCourse");
    }

    private SearchStudentsByCurricularCourseParametersBean getOrCreateSearchBean() {
        SearchStudentsByCurricularCourseParametersBean bean = getRenderedObject("searchBean");
        if (bean == null) {
            bean =
                    new SearchStudentsByCurricularCourseParametersBean(new TreeSet<Degree>(AccessControl.getPerson()
                            .getEmployee().getCurrentDepartmentWorkingPlace().getDegreesSet()));
        }
        return bean;
    }

    public ActionForward chooseExecutionYearPostBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        SearchStudentsByCurricularCourseParametersBean searchBean = getOrCreateSearchBean();
        RenderUtils.invalidateViewState();
        request.setAttribute("searchBean", searchBean);
        request.setAttribute("courseResponsibles", getCourseResponsibles(request, searchBean));

        return mapping.findForward("chooseCurricularCourse");
    }

    private Department getDepartment(final HttpServletRequest request) {
        return getUserView(request).getPerson().getEmployee().getCurrentDepartmentWorkingPlace();
    }

    private List<SearchCourseResponsiblesParametersBean> getCourseResponsibles(HttpServletRequest request,
            SearchStudentsByCurricularCourseParametersBean searchBean) {

        final List<SearchCourseResponsiblesParametersBean> result = new ArrayList<SearchCourseResponsiblesParametersBean>();
        final Department department = getDepartment(request);
        final ExecutionYear executionYear = searchBean.getExecutionYear();
        int semester;

        for (semester = 1; semester <= 2; semester++) {
            ExecutionSemester execSemester = executionYear.getExecutionSemesterFor(semester);
            for (ExecutionCourse execCourse : execSemester.getAssociatedExecutionCoursesSet()) {
                for (Professorship professorship : execCourse.getProfessorshipsSet()) {
                    if (professorship.isResponsibleFor()) {
                        Teacher teacher = professorship.getTeacher();
                        Department dept = teacher.getLastDepartment(executionYear.getAcademicInterval());
                        CurricularCourse curricCourse = null;
                        CompetenceCourse compCourse = null;
                        ExecutionDegree execDegree = null;

                        if (dept != null && dept.equals(department)) {
                            for (CurricularCourse aux : execCourse.getAssociatedCurricularCoursesSet()) {
                                curricCourse = aux;

                                if (curricCourse != null) {
                                    compCourse = curricCourse.getCompetenceCourse();
                                    execDegree = curricCourse.getExecutionDegreeFor(executionYear);

                                    if (compCourse != null && execDegree != null) {
                                        SearchCourseResponsiblesParametersBean bean =
                                                new SearchCourseResponsiblesParametersBean(curricCourse, compCourse,
                                                        professorship.getPerson(), execSemester, execDegree.getCampus(),
                                                        execDegree.getDegree());
                                        result.add(bean);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    private ExecutionYear getExecutionYearParameter(final HttpServletRequest request) {
        final String executionYearIdString = request.getParameter("executionYearId");
        return FenixFramework.getDomainObject(executionYearIdString);
    }

    public ActionForward downloadStatistics(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        SearchStudentsByCurricularCourseParametersBean searchBean = getOrCreateSearchBean();
        final ExecutionYear executionYear = getExecutionYearParameter(request);
        searchBean.setExecutionYear(executionYear);

        final String filename = getResourceMessage("label.statistics") + "_" + executionYear.getName().replace('/', '-');
        final Spreadsheet spreadsheet = new Spreadsheet(filename);
        addStatisticsHeaders(spreadsheet);
        addStatisticsInformation(spreadsheet, getCourseResponsibles(request, searchBean));

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment; filename=" + filename + ".xls");
        ServletOutputStream writer = response.getOutputStream();

        spreadsheet.exportToXLSSheet(writer);
        writer.flush();
        response.flushBuffer();

        return null;
    }

    private void addStatisticsHeaders(final Spreadsheet spreadsheet) {
        spreadsheet.setHeader(getResourceMessage("label.curricular.course.from.curriculum"));
        spreadsheet.setHeader(getResourceMessage("label.competence.course.name"));
        spreadsheet.setHeader(getResourceMessage("degree"));
        spreadsheet.setHeader(getResourceMessage("campus"));
        spreadsheet.setHeader(getResourceMessage("label.responsible"));
        spreadsheet.setHeader(getResourceMessage("label.semester"));
    }

    static private String getResourceMessage(String key) {
        return BundleUtil.getString(Bundle.ACADEMIC, key);
    }

    private void addStatisticsInformation(final Spreadsheet spreadsheet,
            List<SearchCourseResponsiblesParametersBean> responsibleList) {

        for (SearchCourseResponsiblesParametersBean bean : responsibleList) {
            final Row row = spreadsheet.addRow();
            row.setCell(bean.getCurricularCourse().getName());
            row.setCell(bean.getCompetenceCourse().getName());
            row.setCell(bean.getDegree().getSigla());
            row.setCell(bean.getCampus().getName());
            row.setCell(bean.getResponsible().getName());
            row.setCell(bean.getExecutionSemester().getSemester().toString());
        }
    }

}
