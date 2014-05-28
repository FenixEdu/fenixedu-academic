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

import net.sourceforge.fenixedu.util.FenixConfigurationManager;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.domain.MenuItem;
import org.fenixedu.bennu.portal.domain.PortalConfiguration;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/home")
public class HomeAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final MenuItem initialMenuEntry = findTopLevelContainer();
        if (initialMenuEntry == null) {
            response.sendRedirect(FenixConfigurationManager.getConfiguration().getIndexPageRedirect());
        } else {
            response.sendRedirect(request.getContextPath() + initialMenuEntry.getFullPath());
        }

        return null;
    }

    private MenuItem findTopLevelContainer() {
        for (MenuItem item : PortalConfiguration.getInstance().getMenu().getOrderedChild()) {
            if (item.isAvailableForCurrentUser() && item.isVisible()) {
                return item;
            }
        }
        return null;
    }

}
