package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.components.converters.ConversionException;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;


/**
 * This renderer provides a simple way of doing the input of a double value. The
 * value is read with an text input field and parsed with 
 * {@link Double#parseDouble(java.lang.String)}.
 *  
 * <p>
 * Example:
 *  <input type="text" value="10.5"/>
 * 
 * @author cfgi
 */
public class DoubleInputRenderer extends FloatInputRenderer {

    @Override
    protected Converter getConverter() {
        return super.getConverter();
    }

    private class DoubleNumberConverter extends Converter {

        @Override
        public Object convert(Class type, Object value) {
            String numberText = (String) value;

            try {
                return Double.parseDouble(numberText);
            } catch (NumberFormatException e) {
                throw new ConversionException("could not convert '" + value + "' to a double", e);
            }
        }
    }
}
