package net.sourceforge.fenixedu.presentationTier.renderers.functionalities;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;

import org.fenixedu.bennu.portal.domain.MenuContainer;
import org.fenixedu.bennu.portal.domain.MenuFunctionality;
import org.fenixedu.bennu.portal.domain.MenuItem;

import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.Face;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlLinkWithPreprendedComment;
import pt.ist.fenixWebFramework.renderers.components.HtmlList;
import pt.ist.fenixWebFramework.renderers.components.HtmlListItem;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;

/**
 * This renderer generates a menu with a tree of functionalities available to
 * the user in the current context.
 * 
 * <p>
 * Each functionality that has a path defined will be a link to that path. If' a functionality is not visible to the user it will
 * not be shown.
 * 
 * @author cfgi
 */
public class MenuRenderer extends OutputRenderer {

    private String selectedClasses;

    private String moduleClasses;

    public MenuRenderer() {
        super();
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

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(final Object object, final Class type) {
                final MenuFunctionality selectedFunctionality = (MenuFunctionality) object;

                if (selectedFunctionality == null) {
                    return new HtmlText();
                }

                final HtmlList menu = new HtmlList();

                MenuContainer entryPoint = selectedFunctionality.getParent();

                Set<MenuItem> containers =
                        entryPoint.getParent() == null ? Collections.<MenuItem> singleton(entryPoint) : entryPoint.getParent()
                                .getOrderedChild();

                addMenuEntries(selectedFunctionality.getPathFromRoot(), menu, containers);

                return menu;
            }

            private void addMenuEntries(final Collection<MenuItem> fullPath, final HtmlList menu,
                    final Collection<MenuItem> entries) {

                for (MenuItem menuItem : entries) {
                    if (!menuItem.isAvailableForCurrentUser()) {
                        continue;
                    }

                    HtmlListItem item = menu.createItem();
                    item.addChild(getFunctionalityNameComponent(menuItem));

                    if (menuItem instanceof MenuContainer) {

                        item.setClasses(getModuleClasses());

                        HtmlList subMenu = new HtmlList();
                        item.addChild(subMenu);

                        MenuContainer container = (MenuContainer) menuItem;

                        addMenuEntries(fullPath, subMenu, container.getOrderedChild());
                    }

                    if (fullPath.contains(menuItem)) {
                        String existingClasses = item.getClasses() == null ? "" : item.getClasses() + " ";
                        item.setClasses(existingClasses + getSelectedClasses());
                    }
                }
            }
        };
    }

    private HtmlComponent getFunctionalityNameComponent(MenuItem entry) {
        HtmlText text = new HtmlText(entry.getTitle().getContent());
        text.setFace(Face.STANDARD);

        HtmlComponent component = text;

        if (entry instanceof MenuFunctionality) {
            final String linkPrefix = GenericChecksumRewriter.NO_CHECKSUM_PREFIX;
            HtmlLink link = new HtmlLinkWithPreprendedComment(linkPrefix);

            link.setContextRelative(true);
            link.setModuleRelative(false);

            link.setUrl(entry.getFullPath());
            link.setBody(component);

            component = link;
        } else {
            text.setFace(Face.STRONG);
        }

        component.setTitle(entry.getTitle().getContent());

        return component;
    }

    public static String findPathFor(final String contextPath, final Content targetContent, final FunctionalityContext context,
            final Collection<String> subPath) {
        final StringBuilder buffer = new StringBuilder(contextPath);
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
