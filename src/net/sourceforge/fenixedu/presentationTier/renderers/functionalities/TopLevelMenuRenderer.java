package net.sourceforge.fenixedu.presentationTier.renderers.functionalities;

import net.sourceforge.fenixedu.domain.functionalities.Functionality;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities.FilterFunctionalityContext;
import net.sourceforge.fenixedu.renderers.OutputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlList;
import net.sourceforge.fenixedu.renderers.components.HtmlListItem;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.plugin.RenderersRequestProcessor;

/**
 * This renderer generates the top level menu of the application. This 
 * menus is divided in portals and each portal corresponde to a toplevel
 * functionality.
 * 
 * <p>
 * If that functionality has a path then the name of the functionality will
 * link to that path. If not then it will link to a generic action that 
 * shows the local menu and a black body.
 * 
 * @author cfgi
 */
public class TopLevelMenuRenderer extends OutputRenderer {

    private String selectedClasses;
    private String selectedStyle;
    
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
                FunctionalityContext context = (FunctionalityContext) object;
                
                if (context == null) {
                    context = new FilterFunctionalityContext(RenderersRequestProcessor.getCurrentRequest(), null);
                }
                
                HtmlList menu = new HtmlList();
                
                for (Functionality functionality : Functionality.getOrderedTopLevelFunctionalities()) {
                    if (! functionality.isVisible(context)) {
                        continue;
                    }
                    
                    HtmlListItem item = menu.createItem();

                    if (functionality == getSelectedTopLevelFunctionality(context)) {
                        item.setClasses(getSelectedClasses());
                        item.setStyle(getSelectedStyle());
                    }
                    
                    item.addChild(MenuRenderer.getFunctionalityNameComponent(context, functionality, true));
                }
                
                return menu;
            }

            private Functionality getSelectedTopLevelFunctionality(FunctionalityContext context) {
                Functionality functionality = context.getSelectedFunctionality();
                
                while (functionality != null && functionality.getModule() != null) {
                    functionality = functionality.getModule();
                }
                
                return functionality;
            }

        };
    }

}
