package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteInstructions;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteSection;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.TeacherAdministrationSiteView;
import net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.ImportContentBean;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author Fernanda Quitério
 * @deprecated
 */
@Deprecated
public class TeacherAdministrationViewerDispatchAction extends FenixDispatchAction {

    private static Properties properties;

    // ======================== Customization Options Management
    // ========================
    public ActionForward prepareCustomizationOptions(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        final ExecutionCourse executionCourse = rootDomainObject
                .readExecutionCourseByOID(getObjectCode(request));
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

    public ActionForward submitDataToImportCustomizationOptions(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {

        final ExecutionCourse executionCourse = rootDomainObject
                .readExecutionCourseByOID(getObjectCode(request));
        final IViewState viewState = RenderUtils.getViewState();
        final ImportContentBean bean = (ImportContentBean) viewState.getMetaObject().getObject();
        request.setAttribute("importContentBean", bean);
        request.setAttribute("executionCourse", executionCourse);
        return mapping.findForward("importCustomizationOptions");
    }

    public ActionForward submitDataToImportCustomizationOptionsPostBack(ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {

        final ExecutionCourse executionCourse = rootDomainObject
                .readExecutionCourseByOID(getObjectCode(request));
        final IViewState viewState = RenderUtils.getViewState();
        final ImportContentBean bean = (ImportContentBean) viewState.getMetaObject().getObject();
        if (bean.getCurricularYear() == null || bean.getExecutionPeriod() == null
                || bean.getExecutionDegree() == null) {
            bean.setExecutionCourse(null);
        }
        RenderUtils.invalidateViewState();
        request.setAttribute("importContentBean", bean);
        request.setAttribute("executionCourse", executionCourse);
        return mapping.findForward("importCustomizationOptions");
    }

    public ActionForward prepareImportCustomizationOptions(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        final ExecutionCourse executionCourse = rootDomainObject
                .readExecutionCourseByOID(getObjectCode(request));
        request.setAttribute("importContentBean", new ImportContentBean());
        request.setAttribute("executionCourse", executionCourse);
        return mapping.findForward("importCustomizationOptions");
    }

    public ActionForward importCustomizationOptions(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException, FenixActionException {

        final IViewState viewState = RenderUtils.getViewState();
        ImportContentBean bean = (ImportContentBean) viewState.getMetaObject().getObject();
        request.setAttribute("importContentBean", bean);

        ExecutionCourse executionCourseFrom = bean.getExecutionCourse();
        ExecutionCourse executionCourseTo = rootDomainObject
                .readExecutionCourseByOID(getObjectCode(request));

        final Object args[] = { executionCourseTo.getIdInternal(), executionCourseTo.getSite(),
                executionCourseFrom.getSite() };
        try {
            ServiceManagerServiceFactory.executeService(getUserView(request),
                    "ImportCustomizationOptions", args);
        } catch (DomainException e) {
            addActionMessage(request, e.getKey(), e.getArgs());
        }

        return prepareCustomizationOptions(mapping, form, request, response);
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
        IUserView userView = getUserView(request);
        Object args[] = { objectCode, alternativeSite, mail, dynamicMailDistribution, initialStatement,
                introduction };

        ActionMessages errors = new ActionMessages();
        try {
            ServiceManagerServiceFactory.executeService(userView, "EditCustomizationOptions", args);
        } catch (NotAuthorizedFilterException e) {
            errors.add("notResponsible", new ActionMessage("label.notAuthorized.courseInformation"));
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

    private SiteView readSiteView(HttpServletRequest request, ISiteComponent firstPageComponent,
            Integer infoExecutionCourseCode, Object obj1, Object obj2) throws FenixActionException,
            FenixFilterException {

        IUserView userView = getUserView(request);

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

}