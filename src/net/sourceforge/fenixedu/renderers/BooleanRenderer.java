package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

public class BooleanRenderer extends OutputRenderer {

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                Boolean booleanValue = (Boolean) object;
                
                if (booleanValue == null) {
                    return new HtmlText();
                }

                String booleanResourceKey = booleanValue.toString().toUpperCase();
                return new HtmlText(RenderUtils.getResourceString(booleanResourceKey));
            }
            
        };
    }
    
}
