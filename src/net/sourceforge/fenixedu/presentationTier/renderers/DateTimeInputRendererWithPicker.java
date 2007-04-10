package net.sourceforge.fenixedu.presentationTier.renderers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.joda.time.DateTime;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlImage;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.converters.DateConverter;
import net.sourceforge.fenixedu.renderers.model.MetaSlotKey;

/**
 * This renderer provides a more user friendly way of inserting a DateTime.
 * Besides the normal input field the suer can click a calendar button and
 * choose the date and time from that calendar.
 * 
 * <p>
 * Example: <input type="text" value="01/02/3456"/> dd/MM/yyyy HH:mm <input
 * type="button" value="Cal"/>
 * 
 * @author José Pedro Pereira - Linkare TI
 */
public class DateTimeInputRendererWithPicker extends DateInputRendererWithPicker {

    public DateTimeInputRendererWithPicker() {
        super();
        
        setFormat(DateConverter.DEFAULT_FORMAT + " HH:mm");
    }
    
    @Override
    public HtmlComponent render(Object object, Class type) {
        Date date = object == null ? null : ((DateTime) object).toDate(); 
        return super.render(date, Date.class);
    }
    
    @Override
    protected Converter getDateConverter(SimpleDateFormat dateFormat) {
        final Converter dateConverter = super.getDateConverter(dateFormat);

        return new Converter() {
            private static final long serialVersionUID = 1L;

            @Override
            public Object convert(Class type, Object value) {
                Date date = (Date) dateConverter.convert(type, value);
                
                return new DateTime(date);
            } 
            
        };
    }
    
    @Override
    protected String getScriptText(HtmlImage image, MetaSlotKey key) {
        return String.format(
                "Calendar.setup({inputField: '%s', ifFormat: '%s', button: '%s', showsTime: true, timeFormat: 24});",
                key.toString(),
                getInputFormatForCalendar(),
                image.getId()
        );
    }
    
    protected String getInputFormatForCalendar() {
        Locale locale = getLocale();
        SimpleDateFormat format = new SimpleDateFormat(getFormat(), locale);
        
		Calendar c = Calendar.getInstance();
        
		c.set(Calendar.YEAR, 1999);
        c.set(Calendar.MONTH, 11);
        c.set(Calendar.DAY_OF_MONTH, 24);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 55);
		
		String dateStringFormatted = format.format(c.getTime());
        dateStringFormatted = dateStringFormatted.replace("1999", "%Y");
        dateStringFormatted = dateStringFormatted.replace("99", "%y");
        dateStringFormatted = dateStringFormatted.replace("12", "%m");
        dateStringFormatted = dateStringFormatted.replace("24", "%e");
        dateStringFormatted = dateStringFormatted.replace("23", "%H");
        dateStringFormatted = dateStringFormatted.replace("55", "%M");

        return dateStringFormatted;
    }
    
    
}
 