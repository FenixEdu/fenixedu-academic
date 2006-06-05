package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.dataTransferObject.InfoEvaluationMethod;
import net.sourceforge.fenixedu.domain.BibliographicReference;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.domain.EvaluationMethod;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

public class ManageExecutionCourseDA extends FenixDispatchAction {

    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        getExecutionCourseFromParameterAndSetItInRequest(request);
        return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward instructions(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("instructions");
    }

    public ActionForward program(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("program");
    }

    public ActionForward prepareEditProgram(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final ExecutionCourse executionCourse = (ExecutionCourse) request
                .getAttribute("executionCourse");
        final String curriculumIDString = request.getParameter("curriculumID");
        if (executionCourse != null && curriculumIDString != null && curriculumIDString.length() > 0) {
            final Curriculum curriculum = findCurriculum(executionCourse, Integer
                    .valueOf(curriculumIDString));
            if (curriculum != null) {
                final DynaActionForm dynaActionForm = (DynaActionForm) form;
                dynaActionForm.set("program", curriculum.getProgram());
                dynaActionForm.set("programEn", curriculum.getProgramEn());
            }
            request.setAttribute("curriculum", curriculum);
        }
        return mapping.findForward("edit-program");
    }

    public ActionForward editProgram(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final DynaActionForm dynaActionForm = (DynaActionForm) form;
        final String curriculumIDString = request.getParameter("curriculumID");
        final String program = dynaActionForm.getString("program");
        final String programEn = dynaActionForm.getString("programEn");

        final ExecutionCourse executionCourse = (ExecutionCourse) request
                .getAttribute("executionCourse");
        final Curriculum curriculum = findCurriculum(executionCourse, Integer
                .valueOf(curriculumIDString));
        final InfoCurriculum infoCurriculum = new InfoCurriculum();
        infoCurriculum.setProgram(program);
        infoCurriculum.setProgramEn(programEn);
        final IUserView userView = getUserView(request);

        final Object args[] = { executionCourse.getIdInternal(),
                curriculum.getCurricularCourse().getIdInternal(), infoCurriculum,
                userView.getUtilizador() };
        try {
            ServiceManagerServiceFactory.executeService(userView, "EditProgram", args);
        } catch (NotAuthorizedFilterException e) {
            ActionMessages messages = new ActionMessages();
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.teacherNotResponsibleOrNotCoordinator"));
            saveErrors(request, messages);
        } catch (Exception e) {
            throw new FenixActionException();
        }

        return mapping.findForward("program");
    }

    public ActionForward objectives(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("objectives");
    }

    public ActionForward prepareEditObjectives(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final ExecutionCourse executionCourse = (ExecutionCourse) request
                .getAttribute("executionCourse");
        final String curriculumIDString = request.getParameter("curriculumID");
        if (executionCourse != null && curriculumIDString != null && curriculumIDString.length() > 0) {
            final Curriculum curriculum = findCurriculum(executionCourse, Integer
                    .valueOf(curriculumIDString));
            if (curriculum != null) {
                final DynaActionForm dynaActionForm = (DynaActionForm) form;
                dynaActionForm.set("generalObjectives", curriculum.getGeneralObjectives());
                dynaActionForm.set("generalObjectivesEn", curriculum.getGeneralObjectivesEn());
                dynaActionForm.set("operacionalObjectives", curriculum.getOperacionalObjectives());
                dynaActionForm.set("operacionalObjectivesEn", curriculum.getOperacionalObjectivesEn());
            }
            request.setAttribute("curriculum", curriculum);
        }
        return mapping.findForward("edit-objectives");
    }

    public ActionForward editObjectives(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final DynaActionForm dynaActionForm = (DynaActionForm) form;
        final String curriculumIDString = request.getParameter("curriculumID");
        final String generalObjectives = dynaActionForm.getString("generalObjectives");
        final String generalObjectivesEn = dynaActionForm.getString("generalObjectivesEn");
        final String operacionalObjectives = dynaActionForm.getString("operacionalObjectives");
        final String operacionalObjectivesEn = dynaActionForm.getString("operacionalObjectivesEn");

        final ExecutionCourse executionCourse = (ExecutionCourse) request
                .getAttribute("executionCourse");
        final Curriculum curriculum = findCurriculum(executionCourse, Integer
                .valueOf(curriculumIDString));
        final InfoCurriculum infoCurriculum = new InfoCurriculum();
        infoCurriculum.setGeneralObjectives(generalObjectives);
        infoCurriculum.setGeneralObjectivesEn(generalObjectivesEn);
        infoCurriculum.setOperacionalObjectives(operacionalObjectives);
        infoCurriculum.setOperacionalObjectivesEn(operacionalObjectivesEn);
        final IUserView userView = getUserView(request);

        final Object args[] = { executionCourse.getIdInternal(),
                curriculum.getCurricularCourse().getIdInternal(), infoCurriculum,
                userView.getUtilizador() };
        ServiceManagerServiceFactory.executeService(userView, "EditObjectives", args);

        return mapping.findForward("objectives");
    }

    public ActionForward evaluationMethod(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("evaluationMethod");
    }

    public ActionForward prepareEditEvaluationMethod(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final ExecutionCourse executionCourse = (ExecutionCourse) request
                .getAttribute("executionCourse");
        if (executionCourse != null) {
            final DynaActionForm dynaActionForm = (DynaActionForm) form;
            dynaActionForm.set("evaluationMethod", getEvaluationMethod(executionCourse));
            dynaActionForm.set("evaluationMethodEn", getEvaluationMethodEn(executionCourse));
        }

        return mapping.findForward("edit-evaluationMethod");
    }

    public ActionForward editEvaluationMethod(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final DynaActionForm dynaActionForm = (DynaActionForm) form;
        final String evaluationMethod = request.getParameter("evaluationMethod");
        final String evaluationMethodEn = dynaActionForm.getString("evaluationMethodEn");

        final ExecutionCourse executionCourse = (ExecutionCourse) request
                .getAttribute("executionCourse");
        final InfoEvaluationMethod infoEvaluationMethod = new InfoEvaluationMethod();
        infoEvaluationMethod.setEvaluationElements(evaluationMethod);
        infoEvaluationMethod.setEvaluationElementsEn(evaluationMethodEn);
        final IUserView userView = getUserView(request);

        final Object args[] = { executionCourse.getIdInternal(), null, infoEvaluationMethod };
        ServiceManagerServiceFactory.executeService(userView, "EditEvaluation", args);

        return mapping.findForward("evaluationMethod");
    }

    public ActionForward bibliographicReference(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("bibliographicReference");
    }

    public ActionForward prepareCreateBibliographicReference(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("create-bibliographicReference");
    }

    public ActionForward createBibliographicReference(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final DynaActionForm dynaActionForm = (DynaActionForm) form;
        final String title = dynaActionForm.getString("title");
        final String authors = dynaActionForm.getString("authors");
        final String reference = dynaActionForm.getString("reference");
        final String year = dynaActionForm.getString("year");
        final String optional = dynaActionForm.getString("optional");

        final ExecutionCourse executionCourse = (ExecutionCourse) request
                .getAttribute("executionCourse");
        final IUserView userView = getUserView(request);

        final Object args[] = { executionCourse.getIdInternal(), title, authors, reference, year,
                Boolean.valueOf(optional) };
        ServiceManagerServiceFactory.executeService(userView, "CreateBibliographicReference", args);

        return mapping.findForward("bibliographicReference");
    }

    public ActionForward prepareEditBibliographicReference(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final ExecutionCourse executionCourse = (ExecutionCourse) request
                .getAttribute("executionCourse");
        final String bibliographicReferenceIDString = request.getParameter("bibliographicReferenceID");
        if (executionCourse != null && bibliographicReferenceIDString != null
                && bibliographicReferenceIDString.length() > 0) {
            final BibliographicReference bibliographicReference = findBibliographicReference(
                    executionCourse, Integer.valueOf(bibliographicReferenceIDString));
            if (bibliographicReference != null) {
                final DynaActionForm dynaActionForm = (DynaActionForm) form;
                dynaActionForm.set("title", bibliographicReference.getTitle());
                dynaActionForm.set("authors", bibliographicReference.getAuthors());
                dynaActionForm.set("reference", bibliographicReference.getReference());
                dynaActionForm.set("year", bibliographicReference.getYear());
                dynaActionForm.set("optional", bibliographicReference.getOptional().toString());
            }
            request.setAttribute("bibliographicReference", bibliographicReference);
        }
        return mapping.findForward("edit-bibliographicReference");
    }

    public ActionForward editBibliographicReference(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final DynaActionForm dynaActionForm = (DynaActionForm) form;
        final String bibliographicReferenceIDString = request.getParameter("bibliographicReferenceID");
        final String title = dynaActionForm.getString("title");
        final String authors = dynaActionForm.getString("authors");
        final String reference = dynaActionForm.getString("reference");
        final String year = dynaActionForm.getString("year");
        final String optional = dynaActionForm.getString("optional");

        final ExecutionCourse executionCourse = (ExecutionCourse) request
                .getAttribute("executionCourse");
        final BibliographicReference bibliographicReference = findBibliographicReference(
                executionCourse, Integer.valueOf(bibliographicReferenceIDString));
        final IUserView userView = getUserView(request);

        final Object args[] = { bibliographicReference.getIdInternal(), title, authors, reference, year,
                Boolean.valueOf(optional) };
        ServiceManagerServiceFactory.executeService(userView, "EditBibliographicReference", args);

        return mapping.findForward("bibliographicReference");
    }

    public ActionForward deleteBibliographicReference(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final String bibliographicReferenceIDString = request.getParameter("bibliographicReferenceID");

        final IUserView userView = getUserView(request);
        final Object args[] = { Integer.valueOf(bibliographicReferenceIDString) };
        ServiceManagerServiceFactory.executeService(userView, "DeleteBibliographicReference", args);

        return mapping.findForward("bibliographicReference");
    }

    private Object getEvaluationMethod(final ExecutionCourse executionCourse) {
        final EvaluationMethod evaluationMethod = executionCourse.getEvaluationMethod();
        if (evaluationMethod != null && evaluationMethod.getEvaluationElements() != null) {
            return evaluationMethod.getEvaluationElements();
        } else {
            final Set<CompetenceCourse> competenceCourses = executionCourse.getCompetenceCourses();
            return competenceCourses.isEmpty() ? null : competenceCourses.iterator().next()
                    .getEvaluationMethod();
        }
    }

    private Object getEvaluationMethodEn(ExecutionCourse executionCourse) {
        final EvaluationMethod evaluationMethod = executionCourse.getEvaluationMethod();
        if (evaluationMethod != null && evaluationMethod.getEvaluationElementsEn() != null) {
            return evaluationMethod.getEvaluationElementsEn();
        } else {
            final Set<CompetenceCourse> competenceCourses = executionCourse.getCompetenceCourses();
            return competenceCourses.isEmpty() ? null : competenceCourses.iterator().next()
                    .getEvaluationMethodEn();
        }
    }

    public static void getExecutionCourseFromParameterAndSetItInRequest(final HttpServletRequest request) {
        String executionCourseIDString = request.getParameter("executionCourseID");
        if (executionCourseIDString == null || executionCourseIDString.length() == 0) {
            executionCourseIDString = request.getParameter("objectCode");
        }
        
        if (executionCourseIDString != null && executionCourseIDString.length() > 0) {
            final ExecutionCourse executionCourse = findExecutionCourse(request, Integer
                    .valueOf(executionCourseIDString));
            request.setAttribute("executionCourse", executionCourse);
        }
    }

    private static ExecutionCourse findExecutionCourse(final HttpServletRequest request,
            final Integer executionCourseID) {
        final IUserView userView = getUserView(request);
        if (userView != null) {
            final Person person = userView.getPerson();
            if (person != null) {
                final Teacher teacher = person.getTeacher();
                if (teacher != null) {
                    for (final Professorship professorship : teacher.getProfessorshipsSet()) {
                        final ExecutionCourse executionCourse = professorship.getExecutionCourse();
                        if (executionCourse.getIdInternal().equals(executionCourseID)) {
                            return executionCourse;
                        }
                    }
                }
            }
        }
        return null;
    }

    private BibliographicReference findBibliographicReference(ExecutionCourse executionCourse,
            Integer bibliographicReferenceID) {
        for (final BibliographicReference bibliographicReference : executionCourse
                .getAssociatedBibliographicReferencesSet()) {
            if (bibliographicReference.getIdInternal().equals(bibliographicReferenceID)) {
                return bibliographicReference;
            }
        }
        return null;
    }

    private Curriculum findCurriculum(final ExecutionCourse executionCourse, final Integer curriculumID) {
        for (final CurricularCourse curricularCourse : executionCourse
                .getAssociatedCurricularCoursesSet()) {
            for (final Curriculum curriculum : curricularCourse.getAssociatedCurriculumsSet()) {
                if (curriculum.getIdInternal().equals(curriculumID)) {
                    return curriculum;
                }
            }
        }
        return null;
    }

}