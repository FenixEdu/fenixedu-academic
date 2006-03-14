package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.components.converters.ConversionException;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

/**
 * {@inheritDoc}
 * 
 * This renderer converts the value to a float with {@link Float#parseFloat(java.lang.String)}.
 * 
 * @author cfgi
 */
public class FloatInputRenderer extends NumberInputRenderer {

    @Override
    protected Converter getConverter() {
        return new FloatNumberConverter();
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
