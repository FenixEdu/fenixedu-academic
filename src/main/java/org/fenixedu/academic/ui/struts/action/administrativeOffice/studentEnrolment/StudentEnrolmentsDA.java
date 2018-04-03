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

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.UriBuilder;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Grade;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.degreeStructure.EctsConversionTable;
import org.fenixedu.academic.domain.degreeStructure.EmptyConversionTable;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.administrativeOffice.studentEnrolment.EditEnrolmentBean;
import org.fenixedu.academic.dto.administrativeOffice.studentEnrolment.StudentEnrolmentBean;
import org.fenixedu.academic.ui.struts.action.administrativeOffice.student.SearchForStudentsDA;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;
import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/studentEnrolments", module = "academicAdministration", functionality = SearchForStudentsDA.class)
@Forwards({
        @Forward(name = "prepareChooseExecutionPeriod", path = "/academicAdminOffice/chooseStudentEnrolmentExecutionPeriod.jsp"),
    @Forward(name = "visualizeRegistration", path = "/academicAdministration/student.do?method=visualizeRegistration"),
        @Forward(name = "edit-enrolment", path = "/academicAdminOffice/student/enrollment/bolonha/editEnrolment.jsp")})
public class StudentEnrolmentsDA extends FenixDispatchAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentEnrolmentsDA.class);

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        ExecutionSemester executionPeriod = getDomainObject(request, "executionPeriodId");
        final StudentCurricularPlan plan = getDomainObject(request, "scpID");
        StudentEnrolmentBean studentEnrolmentBean = new StudentEnrolmentBean();
        if (plan != null) {
            studentEnrolmentBean.setStudentCurricularPlan(plan);
            studentEnrolmentBean.setExecutionPeriod(Optional.ofNullable(executionPeriod).orElse(ExecutionSemester.readActualExecutionSemester()));
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

    public ActionForward prepareFromStudentEnrollmentWithRules(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        final StudentEnrolmentBean studentEnrolmentBean = new StudentEnrolmentBean();
        studentEnrolmentBean.setExecutionPeriod((ExecutionSemester) request.getAttribute("executionPeriod"));
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
                    .getSpecialSeasonEnrolments(studentEnrolmentBean.getExecutionYear()));
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
            atomic(() ->
            {
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

    public ActionForward prepareEditEnrolmentPostback(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
                                              final HttpServletResponse response) {
        
        EditEnrolmentBean bean = getRenderedObject();
        RenderUtils.invalidateViewState();

        request.setAttribute("enrolmentBean", bean);
        return mapping.findForward("edit-enrolment");
    }

    public ActionForward prepareEditEnrolment(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
                                       final HttpServletResponse response) {

        EditEnrolmentBean bean = getRenderedObject();

        if (bean == null) {
            Enrolment enrolment = getDomainObject(request, "enrolmentId");
            StudentCurricularPlan studentCurricularPlan = getDomainObject(request, "scpID");
            bean = new EditEnrolmentBean(enrolment, studentCurricularPlan);
        }
        
        request.setAttribute("enrolmentBean", bean);
        return mapping.findForward("edit-enrolment");
    }

    public ActionForward editEnrolment(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
                                              final HttpServletResponse response) {

        final EditEnrolmentBean enrolmentBean = getRenderedObject();
        final Enrolment enrolment = enrolmentBean.getEnrolment();

        final Double newWeight = enrolmentBean.getWeight();
        final EctsConversionTable newEctsConversionTable = enrolmentBean.getEctsConversionTable();
        final Grade newNormalizedGrade = enrolmentBean.getNormalizedGrade();

        Double oldWeight = enrolment.getWeigth();

        RenderUtils.invalidateViewState();
        
        FenixFramework.atomic(() -> {
            if (newEctsConversionTable instanceof EmptyConversionTable) {
                if (newNormalizedGrade.isEmpty()) {
                    // use default table
                    enrolment.setEctsConversionTable(null);
                    enrolment.setEctsGrade(null);
                } else {
                    enrolment.setEctsGrade(newNormalizedGrade);
                    enrolment.setEctsConversionTable(newEctsConversionTable);
                }
            } else {
                // default to the selected table
                enrolment.setEctsGrade(null);
                enrolment.setEctsConversionTable(newEctsConversionTable);
            }

            enrolment.setWeigth(newWeight);
        });

        LOGGER.info("Changed enrolment {} of student {} by user {}", enrolmentBean.getEnrolment().getExternalId(),
                    enrolmentBean.getEnrolment().getStudent().getPerson().getUsername(), Authenticate.getUser().getUsername());
        LOGGER.info("\t newEctsConversionTable: {}", newEctsConversionTable == null ? "-" : newEctsConversionTable
                                                                                                .getPresentationName().getContent());
        LOGGER.info("\t newEctsGrade: {}", newNormalizedGrade == null ? "-" : newNormalizedGrade.getValue());
        if (!oldWeight.equals(newWeight)) {
            LOGGER.info("\t oldWeight: {} newWeight: {}", oldWeight.toString(), newWeight.toString());
        }


        final UriBuilder builder = UriBuilder.fromPath("/academicAdministration/studentEnrolments.do");
        builder.queryParam("method", "prepare");
        final String scpID = request.getParameter("scpID");
        final String executionPeriodId = request.getParameter("executionPeriodId");

        if (!Strings.isNullOrEmpty(scpID)) {
            builder.queryParam("scpID", scpID);
        }

        if (!Strings.isNullOrEmpty(executionPeriodId)) {
            builder.queryParam("executionPeriodId", executionPeriodId);
        }

        final String redirectTo = GenericChecksumRewriter.injectChecksumInUrl(request.getContextPath(), builder.toString(), request.getSession(false));
        
        final ActionForward actionForward = new ActionForward();
        actionForward.setModule("/");
        actionForward.setPath(redirectTo);
        actionForward.setRedirect(true);

        return actionForward;
    }

}
