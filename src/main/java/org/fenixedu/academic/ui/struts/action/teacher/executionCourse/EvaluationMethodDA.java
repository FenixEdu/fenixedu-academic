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
package org.fenixedu.academic.ui.struts.action.teacher.executionCourse;

import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.academic.domain.CompetenceCourse;
import org.fenixedu.academic.domain.EvaluationMethod;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.dto.teacher.executionCourse.ImportContentBean;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.teacher.EditEvaluation;
import org.fenixedu.academic.ui.struts.action.teacher.ManageExecutionCourseDA;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Input;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.commons.i18n.LocalizedString;

@Mapping(path = "/manageEvaluationMethod", module = "teacher", functionality = ManageExecutionCourseDA.class,
        formBean = "evaluationMethodForm")
@Forwards({ @Forward(name = "evaluationMethod", path = "/teacher/executionCourse/evaluationMethod.jsp"),
        @Forward(name = "editEvaluationMethod", path = "/teacher/executionCourse/editEvaluationMethod.jsp"),
        @Forward(name = "importEvaluationMethod", path = "/teacher/executionCourse/importEvaluationMethod.jsp") })
public class EvaluationMethodDA extends ManageExecutionCourseDA {

    // EVALUATION METHOD

    @Input
    public ActionForward evaluationMethod(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        final ExecutionCourse executionCourse = getExecutionCourse(request);
        request.setAttribute("evaluationMethods", getEvaluationMethod(executionCourse));
        return mapping.findForward("evaluationMethod");
    }

    private LocalizedString getEvaluationMethod(final ExecutionCourse executionCourse) {
        if (executionCourse.getEvaluationMethod() != null) {
            return executionCourse.getEvaluationMethod().getEvaluationElements();
        } else {
            String competenceMethod = !executionCourse.getCompetenceCourses().isEmpty() ? executionCourse.getCompetenceCourses()
                    .iterator().next().getEvaluationMethod() : "";
            return new LocalizedString(Locale.getDefault(), competenceMethod);
        }
    }

    public ActionForward prepareEditEvaluationMethod(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");
        EvaluationMethod evaluationMethod = executionCourse.getEvaluationMethod();
        LocalizedString evaluationElements = evaluationMethod == null ? null : evaluationMethod.getEvaluationElements();
        if (evaluationMethod == null || evaluationElements == null || evaluationElements.isEmpty()
                || StringUtils.isEmpty(evaluationElements.getContent())) {
            LocalizedString evaluationMethodMls = new LocalizedString();
            final Set<CompetenceCourse> competenceCourses = executionCourse.getCompetenceCourses();
            if (!competenceCourses.isEmpty()) {
                final CompetenceCourse competenceCourse = competenceCourses.iterator().next();
                final String pt = competenceCourse.getEvaluationMethod();
                final String en = competenceCourse.getEvaluationMethodEn();
                evaluationMethodMls = evaluationMethodMls.with(org.fenixedu.academic.util.LocaleUtils.PT, pt == null ? "" : pt)
                        .with(org.fenixedu.academic.util.LocaleUtils.EN, en == null ? "" : en);
            }
            EditEvaluation.runEditEvaluation(executionCourse, evaluationMethodMls);
            evaluationMethod = executionCourse.getEvaluationMethod();
        }
        return mapping.findForward("editEvaluationMethod");
    }

    public ActionForward editEvaluationMethod(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final DynaActionForm dynaActionForm = (DynaActionForm) form;
        final String evaluationMethod = request.getParameter("evaluationMethod");
        final String evaluationMethodEn = dynaActionForm.getString("evaluationMethodEn");
        LocalizedString localizedString = new LocalizedString();
        localizedString = localizedString.with(org.fenixedu.academic.util.LocaleUtils.PT, evaluationMethod);
        localizedString = localizedString.with(org.fenixedu.academic.util.LocaleUtils.EN, evaluationMethodEn);

        final ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");

        EditEvaluation.runEditEvaluation(executionCourse, localizedString);

        return mapping.findForward("evaluationMethod");
    }

    public ActionForward prepareImportEvaluationMethod(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {

        request.setAttribute("importContentBean", new ImportContentBean());
        return mapping.findForward("importEvaluationMethod");
    }

    public ActionForward prepareImportEvaluationMethodPostBack(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) {

        prepareImportContentPostBack(request);
        return mapping.findForward("importEvaluationMethod");
    }

    public ActionForward prepareImportEvaluationMethodInvalid(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) {

        prepareImportContentInvalid(request);
        return mapping.findForward("importEvaluationMethod");
    }

    public ActionForward listExecutionCoursesToImportEvaluationMethod(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) {

        listExecutionCoursesToImportContent(request);
        return mapping.findForward("importEvaluationMethod");
    }

    public ActionForward importEvaluationMethod(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) throws FenixServiceException {

        importContent(request, "ImportEvaluationMethod");
        return mapping.findForward("evaluationMethod");
    }

}
