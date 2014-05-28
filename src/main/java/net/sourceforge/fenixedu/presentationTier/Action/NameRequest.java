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
package net.sourceforge.fenixedu.presentationTier.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;

import org.apache.commons.lang.CharEncoding;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.lowagie.text.DocumentException;

@Mapping(path = "/NameResolution", module = "external")
public class NameRequest extends FenixDispatchAction {

    private static final String storedPassword;
    private static final String storedUsername;

    static {
        storedUsername = FenixConfigurationManager.getConfiguration().getNameResolutionName();
        storedPassword = FenixConfigurationManager.getConfiguration().getNameResolutionPassword();
    }

    public ActionForward resolve(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            final HttpServletResponse httpServletResponse) throws Exception {

        String digest = Hashing.sha1().hashString(storedPassword, Charsets.UTF_8).toString();
        String providedUsername = request.getParameter("username");
        String providedDigest = request.getParameter("password");

        if (storedUsername.equals(providedUsername) && digest.equals(providedDigest)) {
            String id = request.getParameter("id");
            User user = User.findByUsername(id);

            String name = user.getPerson().getName();
            String nickName = user.getPerson().getNickname();
            httpServletResponse.setHeader("Content-Type", "application/json; charset=" + CharEncoding.UTF_8);
            String message = "{\n" + "\"name\" : \"" + name + "\",\n" + "\"nickName\" : \"" + nickName + "\"\n" + "}";
            httpServletResponse.getOutputStream().write(message.getBytes(CharEncoding.UTF_8));
            httpServletResponse.getOutputStream().close();
            return null;

        } else {
            throw new DocumentException("invalid.authentication");
        }

    }

}
