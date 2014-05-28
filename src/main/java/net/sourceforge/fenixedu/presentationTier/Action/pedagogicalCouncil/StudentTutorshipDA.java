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
package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil.NumberBean;
import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.PerformanceGridTableDTO;
import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.StudentsPerformanceInfoBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.domain.TutorshipSummary;
import net.sourceforge.fenixedu.domain.TutorshipSummaryRelation;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.CurriculumDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.commons.tutorship.StudentsPerformanceGridDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.PedagogicalCouncilApp.TutorshipApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = TutorshipApp.class, path = "student-tutorship", titleKey = "link.teacher.tutorship.history",
        bundle = "ApplicationResources")
@Mapping(path = "/studentTutorship", module = "pedagogicalCouncil")
@Forwards({ @Forward(name = "searchStudentTutorship", path = "/pedagogicalCouncil/tutorship/showStudentPerformanceGrid.jsp"),
        @Forward(name = "showStudentPerformanceGrid", path = "/pedagogicalCouncil/tutorship/showStudentPerformanceGrid.jsp") })
public class StudentTutorshipDA extends StudentsPerformanceGridDispatchAction {

    @EntryPoint
    public ActionForward prepareStudentSearch(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        request.setAttribute("tutorateBean", new NumberBean());
        return mapping.findForward("searchStudentTutorship");
    }

    public ActionForward showStudentPerformanceGrid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        NumberBean numberBean = (NumberBean) getRenderedObject("tutorateBean");

        Student student;
        StudentsPerformanceInfoBean performanceBean;
        if (numberBean != null) {
            student = Student.readStudentByNumber(numberBean.getNumber());
        } else {
            performanceBean = (StudentsPerformanceInfoBean) getRenderedObject("performanceGridFiltersBean");
            student = performanceBean.getStudent();

            numberBean = new NumberBean();
            numberBean.setNumber(student.getNumber());
            request.setAttribute("numberBean", numberBean);
        }
        if (student != null) {
            List<Tutorship> tutorships = student.getActiveTutorships();
            performanceBean = getOrCreateStudentsPerformanceBean(request, student);
            if (tutorships.size() > 0) {

                PerformanceGridTableDTO performanceGridTable =
                        createPerformanceGridTable(request, tutorships, performanceBean.getStudentsEntryYear(),
                                performanceBean.getCurrentMonitoringYear());

                getStatisticsAndPutInTheRequest(request, performanceGridTable);

                Integer years = 0;
                for (Tutorship t : tutorships) {
                    if (t.getStudentCurricularPlan().getDegreeDuration().compareTo(years) > 0) {
                        years = t.getStudentCurricularPlan().getDegreeDuration();
                    }
                }
                request.setAttribute("degreeCurricularPeriods", years);
                request.setAttribute("performanceGridTable", performanceGridTable);
                request.setAttribute("monitoringYear", ExecutionYear.readCurrentExecutionYear());
            }
            List<Teacher> tutors = new ArrayList<Teacher>();
            for (Tutorship t : tutorships) {
                tutors.add(t.getTeacher());
            }
            request.setAttribute("tutors", tutors);
            request.setAttribute("student", student.getPerson());

            List<TutorshipSummary> pastSummaries = new ArrayList<TutorshipSummary>();
            for (Tutorship t : student.getTutorships()) {
                for (TutorshipSummaryRelation tsr : t.getTutorshipSummaryRelationsSet()) {
                    if (!tsr.getTutorshipSummary().isActive()) {
                        pastSummaries.add(tsr.getTutorshipSummary());
                    }
                }
            }
            request.setAttribute("pastSummaries", pastSummaries);

        } else {
            studentErrorMessage(request, numberBean.getNumber());
        }

        return mapping.findForward("showStudentPerformanceGrid");
    }

    protected StudentsPerformanceInfoBean getOrCreateStudentsPerformanceBean(HttpServletRequest request, Student student) {
        StudentsPerformanceInfoBean bean = (StudentsPerformanceInfoBean) getRenderedObject("performanceGridFiltersBean");
        if (bean != null) {
            request.setAttribute("performanceGridFiltersBean", bean);
            return bean;
        }

        bean = new StudentsPerformanceInfoBean();
        bean.setStudent(student);
        request.setAttribute("performanceGridFiltersBean", bean);
        return bean;
    }

    public ActionForward prepareStudentCurriculum(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        request.setAttribute("tutorateBean", new NumberBean());
        return mapping.findForward("showStudentCurriculum");
    }

    public ActionForward showStudentRegistration(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final NumberBean numberBean = new NumberBean();
        numberBean.setNumber(getIntegerFromRequest(request, "studentNumber"));
        request.setAttribute("tutorateBean", numberBean);
        return showOrChoose(mapping, request);
    }

    public ActionForward showStudentCurriculum(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        return showOrChoose(mapping, request);
    }

    private ActionForward showOrChoose(final ActionMapping mapping, final HttpServletRequest request) {
        Registration registration = null;

        final String registrationOID = (String) getFromRequest(request, "registrationOID");
        NumberBean bean = (NumberBean) getObjectFromViewState("tutorateBean");
        if (bean == null) {
            bean = (NumberBean) request.getAttribute("tutorateBean");
        }

        if (registrationOID != null) {
            registration = FenixFramework.getDomainObject(registrationOID);
        } else {
            final Student student = Student.readStudentByNumber(bean.getNumber());
            if (student.getRegistrationsSet().size() == 1) {
                registration = student.getRegistrationsSet().iterator().next();
            } else {
                request.setAttribute("student", student);
                return mapping.findForward("chooseRegistration");
            }
        }

        if (registration == null) {
            studentErrorMessage(request, bean.getNumber());
        } else {
            request.setAttribute("registration", registration);
        }

        CurriculumDispatchAction.computeCurricularInfo(request, registration);

        return mapping.findForward("showStudentCurriculum");
    }

    private void studentErrorMessage(HttpServletRequest request, Integer studentNumber) {
        addActionMessage("error", request, "student.does.not.exist", String.valueOf(studentNumber));
    }

}
