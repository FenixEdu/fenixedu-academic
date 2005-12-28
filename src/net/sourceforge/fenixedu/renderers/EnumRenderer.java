package net.sourceforge.fenixedu.renderers;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;

import org.apache.log4j.Logger;

public class EnumRenderer extends OutputRenderer {

    private static Logger logger = Logger.getLogger(EnumRenderer.class);
    
    protected String getEnumDescription(Enum enumerate) {
        ResourceBundle bundle = getEnumerationBundle();
        
        String description = enumerate.toString();
        
        try {
            description = bundle.getString(enumerate.toString());
        } catch (MissingResourceException e) {
            logger.warn("Could not get name of the enumeration: " + enumerate.toString());
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
            public HtmlComponent createComponent(Object object, Class type) {
                Enum enumerate = (Enum) object;
                
                if (enumerate == null) {
                    return new HtmlText();
                }
                
                String description = getEnumDescription(enumerate);
                
                return new HtmlText(description);
            }
            
        };
    }
}
