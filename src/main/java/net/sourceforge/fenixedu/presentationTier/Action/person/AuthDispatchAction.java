package net.sourceforge.fenixedu.presentationTier.Action.person;

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
        @Forward(name = "returnKeys", path = "/auth/returnkeys.jsp"), @Forward(name = "myApps", path = "/auth/showOwnedApps.jsp")

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

        //Should remove auth since important properties may have been edited?
    }

    public ActionForward listApps(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = UserView.getUser();
        Person p = userView.getPerson();
        List<ExternalApplication> appsOwned = p.getUser().getAppOwned();
        request.setAttribute("appsOwned", appsOwned);

        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        System.out.println("appsOwned size: " + (appsOwned != null ? appsOwned.size() : 0));
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

        return mapping.findForward("myApps");
    }

    @Service
    public ActionForward removeAuth(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = UserView.getUser();
        Person p = userView.getPerson();

        String oid = request.getParameter("oid");
        ExternalApplication app = AbstractDomainObject.fromExternalId(oid);

        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        System.out.println("oid: " + oid);
        System.out.println("appsAuth size: "
                + (p.getUser().getAppAuthorized() != null ? p.getUser().getAppAuthorized().size() : 0));
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

        p.getUser().getAppAuthorized().remove(app);

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

        List<ExternalApplication> authApps = p.getUser().getAppAuthorized();

        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        System.out.println("oid: " + oid);
        System.out.println("appsAuth size: " + (authApps != null ? authApps.size() : 0));
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

        app.addUser(p.getUser());
        //p.getUser().getAppAuthorized().add(app);

        AppUserSession aus = new AppUserSession();
        aus.setApplication(app);
        aus.setUser(p.getUser());

        request.setAttribute("authApps", authApps);

        return mapping.findForward("externalAuthApp");
    }

}