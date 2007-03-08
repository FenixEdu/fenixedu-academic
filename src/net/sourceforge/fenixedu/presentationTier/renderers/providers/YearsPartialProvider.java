package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;

public class YearsPartialProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {        
        	
	List<Partial> result = new ArrayList<Partial>();
	ExecutionYear firstExecutionYear = null;
	
	for (ExecutionYear executionYear : RootDomainObject.getInstance().getExecutionYears()) {
	    if(executionYear.getPreviousExecutionYear() == null) {
		firstExecutionYear = executionYear;
	    }
	}
			
	if(firstExecutionYear != null) {
	    ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();	    
	    int firstYear = firstExecutionYear.getBeginDateYearMonthDay().getYear();
	    int lastYear = currentExecutionYear.getEndDateYearMonthDay().getYear();	    	    
	    while(firstYear <= lastYear) {
		result.add(new Partial(DateTimeFieldType.year(), firstYear));
		firstYear++;
	    }
	}
	
	return result;
    }

    public Converter getConverter() {
        return null;        
    }
}