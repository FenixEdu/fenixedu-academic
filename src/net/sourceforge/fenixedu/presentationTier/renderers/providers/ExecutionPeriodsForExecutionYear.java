package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

import org.apache.commons.beanutils.MethodUtils;

public class ExecutionPeriodsForExecutionYear implements DataProvider {

    public Object provide(Object source, Object currentValue) {
    	
    	ExecutionYear executionYear;
		try {
			executionYear = (ExecutionYear) MethodUtils.invokeMethod(source, "getExecutionYear", null);
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
		
    	List<ExecutionSemester> periods = new ArrayList<ExecutionSemester>();
    	if(executionYear!=null) {
    		periods.addAll(executionYear.getExecutionPeriods());
    	}
       
        return periods;
    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
