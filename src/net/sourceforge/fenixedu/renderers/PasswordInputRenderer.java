package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlPasswordInput;

/**
 * This renderer provides a standard way of doing the input of a password. The
 * password is read with a password input field.
 * 
 * <p>
 * Example: <input type="password" value="the password"/>
 * 
 * @author naat
 */
public class PasswordInputRenderer extends TextFieldRenderer {

    @Override
    protected HtmlComponent createTextField(Object object, Class type) {
        String string = (String) object;

        HtmlPasswordInput inputPassword = new HtmlPasswordInput();
        inputPassword.setValue(string);

        return inputPassword;
    }

}
