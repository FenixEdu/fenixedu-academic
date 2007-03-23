package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

import org.apache.commons.beanutils.MethodUtils;

public class ExecutionPeriodsForExecutionYear implements DataProvider {

    public Object provide(Object source, Object currentValue) {
    	
    	ExecutionYear executionYear;
		try {
			executionYear = (ExecutionYear) MethodUtils.invokeMethod(source, "getExecutionYear", null);
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
		
    	List<ExecutionPeriod> periods = new ArrayList<ExecutionPeriod>();
    	if(executionYear!=null) {
    		periods.addAll(executionYear.getExecutionPeriods());
    	}
       
        return periods;
    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
