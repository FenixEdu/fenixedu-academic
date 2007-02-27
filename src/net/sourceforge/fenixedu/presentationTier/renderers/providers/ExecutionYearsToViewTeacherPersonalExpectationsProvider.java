package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class ExecutionYearsToViewTeacherPersonalExpectationsProvider implements DataProvider{
        
    public Object provide(Object source, Object currentValue) {	
	List<ExecutionYear> result = new ArrayList<ExecutionYear>();	
	ExecutionYear year = ExecutionYear.readExecutionYearByName("2005/2006");
	result.add(year);
	while(year.getNextExecutionYear() != null) {
	    result.add(year.getNextExecutionYear());
	    year = year.getNextExecutionYear();
	}
	return result;
    }
    
    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }
}
