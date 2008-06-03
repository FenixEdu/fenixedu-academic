package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.domain.material.Extension;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.ExtensionNumberConverter;
import pt.ist.fenixWebFramework.renderers.StringInputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlFormComponent;

public class ExtensionNumberStringInputRenderer extends StringInputRenderer {

    @Override
    protected HtmlComponent createTextField(Object object, Class type) {
        
        Extension extension = (Extension) object; 
        String number = (extension != null) ? extension.getIdentification().toString() : null; 
        
        HtmlFormComponent formComponent = (HtmlFormComponent) super.createTextField(number, type);        
        formComponent.setConverter(new ExtensionNumberConverter());
        
        return formComponent;        
    }
}
