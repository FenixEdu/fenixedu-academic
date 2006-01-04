package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlFormComponent;
import net.sourceforge.fenixedu.renderers.components.converters.ConversionException;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class NumberInputRenderer extends StringInputRenderer {

    private int base = 10;
    
    public void setBase(int base) {
        this.base = base;
    }
    
    public int getBase() {
        return this.base;
    }
    
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
        
        formComponent.setConverter(new NumberConverter(getBase()));
        
        return formComponent;
    }
}

class NumberConverter extends Converter {

    private int base;
    
    public NumberConverter(int base) {
        this.base = base;
    }

    public int getBase() {
        return this.base;
    }

    public void setBase(int base) {
        this.base = base;
    }

    @Override
    public Object convert(Class type, Object value) {
        String numberText = (String) value;
        
        try {
            return Integer.parseInt(numberText, getBase());
        } catch (NumberFormatException e) {
            throw new ConversionException("Could not convert input " + value, e);
        }
    }
    
}