package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

/**
 * The default output renderer for a boolean value. The value is 
 * used to search for the corresponding message in the resources.
 * The key <tt>TRUE</tt> and <tt>FALSE</tt> are used to retrieve
 * the messages for the <tt>true</tt> and <tt>false</tt> values.
 * 
 * @author cfgi
 */
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
