package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.components.converters.ConversionException;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;


/**
 * {@inheritDoc}
 * 
 * This renderer converts the value to a float with {@link Double#parseDouble(java.lang.String)}.
 * 
 * @author cfgi
 */
public class DoubleInputRenderer extends NumberInputRenderer {

    @Override
    protected Converter getConverter() {
        return new DoubleNumberConverter();
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
