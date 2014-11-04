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
package org.fenixedu.academic.ui.struts.action.teacher.tutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.dto.teacher.tutor.PerformanceGridTableDTO;
import org.fenixedu.academic.dto.teacher.tutor.StudentsPerformanceInfoBean;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.Tutorship;
import org.fenixedu.academic.ui.struts.action.commons.tutorship.StudentsPerformanceGridDispatchAction;
import org.fenixedu.academic.ui.struts.action.teacher.TeacherApplication.TeacherTutorApp;
import org.fenixedu.academic.ui.renderers.providers.teacher.TutorshipEntryExecutionYearProvider;
import org.fenixedu.academic.ui.renderers.providers.teacher.TutorshipMonitoringExecutionYearProvider;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@StrutsFunctionality(app = TeacherTutorApp.class, path = "students-performance",
        titleKey = "link.teacher.tutorship.students.performanceGrid")
@Mapping(path = "/viewStudentsPerformanceGrid", module = "teacher")
@Forwards(@Forward(name = "viewStudentsPerformanceGrid", path = "/teacher/tutor/viewStudentsPerformanceGrid.jsp"))
public class ViewStudentsPerformanceGridDispatchAction extends StudentsPerformanceGridDispatchAction {

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Person person = getLoggedPerson(request);
        generateStudentsPerformanceBean(request, person);
        request.setAttribute("tutor", person);
        RenderUtils.invalidateViewState();
        return prepareStudentsPerformanceGrid(mapping, actionForm, request, response, person);
    }

    protected void generateStudentsPerformanceBean(HttpServletRequest request, Person person) {
        StudentsPerformanceInfoBean bean = getRenderedObject("performanceGridFiltersBean");
        if ((bean != null) && (bean.getTeacher() == person.getTeacher())) {
            request.setAttribute("performanceGridFiltersBean", bean);
            return;
        }

        if (person.getTeacher().getTutorshipsSet().isEmpty()) {
            return;
        }

        bean = StudentsPerformanceInfoBean.create(person.getTeacher());
        request.setAttribute("performanceGridFiltersBean", bean);
    }

    public ActionForward changeDegree(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        StudentsPerformanceInfoBean bean = getRenderedObject("performanceGridFiltersBean");
        if (bean != null && bean.getDegree() != null) {
            bean.setStudentsEntryYear(TutorshipEntryExecutionYearProvider.getExecutionYears(bean).iterator().next());
            bean.setCurrentMonitoringYear(TutorshipMonitoringExecutionYearProvider.getExecutionYears(bean).iterator().next());
            request.setAttribute("performanceGridFiltersBean", bean);
        }
        return prepare(mapping, actionForm, request, response);
    }

    protected StudentsPerformanceInfoBean generateStudentsPerformanceBeanFromRequest(HttpServletRequest request, Person person) {
        StudentsPerformanceInfoBean bean = StudentsPerformanceInfoBean.create(person.getTeacher());
        bean.setDegree((Degree) getDomainObject(request, "degreeOID"));
        bean.setStudentsEntryYear((ExecutionYear) getDomainObject(request, "entryYearOID"));
        bean.setCurrentMonitoringYear((ExecutionYear) getDomainObject(request, "monitoringYearOID"));
        request.setAttribute("performanceGridFiltersBean", bean);
        return bean;
    }

    protected ActionForward prepareStudentsPerformanceGrid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response, Person person) throws Exception {
        StudentsPerformanceInfoBean bean = (StudentsPerformanceInfoBean) request.getAttribute("performanceGridFiltersBean");

        if (bean != null) {
            final List<Tutorship> tutorships;
            if (bean.getActiveTutorships()) {

                tutorships =
                        getTutorshipsByStudentsEntryYearAndDegree(Tutorship.getActiveTutorships(person.getTeacher()).stream(),
                                bean.getStudentsEntryYear(), bean.getDegree());
            } else {
                tutorships =
                        getTutorshipsByStudentsEntryYearAndDegree(Tutorship.getPastTutorships(person.getTeacher()).stream(),
                                bean.getStudentsEntryYear(), bean.getDegree());
            }

            if (tutorships != null && !tutorships.isEmpty()) {

                Collections.sort(tutorships, Tutorship.TUTORSHIP_COMPARATOR_BY_STUDENT_NUMBER);

                PerformanceGridTableDTO performanceGridTable =
                        createPerformanceGridTable(request, tutorships, bean.getStudentsEntryYear(),
                                bean.getCurrentMonitoringYear());
                getStatisticsAndPutInTheRequest(request, performanceGridTable);

                request.setAttribute("performanceGridFiltersBean", bean);
                request.setAttribute("performanceGridTable", performanceGridTable);
                request.setAttribute("totalStudents", tutorships.size());
            }

            request.setAttribute("monitoringYear", bean.getCurrentMonitoringYear());
        }
        request.setAttribute("tutor", person);
        return mapping.findForward("viewStudentsPerformanceGrid");
    }

    public ActionForward prepareAllStudentsStatistics(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        StudentsPerformanceInfoBean bean = generateStudentsPerformanceBeanFromRequest(request, getLoggedPerson(request));
        if (!bean.getTutorships().isEmpty()) {

            List<DegreeCurricularPlan> plans =
                    new ArrayList<DegreeCurricularPlan>(bean.getDegree().getDegreeCurricularPlansSet());
            Collections.sort(plans,
                    DegreeCurricularPlan.DEGREE_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_TYPE_AND_EXECUTION_DEGREE_AND_DEGREE_CODE);

            List<StudentCurricularPlan> students =
                    plans.iterator().next().getStudentsCurricularPlanGivenEntryYear(bean.getStudentsEntryYear());

            putAllStudentsStatisticsInTheRequest(request, students, bean.getCurrentMonitoringYear());

            request.setAttribute("entryYear", bean.getStudentsEntryYear());
            request.setAttribute("totalEntryStudents", students.size());
        }
        return prepareStudentsPerformanceGrid(mapping, actionForm, request, response, getLoggedPerson(request));
    }

    private static List<Tutorship> getTutorshipsByStudentsEntryYearAndDegree(Stream<Tutorship> tutorshipsList,
            ExecutionYear entryYear, Degree degree) {
        return tutorshipsList
                .filter(t -> ExecutionYear.getExecutionYearByDate(t.getStudentCurricularPlan().getRegistration().getStartDate())
                        .equals(entryYear)).filter(t -> t.getStudentCurricularPlan().getDegree().equals(degree))
                .filter(t -> !t.getStudentCurricularPlan().getRegistration().isCanceled()).collect(Collectors.toList());
    }

}
