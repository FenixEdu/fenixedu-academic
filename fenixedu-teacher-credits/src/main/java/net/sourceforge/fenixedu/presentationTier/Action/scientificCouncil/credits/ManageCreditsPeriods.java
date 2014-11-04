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
package org.fenixedu.academic.ui.struts.action.scientificCouncil.credits;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.service.services.scientificCouncil.credits.CreateTeacherCreditsFillingPeriod;
import org.fenixedu.academic.dto.teacherCredits.TeacherCreditsPeriodBean;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.TeacherCredits;
import org.fenixedu.academic.domain.TeacherCreditsQueueJob;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.scientificCouncil.ScientificCouncilApplication.ScientificCreditsApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = ScientificCreditsApp.class, path = "define-periods", titleKey = "link.define.periods")
@Mapping(module = "scientificCouncil", path = "/defineCreditsPeriods")
@Forwards({
        @Forward(name = "edit-teacher-credits-periods", path = "/scientificCouncil/credits/periods/editTeacherCreditsPeriod.jsp"),
        @Forward(name = "show-credits-periods", path = "/scientificCouncil/credits/periods/showPeriods.jsp") })
public class ManageCreditsPeriods extends FenixDispatchAction {

    @EntryPoint
    public ActionForward showPeriods(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        TeacherCreditsPeriodBean bean = createBeanTeacherCreditsPeriodBean(mapping, actionForm, request);
        request.setAttribute("teacherCreditsBean", bean);
        return mapping.findForward("show-credits-periods");
    }

    public ActionForward prepareEditTeacherCreditsPeriod(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ExecutionSemester executionSemester = getExecutionPeriodToEditPeriod(request);
        request.setAttribute("teacherCreditsBean", new TeacherCreditsPeriodBean(executionSemester, true));
        return mapping.findForward("edit-teacher-credits-periods");
    }

    public ActionForward prepareEditDepartmentAdmOfficeCreditsPeriod(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        ExecutionSemester executionSemester = getExecutionPeriodToEditPeriod(request);
        request.setAttribute("teacherCreditsBean", new TeacherCreditsPeriodBean(executionSemester, false));
        return mapping.findForward("edit-teacher-credits-periods");
    }

    public ActionForward editPeriod(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        TeacherCreditsPeriodBean bean = getRenderedObject("teacherCreditsBeanID");

        try {
            CreateTeacherCreditsFillingPeriod.run(bean);

        } catch (DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            request.setAttribute("teacherCreditsBean", bean);
            return mapping.findForward("edit-teacher-credits-periods");
        }

        request.setAttribute("teacherCreditsBean", bean);
        RenderUtils.invalidateViewState("teacherCreditsBeanID");
        return mapping.findForward("show-credits-periods");
    }

    public ActionForward closeAllPeriodsByExecutionSemester(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        TeacherCreditsPeriodBean bean = createBeanTeacherCreditsPeriodBean(mapping, actionForm, request);
        TeacherCreditsQueueJob.createTeacherCreditsQueueJob(bean.getExecutionPeriod());
        request.setAttribute("teacherCreditsBean", bean);
        return mapping.findForward("show-credits-periods");
    }

    public ActionForward openAllPeriodsByExecutionSemester(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        TeacherCreditsPeriodBean bean = createBeanTeacherCreditsPeriodBean(mapping, actionForm, request);
        TeacherCredits.openAllTeacherCredits(bean.getExecutionPeriod());
        request.setAttribute("teacherCreditsBean", bean);
        return mapping.findForward("show-credits-periods");
    }

    private TeacherCreditsPeriodBean createBeanTeacherCreditsPeriodBean(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request) throws Exception {
        TeacherCreditsPeriodBean bean = getRenderedObject("teacherCreditsBeanID");
        if (bean == null) {
            ExecutionSemester executionSemester = getExecutionPeriodToEditPeriod(request);
            if (executionSemester == null) {
                bean = new TeacherCreditsPeriodBean(ExecutionSemester.readActualExecutionSemester());
            } else {
                bean = new TeacherCreditsPeriodBean(executionSemester);
            }
        } else {
            bean.refreshDates();
        }
        return bean;
    }

    private ExecutionSemester getExecutionPeriodToEditPeriod(HttpServletRequest request) {
        String parameter = request.getParameter("executionPeriodId");
        return FenixFramework.getDomainObject(parameter);
    }

    public ActionForward prepareEditAnnualCreditsDates(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("editInterval", request.getParameter("editInterval"));
        request.setAttribute("teacherCreditsBean", getRenderedObject());
        return mapping.findForward("show-credits-periods");
    }

    public ActionForward editAnnualCreditsDates(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        TeacherCreditsPeriodBean bean = getRenderedObject();
        try {
            bean.editIntervals();
        } catch (Exception e) {
            addActionMessage(request, e.getMessage());
            request.setAttribute("editInterval", request.getParameter("editInterval"));
        }
        RenderUtils.invalidateViewState();
        request.setAttribute("teacherCreditsBean", bean);
        return mapping.findForward("show-credits-periods");
    }
}