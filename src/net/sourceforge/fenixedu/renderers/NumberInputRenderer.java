package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlFormComponent;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

/**
 * This renderer provides a simple way of doing the input of numbers. The
 * value is read with an text input field and converted to the appropriate 
 * type.
 *  
 * <p>
 * Example:
 *  <input type="text" value="10"/>
 * 
 * @author cfgi
 */
public abstract class NumberInputRenderer extends StringInputRenderer {
    @Override
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

    @Override
    protected HtmlComponent createTextField(Object object, Class type) {
        HtmlFormComponent formComponent = (HtmlFormComponent) super.createTextField(object, type);
        
        formComponent.setConverter(getConverter());
        
        return formComponent;
    }

    protected abstract Converter getConverter();
}
