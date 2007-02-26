/**
 * 
 */
package net.sourceforge.fenixedu.renderers.converters;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import net.sourceforge.fenixedu.renderers.components.converters.ConversionException;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class MultipleFormatDateConverter extends Converter {
    public static final String DEFAULT_FORMAT = "H:m";

    private DateFormat format;

    public MultipleFormatDateConverter() {
	this.format = new SimpleDateFormat(DEFAULT_FORMAT);
    }

    public MultipleFormatDateConverter(DateFormat format) {
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
	text = text.replaceAll("\\p{Punct}", ":");

	try {
	    return format.parse(text);
	} catch (ParseException e) {
	    try {
		format = new SimpleDateFormat("H");
		return format.parse(text);
	    } catch (ParseException e2) {
		throw new ConversionException("renderers.converter.time", e, true, value);
	    }
	}
    }
}