package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.components.converters.ConversionException;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

/**
 * This renderer provides a simple way of doing the input of a short number.
 * The number is read form a text input field and parsed with
 * {@link Short#parseShort(java.lang.String, int)} were the 
 * second argument is the value given in the {@linkplain IntegerInputRenderer#setBase(int) base}
 * property.
 *  
 * <p>
 * Example:
 *  <input type="text" value="12345"/>
 * 
 * @author cfgi
 */
public class ShortInputRenderer extends IntegerInputRenderer {
    @Override
    protected Converter getConverter() {
        return new ShortNumberConverter(getBase());
    }

    private class ShortNumberConverter extends Converter {

        private int base;
        
        public ShortNumberConverter(int base) {
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
            String numberText = ((String) value).trim();

            if (numberText.length() == 0) {
                return null;
            }
            
            try {
                return Short.parseShort(numberText.trim(), getBase());
            } catch (NumberFormatException e) {
                throw new ConversionException("renderers.converter.short", e, true, value);
            }
        }
        
    }
}
