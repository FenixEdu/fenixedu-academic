package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlTextInput;

/**
 * This renderer provides a standard way of doing the input of a string. The 
 * string is read with a text input field.
 *  
 * <p>
 * Example:
 *  <input type="text" value="the string"/>
 * 
 * @author cfgi
 */
public class StringInputRenderer extends TextFieldRenderer {

    @Override
    protected HtmlComponent createTextField(Object object, Class type) {
        String string = (String) object;
        
        HtmlTextInput input = new HtmlTextInput();
        input.setValue(string);

        return input;
    }
    
}
