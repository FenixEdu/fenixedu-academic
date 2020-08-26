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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.CompetenceCourse;
import org.fenixedu.academic.domain.Country;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.candidacy.StudentCandidacy;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacy;
import org.fenixedu.academic.domain.contacts.PhysicalAddress;
import org.fenixedu.academic.domain.student.PersonalIngressionData;
import org.fenixedu.academic.domain.student.Registration;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        final Spreadsheet spreadsheet = new Spreadsheet(BundleUtil.getString(Bundle.APPLICATION, "list.students"));
        for (final Enrolment enrolment : bean.getEnrolments()) {
            final Row row = spreadsheet.addRow();

            final Registration registration = enrolment.getRegistration();
            final Person person = registration.getPerson();
            final CurricularCourse curricularCourse = enrolment.getCurricularCourse();
            row.setCell(BundleUtil.getString(Bundle.APPLICATION, "label.username"), person.getUsername());
            row.setCell(BundleUtil.getString(Bundle.APPLICATION, "label.number"), registration.getNumber());
            row.setCell(BundleUtil.getString(Bundle.APPLICATION, "label.name"), person.getName());
            row.setCell(BundleUtil.getString(Bundle.APPLICATION, "label.email"), person.getInstitutionalOrDefaultEmailAddressValue());
            row.setCell(BundleUtil.getString(Bundle.APPLICATION, "label.Degree"), registration.getDegree().getSigla());
            row.setCell(BundleUtil.getString(Bundle.APPLICATION, "label.curricularPlan"), registration.getStudentCurricularPlan(bean.getExecutionSemester()).getName());
            row.setCell(BundleUtil.getString(Bundle.APPLICATION, "label.curricular.course.name"), curricularCourse.getAcronym());
            row.setCell(BundleUtil.getString(Bundle.APPLICATION, "label.curricular.course.name"), curricularCourse.getName());
            final boolean isDislocated = isDislocated(registration);
            row.setCell(BundleUtil.getString(Bundle.APPLICATION, "label.isDislocated"), Boolean.toString(isDislocated));
            row.setCell(BundleUtil.getString(Bundle.APPLICATION, "label.residence.country"), getResidenceCountry(person));
            row.setCell(BundleUtil.getString(Bundle.APPLICATION, "label.residence.postCode"), getResidencePostCode(person));
        }

        return spreadsheet;
    }

    private String getResidenceCountry(final Person person) {
        final Country country = person.getCountryOfResidence();
        return country == null ? " " : country.getCode();
    }

    private String getResidencePostCode(final Person person) {
        final PhysicalAddress physicalAddress = person.getDefaultPhysicalAddress();
        return physicalAddress == null ? " " : physicalAddress.getPostalCode();
    }

    private boolean isDislocated(final Registration registration) {
        final IndividualCandidacy individualCandidacy = registration.getIndividualCandidacy();
        if (individualCandidacy != null) {
            if (isTrue(individualCandidacy.getDislocatedFromPermanentResidence())) {
                return true;
            }
        }
        final StudentCandidacy studentCandidacy = registration.getStudentCandidacy();
        if (studentCandidacy != null) {
            if (isTrue(studentCandidacy.getDislocatedFromPermanentResidence())) {
                return true;
            }
        }
        final PersonalIngressionData personalIngressionData = registration.getStudent().getLatestPersonalIngressionData();
        if (personalIngressionData != null) {
            if (personalIngressionData.getDislocatedFromPermanentResidence()) {
                return true;
            }
        }
        return false;
    }

    private boolean isTrue(final Boolean b) {
        return b != null && b.booleanValue();
    }

    protected Department getDepartment() {
        SpecialSeasonStatusTrackerBean bean = getRenderedObject();
        if (bean != null && bean.getDepartment() != null) {
            return bean.getDepartment();
        }
        return null;
    }

}
