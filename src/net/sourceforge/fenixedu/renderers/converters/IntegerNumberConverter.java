package net.sourceforge.fenixedu.renderers.converters;

import net.sourceforge.fenixedu.renderers.components.converters.ConversionException;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class IntegerNumberConverter extends Converter {

    private int base;
    
    public IntegerNumberConverter() {
        setBase(10);
    }
    
    public IntegerNumberConverter(int base) {
        this();
        
        setBase(base);
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
            return Integer.parseInt(numberText.trim(), getBase());
        } catch (NumberFormatException e) {
            throw new ConversionException("renderers.converter.integer", e, true, value);
        }
    }
    
}
