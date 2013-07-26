package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.security.Authenticate;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.MetaDomainObject;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal;
import net.sourceforge.fenixedu.domain.functionalities.AbstractFunctionalityContext;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities.FilterFunctionalityContext;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * 
 * @author pcma
 * 
 */
@Mapping(module = "publico", path = "/viewGenericContent", scope = "session", parameter = "method")
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
        String type = getType(request);
        String forwardSufix = type.substring(type.lastIndexOf(".") + 1, type.length());
        FilterFunctionalityContext context = getContext(request);
        User userView = Authenticate.getUser();

        if (section.isAvailable(context)) {
            prepareProtectedItems(request, userView, section.getOrderedItems(), context);
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

        Section section = (Section) getLastContentInPathWithClass(request, Section.class);
        request.setAttribute("section", section);

        Item item = getItem(request);
        request.setAttribute("item", item);

        FilterFunctionalityContext context = getContext(request);

        request.setAttribute("itemAvailable", item.isAvailable(context));

        String type = getType(request);

        return mapping.findForward("viewItem-" + type.substring(type.lastIndexOf(".") + 1, type.length()));

    }

    private String getType(HttpServletRequest request) {
        MetaDomainObjectPortal portal = getPortal(request);
        String type = portal.getMetaDomainObject().getType();
        return type;
    }

    private FilterFunctionalityContext getContext(HttpServletRequest request) {
        return (FilterFunctionalityContext) AbstractFunctionalityContext.getCurrentContext(request);
    }

    private Content getLastContentInPathWithClass(HttpServletRequest request, Class clazz) {
        FilterFunctionalityContext context = getContext(request);
        return context.getLastContentInPath(clazz);
    }

    private Content getLastContentInPath(HttpServletRequest request) {
        FilterFunctionalityContext context = getContext(request);
        List<Content> contents = context.getSelectedContents();
        return context.getSelectedContents().get(contents.size() - 1);
    }

    private Section getSection(HttpServletRequest request) {
        return (Section) getLastContentInPath(request);
    }

    private Item getItem(HttpServletRequest request) {
        return (Item) getLastContentInPath(request);
    }

    private MetaDomainObjectPortal getPortal(HttpServletRequest request) {
        FilterFunctionalityContext context = (FilterFunctionalityContext) AbstractFunctionalityContext.getCurrentContext(request);
        return (MetaDomainObjectPortal) MetaDomainObject.getMeta(context.getLastContentInPath(Site.class).getClass())
                .getAssociatedPortal();
    }

    private void prepareProtectedItems(HttpServletRequest request, User userView, Collection<Item> items,
            FunctionalityContext context) {
        List<ProtectedItem> protectedItems = setupItems(request, context, items);

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

    private List<ProtectedItem> setupItems(HttpServletRequest request, FunctionalityContext context, Collection<Item> items) {
        List<ProtectedItem> protectedItems = new ArrayList<ProtectedItem>();
        for (Item item : items) {
            if (item.getVisible()) {
                protectedItems.add(new ProtectedItem(context, item));
            }
        }

        request.setAttribute("protectedItems", protectedItems);
        return protectedItems;
    }

    private boolean isAuthenticated(User userView) {
        return userView != null;
    }
}
