package net.sourceforge.fenixedu.renderers;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlMenu;
import net.sourceforge.fenixedu.renderers.components.HtmlMenuOption;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaSlotKey;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.log4j.Logger;

public class EnumInputRenderer extends InputRenderer {

    private static Logger logger = Logger.getLogger(EnumInputRenderer.class);
    
    protected String getEnumDescription(Enum enumerate) {
        ResourceBundle bundle = getEnumerationBundle();
        
        String description = enumerate.toString();
        
        try {
            description = bundle.getString(enumerate.toString());
        } catch (MissingResourceException e) {
            logger.warn("could not get name of enumeration: " + enumerate.toString());
        }
        
        return description;
    }

    protected ResourceBundle getEnumerationBundle() {
        return ResourceBundle.getBundle("ServidorApresentacao.EnumerationResources");
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