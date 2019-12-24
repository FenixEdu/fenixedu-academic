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
package org.fenixedu.academic.ui.struts.action.administrativeOffice.studentEnrolment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.administrativeOffice.studentEnrolment.StudentEnrolmentBean;
import org.fenixedu.academic.ui.struts.action.administrativeOffice.student.SearchForStudentsDA;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@Mapping(path = "/studentEnrolments", module = "academicAdministration", functionality = SearchForStudentsDA.class)
@Forwards({
        @Forward(name = "prepareChooseExecutionPeriod", path = "/academicAdminOffice/chooseStudentEnrolmentExecutionPeriod.jsp"),
        @Forward(name = "visualizeRegistration", path = "/academicAdministration/student.do?method=visualizeRegistration") })
public class StudentEnrolmentsDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        final StudentCurricularPlan plan = getDomainObject(request, "scpID");
        StudentEnrolmentBean studentEnrolmentBean = new StudentEnrolmentBean();
        if (plan != null) {
            studentEnrolmentBean.setStudentCurricularPlan(plan);
            studentEnrolmentBean.setExecutionPeriod(ExecutionInterval.findFirstCurrentChild(plan.getDegree().getCalendar()));
            return showExecutionPeriodEnrolments(studentEnrolmentBean, mapping, actionForm, request, response);
        } else {
            throw new FenixActionException();
        }
    }

    public ActionForward prepareFromExtraEnrolment(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        StudentEnrolmentBean studentEnrolmentBean = (StudentEnrolmentBean) request.getAttribute("studentEnrolmentBean");
        return showExecutionPeriodEnrolments(studentEnrolmentBean, mapping, actionForm, request, response);
    }

    public ActionForward prepareFromStudentEnrollmentWithRules(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final StudentEnrolmentBean studentEnrolmentBean = new StudentEnrolmentBean();
        studentEnrolmentBean.setExecutionPeriod((ExecutionInterval) request.getAttribute("executionPeriod"));
        studentEnrolmentBean.setStudentCurricularPlan((StudentCurricularPlan) request.getAttribute("studentCurricularPlan"));
        return showExecutionPeriodEnrolments(studentEnrolmentBean, mapping, form, request, response);
    }

    private ActionForward showExecutionPeriodEnrolments(StudentEnrolmentBean studentEnrolmentBean, ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("studentEnrolmentBean", studentEnrolmentBean);

        if (studentEnrolmentBean.getExecutionPeriod() != null) {
            request.setAttribute("studentEnrolments", studentEnrolmentBean.getStudentCurricularPlan()
                    .getEnrolmentsByExecutionPeriod(studentEnrolmentBean.getExecutionPeriod()));
            request.setAttribute("studentImprovementEnrolments", studentEnrolmentBean.getStudentCurricularPlan()
                    .getEnroledImprovements(studentEnrolmentBean.getExecutionPeriod()));
            request.setAttribute("studentSpecialSeasonEnrolments", studentEnrolmentBean.getStudentCurricularPlan()
                    .getEnroledSpecialSeasons(studentEnrolmentBean.getExecutionPeriod()));
        }

        return mapping.findForward("prepareChooseExecutionPeriod");
    }

    public ActionForward postBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        StudentEnrolmentBean enrolmentBean = getRenderedObject();
        RenderUtils.invalidateViewState();

        return showExecutionPeriodEnrolments(enrolmentBean, mapping, actionForm, request, response);
    }

    public ActionForward backViewRegistration(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final StudentEnrolmentBean studentEnrolmentBean = getRenderedObject();
        request.setAttribute("registrationId", studentEnrolmentBean.getStudentCurricularPlan().getRegistration().getExternalId());
        return mapping.findForward("visualizeRegistration");
    }

    public ActionForward annulEnrolment(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        final Enrolment enrolment = getDomainObject(request, "enrolmentId");
        try {
            atomic(enrolment::annul);
        } catch (DomainException e) {
            addActionMessage(request, e.getKey(), e.getArgs());
        }

        final StudentEnrolmentBean studentEnrolmentBean = new StudentEnrolmentBean();
        studentEnrolmentBean.setExecutionPeriod(getDomainObject(request, "executionPeriodId"));
        studentEnrolmentBean.setStudentCurricularPlan(getDomainObject(request, "scpID"));
        return showExecutionPeriodEnrolments(studentEnrolmentBean, mapping, form, request, response);

    }

    public ActionForward activateEnrolment(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        try {
            atomic(() -> {
                final Enrolment enrolment = getDomainObject(request, "enrolmentId");
                enrolment.activate();
            });
        } catch (DomainException e) {
            addActionMessage(request, e.getKey(), e.getArgs());
        }

        final StudentEnrolmentBean studentEnrolmentBean = new StudentEnrolmentBean();
        studentEnrolmentBean.setExecutionPeriod(getDomainObject(request, "executionPeriodId"));
        studentEnrolmentBean.setStudentCurricularPlan(getDomainObject(request, "scpID"));
        return showExecutionPeriodEnrolments(studentEnrolmentBean, mapping, form, request, response);

    }

}
