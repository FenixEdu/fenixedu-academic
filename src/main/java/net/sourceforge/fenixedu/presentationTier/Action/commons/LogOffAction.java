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
/*
 * LogOffAction.java
 * 
 * 
 * Created on 09 de Dezembro de 2002, 16:37
 * 
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package net.sourceforge.fenixedu.presentationTier.Action.commons;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.bennu.core.util.CoreConfiguration.CasConfig;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/logoff")
public class LogOffAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        HttpSession session = request.getSession(false);

        ActionForward result = null;

        final CasConfig casConfig = CoreConfiguration.casConfig();

        if (casConfig != null && casConfig.isCasEnabled()) {
            if (request.getParameter("logoutFromCAS") != null && request.getParameter("logoutFromCAS").equals("true")) {
                Authenticate.logout(session);
                result = new ActionForward("/commons/blankWithTitle.jsp");
            } else {
                result = getCasLogoutActionForward(casConfig);
                Authenticate.logout(session);
            }
        } else {
            Authenticate.logout(session);
            result = new ActionForward("/loginPage.jsp");
        }

        return result;
    }

    private ActionForward getCasLogoutActionForward(CasConfig casConfig) {
        ActionForward actionForward = new ActionForward();

        actionForward.setRedirect(true);
        actionForward.setPath(casConfig.getCasLogoutUrl());

        return actionForward;
    }

}
