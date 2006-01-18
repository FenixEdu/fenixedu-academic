package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;

public class EnumRenderer extends OutputRenderer {

    private static Logger logger = Logger.getLogger(EnumRenderer.class);
    
    protected String getEnumDescription(Enum enumerate) {
        String description = enumerate.toString();
        
        MessageResources resources = getEnumerationResources();
        if (resources != null) {
            String message = resources.getMessage(enumerate.toString());
            
            if (message != null) {
                description = message;
            }
        }
            
        return description;
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
