package net.sourceforge.fenixedu.presentationTier.Action.person;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.AppUserSession;
import net.sourceforge.fenixedu.domain.ExternalApplication;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.commons.FenixActionForward;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.JerseyOAuth2;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

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
        @Forward(name = "viewApplicationDetails", path = "/auth/viewApplicationDetails.jsp")

})
public class AuthDispatchAction extends FenixDispatchAction {

    private User getUser() {
        IUserView user = UserView.getUser();
        return user.getPerson().getUser();
    }
    
    
    private void addAllowIstIds(HttpServletRequest request) {
        if (getUser().getPerson().hasRole(RoleType.MANAGER)) {
            request.setAttribute("allowIstIds", JerseyOAuth2.allowIstIds());
        }
    }
    
    /** This will list the applications which you grant access */
    public ActionForward allowIstIds(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        User user = getUser();
        if (user.getPerson().hasRole(RoleType.MANAGER)) {
            JerseyOAuth2.toggleAllowIstIds();
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
                FluentIterable.from(user.getAppUserSessionSet()).transform(new Function<AppUserSession, ExternalApplication>() {

                    @Override
                    public ExternalApplication apply(AppUserSession appUserSession) {
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

    public ActionForward deleteApplication(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ExternalApplication application = getDomainObject(request, "appOid");

        application.delete();

        return manageApplications(mapping, actionForm, request, response);

    }

    @Service
    private void revokeAllAuthorizations(User user, ExternalApplication application) {
        Set<AppUserSession> sessions = new HashSet<AppUserSession>(user.getAppUserSessionSet());

        for (AppUserSession session : sessions) {
            if (session.getApplication().equals(application)) {
                session.delete();
            }
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

        Set<AppUserSession> authSessions =
                FluentIterable.from(p.getUser().getAppUserSessionSet()).filter(new Predicate<AppUserSession>() {

                    @Override
                    public boolean apply(AppUserSession appUserSession) {
                        return appUserSession.getApplication().equals(app);
                    }

                }).toSet();

        if (authSessions.isEmpty()) {
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
        String appExternalId = appUserSession.getApplication().getExternalId();

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
