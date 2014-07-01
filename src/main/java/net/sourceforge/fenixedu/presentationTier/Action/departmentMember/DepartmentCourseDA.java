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
package net.sourceforge.fenixedu.presentationTier.Action.departmentMember;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.studentCurriculum.BranchCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.departmentMember.DepartmentMemberApp.DepartmentMemberDepartmentApp;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.util.Bundle;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

@StrutsFunctionality(app = DepartmentMemberDepartmentApp.class, path = "courses", titleKey = "link.departmentCourses")
@Mapping(path = "/departmentCourses", module = "departmentMember")
@Forwards({ @Forward(name = "viewDegreeCourses", path = "/departmentMember/courseStatistics/viewDegreeCourses.jsp"),
        @Forward(name = "viewCompetenceCourses", path = "/departmentMember/courseStatistics/viewCompetenceCourses.jsp"),
        @Forward(name = "viewExecutionCourses", path = "/departmentMember/courseStatistics/viewExecutionCourses.jsp") })
public class DepartmentCourseDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepareListCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {
        CourseStatisticsBean bean = getRenderedObject("courseStatisticsBean");
        RenderUtils.invalidateViewState();
        if (bean == null) {
            ExecutionSemester executionSemester = getDomainObject(request, "executionSemesterId");
            bean = new CourseStatisticsBean(getDepartment(request), executionSemester);
        }
        request.setAttribute("courseStatisticsBean", bean);

        return mapping.findForward("viewCompetenceCourses");
    }

    public ActionForward prepareDegreeCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {

        CourseStatisticsBean bean = getRenderedObject("courseStatisticsBean");
        RenderUtils.invalidateViewState();
        if (bean == null) {
            CompetenceCourse competenceCourse = getDomainObject(request, "competenceCourseId");
            ExecutionSemester executionSemester = getDomainObject(request, "executionSemesterId");
            if (competenceCourse == null) {
                return prepareListCourses(mapping, form, request, response);
            }
            bean = new CourseStatisticsBean(getDepartment(request), competenceCourse, executionSemester);
        }
        request.setAttribute("courseStatisticsBean", bean);

        return mapping.findForward("viewDegreeCourses");
    }

    public ActionForward downloadExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {
        Degree degree = getDomainObject(request, "degreeId");
        CompetenceCourse competenceCourse = getDomainObject(request, "competenceCourseId");
        ExecutionSemester executionSemester = getDomainObject(request, "executionSemesterId");

        exportStudentsToExcel(response, getCurricularCourseToExport(competenceCourse, degree),
                executionSemester.getExecutionYear());
        return null;
    }

    public ActionForward prepareExecutionCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {

        CourseStatisticsBean bean = getRenderedObject("courseStatisticsBean");
        RenderUtils.invalidateViewState();
        if (bean == null) {
            Degree degree = getDomainObject(request, "degreeId");
            CompetenceCourse competenceCourse = getDomainObject(request, "competenceCourseId");
            ExecutionSemester executionSemester = getDomainObject(request, "executionSemesterId");
            if (competenceCourse == null || degree == null) {
                return prepareListCourses(mapping, form, request, response);
            }
            bean = new CourseStatisticsBean(getDepartment(request), competenceCourse, degree, executionSemester);
        }
        request.setAttribute("courseStatisticsBean", bean);

        return mapping.findForward("viewExecutionCourses");
    }

    private Department getDepartment(HttpServletRequest request) {
        return getLoggedPerson(request).getTeacher().getLastWorkingDepartment();
    }

    private void exportStudentsToExcel(HttpServletResponse response, CurricularCourse curricularCourse,
            ExecutionYear executionYear) throws FenixServiceException {
        try {
            String filename =
                    String.format("%s_%s_%s.xls", new DateTime().toString("dd-MM-yyyy_HH:mm"),
                            BundleUtil.getString(Bundle.APPLICATION, "label.students"), curricularCourse.getName().replaceAll(" ", "_"));
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment; filename=" + filename);
            ServletOutputStream outputStream = response.getOutputStream();

            final Spreadsheet spreadsheet = new Spreadsheet("-", getStudentsEnroledListHeaders());
            reportInfo(spreadsheet, curricularCourse, executionYear);

            spreadsheet.exportToXLSSheet(outputStream);
            outputStream.flush();

            response.flushBuffer();
        } catch (IOException e) {
            throw new FenixServiceException();
        }
    }

    private List<Object> getStudentsEnroledListHeaders() {

        final List<Object> headers = new ArrayList<Object>(8);
        headers.add(BundleUtil.getString(Bundle.APPLICATION, "label.student.number"));
        headers.add(BundleUtil.getString(Bundle.APPLICATION, "label.student.degree"));
        headers.add(BundleUtil.getString(Bundle.APPLICATION, "label.student.curricularCourse"));
        headers.add(BundleUtil.getString(Bundle.APPLICATION, "label.executionYear"));
        headers.add(BundleUtil.getString(Bundle.APPLICATION, "label.student.main.branch"));
        headers.add(BundleUtil.getString(Bundle.APPLICATION, "label.student.minor.branch"));
        headers.add(BundleUtil.getString(Bundle.APPLICATION, "label.student.number.of.enrolments"));
        return headers;
    }

    private void reportInfo(Spreadsheet spreadsheet, CurricularCourse curricularCourse, ExecutionYear executionYear) {
        for (final Enrolment enrolment : curricularCourse.getEnrolments()) {

            if (!enrolment.isValid(executionYear)) {
                continue;
            }

            final Row row = spreadsheet.addRow();

            row.setCell(enrolment.getStudent().getNumber());
            row.setCell(enrolment.getDegreeCurricularPlanOfStudent().getDegree().getPresentationName());
            row.setCell(enrolment.getName().getContent());
            row.setCell(enrolment.getExecutionYear().getName());

            final CycleCurriculumGroup cycle = enrolment.getParentCycleCurriculumGroup();

            final BranchCurriculumGroup major = cycle == null ? null : cycle.getMajorBranchCurriculumGroup();
            row.setCell(major != null ? major.getName().getContent() : "");

            final BranchCurriculumGroup minor = cycle == null ? null : cycle.getMinorBranchCurriculumGroup();
            row.setCell(minor != null ? minor.getName().getContent() : "");

            row.setCell(getNumberOfEnrolments(enrolment));
        }
    }

    private String getNumberOfEnrolments(final Enrolment enrolment) {
        return String.valueOf(enrolment.getStudentCurricularPlan().getEnrolments(enrolment.getCurricularCourse()).size());
    }

    private CurricularCourse getCurricularCourseToExport(CompetenceCourse competenceCourse, Degree degree) {

        for (final CurricularCourse curricularCourse : competenceCourse.getAssociatedCurricularCourses()) {
            if (curricularCourse.getDegree().equals(degree)) {
                return curricularCourse;
            }
        }

        return null;
    }
}
