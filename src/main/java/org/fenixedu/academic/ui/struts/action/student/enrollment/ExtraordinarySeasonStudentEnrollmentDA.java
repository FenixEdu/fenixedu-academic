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
package org.fenixedu.academic.ui.struts.action.student.enrollment;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.EnrolmentPeriodInExtraordinarySeasonEvaluations;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.dto.student.enrollment.bolonha.ExtraordinarySeasonBolonhaStudentEnrolmentBean;
import org.fenixedu.academic.ui.struts.action.administrativeOffice.studentEnrolment.bolonha.AcademicAdminOfficeExtraordinarySeasonBolonhaStudentEnrolmentDA;
import org.fenixedu.academic.ui.struts.action.student.StudentApplication.StudentEnrollApp;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import pt.ist.fenixframework.FenixFramework;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@StrutsFunctionality(app = StudentEnrollApp.class, path = "extraordinary-season", titleKey = "link.extraordinarySeason.enrolment")
@Mapping(path = "/enrollment/evaluations/extraordinarySeason", module = "student")
@Forwards({
        @Forward(name = "showDegreeModulesToEnrol", path = "/student/enrollment/evaluations/extraordinarySeasonShowDegreeModules.jsp"),
        @Forward(name = "showPickSCPAndSemester", path = "/student/enrollment/evaluations/extraordinarySeasonPickSCPAndSemester.jsp"),
        @Forward(name = "showStudentEnrollmentMenu", path = "/student/enrollment/evaluations/extraordinarySeason.jsp") })
public class ExtraordinarySeasonStudentEnrollmentDA extends AcademicAdminOfficeExtraordinarySeasonBolonhaStudentEnrolmentDA {

    @EntryPoint
    public ActionForward entryPoint(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final Student student = getLoggedStudent(request);

        final List<StudentCurricularPlan> scps = generateSCPList(student);
        if (enrollmentPeriodNotOpen(new ArrayList<StudentCurricularPlan>(scps))) {
            EnrolmentPeriodInExtraordinarySeasonEvaluations enrolmentPeriod = getNextEnrollmentPeriod(scps);
            if (enrolmentPeriod == null) {
                addActionMessage("warning", request, "message.out.curricular.course.enrolment.period.default");
            } else {
                addActionMessage("warning", request, "message.out.extraordinary.season.enrolment.period.upcoming", enrolmentPeriod
                                .getStartDateDateTime().toString("dd-MM-yyyy"),
                        enrolmentPeriod.getEndDateDateTime().toString("dd-MM-yyyy"));
            }
            request.setAttribute("disableContinue", true);
        } else if (scps.isEmpty()) {
            request.setAttribute("disableContinue", true);
        }

        return mapping.findForward("showStudentEnrollmentMenu");
    }

    public ActionForward pickSCP(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        final Student student = getLoggedStudent(request);
        ExtraordinarySeasonStudentEnrollmentBean bean = new ExtraordinarySeasonStudentEnrollmentBean(student);
        final List<StudentCurricularPlan> scps = generateSCPList(student);

        if (scps.size() == 1) {
            bean.setScp(scps.iterator().next());
        } else {
            request.setAttribute("scps", scps);
        }

        request.setAttribute("bean", bean);
        return mapping.findForward("showPickSCPAndSemester");
    }

    public ActionForward pickSemester(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final Student student = getLoggedStudent(request);
        final String scpOid = request.getParameter("scpOid");
        final StudentCurricularPlan scp = FenixFramework.getDomainObject(scpOid);
        ExtraordinarySeasonStudentEnrollmentBean bean = new ExtraordinarySeasonStudentEnrollmentBean(student, scp);

        request.setAttribute("bean", bean);
        return mapping.findForward("showPickSCPAndSemester");
    }

    public ActionForward showDegreeModules(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ExtraordinarySeasonStudentEnrollmentBean bean = getRenderedObject("bean");

        request.setAttribute("bolonhaStudentEnrollmentBean",
                new ExtraordinarySeasonBolonhaStudentEnrolmentBean(bean.getScp(), bean.getExecutionSemester()));
        request.setAttribute("bean", bean);
        return mapping.findForward("showDegreeModulesToEnrol");
    }

    private Student getLoggedStudent(final HttpServletRequest request) {
        return getLoggedPerson(request).getStudent();
    }

    private final List<StudentCurricularPlan> generateSCPList(final Student student) {
        final List<StudentCurricularPlan> result = new ArrayList<StudentCurricularPlan>();

        for (final Registration registration : student.getRegistrationsSet()) {
            if (registration.isActive() || registration.hasAnyEnrolmentsIn(ExecutionYear.readCurrentExecutionYear())) {
                result.add(registration.getLastStudentCurricularPlan());
            }
        }

        return result;
    }

    private boolean enrollmentPeriodNotOpen(List<StudentCurricularPlan> scps) {
        if (scps.isEmpty()) {
            return true;
        }

        final StudentCurricularPlan scp = scps.iterator().next();
        scps.remove(0);
        // Any ExtraordinarySeason enrollment period opened for this/last year will
        // count.
        return !(scp.getDegreeCurricularPlan().hasOpenExtraordinarySeasonEnrolmentPeriod(
                ExecutionYear.readCurrentExecutionYear().getFirstExecutionPeriod())
                || scp.getDegreeCurricularPlan().hasOpenExtraordinarySeasonEnrolmentPeriod(
                ExecutionYear.readCurrentExecutionYear().getLastExecutionPeriod())
                || scp.getDegreeCurricularPlan().hasOpenExtraordinarySeasonEnrolmentPeriod(
                ExecutionYear.readCurrentExecutionYear().getPreviousExecutionYear().getFirstExecutionPeriod()) || scp
                .getDegreeCurricularPlan().hasOpenExtraordinarySeasonEnrolmentPeriod(
                        ExecutionYear.readCurrentExecutionYear().getPreviousExecutionYear().getLastExecutionPeriod()))
                && enrollmentPeriodNotOpen(scps);

    }

    private EnrolmentPeriodInExtraordinarySeasonEvaluations getNextEnrollmentPeriod(List<StudentCurricularPlan> scps) {
        final List<EnrolmentPeriodInExtraordinarySeasonEvaluations> nextOpenPeriodsForEachSCP =
                new ArrayList<EnrolmentPeriodInExtraordinarySeasonEvaluations>();
        EnrolmentPeriodInExtraordinarySeasonEvaluations result;
        for (final StudentCurricularPlan scp : scps) {
            result = scp.getDegreeCurricularPlan().getNextExtraordinarySeasonEnrolmentPeriod();
            if (result != null) {
                nextOpenPeriodsForEachSCP.add(result);
            }
        }
        return nextOpenPeriodsForEachSCP.isEmpty() ? null : Collections.min(nextOpenPeriodsForEachSCP,
                EnrolmentPeriodInExtraordinarySeasonEvaluations.COMPARATOR_BY_START);
    }

    @Override
    protected String getAction() {
        return "/enrollment/evaluations/extraordinarySeason.do";
    }

}
