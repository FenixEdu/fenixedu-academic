package net.sourceforge.fenixedu.presentationTier.renderers;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.renderers.DateInputRenderer;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixWebFramework.renderers.converters.DateConverter;

public class JodaTimeInputRenderer extends DateInputRenderer {
    
    public class JodaTimeConverter extends DateConverter {

		public JodaTimeConverter(SimpleDateFormat format) {
			super(format);
		}

		@Override
		public Object convert(Class type, Object value) {
			Date date = (Date) super.convert(type, value);
			
            if (date == null) {
                return null;
            }
            
			return YearMonthDay.fromDateFields(date);
		}
    }

	@Override
	protected Converter getDateConverter(SimpleDateFormat format) {
		return new JodaTimeConverter(format);
	}
}
