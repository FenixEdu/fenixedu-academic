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
package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.studentEnrolment.bolonha;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha.SpecialSeasonBolonhaStudentEnrolmentBean;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent;
import net.sourceforge.fenixedu.domain.accounting.events.AnnualEvent;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEvent;
import net.sourceforge.fenixedu.domain.accounting.events.insurance.InsuranceEvent;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.StudentStatute;
import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.SearchForStudentsDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/specialSeasonBolonhaStudentEnrollment", module = "academicAdministration",
        formBean = "bolonhaStudentEnrollmentForm", functionality = SearchForStudentsDA.class)
@Forwards({
        @Forward(name = "showDegreeModulesToEnrol",
                path = "/academicAdminOffice/student/enrollment/bolonha/showDegreeModulesToEnrol.jsp"),
        @Forward(name = "showStudentEnrollmentMenu", path = "/studentEnrolments.do?method=prepareFromStudentEnrollmentWithRules"),
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
        return "/specialSeasonBolonhaStudentEnrollment.do";
    }

    public ActionForward checkPermission(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        StudentCurricularPlan studentCurricularPlan = getStudentCurricularPlan(request);
        ExecutionSemester executionSemester = getExecutionPeriod(request);

        if (!hasStatute(studentCurricularPlan.getRegistration().getStudent(), executionSemester,
                studentCurricularPlan.getRegistration())) {
            if (!studentCurricularPlan.getRegistration().isSeniorStatuteApplicable(executionSemester.getExecutionYear())) {
                addActionMessage(request, "error.special.season.not.granted");
                request.setAttribute("studentCurricularPlan", studentCurricularPlan);
                request.setAttribute("executionPeriod", executionSemester);

                return mapping.findForward("showStudentEnrollmentMenu");
            }
            studentCurricularPlan.getRegistration().grantSeniorStatute(executionSemester.getExecutionYear());
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
            if (!statute.getStatuteType().isSpecialSeasonGranted() && !statute.hasSeniorStatuteForRegistration(registration)) {
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