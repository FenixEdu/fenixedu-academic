package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.enrollment.shift.EnrollStudentInShifts;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.CreateBibliographicReference;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.CreateLessonPlanning;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.DeleteBibliographicReference;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.DeleteLessonPlanning;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.EditBibliographicReference;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.EditEvaluation;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.ImportBibliographicReferences;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.ImportEvaluationMethod;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.ImportLessonPlannings;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.ImportSections;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.MoveLessonPlanning;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.OrderBibliographicReferences;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.CreateLessonPlanningBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.dataTransferObject.teacher.ImportLessonPlanningsBean;
import net.sourceforge.fenixedu.dataTransferObject.teacher.ImportLessonPlanningsBean.ImportType;
import net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.ImportContentBean;
import net.sourceforge.fenixedu.domain.BibliographicReference;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.domain.EvaluationMethod;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.LessonPlanning;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ManageExecutionCourseDA extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        propageContextIds(request);
        return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward instructions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("instructions");
    }

    private void prepareImportContentPostBack(HttpServletRequest request) {
        IViewState viewState = RenderUtils.getViewState("importContentBean");
        final ImportContentBean bean = (ImportContentBean) viewState.getMetaObject().getObject();
        RenderUtils.invalidateViewState();
        request.setAttribute("importContentBean", bean);
    }

    private void prepareImportContentInvalid(HttpServletRequest request) {
        IViewState viewState = RenderUtils.getViewState("importContentBeanWithExecutionCourse");
        viewState = (viewState == null) ? RenderUtils.getViewState("importContentBean") : viewState;
        final ImportContentBean bean = (ImportContentBean) viewState.getMetaObject().getObject();
        request.setAttribute("importContentBean", bean);
    }

    private void listExecutionCoursesToImportContent(HttpServletRequest request) {
        final IViewState viewState = RenderUtils.getViewState("importContentBean");
        final ImportContentBean bean = (ImportContentBean) viewState.getMetaObject().getObject();
        request.setAttribute("importContentBean", bean);
    }

    private void importContent(HttpServletRequest request, String importContentService) throws FenixServiceException {
        final ExecutionCourse executionCourseTo = (ExecutionCourse) request.getAttribute("executionCourse");
        final IViewState viewState = RenderUtils.getViewState("importContentBeanWithExecutionCourse");
        final ImportContentBean bean = (ImportContentBean) viewState.getMetaObject().getObject();
        request.setAttribute("importContentBean", bean);

        final ExecutionCourse executionCourseFrom = bean.getExecutionCourse();
        final Object args[] = { executionCourseTo.getExternalId(), executionCourseTo, executionCourseFrom, null };
        try {
            if (importContentService.equals("ImportBibliographicReferences")) {
                ImportBibliographicReferences.runImportBibliographicReferences(executionCourseTo.getExternalId(),
                        executionCourseTo, executionCourseFrom, null);
            } else if (importContentService.equals("ImportEvaluationMethod")) {
                ImportEvaluationMethod.runImportEvaluationMethod(executionCourseTo.getExternalId(), executionCourseTo,
                        executionCourseFrom, null);
            } else if (importContentService.equals("ImportSections")) {
                ImportSections.runImportSections(executionCourseTo.getExternalId(), executionCourseTo, executionCourseFrom, null);
            } else {
                throw new UnsupportedOperationException("Sorry, cannot import using " + importContentService);
            }
        } catch (DomainException e) {
            addActionMessage(request, e.getKey(), e.getArgs());
        }
    }

    // PROGRAM

    public ActionForward program(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return mapping.findForward("program");
    }

    public void prepareCurricularCourse(HttpServletRequest request) {
        final ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");
        final String curricularCourseIDString = request.getParameter("curricularCourseID");
        if (executionCourse != null && curricularCourseIDString != null && curricularCourseIDString.length() > 0) {
            final CurricularCourse curricularCourse =
                    findCurricularCourse(executionCourse, Integer.valueOf(curricularCourseIDString));
            request.setAttribute("curricularCourse", curricularCourse);
        }
    }

    public ActionForward prepareCreateProgram(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        prepareCurricularCourse(request);
        return mapping.findForward("create-program");
    }

    public ActionForward createProgram(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException,  FenixServiceException {
        executeFactoryMethod();
        return mapping.findForward("program");
    }

    public ActionForward prepareEditProgram(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");

        final Teacher teacher = getUserView(request).getPerson().getTeacher();
        if (teacher.isResponsibleFor(executionCourse) == null) {
            ActionMessages messages = new ActionMessages();
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.teacherNotResponsibleOrNotCoordinator"));
            saveErrors(request, messages);
            return mapping.findForward("program");
        }

        final String curriculumIDString = request.getParameter("curriculumID");
        if (executionCourse != null && curriculumIDString != null && curriculumIDString.length() > 0) {
            final Curriculum curriculum = findCurriculum(executionCourse, Integer.valueOf(curriculumIDString));
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
            HttpServletResponse response) throws FenixActionException,  FenixServiceException {
        executeFactoryMethod();
        return mapping.findForward("program");
    }

    // OBJECTIVES

    public ActionForward objectives(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("objectives");
    }

    public ActionForward prepareCreateObjectives(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        prepareCurricularCourse(request);
        return mapping.findForward("create-objectives");
    }

    public ActionForward createObjectives(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException, FenixActionException {
        executeFactoryMethod();
        return mapping.findForward("objectives");
    }

    public ActionForward prepareEditObjectives(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");

        final Teacher teacher = getUserView(request).getPerson().getTeacher();
        if (teacher.isResponsibleFor(executionCourse) == null) {
            ActionMessages messages = new ActionMessages();
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.teacherNotResponsibleOrNotCoordinator"));
            saveErrors(request, messages);
            return mapping.findForward("objectives");
        }

        final String curriculumIDString = request.getParameter("curriculumID");
        if (executionCourse != null && curriculumIDString != null && curriculumIDString.length() > 0) {
            final Curriculum curriculum = findCurriculum(executionCourse, Integer.valueOf(curriculumIDString));
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

    public ActionForward editObjectives(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        executeFactoryMethod();
        return mapping.findForward("objectives");
    }

    // EVALUATION METHOD

    public ActionForward evaluationMethod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("evaluationMethod");
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
                evaluationMethodMls = evaluationMethodMls.with(Language.pt, pt == null ? "" : pt).with(Language.en, en == null ? "" : en);
            }
            EditEvaluation.runEditEvaluation(executionCourse, evaluationMethodMls);
            evaluationMethod = executionCourse.getEvaluationMethod();
        }
        return mapping.findForward("edit-evaluationMethod");
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

        return mapping.findForward("evaluationMethod");
    }

    public ActionForward prepareImportEvaluationMethod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("importContentBean", new ImportContentBean());
        return mapping.findForward("importEvaluationMethod");
    }

    public ActionForward prepareImportEvaluationMethodPostBack(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        prepareImportContentPostBack(request);
        return mapping.findForward("importEvaluationMethod");
    }

    public ActionForward prepareImportEvaluationMethodInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        prepareImportContentInvalid(request);
        return mapping.findForward("importEvaluationMethod");
    }

    public ActionForward listExecutionCoursesToImportEvaluationMethod(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        listExecutionCoursesToImportContent(request);
        return mapping.findForward("importEvaluationMethod");
    }

    public ActionForward importEvaluationMethod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {

        importContent(request, "ImportEvaluationMethod");
        return mapping.findForward("evaluationMethod");
    }

    // BIBLIOGRAPHIC REFERENCES

    public ActionForward bibliographicReference(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("bibliographicReference");
    }

    public ActionForward prepareCreateBibliographicReference(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("create-bibliographicReference");
    }

    public ActionForward createBibliographicReference(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final DynaActionForm dynaActionForm = (DynaActionForm) form;
        final String title = dynaActionForm.getString("title");
        final String authors = dynaActionForm.getString("authors");
        final String reference = dynaActionForm.getString("reference");
        final String year = dynaActionForm.getString("year");
        final String optional = dynaActionForm.getString("optional");

        final ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");
        final IUserView userView = getUserView(request);

        CreateBibliographicReference.runCreateBibliographicReference(executionCourse.getExternalId(), title, authors, reference,
                year, Boolean.valueOf(optional));

        return mapping.findForward("bibliographicReference");
    }

    public ActionForward prepareEditBibliographicReference(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");
        final String bibliographicReferenceIDString = request.getParameter("bibliographicReferenceID");
        if (executionCourse != null && bibliographicReferenceIDString != null && bibliographicReferenceIDString.length() > 0) {
            final BibliographicReference bibliographicReference =
                    findBibliographicReference(executionCourse, Integer.valueOf(bibliographicReferenceIDString));
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

    public ActionForward editBibliographicReference(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final DynaActionForm dynaActionForm = (DynaActionForm) form;
        final String bibliographicReferenceIDString = request.getParameter("bibliographicReferenceID");
        final String title = dynaActionForm.getString("title");
        final String authors = dynaActionForm.getString("authors");
        final String reference = dynaActionForm.getString("reference");
        final String year = dynaActionForm.getString("year");
        final String optional = dynaActionForm.getString("optional");

        final ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");
        final BibliographicReference bibliographicReference =
                findBibliographicReference(executionCourse, Integer.valueOf(bibliographicReferenceIDString));
        final IUserView userView = getUserView(request);

        EditBibliographicReference.runEditBibliographicReference(bibliographicReference.getExternalId(), title, authors,
                reference, year, Boolean.valueOf(optional));

        return mapping.findForward("bibliographicReference");
    }

    public ActionForward deleteBibliographicReference(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final String bibliographicReferenceIDString = request.getParameter("bibliographicReferenceID");
        final IUserView userView = getUserView(request);
        DeleteBibliographicReference.runDeleteBibliographicReference(Integer.valueOf(bibliographicReferenceIDString));

        return mapping.findForward("bibliographicReference");
    }

    public ActionForward prepareImportBibliographicReferences(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("importContentBean", new ImportContentBean());
        return mapping.findForward("importBibliographicReferences");
    }

    public ActionForward prepareImportBibliographicReferencesPostBack(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        prepareImportContentPostBack(request);
        return mapping.findForward("importBibliographicReferences");
    }

    public ActionForward prepareImportBibliographicReferencesInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        prepareImportContentInvalid(request);
        return mapping.findForward("importBibliographicReferences");
    }

    public ActionForward listExecutionCoursesToImportBibliographicReferences(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        listExecutionCoursesToImportContent(request);
        return mapping.findForward("importBibliographicReferences");
    }

    public ActionForward importBibliographicReferences(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {

        importContent(request, "ImportBibliographicReferences");
        return mapping.findForward("bibliographicReference");
    }

    // LESSON PLANNINGS
    public ActionForward submitDataToImportLessonPlannings(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final IViewState viewState = RenderUtils.getViewState();
        final ImportLessonPlanningsBean bean = (ImportLessonPlanningsBean) viewState.getMetaObject().getObject();
        request.setAttribute("importLessonPlanningBean", bean);
        return mapping.findForward("importLessonPlannings");
    }

    public ActionForward submitDataToImportLessonPlanningsPostBack(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final IViewState viewState = RenderUtils.getViewState();
        final ImportLessonPlanningsBean bean = (ImportLessonPlanningsBean) viewState.getMetaObject().getObject();
        if (bean.getCurricularYear() == null || bean.getExecutionPeriod() == null || bean.getExecutionDegree() == null) {
            bean.setExecutionCourse(null);
            bean.setImportType(null);
            bean.setShift(null);
        }
        RenderUtils.invalidateViewState();
        request.setAttribute("importLessonPlanningBean", bean);
        return mapping.findForward("importLessonPlannings");
    }

    public ActionForward prepareImportLessonPlannings(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final ExecutionCourse executionCourseTo = (ExecutionCourse) request.getAttribute("executionCourse");
        request.setAttribute("importLessonPlanningBean", new ImportLessonPlanningsBean(executionCourseTo));
        return mapping.findForward("importLessonPlannings");
    }

    public ActionForward importLessonPlannings(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {

        final IViewState viewState = RenderUtils.getViewState();
        ImportLessonPlanningsBean bean = (ImportLessonPlanningsBean) viewState.getMetaObject().getObject();
        request.setAttribute("importLessonPlanningBean", bean);

        ExecutionCourse executionCourseFrom = bean.getExecutionCourse();
        ExecutionCourse executionCourseTo = bean.getExecutionCourseTo();
        ImportType importType = bean.getImportType();

        if (importType != null && importType.equals(ImportLessonPlanningsBean.ImportType.PLANNING)) {
            try {
                ImportLessonPlannings.runImportLessonPlannings(executionCourseTo.getExternalId(), executionCourseTo,
                        executionCourseFrom, null);
            } catch (DomainException e) {
                addActionMessage(request, e.getKey(), e.getArgs());
            }

        } else if (importType != null && importType.equals(ImportLessonPlanningsBean.ImportType.SUMMARIES)) {
            return mapping.findForward("importLessonPlannings");
        }

        return lessonPlannings(mapping, form, request, response);
    }

    public ActionForward importLessonPlanningsBySummaries(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {

        final IViewState viewState = RenderUtils.getViewState();
        ImportLessonPlanningsBean bean = (ImportLessonPlanningsBean) viewState.getMetaObject().getObject();

        ExecutionCourse executionCourseTo = bean.getExecutionCourseTo();
        Shift shiftFrom = bean.getShift();

        try {
            ImportLessonPlannings.runImportLessonPlannings(executionCourseTo.getExternalId(), executionCourseTo,
                    shiftFrom.getExecutionCourse(), shiftFrom);
        } catch (DomainException e) {
            addActionMessage(request, e.getKey(), e.getArgs());
        }

        request.setAttribute("importLessonPlanningBean", bean);
        return lessonPlannings(mapping, form, request, response);
    }

    public ActionForward lessonPlannings(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");
        Map<ShiftType, List<LessonPlanning>> lessonPlanningsMap = new TreeMap<ShiftType, List<LessonPlanning>>();
        for (ShiftType shiftType : executionCourse.getShiftTypes()) {
            List<LessonPlanning> lessonPlanningsOrderedByOrder = executionCourse.getLessonPlanningsOrderedByOrder(shiftType);
            if (!lessonPlanningsOrderedByOrder.isEmpty()) {
                lessonPlanningsMap.put(shiftType, lessonPlanningsOrderedByOrder);
            }
        }
        request.setAttribute("lessonPlanningsMap", lessonPlanningsMap);
        return mapping.findForward("lessonPlannings");
    }

    public ActionForward moveUpLessonPlanning(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {

        Integer lessonPlanningID = Integer.valueOf(request.getParameter("lessonPlanningID"));
        LessonPlanning lessonPlanning = rootDomainObject.readLessonPlanningByOID(lessonPlanningID);
        try {
            MoveLessonPlanning.runMoveLessonPlanning(lessonPlanning.getExecutionCourse().getExternalId(), lessonPlanning,
                    (lessonPlanning.getOrderOfPlanning() - 1));
        } catch (DomainException e) {
            addActionMessage(request, e.getKey(), e.getArgs());
        }

        return lessonPlannings(mapping, form, request, response);
    }

    public ActionForward moveDownLessonPlanning(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {

        Integer lessonPlanningID = Integer.valueOf(request.getParameter("lessonPlanningID"));
        LessonPlanning lessonPlanning = rootDomainObject.readLessonPlanningByOID(lessonPlanningID);
        try {
            MoveLessonPlanning.runMoveLessonPlanning(lessonPlanning.getExecutionCourse().getExternalId(), lessonPlanning,
                    (lessonPlanning.getOrderOfPlanning() + 1));
        } catch (DomainException e) {
            addActionMessage(request, e.getKey(), e.getArgs());
        }

        return lessonPlannings(mapping, form, request, response);
    }

    public ActionForward prepareCreateLessonPlanning(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");
        request.setAttribute("lessonPlanningBean", new CreateLessonPlanningBean(executionCourse));
        return mapping.findForward("create-lessonPlanning");
    }

    public ActionForward prepareEditLessonPlanning(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        Integer lessonPlanningID = Integer.valueOf(request.getParameter("lessonPlanningID"));
        LessonPlanning lessonPlanning = rootDomainObject.readLessonPlanningByOID(lessonPlanningID);
        request.setAttribute("lessonPlanning", lessonPlanning);
        return mapping.findForward("create-lessonPlanning");
    }

    public ActionForward createLessonPlanning(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {

        IViewState viewState = RenderUtils.getViewState();
        final CreateLessonPlanningBean lessonPlanningBean = (CreateLessonPlanningBean) viewState.getMetaObject().getObject();

        try {
            CreateLessonPlanning.runCreateLessonPlanning(lessonPlanningBean.getExecutionCourse().getExternalId(),
                    lessonPlanningBean.getTitle(), lessonPlanningBean.getPlanning(), lessonPlanningBean.getLessonType(),
                    lessonPlanningBean.getExecutionCourse());
        } catch (DomainException e) {
            addActionMessage(request, e.getKey(), e.getArgs());
            request.setAttribute("lessonPlanningBean", lessonPlanningBean);
            return mapping.findForward("create-lessonPlanning");
        }
        return lessonPlannings(mapping, form, request, response);
    }

    public ActionForward deleteLessonPlanning(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {

        Integer lessonPlanningID = Integer.valueOf(request.getParameter("lessonPlanningID"));
        LessonPlanning lessonPlanning = rootDomainObject.readLessonPlanningByOID(lessonPlanningID);
        if (lessonPlanning != null) {
            try {
                DeleteLessonPlanning.runDeleteLessonPlanning(lessonPlanning.getExecutionCourse().getExternalId(), lessonPlanning,
                        null, null);
            } catch (DomainException e) {
                addActionMessage(request, e.getKey(), e.getArgs());
            }
        }
        return lessonPlannings(mapping, form, request, response);
    }

    public ActionForward deleteLessonPlannings(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {

        ShiftType lessonType = ShiftType.valueOf(request.getParameter("shiftType"));
        final ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");

        if (lessonType != null && executionCourse != null) {
            try {
                DeleteLessonPlanning.runDeleteLessonPlanning(executionCourse.getExternalId(), null, executionCourse, lessonType);
            } catch (DomainException e) {
                addActionMessage(request, e.getKey(), e.getArgs());
            }
        }
        return lessonPlannings(mapping, form, request, response);
    }

    public static void propageContextIds(final HttpServletRequest request) {
        String executionCourseIDString = request.getParameter("executionCourseID");

        if (executionCourseIDString == null || executionCourseIDString.length() == 0) {
            executionCourseIDString = request.getParameter("objectCode");
        }

        if (executionCourseIDString != null && executionCourseIDString.length() > 0) {
            final ExecutionCourse executionCourse = findExecutionCourse(request, Integer.valueOf(executionCourseIDString));
            request.setAttribute("executionCourse", executionCourse);
        }
    }

    private static ExecutionCourse findExecutionCourse(final HttpServletRequest request, final Integer executionCourseID) {
        final IUserView userView = getUserView(request);

        if (userView != null) {
            final Person person = userView.getPerson();
            if (person != null) {
                for (final Professorship professorship : person.getProfessorshipsSet()) {
                    final ExecutionCourse executionCourse = professorship.getExecutionCourse();
                    if (executionCourse.getExternalId().equals(executionCourseID)) {
                        return executionCourse;
                    }
                }
            }
        }

        return null;
    }

    private BibliographicReference findBibliographicReference(ExecutionCourse executionCourse, Integer bibliographicReferenceID) {
        for (final BibliographicReference bibliographicReference : executionCourse.getAssociatedBibliographicReferencesSet()) {
            if (bibliographicReference.getExternalId().equals(bibliographicReferenceID)) {
                return bibliographicReference;
            }
        }
        return null;
    }

    private Curriculum findCurriculum(final ExecutionCourse executionCourse, final Integer curriculumID) {
        for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
            for (final Curriculum curriculum : curricularCourse.getAssociatedCurriculumsSet()) {
                if (curriculum.getExternalId().equals(curriculumID)) {
                    return curriculum;
                }
            }
        }
        return null;
    }

    private CurricularCourse findCurricularCourse(final ExecutionCourse executionCourse, final Integer curricularCourseID) {
        for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
            if (curricularCourse.getExternalId().equals(curricularCourseID)) {
                return curricularCourse;
            }
        }
        return null;
    }

    public ActionForward prepareImportSectionsPostBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        prepareImportContentPostBack(request);
        return mapping.findForward("importSections");
    }

    public ActionForward prepareImportSectionsInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        prepareImportContentInvalid(request);
        return mapping.findForward("importSections");
    }

    public ActionForward listExecutionCoursesToImportSections(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        listExecutionCoursesToImportContent(request);
        return mapping.findForward("importSections");
    }

    public ActionForward importSections(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {
        importContent(request, "ImportSections");
        return mapping.findForward("sectionsManagement");
    }

    public ActionForward prepareImportSections(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("importContentBean", new ImportContentBean());
        return mapping.findForward("importSections");
    }

    public ActionForward prepareSortRecommendedBibliography(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return null;
    }

    public ExecutionCourse getExecutionCourse(HttpServletRequest request) {
        return (ExecutionCourse) request.getAttribute("executionCourse");
    }

    public ActionForward prepareSortBibliography(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ExecutionCourse executionCourse = getExecutionCourse(request);
        boolean optional = request.getParameter("optional") != null;
        List<BibliographicReference> references;

        if (optional) {
            references = getOptionalBibliographicReferences(executionCourse);
        } else {
            references = getMainBibliographicReferences(executionCourse);
        }

        request.setAttribute("references", references);
        request.setAttribute("optional", optional);

        return mapping.findForward("orderBibliographicReferences");
    }

    private List<BibliographicReference> getMainBibliographicReferences(ExecutionCourse executionCourse) {
        List<BibliographicReference> references = new ArrayList<BibliographicReference>();

        for (BibliographicReference reference : executionCourse.getOrderedBibliographicReferences()) {
            if (!reference.isOptional()) {
                references.add(reference);
            }
        }

        return references;
    }

    private List<BibliographicReference> getOptionalBibliographicReferences(ExecutionCourse executionCourse) {
        List<BibliographicReference> references = new ArrayList<BibliographicReference>();

        for (BibliographicReference reference : executionCourse.getOrderedBibliographicReferences()) {
            if (reference.isOptional()) {
                references.add(reference);
            }
        }

        return references;
    }

    public ActionForward sortBibliographyReferences(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {
        ExecutionCourse executionCourse = getExecutionCourse(request);
        boolean optional = request.getParameter("optional") != null;

        String orderString = request.getParameter("referencesOrder");

        List<BibliographicReference> initialReferences;
        if (optional) {
            initialReferences = getOptionalBibliographicReferences(executionCourse);
        } else {
            initialReferences = getMainBibliographicReferences(executionCourse);
        }

        List<BibliographicReference> orderedReferences = new ArrayList<BibliographicReference>();

        String[] nodes = orderString.split(",");
        for (String node : nodes) {
            String[] parts = node.split("-");

            Integer itemIndex = getId(parts[0]);
            orderedReferences.add(initialReferences.get(itemIndex - 1));
        }

        List<BibliographicReference> finalOrderedReferences;
        if (optional) {
            finalOrderedReferences = getMainBibliographicReferences(executionCourse);
            finalOrderedReferences.addAll(orderedReferences);
        } else {
            finalOrderedReferences = orderedReferences;
            finalOrderedReferences.addAll(getOptionalBibliographicReferences(executionCourse));
        }

        OrderBibliographicReferences.run(executionCourse, finalOrderedReferences);
        return mapping.findForward("bibliographicReference");
    }

    private Integer getId(String id) {
        if (id == null) {
            return null;
        }

        try {
            return new Integer(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ActionForward manageShifts(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        String executionCourseID = request.getParameter("executionCourseID");

        ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(Integer.valueOf(executionCourseID));
        SortedSet<Shift> shifts = executionCourse.getShiftsOrderedByLessons();

        request.setAttribute("shifts", shifts);
        request.setAttribute("executionCourseID", executionCourseID);

        return mapping.findForward("manageShifts");
    }

    public ActionForward editShift(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        String shiftID = request.getParameter("shiftID");
        String executionCourseID = request.getParameter("executionCourseID");
        String registrationID = request.getParameter("registrationID");

        if (request.getParameter("showPhotos") == null) {
            request.setAttribute("showPhotos", "false");
        }

        Shift shift = rootDomainObject.readShiftByOID(Integer.valueOf(shiftID));
        ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(Integer.valueOf(executionCourseID));

        if (registrationID != null) {
            Registration registration = rootDomainObject.readRegistrationByOID(Integer.valueOf(registrationID));
            shift.removeAttendFromShift(registration, executionCourse);
            request.setAttribute("registration", registration);
        }

        List<Registration> registrations = new ArrayList<Registration>();
        registrations.addAll(shift.getStudents());
        Collections.sort(registrations, Registration.NUMBER_COMPARATOR);

        request.setAttribute("registrations", registrations);
        request.setAttribute("shift", shift);
        request.setAttribute("executionCourseID", executionCourseID);

        request.setAttribute("personBean", new PersonBean());

        return mapping.findForward("editShift");
    }

    @Service
    public ActionForward insertStudentInShift(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        PersonBean bean = getRenderedObject("personBean");
        String id = bean.getUsername();
        Student student = Student.readStudentByNumber(Integer.valueOf(id));
        ExecutionCourse executionCourse =
                rootDomainObject.readExecutionCourseByOID(Integer.parseInt(request.getParameter("executionCourseID")));

        if (student != null) {
            try {
                new EnrollStudentInShifts().run(executionCourse.getRegistration(student.getPerson()),
                        Integer.parseInt(request.getParameter("shiftID")));
            } catch (FenixServiceException e) {
                final ActionErrors actionErrors = new ActionErrors();
                actionErrors.add("error", new ActionMessage("label.invalid.student.number"));
                saveErrors(request, actionErrors);
            }
        } else {
            final ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("error", new ActionMessage("label.invalid.student.number"));
            saveErrors(request, actionErrors);
        }
        return editShift(mapping, form, request, response);
    }

    public ActionForward removeAttendsFromShift(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        String shiftID = request.getParameter("shiftID");
        String registrationID = request.getParameter("registrationID");
        String executionCourseID = request.getParameter("executionCourseID");
        String removeAll = request.getParameter("removeAll");

        Shift shift = rootDomainObject.readShiftByOID(Integer.valueOf(shiftID));

        if (removeAll != null) {
            request.setAttribute("removeAll", removeAll);
        } else {
            Registration registration = rootDomainObject.readRegistrationByOID(Integer.valueOf(registrationID));
            request.setAttribute("registration", registration);
        }

        request.setAttribute("shift", shift);
        request.setAttribute("executionCourseID", executionCourseID);

        return mapping.findForward("removeAttendsFromShift");
    }

    public ActionForward removeAllAttendsFromShift(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        String executionCourseID = request.getParameter("executionCourseID");
        String shiftID = request.getParameter("shiftID");

        Shift shift = rootDomainObject.readShiftByOID(Integer.valueOf(shiftID));
        ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(Integer.valueOf(executionCourseID));

        for (Registration registration : shift.getStudents()) {
            shift.removeAttendFromShift(registration, executionCourse);
        }

        request.setAttribute("shift", shift);
        request.setAttribute("executionCourseID", executionCourseID);
        request.setAttribute("registrations", shift.getStudents());
        request.setAttribute("personBean", new PersonBean());

        return mapping.findForward("editShift");
    }
}