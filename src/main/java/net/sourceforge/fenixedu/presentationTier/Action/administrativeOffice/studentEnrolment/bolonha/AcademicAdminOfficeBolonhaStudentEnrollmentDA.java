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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha.BolonhaStudentEnrollmentBean;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.SearchForStudentsDA;
import net.sourceforge.fenixedu.presentationTier.Action.commons.student.enrollment.bolonha.AbstractBolonhaStudentEnrollmentDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/bolonhaStudentEnrollment", module = "academicAdministration", formBean = "bolonhaStudentEnrollmentForm",
        functionality = SearchForStudentsDA.class)
@Forwards({ @Forward(name = "showStudentEnrollmentMenu",
        path = "/academicAdministration/studentEnrolments.do?method=prepareFromStudentEnrollmentWithRules") })
public class AcademicAdminOfficeBolonhaStudentEnrollmentDA extends AbstractBolonhaStudentEnrollmentDA {

    @Override
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return prepareShowDegreeModulesToEnrol(mapping, form, request, response, getStudentCurricularPlan(request),
                getExecutionPeriod(request));
    }

    @Override
    protected ActionForward prepareShowDegreeModulesToEnrol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response, StudentCurricularPlan studentCurricularPlan, ExecutionSemester executionSemester) {

        request.setAttribute("action", getAction());
        addDebtsWarningMessages(studentCurricularPlan.getRegistration().getStudent(), executionSemester, request);

        return super.prepareShowDegreeModulesToEnrol(mapping, form, request, response, studentCurricularPlan, executionSemester);
    }

    protected void addDebtsWarningMessages(final Student student, final ExecutionSemester executionSemester,
            final HttpServletRequest request) {
        if (student.isAnyGratuityOrAdministrativeOfficeFeeAndInsuranceInDebt()) {
            addActionMessage("warning", request, "label.student.events.in.debt.warning");
        }
    }

    protected StudentCurricularPlan getStudentCurricularPlan(final HttpServletRequest request) {
        return getDomainObject(request, "scpID");
    }

    protected ExecutionSemester getExecutionPeriod(final HttpServletRequest request) {
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
