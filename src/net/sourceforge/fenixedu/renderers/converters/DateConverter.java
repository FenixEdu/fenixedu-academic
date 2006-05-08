/**
 * 
 */
package net.sourceforge.fenixedu.renderers.converters;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import net.sourceforge.fenixedu.renderers.components.converters.ConversionException;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class DateConverter extends Converter {
    public static final String DEFAULT_FORMAT = "dd/MM/yyyy";

    private DateFormat format;
    
    public DateConverter() {
        this.format = new SimpleDateFormat(DEFAULT_FORMAT);
    }
    
    public DateConverter(DateFormat format) {
        this.format = format;
    }

    @Override
    public Object convert(Class type, Object value) {
        if (value == null) {
            return null;
        }
        
        String text = ((String) value).trim();
        
        if (text.length() == 0) {
            return null;
        }
        
        try {
            return format.parse(text);
        } catch (ParseException e) {
            throw new ConversionException("renderers.converter.date", e, true, value);
        }
    }
}