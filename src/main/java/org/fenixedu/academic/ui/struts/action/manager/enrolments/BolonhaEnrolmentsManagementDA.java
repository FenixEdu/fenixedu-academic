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
package org.fenixedu.academic.ui.struts.action.manager.enrolments;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.dto.InfoExecutionPeriod;
import org.fenixedu.academic.dto.commons.student.StudentNumberBean;
import org.fenixedu.academic.dto.student.enrollment.bolonha.BolonhaStudentEnrollmentBean;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.registration.TransitToBolonha;
import org.fenixedu.academic.ui.struts.action.commons.student.enrollment.bolonha.AbstractBolonhaStudentEnrollmentDA;
import org.fenixedu.academic.ui.struts.action.manager.ManagerApplications.ManagerStudentsApp;
import org.fenixedu.academic.util.DateFormatUtil;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@StrutsFunctionality(app = ManagerStudentsApp.class, path = "manage", titleKey = "label.manage")
@Mapping(path = "/bolonhaStudentEnrolment", module = "manager", formBean = "bolonhaStudentEnrolmentForm")
@Forwards({
        @Forward(name = "viewStudentCurriculum", path = "/manager/bolonha/enrolments/displayStudentCurriculum.jsp"),
        @Forward(name = "chooseStudentInformation", path = "/manager/bolonha/enrolments/chooseStudentInformation.jsp"),
        @Forward(name = "showExecutionPeriodToEnrol", path = "/manager/bolonha/enrolments/chooseExecutionPeriod.jsp"),
        @Forward(name = "showDegreeModulesToEnrol", path = "/manager/bolonha/enrolments/showDegreeModulesToEnrol.jsp"),
        @Forward(name = "chooseOptionalCurricularCourseToEnrol",
                path = "/manager/bolonha/enrolments/chooseOptionalCurricularCourseToEnrol.jsp"),
        @Forward(name = "chooseCycleCourseGroupToEnrol", path = "/manager/bolonha/enrolments/chooseCycleCourseGroupToEnrol.jsp"),
        @Forward(name = "transitToBolonha", path = "/manager/bolonha/enrolments/transitToBolonha.jsp"),
        @Forward(name = "showRegistrationStatesLog", path = "/manager/bolonha/enrolments/showRegistrationStatesLog.jsp") })
public class BolonhaEnrolmentsManagementDA extends AbstractBolonhaStudentEnrollmentDA {

    @EntryPoint
    public ActionForward prepareSearchStudent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final Object renderedObject = getRenderedObject("student-number-bean");
        if (renderedObject == null) {
            request.setAttribute("studentNumberBean", new StudentNumberBean());
        } else {
            final StudentNumberBean numberBean = (StudentNumberBean) renderedObject;
            final Student student = getStudent(numberBean);
            if (student != null) {
                RenderUtils.invalidateViewState();
                request.setAttribute("registrations", getAllRegistrations(student));
                request.setAttribute("studentNumberBean", numberBean);
            }
        }

        return mapping.findForward("chooseStudentInformation");
    }

    public ActionForward showAllStudentCurricularPlans(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final StudentNumberBean studentNumberBean = new StudentNumberBean();
        final Student student = getStudent(request);
        studentNumberBean.setNumber(student.getNumber());

        request.setAttribute("studentNumberBean", studentNumberBean);
        request.setAttribute("registrations", getAllRegistrations(student));

        return mapping.findForward("chooseStudentInformation");
    }

    private Student getStudent(final HttpServletRequest request) {
        return getDomainObject(request, "studentId");
    }

    private List<Registration> getAllRegistrations(final Student student) {
        final List<Registration> result = new ArrayList<Registration>();
        result.addAll(student.getRegistrationsSet());
        result.addAll(student.getTransitionRegistrations());
        Collections.sort(result, new ReverseComparator(Registration.COMPARATOR_BY_START_DATE));
        return result;
    }

    private Student getStudent(final StudentNumberBean numberBean) {
        return numberBean.getNumber() != null ? Student.readStudentByNumber(numberBean.getNumber()) : null;
    }

    private StudentCurricularPlan getStudentCurricularPlan(final HttpServletRequest request) {
        return getDomainObject(request, "scpId");
    }

    public ActionForward prepareChooseExecutionPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ((DynaActionForm) form).set("scpId", getStudentCurricularPlan(request).getExternalId());
        request.setAttribute("infoExecutionPeriod", new InfoExecutionPeriod(ExecutionSemester.readActualExecutionSemester()));
        return mapping.findForward("showExecutionPeriodToEnrol");
    }

    public ActionForward prepareShowDegreeModulesToEnrol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        InfoExecutionPeriod executionPeriodBean = getRenderedObject("infoExecutionPeriod");

        request.setAttribute("bolonhaStudentEnrollmentBean", new BolonhaStudentEnrollmentBean(getStudentCurricularPlan(request),
                executionPeriodBean.getExecutionPeriod(), getCurricularYearForCurricularCourses(), getCurricularRuleLevel(form)));

        return mapping.findForward("showDegreeModulesToEnrol");
    }

    public ActionForward backToAllStudentCurricularPlans(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final BolonhaStudentEnrollmentBean bean = getRenderedObject("bolonhaStudentEnrolments");
        request.setAttribute("studentId", bean.getStudentCurricularPlan().getRegistration().getStudent().getExternalId());
        return showAllStudentCurricularPlans(mapping, form, request, response);
    }

    public ActionForward viewStudentCurriculum(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final DynaActionForm form = (DynaActionForm) actionForm;
        if (StringUtils.isEmpty(form.getString("detailed"))) {
            form.set("detailed", Boolean.TRUE.toString());
        }
        request.setAttribute("studentCurricularPlan", getStudentCurricularPlan(request));

        return mapping.findForward("viewStudentCurriculum");
    }

    @Override
    public ActionForward prepareShowDegreeModulesToEnrol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response, BolonhaStudentEnrollmentBean bean) {

        request.setAttribute("bolonhaStudentEnrollmentBean", bean);
        return mapping.findForward("showDegreeModulesToEnrol");
    }

    @Override
    protected int[] getCurricularYearForCurricularCourses() {
        return null; // all years
    }

    @Override
    protected CurricularRuleLevel getCurricularRuleLevel(ActionForm form) {
        return CurricularRuleLevel.ENROLMENT_NO_RULES;
    }

    @Override
    protected String getAction() {
        // unnecessary method
        return null;
    }

    @Override
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        // unnecessary method
        return null;
    }

    public ActionForward prepareTransit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("studentCurricularPlan", getStudentCurricularPlan(request));
        return mapping.findForward("transitToBolonha");
    }

    public ActionForward transitToBolonha(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final DynaActionForm form = (DynaActionForm) actionForm;
        final DateTime date;
        try {
            date = YearMonthDay.fromDateFields(DateFormatUtil.parse("dd/MM/yyyy", form.getString("date"))).toDateTimeAtMidnight();
        } catch (final ParseException e) {
            addActionMessage(request, "error.invalid.date.format");
            return prepareTransit(mapping, actionForm, request, response);
        }

        final StudentCurricularPlan studentCurricularPlan = getStudentCurricularPlan(request);
        try {
            TransitToBolonha.run(null, studentCurricularPlan.getRegistration().getSourceRegistrationForTransition(), date);
        } catch (final DomainException e) {
            addActionMessage(request, e.getKey(), e.getArgs());
            return prepareTransit(mapping, actionForm, request, response);
        }

        return showAllStudentCurricularPlans(mapping, actionForm, request, response);
    }

    public ActionForward showRegistrationStatesLog(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("registration", getDomainObject(request, "registrationId"));
        return mapping.findForward("showRegistrationStatesLog");
    }

}