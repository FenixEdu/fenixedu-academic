package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;

public class NumberInputRenderer extends StringInputRenderer {

    public HtmlComponent render(Object targetObject, Class type) {
        Number number = (Number) targetObject;
        
        String text;
        if (number == null) {
            text = "";
        }
        else {
            text = number.toString();
        }
        
        return super.render(text, type);
    }
}
