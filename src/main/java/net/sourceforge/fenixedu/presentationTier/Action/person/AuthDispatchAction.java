package net.sourceforge.fenixedu.presentationTier.Action.person;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.AppUserSession;
import net.sourceforge.fenixedu.domain.ExternalApplication;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

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
        @Forward(name = "editApp", path = "/auth/editAppRequest.jsp"),
        @Forward(name = "manageAuthorizations", path = "/auth/manageAuthorizations.jsp"),
        @Forward(name = "returnKeys", path = "/auth/returnkeys.jsp"),
        @Forward(name = "manageApplications", path = "/auth/manageApplications.jsp"),
        @Forward(name = "viewAuthorizations", path = "/auth/viewAuthorizations.jsp")

})
public class AuthDispatchAction extends FenixDispatchAction {

    private User getUser() {
        IUserView user = UserView.getUser();
        return user.getPerson().getUser();
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

        request.setAttribute("authorizations", authSessions);
        request.setAttribute("appOid", app.getExternalId());

        return mapping.findForward("viewAuthorizations");
    }

    public ActionForward removeAuth(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        AppUserSession appUserSession = getDomainObject(request, "authorizationOid");
        String appExternalId = appUserSession.getApplication().getExternalId();

        appUserSession.delete();

        request.setAttribute("appOid", appExternalId);
        return viewAuthorizations(mapping, actionForm, request, response);
    }
    
    public ActionForward prepareCreateApplication(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	return mapping.findForward("createApplication");
    }
}