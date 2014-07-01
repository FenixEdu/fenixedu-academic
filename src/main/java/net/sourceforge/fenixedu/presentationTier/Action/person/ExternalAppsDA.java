/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.Action.person;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.AppUserAuthorization;
import net.sourceforge.fenixedu.domain.AppUserSession;
import net.sourceforge.fenixedu.domain.ExternalApplication;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.person.PersonApplication.ExternalApplicationsApp;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.JerseyOAuth2Filter;
import net.sourceforge.fenixedu.util.Bundle;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;
import org.fenixedu.bennu.portal.servlet.PortalLayoutInjector;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import com.google.common.hash.Hashing;
import com.google.common.io.ByteStreams;

@StrutsFunctionality(app = ExternalApplicationsApp.class, path = "manage-applications",
        titleKey = "oauthapps.label.manage.applications")
@Mapping(module = "person", path = "/externalApps")
@Forwards(value = { @Forward(name = "createApplication", path = "/auth/createApplication.jsp"),
        @Forward(name = "editApplication", path = "/auth/editApplication.jsp"),
        @Forward(name = "editApplicationAdmin", path = "/auth/editApplicationAdmin.jsp"),
        @Forward(name = "manageAuthorizations", path = "/auth/manageAuthorizations.jsp"),
        @Forward(name = "returnKeys", path = "/auth/returnkeys.jsp"),
        @Forward(name = "manageApplications", path = "/auth/manageApplications.jsp"),
        @Forward(name = "viewAuthorizations", path = "/auth/viewAuthorizations.jsp"),
        @Forward(name = "viewApplicationDetails", path = "/auth/viewApplicationDetails.jsp"),
        @Forward(name = "viewAllApplications", path = "/auth/viewAllApplications.jsp"),
        @Forward(name = "viewAllAuthorizations", path = "/auth/viewAllAuthorizations.jsp"),
        @Forward(name = "viewAllSessions", path = "/auth/viewAllSessions.jsp") })
public class ExternalAppsDA extends FenixDispatchAction {

    @StrutsFunctionality(app = ExternalApplicationsApp.class, path = "manage-authorizations", bundle = "ApplicationResources",
            titleKey = "oauthapps.label.manage.authorizations")
    @Mapping(path = "/externalAppAuthorizations", module = "person")
    public static final class ExternalAppAuthorizationDA extends ExternalAppsDA {

        @Override
        @EntryPoint
        public ActionForward manageAuthorizations(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
                HttpServletResponse response) throws Exception {
            return super.manageAuthorizations(mapping, actionForm, request, response);
        }
    }

    @StrutsFunctionality(app = ExternalApplicationsApp.class, path = "manage-all-authorizations",
            bundle = "ApplicationResources", titleKey = "oauthapps.label.manage.all.applications", accessGroup = "#managers")
    @Mapping(path = "/externalAppAuthorizations", module = "person")
    public static final class ManageAllExternalAppsDA extends ExternalAppsDA {

        @Override
        @EntryPoint
        public ActionForward viewAllApplications(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
                HttpServletResponse response) throws Exception {
            return super.viewAllApplications(mapping, actionForm, request, response);
        }
    }

    private User getUser() {
        return getLoggedPerson(null).getUser();
    }

    private void addAllowIstIds(HttpServletRequest request) {
        if (getUser().getPerson().hasRole(RoleType.MANAGER)) {
            request.setAttribute("allowIstIds", JerseyOAuth2Filter.allowIstIds());
        }
    }

    public ActionForward allowIstIds(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Person person = getLoggedPerson(request);
        if (person.hasRole(RoleType.MANAGER)) {
            JerseyOAuth2Filter.toggleAllowIstIds();
            return redirect("/externalApps.do?method=manageApplications", request);
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
                        }).filter(new Predicate<ExternalApplication>() {

                            @Override
                            public boolean apply(ExternalApplication input) {
                                return input.isActive();
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

        Set<ExternalApplication> externalApplications = Bennu.getInstance().getAppsSet();

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

        application.setDeleted();

        return manageApplications(mapping, actionForm, request, response);

    }

    public ActionForward deleteApplicationAdmin(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ExternalApplication application = getDomainObject(request, "appOid");

        application.setDeleted();

        return redirect("/externalApps.do?method=viewAllApplications", request);

    }

    public ActionForward banApplicationAdmin(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ExternalApplication application = getDomainObject(request, "appOid");

        application.setBanned();

        return redirect("/externalApps.do?method=viewAllApplications", request);

    }

    public ActionForward unbanApplicationAdmin(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ExternalApplication application = getDomainObject(request, "appOid");

        application.setActive();

        return redirect("/externalApps.do?method=viewAllApplications", request);

    }

    @Atomic
    private void revokeAllAuthorizations(User user, ExternalApplication application) {
        AppUserAuthorization appUserAuthorization = application.getAppUserAuthorization(user);
        if (appUserAuthorization != null) {
            appUserAuthorization.delete();
        }
    }

    private String getServiceAgreementHtml() {
        final InputStream resourceAsStream = getClass().getResourceAsStream("/api/serviceAgreement.html");
        if (resourceAsStream == null) {
            return BundleUtil
                    .getString(Bundle.APPLICATION, "oauthapps.default.service.agreement");
        }
        try {
            return new String(ByteStreams.toByteArray(resourceAsStream));
        } catch (IOException e) {
            return BundleUtil
                    .getString(Bundle.APPLICATION, "oauthapps.default.service.agreement");
        }
    }

    @EntryPoint
    public ActionForward manageApplications(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        if (!getUser().getPerson().hasRole(RoleType.DEVELOPER)) {
            request.setAttribute("serviceAgreement", getServiceAgreementHtml());
        }
        request.setAttribute("appsOwned", getUser().getOwnedAppSet());
        Set<ExternalApplication> externalApplicationsSet = getUser().getOwnedAppSet();

        Set<ExternalApplication> externalApplications =
                FluentIterable.from(externalApplicationsSet).filter(new Predicate<ExternalApplication>() {
                    @Override
                    public boolean apply(ExternalApplication externalApplication) {
                        return externalApplication.isActive();
                    }
                }).toSet();

        Set<ExternalApplication> externalApplicationsBanned =
                FluentIterable.from(externalApplicationsSet).filter(new Predicate<ExternalApplication>() {
                    @Override
                    public boolean apply(ExternalApplication externalApplication) {
                        return externalApplication.isBanned();
                    }
                }).toSet();

        request.setAttribute("appsOwned", externalApplications);
        request.setAttribute("appsBanned", externalApplicationsBanned);
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
            return redirect("/externalApps.do?method=manageAuthorizations", request);
        } else {
            if (app.getLogo() != null) {
                request.setAttribute("logo", Base64.getEncoder().encodeToString(app.getLogo()));
            }
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
        return redirect("/externalApps.do?method=manageApplications", request);
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

    public ActionForward prepareEditApplicationAdmin(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ExternalApplication app = getDomainObject(request, "appOid");
        request.setAttribute("application", app);
        return mapping.findForward("editApplicationAdmin");
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
            ByteStreams.copy(placeholder, outputStream);
        }
        outputStream.flush();
        response.flushBuffer();
        return null;
    }

    public ActionForward agreeServiceAgreement(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final String agreedServiceAgreement = request.getParameter("agreedServiceAgreement");
        if ("on".equals(agreedServiceAgreement)) {
            addDeveloperRole(getUser());
        }
        return redirect("/externalApps.do?method=manageApplications", request);
    }

    @Atomic(mode = TxMode.WRITE)
    private void addDeveloperRole(User user) {
        if (user != null) {
            if (!user.getPerson().hasRole(RoleType.DEVELOPER)) {
                user.getPerson().addPersonRoleByRoleType(RoleType.DEVELOPER);
            }
        }
    }

    public ActionForward showServiceAgreement(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final String serviceAgreementHtml = getServiceAgreementHtml();
        request.setAttribute("serviceAgreement", serviceAgreementHtml);
        request.setAttribute("serviceAgreementChecksum", Hashing.md5().newHasher()
                .putString(serviceAgreementHtml, Charsets.UTF_8).hash().toString());
        PortalLayoutInjector.skipLayoutOn(request);
        return new ActionForward(null, "/auth/showServiceAgreement.jsp", false, "");
    }

}
