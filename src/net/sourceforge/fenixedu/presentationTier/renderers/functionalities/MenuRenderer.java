package net.sourceforge.fenixedu.presentationTier.renderers.functionalities;

import java.util.Hashtable;
import java.util.Map;

import net.sourceforge.fenixedu.domain.functionalities.Functionality;
import net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext;
import net.sourceforge.fenixedu.domain.functionalities.Module;
import net.sourceforge.fenixedu.renderers.OutputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlInlineContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlLink;
import net.sourceforge.fenixedu.renderers.components.HtmlList;
import net.sourceforge.fenixedu.renderers.components.HtmlListItem;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.util.MultiLanguageString;

/**
 * This renderer generates a menu with a tree of functionalities available to the
 * user in the current context.
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
     * Sets the CSS style to be applied to the menu entry that corresponds to
     * the selected funcitonality
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
     * Selects the CSS classes to apply to each level of the menu. The first level
     * is level 0.
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
     * @property
     */
    public void getLevelStyle(String index, String value) {
        this.levelStyle.put(Integer.parseInt(index), value);
    }
    
    public String getModuleClasses() {
        return this.moduleClasses;
    }

    /**
     * Chooses the CSS classes to apply to the an entry corresponding to a module.
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
            public HtmlComponent createComponent(Object object, Class type) {
                FunctionalityContext context = (FunctionalityContext) object;

                if (context == null) {
                    return new HtmlText();
                }
                
                Module module = context.getSelectedModule();
                if (module == null) {
                    return new HtmlText();
                }
                
                while (module.getParent() != null && !module.getMaximized()) {
                    module = module.getParent();
                }
                
                HtmlList menu = new HtmlList();
                addMenuEntries(context, menu, module, 0);
                
                return menu;
            }

            private void addMenuEntries(FunctionalityContext context, HtmlList menu, Module parent, int level) {
                for (Functionality functionality : parent.getOrderedFunctionalities()) {
                    if (! functionality.isVisible(context)) {
                        continue;
                    }
                
                    HtmlListItem item = menu.createItem();

                    HtmlComponent component = getFunctionalityNameComponent(context, functionality, true);

                    if (context.getSelectedFunctionality().equals(functionality)) {
                        component.setClasses(getSelectedClasses());
                        component.setStyle(getSelectedStyle());
                    }
                    
                    if (component instanceof HtmlLink || component.getTitle() != null) {
                        item.addChild(component);
                    }
                    else {
                        HtmlInlineContainer container = new HtmlInlineContainer();
                        container.addChild(component);
                        
                        item.addChild(container);
                    }

                    if (functionality instanceof Module) {
                        Module module = (Module) functionality;
                        
                        item.setClasses(getModuleClasses());
                        item.setStyle(getModuleStyle());
                        
                        HtmlList subMenu = new HtmlList();
                        item.addChild(subMenu);
                        
                        addMenuEntries(context, subMenu, module, level + 1);
                    }
                }
                
                menu.setClasses(getLevelClasses(String.valueOf(level)));
                menu.setStyle(getLevelStyle(String.valueOf(level)));
            }
            
        };
    }
    
    /**
     * Creates a component that shows the functionality name, possibly in a link to the functionality's public path.
     * If the fuctionality is parameterized then the required parameters are appended to the link. 
     */
    public static HtmlComponent getFunctionalityNameComponent(FunctionalityContext context, Functionality functionality, boolean canMakeLink) {
        HtmlComponent component = new HtmlText(functionality.getName().getContent());
        
        String path = functionality.getPublicPath();
        if (path != null && canMakeLink && functionality.isAvailable(context)) {
            HtmlLink link = new HtmlLink();
            
            link.setContextRelative(false);
            link.setUrl(context.getRequest().getContextPath() + path);
            
            if (functionality.isParameterized()) {
                for (String parameter : functionality.getParameterList()) {
                    for (String value : context.getRequest().getParameterValues(parameter)) {
                        link.addParameter(parameter, value);
                    }
                }
            }
            
            link.setBody(component);
            component = link;
        }
        
        MultiLanguageString title = functionality.getTitle();
        if (title != null && !title.isEmpty()) {
            component.setTitle(title.getContent());
        }
        
        return component;
    }

}
