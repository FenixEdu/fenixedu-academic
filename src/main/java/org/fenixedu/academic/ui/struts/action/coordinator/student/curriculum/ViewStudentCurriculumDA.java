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
package org.fenixedu.academic.ui.struts.action.coordinator.student.curriculum;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.coordinator.DegreeCoordinatorIndex;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "coordinator", path = "/viewStudentCurriculumSearch", formBean = "viewStudentCurriculumForm",
        functionality = DegreeCoordinatorIndex.class)
@Forwards({ @Forward(name = "chooseStudent", path = "/coordinator/student/curriculum/chooseStudent.jsp"),
        @Forward(name = "chooseCurriculumType", path = "/coordinator/student/curriculum/chooseCurriculumType.jsp") })
public class ViewStudentCurriculumDA extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DegreeCoordinatorIndex.setCoordinatorContext(request);
        return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        setDegreeCurricularPlanId(form, getDegreeCurricularPlanId(request));
        setExecutionDegreeId(form, getExecutionDegreeId(request));

        return mapping.findForward("chooseStudent");
    }

    public ActionForward showStudentCurriculum(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        final Student student = getStudent(actionForm);

        if (student == null) {
            addActionMessage(request, "label.coordinator.viewStudentCurriculum.noStudent");
            return mapping.findForward("chooseStudent");
        }

        return getOldCurriculumRedirect(actionForm, student);
    }

    private ActionForward getOldCurriculumRedirect(final ActionForm actionForm, final Student student) {
        final ActionForward actionForward = new ActionForward();
        actionForward.setPath("/viewStudentCurriculum.do?method=prepareReadByStudentNumber&studentNumber=" + student.getNumber()
                + "&executionDegreeId=" + getExecutionDegreeId(actionForm) + "&degreeCurricularPlanID="
                + getDegreeCurricularPlanId(actionForm));
        return actionForward;
    }

    private String getExecutionDegreeId(final HttpServletRequest request) {
        return request.getParameter("executionDegreeId");
    }

    private String getExecutionDegreeId(final ActionForm actionForm) {
        return (String) ((DynaActionForm) actionForm).get("executionDegreeId");
    }

    private void setExecutionDegreeId(final ActionForm actionForm, final String id) {
        ((DynaActionForm) actionForm).set("executionDegreeId", id);
    }

    private DegreeCurricularPlan getDegreeCurricularPlan(final ActionForm form) {
        return FenixFramework.getDomainObject(getDegreeCurricularPlanId(form));
    }

    private String getDegreeCurricularPlanId(final HttpServletRequest request) {
        return request.getParameter("degreeCurricularPlanID");
    }

    private void setDegreeCurricularPlanId(final ActionForm actionForm, final String id) {
        ((DynaActionForm) actionForm).set("degreeCurricularPlanId", id);
    }

    private String getDegreeCurricularPlanId(final ActionForm actionForm) {
        return (String) ((DynaActionForm) actionForm).get("degreeCurricularPlanId");
    }

    private Student getStudent(final ActionForm actionForm) {
        final String studentNumberString = ((DynaActionForm) actionForm).getString("studentNumber");

        if (!StringUtils.isEmpty(studentNumberString) && NumberUtils.isNumber(studentNumberString)) {
            return Student.readStudentByNumber(Integer.valueOf(studentNumberString));
        }

        return null;
    }

}
