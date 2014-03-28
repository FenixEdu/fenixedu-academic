package net.sourceforge.fenixedu.presentationTier.Action.teacher.executionCourse;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.EditEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.ImportContentBean;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.EvaluationMethod;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.ManageExecutionCourseDA;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.struts.annotations.Input;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

@Mapping(path = "/manageEvaluationMethod", module = "teacher", functionality = ManageExecutionCourseDA.class,
        formBean = "evaluationMethodForm")
public class EvaluationMethodDA extends ManageExecutionCourseDA {

    // EVALUATION METHOD

    @Input
    public ActionForward evaluationMethod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return forward(request, "/teacher/executionCourse/evaluationMethod.jsp");
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
                        evaluationMethodMls.with(Language.pt, pt == null ? "" : pt).with(Language.en, en == null ? "" : en);
            }
            EditEvaluation.runEditEvaluation(executionCourse, evaluationMethodMls);
            evaluationMethod = executionCourse.getEvaluationMethod();
        }
        return forward(request, "/teacher/executionCourse/editEvaluationMethod.jsp");
    }

    public ActionForward editEvaluationMethod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final DynaActionForm dynaActionForm = (DynaActionForm) form;
        final String evaluationMethod = request.getParameter("evaluationMethod");
        final String evaluationMethodEn = dynaActionForm.getString("evaluationMethodEn");
        MultiLanguageString multiLanguageString = new MultiLanguageString();
        multiLanguageString = multiLanguageString.with(Language.pt, evaluationMethod);
        multiLanguageString = multiLanguageString.with(Language.en, evaluationMethodEn);

        final ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");

        EditEvaluation.runEditEvaluation(executionCourse, multiLanguageString);

        return forward(request, "/teacher/executionCourse/evaluationMethod.jsp");
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
