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
package net.sourceforge.fenixedu.presentationTier.Action.externalServices;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.externalServices.SetEmail;
import net.sourceforge.fenixedu.applicationTier.Servico.externalServices.SetEmail.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.externalServices.SetEmail.UserAlreadyHasEmailException;
import net.sourceforge.fenixedu.applicationTier.Servico.externalServices.SetEmail.UserDoesNotExistException;
import net.sourceforge.fenixedu.domain.candidacy.DegreeCandidacy;
import net.sourceforge.fenixedu.domain.candidacy.IMDCandidacy;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.HostAccessControl;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.protocol.DefaultProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.SSLProtocolSocketFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "external", path = "/setEmail", scope = "request", parameter = "method")
public class SetEmailDA extends FenixDispatchAction {

    private static final Logger logger = LoggerFactory.getLogger(SetEmailDA.class);

    public ActionForward setEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        final String host = HostAccessControl.getRemoteAddress(request);
        final String ip = request.getRemoteAddr();
        final String password = request.getParameter("password");
        final String userUId = request.getParameter("userUId");
        final String email = URLDecoder.decode(request.getParameter("email"), "UTF-8");

        String message = "ko";

        try {

            SetEmail.run(host, ip, password, userUId, email);
            final User user = User.findByUsername(userUId);
            if (user.getPerson() != null && user.getPerson().hasStudent()) {
                final Student student = user.getPerson().getStudent();
                for (final Registration registration : student.getRegistrationsSet()) {
                    final StudentCandidacy candidacy = registration.getStudentCandidacy();
                    if (candidacy != null && (candidacy instanceof DegreeCandidacy || candidacy instanceof IMDCandidacy)
                            && candidacy.getExecutionYear().isCurrent() && candidacy.hasAnyCandidacySituations()) {
                        new PDFGeneratorThread(candidacy.getExternalId(), request.getServerName(), request.getServerPort(),
                                request.getContextPath(), request.getServletPath()).start();
                    }
                }
            }
            message = "ok";
        } catch (NotAuthorizedException ex) {
            message = "Not authorized";
        } catch (UserAlreadyHasEmailException ex) {
            message = "User already has email.";
        } catch (UserDoesNotExistException ex) {
            message = "User does not exist.";
        } catch (Throwable ex) {
            message = ex.getMessage();
            logger.error(ex.getMessage(), ex);
        } finally {
            final ServletOutputStream servletOutputStream = response.getOutputStream();
            response.setContentType("text/html");
            servletOutputStream.print(message);
            servletOutputStream.flush();
            response.flushBuffer();
        }

        return null;
    }

    private static class PDFGeneratorThread extends Thread {

        private final String candidacyId;
        private final String serverName;
        private final int serverPort;
        private final String serverPath;
        private final String contextPath;

        private PDFGeneratorThread(final String candidacyId, String serverName, final int serverPort, final String contextPath,
                String serverPath) {
            this.candidacyId = candidacyId;
            this.serverName = serverName;
            this.serverPort = serverPort;
            this.contextPath = contextPath;
            this.serverPath = serverPath;
        }

        @Override
        public void run() {
            HttpClient httpClient = new HttpClient();
            final Protocol protocol;
            if (serverPort == 80 || serverPort == 8080) {
                protocol = new Protocol("http", new DefaultProtocolSocketFactory(), serverPort);
            } else if (serverPort == 443 || serverPort == 8443) {
                protocol = new Protocol("https", new SSLProtocolSocketFactory(), serverPort);
            } else {
                throw new Error("Unknown protocol for port: " + serverPort);
            }

            httpClient.getHostConfiguration().setHost(serverName, serverPort, protocol);
            httpClient.getState().setCookiePolicy(CookiePolicy.COMPATIBILITY);
            httpClient.setConnectionTimeout(30000000);
            httpClient.setStrictMode(true);

            final String url =
                    contextPath
                            + "/publico/regenerateDocuments.do?method=doOperation&operationType=PRINT_ALL_DOCUMENTS&candidacyID="
                            + candidacyId;

            final GetMethod method = new GetMethod(url);
            method.setFollowRedirects(false);
            try {
                httpClient.executeMethod(method);
            } catch (final IOException e) {
                throw new Error(e);
            }
        }

    }

}
