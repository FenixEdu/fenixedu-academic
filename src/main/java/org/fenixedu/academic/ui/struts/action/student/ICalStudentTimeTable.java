/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.student;

import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.UserPrivateKey;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.dto.messaging.RegistrationsBean;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.student.StudentApplication.StudentViewApp;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.joda.time.DateTime;

import pt.ist.fenixframework.FenixFramework;

import com.google.common.hash.Hashing;

@StrutsFunctionality(app = StudentViewApp.class, descriptionKey = "label.title.sync", path = "sync",
        bundle = "MessagingResources", titleKey = "label.title.sync")
@Mapping(path = "/ICalTimeTable", module = "student")
@Forwards({ @Forward(name = "viewOptions", path = "/student/iCalendar/viewCalendarInformation.jsp"),
        @Forward(name = "chooseRegistration", path = "/student/iCalendar/chooseRegistration.jsp") })
public class ICalStudentTimeTable extends FenixDispatchAction {

    public ActionForward show(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        if (getRenderedObject("bean") == null) {
            Registration registration = FenixFramework.getDomainObject(request.getParameter("registrationId"));

            return forwardToShow(registration, mapping, request);
        } else {
            if (((RegistrationsBean) getRenderedObject("bean")).getSelected() != null) {
                return forwardToShow(((RegistrationsBean) getRenderedObject("bean")).getSelected(), mapping, request);
            } else {
                return prepare(mapping, form, request, response);
            }

        }

    }

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        List<Registration> registrations = getUserView(request).getPerson().getStudent().getActiveRegistrations();
        if (registrations.size() == 1) {
            return forwardToShow(registrations.iterator().next(), mapping, request);
        } else {
            RegistrationsBean bean = new RegistrationsBean();
            bean.setRegistrations(registrations);
            request.setAttribute("bean", bean);
            return mapping.findForward("chooseRegistration");
        }
    }

    public ActionForward generateKey(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        try {
            UserPrivateKey.generateNewKeyForUser(Authenticate.getUser());
        } catch (Exception E) {
            throw new DomainException("error.impossible.to.generate.sha256.key");
        }

        return this.redirect("/ICalTimeTable.do?method=show&registrationId=" + request.getParameter("registrationId"), request);
    }

    private ActionForward forwardToShow(Registration registration, ActionMapping mapping, HttpServletRequest request)
            throws Exception {
        request.setAttribute("registrationId", registration.getExternalId());

        UserPrivateKey privateKey = Authenticate.getUser().getPrivateKey();

        if (privateKey != null && privateKey.getPrivateKeyValidity() != null
                && privateKey.getPrivateKeyValidity().isAfter(new DateTime())) {
            if (privateKey.getPrivateKeyValidity() != null) {
                request.setAttribute("expirationDate", privateKey.getPrivateKeyValidity().toString("dd/MM/yyyy HH:mm"));
                request.setAttribute("user", Authenticate.getUser().getUsername());
                request.setAttribute("classURL", getUrl("syncClasses", registration, request));
                request.setAttribute("examsURL", getUrl("syncExams", registration, request));
            }
            request.setAttribute("stillValid", true);
        } else {
            request.setAttribute("stillValid", false);
            request.setAttribute("payload", "");
        }
        return mapping.findForward("viewOptions");
    }

    public static String calculatePayload(String to, Registration reg, User user) throws Exception {

        Cipher cipher = Cipher.getInstance("AES");

        SecretKeySpec skeySpec = new SecretKeySpec(user.getPrivateKey().getPrivateKey(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        byte[] encrypted =
                cipher.doFinal(("This is for " + to + " calendar ##" + "1.6180339##Sistema Fenix##"
                        + user.getPrivateKey().getPrivateKeyCreation().toString() + "##" + reg.getExternalId() + "##"
                        + user.getPrivateKey().getPrivateKeyValidity().toString() + "##" + "## This is for " + to + " calendar")
                        .getBytes(StandardCharsets.UTF_8));

        return Hashing.sha1().hashBytes(encrypted).toString();
    }

    public static String getUrl(String to, Registration registration, HttpServletRequest request) throws Exception {
        try {
            String scheme = request.getScheme();
            String serverName = request.getServerName();
            int serverPort = request.getServerPort();
            String url =
                    scheme + "://" + serverName + ((serverPort == 80 || serverPort == 443) ? "" : ":" + serverPort)
                            + request.getContextPath();
            url +=
                    "/external/iCalendarSync.do?method=" + to + "&user=" + Authenticate.getUser().getUsername() + ""
                            + "&registrationID=" + registration.getExternalId() + "&payload="
                            + calculatePayload(to, registration, Authenticate.getUser());
            return url;
        } catch (Exception e) {
            return null;
        }
    }

}
