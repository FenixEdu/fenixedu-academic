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
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.externalServices.ConsultRoles;
import net.sourceforge.fenixedu.applicationTier.Servico.externalServices.ConsultRoles.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.util.HostAccessControl;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "external", path = "/consultRoles", scope = "request", parameter = "method")
public class ConsultRolesAction extends FenixAction {

    private static final Logger logger = LoggerFactory.getLogger(ConsultRolesAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        final String host = HostAccessControl.getRemoteAddress(request);
        final String ip = request.getRemoteAddr();
        final String password = request.getParameter("password");
        final String userUId = request.getParameter("userUId");

        String message = "ko";

        try {

            final Set<Role> roles = ConsultRoles.run(host, ip, password, userUId);
            final StringBuilder stringBuilder = new StringBuilder();
            if (roles == null) {
                stringBuilder.append("User does not exist");
            } else {
                stringBuilder.append("ok");
                for (final Role role : roles) {
                    stringBuilder.append('\n');
                    stringBuilder.append(role.getRoleType().getName());
                }
                stringBuilder.append('\n');
            }
            message = stringBuilder.toString();
        } catch (NotAuthorizedException ex) {
            message = "Not authorized";
        } catch (Throwable ex) {
            message = ex.getMessage();
            logger.error(ex.getMessage(), ex);
        } finally {
            writeResponse(response, message);
        }

        return null;
    }

    private void writeResponse(final HttpServletResponse response, final String message) throws IOException {
        final ServletOutputStream servletOutputStream = response.getOutputStream();
        response.setContentType("text/html");
        servletOutputStream.print(message);
        servletOutputStream.flush();
        response.flushBuffer();
    }

}