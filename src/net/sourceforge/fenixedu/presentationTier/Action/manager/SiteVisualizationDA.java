package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.publico.ProtectedItem;
import net.sourceforge.fenixedu.presentationTier.Action.publico.SimpleFunctionalityContext;
import net.sourceforge.fenixedu.presentationTier.Action.utils.RequestUtils;

/**
 * Generic action to coordinate the visualization of a website.
 * 
 * 
 * @author cfgi
 */
public abstract class SiteVisualizationDA extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String directLinkContext = getDirectLinkContext(request);
        if (directLinkContext != null) {
            request.setAttribute("directLinkContext", directLinkContext);
        }
        
        setSectionBreadCrumbs(request);
        return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward item(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Item item = selectItem(request);
        
        if (item == null) {
            return getSiteDefaultView(mapping, form, request, response);
        }
        
        IUserView userView = prepareUserView(request);
        FunctionalityContext context = prepareSectionContext(request);
        
        if (item.isAvailable(context)) {
            return mapping.findForward("site-item");
        }
        else {
            if (isAuthenticated(userView)) {
                return mapping.findForward("site-item-deny");
            }
            else {
                return mapping.findForward("site-item-adviseLogin");
            }
        }
    }
    
    public ActionForward section(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Section section = selectSection(request);

        if (section == null) {
            return getSiteDefaultView(mapping, form, request, response);
        }
        
        IUserView userView = prepareUserView(request);
        FunctionalityContext context = prepareSectionContext(request);

        if (section.isAvailable(context)) {
            prepareProtectedItems(request, userView, section.getOrderedItems(), context);
            return mapping.findForward("site-section");
        }
        else {
            if (isAuthenticated(userView)) {
                return mapping.findForward("site-section-deny");
            }
            else {
                return mapping.findForward("site-section-adviseLogin");
            }
        }
    }

    public ActionForward itemWithLogin(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        IUserView userView = getUserView(request);
        
        if (! isAuthenticated(userView)) {
            RequestUtils.sendLoginRedirect(request, response);
            return null;
        }
        else {
            return item(mapping, form, request, response);
        }
    }
    
    public ActionForward sectionWithLogin(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        IUserView userView = getUserView(request);
        
        if (! isAuthenticated(userView)) {
            RequestUtils.sendLoginRedirect(request, response);
            return null;
        }
        else {
            return section(mapping, form, request, response);
        }
    }
    
    private void prepareProtectedItems(HttpServletRequest request, IUserView userView, Collection<Item> items, FunctionalityContext context) {
        List<ProtectedItem> protectedItems = setupItems(request, context, items);
        
        if (!isAuthenticated(userView) && hasRestrictedItems(protectedItems)) {
            request.setAttribute("hasRestrictedItems", true);
        }
    }

    private boolean hasRestrictedItems(List<ProtectedItem> protectedItems) {
        for (ProtectedItem item : protectedItems) {
            if (! item.isAvailable()) {
                return true;
            }
        }
        
        return false;
    }

    private List<ProtectedItem> setupItems(HttpServletRequest request, FunctionalityContext context, Collection<Item> items) {
        List<ProtectedItem> protectedItems = new ArrayList<ProtectedItem>();
        for (Item item : items) {
            if (item.isVisible()) {
                protectedItems.add(new ProtectedItem(context, item));
            }
        }
        
        request.setAttribute("protectedItems", protectedItems);
        return protectedItems;
    }

    private IUserView prepareUserView(HttpServletRequest request) {
        IUserView userView = getUserView(request);
        request.setAttribute("logged", isAuthenticated(userView));
        
        return userView;
    }

    private FunctionalityContext prepareSectionContext(HttpServletRequest request) {
        FunctionalityContext context = new SimpleFunctionalityContext(request);
        request.setAttribute("context", context);
        return context;
    }

    private boolean isAuthenticated(IUserView userView) {
        return userView != null && !userView.isPublicRequester();
    }

    protected Section selectSection(HttpServletRequest request) {
        return selectSection(request, getSection(request));
    }

    private Section selectSection(HttpServletRequest request, Section section) {
        if (section != null) {
            request.setAttribute("section", section);
            
            final Set<Section> selectedSections = new HashSet<Section>();
            for (Section currentSection = section ; currentSection != null; currentSection = currentSection.getSuperiorSection()) {
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
        
        Integer itemID = Integer.valueOf(parameter);
        return rootDomainObject.readItemByOID(itemID);
    }

    protected Section getSection(final HttpServletRequest request) {
        String parameter = request.getParameter("sectionID");
        
        if (parameter == null) {
            return null;
        }
        
        final Integer sectionID = Integer.valueOf(parameter);
        return rootDomainObject.readSectionByOID(sectionID);
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
    
    protected abstract ActionForward getSiteDefaultView(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response);
    
    protected String getDirectLinkContext(HttpServletRequest request) {
        return null;
    }
}
