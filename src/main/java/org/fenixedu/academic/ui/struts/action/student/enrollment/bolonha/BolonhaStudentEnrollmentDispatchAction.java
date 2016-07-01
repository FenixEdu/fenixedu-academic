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
package org.fenixedu.academic.ui.struts.action.student.enrollment.bolonha;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.studentCurriculum.StudentCurricularPlanEnrolmentPreConditions;
import org.fenixedu.academic.domain.studentCurriculum.StudentCurricularPlanEnrolmentPreConditions.EnrolmentPreConditionResult;
import org.fenixedu.academic.dto.student.enrollment.bolonha.BolonhaStudentEnrollmentBean;
import org.fenixedu.academic.dto.student.enrollment.bolonha.BolonhaStudentEnrollmentBean.StudentEnrolmentHandler;
import org.fenixedu.academic.ui.struts.action.commons.student.enrollment.bolonha.AbstractBolonhaStudentEnrollmentDA;
import org.fenixedu.academic.ui.struts.action.student.enrollment.StudentEnrollmentManagementDA;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

@Mapping(module = "student", path = "/bolonhaStudentEnrollment", functionality = StudentEnrollmentManagementDA.class)
@Forwards(value = {
        @Forward(name = "notAuthorized", path = "/student/notAuthorized_bd.jsp"),
        @Forward(name = "chooseOptionalCurricularCourseToEnrol",
                path = "/student/enrollment/bolonha/chooseOptionalCurricularCourseToEnrol.jsp"),
        @Forward(name = "showDegreeModulesToEnrol", path = "/student/enrollment/bolonha/showDegreeModulesToEnrol.jsp"),
        @Forward(name = "showEnrollmentInstructions", path = "/student/enrollment/bolonha/showEnrollmentInstructions.jsp"),
        @Forward(name = "chooseCycleCourseGroupToEnrol", path = "/student/enrollment/bolonha/chooseCycleCourseGroupToEnrol.jsp"),
        @Forward(name = "welcome", path = "/student/enrollment/welcome.jsp"),
        @Forward(name = "chooseSemester", path = "/student/enrollment/chooseSemester.jsp"),
        @Forward(name = "welcome-dea-degree", path = "/student/phdStudentEnrolment.do?method=showWelcome"),
        @Forward(name = "showEnrollmentInstructions", path = "/student/enrollment/bolonha/showEnrollmentInstructions.jsp"),
        @Forward(name = "enrollmentCannotProceed", path = "/student/enrollment/bolonha/enrollmentCannotProceed.jsp") })
public class BolonhaStudentEnrollmentDispatchAction extends AbstractBolonhaStudentEnrollmentDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ActionForward result = super.execute(mapping, actionForm, request, response);
        final BolonhaStudentEnrollmentBean bolonhaStudentEnrollmentBean =
                (BolonhaStudentEnrollmentBean) request.getAttribute("bolonhaStudentEnrollmentBean");
        if (bolonhaStudentEnrollmentBean != null) {
            Registration registration = bolonhaStudentEnrollmentBean.getRegistration();
            List<ExecutionSemester> openedEnrolmentPeriodsSemesters = Collections.EMPTY_LIST;
            if (registration != null) {
                openedEnrolmentPeriodsSemesters =
                        registration.getLastDegreeCurricularPlan().getEnrolmentPeriodsSet().stream()
                                .filter(ep -> ep.isValid() && ep.isForCurricularCourses()).map(ep -> ep.getExecutionPeriod())
                                .distinct().sorted(ExecutionSemester.COMPARATOR_BY_SEMESTER_AND_YEAR)
                                .collect(Collectors.toList());
            }
            if (openedEnrolmentPeriodsSemesters.size() > 1) {
                request.setAttribute("openedEnrolmentPeriodsSemesters", openedEnrolmentPeriodsSemesters);
            }
        }

        return result;
    }

    @Override
    protected void preProcess(BolonhaStudentEnrollmentBean bolonhaStudentEnrollmentBean) {
        for (StudentEnrolmentHandler handler : BolonhaStudentEnrollmentBean.getHandlers()) {
            handler.preProcess(bolonhaStudentEnrollmentBean);
        }
    }

    public ActionForward showWelcome(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final Registration registration = (Registration) request.getAttribute("registration");
        final ExecutionSemester executionSemester = (ExecutionSemester) request.getAttribute("executionSemester");
        request.setAttribute("registration", registration);
        request.setAttribute("executionSemester", executionSemester);
        return findForwardForRegistration(mapping, registration);
    }

    private ActionForward findForwardForRegistration(ActionMapping mapping, Registration registration) {
        if (registration.getDegree().isDEA()) {
            return mapping.findForward("welcome-dea-degree");
        } else {
            return mapping.findForward("welcome");
        }
    }

    @Override
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        final Registration registration = getDomainObject(request, "registrationOid");
        final ExecutionSemester executionSemester = getDomainObject(request, "executionSemesterID");
        request.setAttribute("registration", registration);
        return prepareShowDegreeModulesToEnrol(mapping, form, request, response, registration.getLastStudentCurricularPlan(),
                executionSemester);
    }

    private static final PeriodFormatter FORMATTER = new PeriodFormatterBuilder().printZeroAlways().appendHours()
            .appendSuffix("h").appendSeparator(" ").appendMinutes().appendSuffix("m").appendSeparator(" ").appendSeconds()
            .appendSuffix("s").toFormatter();

    @Override
    protected ActionForward prepareShowDegreeModulesToEnrol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response, StudentCurricularPlan studentCurricularPlan, ExecutionSemester executionSemester) {

        final EnrolmentPreConditionResult result =
                StudentCurricularPlanEnrolmentPreConditions.checkPreConditionsToEnrol(studentCurricularPlan, executionSemester);

        if (!result.isValid()) {
            if (result.getEnrolmentPeriod() != null) {
                DateTime now = DateTime.now().withMillisOfSecond(0);
                DateTime start = result.getEnrolmentPeriod().getStartDateDateTime();
                Period period = new Period(start.getMillis() - now.getMillis());
                if (start.toLocalDate().equals(now.toLocalDate())) {
                    request.setAttribute("now", now);
                    request.setAttribute("start", start);
                    request.setAttribute("remaining", FORMATTER.print(period));
                }
            }
            addActionMessage(request, result.message(), result.args());
            return mapping.findForward("enrollmentCannotProceed");
        }

        return super.prepareShowDegreeModulesToEnrol(mapping, form, request, response, studentCurricularPlan, executionSemester);
    }

    public ActionForward showEnrollmentInstructions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("showEnrollmentInstructions");
    }

    @Override
    protected int[] getCurricularYearForCurricularCourses() {
        return null; // all years
    }

    @Override
    protected CurricularRuleLevel getCurricularRuleLevel(final ActionForm actionForm) {
        return CurricularRuleLevel.ENROLMENT_WITH_RULES;
    }

    @Override
    protected String getAction() {
        return "";
    }
}
