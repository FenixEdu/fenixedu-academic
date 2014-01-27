package net.sourceforge.fenixedu.presentationTier.renderers.functionalities;

import org.fenixedu.bennu.portal.domain.MenuContainer;
import org.fenixedu.bennu.portal.domain.MenuItem;
import org.fenixedu.bennu.portal.domain.PortalConfiguration;

import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlInlineContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlLinkWithPreprendedComment;
import pt.ist.fenixWebFramework.renderers.components.HtmlList;
import pt.ist.fenixWebFramework.renderers.components.HtmlListItem;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;

/**
 * This renderer generates the top level menu of the application. This menus is
 * divided in portals and each portal corresponde to a toplevel functionality.
 * 
 * <p>
 * If that functionality has a path then the name of the functionality will link to that path. If not then it will link to a
 * generic action that shows the local menu and a black body.
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
     * Sets the CSS style to be applied to the menu entry that corresponds to
     * the selected funcitonality
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
                MenuItem selectedContainer = findTopLevelContainerFor((MenuItem) object);

                for (MenuItem menuItem : PortalConfiguration.getInstance().getMenu().getOrderedChild()) {

                    if (!menuItem.isAvailableForCurrentUser()) {
                        continue;
                    }

                    HtmlListItem item = menu.createItem();

                    if (menuItem.equals(selectedContainer)) {
                        item.setClasses(getSelectedClasses());
                        item.setStyle(getSelectedStyle());
                    } else {
                        item.setClasses(getTopLevelClasses());
                    }

                    item.addChild(getMenuComponent(menuItem));
                }

                return menu;
            }

            private MenuItem findTopLevelContainerFor(MenuItem selectedItem) {
                if (selectedItem == null) {
                    return null;
                }
                MenuContainer root = PortalConfiguration.getInstance().getMenu();
                while (selectedItem.getParent() != root) {
                    selectedItem = selectedItem.getParent();
                }
                return selectedItem;
            }

            private HtmlComponent getMenuComponent(MenuItem menuItem) {

                HtmlLink link = new HtmlLinkWithPreprendedComment(GenericChecksumRewriter.NO_CHECKSUM_PREFIX);

                HtmlInlineContainer container = new HtmlInlineContainer();
                HtmlComponent component = new HtmlText(menuItem.getTitle().getContent());
                container.addChild(component);

                link.setUrl("/" + menuItem.getPath());

                link.setContextRelative(true);
                link.setModuleRelative(false);

                link.setBody(container);
                return link;
            }

        };
    }
}
