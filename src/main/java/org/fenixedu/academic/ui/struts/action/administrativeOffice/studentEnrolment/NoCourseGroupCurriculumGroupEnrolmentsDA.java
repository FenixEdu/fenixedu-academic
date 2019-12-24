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

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.curricularRules.executors.RuleResult;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.exceptions.EnrollmentDomainException;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumModule;
import org.fenixedu.academic.domain.studentCurriculum.NoCourseGroupCurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.NoCourseGroupCurriculumGroupType;
import org.fenixedu.academic.dto.administrativeOffice.studentEnrolment.NoCourseGroupEnrolmentBean;
import org.fenixedu.academic.dto.administrativeOffice.studentEnrolment.StudentEnrolmentBean;
import org.fenixedu.academic.predicate.IllegalDataAccessException;
import org.fenixedu.academic.service.services.student.administrativeOfficeServices.CreateExtraEnrolment;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

abstract public class NoCourseGroupCurriculumGroupEnrolmentsDA extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("actionName", getActionName());
        return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        final NoCourseGroupEnrolmentBean bean =
                createNoCourseGroupEnrolmentBean(getStudentCurricularPlan(request), getExecutionInterval(request));
        return showExtraEnrolments(bean, mapping, actionForm, request, response);
    }

    protected StudentCurricularPlan getStudentCurricularPlan(final HttpServletRequest request) {
        return FenixFramework.getDomainObject(request.getParameter("scpID"));
    }

    protected ExecutionInterval getExecutionInterval(final HttpServletRequest request) {
        return FenixFramework.getDomainObject(request.getParameter("executionPeriodID"));
    }

    protected ActionForward showExtraEnrolments(NoCourseGroupEnrolmentBean bean, ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final NoCourseGroupCurriculumGroup noCourseGroupCurriculumGroup = bean.getNoCourseGroupCurriculumGroup();
        if (noCourseGroupCurriculumGroup != null) {
            bean.setCurriculumGroup(noCourseGroupCurriculumGroup);
            if (noCourseGroupCurriculumGroup.hasAnyEnrolments()) {
                request.setAttribute("enrolments", noCourseGroupCurriculumGroup);
            }
        }

        request.setAttribute("enrolmentBean", bean);

        return mapping.findForward("showExtraEnrolments");
    }

    abstract protected NoCourseGroupEnrolmentBean createNoCourseGroupEnrolmentBean(
            final StudentCurricularPlan studentCurricularPlan, final ExecutionInterval executionInterval);

    public ActionForward postBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("enrolmentBean", getNoCourseGroupEnrolmentBean());
        RenderUtils.invalidateViewState();
        return mapping.findForward("chooseExtraEnrolment");
    }

    protected NoCourseGroupEnrolmentBean getNoCourseGroupEnrolmentBean() {
        return getRenderedObject("enrolmentBean");
    }

    public ActionForward chooseCurricular(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("enrolmentBean", getNoCourseGroupEnrolmentBean());
        RenderUtils.invalidateViewState();
        return mapping.findForward("chooseExtraEnrolment");
    }

    public ActionForward enrol(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final NoCourseGroupEnrolmentBean bean = getNoCourseGroupEnrolmentBean();
        request.setAttribute("enrolmentBean", bean);

        try {
            final RuleResult ruleResult = CreateExtraEnrolment.run(bean);

            if (ruleResult.isWarning()) {
                addRuleResultMessagesToActionMessages("warning", request, ruleResult);
            }

        } catch (final IllegalDataAccessException e) {
            addActionMessage("error", request, "error.notAuthorized");
            return mapping.findForward("chooseExtraEnrolment");

        } catch (final EnrollmentDomainException ex) {
            addRuleResultMessagesToActionMessages("enrolmentError", request, ex.getFalseResult());
            return mapping.findForward("chooseExtraEnrolment");

        } catch (final DomainException e) {
            addActionMessage("error", request, e.getMessage(), e.getArgs());
            return mapping.findForward("chooseExtraEnrolment");
        }

        return showExtraEnrolments(bean, mapping, actionForm, request, response);
    }

    public ActionForward delete(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final Enrolment enrolment = getEnrolment(request);
        final StudentCurricularPlan studentCurricularPlan = getStudentCurricularPlan(request);
        final ExecutionInterval executionInterval = getExecutionInterval(request);

        try {
            studentCurricularPlan.removeCurriculumModulesFromNoCourseGroupCurriculumGroup(
                    Collections.<CurriculumModule> singletonList(enrolment), executionInterval, getGroupType());

        } catch (final IllegalDataAccessException e) {
            addActionMessage("error", request, "error.notAuthorized");

        } catch (EnrollmentDomainException ex) {
            addRuleResultMessagesToActionMessages("error", request, ex.getFalseResult());

        } catch (DomainException e) {
            addActionMessage("error", request, e.getMessage());
        }

        return showExtraEnrolments(createNoCourseGroupEnrolmentBean(studentCurricularPlan, executionInterval), mapping,
                actionForm, request, response);
    }

    protected Enrolment getEnrolment(HttpServletRequest request) {
        return (Enrolment) FenixFramework.getDomainObject(request.getParameter("enrolment"));
    }

    public ActionForward back(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final NoCourseGroupEnrolmentBean bean = getNoCourseGroupEnrolmentBean();

        final StudentEnrolmentBean enrolmentBean = new StudentEnrolmentBean();
        enrolmentBean.setStudentCurricularPlan(bean.getStudentCurricularPlan());
        enrolmentBean.setExecutionPeriod(bean.getExecutionPeriod());
        request.setAttribute("studentEnrolmentBean", enrolmentBean);

        return mapping.findForward("showDegreeModulesToEnrol");
    }

    public ActionForward back2(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return showExtraEnrolments(getNoCourseGroupEnrolmentBean(), mapping, actionForm, request, response);
    }

    abstract protected String getActionName();

    abstract protected NoCourseGroupCurriculumGroupType getGroupType();
}