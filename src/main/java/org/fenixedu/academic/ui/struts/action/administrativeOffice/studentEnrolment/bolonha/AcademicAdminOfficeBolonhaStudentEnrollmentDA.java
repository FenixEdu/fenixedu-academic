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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.treasury.TreasuryBridgeAPIFactory;
import org.fenixedu.academic.dto.student.enrollment.bolonha.BolonhaStudentEnrollmentBean;
import org.fenixedu.academic.ui.struts.action.administrativeOffice.student.SearchForStudentsDA;
import org.fenixedu.academic.ui.struts.action.commons.student.enrollment.bolonha.AbstractBolonhaStudentEnrollmentDA;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.joda.time.LocalDate;

@Mapping(path = "/bolonhaStudentEnrollment", module = "academicAdministration", formBean = "bolonhaStudentEnrollmentForm",
        functionality = SearchForStudentsDA.class)
@Forwards({ @Forward(name = "showStudentEnrollmentMenu",
        path = "/academicAdministration/studentEnrolments.do?method=prepareFromStudentEnrollmentWithRules") })
public class AcademicAdminOfficeBolonhaStudentEnrollmentDA extends AbstractBolonhaStudentEnrollmentDA {

    @Override
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return prepareShowDegreeModulesToEnrol(mapping, form, request, response, getStudentCurricularPlan(request),
                getExecutionPeriod(request));
    }

    @Override
    protected ActionForward prepareShowDegreeModulesToEnrol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response, StudentCurricularPlan studentCurricularPlan, ExecutionInterval executionInterval) {

        request.setAttribute("action", getAction());
        addDebtsWarningMessages(studentCurricularPlan.getRegistration().getStudent(), executionInterval, request);

        return super.prepareShowDegreeModulesToEnrol(mapping, form, request, response, studentCurricularPlan, executionInterval);
    }

    protected void addDebtsWarningMessages(final Student student, final ExecutionInterval executionInterval,
            final HttpServletRequest request) {
        if (TreasuryBridgeAPIFactory.implementation().isAcademicalActsBlocked(student.getPerson(), new LocalDate())) {
            addActionMessage("warning", request, "label.student.events.in.debt.unpayed.warning");
        }
    }

    protected StudentCurricularPlan getStudentCurricularPlan(final HttpServletRequest request) {
        return getDomainObject(request, "scpID");
    }

    protected ExecutionInterval getExecutionPeriod(final HttpServletRequest request) {
        return getDomainObject(request, "executionPeriodID");
    }

    private Boolean getWithRules(final ActionForm form) {
        return (Boolean) ((DynaActionForm) form).get("withRules");
    }

    public ActionForward backToStudentEnrollments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final BolonhaStudentEnrollmentBean bolonhaStudentEnrollmentBean = getBolonhaStudentEnrollmentBeanFromViewState();
        request.setAttribute("studentCurricularPlan", bolonhaStudentEnrollmentBean.getStudentCurricularPlan());
        request.setAttribute("executionPeriod", bolonhaStudentEnrollmentBean.getExecutionPeriod());

        return mapping.findForward("showStudentEnrollmentMenu");

    }

    @Override
    protected int[] getCurricularYearForCurricularCourses() {
        return null;
    }

    @Override
    protected CurricularRuleLevel getCurricularRuleLevel(final ActionForm form) {
        return getWithRules(form) ? CurricularRuleLevel.ENROLMENT_WITH_RULES : CurricularRuleLevel.ENROLMENT_NO_RULES;
    }

    @Override
    protected String getAction() {
        return "/bolonhaStudentEnrollment.do";
    }

    @Override
    public ActionForward prepareChooseCycleCourseGroupToEnrol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("withRules", request.getParameter("withRules"));
        return super.prepareChooseCycleCourseGroupToEnrol(mapping, form, request, response);
    }

    public ActionForward cancelChooseCycleCourseGroupToEnrol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        return prepareShowDegreeModulesToEnrol(mapping, form, request, response, getStudentCurricularPlan(request),
                getExecutionPeriod(request));
    }

}
