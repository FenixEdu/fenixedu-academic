package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.components.converters.ConversionException;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

/**
 * This renderer provides a simple way of doing the input of a long number.
 * The number is read form a text input field and parsed with
 * {@link Long#parseLong(java.lang.String, int)} were the 
 * second argument is the value given in the {@linkplain IntegerInputRenderer#setBase(int) base}
 * property.
 *  
 * <p>
 * Example:
 *  <input type="text" value="12345"/>
 * 
 * @author cfgi
 */
public class LongInputRenderer extends IntegerInputRenderer {

    @Override
    protected Converter getConverter() {
        return new LongNumberConverter(getBase());
    }

    private class LongNumberConverter extends Converter {

        private int base;
        
        public LongNumberConverter(int base) {
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
                return Long.parseLong(numberText.trim(), getBase());
            } catch (NumberFormatException e) {
                throw new ConversionException("Could not convert input '" + value + "' to a long", e);
            }
        }
        
    }
}
