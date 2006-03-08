package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlFormComponent;
import net.sourceforge.fenixedu.renderers.components.converters.ConversionException;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

/**
 * This renderer provides the default input for a integer number.
 * The number is read form a text input field and parsed with
 * {@link Integer#parseInt(java.lang.String, int)} were the 
 * second argument is the value given in the {@linkplain #setBase(int) base}
 * property.
 *  
 * <p>
 * Example:
 *  <input type="text" value="12345"/>
 * 
 * @author cfgi
 */
public class IntegerInputRenderer extends StringInputRenderer {

    private int base = 10;
    
    /**
     * The base in which the number should be interpreted. For instance,
     * if <tt>base</tt> is 16 then an input like <tt>CAFE</tt> will be 
     * interpreted as 51966.
     * 
     * @property
     */
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
    
    private class NumberConverter extends Converter {

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
                return Integer.parseInt(numberText.trim(), getBase());
            } catch (NumberFormatException e) {
                throw new ConversionException("Could not convert input '" + value + "' to a integer", e);
            }
        }
        
    }
}
