package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;

/**
 * This renderer provides a generic presentation for a number. The number
 * is simply converted to a string and presented.
 * 
 * @author cfgi
 */
public class NumberRenderer extends OutputRenderer {

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                Number number = (Number) object;
                
                return new HtmlText(String.valueOf(number));
            }
            
        };
    }
}
