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
package org.fenixedu.academic.ui.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.domain.MenuContainer;
import org.fenixedu.bennu.portal.domain.MenuItem;
import org.fenixedu.bennu.portal.domain.PortalConfiguration;
import org.fenixedu.bennu.struts.annotations.Mapping;

@Mapping(path = "/home")
public class HomeAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final String path = findFirstFuntionalityPath(request);
        response.sendRedirect(path);
        return null;
    }

    public static String findFirstFuntionalityPath(final HttpServletRequest request) {
        final MenuItem initialMenuEntry = findTopLevelContainer(PortalConfiguration.getInstance().getMenu());
        final String contextPath = request.getContextPath();
        return initialMenuEntry == null ? contextPath : contextPath + initialMenuEntry.getFullPath();
    }

    private static MenuItem findTopLevelContainer(final MenuContainer container) {
        for (final MenuItem item : container.getOrderedChild()) {
            if (item.isVisible() && item.isAvailableForCurrentUser()) {
                return item instanceof MenuContainer ? findTopLevelContainer((MenuContainer) item) : item;
            }
        }
        return null;
    }

}
