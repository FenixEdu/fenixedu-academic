package net.sourceforge.fenixedu.renderers.converters;

import net.sourceforge.fenixedu.renderers.components.converters.ConversionException;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class FloatNumberConverter extends Converter {

    @Override
    public Object convert(Class type, Object value) {
        String numberText = ((String) value).trim();

        if (numberText.length() == 0) {
            return null;
        }

        try {
            return Float.parseFloat(numberText);
        } catch (NumberFormatException e) {
            throw new ConversionException("renderers.converter.float", e, true, value);
        }
    }
    
}
