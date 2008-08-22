package net.sourceforge.fenixedu.presentationTier.renderers;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.joda.time.base.AbstractPartial;

import pt.ist.fenixWebFramework.renderers.DateInputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.converters.ConversionException;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.DateConverter;
import pt.ist.fenixWebFramework.renderers.converters.MultipleFormatDateConverter;

public class MultipleFormatTimeInputRenderer extends DateInputRenderer {

    private boolean minute;

    private boolean hour;

    private String inputFormat;

    public MultipleFormatTimeInputRenderer() {
	super();

	minute = hour = false;
    }

    public boolean isHour() {
	return hour;
    }

    public void setHour(boolean hour) {
	this.hour = hour;
    }

    public boolean isMinute() {
	return minute;
    }

    public void setMinute(boolean minute) {
	this.minute = minute;
    }

    @Override
    public String getFormat() {
	// "HH:mm";
	if (isFormatSet()) {
	    return super.getFormat();
	}
	StringBuilder format = new StringBuilder();
	if (isHour()) {
	    format.append((format.length() > 0 ? " " : "") + "HH");
	}
	if (isMinute()) {
	    format.append((format.length() > 0 ? ":" : "") + "mm");
	}
	return format.toString();
    }

    public String getInputFormat() {
	return this.inputFormat == null ? DateConverter.DEFAULT_FORMAT : inputFormat;
    }

    public void setInputFormat(String inputFormat) {
	this.inputFormat = inputFormat;
    }

    @Override
    protected HtmlComponent createTextField(Object object, Class type) {
	Date date = convertPartialToDate((AbstractPartial) object);
	return super.createTextField(date, type);
    }

    private Date convertPartialToDate(AbstractPartial partial) {
	if (partial == null) {
	    return null;
	}
	Calendar calendar = Calendar.getInstance();
	calendar.clear();
	if (isHour()) {
	    calendar.set(Calendar.HOUR_OF_DAY, partial.get(DateTimeFieldType.hourOfDay()));
	}
	if (isMinute()) {
	    calendar.set(Calendar.MINUTE, partial.get(DateTimeFieldType.minuteOfHour()));
	}
	return calendar.getTime();
    }

    @Override
    protected Converter getDateConverter(SimpleDateFormat dateFormat) {
	if (getInputFormat() != null) {
	    dateFormat.applyPattern(getInputFormat());
	}
	return new PartialConverter(new MultipleFormatDateConverter(dateFormat));
    }

    public class PartialConverter extends Converter {
	private Converter dateConverter;

	public PartialConverter(Converter dateConverter) {
	    this.dateConverter = dateConverter;
	}

	@Override
	public Object convert(Class type, Object value) {
	    Date date = (Date) this.dateConverter.convert(type, value);
	    if (date == null) {
		return null;
	    }
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    try {
		return convertCalendarToPartial(type, calendar);
	    } catch (Exception e) {
		throw new ConversionException("fenix.renderers.converter.timeOfDay", e, true, value);
	    }
	}

	private Object convertCalendarToPartial(Class type, Calendar calendar) throws Exception {
	    if (type.equals(Partial.class)) {
		Partial partial = new Partial();
		if (isHour()) {
		    partial = partial.with(DateTimeFieldType.hourOfDay(), calendar.get(Calendar.HOUR_OF_DAY));
		}
		if (isMinute()) {
		    partial = partial.with(DateTimeFieldType.minuteOfHour(), calendar.get(Calendar.MINUTE));
		}
		return partial;
	    } else {
		// ASSUMPTION
		// assume that we want a subtype of BasePartial and that the
		// subtype implements the factory
		// method fromCalendarField(Calendar calendar)
		Method method = type.getMethod("fromCalendarFields", new Class[] { Calendar.class });

		return method.invoke(null, new Object[] { calendar });
	    }
	}

    }

}
