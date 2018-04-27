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

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.EnrolmentEvaluation;
import org.fenixedu.academic.domain.EvaluationSeason;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceEvent;
import org.fenixedu.academic.domain.accounting.events.AnnualEvent;
import org.fenixedu.academic.domain.accounting.events.gratuity.GratuityEvent;
import org.fenixedu.academic.domain.accounting.events.insurance.InsuranceEvent;
import org.fenixedu.academic.domain.curricularRules.CurricularRuleValidationType;
import org.fenixedu.academic.domain.curricularRules.executors.RuleResult;
import org.fenixedu.academic.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.exceptions.EnrollmentDomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.dto.student.enrollment.bolonha.ChooseEvaluationSeasonBean;
import org.fenixedu.academic.dto.student.enrollment.bolonha.SpecialSeasonBolonhaStudentEnrolmentBean;
import org.fenixedu.academic.dto.student.enrollment.bolonha.SpecialSeasonChooseEvaluationSeasonBean;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.action.administrativeOffice.student.SearchForStudentsDA;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import com.google.common.collect.Sets;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@Mapping(path = "/specialSeasonBolonhaStudentEnrollment", module = "academicAdministration",
        formBean = "bolonhaStudentEnrollmentForm", functionality = SearchForStudentsDA.class)
@Forwards({
        @Forward(name = "chooseEvaluationSeason",
                path = "/academicAdminOffice/student/enrollment/bolonha/chooseEvaluationSeason.jsp"),
        @Forward(name = "showDegreeModulesToEnrol",
                path = "/academicAdminOffice/student/enrollment/bolonha/showDegreeModulesToEnrol.jsp"),
        @Forward(name = "showStudentEnrollmentMenu",
                path = "/academicAdministration/studentEnrolments.do?method=prepareFromStudentEnrollmentWithRules") })
public class AcademicAdminOfficeSpecialSeasonBolonhaStudentEnrolmentDA extends AcademicAdminOfficeBolonhaStudentEnrollmentDA {

    @Override
    protected CurricularRuleLevel getCurricularRuleLevel(ActionForm form) {
        return CurricularRuleLevel.SPECIAL_SEASON_ENROLMENT;
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("action", getAction());

        return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward prepareChooseEvaluationSeason(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        request.setAttribute("chooseEvaluationSeasonBean", new SpecialSeasonChooseEvaluationSeasonBean());
        request.setAttribute("studentCurricularPlan", getStudentCurricularPlan(request));
        request.setAttribute("executionPeriod", getExecutionPeriod(request));

        return mapping.findForward("chooseEvaluationSeason");
    }

    public ActionForward chooseEvaluationSeasonInvalid(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        ChooseEvaluationSeasonBean chooseEvaluationSeasonBean = getRenderedObject("chooseEvaluationSeasonBean");

        request.setAttribute("chooseEvaluationSeasonBean", chooseEvaluationSeasonBean);
        request.setAttribute("studentCurricularPlan", getStudentCurricularPlan(request));
        request.setAttribute("executionPeriod", getExecutionPeriod(request));

        return mapping.findForward("chooseEvaluationSeason");
    }

    @Override
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ChooseEvaluationSeasonBean chooseEvaluationSeasonBean = getRenderedObject("chooseEvaluationSeasonBean");

        return prepareShowDegreeModulesToEnrol(mapping, form, request, response, getStudentCurricularPlan(request),
                getExecutionPeriod(request), chooseEvaluationSeasonBean.getEvaluationSeason());
    }

    public ActionForward checkPermission(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        ChooseEvaluationSeasonBean chooseEvaluationSeasonBean = getRenderedObject("chooseEvaluationSeasonBean");

        return prepareShowDegreeModulesToEnrol(mapping, form, request, response, getStudentCurricularPlan(request),
                getExecutionPeriod(request), chooseEvaluationSeasonBean.getEvaluationSeason());
    }

    @Override
    @Deprecated
    protected ActionForward prepareShowDegreeModulesToEnrol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response, StudentCurricularPlan studentCurricularPlan, ExecutionSemester executionSemester) {
        throw new RuntimeException("not to be used");
    }

    protected ActionForward prepareShowDegreeModulesToEnrol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response, StudentCurricularPlan studentCurricularPlan, ExecutionSemester executionSemester,
            final EvaluationSeason evaluationSeason) {
        request.setAttribute("bolonhaStudentEnrollmentBean",
                new SpecialSeasonBolonhaStudentEnrolmentBean(studentCurricularPlan, executionSemester, evaluationSeason));
        request.setAttribute("evaluationSeason", evaluationSeason.getName().getContent());
        request.setAttribute("enroledEctsCredits", getEnroledEctsCredits(studentCurricularPlan, executionSemester));
        request.setAttribute("enroledExtraEctsCredits",
                getEnroledSpecialSeasonEctsCredits(studentCurricularPlan, executionSemester));
        request.setAttribute("label.ects.extra", BundleUtil.getString(Bundle.ACADEMIC, "label.ects.special"));

        addDebtsWarningMessagesForExecutionInterval(studentCurricularPlan.getRegistration().getStudent(),
                executionSemester.getExecutionYear(), request);

        return mapping.findForward("showDegreeModulesToEnrol");
    }

    static private Double getEnroledEctsCredits(final StudentCurricularPlan plan, final ExecutionSemester semester) {
        return isEnrolmentByYear(plan) ? plan.getRoot().getEnroledEctsCredits(semester.getExecutionYear()) : plan.getRoot()
                .getEnroledEctsCredits(semester);
    }

    static private Double getEnroledSpecialSeasonEctsCredits(final StudentCurricularPlan plan, final ExecutionSemester semester) {

        Double result = 0d;

        final Set<EnrolmentEvaluation> toInspect = Sets.newHashSet();
        if (isEnrolmentByYear(plan)) {
            for (final ExecutionSemester iter : semester.getExecutionYear().getExecutionPeriodsSet()) {
                toInspect.addAll(plan.getEnroledSpecialSeasons(iter));
            }
        } else {
            toInspect.addAll(plan.getEnroledSpecialSeasons(semester));
        }

        for (final EnrolmentEvaluation enrolmentEvaluation : toInspect) {
            final Enrolment enrolment = enrolmentEvaluation.getEnrolment();
            result += enrolment.getEctsCredits();
        }

        return result;
    }

    private static boolean isEnrolmentByYear(final StudentCurricularPlan plan) {
        return plan.getDegreeCurricularPlan().getCurricularRuleValidationType() == CurricularRuleValidationType.YEAR;
    }

    private void addDebtsWarningMessagesForExecutionInterval(final Student student, final ExecutionYear executionYear,
            final HttpServletRequest request) {

        if (hasAnyAdministrativeOfficeFeeAndInsuranceInDebt(student, executionYear)) {
            addActionMessage("warning", request, "registration.has.not.payed.insurance.fees");
        }

        if (hasAnyGratuityDebt(student, executionYear)) {
            addActionMessage("warning", request, "registration.has.not.payed.gratuities");
        }
    }

    static protected boolean hasAnyAdministrativeOfficeFeeAndInsuranceInDebt(final Student student,
            final ExecutionYear executionYear) {
        for (final Event event : student.getPerson().getEventsSet()) {

            if (event instanceof AnnualEvent) {
                final AnnualEvent annualEvent = (AnnualEvent) event;
                if (annualEvent.getExecutionYear().isAfter(executionYear)) {
                    continue;
                }
            }

            if ((event instanceof AdministrativeOfficeFeeAndInsuranceEvent || event instanceof InsuranceEvent)
                    && event.isOpen()) {
                return true;
            }
        }

        return false;
    }

    static protected boolean hasAnyGratuityDebt(final Student student, final ExecutionYear executionYear) {
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

    @Override
    public ActionForward enrolInDegreeModules(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final SpecialSeasonBolonhaStudentEnrolmentBean bolonhaStudentEnrollmentBean =
                (SpecialSeasonBolonhaStudentEnrolmentBean) getBolonhaStudentEnrollmentBeanFromViewState();
        try {
            StudentCurricularPlan studentCurricularPlan = bolonhaStudentEnrollmentBean.getStudentCurricularPlan();
            final RuleResult ruleResults = studentCurricularPlan.enrol(bolonhaStudentEnrollmentBean.getExecutionPeriod(),
                    new HashSet<IDegreeModuleToEvaluate>(bolonhaStudentEnrollmentBean.getDegreeModulesToEvaluate()),
                    bolonhaStudentEnrollmentBean.getCurriculumModulesToRemove(),
                    bolonhaStudentEnrollmentBean.getCurricularRuleLevel(), bolonhaStudentEnrollmentBean.getEvaluationSeason());

            if (!bolonhaStudentEnrollmentBean.getDegreeModulesToEvaluate().isEmpty()
                    || !bolonhaStudentEnrollmentBean.getCurriculumModulesToRemove().isEmpty()) {
                addActionMessage("success", request, "label.save.success");
            }

            if (ruleResults.isWarning()) {
                addRuleResultMessagesToActionMessages("warning", request, ruleResults);
            }

            enroledWithSuccess(request, bolonhaStudentEnrollmentBean);

        } catch (EnrollmentDomainException ex) {
            addRuleResultMessagesToActionMessages("error", request, ex.getFalseResult());

            return prepareShowDegreeModulesToEnrol(mapping, form, request, response, bolonhaStudentEnrollmentBean);

        } catch (DomainException ex) {
            addActionMessage("error", request, ex.getKey(), ex.getArgs());

            return prepareShowDegreeModulesToEnrol(mapping, form, request, response, bolonhaStudentEnrollmentBean);
        }

        RenderUtils.invalidateViewState();

        return prepareShowDegreeModulesToEnrol(mapping, form, request, response,
                bolonhaStudentEnrollmentBean.getStudentCurricularPlan(), bolonhaStudentEnrollmentBean.getExecutionPeriod(),
                bolonhaStudentEnrollmentBean.getEvaluationSeason());
    }

}
