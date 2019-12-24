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
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import org.fenixedu.academic.domain.studentCurriculum.NoCourseGroupCurriculumGroupType;
import org.fenixedu.academic.dto.administrativeOffice.studentEnrolment.StudentStandaloneEnrolmentBean;
import org.fenixedu.academic.ui.struts.action.administrativeOffice.student.SearchForStudentsDA;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@Mapping(path = "/studentStandaloneEnrolments", module = "academicAdministration", functionality = SearchForStudentsDA.class)
@Forwards({ @Forward(name = "showExtraEnrolments", path = "/academicAdminOffice/showNoCourseGroupCurriculumGroupEnrolments.jsp"),
        @Forward(name = "chooseExtraEnrolment", path = "/academicAdminOffice/chooseNoCourseGroupCurriculumGroupEnrolment.jsp"),
        @Forward(name = "showDegreeModulesToEnrol",
                path = "/academicAdministration/studentEnrolments.do?method=prepareFromExtraEnrolment") })
public class StudentStandaloneEnrolmentsDA extends NoCourseGroupCurriculumGroupEnrolmentsDA {

    @Override
    protected StudentStandaloneEnrolmentBean createNoCourseGroupEnrolmentBean(final StudentCurricularPlan studentCurricularPlan,
            final ExecutionInterval executionInterval) {
        return new StudentStandaloneEnrolmentBean(studentCurricularPlan, executionInterval);
    }

    @Override
    protected String getActionName() {
        return "studentStandaloneEnrolments";
    }

    @Override
    protected NoCourseGroupCurriculumGroupType getGroupType() {
        return NoCourseGroupCurriculumGroupType.STANDALONE;
    }

    @Override
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        final ExecutionInterval executionInterval = getExecutionInterval(request);
        if (isStudentInPartialRegime(request, executionInterval)) {
            addActionMessage("error", request, "error.Student.has.partial.regime", executionInterval.getQualifiedName());
        }
        return super.prepare(mapping, actionForm, request, response);
    }

    private void chooseCurricular(HttpServletRequest request, CurricularRuleLevel level) {

        final StudentStandaloneEnrolmentBean bean = (StudentStandaloneEnrolmentBean) getNoCourseGroupEnrolmentBean();
        bean.setCurricularRuleLevel(level);

        request.setAttribute("enrolmentBean", bean);
        RenderUtils.invalidateViewState();
    }

    @Override
    public ActionForward chooseCurricular(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        chooseCurricular(request, CurricularRuleLevel.STANDALONE_ENROLMENT);
        return mapping.findForward("chooseExtraEnrolment");
    }

    public ActionForward chooseCurricularWithoutRules(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        chooseCurricular(request, CurricularRuleLevel.STANDALONE_ENROLMENT_NO_RULES);
        return mapping.findForward("chooseExtraEnrolment");
    }

    private boolean isStudentInPartialRegime(final HttpServletRequest request, final ExecutionInterval interval) {
        return getStudentCurricularPlan(request).getRegistration().isPartialRegime(interval.getExecutionYear());
    }

}
