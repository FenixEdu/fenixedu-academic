package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidChangeServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonValidChangeServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoAnnouncement;
import net.sourceforge.fenixedu.dataTransferObject.InfoBibliographicReference;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.dataTransferObject.InfoEvaluationMethod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoGrouping;
import net.sourceforge.fenixedu.dataTransferObject.InfoItem;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteAnnouncement;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteBibliography;
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
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.FileItem;
import net.sourceforge.fenixedu.domain.FileItemPermittedGroupType;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidArgumentsActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidSessionActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.mapping.SiteManagementActionMapping;
import net.sourceforge.fenixedu.util.EnrolmentGroupPolicyType;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.validator.DynaValidatorForm;

import pt.utl.ist.fenix.tools.file.FileManagerException;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;

/**
 * @author Fernanda Quitï¿½rio
 * @deprecated
 */
@Deprecated
public class TeacherAdministrationViewerDispatchAction extends FenixDispatchAction {

    private static Properties properties;

    private static final String FILE_DOWNLOAD_URL_FORMAT = FileManagerFactory.getFileManager()
            .getDirectDownloadUrlFormat();

    public ActionForward instructions(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        ISiteComponent instructionsComponent = new InfoSiteInstructions();
        readSiteView(request, instructionsComponent, null, null, null);
        return mapping.findForward("viewSite");
    }

    // ======================== Customization Options Management
    // ========================
    public ActionForward prepareCustomizationOptions(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
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
        try {
            if (properties == null) {
                properties = PropertiesManager.loadProperties("/SMTPConfiguration.properties");
            }
            return properties.getProperty("mailingList.host.name");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public ActionForward editCustomizationOptions(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        DynaValidatorForm alternativeSiteForm = (DynaValidatorForm) form;
        HttpSession session = request.getSession(false);
        session.removeAttribute(SessionConstants.INFO_SECTION);
        Integer objectCode = getObjectCode(request);
        String alternativeSite = (String) alternativeSiteForm.get("siteAddress");
        String mail = (String) alternativeSiteForm.get("mail");
        String initialStatement = (String) alternativeSiteForm.get("initialStatement");
        String introduction = (String) alternativeSiteForm.get("introduction");
        Boolean dynamicMailDistribution = (Boolean) alternativeSiteForm.get("dynamicMailDistribution");
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Object args[] = { objectCode, alternativeSite, mail, dynamicMailDistribution, initialStatement, introduction };

        ActionErrors errors = new ActionErrors();
        try {
            ServiceManagerServiceFactory.executeService(userView, "EditCustomizationOptions", args);
        } catch (NotAuthorizedFilterException e) {
            errors.add("notResponsible", new ActionError("label.notAuthorized.courseInformation"));
            saveErrors(request, errors);

            return mapping.getInputForward();
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        ISiteComponent instructionsComponent = new InfoSiteInstructions();
        readSiteView(request, instructionsComponent, null, null, null);
        session.setAttribute("alternativeSiteForm", alternativeSiteForm);
        return mapping.findForward("viewSite");
    }

    // ======================== Announcements Management
    // ========================
    public ActionForward showAnnouncements(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {

        ISiteComponent announcementsComponent = new InfoSiteAnnouncement();
        readSiteView(request, announcementsComponent, null, null, null);
        TeacherAdministrationSiteView siteView = (TeacherAdministrationSiteView) request
                .getAttribute("siteView");

        List announcementsList = ((InfoSiteAnnouncement) siteView.getComponent()).getAnnouncements();
        if (!announcementsList.isEmpty()) {
            Collections.sort(announcementsList, new ComparatorChain(new BeanComparator(
                    "lastModifiedDate"), true));
            return mapping.findForward("showAnnouncements");
        }

        DynaActionForm actionForm = (DynaActionForm) form;
        htmlEditorConfigurations(request, actionForm);

        return mapping.findForward("insertAnnouncement");

    }

    public ActionForward prepareCreateAnnouncement(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        readSiteView(request, null, null, null, null);

        DynaActionForm actionForm = (DynaActionForm) form;
        htmlEditorConfigurations(request, actionForm);

        return mapping.findForward("insertAnnouncement");
    }

    public ActionForward createAnnouncement(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        HttpSession session = request.getSession(false);
        Integer objectCode = getObjectCode(request);
        DynaActionForm insertAnnouncementForm = (DynaActionForm) form;
        String title = (String) insertAnnouncementForm.get("title");
        String information = (String) insertAnnouncementForm.get("information");

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Object args[] = { objectCode, title, information };
        try {
            ServiceManagerServiceFactory.executeService(userView, "CreateAnnouncement", args);
        } catch (ExistingServiceException e) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("existingAnnouncementErrot", new ActionError("error.existingAnnouncement"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        ISiteComponent announcementsComponent = new InfoSiteAnnouncement();
        readSiteView(request, announcementsComponent, null, null, null);
        return showAnnouncements(mapping, form, request, response);
    }

    public ActionForward prepareEditAnnouncement(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        // retrieve announcement
        Integer announcementCode = getAnnouncementCode(request);
        ISiteComponent announcementComponent = new InfoAnnouncement();
        SiteView siteView = readSiteView(request, announcementComponent, null, announcementCode, null);

        String information = ((InfoAnnouncement) siteView.getComponent()).getInformation();

        DynaActionForm actionForm = (DynaActionForm) form;
        if (information != null)
            actionForm.set("information", information);

        if (request.getAttribute("announcementTextFlag") != null)
            actionForm.set("information", request.getAttribute("announcementTextFlag"));

        htmlEditorConfigurations(request, actionForm);

        return mapping.findForward("editAnnouncement");
    }

    private Integer getAnnouncementCode(HttpServletRequest request) {
        String announcementCodeString = request.getParameter("announcementCode");
        Integer announcementCode = null;
        if (announcementCodeString == null) {
            announcementCodeString = (String) request.getAttribute("announcementCode");
        }
        if (announcementCodeString != null)
            announcementCode = new Integer(announcementCodeString);
        return announcementCode;
    }

    public ActionForward editAnnouncement(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        HttpSession session = request.getSession(false);
        String announcementCodeString = request.getParameter("announcementCode");
        if (announcementCodeString == null) {
            announcementCodeString = (String) request.getAttribute("announcementCode");
        }
        Integer announcementCode = new Integer(announcementCodeString);
        DynaActionForm insertAnnouncementForm = (DynaActionForm) form;
        String newTitle = (String) insertAnnouncementForm.get("title");
        String newInformation = (String) insertAnnouncementForm.get("information");

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Object args[] = { announcementCode, newTitle, newInformation };
        try {
            ServiceManagerServiceFactory.executeService(userView, "EditAnnouncementService", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        ISiteComponent announecementsComponent = new InfoSiteAnnouncement();
        readSiteView(request, announecementsComponent, null, null, null);

        return showAnnouncements(mapping, form, request, response);
    }

    public ActionForward deleteAnnouncement(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {

        HttpSession session = request.getSession(false);
        String announcementCodeString = request.getParameter("announcementCode");

        if (announcementCodeString == null) {
            announcementCodeString = (String) request.getAttribute("announcementCode");
        }

        Integer announcementCode = new Integer(announcementCodeString);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Object args[] = { announcementCode };

        try {
            ServiceManagerServiceFactory.executeService(userView, "DeleteAnnouncementService", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        ISiteComponent announecementsComponent = new InfoSiteAnnouncement();
        readSiteView(request, announecementsComponent, null, null, null);
        TeacherAdministrationSiteView siteView = (TeacherAdministrationSiteView) request
                .getAttribute("siteView");

        if (!((InfoSiteAnnouncement) siteView.getComponent()).getAnnouncements().isEmpty()) {
            return showAnnouncements(mapping, form, request, response);
        }

        DynaActionForm actionForm = (DynaActionForm) form;
        htmlEditorConfigurations(request, actionForm);
        return mapping.findForward("insertAnnouncement");
    }

    // ======================== Objectives Management ========================
    public ActionForward viewObjectives(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        ISiteComponent objectivesComponent = new InfoSiteObjectives();
        readSiteView(request, objectivesComponent, null, null, null);
        return mapping.findForward("viewObjectives");
    }

    public ActionForward prepareEditObjectives(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {

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

        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        // Filter if the cteacher is responsibles for the execution course
        Object args[] = { userView.getUtilizador(), objectCode, curricularCourseCode };
        Boolean isResponsible = null;
        try {
            isResponsible = (Boolean) ServiceManagerServiceFactory.executeService(null,
                    "TeacherResponsibleByExecutionCourse", args);

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

        TeacherAdministrationSiteView siteView = (TeacherAdministrationSiteView) request
                .getAttribute("siteView");

        if (siteView.getComponent() != null) {
            DynaActionForm objectivesForm = (DynaActionForm) form;

            objectivesForm.set("generalObjectives", ((InfoCurriculum) siteView.getComponent())
                    .getGeneralObjectives());
            objectivesForm.set("generalObjectivesEn", ((InfoCurriculum) siteView.getComponent())
                    .getGeneralObjectivesEn());
            objectivesForm.set("operacionalObjectives", ((InfoCurriculum) siteView.getComponent())
                    .getOperacionalObjectives());
            objectivesForm.set("operacionalObjectivesEn", ((InfoCurriculum) siteView.getComponent())
                    .getOperacionalObjectivesEn());
        }

        return mapping.findForward("editObjectives");
    }

    public ActionForward editObjectives(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        HttpSession session = request.getSession(false);
        Integer objectCode = getObjectCode(request);
        String curricularCourseCodeString = request.getParameter("curricularCourseCode");
        Integer curricularCourseCode = new Integer(curricularCourseCodeString);
        DynaActionForm objectivesForm = (DynaActionForm) form;

        InfoCurriculum infoCurriculumNew = new InfoCurriculum();

        infoCurriculumNew.setIdInternal(curricularCourseCode);
        infoCurriculumNew.setGeneralObjectives((String) objectivesForm.get("generalObjectives"));
        infoCurriculumNew.setGeneralObjectivesEn((String) objectivesForm.get("generalObjectivesEn"));
        infoCurriculumNew.setOperacionalObjectives((String) objectivesForm.get("operacionalObjectives"));
        infoCurriculumNew.setOperacionalObjectivesEn((String) objectivesForm
                .get("operacionalObjectivesEn"));

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        Object args[] = { objectCode, curricularCourseCode, infoCurriculumNew, userView.getUtilizador() };

        try {
            ServiceManagerServiceFactory.executeService(userView, "EditObjectives", args);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return viewObjectives(mapping, form, request, response);
    }

    // ======================== Program Management ========================
    public ActionForward viewProgram(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {
        ISiteComponent programComponent = new InfoSitePrograms();
        readSiteView(request, programComponent, null, null, null);
        return mapping.findForward("viewProgram");
    }

    public ActionForward prepareEditProgram(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
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

        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        // Filter if the cteacher is responsibles for the execution course
        Object args[] = { userView.getUtilizador(), objectCode, curricularCourseCode };
        Boolean isResponsible = null;
        try {
            isResponsible = (Boolean) ServiceManagerServiceFactory.executeService(null,
                    "TeacherResponsibleByExecutionCourse", args);

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

        TeacherAdministrationSiteView siteView = (TeacherAdministrationSiteView) request
                .getAttribute("siteView");

        if (siteView.getComponent() != null) {
            DynaActionForm programForm = (DynaActionForm) form;
            programForm.set("program", ((InfoCurriculum) siteView.getComponent()).getProgram());
            programForm.set("programEn", ((InfoCurriculum) siteView.getComponent()).getProgramEn());
        }

        return mapping.findForward("editProgram");
    }

    public ActionForward editProgram(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {
        HttpSession session = request.getSession(false);
        Integer objectCode = getObjectCode(request);
        String curricularCourseCodeString = request.getParameter("curricularCourseCode");
        Integer curricularCourseCode = new Integer(curricularCourseCodeString);

        DynaActionForm programForm = (DynaActionForm) form;

        InfoCurriculum infoCurriculumNew = new InfoCurriculum();
        infoCurriculumNew.setIdInternal(curricularCourseCode);
        infoCurriculumNew.setProgram((String) programForm.get("program"));
        infoCurriculumNew.setProgramEn((String) programForm.get("programEn"));

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Object args[] = { objectCode, curricularCourseCode, infoCurriculumNew, userView.getUtilizador() };

        try {
            ServiceManagerServiceFactory.executeService(userView, "EditProgram", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return viewProgram(mapping, form, request, response);
    }

    // ======================== EvaluationMethod Management
    // ========================
    public ActionForward viewEvaluationMethod(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {

        ISiteComponent evaluationComponent = new InfoEvaluationMethod();

        readSiteView(request, evaluationComponent, null, null, null);
        return mapping.findForward("viewEvaluationMethod");
    }

    public ActionForward prepareEditEvaluationMethod(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {

        ISiteComponent evaluationComponent = new InfoEvaluationMethod();
        try {
            readSiteView(request, evaluationComponent, null, null, null);
        } catch (FenixActionException e1) {
            throw e1;
        }

        TeacherAdministrationSiteView siteView = (TeacherAdministrationSiteView) request
                .getAttribute("siteView");

        if (siteView.getComponent() != null) {
            DynaActionForm evaluationForm = (DynaActionForm) form;
            evaluationForm.set("evaluationElements", ((InfoEvaluationMethod) siteView.getComponent())
                    .getEvaluationElements());
            evaluationForm.set("evaluationElementsEn", ((InfoEvaluationMethod) siteView.getComponent())
                    .getEvaluationElementsEn());
        }

        return mapping.findForward("editEvaluationMethod");
    }

    public ActionForward editEvaluationMethod(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        HttpSession session = request.getSession(false);

        Integer objectCode = getObjectCode(request);
        Integer evaluationMethodCode = getParameter(request, "evaluationMethodCode");

        DynaActionForm evaluationForm = (DynaActionForm) form;

        InfoEvaluationMethod infoEvaluationMethod = new InfoEvaluationMethod();
        infoEvaluationMethod.setIdInternal(evaluationMethodCode);
        infoEvaluationMethod.setEvaluationElements((String) evaluationForm.get("evaluationElements"));
        infoEvaluationMethod
                .setEvaluationElementsEn((String) evaluationForm.get("evaluationElementsEn"));

        Object args[] = { objectCode, evaluationMethodCode, infoEvaluationMethod };

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        try {
            ServiceManagerServiceFactory.executeService(userView, "EditEvaluation", args);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return viewEvaluationMethod(mapping, form, request, response);
    }

    // ======================== Bibliographic References Management
    // ========================
    public ActionForward viewBibliographicReference(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        ISiteComponent bibliographyComponent = new InfoSiteBibliography();
        readSiteView(request, bibliographyComponent, null, null, null);
        TeacherAdministrationSiteView siteView = (TeacherAdministrationSiteView) request
                .getAttribute("siteView");
        if (((InfoSiteBibliography) siteView.getComponent()).getBibliographicReferences() != null
                && (!((InfoSiteBibliography) siteView.getComponent()).getBibliographicReferences()
                        .isEmpty())) {
            return mapping.findForward("bibliographyManagement");
        }

        HttpSession session = request.getSession(false);
        session.removeAttribute("bibliographicReferenceForm");

        return mapping.findForward("bibliographyManagement");

    }

    public ActionForward prepareCreateBibliographicReference(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        session.removeAttribute("bibliographicReferenceForm");
        readSiteView(request, null, null, null, null);
        return mapping.findForward("insertBibliographicReference");
    }

    public ActionForward createBibliographicReference(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        HttpSession session = request.getSession(false);
        Integer objectCode = getObjectCode(request);
        DynaActionForm insertBibliographicReferenceForm = (DynaActionForm) form;
        String title = (String) insertBibliographicReferenceForm.get("title");
        String authors = (String) insertBibliographicReferenceForm.get("authors");
        String reference = (String) insertBibliographicReferenceForm.get("reference");
        String year = (String) insertBibliographicReferenceForm.get("year");
        Boolean optional = new Boolean((String) insertBibliographicReferenceForm.get("optional"));
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Object args[] = { objectCode, title, authors, reference, year, optional };
        try {
            ServiceManagerServiceFactory.executeService(userView, "CreateBibliographicReference", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        ISiteComponent bibliographyComponent = new InfoSiteBibliography();
        readSiteView(request, bibliographyComponent, null, null, null);
        return mapping.findForward("bibliographyManagement");
    }

    public ActionForward prepareEditBibliographicReference(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        // retrieve bibliographic reference
        String bibliographicReferenceCodeString = request.getParameter("bibliographicReferenceCode");
        if (bibliographicReferenceCodeString == null) {
            bibliographicReferenceCodeString = (String) request
                    .getAttribute("bibliographicReferenceCode");
        }
        Integer bibliographicReferenceCode = new Integer(bibliographicReferenceCodeString);
        ISiteComponent bibliographyComponent = new InfoBibliographicReference();
        readSiteView(request, bibliographyComponent, null, bibliographicReferenceCode, null);
        return mapping.findForward("editBibliographicReference");
    }

    public ActionForward editBibliographicReference(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        HttpSession session = request.getSession(false);
        String bibliographicReferenceCodeString = request.getParameter("bibliographicReferenceCode");
        if (bibliographicReferenceCodeString == null) {
            bibliographicReferenceCodeString = (String) request
                    .getAttribute("bibliographicReferenceCode");
        }
        Integer bibliographicReferenceCode = new Integer(bibliographicReferenceCodeString);
        DynaActionForm editBibliographicReferenceForm = (DynaActionForm) form;
        String title = (String) editBibliographicReferenceForm.get("title");
        String authors = (String) editBibliographicReferenceForm.get("authors");
        String reference = (String) editBibliographicReferenceForm.get("reference");
        String year = (String) editBibliographicReferenceForm.get("year");
        Boolean optional = new Boolean((String) editBibliographicReferenceForm.get("optional"));
        Object args[] = { bibliographicReferenceCode, title, authors, reference, year, optional };
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        try {
            ServiceManagerServiceFactory.executeService(userView, "EditBibliographicReference", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        ISiteComponent bibliographyComponent = new InfoSiteBibliography();
        readSiteView(request, bibliographyComponent, null, null, null);
        return mapping.findForward("bibliographyManagement");
    }

    public ActionForward deleteBibliographicReference(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        HttpSession session = request.getSession(false);
        String bibliographicReferenceCodeString = request.getParameter("bibliographicReferenceCode");
        if (bibliographicReferenceCodeString == null) {
            bibliographicReferenceCodeString = (String) request
                    .getAttribute("bibliographicReferenceCode");
        }
        Integer bibliographicReferenceCode = new Integer(bibliographicReferenceCodeString);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Object args[] = { bibliographicReferenceCode };
        try {
            ServiceManagerServiceFactory.executeService(userView, "DeleteBibliographicReference", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return viewBibliographicReference(mapping, form, request, response);
    }

    // ======================== Teachers Management ========================
    public ActionForward viewTeachersByProfessorship(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        String username = getUsername(request);
        ISiteComponent teachersComponent = new InfoSiteTeachers();
        readSiteView(request, teachersComponent, null, null, username);
        return mapping.findForward("viewTeachers");
    }

    private String getUsername(HttpServletRequest request) throws InvalidSessionActionException {
        HttpSession session = getSession(request);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        String username = userView.getUtilizador();
        return username;
    }

    public ActionForward prepareAssociateTeacher(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        HttpSession session = request.getSession(false);
        session.removeAttribute("teacherForm");
        readSiteView(request, null, null, null, null);
        return mapping.findForward("associateTeacher");
    }

    public ActionForward associateTeacher(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        HttpSession session = getSession(request);
        Integer objectCode = getObjectCode(request);
        DynaActionForm teacherForm = (DynaActionForm) form;
        Integer teacherNumber = new Integer((String) teacherForm.get("teacherNumber"));
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Object args[] = { objectCode, teacherNumber };
        try {
            ServiceManagerServiceFactory.executeService(userView, "AssociateTeacher", args);
        } catch (InvalidArgumentsServiceException e) {
            throw new InvalidArgumentsActionException(teacherNumber.toString(), e);
        } catch (ExistingServiceException e) {
            throw new ExistingActionException(teacherNumber.toString(), e);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return viewTeachersByProfessorship(mapping, form, request, response);
    }

    public ActionForward removeTeacher(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        HttpSession session = getSession(request);
        String teacherCodeString = request.getParameter("teacherCode");
        if (teacherCodeString == null) {
            teacherCodeString = (String) request.getAttribute("teacherCode");
        }
        Integer teacherCode = new Integer(teacherCodeString);
        Integer objectCode = getObjectCode(request);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Object args[] = { objectCode, teacherCode };
        try {
            ServiceManagerServiceFactory.executeService(userView, "DeleteProfessorship", args);
        } catch (NotAuthorizedFilterException e) {
            final ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("error.invalidTeacherRemoval", new ActionError(
                    "error.invalidTeacherRemoval"));
            saveErrors(request, actionErrors);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        } catch (DomainException domainException) {
            final ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("error", new ActionError(domainException.getMessage()));
            saveErrors(request, actionErrors);
        }
        return viewTeachersByProfessorship(mapping, form, request, response);
    }

    // ======================== Evaluation Management ========================
    public ActionForward viewEvaluation(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        ISiteComponent evaluationComponent = new InfoSiteEvaluation();
        readSiteView(request, evaluationComponent, null, null, null);
        return mapping.findForward("viewEvaluation");
    }

    // ======================== Sections Management ========================
    public ActionForward sectionsFirstPage(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        readSiteView(request, null, null, null, null);
        return mapping.findForward("sectionsFirstPage");
    }

    public ActionForward viewSection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {
        Integer sectionCode = getSectionCode(request);
        return viewSection(mapping, form, request, response, sectionCode);
    }

    public ActionForward viewSection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response, Integer sectionCode) throws FenixActionException,
            FenixFilterException {
        ISiteComponent sectionComponent = new InfoSiteSection();
        readSiteView(request, sectionComponent, null, sectionCode, null);

        request.setAttribute("fileDownloadUrlFormat", FILE_DOWNLOAD_URL_FORMAT);

        return mapping.findForward("viewSection");
    }

    public ActionForward prepareCreateRegularSection(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
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

    public ActionForward prepareCreateRootSection(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        ISiteComponent rootSectionsComponent = new InfoSiteRootSections();
        readSiteView(request, rootSectionsComponent, null, null, null);
        return mapping.findForward("createSection");
    }

    public ActionForward createSection(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        HttpSession session = request.getSession(false);
        Integer sectionCode = getSectionCode(request);
        Integer objectCode = getObjectCode(request);
        DynaActionForm dynaForm = (DynaValidatorForm) form;
        String sectionName = (String) dynaForm.get("name");
        Integer order = Integer.valueOf((String) dynaForm.get("sectionOrder"));
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Object args[] = { objectCode, sectionCode, sectionName, order };
        try {
            ServiceManagerServiceFactory.executeService(userView, "InsertSection", args);
        } catch (ExistingServiceException e) {
            throw new ExistingActionException("Uma secï¿½ï¿½o com esse nome", e);
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage(), fenixServiceException);
        }
        return sectionsFirstPage(mapping, form, request, response);
    }

    public ActionForward prepareEditSection(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        Integer sectionCode = getSectionCode(request);
        ISiteComponent sectionsComponent = new InfoSiteSections();
        readSiteView(request, sectionsComponent, null, sectionCode, null);
        return mapping.findForward("editSection");
    }

    public ActionForward editSection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {
        HttpSession session = request.getSession(false);
        Integer sectionCode = getSectionCode(request);
        Integer objectCode = getObjectCode(request);
        DynaActionForm sectionForm = (DynaValidatorForm) form;
        String sectionName = (String) sectionForm.get("name");
        Integer order = (Integer) sectionForm.get("sectionOrder");
        order = new Integer(order.intValue() - 1);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Object editionArgs[] = { objectCode, sectionCode, sectionName, order };
        try {
            ServiceManagerServiceFactory.executeService(userView, "EditSection", editionArgs);
        } catch (ExistingServiceException ex) {
            throw new ExistingActionException(sectionName, ex);
        } catch (NonExistingServiceException ex) {
            throw new NonExistingActionException(sectionName, ex);
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException);
        }
        return sectionsFirstPage(mapping, form, request, response);
    }

    public ActionForward deleteSection(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        HttpSession session = request.getSession(false);
        Integer superiorSectionCode = getSuperiorSectionCode(request);
        Integer sectionCode = getSectionCode(request);
        Integer objectCode = getObjectCode(request);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Object deleteSectionArguments[] = { objectCode, sectionCode };
        try {
            ServiceManagerServiceFactory.executeService(userView, "DeleteSection",
                    deleteSectionArguments);
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

    public ActionForward prepareFileUpload(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        Integer itemCode = getItemCode(request);
        ISiteComponent itemsComponent = new InfoSiteItems();
        readSiteView(request, itemsComponent, null, itemCode, null);
        return mapping.findForward("uploadItemFile");
    }

    public ActionForward fileUpload(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException,
            FenixServiceException, IOException {
        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer itemId = getItemCode(request);
        DynaActionForm fileUploadForm = (DynaActionForm) form;
        FormFile formFile = (FormFile) fileUploadForm.get("theFile");
        String displayName = fileUploadForm.getString("displayName");
        FileItemPermittedGroupType fileItemPermittedGroupType = FileItemPermittedGroupType
                .valueOf((String) fileUploadForm.get("fileItemPermittedGroupType"));

        ActionErrors actionErrors = new ActionErrors();

        if (displayName == null || displayName.length() == 0 || displayName.trim().length() == 0) {
            displayName = getFilenameOnly(formFile.getFileName());
        }

        if (formFile.getFileName() == null || formFile.getFileName().length() == 0
                || formFile.getFileSize() == 0) {
            actionErrors.add("fileRequired", new ActionError("errors.fileRequired"));

            saveErrors(request, actionErrors);

            return prepareFileUpload(mapping, form, request, response);

        }

        InputStream formFileInputStream = null;
        try {
            formFileInputStream = formFile.getInputStream();
            Object[] args = { itemId, formFileInputStream, formFile.getFileName(), displayName,
                    fileItemPermittedGroupType };

            ServiceUtils.executeService(userView, "CreateFileItemForItem", args);

        } catch (FileManagerException e) {
            actionErrors.add("unableToStoreFile", new ActionError("errors.unableToStoreFile", formFile
                    .getFileName()));

            saveErrors(request, actionErrors);

            return prepareFileUpload(mapping, form, request, response);
        } finally {
            if (formFileInputStream != null) {
                formFileInputStream.close();
            }
        }

        return viewSection(mapping, form, request, response);
    }

    public ActionForward prepareEditItemFilePermissions(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException, FenixServiceException {
        HttpSession session = request.getSession(false);
        IUserView userView = getUserView(request);
        DynaActionForm editItemFilePermissionsForm = (DynaActionForm) form;
        Integer fileItemId = getFileItemId(request);
        Integer itemId = getItemCode(request);

        ISiteComponent itemsComponent = new InfoSiteItems();
        readSiteView(request, itemsComponent, null, itemId, null);

        FileItem fileItem = (FileItem) rootDomainObject.readFileByOID(fileItemId);

        editItemFilePermissionsForm.set("fileItemId", fileItem.getIdInternal());
        editItemFilePermissionsForm.set("itemCode", itemId);
        editItemFilePermissionsForm.set("permittedGroupType", fileItem.getFileItemPermittedGroupType()
                .toString());

        request.setAttribute("fileItem", fileItem);

        return mapping.findForward("editItemFilePermissions");
    }

    public ActionForward editItemFilePermissions(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException, FenixServiceException {
        HttpSession session = request.getSession(false);
        IUserView userView = getUserView(request);
        DynaActionForm editItemFilePermissionsForm = (DynaActionForm) form;
        Integer fileItemId = (Integer) editItemFilePermissionsForm.get("fileItemId");
        Integer itemId = (Integer) editItemFilePermissionsForm.get("itemCode");
        FileItemPermittedGroupType permittedGroupType = FileItemPermittedGroupType
                .valueOf(editItemFilePermissionsForm.getString("permittedGroupType"));

        try {
            ServiceUtils.executeService(userView, "EditItemFilePermissions", new Object[] { itemId,
                    fileItemId, permittedGroupType });
        } catch (FileManagerException ex) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors
                    .add(
                            "error.teacher.siteAdministration.editItemFilePermissions.unableToChangeFilePermissions",
                            new ActionError(
                                    "error.teacher.siteAdministration.editItemFilePermissions.unableToChangeFilePermissions"));

            saveErrors(request, actionErrors);

            return prepareEditItemFilePermissions(mapping, editItemFilePermissionsForm, request,
                    response);
        }

        return viewSection(mapping, form, request, response);
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


    public ActionForward deleteFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException, FenixServiceException {

        Integer itemCode = getItemCode(request);
        Integer fileItemId = getFileItemId(request);

        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Object[] args = { itemCode, fileItemId };
        try {
            ServiceUtils.executeService(userView, "DeleteFileItemFromItem", args);
        } catch (FileManagerException e1) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("unableToDeleteFile", new ActionError("errors.unableToDeleteFile"));
            saveErrors(request, actionErrors);
        }

        return viewSection(mapping, form, request, response);
    }

    public ActionForward prepareInsertItem(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        Integer sectionCode = getSectionCode(request);
        ISiteComponent sectionComponent = new InfoSiteSection();
        readSiteView(request, sectionComponent, null, sectionCode, null);

        DynaActionForm actionForm = (DynaActionForm) form;
        htmlEditorConfigurations(request, actionForm);

        return mapping.findForward("insertItem");
    }

    public ActionForward insertItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {
        HttpSession session = request.getSession(false);
        Integer sectionCode = getSectionCode(request);
        Integer objectCode = getObjectCode(request);
        DynaActionForm dynaForm = (DynaValidatorForm) form;
        Integer order = new Integer((String) dynaForm.get("itemOrder"));
        String itemName = (String) dynaForm.get("name");
        String information = (String) dynaForm.get("information");

        // HtmlValidator htmlValidator = new HtmlValidator();
        // htmlValidator.validateHTMLString(information);
        // String errors = htmlValidator.getErrors();
        //  
        // if((errors != null) && (!errors.equals(""))){
        // ActionErrors actionErrors = new ActionErrors();
        // request.setAttribute("errors", errors);
        // actionErrors.add("htmlErrors", new
        // ActionError("html.validate.error"));
        // saveErrors(request, actionErrors);
        // return mapping.getInputForward();
        // }

        String urgentString = (String) dynaForm.get("urgent");
        InfoItem newInfoItem = new InfoItem();
        newInfoItem.setItemOrder(order);
        newInfoItem.setName(itemName);
        newInfoItem.setInformation(information);
        newInfoItem.setUrgent(new Boolean(urgentString));
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Object args[] = { objectCode, sectionCode, newInfoItem };
        try {
            ServiceManagerServiceFactory.executeService(userView, "InsertItem", args);
        } catch (ExistingServiceException e) {
            throw new ExistingActionException("Um item com esse nome", e);
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }
        return viewSection(mapping, form, request, response);
    }

    public ActionForward prepareEditItem(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        Integer itemCode = getItemCode(request);
        ISiteComponent itemsComponent = new InfoSiteItems();
        SiteView siteView = readSiteView(request, itemsComponent, null, itemCode, null);
        String information = ((InfoSiteItems) siteView.getComponent()).getItem().getInformation();

        if (information != null) {
            DynaActionForm itemForm = (DynaActionForm) form;
            itemForm.set("information", ((InfoSiteItems) siteView.getComponent()).getItem()
                    .getInformation());
        }

        DynaActionForm actionForm = (DynaActionForm) form;
        htmlEditorConfigurations(request, actionForm);

        return mapping.findForward("editItem");
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

    public ActionForward editItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {
        HttpSession session = request.getSession(false);
        Integer itemCode = getItemCode(request);
        Integer objectCode = getObjectCode(request);
        DynaActionForm itemForm = (DynaActionForm) form;
        String information = (String) itemForm.get("information");

        // HtmlValidator htmlValidator = new HtmlValidator();
        // htmlValidator.validateHTMLString(information);
        // String errors = htmlValidator.getErrors();
        //  
        // if((errors != null) && (!errors.equals(""))){
        // ActionErrors actionErrors = new ActionErrors();
        // request.setAttribute("errors", errors);
        // actionErrors.add("htmlErrors", new
        // ActionError("html.validate.error"));
        // saveErrors(request, actionErrors);
        // return mapping.getInputForward();
        // }

        String name = (String) itemForm.get("name");
        Boolean urgent = new Boolean((String) itemForm.get("urgent"));
        Integer itemOrder = new Integer((String) itemForm.get("itemOrder"));
        itemOrder = new Integer(itemOrder.intValue() - 1);
        InfoItem newInfoItem = new InfoItem();
        newInfoItem.setInformation(information);
        newInfoItem.setName(name);
        newInfoItem.setItemOrder(itemOrder);
        newInfoItem.setUrgent(urgent);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Object editItemArgs[] = { objectCode, itemCode, newInfoItem };
        try {
            ServiceManagerServiceFactory.executeService(userView, "EditItem", editItemArgs);
        } catch (ExistingServiceException e) {
            throw new ExistingActionException("Um item com esse nome", e);
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException);
        }
        return viewSection(mapping, form, request, response);
    }

    public ActionForward deleteItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {
        HttpSession session = request.getSession(false);
        Integer itemCode = getItemCode(request);
        Integer objectCode = getObjectCode(request);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Object deleteItemArguments[] = { objectCode, itemCode };
        try {
            ServiceManagerServiceFactory.executeService(userView, "DeleteItem", deleteItemArguments);
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

    public ActionForward validationError(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
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
        } else if (siteComponent instanceof InfoAnnouncement) {
            obj1 = getAnnouncementCode(request);
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

    private SiteView readSiteView(HttpServletRequest request, ISiteComponent firstPageComponent,
            Integer infoExecutionCourseCode, Object obj1, Object obj2) throws FenixActionException,
            FenixFilterException {

        HttpSession session = getSession(request);

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        Integer objectCode = null;
        if (infoExecutionCourseCode == null) {
            objectCode = getObjectCode(request);
            infoExecutionCourseCode = objectCode;
        }

        ISiteComponent commonComponent = new InfoSiteCommon();
        Object[] args = { infoExecutionCourseCode, commonComponent, firstPageComponent, objectCode,
                obj1, obj2 };

        try {
            TeacherAdministrationSiteView siteView = (TeacherAdministrationSiteView) ServiceUtils
                    .executeService(userView, "TeacherAdministrationSiteComponentService", args);
            request.setAttribute("siteView", siteView);
            request.setAttribute("objectCode", ((InfoSiteCommon) siteView.getCommonComponent())
                    .getExecutionCourse().getIdInternal());
            if (siteView.getComponent() instanceof InfoSiteSection) {
                request.setAttribute("infoSection", ((InfoSiteSection) siteView.getComponent())
                        .getSection());
            }

            return siteView;

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

    }

    // ======================== GROUPS MANAGEMENT ========================

    public ActionForward viewExecutionCourseProjects(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer objectCode = getObjectCode(request);
        Object[] args = { objectCode };

        try {

            Boolean hasProposals = (Boolean) ServiceManagerServiceFactory.executeService(userView,
                    "ExecutionCourseHasProposals", args);
            Boolean waitingAnswer = (Boolean) ServiceManagerServiceFactory.executeService(userView,
                    "ExecutionCourseWaitingAnswer", args);

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

    public ActionForward prepareViewExecutionCourseProjects(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        ISiteComponent viewProjectsComponent = new InfoSiteProjects();
        readSiteView(request, viewProjectsComponent, null, null, null);
        return viewExecutionCourseProjects(mapping, form, request, response);
    }

    public ActionForward viewNewProjectProposals(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        ISiteComponent viewNewProjectProposalsComponent = new InfoSiteNewProjectProposals();
        readSiteView(request, viewNewProjectProposalsComponent, null, null, null);

        if (((InfoSiteNewProjectProposals) viewNewProjectProposalsComponent)
                .getInfoGroupPropertiesList().size() == 0) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.viewNewProjectProposals");
            actionErrors.add("error.viewNewProjectProposals", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        }
        return mapping.findForward("viewNewProjectProposals");
    }

    public ActionForward viewSentedProjectProposalsWaiting(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        ISiteComponent viewSentedProjectProposalsWaitingComponent = new InfoSiteSentedProjectProposalsWaiting();
        readSiteView(request, viewSentedProjectProposalsWaitingComponent, null, null, null);

        if (((InfoSiteSentedProjectProposalsWaiting) viewSentedProjectProposalsWaitingComponent)
                .getInfoGroupPropertiesList().size() == 0) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.SentedProjectsProposalsWaiting");
            actionErrors.add("error.SentedProjectsProposalsWaiting", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        }
        return mapping.findForward("viewSentedProjectProposalsWaiting");
    }

    public ActionForward rejectNewProjectProposal(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {

        HttpSession session = request.getSession(false);

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer objectCode = getObjectCode(request);
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);
        Object[] args = { objectCode, groupPropertiesCode, userView.getUtilizador() };
        try {
            ServiceManagerServiceFactory.executeService(userView, "RejectNewProjectProposal", args);
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

    public ActionForward acceptNewProjectProposal(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {

        HttpSession session = request.getSession(false);

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer objectCode = getObjectCode(request);
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);
        Object[] args = { objectCode, groupPropertiesCode, userView.getUtilizador() };
        try {
            ServiceManagerServiceFactory.executeService(userView, "AcceptNewProjectProposal", args);
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
            actionErrors.add("error.ProjectProposalName", new ActionError("error.ProjectProposalName", e
                    .getMessage()));
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);

        } catch (FenixServiceException e) {
            e.printStackTrace();
            throw new FenixActionException(e.getMessage());
        }

        return prepareViewExecutionCourseProjects(mapping, form, request, response);
    }

    public ActionForward viewShiftsAndGroups(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer objectCode = getObjectCode(request);
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);
        Object args[] = { objectCode };
        try {
            InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) ServiceManagerServiceFactory
                    .executeService(userView, "ReadExecutionCourseByOID", args);
            InfoExecutionPeriod infoExecutionPeriod = infoExecutionCourse.getInfoExecutionPeriod();
            request.setAttribute(SessionConstants.EXECUTION_PERIOD, infoExecutionPeriod);
            request.setAttribute(SessionConstants.EXECUTION_PERIOD_OID, infoExecutionPeriod
                    .getIdInternal().toString());
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException);
        }

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
            Iterator iterShiftsAndGroups = ((InfoSiteShiftsAndGroups) shiftsAndGroupsView)
                    .getInfoSiteGroupsByShiftList().iterator();
            while (iterShiftsAndGroups.hasNext() && !found) {
                InfoSiteGroupsByShift shiftsAndGroups = (InfoSiteGroupsByShift) iterShiftsAndGroups
                        .next();
                if (!shiftsAndGroups.getInfoSiteStudentGroupsList().isEmpty()) {
                    request.setAttribute("hasGroups", new Boolean(true));
                    found = true;
                }
            }
        }

        return mapping.findForward("viewShiftsAndGroups");

    }

    public ActionForward viewStudentGroupInformation(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer objectCode = getObjectCode(request);
        String studentGroupCodeString = request.getParameter("studentGroupCode");
        Integer studentGroupCode = new Integer(studentGroupCodeString);
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);

        String shiftCodeString = request.getParameter("shiftCode");

        ISiteComponent viewStudentGroup = new InfoSiteStudentGroup();
        readSiteView(request, viewStudentGroup, null, studentGroupCode, null);

        Object[] args = { objectCode, studentGroupCode, groupPropertiesCode, shiftCodeString };
        try {

            Integer type = (Integer) ServiceManagerServiceFactory.executeService(userView,
                    "VerifyStudentGroupWithoutShift", args);

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

    public ActionForward prepareCreateGroupProperties(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        readSiteView(request, null, null, null, null);
        List shiftTypeValues = new ArrayList();

        shiftTypeValues.add(new LabelValueBean("TEORICA", ShiftType.TEORICA.name()));
        shiftTypeValues.add(new LabelValueBean("PRATICA", ShiftType.PRATICA.name()));
        shiftTypeValues.add(new LabelValueBean("TEORICO_PRATICA", ShiftType.TEORICO_PRATICA.name()));
        shiftTypeValues.add(new LabelValueBean("LABORATORIAL", ShiftType.LABORATORIAL.name()));
        shiftTypeValues.add(new LabelValueBean("SEM TURNO", "SEM TURNO"));

        request.setAttribute("shiftTypeValues", shiftTypeValues);
        return mapping.findForward("insertGroupProperties");
    }

    public ActionForward createGroupProperties(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        HttpSession session = request.getSession(false);
        DynaActionForm insertGroupPropertiesForm = (DynaActionForm) form;
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
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
        InfoGrouping infoGroupProperties = new InfoGrouping();
        infoGroupProperties.setName(name);
        infoGroupProperties.setProjectDescription(projectDescription);
        // if(!shiftType.equals("5")){

        if (!shiftType.equals("Sem Turno") && !shiftType.equals("SEM TURNO")) {
            infoGroupProperties.setShiftType(ShiftType.valueOf(shiftType));
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
            if (maximumCapacity != null)
                if (minimumCapacity.compareTo(maximumCapacity) > 0) {
                    ActionErrors actionErrors = new ActionErrors();
                    ActionError error = null;
                    error = new ActionError("error.groupProperties.minimum");
                    actionErrors.add("error.groupProperties.minimum", error);
                    saveErrors(request, actionErrors);
                    return prepareCreateGroupProperties(mapping, form, request, response);

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

        if (!groupMaximumNumber.equals(""))
            infoGroupProperties.setGroupMaximumNumber(new Integer(groupMaximumNumber));

        EnrolmentGroupPolicyType enrolmentPolicy;
        if (optional.booleanValue())
            enrolmentPolicy = new EnrolmentGroupPolicyType(1);
        else
            enrolmentPolicy = new EnrolmentGroupPolicyType(2);
        infoGroupProperties.setEnrolmentPolicy(enrolmentPolicy);
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
        infoGroupProperties.setEnrolmentEndDay(enrolmentEndDay);

        Integer objectCode = getObjectCode(request);
        Object args[] = { objectCode, infoGroupProperties };
        try {
            ServiceManagerServiceFactory.executeService(userView, "CreateGrouping", args);

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

    public ActionForward prepareEditGroupProperties(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);
        ISiteComponent viewGroupProperties = new InfoSiteGrouping();
        SiteView siteView = readSiteView(request, viewGroupProperties, null, groupPropertiesCode, null);
        if (((InfoSiteGrouping) siteView.getComponent()).getInfoGrouping() == null) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noGroupProperties");
            actionErrors.add("error.noGroupProperties", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        }
        List shiftTypeValues = new ArrayList();
        shiftTypeValues.add(new LabelValueBean("TEORICA", ShiftType.TEORICA.name()));
        shiftTypeValues.add(new LabelValueBean("PRATICA", ShiftType.PRATICA.name()));
        shiftTypeValues.add(new LabelValueBean("TEORICO_PRATICA", ShiftType.TEORICO_PRATICA.name()));
        shiftTypeValues.add(new LabelValueBean("LABORATORIAL", ShiftType.LABORATORIAL.name()));
        shiftTypeValues.add(new LabelValueBean("SEM TURNO", "SEM TURNO"));
        request.setAttribute("shiftTypeValues", shiftTypeValues);

        List enrolmentPolicyValues = new ArrayList();
        enrolmentPolicyValues.add(new Integer(1));
        enrolmentPolicyValues.add(new Integer(2));

        List enrolmentPolicyNames = new ArrayList();
        enrolmentPolicyNames.add("Atómica");
        enrolmentPolicyNames.add("Individual");

        InfoGrouping infoGroupProperties = ((InfoSiteGrouping) siteView.getComponent())
                .getInfoGrouping();

        Integer enrolmentPolicy = infoGroupProperties.getEnrolmentPolicy().getType();
        enrolmentPolicyValues.remove(enrolmentPolicy.intValue() - 1);
        String enrolmentPolicyName = enrolmentPolicyNames.remove(enrolmentPolicy.intValue() - 1)
                .toString();

        request.setAttribute("infoGroupProperties", infoGroupProperties);
        request.setAttribute("enrolmentPolicyName", enrolmentPolicyName);
        request.setAttribute("enrolmentPolicyValue", enrolmentPolicy);
        request.setAttribute("enrolmentPolicyValues", enrolmentPolicyValues);
        request.setAttribute("enrolmentPolicyNames", enrolmentPolicyNames);

        return mapping.findForward("editGroupProperties");
    }

    public ActionForward editGroupProperties(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {

        HttpSession session = request.getSession(false);
        DynaActionForm editGroupPropertiesForm = (DynaActionForm) form;
        String groupPropertiesString = request.getParameter("groupPropertiesCode");
        Integer groupPropertiesCode = new Integer(groupPropertiesString);
        String name = (String) editGroupPropertiesForm.get("name");
        String projectDescription = (String) editGroupPropertiesForm.get("projectDescription");
        String maximumCapacityString = (String) editGroupPropertiesForm.get("maximumCapacity");
        String minimumCapacityString = (String) editGroupPropertiesForm.get("minimumCapacity");
        String idealCapacityString = (String) editGroupPropertiesForm.get("idealCapacity");

        String groupMaximumNumber = (String) editGroupPropertiesForm.get("groupMaximumNumber");
        String enrolmentBeginDayString = (String) editGroupPropertiesForm
                .get("enrolmentBeginDayFormatted");
        String enrolmentBeginHourString = (String) editGroupPropertiesForm
                .get("enrolmentBeginHourFormatted");
        String enrolmentEndDayString = (String) editGroupPropertiesForm.get("enrolmentEndDayFormatted");
        String enrolmentEndHourString = (String) editGroupPropertiesForm
                .get("enrolmentEndHourFormatted");
        String shiftType = (String) editGroupPropertiesForm.get("shiftType");
        String enrolmentPolicy = (String) editGroupPropertiesForm.get("enrolmentPolicy");

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
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        InfoGrouping infoGroupProperties = new InfoGrouping();
        infoGroupProperties.setIdInternal(groupPropertiesCode);
        infoGroupProperties.setEnrolmentBeginDay(enrolmentBeginDay);
        infoGroupProperties.setEnrolmentEndDay(enrolmentEndDay);
        infoGroupProperties
                .setEnrolmentPolicy(new EnrolmentGroupPolicyType(new Integer(enrolmentPolicy)));
        if (!groupMaximumNumber.equals(""))
            infoGroupProperties.setGroupMaximumNumber(new Integer(groupMaximumNumber));
        Integer maximumCapacity = null;
        Integer minimumCapacity = null;
        Integer idealCapacity = null;

        if (!maximumCapacityString.equals("")) {
            maximumCapacity = new Integer(maximumCapacityString);
            infoGroupProperties.setMaximumCapacity(maximumCapacity);
        }

        if (!minimumCapacityString.equals("")) {
            minimumCapacity = new Integer(minimumCapacityString);
            if (maximumCapacity != null)
                if (minimumCapacity.compareTo(maximumCapacity) > 0) {
                    ActionErrors actionErrors = new ActionErrors();
                    ActionError error = null;
                    error = new ActionError("error.groupProperties.minimum");
                    actionErrors.add("error.groupProperties.minimum", error);
                    saveErrors(request, actionErrors);
                    return prepareEditGroupProperties(mapping, form, request, response);

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

        if (!shiftType.equals("Sem Turno") && !shiftType.equals("SEM TURNO")) {
            infoGroupProperties.setShiftType(ShiftType.valueOf(shiftType));
        }
        Integer objectCode = getObjectCode(request);
        Object args[] = { objectCode, infoGroupProperties };
        List errors = new ArrayList();
        try {
            errors = (List) ServiceManagerServiceFactory.executeService(userView, "EditGrouping", args);
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

    public ActionForward deleteGroupProperties(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {

        ActionErrors actionErrors = new ActionErrors();

        HttpSession session = request.getSession(false);

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

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

        Object[] args = { objectCode, groupPropertiesCode };

        Boolean result = Boolean.FALSE;
        try {
            result = (Boolean) ServiceManagerServiceFactory.executeService(userView, "DeleteGrouping",
                    args);
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
            actionErrors.add("errors.delete.groupPropertie", new ActionError(
                    "error.groupProperties.delete"));
            saveErrors(request, actionErrors);

            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        }

        return prepareViewExecutionCourseProjects(mapping, form, request, response);
    }

    public ActionForward prepareImportGroupProperties(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
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
        Object args[] = { objectCode, groupPropertiesCode };
        Boolean result;
        try {
            result = (Boolean) ServiceManagerServiceFactory.executeService(userView,
                    "VerifyIfGroupPropertiesHasProjectProposal", args);
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

        String enrolmentPolicyName = ((InfoSiteGrouping) siteView.getComponent()).getInfoGrouping()
                .getEnrolmentPolicy().getTypeFullName();
        request.setAttribute("enrolmentPolicyName", enrolmentPolicyName);

        ShiftType shiftType = ((InfoSiteGrouping) siteView.getComponent()).getInfoGrouping()
                .getShiftType();
        String shiftTypeName = "Sem Turno";
        if (shiftType != null) {
            shiftTypeName = shiftType.getFullNameTipoAula();
        }

        request.setAttribute("shiftTypeName", shiftTypeName);

        return mapping.findForward("importGroupProperties");
    }

    public ActionForward deleteStudentGroup(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {

        HttpSession session = request.getSession(false);

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer objectCode = getObjectCode(request);
        String studentGroupCodeString = request.getParameter("studentGroupCode");
        Integer studentGroupCode = new Integer(studentGroupCodeString);
        Object[] args = { objectCode, studentGroupCode };
        try {
            ServiceManagerServiceFactory.executeService(userView, "DeleteStudentGroup", args);
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

        } catch (FenixServiceException e) {
            e.printStackTrace();
            throw new FenixActionException(e.getMessage());
        }

        return viewShiftsAndGroups(mapping, form, request, response);
    }

    public ActionForward prepareCreateStudentGroup(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        HttpSession session = request.getSession(false);
        session.removeAttribute("insertStudentGroupForm");
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer objectCode = getObjectCode(request);
        String groupPropertiesString = request.getParameter("groupPropertiesCode");
        Integer groupPropertiesCode = new Integer(groupPropertiesString);

        String shiftCodeString = request.getParameter("shiftCode");
        request.setAttribute("shiftCode", shiftCodeString);

        InfoSiteStudentGroup infoSiteStudentGroup;
        Object args[] = { objectCode, groupPropertiesCode };
        try {
            infoSiteStudentGroup = (InfoSiteStudentGroup) ServiceManagerServiceFactory.executeService(
                    userView, "PrepareCreateStudentGroup", args);
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

    public ActionForward createStudentGroup(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {

        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
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

        Object args[] = { objectCode, groupNumber, groupPropertiesCode, shiftCode, studentCodes };
        try {
            ServiceManagerServiceFactory.executeService(userView, "CreateStudentGroup", args);

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
            ActionError error = new ActionError(
                    "errors.notExisting.studentInAttendsSetToCreateStudentGroup");
            actionErrors.add("errors.notExisting.studentInAttendsSetToCreateStudentGroup", error);
            saveErrors(request, actionErrors);
            return prepareCreateStudentGroup(mapping, form, request, response);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return viewShiftsAndGroups(mapping, form, request, response);

    }

    public ActionForward viewStudentsAndGroupsByShift(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer objectCode = getObjectCode(request);

        String groupPropertiesString = request.getParameter("groupPropertiesCode");
        Integer groupPropertiesCode = new Integer(groupPropertiesString);
        String shiftCodeString = request.getParameter("shiftCode");
        Integer shiftCode = new Integer(shiftCodeString);

        InfoSiteStudentsAndGroups infoSiteStudentsAndGroups = new InfoSiteStudentsAndGroups();
        Boolean type = null;
        Object args[] = { groupPropertiesCode, shiftCode };
        Object args1[] = { objectCode, groupPropertiesCode, shiftCode };
        try {
            infoSiteStudentsAndGroups = (InfoSiteStudentsAndGroups) ServiceManagerServiceFactory
                    .executeService(userView, "ReadStudentsAndGroupsByShiftID", args);

            type = (Boolean) ServiceManagerServiceFactory.executeService(userView,
                    "VerifyIfCanEnrollStudentGroupsInShift", args1);

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

    public ActionForward viewStudentsAndGroupsWithoutShift(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        String groupPropertiesString = request.getParameter("groupPropertiesCode");
        Integer groupPropertiesCode = new Integer(groupPropertiesString);

        InfoSiteStudentsAndGroups infoSiteStudentsAndGroups = new InfoSiteStudentsAndGroups();
        Object args[] = { groupPropertiesCode };
        try {
            infoSiteStudentsAndGroups = (InfoSiteStudentsAndGroups) ServiceManagerServiceFactory
                    .executeService(userView, "ReadStudentsAndGroupsWithoutShift", args);
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

    public ActionForward viewAllStudentsAndGroups(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        String groupPropertiesString = request.getParameter("groupPropertiesCode");
        Integer groupPropertiesCode = new Integer(groupPropertiesString);

        InfoSiteStudentsAndGroups infoSiteStudentsAndGroups = new InfoSiteStudentsAndGroups();
        Object args[] = { groupPropertiesCode };
        try {
            infoSiteStudentsAndGroups = (InfoSiteStudentsAndGroups) ServiceManagerServiceFactory
                    .executeService(userView, "ReadAllStudentsAndGroups", args);
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

    public ActionForward prepareEditStudentGroupShift(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {

        String studentGroupCodeString = request.getParameter("studentGroupCode");
        String shiftCodeString = request.getParameter("shiftCode");
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");

        Integer studentGroupCode = new Integer(studentGroupCodeString);
        Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);
        Integer shiftCode = new Integer(shiftCodeString);
        ISiteComponent viewShifts = new InfoSiteShifts();
        TeacherAdministrationSiteView shiftsView = (TeacherAdministrationSiteView) readSiteView(request,
                viewShifts, null, groupPropertiesCode, studentGroupCode);
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

    public ActionForward editStudentGroupShift(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        HttpSession session = request.getSession(false);
        Integer objectCode = getObjectCode(request);
        DynaActionForm editStudentGroupForm = (DynaActionForm) form;
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
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
            Object args[] = { objectCode, studentGroupCode, groupPropertiesCode, newShiftCode };

            try {
                ServiceManagerServiceFactory.executeService(userView, "EditStudentGroupShift", args);
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
    public ActionForward prepareEnrollStudentGroupShift(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {

        String studentGroupCodeString = request.getParameter("studentGroupCode");
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");

        Integer studentGroupCode = new Integer(studentGroupCodeString);
        Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);

        ISiteComponent viewShifts = new InfoSiteShifts();
        TeacherAdministrationSiteView shiftsView = (TeacherAdministrationSiteView) readSiteView(request,
                viewShifts, null, groupPropertiesCode, studentGroupCode);
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

    public ActionForward enrollStudentGroupShift(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        HttpSession session = request.getSession(false);
        Integer objectCode = getObjectCode(request);
        DynaActionForm enrollStudentGroupForm = (DynaActionForm) form;
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
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
        Object args[] = { objectCode, studentGroupCode, groupPropertiesCode, newShiftCode };

        try {
            ServiceManagerServiceFactory.executeService(userView, "EnrollStudentGroupShift", args);
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
    public ActionForward unEnrollStudentGroupShift(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        HttpSession session = request.getSession(false);
        Integer objectCode = getObjectCode(request);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        String studentGroupCodeString = request.getParameter("studentGroupCode");
        Integer studentGroupCode = new Integer(studentGroupCodeString);
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);

        Object args[] = { objectCode, studentGroupCode, groupPropertiesCode };

        try {
            ServiceManagerServiceFactory.executeService(userView, "UnEnrollStudentGroupShift", args);
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

    public ActionForward prepareEditStudentGroupMembers(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {

        HttpSession session = request.getSession(false);

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        String studentGroupCodeString = request.getParameter("studentGroupCode");
        Integer studentGroupCode = new Integer(studentGroupCodeString);

        String shiftCodeString = request.getParameter("shiftCode");
        request.setAttribute("shiftCode", shiftCodeString);
        Integer objectCode = getObjectCode(request);

        ISiteComponent viewStudentGroup = new InfoSiteStudentGroup();
        TeacherAdministrationSiteView siteView = (TeacherAdministrationSiteView) readSiteView(request,
                viewStudentGroup, null, studentGroupCode, null);
        InfoSiteStudentGroup component = (InfoSiteStudentGroup) siteView.getComponent();

        if (component.getInfoSiteStudentInformationList() == null) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noGroup");
            actionErrors.add("error.noGroup", error);
            saveErrors(request, actionErrors);
            return viewShiftsAndGroups(mapping, form, request, response);
        }

        Object args[] = { objectCode, studentGroupCode };
        List infoStudentList = null;
        try {
            infoStudentList = (List) ServiceManagerServiceFactory.executeService(userView,
                    "PrepareEditStudentGroupMembers", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        Collections.sort(infoStudentList, new BeanComparator("number"));
        request.setAttribute("infoStudentList", infoStudentList);
        return mapping.findForward("editStudentGroupMembers");
    }

    public ActionForward prepareEditStudentGroupsShift(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        Integer groupProperties = new Integer(groupPropertiesCodeString);
        String shiftCodeString = request.getParameter("shiftCode");
        Integer shiftCode = new Integer(shiftCodeString);

        ISiteComponent infoSiteStudentGroupAndStudents = new InfoSiteStudentGroupAndStudents();
        TeacherAdministrationSiteView siteView = (TeacherAdministrationSiteView) readSiteView(request,
                infoSiteStudentGroupAndStudents, null, groupProperties, shiftCode);

        InfoSiteStudentGroupAndStudents component = (InfoSiteStudentGroupAndStudents) siteView
                .getComponent();

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

    public ActionForward editStudentGroupsShift(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer objectCode = getObjectCode(request);
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);
        String shiftCodeString = request.getParameter("shiftCode");
        Integer shiftCode = new Integer(shiftCodeString);

        DynaActionForm editStudentGroupsShiftForm = (DynaActionForm) form;
        List studentGroupsCodes = Arrays.asList((Integer[]) editStudentGroupsShiftForm
                .get("studentGroupsCodes"));

        Object args[] = { objectCode, groupPropertiesCode, shiftCode, studentGroupsCodes };
        try {
            ServiceManagerServiceFactory.executeService(userView, "EditStudentGroupsShift", args);
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

    public ActionForward insertStudentGroupMembers(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {

        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer objectCode = getObjectCode(request);
        String studentGroupCodeString = request.getParameter("studentGroupCode");
        Integer studentGroupCode = new Integer(studentGroupCodeString);
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);
        DynaActionForm insertStudentGroupForm = (DynaActionForm) form;
        List studentUsernames = Arrays.asList((String[]) insertStudentGroupForm.get("studentsToInsert"));

        Object args[] = { objectCode, studentGroupCode, groupPropertiesCode, studentUsernames };

        try {
            ServiceManagerServiceFactory.executeService(userView, "InsertStudentGroupMembers", args);
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

    public ActionForward deleteStudentGroupMembers(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {

        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer objectCode = getObjectCode(request);
        String studentGroupCodeString = request.getParameter("studentGroupCode");
        Integer studentGroupCode = new Integer(studentGroupCodeString);
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);
        DynaActionForm deleteStudentGroupForm = (DynaActionForm) form;
        List studentUsernames = Arrays.asList((String[]) deleteStudentGroupForm.get("studentsToRemove"));

        Object args[] = { objectCode, studentGroupCode, groupPropertiesCode, studentUsernames };
        try {
            ServiceManagerServiceFactory.executeService(userView, "DeleteStudentGroupMembers", args);
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

    public ActionForward viewAttendsSet(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        String groupingOIDString = request.getParameter("groupPropertiesCode");
        Integer groupingOID = Integer.valueOf(groupingOIDString);

        ISiteComponent viewAttendsSet = new InfoSiteGrouping();
        TeacherAdministrationSiteView result = (TeacherAdministrationSiteView) readSiteView(request,
                viewAttendsSet, null, groupingOID, null);

        InfoSiteGrouping infoSiteAttendsSet = (InfoSiteGrouping) result.getComponent();
        if (infoSiteAttendsSet.getInfoGrouping() == null) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noAttendsSet");
            actionErrors.add("error.noAttendsSet", error);
            saveErrors(request, actionErrors);

            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        }

        Collections.sort(infoSiteAttendsSet.getInfoGrouping().getInfoAttends(), new BeanComparator(
                "aluno.number"));
        return mapping.findForward("viewAttendsSet");
    }

    public ActionForward prepareEditAttendsSetMembers(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {

        HttpSession session = request.getSession(false);

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        String groupingIDString = request.getParameter("groupPropertiesCode");
        Integer groupingID = new Integer(groupingIDString);

        Integer objectCode = getObjectCode(request);

        ISiteComponent viewAttendsSet = new InfoSiteGrouping();
        TeacherAdministrationSiteView siteView = (TeacherAdministrationSiteView) readSiteView(request,
                viewAttendsSet, null, groupingID, null);
        InfoSiteGrouping component = (InfoSiteGrouping) siteView.getComponent();
        if (component.getInfoGrouping() == null) {
            ActionErrors actionErrors = new ActionErrors();
            ActionError error = null;
            error = new ActionError("error.noAttendsSet");
            actionErrors.add("error.noAttendsSet", error);
            saveErrors(request, actionErrors);
            return prepareViewExecutionCourseProjects(mapping, form, request, response);
        }

        Object args[] = { objectCode, groupingID };
        List infoStudentList = null;
        try {
            infoStudentList = (List) ServiceManagerServiceFactory.executeService(userView,
                    "PrepareEditGroupingMembers", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        Collections.sort(component.getInfoGrouping().getInfoAttends(),
                new BeanComparator("aluno.number"));
        Collections.sort(infoStudentList, new BeanComparator("number"));
        request.setAttribute("infoStudentList", infoStudentList);
        return mapping.findForward("editAttendsSetMembers");
    }

    public ActionForward insertAttendsSetMembers(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {

        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer objectCode = getObjectCode(request);
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);
        DynaActionForm insertAttendsSetForm = (DynaActionForm) form;
        List studentCodes = Arrays.asList((Integer[]) insertAttendsSetForm.get("studentCodesToInsert"));

        Object args[] = { objectCode, groupPropertiesCode, studentCodes };

        try {
            ServiceManagerServiceFactory.executeService(userView, "InsertGroupingMembers", args);
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
    public ActionForward prepareInsertStudentsInAttendsSet(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {

        HttpSession session = request.getSession(false);

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        Integer groupPropertiesCode = Integer.valueOf(groupPropertiesCodeString);

        Integer objectCode = getObjectCode(request);

        readSiteView(request, null, null, null, null);

        Object args[] = { objectCode, groupPropertiesCode };
        List infoStudentList = null;
        try {
            infoStudentList = (List) ServiceManagerServiceFactory.executeService(userView,
                    "PrepareEditGroupingMembers", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        Collections.sort(infoStudentList, new BeanComparator("number"));
        request.setAttribute("infoStudentList", infoStudentList);
        return mapping.findForward("insertStudentsInAttendsSet");
    }

    public ActionForward insertStudentsInAttendsSet(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {

        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer objectCode = getObjectCode(request);
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);
        String[] selected = request.getParameterValues("selected");

        Object args[] = { objectCode, groupPropertiesCode, selected };

        try {
            ServiceManagerServiceFactory.executeService(userView, "InsertStudentsInGrouping", args);
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

    public ActionForward deleteAttendsSetMembers(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {

        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer objectCode = getObjectCode(request);
        DynaActionForm deleteAttendsSetForm = (DynaActionForm) form;
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);
        List studentUsernames = Arrays.asList((String[]) deleteAttendsSetForm.get("studentsToRemove"));

        Object args[] = { objectCode, groupPropertiesCode, studentUsernames };
        try {
            ServiceManagerServiceFactory.executeService(userView, "DeleteGroupingMembers", args);
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

    public ActionForward deleteAttendsSetMembersByExecutionCourse(ActionMapping mapping,
            ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException, FenixFilterException {
        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer objectCode = getObjectCode(request);
        String groupingOIDString = request.getParameter("groupingOID");
        Integer groupingOID = Integer.valueOf(groupingOIDString);

        Object args[] = { objectCode, groupingOID };
        try {
            ServiceManagerServiceFactory.executeService(userView,
                    "DeleteGroupingMembersByExecutionCourseID", args);
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

    public ActionForward deleteAllAttendsSetMembers(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer objectCode = getObjectCode(request);
        String groupingOIDString = request.getParameter("groupingOID");
        Integer groupingOID = Integer.valueOf(groupingOIDString);

        Object args[] = { objectCode, groupingOID };
        try {
            ServiceManagerServiceFactory.executeService(userView, "DeleteAllGroupingMembers", args);
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

    public ActionForward deleteProjectProposal(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {

        HttpSession session = request.getSession(false);

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer objectCode = getObjectCode(request);
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);
        String executionCourseCodeString = request.getParameter("executionCourseCode");
        Integer executionCourseCode = new Integer(executionCourseCodeString);

        Object[] args = { objectCode, groupPropertiesCode, executionCourseCode, userView.getUtilizador() };
        try {
            ServiceManagerServiceFactory.executeService(userView, "DeleteProjectProposal", args);
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
            HttpServletResponse response) throws FenixActionException, FenixFilterException {
        Integer objectCode = getObjectCode(request);
        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        String groupPropertiesCodeString = request.getParameter("groupPropertiesCode");
        Integer groupPropertiesCode = new Integer(groupPropertiesCodeString);
        String goalExecutionCourseIdString = request.getParameter("goalExecutionCourseId");
        Integer goalExecutionCourseId = new Integer(goalExecutionCourseIdString);

        Boolean type = null;
        Object args[] = { objectCode, goalExecutionCourseId, groupPropertiesCode,
                userView.getUtilizador() };
        try {
            type = (Boolean) ServiceManagerServiceFactory.executeService(userView, "NewProjectProposal",
                    args);

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
        if (header.indexOf("Safari/") == -1 && header.indexOf("Opera/") == -1
                && header.indexOf("Konqueror/") == -1) {

            if (actionForm.get("editor").equals("") || (actionForm.get("editor").equals("true")))
                request.setAttribute("verEditor", "true");

        } else
            request.setAttribute("naoVerEditor", "true");
    }
}