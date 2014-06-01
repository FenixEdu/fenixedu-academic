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
package net.sourceforge.fenixedu.presentationTier.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.File;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;

import org.fenixedu.bennu.core.filters.CasAuthenticationFilter;
import org.fenixedu.bennu.core.util.CoreConfiguration;

import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;

import com.google.common.base.Charsets;

@WebServlet(urlPatterns = FileDownloadServlet.SERVLET_PATH + "*")
public class FileDownloadServlet extends HttpServlet {

    private static final long serialVersionUID = 6954413451468325605L;

    static final String SERVLET_PATH = "/downloadFile/";

    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
        final File file = getFileFromURL(request.getRequestURI());
        if (file == null) {
            request.getRequestDispatcher("/notFound.jsp").forward(request, response);
        } else {
            // Translate old paths (/downloadFile/<oid>) to the new ones (/downloadFile/<oid>/<filename>)
            if (request.getPathInfo().equals("/" + file.getExternalId())) {
                response.sendRedirect(file.getDownloadUrl());
                return;
            }
            final Person person = AccessControl.getPerson();
            if (!file.isPrivate() || file.isPersonAllowedToAccess(person)) {
                byte[] content = file.getContent();
                response.setContentType(file.getContentType());
                response.setContentLength(file.getSize().intValue());
                try (OutputStream stream = response.getOutputStream()) {
                    stream.write(content);
                    stream.flush();
                }
            } else if (file.isPrivate() && person == null
                    && request.getAttribute(CasAuthenticationFilter.AUTHENTICATION_EXCEPTION_KEY) == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.sendRedirect(sendLoginRedirect(request, file));
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                request.getRequestDispatcher("/unauthorized.jsp").forward(request, response);
            }
        }
    }

    private String sendLoginRedirect(final HttpServletRequest request, final File file) throws IOException {
        if (CoreConfiguration.casConfig().isCasEnabled()) {
            return CoreConfiguration.casConfig().getCasLoginUrl(URLEncoder.encode(file.getDownloadUrl(), Charsets.UTF_8.name()));
        }
        return FenixConfigurationManager.getConfiguration().getLoginPage() + "?service=" + request.getRequestURI();
    }

    public final static File getFileFromURL(String url) {
        try {
            // Remove trailing path, and split the tokens
            String[] parts = url.substring(url.indexOf(SERVLET_PATH)).replace(SERVLET_PATH, "").split("\\/");
            if (parts.length == 0) {
                return null;
            }
            DomainObject object = FenixFramework.getDomainObject(parts[0]);
            if (object instanceof File && FenixFramework.isDomainObjectValid(object)) {
                return (File) object;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

}
