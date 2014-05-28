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
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.publico.ProtectedItem;
import net.sourceforge.fenixedu.presentationTier.Action.utils.RequestUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;

import pt.ist.fenixframework.FenixFramework;

/**
 * Generic action to coordinate the visualization of a website.
 * 
 * 
 * @author cfgi
 */
public abstract class SiteVisualizationDA extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String directLinkContext = getDirectLinkContext(request);
        if (directLinkContext != null) {
            request.setAttribute("directLinkContext", directLinkContext);
        }

        setSectionBreadCrumbs(request);
        return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward item(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Item item = selectItem(request);

        if (item == null) {
            return getSiteDefaultView(mapping, form, request, response);
        }

        User userView = prepareUserView(request);

        if (item.isAvailable()) {
            return mapping.findForward("site-item");
        } else {
            if (isAuthenticated(userView)) {
                return mapping.findForward("site-item-deny");
            } else {
                return mapping.findForward("site-item-adviseLogin");
            }
        }
    }

    public ActionForward section(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Section section = selectSection(request);

        if (section == null) {
            return getSiteDefaultView(mapping, form, request, response);
        }

        User userView = prepareUserView(request);

        if (section.isAvailable()) {
            prepareProtectedItems(request, userView, section.getChildrenItems());
            return mapping.findForward("site-section");
        } else {
            if (isAuthenticated(userView)) {
                return mapping.findForward("site-section-deny");
            } else {
                return mapping.findForward("site-section-adviseLogin");
            }
        }
    }

    public ActionForward itemWithLogin(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        User userView = getUserView(request);

        if (!isAuthenticated(userView)) {
            RequestUtils.sendLoginRedirect(request, response);
            return null;
        } else {
            return item(mapping, form, request, response);
        }
    }

    public ActionForward sectionWithLogin(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        User userView = getUserView(request);

        if (!isAuthenticated(userView)) {
            RequestUtils.sendLoginRedirect(request, response);
            return null;
        } else {
            return section(mapping, form, request, response);
        }
    }

    private void prepareProtectedItems(HttpServletRequest request, User userView, Collection<Item> items) {
        List<ProtectedItem> protectedItems = setupItems(request, items);

        if (!isAuthenticated(userView) && hasRestrictedItems(protectedItems)) {
            request.setAttribute("hasRestrictedItems", true);
        }
    }

    private boolean hasRestrictedItems(List<ProtectedItem> protectedItems) {
        for (ProtectedItem item : protectedItems) {
            if (!item.isAvailable()) {
                return true;
            }
        }

        return false;
    }

    private List<ProtectedItem> setupItems(HttpServletRequest request, Collection<Item> items) {
        List<ProtectedItem> protectedItems = new ArrayList<ProtectedItem>();
        for (Item item : items) {
            if (item.getVisible()) {
                protectedItems.add(new ProtectedItem(item));
            }
        }

        request.setAttribute("protectedItems", protectedItems);
        return protectedItems;
    }

    private User prepareUserView(HttpServletRequest request) {
        User userView = getUserView(request);
        request.setAttribute("logged", isAuthenticated(userView));

        return userView;
    }

    private boolean isAuthenticated(User userView) {
        return userView != null;
    }

    protected Section selectSection(HttpServletRequest request) {
        return selectSection(request, getSection(request));
    }

    private Section selectSection(HttpServletRequest request, Section section) {
        if (section != null) {
            request.setAttribute("section", section);

            final Set<Section> selectedSections = new HashSet<Section>();
            for (Section currentSection = section; currentSection != null; currentSection = currentSection.getSuperiorSection()) {
                selectedSections.add(currentSection);
            }

            request.setAttribute("selectedSections", selectedSections);
        }

        return section;
    }

    protected Item selectItem(HttpServletRequest request) {
        Item item = getItem(request);

        if (item != null) {
            selectSection(request, item.getSection());
            request.setAttribute("item", item);
        }

        return item;
    }

    private Item getItem(HttpServletRequest request) {
        String parameter = request.getParameter("itemID");

        if (parameter == null) {
            return null;
        }

        return (Item) FenixFramework.getDomainObject(parameter);
    }

    protected Section getSection(final HttpServletRequest request) {
        String parameter = request.getParameter("sectionID");

        if (parameter == null) {
            return null;
        }

        final Section content = FenixFramework.getDomainObject(parameter);
        return content instanceof Section ? content : null;
    }

    protected void setSectionBreadCrumbs(HttpServletRequest request) {
        Section section = selectSection(request);
        Item item = selectItem(request);

        List<Section> sections = new ArrayList<Section>();

        if (section != null && item == null) {
            section = section.getSuperiorSection();
        }

        while (section != null) {
            sections.add(0, section);

            section = section.getSuperiorSection();
        }

        request.setAttribute("sectionCrumbs", sections);
    }

    protected abstract ActionForward getSiteDefaultView(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response);

    protected String getDirectLinkContext(HttpServletRequest request) {
        return null;
    }
}
