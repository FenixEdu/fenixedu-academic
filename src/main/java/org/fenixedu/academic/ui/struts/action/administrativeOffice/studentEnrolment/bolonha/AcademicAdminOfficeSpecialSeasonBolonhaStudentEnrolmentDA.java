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

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent;
import org.fenixedu.academic.domain.accounting.events.AnnualEvent;
import org.fenixedu.academic.domain.accounting.events.insurance.InsuranceEvent;
import org.fenixedu.academic.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.student.StudentStatute;
import org.fenixedu.academic.dto.student.enrollment.bolonha.SpecialSeasonBolonhaStudentEnrolmentBean;
import org.fenixedu.academic.ui.struts.action.administrativeOffice.student.SearchForStudentsDA;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

@Mapping(path = "/specialSeasonBolonhaStudentEnrollment", module = "academicAdministration",
        formBean = "bolonhaStudentEnrollmentForm", functionality = SearchForStudentsDA.class)
@Forwards({
        @Forward(name = "showDegreeModulesToEnrol",
                path = "/academicAdminOffice/student/enrollment/bolonha/showDegreeModulesToEnrol.jsp"),
        @Forward(name = "showStudentEnrollmentMenu",
                path = "/academicAdministration/studentEnrolments.do?method=prepareFromStudentEnrollmentWithRules"),
        @Forward(name = "changeSpecialSeasonCode",
                path = "/academicAdminOffice/student/enrollment/bolonha/chooseSpecialSeasonCode.jsp") })
public class AcademicAdminOfficeSpecialSeasonBolonhaStudentEnrolmentDA extends AcademicAdminOfficeBolonhaStudentEnrollmentDA {

    @Override
    protected CurricularRuleLevel getCurricularRuleLevel(ActionForm form) {
        return CurricularRuleLevel.SPECIAL_SEASON_ENROLMENT;
    }

    @Override
    protected ActionForward prepareShowDegreeModulesToEnrol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response, StudentCurricularPlan studentCurricularPlan, ExecutionSemester executionSemester) {
        request.setAttribute("action", getAction());
        request.setAttribute("bolonhaStudentEnrollmentBean", new SpecialSeasonBolonhaStudentEnrolmentBean(studentCurricularPlan,
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
    }

    protected boolean hasAnyAdministrativeOfficeFeeAndInsuranceInDebt(final Student student, final ExecutionYear executionYear) {
        return student.getPerson().hasAnyAdministrativeOfficeFeeDebtUntil(executionYear) ||
                student.getPerson().hasAnyInsuranceDebtUntil(executionYear);
    }

    @Override
    protected String getAction() {
        return "/specialSeasonBolonhaStudentEnrollment.do";
    }

    public ActionForward checkPermission(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        StudentCurricularPlan studentCurricularPlan = getStudentCurricularPlan(request);
        ExecutionSemester executionSemester = getExecutionPeriod(request);

        if (!hasStatute(studentCurricularPlan.getRegistration().getStudent(), executionSemester,
                studentCurricularPlan.getRegistration())) {
            if (!studentCurricularPlan.getRegistration().getStudent().isSenior(executionSemester.getExecutionYear())) {
                addActionMessage(request, "error.special.season.not.granted");
                request.setAttribute("studentCurricularPlan", studentCurricularPlan);
                request.setAttribute("executionPeriod", executionSemester);

                return mapping.findForward("showStudentEnrollmentMenu");
            }
        }

        request.setAttribute("action", getAction());
        request.setAttribute("bolonhaStudentEnrollmentBean", new SpecialSeasonBolonhaStudentEnrolmentBean(studentCurricularPlan,
                executionSemester));

        addDebtsWarningMessages(studentCurricularPlan.getRegistration().getStudent(), executionSemester, request);
        return mapping.findForward("showDegreeModulesToEnrol");

    }

    protected boolean hasStatute(Student student, ExecutionSemester executionSemester, Registration registration) {
        Collection<StudentStatute> statutes = student.getStudentStatutesSet();
        for (StudentStatute statute : statutes) {
            if (!statute.getType().isSpecialSeasonGranted() && !statute.hasSeniorStatuteForRegistration(registration)) {
                continue;
            }
            if (!statute.isValidInExecutionPeriod(executionSemester)) {
                continue;
            }

            return true;

        }
        return false;
    }

}