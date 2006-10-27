package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.CreateLessonPlanningBean;
import net.sourceforge.fenixedu.dataTransferObject.teacher.ImportLessonPlanningsBean;
import net.sourceforge.fenixedu.dataTransferObject.teacher.ImportLessonPlanningsBean.ImportType;
import net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.ImportContentBean;
import net.sourceforge.fenixedu.domain.BibliographicReference;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.domain.EvaluationMethod;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.FileItem;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.LessonPlanning;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import pt.ist.utl.fenix.utils.Pair;
import pt.utl.ist.fenix.tools.file.FileManagerException;

public class ManageExecutionCourseDA extends FenixDispatchAction {

    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        propageContextIds(request);
        return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward instructions(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("instructions");
    }

    public ActionForward createItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Section section = getSection(request);
        if (section == null) {
            return sections(mapping, form, request, response);
        }
        
        request.setAttribute("creator", new ItemCreator(section));
        
        selectSection(request);
        return mapping.findForward("createItem");
    }

    public ActionForward editItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        selectItem(request);
        return mapping.findForward("editItem");
    }

    public ActionForward deleteItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Item item = getItem(request);
        final Section section = item.getSection();
        
        try {
            final Object[] args = { request.getAttribute("executionCourse"), item };
            executeService(request, "DeleteItem", args);
        }
        catch (DomainException e) {
            addErrorMessage(request, "items", e.getKey(), (Object[]) e.getArgs());
        }

        selectSection(request, section);
        return mapping.findForward("section");
    }

    public ActionForward uploadFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Item item = selectItem(request);
        
        FileItemCreationBean bean = new FileItemCreationBean(item);
        request.setAttribute("fileItemCreator", bean);
        
        return mapping.findForward("uploadFile");
    }

    private Item selectItem(HttpServletRequest request) {
        final Item item = getItem(request);
        
        if (item != null) {
            selectSection(request, item.getSection());
        }
        
        request.setAttribute("item", item);
        return item;
    }


    // SECTIONS

    public ActionForward sections(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("sectionsManagement");
    }
    
    public ActionForward createSection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        selectSection(request);

        Section section = getSection(request);
        if (section == null) {
            Site site = getSite(request);
            
            request.setAttribute("creator", new SectionCreator(site));
        }
        else {
            request.setAttribute("creator", new SectionCreator(section));
        }
        
        return mapping.findForward("createSection");
    }

    public ActionForward editSection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        selectSection(request);
        return mapping.findForward("editSection");
    }

    public ActionForward deleteSection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Section section = selectSection(request);

        if (section.isDeletable()) {
            return mapping.findForward("confirmSectionDelete");
        }
        else {
            addErrorMessage(request, "section", "site.section.delete.notAllowed");
            return section == null ? mapping.findForward("sectionsManagement") : mapping.findForward("section");
        }
    }

    public ActionForward confirmSectionDelete(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Section section = selectSection(request);

        if (section == null) {
            return sections(mapping, form, request, response);
        }
        
        if (request.getParameter("confirm") != null) {
            try {
                Section superiorSection = section.getSuperiorSection();
                executeService(request, "DeleteSection", new Object[] {request.getAttribute("executionCourse"), section});
                
                section = superiorSection;
            } catch (DomainException e) {
                addErrorMessage(request, "section", e.getKey(), (Object[]) e.getArgs());
            }
        }
        
        selectSection(request, section);
        return section == null ? mapping.findForward("sectionsManagement") : mapping.findForward("section");
    }
        
    public ActionForward section(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        selectSection(request);
        return mapping.findForward("section");
    }

    private Section selectSection(final HttpServletRequest request) {
        final Section section = getSection(request);
        selectSection(request, section);
        return section;
    }

    private void selectSection(final HttpServletRequest request, final Section section) {
        request.setAttribute("section", section);
    }

    protected Item getItem(final HttpServletRequest request) {
        String parameter = request.getParameter("itemID");
        if (parameter == null) {
            return null;
        }
        final Integer itemID = Integer.valueOf(parameter);
        return rootDomainObject.readItemByOID(itemID);
    }

    protected Section getSection(final HttpServletRequest request) {
        final String parameter = request.getParameter("sectionID");
        if (parameter == null) {
            return null;
        }
        final Integer sectionID = Integer.valueOf(parameter);
        return rootDomainObject.readSectionByOID(sectionID);
    }
    
    public ActionForward prepareImportSections(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("importContentBean", new ImportContentBean());
        return mapping.findForward("importSections");
    }
    
    public ActionForward prepareImportSectionsPostBack(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {    
        prepareImportContentPostBack(request);
        return mapping.findForward("importSections");
    }

    public ActionForward prepareImportSectionsInvalid(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        prepareImportContentInvalid(request);        
        return mapping.findForward("importSections");
    }

    public ActionForward listExecutionCoursesToImportSections(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        listExecutionCoursesToImportContent(request);
        return mapping.findForward("importSections");
    }

    public ActionForward importSections(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
        importContent(request, "ImportSections");
        return mapping.findForward("sectionsManagement");
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

    private void importContent(HttpServletRequest request, String importContentService) throws FenixServiceException, FenixFilterException {
        final ExecutionCourse executionCourseTo = (ExecutionCourse) request.getAttribute("executionCourse");
        final IViewState viewState = RenderUtils.getViewState("importContentBeanWithExecutionCourse");
        final ImportContentBean bean = (ImportContentBean) viewState.getMetaObject().getObject();                
        request.setAttribute("importContentBean", bean);
        
        final ExecutionCourse executionCourseFrom = bean.getExecutionCourse();
        final Object args[] = { executionCourseTo.getIdInternal(), executionCourseTo, executionCourseFrom, null};
        try {
            ServiceManagerServiceFactory.executeService(getUserView(request), importContentService, args);
        } catch (DomainException e) {
            addActionMessage(request, e.getKey(), e.getArgs());
        }
    }
    
       
    // PROGRAM
    
    public ActionForward program(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("program");
    }

    public void prepareCurricularCourse(HttpServletRequest request) {
    	final ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");
    	final String curricularCourseIDString = request.getParameter("curricularCourseID");
    	if (executionCourse != null && curricularCourseIDString != null && curricularCourseIDString.length() > 0) {
    		final CurricularCourse curricularCourse = findCurricularCourse(executionCourse, Integer.valueOf(curricularCourseIDString));
    		request.setAttribute("curricularCourse", curricularCourse);
    	}
    }

    public ActionForward prepareCreateProgram(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    		throws Exception {
    	prepareCurricularCourse(request);
        return mapping.findForward("create-program");
    }

    public ActionForward createProgram(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    		throws FenixActionException, FenixFilterException, FenixServiceException {
    	executeFactoryMethod(request);
        return mapping.findForward("program");
    }

    public ActionForward prepareEditProgram(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    		throws Exception {
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

    public ActionForward editProgram(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    		throws FenixActionException, FenixFilterException, FenixServiceException {
    	executeFactoryMethod(request);
        return mapping.findForward("program");
    }
    
    // OBJECTIVES

    public ActionForward objectives(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("objectives");
    }

    public ActionForward prepareCreateObjectives(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
    	prepareCurricularCourse(request);
    	return mapping.findForward("create-objectives");
    }

    public ActionForward createObjectives(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws FenixFilterException, FenixServiceException, FenixActionException {
        executeFactoryMethod(request);
        return mapping.findForward("objectives");
    }

    public ActionForward prepareEditObjectives(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    		throws Exception {
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

    public ActionForward editObjectives(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    		throws Exception {
    	executeFactoryMethod(request);
        return mapping.findForward("objectives");
    }

    // EVALUATION METHOD
    
    public ActionForward evaluationMethod(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    		throws Exception {
        return mapping.findForward("evaluationMethod");
    }

    public ActionForward prepareEditEvaluationMethod(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    		throws Exception {
        final ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");
        EvaluationMethod evaluationMethod = executionCourse.getEvaluationMethod();
        if (evaluationMethod == null) {
        	final MultiLanguageString evaluationMethodMls = new MultiLanguageString();
        	final Set<CompetenceCourse> competenceCourses = executionCourse.getCompetenceCourses();
        	if (!competenceCourses.isEmpty()) {
        		final CompetenceCourse competenceCourse = competenceCourses.iterator().next();
        		final String pt = competenceCourse.getEvaluationMethod();
        		final String en = competenceCourse.getEvaluationMethodEn();
        		evaluationMethodMls.setContent(Language.pt, pt == null ? "" : pt);
        		evaluationMethodMls.setContent(Language.en, en == null ? "" : en);
        	}
            final Object args[] = { executionCourse, new MultiLanguageString() };
            executeService(request, "EditEvaluation", args);
            evaluationMethod = executionCourse.getEvaluationMethod();
        }
        final MultiLanguageString multiLanguageString = evaluationMethod.getEvaluationElements();
        if (multiLanguageString != null) {
        	final DynaActionForm dynaActionForm = (DynaActionForm) form;
        	dynaActionForm.set("evaluationMethod", multiLanguageString.getContent(Language.pt));
        	dynaActionForm.set("evaluationMethodEn", multiLanguageString.getContent(Language.en));
        }
        return mapping.findForward("edit-evaluationMethod");
    }

    public ActionForward editEvaluationMethod(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final DynaActionForm dynaActionForm = (DynaActionForm) form;
        final String evaluationMethod = request.getParameter("evaluationMethod");
        final String evaluationMethodEn = dynaActionForm.getString("evaluationMethodEn");
        final MultiLanguageString multiLanguageString = new MultiLanguageString();
        multiLanguageString.setContent(Language.pt, evaluationMethod);
        multiLanguageString.setContent(Language.en, evaluationMethodEn);

        final ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");

        final Object args[] = { executionCourse, multiLanguageString };
        executeService(request, "EditEvaluation", args);

        return mapping.findForward("evaluationMethod");
    }
    
    public ActionForward prepareImportEvaluationMethod(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
          
        request.setAttribute("importContentBean", new ImportContentBean());
        return mapping.findForward("importEvaluationMethod");
    }
    
    public ActionForward prepareImportEvaluationMethodPostBack(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {    
        
        prepareImportContentPostBack(request);
        return mapping.findForward("importEvaluationMethod");
    }

    public ActionForward prepareImportEvaluationMethodInvalid(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        
        prepareImportContentInvalid(request);        
        return mapping.findForward("importEvaluationMethod");
    }

    public ActionForward listExecutionCoursesToImportEvaluationMethod(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        
        listExecutionCoursesToImportContent(request);
        return mapping.findForward("importEvaluationMethod");
    }

    public ActionForward importEvaluationMethod(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
     
        importContent(request, "ImportEvaluationMethod");
        return mapping.findForward("evaluationMethod");
    }


    // BIBLIOGRAPHIC REFERENCES
    
    public ActionForward bibliographicReference(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("bibliographicReference");
    }

    public ActionForward prepareCreateBibliographicReference(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("create-bibliographicReference");
    }

    public ActionForward createBibliographicReference(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    		throws Exception {
        final DynaActionForm dynaActionForm = (DynaActionForm) form;
        final String title = dynaActionForm.getString("title");
        final String authors = dynaActionForm.getString("authors");
        final String reference = dynaActionForm.getString("reference");
        final String year = dynaActionForm.getString("year");
        final String optional = dynaActionForm.getString("optional");

        final ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");
        final IUserView userView = getUserView(request);

        final Object args[] = { executionCourse.getIdInternal(), title, authors, reference, year, Boolean.valueOf(optional) };
        ServiceManagerServiceFactory.executeService(userView, "CreateBibliographicReference", args);

        return mapping.findForward("bibliographicReference");
    }

    public ActionForward prepareEditBibliographicReference(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    		throws Exception {
        final ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");
        final String bibliographicReferenceIDString = request.getParameter("bibliographicReferenceID");
        if (executionCourse != null && bibliographicReferenceIDString != null && bibliographicReferenceIDString.length() > 0) {
            final BibliographicReference bibliographicReference = findBibliographicReference(executionCourse, Integer.valueOf(bibliographicReferenceIDString));
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

        final ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");
        final BibliographicReference bibliographicReference = findBibliographicReference(executionCourse, Integer.valueOf(bibliographicReferenceIDString));
        final IUserView userView = getUserView(request);

        final Object args[] = { bibliographicReference.getIdInternal(), title, authors, reference, year, Boolean.valueOf(optional) };
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
        
    public ActionForward prepareImportBibliographicReferences(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
          
        request.setAttribute("importContentBean", new ImportContentBean());
        return mapping.findForward("importBibliographicReferences");
    }
    
    public ActionForward prepareImportBibliographicReferencesPostBack(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {    
        
	prepareImportContentPostBack(request);
	return mapping.findForward("importBibliographicReferences");
    }
    
    public ActionForward prepareImportBibliographicReferencesInvalid(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        
	prepareImportContentInvalid(request);
	return mapping.findForward("importBibliographicReferences");
    }
    
    public ActionForward listExecutionCoursesToImportBibliographicReferences(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        
	listExecutionCoursesToImportContent(request);
	return mapping.findForward("importBibliographicReferences");
    }

    public ActionForward importBibliographicReferences(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
     
	importContent(request, "ImportBibliographicReferences");
        return mapping.findForward("bibliographicReference");
    }     
    
    // LESSON PLANNINGS
    public ActionForward submitDataToImportLessonPlannings(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        
        final IViewState viewState = RenderUtils.getViewState();
        final ImportLessonPlanningsBean bean = (ImportLessonPlanningsBean) viewState.getMetaObject().getObject();        
        request.setAttribute("importLessonPlanningBean", bean);
        return mapping.findForward("importLessonPlannings");
    }
    
    public ActionForward submitDataToImportLessonPlanningsPostBack(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        
        final IViewState viewState = RenderUtils.getViewState();
        final ImportLessonPlanningsBean bean = (ImportLessonPlanningsBean) viewState.getMetaObject().getObject();
        if(bean.getCurricularYear() == null || bean.getExecutionPeriod() == null || bean.getExecutionDegree() == null) {
            bean.setExecutionCourse(null);
            bean.setImportType(null);
            bean.setShift(null);
        }
        RenderUtils.invalidateViewState();
        request.setAttribute("importLessonPlanningBean", bean);
        return mapping.findForward("importLessonPlannings");
    }
           
    public ActionForward prepareImportLessonPlannings(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
          
        final ExecutionCourse executionCourseTo = (ExecutionCourse) request.getAttribute("executionCourse");        
        request.setAttribute("importLessonPlanningBean", new ImportLessonPlanningsBean(executionCourseTo));        
        return mapping.findForward("importLessonPlannings");
    }
    
    public ActionForward importLessonPlannings(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
     
        final IViewState viewState = RenderUtils.getViewState();
        ImportLessonPlanningsBean bean = (ImportLessonPlanningsBean) viewState.getMetaObject().getObject();                
        request.setAttribute("importLessonPlanningBean", bean);
        
        ExecutionCourse executionCourseFrom = bean.getExecutionCourse();
        ExecutionCourse executionCourseTo = bean.getExecutionCourseTo();
        ImportType importType = bean.getImportType();                   
        
        if(importType != null && importType.equals(ImportLessonPlanningsBean.ImportType.PLANNING)) {
            final Object args[] = { executionCourseTo.getIdInternal(), executionCourseTo, executionCourseFrom, null};
            try {
                executeService(request, "ImportLessonPlannings", args);
            } catch (DomainException e) {
                addActionMessage(request, e.getKey(), e.getArgs());
            }
        } else if(importType != null && importType.equals(ImportLessonPlanningsBean.ImportType.SUMMARIES)) {        
            return mapping.findForward("importLessonPlannings");
        }
        
        return lessonPlannings(mapping, form, request, response);
    }     
       
    public ActionForward importLessonPlanningsBySummaries(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
                       
        final IViewState viewState = RenderUtils.getViewState();
        ImportLessonPlanningsBean bean = (ImportLessonPlanningsBean) viewState.getMetaObject().getObject();                
               
        ExecutionCourse executionCourseTo = bean.getExecutionCourseTo();
        Shift shiftFrom = bean.getShift();  
        
        final Object args[] = { executionCourseTo.getIdInternal(), executionCourseTo, shiftFrom.getDisciplinaExecucao(), shiftFrom};
        try {
            executeService(request, "ImportLessonPlannings", args);
        } catch (DomainException e) {
            addActionMessage(request, e.getKey(), e.getArgs());
        }
       
        request.setAttribute("importLessonPlanningBean", bean);
        return lessonPlannings(mapping, form, request, response);
    }
        
    public ActionForward lessonPlannings(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        
        final ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");            
        Map<ShiftType, List<LessonPlanning>> lessonPlanningsMap = new TreeMap<ShiftType, List<LessonPlanning>>();      
        for (ShiftType shiftType : executionCourse.getShiftTypes()) {            
            List<LessonPlanning> lessonPlanningsOrderedByOrder = executionCourse.getLessonPlanningsOrderedByOrder(shiftType);
            if(!lessonPlanningsOrderedByOrder.isEmpty()) {
                lessonPlanningsMap.put(shiftType, lessonPlanningsOrderedByOrder);            
            }
        }       
        request.setAttribute("lessonPlanningsMap", lessonPlanningsMap);
        return mapping.findForward("lessonPlannings");
    }
    
    public ActionForward moveUpLessonPlanning(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
        
        Integer lessonPlanningID = Integer.valueOf(request.getParameter("lessonPlanningID"));        
        LessonPlanning lessonPlanning = rootDomainObject.readLessonPlanningByOID(lessonPlanningID);
        final Object args[] = { lessonPlanning.getExecutionCourse().getIdInternal(), lessonPlanning, 
                (lessonPlanning.getOrderOfPlanning() - 1) };        
        try {
            executeService(request, "MoveLessonPlanning", args);    
        } catch (DomainException e) {            
            addActionMessage(request, e.getKey(), e.getArgs());
        }   
        return lessonPlannings(mapping, form, request, response);
    }
    
    public ActionForward moveDownLessonPlanning(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
        
        Integer lessonPlanningID = Integer.valueOf(request.getParameter("lessonPlanningID"));        
        LessonPlanning lessonPlanning = rootDomainObject.readLessonPlanningByOID(lessonPlanningID);
        final Object args[] = { lessonPlanning.getExecutionCourse().getIdInternal(), lessonPlanning, 
                (lessonPlanning.getOrderOfPlanning() + 1)};        
        try {
            executeService(request, "MoveLessonPlanning", args);    
        } catch (DomainException e) {            
            addActionMessage(request, e.getKey(), e.getArgs());
        }   
        return lessonPlannings(mapping, form, request, response);
    }
    
    public ActionForward prepareCreateLessonPlanning(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        
        final ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");
        request.setAttribute("lessonPlanningBean", new CreateLessonPlanningBean(executionCourse));        
        return mapping.findForward("create-lessonPlanning");
    }
       
    public ActionForward prepareEditLessonPlanning(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        
        Integer lessonPlanningID = Integer.valueOf(request.getParameter("lessonPlanningID"));
        LessonPlanning lessonPlanning = rootDomainObject.readLessonPlanningByOID(lessonPlanningID);
        request.setAttribute("lessonPlanning", lessonPlanning);
        return mapping.findForward("create-lessonPlanning");
    }
            
    public ActionForward createLessonPlanning(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
               
        IViewState viewState = RenderUtils.getViewState();       
        final CreateLessonPlanningBean lessonPlanningBean = (CreateLessonPlanningBean) viewState.getMetaObject().getObject();
        final Object args[] = { lessonPlanningBean.getExecutionCourse().getIdInternal(), lessonPlanningBean.getTitle(), 
                lessonPlanningBean.getPlanning(), lessonPlanningBean.getLessonType(), lessonPlanningBean.getExecutionCourse()};
        
        try {
            executeService(request, "CreateLessonPlanning", args);    
        } catch (DomainException e) {            
            addActionMessage(request, e.getKey(), e.getArgs());
            request.setAttribute("lessonPlanningBean", lessonPlanningBean);        
            return mapping.findForward("create-lessonPlanning");
        }        
        return lessonPlannings(mapping, form, request, response);
    }
    
    public ActionForward deleteLessonPlanning(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
                       
        Integer lessonPlanningID = Integer.valueOf(request.getParameter("lessonPlanningID"));
        LessonPlanning lessonPlanning = rootDomainObject.readLessonPlanningByOID(lessonPlanningID);
        if(lessonPlanning != null) {
            final Object args[] = { lessonPlanning.getExecutionCourse().getIdInternal(), lessonPlanning, null, null};            
            try {
                executeService(request, "DeleteLessonPlanning", args);    
            } catch (DomainException e) {            
                addActionMessage(request, e.getKey(), e.getArgs());
            }               
        }
        return lessonPlannings(mapping, form, request, response);
    }
    
    public ActionForward deleteLessonPlannings(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException, FenixServiceException {
                       
        ShiftType lessonType = ShiftType.valueOf(request.getParameter("shiftType"));
        final ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");
        
        if(lessonType != null && executionCourse != null) {
            final Object args[] = { executionCourse.getIdInternal(), null, executionCourse, lessonType};            
            try {
                executeService(request, "DeleteLessonPlanning", args);    
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
        for (final BibliographicReference bibliographicReference : executionCourse.getAssociatedBibliographicReferencesSet()) {
            if (bibliographicReference.getIdInternal().equals(bibliographicReferenceID)) {
                return bibliographicReference;
            }
        }
        return null;
    }

    private Curriculum findCurriculum(final ExecutionCourse executionCourse, final Integer curriculumID) {
        for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
            for (final Curriculum curriculum : curricularCourse.getAssociatedCurriculumsSet()) {
                if (curriculum.getIdInternal().equals(curriculumID)) {
                    return curriculum;
                }
            }
        }
        return null;
    }

    private CurricularCourse findCurricularCourse(final ExecutionCourse executionCourse, final Integer curricularCourseID) {
        for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
        	if (curricularCourse.getIdInternal().equals(curricularCourseID)) {
        		return curricularCourse;
        	}
        }
        return null;
    }

    public ActionForward fileUpload(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    		throws Exception {
        final Item item = selectItem(request);

        IViewState viewState = RenderUtils.getViewState("creator");
        if (viewState == null) {
            return section(mapping, form, request, response);
        }
        
        FileItemCreationBean bean = (FileItemCreationBean) viewState.getMetaObject().getObject();
        RenderUtils.invalidateViewState();
        
        String displayName = bean.getDisplayName();
        if (displayName == null || displayName.length() == 0 || displayName.trim().length() == 0) {
            displayName = getFilenameOnly(bean.getFileName());
        }

        if (bean.getFileName() == null || bean.getFileName().length() == 0
                || bean.getFileSize() == 0) {
            addErrorMessage(request, "fileRequired", "errors.fileRequired");
            return uploadFile(mapping, form, request, response);
        }

        InputStream formFileInputStream = null;
        try {
            formFileInputStream = bean.getFile();
            final Object[] args = { item, formFileInputStream, bean.getFileName(), displayName, bean.getPermittedGroup() };
            executeService(request, "CreateFileItemForItem", args);
        } catch (FileManagerException e) {
            addErrorMessage(request, "unableToStoreFile", "errors.unableToStoreFile", bean.getFileName());
            return uploadFile(mapping, form, request, response);
        } finally {
            if (formFileInputStream != null) {
                formFileInputStream.close();
            }
        }

        return mapping.findForward("section");
    }

    private String getFilenameOnly(final String fullPathToFile) {
        // Strip all but the last filename. It would be nice
        // to know which OS the file came from.
        String filenameOnly = fullPathToFile;

        while (filenameOnly.indexOf('/') > -1) {
            filenameOnly = filenameOnly.substring(filenameOnly.indexOf('/') + 1);
        }

        while (filenameOnly.indexOf('\\') > -1) {
            filenameOnly = filenameOnly.substring(filenameOnly.indexOf('\\') + 1);
        }

        return filenameOnly;
    }

    public ActionForward deleteFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException, FenixServiceException {

        selectItem(request);
        FileItem fileItem = selectFileItem(request);

        try {
            ServiceUtils.executeService(getUserView(request), "DeleteFileItemFromItem", fileItem);
        } catch (FileManagerException e1) {
            addErrorMessage(request, "unableToDeleteFile", "errors.unableToDeleteFile");
        }

        return mapping.findForward("section");
    }

    private FileItem selectFileItem(HttpServletRequest request) {
        String fileItemIdString = request.getParameter("fileItemId");
        if (fileItemIdString == null) {
            return null;
        }
        
        FileItem fileItem = FileItem.readByOID(Integer.valueOf(fileItemIdString));
        request.setAttribute("fileItem", fileItem);
        
        return fileItem;
    }

    public ActionForward prepareEditItemFilePermissions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        selectItem(request);
        FileItem fileItem = selectFileItem(request);

        if (fileItem == null) {
            return section(mapping, form, request, response);
        }
        else {
            FileItemPermissionBean bean = new FileItemPermissionBean(fileItem);
            request.setAttribute("fileItemBean", bean);
            
            return mapping.findForward("editFile");
        }
    }

    public ActionForward editItemFilePermissions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    		throws Exception {
        
        selectItem(request);
        final FileItem fileItem = selectFileItem(request);

        IViewState viewState = RenderUtils.getViewState();
        if (viewState == null) {
            return mapping.findForward("section");
        }
        
        try {
            FileItemPermissionBean bean = (FileItemPermissionBean) viewState.getMetaObject().getObject();
            ServiceUtils.executeService(getUserView(request), "EditItemFilePermissions", fileItem, bean.getPermittedGroup());
            return mapping.findForward("section");
        } catch (FileManagerException ex) {
            addErrorMessage(request, "error.teacher.siteAdministration.editItemFilePermissions.unableToChangeFilePermissions");
            return mapping.findForward("editFile");
        }
    }

    public ActionForward saveSectionsOrder(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String orderString = request.getParameter("sectionsOrder");
        Site site = getSite(request); 

        Section parentSection = getSection(request);
        List<Section> initialOrder = flattenSection(site, parentSection);
        
        List<Pair<Section, Section>> hierarchy = new ArrayList<Pair<Section, Section>>();
        
        String[] nodes = orderString.split(",");
        for (int i = 0; i < nodes.length; i++) {
            String[] parts = nodes[i].split("-");
            
            Integer childIndex = getId(parts[0]);
            Integer parentIndex = getId(parts[1]);

            Section parent = initialOrder.get(parentIndex);
            Section child  = initialOrder.get(childIndex);
            
            hierarchy.add(new Pair<Section, Section>(parent, child));
        }

        ServiceUtils.executeService(getUserView(request), "RearrangeSiteSections", hierarchy);
        
        if (parentSection == null) {
            return sections(mapping, form, request, response);
        }
        else {
            return section(mapping, form, request, response);
        }
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

    private List<Section> flattenSection(Site site, Section parent) {
        List<Section> result = new ArrayList<Section>();
        
        result.add(parent);
        
        SortedSet<Section> sections;
        if (parent == null) {
            sections = site.getOrderedTopLevelSections();
        }
        else {
            sections = parent.getOrderedSubSections();
        }
        
        for (Section section : sections) {
            result.addAll(flattenSection(site, section));
        }
        
        return result;
    }

    private Site getSite(HttpServletRequest request) {
        ExecutionCourse executionCourse = (ExecutionCourse) request.getAttribute("executionCourse");
        
        if (executionCourse != null) {
            return executionCourse.getSite();
        } else {
            return null;
        }
    }
    
    public ActionForward organizeSectionItems(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Section section = getSection(request);
        if (section == null) {
            return sections(mapping, form, request, response);
        }
        
        selectSection(request, section);
        return mapping.findForward("organizeItems");
    }

    public ActionForward saveItemsOrder(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String orderString = request.getParameter("itemsOrder");
        Section section = getSection(request);

        if (section == null) {
            return sections(mapping, form, request, response); 
        }
        
        List<Item> initialItems = new ArrayList<Item>(section.getOrderedItems());
        List<Item> orderedItems = new ArrayList<Item>();
        
        String[] nodes = orderString.split(",");
        for (int i = 0; i < nodes.length; i++) {
            String[] parts = nodes[i].split("-");
            
            Integer itemIndex = getId(parts[0]);
            orderedItems.add(initialItems.get(itemIndex - 1));
        }

        ServiceUtils.executeService(getUserView(request), "RearrangeSectionItems", orderedItems);
        return section(mapping, form, request, response);
    }
    
    public ActionForward organizeItemFiles(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        selectItem(request);
        return mapping.findForward("organizeFiles");
    }
    
    public ActionForward saveFilesOrder(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String orderString = request.getParameter("filesOrder");
        Item item = getItem(request);

        if (item == null) {
            return sections(mapping, form, request, response); 
        }
        
        List<FileItem> initialFiles = new ArrayList<FileItem>(item.getSortedFileItems());
        List<FileItem> orderedFiles = new ArrayList<FileItem>();
        
        String[] nodes = orderString.split(",");
        for (int i = 0; i < nodes.length; i++) {
            String[] parts = nodes[i].split("-");
            
            Integer itemIndex = getId(parts[0]);
            orderedFiles.add(initialFiles.get(itemIndex - 1));
        }

        ServiceUtils.executeService(getUserView(request), "RearrangeItemFiles", item, orderedFiles);
        return section(mapping, form, request, response);
    }

    private void addErrorMessage(HttpServletRequest request, String key) {
        addErrorMessage(request, ActionMessages.GLOBAL_MESSAGE, key);
    }
    
    private void addErrorMessage(HttpServletRequest request, String property, String key, Object ... args) {
        ActionMessages messages = getErrors(request);
        messages.add(property, new ActionMessage(key, args));
        
        saveErrors(request, messages);
    }
    
    public ActionForward editSectionPermissions(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        selectSection(request);
        return mapping.findForward("editSectionPermissions");
    }
    
    public ActionForward editItemPermissions(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        selectItem(request);
        return mapping.findForward("editItemPermissions");
    }
}
