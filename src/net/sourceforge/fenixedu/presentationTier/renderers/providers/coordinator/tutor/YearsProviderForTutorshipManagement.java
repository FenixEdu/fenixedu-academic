package net.sourceforge.fenixedu.presentationTier.renderers.providers.coordinator.tutor;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Tutorship;

import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class YearsProviderForTutorshipManagement implements DataProvider {

    public Object provide(Object source, Object currentValue) {        
        	
		List<Integer> result = new ArrayList<Integer>();

		YearMonthDay currentDate = new YearMonthDay();
				
		int firstYear = currentDate.getYear() + 1;
		int lastYear = Tutorship.getLastPossibleTutorshipYear();

		while (firstYear <= lastYear) {
			result.add(firstYear);
			firstYear++;
		}

		return result;
    }

    public Converter getConverter() {
        return null;        
    }
}