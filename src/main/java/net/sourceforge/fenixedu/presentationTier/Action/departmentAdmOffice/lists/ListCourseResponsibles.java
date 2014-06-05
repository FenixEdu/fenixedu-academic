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
package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice.lists;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.academicAdministration.SearchStudentsByCurricularCourseParametersBean;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists.SearchCourseResponsiblesParametersBean;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice.DepartmentAdmOfficeApp.DepartmentAdmOfficeListingsApp;
import net.sourceforge.fenixedu.util.Bundle;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
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
            for (ExecutionCourse execCourse : execSemester.getAssociatedExecutionCourses()) {
                for (Professorship professorship : execCourse.getProfessorships()) {
                    if (professorship.isResponsibleFor()) {
                        Teacher teacher = professorship.getTeacher();
                        Department dept =
                                teacher.getLastWorkingDepartment(executionYear.getBeginDateYearMonthDay(),
                                        executionYear.getEndDateYearMonthDay());
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
