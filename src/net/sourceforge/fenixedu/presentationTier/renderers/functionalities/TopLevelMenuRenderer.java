package net.sourceforge.fenixedu.presentationTier.renderers.functionalities;

import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.contents.MenuEntry;
import net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal;
import net.sourceforge.fenixedu.domain.contents.Portal;
import net.sourceforge.fenixedu.domain.functionalities.AvailabilityPolicy;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter;
import net.sourceforge.fenixedu.renderers.OutputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlInlineContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlLink;
import net.sourceforge.fenixedu.renderers.components.HtmlLinkWithPreprendedComment;
import net.sourceforge.fenixedu.renderers.components.HtmlList;
import net.sourceforge.fenixedu.renderers.components.HtmlListItem;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;

/**
 * This renderer generates the top level menu of the application. This menus is
 * divided in portals and each portal corresponde to a toplevel functionality.
 * 
 * <p>
 * If that functionality has a path then the name of the functionality will link
 * to that path. If not then it will link to a generic action that shows the
 * local menu and a black body.
 * 
 * @author cfgi
 */
public class TopLevelMenuRenderer extends OutputRenderer {

    private String selectedClasses;

    private String selectedStyle;

    private String topLevelClasses;

    public String getTopLevelClasses() {
	return topLevelClasses;
    }

    public void setTopLevelClasses(String topLevelClasses) {
	this.topLevelClasses = topLevelClasses;
    }

    public String getSelectedClasses() {
	return this.selectedClasses;
    }

    /**
         * Sets the CSS classes to be used in the entry that corresponds to the
         * selected functionality.
         * 
         * @property
         */
    public void setSelectedClasses(String selectedClasses) {
	this.selectedClasses = selectedClasses;
    }

    public String getSelectedStyle() {
	return this.selectedStyle;
    }

    /**
         * Sets the CSS style to be applied to the menu entry that corresponds
         * to the selected funcitonality
         * 
         * @property
         */
    public void setSelectedStyle(String selectedStyle) {
	this.selectedStyle = selectedStyle;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new Layout() {

	    @Override
	    public HtmlComponent createComponent(Object object, Class type) {
		HtmlList menu = new HtmlList();
		FunctionalityContext context = (FunctionalityContext) object;

		for (MenuEntry node : Portal.getRootPortal().getMenu()) {

		    AvailabilityPolicy policy = getAvailablityPocility(node);

		    if (!node.isVisible() || (policy != null && !policy.isAvailable(context)) || node.getReferingContent() instanceof MetaDomainObjectPortal) {
			continue;
		    }

		    HtmlListItem item = menu.createItem();

		    if (node.getReferingContent() == context.getSelectedTopLevelContainer()) {
			item.setClasses(getSelectedClasses());
			item.setStyle(getSelectedStyle());
		    } else {
			item.setClasses(getTopLevelClasses());
		    }

		    item.addChild(getMenuComponent(context, node));
		    
		}

		return menu;
	    }

	    private HtmlComponent getMenuComponent(FunctionalityContext context, MenuEntry node) {

		Content child = node.getReferingContent();
		Content content = (child instanceof Section) ? ((Section) child).getInitialContent()
			: child;
		if (content == null) {
		    return new HtmlText(child.getName().getContent());
		}
		String path = content.getPath();

		HtmlComponent component = new HtmlText(child.getName().getContent());

		if (path != null && content.isAvailable()) {
		    HtmlLink link = new HtmlLinkWithPreprendedComment(ContentInjectionRewriter.HAS_CONTEXT_PREFIX_STRING);

		    HtmlInlineContainer container = new HtmlInlineContainer();
		    container.addChild(component);
		    link.setContextRelative(false);
		    link.setUrl(context.getRequest().getContextPath() + "/" + node.getReferingContent().getName().getContent());
		    link.setBody(container);

		    component = link;
		}

		return component;
	    }

	    private AvailabilityPolicy getAvailablityPocility(MenuEntry node) {
		Content content = node.getReferingContent();
		return content.getAvailabilityPolicy();
	    }

	};
    }

}
