package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ReverseComparator;

public class ExecutionYearsProviderForThesisContextBean implements DataProvider {

    public Object provide(Object source, Object currentValue) {
        final List<ExecutionYear> executionYears = new ArrayList<ExecutionYear>(RootDomainObject.getInstance().getExecutionYears());
        
        List<ExecutionYear> notClosedExecutionYears = (List<ExecutionYear>) CollectionUtils.select(executionYears, new Predicate() {
    	    public boolean evaluate(Object arg0) {
    	    	ExecutionYear executionYear = (ExecutionYear) arg0;
    	    	return !executionYear.getState().equals(PeriodState.CLOSED);
    	    }
    	});

    	Collections.sort(notClosedExecutionYears, new ReverseComparator());   

        return notClosedExecutionYears; 
    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
