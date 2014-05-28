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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/loginCAS")
public class CASAuthenticationAction extends BaseAuthenticationAction {

    @Override
    protected User doAuthentication(ActionForm form, HttpServletRequest request) {
        // Actual authentication is performed by CasAuthenticationFilter, so just return the user
        return Authenticate.getUser();
    }

    /*
     * When authentication fails on CAS
     */
    @Override
    protected ActionForward getAuthenticationFailedForward(ActionMapping mapping, HttpServletRequest request, String actionKey,
            String messageKey) {
        Authenticate.logout(request.getSession());
        return new ActionForward("/authenticationFailed.jsp");
    }

}