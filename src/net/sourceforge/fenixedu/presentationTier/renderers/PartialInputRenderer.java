package net.sourceforge.fenixedu.presentationTier.renderers;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import pt.ist.fenixWebFramework.renderers.DateInputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.converters.ConversionException;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.joda.time.base.AbstractPartial;

public class PartialInputRenderer extends DateInputRenderer {

    private boolean second;
    private boolean minute;
    private boolean hour;
    private boolean day;
    private boolean month;
    private boolean year;
    
    public PartialInputRenderer() {
        super();
        
        second = minute = hour = day = month = year = false;
    }

    public boolean isDay() {
        return day;
    }

    public void setDay(boolean day) {
        this.day = day;
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

    public boolean isMonth() {
        return month;
    }

    public void setMonth(boolean month) {
        this.month = month;
    }

    public boolean isSecond() {
        return second;
    }

    public void setSecond(boolean second) {
        this.second = second;
    }

    public boolean isYear() {
        return year;
    }

    public void setYear(boolean year) {
        this.year = year;
    }

    @Override
    public String getFormat() {
        StringBuffer format = new StringBuffer();
        //"HH:mm:ss dd/MM/yyyy";
        
        if (isFormatSet()) {
            return super.getFormat();
        }

        if (isDay()) {
            format.append("dd");
        }
        
        if (isMonth()) {
            format.append((format.length() > 0 ? "/" : "") + "MM");
        }
        
        if (isYear()) {
            format.append((format.length() > 0 ? "/" : "") + "yyyy");
        }

        if (isHour()) {
            format.append((format.length() > 0 ? " " : "") + "HH");
        }
        
        if (isMinute()) {
            format.append((format.length() > 0 ? ":" : "") + "mm");
        }
        
        if (isSecond()) {
            format.append((format.length() > 0 ? ":" : "") + "ss");
        }
        
        return format.toString();
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

        if (isDay()) {
            calendar.set(Calendar.DAY_OF_MONTH, partial.get(DateTimeFieldType.dayOfMonth()));
        }
        
        if (isMonth()) {
            calendar.set(Calendar.MONTH, partial.get(DateTimeFieldType.monthOfYear()) - 1);
        }
        
        if (isYear()) {
            calendar.set(Calendar.YEAR, partial.get(DateTimeFieldType.year()));
        }

        if (isHour()) {
            calendar.set(Calendar.HOUR_OF_DAY, partial.get(DateTimeFieldType.hourOfDay()));
        }

        if (isMinute()) {
            calendar.set(Calendar.MINUTE, partial.get(DateTimeFieldType.minuteOfHour()));
        }

        if (isSecond()) {
            calendar.set(Calendar.SECOND, partial.get(DateTimeFieldType.secondOfMinute()));
        }
        
        return calendar.getTime();
    }

    @Override
    protected Converter getDateConverter(SimpleDateFormat dateFormat) {
        Converter dateConverter = super.getDateConverter(dateFormat);
        
        return new PartialConverter(dateConverter);
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
                throw new ConversionException("fenix.renderers.converter.partial", e, true, value);
            }
        }

        private Object convertCalendarToPartial(Class type, Calendar calendar) throws Exception {
            if (type.equals(Partial.class)) {
                Partial partial = new Partial();
                
                if (isDay()) {
                    partial = partial.with(DateTimeFieldType.dayOfMonth(), calendar.get(Calendar.DAY_OF_MONTH));
                }
                
                if (isMonth()) {
                    partial = partial.with(DateTimeFieldType.monthOfYear(), calendar.get(Calendar.MONTH) + 1);
                }
                
                if (isYear()) {
                    partial = partial.with(DateTimeFieldType.year(), calendar.get(Calendar.YEAR));
                }
    
                if (isHour()) {
                    partial = partial.with(DateTimeFieldType.hourOfDay(), calendar.get(Calendar.HOUR_OF_DAY));
                }
    
                if (isMinute()) {
                    partial = partial.with(DateTimeFieldType.minuteOfHour(), calendar.get(Calendar.MINUTE));
                }
    
                if (isSecond()) {
                    partial = partial.with(DateTimeFieldType.secondOfMinute(), calendar.get(Calendar.SECOND));
                }
                
                return partial;
            }
            else {
                // ASSUMPTION
                // assume that we want a subtype of BasePartial and that the subtype implements the factory
                // method fromCalendarField(Calendar calendar)
                Method method = type.getMethod("fromCalendarFields", new Class[] { Calendar.class });
                
                return method.invoke(null, new Object[] { calendar });
            }
        }
        
    }
    
}
