package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.Calendar;
import java.util.Date;

import net.sourceforge.fenixedu.renderers.DateRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;

public class PartialRenderer extends DateRenderer {
   
    private Partial partial;

    @Override
    public String getFormat() {
        if (isFormatSet()) {
            return super.getFormat();
        }

        String format = "dd/MM/yyyy HH:mm:ss";

        if (! this.partial.isSupported(DateTimeFieldType.dayOfMonth())) {
            format = format.replace("dd/", "");
        }
        
        if (! this.partial.isSupported(DateTimeFieldType.monthOfYear())) {
            format = format.replace("MM/", "");
        }

        if (! this.partial.isSupported(DateTimeFieldType.year())) {
            if (this.partial.isSupported(DateTimeFieldType.dayOfMonth()) || this.partial.isSupported(DateTimeFieldType.monthOfYear())) {
                format = format.replace("/yyyy", "");
            }
            else {
                format = format.replace("yyyy", "");
            }
        }

        if (! this.partial.isSupported(DateTimeFieldType.hourOfDay())) {
            format = format.replace("HH:", "");
        }

        if (! this.partial.isSupported(DateTimeFieldType.minuteOfHour())) {
            format = format.replace("mm:", "");
        }

        if (! this.partial.isSupported(DateTimeFieldType.secondOfMinute())) {
            if (this.partial.isSupported(DateTimeFieldType.hourOfDay()) || this.partial.isSupported(DateTimeFieldType.minuteOfHour())) {
                format = format.replace(":ss", "");
            }
            else {
                format = format.replace("ss", "");
            }
        }

        return format.trim();
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        this.partial = (Partial) object;
        Date date = this.partial != null ? convertPartialToCalendar(this.partial).getTime() : null;
        
        final Layout superLayout = super.getLayout(date, type);
        
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                HtmlText text = (HtmlText) superLayout.createComponent(object, type);
                
                text.setText(text.getText() + " (" + getFormat() + ")");
                
                return text;
            }
            
        };
    }
    
    private Calendar convertPartialToCalendar(Partial partial) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();

        DateTimeFieldType[] fieldTypes = partial.getFieldTypes();
        for (int i = 0; i < fieldTypes.length; i++) {
            DateTimeFieldType fieldType = fieldTypes[i];

            if (fieldType.equals(DateTimeFieldType.dayOfMonth())) {
                calendar.set(Calendar.DAY_OF_MONTH, partial.get(DateTimeFieldType.dayOfMonth()));
                continue;
            }

            if (fieldType.equals(DateTimeFieldType.monthOfYear())) {
                calendar.set(Calendar.MONTH, partial.get(DateTimeFieldType.monthOfYear()));
                continue;
            }
            
            if (fieldType.equals(DateTimeFieldType.year())) {
                calendar.set(Calendar.YEAR, partial.get(DateTimeFieldType.year()));
                continue;
            }
            
            if (fieldType.equals(DateTimeFieldType.hourOfDay())) {
                calendar.set(Calendar.HOUR_OF_DAY, partial.get(DateTimeFieldType.hourOfDay()));
                continue;
            }
            
            if (fieldType.equals(DateTimeFieldType.minuteOfHour())) {
                calendar.set(Calendar.MINUTE, partial.get(DateTimeFieldType.minuteOfHour()));
                continue;
            }
            
            if (fieldType.equals(DateTimeFieldType.secondOfMinute())) {
                calendar.set(Calendar.SECOND, partial.get(DateTimeFieldType.secondOfMinute()));
                continue;
            }
        }
        
        return calendar;
    }
}
