package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlTextInput;

/**
 * This renderer provices the default input for a string. The 
 * string is read with and text input field.
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
