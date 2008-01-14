package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.MetaDomainObject;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.contents.Container;
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

/**
 * 
 * @author pcma
 *
 */
public class ViewGenericContents extends FenixDispatchAction {

    public ActionForward viewSection(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	Section section = getSection(request);
	request.setAttribute("section", section);
	String type = getType(request);
	String forwardSufix = type.substring(type.lastIndexOf(".") + 1, type.length());
	FilterFunctionalityContext context = getContext(request);
	IUserView userView = AccessControl.getUserView();

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

    public ActionForward viewItem(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	Section section = (Section) getLastContentInPathWithClass(request,Section.class);
	request.setAttribute("section", section);
	
	Item item = getItem(request);
	request.setAttribute("item", item);

	String type = getType(request);

	return mapping.findForward("viewItem-"
		+ type.substring(type.lastIndexOf(".") + 1, type.length()));

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
	FilterFunctionalityContext context = (FilterFunctionalityContext) AbstractFunctionalityContext
		.getCurrentContext(request);
	Container selectedContainer = context.getSelectedContainer();
	return (MetaDomainObjectPortal) MetaDomainObject.getMeta(selectedContainer.getClass())
		.getAssociatedPortal();
    }

    private void prepareProtectedItems(HttpServletRequest request, IUserView userView,
	    Collection<Item> items, FunctionalityContext context) {
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

    private List<ProtectedItem> setupItems(HttpServletRequest request, FunctionalityContext context,
	    Collection<Item> items) {
	List<ProtectedItem> protectedItems = new ArrayList<ProtectedItem>();
	for (Item item : items) {
	    if (item.getVisible()) {
		protectedItems.add(new ProtectedItem(context, item));
	    }
	}

	request.setAttribute("protectedItems", protectedItems);
	return protectedItems;
    }

    private boolean isAuthenticated(IUserView userView) {
	return userView != null && !userView.isPublicRequester();
    }
}
