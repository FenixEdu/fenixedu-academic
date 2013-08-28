package net.sourceforge.fenixedu.presentationTier.Action.person;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.AppUserSession;
import net.sourceforge.fenixedu.domain.ExternalApplication;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
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
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(module = "person", path = "/externalAuth")
@Forwards(value = { @Forward(name = "registerApp", path = "/auth/registerAppRequest.jsp"),
        @Forward(name = "editApp", path = "/auth/editAppRequest.jsp"),
        @Forward(name = "externalAuthApp", path = "/auth/externalAppRequest.jsp"),
        @Forward(name = "returnKeys", path = "/auth/returnkeys.jsp"),
        @Forward(name = "myApps", path = "/auth/showOwnedApps.jsp"),
        @Forward(name = "mySessions", path = "/auth/showAuthSessions.jsp")

})
public class AuthDispatchAction extends FenixDispatchAction {

    public ActionForward allowExternalApp(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = UserView.getUser();
        Person p = userView.getPerson();
        List<ExternalApplication> authApps = p.getUser().getAppAuthorized();

        request.setAttribute("authApps", authApps);

        System.out.println("-@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        System.out.println("authApps size: " + (authApps != null ? authApps.size() : 0));
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

        return mapping.findForward("externalAuthApp");
    }

    public ActionForward registerExternalApp(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = UserView.getUser();
        User user = userView.getPerson().getUser();

        request.setAttribute("user", user);

        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        System.out.println("number of existent apps: " + RootDomainObject.getInstance().getApps().size());
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

        return mapping.findForward("registerApp");
    }

    public ActionForward editApp(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = UserView.getUser();
        User user = userView.getPerson().getUser();
        List<ExternalApplication> apps = user.getAppOwned();

        request.setAttribute("apps", apps);

        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        System.out.println("number of owned apps: " + (apps != null ? apps.size() : 0));
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

        return mapping.findForward("editApp");

    }

    @Service
    public ActionForward revokeEditedAppAuth(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = UserView.getUser();
        User user = userView.getPerson().getUser();

        String editedAppOid = request.getParameter("editedApp");
        ExternalApplication editedApp = AbstractDomainObject.fromExternalId(editedAppOid);

        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        System.out.println("Edited App: " + editedApp.getName());
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

        List<AppUserSession> sessions = new ArrayList<AppUserSession>(editedApp.getAppUserSession());
        for (AppUserSession s : sessions) {
            s.delete();
        }

        List<ExternalApplication> appsOwned = user.getAppOwned();
        request.setAttribute("appsOwned", appsOwned);

        return mapping.findForward("myApps");

    }

    public ActionForward listApps(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = UserView.getUser();
        Person p = userView.getPerson();

        List<ExternalApplication> appsOwned = p.getUser().getAppOwned();
        request.setAttribute("appsOwned", appsOwned);

        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        System.out.println("User: " + p.getUsername());
        System.out.println("appsOwned size: " + (appsOwned != null ? appsOwned.size() : 0));
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

        return mapping.findForward("myApps");
    }

    public ActionForward displayAppUserSessions(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = UserView.getUser();
        Person p = userView.getPerson();

        String appOid = request.getParameter("oid");
        ExternalApplication app = AbstractDomainObject.fromExternalId(appOid);

        List<AppUserSession> authSessions = new ArrayList<AppUserSession>();

        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        System.out.println("App: " + app.getName());
        System.out.println("user: " + p.getUsername());
        System.out.println("app.getAppUserSession() size: " + app.getAppUserSession().size());
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

        for (AppUserSession appSession : app.getAppUserSession()) {
            System.out.println("### app session !null ? " + (appSession != null) + "");
            System.out.println("### app session user !null ? " + (appSession.getUser() != null) + "");
            if (appSession.getUser().equals(p.getUser())) {
                System.out.println("### equals " + appSession.getUsername() + " - " + p.getUsername());
                authSessions.add(appSession);
            }
        }

        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        System.out.println("sessions: " + authSessions.size());
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

        request.setAttribute("authSessions", authSessions);
        request.setAttribute("applicationOid", appOid);

        return mapping.findForward("mySessions");
    }

    @Service
    public ActionForward removeAuth(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = UserView.getUser();
        Person p = userView.getPerson();
        User u = p.getUser();

        String oid = request.getParameter("oid");
        ExternalApplication app = AbstractDomainObject.fromExternalId(oid);

        String deviceId = request.getParameter("deviceId");

        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        System.out.println("oid: " + oid);
        System.out.println("deviceId: " + deviceId);
        System.out.println("sessionsAuth size: "
                + (p.getUser().getAppUserSession() != null ? p.getUser().getAppUserSession().size() : 0));
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

        List<AppUserSession> appUserSessions = u.getAppUserSession();

        for (AppUserSession aus : appUserSessions) {
            if (aus.getApplication().equals(app) && aus.getDeviceId().equals(deviceId)) {
                System.out.println("######  founded #####");
                appUserSessions.remove(aus);
                u.removeAppUserSession(aus);
                aus.getApplication().removeAppUserSession(aus);
                aus.delete();
                break;
            }
        }

        List<ExternalApplication> authApps = p.getUser().getAppAuthorized();
        request.setAttribute("authApps", authApps);

        return mapping.findForward("externalAuthApp");
    }

    @Service
    public ActionForward removeAllAuths(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = UserView.getUser();
        Person p = userView.getPerson();
        User u = p.getUser();

        String oid = request.getParameter("appOid");
        ExternalApplication app = AbstractDomainObject.fromExternalId(oid);

        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        System.out.println("app: " + app.getName());
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

        List<AppUserSession> appUserSessions = u.getAppUserSession();

        for (AppUserSession aus : appUserSessions) {
            if (aus.getApplication().equals(app)) {
                System.out.println("######  founded #####");
                appUserSessions.remove(aus);
                u.removeAppUserSession(aus);
                aus.getApplication().removeAppUserSession(aus);
                aus.delete();
            }
        }

        List<ExternalApplication> authApps = p.getUser().getAppAuthorized();
        request.setAttribute("authApps", authApps);

        return mapping.findForward("externalAuthApp");
    }

    /*
     * method used only for testing, allowing to add an owned app to the user's allowedApps list
     */
    @Service
    public ActionForward addAuth(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = UserView.getUser();
        Person p = userView.getPerson();

        String oid = request.getParameter("oid");
        ExternalApplication app = AbstractDomainObject.fromExternalId(oid);

        String deviceId = request.getParameter("deviceId");

        List<ExternalApplication> authApps = p.getUser().getAppAuthorized();
        request.setAttribute("authApps", authApps);

        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        System.out.println("oid: " + oid);
        System.out.println("AppsAuth size: " + (authApps != null ? authApps.size() : 0));
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

        app.addUser(p.getUser());
        p.getUser().getAppAuthorized().add(app);

        AppUserSession aus = new AppUserSession();
        aus.setApplication(app);
        aus.setUser(p.getUser());
        aus.setDeviceId(deviceId);

        p.getUser().addAppUserSession(aus);
        app.addAppUserSession(aus);

        request.setAttribute("authApps", authApps);

        return mapping.findForward("externalAuthApp");
    }
}