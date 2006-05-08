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
 * Example:<br>
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
    
    // NOTE: duplicate code with EnumRenderer
    protected String getEnumDescription(Enum enumerate) {
        String description = RenderUtils.getResourceString("ENUMERATION_RESOURCES", enumerate.toString()); 
            
        if (description == null) {
            description = RenderUtils.getResourceString(enumerate.toString());
        }
        
        if (description == null) {
            description = enumerate.toString();
        }
        
        return description;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object targetObject, Class type) {
                Enum enumerate = (Enum) targetObject;
                
                HtmlMenu menu = new HtmlMenu();
                
                String defaultOptionTitle = RenderUtils.getResourceString("renderers.menu.default.title");
                menu.createDefaultOption(defaultOptionTitle).setSelected(enumerate == null);
                
                Object[] constants = type.getEnumConstants();
                for (Object object : constants) {
                    Enum oneEnum = (Enum) object;
                    String description = getEnumDescription(oneEnum);
                    
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
            
        };
    }
}
