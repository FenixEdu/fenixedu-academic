package net.sourceforge.fenixedu.renderers;


import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlMenu;
import net.sourceforge.fenixedu.renderers.components.HtmlMenuOption;
import net.sourceforge.fenixedu.renderers.converters.EnumConverter;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaSlotKey;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.log4j.Logger;

/**
 * This renderer presents an html menu with one option for each possible enum value.
 * Each option's label is searched in the bundle <tt>ENUMERATION_RESOURCES</tt> using the
 * enum's name.
 * 
 * <p>
 * Example:<br/>
 * Choose a {@link java.lang.annotation.ElementType element type}:
 * <select>
 *  <option>Type</option>
 *  <option>Field</option>
 *  <option>Parameter</option>
 *  <option>Constructor</option>
 *  <option>Local Variable</option>
 *  <option>Annotation</option>
 *  <option>Package</option>
 * </select>
 * 
 * @author cfgi
 */
public class EnumInputRenderer extends InputRenderer {

    private static Logger logger = Logger.getLogger(EnumInputRenderer.class);

    private String defaultText;
    private String bundle;
    private boolean key;
    
    public String getBundle() {
        return this.bundle;
    }

    /**
     * The bundle used if <code>key</code> is <code>true</code>
     * 
     * @property
     */
    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    public String getDefaultText() {
        return this.defaultText;
    }

    /**
     * The text or key of the default menu title.
     * 
     * @property
     */
    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
    }

    public boolean isKey() {
        return this.key;
    }

    /**
     * Indicates the the default text is a key to a resource bundle.
     *  
     * @property
     */
    public void setKey(boolean key) {
        this.key = key;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object targetObject, Class type) {
                Enum enumerate = (Enum) targetObject;
                
                HtmlMenu menu = new HtmlMenu();
                
                String defaultOptionTitle = getDefaultTitle();
                menu.createDefaultOption(defaultOptionTitle).setSelected(enumerate == null);
                
                Object[] constants = type.getEnumConstants();
                for (Object object : constants) {
                    Enum oneEnum = (Enum) object;
                    String description = RenderUtils.getEnumString(oneEnum, getBundle());
                    
                    HtmlMenuOption option = menu.createOption(description);
                    option.setValue(oneEnum.toString());
                    
                    if (enumerate != null && oneEnum.equals(enumerate)) {
                        option.setSelected(true);
                    }
                }
                
                menu.setConverter(new EnumConverter());
                menu.setTargetSlot((MetaSlotKey) getInputContext().getMetaObject().getKey());
                
                return menu;
            }

            // TODO: refactor this, probably mode to HtmlMenu, duplicate id=menu.getDefaultTitle
            private String getDefaultTitle() {
                if (getDefaultText() == null) {
                    return RenderUtils.getResourceString("renderers.menu.default.title");
                }
                else {
                    if (isKey()) {
                        return RenderUtils.getResourceString(getBundle(), getDefaultText());
                    }
                    else {
                        return getDefaultText();
                    }
                }
            }
            
        };
    }
}
