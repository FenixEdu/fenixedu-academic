package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlFormComponent;
import net.sourceforge.fenixedu.renderers.components.converters.ConversionException;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class FloatInputRenderer extends StringInputRenderer {

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
        
        formComponent.setConverter(new FloatNumberConverter());
        
        return formComponent;
    }
    
    private class FloatNumberConverter extends Converter {

        @Override
        public Object convert(Class type, Object value) {
            String numberText = (String) value;

            try {
                return Float.parseFloat(numberText);
            } catch (NumberFormatException e) {
                throw new ConversionException("could not convert '" + value + "' to a float", e);
            }
        }
        
    }
}
