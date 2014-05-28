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

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "external", path = "/retrieveDisplayName", scope = "request", parameter = "method")
public class RetrieveDisplayName extends ExternalInterfaceDispatchAction {

    public ActionForward check(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final String responseMessage;
        if (FenixConfigurationManager.getHostAccessControl().isAllowed(this, request)) {
            final String username = request.getParameter("username");
            responseMessage = getNickname(username);
        } else {
            responseMessage = "";
        }

        response.setContentType("text/plain");

        final OutputStream outputStream = response.getOutputStream();
        outputStream.write(responseMessage.getBytes());
        outputStream.close();

        return null;
    }

    private String getNickname(final String username) {
        return username == null || username.isEmpty() ? "" : getNickname(User.findByUsername(username));
    }

    private String getNickname(final User user) {
        return user == null ? "" : getNickname(user.getPerson());
    }

    private String getNickname(final Person person) {
        return person == null ? "" : person.getNickname();
    }

}
