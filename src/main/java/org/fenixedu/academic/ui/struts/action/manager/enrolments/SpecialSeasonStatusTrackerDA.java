/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.manager.enrolments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.CompetenceCourse;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.manager.ManagerApplications.ManagerStudentsApp;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.fenixedu.commons.spreadsheet.Spreadsheet;
import org.fenixedu.commons.spreadsheet.Spreadsheet.Row;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@StrutsFunctionality(app = ManagerStudentsApp.class, path = "special-season-enrolments",
        titleKey = "label.course.specialSeasonEnrolments")
@Mapping(path = "/specialSeason/specialSeasonStatusTracker", module = "manager")
@Forwards({ @Forward(name = "selectCourse", path = "/manager/specialSeason/selectCourse.jsp"),
        @Forward(name = "listStudents", path = "/manager/specialSeason/listStudents.jsp") })
public class SpecialSeasonStatusTrackerDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward selectCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        SpecialSeasonStatusTrackerBean bean = getRenderedObject();
        if (bean == null) {
            bean = new SpecialSeasonStatusTrackerBean();
        }
        request.setAttribute("bean", bean);
        RenderUtils.invalidateViewState();
        return mapping.findForward("selectCourse");
    }

    public ActionForward updateDepartmentSelection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        SpecialSeasonStatusTrackerBean bean = getRenderedObject();
        request.setAttribute("bean", bean);
        RenderUtils.invalidateViewState();
        return mapping.findForward("selectCourse");
    }

    public ActionForward listStudents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final SpecialSeasonStatusTrackerBean bean = getRenderedObject();
        bean.clearEntries();
        List<Enrolment> enrolments = new ArrayList<Enrolment>();
        List<CompetenceCourse> courses = new ArrayList<CompetenceCourse>();
        List<Department> departments = new ArrayList<Department>();
        if (getDepartment() == null) {
            departments.addAll(Bennu.getInstance().getDepartmentsSet());
        } else {
            departments.add(getDepartment());
        }
        for (Department department : departments) {
            if (bean.getCompetenceCourse() == null) {
                courses.addAll(department.getBolonhaCompetenceCourses());
            } else {
                courses.add(bean.getCompetenceCourse());
            }
            for (CompetenceCourse competence : courses) {
                for (CurricularCourse course : competence.getAssociatedCurricularCoursesSet()) {
                    for (Enrolment enrolment : course.getActiveEnrollments(bean.getExecutionSemester())) {
                        if (enrolment.isSpecialSeason()) {
                            enrolments.add(enrolment);
                            bean.addEntry(enrolment.getRegistration().getNumber(), enrolment.getRegistration().getPerson()
                                    .getName(), enrolment.getRegistration().getDegree().getSigla(), enrolment
                                    .getCurricularCourse().getName(bean.getExecutionSemester()));
                        }
                    }
                }
            }
            courses.clear();
        }
        bean.setEnrolments(enrolments);
        Collections.sort(bean.getEntries(), SpecialSeasonStatusTrackerRegisterBean.COMPARATOR_STUDENT_NUMBER);
        request.setAttribute("bean", bean);
        RenderUtils.invalidateViewState();
        return mapping.findForward("listStudents");
    }

    public ActionForward exportXLS(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        SpecialSeasonStatusTrackerBean bean = getRenderedObject();
        final Spreadsheet spreadsheet = generateSpreadsheet(bean);

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment; filename=" + getFilename(bean) + ".xls");
        spreadsheet.exportToXLSSheet(response.getOutputStream());
        response.getOutputStream().flush();
        response.flushBuffer();
        return null;
    }

    private String getFilename(SpecialSeasonStatusTrackerBean bean) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(BundleUtil.getString(Bundle.APPLICATION, "special.season.filename"));
        if (bean.getCompetenceCourse() != null) {
            strBuilder.append("_");
            strBuilder.append(bean.getCompetenceCourse().getAcronym());
        } else if (getDepartment() != null) {
            strBuilder.append("_");
            strBuilder.append(bean.getDepartment().getAcronym());
        }
        strBuilder.append("_");
        strBuilder.append(bean.getExecutionSemester().getSemester());
        strBuilder.append("_");
        strBuilder.append(BundleUtil.getString(Bundle.APPLICATION, "special.season.semester"));
        strBuilder.append("_");
        strBuilder.append(bean.getExecutionSemester().getExecutionYear().getName());
        return strBuilder.toString();
    }

    private Spreadsheet generateSpreadsheet(SpecialSeasonStatusTrackerBean bean) {
        final Spreadsheet spreadsheet = createSpreadSheet();
        for (final Enrolment enrolment : bean.getEnrolments()) {
            final Row row = spreadsheet.addRow();

            row.setCell(enrolment.getRegistration().getPerson().getUsername());
            row.setCell(enrolment.getRegistration().getNumber());
            row.setCell(enrolment.getRegistration().getPerson().getName());
            row.setCell(enrolment.getRegistration().getPerson().getInstitutionalOrDefaultEmailAddressValue());
            row.setCell(enrolment.getRegistration().getDegree().getSigla());
            row.setCell(enrolment.getRegistration().getStudentCurricularPlan(bean.getExecutionSemester()).getName());
            row.setCell(enrolment.getCurricularCourse().getAcronym());
            row.setCell(enrolment.getCurricularCourse().getName());
        }

        return spreadsheet;
    }

    private Spreadsheet createSpreadSheet() {
        final Spreadsheet spreadsheet = new Spreadsheet(BundleUtil.getString(Bundle.APPLICATION, "list.students"));

        spreadsheet.setHeaders(new String[] {

        BundleUtil.getString(Bundle.APPLICATION, "label.username"),

        BundleUtil.getString(Bundle.APPLICATION, "label.number"),

        BundleUtil.getString(Bundle.APPLICATION, "label.name"),

        BundleUtil.getString(Bundle.APPLICATION, "label.email"),

        BundleUtil.getString(Bundle.APPLICATION, "label.Degree"),

        BundleUtil.getString(Bundle.APPLICATION, "label.curricularPlan"),

        BundleUtil.getString(Bundle.APPLICATION, "label.curricular.course.name"),

        BundleUtil.getString(Bundle.APPLICATION, "label.curricular.course.name"),

        " ", " " });

        return spreadsheet;
    }

    protected Department getDepartment() {
        SpecialSeasonStatusTrackerBean bean = getRenderedObject();
        if (bean != null && bean.getDepartment() != null) {
            return bean.getDepartment();
        }
        return null;
    }

}
