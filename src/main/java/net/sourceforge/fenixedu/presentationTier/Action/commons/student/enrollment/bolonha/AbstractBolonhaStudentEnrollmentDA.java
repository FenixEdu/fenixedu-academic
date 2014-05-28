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
package net.sourceforge.fenixedu.presentationTier.Action.commons.student.enrollment.bolonha;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.student.enrolment.bolonha.EnrolBolonhaStudent;
import net.sourceforge.fenixedu.applicationTier.Servico.student.enrolment.bolonha.EnrolInAffinityCycle;
import net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha.BolonhaStudentEnrollmentBean;
import net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha.BolonhaStudentOptionalEnrollmentBean;
import net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha.CycleEnrolmentBean;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.enrolment.OptionalDegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.EnrollmentDomainException;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import net.sourceforge.fenixedu.injectionCode.IllegalDataAccessException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.CurricularRuleLabelFormatter;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;

@Forwards({

        @Forward(name = "showDegreeModulesToEnrol",
                path = "/academicAdminOffice/student/enrollment/bolonha/showDegreeModulesToEnrol.jsp"),

        @Forward(name = "chooseOptionalCurricularCourseToEnrol",
                path = "/academicAdminOffice/student/enrollment/bolonha/chooseOptionalCurricularCourseToEnrol.jsp"),

        @Forward(name = "chooseCycleCourseGroupToEnrol",
                path = "/academicAdminOffice/student/enrollment/bolonha/chooseCycleCourseGroupToEnrol.jsp"),

        @Forward(name = "notAuthorized", path = "/student/notAuthorized_bd.jsp")

})
public abstract class AbstractBolonhaStudentEnrollmentDA extends FenixDispatchAction {

    protected ActionForward prepareShowDegreeModulesToEnrol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response, final StudentCurricularPlan studentCurricularPlan,
            final ExecutionSemester executionSemester) {

        request.setAttribute("bolonhaStudentEnrollmentBean",
                createStudentEnrolmentBean(form, studentCurricularPlan, executionSemester));

        return mapping.findForward("showDegreeModulesToEnrol");
    }

    protected BolonhaStudentEnrollmentBean createStudentEnrolmentBean(ActionForm form,
            final StudentCurricularPlan studentCurricularPlan, final ExecutionSemester executionSemester) {

        return new BolonhaStudentEnrollmentBean(studentCurricularPlan, executionSemester,
                getCurricularYearForCurricularCourses(), getCurricularRuleLevel(form));
    }

    protected ActionForward prepareShowDegreeModulesToEnrol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response, final BolonhaStudentEnrollmentBean bolonhaStudentEnrollmentBean) {

        request.setAttribute("bolonhaStudentEnrollmentBean", bolonhaStudentEnrollmentBean);
        request.setAttribute("action", getAction());

        return mapping.findForward("showDegreeModulesToEnrol");
    }

    public ActionForward enrolInDegreeModules(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final BolonhaStudentEnrollmentBean bolonhaStudentEnrollmentBean = getBolonhaStudentEnrollmentBeanFromViewState();
        try {
            final RuleResult ruleResults =
                    EnrolBolonhaStudent.run(bolonhaStudentEnrollmentBean.getStudentCurricularPlan(),
                            bolonhaStudentEnrollmentBean.getExecutionPeriod(),
                            bolonhaStudentEnrollmentBean.getDegreeModulesToEvaluate(),
                            bolonhaStudentEnrollmentBean.getCurriculumModulesToRemove(),
                            bolonhaStudentEnrollmentBean.getCurricularRuleLevel());

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
                bolonhaStudentEnrollmentBean.getStudentCurricularPlan(), bolonhaStudentEnrollmentBean.getExecutionPeriod());
    }

    protected void enroledWithSuccess(HttpServletRequest request, BolonhaStudentEnrollmentBean bolonhaStudentEnrollmentBean) {
        // nothing to be done
    }

    protected BolonhaStudentEnrollmentBean getBolonhaStudentEnrollmentBeanFromViewState() {
        return getRenderedObject("bolonhaStudentEnrolments");
    }

    public ActionForward prepareChooseOptionalCurricularCourseToEnrol(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        final BolonhaStudentEnrollmentBean bean = getBolonhaStudentEnrollmentBeanFromViewState();
        request.setAttribute("optionalEnrolmentBean", new BolonhaStudentOptionalEnrollmentBean(bean.getStudentCurricularPlan(),
                bean.getExecutionPeriod(), bean.getOptionalDegreeModuleToEnrol()));

        request.setAttribute("curricularRuleLabels", getLabels(bean.getOptionalDegreeModuleToEnrol().getDegreeModule()
                .getCurricularRules(bean.getExecutionPeriod())));

        return mapping.findForward("chooseOptionalCurricularCourseToEnrol");
    }

    private List<String> getLabels(List<CurricularRule> curricularRules) {
        final List<String> result = new ArrayList<String>();
        for (final CurricularRule curricularRule : curricularRules) {
            result.add(CurricularRuleLabelFormatter.getLabel(curricularRule));
        }
        return result;
    }

    public ActionForward enrolInOptionalCurricularCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final BolonhaStudentOptionalEnrollmentBean optionalStudentEnrollmentBean =
                getBolonhaStudentOptionalEnrollmentBeanFromViewState();
        try {
            final RuleResult ruleResults =
                    EnrolBolonhaStudent.run(optionalStudentEnrollmentBean.getStudentCurricularPlan(),
                            optionalStudentEnrollmentBean.getExecutionPeriod(),
                            buildOptionalDegreeModuleToEnrolList(optionalStudentEnrollmentBean),
                            Collections.<CurriculumModule> emptyList(), getCurricularRuleLevel(form));

            if (ruleResults.isWarning()) {
                addRuleResultMessagesToActionMessages("warning", request, ruleResults);
            }

        } catch (EnrollmentDomainException ex) {
            addRuleResultMessagesToActionMessages("error", request, ex.getFalseResult());
            request.setAttribute("optionalEnrolmentBean", optionalStudentEnrollmentBean);

            return mapping.findForward("chooseOptionalCurricularCourseToEnrol");

        } catch (DomainException ex) {
            addActionMessage("error", request, ex.getKey(), ex.getArgs());
            request.setAttribute("optionalEnrolmentBean", optionalStudentEnrollmentBean);

            return mapping.findForward("chooseOptionalCurricularCourseToEnrol");
        }

        return prepareShowDegreeModulesToEnrol(mapping, form, request, response,
                optionalStudentEnrollmentBean.getStudentCurricularPlan(), optionalStudentEnrollmentBean.getExecutionPeriod());
    }

    private List<IDegreeModuleToEvaluate> buildOptionalDegreeModuleToEnrolList(
            final BolonhaStudentOptionalEnrollmentBean optionalStudentEnrollmentBean) {
        final IDegreeModuleToEvaluate selectedDegreeModuleToEnrol =
                optionalStudentEnrollmentBean.getSelectedDegreeModuleToEnrol();
        final OptionalDegreeModuleToEnrol optionalDegreeModuleToEnrol =
                new OptionalDegreeModuleToEnrol(selectedDegreeModuleToEnrol.getCurriculumGroup(),
                        selectedDegreeModuleToEnrol.getContext(), optionalStudentEnrollmentBean.getExecutionPeriod(),
                        optionalStudentEnrollmentBean.getSelectedOptionalCurricularCourse());

        final List<IDegreeModuleToEvaluate> result = new ArrayList<IDegreeModuleToEvaluate>();
        result.add(optionalDegreeModuleToEnrol);

        return result;
    }

    public ActionForward cancelChooseOptionalCurricularCourseToEnrol(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        final BolonhaStudentOptionalEnrollmentBean bolonhaStudentOptionalEnrollmentBean =
                getBolonhaStudentOptionalEnrollmentBeanFromViewState();
        return prepareShowDegreeModulesToEnrol(mapping, form, request, response,
                bolonhaStudentOptionalEnrollmentBean.getStudentCurricularPlan(),
                bolonhaStudentOptionalEnrollmentBean.getExecutionPeriod());
    }

    protected BolonhaStudentOptionalEnrollmentBean getBolonhaStudentOptionalEnrollmentBeanFromViewState() {
        return getRenderedObject("optionalEnrolment");
    }

    public ActionForward updateParametersToSearchOptionalCurricularCourses(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final BolonhaStudentOptionalEnrollmentBean optionalBean = getBolonhaStudentOptionalEnrollmentBeanFromViewState();
        request.setAttribute("optionalEnrolmentBean", optionalBean);
        RenderUtils.invalidateViewState();

        request.setAttribute("curricularRuleLabels", getLabels(optionalBean.getSelectedDegreeModuleToEnrol().getDegreeModule()
                .getCurricularRules(optionalBean.getExecutionPeriod())));

        return mapping.findForward("chooseOptionalCurricularCourseToEnrol");
    }

    public ActionForward prepareChooseCycleCourseGroupToEnrol(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final BolonhaStudentEnrollmentBean studentEnrollmentBean = getBolonhaStudentEnrollmentBeanFromViewState();

        final CycleEnrolmentBean cycleEnrolmentBean =
                new CycleEnrolmentBean(studentEnrollmentBean.getStudentCurricularPlan(),
                        studentEnrollmentBean.getExecutionPeriod(), studentEnrollmentBean.getCycleTypeToEnrol()
                                .getSourceCycleAffinity(), studentEnrollmentBean.getCycleTypeToEnrol());
        request.setAttribute("cycleEnrolmentBean", cycleEnrolmentBean);

        return mapping.findForward("chooseCycleCourseGroupToEnrol");
    }

    protected ActionForward prepareChooseCycleCourseGroupToEnrol(final ActionMapping mapping, final HttpServletRequest request,
            final StudentCurricularPlan studentCurricularPlan, final ExecutionSemester executionSemester,
            final CycleType sourceCycle, final CycleType cycleToEnrol) {

        request.setAttribute("cycleEnrolmentBean", new CycleEnrolmentBean(studentCurricularPlan, executionSemester, sourceCycle,
                cycleToEnrol));
        return mapping.findForward("chooseCycleCourseGroupToEnrol");
    }

    public ActionForward enrolInCycleCourseGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final CycleEnrolmentBean cycleEnrolmentBean = getCycleEnrolmentBeanFromViewState();

        try {
            EnrolInAffinityCycle.run(getLoggedPerson(request), cycleEnrolmentBean);

        } catch (final IllegalDataAccessException e) {
            addActionMessage(request, "error.NotAuthorized");

            request.setAttribute("withRules", request.getParameter("withRules"));
            request.setAttribute("cycleEnrolmentBean", cycleEnrolmentBean);
            return mapping.findForward("chooseCycleCourseGroupToEnrol");

        } catch (final DomainException e) {
            addActionMessage(request, e.getKey(), e.getArgs());

            request.setAttribute("withRules", request.getParameter("withRules"));
            request.setAttribute("cycleEnrolmentBean", cycleEnrolmentBean);
            return mapping.findForward("chooseCycleCourseGroupToEnrol");
        }

        return prepareShowDegreeModulesToEnrol(mapping, form, request, response, cycleEnrolmentBean.getStudentCurricularPlan(),
                cycleEnrolmentBean.getExecutionPeriod());
    }

    public ActionForward enrolInCycleCourseGroupInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("cycleEnrolmentBean", getCycleEnrolmentBeanFromViewState());
        request.setAttribute("withRules", request.getParameter("withRules"));

        return mapping.findForward("chooseCycleCourseGroupToEnrol");
    }

    private CycleEnrolmentBean getCycleEnrolmentBeanFromViewState() {
        return getRenderedObject("cycleEnrolmentBean");
    }

    abstract public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response);

    abstract protected int[] getCurricularYearForCurricularCourses();

    abstract protected CurricularRuleLevel getCurricularRuleLevel(final ActionForm form);

    abstract protected String getAction();

}