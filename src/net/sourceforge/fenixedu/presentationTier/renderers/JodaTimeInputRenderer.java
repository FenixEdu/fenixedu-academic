package net.sourceforge.fenixedu.presentationTier.renderers;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.sourceforge.fenixedu.renderers.DateInputRenderer;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

import org.joda.time.YearMonthDay;

public class JodaTimeInputRenderer extends DateInputRenderer {
    
    public class JodaTimeConverter extends DateConverter {

		public JodaTimeConverter(SimpleDateFormat format) {
			super(format);
		}

		@Override
		public Object convert(Class type, Object value) {
			Date date = (Date) super.convert(type, value);
			
			return YearMonthDay.fromDateFields(date);
		}
    }

	@Override
	protected Converter getDateConverter(SimpleDateFormat format) {
		return new JodaTimeConverter(format);
	}
}
