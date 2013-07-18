package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionCourseByOID;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidChangeServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonValidChangeServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.DeleteItem;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.DeleteSection;
import net.sourceforge.fenixedu.applicationTier.Servico.publico.ReadAllStudentsAndGroups;
import net.sourceforge.fenixedu.applicationTier.Servico.publico.ReadStudentsAndGroupsByShiftID;
import net.sourceforge.fenixedu.applicationTier.Servico.publico.ReadStudentsAndGroupsWithoutShift;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.AcceptNewProjectProposal;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.CreateGrouping;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.CreateStudentGroup;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.DeleteAllGroupingMembers;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.DeleteGrouping;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.DeleteGroupingMembers;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.DeleteGroupingMembersByExecutionCourseID;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.DeleteProfessorshipWithPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.DeleteProjectProposal;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.DeleteStudentGroup;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.DeleteStudentGroupMembers;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.EditCustomizationOptions;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.EditGrouping;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.EditProgram;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.EditStudentGroupShift;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.EditStudentGroupsShift;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.EnrollStudentGroupShift;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.ExecutionCourseHasProposals;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.ExecutionCourseWaitingAnswer;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.ImportCustomizationOptions;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.InsertGroupingMembers;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.InsertStudentGroupMembers;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.InsertStudentsInGrouping;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.NewProjectProposal;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.PrepareCreateStudentGroup;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.PrepareEditGroupingMembers;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.PrepareEditStudentGroupMembers;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.RejectNewProjectProposal;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.TeacherAdministrationSiteComponentService;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.TeacherResponsibleByExecutionCourse;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.UnEnrollStudentGroupShift;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.VerifyIfCanEnrollStudentGroupsInShift;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.VerifyIfGroupPropertiesHasProjectProposal;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.VerifyStudentGroupWithoutShift;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoGrouping;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteGrouping;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteGroupsByShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteInstructions;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteItems;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteNewProjectProposals;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteObjectives;
import net.sourceforge.fenixedu.dataTransferObject.InfoSitePrograms;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteProjects;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteRegularSections;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteRootSections;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteSection;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteSections;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteSentedProjectProposalsWaiting;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteShifts;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteShiftsAndGroups;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentGroup;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentGroupAndStudents;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentsAndGroups;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteTeachers;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.TeacherAdministrationSiteView;
import net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.ImportContentBean;
import net.sourceforge.fenixedu.domain.CourseLoad;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.mapping.SiteManagementActionMapping;
import net.sourceforge.fenixedu.util.EnrolmentGroupPolicyType;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.validator.DynaValidatorForm;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

/**
 * @author Fernanda Quitï¿½rio
 * @deprecated
 */
@Deprecated
public class TeacherAdministrationViewerDispatchAction extends FenixDispatchAction {

    public ActionForward instructions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        ISiteComponent instructionsComponent = new InfoSiteInstructions();
        readSiteView(request, instructionsComponent, null, null, null);
        return mapping.findForward("viewSite");
    }

    // ======================== Customization Options Management
    // ========================
    public ActionForward prepareCustomizationOptions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(getObjectCode(request));
        final InfoSite infoSite = InfoSite.newInfoFromDomain(executionCourse.getSite());
        final ISiteComponent customizationOptionsComponent = infoSite;
        readSiteView(request, customizationOptionsComponent, null, null, null);
        DynaValidatorForm alternativeSiteForm = (DynaValidatorForm) form;
        alternativeSiteForm.set("siteAddress", infoSite.getAlternativeSite());
        alternativeSiteForm.set("mail", infoSite.getMail());
        alternativeSiteForm.set("dynamicMailDistribution", infoSite.getDynamicMailDistribution());
        alternativeSiteForm.set("initialStatement", infoSite.getInitialStatement());
        alternativeSiteForm.set("introduction", infoSite.getIntroduction());
        return mapping.findForward("editAlternativeSite");
    }

    public static String mailingListDomainConfiguration() {
        return PropertiesManager.getProperty("mailingList.host.name");
    }

    public ActionForward submitDataToImportCustomizationOptions(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(getObjectCode(request));
        final IViewState viewState = RenderUtils.getViewState();
        final ImportContentBean bean = (ImportContentBean) viewState.getMetaObject().getObject();
        request.setAttribute("importContentBean", bean);
        request.setAttribute("executionCourse", executionCourse);
        return mapping.findForward("importCustomizationOptions");
    }

    public ActionForward submitDataToImportCustomizationOptionsPostBack(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(getObjectCode(request));
        final IViewState viewState = RenderUtils.getViewState();
        final ImportContentBean bean = (ImportContentBean) viewState.getMetaObject().getObject();
        if (bean.getCurricularYear() == null || bean.getExecutionPeriod() == null || bean.getExecutionDegree() == null) {
            bean.setExecutionCourse(null);
        }
        RenderUtils.invalidateViewState();
        request.setAttribute("importContentBean", bean);
        request.setAttribute("executionCourse", executionCourse);
        return mapping.findForward("importCustomizationOptions");
    }

    public ActionForward prepareImportCustomizationOptions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(getObjectCode(request));
        request.setAttribute("importContentBean", new ImportContentBean());
        request.setAttribute("executionCourse", executionCourse);
        return mapping.findForward("importCustomizationOptions");
    }

    public ActionForward importCustomizationOptions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException, FenixActionException {

        final IViewState viewState = RenderUtils.getViewState();
        ImportContentBean bean = (ImportContentBean) viewState.getMetaObject().getObject();
        request.setAttribute("importContentBean", bean);

        ExecutionCourse executionCourseFrom = bean.getExecutionCourse();
        ExecutionCourse executionCourseTo = rootDomainObject.readExecutionCourseByOID(getObjectCode(request));

        try {
            ImportCustomizationOptions.runImportCustomizationOptions(executionCourseTo.getIdInternal(),
                    executionCourseTo.getSite(), executionCourseFrom.getSite());
        } catch (DomainException e) {
            addActionMessage(request, e.getKey(), e.getArgs());
        }

        return prepareCustomizationOptions(mapping, form, request, response);
    }

    public ActionForward editCustomizationOptions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        DynaValidatorForm alternativeSiteForm = (DynaValidatorForm) form;
        Integer objectCode = getObjectCode(request);
        String alternativeSite = (String) alternativeSiteForm.get("siteAddress");
        String mail = (String) alternativeSiteForm.get("mail");
        String initialStatement = (String) alternativeSiteForm.get("initialStatement");
        String introduction = (String) alternativeSiteForm.get("introduction");
        Boolean dynamicMailDistribution = (Boolean) alternativeSiteForm.get("dynamicMailDistribution");

        ActionErrors errors = new ActionErrors();
        try {
            EditCustomizationOptions.runEditCustomizationOptions(objectCode, alternativeSite, mail, dynamicMailDistribution,
                    initialStatement, introduction);
        } catch (NotAuthorizedException e) {
            errors.add("notResponsible", new ActionError("label.notAuthorized.courseInformation"));
            saveErrors(request, errors);

            return mapping.getInputForward();
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        ISiteComponent instructionsComponent = new InfoSiteInstructions();
        readSiteView(request, instructionsComponent, null, null, null);
        request.setAttribute("alternativeSiteForm", alternativeSiteForm);
        return mapping.findForward("viewSite");
    }

    // ======================== Objectives Management ========================
    public ActionForward viewObjectives(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        ISiteComponent objectivesComponent = new InfoSiteObjectives();
        readSiteView(request, objectivesComponent, null, null, null);
        return mapping.findForward("viewObjectives");
    }

    public ActionForward prepareEditObjectives(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        ISiteComponent objectivesComponent = new InfoCurriculum();

        Integer objectCode = getObjectCode(request);

        String curricularCourseCodeString = request.getParameter("curricularCourseCode");
        Integer curricularCourseCode = new Integer(curricularCourseCodeString);
        request.setAttribute("curricularCourseCode", curricularCourseCode);

        try {
            readSiteView(request, objectivesComponent, null, curricularCourseCode, null);
        } catch (FenixActionException e1) {
            throw e1;
        }

        IUserView userView = getUserView(request);

        // Filter if the cteacher is responsibles for the execution course

        Boolean isResponsible = null;
        try {
            isResponsible = TeacherResponsibleByExecutionCourse.run(userView.getUtilizador(), objectCode, curricularCourseCode);

        } catch (FenixServiceException e) {
            e.printStackTrace();
            throw new FenixActionException(e);
        }
        if (isResponsible.booleanValue() == false) {
            ActionErrors errors = new ActionErrors();

            errors.add("notAuthorized", new ActionError("error.exception.notAuthorized2"));
            saveErrors(request, errors);

            return mapping.findForward("notAuthorized");
        }

        TeacherAdministrationSiteView siteView = (TeacherAdministrationSiteView) request.getAttribute("siteView");

        if (siteView.getComponent() != null) {
            DynaActionForm objectivesForm = (DynaActionForm) form;

            objectivesForm.set("generalObjectives", ((InfoCurriculum) siteView.getComponent()).getGeneralObjectives());
            objectivesForm.set("generalObjectivesEn", ((InfoCurriculum) siteView.getComponent()).getGeneralObjectivesEn());
            objectivesForm.set("operacionalObjectives", ((InfoCurriculum) siteView.getComponent()).getOperacionalObjectives());
            objectivesForm
                    .set("operacionalObjectivesEn", ((InfoCurriculum) siteView.getComponent()).getOperacionalObjectivesEn());
        }

        return mapping.findForward("editObjectives");
    }

    public ActionForward editObjectives(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        Integer objectCode = getObjectCode(request);
        String curricularCourseCodeString = request.getParameter("curricularCourseCode");
        Integer curricularCourseCode = new Integer(curricularCourseCodeString);
        DynaActionForm objectivesForm = (DynaActionForm) form;

        InfoCurriculum infoCurriculumNew = new InfoCurriculum();

        infoCurriculumNew.setIdInternal(curricularCourseCode);
        infoCurriculumNew.setGeneralObjectives((String) objectivesForm.get("generalObjectives"));
        infoCurriculumNew.setGeneralObjectivesEn((String) objectivesForm.get("generalObjectivesEn"));
        infoCurriculumNew.setOperacionalObjectives((String) objectivesForm.get("operacionalObjectives"));
        infoCurriculumNew.setOperacionalObjectivesEn((String) objectivesForm.get("operacionalObjectivesEn"));

        IUserView userView = getUserView(request);

        Object args[] = { objectCode, curricularCourseCode, infoCurriculumNew, userView.getUtilizador() };

//        try {
        throw new UnsupportedOperationException("Service no longer exists!");
//            null.runEditObjectives(args);

//       } catch (FenixServiceException e) {
//            throw new FenixActionException(e);
//        }
//        return viewObjectives(mapping, form, request, response);
    }

    // ======================== Program Management ========================
    public ActionForward viewProgram(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        ISiteComponent programComponent = new InfoSitePrograms();
        readSiteView(request, programComponent, null, null, null);
        return mapping.findForward("viewProgram");
    }

    public ActionForward prepareEditProgram(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        Integer objectCode = getObjectCode(request);

        String curricularCourseCodeString = request.getParameter("curricularCourseCode");
        Integer curricularCourseCode = new Integer(curricularCourseCodeString);
        request.setAttribute("curricularCourseCode", curricularCourseCode);

        ISiteComponent programComponent = new InfoCurriculum();

        try {
            readSiteView(request, programComponent, null, curricularCourseCode, null);

        } catch (FenixActionException e1) {
            throw e1;
        }

        IUserView userView = getUserView(request);

        // Filter if the cteacher is responsibles for the execution course

        Boolean isResponsible = null;
        try {
            isResponsible = TeacherResponsibleByExecutionCourse.run(userView.getUtilizador(), objectCode, curricularCourseCode);

        } catch (FenixServiceException e) {
            e.printStackTrace();
            throw new FenixActionException(e);
        }
        if (isResponsible.booleanValue() == false) {
            ActionErrors errors = new ActionErrors();

            errors.add("notAuthorized", new ActionError("error.exception.notAuthorized2"));
            saveErrors(request, errors);

            return mapping.findForward("notAuthorized");
        }

        TeacherAdministrationSiteView siteView = (TeacherAdministrationSiteView) request.getAttribute("siteView");

        if (siteView.getComponent() != null) {
            DynaActionForm programForm = (DynaActionForm) form;
            programForm.set("program", ((InfoCurriculum) siteView.getComponent()).getProgram());
            programForm.set("programEn", ((InfoCurriculum) siteView.getComponent()).getProgramEn());
        }

        return mapping.findForward("editProgram");
    }

    public ActionForward editProgram(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        Integer objectCode = getObjectCode(request);
        String curricularCourseCodeString = request.getParameter("curricularCourseCode");
        Integer curricularCourseCode = new Integer(curricularCourseCodeString);

        DynaActionForm programForm = (DynaActionForm) form;

        InfoCurriculum infoCurriculumNew = new InfoCurriculum();
        infoCurriculumNew.setIdInternal(curricularCourseCode);
        infoCurriculumNew.setProgram((String) programForm.get("program"));
        infoCurriculumNew.setProgramEn((String) programForm.get("programEn"));

        IUserView userView = getUserView(request);
        try {
            EditProgram.runEditProgram(objectCode, curricularCourseCode, infoCurriculumNew, userView.getUtilizador());
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return viewProgram(mapping, form, request, response);
    }

    // ======================== Teachers Management ========================
    public ActionForward viewTeachersByProfessorship(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        String username = getUsername(request);
        ISiteComponent teachersComponent = new InfoSiteTeachers();
        readSiteView(request, teachersComponent, null, null, username);
        return mapping.findForward("viewTeachers");
    }

    public ActionForward viewProfessorshipProperties(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        String username = getUsername(request);
        ISiteComponent teachersComponent = new InfoSiteTeachers();
        readSiteView(request, teachersComponent, null, null, username);
        Professorship professorship = getDomainObject(request, "teacherOID");
        request.setAttribute("professorship", professorship);
        return mapping.findForward("professorshipProperties");
    }

    private String getUsername(HttpServletRequest request) {
        IUserView userView = getUserView(request);
        String username = userView.getUtilizador();
        return username;
    }

    public ActionForward prepareAssociateTeacher(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        readSiteView(request, null, null, null, null);
        return mapping.findForward("associateTeacher");
    }

    public ActionForward associateTeacher(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        if (isCancelled(request)) {
            RenderUtils.invalidateViewState();
            DynaActionForm teacherForm = (DynaActionForm) form;
            teacherForm.set("teacherId", "");
            return prepareAssociateTeacher(mapping, form, request, response);
        }

        Integer objectCode = getObjectCode(request);
        DynaActionForm teacherForm = (DynaActionForm) form;
        String id = (String) teacherForm.get("teacherId");
        Person person = null;

        person = Person.readPersonByIstUsername(id);

        if (person != null) {
            try {
                final ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester();
                if ((person.getTeacher() != null && (person.getTeacher().getTeacherAuthorization(executionSemester) != null || person
                        .hasRole(RoleType.TEACHER))) || slappedInTheFaceOneTooManyTimes(executionSemester)) {
                    Professorship professorship =
                            Professorship.create(false, rootDomainObject.readExecutionCourseByOID(objectCode), person, 0.0);
                    request.setAttribute("teacherOID", professorship.getExternalId());
                } else if (person.getTeacher() == null || person.getTeacher().getCategoryByPeriod(executionSemester) == null) {
                    final ActionErrors actionErrors = new ActionErrors();
                    actionErrors.add("error", new ActionMessage("label.invalid.teacher.without.auth"));
                    saveErrors(request, actionErrors);
                    return prepareAssociateTeacher(mapping, teacherForm, request, response);
                }
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }
        } else {
            final ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("error", new ActionMessage("label.invalid.teacher.number"));
            saveErrors(request, actionErrors);
            return prepareAssociateTeacher(mapping, teacherForm, request, response);
        }
        return viewProfessorshipProperties(mapping, teacherForm, request, response);
    }

    private boolean slappedInTheFaceOneTooManyTimes(final ExecutionSemester executionSemester) {
        return executionSemester.getSemester().intValue() == 2
                && executionSemester.getExecutionYear().getYear().equals("2010/2011");
    }

    public ActionForward removeTeacher(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        Professorship professorship = getDomainObject(request, "teacherOID");
        Integer objectCode = getObjectCode(request);
        try {
            DeleteProfessorshipWithPerson.run(professorship.getPerson(), rootDomainObject.readExecutionCourseByOID(objectCode));
        } catch (NotAuthorizedException e) {
            final ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("error", new ActionError("label.not.authorized.action"));
            saveErrors(request, actionErrors);
        } catch (DomainException domainException) {
            final ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("error", new ActionError(domainException.getMessage()));
            saveErrors(request, actionErrors);
        }
        return viewTeachersByProfessorship(mapping, form, request, response);
    }

    // ======================== Evaluation Management
    // ========================
    public ActionForward viewEvaluation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        ISiteComponent evaluationComponent = new InfoSiteEvaluation();
        readSiteView(request, evaluationComponent, null, null, null);
        return mapping.findForward("viewEvaluation");
    }

    // ======================== Sections Management ========================
    public ActionForward sectionsFirstPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        readSiteView(request, null, null, null, null);
        return mapping.findForward("sectionsFirstPage");
    }

    public ActionForward viewSection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        Integer sectionCode = getSectionCode(request);
        return viewSection(mapping, form, request, response, sectionCode);
    }

    public ActionForward viewSection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response, Integer sectionCode) throws FenixActionException {
        ISiteComponent sectionComponent = new InfoSiteSection();
        readSiteView(request, sectionComponent, null, sectionCode, null);

        return mapping.findForward("viewSection");
    }

    public ActionForward prepareCreateRegularSection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        Integer sectionCode = getSectionCode(request);
        ISiteComponent regularSectionsComponent = new InfoSiteRegularSections();
        readSiteView(request, regularSectionsComponent, null, sectionCode, null);
        request.setAttribute("currentSectionCode", sectionCode);
        return mapping.findForward("createSubSection");
    }

    private Integer getSectionCode(HttpServletRequest request) {
        Integer sectionCode = null;
        String sectionCodeString = request.getParameter("currentSectionCode");
        if (sectionCodeString == null) {
            sectionCodeString = (String) request.getAttribute("currentSectionCode");
        }
        if (sectionCodeString != null) {
            sectionCode = new Integer(sectionCodeString);
        }
        return sectionCode;
    }

    public ActionForward prepareCreateRootSection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        ISiteComponent rootSectionsComponent = new InfoSiteRootSections();
        readSiteView(request, rootSectionsComponent, null, null, null);
        return mapping.findForward("createSection");
    }

    public ActionForward deleteSection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        Integer superiorSectionCode = getSuperiorSectionCode(request);
        Integer sectionCode = getSectionCode(request);
        Integer objectCode = getObjectCode(request);
        try {
            DeleteSection.runDeleteSection((Site) RootDomainObject.getInstance().readContentByOID(objectCode),
                    (Section) RootDomainObject.getInstance().readContentByOID(sectionCode));
            // if this section has a parent section
            if (superiorSectionCode != null) {
                return viewSection(mapping, form, request, response, superiorSectionCode);
            }
            return sectionsFirstPage(mapping, form, request, response);

        } catch (DomainException ex) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add(ex.getMessage(), new ActionError(ex.getMessage()));
            saveErrors(request, actionErrors);

            return viewSection(mapping, form, request, response);
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        } catch (Exception e) {
            throw new FenixActionException(e.getMessage());
        }
    }

    private Integer getSuperiorSectionCode(HttpServletRequest request) {
        Integer sectionCode = null;
        String sectionCodeString = request.getParameter("superiorSectionCode");
        if (sectionCodeString == null) {
            sectionCodeString = (String) request.getAttribute("superiorSectionCode");
        }
        if (sectionCodeString != null) {
            sectionCode = new Integer(sectionCodeString);
        }
        return sectionCode;
    }

    public ActionForward prepareFileUpload(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        Integer itemCode = getItemCode(request);
        ISiteComponent itemsComponent = new InfoSiteItems();
        readSiteView(request, itemsComponent, null, itemCode, null);
        return mapping.findForward("uploadItemFile");
    }

    private String getFilenameOnly(String fullPathToFile) {
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

    private Integer getItemCode(HttpServletRequest request) {
        Integer itemCode = null;
        String itemCodeString = request.getParameter("itemCode");
        if (itemCodeString == null) {
            itemCodeString = (String) request.getAttribute("itemCode");
        }
        if (itemCodeString != null) {
            itemCode = new Integer(itemCodeString);
        }
        return itemCode;
    }

    private Integer getFileItemId(HttpServletRequest request) {
        Integer fileItemId = null;
        String fileItemIdString = request.getParameter("fileItemId");
        if (fileItemIdString != null) {
            fileItemId = Integer.valueOf(fileItemIdString);
        } else {
            fileItemId = (Integer) request.getAttribute("fileItemId");
        }

        return fileItemId;
    }

    public ActionForward deleteItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        Integer itemCode = getItemCode(request);
        Integer objectCode = getObjectCode(request);
        try {
            DeleteItem.runDeleteItem((Site) RootDomainObject.getInstance().readContentByOID(objectCode), (Item) RootDomainObject
                    .getInstance().readContentByOID(itemCode));
        } catch (DomainException ex) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add(ex.getMessage(), new ActionError(ex.getMessage()));
            saveErrors(request, actionErrors);

            return viewSection(mapping, form, request, response);
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException);
        }

        return viewSection(mapping, form, request, response);
    }

    public ActionForward validationError(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        SiteManagementActionMapping siteManagementActionMapping = (SiteManagementActionMapping) mapping;
        ISiteComponent siteComponent = getSiteComponentForValidationError(siteManagementActionMapping);
        Integer infoExecutionCourseCode = null;
        Object obj1 = null;
        Object obj2 = null;
        if (siteComponent instanceof InfoSiteItems) {
            obj1 = getItemCode(request);
        } else if (siteComponent instanceof InfoSiteTeachers) {
            obj2 = getUsername(request);
        } else if (siteComponent instanceof InfoSiteRegularSections) {
            obj1 = getSectionCode(request);
        } else if (siteComponent instanceof InfoSiteSections) {
            obj1 = getSectionCode(request);
        } else if (siteComponent instanceof InfoSiteSection) {
            obj1 = getSectionCode(request);
        }
        readSiteView(request, siteComponent, infoExecutionCourseCode, obj1, obj2);
        return mapping.findForward(siteManagementActionMapping.getInputForwardName());
    }

    private ISiteComponent getSiteComponentForValidationError(SiteManagementActionMapping mapping) {
        ISiteComponent siteComponent = null;
        String className = mapping.getComponentClassName();
        try {
            Class componentClass = this.getClass().getClassLoader().loadClass(className);
            Constructor c = componentClass.getConstructor(new Class[] {});
            c.setAccessible(true);
            siteComponent = (ISiteComponent) c.newInstance(new Object[] {});
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return siteComponent;
    }

    private Integer getObjectCode(HttpServletRequest request) {
        Integer objectCode = null;
        String objectCodeString = request.getParameter("objectCode");
        if (objectCodeString == null) {
            objectCodeString = (String) request.getAttribute("objectCode");
        }
        if (objectCodeString != null) {
            objectCode = new Integer(objectCodeString);
        }
        return objectCode;
    }

    private Integer getParameter(HttpServletRequest request, String name) {
        Integer objectCode = null;
        String objectCodeString = request.getParameter(name);
        if (objectCodeString == null) {
            objectCodeString = (String) request.getAttribute(name);
        }
        if (objectCodeString != null) {
            objectCode = new Integer(objectCodeString);
        }
        return objectCode;
    }

    private SiteView readSiteView(HttpServletRequest request, ISiteComponent firstPageComponent, Integer infoExecutionCourseCode,
            Object obj1, Object obj2) throws FenixActionException {

        Integer objectCode = null;
        if (infoExecutionCourseCode == null) {
            objectCode = getObjectCode(request);
            infoExecutionCourseCode = objectCode;
        }

        ISiteComponent commonComponent = new InfoSiteCommon();
        ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(infoExecutionCourseCode);
        try {
            TeacherAdministrationSiteView siteView =
                    TeacherAdministrationSiteComponentService.runTeacherAdministrationSiteComponentService(
                            infoExecutionCourseCode, commonComponent, firstPageComponent, objectCode, obj1, obj2);
            request.setAttribute("siteView", siteView);
            request.setAttribute("listPersons", executionCourse.getProfessorships());
            request.setAttribute("objectCode", ((InfoSiteCommon) siteView.getCommonComponent()).getExecutionCourse()
                    .getIdInternal());
            if (siteView.getComponent() instanceof InfoSiteSection) {
                request.setAttribute("infoSection", ((InfoSiteSection) siteView.getComponent()).getSection());
            }

            return siteView;

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

    }

    // ======================== GROUPS MANAGEMENT ========================

    public ActionForward viewExecutionCourseProjects(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        Integer objectCode = getObjectCode(request);

        try {

            Boolean hasProposals = ExecutionCourseHasProposals.runExecutionCourseHasProposals(objectCode);
            Boolean waitingAnswer = ExecutionCourseWaitingAnswer.runExecutionCourseWaitingAnswer(objectCode);

            if (hasProposals.booleanValue()) {
                request.setAttribute("hasProposals", new Boolean(true));
            }
            if (waitingAnswer.booleanValue()) {
                request.setAttribute("waitingAnswer", new Boolean(true));
            }

        } catch (FenixServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noExecutionCourse");
            actionErrors.add("error.noExecutionCourse", error);
            saveErrors(request, actionErrors);
            return instructions(mapping, form, request, response);
        }
        return mapping.findForward("viewProjectsAndLink");

    }

    public ActionForward prepareViewExecutionCourseProjects(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        ISiteComponent viewProjectsComponent = new InfoSiteProjects();
        readSiteView(request, viewProjectsComponent, null, null, null);
        return viewExecutionCourseProjects(mapping, form, request, response);
    }

    public ActionForward viewNewProjectProposals(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        ISiteComponent viewNewProjectProposalsComponent = new InfoSiteNewProjectProposals();
        readSiteView(request, viewNewProjectProposalsComponent, null, null, null);

        if (((InfoSiteNewProjectProposals) viewNewProjectProposalsComponent).getInfoGroupPropertiesList().size() == 0) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.viewNewProjectProposals");
            actionErrors.add("error.viewNewProjectProposals", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        }
        return mapping.findForward("viewNewProjectProposals");
    }

    public ActionForward viewSentedProjectProposalsWaiting(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        ISiteComponent viewSentedProjectProposalsWaitingComponent = new InfoSiteSentedProjectProposalsWaiting();
        readSiteView(request, viewSentedProjectProposalsWaitingComponent, null, null, null);

        if (((InfoSiteSentedProjectProposalsWaiting) viewSentedProjectProposalsWaitingComponent).getInfoGroupPropertiesList()
                .size() == 0) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.SentedProjectsProposalsWaiting");
            actionErrors.add("error.SentedProjectsProposalsWaiting", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        }
        return mapping.findForward("viewSentedProjectProposalsWaiting");
    }

    public ActionForward rejectNewProjectProposal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        IUserView userView = getUserView(request);
        Integer objectCode = getObjectCode(request);
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);
        Object[] args = { objectCode, groupPropertiesCode, userView.getUtilizador() };
        try {
            RejectNewProjectProposal.runRejectNewProjectProposal(objectCode, groupPropertiesCode, userView.getUtilizador());
        } catch (ExistingServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noProjectProposal");
            actionErrors.add("error.noProjectProposal", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (NotAuthorizedException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noGroupProperties.ProjectProposal");
            actionErrors.add("error.noGroupProperties.ProjectProposal", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (FenixServiceException e) {
            e.printStackTrace();
            throw new FenixActionException(e.getMessage());
        }

        return prepareViewExecutionCourseProjects(mapping, form, request, response);
    }

    public ActionForward acceptNewProjectProposal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        IUserView userView = getUserView(request);
        Integer objectCode = getObjectCode(request);
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);
        try {
            AcceptNewProjectProposal.runAcceptNewProjectProposal(objectCode, groupPropertiesCode, userView.getUtilizador());
        } catch (ExistingServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noProjectProposal");
            actionErrors.add("error.noProjectProposal", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (NotAuthorizedException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noGroupProperties.ProjectProposal");
            actionErrors.add("error.noGroupProperties.ProjectProposal", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (InvalidSituationServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("error.ProjectProposalName", new ActionError("error.ProjectProposalName", e.getMessage()));
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);

        } catch (FenixServiceException e) {
            e.printStackTrace();
            throw new FenixActionException(e.getMessage());
        }

        return prepareViewExecutionCourseProjects(mapping, form, request, response);
    }

    public ActionForward viewShiftsAndGroups(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        Integer objectCode = getObjectCode(request);
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);

        InfoExecutionCourse infoExecutionCourse = ReadExecutionCourseByOID.run(objectCode);
        InfoExecutionPeriod infoExecutionPeriod = infoExecutionCourse.getInfoExecutionPeriod();
        request.setAttribute(PresentationConstants.EXECUTION_PERIOD, infoExecutionPeriod);
        request.setAttribute(PresentationConstants.EXECUTION_PERIOD_OID, infoExecutionPeriod.getIdInternal().toString());

        ISiteComponent shiftsAndGroupsView = new InfoSiteShiftsAndGroups();
        readSiteView(request, shiftsAndGroupsView, null, groupPropertiesCode, null);

        if (((InfoSiteShiftsAndGroups) shiftsAndGroupsView).getInfoGrouping() == null) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noGroupProperties");
            actionErrors.add("error.noGroupProperties", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        }

        if (((InfoSiteShiftsAndGroups) shiftsAndGroupsView).getInfoSiteGroupsByShiftList().isEmpty()) {
            request.setAttribute("noShifts", new Boolean(true));
        } else {
            boolean found = false;
            Iterator iterShiftsAndGroups =
                    ((InfoSiteShiftsAndGroups) shiftsAndGroupsView).getInfoSiteGroupsByShiftList().iterator();
            while (iterShiftsAndGroups.hasNext() && !found) {
                InfoSiteGroupsByShift shiftsAndGroups = (InfoSiteGroupsByShift) iterShiftsAndGroups.next();
                if (!shiftsAndGroups.getInfoSiteStudentGroupsList().isEmpty()) {
                    request.setAttribute("hasGroups", new Boolean(true));
                    found = true;
                }
            }
        }

        return mapping.findForward("viewShiftsAndGroups");

    }

    public ActionForward viewStudentGroupInformation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        Integer objectCode = getObjectCode(request);
        String studentGroupCodeString = request.getParameter("studentGroupCode");
        Integer studentGroupCode = new Integer(studentGroupCodeString);
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);

        String shiftCodeString = request.getParameter("shiftCode");

        ISiteComponent viewStudentGroup = new InfoSiteStudentGroup();
        readSiteView(request, viewStudentGroup, null, studentGroupCode, null);
        StudentGroup group = rootDomainObject.readStudentGroupByOID(studentGroupCode);
        request.setAttribute("groupMembers", group.getAttends());

        if (request.getParameter("showPhotos") == null) {
            request.setAttribute("showPhotos", "false");
        }

        try {

            Integer type =
                    VerifyStudentGroupWithoutShift.runVerifyStudentGroupWithoutShift(objectCode, studentGroupCode,
                            groupPropertiesCode, shiftCodeString);

            request.setAttribute("ShiftType", type);

        } catch (ExistingServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noGroupProperties");
            actionErrors.add("error.noGroupProperties", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (InvalidSituationServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noGroup");
            actionErrors.add("error.noGroup", error);
            saveErrors(request, actionErrors);
            if (shiftCodeString != null) {
                Integer shiftCode = new Integer(shiftCodeString);
                request.setAttribute("shiftCode", shiftCode);
            }
            return viewShiftsAndGroups(mapping, form, request, response);
        } catch (InvalidArgumentsServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.StudentGroupShiftIsChanged");
            actionErrors.add("error.StudentGroupShiftIsChanged", error);
            saveErrors(request, actionErrors);
            return viewShiftsAndGroups(mapping, form, request, response);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return mapping.findForward("viewStudentGroupInformation");
    }

    public ActionForward viewDeletedStudentGroupInformation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        int groupId = Integer.valueOf(request.getParameter("studentGroupId"));
        StudentGroup studentGroup = rootDomainObject.readStudentGroupByOID(groupId);
        request.setAttribute("studentGroup", studentGroup);
        request.setAttribute("executionCourseID", request.getParameter("executionCourseID"));
        request.setAttribute("projectID", request.getParameter("projectID"));
        return mapping.findForward("viewDeletedStudentGroupInformation");
    }

    public List<LabelValueBean> getShiftTypeLabelValues(HttpServletRequest request, Boolean differentiatedCapacity) {
        Integer executionCourseCode = getObjectCode(request);
        ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseCode);

        List<LabelValueBean> shiftTypeValues = new ArrayList<LabelValueBean>();

        if (executionCourse != null) {
            for (CourseLoad cl : executionCourse.getCourseLoads()) {
                shiftTypeValues.add(new LabelValueBean(RenderUtils.getEnumString(cl.getType()), cl.getType().name()));
            }
        }
        if (!differentiatedCapacity) {
            shiftTypeValues.add(new LabelValueBean("SEM TURNO", "SEM TURNO"));
        }

        return shiftTypeValues;
    }

    public ActionForward prepareCreateGroupProperties(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        DynaActionForm groupPropertiesForm = (DynaActionForm) form;
        readSiteView(request, null, null, null, null);
        List<LabelValueBean> shiftTypeValues = getShiftTypeLabelValues(request, getDifferentiatedCapacity(groupPropertiesForm));
        request.setAttribute("shiftTypeValues", shiftTypeValues);

        if (getDifferentiatedCapacity(groupPropertiesForm)) {
            request.setAttribute("automaticEnrolmentDisable", "true");
        }
        if (getAutomaticEnrolment(groupPropertiesForm)) {
            request.setAttribute("differentiatedCapacityDisable", "true");
        }
        return mapping.findForward("insertGroupProperties");
    }

    public ActionForward createGroupPropertiesPostBack(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        DynaActionForm groupPropertiesForm = setAutomaticEnrolmentFields(form, request, null);

        return prepareCreateGroupProperties(mapping, groupPropertiesForm, request, response);
    }

    public ActionForward createGroupCapacityPropertiesPostBack(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        DynaActionForm groupPropertiesForm = setDifferentiatedCapacity(form, request, null);
        readSiteView(request, null, null, null, null);
        Integer executionCourseCode = getObjectCode(request);

        ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseCode);
        String shiftType = (String) groupPropertiesForm.get("shiftType");

        if (shiftType.equalsIgnoreCase("Sem Turno")) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.groupProperties.DiffCapacityNoShift");
            actionErrors.add("error.groupProperties.DiffCapacityNoShift", error);
            saveErrors(request, actionErrors);
            groupPropertiesForm.set("differentiatedCapacity", Boolean.FALSE);
            return prepareCreateGroupProperties(mapping, groupPropertiesForm, request, response);
        }
        List<InfoShift> shiftsList = InfoShift.getInfoShiftsByType(executionCourse, ShiftType.valueOf(shiftType));

        if (shiftsList == null || shiftsList.size() == 0) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.groupProperties.DiffCapacityNoShiftsList");
            actionErrors.add("error.groupProperties.DiffCapacityNoShiftsList", error);
            saveErrors(request, actionErrors);
            groupPropertiesForm.set("differentiatedCapacity", Boolean.FALSE);
            return prepareCreateGroupProperties(mapping, groupPropertiesForm, request, response);
        }
        request.setAttribute("shiftsList", shiftsList);

        RenderUtils.invalidateViewState();

        return prepareCreateGroupProperties(mapping, groupPropertiesForm, request, response);
    }

    private DynaActionForm setDifferentiatedCapacity(ActionForm form, HttpServletRequest request, InfoGrouping infoGroupProperties) {
        DynaActionForm groupPropertiesForm = (DynaActionForm) form;
        Boolean differentiatedCapacity = getDifferentiatedCapacity(groupPropertiesForm);

        if (differentiatedCapacity) {
            if (infoGroupProperties != null) {
                infoGroupProperties.setGroupMaximumNumber(null);
                infoGroupProperties.setAutomaticEnrolment(Boolean.FALSE);
                infoGroupProperties.setDifferentiatedCapacity(Boolean.TRUE);
            }
            groupPropertiesForm.set("groupMaximumNumber", StringUtils.EMPTY);
            groupPropertiesForm.set("automaticEnrolment", Boolean.FALSE);
            groupPropertiesForm.set("differentiatedCapacity", Boolean.TRUE);
        } else {
            if (infoGroupProperties != null) {
                infoGroupProperties.setGroupMaximumNumber(null);
                infoGroupProperties.setAutomaticEnrolment(Boolean.FALSE);
                infoGroupProperties.setDifferentiatedCapacity(Boolean.FALSE);
            }
            groupPropertiesForm.set("groupMaximumNumber", StringUtils.EMPTY);
            groupPropertiesForm.set("differentiatedCapacity", Boolean.FALSE);
        }
        return groupPropertiesForm;
    }

    private DynaActionForm setAutomaticEnrolmentFields(ActionForm form, HttpServletRequest request,
            InfoGrouping infoGroupProperties) {
        DynaActionForm groupPropertiesForm = (DynaActionForm) form;
        Boolean automaticEnrolment = getAutomaticEnrolment(groupPropertiesForm);
        if (automaticEnrolment) {
            Integer objectCode = getObjectCode(request);
            ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(objectCode);
            if (infoGroupProperties != null) {
                infoGroupProperties.setMaximumCapacity(1);
                infoGroupProperties.setMinimumCapacity(1);
                infoGroupProperties.setIdealCapacity(1);
                infoGroupProperties.setGroupMaximumNumber(executionCourse.getAttendsCount());
                infoGroupProperties.setShiftType(null);
                infoGroupProperties.setEnrolmentPolicy(new EnrolmentGroupPolicyType(2));
                infoGroupProperties.setAutomaticEnrolment(Boolean.TRUE);
                infoGroupProperties.setDifferentiatedCapacity(Boolean.FALSE);
            }
            groupPropertiesForm.set("maximumCapacity", "1");
            groupPropertiesForm.set("minimumCapacity", "1");
            groupPropertiesForm.set("idealCapacity", "1");
            groupPropertiesForm.set("groupMaximumNumber", String.valueOf(executionCourse.getAttendsCount()));
            groupPropertiesForm.set("shiftType", "SEM TURNO");
            groupPropertiesForm.set("enrolmentPolicy", "false");
            request.setAttribute("automaticEnrolment", "true");
            request.setAttribute("differentiatedCapacity", "false");
        } else {
            if (infoGroupProperties != null) {
                infoGroupProperties.setMaximumCapacity(null);
                infoGroupProperties.setMinimumCapacity(null);
                infoGroupProperties.setIdealCapacity(null);
                infoGroupProperties.setGroupMaximumNumber(null);
                infoGroupProperties.setShiftType(null);
                infoGroupProperties.setEnrolmentPolicy(new EnrolmentGroupPolicyType(1));
                infoGroupProperties.setAutomaticEnrolment(Boolean.FALSE);
                infoGroupProperties.setDifferentiatedCapacity(Boolean.FALSE);
            }
            groupPropertiesForm.set("maximumCapacity", StringUtils.EMPTY);
            groupPropertiesForm.set("minimumCapacity", StringUtils.EMPTY);
            groupPropertiesForm.set("idealCapacity", StringUtils.EMPTY);
            groupPropertiesForm.set("groupMaximumNumber", StringUtils.EMPTY);
            groupPropertiesForm.set("shiftType", StringUtils.EMPTY);
            groupPropertiesForm.set("enrolmentPolicy", StringUtils.EMPTY);
        }
        return groupPropertiesForm;
    }

    public ActionForward createGroupProperties(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        DynaActionForm insertGroupPropertiesForm = (DynaActionForm) form;
        String name = (String) insertGroupPropertiesForm.get("name");
        String projectDescription = (String) insertGroupPropertiesForm.get("projectDescription");
        String maximumCapacityString = (String) insertGroupPropertiesForm.get("maximumCapacity");
        String minimumCapacityString = (String) insertGroupPropertiesForm.get("minimumCapacity");
        String idealCapacityString = (String) insertGroupPropertiesForm.get("idealCapacity");
        String groupMaximumNumber = (String) insertGroupPropertiesForm.get("groupMaximumNumber");
        String enrolmentBeginDayString = (String) insertGroupPropertiesForm.get("enrolmentBeginDay");
        String enrolmentBeginHourString = (String) insertGroupPropertiesForm.get("enrolmentBeginHour");
        String enrolmentEndDayString = (String) insertGroupPropertiesForm.get("enrolmentEndDay");
        String enrolmentEndHourString = (String) insertGroupPropertiesForm.get("enrolmentEndHour");
        String shiftType = (String) insertGroupPropertiesForm.get("shiftType");
        Boolean optional = new Boolean((String) insertGroupPropertiesForm.get("enrolmentPolicy"));
        Boolean automaticEnrolment = getAutomaticEnrolment(insertGroupPropertiesForm);
        Boolean differentiatedCapacity = getDifferentiatedCapacity(insertGroupPropertiesForm);

        final ArrayList<InfoShift> infoShifts = getRenderedObject("shiftsTable");

        InfoGrouping infoGroupProperties = new InfoGrouping();
        infoGroupProperties.setName(name);
        infoGroupProperties.setProjectDescription(projectDescription);

        if (!shiftType.equalsIgnoreCase("Sem Turno")) {
            infoGroupProperties.setShiftType(ShiftType.valueOf(shiftType));
        }

        if (differentiatedCapacity) {
            for (InfoShift info : infoShifts) {
                if (!maximumCapacityString.equals("")
                        && info.getGroupCapacity() * Integer.parseInt(maximumCapacityString) > info.getLotacao()) {
                    ActionErrors actionErrors = new ActionErrors();
                    ActionError error = null;
                    error = new ActionError("error.groupProperties.capacityOverflow");
                    actionErrors.add("error.groupProperties.capacityOverflow", error);
                    saveErrors(request, actionErrors);
                    return prepareCreateGroupProperties(mapping, form, request, response);
                }
            }
            infoGroupProperties.setInfoShifts(infoShifts);
        }

        Integer maximumCapacity = null;
        Integer minimumCapacity = null;
        Integer idealCapacity = null;

        if (!maximumCapacityString.equals("")) {
            maximumCapacity = new Integer(maximumCapacityString);
            infoGroupProperties.setMaximumCapacity(maximumCapacity);
        }

        if (!minimumCapacityString.equals("")) {
            minimumCapacity = new Integer(minimumCapacityString);
            if (maximumCapacity != null) {
                if (minimumCapacity.compareTo(maximumCapacity) > 0) {
                    ActionErrors actionErrors = new ActionErrors();
                    ActionError error = null;
                    error = new ActionError("error.groupProperties.minimum");
                    actionErrors.add("error.groupProperties.minimum", error);
                    saveErrors(request, actionErrors);
                    return prepareCreateGroupProperties(mapping, form, request, response);
                }
            }
            infoGroupProperties.setMinimumCapacity(minimumCapacity);
        }

        if (!idealCapacityString.equals("")) {

            idealCapacity = new Integer(idealCapacityString);

            if (!minimumCapacityString.equals("")) {
                if (idealCapacity.compareTo(minimumCapacity) < 0) {
                    ActionErrors actionErrors = new ActionErrors();
                    ActionError error = null;
                    error = new ActionError("error.groupProperties.ideal.minimum");
                    actionErrors.add("error.groupProperties.ideal.minimum", error);
                    saveErrors(request, actionErrors);
                    return prepareCreateGroupProperties(mapping, form, request, response);
                }
            }

            if (!maximumCapacityString.equals("")) {
                if (idealCapacity.compareTo(maximumCapacity) > 0) {
                    ActionErrors actionErrors = new ActionErrors();
                    ActionError error = null;
                    error = new ActionError("error.groupProperties.ideal.maximum");
                    actionErrors.add("error.groupProperties.ideal.maximum", error);
                    saveErrors(request, actionErrors);
                    return prepareCreateGroupProperties(mapping, form, request, response);
                }
            }
            infoGroupProperties.setIdealCapacity(idealCapacity);
        }

        if (!groupMaximumNumber.equals("")) {
            infoGroupProperties.setGroupMaximumNumber(new Integer(groupMaximumNumber));
        }

        infoGroupProperties.setDifferentiatedCapacity(differentiatedCapacity);

        EnrolmentGroupPolicyType enrolmentPolicy;
        if (optional.booleanValue()) {
            enrolmentPolicy = new EnrolmentGroupPolicyType(1);
        } else {
            enrolmentPolicy = new EnrolmentGroupPolicyType(2);
        }
        infoGroupProperties.setEnrolmentPolicy(enrolmentPolicy);
        infoGroupProperties.setAutomaticEnrolment(automaticEnrolment);
        Calendar enrolmentBeginDay = null;
        if (!enrolmentBeginDayString.equals("")) {
            String[] beginDate = enrolmentBeginDayString.split("/");
            enrolmentBeginDay = Calendar.getInstance();
            enrolmentBeginDay.set(Calendar.DAY_OF_MONTH, (new Integer(beginDate[0])).intValue());
            enrolmentBeginDay.set(Calendar.MONTH, (new Integer(beginDate[1])).intValue() - 1);
            enrolmentBeginDay.set(Calendar.YEAR, (new Integer(beginDate[2])).intValue());

            if (!enrolmentBeginHourString.equals("")) {
                String[] beginHour = enrolmentBeginHourString.split(":");
                enrolmentBeginDay.set(Calendar.HOUR_OF_DAY, (new Integer(beginHour[0])).intValue());
                enrolmentBeginDay.set(Calendar.MINUTE, (new Integer(beginHour[1])).intValue());
                enrolmentBeginDay.set(Calendar.SECOND, 0);
            }
        }

        infoGroupProperties.setEnrolmentBeginDay(enrolmentBeginDay);

        Calendar enrolmentEndDay = null;
        if (!enrolmentEndDayString.equals("")) {
            String[] endDate = enrolmentEndDayString.split("/");
            enrolmentEndDay = Calendar.getInstance();
            enrolmentEndDay.set(Calendar.DAY_OF_MONTH, (new Integer(endDate[0])).intValue());
            enrolmentEndDay.set(Calendar.MONTH, (new Integer(endDate[1])).intValue() - 1);
            enrolmentEndDay.set(Calendar.YEAR, (new Integer(endDate[2])).intValue());

            if (!enrolmentEndHourString.equals("")) {
                String[] endHour = enrolmentEndHourString.split(":");
                enrolmentEndDay.set(Calendar.HOUR_OF_DAY, (new Integer(endHour[0])).intValue());
                enrolmentEndDay.set(Calendar.MINUTE, (new Integer(endHour[1])).intValue());
                enrolmentEndDay.set(Calendar.SECOND, 0);
            }
        }

        float compareDate = enrolmentBeginDay.compareTo(enrolmentEndDay);

        if (compareDate >= 0.0) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.manager.wrongDates");
            actionErrors.add("error.manager.wrongDates", error);
            saveErrors(request, actionErrors);
            return prepareCreateGroupProperties(mapping, form, request, response);
        }

        infoGroupProperties.setEnrolmentEndDay(enrolmentEndDay);

        Integer objectCode = getObjectCode(request);
        try {
            CreateGrouping.runCreateGrouping(objectCode, infoGroupProperties);
        } catch (DomainException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = new ActionError("error.exception.existing.groupProperties");
            actionErrors.add("error.exception.existing.groupProperties", error);
            saveErrors(request, actionErrors);
            return prepareCreateGroupProperties(mapping, form, request, response);
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }
        return prepareViewExecutionCourseProjects(mapping, form, request, response);
    }

    public ActionForward prepareEditGroupProperties(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        InfoGrouping infoGroupProperties = getInfoGrouping(request);
        Integer enrolmentPolicy = infoGroupProperties.getEnrolmentPolicy().getType();
        DynaActionForm groupPropertiesForm = (DynaActionForm) form;

        request.setAttribute("infoGroupProperties", infoGroupProperties);
        request.setAttribute("enrolmentPolicyValue", enrolmentPolicy);

        if (infoGroupProperties == null) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noGroupProperties");
            actionErrors.add("error.noGroupProperties", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        }

        List<LabelValueBean> shiftTypeValues = getShiftTypeLabelValues(request, getDifferentiatedCapacity(groupPropertiesForm));
        request.setAttribute("shiftTypeValues", shiftTypeValues);

        List enrolmentPolicyValues = new ArrayList();
        enrolmentPolicyValues.add(new Integer(1));
        enrolmentPolicyValues.add(new Integer(2));

        List enrolmentPolicyNames = new ArrayList();
        enrolmentPolicyNames.add("Atómica");
        enrolmentPolicyNames.add("Individual");

        enrolmentPolicyValues.remove(enrolmentPolicy.intValue() - 1);
        String enrolmentPolicyName = enrolmentPolicyNames.remove(enrolmentPolicy.intValue() - 1).toString();

        request.setAttribute("enrolmentPolicyName", enrolmentPolicyName);
        request.setAttribute("enrolmentPolicyValues", enrolmentPolicyValues);
        request.setAttribute("enrolmentPolicyNames", enrolmentPolicyNames);

        final Grouping grouping = rootDomainObject.readGroupingByOID(infoGroupProperties.getIdInternal());

        String shiftType = (String) groupPropertiesForm.get("shiftType");
        if (getDifferentiatedCapacity(groupPropertiesForm) && shiftType.equalsIgnoreCase("Sem Turno")) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.groupProperties.DiffCapacityNoShift");
            actionErrors.add("error.groupProperties.DiffCapacityNoShift", error);
            saveErrors(request, actionErrors);
            infoGroupProperties.setDifferentiatedCapacity(Boolean.FALSE);
        } else if (infoGroupProperties.getDifferentiatedCapacity()) {
            Integer executionCourseCode = getObjectCode(request);
            ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseCode);
            List<InfoShift> shiftsList;
            if (!shiftType.isEmpty()) {
                shiftsList = InfoShift.getInfoShiftsByType(executionCourse, ShiftType.valueOf(shiftType));
            } else {
                shiftsList = InfoShift.getInfoShiftsByType(executionCourse, grouping.getShiftType());
            }
            request.setAttribute("shiftsList", shiftsList);
        }

        if (StringUtils.isEmpty(shiftType)) {
            if (grouping.getShiftType() != null) {
                groupPropertiesForm.set("shiftType", grouping.getShiftType().getName());
            } else {
                groupPropertiesForm.set("shiftType", "SEM TURNO");
            }
        }

        if (getDifferentiatedCapacity(groupPropertiesForm)) {
            request.setAttribute("automaticEnrolmentDisable", "true");
        }
        if (getAutomaticEnrolment(groupPropertiesForm)) {
            request.setAttribute("differentiatedCapacityDisable", "true");
        }

        if (!grouping.getStudentGroups().isEmpty() && !grouping.getAutomaticEnrolment()) {
            request.setAttribute("automaticEnrolmentDisable", "true");
        }
        if (!grouping.getStudentGroups().isEmpty() && !grouping.getDifferentiatedCapacity()) {
            request.setAttribute("differentiatedCapacityDisable", "true");
        }
        if (grouping.hasAnyStudentGroups() && grouping.getAutomaticEnrolment() && !infoGroupProperties.getAutomaticEnrolment()) {
            request.setAttribute("notPosibleToRevertChoice", "true");
        }
        if (grouping.hasAnyStudentGroups() && grouping.getDifferentiatedCapacity()
                && !infoGroupProperties.getDifferentiatedCapacity()) {
            request.setAttribute("notPosibleToRevertChoice", "true");
        }

        return mapping.findForward("editGroupProperties");
    }

    private InfoGrouping getInfoGrouping(HttpServletRequest request) throws FenixActionException {

        InfoGrouping infoGroupProperties =
                request.getAttribute("infoGroupProperties") != null ? (InfoGrouping) request.getAttribute("infoGroupProperties") : null;

        if (infoGroupProperties == null) {
            String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
            Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);
            ISiteComponent viewGroupProperties = new InfoSiteGrouping();
            SiteView siteView = readSiteView(request, viewGroupProperties, null, groupPropertiesCode, null);
            infoGroupProperties = ((InfoSiteGrouping) siteView.getComponent()).getInfoGrouping();
        }
        return infoGroupProperties;
    }

    public ActionForward editGroupPropertiesPostBack(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        InfoGrouping infoGroupProperties = getInfoGrouping(request);
        request.setAttribute("infoGroupProperties", infoGroupProperties);

        DynaActionForm groupPropertiesForm = setAutomaticEnrolmentFields(form, request, infoGroupProperties);

        return prepareEditGroupProperties(mapping, groupPropertiesForm, request, response);
    }

    public ActionForward editGroupCapacityPropertiesPostBack(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        InfoGrouping infoGroupProperties = getInfoGrouping(request);
        request.setAttribute("infoGroupProperties", infoGroupProperties);

        DynaActionForm groupPropertiesForm = setDifferentiatedCapacity(form, request, infoGroupProperties);
        return prepareEditGroupProperties(mapping, groupPropertiesForm, request, response);
    }

    public ActionForward editGroupProperties(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        DynaActionForm editGroupPropertiesForm = (DynaActionForm) form;
        String groupPropertiesString = request.getParameter("groupPropertiesCode");
        Integer groupPropertiesCode = new Integer(groupPropertiesString);
        String name = (String) editGroupPropertiesForm.get("name");
        String projectDescription = (String) editGroupPropertiesForm.get("projectDescription");
        String maximumCapacityString = (String) editGroupPropertiesForm.get("maximumCapacity");
        String minimumCapacityString = (String) editGroupPropertiesForm.get("minimumCapacity");
        String idealCapacityString = (String) editGroupPropertiesForm.get("idealCapacity");

        String groupMaximumNumber = (String) editGroupPropertiesForm.get("groupMaximumNumber");
        String enrolmentBeginDayString = (String) editGroupPropertiesForm.get("enrolmentBeginDayFormatted");
        String enrolmentBeginHourString = (String) editGroupPropertiesForm.get("enrolmentBeginHourFormatted");
        String enrolmentEndDayString = (String) editGroupPropertiesForm.get("enrolmentEndDayFormatted");
        String enrolmentEndHourString = (String) editGroupPropertiesForm.get("enrolmentEndHourFormatted");
        String shiftType = (String) editGroupPropertiesForm.get("shiftType");
        String enrolmentPolicy = (String) editGroupPropertiesForm.get("enrolmentPolicy");
        Boolean automaticEnrolment = getAutomaticEnrolment(editGroupPropertiesForm);
        Boolean differentiatedCapacity = getDifferentiatedCapacity(editGroupPropertiesForm);

        final ArrayList<InfoShift> infoShifts = getRenderedObject("shiftsTable");

        Calendar enrolmentBeginDay = null;
        if (!enrolmentBeginDayString.equals("")) {
            String[] beginDate = enrolmentBeginDayString.split("/");
            enrolmentBeginDay = Calendar.getInstance();
            enrolmentBeginDay.set(Calendar.DAY_OF_MONTH, (new Integer(beginDate[0])).intValue());
            enrolmentBeginDay.set(Calendar.MONTH, (new Integer(beginDate[1])).intValue() - 1);
            enrolmentBeginDay.set(Calendar.YEAR, (new Integer(beginDate[2])).intValue());

            if (!enrolmentBeginHourString.equals("")) {
                String[] beginHour = enrolmentBeginHourString.split(":");
                enrolmentBeginDay.set(Calendar.HOUR_OF_DAY, (new Integer(beginHour[0])).intValue());
                enrolmentBeginDay.set(Calendar.MINUTE, (new Integer(beginHour[1])).intValue());
                enrolmentBeginDay.set(Calendar.SECOND, 0);
            }
        }
        Calendar enrolmentEndDay = null;
        if (!enrolmentEndDayString.equals("")) {
            String[] endDate = enrolmentEndDayString.split("/");
            enrolmentEndDay = Calendar.getInstance();
            enrolmentEndDay.set(Calendar.DAY_OF_MONTH, (new Integer(endDate[0])).intValue());
            enrolmentEndDay.set(Calendar.MONTH, (new Integer(endDate[1])).intValue() - 1);
            enrolmentEndDay.set(Calendar.YEAR, (new Integer(endDate[2])).intValue());

            if (!enrolmentEndHourString.equals("")) {
                String[] endHour = enrolmentEndHourString.split(":");
                enrolmentEndDay.set(Calendar.HOUR_OF_DAY, (new Integer(endHour[0])).intValue());
                enrolmentEndDay.set(Calendar.MINUTE, (new Integer(endHour[1])).intValue());
                enrolmentEndDay.set(Calendar.SECOND, 0);
            }
        }

        float compareDate = enrolmentBeginDay.compareTo(enrolmentEndDay);

        if (compareDate >= 0.0) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.manager.wrongDates");
            actionErrors.add("error.manager.wrongDates", error);
            saveErrors(request, actionErrors);
            return prepareEditGroupProperties(mapping, form, request, response);
        }

        InfoGrouping infoGroupProperties = new InfoGrouping();
        infoGroupProperties.setIdInternal(groupPropertiesCode);
        infoGroupProperties.setEnrolmentBeginDay(enrolmentBeginDay);
        infoGroupProperties.setEnrolmentEndDay(enrolmentEndDay);
        infoGroupProperties.setEnrolmentPolicy(new EnrolmentGroupPolicyType(new Integer(enrolmentPolicy)));
        infoGroupProperties.setAutomaticEnrolment(automaticEnrolment);
        infoGroupProperties.setDifferentiatedCapacity(differentiatedCapacity);

        if (differentiatedCapacity) {
            for (InfoShift info : infoShifts) {
                if (!maximumCapacityString.equals("")
                        && info.getGroupCapacity() * Integer.parseInt(maximumCapacityString) > info.getLotacao()) {
                    ActionErrors actionErrors = new ActionErrors();
                    ActionError error = null;
                    error = new ActionError("error.groupProperties.capacityOverflow");
                    actionErrors.add("error.groupProperties.capacityOverflow", error);
                    saveErrors(request, actionErrors);
                    return prepareEditGroupProperties(mapping, form, request, response);
                }
            }
            infoGroupProperties.setInfoShifts(infoShifts);
        }

        infoGroupProperties.setDifferentiatedCapacity(differentiatedCapacity);

        if (!groupMaximumNumber.equals("")) {
            infoGroupProperties.setGroupMaximumNumber(new Integer(groupMaximumNumber));
        }
        Integer maximumCapacity = null;
        Integer minimumCapacity = null;
        Integer idealCapacity = null;

        if (!maximumCapacityString.equals("")) {
            maximumCapacity = new Integer(maximumCapacityString);
            infoGroupProperties.setMaximumCapacity(maximumCapacity);
        }

        if (!minimumCapacityString.equals("")) {
            minimumCapacity = new Integer(minimumCapacityString);
            if (maximumCapacity != null) {
                if (minimumCapacity.compareTo(maximumCapacity) > 0) {
                    ActionErrors actionErrors = new ActionErrors();
                    ActionError error = null;
                    error = new ActionError("error.groupProperties.minimum");
                    actionErrors.add("error.groupProperties.minimum", error);
                    saveErrors(request, actionErrors);
                    return prepareEditGroupProperties(mapping, form, request, response);

                }
            }
            infoGroupProperties.setMinimumCapacity(minimumCapacity);
        }

        if (!idealCapacityString.equals("")) {

            idealCapacity = new Integer(idealCapacityString);

            if (!minimumCapacityString.equals("")) {
                if (idealCapacity.compareTo(minimumCapacity) < 0) {
                    ActionErrors actionErrors = new ActionErrors();
                    ActionError error = null;
                    error = new ActionError("error.groupProperties.ideal.minimum");
                    actionErrors.add("error.groupProperties.ideal.minimum", error);
                    saveErrors(request, actionErrors);
                    return prepareEditGroupProperties(mapping, form, request, response);

                }
            }

            if (!maximumCapacityString.equals("")) {
                if (idealCapacity.compareTo(maximumCapacity) > 0) {
                    ActionErrors actionErrors = new ActionErrors();
                    ActionError error = null;
                    error = new ActionError("error.groupProperties.ideal.maximum");
                    actionErrors.add("error.groupProperties.ideal.maximum", error);
                    saveErrors(request, actionErrors);
                    return prepareEditGroupProperties(mapping, form, request, response);

                }
            }

            infoGroupProperties.setIdealCapacity(idealCapacity);
        }

        infoGroupProperties.setName(name);
        infoGroupProperties.setProjectDescription(projectDescription);

        if (!shiftType.equalsIgnoreCase("Sem Turno")) {
            infoGroupProperties.setShiftType(ShiftType.valueOf(shiftType));
        }
        Integer objectCode = getObjectCode(request);
        List errors = new ArrayList();
        try {
            errors = EditGrouping.runEditGrouping(objectCode, infoGroupProperties);
        } catch (InvalidSituationServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noGroupProperties");
            actionErrors.add("error.noGroupProperties", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (DomainException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError(e.getArgs()[0]);
            actionErrors.add("error.noGroupProperties", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        if (errors.size() != 0) {
            ActionErrors actionErrors = new ActionErrors();

            Iterator iterErrors = errors.iterator();
            ActionError errorInt = null;
            errorInt = new ActionError("error.exception.editGroupProperties");
            actionErrors.add("error.exception.editGroupProperties", errorInt);
            while (iterErrors.hasNext()) {
                Integer intError = (Integer) iterErrors.next();

                if (intError.equals(Integer.valueOf(-1))) {
                    ActionError error = null;
                    error = new ActionError("error.exception.nrOfGroups.editGroupProperties");
                    actionErrors.add("error.exception.nrOfGroups.editGroupProperties", error);
                }
                if (intError.equals(Integer.valueOf(-2))) {
                    ActionError error = null;
                    error = new ActionError("error.exception.maximumCapacity.editGroupProperties");
                    actionErrors.add("error.exception.maximumCapacity.editGroupProperties", error);
                }
                if (intError.equals(Integer.valueOf(-3))) {
                    ActionError error = null;
                    error = new ActionError("error.exception.minimumCapacity.editGroupProperties");
                    actionErrors.add("error.exception.minimumCapacity.editGroupProperties", error);
                }
            }
            saveErrors(request, actionErrors);

        }
        return prepareViewExecutionCourseProjects(mapping, form, request, response);
    }

    private Boolean getAutomaticEnrolment(DynaActionForm form) {
        Boolean automaticEnrolment = (Boolean) form.get("automaticEnrolment");
        if (automaticEnrolment == null) {
            return Boolean.FALSE;
        }
        return automaticEnrolment;
    }

    private Boolean getDifferentiatedCapacity(DynaActionForm form) {
        Boolean differenciatedCapacity = (Boolean) form.get("differentiatedCapacity");
        if (differenciatedCapacity == null) {
            return Boolean.FALSE;
        }
        return differenciatedCapacity;
    }

    public ActionForward deleteGroupProperties(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        ActionErrors actionErrors = new ActionErrors();

        Integer objectCode = getObjectCode(request);

        Integer groupPropertiesCode = null;
        try {
            String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
            groupPropertiesCode = new Integer(groupPropertiesCodeString);
        } catch (Exception e) {
            e.printStackTrace();

            actionErrors.add("errors.delete.groupPropertie", new ActionError(e.getMessage()));
            saveErrors(request, actionErrors);

            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        }

        Boolean result = Boolean.FALSE;
        try {
            result = DeleteGrouping.runDeleteGrouping(objectCode, groupPropertiesCode);
        } catch (ExistingServiceException e) {
            ActionErrors actionErrors1 = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noGroupProperties");
            actionErrors1.add("error.noGroupProperties", error);
            saveErrors(request, actionErrors1);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (InvalidSituationServiceException e) {
            ActionErrors actionErrors2 = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.groupProperties.delete.attendsSet.withGroups");
            actionErrors2.add("error.groupProperties.delete.attendsSet.withGroups", error);
            saveErrors(request, actionErrors2);
            return viewShiftsAndGroups(mapping, form, request, response);
        } catch (FenixServiceException e) {
            e.printStackTrace();
            throw new FenixActionException(e.getMessage());
        }
        if (result.equals(Boolean.FALSE)) {
            actionErrors.add("errors.delete.groupPropertie", new ActionError("error.groupProperties.delete"));
            saveErrors(request, actionErrors);

            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        }

        return prepareViewExecutionCourseProjects(mapping, form, request, response);
    }

    public ActionForward prepareImportGroupProperties(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);
        Integer objectCode = getObjectCode(request);
        ISiteComponent viewGroupProperties = new InfoSiteGrouping();
        SiteView siteView = readSiteView(request, viewGroupProperties, null, groupPropertiesCode, null);

        if (((InfoSiteGrouping) siteView.getComponent()).getInfoGrouping() == null) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noGroupProperties.ProjectProposal");
            actionErrors.add("error.noGroupProperties.ProjectProposal", error);
            saveErrors(request, actionErrors);
            return viewNewProjectProposals(mapping, form, request, response);
        }
        Boolean result;
        try {
            result =
                    VerifyIfGroupPropertiesHasProjectProposal.runVerifyIfGroupPropertiesHasProjectProposal(objectCode,
                            groupPropertiesCode);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        if (!result.booleanValue()) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noProjectProposal");
            actionErrors.add("error.noProjectProposal", error);
            saveErrors(request, actionErrors);
            return viewNewProjectProposals(mapping, form, request, response);
        }

        String enrolmentPolicyName =
                ((InfoSiteGrouping) siteView.getComponent()).getInfoGrouping().getEnrolmentPolicy().getTypeFullName();
        request.setAttribute("enrolmentPolicyName", enrolmentPolicyName);

        ShiftType shiftType = ((InfoSiteGrouping) siteView.getComponent()).getInfoGrouping().getShiftType();
        String shiftTypeName = "Sem Turno";
        if (shiftType != null) {
            shiftTypeName = shiftType.getFullNameTipoAula();
        }

        request.setAttribute("shiftTypeName", shiftTypeName);

        return mapping.findForward("importGroupProperties");
    }

    public ActionForward deleteStudentGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        Integer objectCode = getObjectCode(request);
        String studentGroupCodeString = request.getParameter("studentGroupCode");
        Integer studentGroupCode = new Integer(studentGroupCodeString);
        try {
            DeleteStudentGroup.runDeleteStudentGroup(objectCode, studentGroupCode);
        } catch (ExistingServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noGroup");
            actionErrors.add("error.noGroup", error);
            saveErrors(request, actionErrors);
            return viewShiftsAndGroups(mapping, form, request, response);
        } catch (InvalidSituationServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("errors.invalid.delete.not.empty.studentGroup");
            actionErrors.add("errors.invalid.delete.not.empty.studentGroup", error);
            saveErrors(request, actionErrors);
            return viewStudentGroupInformation(mapping, form, request, response);
        } catch (DomainException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("errors.invalid.delete.not.empty.studentGroup");
            actionErrors.add("errors.invalid.delete.not.empty.studentGroup", error);
            saveErrors(request, actionErrors);
            return viewStudentGroupInformation(mapping, form, request, response);
        } catch (FenixServiceException e) {
            e.printStackTrace();
            throw new FenixActionException(e.getMessage());
        }

        return viewShiftsAndGroups(mapping, form, request, response);
    }

    public ActionForward prepareCreateStudentGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        Integer objectCode = getObjectCode(request);
        String groupPropertiesString = request.getParameter("groupPropertiesCode");
        Integer groupPropertiesCode = new Integer(groupPropertiesString);

        String shiftCodeString = request.getParameter("shiftCode");
        request.setAttribute("shiftCode", shiftCodeString);

        if (request.getParameter("showPhotos") == null) {
            request.setAttribute("showPhotos", "false");
        }

        InfoSiteStudentGroup infoSiteStudentGroup;
        try {
            infoSiteStudentGroup =
                    (InfoSiteStudentGroup) PrepareCreateStudentGroup
                            .runPrepareCreateStudentGroup(objectCode, groupPropertiesCode);
        } catch (ExistingServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noGroupProperties");
            actionErrors.add("error.noGroupProperties", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("infoSiteStudentGroup", infoSiteStudentGroup);
        readSiteView(request, null, null, null, null);
        return mapping.findForward("insertStudentGroup");
    }

    public ActionForward createStudentGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        Integer objectCode = getObjectCode(request);

        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);

        String shiftCodeString = request.getParameter("shiftCode");
        Integer shiftCode = null;
        if (shiftCodeString != null) {
            shiftCode = new Integer(shiftCodeString);
        }

        DynaActionForm insertStudentGroupForm = (DynaActionForm) form;

        List studentCodes = Arrays.asList((String[]) insertStudentGroupForm.get("studentCodes"));

        String groupNumberString = (String) insertStudentGroupForm.get("nrOfElements");
        Integer groupNumber = new Integer(groupNumberString);

        try {
            CreateStudentGroup.runCreateStudentGroup(objectCode, groupNumber, groupPropertiesCode, shiftCode, studentCodes);

        } catch (DomainException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = new ActionError(e.getArgs()[0]);
            actionErrors.add("error.invalidNumberOfStudentsGroups", error);
            saveErrors(request, actionErrors);
            return prepareCreateStudentGroup(mapping, form, request, response);

        } catch (NotAuthorizedException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noGroupProperties");
            actionErrors.add("error.noGroupProperties", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);

        } catch (InvalidSituationServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = new ActionError("errors.existing.studentEnrolment");
            actionErrors.add("errors.existing.studentEnrolment", error);
            saveErrors(request, actionErrors);
            return prepareCreateStudentGroup(mapping, form, request, response);

        } catch (InvalidArgumentsServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = new ActionError("errors.notExisting.studentInAttendsSetToCreateStudentGroup");
            actionErrors.add("errors.notExisting.studentInAttendsSetToCreateStudentGroup", error);
            saveErrors(request, actionErrors);
            return prepareCreateStudentGroup(mapping, form, request, response);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return viewShiftsAndGroups(mapping, form, request, response);

    }

    public ActionForward viewStudentsAndGroupsByShift(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        Integer objectCode = getObjectCode(request);

        String groupPropertiesString = request.getParameter("groupPropertiesCode");
        Integer groupPropertiesCode = new Integer(groupPropertiesString);
        String shiftCodeString = request.getParameter("shiftCode");
        Integer shiftCode = new Integer(shiftCodeString);

        InfoSiteStudentsAndGroups infoSiteStudentsAndGroups = new InfoSiteStudentsAndGroups();
        Boolean type = null;

        if (request.getParameter("showPhotos") == null) {
            request.setAttribute("showPhotos", "false");
        }

        try {
            infoSiteStudentsAndGroups = ReadStudentsAndGroupsByShiftID.run(groupPropertiesCode, shiftCode);

            type =
                    VerifyIfCanEnrollStudentGroupsInShift.runVerifyIfCanEnrollStudentGroupsInShift(objectCode,
                            groupPropertiesCode, shiftCode);

        } catch (ExistingServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noGroupProperties");
            actionErrors.add("error.noGroupProperties", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        readSiteView(request, null, null, null, null);
        request.setAttribute("infoSiteStudentsAndGroups", infoSiteStudentsAndGroups);

        if (type.booleanValue() == true) {
            request.setAttribute("type", new Boolean(true));
        } else {
            request.setAttribute("type", new Boolean(false));
        }
        return mapping.findForward("viewStudentsAndGroupsByShift");
    }

    public ActionForward viewStudentsAndGroupsWithoutShift(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        String groupPropertiesString = request.getParameter("groupPropertiesCode");
        Integer groupPropertiesCode = new Integer(groupPropertiesString);

        InfoSiteStudentsAndGroups infoSiteStudentsAndGroups = new InfoSiteStudentsAndGroups();

        if (request.getParameter("showPhotos") == null) {
            request.setAttribute("showPhotos", "false");
        }

        try {
            infoSiteStudentsAndGroups = ReadStudentsAndGroupsWithoutShift.run(groupPropertiesCode);
        } catch (ExistingServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noGroupProperties");
            actionErrors.add("error.noGroupProperties", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        readSiteView(request, null, null, null, null);
        request.setAttribute("infoSiteStudentsAndGroups", infoSiteStudentsAndGroups);
        return mapping.findForward("viewStudentsAndGroupsWithoutShift");
    }

    public ActionForward viewAllStudentsAndGroups(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        String groupPropertiesString = request.getParameter("groupPropertiesCode");
        Integer groupPropertiesCode = new Integer(groupPropertiesString);

        if (request.getParameter("showPhotos") == null) {
            request.setAttribute("showPhotos", "false");
        }

        InfoSiteStudentsAndGroups infoSiteStudentsAndGroups = new InfoSiteStudentsAndGroups();

        try {
            infoSiteStudentsAndGroups = ReadAllStudentsAndGroups.run(groupPropertiesCode);
        } catch (ExistingServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noGroupProperties");
            actionErrors.add("error.noGroupProperties", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        readSiteView(request, null, null, null, null);
        request.setAttribute("infoSiteStudentsAndGroups", infoSiteStudentsAndGroups);
        return mapping.findForward("viewAllStudentsAndGroups");
    }

    public ActionForward prepareEditStudentGroupShift(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        String studentGroupCodeString = request.getParameter("studentGroupCode");
        String shiftCodeString = request.getParameter("shiftCode");
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");

        Integer studentGroupCode = new Integer(studentGroupCodeString);
        Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);
        Integer shiftCode = new Integer(shiftCodeString);
        ISiteComponent viewShifts = new InfoSiteShifts();
        TeacherAdministrationSiteView shiftsView =
                (TeacherAdministrationSiteView) readSiteView(request, viewShifts, null, groupPropertiesCode, studentGroupCode);
        if (((InfoSiteShifts) shiftsView.getComponent()) == null) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noGroupProperties");
            actionErrors.add("error.noGroupProperties", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        }

        List shifts = ((InfoSiteShifts) shiftsView.getComponent()).getShifts();
        if (shifts == null) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noGroup");
            actionErrors.add("error.noGroup", error);
            saveErrors(request, actionErrors);
            return viewShiftsAndGroups(mapping, form, request, response);
        }
        if (shifts.size() == 0) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("errors.shifts.not.available");
            actionErrors.add("errors.shifts.not.available", error);
            saveErrors(request, actionErrors);
            return viewShiftsAndGroups(mapping, form, request, response);
        }

        ArrayList shiftsList = new ArrayList();
        InfoShift oldInfoShift = ((InfoSiteShifts) shiftsView.getComponent()).getOldShift();

        if (shifts.size() != 0) {
            shiftsList.add(new LabelValueBean("(escolher)", ""));
            InfoShift infoShift;
            Iterator iter = shifts.iterator();
            String label, value;
            while (iter.hasNext()) {
                infoShift = (InfoShift) iter.next();
                value = infoShift.getIdInternal().toString();
                label = infoShift.getNome();
                shiftsList.add(new LabelValueBean(label, value));
            }
            request.setAttribute("shiftsList", shiftsList);
        }
        if (shiftCode != null) {
            request.setAttribute("shift", oldInfoShift);
        }
        return mapping.findForward("editStudentGroupShift");
    }

    public ActionForward editStudentGroupShift(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        Integer objectCode = getObjectCode(request);
        DynaActionForm editStudentGroupForm = (DynaActionForm) form;
        String oldShiftString = request.getParameter("shiftCode");
        String studentGroupCodeString = request.getParameter("studentGroupCode");
        Integer studentGroupCode = new Integer(studentGroupCodeString);
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);
        String newShiftString = (String) editStudentGroupForm.get("shift");
        if (newShiftString.equals(oldShiftString)) {
            return viewShiftsAndGroups(mapping, form, request, response);
        } else if (newShiftString.equals("")) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("errors.invalid.insert.studentGroupShift");
            actionErrors.add("errors.invalid.insert.studentGroupShift", error);
            saveErrors(request, actionErrors);
            return prepareEditStudentGroupShift(mapping, form, request, response);
        } else {

            Integer newShiftCode = new Integer(newShiftString);
            try {
                EditStudentGroupShift.runEditStudentGroupShift(objectCode, studentGroupCode, groupPropertiesCode, newShiftCode);
            } catch (ExistingServiceException e) {
                ActionErrors actionErrors = new ActionErrors();
                ActionError error = null;
                error = new ActionError("error.noGroupProperties");
                actionErrors.add("error.noGroupProperties", error);
                saveErrors(request, actionErrors);
                return prepareViewExecutionCourseProjects(mapping, form, request, response);

            } catch (InvalidArgumentsServiceException e) {
                ActionErrors actionErrors = new ActionErrors();
                ActionError error = null;
                error = new ActionError("error.noGroup");
                actionErrors.add("error.noGroup", error);
                saveErrors(request, actionErrors);
                return viewShiftsAndGroups(mapping, form, request, response);

            } catch (DomainException e) {
                ActionErrors actionErrors = new ActionErrors();
                ActionError error = null;
                error = new ActionError(e.getArgs()[0]);
                actionErrors.add("error.noGroup", error);
                saveErrors(request, actionErrors);
                return viewShiftsAndGroups(mapping, form, request, response);

            } catch (InvalidChangeServiceException e) {
                ActionErrors actionErrors = new ActionErrors();
                ActionError error = null;
                error = new ActionError("error.GroupPropertiesShiftTypeChanged");
                actionErrors.add("error.GroupPropertiesShiftTypeChanged", error);
                saveErrors(request, actionErrors);
                return viewShiftsAndGroups(mapping, form, request, response);

            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }

            return viewShiftsAndGroups(mapping, form, request, response);
        }
    }

    // ////////////////
    public ActionForward prepareEnrollStudentGroupShift(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        String studentGroupCodeString = request.getParameter("studentGroupCode");
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");

        Integer studentGroupCode = new Integer(studentGroupCodeString);
        Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);

        ISiteComponent viewShifts = new InfoSiteShifts();
        TeacherAdministrationSiteView shiftsView =
                (TeacherAdministrationSiteView) readSiteView(request, viewShifts, null, groupPropertiesCode, studentGroupCode);
        if (((InfoSiteShifts) shiftsView.getComponent()) == null) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noGroupProperties");
            actionErrors.add("error.noGroupProperties", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        }

        List shifts = ((InfoSiteShifts) shiftsView.getComponent()).getShifts();
        if (shifts == null) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noGroup");
            actionErrors.add("error.noGroup", error);
            saveErrors(request, actionErrors);
            return viewShiftsAndGroups(mapping, form, request, response);
        }
        if (shifts.size() == 0) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("errors.shifts.not.available");
            actionErrors.add("errors.shifts.not.available", error);
            saveErrors(request, actionErrors);
            return viewShiftsAndGroups(mapping, form, request, response);
        }

        ArrayList shiftsList = new ArrayList();

        if (shifts.size() != 0) {
            shiftsList.add(new LabelValueBean("(escolher)", ""));
            InfoShift infoShift;
            Iterator iter = shifts.iterator();
            String label, value;
            while (iter.hasNext()) {
                infoShift = (InfoShift) iter.next();
                value = infoShift.getIdInternal().toString();
                label = infoShift.getNome();
                shiftsList.add(new LabelValueBean(label, value));
            }
            request.setAttribute("shiftsList", shiftsList);
        }

        return mapping.findForward("enrollStudentGroupShift");
    }

    // //////////////

    public ActionForward enrollStudentGroupShift(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        Integer objectCode = getObjectCode(request);
        DynaActionForm enrollStudentGroupForm = (DynaActionForm) form;
        String studentGroupCodeString = request.getParameter("studentGroupCode");
        Integer studentGroupCode = new Integer(studentGroupCodeString);
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);
        String newShiftString = (String) enrollStudentGroupForm.get("shift");
        if (newShiftString.equals("")) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("errors.invalid.insert.studentGroupShift");
            actionErrors.add("errors.invalid.insert.studentGroupShift", error);
            saveErrors(request, actionErrors);
            return prepareEnrollStudentGroupShift(mapping, form, request, response);
        }
        Integer newShiftCode = new Integer(newShiftString);

        try {
            EnrollStudentGroupShift.runEnrollStudentGroupShift(objectCode, studentGroupCode, groupPropertiesCode, newShiftCode);
        } catch (ExistingServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noGroupProperties");
            actionErrors.add("error.noGroupProperties", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (InvalidArgumentsServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noGroup");
            actionErrors.add("error.noGroup", error);
            saveErrors(request, actionErrors);
            return viewShiftsAndGroups(mapping, form, request, response);

        } catch (InvalidSituationServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noShift");
            actionErrors.add("error.noShift", error);
            saveErrors(request, actionErrors);
            return viewShiftsAndGroups(mapping, form, request, response);

        } catch (InvalidChangeServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.enrollStudentGroupShift");
            actionErrors.add("error.enrollStudentGroupShift", error);
            saveErrors(request, actionErrors);
            return viewShiftsAndGroups(mapping, form, request, response);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return viewShiftsAndGroups(mapping, form, request, response);
    }

    // ////////////////////
    public ActionForward unEnrollStudentGroupShift(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        Integer objectCode = getObjectCode(request);
        String studentGroupCodeString = request.getParameter("studentGroupCode");
        Integer studentGroupCode = new Integer(studentGroupCodeString);
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);

        try {
            UnEnrollStudentGroupShift.runUnEnrollStudentGroupShift(objectCode, studentGroupCode, groupPropertiesCode);
        } catch (ExistingServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noGroupProperties");
            actionErrors.add("error.noGroupProperties", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (InvalidArgumentsServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noGroup");
            actionErrors.add("error.noGroup", error);
            saveErrors(request, actionErrors);
            return viewShiftsAndGroups(mapping, form, request, response);
        } catch (InvalidChangeServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.UnEnrollStudentGroupShift");
            actionErrors.add("error.UnEnrollStudentGroupShift", error);
            saveErrors(request, actionErrors);
            return viewShiftsAndGroups(mapping, form, request, response);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return viewShiftsAndGroups(mapping, form, request, response);
    }

    public ActionForward prepareEditStudentGroupMembers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        String studentGroupCodeString = request.getParameter("studentGroupCode");
        Integer studentGroupCode = new Integer(studentGroupCodeString);

        String shiftCodeString = request.getParameter("shiftCode");
        request.setAttribute("shiftCode", shiftCodeString);
        Integer objectCode = getObjectCode(request);

        ISiteComponent viewStudentGroup = new InfoSiteStudentGroup();
        TeacherAdministrationSiteView siteView =
                (TeacherAdministrationSiteView) readSiteView(request, viewStudentGroup, null, studentGroupCode, null);
        InfoSiteStudentGroup component = (InfoSiteStudentGroup) siteView.getComponent();

        if (component.getInfoSiteStudentInformationList() == null) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noGroup");
            actionErrors.add("error.noGroup", error);
            saveErrors(request, actionErrors);
            return viewShiftsAndGroups(mapping, form, request, response);
        }

        List infoStudentList = null;
        try {
            infoStudentList = PrepareEditStudentGroupMembers.runPrepareEditStudentGroupMembers(objectCode, studentGroupCode);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        Collections.sort(infoStudentList, new BeanComparator("number"));
        request.setAttribute("infoStudentList", infoStudentList);
        return mapping.findForward("editStudentGroupMembers");
    }

    public ActionForward prepareEditStudentGroupsShift(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        Integer groupProperties = new Integer(groupPropertiesCodeString);
        String shiftCodeString = request.getParameter("shiftCode");
        Integer shiftCode = new Integer(shiftCodeString);

        ISiteComponent infoSiteStudentGroupAndStudents = new InfoSiteStudentGroupAndStudents();
        TeacherAdministrationSiteView siteView =
                (TeacherAdministrationSiteView) readSiteView(request, infoSiteStudentGroupAndStudents, null, groupProperties,
                        shiftCode);

        InfoSiteStudentGroupAndStudents component = (InfoSiteStudentGroupAndStudents) siteView.getComponent();

        if (component.getInfoSiteStudentsAndShiftByStudentGroupList() == null) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noGroup");
            actionErrors.add("error.noGroup", error);
            saveErrors(request, actionErrors);
            return viewShiftsAndGroups(mapping, form, request, response);
        }
        return mapping.findForward("editStudentGroupsShift");
    }

    public ActionForward editStudentGroupsShift(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        Integer objectCode = getObjectCode(request);
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);
        String shiftCodeString = request.getParameter("shiftCode");
        Integer shiftCode = new Integer(shiftCodeString);

        DynaActionForm editStudentGroupsShiftForm = (DynaActionForm) form;
        List studentGroupsCodes = Arrays.asList((Integer[]) editStudentGroupsShiftForm.get("studentGroupsCodes"));

        try {
            EditStudentGroupsShift.runEditStudentGroupsShift(objectCode, groupPropertiesCode, shiftCode, studentGroupsCodes);
        } catch (ExistingServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noGroupProperties");
            actionErrors.add("error.noGroupProperties", error);
            saveErrors(request, actionErrors);
            prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (DomainException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError(e.getArgs()[0]);
            actionErrors.add("error.noGroupProperties", error);
            saveErrors(request, actionErrors);
            prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (InvalidChangeServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noShift");
            actionErrors.add("error.noShift", error);
            saveErrors(request, actionErrors);
            return viewShiftsAndGroups(mapping, form, request, response);
        } catch (InvalidSituationServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError(e.getMessage());
            actionErrors.add("error.studentGroupNotInList", error);
            saveErrors(request, actionErrors);
            return prepareEditStudentGroupsShift(mapping, form, request, response);
        } catch (InvalidArgumentsServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.studentGroupNotFromGroupProperties");
            actionErrors.add("error.studentGroupNotFromGroupProperties", error);
            saveErrors(request, actionErrors);
            return prepareEditStudentGroupsShift(mapping, form, request, response);
        } catch (NonValidChangeServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.studentGroupAndGroupPropertiesDifferentShiftTypes");
            actionErrors.add("error.studentGroupAndGroupPropertiesDifferentShiftTypes", error);
            saveErrors(request, actionErrors);
            return viewStudentsAndGroupsByShift(mapping, form, request, response);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return prepareEditStudentGroupsShift(mapping, form, request, response);
    }

    public ActionForward insertStudentGroupMembers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        Integer objectCode = getObjectCode(request);
        String studentGroupCodeString = request.getParameter("studentGroupCode");
        Integer studentGroupCode = new Integer(studentGroupCodeString);
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);
        DynaActionForm insertStudentGroupForm = (DynaActionForm) form;
        List studentUsernames = Arrays.asList((String[]) insertStudentGroupForm.get("studentsToInsert"));

        try {
            InsertStudentGroupMembers.runInsertStudentGroupMembers(objectCode, studentGroupCode, groupPropertiesCode,
                    studentUsernames);
        } catch (ExistingServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = new ActionError("error.noGroup");
            actionErrors.add("error.noGroup", error);
            saveErrors(request, actionErrors);
            return viewShiftsAndGroups(mapping, form, request, response);
        } catch (InvalidChangeServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = new ActionError("error.insertStudentGroupMembers.AttendsSet");
            actionErrors.add("error.insertStudentGroupMembers.AttendsSet", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (InvalidSituationServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = new ActionError("errors.existing.studentInGroup");
            actionErrors.add("errors.existing.studentInGroup", error);
            saveErrors(request, actionErrors);
            return prepareEditStudentGroupMembers(mapping, form, request, response);
        } catch (InvalidArgumentsServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = new ActionError(e.getMessage());
            actionErrors.add("error.editStudentGroupMembers", error);
            saveErrors(request, actionErrors);
            return prepareEditStudentGroupMembers(mapping, form, request, response);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return prepareEditStudentGroupMembers(mapping, form, request, response);
    }

    public ActionForward deleteStudentGroupMembers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        Integer objectCode = getObjectCode(request);
        String studentGroupCodeString = request.getParameter("studentGroupCode");
        Integer studentGroupCode = new Integer(studentGroupCodeString);
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);
        DynaActionForm deleteStudentGroupForm = (DynaActionForm) form;
        List studentUsernames = Arrays.asList((String[]) deleteStudentGroupForm.get("studentsToRemove"));

        try {
            DeleteStudentGroupMembers.runDeleteStudentGroupMembers(objectCode, studentGroupCode, groupPropertiesCode,
                    studentUsernames);
        } catch (ExistingServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noGroup");
            actionErrors.add("error.noGroup", error);
            saveErrors(request, actionErrors);
            return viewShiftsAndGroups(mapping, form, request, response);
        } catch (InvalidChangeServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.deleteStudentGroupMembers.AttendsSet");
            actionErrors.add("error.deleteStudentGroupMembers.AttendsSet", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (InvalidSituationServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("errors.notExisting.studentInGroup");
            actionErrors.add("errors.notExisting.studentInGroup", error);
            saveErrors(request, actionErrors);
            return prepareEditStudentGroupMembers(mapping, form, request, response);
        } catch (InvalidArgumentsServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("errors.notExisting.studentInAttendsSet");
            actionErrors.add("errors.notExisting.studentInAttendsSet", error);
            saveErrors(request, actionErrors);
            return prepareEditStudentGroupMembers(mapping, form, request, response);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return prepareEditStudentGroupMembers(mapping, form, request, response);
    }

    public ActionForward viewAttendsSet(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        String groupingOIDString = request.getParameter("groupPropertiesCode");
        Integer groupingOID = Integer.valueOf(groupingOIDString);

        ISiteComponent viewAttendsSet = new InfoSiteGrouping();
        TeacherAdministrationSiteView result =
                (TeacherAdministrationSiteView) readSiteView(request, viewAttendsSet, null, groupingOID, null);

        if (request.getParameter("showPhotos") == null) {
            request.setAttribute("showPhotos", "false");
        }

        InfoSiteGrouping infoSiteAttendsSet = (InfoSiteGrouping) result.getComponent();
        if (infoSiteAttendsSet.getInfoGrouping() == null) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noAttendsSet");
            actionErrors.add("error.noAttendsSet", error);
            saveErrors(request, actionErrors);

            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        }

        Collections.sort(infoSiteAttendsSet.getInfoGrouping().getInfoAttends(), new BeanComparator("aluno.number"));
        return mapping.findForward("viewAttendsSet");
    }

    public ActionForward prepareEditAttendsSetMembers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        String groupingIDString = request.getParameter("groupPropertiesCode");
        Integer groupingID = new Integer(groupingIDString);

        Integer objectCode = getObjectCode(request);

        if (request.getParameter("showPhotos") == null) {
            request.setAttribute("showPhotos", "false");
        }

        ISiteComponent viewAttendsSet = new InfoSiteGrouping();
        TeacherAdministrationSiteView siteView =
                (TeacherAdministrationSiteView) readSiteView(request, viewAttendsSet, null, groupingID, null);
        InfoSiteGrouping component = (InfoSiteGrouping) siteView.getComponent();
        if (component.getInfoGrouping() == null) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noAttendsSet");
            actionErrors.add("error.noAttendsSet", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        }

        List infoStudentList = null;
        try {
            infoStudentList = PrepareEditGroupingMembers.run(objectCode, groupingID);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        Collections.sort(component.getInfoGrouping().getInfoAttends(), new BeanComparator("aluno.number"));
        Collections.sort(infoStudentList, new BeanComparator("number"));
        request.setAttribute("infoStudentList", infoStudentList);
        return mapping.findForward("editAttendsSetMembers");
    }

    public ActionForward insertAttendsSetMembers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        Integer objectCode = getObjectCode(request);
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);
        DynaActionForm insertAttendsSetForm = (DynaActionForm) form;
        List studentCodes = Arrays.asList((Integer[]) insertAttendsSetForm.get("studentCodesToInsert"));

        try {
            InsertGroupingMembers.runInsertGroupingMembers(objectCode, groupPropertiesCode, studentCodes);
        } catch (ExistingServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noAttendsSet");
            actionErrors.add("error.noAttendsSet", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (InvalidSituationServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("errors.existing.studentInAttendsSet");
            actionErrors.add("errors.existing.studentInAttendsSet", error);
            saveErrors(request, actionErrors);
            return prepareEditAttendsSetMembers(mapping, form, request, response);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return prepareEditAttendsSetMembers(mapping, form, request, response);
    }

    // ///////////////
    public ActionForward prepareInsertStudentsInAttendsSet(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        Integer groupPropertiesCode = Integer.valueOf(groupPropertiesCodeString);

        Integer objectCode = getObjectCode(request);

        readSiteView(request, null, null, null, null);

        List infoStudentList = null;
        try {
            infoStudentList = PrepareEditGroupingMembers.run(objectCode, groupPropertiesCode);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        Collections.sort(infoStudentList, new BeanComparator("number"));
        request.setAttribute("infoStudentList", infoStudentList);
        return mapping.findForward("insertStudentsInAttendsSet");
    }

    public ActionForward insertStudentsInAttendsSet(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        Integer objectCode = getObjectCode(request);
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);
        String[] selected = request.getParameterValues("selected");

        try {
            InsertStudentsInGrouping.runInsertStudentsInGrouping(objectCode, groupPropertiesCode, selected);
        } catch (ExistingServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noAttendsSet");
            actionErrors.add("error.noAttendsSet", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (InvalidSituationServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("errors.existing.studentInAttendsSet");
            actionErrors.add("errors.existing.studentInAttendsSet", error);
            saveErrors(request, actionErrors);
            return prepareInsertStudentsInAttendsSet(mapping, form, request, response);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return viewShiftsAndGroups(mapping, form, request, response);
    }

    public ActionForward deleteAttendsSetMembers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        Integer objectCode = getObjectCode(request);
        DynaActionForm deleteAttendsSetForm = (DynaActionForm) form;
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);
        List studentUsernames = Arrays.asList((String[]) deleteAttendsSetForm.get("studentsToRemove"));

        try {
            DeleteGroupingMembers.runDeleteGroupingMembers(objectCode, groupPropertiesCode, studentUsernames);
        } catch (ExistingServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noAttendsSet");
            actionErrors.add("error.noAttendsSet", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (InvalidSituationServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("errors.notExisting.studentInAttendsSet");
            actionErrors.add("errors.notExisting.studentInAttendsSet", error);
            saveErrors(request, actionErrors);
            return prepareEditAttendsSetMembers(mapping, form, request, response);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return prepareEditAttendsSetMembers(mapping, form, request, response);
    }

    public ActionForward deleteAttendsSetMembersByExecutionCourse(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {
        Integer objectCode = getObjectCode(request);
        String groupingOIDString = request.getParameter("groupingOID");
        Integer groupingOID = Integer.valueOf(groupingOIDString);

        try {
            DeleteGroupingMembersByExecutionCourseID.runDeleteGroupingMembersByExecutionCourseID(objectCode, groupingOID);
        } catch (ExistingServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noAttendsSet");
            actionErrors.add("error.noAttendsSet", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (InvalidSituationServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noExecutionCourse");
            actionErrors.add("error.noExecutionCourse", error);
            saveErrors(request, actionErrors);
            return viewAttendsSet(mapping, form, request, response);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return viewAttendsSet(mapping, form, request, response);
    }

    public ActionForward deleteAllAttendsSetMembers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        Integer objectCode = getObjectCode(request);
        String groupingOIDString = request.getParameter("groupingOID");
        Integer groupingOID = Integer.valueOf(groupingOIDString);

        try {
            DeleteAllGroupingMembers.runDeleteAllGroupingMembers(objectCode, groupingOID);
        } catch (ExistingServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noAttendsSet");
            actionErrors.add("error.noAttendsSet", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return viewAttendsSet(mapping, form, request, response);
    }

    public ActionForward deleteProjectProposal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        IUserView userView = getUserView(request);
        Integer objectCode = getObjectCode(request);
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);
        String executionCourseCodeString = request.getParameter("executionCourseCode");
        Integer executionCourseCode = new Integer(executionCourseCodeString);

        try {
            DeleteProjectProposal.runDeleteProjectProposal(objectCode, groupPropertiesCode, executionCourseCode,
                    userView.getUtilizador());
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("sucessfull", new ActionError("error.DeleteProjectProposal"));
            saveErrors(request, actionErrors);

        } catch (InvalidArgumentsServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add(e.getMessage(), new ActionError(e.getMessage()));
            saveErrors(request, actionErrors);

            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (FenixServiceException e) {
            e.printStackTrace();
            throw new FenixActionException(e.getMessage());
        }

        return prepareViewExecutionCourseProjects(mapping, form, request, response);
    }

    public ActionForward firstPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        Integer objectCode = getObjectCode(request);
        IUserView userView = getUserView(request);
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);
        String goalExecutionCourseIdString = request.getParameter("goalExecutionCourseId");
        Integer goalExecutionCourseId = new Integer(goalExecutionCourseIdString);

        Boolean type = null;
        try {
            type =
                    NewProjectProposal.runNewProjectProposal(objectCode, goalExecutionCourseId, groupPropertiesCode,
                            userView.getUtilizador());
        } catch (InvalidArgumentsServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add(e.getMessage(), new ActionError(e.getMessage()));
            saveErrors(request, actionErrors);

            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (InvalidSituationServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add(e.getMessage(), new ActionError(e.getMessage()));
            saveErrors(request, actionErrors);

            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        if (type.booleanValue() == false) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("sucessfull", new ActionError("error.NewProjectProposalSucessfull"));
            saveErrors(request, actionErrors);
        } else {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("sucessfull", new ActionError("error.NewProjectCreated"));
            saveErrors(request, actionErrors);
        }
        return prepareViewExecutionCourseProjects(mapping, form, request, response);
    }

    private void htmlEditorConfigurations(HttpServletRequest request, DynaActionForm actionForm) {
        String header = request.getHeader("User-Agent");
        if (header.indexOf("Safari/") == -1 && header.indexOf("Opera/") == -1 && header.indexOf("Konqueror/") == -1) {

            if (actionForm.get("editor").equals("") || (actionForm.get("editor").equals("true"))) {
                request.setAttribute("verEditor", "true");
            }

        } else {
            request.setAttribute("naoVerEditor", "true");
        }
    }
}