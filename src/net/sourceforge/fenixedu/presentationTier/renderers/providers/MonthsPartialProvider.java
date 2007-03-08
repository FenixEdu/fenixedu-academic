package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;

public class MonthsPartialProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {                	
	List<Partial> result = new ArrayList<Partial>();
	for (int i = 1 ; i <= 12; i++) {
	    result.add(new Partial(DateTimeFieldType.monthOfYear(), i));
	} 	
	return result;
    }

    public Converter getConverter() {
        return null;        
    }
}
