package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlMenu;
import net.sourceforge.fenixedu.renderers.components.HtmlMenuOption;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaSlotKey;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;

/**
 * The default enumeration input renderer. This renderer presents an
 * html menu with one option for each possible enum value. Each option's
 * label is searched in the bundle <tt>ENUMERATION_RESOURCES</tt> using the
 * enum's name.
 * 
 * @author cfgi
 */
public class EnumInputRenderer extends InputRenderer {

    private static Logger logger = Logger.getLogger(EnumInputRenderer.class);
    
    // NOTE: duplicate code with EnumRenderer
    protected String getEnumDescription(Enum enumerate) {
        String description = enumerate.toString();
        
        MessageResources resources = getEnumerationResources();
        if (resources != null) {
            if (resources.isPresent(enumerate.toString())) {               
                description = resources.getMessage(enumerate.toString());
            }
        }

        resources = getResources();
        if (resources != null) {
            if (resources.isPresent(enumerate.toString())) {               
                description = resources.getMessage(enumerate.toString());
            }
        }
        
        return description;
    }

    protected MessageResources getResources() {
        return RenderUtils.getMessageResources();
    }

    protected MessageResources getEnumerationResources() {
        // TODO: allow the name to be configured or fetch the resources in other way
        MessageResources resources = RenderUtils.getMessageResources("ENUMERATION_RESOURCES");
        
        if (resources == null) {
            resources = RenderUtils.getMessageResources();
        }
        
        return resources;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object targetObject, Class type) {
                Enum enumerate = (Enum) targetObject;
                
                HtmlMenu menu = new HtmlMenu();
                
                String defaultOptionTitle = RenderUtils.getResourceString("menu.default.title");
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

class EnumConverter extends Converter {

    @Override
    public Object convert(Class type, Object value) {
        Object[] enums = type.getEnumConstants();
        
        for (int i = 0; i < enums.length; i++) {
            if (enums[i].toString().equals(value)) {
                return enums[i];
            }
        }
        
        return null;
    }
    
}