package net.sourceforge.fenixedu.presentationTier.Action.person;

import java.io.InputStream;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.AppUserAuthorization;
import net.sourceforge.fenixedu.domain.AppUserSession;
import net.sourceforge.fenixedu.domain.ExternalApplication;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.JerseyOAuth2Filter;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.Atomic;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;

@Mapping(module = "person", path = "/externalApps")
@Forwards(value = { @Forward(name = "createApplication", path = "/auth/createApplication.jsp"),
        @Forward(name = "editApplication", path = "/auth/editApplication.jsp"),
        @Forward(name = "manageAuthorizations", path = "/auth/manageAuthorizations.jsp"),
        @Forward(name = "returnKeys", path = "/auth/returnkeys.jsp"),
        @Forward(name = "manageApplications", path = "/auth/manageApplications.jsp"),
        @Forward(name = "viewAuthorizations", path = "/auth/viewAuthorizations.jsp"),
        @Forward(name = "viewApplicationDetails", path = "/auth/viewApplicationDetails.jsp"),
        @Forward(name = "viewAllApplications", path = "/auth/viewAllApplications.jsp"),
        @Forward(name = "viewAllAuthorizations", path = "/auth/viewAllAuthorizations.jsp"),
        @Forward(name = "viewAllSessions", path = "/auth/viewAllSessions.jsp") })
public class ExternalAppsDA extends FenixDispatchAction {

    private User getUser() {
        return getLoggedPerson(null).getUser();
    }

    private void addAllowIstIds(HttpServletRequest request) {
        if (getUser().getPerson().hasRole(RoleType.MANAGER)) {
            request.setAttribute("allowIstIds", JerseyOAuth2Filter.allowIstIds());
        }
    }

    /** This will list the applications which you grant access */
    public ActionForward allowIstIds(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Person person = getLoggedPerson(request);
        if (person.hasRole(RoleType.MANAGER)) {
            JerseyOAuth2Filter.toggleAllowIstIds();
            return redirect("/externalApps.do?method=manageApplications", request, true);
        } else {
            throw new ServletException("no.permissions");
        }
    }

    /** This will list the applications which you grant access */
    public ActionForward manageAuthorizations(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        User user = getUser();

        ImmutableSet<ExternalApplication> authApps =
                FluentIterable.from(user.getAppUserAuthorizationSet())
                        .transform(new Function<AppUserAuthorization, ExternalApplication>() {

                            @Override
                            public ExternalApplication apply(AppUserAuthorization appUserSession) {
                                return appUserSession.getApplication();
                            }
                        }).toSet();

        request.setAttribute("authApps", authApps);

        return mapping.findForward("manageAuthorizations");
    }

    public ActionForward revokeApplication(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        User user = getUser();
        ExternalApplication application = getDomainObject(request, "appOid");

        revokeAllAuthorizations(user, application);

        return manageAuthorizations(mapping, actionForm, request, response);

    }

    public ActionForward viewApplicationDetails(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ExternalApplication application = getDomainObject(request, "appOid");

        request.setAttribute("application", application);

        return mapping.findForward("viewApplicationDetails");
    }

    public ActionForward viewAllApplications(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Set<ExternalApplication> externalApplications = RootDomainObject.getInstance().getAppsSet();

        request.setAttribute("application", externalApplications);

        return mapping.findForward("viewAllApplications");
    }

    public ActionForward viewAllAuthorizations(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ExternalApplication application = getDomainObject(request, "appOid");

        Set<AppUserAuthorization> appUserAuthorization = application.getAppUserAuthorizationSet();

        request.setAttribute("application", application);

        request.setAttribute("userAuthorizations", appUserAuthorization);

        return mapping.findForward("viewAllAuthorizations");
    }

    public ActionForward viewAllSessions(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        AppUserAuthorization appUserAuthorization = getDomainObject(request, "session");

        request.setAttribute("sessions", appUserAuthorization.getSessionSet());
        request.setAttribute("appOid", appUserAuthorization.getApplication().getExternalId());

        return mapping.findForward("viewAllSessions");
    }

    public ActionForward deleteApplication(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ExternalApplication application = getDomainObject(request, "appOid");

        application.delete();

        return manageApplications(mapping, actionForm, request, response);

    }

    @Atomic
    private void revokeAllAuthorizations(User user, ExternalApplication application) {
        AppUserAuthorization appUserAuthorization = application.getAppUserAuthorization(user);
        if (appUserAuthorization != null) {
            appUserAuthorization.delete();
        }
    }

    public ActionForward manageApplications(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        request.setAttribute("appsOwned", getUser().getOwnedAppSet());
        addAllowIstIds(request);
        return mapping.findForward("manageApplications");
    }

    /** This will show individual authorizations */

    public ActionForward viewAuthorizations(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Person p = getUser().getPerson();

        final ExternalApplication app = getDomainObject(request, "appOid");
        AppUserAuthorization appUserAuthorization = app.getAppUserAuthorization(getUser());

        Set<AppUserSession> authSessions = null;

        if (appUserAuthorization != null) {
            authSessions = FluentIterable.from(appUserAuthorization.getSessionSet()).filter(new Predicate<AppUserSession>() {

                @Override
                public boolean apply(AppUserSession appUserSession) {
                    return appUserSession.isRefreshTokenValid();
                }
            }).toSet();
        }

        if (authSessions == null) {
            return redirect("/externalApps.do?method=manageAuthorizations", request, true);
        } else {
            request.setAttribute("logo", Base64.encodeBase64String(app.getLogo()));
            request.setAttribute("authorizations", authSessions);
            request.setAttribute("application", app);
            return mapping.findForward("viewAuthorizations");
        }
    }

    public ActionForward revokeAuth(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        AppUserSession appUserSession = getDomainObject(request, "authorizationOid");
        String appExternalId = appUserSession.getAppUserAuthorization().getApplication().getExternalId();

        appUserSession.delete();

        request.setAttribute("appOid", appExternalId);
        return viewAuthorizations(mapping, actionForm, request, response);
    }

    public ActionForward createApplication(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        RenderUtils.invalidateViewState();
        return redirect("/externalApps.do?method=manageApplications", request, true);
    }

    public ActionForward prepareCreateApplication(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("currentUser", getUser());
        return mapping.findForward("createApplication");
    }

    public ActionForward prepareEditApplication(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ExternalApplication app = getDomainObject(request, "appOid");
        request.setAttribute("application", app);
        return mapping.findForward("editApplication");
    }

    public ActionForward appLogo(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final ExternalApplication app = getDomainObject(request, "appOid");

        response.setContentType("image");
        ServletOutputStream outputStream = response.getOutputStream();
        byte[] logo = app.getLogo();
        if (logo != null && logo.length > 0) {
            outputStream.write(logo);
        } else {
            InputStream placeholder = getClass().getResourceAsStream("/externalAppPlaceholder.jpg");
            IOUtils.copy(placeholder, outputStream);
        }
        outputStream.flush();
        response.flushBuffer();
        return null;
    }

}
