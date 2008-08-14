package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class MonthsPartialProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
	List<Partial> result = new ArrayList<Partial>();
	for (int i = 1; i <= 12; i++) {
	    result.add(new Partial(DateTimeFieldType.monthOfYear(), i));
	}
	return result;
    }

    public Converter getConverter() {
	return null;
    }
}
