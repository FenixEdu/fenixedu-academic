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
package org.fenixedu.academic.ui.struts.action.administrativeOffice.studentEnrolment.bolonha;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent;
import org.fenixedu.academic.domain.accounting.events.AnnualEvent;
import org.fenixedu.academic.domain.accounting.events.gratuity.GratuityEvent;
import org.fenixedu.academic.domain.accounting.events.insurance.InsuranceEvent;
import org.fenixedu.academic.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.dto.student.enrollment.bolonha.ExtraordinarySeasonBolonhaStudentEnrolmentBean;
import org.fenixedu.academic.ui.struts.action.administrativeOffice.student.SearchForStudentsDA;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Mapping(path = "/extraordinarySeasonBolonhaStudentEnrollment", module = "academicAdministration",
        formBean = "bolonhaStudentEnrollmentForm", functionality = SearchForStudentsDA.class)
@Forwards({
        @Forward(name = "showDegreeModulesToEnrol",
                path = "/academicAdminOffice/student/enrollment/bolonha/showDegreeModulesToEnrol.jsp"),
        @Forward(name = "showStudentEnrollmentMenu",
                path = "/academicAdministration/studentEnrolments.do?method=prepareFromStudentEnrollmentWithRules") })
public class AcademicAdminOfficeExtraordinarySeasonBolonhaStudentEnrolmentDA extends AcademicAdminOfficeBolonhaStudentEnrollmentDA {

    @Override
    protected CurricularRuleLevel getCurricularRuleLevel(ActionForm form) {
        return CurricularRuleLevel.EXTRAORDINARY_SEASON_ENROLMENT;
    }

    @Override
    protected ActionForward prepareShowDegreeModulesToEnrol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response, StudentCurricularPlan studentCurricularPlan, ExecutionSemester executionSemester) {
        request.setAttribute("action", getAction());
        request.setAttribute("bolonhaStudentEnrollmentBean", new ExtraordinarySeasonBolonhaStudentEnrolmentBean(studentCurricularPlan,
                executionSemester));

        addDebtsWarningMessages(studentCurricularPlan.getRegistration().getStudent(), executionSemester, request);

        return mapping.findForward("showDegreeModulesToEnrol");
    }

    @Override
    protected void addDebtsWarningMessages(final Student student, final ExecutionSemester executionSemester,
            final HttpServletRequest request) {

        if (hasAnyAdministrativeOfficeFeeAndInsuranceInDebt(student, executionSemester.getExecutionYear())) {
            addActionMessage("warning", request, "registration.has.not.payed.insurance.fees");
        }

        if (hasAnyGratuityDebt(student, executionSemester.getExecutionYear())) {
            addActionMessage("warning", request, "registration.has.not.payed.gratuities");
        }
    }

    protected boolean hasAnyAdministrativeOfficeFeeAndInsuranceInDebt(final Student student, final ExecutionYear executionYear) {
        for (final Event event : student.getPerson().getEventsSet()) {

            if (event instanceof AnnualEvent) {
                final AnnualEvent annualEvent = (AnnualEvent) event;
                if (annualEvent.getExecutionYear().isAfter(executionYear)) {
                    continue;
                }
            }

            if ((event instanceof AdministrativeOfficeFeeAndInsuranceEvent || event instanceof InsuranceEvent) && event.isOpen()) {
                return true;
            }
        }

        return false;
    }

    protected boolean hasAnyGratuityDebt(final Student student, final ExecutionYear executionYear) {
        for (final Registration registration : student.getRegistrationsSet()) {
            for (final StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlansSet()) {
                for (final GratuityEvent gratuityEvent : studentCurricularPlan.getGratuityEventsSet()) {
                    if (gratuityEvent.getExecutionYear().isBeforeOrEquals(executionYear) && gratuityEvent.isInDebt()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected String getAction() {
        return "/extraordinarySeasonBolonhaStudentEnrollment.do";
    }

    public ActionForward checkPermission(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        StudentCurricularPlan studentCurricularPlan = getStudentCurricularPlan(request);
        ExecutionSemester executionSemester = getExecutionPeriod(request);

        request.setAttribute("action", getAction());
        request.setAttribute("bolonhaStudentEnrollmentBean", new ExtraordinarySeasonBolonhaStudentEnrolmentBean(studentCurricularPlan,
                executionSemester));

        addDebtsWarningMessages(studentCurricularPlan.getRegistration().getStudent(), executionSemester, request);
        return mapping.findForward("showDegreeModulesToEnrol");

    }


}