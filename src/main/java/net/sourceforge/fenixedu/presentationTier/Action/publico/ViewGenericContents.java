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
package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.cms.OldCmsSemanticURLHandler;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * 
 * @author pcma
 * 
 */
@Mapping(module = "publico", path = "/viewGenericContent")
@Forwards(value = { @Forward(name = "viewItem-TutorSite", path = "basicUnit-item"),
        @Forward(name = "viewItem-AssemblySite", path = "basicUnit-item"),
        @Forward(name = "site-section-adviseLogin-DegreeSite", path = "degree-section-adviseLogin"),
        @Forward(name = "site-section-deny-PedagogicalCouncilSite", path = "pedagogicalCouncil-section-deny"),
        @Forward(name = "viewItem-ScientificAreaSite", path = "basicUnit-item"),
        @Forward(name = "site-section-adviseLogin-AssemblySite", path = "basicUnit-section-adviseLogin"),
        @Forward(name = "viewItem-ExecutionCourseSite", path = "execution-course-item"),
        @Forward(name = "site-section-deny-StudentsSite", path = "basicUnit-section-deny"),
        @Forward(name = "site-section-deny-DegreeSite", path = "degree-section-deny"),
        @Forward(name = "site-section-adviseLogin-UnitSite", path = "basicUnit-section-adviseLogin"),
        @Forward(name = "viewItem-ResearchUnitSite", path = "view-researchUnit-item"),
        @Forward(name = "viewSection-ExecutionCourseSite", path = "execution-course-section"),
        @Forward(name = "site-section-deny-ResearchUnitSite", path = "view-researchUnit-section-deny"),
        @Forward(name = "site-section-deny-ScientificCouncilSite", path = "scientificCouncil-section-deny"),
        @Forward(name = "site-section-adviseLogin-EdamSite", path = "basicUnit-section-adviseLogin"),
        @Forward(name = "viewSection-TutorSite", path = "basicUnit-section"),
        @Forward(name = "viewItem-Homepage", path = "view-homepage-item"),
        @Forward(name = "site-section-adviseLogin-PedagogicalCouncilSite", path = "pedagogicalCouncil-section-adviseLogin"),
        @Forward(name = "viewSection-DegreeSite", path = "degree-section"),
        @Forward(name = "viewSection-UnitSite", path = "basicUnit-section"),
        @Forward(name = "site-section-adviseLogin-ResearchUnitSite", path = "researchUnit-section-adviseLogin"),
        @Forward(name = "site-section-deny-Homepage", path = "homepage-section-deny"),
        @Forward(name = "site-section-deny-ExecutionCourseSite", path = "execution-course-section-deny"),
        @Forward(name = "viewItem-ManagementCouncilSite", path = "basicUnit-item"),
        @Forward(name = "site-section-adviseLogin-ScientificAreaSite", path = "basicUnit-section-adviseLogin"),
        @Forward(name = "viewItem-UnitSite", path = "basicUnit-item"),
        @Forward(name = "viewItem-StudentsSite", path = "basicUnit-item"),
        @Forward(name = "viewSection-Homepage", path = "view-homepage-section"),
        @Forward(name = "site-section-adviseLogin-StudentsSite", path = "basicUnit-section-adviseLogin"),
        @Forward(name = "site-section-deny-TutorSite", path = "basicUnit-section-deny"),
        @Forward(name = "viewSection-DepartmentSite", path = "department-section"),
        @Forward(name = "viewSection-EdamSite", path = "basicUnit-section"),
        @Forward(name = "site-section-adviseLogin-DepartmentSite", path = "department-section-adviseLogin"),
        @Forward(name = "site-section-deny-DepartmentSite", path = "department-section-deny"),
        @Forward(name = "viewSection-StudentsSite", path = "basicUnit-section"),
        @Forward(name = "viewItem-EdamSite", path = "basicUnit-item"),
        @Forward(name = "viewItem-DegreeSite", path = "degree-item"),
        @Forward(name = "site-section-adviseLogin-ScientificCouncilSite", path = "scientificCouncil-section-adviseLogin"),
        @Forward(name = "viewSection-AssemblySite", path = "basicUnit-section"),
        @Forward(name = "viewSection-PedagogicalCouncilSite", path = "pedagogicalCouncil-section"),
        @Forward(name = "viewSection-ScientificAreaSite", path = "basicUnit-section"),
        @Forward(name = "viewItem-ScientificCouncilSite", path = "scientificCouncil-item"),
        @Forward(name = "viewItem-PedagogicalCouncilSite", path = "pedagogicalCouncil-item"),
        @Forward(name = "site-section-deny-ManagementCouncilSite", path = "basicUnit-section-deny"),
        @Forward(name = "site-section-deny-EdamSite", path = "basicUnit-section-deny"),
        @Forward(name = "site-section-deny-ScientificAreaSite", path = "basicUnit-section-deny"),
        @Forward(name = "site-section-adviseLogin-ManagementCouncilSite", path = "basicUnit-section-adviseLogin"),
        @Forward(name = "site-section-adviseLogin-TutorSite", path = "basicUnit-section-adviseLogin"),
        @Forward(name = "viewSection-ManagementCouncilSite", path = "basicUnit-section"),
        @Forward(name = "viewSection-ScientificCouncilSite", path = "scientificCouncil-section"),
        @Forward(name = "site-section-adviseLogin-Homepage", path = "homepage-section-adviseLogin"),
        @Forward(name = "viewSection-ResearchUnitSite", path = "view-researchUnit-section"),
        @Forward(name = "site-section-deny-UnitSite", path = "basicUnit-section-deny"),
        @Forward(name = "site-section-adviseLogin-ExecutionCourseSite", path = "execution-course-section-adviseLogin"),
        @Forward(name = "site-section-deny-AssemblySite", path = "basicUnit-section-deny"),
        @Forward(name = "viewItem-DepartmentSite", path = "department-item") })
public class ViewGenericContents extends FenixDispatchAction {

    public ActionForward viewSection(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        Section section = getSection(request);
        request.setAttribute("section", section);
        String forwardSufix = getSite(request).getClass().getSimpleName();
        User userView = Authenticate.getUser();

        if (section.isAvailable()) {
            prepareProtectedItems(request, userView, section.getOrderedChildItems());
            return mapping.findForward("viewSection-" + forwardSufix);
        } else {
            if (isAuthenticated(userView)) {
                return mapping.findForward("site-section-deny-" + forwardSufix);
            } else {
                return mapping.findForward("site-section-adviseLogin-" + forwardSufix);
            }
        }

    }

    public ActionForward viewItem(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        Item item = getItem(request);
        request.setAttribute("item", item);

        Section section = item.getSection();
        request.setAttribute("section", section);

        request.setAttribute("itemAvailable", item.isAvailable());

        String type = getSite(request).getClass().getSimpleName();

        return mapping.findForward("viewItem-" + type);

    }

    private Site getSite(HttpServletRequest request) {
        return OldCmsSemanticURLHandler.getSite(request);
    }

    private Section getSection(HttpServletRequest request) {
        return (Section) OldCmsSemanticURLHandler.getContent(request);
    }

    private Item getItem(HttpServletRequest request) {
        return (Item) OldCmsSemanticURLHandler.getContent(request);
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

    private boolean isAuthenticated(User userView) {
        return userView != null;
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("site", OldCmsSemanticURLHandler.getSite(request));
        return super.execute(mapping, actionForm, request, response);
    }
}