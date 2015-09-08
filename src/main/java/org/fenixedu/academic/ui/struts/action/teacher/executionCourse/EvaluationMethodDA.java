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
import org.fenixedu.academic.domain.CompetenceCourse;
import org.fenixedu.academic.domain.EvaluationMethod;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.dto.teacher.executionCourse.ImportContentBean;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.teacher.EditEvaluation;
import org.fenixedu.academic.ui.struts.action.teacher.ManageExecutionCourseDA;
import org.fenixedu.academic.util.MultiLanguageString;
import org.fenixedu.bennu.struts.annotations.Input;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.commons.i18n.LocalizedString;

@Mapping(path = "/manageEvaluationMethod", module = "teacher", functionality = ManageExecutionCourseDA.class,
        formBean = "evaluationMethodForm")
public class EvaluationMethodDA extends ManageExecutionCourseDA {

    // EVALUATION METHOD

    @Input
    public ActionForward evaluationMethod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final ExecutionCourse executionCourse = getExecutionCourse(request);
        request.setAttribute("evaluationMethods", getEvaluationMethod(executionCourse));
        return forward(request, "/teacher/executionCourse/evaluationMethod.jsp");
    }

    private LocalizedString getEvaluationMethod(ExecutionCourse executionCourse) {
        if (executionCourse.getEvaluationMethod() != null) {
            return executionCourse.getEvaluationMethod().getEvaluationElements().toLocalizedString();
        } else {
            String competenceMethod =
                    !executionCourse.getCompetenceCourses().isEmpty() ? executionCourse.getCompetenceCourses().iterator().next()
                            .getEvaluationMethod() : "";
            return new LocalizedString(Locale.getDefault(), competenceMethod);
        }
    }

    public ActionForward prepareEditEvaluationMethod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");
        EvaluationMethod evaluationMethod = executionCourse.getEvaluationMethod();
        MultiLanguageString evaluationElements = evaluationMethod == null ? null : evaluationMethod.getEvaluationElements();
        if (evaluationMethod == null || evaluationElements == null || evaluationElements.isEmpty()
                || StringUtils.isEmpty(evaluationElements.getContent())) {
            MultiLanguageString evaluationMethodMls = new MultiLanguageString();
            final Set<CompetenceCourse> competenceCourses = executionCourse.getCompetenceCourses();
            if (!competenceCourses.isEmpty()) {
                final CompetenceCourse competenceCourse = competenceCourses.iterator().next();
                final String pt = competenceCourse.getEvaluationMethod();
                final String en = competenceCourse.getEvaluationMethodEn();
                evaluationMethodMls =
                        evaluationMethodMls.with(MultiLanguageString.pt, pt == null ? "" : pt).with(MultiLanguageString.en,
                                en == null ? "" : en);
            }
            EditEvaluation.runEditEvaluation(executionCourse, evaluationMethodMls);
            evaluationMethod = executionCourse.getEvaluationMethod();
        }
        return forward(request, "/teacher/executionCourse/editEvaluationMethod.jsp");
    }

    public ActionForward prepareImportEvaluationMethod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("importContentBean", new ImportContentBean());
        return forward(request, "/teacher/executionCourse/importEvaluationMethod.jsp");
    }

    public ActionForward prepareImportEvaluationMethodPostBack(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        prepareImportContentPostBack(request);
        return forward(request, "/teacher/executionCourse/importEvaluationMethod.jsp");
    }

    public ActionForward prepareImportEvaluationMethodInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        prepareImportContentInvalid(request);
        return forward(request, "/teacher/executionCourse/importEvaluationMethod.jsp");
    }

    public ActionForward listExecutionCoursesToImportEvaluationMethod(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        listExecutionCoursesToImportContent(request);
        return forward(request, "/teacher/executionCourse/importEvaluationMethod.jsp");
    }

    public ActionForward importEvaluationMethod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        importContent(request, "ImportEvaluationMethod");
        return forward(request, "/teacher/executionCourse/evaluationMethod.jsp");
    }

}
