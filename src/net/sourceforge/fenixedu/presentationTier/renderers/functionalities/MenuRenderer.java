package net.sourceforge.fenixedu.presentationTier.renderers.functionalities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;

import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.contents.MenuEntry;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities.FilterFunctionalityContext;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.Face;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlLinkWithPreprendedComment;
import pt.ist.fenixWebFramework.renderers.components.HtmlList;
import pt.ist.fenixWebFramework.renderers.components.HtmlListItem;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * This renderer generates a menu with a tree of functionalities available to
 * the user in the current context.
 * 
 * <p>
 * Each functionality that has a path defined will be a link to that path. If' a
 * functionality is not visible to the user it will not be shown.
 * 
 * @author cfgi
 */
public class MenuRenderer extends OutputRenderer {

    private String selectedClasses;

    private String selectedStyle;

    private String moduleClasses;

    private String moduleStyle;

    private Map<Integer, String> levelClasses;

    private Map<Integer, String> levelStyle;

    public MenuRenderer() {
	super();

	this.levelClasses = new Hashtable<Integer, String>();
	this.levelStyle = new Hashtable<Integer, String>();
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

    public String getLevelClasses(String index) {
	return this.levelClasses.get(Integer.parseInt(index));
    }

    /**
         * Selects the CSS classes to apply to each level of the menu. The first
         * level is level 0.
         * 
         * @property
         */
    public void setLevelClasses(String index, String value) {
	this.levelClasses.put(Integer.parseInt(index), value);
    }

    public String getLevelStyle(String index) {
	return this.levelStyle.get(Integer.parseInt(index));
    }

    /**
         * Selects the CSS style to apply to each level of the menu.
         * 
         * @property
         */
    public void getLevelStyle(String index, String value) {
	this.levelStyle.put(Integer.parseInt(index), value);
    }

    public String getModuleClasses() {
	return this.moduleClasses;
    }

    /**
         * Chooses the CSS classes to apply to the an entry corresponding to a
         * module.
         * 
         * @property
         */
    public void setModuleClasses(String moduleClasses) {
	this.moduleClasses = moduleClasses;
    }

    public String getModuleStyle() {
	return this.moduleStyle;
    }

    /**
         * Sets the CSS style to apply to an a module entry.
         * 
         * @property
         */
    public void setModuleStyle(String moduleStyle) {
	this.moduleStyle = moduleStyle;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new Layout() {

	    @Override
	    public HtmlComponent createComponent(final Object object, final Class type) {
		final FilterFunctionalityContext context = (FilterFunctionalityContext) object;

		if (context == null || context.getCurrentContextPath() == null) {
		    return new HtmlText();
		}

		final Container container = context.getSelectedContainer();

		final HtmlList menu = new HtmlList();
		addMenuEntries(context, menu, (Collection) container.getOrderedChildrenNodes(), new ArrayList<String>());

		return menu;
	    }

	    private void addMenuEntries(final FilterFunctionalityContext context, final HtmlList menu,
		    final Collection<MenuEntry> entries, final Collection<String> subPath) {

		for (MenuEntry entry : entries) {
		    if (!entry.isNodeVisible() || !entry.isAvailable(context)) {
			continue;
		    }

		    HtmlListItem item = menu.createItem();
		    item.addChild(getFunctionalityNameComponent(context, entry, true, subPath));

		    if (!entry.getChildren().isEmpty()) {
			HtmlComponent child = item.getChildren().get(0);

			HtmlText text;
			if (child instanceof HtmlText) {
			    text = (HtmlText) child;
			} else {
			    text = (HtmlText) ((HtmlLink) child).getBody();
			}

			text.setFace(Face.STRONG);

			item.setClasses(getModuleClasses());
			item.setStyle(getModuleStyle());

			HtmlList subMenu = new HtmlList();
			item.addChild(subMenu);

			final String name = entry.getReferingContent().getNormalizedName().getContent();
			subPath.add(name);
			addMenuEntries(context, subMenu, entry.getChildren(), subPath);
			subPath.remove(name);
		    }

		    boolean selected = false;
		    if (context.getSelectedContent() != null && context.getSelectedContent().getContentId().equals(entry.getEntryId())) {
			selected = true;
		    } else {
			String nodeId = context.getRequest().getParameter("nodeID");
			if (nodeId != null && nodeId.equals(entry.getEntryId())) {
			    selected = true;
			}
		    }

		    if (selected) {
			String existingClasses = item.getClasses() == null ? "" : item.getClasses();
			String existingStyle = item.getStyle() == null ? "" : item.getStyle();

			item.setClasses(existingClasses + getSelectedClasses());
			item.setStyle(existingStyle + getSelectedStyle());
		    }

		}

		menu.setClasses(getLevelClasses(String.valueOf(subPath.size())));
		menu.setStyle(getLevelStyle(String.valueOf(subPath.size())));
	    }

	};
    }

    /**
         * Creates a component that shows the functionality name, possibly in a
         * link to the functionality's public path. If the fuctionality is
         * parameterized then the required parameters are appended to the link.
         */
    public static HtmlComponent getFunctionalityNameComponent(FilterFunctionalityContext context,
	    MenuEntry entry, boolean canMakeLink, final Collection<String> subPath) {
	HtmlText text = new HtmlText(entry.getName().getContent());
	text.setFace(Face.STANDARD);

	HtmlComponent component = text;

	String path = entry.getPath();
	if (path != null && canMakeLink && entry.isAvailable()) {
	    final Content content = entry.getReferingContent();
	    final String linkPrefix = content.isPublic() ? ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX : ContentInjectionRewriter.HAS_CONTEXT_PREFIX;
	    HtmlLink link = new HtmlLinkWithPreprendedComment(linkPrefix);

	    link.setContextRelative(false);
	    link.setUrl(findPathFor(context.getRequest().getContextPath(), content, context, subPath));
	    link.setBody(component);

	    component = link;
	}

	MultiLanguageString title = entry.getTitle();
	if (title != null && !title.isEmpty()) {
	    component.setTitle(title.getContent());
	}

	return component;
    }

    public static String findPathFor(final String contextPath, final Content targetContent,
	    final FilterFunctionalityContext context, final Collection<String> subPath) {
	final StringBuffer buffer = new StringBuffer(contextPath);
	buffer.append(context.getSelectedContainerPath());
	for (final String name : subPath) {
	    buffer.append('/');
	    buffer.append(name);
	}
	buffer.append('/');
	buffer.append(targetContent.getNormalizedName().getContent());
	return buffer.toString();
    }

}
